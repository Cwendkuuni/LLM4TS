import static org.junit.Assert.*;
import static com.google.javascript.rhino.NodeFactory.newASTNode;
import static com.google.javascript.rhino.Token.*;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Sets;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CompilerInput;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.NodeUtil;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.rhino.FunctionType;
import com.google.javascript.rhino.JSType;
import com.google.javascript.rhino.ObjectType;
import com.google.javascript.rhino.TokenFactory;
import com.google.javascript.rhino.UnionType;

import java.util.Arrays;
import java.util.Comparator;
import java.util.TreeSet;

public class RuntimeTypeCheckTest {

  private static final Comparator<JSType> ALPHA = new Comparator<JSType>() {
    public int compare(JSType t1, JSType t2) {
      return getName(t1).compareTo(getName(t2));
    }

    private String getName(JSType type) {
      if (type.isInstanceType()) {
        return ((ObjectType) type).getReferenceName();
      } else if (type.isNullType()
          || type.isBooleanValueType()
          || type.isNumberValueType()
          || type.isStringValueType()
          || type.isVoidType()) {
        return type.toString();
      } else {
        // Type unchecked at runtime, so we don't care about the sorting order.
        return "";
      }
    }
  };

  private static AbstractCompiler createCompiler() {
    CompilerOptions options = new CompilerOptions(new TokenFactory());
    AbstractCompiler compiler = new Compiler(options);
    return compiler;
  }

  @org.junit.Test
  public void testRuntimeTypeCheck() throws Exception {
    AbstractCompiler compiler = createCompiler();
    String logFunction = "function(warning, expr) { console.log(warning, expr); }";

    RuntimeTypeCheck runtimeTypeCheck = new RuntimeTypeCheck(compiler, logFunction);
    CompilerPass[] passes = new CompilerPass[]{runtimeTypeCheck};

    String sourceCode =
        "class Foo {" +
            "constructor(x: number, y: number) {" +
            "}" +
            "}" +
            "function bar(x: Foo, y: number): void {" +
            "}" +
            "function baz(): void {" +
            "function qux(): Foo {" +
            "}";

    CompilerInput input = new CompilerInput("test", sourceCode, null, null);
    compiler.compile(input, passes);

    FunctionType fooType = new FunctionType(new ObjectType("Foo"), null, true);

    Node root = compiler.getResultNode();
    Node nodeToInsertAfter = findNodeToInsertAfter(root, 13);
    Node classNode = NodeUtil.newQualifiedNameNode("Foo", -1, -1);

    TreeSet<ObjectType> stuff = Sets.newTreeSet(ALPHA);
    stuff.add(fooType);
    for (ObjectType interfaceType : fooType.getAllImplementedInterfaces()) {
      stuff.add(interfaceType);
    }

    Node marker = Node.newString("instance_of__Foo");
    Node assign = new Node(EXPR_RESULT, new Node(ASSIGN,
        new Node(GET_ELEM,
            new Node(GET_PROP, classNode, new String("prototype")), marker),
        new Node(TRUE)));

    nodeToInsertAfter.getParent().addChildAfter(assign, nodeToInsertAfter);

    for (ObjectType interfaceType : fooType.getAllImplementedInterfaces()) {
      marker = Node.newString("implements__" + interfaceType.getReferenceName());
      assign = new Node(EXPR_RESULT, new Node(ASSIGN,
          new Node(GET_ELEM, classNode, marker),
          new Node(TRUE)));

      nodeToInsertAfter.getParent().addChildAfter(assign, nodeToInsertAfter);
      nodeToInsertAfter = assign;
    }

    assertEquals(getExpectedCode(sourceCode), compiler.getSourceOutput("test.js"));

    Node functionNode = findNodeByName(root, "bar");
    assertEquals(1, functionNode.getNumChildren());

    Node paramNode = functionNode.getFirstChild().getFirstChild();
    assertEquals("x", paramNode.getString());

    Node checkNode = findCheckNode(functionNode, 0, 0);
    assertEquals("classChecker", checkNode.getFirstChild().getString());
    assertEquals("Foo", checkNode.getLastChild().getString());

    Node returnNode = findNodeByName(root, "qux");
    assertEquals(1, returnNode.getNumChildren());

    checkNode = findCheckNode(returnNode, 0, 0);
    assertEquals("classChecker", checkNode.getFirstChild().getString());
    assertEquals("Foo", checkNode.getLastChild().getString());
  }

  private Node findNodeByName(Node node, String name, int depth) {
    if (node.getType() == Token.NAME) {
      if (node.getString().equals(name)) {
        return node;
      }
    }

    for (int i = 0; i < node.getNumChildren(); i++) {
      Node child = node.getChild(i);
      Node result = findNodeByName(child, name, depth + 1);
      if (result != null) {
        return result;
      }
    }

    return null;
  }

  private Node findNodeToInsertAfter(Node node, int index) {
    while (index > 0 && node != null) {
      node = node.getParent();
      index--;
    }

    return node;
  }

  private Node findCheckNode(Node node, int index, int depth) {
    if (node.getType() != Token.CALL || node.getFirstChild().getType() != IDENT) {
      return null;
    }

    String checkTypeFunctionName = "checkType";
    if (!checkTypeFunctionName.equals(node.getFirstChild().getString())) {
      return null;
    }

    if (depth > 0 && node.getLastChild().getType() != TOKEN_OPEN_BRACKET) {
      return null;
    }

    if (index == 0) {
      return node;
    }

    return findCheckNode(node.getChild(index), index - 1, depth + 1);
  }

  private String getExpectedCode(String sourceCode) {
    String boilerplateCode =
        "function checkType(expr, checkers) {" +
            "try {" +
                "return expr;" +
            "} catch (e) {" +
                "for (var i = 0, l = checkers.length; i < l; ++i) {" +
                    "if (checkers[i].check(expr)) {" +
                        "return expr;" +
                    "}" +
                "}" +
                "throw e;" +
            "}" +
        "}" +

        "function valueChecker(type) {" +
            "return {" +
                "check: function(expr) {" +
                    "return typeof expr === '"+type+"' &&" +
                        "typeof expr.constructor.constructor === 'object' &&" +
                        "expr.constructor === (function () {" +
                            "return new " + type + "()" +
                        "}())";

    return boilerplateCode + System.lineSeparator() + sourceCode;
  }
}
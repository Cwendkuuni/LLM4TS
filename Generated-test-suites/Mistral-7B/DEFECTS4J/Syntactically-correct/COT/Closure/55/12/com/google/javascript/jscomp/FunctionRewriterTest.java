import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

import com.google.javascript.jscomp.FunctionRewriter;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.Compiler;
import com.google.javascript.rhino.NodeFactory;
import com.google.javascript.rhino.Token;

import org.junit.Before;
import org.junit.Test;

public class FunctionRewriterTest {

  private NodeFactory nodeFactory;
  private Compiler compiler;
  private FunctionRewriter functionRewriter;

  @Before
  public void setUp() {
    nodeFactory = new NodeFactory();
    compiler = new Compiler();
    functionRewriter = new FunctionRewriter(compiler);
  }

  @Test
  public void testEmptyFunctionReducer_emptyFunction_returnsEmptyFunctionHelper() {
    Node root = nodeFactory.compile("function foo() {}");
    FunctionRewriter functionRewriter = new FunctionRewriter(compiler);
    Node parsedHelperCode = functionRewriter.parseHelperCode(EmptyFunctionReducer.class);

    assertSame(parsedHelperCode, functionRewriter.parseHelperCode(EmptyFunctionReducer.class));
  }

  @Test
  public void testEmptyFunctionReducer_emptyFunction_helperIsApplied() {
    Node root = nodeFactory.compile("function foo() {}");
    FunctionRewriter functionRewriter = new FunctionRewriter(compiler);
    Node parsedHelperCode = functionRewriter.parseHelperCode(EmptyFunctionReducer.class);
    Node helperCode = nodeFactory.parseSyntheticCode(
        EmptyFunctionReducer.class.toString() + ":helper", parsedHelperCode.getCode());

    functionRewriter.process(null, root);
    Node functionNode = findFunctionNode("foo", root);

    Node expectedHelperCode = nodeFactory.parseSyntheticCode(
        EmptyFunctionReducer.class.toString() + ":helper", parsedHelperCode.getCode());
    Node actualHelperCode = findNodeByType(Token.CALL, root, "JSCompiler_emptyFn");

    assertSame(expectedHelperCode, actualHelperCode);
  }

  @Test
  public void testIdentityReducer_identityFunction_returnsIdentityFunctionHelper() {
    String expectedHelperSource = "function JSCompiler_identityFn() {" +
            "  return function(JSCompiler_identityFn_value) {" +
            "    return JSCompiler_identityFn_value" +
            "  }";
    Node root = nodeFactory.compile("function foo(x) { return x }");
    FunctionRewriter functionRewriter = new FunctionRewriter(compiler);

    Node parsedHelperCode = functionRewriter.parseHelperCode(IdentityReducer.class);

    assertSame(nodeFactory.parseSyntheticCode(
            IdentityReducer.class.toString() + ":helper", expectedHelperSource).getCode(),
            parsedHelperCode.getCode());
  }

  @Test
  public void testIdentityReducer_identityFunction_helperIsApplied() {
    String expectedHelperSource = "function JSCompiler_identityFn() {" +
            "  return function(JSCompiler_identityFn_value) {" +
            "    return JSCompiler_identityFn_value" +
            "  }";
    Node root = nodeFactory.compile("function foo(x) { return x }");
    FunctionRewriter functionRewriter = new FunctionRewriter(compiler);

    Node parsedHelperCode = functionRewriter.parseHelperCode(IdentityReducer.class);
    Node helperCode = nodeFactory.parseSyntheticCode(
        IdentityReducer.class.toString() + ":helper", parsedHelperCode.getCode());

    functionRewriter.process(null, root);
    Node functionNode = findFunctionNode("foo", root);

    Node expectedHelperCodeRoot = nodeFactory.parseSyntheticCode(
        IdentityReducer.class.toString() + ":helper", expectedHelperSource).removeFirstChild();
    Node actualHelperCode = findNodeByType(Token.CALL, root, "JSCompiler_identityFn");

    assertSame(expectedHelperCodeRoot, actualHelperCode);
  }

  @Test
  public void testIdentityReducer_nonIdentityFunction_notApplied() {
    Node root = nodeFactory.compile("function foo(x) { return x * 2 }");
    FunctionRewriter functionRewriter = new FunctionRewriter(compiler);

    functionRewriter.process(null, root);

    Node functionNode = findFunctionNode("foo", root);
    assertNull(findNodeByType(Token.CALL, root, "JSCompiler_identityFn"));
  }

  @Test(expected = IllegalStateException.class)
  public void testGetterReducer_nonGetterFunction_throwsException() {
    Node root = nodeFactory.compile("function foo(x) { return x + 1 }");
    FunctionRewriter functionRewriter = new FunctionRewriter(compiler);

    functionRewriter.process(null, root);

    throw new IllegalStateException();
  }

  @Test
  public void testGetterReducer_getterFunction_returnsGetterFunctionHelper() {
    String expectedHelperSource = "function JSCompiler_get(name) {" +
            "  return function() {return this[name]}" +
            "}";
    Node root = nodeFactory.compile("function foo() { return this.a }");
    FunctionRewriter functionRewriter = new FunctionRewriter(compiler);

    Node parsedHelperCode = functionRewriter.parseHelperCode(GetterReducer.class);

    assertSame(nodeFactory.parseSyntheticCode(
            GetterReducer.class.toString() + ":helper", expectedHelperSource).getCode(),
            parsedHelperCode.getCode());
  }

  @Test
  public void testGetterReducer_getterFunction_helperIsApplied() {
    String expectedHelperSource = "function JSCompiler_get(name) {" +
            "  return function() {return this[name]}" +
            "}";
    Node root = nodeFactory.compile("function foo() { return this.a }");
    FunctionRewriter functionRewriter = new FunctionRewriter(compiler);

    Node parsedHelperCode = functionRewriter.parseHelperCode(GetterReducer.class);
    Node helperCode = nodeFactory.parseSyntheticCode(
        GetterReducer.class.toString() + ":helper", parsedHelperCode.getCode());

    functionRewriter.process(null, root);
    Node functionNode = findFunctionNode("foo", root);

    Node expectedHelperCodeRoot = nodeFactory.parseSyntheticCode(
        GetterReducer.class.toString() + ":helper", expectedHelperSource).removeFirstChild();
    Node actualHelperCode = findNodeByType(Token.CALL, root, "JSCompiler_get");

    assertSame(expectedHelperCodeRoot, actualHelperCode);
  }

  @Test
  public void testSetterReducer_setterFunction_returnsSetterFunctionHelper() {
    String expectedHelperSource = "function JSCompiler_set(name) {" +
            "  return function(value) {" +
            "this[" + name + "] = value" +
            "  }" +
            "}";
    Node root = nodeFactory.compile("function foo(value) { this.a = value }");
    FunctionRewriter functionRewriter = new FunctionRewriter(compiler);

    Node parsedHelperCode = functionRewriter.parseHelperCode(SetterReducer.class);

    assertSame(nodeFactory.parseSyntheticCode(
            SetterReducer.class.toString() + ":helper", expectedHelperSource).getCode(),
            parsedHelperCode.getCode());
  }

  @Test
  public void testSetterReducer_setterFunction_helperIsApplied() {
    String expectedHelperSource = "function JSCompiler_set(name) {" +
            "  return function(value) {" +
            "this[" + name + "] = value" +
            "  }" +
            "}";
    Node root = nodeFactory.compile("function foo(value) { this.a = value }");
    FunctionRewriter functionRewriter = new FunctionRewriter(compiler);

    Node parsedHelperCode = functionRewriter.parseHelperCode(SetterReducer.class);
    Node helperCode = nodeFactory.parseSyntheticCode(
        SetterReducer.class.toString() + ":helper", parsedHelperCode.getCode());

    functionRewriter.process(null, root);
    Node functionNode = findFunctionNode("foo", root);

    Node expectedHelperCodeRoot = nodeFactory.parseSyntheticCode(
        SetterReducer.class.toString() + ":helper", expectedHelperSource).removeFirstChild();
    Node actualHelperCode = findNodeByType(Token.CALL, root, "JSCompiler_set");

    assertSame(expectedHelperCodeRoot, actualHelperCode);
  }

  @Test
  public void testSetterReducer_nonSetterFunction_notApplied() {
    Node root = nodeFactory.compile("function foo(value) { return value * 2 }");
    FunctionRewriter functionRewriter = new FunctionRewriter(compiler);

    functionRewriter.process(null, root);

    Node functionNode = findFunctionNode("foo", root);
    assertNull(findNodeByType(Token.CALL, root, "JSCompiler_set"));
  }

  private Node findFunctionNode(String name, Node root) {
    NodeTraversal nodeTraversal = new NodeTraversal(compiler) {
      @Override
      public boolean shouldTraverse(NodeTraversal traverser, Node node, Node parent) {
        if (isFunctionDeclaration(node) && node.getString().equals(name)) {
          return false;
        }
        return super.shouldTraverse(traverser, node, parent);
      }
    };
    nodeTraversal.traverse(root);
    return nodeTraversal.getCurrentNode();
  }

  private Node findNodeByType(int nodeType, Node root, String nodeName) {
    NodeTraversal nodeTraversal = new NodeTraversal(compiler) {
      @Override
      public boolean shouldTraverse(NodeTraversal traverser, Node node, Node parent) {
        if (node.getType() == nodeType && node.getString().equals(nodeName)) {
          return false;
        }
        return super.shouldTraverse(traverser, node, parent);
      }
    };
    nodeTraversal.traverse(root);
    return nodeTraversal.getCurrentNode();
  }

  private boolean isFunctionDeclaration(Node node) {
    return node.getType() == Token.FUNCTION && node.getLastChild().getType() != Token.BLOCK;
  }
}


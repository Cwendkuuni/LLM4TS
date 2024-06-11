import com.google.common.collect.ImmutableList;
import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.CompilerInput;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.RuntimeTypeCheck;
import com.google.javascript.rhino.NodeFactory;
import org.junit.Before;
import org.junit.Test;

import static com.google.common.truth.Truth.assertThat;

public class RuntimeTypeCheckTest {

  private Compiler compiler;
  private CompilerInput input;
  private NodeFactory nodeFactory;

  @Before
  public void setup() {
    compiler = new Compiler();
    input = new CompilerInput("test.js", "test.js");
    nodeFactory = compiler.getFactory();
  }

  @Test
  public void testFunctionTypeCheck() {
    String jsCode = "function add(x, y) { return x + y; }";
    compiler.compile(ImmutableList.of(jsCode), input, null);
    Node ast = compiler.getAbstractSyntaxTree();
    RuntimeTypeCheck runtimeTypeCheck = new RuntimeTypeCheck(compiler, null);
    runtimeTypeCheck.process(input, ast);

    Node functionNode = findFunctionNodeByName(ast, "add");
    Node checkNode = findCheckerNode(functionNode);
    assertThat(checkNode).isNotNull();
  }

  @Test
  public void testBooleanValueTypeCheck() {
    String jsCode = "function isEven(x) { return x % 2 == 0; }";
    compiler.compile(ImmutableList.of(jsCode), input, null);
    Node ast = compiler.getAbstractSyntaxTree();
    RuntimeTypeCheck runtimeTypeCheck = new RuntimeTypeCheck(compiler, null);
    runtimeTypeCheck.process(input, ast);

    Node functionNode = findFunctionNodeByName(ast, "isEven");
    Node checkNode = findCheckerNode(functionNode.getParameter(0));
    assertThat(checkNode).isNotNull();
  }

  @Test
  public void testStringValueTypeCheck() {
    String jsCode = "function concat(x, y) { return x + y; }";
    compiler.compile(ImmutableList.of(jsCode), input, null);
    Node ast = compiler.getAbstractSyntaxTree();
    RuntimeTypeCheck runtimeTypeCheck = new RuntimeTypeCheck(compiler, null);
    runtimeTypeCheck.process(input, ast);

    Node functionNode = findFunctionNodeByName(ast, "concat");
    Node checkNode = findCheckerNode(functionNode.getParameter(0));
    assertThat(checkNode).isNotNull();

    checkNode = findCheckerNode(functionNode.getParameter(1));
    assertThat(checkNode).isNotNull();
  }

  @Test
  public void testNullTypeCheck() {
    String jsCode = "function isNull(x) { return x == null; }";
    compiler.compile(ImmutableList.of(jsCode), input, null);
    Node ast = compiler.getAbstractSyntaxTree();
    RuntimeTypeCheck runtimeTypeCheck = new RuntimeTypeCheck(compiler, null);
    runtimeTypeCheck.process(input, ast);

    Node functionNode = findFunctionNodeByName(ast, "isNull");
    Node checkNode = findCheckerNode(functionNode.getParameter(0));
    assertThat(checkNode).isNotNull();
    checkNode = findCheckerNode(nodeFactory.literal(null));
    assertThat(checkNode).isNotNull();
  }

  @Test
  public void testVoidTypeCheck() {
    String jsCode = "function print() { console.log('Hello'); }";
    compiler.compile(ImmutableList.of(jsCode), input, null);
    Node ast = compiler.getAbstractSyntaxTree();
    RuntimeTypeCheck runtimeTypeCheck = new RuntimeTypeCheck(compiler, null);
    runtimeTypeCheck.process(input, ast);

    Node functionNode = findFunctionNodeByName(ast, "print");
    Node checkNode = findCheckerNode(functionNode.getReturnStatement());
    assertThat(checkNode).isNotNull();
  }

  @Test
  public void testInterfaceTypeCheck() {
    String jsCode = "interface MyInterface {foo: number;} class MyClass implements MyInterface {}";
    compiler.compile(ImmutableList.of(jsCode), input, null);
    Node ast = compiler.getAbstractSyntaxTree();
    RuntimeTypeCheck runtimeTypeCheck = new RuntimeTypeCheck(compiler, null);
    runtimeTypeCheck.process(input, ast);

    Node classNode = findClassNodeByName(ast, "MyClass");
    Node constructorNode = classNode.getFirstChild();
    Node prototypeNode = constructorNode.getNext();

    Node checkNode = findCheckerNode(prototypeNode.getFirstChild());
    Node markerNode = findMarkerNodeByName(prototypeNode, "implements__MyInterface");
    checkNode = findCheckerNode(markerNode);
    assertThat(checkNode).isNotNull();
  }

  @Test
  public void testUserDefinedClassTypeCheck() {
    String jsCode = "class MyClass {}";
    compiler.compile(ImmutableList.of(jsCode), input, null);
    Node ast = compiler.getAbstractSyntaxTree();
    RuntimeTypeCheck runtimeTypeCheck = new RuntimeTypeCheck(compiler, null);
    runtimeTypeCheck.process(input, ast);

    Node classNode = findClassNodeByName(ast, "MyClass");
    Node constructorNode = classNode.getFirstChild();
    Node prototypeNode = constructorNode.getNext();

    Node checkNode = findCheckerNode(prototypeNode.getFirstChild());
    Node markerNode = findMarkerNodeByName(prototypeNode, "instance_of__MyClass");
    checkNode = findCheckerNode(markerNode);
    assertThat(checkNode).isNotNull();
  }

  @Test
  public void testArrayTypeCheck() {
    String jsCode = "function sum(x: number[], y: number[]) { return x[0] + y[0]; }";
    compiler.compile(ImmutableList.of(jsCode), input, null);
    Node ast = compiler.getAbstractSyntaxTree();
    RuntimeTypeCheck runtimeTypeCheck = new RuntimeTypeCheck(compiler, null);
    runtimeTypeCheck.process(input, ast);

    Node functionNode = findFunctionNodeByName(ast, "sum");
    Node checkNode1 = findCheckerNode(functionNode.getParameter(0));
    Node checkNode2 = findCheckerNode(functionNode.getParameter(1));
    Node checkNode3 = findCheckerNode(findExpression(functionNode, 0));
    Node checkNode4 = findCheckerNode(findExpression(functionNode, 1));

    assertThat(checkNode1).isNotNull();
    assertThat(checkNode2).isNotNull();
    assertThat(checkNode3).isNotNull();
    assertThat(checkNode4).isNotNull();
    assertThat(checkNode1.getType()).isEqualTo(JSType.ARRAY_TYPE);
    assertThat(checkNode1.getType().getComponentType()).isEqualTo(JSType.NUMBER_TYPE);

    checkNode1 = findCheckerNode(findExpression(functionNode, 0).getFirstChild());
    checkNode2 = findCheckerNode(findExpression(functionNode, 0).getLastChild());
    checkNode3 = findCheckerNode(findExpression(functionNode, 1).getFirstChild());
    checkNode4 = findCheckerNode(findExpression(functionNode, 1).getLastChild());

    assertThat(checkNode2).isNotNull();
    assertThat(checkNode3).isNotNull();
    assertThat(checkNode4).isNotNull();
  }

  @Test
  public void testUnionTypeCheck() {
    String jsCode = "function sum(x: number|string, y: number) { return x + y; }";
    compiler.compile(ImmutableList.of(jsCode), input, null);
    Node ast = compiler.getAbstractSyntaxTree();
    RuntimeTypeCheck runtimeTypeCheck = new RuntimeTypeCheck(compiler, null);
    runtimeTypeCheck.process(input, ast);

    Node functionNode = findFunctionNodeByName(ast, "sum");
    Node checkNode1 = findCheckerNode(functionNode.getParameter(0));
    Node checkNode2 = findCheckerNode(findExpression(functionNode, 0));

    Node checkNodeUnion = findCheckerNode(checkNode1);
    nodeFactory.deleteTree(checkNode1);
    checkNode1 = findCheckerNode(findExpression(functionNode, 0));

    assertThat(checkNode1).isNotNull();
    assertThat(checkNodeUnion).isInstanceOf(UnionType.class);
    assertThat(checkNodeUnion.getAlternates()).containsExactly(
        JSType.NUMBER_TYPE, JSType.STRING_TYPE);
  }

  @Test(expected = AssertionError.class)
  public void testUncheckedTypeCheck() {
    String jsCode = "function sum(x: any, y: any) { return x + y; }";
    compiler.compile(ImmutableList.of(jsCode), input, null);
    Node ast = compiler.getAbstractSyntaxTree();
    RuntimeTypeCheck runtimeTypeCheck = new RuntimeTypeCheck(compiler, null);
    runtimeTypeCheck.process(input, ast);

    Node functionNode = findFunctionNodeByName(ast, "sum");
    Node checkNode1 = findCheckerNode(functionNode.getParameter(0));
    Node checkNode2 = findCheckerNode(findExpression(functionNode, 0));

    assertThat(checkNode1).isNull();
    checkNode1 = findCheckerNode(findExpression(functionNode, 0));
    assertThat(checkNode1).isNull();
  }

  @Test(expected = AssertionError.class)
  public void testInvalidTypeCheck() {
    String jsCode = "function sum(x: invalidType, y: number) { return x + y; }";
    compiler.compile(ImmutableList.of(jsCode), input, null);
    Node ast = compiler.getAbstractSyntaxTree();
    RuntimeTypeCheck runtimeTypeCheck = new RuntimeTypeCheck(compiler, null);
    runtimeTypeCheck.process(input, ast);

    Node functionNode = findFunctionNodeByName(ast, "sum");
    Node checkNode1 = findCheckerNode(functionNode.getParameter(0));
    Node checkNode2 = findCheckerNode(findExpression(functionNode, 0));

    assertThat(checkNode1).isNull();
    checkNode1 = findCheckerNode(findExpression(functionNode, 0));
    assertThat(checkNode1).isNull();
  }

  private Node findFunctionNodeByName(Node node, String name) {
    for (Node child = node.getFirstChild(); child != null; child = child.getNext()) {
      if (NodeUtil.isFunction(child)) {
        FunctionType functionType = (FunctionType) child.getJSType();
        if (NodeUtil.getFunctionName(functionType.getSource()).equals(name)) {
          return child;
        }
      }
    }
    return null;
  }

  private Node findClassNodeByName(Node node, String name) {
    for (Node child = node.getFirstChild(); child != null; child = child.getNext()) {
      if (child.getType() == Token.CLASS) {
        ObjectType objectType = (ObjectType) child.getJSType();
        if (objectType.getReferenceName().equals(name)) {
          return child;
        }
      }
    }
    return null;
  }

  private Node findMarkerNodeByName(Node node, String name) {
    for (Node child = node.getFirstChild(); child != null; child = child.getNext()) {
      if (child.getType() == Token.GETPROP && child.getFirstChild().getText().equals(name)) {
        return child;
      }
    }
    return null;
  }

  private Node findCheckerNode(Node node) {
    for (Node child = node.getFirstChild(); child != null; child = child.getNext()) {
      if (child.getType() == Token.CALL && child.getFirstChild().getText().equals("checkType")) {
        return child;
      }
    }
    return null;
  }

  private Node findExpression(Node node, int index) {
    for (Node child = node.getFirstChild(); child != null; child = child.getNext()) {
      if (child.getType() == Token.EXPR_STMT) {
        if (index == 0) {
          return child;
        }
        index--;
      } else if (child.getType() == Token.LIST) {
        Node expressionNode = (Node) child.getFirstChild();
        if (index < expressionNode.getLength()) {
          return expressionNode.getChild(index);
        } else {
          index -= expressionNode.getLength();
        }
      }
    }
    throw new RuntimeException("Index out of bounds");
  }
}
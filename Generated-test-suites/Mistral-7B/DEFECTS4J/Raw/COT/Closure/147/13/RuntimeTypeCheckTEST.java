import org.junit.Before;
import org.junit.Test;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CompilerInput;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.RuntimeTypeCheck;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

public class RuntimeTypeCheckTest {

  private AbstractCompiler compiler;
  private RuntimeTypeCheck runtimeTypeCheck;

  @Before
  public void setup() {
    compiler = new Compiler();
    runtimeTypeCheck = new RuntimeTypeCheck(compiler, null);
  }

  @Test
  public void testProcess_SimpleFunctionWithConstructorType() throws Exception {
    CompilerInput input = new CompilerInput("test.js", "sourceContent");
    Node root = Node.parse("function Foo() {}");

    runtimeTypeCheck.process(input.getExterns(), root);

    Node checkNode = Node.newQualifiedNameNode("checkType", -1, -1);
    Node result = root.getFirstChild();

    Node functionNode = NodeUtil.findFunctionNodeByName("Foo", root);
    Node constructorNode = functionNode.getFirstChild();

    Node checkTypeCallNode = Node.newCall(checkNode, functionNode, constructorNode);

    Node expectedResult = Node.newQualifiedNameNode("Foo", -1, -1);
    Node expectedCheckTypeCallNode = Node.newCall(checkNode, expectedResult);

    Node expectedFunctionBody = Node.newBlock(checkTypeCallNode);

    Node expectedFunctionNode = Node.newFunction(
        Node.newQualifiedNameNode("Foo", -1, -1),
        Node.newArray(),
        expectedFunctionBody);

    Node expectedRoot = Node.newBlock(expectedFunctionNode);

    assertCodeEquals(expectedRoot, result);
  }

  @Test
  public void testProcess_FunctionWithMultipleConstructorTypes() throws Exception {
    CompilerInput input = new CompilerInput("test.js", "sourceContent");
    Node root = Node.parse("function Foo() {} function Bar() {}");

    runtimeTypeCheck.process(input.getExterns(), root);

    Node checkNode = Node.newQualifiedNameNode("checkType", -1, -1);
    Node functionNode1 = NodeUtil.findFunctionNodeByName("Foo", root);
    Node constructorNode1 = functionNode1.getFirstChild();

    Node functionNode2 = NodeUtil.findFunctionNodeByName("Bar", root);
    Node constructorNode2 = functionNode2.getFirstChild();

    Node checkTypeCallNode1 = Node.newCall(checkNode, functionNode1, constructorNode1);
    Node checkTypeCallNode2 = Node.newCall(checkNode, functionNode2, constructorNode2);

    Node expectedResult = Node.newBlock(
        checkTypeCallNode1,
        checkTypeCallNode2
    );

    assertCodeEquals(expectedResult, root.getFirstChild());
  }

  @Test
  public void testProcess_FunctionWithNullTypeParameter() throws Exception {
    CompilerInput input = new CompilerInput("test.js", "sourceContent");
    Node root = Node.parse("function Foo(x = null) {}");

    runtimeTypeCheck.process(input.getExterns(), root);

    Node checkNode = Node.newQualifiedNameNode("checkType", -1, -1);
    Node functionNode = NodeUtil.findFunctionNodeByName("Foo", root);
    Node parameterNode = functionNode.getParameter(0);

    Node checkTypeCallNode = Node.newCall(checkNode, functionNode, parameterNode);

    Node expectedResult = Node.newNull();

    assertCodeEquals(expectedResult, checkTypeCallNode);
  }

  @Test
  public void testProcess_FunctionWithVoidReturnType() throws Exception {
    CompilerInput input = new CompilerInput("test.js", "sourceContent");
    Node root = Node.parse("function Foo() { return; }");

    runtimeTypeCheck.process(input.getExterns(), root);

    Node checkNode = Node.newQualifiedNameNode("checkType", -1, -1);
    Node functionNode = NodeUtil.findFunctionNodeByName("Foo", root);

    Node checkTypeCallNode = Node.newCall(checkNode, functionNode, Node.newNull());

    Node expectedResult = Node.newNull();

    assertCodeEquals(expectedResult, checkTypeCallNode);
  }

  @Test(expected = AssertionError.class)
  public void testProcess_FunctionWithUndefinedReturnType() throws Exception {
    CompilerInput input = new CompilerInput("test.js", "sourceContent");
    Node root = Node.parse("function Foo() { }");

    runtimeTypeCheck.process(input.getExterns(), root);

    Node checkNode = Node.newQualifiedNameNode("checkType", -1, -1);
    Node functionNode = NodeUtil.findFunctionNodeByName("Foo", root);

    Node checkTypeCallNode = Node.newCall(checkNode, functionNode, Node.newNull());

    Node expectedResult = Node.newIdentifier("undefined");

    assertCodeEquals(expectedResult, checkTypeCallNode);
  }

  @Test
  public void testProcess_FunctionWithCustomInterfaceType() throws Exception {
    CompilerInput input = new CompilerInput("test.js", "sourceContent");
    Node root = Node.parse("interface Foo {} function Bar implements Foo {}");

    runtimeTypeCheck.process(input.getExterns(), root);

    Node checkNode = Node.newQualifiedNameNode("checkType", -1, -1);
    Node functionNode = NodeUtil.findFunctionNodeByName("Bar", root);

    Node checkTypeCallNode = Node.newCall(checkNode, functionNode, Node.newNull());

    Node expectedResult = Node.newQualifiedNameNode("Foo", -1, -1);

    assertCodeEquals(expectedResult, checkTypeCallNode);
  }

  @Test(expected = AssertionError.class)
  public void testProcess_FunctionWithUnknownInterfaceType() throws Exception {
    CompilerInput input = new CompilerInput("test.js", "sourceContent");
    Node root = Node.parse("interface Unknown {} function Bar implements Unknown {}");

    runtimeTypeCheck.process(input.getExterns(), root);

    Node checkNode = Node.newQualifiedNameNode("checkType", -1, -1);
    Node functionNode = NodeUtil.findFunctionNodeByName("Bar", root);

    Node checkTypeCallNode = Node.newCall(checkNode, functionNode, Node.newNull());

    Node expectedResult = Node.newIdentifier("unknown");

    assertCodeEquals(expectedResult, checkTypeCallNode);
  }

  @Test
  public void testProcess_FunctionWithCustomClassType() throws Exception {
    CompilerInput input = new CompilerInput("test.js", "sourceContent");
    Node root = Node.parse("class Foo {} function Bar(x) { new Foo(x); }");

    runtimeTypeCheck.process(input.getExterns(), root);

    Node checkNode = Node.newQualifiedNameNode("checkType", -1, -1);
    Node functionNode = NodeUtil.findFunctionNodeByName("Bar", root);
    Node argumentNode = functionNode.getParameter(0);

    Node checkTypeCallNode = Node.newCall(checkNode, functionNode, argumentNode);

    Node expectedResult = Node.newQualifiedNameNode("Foo", -1, -1);

    assertCodeEquals(expectedResult, checkTypeCallNode);
  }

  @Test(expected = AssertionError.class)
  public void testProcess_FunctionWithUnknownClassType() throws Exception {
    CompilerInput input = new CompilerInput("test.js", "sourceContent");
    Node root = Node.parse("class Unknown {} function Bar() { new Unknown(); }");

    runtimeTypeCheck.process(input.getExterns(), root);

    Node checkNode = Node.newQualifiedNameNode("checkType", -1, -1);
    Node functionNode = NodeUtil.findFunctionNodeByName("Bar", root);

    Node checkTypeCallNode = Node.newCall(checkNode, functionNode, Node.newNull());

    Node expectedResult = Node.newIdentifier("unknown");

    assertCodeEquals(expectedResult, checkTypeCallNode);
  }

  @Test
  public void testProcess_FunctionWithUnionType() throws Exception {
    CompilerInput input = new CompilerInput("test.js", "sourceContent");
    Node root = Node.parse("function Foo(x) { if (x instanceof Number) { x++; } }");

    runtimeTypeCheck.process(input.getExterns(), root);

    Node checkNode = Node.newQualifiedNameNode("checkType", -1, -1);
    Node functionNode = NodeUtil.findFunctionNodeByName("Foo", root);
    Node argumentNode = functionNode.getParameter(0);

    Node checkTypeCallNode = Node.newCall(checkNode, functionNode, argumentNode);

    Node expectedUnionType = Node.newUnionType(
        Node.newNumberLiteral(0),
        Node.newIdentifier("Number")
    );

    assertCodeEquals(expectedUnionType, checkTypeCallNode);
  }

  @Test
  public void testProcess_FunctionWithMultipleUnionTypes() throws Exception {
    CompilerInput input = new CompilerInput("test.js", "sourceContent");
    Node root = Node.parse("function Foo(x) { if (x instanceof Number || x instanceof String) { x++; } }");

    runtimeTypeCheck.process(input.getExterns(), root);

    Node checkNode = Node.newQualifiedNameNode("checkType", -1, -1);
    Node functionNode = NodeUtil.findFunctionNodeByName("Foo", root);
    Node argumentNode = functionNode.getParameter(0);

    Node checkTypeCallNode = Node.newCall(checkNode, functionNode, argumentNode);

    Node expectedUnionType = Node.newUnionType(
        Node.newNumberLiteral(0),
        Node.newIdentifier("Number"),
        Node.newIdentifier("String")
    );

    assertCodeEquals(expectedUnionType, checkTypeCallNode);
  }

  @Test
  public void testProcess_FunctionWithExternType() throws Exception {
    CompilerInput input = new CompilerInput("test.js", "sourceContent");
    Node root = Node.parse("function Foo() {} extern class Bar {}");

    runtimeTypeCheck.process(input.getExterns(), root);

    Node checkNode = Node.newQualifiedNameNode("checkType", -1, -1);
    Node functionNode = NodeUtil.findFunctionNodeByName("Foo", root);

    Node checkTypeCallNode = Node.newCall(checkNode, functionNode, Node.newNull());

    Node expectedExternType = Node.newQualifiedNameNode("Bar", -1, -1);

    assertCodeEquals(expectedExternType, checkTypeCallNode);
  }

  private void assertCodeEquals(Node expected, Node actual) {
    Node expectedWithoutBoilerplate = Normalize.parseAndNormalizeSyntheticCode(
        compiler, expected.toString(), "expectedCode_");

    Node actualWithoutBoilerplate = Normalize.parseAndNormalizeSyntheticCode(
        compiler, actual.toString(), "actualCode_");

    Node expectedWithBoilerplate = Node.newBlock(expectedWithoutBoilerplate);
    Node actualWithBoilerplate = Node.newBlock(actualWithoutBoilerplate);

    Node expectedNode = Node.newCall(
        Node.newQualifiedNameNode("checkCode", -1, -1),
        expectedWithBoilerplate,
        actualWithBoilerplate
    );

    Node resultNode = Node.newCall(
        Node.newQualifiedNameNode("checkCode", -1, -1),
        Node.newBlock(Node.newArray()),
        Node.newBlock(Node.newArray(expectedNode, Node.newNull()))
    );

    Node result = compiler.compileSingleFile(
        "test.js",
        Node.newBlock(Node.newFunction(
            Node.newQualifiedNameNode("checkCode", -1, -1),
            Node.newArray(),
            resultNode
        ))
    ).getTree();

    Node resultWithoutBoilerplate = Node.newGetProperty(
        result, "result"
    );

    assertNotNull(resultWithoutBoilerplate);
    assertCodeEquals(Node.newNull(), resultWithoutBoilerplate);
  }
}
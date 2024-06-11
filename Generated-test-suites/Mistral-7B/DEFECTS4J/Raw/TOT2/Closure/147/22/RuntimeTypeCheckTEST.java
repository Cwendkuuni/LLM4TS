import com.google.javascript.jscomp.RuntimeTypeCheck;
import com.google.javascript.rhino.JSType;
import com.google.javascript.rhino.Node;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class RuntimeTypeCheckTest {

  private static Node createTestNode(String code) {
    try {
      Node root = Node.parse(code);
      return root;
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  @Test
  public void testFunctionWithValidParameters() {
    String jsCode = "function testFunction(x) { return x; }";
    Node root = createTestNode(jsCode);

    RuntimeTypeCheck typeChecker = new RuntimeTypeCheck(null, null);
    typeChecker.process(null, root);

    Node functionNode = NodeUtil.getFunctionNode(root, "testFunction");
    JSType functionType = functionNode.getJSType();

    Node callNode = new Node(Token.CALL, Node.newString("testFunction"), Node.newNumber(123));
    JSType resultType = typeChecker.getType(callNode);

    assertEquals(JSType.getNumberType(), resultType);
  }

  @Test
  public void testFunctionWithInvalidParameterType() {
    String jsCode = "function testFunction(x) { return x; }";
    Node root = createTestNode(jsCode);

    RuntimeTypeCheck typeChecker = new RuntimeTypeCheck(null, null);
    typeChecker.process(null, root);

    Node functionNode = NodeUtil.getFunctionNode(root, "testFunction");
    JSType functionType = functionNode.getJSType();

    Node callNode = new Node(Token.CALL, Node.newString("testFunction"), Node.newString("hello"));
    Node checkNode = NodeUtil.getFirstChild(functionNode);

    try {
      JSType resultType = typeChecker.getType(callNode);
      fail("Type checking should have thrown an error.");
    } catch (RuntimeException e) {
      assertEquals("TypeError: 'hello' is not a number", e.getMessage());
    }
  }

  @Test
  public void testConstructorWithValidArguments() {
    String jsCode = "function MyClass() { }";
    Node root = createTestNode(jsCode);

    RuntimeTypeCheck typeChecker = new RuntimeTypeCheck(null, null);
    typeChecker.process(null, root);

    Node constructorNode = NodeUtil.getConstructorNode(root, "MyClass");
    JSType constructorType = constructorNode.getJSType();

    Node callNode = new Node(Token.CALL, constructorNode, Node.newArray());
    JSType resultType = typeChecker.getType(callNode);

    assertEquals(JSType.getObjectType(), resultType);
  }

  @Test
  public void testConstructorWithInvalidArguments() {
    String jsCode = "function MyClass(x) { }";
    Node root = createTestNode(jsCode);

    RuntimeTypeCheck typeChecker = new RuntimeTypeCheck(null, null);
    typeChecker.process(null, root);

    Node constructorNode = NodeUtil.getConstructorNode(root, "MyClass");
    JSType constructorType = constructorNode.getJSType();

    Node callNode = new Node(Token.CALL, constructorNode, Node.newString("hello"));
    Node checkNode = NodeUtil.getFirstChild(constructorNode);

    try {
      JSType resultType = typeChecker.getType(callNode);
      fail("Type checking should have thrown an error.");
    } catch (RuntimeException e) {
      assertEquals("TypeError: 'hello' is not a function", e.getMessage());
    }
  }

  @Test
  public void testInterfaceImplementation() {
    String jsCode = "interface MyInterface { myMethod(): void; }" +
        "class MyClass implements MyInterface { myMethod() {} }";
    Node root = createTestNode(jsCode);

    RuntimeTypeCheck typeChecker = new RuntimeTypeCheck(null, null);
    typeChecker.process(null, root);

    Node instanceNode = NodeUtil.newQualifiedNameNode("MyClass.prototype");
    JSType instanceType = instanceNode.getJSType();

    Node methodNode = NodeUtil.getMethodNode(instanceNode, "myMethod");
    JSType methodType = methodNode.getJSType();

    assertEquals(JSType.getFunctionType(null, JSType.getVoidType(), null), methodType);
  }

  @Test
  public void testNullCheck() {
    String jsCode = "function testFunction(x) { return x == null; }";
    Node root = createTestNode(jsCode);

    RuntimeTypeCheck typeChecker = new RuntimeTypeCheck(null, null);
    typeChecker.process(null, root);

    Node functionNode = NodeUtil.getFunctionNode(root, "testFunction");
    JSType functionType = functionNode.getJSType();

    Node callNode = new Node(Token.CALL, functionNode, Node.newNull());
    JSType resultType = typeChecker.getType(callNode);

    assertEquals(JSType.getBooleanValueType(), resultType);
  }

  @Test
  public void testNumberCheck() {
    String jsCode = "function testFunction(x) { return x > 5; }";
    Node root = createTestNode(jsCode);

    RuntimeTypeCheck typeChecker = new RuntimeTypeCheck(null, null);
    typeChecker.process(null, root);

    Node functionNode = NodeUtil.getFunctionNode(root, "testFunction");
    JSType functionType = functionNode.getJSType();

    Node callNode = new Node(Token.CALL, functionNode, Node.newNumber(6));
    JSType resultType = typeChecker.getType(callNode);

    assertEquals(JSType.getBooleanValueType(), resultType);
  }

  @Test
  public void testStringCheck() {
    String jsCode = "function testFunction(x) { return x.length > 5; }";
    Node root = createTestNode(jsCode);

    RuntimeTypeCheck typeChecker = new RuntimeTypeCheck(null, null);
    typeChecker.process(null, root);

    Node functionNode = NodeUtil.getFunctionNode(root, "testFunction");
    JSType functionType = functionNode.getJSType();

    Node callNode = new Node(Token.CALL, functionNode, Node.newString("hello"));
    JSType resultType = typeChecker.getType(callNode);

    assertEquals(JSType.getBooleanValueType(), resultType);
  }

  @Test
  public void testVoidCheck() {
    String jsCode = "function testFunction() { return; }";
    Node root = createTestNode(jsCode);

    RuntimeTypeCheck typeChecker = new RuntimeTypeCheck(null, null);
    typeChecker.process(null, root);

    Node functionNode = NodeUtil.getFunctionNode(root, "testFunction");
    JSType functionType = functionNode.getJSType();

    Node callNode = new Node(Token.CALL, functionNode, Node.newArray());
    JSType resultType = typeChecker.getType(callNode);

    assertEquals(JSType.getVoidType(), resultType);
  }

  @Test
  public void testArrayCheck() {
    String jsCode = "function testFunction(x) { return x.length > 5; }" +
        "function testArray() { return [1, 2, 3, 4, 5]; }";
    Node root = createTestNode(jsCode);

    RuntimeTypeCheck typeChecker = new RuntimeTypeCheck(null, null);
    typeChecker.process(null, root);

    Node functionNode = NodeUtil.getFunctionNode(root, "testFunction");
    JSType functionType = functionNode.getJSType();

    Node arrayNode = NodeUtil.getFunctionNode(root, "testArray");
    JSType arrayType = arrayNode.getJSType();

    Node callNode = new Node(Token.CALL, functionNode, Node.newArray(Node.newArray(Node.newNumber(1), Node.newNumber(2), Node.newNumber(3), Node.newNumber(4), Node.newNumber(5))));
    JSType resultType = typeChecker.getType(callNode);

    assertEquals(JSType.getBooleanValueType(), resultType);
  }
}
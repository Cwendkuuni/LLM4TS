import com.google.javascript.jscomp.RuntimeTypeCheck;
import com.google.javascript.rhino.Node;
import junit.framework.TestCase;

public class RuntimeTypeCheckTest extends TestCase {

  private AbstractCompiler compiler;
  private Node rootNode;
  private RuntimeTypeCheck runtimeTypeCheck;

  @Override
  protected void setUp() throws Exception {
    super.setUp();
    compiler = new Compiler();
    rootNode = Node.parse("function foo() {}");
    runtimeTypeCheck = new RuntimeTypeCheck(compiler, null);
  }

  @Test
  public void testRuntimeTypeCheck_ValidFunctionType() {
    runtimeTypeCheck.process(null, rootNode);
    Node fooFunction = NodeUtil.getFunctionByName(rootNode, "foo");
    assertTrue(runtimeTypeCheckCheckType(fooFunction, "Function"));
  }

  @Test
  public void testRuntimeTypeCheck_ValidObjectType() {
    rootNode = Node.parse("var obj = {a: 1}");
    runtimeTypeCheck.process(null, rootNode);
    Node objNode = NodeUtil.getFirstChild(rootNode);
    assertTrue(runtimeTypeCheckCheckType(objNode, "Object"));
  }

  @Test
  public void testRuntimeTypeCheck_ValidUnionType() {
    rootNode = Node.parse("function foo() {}");
    runtimeTypeCheck.process(null, rootNode);
    Node fooFunction = NodeUtil.getFunctionByName(rootNode, "foo");
    assertTrue(runtimeTypeCheckCheckType(fooFunction, "Function|String"));
  }

  @Test
  public void testRuntimeTypeCheck_ValidNullType() {
    rootNode = Node.parse("var nullValue = null");
    runtimeTypeCheck.process(null, rootNode);
    Node nullNode = NodeUtil.getFirstChild(rootNode);
    assertTrue(runtimeTypeCheckCheckType(nullNode, "Null"));
  }

  @Test
  public void testRuntimeTypeCheck_ValidBooleanValueType() {
    rootNode = Node.parse("var bool = true");
    runtimeTypeCheck.process(null, rootNode);
    Node boolNode = NodeUtil.getFirstChild(rootNode);
    assertTrue(runtimeTypeCheckCheckType(boolNode, "Boolean"));
  }

  @Test
  public void testRuntimeTypeCheck_ValidNumberValueType() {
    rootNode = Node.parse("var num = 10");
    runtimeTypeCheck.process(null, rootNode);
    Node numNode = NodeUtil.getFirstChild(rootNode);
    assertTrue(runtimeTypeCheckCheckType(numNode, "Number"));
  }

  @Test
  public void testRuntimeTypeCheck_ValidStringValueType() {
    rootNode = Node.parse("var str = 'hello'");
    runtimeTypeCheck.process(null, rootNode);
    Node strNode = NodeUtil.getFirstChild(rootNode);
    assertTrue(runtimeTypeCheckCheckType(strNode, "String"));
  }

  @Test
  public void testRuntimeTypeCheck_ValidVoidType() {
    rootNode = Node.parse("function foo() {}");
    runtimeTypeCheck.process(null, rootNode);
    Node fooFunction = NodeUtil.getFunctionByName(rootNode, "foo");
    assertTrue(runtimeTypeCheckCheckType(fooFunction, "Function|Void"));

    fooFunction = NodeUtil.getLastChild(fooFunction.getBody());
    assertTrue(runtimeTypeCheckCheckType(fooFunction, "Void"));
  }

  @Test
  public void testRuntimeTypeCheck_ValidInterfaceType() {
    rootNode = Node.parse("interface Foo {}");
    runtimeTypeCheck.process(null, rootNode);
    Node fooInterface = NodeUtil.getFirstChild(rootNode);
    assertTrue(runtimeTypeCheckCheckType(fooInterface, "Foo"));
  }

  @Test
  public void testRuntimeTypeCheck_ValidClassType() {
    rootNode = Node.parse("class Foo {}");
    runtimeTypeCheck.process(null, rootNode);
    Node fooClass = NodeUtil.getFirstChild(rootNode);
    assertTrue(runtimeTypeCheckCheckType(fooClass, "Foo"));
  }

  @Test
  public void testRuntimeTypeCheck_ValidExternedType() {
    rootNode = Node.parse("/** @type {number} */ var num = 10");
    runtimeTypeCheck.process(null, rootNode);
    Node numNode = NodeUtil.getFirstChild(rootNode);
    assertTrue(runtimeTypeCheckCheckType(numNode, "Number"));
  }

  @Test
  public void testRuntimeTypeCheck_InvalidFunctionType() {
    rootNode = Node.parse("function foo() {}");
    runtimeTypeCheck.process(null, rootNode);
    Node fooFunction = NodeUtil.getFunctionByName(rootNode, "foo");

    assertFalse(runtimeTypeCheckCheckType(fooFunction, "String"));
  }

  @Test
  public void testRuntimeTypeCheck_InvalidObjectType() {
    rootNode = Node.parse("function foo() {}");
    runtimeTypeCheck.process(null, rootNode);
    Node fooFunction = NodeUtil.getFunctionByName(rootNode, "foo");

    assertFalse(runtimeTypeCheckCheckType(fooFunction, "Number"));
  }

  @Test
  public void testRuntimeTypeCheck_InvalidUnionTypeMismatch() {
    rootNode = Node.parse("function foo() {}");
    runtimeTypeCheck.process(null, rootNode);
    Node fooFunction = NodeUtil.getFunctionByName(rootNode, "foo");

    assertFalse(runtimeTypeCheckCheckType(fooFunction, "String|Number"));
  }

  @Test
  public void testRuntimeTypeCheck_InvalidUnionTypeMissingAlternative() {
    rootNode = Node.parse("function foo() {}");
    runtimeTypeCheck.process(null, rootNode);
    Node fooFunction = NodeUtil.getFunctionByName(rootNode, "foo");

    assertFalse(runtimeTypeCheckCheckType(fooFunction, "String|Foo"));
  }

  @Test
  public void testRuntimeTypeCheck_InvalidNullType() {
    rootNode = Node.parse("function foo() {}");
    runtimeTypeCheck.process(null, rootNode);
    Node fooFunction = NodeUtil.getFunctionByName(rootNode, "foo");

    assertFalse(runtimeTypeCheckCheckType(fooFunction, "Null"));
  }

  private boolean runtimeTypeCheckCheckType(Node node, String expectedType) {
    Node checkNode = new Node(Token.GETELEM, node, Node.newString("instance_of__"));
    Node checkTypeNode = NodeUtil.getNextSibling(checkNode);

    String actualType = getTypeName(checkTypeNode);
    return expectedType.equals(actualType);
  }

  private String getTypeName(Node node) {
    if (node.getType() == Token.STRING) {
      return node.getString();
    } else {
      return node.getFirstChild().getFirstChild().getString();
    }
  }
}
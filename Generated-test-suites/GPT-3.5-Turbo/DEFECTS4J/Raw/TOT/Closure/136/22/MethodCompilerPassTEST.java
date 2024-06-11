import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class MethodCompilerPassTest {

  private MethodCompilerPass compilerPass;

  @Before
  public void setUp() {
    compilerPass = new MethodCompilerPass();
  }

  @Test
  public void testGetActingCallback() {
    // Test the return value of the getActingCallback() method
    Callback callback = compilerPass.getActingCallback();
    assertNotNull(callback);
  }

  @Test
  public void testGetSignatureStore() {
    // Test the return value of the getSignatureStore() method
    SignatureStore store = compilerPass.getSignatureStore();
    assertNotNull(store);
  }

  @Test
  public void testProcess() {
    // Test the process() method by passing in test cases for externs and root nodes
    // and assert that the methodDefinitions, externMethods and externMethodsWithoutSignatures are properly populated
    Node externs = createExternsNode();
    Node root = createRootNode();
    compilerPass.process(externs, root);

    Set<String> externMethods = compilerPass.externMethods;
    Set<String> externMethodsWithoutSignatures = compilerPass.externMethodsWithoutSignatures;
    Multimap<String, Node> methodDefinitions = compilerPass.methodDefinitions;

    assertNotNull(externMethods);
    assertEquals(2, externMethods.size());
    assertTrue(externMethods.contains("setTimeout"));
    assertTrue(externMethods.contains("setInterval"));

    assertNotNull(externMethodsWithoutSignatures);
    assertEquals(1, externMethodsWithoutSignatures.size());
    assertTrue(externMethodsWithoutSignatures.contains("setImmediate"));

    assertNotNull(methodDefinitions);
    assertEquals(2, methodDefinitions.size());
    assertTrue(methodDefinitions.containsKey("setTimeout"));
    assertEquals(1, methodDefinitions.get("setTimeout").size());
    assertTrue(methodDefinitions.containsEntry("setTimeout", createTimeoutFunctionNode()));

    assertTrue(methodDefinitions.containsKey("setInterval"));
    assertEquals(1, methodDefinitions.get("setInterval").size());
    assertTrue(methodDefinitions.containsEntry("setInterval", createIntervalFunctionNode()));
  }

  @Test
  public void testAddPossibleSignature() {
    // Test the addPossibleSignature() method by passing in a name and a function node
    // and assert that the signature is properly added to the SignatureStore and methodDefinitions
    String name = "testFunction";
    Node functionNode = createFunctionNode();

    compilerPass.addPossibleSignature(name, functionNode);

    SignatureStore store = compilerPass.getSignatureStore();
    Multimap<String, Node> methodDefinitions = compilerPass.methodDefinitions;

    assertNotNull(store);
    assertTrue(store.containsSignature(name));
    assertEquals(1, methodDefinitions.size());
    assertTrue(methodDefinitions.containsKey(name));
    assertEquals(1, methodDefinitions.get(name).size());
    assertTrue(methodDefinitions.containsEntry(name, functionNode));
  }

  @Test
  public void testAddSignature() {
    // Test the addSignature() method by passing in a name, a function node, and a source file name
    // and assert that the signature is properly added to the SignatureStore and methodDefinitions
    String name = "testFunction";
    Node functionNode = createFunctionNode();
    String sourceFile = "test.js";

    compilerPass.addSignature(name, functionNode, sourceFile);

    SignatureStore store = compilerPass.getSignatureStore();
    Multimap<String, Node> methodDefinitions = compilerPass.methodDefinitions;

    assertNotNull(store);
    assertTrue(store.containsSignature(name));
    assertEquals(1, methodDefinitions.size());
    assertTrue(methodDefinitions.containsKey(name));
    assertEquals(1, methodDefinitions.get(name).size());
    assertTrue(methodDefinitions.containsEntry(name, functionNode));
  }

  @Test
  public void testGetExternMethods() {
    // Test the GetExternMethods callback by creating a MockNodeTraversal instance and calling visit()
    // and assert that the externMethods and externMethodsWithoutSignatures are properly populated
    MockNodeTraversal traversal = new MockNodeTraversal();
    Node n = createGetPropertyNode("setTimeout", "prop");

    compilerPass.new GetExternMethods().visit(traversal, n, null);

    Set<String> externMethods = compilerPass.externMethods;
    Set<String> externMethodsWithoutSignatures = compilerPass.externMethodsWithoutSignatures;

    assertNotNull(externMethods);
    assertEquals(1, externMethods.size());
    assertTrue(externMethods.contains("setTimeout"));

    assertNotNull(externMethodsWithoutSignatures);
    assertEquals(0, externMethodsWithoutSignatures.size());
  }

  @Test
  public void testGatherSignatures() {
    // Test the GatherSignatures callback by creating a MockNodeTraversal instance and calling visit()
    // and assert that the methodDefinitions are properly populated
    MockNodeTraversal traversal = new MockNodeTraversal();
    Node n = createGetPropertyNode("getBar", "prototype");

    compilerPass.new GatherSignatures().visit(traversal, n, null);

    Multimap<String, Node> methodDefinitions = compilerPass.methodDefinitions;

    assertNotNull(methodDefinitions);
    assertEquals(1, methodDefinitions.size());
    assertTrue(methodDefinitions.containsKey("getBar"));
    assertEquals(1, methodDefinitions.get("getBar").size());
    assertTrue(methodDefinitions.containsEntry("getBar", createFunctionNode()));
  }

  // helper methods to create nodes for testing

  private Node createExternsNode() {
    // create an example externs node with two getprop nodes
    // one with a function node as the next sibling and another with a string node as the next sibling
    Node getprop1 = createGetPropertyNode("setTimeout", "prop1");
    Node functionNode = createFunctionNode();
    Node assign1 = createAssignNode(getprop1, functionNode);

    Node getprop2 = createGetPropertyNode("setInterval", "prop2");
    Node stringNode = createStringNode("myString");
    Node assign2 = createAssignNode(getprop2, stringNode);

    Node externs = createRootNode();
    externs.addChildToBack(assign1);
    externs.addChildToBack(assign2);
    return externs;
  }

  private Node createRootNode() {
    // create an empty root node
    return new Node(Token.ROOT);
  }

  private Node createGetPropertyNode(String name, String prop) {
    // create a getprop node with the given name and property
    Node getprop = new Node(Token.GETPROP);
    Node nameNode = new Node(Token.NAME, name);
    Node propNode = new Node(Token.STRING, prop);
    getprop.addChildToBack(nameNode);
    getprop.addChildToBack(propNode);
    return getprop;
  }

  private Node createFunctionNode() {
    // create a simple function node
    Node functionNode = new Node(Token.FUNCTION);
    functionNode.addChildToBack(new Node(Token.NAME, "testFunction"));
    Node paramList = new Node(Token.PARAM_LIST);
    functionNode.addChildToBack(paramList);
    Node body = new Node(Token.BLOCK);
    functionNode.addChildToBack(body);
    return functionNode;
  }

  private Node createStringNode(String value) {
    // create a string node with the given value
    return new Node(Token.STRING, value);
  }

  private Node createAssignNode(Node lhs, Node rhs) {
    // create an assign node with the given lhs and rhs
    Node assign = new Node(Token.ASSIGN);
    assign.addChildToBack(lhs);
    assign.addChildToBack(rhs);
    return assign;
  }

  // helper methods to create mock objects for testing

  private class MockNodeTraversal extends NodeTraversal {
    public MockNodeTraversal() {
      super(compiler, null, null);
    }

    @Override
    public Scope getScope() {
      return new Scope();
    }
  }

}
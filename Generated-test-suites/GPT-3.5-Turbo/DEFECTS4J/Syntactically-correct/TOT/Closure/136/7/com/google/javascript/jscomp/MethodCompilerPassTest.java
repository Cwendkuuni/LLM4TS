import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class MethodCompilerPassTest {

  private MethodCompilerPass methodCompilerPass;

  @Before
  public void setUp() {
    methodCompilerPass = new MethodCompilerPass();
  }

  @Test
  public void testProcess_WithExternMethods_AddsMethodsToExternMethodsSet() {
    Node externMethods = new Node(Token.BLOCK);
    externMethods.addChildToFront(createExternMethodNode("method1"));
    externMethods.addChildToFront(createExternMethodNode("method2"));
    Node root = new Node(Token.BLOCK);
    
    methodCompilerPass.process(externMethods, root);
    
    assertTrue(methodCompilerPass.externMethods.contains("method1"));
    assertTrue(methodCompilerPass.externMethods.contains("method2"));
  }

  @Test
  public void testProcess_WithExternMethodsWithoutSignatures_AddsMethodsToExternMethodsWithoutSignaturesSet() {
    Node externMethods = new Node(Token.BLOCK);
    externMethods.addChildToFront(createExternMethodNode("method1"));
    externMethods.addChildToFront(createExternMethodNode("method2"));
    Node root = new Node(Token.BLOCK);

    methodCompilerPass.process(externMethods, root);

    assertTrue(methodCompilerPass.externMethodsWithoutSignatures.contains("method1"));
    assertTrue(methodCompilerPass.externMethodsWithoutSignatures.contains("method2"));
  }

  @Test
  public void testProcess_WithFunctionSignature_AddsSignatureToSignatureStoreAndMethodDefinitions() {
    Node externMethods = new Node(Token.BLOCK);
    Node root = new Node(Token.BLOCK);
    Node functionNode = createFunctionNode();
    root.addChildToFront(createAssignNode("method1", functionNode));
    
    methodCompilerPass.process(externMethods, root);

    assertNotNull(methodCompilerPass.getSignatureStore().getSignature("method1"));
    assertTrue(methodCompilerPass.methodDefinitions.containsKey("method1"));
    assertEquals(functionNode, methodCompilerPass.methodDefinitions.get("method1"));
  }

  @Test
  public void testProcess_WithMethodNameSignature_AddsSignatureToSignatureStoreAndMethodDefinitions() {
    Node externMethods = new Node(Token.BLOCK);
    Node root = new Node(Token.BLOCK);
    root.addChildToFront(createAssignNode("method1", createNameNode("function1")));
    
    methodCompilerPass.process(externMethods, root);

    assertNotNull(methodCompilerPass.getSignatureStore().getSignature("method1"));
    assertTrue(methodCompilerPass.methodDefinitions.containsKey("method1"));
    assertEquals(createNameNode("function1"), methodCompilerPass.methodDefinitions.get("method1"));
  }

  @Test
  public void testProcess_WithNonMethodPropertyName_AddsPropertyNameToNonMethodPropertiesSet() {
    Node externMethods = new Node(Token.BLOCK);
    Node root = new Node(Token.BLOCK);
    root.addChildToFront(createAssignNode("property1", createValueNode()));
    
    methodCompilerPass.process(externMethods, root);

    assertTrue(methodCompilerPass.nonMethodProperties.contains("property1"));
  }

  private Node createExternMethodNode(String methodName) {
    Node externMethod = new Node(Token.STRING);
    externMethod.setString(methodName);
    return externMethod;
  }

  private Node createFunctionNode() {
    Node functionNode = new Node(Token.FUNCTION);
    functionNode.addChildToFront(new Node(Token.NAME, createNameNode("function1")));
    return functionNode;
  }

  private Node createAssignNode(String propertyName, Node assignee) {
    Node assign = new Node(Token.ASSIGN);
    Node getProp = new Node(Token.GETPROP);
    getProp.addChildToFront(createNameNode("object1"));
    getProp.addChildToBack(new Node(Token.STRING, propertyName));
    assign.addChildToFront(getProp);
    assign.addChildToBack(assignee);
    return assign;
  }

  private Node createNameNode(String name) {
    Node nameNode = new Node(Token.NAME);
    nameNode.setString(name);
    return nameNode;
  }

  private Node createValueNode() {
    Node valueNode = new Node(Token.STRING);
    valueNode.setString("value1");
    return valueNode;
  }
}
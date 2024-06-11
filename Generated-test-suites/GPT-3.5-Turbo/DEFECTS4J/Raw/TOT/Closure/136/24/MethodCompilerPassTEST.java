import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class MethodCompilerPassTest {

  private MethodCompilerPass methodCompilerPass;

  @Mock
  private AbstractCompiler compiler;

  @Mock
  private SignatureStore signatureStore;

  @Before
  public void setUp() {
    MockitoAnnotations.initMocks(this);
    methodCompilerPass = new MethodCompilerPass(compiler) {
      @Override
      Callback getActingCallback() {
        return null;
      }

      @Override
      SignatureStore getSignatureStore() {
        return signatureStore;
      }
    };
  }

  @Test
  public void process_WithValidExternsAndRoot_CallsMethodsAndStoresSignatures() {
    Node externs = new Node();
    Node root = new Node();
    NodeTraversal traversal = Mockito.mock(NodeTraversal.class);
    GetExternMethods getExternMethods = methodCompilerPass.new GetExternMethods();

    Mockito.when(traversal.getSourceName()).thenReturn("externs");
    Mockito.when(compiler.isIdeMode()).thenReturn(false);
    Mockito.when(signatureStore.containsSignature("setTimeout")).thenReturn(true);

    getExternMethods.visit(traversal, createGetMethodNode("setTimeout"), null);

    assertFalse(methodCompilerPass.externMethods.isEmpty());
    assertTrue(methodCompilerPass.externMethodsWithoutSignatures.isEmpty());
    assertTrue(methodCompilerPass.methodDefinitions.containsKey("setTimeout"));

    Mockito.verify(signatureStore).addSignature("setTimeout", createGetMethodNode("setTimeout"),
        "externs");
  }

  @Test
  public void addPossibleSignature_WithFunctionNode_AddsSignatureToStore() {
    String functionName = "testFunction";
    Node functionNode = createFunctionNode();

    methodCompilerPass.addPossibleSignature(functionName, functionNode, null);

    Mockito.verify(signatureStore).addSignature(functionName, functionNode, null);
    assertTrue(methodCompilerPass.methodDefinitions.containsKey(functionName));
  }

  @Test
  public void addPossibleSignature_WithNameNode_AddsSignatureToStore() {
    String functionName = "testFunction";
    String inputName = "testFile";
    Node nameNode = createNameNode("testFunction");

    methodCompilerPass.addPossibleSignature(functionName, nameNode, createNodeTraversal(inputName));

    Mockito.verify(signatureStore).addSignature(functionName, nameNode, inputName);
    assertTrue(methodCompilerPass.methodDefinitions.containsKey(functionName));
  }

  @Test
  public void addPossibleSignature_WithNonFunctionNode_AddsToNonMethodProperties() {
    String functionName = "testFunction";
    Node nonFunctionNode = new Node();

    methodCompilerPass.addPossibleSignature(functionName, nonFunctionNode, null);

    assertTrue(methodCompilerPass.nonMethodProperties.contains(functionName));
    Mockito.verifyZeroInteractions(signatureStore);
    assertFalse(methodCompilerPass.methodDefinitions.containsKey(functionName));
  }

  @Test
  public void GatherSignatures_VisitGetMethod_AddsSignatureToStore() {
    NodeTraversal traversal = createNodeTraversal(null);

    methodCompilerPass.new GatherSignatures().visit(
        traversal, createGetMethodNode("testMethod"), null);

    Mockito.verify(signatureStore).addSignature("testMethod", createTestMethodNode(), null);
    assertTrue(methodCompilerPass.methodDefinitions.containsKey("testMethod"));
  }

  @Test
  public void GatherSignatures_VisitPrototypeParent_AddsSignatureToStore() {
    NodeTraversal traversal = createNodeTraversal(null);

    methodCompilerPass.new GatherSignatures().visit(
        traversal, createGetPropPrototypeNode(), null);

    Mockito.verify(signatureStore).addSignature("getBar", createFunctionNode(), null);
    assertTrue(methodCompilerPass.methodDefinitions.containsKey("getBar"));
  }

  private Node createGetMethodNode(String methodName) {
    Node getPropNode = new Node(Token.GETPROP);
    Node nameNode = new Node(Token.NAME, new Node(Token.GETPROP));
    nameNode.setString("name");
    getPropNode.addChildToBack(nameNode);
    getPropNode.addChildToBack(new Node(Token.STRING, methodName));
    return getPropNode;
  }

  private Node createFunctionNode() {
    Node functionNode = new Node(Token.FUNCTION);
    functionNode.setChidren(new Node(Token.BLOCK));
    return functionNode;
  }

  private Node createNameNode(String name) {
    Node nameNode = new Node(Token.NAME);
    nameNode.setString(name);
    return nameNode;
  }

  private Node createTestMethodNode() {
    Node testMethodNode = new Node(Token.NAME);
    testMethodNode.setString("testMethod");
    return testMethodNode;
  }

  private Node createGetPropPrototypeNode() {
    Node getProtoNode = new Node(Token.GETPROP);
    Node nameNode = new Node(Token.NAME, new Node(Token.GETPROP));
    nameNode.setString("name");
    getProtoNode.addChildToBack(new Node(Token.GETPROP));
    getProtoNode.addChildToBack(new Node(Token.STRING, "prototype"));
    getProtoNode.addChildToBack(new Node(Token.STRING, "getBar"));
    return getProtoNode;
  }

  private NodeTraversal createNodeTraversal(String sourceName) {
    NodeTraversal traversal = Mockito.mock(NodeTraversal.class);
    Mockito.when(traversal.getSourceName()).thenReturn(sourceName);
    return traversal;
  }
}
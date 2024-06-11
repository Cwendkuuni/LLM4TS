
import static org.junit.Assert.*;
import org.junit.Test;
import com.google.javascript.rhino.Node;

public class MethodCompilerPassTest {
  
  @Test
  public void testProcess_WithExternsAndRoot_ExternMethodsAndMethodDefinitionsPopulated() {
    MethodCompilerPass compilerPass = new MethodCompilerPass(null);
    Node externs = createExternsNode();
    Node root = new Node(Token.ROOT);
    
    compilerPass.process(externs, root);
    
    // Verify that externMethods and methodDefinitions are populated correctly
    assertEquals(2, compilerPass.externMethods.size());
    assertTrue(compilerPass.externMethods.contains("setTimeout"));
    assertTrue(compilerPass.externMethods.contains("setInterval"));
    
    assertEquals(2, compilerPass.methodDefinitions.size());
    assertTrue(compilerPass.methodDefinitions.containsKey("setTimeout"));
    assertTrue(compilerPass.methodDefinitions.containsKey("setInterval"));
    
    assertEquals(1, compilerPass.methodDefinitions.get("setTimeout").size());
    assertEquals(1, compilerPass.methodDefinitions.get("setInterval").size());
  }
  
  @Test
  public void testProcess_NoExterns_ExternMethodsAndMethodDefinitionsEmpty() {
    MethodCompilerPass compilerPass = new MethodCompilerPass(null);
    Node root = new Node(Token.ROOT);
    
    compilerPass.process(null, root);
    
    // Verify that externMethods and methodDefinitions are empty
    assertTrue(compilerPass.externMethods.isEmpty());
    assertTrue(compilerPass.methodDefinitions.isEmpty());
  }
  
  @Test
  public void testGetActingCallback_ReturnsInstanceOfActingCallback() {
    MethodCompilerPass compilerPass = new MethodCompilerPass(null);
    
    MethodCompilerPass.Callback callback = compilerPass.getActingCallback();
    
    assertTrue(callback instanceof ActingCallback);
  }
  
  @Test
  public void testGetSignatureStore_ReturnsInstanceOfSignatureStore() {
    MethodCompilerPass compilerPass = new MethodCompilerPass(null);
    
    MethodCompilerPass.SignatureStore store = compilerPass.getSignatureStore();
    
    assertTrue(store instanceof SignatureStoreImpl);
  }
  
  @Test
  public void testAddPossibleSignature_NodeIsFunction_AddsSignatureToStoreAndMethodDefinitions() {
    MethodCompilerPass compilerPass = new MethodCompilerPass(null);
    String name = "testFunction";
    Node node = new Node(Token.FUNCTION);
    
    compilerPass.addPossibleSignature(name, node, new NodeTraversal());
    
    assertTrue(compilerPass.methodDefinitions.containsKey(name));
    assertEquals(1, compilerPass.methodDefinitions.get(name).size());
    
    MethodCompilerPass.SignatureStore store = compilerPass.getSignatureStore();
    // Verify that the signature is added to the store
    assertNotNull(store.getSignature(name));
  }
  
  @Test
  public void testAddPossibleSignature_NodeIsNameWithDefinedVar_AddsSignatureToStoreAndMethodDefinitions() {
    MethodCompilerPass compilerPass = new MethodCompilerPass(null);
    String name = "testFunction";
    Node node = new Node(Token.NAME, name);
    
    compilerPass.addPossibleSignature(name, node, new NodeTraversal());
    
    assertTrue(compilerPass.methodDefinitions.containsKey(name));
    assertEquals(1, compilerPass.methodDefinitions.get(name).size());
    
    MethodCompilerPass.SignatureStore store = compilerPass.getSignatureStore();
    // Verify that the signature is added to the store
    assertNotNull(store.getSignature(name));
  }
  
  @Test
  public void testAddPossibleSignature_NodeIsNameWithUndefinedVar_NonMethodPropertiesContainsName() {
    MethodCompilerPass compilerPass = new MethodCompilerPass(null);
    String name = "undefinedFunction";
    Node node = new Node(Token.NAME, name);
    
    compilerPass.addPossibleSignature(name, node, new NodeTraversal());
    
    assertTrue(compilerPass.nonMethodProperties.contains(name));
  }
  
  private Node createExternsNode() {
    Node externs = new Node(Token.ROOT);
    
    Node setTimeoutNode = new Node(Token.GETPROP);
    Node setTimeoutName = new Node(Token.NAME, "setTimeout");
    Node setTimeoutString = new Node(Token.STRING, "setTimeout");
    setTimeoutNode.addChildToBack(setTimeoutName);
    setTimeoutNode.addChildToBack(setTimeoutString);
    
    Node setIntervalNode = new Node(Token.GETPROP);
    Node setIntervalName = new Node(Token.NAME, "setInterval");
    Node setIntervalString = new Node(Token.STRING, "setInterval");
    setIntervalNode.addChildToBack(setIntervalName);
    setIntervalNode.addChildToBack(setIntervalString);
    
    externs.addChildToBack(setTimeoutNode);
    externs.addChildToBack(setIntervalNode);
    
    return externs;
  }
}

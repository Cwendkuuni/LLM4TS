import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.Token;
import org.junit.Assert;
import org.junit.Test;

public class MethodCompilerPassTest {

  @Test
  public void testProcess() {
    // typical use case
    Node externs = new Node(Token.EXTERNALSCRIPT);
    Node root = new Node(Token.SCRIPT);
    AbstractCompiler compiler = new AbstractCompiler();
    MethodCompilerPass methodCompilerPass = new TestMethodCompilerPass(compiler);
    
    methodCompilerPass.process(externs, root);
    
    // verify that the method definitions were collected correctly
    Assert.assertEquals(2, methodCompilerPass.methodDefinitions.size());
    Assert.assertEquals(1, methodCompilerPass.methodDefinitions.get("function1").size());
    Assert.assertEquals(1, methodCompilerPass.methodDefinitions.get("function2").size());
    
    // verify that the signature store was updated correctly
    TestSignatureStore signatureStore = (TestSignatureStore) methodCompilerPass.getSignatureStore();
    Assert.assertEquals(2, signatureStore.signatures.size());
    Assert.assertTrue(signatureStore.signatures.containsKey("function1"));
    Assert.assertTrue(signatureStore.signatures.containsKey("function2"));
  }
  
  @Test
  public void testProcess_WithExterns() {
    // typical use case with externs
    Node externs = new Node(Token.VAR, new Node(Token.NAME, "externMethods"), new Node(Token.OBJECTLIT));
    Node root = new Node(Token.SCRIPT);
    AbstractCompiler compiler = new AbstractCompiler();
    MethodCompilerPass methodCompilerPass = new TestMethodCompilerPass(compiler);
    
    methodCompilerPass.process(externs, root);
    
    // verify that the extern methods were collected correctly
    Assert.assertEquals(2, methodCompilerPass.externMethods.size());
    Assert.assertTrue(methodCompilerPass.externMethods.contains("externMethod1"));
    Assert.assertTrue(methodCompilerPass.externMethods.contains("externMethod2"));
    
    // verify that the extern methods without signatures were identified correctly
    Assert.assertEquals(1, methodCompilerPass.externMethodsWithoutSignatures.size());
    Assert.assertTrue(methodCompilerPass.externMethodsWithoutSignatures.contains("externMethod2"));
  }
  
  @Test
  public void testProcess_WithNonMethodProperties() {
    // typical use case with non-method properties
    Node externs = new Node(Token.VAR, new Node(Token.NAME, "nonMethodProperties"), new Node(Token.OBJECTLIT));
    Node root = new Node(Token.SCRIPT);
    AbstractCompiler compiler = new AbstractCompiler();
    MethodCompilerPass methodCompilerPass = new TestMethodCompilerPass(compiler);
    
    methodCompilerPass.process(externs, root);
    
    // verify that the non-method properties were collected correctly
    Assert.assertEquals(2, methodCompilerPass.nonMethodProperties.size());
    Assert.assertTrue(methodCompilerPass.nonMethodProperties.contains("property1"));
    Assert.assertTrue(methodCompilerPass.nonMethodProperties.contains("property2"));
  }
  
  @Test(expected = IllegalStateException.class)
  public void testProcess_VariableNotFound() {
    // error scenario where a variable is not found
    Node externs = new Node(Token.VAR, new Node(Token.NAME, "undefinedVariable"), new Node(Token.NULL));
    Node root = new Node(Token.SCRIPT);
    AbstractCompiler compiler = new AbstractCompiler();
    MethodCompilerPass methodCompilerPass = new TestMethodCompilerPass(compiler);
    
    methodCompilerPass.process(externs, root);
  }
  
  @Test
  public void testGatherSignatures() {
    // typical use case for gathering method signatures
    Node script = new Node(Token.SCRIPT);
    MethodCompilerPass methodCompilerPass = new TestMethodCompilerPass(null);
    TestCallback callback = methodCompilerPass.new TestCallback();
    
    methodCompilerPass.gatherSignatures(script, callback);
    
    // verify that the signatures were added correctly
    TestSignatureStore signatureStore = (TestSignatureStore) methodCompilerPass.getSignatureStore();
    Assert.assertEquals(2, signatureStore.signatures.size());
    Assert.assertTrue(signatureStore.signatures.containsKey("function1"));
    Assert.assertTrue(signatureStore.signatures.containsKey("function2"));
  }
  
  @Test
  public void testGetActingCallback() {
    // typical use case for getting the acting callback
    AbstractCompiler compiler = new AbstractCompiler();
    MethodCompilerPass methodCompilerPass = new TestMethodCompilerPass(compiler);
    
    Callback callback = methodCompilerPass.getActingCallback();
    
    // verify that the returned callback is an instance of the expected subclass
    Assert.assertTrue(callback instanceof TestCallback);
  }
  
  @Test
  public void testGetSignatureStore() {
    // typical use case for getting the signature store
    AbstractCompiler compiler = new AbstractCompiler();
    MethodCompilerPass methodCompilerPass = new TestMethodCompilerPass(compiler);
    
    SignatureStore signatureStore = methodCompilerPass.getSignatureStore();
    
    // verify that the returned signature store is an instance of the expected subclass
    Assert.assertTrue(signatureStore instanceof TestSignatureStore);
  }
  
  // create a test implementation of MethodCompilerPass for testing purposes
  private class TestMethodCompilerPass extends MethodCompilerPass {
    
    public TestMethodCompilerPass(AbstractCompiler compiler) {
      super(compiler);
    }
    
    @Override
    Callback getActingCallback() {
      return new TestCallback();
    }
    
    @Override
    SignatureStore getSignatureStore() {
      return new TestSignatureStore();
    }
  }
  
  // create a test implementation of Callback for testing purposes
  private class TestCallback implements Callback {
    
    @Override
    public boolean shouldTraverse(NodeTraversal traversal, Node node, Node parent) {
      return true;
    }
    
    @Override
    public void visit(NodeTraversal traversal, Node node, Node parent) {
      // do nothing
    }
  }
  
  // create a test implementation of SignatureStore for testing purposes
  private class TestSignatureStore implements SignatureStore {
    
    public HashMap<String, Node> signatures = new HashMap<String, Node>();
    
    @Override
    public void addSignature(String functionName, Node functionNode, String sourceFile) {
      signatures.put(functionName, functionNode);
    }
    
    @Override
    public void removeSignature(String functionName) {
      signatures.remove(functionName);
    }
    
    @Override
    public void reset() {
      signatures.clear();
    }
  }
}
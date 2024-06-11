import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class MethodCompilerPassTest {
  
  private MethodCompilerPass methodCompilerPass;
  
  @Before
  public void setUp() {
    methodCompilerPass = new MethodCompilerPass() {
      @Override
      Callback getActingCallback() {
        return null;
      }
      
      @Override
      SignatureStore getSignatureStore() {
        return null;
      }
    };
  }
  
  @Test
  public void testProcess() {
    // Test case 1
    Node externs1 = null;
    Node root1 = null;
    methodCompilerPass.process(externs1, root1);
    // TODO: Add assertions
    
    // Test case 2
    Node externs2 = null;
    Node root2 = null;
    methodCompilerPass.process(externs2, root2);
    // TODO: Add assertions
    
    // Test case 3
    Node externs3 = null;
    Node root3 = null;
    methodCompilerPass.process(externs3, root3);
    // TODO: Add assertions
  }
  
  @Test
  public void testGetActingCallback() {
    Callback actingCallback = methodCompilerPass.getActingCallback();
    // TODO: Add assertions
  }
  
  @Test
  public void testGetSignatureStore() {
    SignatureStore signatureStore = methodCompilerPass.getSignatureStore();
    // TODO: Add assertions
  }
  
  @Test
  public void testAddPossibleSignature() {
    String name = "";
    Node node = null;
    NodeTraversal t = null;
    methodCompilerPass.addPossibleSignature(name, node, t);
    // TODO: Add assertions
  }
  
  @Test
  public void testAddSignature() {
    String name = "";
    Node function = null;
    String fnSourceName = "";
    methodCompilerPass.addSignature(name, function, fnSourceName);
    // TODO: Add assertions
  }
  
  @Test
  public void testGetExternMethods() {
    NodeTraversal t = null;
    Node n = null;
    Node parent = null;
    MethodCompilerPass.GetExternMethods getExternMethods = methodCompilerPass.new GetExternMethods();
    getExternMethods.visit(t, n, parent);
    // TODO: Add assertions
  }
  
  @Test
  public void testGatherSignatures() {
    NodeTraversal t = null;
    Node n = null;
    Node parent = null;
    MethodCompilerPass.GatherSignatures gatherSignatures = methodCompilerPass.new GatherSignatures();
    gatherSignatures.visit(t, n, parent);
    // TODO: Add assertions
  }
  
}
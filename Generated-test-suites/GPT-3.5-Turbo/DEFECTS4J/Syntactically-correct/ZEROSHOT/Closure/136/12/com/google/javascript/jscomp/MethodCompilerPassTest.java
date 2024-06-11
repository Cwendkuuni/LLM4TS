import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.Token;

public class MethodCompilerPassTest {
  
  private MethodCompilerPass methodCompilerPass;
  
  @Before
  public void setUp() {
    methodCompilerPass = new MethodCompilerPass(null) {
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
    Node externs = new Node(Token.EXTERN);
    Node root = new Node(Token.ROOT);
    
    methodCompilerPass.process(externs, root);
    
    // Assert the processed results
    // Add your assertions here
    
    //assertEquals(expectedResult, actualResult);
  }
  
  @Test
  public void testGetExternMethods() {
    NodeTraversal t = null;
    Node n = new Node(Token.SOMETHING);
    Node parent = new Node(Token.PARENT);
    
    methodCompilerPass.new GetExternMethods().visit(t, n, parent);
    
    // Assert the processed results
    // Add your assertions here
    
    //assertEquals(expectedResult, actualResult);
  }
  
  @Test
  public void testGatherSignatures() {
    NodeTraversal t = null;
    Node n = new Node(Token.SOMETHING);
    Node parent = new Node(Token.PARENT);
    
    methodCompilerPass.new GatherSignatures().visit(t, n, parent);
    
    // Assert the processed results
    // Add your assertions here
    
    //assertEquals(expectedResult, actualResult);
  }
}
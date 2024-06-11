import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class CheckGlobalThisTest {
  
  private CheckGlobalThis checkGlobalThis;
  
  @Before
  public void setUp() {
    checkGlobalThis = new CheckGlobalThis(new AbstractCompiler());
  }
  
  @Test
  public void testShouldReportThis() {
    Node n = new Node(Token.THIS);
    boolean result = checkGlobalThis.shouldReportThis(n);
    assertTrue(result);
    
    Node parent = new Node(Token.GETPROP);
    parent.addChildToFront(n);
    boolean result2 = checkGlobalThis.shouldReportThis(parent);
    assertTrue(result2);
  }
  
  @Test
  public void testGetFunctionJsDocInfo() {
    Node n = new Node(Token.FUNCTION);
    JSDocInfo jsDoc = checkGlobalThis.getFunctionJsDocInfo(n);
    assertNull(jsDoc);
    
    Node parent = new Node(Token.NAME);
    parent.addChildToFront(n);
    JSDocInfo jsDoc2 = checkGlobalThis.getFunctionJsDocInfo(n);
    assertNull(jsDoc2);
    
    JSDocInfo jsDoc3 = checkGlobalThis.getFunctionJsDocInfo(parent);
    assertNull(jsDoc3);
  }
  
  // Add more test methods here
  
}
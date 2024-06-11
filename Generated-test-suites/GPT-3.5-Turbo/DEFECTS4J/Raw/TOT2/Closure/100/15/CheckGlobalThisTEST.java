import static org.junit.Assert.*;
import org.junit.Test;

public class CheckGlobalThisTest {

  @Test
  public void testShouldTraverse() {
    // Test the shouldTraverse method
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis();
    boolean result = checkGlobalThis.shouldTraverse(new NodeTraversal(), new Node(), new Node());
    
    assertEquals(true, result);
  }

  @Test
  public void testGetFunctionJsDocInfo() {
    // Test the getFunctionJsDocInfo method
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis();
    Node node = new Node(Token.FUNCTION);
    JSDocInfo result = checkGlobalThis.getFunctionJsDocInfo(node);
    
    assertNull(result);
  }

  @Test
  public void testShouldReportThis() {
    // Test the shouldReportThis method
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis();
    boolean result = checkGlobalThis.shouldReportThis(new Node(Token.THIS), new Node(Token.ASSIGN));
    
    assertEquals(false, result);
  }

}
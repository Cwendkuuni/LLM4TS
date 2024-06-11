
import org.junit.Test;
import static org.junit.Assert.*;

public class CheckGlobalThisTest {

  @Test
  public void testShouldReportThis() {
    Node node = new Node(Token.THIS);
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null);
    boolean result = checkGlobalThis.shouldReportThis(node);
    assertEquals(true, result);
  }
  
  @Test
  public void testShouldNotReportThis() {
    Node node = new Node(Token.THIS);
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null);
    checkGlobalThis.assignLhsChild = new Node(Token.ASSIGN);
    boolean result = checkGlobalThis.shouldReportThis(node);
    assertEquals(false, result);
  }
  
  @Test
  public void testVisitThisNodeShouldReportThis() {
    Node node = new Node(Token.THIS);
    NodeTraversal t = new NodeTraversal(null);
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null);
    checkGlobalThis.assignLhsChild = new Node(Token.ASSIGN);
    checkGlobalThis.visit(t, node, null);
    // assert that error is reported
  }
  
  @Test
  public void testVisitThisNodeShouldNotReportThis() {
    Node node = new Node(Token.THIS);
    NodeTraversal t = new NodeTraversal(null);
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null);
    checkGlobalThis.assignLhsChild = null;
    checkGlobalThis.visit(t, node, null);
    // assert that no error is reported
  }

  @Test
  public void testGetFunctionJsDocInfo() {
    Node node = new Node(Token.FUNCTION);
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null);
    checkGlobalThis.getFunctionJsDocInfo(node);
    // assert the logic and expected results of this method
  }

}

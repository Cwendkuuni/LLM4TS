import static org.junit.Assert.*;
import org.junit.Test;

public class CheckGlobalThisTest {

  @Test
  public void testShouldReportThis() {
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null);
    Node node = new Node(Token.THIS);
    assertTrue(checkGlobalThis.shouldReportThis(node));
  }

  @Test
  public void testShouldNotReportThis() {
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null);
    Node node = new Node(Token.THIS);
    node.addChildToBack(new Node(Token.GETPROP));
    assertFalse(checkGlobalThis.shouldReportThis(node));
  }

  @Test
  public void testShouldTraverse() {
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null);
    NodeTraversal t = new NodeTraversal();
    Node node = new Node(Token.FUNCTION);
    Node parent = new Node(Token.BLOCK);
    assertTrue(checkGlobalThis.shouldTraverse(t, node, parent));
  }

  @Test
  public void testShouldNotTraverse() {
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null);
    NodeTraversal t = new NodeTraversal();
    Node node = new Node(Token.FUNCTION);
    Node parent = new Node(Token.ASSIGN);
    assertFalse(checkGlobalThis.shouldTraverse(t, node, parent));
  }

  @Test
  public void testGetFunctionJsDocInfo() {
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null);
    Node node = new Node(Token.FUNCTION);
    JSDocInfo jsDoc = checkGlobalThis.getFunctionJsDocInfo(node);
    assertNull(jsDoc);
  }
}
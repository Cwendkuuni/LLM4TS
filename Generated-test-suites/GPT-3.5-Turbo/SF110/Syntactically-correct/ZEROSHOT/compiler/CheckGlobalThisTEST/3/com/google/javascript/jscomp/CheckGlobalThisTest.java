import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class CheckGlobalThisTest {

  private CheckGlobalThis checkGlobalThis;

  @Before
  public void setUp() {
    checkGlobalThis = new CheckGlobalThis();
  }

  @Test
  public void testShouldReportThis_whenThisOnLeftSideOfAssign_returnsTrue() {
    Node n = new Node(Token.THIS);
    boolean result = checkGlobalThis.shouldReportThis(n);
    assertTrue(result);
  }

  @Test
  public void testShouldReportThis_whenThisWithPropertyAccess_returnsTrue() {
    Node parent = new Node(Token.GETPROP);
    Node n = new Node(Token.THIS);
    parent.addChildToBack(n);

    boolean result = checkGlobalThis.shouldReportThis(n);
    assertTrue(result);
  }

  @Test
  public void testShouldReportThis_whenThisWithPropertyAccessOnLeftSideOfAssign_returnsFalse() {
    Node parent = new Node(Token.GETPROP);
    Node assign = new Node(Token.ASSIGN);

    Node lhs = new Node(Token.THIS);
    parent.addChildToBack(lhs);

    assign.addChildToBack(lhs);

    boolean result = checkGlobalThis.shouldReportThis(lhs);
    assertFalse(result);
  }

  @Test
  public void testVisit_whenThisAndShouldReportThis_returnsError() {
    Node n = new Node(Token.THIS);
    Node parent = new Node(Token.GETPROP);
    parent.addChildToBack(n);
    NodeTraversal t = new NodeTraversal();

    checkGlobalThis.visit(t, n, parent);

    assertEquals(GLOBAL_THIS, t.getError().getType());
  }

  @Test
  public void testVisit_whenThisAndNotShouldReportThis_doesNotReportError() {
    Node n = new Node(Token.THIS);
    Node parent = new Node(Token.GETPROP);
    parent.addChildToBack(n);
    NodeTraversal t = new NodeTraversal();

    checkGlobalThis.visit(t, n, null);

    assertNull(t.getError());
  }
}
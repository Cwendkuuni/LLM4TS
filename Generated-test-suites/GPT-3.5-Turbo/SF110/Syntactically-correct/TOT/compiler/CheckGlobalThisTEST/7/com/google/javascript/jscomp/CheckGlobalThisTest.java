import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

public class CheckGlobalThisTest {

  private CheckGlobalThis checkGlobalThis;

  @Before
  public void setUp() {
    checkGlobalThis = new CheckGlobalThis(null);
  }

  @Test
  public void shouldReportGlobalThisWhenUsedOnTheLeftSideOfAssignment() {
    Node n = new Node(Token.THIS);
    checkGlobalThis.assignLhsChild = new Node(Token.NAME);
    boolean result = checkGlobalThis.shouldReportThis(n);
    assertEquals(true, result);
  }

  @Test
  public void shouldReportGlobalThisWhenUsedWithPropertyAccess() {
    Node n = new Node(Token.THIS);
    Node parent = new Node(Token.GETPROP);
    parent.addChildToFront(new Node(Token.NAME, "obj"));
    parent.addChildToBack(new Node(Token.STRING, "property"));
    n.setParent(parent);
    boolean result = checkGlobalThis.shouldReportThis(n);
    assertEquals(true, result);
  }

  @Test
  public void shouldNotReportGlobalThisWhenUsedOnTheRightSideOfAssignment() {
    Node n = new Node(Token.THIS);
    checkGlobalThis.assignLhsChild = null;
    boolean result = checkGlobalThis.shouldReportThis(n);
    assertEquals(false, result);
  }

  @Test
  public void shouldNotReportGlobalThisWhenUsedWithoutPropertyAccess() {
    Node n = new Node(Token.THIS);
    Node parent = new Node(Token.VAR);
    parent.addChildToFront(new Node(Token.NAME, "var"));
    parent.addChildToBack(new Node(Token.EMPTY));
    n.setParent(parent);
    boolean result = checkGlobalThis.shouldReportThis(n);
    assertEquals(false, result);
  }

  @Test
  public void shouldCreateErrorWhenEncounteringGlobalThis() {
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null);
    NodeTraversal t = new NodeTraversal(null, null);
    Node n = new Node(Token.THIS);
    DiagnosticType expectedError = DiagnosticType.warning(
        "JSC_USED_GLOBAL_THIS",
        "dangerous use of the global 'this' object");
    
    checkGlobalThis.visit(t, n, null);
    
    List<DiagnosticType> errors = t.getErrors();
    assertEquals(1, errors.size());
    assertEquals(expectedError, errors.get(0));
  }
}
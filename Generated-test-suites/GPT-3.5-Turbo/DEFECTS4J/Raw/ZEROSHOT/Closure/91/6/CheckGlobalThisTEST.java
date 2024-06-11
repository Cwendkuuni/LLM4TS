import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class CheckGlobalThisTest {

  private CheckGlobalThis checkGlobalThis;

  @Before
  public void setUp() {
    checkGlobalThis = new CheckGlobalThis(new AbstractCompiler(), CheckLevel.WARNING);
  }

  @Test
  public void shouldTraverse_shouldReturnTrueForFunctionsNotAnnotated() {
    NodeTraversal t = new NodeTraversal();
    Node n = new Node(Token.FUNCTION);
    Node parent = new Node(Token.SCRIPT);
    boolean result = checkGlobalThis.shouldTraverse(t, n, parent);
    assertTrue(result);
  }

  @Test
  public void shouldTraverse_shouldReturnFalseForConstructorAnnotatedFunction() {
    NodeTraversal t = new NodeTraversal();
    Node n = new Node(Token.FUNCTION);
    JSDocInfo jsDoc = new JSDocInfo();
    jsDoc.setConstructor(true);
    n.setJSDocInfo(jsDoc);
    boolean result = checkGlobalThis.shouldTraverse(t, n, null);
    assertFalse(result);
  }

  @Test
  public void shouldTraverse_shouldReturnFalseForInterfaceAnnotatedFunction() {
    NodeTraversal t = new NodeTraversal();
    Node n = new Node(Token.FUNCTION);
    JSDocInfo jsDoc = new JSDocInfo();
    jsDoc.setInterface(true);
    n.setJSDocInfo(jsDoc);
    boolean result = checkGlobalThis.shouldTraverse(t, n, null);
    assertFalse(result);
  }

  @Test
  public void shouldTraverse_shouldReturnFalseForThisTypeAnnotatedFunction() {
    NodeTraversal t = new NodeTraversal();
    Node n = new Node(Token.FUNCTION);
    JSDocInfo jsDoc = new JSDocInfo();
    jsDoc.recordType(new Node(Token.THIS));
    n.setJSDocInfo(jsDoc);
    boolean result = checkGlobalThis.shouldTraverse(t, n, null);
    assertFalse(result);
  }

  @Test
  public void shouldTraverse_shouldReturnFalseForOverrideAnnotatedFunction() {
    NodeTraversal t = new NodeTraversal();
    Node n = new Node(Token.FUNCTION);
    JSDocInfo jsDoc = new JSDocInfo();
    jsDoc.setOverride(true);
    n.setJSDocInfo(jsDoc);
    boolean result = checkGlobalThis.shouldTraverse(t, n, null);
    assertFalse(result);
  }

  @Test
  public void shouldTraverse_shouldReturnFalseForNonGlobalContexts() {
    NodeTraversal t = new NodeTraversal();
    Node n = new Node(Token.FUNCTION);
    Node parent = new Node(Token.ADD);
    boolean result = checkGlobalThis.shouldTraverse(t, n, parent);
    assertFalse(result);
  }

  @Test
  public void visit_shouldReportErrorForGlobalThisOnLeftSideOfAssign() {
    NodeTraversal t = new NodeTraversal();
    Node n = new Node(Token.THIS);
    Node parent = new Node(Token.ASSIGN);
    boolean result = checkGlobalThis.shouldReportThis(n, parent);
    assertTrue(result);
  }

  @Test
  public void visit_shouldReportErrorForGlobalThisWithPropertyAccess() {
    NodeTraversal t = new NodeTraversal();
    Node n = new Node(Token.THIS);
    Node parent = new Node(Token.GETPROP);
    boolean result = checkGlobalThis.shouldReportThis(n, parent);
    assertTrue(result);
  }

  @Test
  public void visit_shouldNotReportErrorForGlobalThisWithoutPropertyAccess() {
    NodeTraversal t = new NodeTraversal();
    Node n = new Node(Token.THIS);
    Node parent = new Node(Token.BLOCK);
    boolean result = checkGlobalThis.shouldReportThis(n, parent);
    assertFalse(result);
  }
}
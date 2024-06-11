import static org.junit.Assert.assertEquals;
import org.junit.Before;
import org.junit.Test;

public class CheckGlobalThisTest {

  private CheckGlobalThis checkGlobalThis;

  @Before
  public void setUp() {
    checkGlobalThis = new CheckGlobalThis(new AbstractCompiler());
  }

  @Test
  public void testShouldReportThis() {
    Node node = new Node(Token.THIS);
    boolean result = checkGlobalThis.shouldReportThis(node);
    assertTrue(result);
  }

  @Test
  public void testGetFunctionJsDocInfo() {
    Node node = new Node(Token.FUNCTION);
    JSDocInfo jsDocInfo = checkGlobalThis.getFunctionJsDocInfo(node);
    assertNull(jsDocInfo);
  }

  @Test
  public void testTraverseFunction() {
    NodeTraversal t = new NodeTraversal();
    Node node = new Node(Token.FUNCTION);
    boolean result = checkGlobalThis.shouldTraverse(t, node, null);
    assertTrue(result);
  }

  @Test
  public void testTraverseNonFunction() {
    NodeTraversal t = new NodeTraversal();
    Node node = new Node(Token.SCRIPT);
    boolean result = checkGlobalThis.shouldTraverse(t, node, null);
    assertFalse(result);
  }

  @Test
  public void testVisitThisNode() {
    NodeTraversal t = new NodeTraversal();
    Node node = new Node(Token.THIS);
    Node parent = new Node(Token.GETPROP, new Node(Token.THIS), new Node(Token.STRING, "property"));
    checkGlobalThis.visit(t, node, parent);
    assertEquals(1, t.getErrors().size());
  }

}
import com.google.javascript.rhino.Node;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class CheckGlobalThisTest {

  private CheckGlobalThis checkGlobalThis;

  @Before
  public void setUp() {
    checkGlobalThis = new CheckGlobalThis(new AbstractCompiler());
  }

  @After
  public void tearDown() {
    checkGlobalThis = null;
  }

  @Test
  public void testShouldTraverse_FunctionConstructorWithThisAnnotation_ReturnsFalse() {
    NodeTraversal t = null;
    Node n = new Node();
    Node parent = new Node();
    boolean result = checkGlobalThis.shouldTraverse(t, n, parent);
    assertFalse(result);
  }

  @Test
  public void testShouldTraverse_FunctionConstructorWithOverrideAnnotation_ReturnsFalse() {
    NodeTraversal t = null;
    Node n = new Node();
    Node parent = new Node();
    boolean result = checkGlobalThis.shouldTraverse(t, n, parent);
    assertFalse(result);
  }

  @Test
  public void testShouldTraverse_FunctionConstructorWithJsDocInfo_ReturnsFalse() {
    NodeTraversal t = null;
    Node n = new Node();
    Node parent = new Node();
    boolean result = checkGlobalThis.shouldTraverse(t, n, parent);
    assertFalse(result);
  }

  @Test
  public void testShouldTraverse_NonFunctionParent_ReturnsFalse() {
    NodeTraversal t = null;
    Node n = new Node();
    Node parent = new Node();
    boolean result = checkGlobalThis.shouldTraverse(t, n, parent);
    assertFalse(result);
  }

  @Test
  public void testShouldTraverse_FunctionLentToPrototype_ReturnsFalse() {
    NodeTraversal t = null;
    Node n = new Node();
    Node parent = new Node();
    boolean result = checkGlobalThis.shouldTraverse(t, n, parent);
    assertFalse(result);
  }

  @Test
  public void testVisit_ThisShouldBeReported_ReturnsTrue() {
    NodeTraversal t = null;
    Node n = new Node();
    Node parent = new Node();
    boolean result = checkGlobalThis.shouldReportThis(n);
    assertTrue(result);
  }

  @Test
  public void testVisit_ThisShouldNotBeReported_ReturnsFalse() {
    NodeTraversal t = null;
    Node n = new Node();
    Node parent = new Node();
    boolean result = checkGlobalThis.shouldReportThis(n);
    assertFalse(result);
  }

  @Test
  public void testGetFunctionJsDocInfo_FunctionWithJSDoc_ReturnsJSDocInfo() {
    Node n = new Node();
    JSDocInfo result = checkGlobalThis.getFunctionJsDocInfo(n);
    assertNotNull(result);
  }

  @Test
  public void testGetFunctionJsDocInfo_FunctionWithoutJSDoc_ReturnsNull() {
    Node n = new Node();
    JSDocInfo result = checkGlobalThis.getFunctionJsDocInfo(n);
    assertNull(result);
  }
}
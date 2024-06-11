import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.rhino.JSDocInfo;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.Token;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class CheckGlobalThisTest {

  private AbstractCompiler compiler;
  private CheckGlobalThis checkGlobalThis;

  @Before
  public void setUp() {
    compiler = new AbstractCompiler();
    checkGlobalThis = new CheckGlobalThis(compiler, CheckLevel.WARNING);
  }

  @After
  public void tearDown() {
    compiler = null;
    checkGlobalThis = null;
  }

  @Test
  public void shouldTraverse_shouldReturnFalseForConstructorFunction() {
    NodeTraversal t = new NodeTraversal(compiler, checkGlobalThis);
    Node n = new Node(Token.FUNCTION);
    JSDocInfo jsDocInfo = new JSDocInfo();
    jsDocInfo.setConstructor(true);
    n.setJSDocInfo(jsDocInfo);
    boolean result = checkGlobalThis.shouldTraverse(t, n, null);
    assertFalse(result);
  }

  @Test
  public void shouldTraverse_shouldReturnFalseForFunctionWithThisTypeAnnotation() {
    NodeTraversal t = new NodeTraversal(compiler, checkGlobalThis);
    Node n = new Node(Token.FUNCTION);
    JSDocInfo jsDocInfo = new JSDocInfo();
    jsDocInfo.recordThisType();
    n.setJSDocInfo(jsDocInfo);
    boolean result = checkGlobalThis.shouldTraverse(t, n, null);
    assertFalse(result);
  }

  @Test
  public void shouldTraverse_shouldReturnTrueForRegularFunction() {
    NodeTraversal t = new NodeTraversal(compiler, checkGlobalThis);
    Node n = new Node(Token.FUNCTION);
    boolean result = checkGlobalThis.shouldTraverse(t, n, null);
    assertTrue(result);
  }

  @Test
  public void shouldTraverse_shouldReturnTrueForAssignmentWithThisOnLeftSide() {
    NodeTraversal t = new NodeTraversal(compiler, checkGlobalThis);
    Node n = new Node(Token.NAME);
    Node parent = new Node(Token.ASSIGN);
    parent.addChildToBack(n);
    boolean result = checkGlobalThis.shouldTraverse(t, n, parent);
    assertTrue(result);
  }

  @Test
  public void shouldTraverse_shouldReturnFalseForPrototypeAssignment() {
    NodeTraversal t = new NodeTraversal(compiler, checkGlobalThis);
    Node n = new Node(Token.GETPROP);
    Node lhs = new Node(Token.NAME);
    lhs.setString("prototype");
    n.addChildToBack(lhs);
    boolean result = checkGlobalThis.shouldTraverse(t, n, null);
    assertFalse(result);
  }

  @Test
  public void shouldTraverse_shouldReturnFalseForNestedPrototypeAssignment() {
    NodeTraversal t = new NodeTraversal(compiler, checkGlobalThis);
    Node n = new Node(Token.GETPROP);
    Node lhs = new Node(Token.NAME);
    lhs.setString("prototype");
    n.addChildToBack(lhs);
    lhs = new Node(Token.NAME);
    lhs.setString("property");
    n.addChildToBack(lhs);
    boolean result = checkGlobalThis.shouldTraverse(t, n, null);
    assertFalse(result);
  }

  @Test
  public void visit_shouldReportGlobalThisWithNoParent() {
    NodeTraversal t = new NodeTraversal(compiler, checkGlobalThis);
    Node n = new Node(Token.THIS);
    checkGlobalThis.visit(t, n, null);
    assertEquals(1, compiler.getErrorCount());
    assertEquals("dangerous use of the global 'this' object", compiler.getErrors()[0].getType().key);
  }

  @Test
  public void visit_shouldNotReportGlobalThisWithParent() {
    NodeTraversal t = new NodeTraversal(compiler, checkGlobalThis);
    Node n = new Node(Token.THIS);
    Node parent = new Node(Token.NAME);
    checkGlobalThis.visit(t, n, parent);
    assertEquals(0, compiler.getErrorCount());
  }

}
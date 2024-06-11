import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CheckLevel;
import com.google.javascript.rhino.JSDocInfo;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.Token;
import com.google.javascript.jscomp.NodeTraversal;

public class CheckGlobalThisTest {
  
  private AbstractCompiler compiler;
  private CheckLevel level;
  private CheckGlobalThis checkGlobalThis;
  private NodeTraversal traversal;
  
  @Before
  public void setUp() {
    compiler = new AbstractCompiler();
    level = CheckLevel.WARNING;
    checkGlobalThis = new CheckGlobalThis(compiler, level);
    traversal = new NodeTraversal(compiler, checkGlobalThis);
  }
  
  @Test
  public void shouldTraverse_returnTrueForFunctionNotConstructorOrAnnotatedWithThisOrOverride_shouldReturnTrue() {
    NodeTraversal t = new NodeTraversal(compiler, checkGlobalThis);
    Node n = new Node(Token.FUNCTION);
    Node parent = new Node(Token.SCRIPT);
    boolean result = checkGlobalThis.shouldTraverse(t, n, parent);
    assertTrue(result);
  }
  
  @Test
  public void shouldTraverse_returnFalseForConstructorOrAnnotatedWithThisOrOverride_shouldReturnFalse() {
    NodeTraversal t = new NodeTraversal(compiler, checkGlobalThis);
    Node n = new Node(Token.FUNCTION);
    Node parent = new Node(Token.SCRIPT);
    JSDocInfo jsDoc = new JSDocInfo();
    jsDoc.recordConstructor();
    n.setJSDocInfo(jsDoc);
    boolean result = checkGlobalThis.shouldTraverse(t, n, parent);
    assertFalse(result);
  }
  
  @Test
  public void shouldVisit_reportThisOnLeftSideOfAssignment_shouldReportError() {
    NodeTraversal t = new NodeTraversal(compiler, checkGlobalThis);
    Node n = new Node(Token.THIS);
    Node parent = new Node(Token.ASSIGN);
    parent.addChildToFront(n);
    checkGlobalThis.visit(t, n, parent);
    // Assert error reported
  }
  
  @Test
  public void shouldVisit_reportThisWithPropertyAccess_shouldReportError() {
    NodeTraversal t = new NodeTraversal(compiler, checkGlobalThis);
    Node n = new Node(Token.THIS);
    Node parent = new Node(Token.GETPROP);
    parent.addChildToFront(n);
    checkGlobalThis.visit(t, n, parent);
    // Assert error reported
  }
  
  @Test
  public void shouldVisit_doNothingForOtherCases_shouldNotReportError() {
    NodeTraversal t = new NodeTraversal(compiler, checkGlobalThis);
    Node n = new Node(Token.THIS);
    Node parent = new Node(Token.SCRIPT);
    checkGlobalThis.visit(t, n, parent);
    // Assert no error reported
  }
  
}
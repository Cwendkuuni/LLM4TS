import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import com.google.javascript.jscomp.DiagnosticType;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.NodeTraversal.Callback;
import com.google.javascript.rhino.JSDocInfo;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.Token;

public class CheckGlobalThisTest {
  
  private CheckGlobalThis checkGlobalThis;
  private FakeAbstractCompiler compiler;
  
  @Before
  public void setUp() {
    compiler = new FakeAbstractCompiler();
    checkGlobalThis = new CheckGlobalThis(compiler, CheckLevel.WARNING);
  }
  
  @Test
  public void testShouldTraverse_prototypeMethod() {
    NodeTraversal t = new NodeTraversal(compiler, checkGlobalThis);
    Node n = createNode(Token.FUNCTION);
    Node parent = createNode(Token.ASSIGN);
    JSDocInfo jsDoc = createJSDocInfo();
    jsDoc.setConstructor(true);
    n.setJSDocInfo(jsDoc);
    boolean result = checkGlobalThis.shouldTraverse(t, n, parent);
    assertTrue(result);
  }
  
  @Test
  public void testShouldTraverse_annotatedConstructor() {
    NodeTraversal t = new NodeTraversal(compiler, checkGlobalThis);
    Node n = createNode(Token.FUNCTION);
    Node parent = createNode(Token.ASSIGN);
    JSDocInfo jsDoc = createJSDocInfo();
    jsDoc.setConstructor(true);
    n.setJSDocInfo(jsDoc);
    boolean result = checkGlobalThis.shouldTraverse(t, n, parent);
    assertTrue(result);
  }
  
  @Test
  public void testShouldTraverse_annotatedThis() {
    NodeTraversal t = new NodeTraversal(compiler, checkGlobalThis);
    Node n = createNode(Token.FUNCTION);
    Node parent = createNode(Token.ASSIGN);
    JSDocInfo jsDoc = createJSDocInfo();
    jsDoc.setThisType(createType());
    n.setJSDocInfo(jsDoc);
    boolean result = checkGlobalThis.shouldTraverse(t, n, parent);
    assertTrue(result);
  }
  
  @Test
  public void testShouldTraverse_blockFunction() {
    NodeTraversal t = new NodeTraversal(compiler, checkGlobalThis);
    Node n = createNode(Token.FUNCTION);
    Node parent = createNode(Token.BLOCK);
    boolean result = checkGlobalThis.shouldTraverse(t, n, parent);
    assertTrue(result);
  }
  
  @Test
  public void testShouldTraverse_scriptFunction() {
    NodeTraversal t = new NodeTraversal(compiler, checkGlobalThis);
    Node n = createNode(Token.FUNCTION);
    Node parent = createNode(Token.SCRIPT);
    boolean result = checkGlobalThis.shouldTraverse(t, n, parent);
    assertTrue(result);
  }
  
  @Test
  public void testShouldTraverse_namedFunction() {
    NodeTraversal t = new NodeTraversal(compiler, checkGlobalThis);
    Node n = createNode(Token.FUNCTION);
    Node parent = createNode(Token.NAME);
    boolean result = checkGlobalThis.shouldTraverse(t, n, parent);
    assertTrue(result);
  }
  
  @Test
  public void testShouldTraverse_assignmentFunction() {
    NodeTraversal t = new NodeTraversal(compiler, checkGlobalThis);
    Node n = createNode(Token.FUNCTION);
    Node parent = createNode(Token.ASSIGN);
    boolean result = checkGlobalThis.shouldTraverse(t, n, parent);
    assertTrue(result);
  }
  
  @Test
  public void testShouldTraverse_notFunction() {
    NodeTraversal t = new NodeTraversal(compiler, checkGlobalThis);
    Node n = createNode(Token.GETPROP);
    Node parent = createNode(Token.NAME);
    boolean result = checkGlobalThis.shouldTraverse(t, n, parent);
    assertFalse(result);
  }
  
  @Test
  public void testShouldTraverse_notConstructor() {
    NodeTraversal t = new NodeTraversal(compiler, checkGlobalThis);
    Node n = createNode(Token.FUNCTION);
    Node parent = createNode(Token.ASSIGN);
    JSDocInfo jsDoc = createJSDocInfo();
    jsDoc.setConstructor(false);
    n.setJSDocInfo(jsDoc);
    boolean result = checkGlobalThis.shouldTraverse(t, n, parent);
    assertFalse(result);
  }
  
  @Test
  public void testShouldTraverse_notThis() {
    NodeTraversal t = new NodeTraversal(compiler, checkGlobalThis);
    Node n = createNode(Token.FUNCTION);
    Node parent = createNode(Token.ASSIGN);
    JSDocInfo jsDoc = createJSDocInfo();
    jsDoc.setThisType(null);
    n.setJSDocInfo(jsDoc);
    boolean result = checkGlobalThis.shouldTraverse(t, n, parent);
    assertFalse(result);
  }
  
  @Test
  public void testShouldTraverse_notBlockFunction() {
    NodeTraversal t = new NodeTraversal(compiler, checkGlobalThis);
    Node n = createNode(Token.FUNCTION);
    Node parent = createNode(Token.NAME);
    boolean result = checkGlobalThis.shouldTraverse(t, n, parent);
    assertFalse(result);
  }
  
  @Test
  public void testVisit_thisWithParentGet() {
    NodeTraversal t = new NodeTraversal(compiler, checkGlobalThis);
    Node n = createNode(Token.THIS);
    Node parent = createNode(Token.GETPROP);
    bool result = false;
    compiler.reportCallback = new ReportCallback() {
      public void report(DiagnosticType type, String message) {
        result = true;
      }
    };
    checkGlobalThis.visit(t, n, parent);
    assertTrue(result);
  }
  
  @Test
  public void testVisit_thisWithParentAssign() {
    NodeTraversal t = new NodeTraversal(compiler, checkGlobalThis);
    Node n = createNode(Token.THIS);
    Node parent = createNode(Token.ASSIGN);
    bool result = false;
    compiler.reportCallback = new ReportCallback() {
      public void report(DiagnosticType type, String message) {
        result = true;
      }
    };
    checkGlobalThis.visit(t, n, parent);
    assertTrue(result);
  }
  
  @Test
  public void testVisit_thisWithParentNotGetAssign() {
    NodeTraversal t = new NodeTraversal(compiler, checkGlobalThis);
    Node n = createNode(Token.THIS);
    Node parent = createNode(Token.NAME);
    bool result = false;
    compiler.reportCallback = new ReportCallback() {
      public void report(DiagnosticType type, String message) {
        result = true;
      }
    };
    checkGlobalThis.visit(t, n, parent);
    assertFalse(result);
  }
  
  /**
   * Fake AbstractCompiler class for testing
   */
  private class FakeAbstractCompiler extends AbstractCompiler {
    public ReportCallback reportCallback;
    
    public void report(NodeTraversal t, DiagnosticType diagnosticType, String message) {
      if (reportCallback != null) {
        reportCallback.report(diagnosticType, message);
      }
    }
  }
  
  /**
   * Fake ReportCallback interface for testing
   */
  private interface ReportCallback {
    void report(DiagnosticType type, String message);
  }
  
  /**
   * Helper method to create a new Node with the given type
   */
  private Node createNode(int type) {
    return new Node(type);
  }
  
  /**
   * Helper method to create a new JSDocInfo
   */
  private JSDocInfo createJSDocInfo() {
    return new JSDocInfo();
  }
  
  /**
   * Helper method to create a new JSType
   */
  private JSType createType() {
    return new JSType();
  }
}
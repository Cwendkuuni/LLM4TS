import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.DiagnosticType;
import com.google.javascript.rhino.JSDocInfo;
import com.google.javascript.rhino.Node;

public class CheckGlobalThisTest {

  private CheckGlobalThis checkGlobalThis;
  private AbstractCompiler compiler;

  @Before
  public void setUp() {
    compiler = new AbstractCompiler();
    checkGlobalThis = new CheckGlobalThis(compiler);
  }

  @Test
  public void testShouldReportThis_assignLhsChildNotNull() {
    Node n = new Node(Token.THIS);
    checkGlobalThis.assignLhsChild = new Node(Token.NAME);
    boolean result = checkGlobalThis.shouldReportThis(n);
    assertTrue(result);
  }

  @Test
  public void testShouldReportThis_parentIsNull() {
    Node n = new Node(Token.THIS);
    boolean result = checkGlobalThis.shouldReportThis(n);
    assertFalse(result);
  }

  @Test
  public void testShouldReportThis_getParentIsNotGet() {
    Node n = new Node(Token.THIS);
    Node parent = new Node(Token.NAME);
    n.setParent(parent);
    boolean result = checkGlobalThis.shouldReportThis(n);
    assertFalse(result);
  }

  @Test
  public void testShouldReportThis_getParentIsGet() {
    Node n = new Node(Token.THIS);
    Node parent = new Node(Token.GETPROP);
    n.setParent(parent);
    boolean result = checkGlobalThis.shouldReportThis(n);
    assertTrue(result);
  }

  @Test
  public void testVisit_thisShouldBeReported() {
    Node n = new Node(Token.THIS);
    Node parent = new Node(Token.NAME);
    n.setParent(parent);
    NodeTraversal t = new NodeTraversal(compiler);
    checkGlobalThis.visit(t, n, parent);
    DiagnosticType warning = new DiagnosticType("JSC_USED_GLOBAL_THIS", "dangerous use of the global 'this' object");
    assertTrue(compiler.hasReportedWarning(warning));
  }

  @Test
  public void testVisit_assignLhsChildIsNotNull() {
    Node n = new Node(Token.THIS);
    Node assignLhsChild = new Node(Token.NAME);
    checkGlobalThis.assignLhsChild = assignLhsChild;
    NodeTraversal t = new NodeTraversal(compiler);
    checkGlobalThis.visit(t, n, null);
    assertNull(checkGlobalThis.assignLhsChild);
  }

  @Test
  public void testGetFunctionJsDocInfo_jsDocNotNull() {
    Node n = new Node(Token.FUNCTION);
    JSDocInfo jsDoc = new JSDocInfo();
    n.setJSDocInfo(jsDoc);
    JSDocInfo result = checkGlobalThis.getFunctionJsDocInfo(n);
    assertEquals(jsDoc, result);
  }

  @Test
  public void testGetFunctionJsDocInfo_parentTypeNotNameOrAssign() {
    Node n = new Node(Token.FUNCTION);
    Node parent = new Node(Token.GETPROP);
    n.setParent(parent);
    JSDocInfo result = checkGlobalThis.getFunctionJsDocInfo(n);
    assertNull(result);
  }

  @Test
  public void testGetFunctionJsDocInfo_jsDocNull_parentTypeIsName_noJsDocOnName() {
    Node n = new Node(Token.FUNCTION);
    Node parent = new Node(Token.NAME);
    n.setParent(parent);
    JSDocInfo result = checkGlobalThis.getFunctionJsDocInfo(n);
    assertNull(result);
  }

  @Test
  public void testGetFunctionJsDocInfo_jsDocNull_parentTypeIsName_jsDocOnName() {
    Node n = new Node(Token.FUNCTION);
    Node parent = new Node(Token.NAME);
    JSDocInfo jsDoc = new JSDocInfo();
    parent.setJSDocInfo(jsDoc);
    n.setParent(parent);
    JSDocInfo result = checkGlobalThis.getFunctionJsDocInfo(n);
    assertEquals(jsDoc, result);
  }

  @Test
  public void testGetFunctionJsDocInfo_jsDocNull_parentTypeIsAssign_noJsDocOnAssign() {
    Node n = new Node(Token.FUNCTION);
    Node parent = new Node(Token.ASSIGN);
    n.setParent(parent);
    JSDocInfo result = checkGlobalThis.getFunctionJsDocInfo(n);
    assertNull(result);
  }

  @Test
  public void testGetFunctionJsDocInfo_jsDocNull_parentTypeIsAssign_jsDocOnAssign() {
    Node n = new Node(Token.FUNCTION);
    Node parent = new Node(Token.ASSIGN);
    JSDocInfo jsDoc = new JSDocInfo();
    parent.setJSDocInfo(jsDoc);
    n.setParent(parent);
    JSDocInfo result = checkGlobalThis.getFunctionJsDocInfo(n);
    assertEquals(jsDoc, result);
  }

}
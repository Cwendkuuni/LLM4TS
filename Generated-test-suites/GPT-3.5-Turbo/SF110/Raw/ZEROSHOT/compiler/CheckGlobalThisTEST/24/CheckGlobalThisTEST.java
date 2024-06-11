import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.DiagnosticType;
import com.google.javascript.jscomp.Node;

public class CheckGlobalThisTest {
  
  private CheckGlobalThis checkGlobalThis;
  private AbstractCompiler compiler;

  @Before
  public void setUp() {
    compiler = new AbstractCompiler();
    checkGlobalThis = new CheckGlobalThis(compiler);
  }

  @Test
  public void testShouldTraverse_notFunction_returnFalse() {
    NodeTraversal t = new NodeTraversal();
    Node n = new Node(Token.NAME);
    Node parent = new Node(Token.BLOCK);
    boolean result = checkGlobalThis.shouldTraverse(t, n, parent);
    assertFalse(result);
  }
  
  @Test
  public void testShouldTraverse_functionConstructorOrThisAnnotation_returnFalse() {
    NodeTraversal t = new NodeTraversal();
    Node n = new Node(Token.FUNCTION);
    Node parent = new Node(Token.BLOCK);
    JSDocInfo jsDoc = new JSDocInfo();
    jsDoc.setConstructor(true);
    n.setJSDocInfo(jsDoc);
    boolean result = checkGlobalThis.shouldTraverse(t, n, parent);
    assertFalse(result);
  }
  
  @Test
  public void testShouldTraverse_functionPossiblyWithThisAnnotation_returnTrue() {
    NodeTraversal t = new NodeTraversal();
    Node n = new Node(Token.FUNCTION);
    Node parent = new Node(Token.BLOCK);
    JSDocInfo jsDoc = new JSDocInfo();
    n.setJSDocInfo(jsDoc);
    boolean result = checkGlobalThis.shouldTraverse(t, n, parent);
    assertTrue(result);
  }
  
  @Test
  public void testShouldTraverse_varAssignment_returnTrue() {
    NodeTraversal t = new NodeTraversal();
    Node n = new Node(Token.NAME);
    Node parent = new Node(Token.VAR);
    boolean result = checkGlobalThis.shouldTraverse(t, n, parent);
    assertTrue(result);
  }
  
  @Test
  public void testShouldTraverse_functionGettingLentToPrototype_returnFalse() {
    NodeTraversal t = new NodeTraversal();
    Node n = new Node(Token.FUNCTION);
    Node parent = new Node(Token.STRING_KEY);
    Node gramps = new Node(Token.NAME);
    JSDocInfo jsDoc = new JSDocInfo();
    jsDoc.setLendsName("prototype");
    gramps.setJSDocInfo(jsDoc);
    parent.setParent(gramps);
    boolean result = checkGlobalThis.shouldTraverse(t, n, parent);
    assertFalse(result);
  }
  
  @Test
  public void testVisit_thisKeyword_reportDiagnostic() {
    NodeTraversal t = new NodeTraversal();
    Node n = new Node(Token.NAME);
    Node parent = new Node(Token.GETPROP);
    CheckGlobalThis.DiagnosticType GLOBAL_THIS = CheckGlobalThis.GLOBAL_THIS;
    compiler.report(t.makeError(n, GLOBAL_THIS));
    checkGlobalThis.visit(t, n, parent);
    // assert error reported
  }
  
  @Test
  public void testVisit_leftSideAssignment_clearAssignLhsChild() {
    NodeTraversal t = new NodeTraversal();
    Node n = new Node(Token.NAME);
    Node parent = new Node(Token.NAME);
    checkGlobalThis.assignLhsChild = n;
    checkGlobalThis.visit(t, n, parent);
    assertNull(checkGlobalThis.assignLhsChild);
  }
  
  @Test
  public void testShouldReportThis_thisInAssignLhsChild_returnTrue() {
    NodeTraversal t = new NodeTraversal();
    Node n = new Node(Token.NAME);
    Node parent = new Node(Token.NAME);
    checkGlobalThis.assignLhsChild = n;
    boolean result = checkGlobalThis.shouldReportThis(n);
    assertTrue(result);
  }
  
  @Test
  public void testShouldReportThis_thisWithProperty_returnTrue() {
    NodeTraversal t = new NodeTraversal();
    Node n = new Node(Token.NAME);
    Node parent = new Node(Token.GETPROP);
    boolean result = checkGlobalThis.shouldReportThis(n);
    assertTrue(result);
  }
  
  @Test
  public void testGetFunctionJsDocInfo_noJsDocInfo_returnNull() {
    Node n = new Node(Token.FUNCTION);
    Node parent = new Node(Token.BLOCK);
    JSDocInfo result = checkGlobalThis.getFunctionJsDocInfo(n);
    assertNull(result);
  }
  
  @Test
  public void testGetFunctionJsDocInfo_parentIsName_returnParentJSDocInfo() {
    Node n = new Node(Token.FUNCTION);
    Node parent = new Node(Token.NAME);
    JSDocInfo jsDoc = new JSDocInfo();
    parent.setJSDocInfo(jsDoc);
    JSDocInfo result = checkGlobalThis.getFunctionJsDocInfo(n);
    assertEquals(jsDoc, result);
  }
  
  @Test
  public void testGetFunctionJsDocInfo_parentIsAssign_returnParentJSDocInfo() {
    Node n = new Node(Token.FUNCTION);
    Node parent = new Node(Token.ASSIGN);
    JSDocInfo jsDoc = new JSDocInfo();
    parent.setJSDocInfo(jsDoc);
    JSDocInfo result = checkGlobalThis.getFunctionJsDocInfo(n);
    assertEquals(jsDoc, result);
  }
  
  @Test
  public void testGetFunctionJsDocInfo_parentIsNameInVar_returnGrandParentJSDocInfo() {
    Node n = new Node(Token.FUNCTION);
    Node parent = new Node(Token.NAME);
    Node gramps = new Node(Token.VAR);
    JSDocInfo jsDoc = new JSDocInfo();
    gramps.setJSDocInfo(jsDoc);
    parent.setParent(gramps);
    JSDocInfo result = checkGlobalThis.getFunctionJsDocInfo(n);
    assertEquals(jsDoc, result);
  }

}
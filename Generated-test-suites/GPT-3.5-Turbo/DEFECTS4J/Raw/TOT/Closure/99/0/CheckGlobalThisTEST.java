import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

import org.junit.Before;
import org.junit.Test;

public class CheckGlobalThisTest {

  private CheckGlobalThis checkGlobalThis;

  @Before
  public void setUp() {
    checkGlobalThis = new CheckGlobalThis(null, null);
  }

  @Test
  public void shouldTraverse_WithNonGlobalFunction_ReturnsTrue() {
    NodeTraversal t = new NodeTraversal();
    Node n = new Node(Token.FUNCTION);
    Node parent = new Node();
    
    boolean result = checkGlobalThis.shouldTraverse(t, n, parent);

    assertTrue(result);
  }
  
  @Test
  public void shouldTraverse_WithConstructor_ReturnsFalse() {
    NodeTraversal t = new NodeTraversal();
    Node n = new Node(Token.FUNCTION);
    Node parent = new Node();
    JSDocInfo jsDocInfo = new JSDocInfo();
    jsDocInfo.setConstructor(true);
    n.setJSDocInfo(jsDocInfo);
    
    boolean result = checkGlobalThis.shouldTraverse(t, n, parent);

    assertFalse(result);
  }
  
  @Test
  public void shouldTraverse_WithThisType_ReturnsFalse() {
    NodeTraversal t = new NodeTraversal();
    Node n = new Node(Token.FUNCTION);
    Node parent = new Node();
    JSDocInfo jsDocInfo = new JSDocInfo();
    jsDocInfo.recordThisType();
    n.setJSDocInfo(jsDocInfo);
    
    boolean result = checkGlobalThis.shouldTraverse(t, n, parent);

    assertFalse(result);
  }
  
  @Test
  public void shouldTraverse_WithOverride_ReturnsFalse() {
    NodeTraversal t = new NodeTraversal();
    Node n = new Node(Token.FUNCTION);
    Node parent = new Node();
    JSDocInfo jsDocInfo = new JSDocInfo();
    jsDocInfo.recordOverride();
    n.setJSDocInfo(jsDocInfo);
    
    boolean result = checkGlobalThis.shouldTraverse(t, n, parent);

    assertFalse(result);
  }
  
  @Test
  public void shouldTraverse_WithBlockParent_ReturnsTrue() {
    NodeTraversal t = new NodeTraversal();
    Node n = new Node(Token.FUNCTION);
    Node parent = new Node(Token.BLOCK);
    
    boolean result = checkGlobalThis.shouldTraverse(t, n, parent);

    assertTrue(result);
  }
  
  @Test
  public void shouldTraverse_WithScriptParent_ReturnsTrue() {
    NodeTraversal t = new NodeTraversal();
    Node n = new Node(Token.FUNCTION);
    Node parent = new Node(Token.SCRIPT);
    
    boolean result = checkGlobalThis.shouldTraverse(t, n, parent);

    assertTrue(result);
  }
  
  @Test
  public void shouldTraverse_WithNameParent_ReturnsTrue() {
    NodeTraversal t = new NodeTraversal();
    Node n = new Node(Token.FUNCTION);
    Node parent = new Node(Token.NAME);
    
    boolean result = checkGlobalThis.shouldTraverse(t, n, parent);

    assertTrue(result);
  }
  
  @Test
  public void shouldTraverse_WithAssignParent_ReturnsTrue() {
    NodeTraversal t = new NodeTraversal();
    Node n = new Node(Token.FUNCTION);
    Node parent = new Node(Token.ASSIGN);
    
    boolean result = checkGlobalThis.shouldTraverse(t, n, parent);

    assertTrue(result);
  }
  
  @Test
  public void visit_WithThisOnLeftSideOfAssign_ReportsError() {
    NodeTraversal t = new NodeTraversal();
    Node n = new Node(Token.THIS);
    Node parent = new Node(Token.ASSIGN);
    CheckLevel level = new CheckLevel();
    AbstractCompiler compiler = new AbstractCompiler();
    compiler.setReportedErrorCount(0);
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler, level);
    
    checkGlobalThis.visit(t, n, parent);

    assertEquals(1, compiler.getReportedErrorCount());
  }
  
  @Test
  public void visit_WithThisOnRightSideOfAssign_ReportsNoError() {
    NodeTraversal t = new NodeTraversal();
    Node n = new Node(Token.THIS);
    Node parent = new Node(Token.ASSIGN);
    CheckLevel level = new CheckLevel();
    AbstractCompiler compiler = new AbstractCompiler();
    compiler.setReportedErrorCount(0);
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler, level);
    
    checkGlobalThis.visit(t, n, parent);

    assertEquals(0, compiler.getReportedErrorCount());
  }
  
  @Test
  public void visit_WithThisOnLeftSideOfAssign_UpdatesAssignLhsChildToNull() {
    NodeTraversal t = new NodeTraversal();
    Node n = new Node(Token.THIS);
    Node parent = new Node(Token.ASSIGN);
    CheckLevel level = new CheckLevel();
    AbstractCompiler compiler = new AbstractCompiler();
    compiler.setReportedErrorCount(0);
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler, level);
    checkGlobalThis.assignLhsChild = new Node();
    
    checkGlobalThis.visit(t, n, parent);

    assertEquals(null, checkGlobalThis.assignLhsChild);
  }
  
  @Test
  public void visit_WithThisNotOnLeftSideOfAssign_DoesNotUpdateAssignLhsChild() {
    NodeTraversal t = new NodeTraversal();
    Node n = new Node(Token.THIS);
    Node parent = new Node(Token.CALL);
    CheckLevel level = new CheckLevel();
    AbstractCompiler compiler = new AbstractCompiler();
    compiler.setReportedErrorCount(0);
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler, level);
    checkGlobalThis.assignLhsChild = new Node();
    
    checkGlobalThis.visit(t, n, parent);

    assertTrue(checkGlobalThis.assignLhsChild != null);
  }
  
  @Test
  public void shouldReportThis_WithAssignLhsChildNotNull_ReturnsTrue() {
    Node n = new Node();
    Node parent = new Node();
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null, null);
    checkGlobalThis.assignLhsChild = new Node();
    
    boolean result = checkGlobalThis.shouldReportThis(n, parent);

    assertTrue(result);
  }
  
  @Test
  public void shouldReportThis_WithParentNull_ReturnsFalse() {
    Node n = new Node();
    Node parent = null;
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null, null);
    
    boolean result = checkGlobalThis.shouldReportThis(n, parent);

    assertFalse(result);
  }
  
  @Test
  public void shouldReportThis_WithParentNotGet_ReturnsFalse() {
    Node n = new Node();
    Node parent = new Node();
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null, null);
    
    boolean result = checkGlobalThis.shouldReportThis(n, parent);

    assertFalse(result);
  }
  
  @Test
  public void shouldReportThis_WithParentGet_ReturnsTrue() {
    Node n = new Node();
    Node parent = new Node(Token.GETPROP);
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null, null);
    
    boolean result = checkGlobalThis.shouldReportThis(n, parent);

    assertTrue(result);
  }

  @Test
  public void getFunctionJsDocInfo_WithJSDocInfoNotNull_ReturnsJSDocInfo() {
    Node n = new Node();
    JSDocInfo jsDocInfo = new JSDocInfo();
    n.setJSDocInfo(jsDocInfo);
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null, null);
    
    JSDocInfo result = checkGlobalThis.getFunctionJsDocInfo(n);

    assertEquals(jsDocInfo, result);
  }
  
  @Test
  public void getFunctionJsDocInfo_WithJSDocInfoNullAndParentTypeIsName_ReturnsJSDocInfoOfParent() {
    Node n = new Node();
    Node parent = new Node();
    JSDocInfo jsDocInfo = new JSDocInfo();
    parent.setJSDocInfo(jsDocInfo);
    parent.setType(Token.NAME);
    n.setParent(parent);
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null, null);
    
    JSDocInfo result = checkGlobalThis.getFunctionJsDocInfo(n);

    assertEquals(jsDocInfo, result);
  }
  
  @Test
  public void getFunctionJsDocInfo_WithJSDocInfoNullAndParentTypeIsAssign_ReturnsJSDocInfoOfParent() {
    Node n = new Node();
    Node parent = new Node();
    JSDocInfo jsDocInfo = new JSDocInfo();
    parent.setJSDocInfo(jsDocInfo);
    parent.setType(Token.ASSIGN);
    n.setParent(parent);
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null, null);
    
    JSDocInfo result = checkGlobalThis.getFunctionJsDocInfo(n);

    assertEquals(jsDocInfo, result);
  }
  
  @Test
  public void getFunctionJsDocInfo_WithJSDocInfoNullAndParentTypeIsVar_ReturnsJSDocInfoOfGrandparent() {
    Node n = new Node();
    Node parent = new Node();
    Node grandparent = new Node();
    JSDocInfo jsDocInfo = new JSDocInfo();
    grandparent.setJSDocInfo(jsDocInfo);
    grandparent.setType(Token.VAR);
    parent.setParent(grandparent);
    n.setParent(parent);
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null, null);
    
    JSDocInfo result = checkGlobalThis.getFunctionJsDocInfo(n);

    assertEquals(jsDocInfo, result);
  }
  
}
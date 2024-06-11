import org.junit.*;
import static org.junit.Assert.*;
import com.google.javascript.jscomp.*;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.Token;

public class CheckGlobalThisTest {
  
  @Test
  public void testShouldTraverse_NonFunctionNode_ReturnsTrue() {
    AbstractCompiler compiler = new AbstractCompiler();
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler, CheckLevel.WARNING);
    NodeTraversal t = new NodeTraversal(null, null);
    Node n = new Node(Token.NUMBER);
    Node parent = new Node(Token.VAR);
    assertTrue(checkGlobalThis.shouldTraverse(t, n, parent));
  }
  
  @Test
  public void testShouldTraverse_FunctionNodeWithConstructorJsDoc_ReturnsFalse() {
    AbstractCompiler compiler = new AbstractCompiler();
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler, CheckLevel.WARNING);
    NodeTraversal t = new NodeTraversal(null, null);
    Node n = new Node(Token.FUNCTION);
    JSDocInfo jsDoc = new JSDocInfo();
    jsDoc.setConstructor(true);
    n.setJSDocInfo(jsDoc);
    Node parent = new Node(Token.VAR);
    assertFalse(checkGlobalThis.shouldTraverse(t, n, parent));
  }
  
  @Test
  public void testShouldTraverse_FunctionNodeWithThisTypeJsDoc_ReturnsFalse() {
    AbstractCompiler compiler = new AbstractCompiler();
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler, CheckLevel.WARNING);
    NodeTraversal t = new NodeTraversal(null, null);
    Node n = new Node(Token.FUNCTION);
    JSDocInfo jsDoc = new JSDocInfo();
    jsDoc.recordThisType();
    n.setJSDocInfo(jsDoc);
    Node parent = new Node(Token.VAR);
    assertFalse(checkGlobalThis.shouldTraverse(t, n, parent));
  }
  
  @Test
  public void testShouldTraverse_FunctionNodeWithoutJsDoc_ReturnsTrue() {
    AbstractCompiler compiler = new AbstractCompiler();
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler, CheckLevel.WARNING);
    NodeTraversal t = new NodeTraversal(null, null);
    Node n = new Node(Token.FUNCTION);
    Node parent = new Node(Token.VAR);
    assertTrue(checkGlobalThis.shouldTraverse(t, n, parent));
  }
  
  @Test
  public void testShouldTraverse_LeftSideOfAssignment_ReturnsTrue() {
    AbstractCompiler compiler = new AbstractCompiler();
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler, CheckLevel.WARNING);
    NodeTraversal t = new NodeTraversal(null, null);
    Node n = new Node(Token.NAME);
    Node parent = new Node(Token.ASSIGN);
    parent.addChildToFront(n);
    assertTrue(checkGlobalThis.shouldTraverse(t, n, parent));
  }
  
  @Test
  public void testShouldTraverse_RightSideOfAssignment_PrototypeProperty_ReturnsFalse() {
    AbstractCompiler compiler = new AbstractCompiler();
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler, CheckLevel.WARNING);
    NodeTraversal t = new NodeTraversal(null, null);
    Node n = new Node(Token.THIS);
    Node leftSide = new Node(Token.GETPROP);
    leftSide.addChildToFront(new Node(Token.NAME, "prototype"));
    parent.addChildToFront(leftSide);
    parent.addChildToBack(n);
    Node parent = new Node(Token.ASSIGN);
    assertFalse(checkGlobalThis.shouldTraverse(t, n, parent));
  }
  
  @Test
  public void testShouldTraverse_RightSideOfAssignment_PrototypeSubproperty_ReturnsFalse() {
    AbstractCompiler compiler = new AbstractCompiler();
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler, CheckLevel.WARNING);
    NodeTraversal t = new NodeTraversal(null, null);
    Node n = new Node(Token.THIS);
    Node leftSide = new Node(Token.GETPROP);
    leftSide.addChildToFront(new Node(Token.NAME, "myObject"));
    leftSide.addChildToBack(new Node(Token.STRING, "prototype"));
    parent.addChildToFront(leftSide);
    parent.addChildToBack(n);
    Node parent = new Node(Token.ASSIGN);
    assertFalse(checkGlobalThis.shouldTraverse(t, n, parent));
  }
  
  @Test
  public void testShouldTraverse_RightSideOfAssignment_OtherProperty_ReturnsTrue() {
    AbstractCompiler compiler = new AbstractCompiler();
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler, CheckLevel.WARNING);
    NodeTraversal t = new NodeTraversal(null, null);
    Node n = new Node(Token.THIS);
    Node leftSide = new Node(Token.GETPROP);
    leftSide.addChildToFront(new Node(Token.NAME, "myObject"));
    leftSide.addChildToBack(new Node(Token.STRING, "otherProperty"));
    parent.addChildToFront(leftSide);
    parent.addChildToBack(n);
    Node parent = new Node(Token.ASSIGN);
    assertTrue(checkGlobalThis.shouldTraverse(t, n, parent));
  }
  
  @Test
  public void testVisit_ThisNode_NoParentNode_DoesNotReportError() {
    AbstractCompiler compiler = new AbstractCompiler();
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler, CheckLevel.WARNING);
    Node n = new Node(Token.THIS);
    NodeTraversal t = new NodeTraversal(null, null);
    checkGlobalThis.visit(t, n, null);
    // No assertion, as long as it does not throw any exception, the test passed.
  }
  
  @Test
  public void testVisit_ThisNode_ParentNodeIsAssignLhs_AssignLhsChildNotNull_ReportsError() {
    AbstractCompiler compiler = new AbstractCompiler();
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler, CheckLevel.WARNING);
    Node n = new Node(Token.THIS);
    Node parent = new Node(Token.ASSIGN);
    parent.addChildToFront(new Node(Token.NAME, "a"));
    parent.addChildToBack(n);
    checkGlobalThis.assignLhsChild = parent.getFirstChild();
    NodeTraversal t = new NodeTraversal(null, null);
    try {
      checkGlobalThis.visit(t, n, parent);
      fail("Expected an error to be reported");
    } catch (Exception e) {
      assertEquals(e.getMessage(), "dangerous use of the global 'this' object");
    }
  }
  
  @Test
  public void testVisit_ThisNode_ParentNodeIsAssignLhs_AssignLhsChildNull_DoesNotReportError() {
    AbstractCompiler compiler = new AbstractCompiler();
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler, CheckLevel.WARNING);
    Node n = new Node(Token.THIS);
    Node parent = new Node(Token.ASSIGN);
    parent.addChildToFront(new Node(Token.NAME, "a"));
    parent.addChildToBack(n);
    checkGlobalThis.assignLhsChild = null;
    NodeTraversal t = new NodeTraversal(null, null);
    checkGlobalThis.visit(t, n, parent);
    // No assertion, as long as it does not throw any exception, the test passed.
  }
  
  @Test
  public void testShouldReportThis_LeftSideOfAssignment_ReturnsTrue() {
    AbstractCompiler compiler = new AbstractCompiler();
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler, CheckLevel.WARNING);
    Node n = new Node(Token.THIS);
    Node parent = new Node(Token.ASSIGN);
    parent.addChildToFront(new Node(Token.NAME, "a"));
    parent.addChildToBack(new Node(Token.THIS));
    checkGlobalThis.assignLhsChild = parent.getFirstChild();
    assertTrue(checkGlobalThis.shouldReportThis(n, parent));
  }
  
  @Test
  public void testShouldReportThis_RightSideOfAssignment_ReturnsFalse() {
    AbstractCompiler compiler = new AbstractCompiler();
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler, CheckLevel.WARNING);
    Node n = new Node(Token.THIS);
    Node parent = new Node(Token.ASSIGN);
    parent.addChildToFront(new Node(Token.NAME, "a"));
    parent.addChildToBack(new Node(Token.THIS));
    checkGlobalThis.assignLhsChild = null;
    assertFalse(checkGlobalThis.shouldReportThis(n, parent));
  }
  
  @Test
  public void testShouldReportThis_OtherNode_ReturnsFalse() {
    AbstractCompiler compiler = new AbstractCompiler();
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler, CheckLevel.WARNING);
    Node n = new Node(Token.THIS);
    Node parent = new Node(Token.NAME);
    assertFalse(checkGlobalThis.shouldReportThis(n, parent));
  }
  
  @Test
  public void testGetFunctionJsDocInfo_FunctionNodeWithJsDoc_ReturnsJsDocInfo() {
    AbstractCompiler compiler = new AbstractCompiler();
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler, CheckLevel.WARNING);
    Node n = new Node(Token.FUNCTION);
    JSDocInfo jsDoc = new JSDocInfo();
    n.setJSDocInfo(jsDoc);
    assertEquals(jsDoc, checkGlobalThis.getFunctionJsDocInfo(n));
  }
  
  @Test
  public void testGetFunctionJsDocInfo_ParentNodeIsNameNodeAndHasJsDoc_ReturnsJsDocInfo() {
    AbstractCompiler compiler = new AbstractCompiler();
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler, CheckLevel.WARNING);
    Node n = new Node(Token.NAME);
    n.setString("myFunction");
    JSDocInfo jsDoc = new JSDocInfo();
    n.setJSDocInfo(jsDoc);
    Node parent = new Node(Token.VAR);
    parent.addChildToBack(n);
    assertEquals(jsDoc, checkGlobalThis.getFunctionJsDocInfo(n));
  }
  
  @Test
  public void testGetFunctionJsDocInfo_ParentNodeIsNameNodeAndHasNoJsDoc_ReturnsJsDocFromVarParentNode() {
    AbstractCompiler compiler = new AbstractCompiler();
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler, CheckLevel.WARNING);
    Node n = new Node(Token.NAME);
    n.setString("myFunction");
    Node parent = new Node(Token.VAR);
    JSDocInfo jsDoc = new JSDocInfo();
    parent.setJSDocInfo(jsDoc);
    parent.addChildToBack(n);
    assertEquals(jsDoc, checkGlobalThis.getFunctionJsDocInfo(n));
  }
  
  @Test
  public void testGetFunctionJsDocInfo_ParentNodeIsAssignNode_ReturnsJsDocFromVarGreatGrandParentNode() {
    AbstractCompiler compiler = new AbstractCompiler();
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler, CheckLevel.WARNING);
    Node n = new Node(Token.NAME);
    n.setString("myFunction");
    Node parent = new Node(Token.VAR);
    parent.addChildToBack(n);
    Node gramps = new Node(Token.EXPR_RESULT);
    gramps.addChildToBack(parent);
    Node greatGramps = new Node(Token.VAR);
    JSDocInfo jsDoc = new JSDocInfo();
    greatGramps.setJSDocInfo(jsDoc);
    greatGramps.addChildToBack(gramps);
    assertEquals(jsDoc, checkGlobalThis.getFunctionJsDocInfo(n));
  }
  
  @Test
  public void testGetFunctionJsDocInfo_ParentNodeHasNoJsDoc_ReturnsNull() {
    AbstractCompiler compiler = new AbstractCompiler();
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler, CheckLevel.WARNING);
    Node n = new Node(Token.NAME);
    n.setString("myFunction");
    assertEquals(null, checkGlobalThis.getFunctionJsDocInfo(n));
  }
  
}
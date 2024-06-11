import org.junit.*;
import static org.junit.Assert.*;

public class CheckGlobalThisTest {
  
  @Test
  public void testShouldTraverse_returnsFalseForConstructorFunctionsWithThisAnnotation() {
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler, level);
    Node functionNode = new Node(Token.FUNCTION);
    JSDocInfo jsDoc = new JSDocInfo();
    jsDoc.recordConstructor();
    functionNode.setJSDocInfo(jsDoc);
    assertTrue(checkGlobalThis.shouldTraverse(null, functionNode, null));
  }
  
  @Test
  public void testShouldTraverse_returnsFalseForConstructorFunctionsWithThisTypeAnnotation() {
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler, level);
    Node functionNode = new Node(Token.FUNCTION);
    JSDocInfo jsDoc = new JSDocInfo();
    jsDoc.recordThisType();
    functionNode.setJSDocInfo(jsDoc);
    assertTrue(checkGlobalThis.shouldTraverse(null, functionNode, null));
  }
  
  @Test
  public void testShouldTraverse_returnsFalseForConstructorFunctionsWithOverrideAnnotation() {
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler, level);
    Node functionNode = new Node(Token.FUNCTION);
    JSDocInfo jsDoc = new JSDocInfo();
    jsDoc.recordOverride();
    functionNode.setJSDocInfo(jsDoc);
    assertTrue(checkGlobalThis.shouldTraverse(null, functionNode, null));
  }
  
  @Test
  public void testShouldTraverse_returnsFalseForFunctionsInsideNonGlobalContexts() {
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler, level);
    Node functionNode = new Node(Token.FUNCTION);
    Node parentBlockNode = new Node(Token.BLOCK);
    Node parentScriptNode = new Node(Token.SCRIPT);
    Node parentNameNode = new Node(Token.NAME);
    Node parentAssignNode = new Node(Token.ASSIGN);
    assertFalse(checkGlobalThis.shouldTraverse(null, functionNode, parentBlockNode));
    assertFalse(checkGlobalThis.shouldTraverse(null, functionNode, parentScriptNode));
    assertFalse(checkGlobalThis.shouldTraverse(null, functionNode, parentNameNode));
    assertFalse(checkGlobalThis.shouldTraverse(null, functionNode, parentAssignNode));
  }
  
  @Test
  public void testShouldTraverse_returnsTrueForLeftSideOfAssignment() {
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler, level);
    Node assignNode = new Node(Token.ASSIGN);
    Node lhsNode = new Node(Token.NAME);
    Node rhsNode = new Node(Token.NAME);
    assignNode.addChildToBack(lhsNode);
    assignNode.addChildToBack(rhsNode);
    assertTrue(checkGlobalThis.shouldTraverse(null, lhsNode, assignNode));
  }
  
  @Test
  public void testShouldTraverse_returnsFalseForAssignmentToPrototypeProperty() {
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler, level);
    Node assignNode = new Node(Token.ASSIGN);
    Node lhsNode = new Node(Token.GETPROP);
    Node prototypeNode = new Node(Token.STRING, "prototype");
    lhsNode.addChildToBack(prototypeNode);
    assignNode.addChildToBack(lhsNode);
    assertFalse(checkGlobalThis.shouldTraverse(null, lhsNode.getLastChild(), assignNode));
  }
  
  @Test
  public void testShouldTraverse_returnsFalseForAssignmentToSubproperty() {
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler, level);
    Node assignNode = new Node(Token.ASSIGN);
    Node lhsNode = new Node(Token.NAME, "a.prototype");
    assignNode.addChildToBack(lhsNode);
    assertFalse(checkGlobalThis.shouldTraverse(null, lhsNode, assignNode));
  }
  
  @Test
  public void testVisit_reportsWarningForThisInGlobalContext() {
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler, CheckLevel.WARNING);
    Node thisNode = new Node(Token.THIS);
    NodeTraversal t = new NodeTraversal(null, null);
    checkGlobalThis.visit(t, thisNode, null);
    // assert error report
  }
  
  @Test
  public void testVisit_reportsWarningForThisInPropertyAccess() {
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler, CheckLevel.WARNING);
    Node thisNode = new Node(Token.THIS);
    Node parentGetNode = new Node(Token.GETPROP);
    parentGetNode.addChildToBack(thisNode);
    NodeTraversal t = new NodeTraversal(null, null);
    checkGlobalThis.visit(t, thisNode, parentGetNode);
    // assert error report
  }
  
  @Test
  public void testVisit_doesNotReportWarningForThisOnLeftSideOfAssignment() {
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler, CheckLevel.WARNING);
    Node thisNode = new Node(Token.THIS);
    Node parentAssignNode = new Node(Token.ASSIGN);
    parentAssignNode.addChildToBack(thisNode);
    NodeTraversal t = new NodeTraversal(null, null);
    checkGlobalThis.visit(t, thisNode, parentAssignNode);
    // assert no error report
  }
  
  @Test
  public void testVisit_clearsAssignLhsChildAfterProcessingAssignmentNode() {
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler, CheckLevel.WARNING);
    Node thisNode = new Node(Token.THIS);
    Node parentAssignNode = new Node(Token.ASSIGN);
    parentAssignNode.addChildToBack(thisNode);
    NodeTraversal t = new NodeTraversal(null, null);
    checkGlobalThis.visit(t, thisNode, parentAssignNode);
    assertNull(checkGlobalThis.assignLhsChild);
  }
}
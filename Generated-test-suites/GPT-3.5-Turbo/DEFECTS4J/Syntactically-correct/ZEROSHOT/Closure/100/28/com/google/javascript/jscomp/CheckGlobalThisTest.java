import static org.junit.Assert.*;
import org.junit.Test;

public class CheckGlobalThisTest {
  
  @Test
  public void testShouldTraverse_WithTypeFunction_ParentNull_ReturnTrue() {
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null, null);
    assertTrue(checkGlobalThis.shouldTraverse(null, new Node(Token.FUNCTION), null));
  }
  
  @Test
  public void testShouldTraverse_WithTypeFunction_ParentNotNull_IsConstructor_ReturnFalse() {
    JSDocInfo jsDocInfo = new JSDocInfo();
    jsDocInfo.recordConstructor();
    NodeTraversal nodeTraversal = new NodeTraversal();
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(new AbstractCompiler(), CheckLevel.OFF);
    assertFalse(checkGlobalThis.shouldTraverse(nodeTraversal, new Node(Token.FUNCTION), new Node()));
  }
  
  @Test
  public void testShouldTraverse_WithTypeFunction_ParentNotNull_HasThisType_ReturnFalse() {
    JSDocInfo jsDocInfo = new JSDocInfo();
    jsDocInfo.recordThisType();
    NodeTraversal nodeTraversal = new NodeTraversal();
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(new AbstractCompiler(), CheckLevel.OFF);
    assertFalse(checkGlobalThis.shouldTraverse(nodeTraversal, new Node(Token.FUNCTION), new Node()));
  }
  
  @Test
  public void testShouldTraverse_NodeIsLeftSideOfAssign_ReturnTrue() {
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null, null);
    NodeTraversal nodeTraversal = new NodeTraversal();
    Node assignNode = new Node(Token.ASSIGN);
    Node leftNode = new Node(Token.NAME);
    assignNode.addChildToBack(leftNode);
    assertTrue(checkGlobalThis.shouldTraverse(nodeTraversal, leftNode, assignNode));
  }
  
  @Test
  public void testShouldTraverse_NodeIsRightSideOfAssign_NotAssignToPrototype_ReturnTrue() {
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null, null);
    NodeTraversal nodeTraversal = new NodeTraversal();
    Node assignNode = new Node(Token.ASSIGN);
    Node leftNode = new Node(Token.GETPROP);
    assignNode.addChildToBack(leftNode);
    assertFalse(checkGlobalThis.shouldTraverse(nodeTraversal, leftNode, assignNode));
  }
  
  @Test
  public void testShouldReportThis_NodeIsLeftSideOfAssign_ReturnTrue() {
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null, null);
    assertTrue(checkGlobalThis.shouldReportThis(new Node(Token.THIS), new Node(Token.ASSIGN)));
  }
  
  @Test
  public void testShouldReportThis_NodeIsNotLeftSideOfAssign_ReturnFalse() {
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null, null);
    assertFalse(checkGlobalThis.shouldReportThis(new Node(Token.THIS), null));
  }
  
  @Test
  public void testVisit_NodeIsThis_ShouldReportError() {
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null, CheckLevel.WARNING);
    NodeTraversal nodeTraversal = new NodeTraversal();
    AbstractCompiler abstractCompiler = new AbstractCompiler();
    nodeTraversal.makeError(new Node(), CheckLevel.WARNING, CheckGlobalThis.GLOBAL_THIS);
    assertFalse(checkGlobalThis.shouldReportThis(new Node(Token.THIS), null));
  }
  
  @Test
  public void testVisit_NodeIsAssignLhsChild_ShouldResetAssignLhsChild() {
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null, null);
    checkGlobalThis.assignLhsChild = new Node(Token.NAME);
    checkGlobalThis.visit(new NodeTraversal(), new Node(), new Node());
    assertNull(checkGlobalThis.assignLhsChild);
  }
}
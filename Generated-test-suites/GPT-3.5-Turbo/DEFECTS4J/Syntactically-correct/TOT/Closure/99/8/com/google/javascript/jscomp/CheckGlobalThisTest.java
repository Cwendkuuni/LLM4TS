import static org.junit.Assert.*;
import org.junit.Test;
import org.junit.Before;
import com.google.javascript.rhino.Node;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CheckGlobalThis;
import com.google.javascript.jscomp.CheckLevel;
import com.google.javascript.rhino.JSDocInfo;
import com.google.javascript.rhino.Token;
import com.google.javascript.jscomp.NodeTraversal;

public class CheckGlobalThisTest {
  
  private NodeTraversal.Callback callback;
  private NodeTraversal t;
  private AbstractCompiler compiler;

  @Before
  public void setUp() {
    compiler = new AbstractCompiler();
    callback = new CheckGlobalThis(compiler, CheckLevel.WARNING);
    t = new NodeTraversal(compiler, callback);
  }
  
  // Test CASE 1: Method shouldTraverse() - Test with a FUNCTION node that has constructor annotation
  @Test
  public void shouldTraverse_FunctionNodeWithConstructorAnnotation_ReturnsFalse() {
    Node functionNode = new Node(Token.FUNCTION);
    JSDocInfo jsDocInfo = new JSDocInfo();
    jsDocInfo.setConstructor(true);
    functionNode.setJSDocInfo(jsDocInfo);
    assertFalse(callback.shouldTraverse(t, functionNode, null));
  }

  // Test CASE 2: Method shouldTraverse() - Test with a FUNCTION node that has this annotation
  @Test
  public void shouldTraverse_FunctionNodeWithThisAnnotation_ReturnsFalse() {
    Node functionNode = new Node(Token.FUNCTION);
    JSDocInfo jsDocInfo = new JSDocInfo();
    jsDocInfo.recordThisType();
    functionNode.setJSDocInfo(jsDocInfo);
    assertFalse(callback.shouldTraverse(t, functionNode, null));
  }
  
  // Test CASE 3: Method shouldTraverse() - Test with a FUNCTION node inside an OBJECTLIT node
  @Test
  public void shouldTraverse_FunctionNodeInsideObjectLiteral_ReturnsFalse() {
    Node functionNode = new Node(Token.FUNCTION);
    Node objectLiteralNode = new Node(Token.OBJECTLIT, functionNode);
    assertTrue(callback.shouldTraverse(t, functionNode, objectLiteralNode));
  }

  // Test CASE 4: Method shouldTraverse() - Test with a FUNCTION node inside a BLOCK node
  @Test
  public void shouldTraverse_FunctionNodeInsideBlock_ReturnsTrue() {
    Node functionNode = new Node(Token.FUNCTION);
    Node blockNode = new Node(Token.BLOCK, functionNode);
    assertTrue(callback.shouldTraverse(t, functionNode, blockNode));
  }

  // Test CASE 5: Method shouldTraverse() - Test with a non-FUNCTION node 
  @Test
  public void shouldTraverse_NonFunctionNode_ReturnsTrue() {
    Node nonFunctionNode = new Node(Token.GETPROP);
    assertTrue(callback.shouldTraverse(t, nonFunctionNode, null));
  }

  // Test CASE 6: Method shouldTraverse() - Test with a FUNCTION node inside an IFE
  @Test
  public void shouldTraverse_FunctionNodeInsideIFE_ReturnsTrue() {
    Node functionNode = new Node(Token.FUNCTION);
    Node IFE = new Node(Token.IFE, functionNode);
    assertTrue(callback.shouldTraverse(t, functionNode, IFE));
  }

  // Test CASE 7: Method shouldTraverse() - Test with a FUNCTION node inside an ASSIGN node but not on the left side 
  @Test
  public void shouldTraverse_FunctionNodeInsideAssignNode_NotOnLeftSide_ReturnsTrue() {
    Node functionNode = new Node(Token.FUNCTION);
    Node assignNode = new Node(Token.ASSIGN);
    assignNode.addChildToBack(new Node(Token.GETPROP));
    assignNode.addChildToBack(functionNode);
    assertFalse(callback.shouldTraverse(t, functionNode, assignNode));
  }

  // Test CASE 8: Method shouldTraverse() - Test with a FUNCTION node inside an ASSIGN node on the left side 
  @Test
  public void shouldTraverse_FunctionNodeInsideAssignNode_OnLeftSide_ReturnsTrue() {
    Node functionNode = new Node(Token.FUNCTION);
    Node assignNode = new Node(Token.ASSIGN);
    assignNode.addChildToBack(functionNode);
    assignNode.addChildToBack(new Node(Token.GETPROP));
    assertTrue(callback.shouldTraverse(t, functionNode, assignNode));
  }

  // Test CASE 9: Method shouldTraverse() - Test with a FUNCTION node inside an ASSIGN node on the left side and .prototype chain
  @Test
  public void shouldTraverse_FunctionNodeInsideAssignNode_OnLeftSideWithPrototypeHierarchy_ReturnsFalse() {
    Node functionNode = new Node(Token.FUNCTION);
    Node assignNode = new Node(Token.ASSIGN);
    assignNode.addChildToBack(new Node(Token.GETPROP, new Node(Token.NAME, "prototype")));
    assignNode.addChildToBack(functionNode);
    assertFalse(callback.shouldTraverse(t, functionNode, assignNode));
  }


  // Test CASE 10: Method shouldReportThis() - Test with a THIS node on left side of an ASSIGN
  @Test
  public void shouldReportThis_ThisNodeOnLeftSideOfAssign_ReturnsTrue() {
    Node thisNode = new Node(Token.THIS);
    Node assignNode = new Node(Token.ASSIGN);
    assignNode.addChildToBack(thisNode);
    assertTrue(callback.shouldReportThis(thisNode, assignNode));
  }

  // Test CASE 11: Method shouldReportThis() - Test with a THIS node without parent
  @Test
  public void shouldReportThis_ThisNodeWithoutParent_ReturnsFalse() {
    Node thisNode = new Node(Token.THIS);
    assertFalse(callback.shouldReportThis(thisNode, null));
  }

  // Test CASE 12: Method shouldReportThis() - Test with a non-GET node as parent
  @Test
  public void shouldReportThis_ParentIsNonGetNode_ReturnsFalse() {
    Node parent = new Node(Token.ADD);
    Node thisNode = new Node(Token.THIS);
    assertFalse(callback.shouldReportThis(thisNode, parent));
  }

  // Test CASE 13: Method shouldReportThis() - Test with a THIS node and parent node is GET
  @Test
  public void shouldReportThis_ThisNodeWithParentGetNode_ReturnsTrue() {
    Node parent = new Node(Token.GETPROP);
    Node thisNode = new Node(Token.THIS);
    assertTrue(callback.shouldReportThis(thisNode, parent));
  }

  // Test CASE 14: Method shouldReportThis() - Test with an assigned THIS node and parent node is GET
  @Test
  public void shouldReportThis_AssignedThisNodeWithParentGetNode_ReturnsTrue() {
    Node parent = new Node(Token.GETPROP);
    Node thisNode = new Node(Token.THIS);
    Node assignNode = new Node(Token.ASSIGN, thisNode, new Node(Token.NULL));
    assertTrue(callback.shouldReportThis(thisNode, assignNode));
  }

  // Test CASE 15: Method visit() - Test with an assigned THIS node and assignLhsChild is not null
  @Test
  public void visit_AssignedThisNodeWithNotNullAssignLhsChild_ShouldSetAssignLhsChildToNull() {
    Node parent = new Node(Token.GETPROP);
    Node thisNode = new Node(Token.THIS);
    Node assignNode = new Node(Token.ASSIGN, thisNode, new Node(Token.NULL));
    callback.shouldReportThis(thisNode, assignNode);
    callback.visit(t, thisNode, assignNode);
    assertNull(((CheckGlobalThis) callback).assignLhsChild);
  }

  // Test CASE 16: Method visit() - Test with a non-assigned THIS node and assignLhsChild is not null
  @Test
  public void visit_NonAssignedThisNodeWithNotNullAssignLhsChild_ShouldKeepAssignLhsChildNotNull() {
    Node parent = new Node(Token.GETPROP);
    Node thisNode = new Node(Token.THIS);
    Node assignNode = new Node(Token.ACTION, new Node(Token.NAME), thisNode);
    callback.shouldReportThis(thisNode, assignNode);
    callback.visit(t, thisNode, assignNode);
    assertNotNull(((CheckGlobalThis) callback).assignLhsChild);
  }

  // Test CASE 17: Method visit() - Test with a non-assigned THIS node and assignLhsChild is null
  @Test
  public void visit_NonAssignedThisNodeWithNullAssignLhsChild_ShouldKeepAssignLhsChildNull() {
    Node parent = new Node(Token.GETPROP);
    Node thisNode = new Node(Token.THIS);
    Node assignNode = new Node(Token.ACTION, new Node(Token.NAME), new Node(Token.ACTION));
    callback.shouldReportThis(thisNode, assignNode);
    callback.visit(t, thisNode, assignNode);
    assertNull(((CheckGlobalThis) callback).assignLhsChild);
  }

  // Test CASE 18: Method visit() - Test with a non-THIS node
  @Test
  public void visit_NonThisNode_ShouldNotReport() {
    Node parent = new Node(Token.GETPROP);
    Node nonThisNode = new Node(Token.NOT_THIS);
    assertFalse(callback.shouldReportThis(nonThisNode, parent));
  }

  // Test CASE 19: Method visit() - Test with a THIS node and shouldReportThis() returns true
  @Test
  public void shouldReportThis_ThisNodeWithTrueFromMethod_ReturnsTrue() {
    Node parent = new Node(Token.GETPROP);
    Node thisNode = new Node(Token.THIS);
    assertTrue(callback.shouldReportThis(thisNode, parent));
  }

  // Test CASE 20: Method getFunctionJsDocInfo() - Test with a FUNCTION node that has JSDocInfo set
  @Test
  public void getFunctionJsDocInfo_FunctionNodeWithJSDocInfoSet_ShouldReturnJSDocInfo() {
    Node functionNode = new Node(Token.FUNCTION);
    JSDocInfo jsDocInfo = new JSDocInfo();
    functionNode.setJSDocInfo(jsDocInfo);
    assertEquals(jsDocInfo, ((CheckGlobalThis) callback).getFunctionJsDocInfo(functionNode));
  }

  // Test CASE 21: Method getFunctionJsDocInfo() - Test with a FUNCTION node in ASSIGN node with JSDocInfo in parent
  @Test
  public void getFunctionJsDocInfo_FunctionNodeInAssignNodeWithJSDocInfoInParent_ShouldReturnJSDocInfoInParentNode() {
    Node functionNode = new Node(Token.FUNCTION);
    Node assignNode = new Node(Token.ASSIGN, new Node(Token.NAME, "x"), functionNode);
    JSDocInfo jsDocInfo = new JSDocInfo();
    assignNode.setJSDocInfo(jsDocInfo);
    assertEquals(jsDocInfo, ((CheckGlobalThis) callback).getFunctionJsDocInfo(functionNode));
  }

  // Test CASE 22: Method getFunctionJsDocInfo() - Test with a FUNCTION node in ASSIGN node without JSDocInfo in parent
  @Test
  public void getFunctionJsDocInfo_FunctionNodeInAssignNodeWithoutJSDocInfoInParent_ShouldReturnNull() {
    Node functionNode = new Node(Token.FUNCTION);
    Node assignNode = new Node(Token.ASSIGN, new Node(Token.NAME, "x"), functionNode);
    assertNull(((CheckGlobalThis) callback).getFunctionJsDocInfo(functionNode));
  }

}
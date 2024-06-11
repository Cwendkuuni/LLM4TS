import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CheckLevel;
import com.google.javascript.rhino.JSDocInfo;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.Token;

public class CheckGlobalThisTest {

  private AbstractCompiler compiler;
  private CheckLevel level;
  private CheckGlobalThis checkGlobalThis;

  @Before
  public void setUp() {
    compiler = new AbstractCompiler();
    level = CheckLevel.WARNING;
    checkGlobalThis = new CheckGlobalThis(compiler, level);
  }

  @Test
  public void testShouldTraverse_FunctionNotConstructorOrAnnotated_NoTraverse() {
    // Arrange
    NodeTraversal t = new NodeTraversal();

    Node functionNode = new Node(Token.FUNCTION);
    Node parent = new Node(Token.BLOCK);

    // Act
    boolean result = checkGlobalThis.shouldTraverse(t, functionNode, parent);

    // Assert
    assertFalse(result);
  }

  @Test
  public void testShouldTraverse_FunctionIsConstructor_NoTraverse() {
    // Arrange
    NodeTraversal t = new NodeTraversal();

    Node functionNode = new Node(Token.FUNCTION);
    JSDocInfo jsDoc = new JSDocInfo();
    jsDoc.setConstructor(true);
    functionNode.setJSDocInfo(jsDoc);
    Node parent = new Node(Token.SCRIPT);

    // Act
    boolean result = checkGlobalThis.shouldTraverse(t, functionNode, parent);

    // Assert
    assertFalse(result);
  }

  @Test
  public void testShouldTraverse_FunctionHasThisAnnotation_NoTraverse() {
    // Arrange
    NodeTraversal t = new NodeTraversal();

    Node functionNode = new Node(Token.FUNCTION);
    JSDocInfo jsDoc = new JSDocInfo();
    jsDoc.recordThisType("SomeType");
    functionNode.setJSDocInfo(jsDoc);
    Node parent = new Node(Token.SCRIPT);

    // Act
    boolean result = checkGlobalThis.shouldTraverse(t, functionNode, parent);

    // Assert
    assertFalse(result);
  }

  @Test
  public void testShouldTraverse_FunctionHasOverrideAnnotation_NoTraverse() {
    // Arrange
    NodeTraversal t = new NodeTraversal();

    Node functionNode = new Node(Token.FUNCTION);
    JSDocInfo jsDoc = new JSDocInfo();
    jsDoc.setOverride(true);
    functionNode.setJSDocInfo(jsDoc);
    Node parent = new Node(Token.SCRIPT);

    // Act
    boolean result = checkGlobalThis.shouldTraverse(t, functionNode, parent);

    // Assert
    assertFalse(result);
  }

  @Test
  public void testShouldTraverse_ParentIsNotBlockScriptNameAssign_NoTraverse() {
    // Arrange
    NodeTraversal t = new NodeTraversal();

    Node functionNode = new Node(Token.FUNCTION);
    Integer[] parentTypes = {Token.BLOCK, Token.SCRIPT, Token.NAME, Token.ASSIGN};

    for (Integer parentType : parentTypes) {
      Node parent = new Node(parentType);

      // Act
      boolean result = checkGlobalThis.shouldTraverse(t, functionNode, parent);

      // Assert
      assertFalse(result);
    }
  }

  @Test
  public void testShouldTraverse_ParentIsAssignThisIsLhs_Traverse() {
    // Arrange
    NodeTraversal t = new NodeTraversal();

    Node functionNode = new Node(Token.FUNCTION);
    Node parent = new Node(Token.ASSIGN);
    parent.addChildToFront(functionNode);
    parent.addChildToBack(functionNode.cloneTree());

    // Act
    boolean result = checkGlobalThis.shouldTraverse(t, functionNode, parent);

    // Assert
    assertTrue(result);
  }

  @Test
  public void testShouldTraverse_ParentIsAssignThisIsRhsNoGet_Traverse() {
    // Arrange
    NodeTraversal t = new NodeTraversal();

    Node functionNode = new Node(Token.FUNCTION);
    Node parent = new Node(Token.ASSIGN);
    parent.addChildToBack(functionNode);
    parent.addChildToFront(functionNode.cloneTree());

    // Act
    boolean result = checkGlobalThis.shouldTraverse(t, functionNode, parent);

    // Assert
    assertTrue(result);
  }

  @Test
  public void testShouldTraverse_ParentIsAssignThisIsRhsGetNoPrototype_Traverse() {
    // Arrange
    NodeTraversal t = new NodeTraversal();

    Node functionNode = new Node(Token.FUNCTION);
    Node grandParent = new Node(Token.GETPROP, NodeUtil.newName("prototype"));
    Node parent = new Node(Token.GETPROP, NodeUtil.newName("useful"));
    parent.addChildToFront(grandParent);
    grandParent.addChildToBack(functionNode);

    // Act
    boolean result = checkGlobalThis.shouldTraverse(t, functionNode, parent);

    // Assert
    assertTrue(result);
  }

  @Test
  public void testShouldTraverse_ParentIsAssignThisIsRhsGetWithPrototype_NoTraverse() {
    // Arrange
    NodeTraversal t = new NodeTraversal();

    Node functionNode = new Node(Token.FUNCTION);
    Node grandParent = new Node(Token.GETPROP, NodeUtil.newName("prototype"));
    Node parent = new Node(Token.GETPROP, NodeUtil.newName("useful"));
    parent.addChildToFront(NodeUtil.newName("prototype"));
    parent.addChildToBack(grandParent);
    grandParent.addChildToBack(functionNode);

    // Act
    boolean result = checkGlobalThis.shouldTraverse(t, functionNode, parent);

    // Assert
    assertFalse(result);
  }

  @Test
  public void testVisit_NodeIsThisShouldReportThis() {
    // Arrange
    NodeTraversal t = new NodeTraversal();
    DiagnosticType expectedDiagnostic = DiagnosticType.warning(
        "JSC_USED_GLOBAL_THIS",
        "dangerous use of the global 'this' object");

    Node thisNode = new Node(Token.THIS);
    Node parent = new Node(Token.BLOCK);
    parent.addChildToFront(thisNode);

    // Act
    checkGlobalThis.visit(t, thisNode, parent);
    DiagnosticType reportedDiagnostic = t.getReportedDiagnostic();

    // Assert
    assertEquals(expectedDiagnostic, reportedDiagnostic);
  }

  @Test
  public void testVisit_NodeIsThisIsAssignLhs_NoReportThisOnVisit() {
    // Arrange
    NodeTraversal t = new NodeTraversal();
    DiagnosticType expectedDiagnostic = null;

    Node thisNode = new Node(Token.THIS);
    Node parent = new Node(Token.ASSIGN);
    parent.addChildToFront(thisNode);

    // Act
    checkGlobalThis.visit(t, thisNode, parent);
    DiagnosticType reportedDiagnostic = t.getReportedDiagnostic();

    // Assert
    assertEquals(expectedDiagnostic, reportedDiagnostic);
  }

  @Test
  public void testVisit_AssignLhsChildResetToNullAfterVisit() {
    // Arrange
    NodeTraversal t = new NodeTraversal();
    Node thisNode = new Node(Token.THIS);
    Node assignNode = new Node(Token.ASSIGN, thisNode, NodeUtil.newName("test"));
    Node parent = new Node(Token.BLOCK);
    parent.addChildToFront(assignNode);

    // Act
    checkGlobalThis.visit(t, thisNode, parent);
    Node secondThisNode = thisNode.cloneNode();
    checkGlobalThis.visit(t, secondThisNode, parent);

    // Assert
    assertNotNull(t.getReportedDiagnostic());
    assertNull(t.getReportedDiagnostic());
  }
  
  @Test
  public void testGetFunctionJsDocInfo_FunctionHasJSDoc_ReturnsJSDoc() {
    // Arrange
    Node functionNode = new Node(Token.FUNCTION);
    JSDocInfo expectedJSDoc = new JSDocInfo();
    functionNode.setJSDocInfo(expectedJSDoc);

    // Act
    JSDocInfo result = checkGlobalThis.getFunctionJsDocInfo(functionNode);

    // Assert
    assertEquals(expectedJSDoc, result);
  }

  @Test
  public void testGetFunctionJsDocInfo_FunctionHasParentWithJSDoc_ReturnsParentJSDoc() {
    // Arrange
    Node functionNode = new Node(Token.FUNCTION);
    Node parent = new Node(Token.NAME);
    JSDocInfo expectedJSDoc = new JSDocInfo();
    parent.setJSDocInfo(expectedJSDoc);
    parent.addChildToFront(functionNode);

    // Act
    JSDocInfo result = checkGlobalThis.getFunctionJsDocInfo(functionNode);

    // Assert
    assertEquals(expectedJSDoc, result);
  }

  @Test
  public void testGetFunctionJsDocInfo_FunctionHasParentIsVarWithGrandParentJSDoc_ReturnsGrandParentJSDoc() {
    // Arrange
    Node functionNode = new Node(Token.FUNCTION);
    Node parent = new Node(Token.NAME);
    Node grandParent = new Node(Token.VAR);
    JSDocInfo expectedJSDoc = new JSDocInfo();
    grandParent.setJSDocInfo(expectedJSDoc);
    parent.setParent(grandParent);
    grandParent.addChildToBack(parent);
    parent.addChildToFront(functionNode);

    // Act
    JSDocInfo result = checkGlobalThis.getFunctionJsDocInfo(functionNode);

    // Assert
    assertEquals(expectedJSDoc, result);
  }

  @Test
  public void testGetFunctionJsDocInfo_FunctionDoesNotHaveJSDoc_ReturnsNull() {
    // Arrange
    Node functionNode = new Node(Token.FUNCTION);

    // Act
    JSDocInfo result = checkGlobalThis.getFunctionJsDocInfo(functionNode);

    // Assert
    assertNull(result);
  }

  @Test
  public void testGetFunctionJsDocInfo_FunctionHasParentWithAssignJSDoc_ReturnsNull() {
    // Arrange
    Node functionNode = new Node(Token.FUNCTION);
    Node parent = new Node(Token.ASSIGN);
    JSDocInfo expectedJSDoc = new JSDocInfo();
    parent.setJSDocInfo(expectedJSDoc);
    parent.addChildToFront(functionNode);

    // Act
    JSDocInfo result = checkGlobalThis.getFunctionJsDocInfo(functionNode);

    // Assert
    assertNull(result);
  }
}
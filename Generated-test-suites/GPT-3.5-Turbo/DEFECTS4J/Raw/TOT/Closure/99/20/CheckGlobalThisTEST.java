import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CheckLevel;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.rhino.Node;

public class CheckGlobalThisTest {
  
  private AbstractCompiler compiler;
  
  @Before
  public void setUp() {
    compiler = new AbstractCompiler();
  }
  
  @Test
  public void testShouldTraverse_FunctionIsConstructor_DoesntTraverse() {
    // Arrange
    Node functionNode = new Node(Token.FUNCTION);
    functionNode.setIsConstructor(true);
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler, CheckLevel.WARNING);
    NodeTraversal nodeTraversal = new NodeTraversal(compiler, checkGlobalThis);
    
    // Act
    boolean result = checkGlobalThis.shouldTraverse(nodeTraversal, functionNode, null);
    
    // Assert
    assertFalse(result);
  }
  
  @Test
  public void testShouldTraverse_FunctionHasThisType_DoesntTraverse() {
    // Arrange
    Node functionNode = new Node(Token.FUNCTION);
    functionNode.setJSDocInfo(new JSDocInfo());
    functionNode.getJSDocInfo().setHasThisType(true);
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler, CheckLevel.WARNING);
    NodeTraversal nodeTraversal = new NodeTraversal(compiler, checkGlobalThis);
    
    // Act
    boolean result = checkGlobalThis.shouldTraverse(nodeTraversal, functionNode, null);
    
    // Assert
    assertFalse(result);
  }
  
  @Test
  public void testShouldTraverse_FunctionIsOverride_DoesntTraverse() {
    // Arrange
    Node functionNode = new Node(Token.FUNCTION);
    functionNode.setJSDocInfo(new JSDocInfo());
    functionNode.getJSDocInfo().setOverride(true);
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler, CheckLevel.WARNING);
    NodeTraversal nodeTraversal = new NodeTraversal(compiler, checkGlobalThis);
    
    // Act
    boolean result = checkGlobalThis.shouldTraverse(nodeTraversal, functionNode, null);
    
    // Assert
    assertFalse(result);
  }
  
  @Test
  public void testShouldTraverse_AssignmentLeftSideNotBlockScriptNameAssign_DoesntTraverse() {
    // Arrange
    Node functionNode = new Node(Token.FUNCTION);
    Node parentNode = new Node(Token.ADD);
    parentNode.addChildToBack(functionNode);
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler, CheckLevel.WARNING);
    NodeTraversal nodeTraversal = new NodeTraversal(compiler, checkGlobalThis);
    
    // Act
    boolean result = checkGlobalThis.shouldTraverse(nodeTraversal, functionNode, parentNode);
    
    // Assert
    assertFalse(result);
  }
  
  @Test
  public void testShouldTraverse_AssignmentLeftSideBlock_DoesTraverse() {
    // Arrange
    Node functionNode = new Node(Token.FUNCTION);
    Node parentNode = new Node(Token.BLOCK);
    parentNode.addChildToBack(functionNode);
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler, CheckLevel.WARNING);
    NodeTraversal nodeTraversal = new NodeTraversal(compiler, checkGlobalThis);
    
    // Act
    boolean result = checkGlobalThis.shouldTraverse(nodeTraversal, functionNode, parentNode);
    
    // Assert
    assertTrue(result);
  }
  
  @Test
  public void testShouldTraverse_AssignmentLeftSideScript_DoesTraverse() {
    // Arrange
    Node functionNode = new Node(Token.FUNCTION);
    Node parentNode = new Node(Token.SCRIPT);
    parentNode.addChildToBack(functionNode);
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler, CheckLevel.WARNING);
    NodeTraversal nodeTraversal = new NodeTraversal(compiler, checkGlobalThis);
    
    // Act
    boolean result = checkGlobalThis.shouldTraverse(nodeTraversal, functionNode, parentNode);
    
    // Assert
    assertTrue(result);
  }
  
  @Test
  public void testShouldTraverse_AssignmentLeftSideName_DoesTraverse() {
    // Arrange
    Node functionNode = new Node(Token.FUNCTION);
    Node parentNode = new Node(Token.NAME);
    parentNode.addChildToBack(functionNode);
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler, CheckLevel.WARNING);
    NodeTraversal nodeTraversal = new NodeTraversal(compiler, checkGlobalThis);
    
    // Act
    boolean result = checkGlobalThis.shouldTraverse(nodeTraversal, functionNode, parentNode);
    
    // Assert
    assertTrue(result);
  }
  
  @Test
  public void testShouldTraverse_AssignmentRightSideNotPrototypeProperty_DoesTraverse() {
    // Arrange
    Node lhsNode = new Node(Token.NAME);
    lhsNode.setString("a");
    Node rhsNode = new Node(Token.ADD);
    Node assignmentNode = new Node(Token.ASSIGN, lhsNode, rhsNode);
    Node parentNode = new Node(Token.BLOCK);
    parentNode.addChildToBack(assignmentNode);
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler, CheckLevel.WARNING);
    NodeTraversal nodeTraversal = new NodeTraversal(compiler, checkGlobalThis);
    
    // Act
    boolean result = checkGlobalThis.shouldTraverse(nodeTraversal, rhsNode, assignmentNode);
    
    // Assert
    assertTrue(result);
  }
  
  @Test
  public void testShouldTraverse_AssignmentRightSideIsPrototypeProperty_DoesntTraverse() {
    // Arrange
    Node lhsNode = new Node(Token.GETPROP);
    Node prototypeNode = new Node(Token.STRING);
    prototypeNode.setString("prototype");
    lhsNode.addChildToBack(prototypeNode);
    Node rhsNode = new Node(Token.ADD);
    Node assignmentNode = new Node(Token.ASSIGN, lhsNode, rhsNode);
    Node parentNode = new Node(Token.BLOCK);
    parentNode.addChildToBack(assignmentNode);
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler, CheckLevel.WARNING);
    NodeTraversal nodeTraversal = new NodeTraversal(compiler, checkGlobalThis);
    
    // Act
    boolean result = checkGlobalThis.shouldTraverse(nodeTraversal, rhsNode, assignmentNode);
    
    // Assert
    assertFalse(result);
  }
  
  @Test
  public void testShouldTraverse_AssignmentRightSideQualifiedNameIsPrototype_DoesntTraverse() {
    // Arrange
    Node lhsNode = new Node(Token.GETPROP);
    Node prototypeNode = new Node(Token.STRING);
    prototypeNode.setString("prototype");
    lhsNode.addChildToBack(prototypeNode);
    Node rhsNode = new Node(Token.ADD);
    Node assignmentNode = new Node(Token.ASSIGN, lhsNode, rhsNode);
    Node parentNode = new Node(Token.BLOCK);
    parentNode.addChildToBack(assignmentNode);
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler, CheckLevel.WARNING);
    NodeTraversal nodeTraversal = new NodeTraversal(compiler, checkGlobalThis);
    lhsNode.setQualifiedName("a.prototype.b");
    
    // Act
    boolean result = checkGlobalThis.shouldTraverse(nodeTraversal, rhsNode, assignmentNode);
    
    // Assert
    assertFalse(result);
  }
  
  @Test
  public void testVisit_ThisIsDangerous_DiagnosticReported() {
    // Arrange
    Node thisNode = new Node(Token.THIS);
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler, CheckLevel.WARNING);
    NodeTraversal nodeTraversal = new NodeTraversal(compiler, checkGlobalThis);
    
    // Act
    checkGlobalThis.visit(nodeTraversal, thisNode, null);
    
    // Assert
    assertEquals(1, compiler.getErrors().length);
    assertEquals(CheckGlobalThis.GLOBAL_THIS, compiler.getErrors()[0].getType());
  }
  
  @Test
  public void testVisit_ThisIsOnLeftSideOfAssignment_DiagnosticReported() {
    // Arrange
    Node thisNode = new Node(Token.THIS);
    Node lhsNode = new Node(Token.NAME);
    lhsNode.setString("a");
    Node assignmentNode = new Node(Token.ASSIGN, lhsNode, thisNode);
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler, CheckLevel.WARNING);
    NodeTraversal nodeTraversal = new NodeTraversal(compiler, checkGlobalThis);
    
    // Act
    checkGlobalThis.visit(nodeTraversal, thisNode, assignmentNode);
    
    // Assert
    assertEquals(1, compiler.getErrors().length);
    assertEquals(CheckGlobalThis.GLOBAL_THIS, compiler.getErrors()[0].getType());
  }
  
  @Test
  public void testVisit_ThisIsNotOnLeftSideOfAssignment_NoDiagnosticReported() {
    // Arrange
    Node thisNode = new Node(Token.THIS);
    Node lhsNode = new Node(Token.GETPROP);
    Node rhsNode = new Node(Token.STRING);
    lhsNode.addChildToBack(rhsNode);
    Node assignmentNode = new Node(Token.ASSIGN, lhsNode, thisNode);
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler, CheckLevel.WARNING);
    NodeTraversal nodeTraversal = new NodeTraversal(compiler, checkGlobalThis);
    
    // Act
    checkGlobalThis.visit(nodeTraversal, thisNode, assignmentNode);
    
    // Assert
    assertEquals(0, compiler.getErrors().length);
  }
  
  @Test
  public void testVisit_AssignLhsChildIsNull_AssignLhsChildIsNull() {
    // Arrange
    Node thisNode = new Node(Token.THIS);
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler, CheckLevel.WARNING);
    NodeTraversal nodeTraversal = new NodeTraversal(compiler, checkGlobalThis);
    checkGlobalThis.visit(nodeTraversal, thisNode, null);
    
    // Act
    Node result = checkGlobalThis.assignLhsChild;
    
    // Assert
    assertNull(result);
  }
  
  @Test
  public void testVisit_AssignLhsChildIsNotNull_AssignLhsChildIsNull() {
    // Arrange
    Node thisNode = new Node(Token.THIS);
    Node lhsNode = new Node(Token.NAME);
    lhsNode.setString("a");
    Node assignmentNode = new Node(Token.ASSIGN, lhsNode, thisNode);
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler, CheckLevel.WARNING);
    NodeTraversal nodeTraversal = new NodeTraversal(compiler, checkGlobalThis);
    checkGlobalThis.assignLhsChild = lhsNode;
    
    // Act
    checkGlobalThis.visit(nodeTraversal, thisNode, assignmentNode);
    Node result = checkGlobalThis.assignLhsChild;
    
    // Assert
    assertNull(result);
  }
  
  @Test
  public void testShouldReportThis_AssignLhsChildIsNull_ReturnsTrue() {
    // Arrange
    Node thisNode = new Node(Token.THIS);
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler, CheckLevel.WARNING);
    NodeTraversal nodeTraversal = new NodeTraversal(compiler, checkGlobalThis);
    checkGlobalThis.assignLhsChild = null;
    Node parent = new Node(Token.NAME);
    
    // Act
    boolean result = checkGlobalThis.shouldReportThis(thisNode, parent);
    
    // Assert
    assertTrue(result);
  }
  
  @Test
  public void testShouldReportThis_AssignLhsChildIsNotNull_ReturnsFalse() {
    // Arrange
    Node thisNode = new Node(Token.THIS);
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler, CheckLevel.WARNING);
    NodeTraversal nodeTraversal = new NodeTraversal(compiler, checkGlobalThis);
    checkGlobalThis.assignLhsChild = new Node(Token.NAME);
    Node parent = new Node(Token.NAME);
    
    // Act
    boolean result = checkGlobalThis.shouldReportThis(thisNode, parent);
    
    // Assert
    assertFalse(result);
  }
}
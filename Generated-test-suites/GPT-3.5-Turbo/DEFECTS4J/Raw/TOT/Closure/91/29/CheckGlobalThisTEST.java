import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import com.google.javascript.rhino.Node;

@RunWith(MockitoJUnitRunner.class)
public class CheckGlobalThisTest {

  private CheckGlobalThis checkGlobalThis;
  private AbstractCompiler compiler;
  private NodeTraversal t;
  private Node n;
  private Node parent;

  @Before
  public void setup() {
    compiler = mock(AbstractCompiler.class);
    checkGlobalThis = new CheckGlobalThis(compiler, CheckLevel.WARNING);
    n = mock(Node.class);
    parent = mock(Node.class);
    t = mock(NodeTraversal.class);
  }

  @Test
  public void testShouldTraverse_GlobalContext_ReturnsTrue() {
    // Arrange
    when(n.getType()).thenReturn(Token.GETPROP);
    
    // Act
    boolean result = checkGlobalThis.shouldTraverse(t, n, parent);
    
    // Assert
    assertEquals(true, result);
  }
  
  @Test
  public void testShouldTraverse_ConstructorAnnotatedFunction_ReturnsFalse() {
    // Arrange
    when(n.getType()).thenReturn(Token.FUNCTION);
    when(parent.getType()).thenReturn(Token.ASSIGN);
    when(parent.getJSDocInfo()).thenReturn(getJSDocInfo(true, false, false, false));
    
    // Act
    boolean result = checkGlobalThis.shouldTraverse(t, n, parent);
    
    // Assert
    assertEquals(false, result);
  }
  
  @Test
  public void testShouldTraverse_InterfaceAnnotatedFunction_ReturnsFalse() {
    // Arrange
    when(n.getType()).thenReturn(Token.FUNCTION);
    when(parent.getType()).thenReturn(Token.ASSIGN);
    when(parent.getJSDocInfo()).thenReturn(getJSDocInfo(false, true, false, false));
    
    // Act
    boolean result = checkGlobalThis.shouldTraverse(t, n, parent);
    
    // Assert
    assertEquals(false, result);
  }
  
  @Test
  public void testShouldTraverse_ThisTypeAnnotatedFunction_ReturnsFalse() {
    // Arrange
    when(n.getType()).thenReturn(Token.FUNCTION);
    when(parent.getType()).thenReturn(Token.ASSIGN);
    when(parent.getJSDocInfo()).thenReturn(getJSDocInfo(false, false, true, false));
    
    // Act
    boolean result = checkGlobalThis.shouldTraverse(t, n, parent);
    
    // Assert
    assertEquals(false, result);
  }
  
  @Test
  public void testShouldTraverse_OverrideAnnotatedFunction_ReturnsFalse() {
    // Arrange
    when(n.getType()).thenReturn(Token.FUNCTION);
    when(parent.getType()).thenReturn(Token.ASSIGN);
    when(parent.getJSDocInfo()).thenReturn(getJSDocInfo(false, false, false, true));
    
    // Act
    boolean result = checkGlobalThis.shouldTraverse(t, n, parent);
    
    // Assert
    assertEquals(false, result);
  }
  
  private JSDocInfo getJSDocInfo(boolean isConstructor, boolean isInterface, boolean hasThisType, boolean isOverride) {
    JSDocInfo jsDoc = new JSDocInfo();
    jsDoc.setConstructor(isConstructor);
    jsDoc.setInterface(isInterface);
    jsDoc.setHasThisType(hasThisType);
    jsDoc.setOverride(isOverride);
    return jsDoc;
  }

  @Test
  public void testVisit_ThisOnLeftSideOfAssignmentAndShouldReportThis_ReportGlobalThis() {
    // Arrange
    when(n.getType()).thenReturn(Token.THIS);
    when(checkGlobalThis.shouldReportThis(n, parent)).thenReturn(true);
    
    // Act
    checkGlobalThis.visit(t, n, parent);
    
    // Assert
    verify(compiler, times(1)).report(t.makeError(n, CheckLevel.WARNING, CheckGlobalThis.GLOBAL_THIS));
  }
  
  @Test
  public void testVisit_NotThisOnLeftSideOfAssignmentAndShouldReportThis_DoNotReportGlobalThis() {
    // Arrange
    when(n.getType()).thenReturn(Token.NAME);
    when(checkGlobalThis.shouldReportThis(n, parent)).thenReturn(true);
    
    // Act
    checkGlobalThis.visit(t, n, parent);
    
    // Assert
    verify(compiler, never()).report(t.makeError(n, CheckLevel.WARNING, CheckGlobalThis.GLOBAL_THIS));
  }
  
  @Test
  public void testVisit_NotThisOnLeftSideOfAssignmentAndShouldNotReportThis_DoNotReportGlobalThis() {
    // Arrange
    when(n.getType()).thenReturn(Token.NAME);
    when(checkGlobalThis.shouldReportThis(n, parent)).thenReturn(false);
    
    // Act
    checkGlobalThis.visit(t, n, parent);
    
    // Assert
    verify(compiler, never()).report(t.makeError(n, CheckLevel.WARNING, CheckGlobalThis.GLOBAL_THIS));
  }
  
  @Test
  public void testVisit_ThisOnLeftSideOfAssignmentAndShouldNotReportThis_DoNotReportGlobalThis() {
    // Arrange
    when(n.getType()).thenReturn(Token.THIS);
    when(checkGlobalThis.shouldReportThis(n, parent)).thenReturn(false);
    
    // Act
    checkGlobalThis.visit(t, n, parent);
    
    // Assert
    verify(compiler, never()).report(t.makeError(n, CheckLevel.WARNING, CheckGlobalThis.GLOBAL_THIS));
  }
  
  @Test
  public void testVisit_NodeEqualsAssignLhsChild_AssignLhsChildIsReset() {
    // Arrange
    Node assignLhsChild = mock(Node.class);
    checkGlobalThis.assignLhsChild = assignLhsChild;
    
    // Act
    checkGlobalThis.visit(t, assignLhsChild, parent);
    
    // Assert
    assertEquals(null, checkGlobalThis.assignLhsChild);
  }
  
  @Test
  public void testVisit_NodeNotEqualsAssignLhsChild_AssignLhsChildIsNotReset() {
    // Arrange
    Node assignLhsChild = mock(Node.class);
    checkGlobalThis.assignLhsChild = assignLhsChild;
    
    // Act
    checkGlobalThis.visit(t, n, parent);
    
    // Assert
    assertEquals(assignLhsChild, checkGlobalThis.assignLhsChild);
  }
  
  private Node createSampleNode(int nodeType) {
    Node node = mock(Node.class);
    when(node.getType()).thenReturn(nodeType);
    return node;
  }

  private Node createSampleParent(int parentType) {
    Node parent = mock(Node.class);
    when(parent.getType()).thenReturn(parentType);
    return parent;
  }
  
  @Test
  public void testVisit_ParentIsNull_NodeNotReported() {
    // Arrange
    when(n.getType()).thenReturn(Token.THIS);
    when(checkGlobalThis.shouldReportThis(n, null)).thenReturn(true);
    
    // Act
    checkGlobalThis.visit(t, n, null);
    
    // Assert
    verify(compiler, never()).report(t.makeError(n, CheckLevel.WARNING, CheckGlobalThis.GLOBAL_THIS));
  }
  
  @Test
  public void testVisit_ParentIsNotGet_NodeNotReported() {
    // Arrange
    when(n.getType()).thenReturn(Token.THIS);
    when(checkGlobalThis.shouldReportThis(n, parent)).thenReturn(true);
    when(NodeUtil.isGet(parent)).thenReturn(false);
    
    // Act
    checkGlobalThis.visit(t, n, parent);
    
    // Assert
    verify(compiler, never()).report(t.makeError(n, CheckLevel.WARNING, CheckGlobalThis.GLOBAL_THIS));
  }
  
  @Test
  public void testVisit_ParentIsGetPropAndHasPrototypeAsLastChild_NodeNotReported() {
    // Arrange
    when(n.getType()).thenReturn(Token.THIS);
    when(checkGlobalThis.shouldReportThis(n, parent)).thenReturn(true);
    when(NodeUtil.isGet(parent)).thenReturn(true);
    Node prototypeChild = createSampleNode(Token.STRING);
    when(prototypeChild.getString()).thenReturn("prototype");
    Node lhs = createSampleNode(Token.GETPROP);
    when(lhs.getLastChild()).thenReturn(prototypeChild);
    when(parent.getFirstChild()).thenReturn(lhs);
    
    // Act
    checkGlobalThis.visit(t, n, parent);
    
    // Assert
    verify(compiler, never()).report(t.makeError(n, CheckLevel.WARNING, CheckGlobalThis.GLOBAL_THIS));
  }
  
  @Test
  public void testVisit_ParentIsGetPropAndHasNestedPrototypeAsLastChild_NodeNotReported() {
    // Arrange
    when(n.getType()).thenReturn(Token.THIS);
    when(checkGlobalThis.shouldReportThis(n, parent)).thenReturn(true);
    when(NodeUtil.isGet(parent)).thenReturn(true);
    Node prototypeChild = createSampleNode(Token.STRING);
    when(prototypeChild.getString()).thenReturn("prototype");
    Node grandParent = createSampleParent(Token.VAR);
    when(grandParent.getFirstChild()).thenReturn(parent);
    Node lhs = createSampleNode(Token.GETPROP);
    when(lhs.getLastChild()).thenReturn(prototypeChild);
    Node llhs = createSampleNode(Token.GETPROP);
    when(llhs.getLastChild()).thenReturn(prototypeChild);
    when(llhs.getParent()).thenReturn(lhs);
    when(lhs.getFirstChild()).thenReturn(llhs);
    when(parent.getParent()).thenReturn(grandParent);
    
    // Act
    checkGlobalThis.visit(t, n, parent);
    
    // Assert
    verify(compiler, never()).report(t.makeError(n, CheckLevel.WARNING, CheckGlobalThis.GLOBAL_THIS));
  }
  
  @Test
  public void testVisit_ParentIsGetPropAndNotPrototypeAsLastChild_ReportGlobalThis() {
    // Arrange
    when(n.getType()).thenReturn(Token.THIS);
    when(checkGlobalThis.shouldReportThis(n, parent)).thenReturn(true);
    when(NodeUtil.isGet(parent)).thenReturn(true);
    Node nonPrototypeChild = createSampleNode(Token.STRING);
    when(nonPrototypeChild.getString()).thenReturn("x");
    Node lhs = createSampleNode(Token.GETPROP);
    when(lhs.getLastChild()).thenReturn(nonPrototypeChild);
    when(parent.getFirstChild()).thenReturn(lhs);
    
    // Act
    checkGlobalThis.visit(t, n, parent);
    
    // Assert
    verify(compiler, times(1)).report(t.makeError(n, CheckLevel.WARNING, CheckGlobalThis.GLOBAL_THIS));
  }

}
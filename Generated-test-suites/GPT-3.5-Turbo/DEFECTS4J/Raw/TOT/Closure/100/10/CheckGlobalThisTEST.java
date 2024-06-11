import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class CheckGlobalThisTest {

  private CheckGlobalThis checkGlobalThis;

  @Before
  public void setup() {
    checkGlobalThis = new CheckGlobalThis(new AbstractCompiler(), CheckLevel.WARNING);
  }

  @Test
  public void testShouldTraverse_FunctionConstructorWithThisAnnotation_ReturnsFalse() {
    // Arrange
    NodeTraversal t = createNodeTraversalMock();
    Node n = createNodeMock(Token.FUNCTION, "FunctionConstructor");
    Node parent = createNodeMock(Token.NAME, "Parent");
    JSDocInfo jsDoc = createJsDocInfoMock();
    jsDoc.recordThisType();
    jsDoc.recordConstructor();
    when(n.getJSDocInfo()).thenReturn(jsDoc);
    
    // Act
    boolean result = checkGlobalThis.shouldTraverse(t, n, parent);

    // Assert
    assertFalse(result);
    verify(n, times(1)).getJSDocInfo();
  }

  @Test
  public void testShouldTraverse_FunctionConstructor_ReturnsFalse() {
    // Arrange
    NodeTraversal t = createNodeTraversalMock();
    Node n = createNodeMock(Token.FUNCTION, "FunctionConstructor");
    Node parent = createNodeMock(Token.NAME, "Parent");
    JSDocInfo jsDoc = createJsDocInfoMock();
    jsDoc.recordConstructor();
    when(n.getJSDocInfo()).thenReturn(jsDoc);
    
    // Act
    boolean result = checkGlobalThis.shouldTraverse(t, n, parent);

    // Assert
    assertFalse(result);
    verify(n, times(1)).getJSDocInfo();
  }

  @Test
  public void testShouldTraverse_FunctionWithThisAnnotation_ReturnsFalse() {
    // Arrange
    NodeTraversal t = createNodeTraversalMock();
    Node n = createNodeMock(Token.FUNCTION, "Function");
    Node parent = createNodeMock(Token.NAME, "Parent");
    JSDocInfo jsDoc = createJsDocInfoMock();
    jsDoc.recordThisType();
    when(n.getJSDocInfo()).thenReturn(jsDoc);
    
    // Act
    boolean result = checkGlobalThis.shouldTraverse(t, n, parent);

    // Assert
    assertFalse(result);
    verify(n, times(1)).getJSDocInfo();
  }

  @Test
  public void testShouldTraverse_Function_ReturnsTrue() {
    // Arrange
    NodeTraversal t = createNodeTraversalMock();
    Node n = createNodeMock(Token.FUNCTION, "Function");
    Node parent = createNodeMock(Token.NAME, "Parent");
    when(n.getJSDocInfo()).thenReturn(null);
    
    // Act
    boolean result = checkGlobalThis.shouldTraverse(t, n, parent);

    // Assert
    assertTrue(result);
    verify(n, times(1)).getJSDocInfo();
  }

  @Test
  public void testShouldTraverse_AssignmentLeftSide_ReturnsTrue() {
    // Arrange
    NodeTraversal t = createNodeTraversalMock();
    Node n = createNodeMock(Token.GENEric_NAME, "a");
    Node parent = createNodeMock(Token.ASSIGN, "=");
    Node lhs = createNodeMock(Token.NAME, "a");
    Node rhs = createNodeMock(Token.THIS, "this");
    lhs.addChildToBack(rhs);
    parent.addChildToBack(lhs);
    CheckGlobalThis assignLhsNodeMock = mock(CheckGlobalThis.class);
    assignLhsNodeMock.assignLhsChild = lhs;
    checkGlobalThis = spy(checkGlobalThis);
    doReturn(assignLhsNodeMock).when(checkGlobalThis).setAssignLhsChild(any());
    
    // Act
    boolean result = checkGlobalThis.shouldTraverse(t, n, parent);

    // Assert
    assertTrue(result);
    verify(checkGlobalThis, times(1)).setAssignLhsChild(any());
  }

  @Test
  public void testShouldTraverse_PrototypePropertyAssignment_ReturnsFalse() {
    // Arrange
    NodeTraversal t = createNodeTraversalMock();
    Node n = createNodeMock(Token.NAME, "prototype");
    Node parent = createNodeMock(Token.GETPROP, ".");
    Node lhs = createNodeMock(Token.GETPROP, ".");
    Node prototypeProperty = createNodeMock(Token.STRING, "prototype");
    lhs.addChildToBack(prototypeProperty);
    parent.addChildToBack(lhs);
    CheckGlobalThis assignLhsNodeMock = mock(CheckGlobalThis.class);
    assignLhsNodeMock.assignLhsChild = lhs;
    checkGlobalThis = spy(checkGlobalThis);
    doReturn(assignLhsNodeMock).when(checkGlobalThis).setAssignLhsChild(any());
    
    // Act
    boolean result = checkGlobalThis.shouldTraverse(t, n, parent);

    // Assert
    assertFalse(result);
    verify(checkGlobalThis, never()).setAssignLhsChild(any());
  }

  @Test
  public void testShouldTraverse_PrototypeSubpropertyAssignment_ReturnsFalse() {
    // Arrange
    NodeTraversal t = createNodeTraversalMock();
    Node n = createNodeMock(Token.NAME, "subproperty");
    Node parent = createNodeMock(Token.GETPROP, ".");
    Node lhs = createNodeMock(Token.GETPROP, ".");
    Node prototypeProperty = createNodeMock(Token.STRING, "prototype");
    lhs.addChildToBack(prototypeProperty);
    parent.addChildToBack(lhs);
    CheckGlobalThis assignLhsNodeMock = mock(CheckGlobalThis.class);
    assignLhsNodeMock.assignLhsChild = lhs;
    checkGlobalThis = spy(checkGlobalThis);
    doReturn(assignLhsNodeMock).when(checkGlobalThis).setAssignLhsChild(any());
    
    // Act
    boolean result = checkGlobalThis.shouldTraverse(t, n, parent);

    // Assert
    assertFalse(result);
    verify(checkGlobalThis, never()).setAssignLhsChild(any());
  }

  @Test
  public void testShouldTraverse_AssignmentRightSide_PrototypePropertyAssignment_ReturnsFalse() {
    // Arrange
    NodeTraversal t = createNodeTraversalMock();
    Node n = createNodeMock(Token.THIS, "this");
    Node parent = createNodeMock(Token.GETPROP, ".");
    Node lhs = createNodeMock(Token.GETPROP, ".");
    Node prototypeProperty = createNodeMock(Token.STRING, "prototype");
    lhs.addChildToBack(prototypeProperty);
    parent.addChildToBack(lhs);
    CheckGlobalThis assignLhsNodeMock = mock(CheckGlobalThis.class);
    assignLhsNodeMock.assignLhsChild = null;
    checkGlobalThis = spy(checkGlobalThis);
    doReturn(assignLhsNodeMock).when(checkGlobalThis).setAssignLhsChild(any());
    
    // Act
    boolean result = checkGlobalThis.shouldTraverse(t, n, parent);

    // Assert
    assertFalse(result);
    verify(checkGlobalThis, never()).setAssignLhsChild(any());
  }

  @Test
  public void testVisit_ThisOnLeftSideAndShouldReport_ReturnsTrue() {
    // Arrange
    NodeTraversal t = createNodeTraversalMock();
    Node n = createNodeMock(Token.THIS, "this");
    Node parent = createNodeMock(Token.ASSIGN, "=");
    CheckGlobalThis checkGlobalThisMock = mock(CheckGlobalThis.class);
    checkGlobalThisMock.assignLhsChild = n;
    checkGlobalThis = spy(checkGlobalThis);
    doReturn(checkGlobalThisMock).when(checkGlobalThis).setAssignLhsChild(any());
    DiagnosticType diagnosticType = createDiagnosticTypeMock();
    when(checkGlobalThis.shouldReportThis(n, parent)).thenReturn(true);
    Compiler compiler = mock(Compiler.class);
    ErrorReporter errorReporter = createErrorReporterMock();
    when(t.makeError(n, CheckLevel.WARNING, diagnosticType)).thenReturn(errorReporter);
    
    // Act
    checkGlobalThis.visit(t, n, parent);

    // Assert
    verify(checkGlobalThis, times(1)).setAssignLhsChild(any());
    verify(checkGlobalThis, times(1)).shouldReportThis(n, parent);
    verify(t, times(1)).makeError(n, CheckLevel.WARNING, diagnosticType);
    verify(compiler, times(1)).report(errorReporter);
  }

  @Test
  public void testVisit_AssignLhsChildIsNull() {
    // Arrange
    NodeTraversal t = createNodeTraversalMock();
    Node n = createNodeMock(Token.THIS, "this");
    Node parent = createNodeMock(Token.ASSIGN, "=");
    CheckGlobalThis checkGlobalThisMock = mock(CheckGlobalThis.class);
    checkGlobalThisMock.assignLhsChild = null;
    checkGlobalThis = spy(checkGlobalThis);
    doReturn(checkGlobalThisMock).when(checkGlobalThis).setAssignLhsChild(any());
    
    // Act
    checkGlobalThis.visit(t, n, parent);

    // Assert
    verify(checkGlobalThis, times(1)).setAssignLhsChild(any());
    verify(checkGlobalThis, never()).shouldReportThis(any(), any());
  }

  @Test
  public void testVisit_AssignLhsChildIsNotNull() {
    // Arrange
    NodeTraversal t = createNodeTraversalMock();
    Node n = createNodeMock(Token.THIS, "this");
    Node parent = createNodeMock(Token.ASSIGN, "=");
    CheckGlobalThis checkGlobalThisMock = mock(CheckGlobalThis.class);
    checkGlobalThisMock.assignLhsChild = createNodeMock(Token.GENEric_NAME, "a");
    checkGlobalThis = spy(checkGlobalThis);
    doReturn(checkGlobalThisMock).when(checkGlobalThis).setAssignLhsChild(any());
    
    // Act
    checkGlobalThis.visit(t, n, parent);

    // Assert
    verify(checkGlobalThis, times(1)).setAssignLhsChild(any());
    verify(checkGlobalThis, never()).shouldReportThis(any(), any());
  }

  @Test
  public void testVisit_AssignLhsChildIsNotNullButNotMatch() {
    // Arrange
    NodeTraversal t = createNodeTraversalMock();
    Node n = createNodeMock(Token.THIS, "this");
    Node parent = createNodeMock(Token.ASSIGN, "=");
    CheckGlobalThis checkGlobalThisMock = mock(CheckGlobalThis.class);
    checkGlobalThisMock.assignLhsChild = createNodeMock(Token.GENEric_NAME, "b");
    checkGlobalThis = spy(checkGlobalThis);
    doReturn(checkGlobalThisMock).when(checkGlobalThis).setAssignLhsChild(any());
    
    // Act
    checkGlobalThis.visit(t, n, parent);

    // Assert
    verify(checkGlobalThis, times(1)).setAssignLhsChild(any());
    verify(checkGlobalThis, never()).shouldReportThis(any(), any());
  }

  // Helper methods to create mocks for testing

  private NodeTraversal createNodeTraversalMock() {
    return mock(NodeTraversal.class);
  }

  private Node createNodeMock(int type, String name) {
    Node node = mock(Node.class);
    when(node.getType()).thenReturn(type);
    when(node.getString()).thenReturn(name);
    return node;
  }

  private JSDocInfo createJsDocInfoMock() {
    return mock(JSDocInfo.class);
  }

  private DiagnosticType createDiagnosticTypeMock() {
    return mock(DiagnosticType.class);
  }

  private ErrorReporter createErrorReporterMock() {
    return mock(ErrorReporter.class);
  }
}
import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class CheckGlobalThisTest {

  private CheckGlobalThis checkGlobalThis;

  @Before
  public void setUp() {
    checkGlobalThis = new CheckGlobalThis();
  }

  // Test cases for shouldTraverse method

  @Test
  public void testShouldTraverse_withFunctionConstructorAnnotation_shouldReturnFalse() {
    NodeTraversal t = createMockNodeTraversal();
    Node n = createMockFunctionNode();
    Node parent = createMockParentNode();
    JSDocInfo jsDoc = createMockJSDocInfo(true, false, false, false);
    when(n.getJSDocInfo()).thenReturn(jsDoc);
    when(parent.getType()).thenReturn(Token.SCRIPT);
    
    assertFalse(checkGlobalThis.shouldTraverse(t, n, parent));
  }

  @Test
  public void testShouldTraverse_withFunctionThisAnnotation_shouldReturnFalse() {
    NodeTraversal t = createMockNodeTraversal();
    Node n = createMockFunctionNode();
    Node parent = createMockParentNode();
    JSDocInfo jsDoc = createMockJSDocInfo(false, true, false, false);
    when(n.getJSDocInfo()).thenReturn(jsDoc);
    when(parent.getType()).thenReturn(Token.SCRIPT);
    
    assertFalse(checkGlobalThis.shouldTraverse(t, n, parent));
  }

  @Test
  public void testShouldTraverse_withFunctionOverrideAnnotation_shouldReturnFalse() {
    NodeTraversal t = createMockNodeTraversal();
    Node n = createMockFunctionNode();
    Node parent = createMockParentNode();
    JSDocInfo jsDoc = createMockJSDocInfo(false, false, true, false);
    when(n.getJSDocInfo()).thenReturn(jsDoc);
    when(parent.getType()).thenReturn(Token.SCRIPT);
    
    assertFalse(checkGlobalThis.shouldTraverse(t, n, parent));
  }

  @Test
  public void testShouldTraverse_withFunctionInterfaceAnnotation_shouldReturnFalse() {
    NodeTraversal t = createMockNodeTraversal();
    Node n = createMockFunctionNode();
    Node parent = createMockParentNode();
    JSDocInfo jsDoc = createMockJSDocInfo(false, false, false, true);
    when(n.getJSDocInfo()).thenReturn(jsDoc);
    when(parent.getType()).thenReturn(Token.SCRIPT);
    
    assertFalse(checkGlobalThis.shouldTraverse(t, n, parent));
  }

  @Test
  public void testShouldTraverse_withFunctionNestedAssignment_shouldReturnTrue() {
    NodeTraversal t = createMockNodeTraversal();
    Node n = createMockFunctionNode();
    Node parent = createMockAssignmentNode();
    when(parent.getFirstChild()).thenReturn(createMockChildNode());
    
    assertTrue(checkGlobalThis.shouldTraverse(t, n, parent));
    assertNull(checkGlobalThis.assignLhsChild);
  }

  @Test
  public void testShouldTraverse_withFunctionGetAssignmentToPrototype_shouldReturnFalse() {
    NodeTraversal t = createMockNodeTraversal();
    Node n = createMockFunctionNode();
    Node parent = createMockAssignmentNode();
    when(parent.getFirstChild()).thenReturn(createMockGetPropNode("prototype"));
    
    assertFalse(checkGlobalThis.shouldTraverse(t, n, parent));
  }

  @Test
  public void testShouldTraverse_withFunctionGetAssignmentToNonPrototype_shouldReturnTrue() {
    NodeTraversal t = createMockNodeTraversal();
    Node n = createMockFunctionNode();
    Node parent = createMockAssignmentNode();
    when(parent.getFirstChild()).thenReturn(createMockGetPropNode("nonprototype"));
    
    assertTrue(checkGlobalThis.shouldTraverse(t, n, parent));
    assertNull(checkGlobalThis.assignLhsChild);
  }

  // Test cases for visit method

  @Test
  public void testVisit_withThisKeywordShouldReportThis() {
    NodeTraversal t = createMockNodeTraversal();
    Node n = createMockThisNode();
    Node parent = createMockParentNode();
    when(checkGlobalThis.shouldReportThis(n)).thenReturn(true);
    DiagnosticType diagnosticType = createMockDiagnosticType();
    when(t.makeError(n, diagnosticType)).thenReturn(createMockDiagnostic());
    AbstractCompiler compiler = createMockAbstractCompiler();
    when(t.getCompiler()).thenReturn(compiler);

    checkGlobalThis.visit(t, n, parent);

    verify(compiler).report(any(Diagnostic.class));
    verify(checkGlobalThis).shouldReportThis(n);
  }

  @Test
  public void testVisit_withAssignmentLhsChildSetToFirstChild_shouldClearAssignmentLhsChild() {
    NodeTraversal t = createMockNodeTraversal();
    Node n = createMockNode();
    Node parent = createMockNode();
    Node assignLhsChild = createMockNode();
    when(checkGlobalThis.assignLhsChild).thenReturn(assignLhsChild);
    when(n).thenReturn(assignLhsChild);

    checkGlobalThis.visit(t, n, parent);

    assertNull(checkGlobalThis.assignLhsChild);
  }

  @Test
  public void testVisit_withAssignmentLhsChildNotSetToFirstChild_shouldNotClearAssignmentLhsChild() {
    NodeTraversal t = createMockNodeTraversal();
    Node n = createMockNode();
    Node parent = createMockNode();
    Node assignLhsChild = createMockNode();
    when(checkGlobalThis.assignLhsChild).thenReturn(assignLhsChild);
    when(n).thenReturn(createMockNode());

    checkGlobalThis.visit(t, n, parent);

    assertSame(assignLhsChild, checkGlobalThis.assignLhsChild);
  }

  // Test cases for shouldReportThis method

  @Test
  public void testShouldReportThis_withAssignmentLhsChildIsNull_shouldReturnFalse() {
    Node n = createMockThisNode();
    Node parent = createMockNode();
    when(checkGlobalThis.assignLhsChild).thenReturn(null);
    
    assertFalse(checkGlobalThis.shouldReportThis(n));
  }

  @Test
  public void testShouldReportThis_withAssignmentLhsChildNotNull_shouldReturnTrue() {
    Node n = createMockThisNode();
    Node parent = createMockNode();
    when(checkGlobalThis.assignLhsChild).thenReturn(createMockNode());
    
    assertTrue(checkGlobalThis.shouldReportThis(n));
  }

  // Helper methods

  private NodeTraversal createMockNodeTraversal() {
    return mock(NodeTraversal.class);
  }

  private Node createMockFunctionNode() {
    return mock(Node.class);
  }

  private Node createMockParentNode() {
    return mock(Node.class);
  }

  private JSDocInfo createMockJSDocInfo(boolean isConstructor, boolean hasThisType, boolean isOverride, 
                                       boolean isInterface) {
    JSDocInfo jsDoc = mock(JSDocInfo.class);
    when(jsDoc.isConstructor()).thenReturn(isConstructor);
    when(jsDoc.hasThisType()).thenReturn(hasThisType);
    when(jsDoc.isOverride()).thenReturn(isOverride);
    when(jsDoc.isInterface()).thenReturn(isInterface);
    return jsDoc;
  }

  private Node createMockAssignmentNode() {
    return mock(Node.class);
  }

  private Node createMockChildNode() {
    return mock(Node.class);
  }

  private Node createMockGetPropNode(String propName) {
    Node getPropNode = mock(Node.class);
    Node lastChildNode = mock(Node.class);
    when(getPropNode.getLastChild()).thenReturn(lastChildNode);
    when(lastChildNode.getString()).thenReturn(propName);
    return getPropNode;
  }

  private Node createMockThisNode() {
    return mock(Node.class);
  }

  private DiagnosticType createMockDiagnosticType() {
    return mock(DiagnosticType.class);
  }

  private Diagnostic createMockDiagnostic() {
    return mock(Diagnostic.class);
  }

  private AbstractCompiler createMockAbstractCompiler() {
    return mock(AbstractCompiler.class);
  }

}
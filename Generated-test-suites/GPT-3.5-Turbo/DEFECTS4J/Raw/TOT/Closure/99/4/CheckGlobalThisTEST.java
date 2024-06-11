import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

public class CheckGlobalThisTest {

  private CheckGlobalThis checkGlobalThis;
  private AbstractCompiler compilerMock;
  private DiagnosticType diagnosticTypeMock;
  private NodeTraversal traversalMock;
  private Node nodeMock;
  private Node parentMock;
  private JSDocInfo jsDocMock;
  private ByteArrayOutputStream outContent;
  private PrintStream originalOut;

  @Before
  public void setUp() {
    MockitoAnnotations.initMocks(this);
    compilerMock = mock(AbstractCompiler.class);
    diagnosticTypeMock = mock(DiagnosticType.class);
    traversalMock = mock(NodeTraversal.class);
    nodeMock = mock(Node.class);
    parentMock = mock(Node.class);
    jsDocMock = mock(JSDocInfo.class);
    outContent = new ByteArrayOutputStream();
    originalOut = System.out;
    System.setOut(new PrintStream(outContent));

    checkGlobalThis = new CheckGlobalThis(compilerMock, CheckLevel.WARNING);
  }

  @Test
  public void shouldTraverse_withFunctionConstructorAnnotation_returnsFalse() {
    when(nodeMock.getType()).thenReturn(Token.FUNCTION);
    when(checkGlobalThis.shouldTraverse(traversalMock, nodeMock, parentMock)).thenReturn(false);
    assertEquals(false, checkGlobalThis.shouldTraverse(traversalMock, nodeMock, parentMock));
  }

  @Test
  public void shouldTraverse_withThisAnnotation_returnsFalse() {
    when(nodeMock.getType()).thenReturn(Token.FUNCTION);
    when(checkGlobalThis.shouldTraverse(traversalMock, nodeMock, parentMock)).thenReturn(false);
    assertEquals(false, checkGlobalThis.shouldTraverse(traversalMock, nodeMock, parentMock));
  }

  @Test
  public void shouldTraverse_withNoLogicalPlaceForThisAnnotation_returnsFalse() {
    when(nodeMock.getType()).thenReturn(Token.FUNCTION);
    when(checkGlobalThis.shouldTraverse(traversalMock, nodeMock, parentMock)).thenReturn(false);
    assertEquals(false, checkGlobalThis.shouldTraverse(traversalMock, nodeMock, parentMock));
  }

  @Test
  public void shouldTraverse_withNestedAssignmentAndPrototypeProperty_returnsFalse() {
    when(nodeMock.getType()).thenReturn(Token.FUNCTION);
    when(parentMock.getType()).thenReturn(Token.ASSIGN);
    when(parentMock.getFirstChild()).thenReturn(mock(Node.class)); // lhs node
    when(parentMock.getFirstChild().getNext()).thenReturn(mock(Node.class)); // rhs node
    when(parentMock.getFirstChild().getType()).thenReturn(Token.GETPROP);
    when(parentMock.getFirstChild().getLastChild().getString()).thenReturn("prototype");
    when(checkGlobalThis.shouldTraverse(traversalMock, nodeMock, parentMock)).thenReturn(false);
    assertEquals(false, checkGlobalThis.shouldTraverse(traversalMock, nodeMock, parentMock));
  }

  @Test
  public void shouldTraverse_withNestedAssignmentAndPrototypeSubproperty_returnsFalse() {
    when(nodeMock.getType()).thenReturn(Token.FUNCTION);
    when(parentMock.getType()).thenReturn(Token.ASSIGN);
    when(parentMock.getFirstChild()).thenReturn(mock(Node.class)); // lhs node
    when(parentMock.getFirstChild().getNext()).thenReturn(mock(Node.class)); // rhs node
    when(parentMock.getFirstChild().getType()).thenReturn(Token.GETPROP);
    when(parentMock.getFirstChild().getQualifiedName()).thenReturn("some.prototype.property");
    when(checkGlobalThis.shouldTraverse(traversalMock, nodeMock, parentMock)).thenReturn(false);
    assertEquals(false, checkGlobalThis.shouldTraverse(traversalMock, nodeMock, parentMock));
  }

  @Test
  public void shouldTraverse_withAssignmentLeftHandSide_returnsTrue() {
    when(nodeMock.getType()).thenReturn(Token.NAME);
    when(parentMock.getType()).thenReturn(Token.ASSIGN);
    when(parentMock.getFirstChild()).thenReturn(nodeMock); // lhs node
    when(parentMock.getFirstChild().getNext()).thenReturn(mock(Node.class)); // rhs node
    when(checkGlobalThis.shouldTraverse(traversalMock, nodeMock, parentMock)).thenReturn(true);
    assertEquals(true, checkGlobalThis.shouldTraverse(traversalMock, nodeMock, parentMock));
  }

  @Test
  public void visit_withMatchingGlobalThisReportError() {
    when(nodeMock.getType()).thenReturn(Token.THIS);
    when(checkGlobalThis.shouldReportThis(nodeMock, parentMock)).thenReturn(true);
    checkGlobalThis.visit(traversalMock, nodeMock, parentMock);
    verify(compilerMock, times(1)).report(traversalMock.makeError(nodeMock, CheckLevel.WARNING, CheckGlobalThis.GLOBAL_THIS));
  }

  @Test
  public void visit_withNonMatchingGlobalThisShouldNotReportError() {
    when(nodeMock.getType()).thenReturn(Token.THIS);
    when(checkGlobalThis.shouldReportThis(nodeMock, parentMock)).thenReturn(false);
    checkGlobalThis.visit(traversalMock, nodeMock, parentMock);
    verify(compilerMock, times(0)).report(traversalMock.makeError(nodeMock, CheckLevel.WARNING, CheckGlobalThis.GLOBAL_THIS));
  }

  @Test
  public void visit_withAssignLhsChildNotNull_resetAssignLhsChildToNull() {
    checkGlobalThis.visit(traversalMock, nodeMock, parentMock);
    assertEquals(null, checkGlobalThis.assignLhsChild);
  }

  @Test
  public void getFunctionJsDocInfo_withJSDocInfo_null_returnFunctionParentJSDocInfo() {
    when(nodeMock.getJSDocInfo()).thenReturn(null);
    when(nodeMock.getParent()).thenReturn(parentMock);
    when(parentMock.getType()).thenReturn(Token.NAME);

    when(parentMock.getJSDocInfo()).thenReturn(jsDocMock);
    assertEquals(jsDocMock, checkGlobalThis.getFunctionJsDocInfo(nodeMock));

    when(parentMock.getType()).thenReturn(Token.ASSIGN);
    assertEquals(jsDocMock, checkGlobalThis.getFunctionJsDocInfo(nodeMock));

    when(parentMock.getParent()).thenReturn(mock(Node.class));
    when(parentMock.getParent().getType()).thenReturn(Token.VAR);
    when(parentMock.getType()).thenReturn(Token.NAME);
    assertEquals(jsDocMock, checkGlobalThis.getFunctionJsDocInfo(nodeMock));
  }

  @Test
  public void getFunctionJsDocInfo_withJSDocInfo_notNull_returnNodeJSDocInfo() {
    when(nodeMock.getJSDocInfo()).thenReturn(jsDocMock);
    assertEquals(jsDocMock, checkGlobalThis.getFunctionJsDocInfo(nodeMock));
  }

  @Test
  public void shouldReportThis_withAssignLhsChildNotNull_returnTrue() {
    checkGlobalThis.assignLhsChild = nodeMock;
    assertEquals(true, checkGlobalThis.shouldReportThis(nodeMock, parentMock));
  }

  @Test
  public void shouldReportThis_withNonMatchingParent_returnFalse() {
    when(parentMock.getType()).thenReturn(Token.ASSIGN);
    when(NodeUtil.isGet(parentMock)).thenReturn(false);
    assertEquals(false, checkGlobalThis.shouldReportThis(nodeMock, parentMock));
  }

  @Test
  public void shouldReportThis_withMatchingParent_returnTrue() {
    when(parentMock.getType()).thenReturn(Token.ASSIGN);
    when(NodeUtil.isGet(parentMock)).thenReturn(true);
    assertEquals(true, checkGlobalThis.shouldReportThis(nodeMock, parentMock));
  }

  @Test
  public void shouldReportThis_withMatchingParentAndAssignLhsChildNull_returnFalse() {
    checkGlobalThis.assignLhsChild = null;
    when(parentMock.getType()).thenReturn(Token.ASSIGN);
    when(NodeUtil.isGet(parentMock)).thenReturn(true);
    assertEquals(false, checkGlobalThis.shouldReportThis(nodeMock, parentMock));
  }

  @Test
  public void testCheckGlobalThis() {
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compilerMock, CheckLevel.WARNING);
    assertEquals(compilerMock, checkGlobalThis.compiler);
    assertEquals(CheckLevel.WARNING, checkGlobalThis.level);
  }

  @After
  public void restoreStreams() {
    System.setOut(originalOut);
  }
}
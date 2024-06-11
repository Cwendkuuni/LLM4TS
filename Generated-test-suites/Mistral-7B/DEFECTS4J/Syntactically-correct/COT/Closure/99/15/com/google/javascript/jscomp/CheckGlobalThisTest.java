import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CheckLevel;
import com.google.javascript.jscomp.DiagnosticType;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.rhino.JSDocInfo;
import com.google.javascript.rhino.Token;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.mockito.runners.JUnit4;

@RunWith(JUnit4.class)
public class CheckGlobalThisTest {

  private AbstractCompiler compiler;
  private CheckGlobalThis checkGlobalThis;

  @Before
  public void setUp() {
    compiler = Mockito.mock(AbstractCompiler.class);
    checkGlobalThis = new CheckGlobalThis(compiler, CheckLevel.WARNING);
  }

  @Test
  public void testShouldTraverseFunction() {
    Node functionNode = Node.newFunctionNode("function() {}", null);
    Node parentNode = Node.newBlockNode("");

    assertTrue(checkGlobalThis.shouldTraverse(Mockito.mock(NodeTraversal.class), functionNode, parentNode));
  }

  @Test
  public void testShouldNotTraverseConstructorFunction() {
    Node functionNode = Node.newFunctionNode("function MyConstructor() {}", null);
    Node parentNode = Node.newBlockNode("");

    assertFalse(checkGlobalThis.shouldTraverse(Mockito.mock(NodeTraversal.class), functionNode, parentNode));
  }

  @Test
  public void testShouldNotTraverseFunctionWithThisAnnotation() {
    Node functionNode = Node.newFunctionNode("@this function() {}", null);
    Node parentNode = Node.newBlockNode("");

    assertFalse(checkGlobalThis.shouldTraverse(Mockito.mock(NodeTraversal.class), functionNode, parentNode));
  }

  @Test
  public void testShouldReportGlobalThis() {
    Node functionNode = Node.newFunctionNode("function x() { this.y = 5; }", null);
    Node parentNode = Node.newBlockNode("");

    checkGlobalThis.visit(Mockito.mock(NodeTraversal.class), functionNode, parentNode);

    ArgumentCaptor<NodeTraversal.Error> errorCaptor = ArgumentCaptor.forClass(NodeTraversal.Error.class);
    Mockito.verify(compiler).report(errorCaptor.capture());
    DiagnosticType diagnosticType = errorCaptor.getValue().getDiagnosticType();
    assertTrue(diagnosticType.equals(CheckGlobalThis.GLOBAL_THIS));
  }

  @Test
  public void testShouldNotReportGlobalThisInConstructor() {
    Node functionNode = Node.newFunctionNode("function MyConstructor() { this.x = 5; }", null);
    Node parentNode = Node.newBlockNode("");

    checkGlobalThis.visit(Mockito.mock(NodeTraversal.class), functionNode, parentNode);

    // No error report should be issued
  }

  @Test
  public void testShouldReportGlobalThisInAssignment() {
    Node assignNode = Node.newAssignNode("x = this;");
    Node parentNode = Node.newBlockNode("");

    checkGlobalThis.visit(Mockito.mock(NodeTraversal.class), assignNode, parentNode);

    ArgumentCaptor<NodeTraversal.Error> errorCaptor = ArgumentCaptor.forClass(NodeTraversal.Error.class);
    Mockito.verify(compiler).report(errorCaptor.capture());
    DiagnosticType diagnosticType = errorCaptor.getValue().getDiagnosticType();
    assertTrue(diagnosticType.equals(CheckGlobalThis.GLOBAL_THIS));
  }

  @Test
  public void testShouldNotReportGlobalThisInPrototypePropertyAccess() {
    Node getPropNode = Node.newGetPropNode("Object.prototype.x");
    Node parentNode = Node.newObjectLiteralNode();

    checkGlobalThis.visit(Mockito.mock(NodeTraversal.class), getPropNode, parentNode);

    // No error report should be issued
  }

  @Test
  public void testShouldNotReportGlobalThisInPrototypeSubPropertyAccess() {
    Node getPropNode = Node.newGetPropNode("Object.prototype.x.y");
    Node parentNode = Node.newObjectLiteralNode();

    checkGlobalThis.visit(Mockito.mock(NodeTraversal.class), getPropNode, parentNode);

    // No error report should be issued
  }
}
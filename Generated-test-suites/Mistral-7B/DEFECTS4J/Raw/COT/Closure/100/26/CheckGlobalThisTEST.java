import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CheckLevel;
import com.google.javascript.jscomp.DiagnosticType;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.rhino.NodeTraversal.Callback;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

public class CheckGlobalThisTest {
  CheckGlobalThis checker;
  AbstractCompiler compiler;

  @Before
  public void setup() {
    compiler = mock(AbstractCompiler.class);
    checker = new CheckGlobalThis(compiler, CheckLevel.WARNING);
  }

  @Test
  public void shouldTraverseFunctionsWithNoAnnotations() {
    Node node = Node.newFunctionNoArg();
    assertTrue(checker.shouldTraverse(NodeTraversal.EMPTY, node, null));
  }

  @Test
  public void shouldNotTraverseConstructors() {
    Node constructorNode = Node.newFunctionNoArg();
    when(constructorNode.getJSDocInfo()).thenReturn(new JSDocInfo("@constructor"));

    Node parentNode = Node.newNode(Token.FUNCTION);
    NodeTraversal traversal = mock(NodeTraversal.class);

    boolean result = checker.shouldTraverse(traversal, constructorNode, parentNode);
    assertFalse(result);
  }

  @Test
  public void shouldNotTraverseFunctionsWithThisAnnotation() {
    Node functionNode = Node.newFunctionNoArg();
    when(functionNode.getJSDocInfo()).thenReturn(new JSDocInfo("@this"));

    Node parentNode = Node.newNode(Token.FUNCTION);
    NodeTraversal traversal = mock(NodeTraversal.class);

    boolean result = checker.shouldTraverse(traversal, functionNode, parentNode);
    assertFalse(result);
  }

  @Test
  public void shouldTraverseFunctionWithNoAnnotationOrContext() {
    Node functionNode = Node.newFunctionNoArg();
    Node parentNode = Node.newNode(Token.NAME, "parent");
    NodeTraversal traversal = mock(NodeTraversal.class);

    ArgumentCaptor<DiagnosticType> diagnosticTypeCaptor = ArgumentCaptor.forClass(DiagnosticType.class);

    checker.visit(traversal, functionNode, parentNode);
    verify(compiler).report(traversal.makeError(functionNode, CheckLevel.WARNING, diagnosticTypeCaptor.capture()));
    assertTrue(diagnosticTypeCaptor.getValue().getName().equals("JSC_USED_GLOBAL_THIS"));
  }

  @Test
  public void shouldNotReportThisOnRightSideOfAssignment() {
    Node assignNode = Node.newNode(Token.ASSIGN, new Node[] {
        Node.newNode(Token.THIS),
        Node.newNode(Token.IDENT, "a")
    });

    Node parentNode = Node.newNode(Token.FUNCTION);
    NodeTraversal traversal = mock(NodeTraversal.class);

    checker.visit(traversal, assignNode, parentNode);
    verify(compiler, never()).report(Anything.class);
  }

  @Test
  public void shouldReportThisOnLeftSideOfAssignment() {
    Node assignNode = Node.newNode(Token.ASSIGN, new Node[] {
        Node.newNode(Token.IDENT, "a"),
        Node.newNode(Token.THIS)
    });

    Node parentNode = Node.newNode(Token.FUNCTION);
    NodeTraversal traversal = mock(NodeTraversal.class);

    ArgumentCaptor<DiagnosticType> diagnosticTypeCaptor = ArgumentCaptor.forClass(DiagnosticType.class);

    checker.visit(traversal, assignNode, parentNode);
    verify(compiler).report(traversal.makeError(assignNode, CheckLevel.WARNING, diagnosticTypeCaptor.capture()));
    assertTrue(diagnosticTypeCaptor.getValue().getName().equals("JSC_USED_GLOBAL_THIS"));
  }

  @Test
  public void shouldNotReportThisOnPrototypeProperty() {
    Node assignNode = Node.newNode(Token.ASSIGN, new Node[] {
        Node.newNode(Token.GETPROP, Node.newNode(Token.THIS), Node.newNode(Token.IDENT, "prototype")),
        Node.newNode(Token.IDENT, "a")
    });

    Node parentNode = Node.newNode(Token.FUNCTION);
    NodeTraversal traversal = mock(NodeTraversal.class);

    checker.visit(traversal, assignNode, parentNode);
    verify(compiler, never()).report(Anything.class);
  }

  @Test
  public void shouldNotReportThisOnSubProperty() {
    Node assignNode = Node.newNode(Token.ASSIGN, new Node[] {
        Node.newNode(Token.GETPROP, Node.newNode(Token.THIS), Node.newNode(Token.IDENT, "prototype"), Node.newNode(Token.IDENT, "subProperty")),
        Node.newNode(Token.IDENT, "a")
    });

    Node parentNode = Node.newNode(Token.FUNCTION);
    NodeTraversal traversal = mock(NodeTraversal.class);

    checker.visit(traversal, assignNode, parentNode);
    verify(compiler, never()).report(Anything.class);
  }
}
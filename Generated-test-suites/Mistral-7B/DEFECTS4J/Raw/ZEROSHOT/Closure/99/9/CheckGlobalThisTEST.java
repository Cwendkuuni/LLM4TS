import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.Test;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CheckLevel;
import com.google.javascript.jscomp.Diagnostics;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.DiagnosticType;
import com.google.javascript.rhino.JSDocInfo;
import com.google.javascript.rhino.NodeUtil;

public class CheckGlobalThisTest {

  @Test
  public void testShouldTraverseFunction() {
    AbstractCompiler compiler = mock(AbstractCompiler.class);
    CheckLevel level = CheckLevel.WARNING;
    CheckGlobalThis checker = new CheckGlobalThis(compiler, level);

    Node functionNode = Node.newFunctionNode(null, null, null, null, null, null, null, null, null, null, null, null);

    NodeTraversal traversal = mock(NodeTraversal.class);
    Node parentNode = Node.newNameNode("functionName");
    when(traversal.getParent()).thenReturn(parentNode);

    assertTrue(checker.shouldTraverse(traversal, functionNode, null));
  }

  @Test
  public void testShouldNotTraverseConstructorFunction() {
    AbstractCompiler compiler = mock(AbstractCompiler.class);
    CheckLevel level = CheckLevel.WARNING;
    CheckGlobalThis checker = new CheckGlobalThis(compiler, level);

    Node constructorFunctionNode = Node.newFunctionNode(null, null, null, null, null, null, null, null, null, null, null, null, "constructor");

    NodeTraversal traversal = mock(NodeTraversal.class);

    assertTrue(checker.shouldNotTraverse(traversal, constructorFunctionNode, null));
  }

  @Test
  public void testShouldNotTraverseFunctionWithThisAnnotation() {
    AbstractCompiler compiler = mock(AbstractCompiler.class);
    CheckLevel level = CheckLevel.WARNING;
    CheckGlobalThis checker = new CheckGlobalThis(compiler, level);

    Node functionWithThisAnnotationNode = Node.newFunctionNode(null, null, null, null, null, null, null, null, null, null, null, null, null, new JSDocInfo("@this", null));

    NodeTraversal traversal = mock(NodeTraversal.class);

    assertTrue(checker.shouldNotTraverse(traversal, functionWithThisAnnotationNode, null));
  }

  @Test
  public void testShouldNotTraverseFunctionWithoutParent() {
    AbstractCompiler compiler = mock(AbstractCompiler.class);
    CheckLevel level = CheckLevel.WARNING;
    CheckGlobalThis checker = new CheckGlobalThis(compiler, level);

    Node functionNode = Node.newFunctionNode(null, null, null, null, null, null, null, null, null, null, null, null);

    NodeTraversal traversal = mock(NodeTraversal.class);

    assertTrue(checker.shouldTraverse(traversal, functionNode, null));
  }

  @Test
  public void testShouldTraverseAssignLeftSide() {
    AbstractCompiler compiler = mock(AbstractCompiler.class);
    CheckLevel level = CheckLevel.WARNING;
    CheckGlobalThis checker = new CheckGlobalThis(compiler, level);

    Node assignNode = Node.newAssignNode(null, null, Node.newNameNode("variableName"), Node.newThisNode());

    NodeTraversal traversal = mock(NodeTraversal.class);
    Node parentNode = Node.newNameNode("assignParentName");
    when(traversal.getParent()).thenReturn(parentNode);

    assertTrue(checker.shouldReportThis(assignNode, parentNode));
  }

  @Test
  public void testShouldNotTraverseAssignRightSide() {
    AbstractCompiler compiler = mock(AbstractCompiler.class);
    CheckLevel level = CheckLevel.WARNING;
    CheckGlobalThis checker = new CheckGlobalThis(compiler, level);

    Node assignNode = Node.newAssignNode(Node.newNameNode("assignLeftSide"), null, Node.newThisNode(), Node.newStringLiteralNode(""));

    NodeTraversal traversal = mock(NodeTraversal.class);
    Node parentNode = Node.newNameNode("assignParentName");
    when(traversal.getParent()).thenReturn(parentNode);

    assertFalse(checker.shouldReportThis(assignNode, parentNode));
  }

  @Test
  public void testShouldNotTraverseGetPropertyOfPrototype() {
    AbstractCompiler compiler = mock(AbstractCompiler.class);
    CheckLevel level = CheckLevel.WARNING;
    CheckGlobalThis checker = new CheckGlobalThis(compiler, level);

    Node getPrototypePropertyNode = Node.newGetPropNode(null, Node.newNameNode("prototype"), Node.newThisNode());

    NodeTraversal traversal = mock(NodeTraversal.class);
    Node parentNode = Node.newNameNode("parentNodeName");
    when(traversal.getParent()).thenReturn(parentNode);

    assertFalse(checker.shouldReportThis(getPrototypePropertyNode, parentNode));
  }

  @Test
  public void testShouldReportThisWithErrorMessage() {
    AbstractCompiler compiler = mock(AbstractCompiler.class);
    Diagnostics diagnostics = mock(Diagnostics.class);
    CheckLevel level = CheckLevel.WARNING;
    CheckGlobalThis checker = new CheckGlobalThis(compiler, level);

    Node thisNode = Node.newThisNode();
    Node parentNode = Node.newNameNode("parentNodeName");

    NodeTraversal traversal = mock(NodeTraversal.class);
    when(traversal.getParent()).thenReturn(parentNode);
    when(compiler.getDiagnostics()).thenReturn(diagnostics);

    Node traverseNode = Node.newNameNode("traverseNodeName");

    checker.visit(traversal, traverseNode, null);
    checker.visit(traversal, thisNode, null);

    DiagnosticType diagnosticType = new DiagnosticType("JSC_USED_GLOBAL_THIS", "dangerous use of the global 'this' object");

    assertTrue(diagnostics.hasError(diagnosticType));
  }

  @Test
  public void testShouldNotReportThisWithinFunction() {
    AbstractCompiler compiler = mock(AbstractCompiler.class);
    Diagnostics diagnostics = mock(Diagnostics.class);
    CheckLevel level = CheckLevel.WARNING;
    CheckGlobalThis checker = new CheckGlobalThis(compiler, level);

    Node functionNode = Node.newFunctionNode(null, null, null, null, null, null, null, null, null, null, null, null);
    Node thisNode = Node.newThisNode();
    Node parentNode = Node.newNameNode("parentNodeName");

    NodeTraversal traversal = mock(NodeTraversal.class);
    when(traversal.getParent()).thenReturn(parentNode);
    when(compiler.getDiagnostics()).thenReturn(diagnostics);

    checker.visit(traversal, functionNode, null);
    checker.visit(traversal, thisNode, null);

    DiagnosticType diagnosticType = new DiagnosticType("JSC_USED_GLOBAL_THIS", "dangerous use of the global 'this' object");

    assertFalse(diagnostics.hasError(diagnosticType));
  }
}
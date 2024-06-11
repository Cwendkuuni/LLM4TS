import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.Test;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CheckLevel;
import com.google.javascript.jscomp.DiagnosticType;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.NodeUtil;
import com.google.javascript.rhino.JSDocInfo;
import com.google.javascript.rhino.Token;

public class CheckGlobalThisTest {

  @Test
  public void testShouldTraverseGlobalFunction() {
    NodeTraversal traversal = mock(NodeTraversal.class);
    AbstractCompiler compiler = mock(AbstractCompiler.class);
    CheckGlobalThis checker = new CheckGlobalThis(compiler, CheckLevel.WARNING);

    Node functionNode = Node.newFunction(null, null, null, null);

    when(compiler.report(traversal.makeError(Node.ANY, CheckLevel.WARNING, DiagnosticType.ANY))).thenReturn(true);
    when(traversal.getCurrentScope()).thenReturn(null);

    assertTrue(checker.shouldTraverse(traversal, functionNode, Node.ANY));
  }

  @Test
  public void testShouldNotTraverseConstructorFunction() {
    NodeTraversal traversal = mock(NodeTraversal.class);
    AbstractCompiler compiler = mock(AbstractCompiler.class);
    CheckGlobalThis checker = new CheckGlobalThis(compiler, CheckLevel.WARNING);

    Node constructorNode = Node.newFunction(Node.NEW, null, null, null);
    JSDocInfo constructorJsDoc = Node.newJSDocInfo("constructor");

    when(constructorNode.getJSDocInfo()).thenReturn(constructorJsDoc);

    assertFalse(checker.shouldTraverse(traversal, constructorNode, Node.ANY));
  }

  @Test
  public void testShouldNotTraverseFunctionWithThisTypeAnnotation() {
    NodeTraversal traversal = mock(NodeTraversal.class);
    AbstractCompiler compiler = mock(AbstractCompiler.class);
    CheckGlobalThis checker = new CheckGlobalThis(compiler, CheckLevel.WARNING);

    Node functionNode = Node.newFunction(null, null, null, null);
    JSDocInfo functionJsDoc = Node.newJSDocInfo("@this", null, null, null);

    when(functionNode.getJSDocInfo()).thenReturn(functionJsDoc);

    assertFalse(checker.shouldTraverse(traversal, functionNode, Node.ANY));
  }

  @Test
  public void testShouldNotTraverseFunctionAnnotatedWithOverride() {
    NodeTraversal traversal = mock(NodeTraversal.class);
    AbstractCompiler compiler = mock(AbstractCompiler.class);
    CheckGlobalThis checker = new CheckGlobalThis(compiler, CheckLevel.WARNING);

    Node functionNode = Node.newFunction(null, null, null, null);
    JSDocInfo functionJsDoc = Node.newJSDocInfo("@override");

    when(functionNode.getJSDocInfo()).thenReturn(functionJsDoc);

    assertFalse(checker.shouldTraverse(traversal, functionNode, Node.ANY));
  }

  @Test
  public void testShouldNotTraverseFunctionWithNoLogicalPlaceForThisAnnotation() {
    NodeTraversal traversal = mock(NodeTraversal.class);
    AbstractCompiler compiler = mock(AbstractCompiler.class);
    CheckGlobalThis checker = new CheckGlobalThis(compiler, CheckLevel.WARNING);

    Node functionNode = Node.newFunction(null, null, null, null);
    JSDocInfo functionJsDoc = Node.newJSDocInfo();

    when(functionNode.getJSDocInfo()).thenReturn(functionJsDoc);

    Node parentNode = Node.newBlock(null);

    when(traversal.getCurrentScope()).thenReturn(parentNode);

    assertFalse(checker.shouldTraverse(traversal, functionNode, Node.ANY));
  }

  @Test
  public void testShouldTraverseLeftSideOfAssignment() {
    NodeTraversal traversal = mock(NodeTraversal.class);
    AbstractCompiler compiler = mock(AbstractCompiler.class);
    CheckGlobalThis checker = new CheckGlobalThis(compiler, CheckLevel.WARNING);

    Node assignmentNode = Node.newAssign(Node.ANY, Node.ANY, Node.newThis());

    assertTrue(checker.shouldTraverse(traversal, assignmentNode, Node.ANY));
  }

  @Test
  public void testShouldNotTraverseRightSideOfAssignment() {
    NodeTraversal traversal = mock(NodeTraversal.class);
    AbstractCompiler compiler = mock(AbstractCompiler.class);
    CheckGlobalThis checker = new CheckGlobalThis(compiler, CheckLevel.WARNING);

    Node assignmentNode = Node.newAssign(Node.ANY, Node.newThis(), Node.ANY);

    assertFalse(checker.shouldTraverse(traversal, Node.ANY, assignmentNode));
  }

  @Test
  public void testShouldNotTraversePropertyAccessToPrototype() {
    NodeTraversal traversal = mock(NodeTraversal.class);
    AbstractCompiler compiler = mock(AbstractCompiler.class);
    CheckGlobalThis checker = new CheckGlobalThis(compiler, CheckLevel.WARNING);

    Node propertyAccessNode = Node.newGetProp(Node.ANY, Node.newString("prototype"));

    assertFalse(checker.shouldTraverse(traversal, Node.ANY, propertyAccessNode));
  }

  @Test
  public void testShouldNotTraverseNestedPropertyAccessToPrototype() {
    NodeTraversal traversal = mock(NodeTraversal.class);
    AbstractCompiler compiler = mock(AbstractCompiler.class);
    CheckGlobalThis checker = new CheckGlobalThis(compiler, CheckLevel.WARNING);

    Node propertyAccessNode = Node.newGetProp(Node.ANY, Node.newString("prototype"));
    Node nestedPropertyAccessNode = Node.newGetProp(Node.ANY, Node.newString("prototype"));

    assertFalse(checker.shouldTraverse(traversal, Node.ANY, nestedPropertyAccessNode));
  }

  @Test
  public void testShouldReportThisKeywordInsideFunction() {
    NodeTraversal traversal = mock(NodeTraversal.class);
    AbstractCompiler compiler = mock(AbstractCompiler.class);
    CheckGlobalThis checker = new CheckGlobalThis(compiler, CheckLevel.WARNING);

    Node functionNode = Node.newFunction(null, null, null, null);
    Node thisKeywordNode = Node.newThis();

    when(functionNode.getFirstChild()).thenReturn(thisKeywordNode);

    assertTrue(checker.shouldReportThis(traversal, thisKeywordNode, functionNode));
  }

  @Test
  public void testShouldReportThisKeywordOnLeftSideOfAssignment() {
    NodeTraversal traversal = mock(NodeTraversal.class);
    AbstractCompiler compiler = mock(AbstractCompiler.class);
    CheckGlobalThis checker = new CheckGlobalThis(compiler, CheckLevel.WARNING);

    Node assignmentNode = Node.newAssign(Node.ANY, Node.newThis(), Node.ANY);

    assertTrue(checker.shouldReportThis(traversal, assignmentNode, Node.ANY));
  }

  @Test
  public void testShouldReportThisKeywordOnLeftSideOfPropertyAccess() {
    NodeTraversal traversal = mock(NodeTraversal.class);
    AbstractCompiler compiler = mock(AbstractCompiler.class);
    CheckGlobalThis checker = new CheckGlobalThis(compiler, CheckLevel.WARNING);

    Node propertyAccessNode = Node.newGetProp(Node.newThis(), Node.newString("property"));

    assertTrue(checker.shouldReportThis(traversal, propertyAccessNode, Node.ANY));
  }

  @Test
  public void testShouldNotReportThisKeywordInsidePrototypeMethod() {
    NodeTraversal traversal = mock(NodeTraversal.class);
    AbstractCompiler compiler = mock(AbstractCompiler.class);
    CheckGlobalThis checker = new CheckGlobalThis(compiler, CheckLevel.WARNING);

    Node functionNode = Node.newFunction(Node.NEW, Node.newString("prototype"), null, null);
    Node thisKeywordNode = Node.newThis();

    when(functionNode.getFirstChild()).thenReturn(thisKeywordNode);

    assertFalse(checker.shouldReportThis(traversal, thisKeywordNode, functionNode));
  }

  @Test
  public void testShouldNotReportThisKeywordInsideFunctionWithThisTypeAnnotation() {
    NodeTraversal traversal = mock(NodeTraversal.class);
    AbstractCompiler compiler = mock(AbstractCompiler.class);
    CheckGlobalThis checker = new CheckGlobalThis(compiler, CheckLevel.WARNING);

    Node functionNode = Node.newFunction(null, null, null, null);
    Node thisKeywordNode = Node.newThis();
    JSDocInfo functionJsDoc = Node.newJSDocInfo("@this");

    when(functionNode.getJSDocInfo()).thenReturn(functionJsDoc);
    when(functionJsDoc.getThisType()).thenReturn(Node.newString("MyType"));

    when(functionNode.getFirstChild()).thenReturn(thisKeywordNode);

    assertFalse(checker.shouldReportThis(traversal, thisKeywordNode, functionNode));
  }
}
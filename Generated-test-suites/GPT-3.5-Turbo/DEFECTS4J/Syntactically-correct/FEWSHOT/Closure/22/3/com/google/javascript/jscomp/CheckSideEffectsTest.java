import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CheckLevel;
import com.google.javascript.jscomp.CompileOptions;
import com.google.javascript.jscomp.CompilerInput;
import com.google.javascript.jscomp.FunctionInjector;
import com.google.javascript.jscomp.JsAst;
import com.google.javascript.jscomp.NodeUtil;
import com.google.javascript.jscomp.SourceFile;
import com.google.javascript.jscomp.testing.MockJsAst;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

public class CheckSideEffectsTest {

  private CheckSideEffects checkSideEffects;
  private AbstractCompiler compiler;

  @Before
  public void setUp() {
    compiler = mock(AbstractCompiler.class);
    checkSideEffects = new CheckSideEffects(compiler, CheckLevel.WARNING, true);
  }

  @Test
  public void testProcess() {
    Node externs = new Node(Token.ROOT);
    Node root = new Node(Token.ROOT);
    checkSideEffects.process(externs, root);

    ArgumentCaptor<NodeTraversal> traversalCaptor = ArgumentCaptor.forClass(NodeTraversal.class);
    verify(compiler).report(traversalCaptor.capture());

    NodeTraversal traversal = traversalCaptor.getValue();
    assertEquals(externs, traversal.getScopeRoot());
    assertEquals(root, traversal.getSourceFile());
  }

  @Test
  public void testHotSwapScript() {
    Node scriptRoot = new Node(Token.ROOT);
    Node originalRoot = new Node(Token.ROOT);
    checkSideEffects.hotSwapScript(scriptRoot, originalRoot);

    ArgumentCaptor<NodeTraversal> traversalCaptor = ArgumentCaptor.forClass(NodeTraversal.class);
    verify(compiler).report(traversalCaptor.capture());

    NodeTraversal traversal = traversalCaptor.getValue();
    assertEquals(scriptRoot, traversal.getScopeRoot());
    assertEquals(originalRoot, traversal.getSourceFile());
  }

  @Test
  public void testVisit_emptyNode() {
    NodeTraversal traversal = mock(NodeTraversal.class);
    Node emptyNode = new Node(Token.EMPTY);
    checkSideEffects.visit(traversal, emptyNode, null);

    // No report should be made for empty nodes
    verify(compiler, Mockito.never()).report(ArgumentMatchers.any());
  }

  @Test
  public void testVisit_commaNode() {
    NodeTraversal traversal = mock(NodeTraversal.class);
    Node commaNode = new Node(Token.COMMA);
    checkSideEffects.visit(traversal, commaNode, null);

    // No report should be made for comma nodes
    verify(compiler, Mockito.never()).report(ArgumentMatchers.any());
  }

  @Test
  public void testVisit_stringNode() {
    NodeTraversal traversal = mock(NodeTraversal.class);
    Node stringNode = new Node(Token.STRING);
    stringNode.setIsString();
    checkSideEffects.visit(traversal, stringNode, null);

    ArgumentCaptor<CompilerInput> inputCaptor = ArgumentCaptor.forClass(CompilerInput.class);
    verify(compiler).getSynthesizedExternsInput(inputCaptor.capture());

    CompilerInput input = inputCaptor.getValue();
    assertEquals(compiler, input.getCompiler());
  }

  @Test
  public void testVisit_qualifiedNameNode_withJSDocInfo() {
    NodeTraversal traversal = mock(NodeTraversal.class);
    Node qualifiedNameNode = new Node(Token.NAME);
    qualifiedNameNode.setJSDocInfo(new JSDocInfoBuilder().build());
    checkSideEffects.visit(traversal, qualifiedNameNode, null);

    // No report should be made for qualified name nodes with JSDocInfo
    verify(compiler, Mockito.never()).report(ArgumentMatchers.any());
  }

  @Test
  public void testVisit_qualifiedNameNode_withoutJSDocInfo() {
    NodeTraversal traversal = mock(NodeTraversal.class);
    Node qualifiedNameNode = new Node(Token.NAME);
    checkSideEffects.visit(traversal, qualifiedNameNode, null);

    ArgumentCaptor<Node> nodeCaptor = ArgumentCaptor.forClass(Node.class);
    verify(compiler).report(ArgumentMatchers.eq(traversal.getCompiler().makeError(nodeCaptor.capture(), CheckLevel.WARNING, CheckSideEffects.USELESS_CODE_ERROR, "This code lacks side-effects. Is there a bug?")));

    Node reportedNode = nodeCaptor.getValue();
    assertEquals(qualifiedNameNode, reportedNode);
  }

  @Test
  public void testVisit_exprResultNode() {
    NodeTraversal traversal = mock(NodeTraversal.class);
    Node exprResultNode = new Node(Token.EXPR_RESULT);
    checkSideEffects.visit(traversal, exprResultNode, null);

    // No report should be made for expression result nodes
    verify(compiler, Mockito.never()).report(ArgumentMatchers.any());
  }

  @Test
  public void testVisit_otherParentType() {
    NodeTraversal traversal = mock(NodeTraversal.class);
    Node parent = new Node(Token.EXPR_RESULT);
    Node otherNode = new Node(Token.NAME);
    checkSideEffects.visit(traversal, otherNode, parent);

    // No report should be made for other parent types
    verify(compiler, Mockito.never()).report(ArgumentMatchers.any());
  }

  @Test
  public void testVisit_resultNotUsed() {
    NodeTraversal traversal = mock(NodeTraversal.class);
    Node resultNotUsedNode = new Node(Token.MUL, new Node(Token.NUMBER), new Node(Token.NAME));
    checkSideEffects.visit(traversal, resultNotUsedNode, null);

    ArgumentCaptor<Node> nodeCaptor = ArgumentCaptor.forClass(Node.class);
    verify(compiler).report(ArgumentMatchers.eq(traversal.getCompiler().makeError(nodeCaptor.capture(), CheckLevel.WARNING, CheckSideEffects.USELESS_CODE_ERROR, "The result of the '*' operator is not being used.")));

    Node reportedNode = nodeCaptor.getValue();
    assertEquals(resultNotUsedNode, reportedNode);
  }

  @Test
  public void testVisit_resultNotUsed_stringNode() {
    NodeTraversal traversal = mock(NodeTraversal.class);
    Node stringNode = new Node(Token.STRING);
    stringNode.setIsString();
    Node resultNotUsedNode = new Node(Token.ADD, new Node(Token.NUMBER), stringNode);
    checkSideEffects.visit(traversal, resultNotUsedNode, null);

    ArgumentCaptor<Node> nodeCaptor = ArgumentCaptor.forClass(Node.class);
    verify(compiler).report(ArgumentMatchers.eq(traversal.getCompiler().makeError(nodeCaptor.capture(), CheckLevel.WARNING, CheckSideEffects.USELESS_CODE_ERROR, "Is there a missing '+' on the previous line?")));

    Node reportedNode = nodeCaptor.getValue();
    assertEquals(stringNode, reportedNode);
  }

  @Test
  public void testVisit_resultNotUsed_qualifiedNameNode() {
    NodeTraversal traversal = mock(NodeTraversal.class);
    Node qualifiedNameNode = new Node(Token.NAME);
    Node resultNotUsedNode = new Node(Token.NAME, qualifiedNameNode);
    checkSideEffects.visit(traversal, resultNotUsedNode, null);

    ArgumentCaptor<Node> nodeCaptor = ArgumentCaptor.forClass(Node.class);
    verify(compiler).report(ArgumentMatchers.eq(traversal.getCompiler().makeError(nodeCaptor.capture(), CheckLevel.WARNING, CheckSideEffects.USELESS_CODE_ERROR, "This code lacks side-effects. Is there a bug?")));

    Node reportedNode = nodeCaptor.getValue();
    assertEquals(qualifiedNameNode, reportedNode);
  }

  // Add more test cases here

}
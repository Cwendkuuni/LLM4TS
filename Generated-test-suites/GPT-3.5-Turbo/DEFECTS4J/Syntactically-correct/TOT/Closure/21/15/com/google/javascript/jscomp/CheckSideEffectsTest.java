import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;

public class CheckSideEffectsTest {

  private CheckSideEffects checkSideEffects;
  private AbstractCompiler compiler;

  @Before
  public void setUp() {
    compiler = mock(AbstractCompiler.class);
    checkSideEffects = new CheckSideEffects(compiler, CheckLevel.WARNING, true);
  }

  @Test
  public void testProcess_SimpleCodeWithSideEffect_NoWarning() {
    Node externs = createMockNode();
    Node root = createMockNode();
    when(compiler.getSynthesizedExternsInput()).thenReturn(new CompilerInput(externs));

    checkSideEffects.process(externs, root);

    // No warning should be generated
    assertTrue(checkSideEffects.getProblemNodes().isEmpty());
  }

  @Test
  public void testProcess_CodeWithoutSideEffect_GenerateWarning() {
    Node externs = createMockNode();
    Node root = createMockNode();
    when(compiler.getSynthesizedExternsInput()).thenReturn(new CompilerInput(externs));

    // Add a node without side effects
    Node node = createMockNode();
    root.addChildToBack(node);

    checkSideEffects.process(externs, root);

    // One warning should be generated
    assertEquals(1, checkSideEffects.getProblemNodes().size());
    assertEquals(node, checkSideEffects.getProblemNodes().get(0));
  }

  @Test
  public void testHotSwapScript_SimpleCodeWithSideEffect_NoWarning() {
    Node scriptRoot = createMockNode();
    Node originalRoot = createMockNode();
    when(compiler.getSynthesizedExternsInput()).thenReturn(new CompilerInput(createMockNode()));

    checkSideEffects.hotSwapScript(scriptRoot, originalRoot);

    // No warning should be generated
    assertTrue(checkSideEffects.getProblemNodes().isEmpty());
  }

  @Test
  public void testHotSwapScript_CodeWithoutSideEffect_GenerateWarning() {
    Node scriptRoot = createMockNode();
    Node originalRoot = createMockNode();
    when(compiler.getSynthesizedExternsInput()).thenReturn(new CompilerInput(createMockNode()));

    // Add a node without side effects
    Node node = createMockNode();
    scriptRoot.addChildToBack(node);

    checkSideEffects.hotSwapScript(scriptRoot, originalRoot);

    // One warning should be generated
    assertEquals(1, checkSideEffects.getProblemNodes().size());
    assertEquals(node, checkSideEffects.getProblemNodes().get(0));
  }

  @Test
  public void testVisit_StatementWithSideEffect_NoWarning() {
    NodeTraversal t = createMockNodeTraversal();
    Node n = createMockNode();
    Node parent = createMockNode();
    when(parent.getType()).thenReturn(Token.BLOCK);

    // Add a statement with side effects
    when(NodeUtil.mayHaveSideEffects(n, compiler)).thenReturn(true);

    checkSideEffects.visit(t, n, parent);

    // No warning should be generated
    assertTrue(checkSideEffects.getProblemNodes().isEmpty());
  }

  @Test
  public void testVisit_StatementWithoutSideEffect_GenerateWarning() {
    NodeTraversal t = createMockNodeTraversal();
    Node n = createMockNode();
    Node parent = createMockNode();

    // Add a statement without side effects
    when(NodeUtil.mayHaveSideEffects(n, compiler)).thenReturn(false);

    checkSideEffects.visit(t, n, parent);

    // One warning should be generated
    assertEquals(1, checkSideEffects.getProblemNodes().size());
    assertEquals(n, checkSideEffects.getProblemNodes().get(0));
  }

  @Test
  public void testVisit_IfStatementWithSideEffect_NoWarning() {
    NodeTraversal t = createMockNodeTraversal();
    Node n = createMockNode();
    Node parent = createMockNode();
    when(parent.getType()).thenReturn(Token.BLOCK);
    when(parent.getParent()).thenReturn(createMockNode());

    // Add a if statement with side effects
    when(NodeUtil.mayHaveSideEffects(n, compiler)).thenReturn(true);

    checkSideEffects.visit(t, n, parent);

    // No warning should be generated
    assertTrue(checkSideEffects.getProblemNodes().isEmpty());
  }

  @Test
  public void testVisit_IfStatementWithoutSideEffect_GenerateWarning() {
    NodeTraversal t = createMockNodeTraversal();
    Node n = createMockNode();
    Node parent = createMockNode();
    when(parent.getParent()).thenReturn(createMockNode());

    // Add a if statement without side effects
    when(NodeUtil.mayHaveSideEffects(n, compiler)).thenReturn(false);

    checkSideEffects.visit(t, n, parent);

    // One warning should be generated
    assertEquals(1, checkSideEffects.getProblemNodes().size());
    assertEquals(n, checkSideEffects.getProblemNodes().get(0));
  }

  // Helper method to create a mock node
  private Node createMockNode() {
    return mock(Node.class);
  }

  // Helper method to create a mock NodeTraversal
  private NodeTraversal createMockNodeTraversal() {
    NodeTraversal t = mock(NodeTraversal.class);
    when(t.getCompiler()).thenReturn(compiler);
    return t;
  }
}
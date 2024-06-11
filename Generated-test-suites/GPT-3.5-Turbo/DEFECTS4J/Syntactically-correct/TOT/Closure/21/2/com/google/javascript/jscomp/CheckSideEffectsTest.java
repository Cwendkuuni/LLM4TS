import org.junit.Assert;
import org.junit.Test;

public class CheckSideEffectsTest {

  @Test
  public void testProcess_NoNodes_NoWarnings() {
    // Arrange
    AbstractCompiler compiler = new AbstractCompiler();
    CheckLevel level = CheckLevel.WARNING;
    boolean protectSideEffectFreeCode = true;
    CheckSideEffects checkSideEffects = new CheckSideEffects(compiler, level, protectSideEffectFreeCode);

    Node externs = new Node();
    Node root = new Node();

    // Act
    checkSideEffects.process(externs, root);

    // Assert
    // No warnings should be generated when there are no nodes
    Assert.assertEquals(0, compiler.getWarnings().size());
  }

  @Test
  public void testHotSwapScript_HasNodes_WarningsGenerated() {
    // Arrange
    AbstractCompiler compiler = new AbstractCompiler();
    CheckLevel level = CheckLevel.WARNING;
    boolean protectSideEffectFreeCode = true;
    CheckSideEffects checkSideEffects = new CheckSideEffects(compiler, level, protectSideEffectFreeCode);

    Node scriptRoot = createScriptRoot();

    // Act
    checkSideEffects.hotSwapScript(scriptRoot, null);

    // Assert
    // Warnings should be generated for the nodes
    Assert.assertEquals(3, compiler.getWarnings().size());
    // Additional assertions for specific warnings can be added if necessary
  }

  @Test
  public void testVisit_ValidNode_ResultUsed_NoWarning() {
    // Arrange
    AbstractCompiler compiler = new AbstractCompiler();
    CheckLevel level = CheckLevel.WARNING;
    boolean protectSideEffectFreeCode = true;
    CheckSideEffects checkSideEffects = new CheckSideEffects(compiler, level, protectSideEffectFreeCode);

    NodeTraversal t = createNodeTraversal();
    Node n = createValidNode();

    // Act
    checkSideEffects.visit(t, n, null);

    // Assert
    // No warnings should be generated for a valid node with result used
    Assert.assertEquals(0, compiler.getWarnings().size());
  }

  @Test
  public void testVisit_ValidNode_ResultNotUsed_WarningGenerated() {
    // Arrange
    AbstractCompiler compiler = new AbstractCompiler();
    CheckLevel level = CheckLevel.WARNING;
    boolean protectSideEffectFreeCode = true;
    CheckSideEffects checkSideEffects = new CheckSideEffects(compiler, level, protectSideEffectFreeCode);

    NodeTraversal t = createNodeTraversal();
    Node n = createValidNode();
    n.useSourceInfoFromForTree(n);

    // Act
    checkSideEffects.visit(t, n, null);

    // Assert
    // A warning should be generated for a valid node with result not used
    Assert.assertEquals(1, compiler.getWarnings().size());
    // Additional assertions for specific warning message can be added if necessary
  }

  @Test
  public void testVisit_InvalidNode_WarningGenerated() {
    // Arrange
    AbstractCompiler compiler = new AbstractCompiler();
    CheckLevel level = CheckLevel.WARNING;
    boolean protectSideEffectFreeCode = true;
    CheckSideEffects checkSideEffects = new CheckSideEffects(compiler, level, protectSideEffectFreeCode);

    NodeTraversal t = createNodeTraversal();
    Node n = createInvalidNode();
    n.useSourceInfoFromForTree(n);

    // Act
    checkSideEffects.visit(t, n, null);

    // Assert
    // A warning should be generated for an invalid node
    Assert.assertEquals(1, compiler.getWarnings().size());
    // Additional assertions for specific warning message can be added if necessary
  }

  @Test
  public void testHotSwapScript_ProtectSideEffectFreeCode_WarningsGeneratedAndNodesProtected() {
    // Arrange
    AbstractCompiler compiler = new AbstractCompiler();
    CheckLevel level = CheckLevel.WARNING;
    boolean protectSideEffectFreeCode = true;
    CheckSideEffects checkSideEffects = new CheckSideEffects(compiler, level, protectSideEffectFreeCode);

    Node scriptRoot = createScriptRoot();

    // Act
    checkSideEffects.hotSwapScript(scriptRoot, null);

    // Assert
    // Warnings should be generated for the nodes
    Assert.assertEquals(3, compiler.getWarnings().size());
    // Additional assertions for specific warnings can be added if necessary

    // All problem nodes should have been protected
    Assert.assertEquals(3, checkSideEffects.getProblemNodes().size());
  }

  @Test
  public void testHotSwapScript_DoNotProtectSideEffectFreeCode_WarningsGeneratedAndNodesNotProtected() {
    // Arrange
    AbstractCompiler compiler = new AbstractCompiler();
    CheckLevel level = CheckLevel.WARNING;
    boolean protectSideEffectFreeCode = false;
    CheckSideEffects checkSideEffects = new CheckSideEffects(compiler, level, protectSideEffectFreeCode);

    Node scriptRoot = createScriptRoot();

    // Act
    checkSideEffects.hotSwapScript(scriptRoot, null);

    // Assert
    // Warnings should be generated for the nodes
    Assert.assertEquals(3, compiler.getWarnings().size());
    // Additional assertions for specific warnings can be added if necessary

    // No problem nodes should have been protected
    Assert.assertEquals(0, checkSideEffects.getProblemNodes().size());
  }

  private Node createScriptRoot() {
    Node script = new Node(Token.SCRIPT);
    Node expr1 = new Node(Token.EXPR_RESULT);
    expr1.useSourceInfoFromForTree(expr1);
    Node expr2 = new Node(Token.EXPR_RESULT);
    expr2.useSourceInfoFromForTree(expr2);
    Node expr3 = new Node(Token.EXPR_RESULT);
    expr3.useSourceInfoFromForTree(expr3);

    script.addChildToBack(expr1);
    script.addChildToBack(expr2);
    script.addChildToBack(expr3);

    return script;
  }

  private NodeTraversal createNodeTraversal() {
    AbstractCompiler compiler = new AbstractCompiler();
    CompilerOptions options = new CompilerOptions();
    NodeTraversal.Callback callback = new CheckSideEffects(compiler, CheckLevel.WARNING, true);

    return new NodeTraversal(compiler, callback, options);
  }

  private Node createValidNode() {
    return new Node(Token.VAR);
  }

  private Node createInvalidNode() {
    return new Node(Token.INVALID);
  }
}
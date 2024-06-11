import com.google.javascript.jscomp.*;
import org.junit.Before;
import org.junit.Test;
import static org.mockito.Mockito.*;

public class CheckSideEffectsTest {

  private AbstractCompiler compiler;
  private CheckLevel level;
  private boolean protectSideEffectFreeCode;
  private CheckSideEffects checkSideEffects;

  @Before
  public void setUp() {
    compiler = mock(AbstractCompiler.class);
    level = CheckLevel.WARNING;
    protectSideEffectFreeCode = true;
    checkSideEffects = new CheckSideEffects(compiler, level, protectSideEffectFreeCode);
  }

  @Test
  public void testProcess_WithSideEffectExpression() {
    Node externs = new Node(Token.EXTERN_EXPR);
    Node root = new Node(Token.ROOT);

    Node node = new Node(Token.EXPR_RESULT);
    node.addChildToFront(new Node(Token.GETPROP));
    root.addChildToFront(node);

    checkSideEffects.process(externs, root);

    // Verify that compiler.reportCodeChange() is called
    verify(compiler, times(1)).reportCodeChange();
  }

  @Test
  public void testProcess_WithoutSideEffectExpression() {
    Node externs = new Node(Token.EXTERN_EXPR);
    Node root = new Node(Token.ROOT);

    Node node = new Node(Token.EXPR_RESULT);
    node.addChildToFront(new Node(Token.NAME));
    root.addChildToFront(node);

    checkSideEffects.process(externs, root);

    // Verify that compiler.reportCodeChange() is not called
    verify(compiler, times(0)).reportCodeChange();
  }

  @Test
  public void testHotSwapScript_WithSideEffectExpression() {
    Node scriptRoot = new Node(Token.ROOT);
    Node originalRoot = new Node(Token.ROOT);

    Node node = new Node(Token.EXPR_RESULT);
    node.addChildToFront(new Node(Token.GETPROP));
    scriptRoot.addChildToFront(node);

    checkSideEffects.hotSwapScript(scriptRoot, originalRoot);

    // Verify that compiler.reportCodeChange() is called
    verify(compiler, times(1)).reportCodeChange();
  }

  @Test
  public void testHotSwapScript_WithoutSideEffectExpression() {
    Node scriptRoot = new Node(Token.ROOT);
    Node originalRoot = new Node(Token.ROOT);

    Node node = new Node(Token.EXPR_RESULT);
    node.addChildToFront(new Node(Token.NAME));
    scriptRoot.addChildToFront(node);

    checkSideEffects.hotSwapScript(scriptRoot, originalRoot);

    // Verify that compiler.reportCodeChange() is not called
    verify(compiler, times(0)).reportCodeChange();
  }
}
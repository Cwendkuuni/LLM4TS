
import org.junit.Test;
import static org.junit.Assert.*;

public class CheckSideEffectsTest {

  @Test
  public void testProcess() {
    AbstractCompiler compiler = new AbstractCompiler();
    CheckLevel level = CheckLevel.WARNING;
    boolean protectSideEffectFreeCode = true;
    CheckSideEffects checkSideEffects = new CheckSideEffects(compiler, level, protectSideEffectFreeCode);
    Node externs = new Node(Token.EXTERNs);
    Node root = new Node(Token.ROOT);
    checkSideEffects.process(externs, root);
  }

  @Test
  public void testHotSwapScript() {
    AbstractCompiler compiler = new AbstractCompiler();
    CheckLevel level = CheckLevel.WARNING;
    boolean protectSideEffectFreeCode = true;
    CheckSideEffects checkSideEffects = new CheckSideEffects(compiler, level, protectSideEffectFreeCode);
    Node scriptRoot = new Node(Token.SCRIPT);
    Node originalRoot = new Node(Token.ROOT);
    checkSideEffects.hotSwapScript(scriptRoot, originalRoot);
  }
}

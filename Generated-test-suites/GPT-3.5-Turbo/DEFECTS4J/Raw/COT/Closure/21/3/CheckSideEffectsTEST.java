import static org.junit.Assert.*;
import org.junit.Test;
import com.google.javascript.jscomp.*;

public class CheckSideEffectsTest {

   @Test
   public void testProcess() {
      AbstractCompiler compiler = new DummyCompiler();
      CheckLevel level = CheckLevel.WARNING;
      boolean protectSideEffectFreeCode = true;
      CheckSideEffects checkSideEffects = new CheckSideEffects(compiler, level, protectSideEffectFreeCode);
      assertNull(checkSideEffects.process(externs, root));
   }

   @Test
   public void testHotSwapScript() {
      AbstractCompiler compiler = new DummyCompiler();
      CheckLevel level = CheckLevel.WARNING;
      boolean protectSideEffectFreeCode = true;
      CheckSideEffects checkSideEffects = new CheckSideEffects(compiler, level, protectSideEffectFreeCode);
      assertNull(checkSideEffects.hotSwapScript(scriptRoot, originalRoot));
   }

   @Test
   public void testGenWarnings() {
      AbstractCompiler compiler = new DummyCompiler();
      CheckLevel level = CheckLevel.WARNING;
      boolean protectSideEffectFreeCode = true;
      CheckSideEffects checkSideEffects = new CheckSideEffects(compiler, level, protectSideEffectFreeCode);
      assertNull(checkSideEffects.genWarnings());
   }
}
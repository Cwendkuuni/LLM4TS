import org.junit.Test;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CheckLevel;
import com.google.javascript.jscomp.CompilerInput;
import com.google.javascript.jscomp.Node;
import com.google.javascript.rhino.IR;
import com.google.javascript.rhino.NodeTraversal;

public class CheckSideEffectsTest {

    @Test
    public void testProcess() {
        AbstractCompiler compiler = new AbstractCompiler();
        CheckLevel level = CheckLevel.WARNING;
        boolean protectSideEffectFreeCode = true;
        CheckSideEffects checkSideEffects = new CheckSideEffects(compiler, level, protectSideEffectFreeCode);
        Node externs = IR.empty();
        Node root = IR.empty();
        checkSideEffects.process(externs, root);
    }
  
    @Test
    public void testHotSwapScript() {
        AbstractCompiler compiler = new AbstractCompiler();
        CheckLevel level = CheckLevel.WARNING;
        boolean protectSideEffectFreeCode = true;
        CheckSideEffects checkSideEffects = new CheckSideEffects(compiler, level, protectSideEffectFreeCode);
        Node scriptRoot = IR.empty();
        Node originalRoot = IR.empty();
        checkSideEffects.hotSwapScript(scriptRoot, originalRoot);
    }
  
    @Test
    public void testVisit() {
        NodeTraversal t = null;
        Node n = IR.empty();
        Node parent = IR.empty();
        AbstractCompiler compiler = new AbstractCompiler();
        CheckLevel level = CheckLevel.WARNING;
        boolean protectSideEffectFreeCode = true;
        CheckSideEffects checkSideEffects = new CheckSideEffects(compiler, level, protectSideEffectFreeCode);
        checkSideEffects.visit(t, n, parent);
    }
  
    @Test
    public void testProtectSideEffects() {
        AbstractCompiler compiler = new AbstractCompiler();
        CheckLevel level = CheckLevel.WARNING;
        boolean protectSideEffectFreeCode = true;
        CheckSideEffects checkSideEffects = new CheckSideEffects(compiler, level, protectSideEffectFreeCode);
        checkSideEffects.protectSideEffects();
    }
  
    @Test
    public void testAddExtern() {
        AbstractCompiler compiler = new AbstractCompiler();
        CheckLevel level = CheckLevel.WARNING;
        boolean protectSideEffectFreeCode = true;
        CheckSideEffects checkSideEffects = new CheckSideEffects(compiler, level, protectSideEffectFreeCode);
        checkSideEffects.addExtern();
    }

    @Test
    public void testStripProtection() {
        AbstractCompiler compiler = new AbstractCompiler();
        CheckSideEffects.StripProtection stripProtection = new CheckSideEffects.StripProtection(compiler);
        Node externs = IR.empty();
        Node root = IR.empty();
        stripProtection.process(externs, root);
    }
}
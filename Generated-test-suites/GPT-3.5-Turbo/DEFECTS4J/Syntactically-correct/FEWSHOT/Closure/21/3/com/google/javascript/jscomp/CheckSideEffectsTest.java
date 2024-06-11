import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import com.google.javascript.jscomp.CheckLevel;
import java.util.List;
import com.google.common.collect.Lists;
import com.google.javascript.jscomp.NodeTraversal.AbstractPostOrderCallback;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.rhino.IR;
import com.google.javascript.rhino.JSDocInfoBuilder;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.Token;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CompilerInput;
import com.google.javascript.jscomp.DiagnosticType;
import com.google.javascript.jscomp.NodeUtil;

public class CheckSideEffectsTest {
    @Test
    public void testProcess() {
        AbstractCompiler compiler = null;
        CheckLevel level = CheckLevel.WARNING;
        boolean protectSideEffectFreeCode = true;
        CheckSideEffects checkSideEffects = new CheckSideEffects(compiler, level, protectSideEffectFreeCode);
        Node externs = null;
        Node root = null;
        checkSideEffects.process(externs, root);
    }

    @Test
    public void testHotSwapScript() {
        AbstractCompiler compiler = null;
        CheckLevel level = CheckLevel.WARNING;
        boolean protectSideEffectFreeCode = true;
        CheckSideEffects checkSideEffects = new CheckSideEffects(compiler, level, protectSideEffectFreeCode);
        Node scriptRoot = null;
        Node originalRoot = null;
        checkSideEffects.hotSwapScript(scriptRoot, originalRoot);
    }

    @Test
    public void testVisit() {
        NodeTraversal t = null;
        Node n = null;
        Node parent = null;
        AbstractCompiler compiler = null;
        CheckLevel level = CheckLevel.WARNING;
        boolean protectSideEffectFreeCode = true;
        CheckSideEffects checkSideEffects = new CheckSideEffects(compiler, level, protectSideEffectFreeCode);
        checkSideEffects.visit(t, n, parent);
    }

    @Test
    public void testProtectSideEffects() {
        AbstractCompiler compiler = null;
        CheckLevel level = CheckLevel.WARNING;
        boolean protectSideEffectFreeCode = true;
        CheckSideEffects checkSideEffects = new CheckSideEffects(compiler, level, protectSideEffectFreeCode);
        checkSideEffects.protectSideEffects();
    }

    @Test
    public void testAddExtern() {
        AbstractCompiler compiler = null;
        CheckLevel level = CheckLevel.WARNING;
        boolean protectSideEffectFreeCode = true;
        CheckSideEffects checkSideEffects = new CheckSideEffects(compiler, level, protectSideEffectFreeCode);
        checkSideEffects.addExtern();
    }

    @Test
    public void testStripProtection() {
        AbstractCompiler compiler = null;
        CheckLevel level = CheckLevel.WARNING;
        boolean protectSideEffectFreeCode = true;
        CheckSideEffects.StripProtection stripProtection = new CheckSideEffects.StripProtection(compiler);
        Node externs = null;
        Node root = null;
        stripProtection.process(externs, root);
    }
}
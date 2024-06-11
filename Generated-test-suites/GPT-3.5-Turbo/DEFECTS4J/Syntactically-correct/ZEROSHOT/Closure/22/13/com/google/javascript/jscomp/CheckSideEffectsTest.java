import com.google.common.collect.Lists;
import com.google.javascript.jscomp.CheckLevel;
import com.google.javascript.jscomp.NodeTraversal.AbstractPostOrderCallback;
import com.google.javascript.rhino.IR;
import com.google.javascript.rhino.JSDocInfoBuilder;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.Token;
import org.junit.Test;
import static org.junit.Assert.*;

public class CheckSideEffectsTest {

    @Test
    public void testVisit() {
        AbstractCompiler compiler = new AbstractCompiler();
        CheckLevel level = CheckLevel.WARNING;
        boolean protectSideEffectFreeCode = true;
        CheckSideEffects checkSideEffects = new CheckSideEffects(compiler, level, protectSideEffectFreeCode);

        NodeTraversal traversal = new NodeTraversal(compiler, checkSideEffects);
        Node node = new Node(Token.SCRIPT);
        Node externs = new Node(Token.BLOCK);
        Node root = new Node(Token.BLOCK);
        checkSideEffects.hotSwapScript(root, root);
        checkSideEffects.process(externs, root);
        checkSideEffects.visit(traversal, node, node);
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

        NodeTraversal traversal = new NodeTraversal(compiler, stripProtection);
        Node node = new Node(Token.SCRIPT);
        Node externs = new Node(Token.BLOCK);
        Node root = new Node(Token.BLOCK);
        stripProtection.process(externs, root);
        stripProtection.visit(traversal, node, node);
    }

}
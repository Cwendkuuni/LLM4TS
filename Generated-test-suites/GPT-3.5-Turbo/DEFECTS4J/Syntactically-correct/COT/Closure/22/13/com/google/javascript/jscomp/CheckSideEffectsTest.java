import com.google.javascript.jscomp.CheckLevel;
import com.google.javascript.jscomp.Node;
import com.google.javascript.rhino.Token;
import org.junit.Assert;
import org.junit.Test;

public class CheckSideEffectsTest {

    @Test
    public void shouldFindUselessCodeErrorForMissingPlus() {
        CheckLevel level = CheckLevel.WARNING;
        boolean protectSideEffectFreeCode = false;
        CheckSideEffects checkSideEffects = new CheckSideEffects(null, level, protectSideEffectFreeCode);
        Node n = new Node(Token.ADD);
        n.addChildToBack(new Node(Token.STRING));
        n.putProp(Node.MAY_HAVE_SIDE_EFFECTS, false);
        NodeTraversal t = new NodeTraversal(null, null, null);
        checkSideEffects.visit(t, n, null);
        Assert.assertEquals("This code lacks side-effects. Is there a bug?",
                t.getCompiler().getLastWarning().description);
    }

    @Test
    public void shouldFindUselessCodeErrorForUnusedOperatorResult() {
        CheckLevel level = CheckLevel.WARNING;
        boolean protectSideEffectFreeCode = false;
        CheckSideEffects checkSideEffects = new CheckSideEffects(null, level, protectSideEffectFreeCode);
        Node n = new Node(Token.MOD);
        NodeTraversal t = new NodeTraversal(null, null, null);
        checkSideEffects.visit(t, n, null);
        Assert.assertEquals("The result of the 'mod' operator is not being used.",
                t.getCompiler().getLastWarning().description);
    }

    @Test
    public void shouldProtectSideEffectFreeCode() {
        CheckLevel level = CheckLevel.WARNING;
        boolean protectSideEffectFreeCode = true;
        CheckSideEffects checkSideEffects = new CheckSideEffects(null, level, protectSideEffectFreeCode);
        Node n = new Node(Token.NAME);
        n.setString("test");
        NodeTraversal t = new NodeTraversal(null, null, null);
        checkSideEffects.visit(t, n, null);
        Assert.assertEquals(PROTECTOR_FN, n.getParent().getFirstChild().getString());
    }

    @Test
    public void shouldStripProtection() {
        AbstractCompiler compiler = null;
        CheckSideEffects.StripProtection stripProtection = new CheckSideEffects.StripProtection(compiler);
        Node n = new Node(Token.CALL);
        Node target = new Node(Token.NAME);
        target.setString(PROTECTOR_FN);
        n.addChildToBack(target);
        Node expr = new Node(Token.NAME);
        n.addChildToBack(expr);
        Node parent = new Node(Token.EXPR_RESULT);
        parent.addChildToBack(n);
        NodeTraversal t = new NodeTraversal(null, null, null);
        stripProtection.visit(t, n, parent);
        Assert.assertEquals(expr, parent.getFirstChild());
    }
}
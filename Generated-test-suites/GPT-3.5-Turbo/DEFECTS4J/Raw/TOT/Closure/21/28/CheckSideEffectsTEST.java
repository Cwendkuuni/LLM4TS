import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class CheckSideEffectsTest {

    private CheckSideEffects checkSideEffects;

    @Before
    public void setUp() {
        checkSideEffects = new CheckSideEffects(new AbstractCompiler(), CheckLevel.WARNING, false);
    }

    @Test
    public void testProcess() {
        // Test case with a simple side-effect free expression
        Node root = new Node(Token.BLOCK);
        Node expr = new Node(Token.EXPR_RESULT);
        Node value = new Node(Token.TRUE);
        expr.addChildToBack(value);
        root.addChildToBack(expr);
        checkSideEffects.process(null, root);
        // No warnings should be reported
        assertEquals(0, checkSideEffects.problemNodes.size());

        // Test case with a side-effect expression
        root = new Node(Token.BLOCK);
        expr = new Node(Token.EXPR_RESULT);
        value = new Node(Token.CALL);
        Node target = new Node(Token.NAME, "console.log");
        value.addChildToFront(target);
        expr.addChildToBack(value);
        root.addChildToBack(expr);
        checkSideEffects.process(null, root);
        // A warning should be reported
        assertEquals(1, checkSideEffects.problemNodes.size());
    }

    @Test
    public void testHotSwapScript() {
        // Test case with a simple side-effect free expression
        Node root = new Node(Token.BLOCK);
        Node expr = new Node(Token.EXPR_RESULT);
        Node value = new Node(Token.TRUE);
        expr.addChildToBack(value);
        root.addChildToBack(expr);
        checkSideEffects.hotSwapScript(root, null);
        // No warnings should be reported
        assertEquals(0, checkSideEffects.problemNodes.size());

        // Test case with a side-effect expression
        root = new Node(Token.BLOCK);
        expr = new Node(Token.EXPR_RESULT);
        value = new Node(Token.CALL);
        Node target = new Node(Token.NAME, "console.log");
        value.addChildToFront(target);
        expr.addChildToBack(value);
        root.addChildToBack(expr);
        checkSideEffects.hotSwapScript(root, null);
        // A warning should be reported
        assertEquals(1, checkSideEffects.problemNodes.size());
    }
}
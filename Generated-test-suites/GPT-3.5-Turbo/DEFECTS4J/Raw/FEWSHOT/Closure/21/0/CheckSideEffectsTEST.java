import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class CheckSideEffectsTest {
    @Test
    public void testProcess_WithProblemNodes() {
        CheckSideEffects checkSideEffects = new CheckSideEffects(compiler, level, protectSideEffectFreeCode);
        checkSideEffects.problemNodes.add(new Node());
        checkSideEffects.process(externs, root);
        assertTrue(checkSideEffects.problemNodes.isEmpty());
        assertTrue(compiler.wasReported(DiagnosticType.warning("JSC_USELESS_CODE", "Suspicious code. {0}")));
    }

    @Test
    public void testHotSwapScript() {
        CheckSideEffects checkSideEffects = new CheckSideEffects(compiler, level, protectSideEffectFreeCode);
        checkSideEffects.problemNodes.add(new Node());
        checkSideEffects.hotSwapScript(scriptRoot, originalRoot);
        assertTrue(checkSideEffects.problemNodes.isEmpty());
        assertTrue(compiler.wasReported(DiagnosticType.warning("JSC_USELESS_CODE", "Suspicious code. {0}")));
    }

    @Test
    public void testVisit_ExpressionResult() {
        CheckSideEffects checkSideEffects = new CheckSideEffects(compiler, level, protectSideEffectFreeCode);
        Node parent = new Node(Token.EXPR_RESULT);
        Node n = new Node();
        checkSideEffects.visit(t, n, parent);
        assertTrue(checkSideEffects.problemNodes.isEmpty());
        assertEquals(parent, n.getParent()); // Node should not be removed
    }

    @Test
    public void testVisit_Block() {
        CheckSideEffects checkSideEffects = new CheckSideEffects(compiler, level, protectSideEffectFreeCode);
        Node parent = new Node(Token.BLOCK);
        Node n = new Node();
        checkSideEffects.visit(t, n, parent);
        assertTrue(checkSideEffects.problemNodes.isEmpty());
        assertEquals(parent, n.getParent()); // Node should not be removed
    }

    @Test
    public void testVisit_ReadableNameWithJSDocInfo() {
        CheckSideEffects checkSideEffects = new CheckSideEffects(compiler, level, protectSideEffectFreeCode);
        Node parent = new Node();
        parent.setJSDocInfo(new JSDocInfoBuilder(true).build());
        Node n = new Node(Token.NAME);
        n.putProp(Node.ALIASING_GET, true);
        n.setJSDocInfo(new JSDocInfoBuilder(true).build());
        checkSideEffects.visit(t, n, parent);
        assertTrue(checkSideEffects.problemNodes.isEmpty());
        assertEquals(parent, n.getParent()); // Node should not be removed
    }

    @Test
    public void testVisit_Comma_WithResultUsed() {
        CheckSideEffects checkSideEffects = new CheckSideEffects(compiler, level, protectSideEffectFreeCode);
        Node parent = new Node(Token.COMMA);
        Node n = new Node();
        Node commaChild = new Node();
        commaChild.putBooleanProp(Node.IS_CONSTANT_NAME, true);
        parent.addChildToBack(commaChild);
        parent.addChildToBack(n);
        parent.putBooleanProp(Node.FREE_CALL, true);
        checkSideEffects.visit(t, n, parent);
        assertTrue(checkSideEffects.problemNodes.isEmpty());
        assertEquals(parent, n.getParent()); // Node should not be removed
    }

    @Test
    public void testVisit_Comma_LastChild_WithAncestor() {
        CheckSideEffects checkSideEffects = new CheckSideEffects(compiler, level, protectSideEffectFreeCode);
        Node parent = new Node(Token.COMMA);
        Node n = new Node();
        Node ancestor = new Node(Token.EXPR_RESULT);
        parent.addChildToBack(n);
        ancestor.addChildToBack(parent);
        checkSideEffects.visit(t, n, parent);
        assertTrue(checkSideEffects.problemNodes.isEmpty());
        assertEquals(parent, n.getParent()); // Node should not be removed
    }

    @Test
    public void testVisit_SimpleOp_WithoutSideEffects() {
        CheckSideEffects checkSideEffects = new CheckSideEffects(compiler, level, protectSideEffectFreeCode);
        Node parent = new Node();
        Node n = new Node(Token.ADD);
        n.putBooleanProp(Node.IS_CONSTANT_NAME, true);
        checkSideEffects.visit(t, n, parent);
        assertTrue(checkSideEffects.problemNodes.isEmpty());
        assertEquals(parent, n.getParent()); // Node should not be removed
    }

    @Test
    public void testVisit_SimpleOp_WithSideEffects() {
        CheckSideEffects checkSideEffects = new CheckSideEffects(compiler, level, protectSideEffectFreeCode);
        Node parent = new Node();
        Node n = new Node(Token.DELPROP);
        n.putBooleanProp(Node.IS_CONSTANT_NAME, true);
        checkSideEffects.visit(t, n, parent);
        assertTrue(checkSideEffects.problemNodes.isEmpty());
        assertEquals(parent, n.getParent()); // Node should not be removed
    }

    @Test
    public void testVisit_SimpleOp_WithString() {
        CheckSideEffects checkSideEffects = new CheckSideEffects(compiler, level, protectSideEffectFreeCode);
        Node parent = new Node();
        Node n = new Node(Token.STRING);
        n.putBooleanProp(Node.IS_CONSTANT_NAME, true);
        checkSideEffects.visit(t, n, parent);
        assertEquals(1, checkSideEffects.problemNodes.size());
        assertTrue(compiler.wasReported(DiagnosticType.warning("JSC_USELESS_CODE", "Is there a missing '+' on the previous line?")));
    }
}
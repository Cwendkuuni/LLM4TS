import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class CheckSideEffectsTest {

    private CheckSideEffects checkSideEffects;

    @Before
    public void setUp() {
        checkSideEffects = new CheckSideEffects(null, null, false);
    }

    @Test
    public void testVisit_ValidSideEffectExpression_NoReportedProblem() {
        NodeTraversal t = new NodeTraversal(null);
        Node n = new Node(Token.ADD, new Node(Token.STRING, "this string is "),
                new Node(Token.STRING, "continued on the next line but you forgot the +"));

        checkSideEffects.visit(t, n, null);

        // Check that no problem was reported
        assertEquals(0, problemNodes.size());
    }

    @Test
    public void testVisit_ValidSideEffectAssignment_NoReportedProblem() {
        NodeTraversal t = new NodeTraversal(null);
        Node n = new Node(Token.ASSIGN, new Node(Token.NAME, "x"),
                new Node(Token.CALL).putBooleanProp(Node.FREE_CALL, true));

        checkSideEffects.visit(t, n, null);

        // Check that no problem was reported
        assertEquals(0, problemNodes.size());
    }

    @Test
    public void testVisit_StringMissingConcatenation_PluginReportsProblem() {
        NodeTraversal t = new NodeTraversal(null);
        Node n = new Node(Token.STRING, "Is there a missing '+' on the previous line?");

        checkSideEffects.visit(t, n, null);

        // Check that a problem was reported
        assertEquals(1, problemNodes.size());
        assertEquals("Is there a missing '+' on the previous line?", problemNodes.get(0).getMessage());
    }

    @Test
    public void testVisit_OperatorResultNotUsed_PluginReportsProblem() {
        NodeTraversal t = new NodeTraversal(null);
        Node n = new Node(Token.AND, new Node(Token.TRUE), new Node(Token.FALSE));

        checkSideEffects.visit(t, n, null);

        // Check that a problem was reported
        assertEquals(1, problemNodes.size());
        assertEquals("The result of the 'AND' operator is not being used.", problemNodes.get(0).getMessage());
    }

    @Test
    public void testVisit_SimpleUnaryOperatorResultNotUsed_PluginReportsProblem() {
        NodeTraversal t = new NodeTraversal(null);
        Node n = new Node(Token.INC, new Node(Token.NAME, "x"));

        checkSideEffects.visit(t, n, null);

        // Check that a problem was reported
        assertEquals(1, problemNodes.size());
        assertEquals("The result of the 'INC' operator is not being used.", problemNodes.get(0).getMessage());
    }

    @Test
    public void testVisit_SideEffectFreeStatement_PluginDoesNotReportProblem() {
        NodeTraversal t = new NodeTraversal(null);
        Node n = new Node(Token.EMPTY);

        checkSideEffects.visit(t, n, null);

        // Check that no problem was reported
        assertEquals(0, problemNodes.size());
    }

    @Test
    public void testVisit_StatementWithQualifiedNameAndJSDocInfo_PluginDoesNotReportProblem() {
        NodeTraversal t = new NodeTraversal(null);
        Node n = new Node(Token.NAME, "x");
        n.putProp(Node.JSDOC_INFO_PROP, "JSDocInfo");

        checkSideEffects.visit(t, n, null);

        // Check that no problem was reported
        assertEquals(0, problemNodes.size());
    }

    @Test
    public void testVisit_StatementIsExprResult_PluginDoesNotReportProblem() {
        NodeTraversal t = new NodeTraversal(null);
        Node n = new Node(Token.EXPR_RESULT);

        checkSideEffects.visit(t, n, null);

        // Check that no problem was reported
        assertEquals(0, problemNodes.size());
    }

    @Test
    public void testHotSwapScript_ReplaceSideEffectExpression_ValidSideEffectExpression_NoReportedProblem() {
        NodeTraversal t = new NodeTraversal(null);
        Node scriptRoot = new Node(Token.SCRIPT);
        Node originalRoot = new Node(Token.SCRIPT);

        Node n = new Node(Token.ADD, new Node(Token.STRING, "this string is "),
                new Node(Token.STRING, "continued on the next line but you forgot the +"));

        scriptRoot.addChildToBack(n);

        checkSideEffects.hotSwapScript(scriptRoot, originalRoot);

        // Check that no problem was reported
        assertEquals(0, problemNodes.size());
    }

    @Test
    public void testHotSwapScript_ReplaceSideEffectAssignment_ValidSideEffectAssignment_NoReportedProblem() {
        NodeTraversal t = new NodeTraversal(null);
        Node scriptRoot = new Node(Token.SCRIPT);
        Node originalRoot = new Node(Token.SCRIPT);

        Node n = new Node(Token.ASSIGN, new Node(Token.NAME, "x"),
                new Node(Token.CALL).putBooleanProp(Node.FREE_CALL, true));

        scriptRoot.addChildToBack(n);

        checkSideEffects.hotSwapScript(scriptRoot, originalRoot);

        // Check that no problem was reported
        assertEquals(0, problemNodes.size());
    }

    @Test
    public void testHotSwapScript_ReplaceStringMissingConcatenation_ValidString_NoReportedProblem() {
        NodeTraversal t = new NodeTraversal(null);
        Node scriptRoot = new Node(Token.SCRIPT);
        Node originalRoot = new Node(Token.SCRIPT);

        Node n = new Node(Token.STRING, "Is there a missing '+' on the previous line?");

        scriptRoot.addChildToBack(n);

        checkSideEffects.hotSwapScript(scriptRoot, originalRoot);

        // Check that no problem was reported
        assertEquals(0, problemNodes.size());
    }

    @Test
    public void testHotSwapScript_ReplaceOperatorResultNotUsed_ValidExpression_NoReportedProblem() {
        NodeTraversal t = new NodeTraversal(null);
        Node scriptRoot = new Node(Token.SCRIPT);
        Node originalRoot = new Node(Token.SCRIPT);

        Node n = new Node(Token.AND, new Node(Token.TRUE), new Node(Token.FALSE));

        scriptRoot.addChildToBack(n);

        checkSideEffects.hotSwapScript(scriptRoot, originalRoot);

        // Check that no problem was reported
        assertEquals(0, problemNodes.size());
    }

    @Test
    public void testHotSwapScript_ReplaceSimpleUnaryOperatorResultNotUsed_ValidStatement_NoReportedProblem() {
        NodeTraversal t = new NodeTraversal(null);
        Node scriptRoot = new Node(Token.SCRIPT);
        Node originalRoot = new Node(Token.SCRIPT);

        Node n = new Node(Token.INC, new Node(Token.NAME, "x"));

        scriptRoot.addChildToBack(n);

        checkSideEffects.hotSwapScript(scriptRoot, originalRoot);

        // Check that no problem was reported
        assertEquals(0, problemNodes.size());
    }

    @Test
    public void testHotSwapScript_ReplaceSideEffectFreeStatement_ValidStatement_NoReportedProblem() {
        NodeTraversal t = new NodeTraversal(null);
        Node scriptRoot = new Node(Token.SCRIPT);
        Node originalRoot = new Node(Token.SCRIPT);

        Node n = new Node(Token.EMPTY);

        scriptRoot.addChildToBack(n);

        checkSideEffects.hotSwapScript(scriptRoot, originalRoot);

        // Check that no problem was reported
        assertEquals(0, problemNodes.size());
    }

    @Test
    public void testHotSwapScript_ReplaceStatementWithQualifiedNameAndJSDocInfo_ValidStatement_NoReportedProblem() {
        NodeTraversal t = new NodeTraversal(null);
        Node scriptRoot = new Node(Token.SCRIPT);
        Node originalRoot = new Node(Token.SCRIPT);

        Node n = new Node(Token.NAME, "x");
        n.putProp(Node.JSDOC_INFO_PROP, "JSDocInfo");

        scriptRoot.addChildToBack(n);

        checkSideEffects.hotSwapScript(scriptRoot, originalRoot);

        // Check that no problem was reported
        assertEquals(0, problemNodes.size());
    }

    @Test
    public void testHotSwapScript_ReplaceExprResultStatement_ValidStatement_NoReportedProblem() {
        NodeTraversal t = new NodeTraversal(null);
        Node scriptRoot = new Node(Token.SCRIPT);
        Node originalRoot = new Node(Token.SCRIPT);

        Node n = new Node(Token.EXPR_RESULT);

        scriptRoot.addChildToBack(n);

        checkSideEffects.hotSwapScript(scriptRoot, originalRoot);

        // Check that no problem was reported
        assertEquals(0, problemNodes.size());
    }
}
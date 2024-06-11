import org.junit.Assert;
import org.junit.Test;

import com.google.javascript.jscomp.CheckLevel;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.Token;

public class CheckSideEffectsTest {

    @Test
    public void testVisit_ExpressionWithSideEffect() {
        Node node = createNode(Token.EXPR_RESULT, createNode(Token.BITWISEAND));
        CheckSideEffects checkSideEffects = createCheckSideEffects();
        checkSideEffects.visit(null, node, null);
        Assert.assertEquals("This code lacks side-effects. Is there a bug?", checkSideEffects.getDiagnosticMessage());
        Assert.assertTrue(checkSideEffects.isNodeAddedToProblemList());
    }

    @Test
    public void testVisit_ExpressionWithoutSideEffect() {
        Node node = createNode(Token.BITWISEAND);
        CheckSideEffects checkSideEffects = createCheckSideEffects();
        checkSideEffects.visit(null, node, null);
        Assert.assertNull(checkSideEffects.getDiagnosticMessage());
        Assert.assertFalse(checkSideEffects.isNodeAddedToProblemList());
    }

    private Node createNode(int type, Node... children) {
        Node node = new Node(type);
        for (Node child : children) {
            node.addChildToBack(child);
        }
        return node;
    }

    private CheckSideEffects createCheckSideEffects() {
        return new CheckSideEffects(null, CheckLevel.WARNING, false) {
            private String diagnosticMessage;
            private boolean nodeAddedToProblemList;

            public String getDiagnosticMessage() {
                return diagnosticMessage;
            }

            public boolean isNodeAddedToProblemList() {
                return nodeAddedToProblemList;
            }

            @Override
            public void visit(NodeTraversal t, Node n, Node parent) {
                diagnosticMessage = super.getDiagnosticMessage();
                nodeAddedToProblemList = super.isNodeAddedToProblemList();
            }
        };
    }
}

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

public class UnreachableCodeEliminationTest {
    private UnreachableCodeElimination unreachableCodeElimination;

    @Before
    public void setUp() {
        unreachableCodeElimination = new UnreachableCodeElimination(new AbstractCompiler(), true);
    }

    @Test
    public void testEnterScope() {
        // TODO: Write test case for testEnterScope method
    }

    @Test
    public void testExitScope() {
        // TODO: Write test case for testExitScope method
    }

    @Test
    public void testProcess() {
        // TODO: Write test case for testProcess method
    }

    @Test
    public void testVisit_withReturnValue() {
        Node n = new Node(Token.RETURN);
        Node parent = new Node(Token.SCRIPT);
        n.setParent(parent);

        assertNull(unreachableCodeElimination.tryRemoveUnconditionalBranching(n));
        assertTrue(parent.hasChildren());
    }

    @Test
    public void testVisit_withBreak() {
        Node n = new Node(Token.BREAK);
        Node parent = new Node(Token.SCRIPT);
        n.setParent(parent);

        assertNull(unreachableCodeElimination.tryRemoveUnconditionalBranching(n));
        assertTrue(parent.hasChildren());
    }

    @Test
    public void testVisit_withContinue() {
        Node n = new Node(Token.CONTINUE);
        Node parent = new Node(Token.SCRIPT);
        n.setParent(parent);

        assertNull(unreachableCodeElimination.tryRemoveUnconditionalBranching(n));
        assertTrue(parent.hasChildren());
    }

    @Test
    public void testVisit_withBlock() {
        Node n = new Node(Token.BLOCK);
        Node parent = new Node(Token.SCRIPT);
        n.setParent(parent);

        assertNull(unreachableCodeElimination.tryRemoveUnconditionalBranching(n));
        assertTrue(parent.hasChildren());
    }

    @Test
    public void testVisit_withDo() {
        Node n = new Node(Token.DO);
        Node parent = new Node(Token.SCRIPT);
        n.setParent(parent);

        assertNull(unreachableCodeElimination.tryRemoveUnconditionalBranching(n));
        assertTrue(parent.hasChildren());
    }

    @Test
    public void testVisit_withCatch() {
        Node n = new Node(Token.CATCH);
        Node parent = new Node(Token.SCRIPT);
        n.setParent(parent);

        assertNull(unreachableCodeElimination.tryRemoveUnconditionalBranching(n));
        assertTrue(parent.hasChildren());
    }

    @Test
    public void testRemoveDeadExprStatementSafely_withEmptyNode() {
        Node n = new Node(Token.EMPTY);
        Node parent = new Node(Token.SCRIPT);
        n.setParent(parent);

        unreachableCodeElimination.removeDeadExprStatementSafely(n);
        assertTrue(parent.hasChildren());
    }

    @Test
    public void testRemoveDeadExprStatementSafely_withBlockNode() {
        Node n = new Node(Token.BLOCK);
        Node parent = new Node(Token.SCRIPT);
        n.setParent(parent);

        unreachableCodeElimination.removeDeadExprStatementSafely(n);
        assertTrue(parent.hasChildren());
    }
}

import org.junit.Test;

import static org.junit.Assert.*;

public class UnreachableCodeEliminationTest {

    @Test
    public void testProcess() {
        UnreachableCodeElimination uce = new UnreachableCodeElimination(null, true);
        uce.process(null, null);
    }

    @Test
    public void testEnterScope() {
        UnreachableCodeElimination uce = new UnreachableCodeElimination(null, true);
        NodeTraversal t = new NodeTraversal(null, null);
        uce.enterScope(t);
    }

    @Test
    public void testExitScope() {
        UnreachableCodeElimination uce = new UnreachableCodeElimination(null, true);
        NodeTraversal t = new NodeTraversal(null, null);
        uce.exitScope(t);
    }

    @Test
    public void testVisit() {
        UnreachableCodeElimination uce = new UnreachableCodeElimination(null, true);
        NodeTraversal t = new NodeTraversal(null, null);
        Node n = new Node();
        Node parent = null;
        uce.visit(t, n, parent);
        assertNotNull(n);
    }

    @Test
    public void testTryRemoveUnconditionalBranching() {
        UnreachableCodeElimination uce = new UnreachableCodeElimination(null, true);
        Node n = new Node();
        Node result = uce.tryRemoveUnconditionalBranching(n);
        assertEquals(n, result);
    }

    @Test
    public void testComputeFollowing() {
        UnreachableCodeElimination uce = new UnreachableCodeElimination(null, true);
        Node n = new Node();
        Node result = uce.computeFollowing(n);
        assertNotNull(result);
    }

    @Test
    public void testRemoveDeadExprStatementSafely() {
        UnreachableCodeElimination uce = new UnreachableCodeElimination(null, true);
        Node n = new Node();
        uce.removeDeadExprStatementSafely(n);
    }
}
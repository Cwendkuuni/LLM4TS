import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class DeadAssignmentsEliminationTest {
    private DeadAssignmentsElimination deadAssignmentsElimination;

    @Before
    public void setUp() {
        deadAssignmentsElimination = new DeadAssignmentsElimination();
    }

    @Test
    public void testEnterScope_GlobalScope() {
        NodeTraversal t = new NodeTraversal();

        deadAssignmentsElimination.enterScope(t);

        // TODO: Add assertions
    }

    @Test
    public void testEnterScope_InnerFunctionPresent() {
        NodeTraversal t = new NodeTraversal();

        deadAssignmentsElimination.enterScope(t);

        // TODO: Add assertions
    }

    @Test
    public void testEnterScope_NoAssignmentsToEliminate() {
        NodeTraversal t = new NodeTraversal();

        deadAssignmentsElimination.enterScope(t);

        // TODO: Add assertions
    }

    @Test
    public void testEnterScope_RemoveDeadAssignments() {
        NodeTraversal t = new NodeTraversal();

        deadAssignmentsElimination.enterScope(t);

        // TODO: Add assertions
    }

    @Test
    public void testExitScope() {
        NodeTraversal t = new NodeTraversal();

        deadAssignmentsElimination.exitScope(t);

        // TODO: Add assertions
    }

    @Test
    public void testVisit() {
        NodeTraversal t = new NodeTraversal();
        Node n = new Node();
        Node parent = new Node();

        deadAssignmentsElimination.visit(t, n, parent);

        // TODO: Add assertions
    }

    @Test
    public void testProcess() {
        AbstractCompiler compiler = new AbstractCompiler();
        Node externs = new Node();
        Node root = new Node();

        deadAssignmentsElimination.process(externs, root);

        // TODO: Add assertions
    }
}
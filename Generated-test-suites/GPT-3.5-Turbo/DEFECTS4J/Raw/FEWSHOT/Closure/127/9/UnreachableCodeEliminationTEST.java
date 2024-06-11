import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class UnreachableCodeEliminationTest {
    @Test
    public void testProcess() {
        // Create CompilerPass and AbstractCompiler objects
        AbstractCompiler compiler = new AbstractCompiler();
        UnreachableCodeElimination uce = new UnreachableCodeElimination(compiler, true);

        // Create Nodes for externs and toplevel
        Node externs = new Node(Token.EXTERN);
        Node toplevel = new Node(Token.ROOT);

        // Test process() method
        uce.process(externs, toplevel);

        // TODO: Assert expected output here
    }

    @Test
    public void testEliminationPass() {
        // Create CompilerPass and ControlFlowGraph objects
        AbstractCompiler compiler = new AbstractCompiler();
        ControlFlowGraph<Node> cfg = new ControlFlowGraph<Node>();
        UnreachableCodeElimination uce = new UnreachableCodeElimination(compiler, true);
        UnreachableCodeElimination.EliminationPass eliminationPass = uce.new EliminationPass(cfg);

        // Create Nodes for traversal
        NodeTraversal t = new NodeTraversal(compiler, eliminationPass);
        Node n = new Node(Token.FOR);

        // Test visit() method for unreachable code
        eliminationPass.visit(t, n, null);

        // TODO: Assert expected output here
    }

    @Test
    public void testTryRemoveUnconditionalBranching() {
        // Create CompilerPass and ControlFlowGraph objects
        AbstractCompiler compiler = new AbstractCompiler();
        ControlFlowGraph<Node> cfg = new ControlFlowGraph<Node>();
        UnreachableCodeElimination uce = new UnreachableCodeElimination(compiler, true);
        UnreachableCodeElimination.EliminationPass eliminationPass = uce.new EliminationPass(cfg);

        // Create Nodes for visit() method
        Node n = new Node(Token.BREAK);

        // Test tryRemoveUnconditionalBranching() method
        eliminationPass.tryRemoveUnconditionalBranching(n);

        // TODO: Assert expected output here
    }

    @Test
    public void testComputeFollowing() {
        // Create CompilerPass and ControlFlowGraph objects
        AbstractCompiler compiler = new AbstractCompiler();
        ControlFlowGraph<Node> cfg = new ControlFlowGraph<Node>();
        UnreachableCodeElimination uce = new UnreachableCodeElimination(compiler, true);
        UnreachableCodeElimination.EliminationPass eliminationPass = uce.new EliminationPass(cfg);

        // Create Nodes for computeFollowing() method
        Node n = new Node(Token.BLOCK);

        // Test computeFollowing() method
        Node result = eliminationPass.computeFollowing(n);

        // TODO: Assert expected output here
    }

    @Test
    public void testRemoveDeadExprStatementSafely() {
        // Create CompilerPass and ControlFlowGraph objects
        AbstractCompiler compiler = new AbstractCompiler();
        ControlFlowGraph<Node> cfg = new ControlFlowGraph<Node>();
        UnreachableCodeElimination uce = new UnreachableCodeElimination(compiler, true);
        UnreachableCodeElimination.EliminationPass eliminationPass = uce.new EliminationPass(cfg);

        // Create Nodes for removeDeadExprStatementSafely() method
        Node n = new Node(Token.BLOCK);

        // Test removeDeadExprStatementSafely() method
        eliminationPass.removeDeadExprStatementSafely(n);

        // TODO: Assert expected output here
    }

    @Test
    public void testRemoveNode() {
        // Create CompilerPass and ControlFlowGraph objects
        AbstractCompiler compiler = new AbstractCompiler();
        ControlFlowGraph<Node> cfg = new ControlFlowGraph<Node>();
        UnreachableCodeElimination uce = new UnreachableCodeElimination(compiler, true);
        UnreachableCodeElimination.EliminationPass eliminationPass = uce.new EliminationPass(cfg);

        // Create Nodes for removeNode() method
        Node parent = new Node(Token.BLOCK);
        Node n = new Node(Token.FOR);

        // Test removeNode() method
        eliminationPass.removeNode(parent, n);

        // TODO: Assert expected output here
    }
}
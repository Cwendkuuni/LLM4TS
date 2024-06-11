import org.junit.Test;
import static org.junit.Assert.*;

public class UnreachableCodeEliminationTest {

    @Test
    public void testProcess() {
        AbstractCompiler compiler = new AbstractCompiler();
        boolean removeNoOpStatements = true;
        UnreachableCodeElimination elimination = new UnreachableCodeElimination(compiler, removeNoOpStatements);
        elimination.process(null, null);
    }

    @Test
    public void testEliminationPass() {
        AbstractCompiler compiler = new AbstractCompiler();
        ControlFlowGraph<Node> cfg = new ControlFlowGraph<Node>();
        UnreachableCodeElimination.EliminationPass eliminationPass = new UnreachableCodeElimination(compiler).new EliminationPass(cfg);
        NodeTraversal traversal = new NodeTraversal(compiler, eliminationPass);
        Node node = new Node(Token.GETPROP);
        eliminationPass.visit(traversal, node, null);
    }

    @Test
    public void testTryRemoveUnconditionalBranching() {
        AbstractCompiler compiler = new AbstractCompiler();
        ControlFlowGraph<Node> cfg = new ControlFlowGraph<Node>();
        UnreachableCodeElimination.EliminationPass eliminationPass = new UnreachableCodeElimination(compiler).new EliminationPass(cfg);
        NodeTraversal traversal = new NodeTraversal(compiler, eliminationPass);
        Node node = new Node(Token.BREAK);
        eliminationPass.tryRemoveUnconditionalBranching(node);
    }

    @Test
    public void testComputeFollowing() {
        Node node = new Node(Token.RETURN);
        UnreachableCodeElimination.EliminationPass eliminationPass = new UnreachableCodeElimination().new EliminationPass(null);
        Node followingNode = eliminationPass.computeFollowing(node);
        assertNotNull(followingNode);
    }

    @Test
    public void testRemoveDeadExprStatementSafely() {
        AbstractCompiler compiler = new AbstractCompiler();
        ControlFlowGraph<Node> cfg = new ControlFlowGraph<Node>();
        UnreachableCodeElimination.EliminationPass eliminationPass = new UnreachableCodeElimination(compiler).new EliminationPass(cfg);
        NodeTraversal traversal = new NodeTraversal(compiler, eliminationPass);
        Node node = new Node(Token.EXPR_RESULT);
        eliminationPass.removeDeadExprStatementSafely(node);
    }

    @Test
    public void testRemoveNode() {
        AbstractCompiler compiler = new AbstractCompiler();
        ControlFlowGraph<Node> cfg = new ControlFlowGraph<Node>();
        UnreachableCodeElimination.EliminationPass eliminationPass = new UnreachableCodeElimination(compiler).new EliminationPass(cfg);
        NodeTraversal traversal = new NodeTraversal(compiler, eliminationPass);
        Node node = new Node(Token.RETURN);
        eliminationPass.removeNode(node);
    }
}
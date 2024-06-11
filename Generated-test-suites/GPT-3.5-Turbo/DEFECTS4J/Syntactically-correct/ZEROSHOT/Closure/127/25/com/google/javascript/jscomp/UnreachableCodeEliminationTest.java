import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import com.google.javascript.jscomp.ControlFlowGraph.Branch;
import com.google.javascript.rhino.Node;

public class UnreachableCodeEliminationTest {
    
    private UnreachableCodeElimination elimination;
    
    @Before
    public void setUp() {
        elimination = new UnreachableCodeElimination(new AbstractCompiler(), true);
    }
    
    @Test
    public void testProcess() {
        Node externs = new Node();
        Node toplevel = new Node();
        elimination.process(externs, toplevel);
    }
    
    @Test
    public void testEliminationPass() {
        NodeTraversal t = new NodeTraversal();
        Node n = new Node();
        Node parent = new Node();
        elimination.new EliminationPass(new ControlFlowGraph<Node>(new DiGraphNode<Node, Branch>(null), null)).visit(t, n, parent);
    }
    
    @Test
    public void testTryRemoveUnconditionalBranching() {
        Node n = new Node();
        elimination.new EliminationPass(new ControlFlowGraph<Node>(new DiGraphNode<Node, Branch>(null), null)).tryRemoveUnconditionalBranching(n);
    }
    
    @Test
    public void testComputeFollowing() {
        Node n = new Node();
        Node result = elimination.new EliminationPass(new ControlFlowGraph<Node>(new DiGraphNode<Node, Branch>(null), null)).computeFollowing(n);
        assertNull(result);
    }
    
    @Test
    public void testRemoveDeadExprStatementSafely() {
        Node n = new Node();
        elimination.new EliminationPass(new ControlFlowGraph<Node>(new DiGraphNode<Node, Branch>(null), null)).removeDeadExprStatementSafely(n);
    }
    
    @Test
    public void testRemoveNode() {
        Node n = new Node();
        elimination.new EliminationPass(new ControlFlowGraph<Node>(new DiGraphNode<Node, Branch>(null), null)).removeNode(n);
    }
}
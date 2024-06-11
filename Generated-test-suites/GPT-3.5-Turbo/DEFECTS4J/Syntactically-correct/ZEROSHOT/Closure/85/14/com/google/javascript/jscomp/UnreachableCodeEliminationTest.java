import org.junit.Test;

public class UnreachableCodeEliminationTest {

    @Test
    public void testEnterScope() {
        AbstractCompiler compiler = new AbstractCompiler();
        boolean removeNoOpStatements = true;
        UnreachableCodeElimination elimination = new UnreachableCodeElimination(compiler, removeNoOpStatements);
        
        elimination.enterScope(null);
    }

    @Test
    public void testExitScope() {
        AbstractCompiler compiler = new AbstractCompiler();
        boolean removeNoOpStatements = true;
        UnreachableCodeElimination elimination = new UnreachableCodeElimination(compiler, removeNoOpStatements);
        
        elimination.exitScope(null);
    }

    @Test
    public void testProcess() {
        AbstractCompiler compiler = new AbstractCompiler();
        boolean removeNoOpStatements = true;
        UnreachableCodeElimination elimination = new UnreachableCodeElimination(compiler, removeNoOpStatements);
        
        elimination.process(null, null);
    }

    @Test
    public void testVisit() {
        AbstractCompiler compiler = new AbstractCompiler();
        boolean removeNoOpStatements = true;
        UnreachableCodeElimination elimination = new UnreachableCodeElimination(compiler, removeNoOpStatements);
        
        NodeTraversal t = new NodeTraversal(compiler, null);
        Node n = new Node(Token.EMPTY);
        Node parent = new Node(Token.EMPTY);
        
        elimination.visit(t, n, parent);
    }

    @Test
    public void testTryRemoveUnconditionalBranching() {
        AbstractCompiler compiler = new AbstractCompiler();
        boolean removeNoOpStatements = true;
        UnreachableCodeElimination elimination = new UnreachableCodeElimination(compiler, removeNoOpStatements);
        
        Node n = new Node(Token.EMPTY);
        
        elimination.tryRemoveUnconditionalBranching(n);
    }

    @Test
    public void testComputeFollowing() {
        AbstractCompiler compiler = new AbstractCompiler();
        boolean removeNoOpStatements = true;
        UnreachableCodeElimination elimination = new UnreachableCodeElimination(compiler, removeNoOpStatements);
        
        Node n = new Node(Token.EMPTY);
        
        elimination.computeFollowing(n);
    }

    @Test
    public void testRemoveDeadExprStatementSafely() {
        AbstractCompiler compiler = new AbstractCompiler();
        boolean removeNoOpStatements = true;
        UnreachableCodeElimination elimination = new UnreachableCodeElimination(compiler, removeNoOpStatements);
        
        Node n = new Node(Token.EMPTY);
        
        elimination.removeDeadExprStatementSafely(n);
    }
}
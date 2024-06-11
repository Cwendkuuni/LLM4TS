import org.junit.Test;

public class UnreachableCodeEliminationTest {

    @Test
    public void testRemoveDeadCode() {
        // create dummy compiler and removeNoOpStatements
        AbstractCompiler compiler = new AbstractCompiler();
        boolean removeNoOpStatements = true;
        
        // create UnreachableCodeElimination object
        UnreachableCodeElimination uce = new UnreachableCodeElimination(compiler, removeNoOpStatements);
        
        // create dummy nodes
        Node externs = new Node(Token.EXTERN);
        Node root = new Node(Token.ROOT);
        
        // call process method
        uce.process(externs, root);
        
        // assert code changes
        // TODO: implement assertion
    }
}
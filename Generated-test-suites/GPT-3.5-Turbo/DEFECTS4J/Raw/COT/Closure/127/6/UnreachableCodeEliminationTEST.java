
import static org.junit.Assert.*;
import org.junit.Test;

public class UnreachableCodeEliminationTest {

@Test
public void testProcess_emptyCode() {
    AbstractCompiler compiler = new AbstractCompiler();
    UnreachableCodeElimination elimination = new UnreachableCodeElimination(compiler, true);
    Node externs = new Node(Token.EXTERN);
    Node toplevel = new Node(Token.SCRIPT);
    elimination.process(externs, toplevel);
    // TODO: Verify that the code was correctly processed
}

@Test
public void testProcess_deadCodeAfterReturn() {
    AbstractCompiler compiler = new AbstractCompiler();
    UnreachableCodeElimination elimination = new UnreachableCodeElimination(compiler, true);
    Node externs = new Node(Token.EXTERN);
    Node toplevel = new Node(Token.SCRIPT);
    Node function = new Node(Token.FUNCTION, new Node(Token.NAME, "foo"), new Node(Token.EMPTY));
    Node returnStatement = new Node(Token.RETURN);
    function.addChildToFront(returnStatement);
    toplevel.addChildToFront(function);
    elimination.process(externs, toplevel);
    // TODO: Verify that the code after the return statement was removed
}

@Test
public void testProcess_noOpStatements() {
    AbstractCompiler compiler = new AbstractCompiler();
    UnreachableCodeElimination elimination = new UnreachableCodeElimination(compiler, true);
    Node externs = new Node(Token.EXTERN);
    Node toplevel = new Node(Token.SCRIPT);
    Node statement1 = new Node(Token.EXPR_RESULT, new Node(Token.NAME, "foo"));
    Node statement2 = new Node(Token.EXPR_RESULT, new Node(Token.TRUE));
    toplevel.addChildToFront(statement1);
    toplevel.addChildToFront(statement2);
    elimination.process(externs, toplevel);
    // TODO: Verify that the no-op statements were removed
}

@Test
public void testProcess_unconditionalBranching() {
    AbstractCompiler compiler = new AbstractCompiler();
    UnreachableCodeElimination elimination = new UnreachableCodeElimination(compiler, true);
    Node externs = new Node(Token.EXTERN);
    Node toplevel = new Node(Token.SCRIPT);
    Node returnStatement = new Node(Token.RETURN);
    Node breakStatement = new Node(Token.BREAK);
    Node continueStatement = new Node(Token.CONTINUE);
    Function<Node, Branch> getBranchValue = n -> Branch.UNCOND;
    elimination.process(externs, toplevel);
    // TODO: Verify that the unconditional branching statements were removed
}

}

import org.junit.Test;
import static org.junit.Assert.*;

public class MustBeReachingVariableDefTest {

    @Test
    public void testComputeMustDef() {
        // Initialize the class and necessary objects
        ControlFlowGraph<Node> cfg = new ControlFlowGraph<>();
        Scope jsScope = new Scope();
        AbstractCompiler compiler = new AbstractCompiler();
        MustBeReachingVariableDef mustBeReachingVariableDef = new MustBeReachingVariableDef(cfg, jsScope, compiler);

        // Test case for a simple assignment operation
        Node node1 = new Node(Token.ASSIGN, new Node(Token.NAME, new Node("x")), new Node("10"));
        MustDef result1 = mustBeReachingVariableDef.flowThrough(node1, new MustDef());
        MustDef expected1 = new MustDef();
        expected1.reachingDef.put(new Var("x"), new Definition(node1));
        assertEquals(expected1, result1);

        // Test case for an assignment operation with dependencies
        Node node2 = new Node(Token.ASSIGN, new Node(Token.NAME, new Node("y")), new Node(Token.ADD, new Node(Token.NAME, new Node("x")), new Node("5")));
        MustDef result2 = mustBeReachingVariableDef.flowThrough(node2, expected1);
        MustDef expected2 = new MustDef();
        expected2.reachingDef.put(new Var("x"), new Definition(node1));
        expected2.reachingDef.put(new Var("y"), new Definition(node2));
        expected2.reachingDef.get(new Var("x")).depends.add(new Var("x"));
        assertEquals(expected2, result2);
    }

    @Test
    public void testGetDef() {
        // Initialize the class and necessary objects
        ControlFlowGraph<Node> cfg = new ControlFlowGraph<>();
        Scope jsScope = new Scope();
        AbstractCompiler compiler = new AbstractCompiler();
        MustBeReachingVariableDef mustBeReachingVariableDef = new MustBeReachingVariableDef(cfg, jsScope, compiler);

        // Test case for getting the must reaching definition of a variable
        Node node1 = new Node(Token.ASSIGN, new Node(Token.NAME, new Node("x")), new Node("10"));
        MustDef result1 = mustBeReachingVariableDef.flowThrough(node1, new MustDef());
        Node defNode = mustBeReachingVariableDef.getDef("x", node1);
        assertEquals(node1, defNode);
    }

    @Test
    public void testDependsOnOuterScopeVars() {
        // Initialize the class and necessary objects
        ControlFlowGraph<Node> cfg = new ControlFlowGraph<>();
        Scope jsScope = new Scope();
        AbstractCompiler compiler = new AbstractCompiler();
        MustBeReachingVariableDef mustBeReachingVariableDef = new MustBeReachingVariableDef(cfg, jsScope, compiler);

        // Test case for checking if a variable depends on outer scope variables
        Node node1 = new Node(Token.ASSIGN, new Node(Token.NAME, new Node("x")), new Node("10"));
        MustDef result1 = mustBeReachingVariableDef.flowThrough(node1, new MustDef());
        boolean dependsOnOuterVars = mustBeReachingVariableDef.dependsOnOuterScopeVars("x", node1);
        assertFalse(dependsOnOuterVars);
    }
}
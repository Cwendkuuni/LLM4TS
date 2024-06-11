import org.junit.Test;
import static org.junit.Assert.*;

public class MustBeReachingVariableDefTest {
    @Test
    public void testGetDef() {
        // Create a MustBeReachingVariableDef object for testing
        ControlFlowGraph<Node> cfg = new ControlFlowGraph<Node>();
        Scope jsScope = new Scope();
        AbstractCompiler compiler = new AbstractCompiler();
        MustBeReachingVariableDef tester = new MustBeReachingVariableDef(cfg, jsScope, compiler);
        
        // Test case 1: Node has a reaching definition
        Node node = new Node(Token.NAME, "test");
        GraphNode<Node, Branch> n = new GraphNode<Node, Branch>(node);
        FlowState<MustDef> state = new FlowState<MustDef>();
        MustDef mustDef = new MustDef();
        Var var = new Var();
        var.setString("test");
        Definition def = new Definition(node);
        mustDef.reachingDef.put(var, def);
        state.setIn(mustDef);
        n.setAnnotation(state);
        cfg.addNode(n);
        
        Node result1 = tester.getDef("test", node);
        assertEquals(node, result1);
        
        // Test case 2: Node does not have a reaching definition
        Node result2 = tester.getDef("test", new Node(Token.NAME, "anotherNode"));
        assertNull(result2);
    }
    
    @Test
    public void testDependsOnOuterScopeVars() {
        // Create a MustBeReachingVariableDef object for testing
        ControlFlowGraph<Node> cfg = new ControlFlowGraph<Node>();
        Scope jsScope = new Scope();
        AbstractCompiler compiler = new AbstractCompiler();
        MustBeReachingVariableDef tester = new MustBeReachingVariableDef(cfg, jsScope, compiler);
        
        // Test case 1: Variable depends on outer scope vars
        Node node = new Node(Token.NAME, "test");
        GraphNode<Node, Branch> n = new GraphNode<Node, Branch>(node);
        FlowState<MustDef> state = new FlowState<MustDef>();
        MustDef mustDef = new MustDef();
        Var var = new Var();
        var.setString("test");
        Definition def = new Definition(node);
        Var outerVar = new Var();
        outerVar.scope = new Scope();
        def.depends.add(outerVar);
        mustDef.reachingDef.put(var, def);
        state.setIn(mustDef);
        n.setAnnotation(state);
        cfg.addNode(n);
        
        boolean result1 = tester.dependsOnOuterScopeVars("test", node);
        assertTrue(result1);
        
        // Test case 2: Variable does not depend on outer scope vars
        boolean result2 = tester.dependsOnOuterScopeVars("test", new Node(Token.NAME, "anotherNode"));
        assertFalse(result2);
    }
}
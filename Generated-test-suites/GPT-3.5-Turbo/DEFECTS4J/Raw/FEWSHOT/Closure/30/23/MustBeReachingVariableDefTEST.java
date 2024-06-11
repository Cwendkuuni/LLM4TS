import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class MustBeReachingVariableDefTest {
    @Test
    public void testComputeMustDef() {
        ControlFlowGraph<Node> cfg = new ControlFlowGraph<>();
        Scope jsScope = new Scope();
        AbstractCompiler compiler = new AbstractCompiler();
        
        MustBeReachingVariableDef variableDef = new MustBeReachingVariableDef(cfg, jsScope, compiler);
        Node n = new Node(Token.FOR);
        variableDef.computeMustDef(n, n, variableDef.createInitialEstimateLattice(), false);
        
        // Assert the result
        
    }
    
    @Test
    public void testGetDef() {
        ControlFlowGraph<Node> cfg = new ControlFlowGraph<>();
        Scope jsScope = new Scope();
        AbstractCompiler compiler = new AbstractCompiler();
        
        MustBeReachingVariableDef variableDef = new MustBeReachingVariableDef(cfg, jsScope, compiler);
        Node useNode = new Node(Token.NAME, "name");
        variableDef.getDef("name", useNode);
        
        // Assert the result
        
    }
    
    @Test
    public void testDependsOnOuterScopeVars() {
        ControlFlowGraph<Node> cfg = new ControlFlowGraph<>();
        Scope jsScope = new Scope();
        AbstractCompiler compiler = new AbstractCompiler();
        
        MustBeReachingVariableDef variableDef = new MustBeReachingVariableDef(cfg, jsScope, compiler);
        Node useNode = new Node(Token.NAME, "name");
        variableDef.dependsOnOuterScopeVars("name", useNode);
        
        // Assert the result
        
    }
}
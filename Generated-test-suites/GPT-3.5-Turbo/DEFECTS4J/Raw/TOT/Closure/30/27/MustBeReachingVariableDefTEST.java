import org.junit.Before;
import org.junit.Test;

public class MustBeReachingVariableDefTest {

    private MustBeReachingVariableDef mustBeReachingVariableDef;

    @Before
    public void setUp() {
        ControlFlowGraph<Node> cfg = new ControlFlowGraph<>();
        Scope jsScope = new Scope();
        AbstractCompiler compiler = new AbstractCompiler();
        mustBeReachingVariableDef = new MustBeReachingVariableDef(cfg, jsScope, compiler);
    }

    @Test
    public void testGetDef() {
        String name = "testVariable";
        Node useNode = new Node(Token.USE_VARIABLE);
        Node defNode = new Node(Token.DEF_VARIABLE);
        GraphNode<Node, Branch> n = new GraphNode<>(useNode);
        FlowState<MustBeReachingVariableDef.MustDef> state = new FlowState<>(null, null);
        MustBeReachingVariableDef.MustDef mustDef = new MustBeReachingVariableDef.MustDef();

        mustDef.reachingDef.put(new Var(name), new MustBeReachingVariableDef.Definition(defNode));
        state.setIn(mustDef);
        n.setAnnotation(state);

        Node result = mustBeReachingVariableDef.getDef(name, useNode);

        assertEquals(defNode, result);
    }

    @Test
    public void testDependsOnOuterScopeVars() {
        String name = "testVariable";
        Node useNode = new Node(Token.USE_VARIABLE);
        Node defNode = new Node(Token.DEF_VARIABLE);
        GraphNode<Node, Branch> n = new GraphNode<>(useNode);
        FlowState<MustBeReachingVariableDef.MustDef> state = new FlowState<>(null, null);
        MustBeReachingVariableDef.MustDef mustDef = new MustBeReachingVariableDef.MustDef();

        Var outerScopeVar = new Var("outerScopeVar", null);
        outerScopeVar.scope = new Scope();
        outerScopeVar.scope.getRootNode().addChildToFront(new Node(Token.SCRIPT));
        mustDef.reachingDef.put(new Var(name), new MustBeReachingVariableDef.Definition(defNode));
        mustDef.reachingDef.get(new Var(name)).depends.add(outerScopeVar);
        state.setIn(mustDef);
        n.setAnnotation(state);

        boolean result = mustBeReachingVariableDef.dependsOnOuterScopeVars(name, useNode);

        assertTrue(result);
    }
}
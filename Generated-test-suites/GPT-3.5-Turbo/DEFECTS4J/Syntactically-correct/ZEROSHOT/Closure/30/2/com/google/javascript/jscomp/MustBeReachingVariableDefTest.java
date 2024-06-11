import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class MustBeReachingVariableDefTest {

  private MustBeReachingVariableDef variableDef;

  @Before
  public void setUp() {
    // Initialize the MustBeReachingVariableDef object with a ControlFlowGraph and a Scope
    ControlFlowGraph<Node> cfg = new ControlFlowGraph<>();
    Scope jsScope = new Scope();
    AbstractCompiler compiler = new AbstractCompiler();
    variableDef = new MustBeReachingVariableDef(cfg, jsScope, compiler);
  }

  @After
  public void tearDown() {
    variableDef = null;
  }

  @Test
  public void testGetDef() {
    Node def = variableDef.getDef("A", new Node());
    assertNull(def);

    Node useNode = new Node();
    GraphNode<Node, MustBeReachingVariableDef.MustDef> node = new GraphNode<>(useNode);
    node.setAnnotation(new FlowState<>(new MustBeReachingVariableDef.MustDef()));
    variableDef.getCfg().putNode(useNode, node);
    def = variableDef.getDef("A", useNode);
    assertNotNull(def);
  }

  @Test
  public void testDependsOnOuterScopeVars() {
    boolean depends = variableDef.dependsOnOuterScopeVars("A", new Node());
    assertFalse(depends);

    Node useNode = new Node();
    GraphNode<Node, MustBeReachingVariableDef.MustDef> node = new GraphNode<>(useNode);
    MustBeReachingVariableDef.MustDef mustDef = new MustBeReachingVariableDef.MustDef();
    mustDef.depends.add(new Scope.Var());
    node.setAnnotation(new FlowState<>(mustDef));
    variableDef.getCfg().putNode(useNode, node);
    depends = variableDef.dependsOnOuterScopeVars("A", useNode);
    assertTrue(depends);
  }

}
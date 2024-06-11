import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class MustBeReachingVariableDefTest {

  private MustBeReachingVariableDef analysis;
  private ControlFlowGraph<Node> cfg;
  private Scope jsScope;
  private AbstractCompiler compiler;
  private Set<Var> escaped;

  @Before
  public void setUp() {
    // Set up the necessary objects for testing
    cfg = new ControlFlowGraph<Node>();
    jsScope = new Scope();
    compiler = new AbstractCompiler();
    escaped = new HashSet<>();
    analysis = new MustBeReachingVariableDef(cfg, jsScope, compiler);
  }

  @Test
  public void testGetDef() {
    Node node1 = new Node(Token.NAME, "A");
    Node node2 = new Node(Token.NAME, "alert");
    Node node3 = new Node(Token.NAME, "A");
    node1.addChildToBack(node2);
    node2.addChildToBack(node3);
    cfg.addNode(node1);
    FlowState<MustDef> state = new FlowState<MustDef>(cfg);
    state.getIn().reachingDef.put(jsScope.getVar("A"), new Definition(node1));
    node1.putProp(ConcreteTypeGraph.BRANCHED,
            Branch.UNCOND);
    analysis.getCfg().createEdge(node1, node2, Branch.UNCOND);
    GraphNode<Node, Branch> n = analysis.getCfg().getNode(node1);
    n.setAnnotation(state);
    
    // Test getting the correct definition node
    Node defNode = analysis.getDef("A", node2);
    assertEquals(node1, defNode);
  }

  @Test
  public void testDependsOnOuterScopeVars() {
    Node node1 = new Node(Token.NAME, "A");
    Node node2 = new Node(Token.NAME, "alert");
    Node node3 = new Node(Token.NAME, "A");
    node1.addChildToBack(node2);
    node2.addChildToBack(node3);
    cfg.addNode(node1);
    FlowState<MustDef> state = new FlowState<MustDef>(cfg);
    Definition def = new Definition(node1);
    def.depends.add(jsScope.getVar("B"));
    state.getIn().reachingDef.put(jsScope.getVar("A"), def);
    node1.putProp(ConcreteTypeGraph.BRANCHED,
            Branch.UNCOND);
    analysis.getCfg().createEdge(node1, node2, Branch.UNCOND);
    GraphNode<Node, Branch> n = analysis.getCfg().getNode(node1);
    n.setAnnotation(state);
    
    // Test that dependsOnOuterScopeVars returns true since there is an outer scope variable dependency
    boolean depends = analysis.dependsOnOuterScopeVars("A", node2);
    assertTrue(depends);
  }

}
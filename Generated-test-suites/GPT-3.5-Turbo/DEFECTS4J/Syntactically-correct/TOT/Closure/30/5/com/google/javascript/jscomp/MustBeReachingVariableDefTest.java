import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

public class MustBeReachingVariableDefTest {

  private MustBeReachingVariableDef mustBeReachingVariableDef;

  @Before
  public void setup() {
    // Set up the MustBeReachingVariableDef class with a sample control flow graph and scope
    ControlFlowGraph<Node> cfg = new ControlFlowGraph<>();
    Scope jsScope = new Scope();
    AbstractCompiler compiler = new AbstractCompiler();

    mustBeReachingVariableDef = new MustBeReachingVariableDef(cfg, jsScope, compiler);
  }

  @Test
  public void testGetDef() {
    // Test getting a definition for a valid variable name and use node
    String name = "var1";
    Node useNode = new Node(Token.NAME, name);

    Node def = mustBeReachingVariableDef.getDef(name, useNode);

    assertNull(def);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testGetDefInvalidNode() {
    // Test getting a definition for an invalid use node
    String name = "var1";
    Node useNode = new Node();

    mustBeReachingVariableDef.getDef(name, useNode);
  }

  @Test
  public void testDependsOnOuterScopeVars() {
    // Test checking if a variable depends on outer scope variables
    String name = "var1";
    Node useNode = new Node(Token.NAME, name);

    boolean dependsOnOuterScopeVars = mustBeReachingVariableDef.dependsOnOuterScopeVars(name, useNode);

    assertFalse(dependsOnOuterScopeVars);
  }

  @Test
  public void testDependsOnOuterScopeVarsDependencies() {
    // Test checking if a variable depends on outer scope variables with dependencies
    String name = "var1";
    Node useNode = new Node(Token.NAME, name);

    // Add a dependency to the variable
    ControlFlowGraph<Node> cfg = mustBeReachingVariableDef.getCfg();
    GraphNode<Node, MustBeReachingVariableDef.MustDef> n = cfg.getNode(useNode);
    n.getAnnotation().getIn().reachingDef.get(mustBeReachingVariableDef.jsScope.getVar(name)).depends.add(new Var());

    boolean dependsOnOuterScopeVars = mustBeReachingVariableDef.dependsOnOuterScopeVars(name, useNode);

    assertTrue(dependsOnOuterScopeVars);
  }

}
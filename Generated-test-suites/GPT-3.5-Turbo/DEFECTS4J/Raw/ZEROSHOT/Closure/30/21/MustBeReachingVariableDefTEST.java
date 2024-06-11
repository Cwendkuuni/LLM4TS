import static org.junit.Assert.*;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import org.junit.After;
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

  @After
  public void tearDown() {
    mustBeReachingVariableDef = null;
  }

  @Test
  public void testCreateEntryLattice() {
    MustDef mustDef = mustBeReachingVariableDef.createEntryLattice();

    Iterator<Var> vars = mustDef.reachingDef.keySet().iterator();
    while (vars.hasNext()) {
      Var var = vars.next();
      Definition definition = mustDef.reachingDef.get(var);
      assertNotNull(definition);
      assertNotNull(definition.node);
    }
  }

  @Test
  public void testCreateInitialEstimateLattice() {
    MustDef mustDef = mustBeReachingVariableDef.createInitialEstimateLattice();
    assertNotNull(mustDef);
    assertNotNull(mustDef.reachingDef);
  }

  @Test
  public void testFlowThrough() {
    Node n = new Node();
    MustDef input = new MustDef();
    MustDef output = mustBeReachingVariableDef.flowThrough(n, input);
    assertNotNull(output);
    assertEquals(input.reachingDef, output.reachingDef);
  }

  @Test
  public void testGetDef() {
    String name = "variable";
    Node useNode = new Node();

    Node def = mustBeReachingVariableDef.getDef(name, useNode);
    assertNull(def);
  }

  @Test
  public void testDependsOnOuterScopeVars() {
    String name = "variable";
    Node useNode = new Node();

    boolean dependsOnOuterScope = mustBeReachingVariableDef.dependsOnOuterScopeVars(name, useNode);
    assertFalse(dependsOnOuterScope);
  }

}
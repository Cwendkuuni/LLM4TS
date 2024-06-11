import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.ControlFlowGraph;
import com.google.javascript.jscomp.MustBeReachingVariableDef;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.rhino.Node;

public class MustBeReachingVariableDefTest {

  private ControlFlowGraph<Node> cfg;
  private Scope jsScope;
  private AbstractCompiler compiler;

  @Before
  public void setUp() {
    cfg = new ControlFlowGraph<Node>(null);
    jsScope = new Scope(Node.newNumber(1));
    compiler = new AbstractCompiler();
  }

  @Test
  public void testGetDef_ExistingDefinition_ReturnsCorrectNode() {
    Node defNode = Node.newString("defNode");
    MustBeReachingVariableDef mustBeReachingVariableDef = new MustBeReachingVariableDef(
        cfg, jsScope, compiler);
    mustBeReachingVariableDef.getDef("foo", defNode);
    Node result = mustBeReachingVariableDef.getDef("foo", defNode);
    assertEquals(defNode, result);
  }

  @Test
  public void testGetDef_NoExistingDefinition_ReturnsNull() {
    Node defNode = Node.newString("defNode");
    MustBeReachingVariableDef mustBeReachingVariableDef = new MustBeReachingVariableDef(
        cfg, jsScope, compiler);
    Node result = mustBeReachingVariableDef.getDef("foo", defNode);
    assertNull(result);
  }

  @Test
  public void testDependsOnOuterScopeVars_DoesNotDependOnOuterScopeVars_ReturnsFalse() {
    Node useNode = Node.newString("useNode");
    MustBeReachingVariableDef mustBeReachingVariableDef = new MustBeReachingVariableDef(
        cfg, jsScope, compiler);
    boolean result = mustBeReachingVariableDef.dependsOnOuterScopeVars("foo", useNode);
    assertFalse(result);
  }

  @Test
  public void testDependsOnOuterScopeVars_DependOnOuterScopeVars_ReturnsTrue() {
    Node useNode = Node.newString("useNode");
    MustBeReachingVariableDef mustBeReachingVariableDef = new MustBeReachingVariableDef(
        cfg, jsScope, compiler);
    boolean result = mustBeReachingVariableDef.dependsOnOuterScopeVars("bar", useNode);
    assertTrue(result);
  }
}
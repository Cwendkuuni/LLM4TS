import static org.junit.Assert.*;

import org.junit.Test;

import com.google.common.base.Preconditions;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.google.javascript.jscomp.ControlFlowGraph.AbstractCfgNodeTraversalCallback;
import com.google.javascript.jscomp.ControlFlowGraph.Branch;
import com.google.javascript.jscomp.Scope.Var;
import com.google.javascript.jscomp.graph.GraphNode;
import com.google.javascript.jscomp.graph.LatticeElement;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.Token;

import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.annotation.Nullable;

public class MustBeReachingVariableDefTest {

  @Test
  public void testGetDef() {
    MustBeReachingVariableDef analyzer = new MustBeReachingVariableDef(cfg, jsScope, compiler);
    
    Node def = analyzer.getDef("name", useNode);
    assertEquals(null, def);
  }
  
  @Test
  public void testDependsOnOuterScopeVars() {
    MustBeReachingVariableDef analyzer = new MustBeReachingVariableDef(cfg, jsScope, compiler);
    
    boolean depends = analyzer.dependsOnOuterScopeVars("name", useNode);
    assertFalse(depends);
  }
  
}
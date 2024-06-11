import com.google.common.base.Preconditions;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.google.javascript.jscomp.ControlFlowGraph.AbstractCfgNodeTraversalCallback;
import com.google.javascript.jscomp.ControlFlowGraph.Branch;
import com.google.javascript.jscomp.MustBeReachingVariableDef.MustDef;
import com.google.javascript.jscomp.Scope.Var;
import com.google.javascript.jscomp.graph.GraphNode;
import com.google.javascript.jscomp.graph.LatticeElement;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.Token;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

public class MustBeReachingVariableDefTest {

    private MustBeReachingVariableDef mustBeReachingVariableDef;

    @Before
    public void setUp() {
        Node rootNode = new Node();
        ControlFlowGraph<Node> cfg = new ControlFlowGraph<>(rootNode);
        Scope jsScope = new Scope(rootNode);
        AbstractCompiler compiler = new AbstractCompiler();
        mustBeReachingVariableDef = new MustBeReachingVariableDef(cfg, jsScope, compiler);
    }

    @Test
    public void testGetDef() {
        String name = "testVariable";
        Node useNode = new Node();
        Node defNode = mustBeReachingVariableDef.getDef(name, useNode);
        Assert.assertNull(defNode);
    }

    @Test
    public void testDependsOnOuterScopeVars() {
        String name = "testVariable";
        Node useNode = new Node();
        boolean dependsOnOuterScopeVars = mustBeReachingVariableDef.dependsOnOuterScopeVars(name, useNode);
        Assert.assertFalse(dependsOnOuterScopeVars);
    }
}
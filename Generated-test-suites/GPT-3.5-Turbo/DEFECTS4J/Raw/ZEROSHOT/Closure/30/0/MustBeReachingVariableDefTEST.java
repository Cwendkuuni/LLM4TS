import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.google.javascript.jscomp.*;
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
    private ControlFlowGraph<Node> cfg;
    private Scope jsScope;
    private AbstractCompiler compiler;

    @Before
    public void setUp() {
        cfg = new ControlFlowGraph<>();
        jsScope = new Scope();
        compiler = new AbstractCompiler();
        mustBeReachingVariableDef = new MustBeReachingVariableDef(cfg, jsScope, compiler);
    }

    @Test
    public void testCreateEntryLattice() {
        MustBeReachingVariableDef.MustDef mustDef = mustBeReachingVariableDef.createEntryLattice();
        Assert.assertEquals(mustDef.reachingDef.size(), 0);
    }

    @Test
    public void testCreateInitialEstimateLattice() {
        MustBeReachingVariableDef.MustDef mustDef = mustBeReachingVariableDef.createInitialEstimateLattice();
        Assert.assertEquals(mustDef.reachingDef.size(), 0);
    }

    @Test
    public void testFlowThrough() {
        Node n = new Node(Token.EMPTY);
        MustBeReachingVariableDef.MustDef input = new MustBeReachingVariableDef.MustDef();
        MustBeReachingVariableDef.MustDef output = mustBeReachingVariableDef.flowThrough(n, input);
        Assert.assertEquals(input.reachingDef, output.reachingDef);
    }

    @Test
    public void testGetDef() {
        String name = "name";
        Node useNode = new Node(Token.EMPTY);
        Node def = mustBeReachingVariableDef.getDef(name, useNode);
        Assert.assertNull(def);
    }

    @Test
    public void testDependsOnOuterScopeVars() {
        String name = "name";
        Node useNode = new Node(Token.EMPTY);
        boolean depends = mustBeReachingVariableDef.dependsOnOuterScopeVars(name, useNode);
        Assert.assertFalse(depends);
    }

}
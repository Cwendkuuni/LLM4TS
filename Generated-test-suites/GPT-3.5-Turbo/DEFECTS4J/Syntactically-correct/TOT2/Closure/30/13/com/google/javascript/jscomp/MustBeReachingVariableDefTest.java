package com.google.javascript.jscomp;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

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
    public void testCreateEntryLattice() {
        MustBeReachingVariableDef.MustDef mustDef = mustBeReachingVariableDef.createEntryLattice();
        assertNotNull(mustDef);
    }

    @Test
    public void testCreateInitialEstimateLattice() {
        MustBeReachingVariableDef.MustDef mustDef = mustBeReachingVariableDef.createInitialEstimateLattice();
        assertNotNull(mustDef);
    }

    @Test
    public void testFlowThrough() {
        Node n = new Node(Token.FUNCTION);
        MustBeReachingVariableDef.MustDef input = new MustBeReachingVariableDef.MustDef();
        MustBeReachingVariableDef.MustDef output = mustBeReachingVariableDef.flowThrough(n, input);
        assertNotNull(output);
    }

    @Test
    public void testGetDef() {
        Node n = new Node(Token.NAME, "test");
        n.setInputId(Token.NAME);
        Node useNode = new Node(Token.USE_NAME, "test");
        useNode.setInputId(Token.USE_NAME);
        Node defNode = mustBeReachingVariableDef.getDef("test", useNode);
        assertEquals(n, defNode);
    }

    @Test
    public void testDependsOnOuterScopeVars() {
        Node n = new Node(Token.NAME, "test");
        n.setInputId(Token.NAME);
        Node useNode = new Node(Token.USE_NAME, "test");
        useNode.setInputId(Token.USE_NAME);
        boolean result = mustBeReachingVariableDef.dependsOnOuterScopeVars("test", useNode);
        assertFalse(result);
    }
}
package com.google.javascript.jscomp;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import com.google.common.collect.Sets;
import com.google.javascript.rhino.Node;

import org.junit.Before;
import org.junit.Test;

import java.util.Date;

public class MustBeReachingVariableDefTest {

    private MustBeReachingVariableDef def;

    @Before
    public void setUp() {
        ControlFlowGraph<Node> cfg = null;
        Scope jsScope = null;
        AbstractCompiler compiler = null;
        def = new MustBeReachingVariableDef(cfg, jsScope, compiler);
    }

    @Test
    public void testGetDef() {
        Node node = new Node(Token.FUNCTION);

        Node defNode = def.getDef("name", node);
        assertNull(defNode);

        node.getFirstChild().setString("name");
        Node defNode2 = def.getDef("name", node);
        assertEquals(node, defNode2);
    }

    @Test
    public void testDependsOnOuterScopeVars() {
        Node node = new Node(Token.FUNCTION);

        boolean depends = def.dependsOnOuterScopeVars("name", node);
        assertFalse(depends);

        node.getFirstChild().setString("name");
        boolean depends2 = def.dependsOnOuterScopeVars("name", node);
        assertTrue(depends2);
    }
}
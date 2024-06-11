package com.google.javascript.jscomp;

import static org.junit.Assert.*;
import static org.junit.Assert.assertNull;

import org.junit.Test;

import com.google.common.base.Preconditions;
import com.google.javascript.rhino.ErrorReporter;
import com.google.javascript.rhino.JSDocInfo;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.Token;
import com.google.javascript.rhino.jstype.FunctionType;
import com.google.javascript.rhino.jstype.JSType;
import com.google.javascript.rhino.jstype.ObjectType;
import com.google.javascript.rhino.jstype.StaticScope;
import com.google.javascript.rhino.jstype.StaticSlot;
import com.google.javascript.rhino.jstype.StaticTypeRegistry;
import com.google.javascript.rhino.node.GlobalScope;
import com.google.javascript.rhino.node.RootNode;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

public class ScopeTest {
    private static final ErrorReporter errorReporter = new ErrorReporter() {
        @Override
        public void error(String message, Object[] args) {
            throw new RuntimeException(message);
        }
    };

    @Test
    public void testCreateScopeWithParentAndRootNode() {
        Node rootNode = new RootNode();
        Scope parentScope = new Scope(null, rootNode);
        Scope childScope = new Scope(parentScope, rootNode);

        assertEquals(parentScope, childScope.getParent());
        assertEquals(rootNode, childScope.getRootNode());
    }

    @Test
    public void testCreateGlobalScope() {
        Node rootNode = new RootNode();
        Scope globalScope = new Scope(rootNode, null);

        assertEquals(null, globalScope.getParent());
        assertEquals(rootNode, globalScope.getRootNode());
    }

    @Test
    public void testCreateEmptyScope() {
        Node rootNode = new RootNode();
        Scope emptyScope = new Scope(rootNode, null);

        assertEquals(null, emptyScope.getParent());
        assertEquals(rootNode, emptyScope.getRootNode());
        assertTrue(emptyScope.isBottom());
    }

    @Test
    public void testGetDepth() {
        Node rootNode = new RootNode();
        Scope parentScope = new Scope(null, rootNode);
        Scope childScope = new Scope(parentScope, rootNode);

        assertEquals(0, parentScope.getDepth());
        assertEquals(1, childScope.getDepth());
    }

    @Test
    public void testIsBottom() {
        Node rootNode = new RootNode();
        Scope parentScope = new Scope(null, rootNode);
        Scope emptyScope = new Scope(rootNode, null);

        assertFalse(parentScope.isBottom());
        assertTrue(emptyScope.isBottom());
    }

    @Test
    public void testGetParent() {
        Node rootNode = new RootNode();
        Scope parentScope = new Scope(null, rootNode);
        Scope childScope = new Scope(parentScope, rootNode);

        assertEquals(parentScope, childScope.getParent());
    }

    @Test
    public void testGetRootNode() {
        Node rootNode = new RootNode();
        Scope parentScope = new Scope(null, rootNode);
        Scope childScope = new Scope(parentScope, rootNode);

        assertEquals(rootNode, childScope.getRootNode());
    }

    @Test
    public void testGetGlobalScope() {
        Node rootNode = new RootNode();
        Scope globalScope = new Scope(rootNode, null);

        assertEquals(globalScope, globalScope.getGlobalScope());
    }

    @Test
    public void testDeclareVariable() {
        Node rootNode = new RootNode();
        Scope parentScope = new Scope(null, rootNode);
        Scope childScope = new Scope(parentScope, rootNode);

        String name = "foo";
        Var variable = childScope.declare(name, null, JSType.UNDEFINED, null);

        assertEquals(childScope, variable.getScope());
        assertNotNull(variable.getNameNode());
        assertEquals(name, variable.getName());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDeclareVariableWithNullName() {
        Node rootNode = new RootNode();
        Scope parentScope = new Scope(null, rootNode);
        Scope childScope = new Scope(parentScope, rootNode);

        childScope.declare(null, null, JSType.UNDEFINED, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDeclareVariableWithEmptyName() {
        Node rootNode = new RootNode();
        Scope parentScope = new Scope(null, rootNode);
        Scope childScope = new Scope(parentScope, rootNode);

        childScope.declare("", null, JSType.UNDEFINED, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDeclareVariableWithNullParentScope() {
        Node rootNode = new RootNode();
        Scope childScope = new Scope(null, rootNode);

        childScope.declare("foo", null, JSType.UNDEFINED, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDeclareVariableWithNullRootNode() {
        Scope parentScope = new Scope(null, null);
        Scope childScope = new Scope(parentScope, null);

        childScope.declare("foo", null, JSType.UNDEFINED, null);
    }

    @Test
    public void testGetSlot() {
        Node rootNode = new RootNode();
        Scope parentScope = new Scope(null, rootNode);
        Scope childScope = new Scope(parentScope, rootNode);

        String name = "foo";
        Var variable = childScope.declare(name, null, JSType.UNDEFINED, null);

        assertEquals(variable, childScope.getSlot(name));
    }

    @Test
    public void testGetOwnSlot() {
        Node rootNode = new RootNode();
        Scope parentScope = new Scope(null, rootNode);
        Scope childScope = new Scope(parentScope, rootNode);

        String name = "foo";
        Var variable = childScope.declare(name, null, JSType.UNDEFINED, null);

        assertEquals(variable, childScope.getOwnSlot(name));
    }

    @Test
    public void testGetVar() {
        Node rootNode = new RootNode();
        Scope parentScope = new Scope(null, rootNode);
        Scope childScope = new Scope(parentScope, rootNode);

        String name = "foo";
        Var variable = childScope.declare(name, null, JSType.UNDEFINED, null);

        assertEquals(variable, childScope.getVar(name));
    }

    @Test
    public void testUndeleclareVariable() {
        Node rootNode = new RootNode();
        Scope parentScope = new Scope(null, rootNode);
        Scope childScope = new Scope(parentScope, rootNode);

        String name = "foo";
        Var variable = childScope.declare(name, null, JSType.UNDEFINED, null);
        childScope.undeclare(variable);

        assertNull(childScope.getVar(name));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testUndeclareVariableWithNullVariable() {
        Node rootNode = new RootNode();
        Scope childScope = new Scope(null, rootNode);

        childScope.undeclare(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testUndeclareVariableWithNullScope() {
        Node rootNode = new RootNode();
        Var variable = new Var(false, "foo", null, null, null, 0, null, false, null);

        try {
            Scope.undeclare(variable);
        } catch (IllegalArgumentException e) {
            assertEquals("Variable is not in any scope.", e.getMessage());
        }
    }

    @Test
    public void testIsDeclared() {
        Node rootNode = new RootNode();
        Scope parentScope = new Scope(null, rootNode);
        Scope childScope = new Scope(parentScope, rootNode);

        String name = "foo";
        Var variable = childScope.declare(name, null, JSType.UNDEFINED, null);

        assertTrue(childScope.isDeclared(name, true));
        assertTrue(childScope.isDeclared(name, false));
        assertFalse(parentScope.isDeclared(name, true));
        assertFalse(parentScope.isDeclared(name, false));
    }

    @Test
    public void testGetVars() {
        Node rootNode = new RootNode();
        Scope parentScope = new Scope(null, rootNode);
        Scope childScope = new Scope(parentScope, rootNode);

        String name1 = "foo";
        Var variable1 = childScope.declare(name1, null, JSType.UNDEFINED, null);
        String name2 = "bar";
        Var variable2 = childScope.declare(name2, null, JSType.UNDEFINED, null);

        Iterator<Var> iterator = childScope.getVars();

        Var variable = iterator.next();
        assertEquals(variable1, variable);

        variable = iterator.next();
        assertEquals(variable2, variable);

        assertFalse(iterator.hasNext());
    }

    @Test
    public void testGetVarCount() {
        Node rootNode = new RootNode();
        Scope parentScope = new Scope(null, rootNode);
        Scope childScope = new Scope(parentScope, rootNode);

        String name1 = "foo";
        Var variable1 = childScope.declare(name1, null, JSType.UNDEFINED, null);
        String name2 = "bar";
        Var variable2 = childScope.declare(name2, null, JSType.UNDEFINED, null);

        assertEquals(2, childScope.getVarCount());
    }

    @Test
    public void testIsLocal() {
        Node rootNode = new RootNode();
        Scope globalScope = new Scope(rootNode, null);
        Scope localScope = new Scope(globalScope, rootNode);

        assertFalse(globalScope.isLocal());
        assertTrue(localScope.isLocal());
    }

    @Test
    public void testIsGlobal() {
        Node rootNode = new RootNode();
        Scope globalScope = new Scope(rootNode, null);

        assertFalse(globalScope.isGlobal());
        assertTrue(globalScope.isGlobal());
    }
}
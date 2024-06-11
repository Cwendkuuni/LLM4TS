package com.google.javascript.jscomp;

import static org.junit.Assert.*;
import static org.junit.Assert.assertNull;

import com.google.common.base.Preconditions;
import com.google.javascript.jscomp.Scope.Var;
import org.junit.Test;

public class ScopeTest {

    @Test
    public void testDeclare() {
        Scope scope = new Scope(null, new Node(Token.BLOCK, null));
        Var var1 = scope.declare("x", new Node(Token.NAME, "x"), JSType.UNDEFINED, null);
        Var var2 = scope.declare("x", new Node(Token.NAME, "x"), JSType.STRING, null);
        Var var3 = scope.declare("y", new Node(Token.NAME, "y"), JSType.NUMBER, null);

        assertEquals("x", var1.getName());
        assertEquals("x", var2.getName());
        assertEquals("y", var3.getName());
        assertEquals(Scope.thisType, scope.getTypeOfThis());
        assertEquals(3, scope.getVarCount());

        Var variableX = scope.getVar("x");
        assertEquals(var1, variableX);
        assertEquals(var2, variableX);
    }

    @Test
    public void testGetVar() {
        Scope scope = new Scope(null, new Node(Token.BLOCK, null));
        Var var1 = scope.declare("x", new Node(Token.NAME, "x"), JSType.UNDEFINED, null);
        Var var2 = scope.declare("y", new Node(Token.NAME, "y"), JSType.NUMBER, null);

        assertNull(scope.getVar("z"));
        assertEquals(var1, scope.getVar("x"));
        assertEquals(var2, scope.getVar("y"));

        Scope parentScope = new Scope(scope, new Node(Token.FUNCTION, null));
        Var parentVarX = parentScope.declare("x", new Node(Token.NAME, "x"), JSType.STRING, null);

        assertEquals(var1, scope.getVar("x"));
        assertEquals(parentVarX, parentScope.getVar("x"));
    }

    @Test
    public void testGetSlot() {
        Scope scope = new Scope(null, new Node(Token.BLOCK, null));
        Var var1 = scope.declare("x", new Node(Token.NAME, "x"), JSType.UNDEFINED, null);

        assertEquals(var1, scope.getSlot("x"));
    }

    @Test
    public void testIsDeclared() {
        Scope scope = new Scope(null, new Node(Token.BLOCK, null));
        Var var1 = scope.declare("x", new Node(Token.NAME, "x"), JSType.UNDEFINED, null);

        assertTrue(scope.isDeclared("x", true));
        assertFalse(scope.isDeclared("y", true));
        assertFalse(scope.isDeclared("x", false));
    }

    @Test
    public void testGetVars() {
        Scope scope = new Scope(null, new Node(Token.BLOCK, null));
        Var var1 = scope.declare("x", new Node(Token.NAME, "x"), JSType.UNDEFINED, null);
        Var var2 = scope.declare("y", new Node(Token.NAME, "y"), JSType.NUMBER, null);

        Iterator<Var> iterator = scope.getVars();
        assertEquals(var1, iterator.next());
        assertEquals(var2, iterator.next());
        assertFalse(iterator.hasNext());
    }
}
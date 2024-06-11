package com.google.javascript.jscomp;

import static com.google.javascript.rhino.jstype.JSTypeNative.GLOBAL_THIS;
import static org.junit.Assert.*;

import com.google.common.base.Preconditions;
import com.google.javascript.rhino.ErrorReporter;
import com.google.javascript.rhino.JSDocInfo;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.jstype.*;
import com.google.javascript.rhino.jstype.StaticScope;
import org.junit.Test;

import java.util.Iterator;
import java.util.Map;

public class ScopeTest {

    @Test
    public void testCreateScope() {
        Scope parent = new Scope(null, new Node(Token.BLOCK, null));
        Scope scope = new Scope(parent, new Node(Token.FUNCTION, null));
        assertEquals(parent, scope.getParent());
        assertEquals(0, scope.getDepth());
        assertNull(scope.getTypeOfThis());
        assertTrue(scope.isBottom());
    }

    @Test
    public void testCreateGlobalScope() {
        Scope globalScope = new Scope(new Node(Token.BLOCK, null), null);
        assertEquals(null, globalScope.getParent());
        assertEquals(0, globalScope.getDepth());
        assertNotNull(globalScope.getTypeOfThis());
    }

    @Test
    public void testDeclareVariable() {
        Scope parent = new Scope(null, new Node(Token.BLOCK, null));
        Scope scope = new Scope(parent, new Node(Token.FUNCTION, null));

        Node nameNode = new Node(Token.IDENT, "x");
        JSType type = JSType.INT;
        CompilerInput input = new CompilerInput("");

        Var var = scope.declare("x", nameNode, type, input);
        assertEquals("x", var.getName());
        assertEquals(nameNode, var.getNameNode());
        assertEquals(type, var.getType());
        assertFalse(var.isTypeInferred());
        assertEquals(input, var.getInput());
        assertTrue(var.isDefine());
        assertFalse(var.isExtern());
        assertFalse(var.isConst());
        assertNotNull(var.getScope());
        assertFalse(var.isGlobal());
        assertFalse(var.isLocal());
        assertEquals(0, var.getIndex());

        Var var2 = scope.declare("x", nameNode, type, input, true);
        assertEquals(var, var2);

        Var var3 = scope.declare("y", new Node(Token.IDENT, "y"), type, input);
        assertEquals("y", var3.getName());
        assertEquals(new Node(Token.IDENT, "y"), var3.getNameNode());
        assertNotEquals(var, var3);
        assertEquals(1, var3.getIndex());
    }

    @Test
    public void testUndeclareVariable() {
        Scope parent = new Scope(null, new Node(Token.BLOCK, null));
        Scope scope = new Scope(parent, new Node(Token.FUNCTION, null));

        Node nameNode = new Node(Token.IDENT, "x");
        JSType type = JSType.INT;
        CompilerInput input = new CompilerInput("");

        Var var = scope.declare("x", nameNode, type, input);
        assertEquals(1, scope.getVarCount());

        scope.undeclare(var);
        assertEquals(0, scope.getVarCount());
        assertNull(scope.getVar("x"));
    }

    @Test
    public void testGetVariable() {
        Scope parent = new Scope(null, new Node(Token.BLOCK, null));
        Scope scope = new Scope(parent, new Node(Token.FUNCTION, null));

        Node nameNode1 = new Node(Token.IDENT, "x");
        Node nameNode2 = new Node(Token.IDENT, "y");
        JSType type = JSType.INT;
        CompilerInput input = new CompilerInput("");

        Var var1 = scope.declare("x", nameNode1, type, input);
        Var var2 = scope.declare("y", nameNode2, type, input);

        Var getVar1 = scope.getVar("x");
        Var getVar2 = scope.getVar("y");

        assertEquals(var1, getVar1);
        assertEquals(var2, getVar2);
    }

    @Test
    public void testIsDeclared() {
        Scope parent = new Scope(null, new Node(Token.BLOCK, null));
        Scope scope = new Scope(parent, new Node(Token.FUNCTION, null));

        Node nameNode1 = new Node(Token.IDENT, "x");
        Node nameNode2 = new Node(Token.IDENT, "y");
        JSType type = JSType.INT;
        CompilerInput input = new CompilerInput("");

        Var var1 = scope.declare("x", nameNode1, type, input);
        Var var2 = scope.declare("y", nameNode2, type, input);

        assertTrue(scope.isDeclared("x", false));
        assertTrue(scope.isDeclared("x", true));
        assertTrue(scope.isDeclared("y", false));
        assertTrue(scope.isDeclared("y", true));
        assertFalse(scope.isDeclared("z", false));
        assertFalse(scope.isDeclared("z", true));
    }

    @Test
    public void testGetVars() {
        Scope parent = new Scope(null, new Node(Token.BLOCK, null));
        Scope scope = new Scope(parent, new Node(Token.FUNCTION, null));

        Node nameNode1 = new Node(Token.IDENT, "x");
        Node nameNode2 = new Node(Token.IDENT, "y");
        JSType type = JSType.INT;
        CompilerInput input = new CompilerInput("");

        Var var1 = scope.declare("x", nameNode1, type, input);
        Var var2 = scope.declare("y", nameNode2, type, input);

        Iterator<Var> it = scope.getVars();
        assertTrue(it.hasNext());
        assertEquals(var1, it.next());
        assertTrue(it.hasNext());
        assertEquals(var2, it.next());
        assertFalse(it.hasNext());
    }

    @Test
    public void testJoin() {
        Scope parent1 = new Scope(null, new Node(Token.BLOCK, null));
        Scope scope1 = new Scope(parent1, new Node(Token.FUNCTION, null));
        scope1.declare("x", new Node(Token.IDENT, "x"), JSType.INT, new CompilerInput(""));

        Scope parent2 = new Scope(null, new Node(Token.BLOCK, null));
        Scope scope2 = new Scope(parent2, new Node(Token.FUNCTION, null));
        scope2.declare("y", new Node(Token.IDENT, "y"), JSType.STRING, new CompilerInput(""));

        Scope scope3 = new Scope(scope1, new Node(Token.FUNCTION, null));
        scope3.declare("z", new Node(Token.IDENT, "z"), JSType.BOOLEAN, new CompilerInput(""));

        Map<String, Var> map1 = scope1.getVars();
        Map<String, Var> map2 = scope2.getVars();
        Map<String, Var> map3 = scope3.getVars();

        Map<String, Var> mapJoin = Scope.join(map1, map2);
        Map<String, Var> mapJoin2 = Scope.join(mapJoin, map3);

        Iterator<Map.Entry<String, Var>> it = mapJoin2.entrySet().iterator();
        assertTrue(it.hasNext());
        Map.Entry<String, Var> entry1 = it.next();
        assertEquals("x", entry1.getKey());
        assertEquals(scope1.getVar("x"), entry1.getValue());

        assertTrue(it.hasNext());
        Map.Entry<String, Var> entry2 = it.next();
        assertEquals("y", entry2.getKey());
        assertEquals(scope2.getVar("y"), entry2.getValue());

        assertTrue(it.hasNext());
        Map.Entry<String, Var> entry3 = it.next();
        assertEquals("z", entry3.getKey());
        assertEquals(scope3.getVar("z"), entry3.getValue());

        assertFalse(it.hasNext());
    }

    @Test
    public void testGetTypeOfThis() {
        Scope globalScope = new Scope(new Node(Token.BLOCK, null), null);
        assertEquals(JSType.getNativeObjectType(GLOBAL_THIS), globalScope.getTypeOfThis());

        Node rootNode = new Node(Token.FUNCTION, null);
        Scope scope = new Scope(null, rootNode);
        FunctionType functionType = new FunctionType(JSType.VOID, new ObjectType[0]);
        scope.thisType = functionType.getTypeOfThis();
        assertEquals(functionType.getTypeOfThis(), scope.getTypeOfThis());
    }
}
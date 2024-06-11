package com.google.javascript.jscomp;

import static org.junit.Assert.*;

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
import org.junit.Test;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

public class ScopeTest {

    @Test
    public void testScopeConstructorWithParentAndRootNode() {
        Scope parentScope = new Scope(null, new Node(Token.BLOCK, null, null));
        Node rootNode = new Node(Token.FUNCTION, null, null);
        Scope scope = new Scope(parentScope, rootNode);
        assertEquals(parentScope, scope.getParent());
        assertEquals(rootNode, scope.getRootNode());
        assertFalse(scope.isBottom());
    }

    @Test
    public void testScopeConstructorWithGlobalAndRootNode() {
        Node rootNode = new Node(Token.BLOCK, null, null);
        Scope scope = new Scope(rootNode, new AbstractCompiler() {});
        assertNull(scope.getParent());
        assertEquals(rootNode, scope.getRootNode());
        assertTrue(scope.isBottom());
    }

    @Test
    public void testScopeConstructorWithNullParentAndRootNode() {
        Node rootNode = new Node(Token.BLOCK, null, null);
        assertThrows(IllegalArgumentException.class, () -> {
            new Scope(null, rootNode);
        });
    }

    @Test
    public void testScopeConstructorWithNullRootNode() {
        Scope parentScope = new Scope(null, new Node(Token.BLOCK, null, null));
        assertThrows(IllegalArgumentException.class, () -> {
            new Scope(parentScope, null);
        });
    }

    @Test
    public void testGetDepth() {
        Scope parentScope = new Scope(null, new Node(Token.BLOCK, null, null));
        Node rootNode = new Node(Token.FUNCTION, null, null);
        Scope scope = new Scope(parentScope, rootNode);
        assertEquals(1, scope.getDepth());
        assertEquals(0, parentScope.getDepth());
    }

    @Test
    public void testIsBottom() {
        Scope globalScope = new Scope(null, new Node(Token.BLOCK, null, null));
        assertTrue(globalScope.isBottom());

        Node rootNode = new Node(Token.BLOCK, null, null);
        Scope scope = new Scope(null, rootNode);
        assertFalse(scope.isBottom());

        Scope parentScope = new Scope(null, new Node(Token.BLOCK, null, null));
        Scope childScope = new Scope(parentScope, new Node(Token.FUNCTION, null, null));
        assertFalse(childScope.isBottom());
        assertTrue(parentScope.isBottom());
    }

    @Test
    public void testGetParent() {
        Scope parentScope = new Scope(null, new Node(Token.BLOCK, null, null));
        Node rootNode = new Node(Token.FUNCTION, null, null);
        Scope childScope = new Scope(parentScope, rootNode);
        assertEquals(parentScope, childScope.getParent());
        assertNull(parentScope.getParent());
    }

    @Test
    public void testGetGlobalScope() {
        Node rootNode = new Node(Token.BLOCK, null, null);
        Scope scope = new Scope(rootNode, new AbstractCompiler());
        Scope globalScope = scope.getGlobalScope();
        assertEquals(scope, globalScope.getParent());
        assertNull(globalScope.getParent());
    }

    @Test
    public void testGetTypeOfThis() {
        ObjectType objectType = new ObjectType("Object");
        Node rootNode = new Node(Token.BLOCK, null, null);
        Scope scope = new Scope(null, rootNode, objectType);
        assertEquals(objectType, scope.getTypeOfThis());
    }

    @Test
    public void testDeclare() {
        Scope parentScope = new Scope(null, new Node(Token.BLOCK, null, null));
        Node rootNode = new Node(Token.FUNCTION, null, null);
        Scope scope = new Scope(parentScope, rootNode);

        String name = "name";
        Node nameNode = new Node(Token.IDENT, null, null);
        JSType type = new FunctionType(null, null);
        CompilerInput input = new CompilerInput("test.js");

        Var var = scope.declare(name, nameNode, type, input);

        assertEquals(name, var.getName());
        assertEquals(nameNode, var.getNameNode());
        assertEquals(type, var.getType());
        assertEquals(scope, var.getScope());
        assertEquals(0, var.getIndex());
        assertEquals(input, var.getInput());
        assertTrue(var.isDefine());

        assertTrue(scope.getVarCount() == 1);
        Var declaredVar = scope.getVar(name);
        assertEquals(var, declaredVar);
    }

    @Test
    public void testDeclareWithInferredType() {
        Scope parentScope = new Scope(null, new Node(Token.BLOCK, null, null));
        Node rootNode = new Node(Token.FUNCTION, null, null);
        Scope scope = new Scope(parentScope, rootNode);

        String name = "name";
        Node nameNode = new Node(Token.IDENT, null, null);
        CompilerInput input = new CompilerInput("test.js");

        Var var = scope.declare(name, nameNode, null, input, true);

        assertEquals(name, var.getName());
        assertEquals(nameNode, var.getNameNode());
        assertNull(var.getType());
        assertEquals(scope, var.getScope());
        assertEquals(0, var.getIndex());
        assertEquals(input, var.getInput());
        assertTrue(var.isDefine());
    }

    @Test
    public void testUndeclare() {
        Scope parentScope = new Scope(null, new Node(Token.BLOCK, null, null));
        Node rootNode = new Node(Token.FUNCTION, null, null);
        Scope scope = new Scope(parentScope, rootNode);

        String name = "name";
        Node nameNode = new Node(Token.IDENT, null, null);
        JSType type = new FunctionType(null, null);
        CompilerInput input = new CompilerInput("test.js");
        Var var = scope.declare(name, nameNode, type, input);

        scope.undeclare(var);

        assertNull(scope.getVar(name));
        assertEquals(0, scope.getVarCount());
    }

    @Test
    public void testGetSlot() {
        Scope parentScope = new Scope(null, new Node(Token.BLOCK, null, null));
        Node rootNode = new Node(Token.FUNCTION, null, null);
        Scope scope = new Scope(parentScope, rootNode);

        String name = "name";
        Node nameNode = new Node(Token.IDENT, null, null);
        JSType type = new FunctionType(null, null);
        CompilerInput input = new CompilerInput("test.js");
        Var var = scope.declare(name, nameNode, type, input);

        StaticSlot<JSType> slot = scope.getSlot(name);
        assertEquals(var, slot);
    }

    @Test
    public void testGetOwnSlot() {
        Scope parentScope = new Scope(null, new Node(Token.BLOCK, null, null));
        Node rootNode = new Node(Token.FUNCTION, null, null);
        Scope scope = new Scope(parentScope, rootNode);

        String name = "name";
        Node nameNode = new Node(Token.IDENT, null, null);
        JSType type = new FunctionType(null, null);
        CompilerInput input = new CompilerInput("test.js");
        Var var = scope.declare(name, nameNode, type, input);

        StaticSlot<JSType> slot = scope.getOwnSlot(name);
        assertEquals(var, slot);
    }

    @Test
    public void testGetVar() {
        Scope parentScope = new Scope(null, new Node(Token.BLOCK, null, null));
        Node rootNode = new Node(Token.FUNCTION, null, null);
        Scope scope = new Scope(parentScope, rootNode);

        String name = "name";
        Node nameNode = new Node(Token.IDENT, null, null);
        JSType type = new FunctionType(null, null);
        CompilerInput input = new CompilerInput("test.js");
        Var var = scope.declare(name, nameNode, type, input);

        Var declaredVar = scope.getVar(name);
        assertEquals(var, declaredVar);

        assertNull(parentScope.getVar(name));
    }

    @Test
    public void testIsDeclared() {
        Scope parentScope = new Scope(null, new Node(Token.BLOCK, null, null));
        Node rootNode = new Node(Token.FUNCTION, null, null);
        Scope scope = new Scope(parentScope, rootNode);

        String name = "name";
        Node nameNode = new Node(Token.IDENT, null, null);
        JSType type = new FunctionType(null, null);
        CompilerInput input = new CompilerInput("test.js");
        Var var = scope.declare(name, nameNode, type, input);

        assertTrue(scope.isDeclared(name, true));
        assertTrue(scope.isDeclared(name, false));
        assertFalse(parentScope.isDeclared(name, true));
        assertFalse(parentScope.isDeclared(name, false));
    }

    @Test
    public void testGetVars() {
        Scope parentScope = new Scope(null, new Node(Token.BLOCK, null, null));
        Node rootNode = new Node(Token.FUNCTION, null, null);
        Scope scope = new Scope(parentScope, rootNode);

        String name1 = "name1";
        Node nameNode1 = new Node(Token.IDENT, null, null);
        JSType type1 = new FunctionType(null, null);
        CompilerInput input1 = new CompilerInput("test1.js");
        Var var1 = scope.declare(name1, nameNode1, type1, input1);

        String name2 = "name2";
        Node nameNode2 = new Node(Token.IDENT, null, null);
        JSType type2 = new FunctionType(null, null);
        CompilerInput input2 = new CompilerInput("test2.js");
        Var var2 = scope.declare(name2, nameNode2, type2, input2);

        Iterator<Var> iterator = scope.getVars().iterator();
        Var firstVar = iterator.next();
        Var secondVar = iterator.next();

        assertEquals(var1, firstVar);
        assertEquals(var2, secondVar);
    }
}
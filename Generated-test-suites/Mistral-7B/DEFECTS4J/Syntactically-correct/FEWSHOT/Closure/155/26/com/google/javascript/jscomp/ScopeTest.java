package com.google.javascript.jscomp;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import com.google.common.collect.ImmutableList;
import com.google.javascript.jscomp.Scope.Var;
import org.junit.Test;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

public class ScopeTest {

    @Test
    public void testVarCreationAndGetters() {
        Scope scope = new Scope(null, null);
        Var var = scope.declare("foo", null, JSType.STRING, null);
        assertEquals("foo", var.getName());
        assertEquals(null, var.getParentNode());
        assertNull(var.getType());
        assertFalse(var.isBleedingFunction());
        assertSame(scope, var.getScope());
        assertEquals(0, var.getIndex());
        assertNull(var.getInput());
        assertFalse(var.isDefine());
        assertEquals(false, var.isConst());
        assertFalse(var.isExtern());
        assertNull(var.getInitialValue());
        assertFalse(var.isTypeInferred());
        assertEquals("<non-file>", var.getInputName());
        assertFalse(var.isNoShadow());

        assertEquals(1, scope.getVarCount());
        assertFalse(scope.isDeclared("foo", false));
        assertFalse(scope.isDeclared("bar", false));
        Var var2 = scope.getVar("foo");
        assertSame(var, var2);
        Iterator<Var> iterator = scope.getVars();
        assertTrue(iterator.hasNext());
        Var varIter = iterator.next();
        assertEquals(var, varIter);
        assertFalse(iterator.hasNext());
    }

    @Test
    public void testVarCreationWithInfo() {
        Scope scope = new Scope(null, null);
        Var var = scope.declare("foo", null, JSType.STRING, null, true, null);
        assertEquals("foo", var.getName());
        assertNull(var.getParentNode());
        assertNull(var.getType());
        assertFalse(var.isBleedingFunction());
        assertSame(scope, var.getScope());
        assertEquals(0, var.getIndex());
        assertNull(var.getInput());
        assertTrue(var.isDefine());
        assertEquals(false, var.isConst());
        assertFalse(var.isExtern());
        assertNull(var.getInitialValue());
        assertFalse(var.isTypeInferred());
        assertEquals("<non-file>", var.getInputName());
        assertFalse(var.isNoShadow());

        assertEquals(1, scope.getVarCount());
        assertFalse(scope.isDeclared("foo", false));
        assertFalse(scope.isDeclared("bar", false));
        Var var2 = scope.getVar("foo");
        assertSame(var, var2);
        Iterator<Var> iterator = scope.getVars();
        assertTrue(iterator.hasNext());
        Var varIter = iterator.next();
        assertEquals(var, varIter);
        assertFalse(iterator.hasNext());

        JSDocInfo info = new JSDocInfo();
        info.setName("name");
        info.setKind(JSDocInfo.Kind.VAR);
        info.setDeclared(true);
        info.setDefine(true);
        info.setPrivate(true);
        info.setStatic(false);
        info.setNoShadow(true);
        info.setType(JSType.STRING);
        info.setValue(JSType.NUMBER);
        Var varWithInfo = scope.declare("foo2", null, null, null, true, info);
        assertEquals("foo2", varWithInfo.getName());
        assertNull(varWithInfo.getParentNode());
        assertNull(varWithInfo.getType());
        assertFalse(varWithInfo.isBleedingFunction());
        assertSame(scope, varWithInfo.getScope());
        assertEquals(0, varWithInfo.getIndex());
        assertNull(varWithInfo.getInput());
        assertTrue(varWithInfo.isDefine());
        assertEquals(false, varWithInfo.isConst());
        assertFalse(varWithInfo.isExtern());
        assertNull(varWithInfo.getInitialValue());
        assertFalse(varWithInfo.isTypeInferred());
        assertEquals("<non-file>", varWithInfo.getInputName());
        assertTrue(varWithInfo.isNoShadow());

        assertEquals(2, scope.getVarCount());
        assertFalse(scope.isDeclared("foo", false));
        assertFalse(scope.isDeclared("foo2", false));
        assertFalse(scope.isDeclared("bar", false));
        Var var1 = scope.getVar("foo");
        assertSame(var, var1);
        Var var2 = scope.getVar("foo2");
        assertSame(varWithInfo, var2);
    }

    @Test
    public void testVarCreationWithoutInfo() {
        Scope scope = new Scope(null, null);
        Var var = scope.declare("foo", null, JSType.STRING, null, false, null);
        assertEquals("foo", var.getName());
        assertNull(var.getParentNode());
        assertNull(var.getType());
        assertFalse(var.isBleedingFunction());
        assertSame(scope, var.getScope());
        assertEquals(0, var.getIndex());
        assertNull(var.getInput());
        assertFalse(var.isDefine());
        assertFalse(var.isConst());
        assertFalse(var.isExtern());
        assertNull(var.getInitialValue());
        assertTrue(var.isTypeInferred());
        assertEquals("<non-file>", var.getInputName());
        assertFalse(var.isNoShadow());

        assertEquals(1, scope.getVarCount());
        assertFalse(scope.isDeclared("foo", false));
        assertFalse(scope.isDeclared("bar", false));
        Var var2 = scope.getVar("foo");
        assertSame(var, var2);
        Iterator<Var> iterator = scope.getVars();
        assertTrue(iterator.hasNext());
        Var varIter = iterator.next();
        assertEquals(var, varIter);
        assertFalse(iterator.hasNext());

        Scope parentScope = new Scope(scope, null);
        Var parentVar = parentScope.declare("foo", null, JSType.NUMBER, null, false, null);
        assertEquals("foo", parentVar.getName());
        assertEquals(scope, parentVar.getScope());
        assertEquals(0, parentVar.getIndex());
        assertEquals(scope, parentVar.getScope().getParent());
        assertTrue(parentVar.isBleedingFunction());

        assertEquals(1, scope.getVarCount());
        assertEquals(1, parentScope.getVarCount());
        Var varParent = parentScope.getVar("foo");
        assertSame(parentVar, varParent);
        assertFalse(scope.isDeclared("foo", false));
        assertTrue(parentScope.isDeclared("foo", false));
    }

    @Test
    public void testGetVarWithNullName() {
        Scope scope = new Scope(null, null);
        assertNull(scope.getVar(null));
    }

    @Test
    public void testUndeclareVar() {
        Scope scope = new Scope(null, null);
        Var var = scope.declare("foo", null, JSType.STRING, null);
        assertEquals("foo", var.getName());
        assertEquals(1, scope.getVarCount());
        scope.undeclare(var);
        assertNull(scope.getVar("foo"));
        assertEquals(0, scope.getVarCount());
    }

    @Test
    public void testGetParent() {
        Scope parent = new Scope(null, null);
        Scope child = new Scope(parent, null);
        assertEquals(parent, child.getParent());
        assertNull(parent.getParent());
    }

    @Test
    public void testFunctionScope() {
        Node rootNode = new Node(Token.FUNCTION, null);
        Scope functionScope = new Scope(null, rootNode);
        assertEquals(false, functionScope.isBottom());
        assertEquals(0, functionScope.getDepth());
        Map<String, Var> functionVars = functionScope.vars;
        assertEquals(ImmutableList.of(), functionVars.values());

        Var functionThis = new Var(true, "this", null, JSType.OBJECT, functionScope, 0, null, false, null);
        functionScope.vars.put("this", functionThis);
        assertEquals(1, functionScope.getVarCount());
        assertEquals(functionThis, functionScope.getVar("this"));
    }

    @Test
    public void testGlobalScope() {
        Node rootNode = new Node(Token.BLOCK, null);
        Scope globalScope = new Scope(rootNode, null);
        assertEquals(true, globalScope.isBottom());
        assertEquals(0, globalScope.getDepth());
        Map<String, Var> globalVars = globalScope.vars;
        assertEquals(ImmutableList.of(), globalVars.values());
    }

    @Test
    public void testEmptyScope() {
        Node rootNode = new Node(Token.FUNCTION, null);
        Scope emptyScope = new Scope(rootNode, JSType.OBJECT);
        assertEquals(true, emptyScope.isBottom());
        assertEquals(0, emptyScope.getDepth());
        Map<String, Var> emptyVars = emptyScope.vars;
        assertEquals(ImmutableList.of(), emptyVars.values());
    }

    @Test
    public void testScopeHierarchy() {
        Node rootNode = new Node(Token.FUNCTION, null);
        Scope outerScope = new Scope(null, rootNode);
        Scope innerScope = new Scope(outerScope, null);
        assertEquals(outerScope, innerScope.getParent());
        assertEquals(outerScope, innerScope.getParentScope());
        Scope innerInnerScope = new Scope(innerScope, null);
        assertEquals(innerScope, innerInnerScope.getParent());
        assertEquals(outerScope, innerInnerScope.getParentScope());
    }

    @Test
    public void testGetRootNode() {
        Node rootNode = new Node(Token.FUNCTION, null);
        Scope scope = new Scope(null, rootNode);
        assertEquals(rootNode, scope.getRootNode());
    }

    @Test
    public void testGetTypeOfThis() {
        Node rootNode = new Node(Token.FUNCTION, null);
        ObjectType thisType = new ObjectType();
        Scope scope = new Scope(null, rootNode);
        scope.thisType = thisType;
        assertEquals(thisType, scope.getTypeOfThis());
    }

    @Test
    public void testGetParentScope() {
        Scope parentScope = new Scope(null, null);
        Scope childScope = new Scope(parentScope, null);
        assertEquals(parentScope, childScope.getParentScope());
    }
}
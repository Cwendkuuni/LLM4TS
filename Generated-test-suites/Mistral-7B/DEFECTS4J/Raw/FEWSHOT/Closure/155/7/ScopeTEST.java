package com.google.javascript.jscomp;

import static org.junit.Assert.*;

import com.google.common.base.Preconditions;
import com.google.javascript.rhino.ErrorReporter;
import com.google.javascript.rhino.JSDocInfo;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.jstype.JSType;
import com.google.javascript.rhino.jstype.ObjectType;
import com.google.javascript.rhino.jstype.StaticScope;
import com.google.javascript.rhino.jstype.StaticSlot;
import org.junit.Test;

import java.util.Iterator;
import java.util.Map;

public class ScopeTest {

    @Test
    public void testCreateScope() {
        Scope scope = new Scope(null, null);
        assertNull(scope.getParent());
        assertEquals(0, scope.getDepth());
        assertNotNull(scope.getTypeOfThis());
        assertTrue(scope.isBottom());

        Node rootNode = new Node(Token.SCRIPT, 0, 0);
        Scope scopeWithRootNode = new Scope(null, rootNode);
        assertEquals(rootNode, scopeWithRootNode.getRootNode());
        assertSame(scope, scopeWithRootNode.getParent());
        assertEquals(0, scopeWithRootNode.getDepth());
        assertSame(scope.getTypeOfThis(), scopeWithRootNode.getTypeOfThis());
        assertFalse(scopeWithRootNode.isBottom());

        Scope parentScope = new Scope(null, null);
        Scope childScope = new Scope(parentScope, null);
        assertEquals(parentScope, childScope.getParent());
        assertEquals(parentScope.getDepth() + 1, childScope.getDepth());
        assertSame(parentScope.getTypeOfThis(), childScope.getTypeOfThis());
        assertFalse(childScope.isBottom());
    }

    @Test
    public void testVariableDeclaration() {
        Scope scope = new Scope(null, null);
        Node rootNode = new Node(Token.SCRIPT, 0, 0);
        Scope scopeWithRootNode = new Scope(scope, rootNode);

        String name = "foo";
        JSType type = JSType.STRING;
        CompilerInput input = new CompilerInput("test.js", "test.js");

        Var var = scopeWithRootNode.declare(name, null, type, input, true);
        Var varDeclaredAgain = scopeWithRootNode.declare(name, null, type, input, true);
        assertEquals(var, varDeclaredAgain);

        Var varWithDocInfo = scopeWithRootNode.declare("bar", null, JSType.NUMBER, input, true);
        JSDocInfo docInfo = NodeUtil.getInfoForNameNode(new Node(Token.IDENT, 0, 0, "bar"));
        Var varWithDocInfoDeclared = scopeWithRootNode.declare("bar", null, JSType.NUMBER, input, true, docInfo);
        assertEquals(varWithDocInfo, varWithDocInfoDeclared);
    }

    @Test
    public void testGetSlot() {
        Scope scope = new Scope(null, null);
        Node rootNode = new Node(Token.SCRIPT, 0, 0);
        Scope scopeWithRootNode = new Scope(scope, rootNode);

        String name = "foo";
        JSType type = JSType.STRING;
        CompilerInput input = new CompilerInput("test.js", "test.js");
        Var var = scopeWithRootNode.declare(name, null, type, input, true);

        StaticSlot<JSType> slot = scopeWithRootNode.getSlot(name);
        assertEquals(var, slot);
    }

    @Test
    public void testGetVar() {
        Scope scope = new Scope(null, null);
        Node rootNode = new Node(Token.SCRIPT, 0, 0);
        Scope scopeWithRootNode = new Scope(scope, rootNode);

        String name = "foo";
        JSType type = JSType.STRING;
        CompilerInput input = new CompilerInput("test.js", "test.js");
        Var var = scopeWithRootNode.declare(name, null, type, input, true);

        Var varDeclaredAgain = scopeWithRootNode.declare(name, null, type, input, true);
        assertEquals(var, varDeclaredAgain);

        Var varInParentScope = scope.declare(name, null, type, input, true);
        assertNull(scopeWithRootNode.getVar(name));
        assertEquals(varInParentScope, scope.getVar(name));
    }

    @Test
    public void testGetVars() {
        Scope scope = new Scope(null, null);
        Node rootNode = new Node(Token.SCRIPT, 0, 0);
        Scope scopeWithRootNode = new Scope(scope, rootNode);

        String name1 = "foo";
        JSType type1 = JSType.STRING;
        CompilerInput input1 = new CompilerInput("test1.js", "test1.js");
        Var var1 = scopeWithRootNode.declare(name1, null, type1, input1, true);

        String name2 = "bar";
        JSType type2 = JSType.NUMBER;
        CompilerInput input2 = new CompilerInput("test2.js", "test2.js");
        Var var2 = scopeWithRootNode.declare(name2, null, type2, input2, true);

        Iterator<Var> vars = scopeWithRootNode.getVars();
        List<Var> variables = new ArrayList<>();
        while (vars.hasNext()) {
            variables.add(vars.next());
        }
        assertEquals(2, variables.size());
        assertTrue(variables.contains(var1));
        assertTrue(variables.contains(var2));
    }

    @Test
    public void testIsDeclared() {
        Scope scope = new Scope(null, null);
        Node rootNode = new Node(Token.SCRIPT, 0, 0);
        Scope scopeWithRootNode = new Scope(scope, rootNode);

        String name1 = "foo";
        JSType type1 = JSType.STRING;
        CompilerInput input1 = new CompilerInput("test1.js", "test1.js");
        Var var1 = scopeWithRootNode.declare(name1, null, type1, input1, true);

        String name2 = "bar";
        JSType type2 = JSType.NUMBER;
        CompilerInput input2 = new CompilerInput("test2.js", "test2.js");
        Var var2 = scopeWithRootNode.declare(name2, null, type2, input2, true);

        assertTrue(scopeWithRootNode.isDeclared(name1, false));
        assertTrue(scopeWithRootNode.isDeclared(name1, true));
        assertTrue(scopeWithRootNode.isDeclared(name2, false));
        assertTrue(scopeWithRootNode.isDeclared(name2, true));
        assertTrue(scope.isDeclared(name1, false));
        assertFalse(scope.isDeclared(name1, true));
        assertFalse(scope.isDeclared(name2, false));
        assertFalse(scope.isDeclared(name2, true));
    }
}
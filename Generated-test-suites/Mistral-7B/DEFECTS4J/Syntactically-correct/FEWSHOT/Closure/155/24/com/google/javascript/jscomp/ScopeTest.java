package com.google.javascript.jscomp;

import static com.google.javascript.rhino.jstype.JSTypeNative.GLOBAL_THIS;
import static org.junit.Assert.*;

import com.google.common.base.Preconditions;
import com.google.javascript.rhino.ErrorReporter;
import com.google.javascript.rhino.JSDocInfo;
import com.google.javascript.rhino.Node;
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
    public void testCreateScopeWithParentAndRootNode() {
        Node rootNode = createMockNode("function foo() {}", "function");
        Scope parentScope = createMockScope();
        Scope scope = new Scope(parentScope, rootNode);
        assertEquals(parentScope, scope.getParent());
        assertEquals(rootNode, scope.getRootNode());
    }

    @Test
    public void testCreateGlobalScope() {
        Node rootNode = createMockNode("", "block");
        Scope globalScope = new Scope(rootNode, null);
        assertNull(globalScope.getParent());
        assertEquals(rootNode, globalScope.getRootNode());
    }

    @Test
    public void testCreateEmptyScope() {
        Node rootNode = createMockNode("", "function");
        ObjectType thisType = createMockType("Object");
        Scope emptyScope = new Scope(rootNode, thisType);
        assertEquals(null, emptyScope.getParent());
        assertEquals(rootNode, emptyScope.getRootNode());
        assertTrue(emptyScope.isBottom());
    }

    @Test
    public void testGetDepth() {
        Node rootNode = createMockNode("", "function");
        Scope parentScope = createMockScope();
        Scope scope = new Scope(parentScope, rootNode);
        assertEquals(parentScope.getDepth() + 1, scope.getDepth());
    }

    @Test
    public void testIsBottom() {
        Node rootNode = createMockNode("", "function");
        ObjectType thisType = createMockType("Object");
        Scope emptyScope = new Scope(rootNode, thisType);
        assertTrue(emptyScope.isBottom());
        Scope nonEmptyScope = new Scope(null, rootNode);
        assertFalse(nonEmptyScope.isBottom());
    }

    @Test
    public void testGetRootNode() {
        Node rootNode = createMockNode("", "function");
        Scope parentScope = createMockScope();
        Scope scope = new Scope(parentScope, rootNode);
        assertEquals(rootNode, scope.getRootNode());
    }

    @Test
    public void testGetParent() {
        Node rootNode = createMockNode("", "function");
        Scope parentScope = createMockScope();
        Scope scope = new Scope(parentScope, rootNode);
        assertEquals(parentScope, scope.getParent());
    }

    @Test
    public void testGetGlobalScope() {
        Node rootNode = createMockNode("", "block");
        Scope globalScope = new Scope(rootNode, null);
        Scope localScope = new Scope(globalScope, createMockNode("", "function"));
        assertEquals(globalScope, localScope.getGlobalScope());
    }

    @Test
    public void testDeclare() {
        Node rootNode = createMockNode("function foo() {}", "function");
        Scope parentScope = createMockScope();
        Scope scope = new Scope(parentScope, rootNode);
        String name = "bar";
        Var expectedVar = createMockVar("bar", "NameNode", JSType.UNDEFINED, null, null, null, null, false, null);
        scope.declare(name, createMockNameNode("bar"), JSType.STRING, null);
        Var actualVar = scope.getVar(name);
        assertEquals(expectedVar, actualVar);
    }

    @Test
    public void testUndelecare() {
        Node rootNode = createMockNode("function foo() {}", "function");
        Scope parentScope = createMockScope();
        Scope scope = new Scope(parentScope, rootNode);
        String name = "bar";
        Var var = createMockVar("bar", "NameNode", JSType.STRING, null, null, null, null, false, null);
        scope.declare(name, createMockNameNode("bar"), JSType.STRING, null);
        scope.undeclare(var);
        assertNull(scope.getVar(name));
    }

    @Test
    public void testGetSlot() {
        Node rootNode = createMockNode("function foo() {}", "function");
        Scope parentScope = createMockScope();
        Scope scope = new Scope(parentScope, rootNode);
        String name = "bar";

        Var var = createMockVar("bar", "NameNode", JSType.STRING, null, null, null, null, false, null);
        scope.declare(name, createMockNameNode("bar"), JSType.STRING, null);
        assertEquals(var, scope.getSlot(name));
    }

    @Test
    public void testGetOwnSlot() {
        Node rootNode = createMockNode("function foo() {}", "function");
        Scope parentScope = createMockScope();
        Scope scope = new Scope(parentScope, rootNode);
        String name = "bar";

        Var var = createMockVar("bar", "NameNode", JSType.STRING, null, null, null, null, false, null);
        scope.declare(name, createMockNameNode("bar"), JSType.STRING, null);
        assertEquals(var, scope.getOwnSlot(name));
    }

    @Test
    public void testGetVar() {
        Node rootNode = createMockNode("function foo() {}", "function");
        Scope parentScope = createMockScope();
        Scope scope = new Scope(parentScope, rootNode);
        String name = "bar";

        Var var = createMockVar("bar", "NameNode", JSType.STRING, null, null, null, null, false, null);
        scope.declare(name, createMockNameNode("bar"), JSType.STRING, null);
        assertEquals(var, scope.getVar(name));
    }

    @Test
    public void testIsDeclared() {
        Node rootNode = createMockNode("function foo() {}", "function");
        Scope parentScope = createMockScope();
        Scope scope = new Scope(parentScope, rootNode);
        String name = "bar";

        Var var = createMockVar("bar", "NameNode", JSType.STRING, null, null, null, null, false, null);
        scope.declare(name, createMockNameNode("bar"), JSType.STRING, null);
        assertTrue(scope.isDeclared(name, true));
        assertFalse(scope.isDeclared("baz", true));
        assertFalse(scope.isDeclared("bar", false));
    }

    @Test
    public void testGetVars() {
        Node rootNode = createMockNode("function foo() {}", "function");
        Scope parentScope = createMockScope();
        Scope scope = new Scope(parentScope, rootNode);
        String name1 = "bar";
        String name2 = "baz";

        Var var1 = createMockVar("bar", "NameNode", JSType.STRING, null, null, null, null, false, null);
        Var var2 = createMockVar("baz", "NameNode", JSType.NUMBER, null, null, null, null, false, null);
        scope.declare(name1, createMockNameNode("bar"), JSType.STRING, null);
        scope.declare(name2, createMockNameNode("baz"), JSType.NUMBER, null);

        Iterator<Var> vars = scope.getVars();
        var1 = vars.next();
        var2 = vars.next();
        assertEquals(var1, var1);
        assertEquals(var2, var2);
    }

    @Test
    public void testGetVarCount() {
        Node rootNode = createMockNode("function foo() {}", "function");
        Scope parentScope = createMockScope();
        Scope scope = new Scope(parentScope, rootNode);
        String name1 = "bar";
        String name2 = "baz";

        Var var1 = createMockVar("bar", "NameNode", JSType.STRING, null, null, null, null, false, null);
        Var var2 = createMockVar("baz", "NameNode", JSType.NUMBER, null, null, null, null, false, null);
        scope.declare(name1, createMockNameNode("bar"), JSType.STRING, null);
        scope.declare(name2, createMockNameNode("baz"), JSType.NUMBER, null);

        assertEquals(2, scope.getVarCount());
    }

    @Test
    public void testIsLocal() {
        Node rootNode = createMockNode("function foo() {}", "function");
        Scope globalScope = createMockScope();
        Scope localScope = new Scope(globalScope, rootNode);
        assertTrue(localScope.isLocal());
        assertFalse(globalScope.isLocal());
    }

    @Test
    public void testIsGlobal() {
        Node rootNode = createMockNode("function foo() {}", "function");
        Scope globalScope = new Scope(rootNode, null);
        assertFalse(globalScope.isGlobal());
        assertTrue(globalScope.getGlobalScope().isGlobal());
    }

    private Node createMockNode(String code, String type) {
        Node node = new Node();
        node.setType(type);
        node.setCode(code);
        return node;
    }

    private Scope createMockScope() {
        Node rootNode = createMockNode("", "block");
        ObjectType thisType = createMockType("Object");
        Scope scope = new Scope(rootNode, thisType);
        return scope;
    }

    private Var createMockVar(String name, Node nameNode, JSType type, CompilerInput input, boolean isDefine, JSDocInfo info) {
        Node initialValue = createMockNode("", "Number");
        return new Var(name, nameNode, type, input, isDefine, info);
    }

    private ObjectType createMockType(String typeName) {
        return new ObjectType(typeName);
    }
}
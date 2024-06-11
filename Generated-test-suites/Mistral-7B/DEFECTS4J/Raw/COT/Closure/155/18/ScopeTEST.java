import static org.junit.Assert.*;
import static com.google.javascript.rhino.Token.*;

import com.google.common.collect.LinkedHashMultimap;
import com.google.common.collect.Multimap;
import com.google.javascript.jscomp.CompilerInput;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.jscomp.Scope.Var;
import com.google.javascript.rhino.ErrorReporter;
import com.google.javascript.rhino.JSDocInfo;
import com.google.javascript.rhino.JSType;
import com.google.javascript.rhino.JSTypeNative;
import com.google.javascript.rhino.NodeTraversal;
import com.google.javascript.rhino.TokenType;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ScopeTest {

    private ErrorReporter errorReporter;
    private CompilerInput input;
    private Node rootNode;
    private Scope scope;

    @Before
    public void setUp() {
        errorReporter = Mockito.mock(ErrorReporter.class);
        input = new CompilerInput("test.js", "Test input file");
        rootNode = Mockito.mock(Node.class);
        scope = new Scope(null, rootNode);
    }

    @Test
    public void testCreateScopeWithRootNodeAndCompiler() {
        Scope scope = new Scope(rootNode, null);
        assertEquals(0, scope.getDepth());
        assertEquals(null, scope.getParent());
        assertEquals(JSTypeNative.getNativeObjectType(JSTypeNative.GLOBAL_THIS), scope.getTypeOfThis());
        assertTrue(scope.isBottom());
    }

    @Test
    public void testCreateScopeWithParentAndRootNode() {
        Scope parentScope = Mockito.mock(Scope.class);
        Mockito.when(parentScope.getTypeOfThis()).thenReturn(JSTypeNative.getNativeObjectType(JSTypeNative.GLOBAL_THIS));

        Scope scope = new Scope(parentScope, rootNode);
        assertEquals(parentScope, scope.getParent());
        assertEquals(parentScope.getDepth() + 1, scope.getDepth());
        assertEquals(parentScope.getTypeOfThis(), scope.getTypeOfThis());
        assertFalse(scope.isBottom());
    }

    @Test
    public void testCreateEmptyScope() {
        Scope scope = new Scope(rootNode, JSTypeNative.getNativeObjectType(JSTypeNative.GLOBAL_THIS));
        assertEquals(0, scope.getDepth());
        assertEquals(null, scope.getParent());
        assertEquals(JSTypeNative.getNativeObjectType(JSTypeNative.GLOBAL_THIS), scope.getTypeOfThis());
        assertTrue(scope.isBottom());
    }

    @Test
    public void testDeclareVariable() {
        String name = "testVariable";
        Node nameNode = Mockito.mock(Node.class);
        JSType type = JSType.STRING;
        Var var = scope.declare(name, nameNode, type, input);

        assertNotNull(var);
        assertEquals(name, var.getName());
        assertEquals(nameNode, var.getNameNode());
        assertEquals(type, var.getType());
        assertEquals(scope, var.getScope());
        assertEquals(0, var.getIndex());
        assertFalse(var.isTypeInferred());
        assertEquals(input, var.getInput());
        assertFalse(var.isDefine());

        assertEquals(1, scope.getVarCount());
        Var declaredVar = scope.getVar(name);
        assertEquals(var, declaredVar);
    }

    @Test
    public void testDeclareMultipleVariables() {
        String name1 = "testVariable1";
        String name2 = "testVariable2";
        Node nameNode1 = Mockito.mock(Node.class);
        Node nameNode2 = Mockito.mock(Node.class);
        JSType type1 = JSType.NUMBER;
        JSType type2 = JSType.BOOLEAN;

        scope.declare(name1, nameNode1, type1, input);
        scope.declare(name2, nameNode2, type2, input);

        assertEquals(2, scope.getVarCount());

        Var variable1 = scope.getVar(name1);
        Var variable2 = scope.getVar(name2);

        assertNotNull(variable1);
        assertNotNull(variable2);
        assertEquals(name1, variable1.getName());
        assertEquals(name2, variable2.getName());
        assertNotEquals(variable1, variable2);
    }

    @Test
    public void testGetVar() {
        String name = "testVariable";
        Node nameNode = Mockito.mock(Node.class);
        JSType type = JSType.NUMBER;
        Var var = scope.declare(name, nameNode, type, input);

        Var declaredVar = scope.getVar(name);
        assertEquals(var, declaredVar);
    }

    @Test
    public void testGetVarNull() {
        assertNull(scope.getVar("nonExistentVariable"));
    }

    @Test
    public void testGetParentScope() {
        Scope parentScope = Mockito.mock(Scope.class);
        Mockito.when(parentScope.getTypeOfThis()).thenReturn(JSTypeNative.getNativeObjectType(JSTypeNative.GLOBAL_THIS));

        scope.parent = parentScope;

        assertEquals(parentScope, scope.getParentScope());
    }

    @Test
    public void testGetParentScopeNull() {
        assertNull(scope.getParentScope());
    }

    @Test
    public void testGetSlot() {
        String name = "testVariable";
        Var var = scope.declare(name, null, JSType.NUMBER, input);

        StaticSlot<JSType> slot = scope.getSlot(name);

        assertEquals(var, slot);
    }

    @Test
    public void testGetOwnSlotNull() {
        assertNull(scope.getOwnSlot("nonExistentVariable"));
    }

    @Test
    public void testIsDeclared() {
        String name1 = "testVariable1";
        String name2 = "testVariable2";

        scope.declare(name1, null, JSType.NUMBER, input);
        scope.declare(name2, null, JSType.BOOLEAN, input);

        assertTrue(scope.isDeclared(name1, false));
        assertTrue(scope.isDeclared(name2, false));
        assertTrue(scope.isDeclared(name1, true));
        assertTrue(scope.isDeclared(name2, true));
        assertFalse(scope.isDeclared("nonExistentVariable", false));
        assertFalse(scope.isDeclared("nonExistentVariable", true));
    }

    @Test
    public void testGetVars() {
        String name1 = "testVariable1";
        String name2 = "testVariable2";

        scope.declare(name1, null, JSType.NUMBER, input);
        scope.declare(name2, null, JSType.BOOLEAN, input);

        List<Var> vars = new ArrayList<>();
        Iterator<Var> it = scope.getVars();

        while (it.hasNext()) {
            vars.add(it.next());
        }

        assertEquals(2, vars.size());
        assertTrue(vars.contains(scope.getVar(name1)));
        assertTrue(vars.contains(scope.getVar(name2)));
    }

    @Test
    public void testGetVarCount() {
        String name1 = "testVariable1";
        String name2 = "testVariable2";

        scope.declare(name1, null, JSType.NUMBER, input);
        scope.declare(name2, null, JSType.BOOLEAN, input);

        assertEquals(2, scope.getVarCount());
    }

    @Test
    public void testIsGlobal() {
        assertTrue(scope.isGlobal());
    }

    @Test
    public void testIsLocal() {
        Scope parentScope = Mockito.mock(Scope.class);
        Mockito.when(parentScope.isGlobal()).thenReturn(false);
        Mockito.when(parentScope.getTypeOfThis()).thenReturn(JSTypeNative.getNativeObjectType(JSTypeNative.GLOBAL_THIS));

        scope.parent = parentScope;

        assertFalse(scope.isLocal());
    }

    @Test
    public void testUndeleclareVariable() {
        String name = "testVariable";
        Node nameNode = Mockito.mock(Node.class);
        Var var = scope.declare(name, nameNode, JSType.NUMBER, input);

        scope.undeclare(var);

        assertNull(scope.getVar(name));
        assertEquals(0, scope.getVarCount());
    }
}
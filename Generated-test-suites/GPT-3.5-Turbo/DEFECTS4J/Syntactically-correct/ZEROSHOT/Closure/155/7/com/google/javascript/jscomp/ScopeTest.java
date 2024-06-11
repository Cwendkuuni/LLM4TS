import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class ScopeTest {

    private Scope scope;

    @Before
    public void setUp() {
        scope = new Scope(null, null);
    }

    @Test
    public void testDeclare() {
        String name = "varName";
        Node nameNode = new Node();
        JSType type = new JSType();
        CompilerInput input = new CompilerInput();
        boolean inferred = true;

        Var var = scope.declare(name, nameNode, type, input, inferred);

        assertEquals("varName", var.name);
        assertEquals(nameNode, var.nameNode);
        assertEquals(type, var.type);
        assertEquals(input, var.input);
        assertEquals(inferred, var.typeInferred);
    }

    @Test
    public void testUndeclare() {
        String name = "varName";
        Node nameNode = new Node();
        JSType type = new JSType();
        CompilerInput input = new CompilerInput();
        boolean inferred = true;
        
        Var var = scope.declare(name, nameNode, type, input, inferred);
        scope.undeclare(var);

        Var undeclaredVar = scope.getVar(name);

        assertNull(undeclaredVar);
    }

    @Test
    public void testGetVar() {
        String name = "varName";
        Node nameNode = new Node();
        JSType type = new JSType();
        CompilerInput input = new CompilerInput();
        boolean inferred = true;
        
        scope.declare(name, nameNode, type, input, inferred);

        Var var = scope.getVar(name);

        assertNotNull(var);
        assertEquals("varName", var.name);
        assertEquals(nameNode, var.nameNode);
        assertEquals(type, var.type);
        assertEquals(input, var.input);
        assertEquals(inferred, var.typeInferred);
    }

    @Test
    public void testIsDeclared() {
        String name = "varName";
        Node nameNode = new Node();
        JSType type = new JSType();
        CompilerInput input = new CompilerInput();
        boolean inferred = true;

        assertFalse(scope.isDeclared(name, false));

        scope.declare(name, nameNode, type, input, inferred);

        assertTrue(scope.isDeclared(name, false));
        assertTrue(scope.isDeclared(name, true));
    }

    @Test
    public void testIsGlobal() {
        assertFalse(scope.isGlobal());
        assertTrue(scope.isLocal());
    }

    @Test
    public void testParent() {
        Scope parentScope = new Scope(null, null);
        scope = new Scope(parentScope, null);

        assertEquals(parentScope, scope.getParent());
    }

    @Test
    public void testGetRootNode() {
        Node rootNode = new Node();
        scope = new Scope(null, rootNode);

        assertEquals(rootNode, scope.getRootNode());
    }

    @Test
    public void testGetTypeOfThis() {
        ObjectType thisType = new ObjectType();
        scope = new Scope(null, thisType);

        assertEquals(thisType, scope.getTypeOfThis());
    }
}
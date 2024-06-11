import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class ScopeTest {

    private Scope globalScope;

    @Before
    public void setUp() {
        // Create a global scope with a dummy root node
        Node rootNode = new Node(Token.BLOCK);
        globalScope = new Scope(rootNode, null);
    }

    @Test
    public void testDeclareVariable() {
        // Arrange
        String name = "x";
        Node nameNode = new Node(Token.NAME, name);
        JSType type = new JSType("number");
        CompilerInput input = new CompilerInput("test.js");

        // Act
        Var var = globalScope.declare(name, nameNode, type, input);

        // Assert
        assertNotNull(var);
        assertEquals(name, var.getName());
        assertSame(nameNode, var.getNameNode());
        assertEquals(type, var.getType());
        assertFalse(var.isTypeInferred());
    }

    @Test
    public void testUndeclareVariable() {
        // Arrange
        String name = "x";
        Node nameNode = new Node(Token.NAME, name);
        JSType type = new JSType("number");
        CompilerInput input = new CompilerInput("test.js");
        Var var = globalScope.declare(name, nameNode, type, input);

        // Act
        globalScope.undeclare(var);

        // Assert
        Var undeclaredVar = globalScope.getVar(name);
        assertNull(undeclaredVar);
    }

    @Test
    public void testIsDeclared() {
        // Arrange
        String name = "x";
        Node nameNode = new Node(Token.NAME, name);
        JSType type = new JSType("number");
        CompilerInput input = new CompilerInput("test.js");
        globalScope.declare(name, nameNode, type, input);

        // Act
        boolean isDeclared = globalScope.isDeclared(name, false);

        // Assert
        assertTrue(isDeclared);
    }

    @Test
    public void testIsDeclaredWithRecurse() {
        // Arrange
        String name = "x";
        Node nameNode = new Node(Token.NAME, name);
        JSType type = new JSType("number");
        CompilerInput input = new CompilerInput("test.js");
        globalScope.declare(name, nameNode, type, input);

        // Act
        boolean isDeclared = globalScope.isDeclared(name, true);

        // Assert
        assertTrue(isDeclared);
    }

    @Test
    public void testIsNotDeclared() {
        // Arrange
        String name = "x";

        // Act
        boolean isDeclared = globalScope.isDeclared(name, false);

        // Assert
        assertFalse(isDeclared);
    }

    @Test
    public void testGetTypeOfThis() {
        // Arrange
        ObjectType thisType = new ObjectType("MyObject");
        Scope scope = new Scope(null, thisType);

        // Act
        ObjectType result = scope.getTypeOfThis();

        // Assert
        assertEquals(thisType, result);
    }

    @Test
    public void testGetRootNode() {
        // Arrange
        Node rootNode = new Node(Token.BLOCK);
        Scope scope = new Scope(rootNode, null);

        // Act
        Node result = scope.getRootNode();

        // Assert
        assertSame(rootNode, result);
    }

}
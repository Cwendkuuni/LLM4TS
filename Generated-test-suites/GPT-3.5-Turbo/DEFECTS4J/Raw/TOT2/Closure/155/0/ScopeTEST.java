import org.junit.Test;
import static org.junit.Assert.*;

public class ScopeTest {

    @Test
    public void testDeclareVariable() {
        Scope scope = new Scope(null, null);
        String name = "variable";
        Node nameNode = new Node(Token.NAME, name);
        JSType type = JSType.BOOLEAN;
        CompilerInput input = new CompilerInput("test.js");
        
        Var var = scope.declare(name, nameNode, type, input);
        
        assertNotNull(var);
        assertEquals(name, var.getName());
        assertEquals(nameNode, var.getNameNode());
        assertEquals(type, var.getType());
        assertEquals(input, var.input);
    }

    @Test
    public void testUndeclareVariable() {
        Scope scope = new Scope(null, null);
        String name = "variable";
        Node nameNode = new Node(Token.NAME, name);
        JSType type = JSType.BOOLEAN;
        CompilerInput input = new CompilerInput("test.js");

        Var var = scope.declare(name, nameNode, type, input);
        scope.undeclare(var);

        assertNull(scope.getVar(name));
    }

    @Test
    public void testGetGlobalScope() {
        Scope globalScope = new Scope(null, new Node(Token.BLOCK), new AbstractCompiler());
        Scope scope = new Scope(globalScope, new Node(Token.FUNCTION));

        Scope result = scope.getGlobalScope();

        assertEquals(globalScope, result);
    }

    @Test
    public void testIsDeclared() {
        Scope scope = new Scope(null, null);
        String name = "variable";
        Node nameNode = new Node(Token.NAME, name);
        JSType type = JSType.BOOLEAN;
        CompilerInput input = new CompilerInput("test.js");

        scope.declare(name, nameNode, type, input);

        assertTrue(scope.isDeclared(name, false));
        assertFalse(scope.isDeclared("nonexistent", false));
    }

    @Test
    public void testGetVars() {
        Scope scope = new Scope(null, null);
        String name1 = "variable1";
        Node nameNode1 = new Node(Token.NAME, name1);
        JSType type1 = JSType.BOOLEAN;
        CompilerInput input1 = new CompilerInput("test1.js");

        String name2 = "variable2";
        Node nameNode2 = new Node(Token.NAME, name2);
        JSType type2 = JSType.NUMBER;
        CompilerInput input2 = new CompilerInput("test2.js");

        scope.declare(name1, nameNode1, type1, input1);
        scope.declare(name2, nameNode2, type2, input2);

        Iterator<Var> iterator = scope.getVars();

        assertTrue(iterator.hasNext());
        Var var1 = iterator.next();
        assertEquals(name1, var1.getName());
        assertEquals(input1, var1.input);

        assertTrue(iterator.hasNext());
        Var var2 = iterator.next();
        assertEquals(name2, var2.getName());
        assertEquals(input2, var2.input);
    }

}
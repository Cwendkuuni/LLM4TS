import org.junit.Test;
import static org.junit.Assert.*;

public class ScopeTest {

    @Test
    public void testDeclareVar() {
        Scope scope = new Scope(null, null);
        String name = "testVar";
        Node nameNode = new Node(Token.NAME, new Node(Token.STRING, "testVar"));
        JSType type = JSType.UNKNOWN;
        CompilerInput input = new CompilerInput("test_input.js");
        
        Var var = scope.declare(name, nameNode, type, input);
        
        assertNotNull(var);
        assertEquals(name, var.getName());
        assertEquals(nameNode, var.getNameNode());
        assertEquals(type, var.getType());
        assertEquals(input, var.getInputName());
    }

    // Add more test cases for other public methods in Scope class here

}
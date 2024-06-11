import static org.junit.Assert.*;
import org.junit.Test;

public class CollapseVariableDeclarationsTest {

    @Test
    public void testProcess_NoVariableCollapses() {
        // Create a Compiler
        CollapseVariableDeclarations c = new CollapseVariableDeclarations(new Compiler());

        // Create some nodes without variable collapses
        Node externs = new Node(Token.EXTERNS);
        Node root = new Node(Token.ROOT);
        root.addChildToBack(new Node(Token.VAR));
        root.addChildToBack(new Node(Token.FUNCTION));

        // Call process method
        c.process(externs, root);

        // Assert that there are no collapses
        assertEquals(0, c.collapses.size());
    }

    @Test
    public void testProcess_VariableCollapses() {
        // Create a Compiler
        CollapseVariableDeclarations c = new CollapseVariableDeclarations(new Compiler());

        // Create some nodes with variable collapses
        Node externs = new Node(Token.EXTERNS);
        Node root = new Node(Token.ROOT);
        Node var1 = new Node(Token.VAR);
        Node var2 = new Node(Token.VAR);
        Node var3 = new Node(Token.VAR);
        var1.addChildToBack(new Node(Token.NAME, new Node(Token.STRING)));
        var2.addChildToBack(new Node(Token.NAME, new Node(Token.NUMBER)));
        var3.addChildToBack(new Node(Token.NAME, new Node(Token.NULL)));

        root.addChildToBack(var1);
        root.addChildToBack(var2);
        root.addChildToBack(var3);

        // Call process method
        c.process(externs, root);

        // Assert that there are variable collapses
        assertEquals(1, c.collapses.size());
    }

    // Add more test cases if needed for edge cases and exceptions

}
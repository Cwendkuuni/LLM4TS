import com.google.javascript.rhino.Node;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class CollapseVariableDeclarationsTest {

    private CollapseVariableDeclarations collapseVariableDeclarations;

    @Before
    public void setUp() {
        collapseVariableDeclarations = new CollapseVariableDeclarations();
    }

    // Test if multiple variable declarations within a function are collapsed into a single declaration.
    @Test
    public void testCollapseVariableDeclarationsWithinFunction() {
        Node rootNode = createTestNodeWithMultipleVariableDeclarationsWithinFunction();
        collapseVariableDeclarations.process(null, rootNode);

        // Verify that the variable declarations have been collapsed into a single declaration
        String expectedOutput = "var a, b = 1, c = 2;";
        assertEquals(expectedOutput, rootNode.toString());
    }

    // Test if assigns are collapsed into a single declaration.
    @Test
    public void testCollapseVariableAssigns() {
        Node rootNode = createTestNodeWithVariableAssigns();
        collapseVariableDeclarations.process(null, rootNode);

        // Verify that the variable assigns have been collapsed into a single declaration
        String expectedOutput = "var c = b = a = true;";
        assertEquals(expectedOutput, rootNode.toString());
    }

    // Test if there are no variable collapses, the parse tree remains unchanged
    @Test
    public void testNoCollapses() {
        Node rootNode = createTestNodeWithoutCollapses();
        collapseVariableDeclarations.process(null, rootNode);

        // Verify that the parse tree remains unchanged
        String expectedOutput = rootNode.toString();
        assertEquals(expectedOutput, rootNode.toString());
    }

    private Node createTestNodeWithMultipleVariableDeclarationsWithinFunction() {
        // var a;
        Node a = new Node(Token.NAME, "a");
        Node varA = new Node(Token.VAR, a);

        // var b = 1;
        Node b = new Node(Token.NAME, "b");
        Node one = new Node(Token.NUMBER, "1");
        Node varB = new Node(Token.VAR, new Node(Token.ASSIGN, b, one));

        // var c = 2;
        Node c = new Node(Token.NAME, "c");
        Node two = new Node(Token.NUMBER, "2");
        Node varC = new Node(Token.VAR, new Node(Token.ASSIGN, c, two));

        // function test() {...}
        Node name = new Node(Token.NAME, "test");
        Node args = new Node(Token.PARAM_LIST);
        Node block = createFunctionBody();
        Node function = new Node(Token.FUNCTION, name, args, block);

        Node rootNode = new Node(Token.SCRIPT, varA, varB, varC, function);
        return rootNode;
    }

    private Node createTestNodeWithVariableAssigns() {
        // a = true;
        Node a = new Node(Token.NAME, "a");
        Node trueValue = new Node(Token.TRUE);
        Node assignA = new Node(Token.ASSIGN, a, trueValue);

        // b = true;
        Node b = new Node(Token.NAME, "b");
        Node assignB = new Node(Token.ASSIGN, b, trueValue.cloneTree());

        // var c = true;
        Node c = new Node(Token.NAME, "c");
        Node varC = new Node(Token.VAR, new Node(Token.ASSIGN, c, trueValue.cloneTree()));

        Node rootNode = new Node(Token.SCRIPT, assignA, assignB, varC);
        return rootNode;
    }

    private Node createTestNodeWithoutCollapses() {
        // var a = 1;
        Node a = new Node(Token.NAME, "a");
        Node one = new Node(Token.NUMBER, "1");
        Node varA = new Node(Token.VAR, new Node(Token.ASSIGN, a, one));

        // var b = 2;
        Node b = new Node(Token.NAME, "b");
        Node two = new Node(Token.NUMBER, "2");
        Node varB = new Node(Token.VAR, new Node(Token.ASSIGN, b, two));

        Node rootNode = new Node(Token.SCRIPT, varA, varB);
        return rootNode;
    }

}
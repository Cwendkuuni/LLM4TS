import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class CheckGlobalThisTest {

    @Test
    public void shouldReportGlobalThisWhenThisOnLeftSideOfAssignment() {
        // Arrange
        // Create a new instance of CheckGlobalThis class
        CheckGlobalThis checkGlobalThis = new CheckGlobalThis();

        // Act
        boolean result = checkGlobalThis.shouldReportThis(new Node(Token.THIS), new Node(Token.ASSIGN));

        // Assert
        assertTrue(result);
    }

    @Test
    public void shouldNotReportGlobalThisWhenThisNotOnLeftSideOfAssignment() {
        // Arrange
        // Create a new instance of CheckGlobalThis class
        CheckGlobalThis checkGlobalThis = new CheckGlobalThis();

        // Act
        boolean result = checkGlobalThis.shouldReportThis(new Node(Token.THIS), new Node(Token.GETPROP));

        // Assert
        assertFalse(result);
    }

    @Test
    public void shouldGenerateJSDocInfoForFunction() {
        // Arrange
        // Create a new instance of CheckGlobalThis class
        CheckGlobalThis checkGlobalThis = new CheckGlobalThis();
        Node functionNode = new Node(Token.FUNCTION);

        // Act
        JSDocInfo result = checkGlobalThis.getFunctionJsDocInfo(functionNode);

        // Assert
        assertEquals(null, result);
    }

    @Test
    public void shouldTraverseNodeBasedOnParentType() {
        // Arrange
        // Create a new instance of CheckGlobalThis class
        CheckGlobalThis checkGlobalThis = new CheckGlobalThis();
        NodeTraversal t = new NodeTraversal();
        Node n = new Node(Token.FUNCTION);
        Node parent = new Node(Token.BLOCK);

        // Act
        boolean result = checkGlobalThis.shouldTraverse(t, n, parent);

        // Assert
        assertTrue(result);
    }

    @Test
    public void shouldNotTraverseNodeForConstructorFunction() {
        // Arrange
        // Create a new instance of CheckGlobalThis class
        CheckGlobalThis checkGlobalThis = new CheckGlobalThis();
        NodeTraversal t = new NodeTraversal();
        Node n = new Node(Token.FUNCTION);
        Node parent = new Node(Token.BLOCK);
        n.putProp(Node.JSDOC_INFO_PROP, new JSDocInfoBuilder(true).buildConstructor());

        // Act
        boolean result = checkGlobalThis.shouldTraverse(t, n, parent);

        // Assert
        assertFalse(result);
    }

    @Test
    public void shouldReportGlobalThisForGetParentNode() {
        // Arrange
        // Create a new instance of CheckGlobalThis class
        CheckGlobalThis checkGlobalThis = new CheckGlobalThis();
        NodeTraversal t = new NodeTraversal();
        Node n = new Node(Token.THIS);
        Node parent = new Node(Token.GETPROP);

        // Act
        boolean result = checkGlobalThis.shouldReportThis(n, parent);

        // Assert
        assertTrue(result);
    }
}
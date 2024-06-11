import org.junit.Test;
import static org.junit.Assert.*;

public class CheckGlobalThisTest {

    @Test
    public void testShouldTraverse_MatchFunctionWithConstructorAnnotation_ReturnsFalse() {
        // Test method should return false when encountering a function with @constructor annotation
        // Arrane
        NodeTraversal t = new NodeTraversal();
        Node n = new Node(Token.FUNCTION);
        JSDocInfo jsDoc = new JSDocInfo();
        jsDoc.recordConstructor();
        n.setJSDocInfo(jsDoc);
        
        // Act
        boolean result = checkGlobalThis.shouldTraverse(t, n, null);
        
        // Assert
        assertFalse(result);
    }

    @Test
    public void testShouldTraverse_MatchFunctionWithThisTypeAnnotation_ReturnsFalse() {
        // Test method should return false when encountering a function with @this annotation
        // Arrange
        NodeTraversal t = new NodeTraversal();
        Node n = new Node(Token.FUNCTION);
        JSDocInfo jsDoc = new JSDocInfo();
        jsDoc.recordThisType();
        n.setJSDocInfo(jsDoc);

        // Act
        boolean result = checkGlobalThis.shouldTraverse(t, n, null);

        // Assert
        assertFalse(result);
    }

    @Test
    public void testVisit_SafeThisUsage_DoesNotReportError() {
        // Test method should not report error for safe usage of 'this'
        // Arrange
        NodeTraversal t = new NodeTraversal();
        Node n = new Node(Token.THIS);
        Node parent = new Node(Token.ASSIGN);

        // Act
        checkGlobalThis.visit(t, n, parent);

        // Assert
        assertFalse(compiler.hasErrors());
    }

    @Test
    public void testVisit_UnsafeThisUsage_ReportsError() {
        // Test method should report error for unsafe usage of 'this'
        // Arrange
        NodeTraversal t = new NodeTraversal();
        Node n = new Node(Token.THIS);
        Node parent = new Node(Token.SUBPROPERTY);

        // Act
        checkGlobalThis.visit(t, n, parent);

        // Assert
        assertTrue(compiler.hasErrors());
    }

    @Test
    public void testGetFunctionJsDocInfo_NoJsDoc_ReturnsNull() {
        // Test method should return null when there is no JSDoc for a function
        // Arrange
        Node node = new Node(Token.FUNCTION);

        // Act
        JSDocInfo result = checkGlobalThis.getFunctionJsDocInfo(node);

        // Assert
        assertNull(result);
    }

}
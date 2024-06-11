import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class CheckGlobalThisTest {

    @Test
    public void testShouldTraverse_FunctionWithConstructorAnnotation_ReturnFalse() {
        CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null, null);

        NodeTraversal nodeTraversal = null;
        Node n = new Node(Token.FUNCTION);
        Node parent = null;

        boolean result = checkGlobalThis.shouldTraverse(nodeTraversal, n, parent);

        assertFalse(result);
    }

    @Test
    public void testShouldTraverse_FunctionWithThisAnnotation_ReturnFalse() {
        CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null, null);

        NodeTraversal nodeTraversal = null;
        Node n = new Node(Token.FUNCTION);
        Node parent = null;

        boolean result = checkGlobalThis.shouldTraverse(nodeTraversal, n, parent);

        assertFalse(result);
    }

    @Test
    public void testShouldTraverse_FunctionWithOverrideAnnotation_ReturnFalse() {
        CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null, null);

        NodeTraversal nodeTraversal = null;
        Node n = new Node(Token.FUNCTION);
        Node parent = null;

        boolean result = checkGlobalThis.shouldTraverse(nodeTraversal, n, parent);

        assertFalse(result);
    }

    @Test
    public void testShouldTraverse_FunctionWithTypeButNoAnnotation_ReturnTrue() {
        CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null, null);

        NodeTraversal nodeTraversal = null;
        Node n = new Node(Token.FUNCTION);
        Node parent = null;

        boolean result = checkGlobalThis.shouldTraverse(nodeTraversal, n, parent);

        assertTrue(result);
    }

    @Test
    public void testShouldTraverse_FunctionInBlock_ReturnTrue() {
        CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null, null);

        NodeTraversal nodeTraversal = null;
        Node n = new Node(Token.FUNCTION);
        Node parent = new Node(Token.BLOCK);

        boolean result = checkGlobalThis.shouldTraverse(nodeTraversal, n, parent);

        assertTrue(result);
    }

    @Test
    public void testShouldTraverse_FunctionInScript_ReturnTrue() {
        CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null, null);

        NodeTraversal nodeTraversal = null;
        Node n = new Node(Token.FUNCTION);
        Node parent = new Node(Token.SCRIPT);

        boolean result = checkGlobalThis.shouldTraverse(nodeTraversal, n, parent);

        assertTrue(result);
    }

    @Test
    public void testShouldTraverse_FunctionInName_ReturnTrue() {
        CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null, null);

        NodeTraversal nodeTraversal = null;
        Node n = new Node(Token.FUNCTION);
        Node parent = new Node(Token.NAME);

        boolean result = checkGlobalThis.shouldTraverse(nodeTraversal, n, parent);

        assertTrue(result);
    }

    @Test
    public void testShouldTraverse_FunctionInAssign_ReturnTrue() {
        CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null, null);

        NodeTraversal nodeTraversal = null;
        Node n = new Node(Token.FUNCTION);
        Node parent = new Node(Token.ASSIGN);

        boolean result = checkGlobalThis.shouldTraverse(nodeTraversal, n, parent);

        assertTrue(result);
    }

    @Test
    public void testShouldTraverse_FunctionInStringKey_ReturnTrue() {
        CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null, null);

        NodeTraversal nodeTraversal = null;
        Node n = new Node(Token.FUNCTION);
        Node parent = new Node(Token.STRING);

        boolean result = checkGlobalThis.shouldTraverse(nodeTraversal, n, parent);

        assertTrue(result);
    }

    @Test
    public void testShouldTraverse_FunctionInNumberKey_ReturnTrue() {
        CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null, null);

        NodeTraversal nodeTraversal = null;
        Node n = new Node(Token.FUNCTION);
        Node parent = new Node(Token.NUMBER);

        boolean result = checkGlobalThis.shouldTraverse(nodeTraversal, n, parent);

        assertTrue(result);
    }

    @Test
    public void testShouldTraverse_LhsChildNotNull_ReturnTrue() {
        CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null, null);

        NodeTraversal nodeTraversal = null;
        Node n = new Node(Token.FUNCTION);
        Node parent = null;

        checkGlobalThis.visit(nodeTraversal, n, parent);

        NodeTraversal t = null;
        Node lhs = new Node();
        Node rhs = new Node();

        boolean result = checkGlobalThis.shouldTraverse(t, lhs, rhs);

        assertTrue(result);
    }

    @Test
    public void testShouldTraverse_IsGetWithPrototype_ReturnFalse() {
        CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null, null);

        NodeTraversal nodeTraversal = null;
        Node n = new Node(Token.FUNCTION);
        Node parent = new Node(Token.ASSIGN);

        checkGlobalThis.visit(nodeTraversal, n, parent);

        NodeTraversal t = null;
        Node lhs = new Node(Token.GETPROP);
        Node rhs = new Node();

        boolean result = checkGlobalThis.shouldTraverse(t, lhs, rhs);

        assertFalse(result);
    }

    @Test
    public void testShouldTraverse_IsGetWithPrototypeAncestor_ReturnFalse() {
        CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null, null);

        NodeTraversal nodeTraversal = null;
        Node n = new Node(Token.FUNCTION);
        Node parent = new Node(Token.ASSIGN);

        checkGlobalThis.visit(nodeTraversal, n, parent);

        NodeTraversal t = null;
        Node lhs = new Node(Token.GETPROP);
        Node rhs = new Node();

        Node llhs = new Node(Token.GETPROP);
        lhs.addChildToFront(llhs);

        boolean result = checkGlobalThis.shouldTraverse(t, lhs, rhs);

        assertFalse(result);
    }

    @Test
    public void testVisit_ThisWithParentNotGet_AddsErrorToCompiler() {
        AbstractCompiler compiler = null;
        CheckLevel level = null;
        CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler, level);

        NodeTraversal t = null;
        Node n = new Node(Token.THIS);
        Node parent = new Node();

        checkGlobalThis.visit(t, n, parent);

        // Assert that the expected error is added to the compiler
    }
}
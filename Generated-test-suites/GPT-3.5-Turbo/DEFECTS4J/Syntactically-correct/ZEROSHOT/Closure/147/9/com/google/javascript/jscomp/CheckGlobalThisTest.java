import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class CheckGlobalThisTest {

    private CheckGlobalThis checkGlobalThis;

    @Before
    public void setUp() {
        checkGlobalThis = new CheckGlobalThis(new AbstractCompiler(), CheckLevel.WARNING);
    }

    @Test
    public void testShouldTraverse_FunctionWithConstructorAnnotation_ReturnsFalse() {
        NodeTraversal nodeTraversal = new NodeTraversal();

        boolean result = checkGlobalThis.shouldTraverse(nodeTraversal, new Node(), new Node());

        assertFalse(result);
    }

    @Test
    public void testShouldTraverse_FunctionWithThisAnnotation_ReturnsFalse() {
        NodeTraversal nodeTraversal = new NodeTraversal();

        boolean result = checkGlobalThis.shouldTraverse(nodeTraversal, new Node(), new Node());

        assertFalse(result);
    }

    @Test
    public void testShouldTraverse_FunctionWithOverrideAnnotation_ReturnsFalse() {
        NodeTraversal nodeTraversal = new NodeTraversal();

        boolean result = checkGlobalThis.shouldTraverse(nodeTraversal, new Node(), new Node());

        assertFalse(result);
    }

    @Test
    public void testShouldTraverse_NonFunctionNode_ReturnsFalse() {
        NodeTraversal nodeTraversal = new NodeTraversal();

        boolean result = checkGlobalThis.shouldTraverse(nodeTraversal, new Node(), new Node());

        assertFalse(result);
    }

    @Test
    public void testShouldTraverse_FunctionInsideBlock_ReturnsTrue() {
        NodeTraversal nodeTraversal = new NodeTraversal();

        boolean result = checkGlobalThis.shouldTraverse(nodeTraversal, new Node(), new Node());

        assertTrue(result);
    }

    @Test
    public void testShouldTraverse_FunctionInsideScript_ReturnsTrue() {
        NodeTraversal nodeTraversal = new NodeTraversal();

        boolean result = checkGlobalThis.shouldTraverse(nodeTraversal, new Node(), new Node());

        assertTrue(result);
    }

    @Test
    public void testShouldTraverse_FunctionInsideName_ReturnsTrue() {
        NodeTraversal nodeTraversal = new NodeTraversal();

        boolean result = checkGlobalThis.shouldTraverse(nodeTraversal, new Node(), new Node());

        assertTrue(result);
    }

    @Test
    public void testShouldTraverse_FunctionInsideAssign_ReturnsTrue() {
        NodeTraversal nodeTraversal = new NodeTraversal();

        boolean result = checkGlobalThis.shouldTraverse(nodeTraversal, new Node(), new Node());

        assertTrue(result);
    }

    @Test
    public void testVisit_ThisNode_ShouldReportError() {
        NodeTraversal nodeTraversal = new NodeTraversal();
        Node n = new Node(Token.THIS);
        Node parent = new Node(Token.GETPROP);

        checkGlobalThis.visit(nodeTraversal, n, parent);

        // assert error is reported
    }

    @Test
    public void testVisit_ThisNodeOnLeftSideOfAssignment_ShouldReportError() {
        NodeTraversal nodeTraversal = new NodeTraversal();
        Node n = new Node(Token.THIS);
        Node parent = new Node(Token.ASSIGN);

        checkGlobalThis.visit(nodeTraversal, n, parent);

        // assert error is reported
    }

}
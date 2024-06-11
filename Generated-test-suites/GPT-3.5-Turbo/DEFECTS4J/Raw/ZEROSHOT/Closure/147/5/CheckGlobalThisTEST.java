import static org.junit.Assert.*;
import org.junit.Test;

public class CheckGlobalThisTest {

    @Test
    public void testShouldTraverse_GlobalContext_ReturnsTrue() {
        AbstractCompiler compiler = new AbstractCompiler();
        CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler, CheckLevel.WARNING);
        NodeTraversal t = new NodeTraversal();
        Node n = new Node();
        Node parent = new Node();

        boolean result = checkGlobalThis.shouldTraverse(t, n, parent);

        assertTrue(result);
    }

    @Test
    public void testShouldTraverse_ConstructorWithThisAnnotation_ReturnsFalse() {
        AbstractCompiler compiler = new AbstractCompiler();
        CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler, CheckLevel.WARNING);
        NodeTraversal t = new NodeTraversal();
        Node n = new Node(Token.FUNCTION);
        n.setJSDocInfo(new JSDocInfo().setConstructor(true));
        Node parent = new Node();

        boolean result = checkGlobalThis.shouldTraverse(t, n, parent);

        assertFalse(result);
    }

    @Test
    public void testShouldTraverse_FunctionWithThisAnnotation_ReturnsFalse() {
        AbstractCompiler compiler = new AbstractCompiler();
        CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler, CheckLevel.WARNING);
        NodeTraversal t = new NodeTraversal();
        Node n = new Node(Token.FUNCTION);
        n.setJSDocInfo(new JSDocInfo().setThisType(new JSTypeExpression()));
        Node parent = new Node();

        boolean result = checkGlobalThis.shouldTraverse(t, n, parent);

        assertFalse(result);
    }

    @Test
    public void testShouldTraverse_FunctionWithoutLogicalPlaceForThisAnnotation_ReturnsFalse() {
        AbstractCompiler compiler = new AbstractCompiler();
        CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler, CheckLevel.WARNING);
        NodeTraversal t = new NodeTraversal();
        Node n = new Node(Token.FUNCTION);
        Node parent = new Node(Token.ADD);

        boolean result = checkGlobalThis.shouldTraverse(t, n, parent);

        assertFalse(result);
    }

    @Test
    public void testShouldTraverse_AssignmentToPrototypeProperty_ReturnsFalse() {
        AbstractCompiler compiler = new AbstractCompiler();
        CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler, CheckLevel.WARNING);
        NodeTraversal t = new NodeTraversal();
        Node n = new Node(Token.ASSIGN);
        Node lhs = new Node(Token.GETPROP, new Node(Token.NAME, "prototype"), new Node(Token.STRING, "property"));
        n.addChildToBack(lhs);
        Node parent = new Node();

        boolean result = checkGlobalThis.shouldTraverse(t, n, parent);

        assertFalse(result);
    }

    @Test
    public void testShouldTraverse_AssignmentToPrototypeSubproperty_ReturnsFalse() {
        AbstractCompiler compiler = new AbstractCompiler();
        CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler, CheckLevel.WARNING);
        NodeTraversal t = new NodeTraversal();
        Node n = new Node(Token.ASSIGN);
        Node lhs = new Node(Token.GETPROP, new Node(Token.GETPROP, new Node(Token.NAME, "prototype"), new Node(Token.STRING, "property")), new Node(Token.STRING, "subproperty"));
        n.addChildToBack(lhs);
        Node parent = new Node();

        boolean result = checkGlobalThis.shouldTraverse(t, n, parent);

        assertFalse(result);
    }

    @Test
    public void testVisit_ThisInGlobalScope_ShouldReportThis() {
        AbstractCompiler compiler = new AbstractCompiler();
        CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler, CheckLevel.WARNING);
        NodeTraversal t = new NodeTraversal();
        Node n = new Node(Token.THIS);
        Node parent = new Node();

        checkGlobalThis.visit(t, n, parent);

        assertEquals(1, compiler.getErrors().size());
        // assert error message
    }

    @Test
    public void testVisit_ThisOnLeftSideOfAssignment_ShouldReportThis() {
        AbstractCompiler compiler = new AbstractCompiler();
        CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler, CheckLevel.WARNING);
        NodeTraversal t = new NodeTraversal();
        Node n = new Node(Token.THIS);
        Node parent = new Node(Token.ASSIGN);
        parent.addChildToFront(n);

        checkGlobalThis.visit(t, n, parent);

        assertEquals(1, compiler.getErrors().size());
        // assert error message
    }

    @Test
    public void testVisit_ThisWithPropertyAccess_ShouldReportThis() {
        AbstractCompiler compiler = new AbstractCompiler();
        CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler, CheckLevel.WARNING);
        NodeTraversal t = new NodeTraversal();
        Node n = new Node(Token.THIS);
        Node parent = new Node(Token.GETPROP);
        parent.addChildToFront(n);

        checkGlobalThis.visit(t, n, parent);

        assertEquals(1, compiler.getErrors().size());
        // assert error message
    }
}
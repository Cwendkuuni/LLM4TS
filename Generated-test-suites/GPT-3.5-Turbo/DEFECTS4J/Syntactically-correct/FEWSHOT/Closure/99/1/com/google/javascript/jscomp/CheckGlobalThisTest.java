import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class CheckGlobalThisTest {

    @Test
    public void testShouldTraversePrototypeMethod() {
        NodeTraversal t = new NodeTraversal();
        Node n = new Node(Token.FUNCTION);
        Node parent = new Node(Token.NAME);

        assertFalse(CheckGlobalThis.shouldTraverse(t, n, parent));
    }

    @Test
    public void testShouldTraverseConstructorFunction() {
        NodeTraversal t = new NodeTraversal();
        Node n = new Node(Token.FUNCTION);
        Node parent = new Node(Token.NAME);
        JSDocInfo jsDoc = new JSDocInfo();
        jsDoc.setConstructor(true);
        n.setJSDocInfo(jsDoc);

        assertFalse(CheckGlobalThis.shouldTraverse(t, n, parent));
    }

    @Test
    public void testShouldTraverseThisAnnotatedFunction() {
        NodeTraversal t = new NodeTraversal();
        Node n = new Node(Token.FUNCTION);
        Node parent = new Node(Token.NAME);
        JSDocInfo jsDoc = new JSDocInfo();
        jsDoc.recordThisType();
        n.setJSDocInfo(jsDoc);

        assertFalse(CheckGlobalThis.shouldTraverse(t, n, parent));
    }

    @Test
    public void testShouldTraverseOverrideFunction() {
        NodeTraversal t = new NodeTraversal();
        Node n = new Node(Token.FUNCTION);
        Node parent = new Node(Token.NAME);
        JSDocInfo jsDoc = new JSDocInfo();
        jsDoc.setOverride(true);
        n.setJSDocInfo(jsDoc);

        assertFalse(CheckGlobalThis.shouldTraverse(t, n, parent));
    }

    @Test
    public void testShouldTraverseFunctionInBlock() {
        NodeTraversal t = new NodeTraversal();
        Node n = new Node(Token.FUNCTION);
        Node parent = new Node(Token.BLOCK);

        assertTrue(CheckGlobalThis.shouldTraverse(t, n, parent));
    }

    @Test
    public void testShouldTraverseFunctionInScript() {
        NodeTraversal t = new NodeTraversal();
        Node n = new Node(Token.FUNCTION);
        Node parent = new Node(Token.SCRIPT);

        assertTrue(CheckGlobalThis.shouldTraverse(t, n, parent));
    }

    @Test
    public void testShouldTraverseFunctionInName() {
        NodeTraversal t = new NodeTraversal();
        Node n = new Node(Token.FUNCTION);
        Node parent = new Node(Token.NAME);

        assertTrue(CheckGlobalThis.shouldTraverse(t, n, parent));
    }

    @Test
    public void testShouldTraverseFunctionInAssignment() {
        NodeTraversal t = new NodeTraversal();
        Node n = new Node(Token.FUNCTION);
        Node parent = new Node(Token.ASSIGN);

        assertTrue(CheckGlobalThis.shouldTraverse(t, n, parent));
    }

    @Test
    public void testShouldNotReportNestedAssignmentToPrototypeProperty() {
        NodeTraversal t = new NodeTraversal();
        Node n = new Node(Token.ASSIGN);
        Node parent = new Node(Token.GETPROP);
        parent.addChildToFront(new Node(Token.NAME, "prototype"));

        assertFalse(CheckGlobalThis.shouldTraverse(t, n, parent));
    }

    @Test
    public void testShouldNotReportNestedAssignmentToPrototypeSubproperty() {
        NodeTraversal t = new NodeTraversal();
        Node n = new Node(Token.ASSIGN);
        Node parent = new Node(Token.GETPROP);
        parent.addChildToFront(new Node(Token.GETPROP, "prototype"));

        assertFalse(CheckGlobalThis.shouldTraverse(t, n, parent));
    }

    @Test
    public void testShouldReportLeftSideOfAssignment() {
        NodeTraversal t = new NodeTraversal();
        Node n = new Node(Token.NAME);
        Node parent = new Node(Token.ASSIGN);

        assertTrue(CheckGlobalThis.shouldTraverse(t, n, parent));
    }

    @Test
    public void testShouldReportThisWithPropertyAccess() {
        NodeTraversal t = new NodeTraversal();
        Node n = new Node(Token.THIS);
        Node parent = new Node(Token.GETPROP);

        assertTrue(CheckGlobalThis.shouldTraverse(t, n, parent));
    }

    @Test
    public void testVisitReportsErrorForGlobalThis() {
        NodeTraversal t = new NodeTraversal();
        CheckGlobalThis checkGlobalThis = new CheckGlobalThis();
        AbstractCompiler compiler = new AbstractCompiler();
        checkGlobalThis.setCompiler(compiler);
        checkGlobalThis.setLevel(CheckLevel.WARNING);
        Node n = new Node(Token.THIS);
        Node parent = new Node(Token.NAME);

        checkGlobalThis.visit(t, n, parent);

        assertEquals(1, compiler.getErrorCount());
    }

    @Test
    public void testVisitDoesNotReportErrorForNonGlobalThis() {
        NodeTraversal t = new NodeTraversal();
        CheckGlobalThis checkGlobalThis = new CheckGlobalThis();
        AbstractCompiler compiler = new AbstractCompiler();
        checkGlobalThis.setCompiler(compiler);
        checkGlobalThis.setLevel(CheckLevel.WARNING);
        Node n = new Node(Token.NAME);
        Node parent = new Node(Token.NAME);

        checkGlobalThis.visit(t, n, parent);

        assertEquals(0, compiler.getErrorCount());
    }

    @Test
    public void testVisitResetsAssignLhsChild() {
        NodeTraversal t = new NodeTraversal();
        CheckGlobalThis checkGlobalThis = new CheckGlobalThis();
        AbstractCompiler compiler = new AbstractCompiler();
        checkGlobalThis.setCompiler(compiler);
        checkGlobalThis.setLevel(CheckLevel.WARNING);
        Node n = new Node(Token.THIS);
        Node parent = new Node(Token.NAME);

        checkGlobalThis.visit(t, n, parent);

        assertNull(checkGlobalThis.getAssignLhsChild());
    }
}
package com.google.javascript.jscomp;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

public class CheckGlobalThisTest {
    
    private CheckGlobalThis checkGlobalThis;
    private AbstractCompiler compiler;
    private CheckLevel level;

    @Before
    public void setUp() {
        compiler = new AbstractCompiler();
        level = CheckLevel.WARNING;
        checkGlobalThis = new CheckGlobalThis(compiler, level);
    }

    @Test
    public void testShouldTraverse_shouldReturnTrueForNonGlobalContext() {
        NodeTraversal t = new NodeTraversal(compiler);
        Node n = new Node(Token.ADD);
        Node parent = new Node(Token.EXPR_RESULT);

        boolean result = checkGlobalThis.shouldTraverse(t, n, parent);

        assertFalse(result);
    }

    @Test
    public void testShouldTraverse_shouldReturnTrueForFunctionWithoutJsDoc() {
        NodeTraversal t = new NodeTraversal(compiler);
        Node n = new Node(Token.FUNCTION, new Node(Token.NAME, "func"));
        Node parent = new Node(Token.FUNCTION, n);

        boolean result = checkGlobalThis.shouldTraverse(t, n, parent);

        assertFalse(result);
    }

    @Test
    public void testShouldTraverse_shouldReturnTrueForFunctionWithOnlyConstructorJsDoc() {
        NodeTraversal t = new NodeTraversal(compiler);
        Node n = new Node(Token.FUNCTION, new Node(Token.NAME, "func"));
        n.setJSDocInfo(createConstructorJsDoc());
        Node parent = new Node(Token.FUNCTION, n);

        boolean result = checkGlobalThis.shouldTraverse(t, n, parent);

        assertFalse(result);
    }

    @Test
    public void testShouldTraverse_shouldReturnTrueForFunctionWithOnlyThisJsDoc() {
        NodeTraversal t = new NodeTraversal(compiler);
        Node n = new Node(Token.FUNCTION, new Node(Token.NAME, "func"));
        n.setJSDocInfo(createThisJsDoc());
        Node parent = new Node(Token.FUNCTION, n);

        boolean result = checkGlobalThis.shouldTraverse(t, n, parent);

        assertFalse(result);
    }

    @Test
    public void testShouldTraverse_shouldReturnTrueForFunctionWithNonConstructorJsDoc() {
        NodeTraversal t = new NodeTraversal(compiler);
        Node n = new Node(Token.FUNCTION, new Node(Token.NAME, "func"));
        n.setJSDocInfo(createNonConstructorJsDoc());
        Node parent = new Node(Token.FUNCTION, n);

        boolean result = checkGlobalThis.shouldTraverse(t, n, parent);

        assertFalse(result);
    }

    @Test
    public void testShouldTraverse_shouldReturnTrueForFunctionWithoutAssignableThis() {
        NodeTraversal t = new NodeTraversal(compiler);
        Node n = new Node(Token.FUNCTION, new Node(Token.NAME, "func"));
        Node parent = new Node(Token.FUNCTION, n);

        boolean result = checkGlobalThis.shouldTraverse(t, n, parent);

        assertFalse(result);
    }

    @Test
    public void testShouldTraverse_shouldReturnTrueForAssignableThisOnLeftSideOfAssign() {
        NodeTraversal t = new NodeTraversal(compiler);
        Node n = new Node(Token.THIS);
        Node parent = new Node(Token.ASSIGN, n, new Node(Token.NAME, "x"));

        boolean result = checkGlobalThis.shouldTraverse(t, n, parent);

        assertTrue(result);
    }

    @Test
    public void testShouldTraverse_shouldReturnFalseForAssignableThisOnRightSideOfAssignToPrototype() {
        NodeTraversal t = new NodeTraversal(compiler);
        Node n = new Node(Token.THIS);
        Node parent = new Node(Token.ASSIGN, new Node(Token.GETPROP, n, new Node("prototype")), new Node(Token.NAME, "x"));

        boolean result = checkGlobalThis.shouldTraverse(t, n, parent);

        assertFalse(result);
    }

    @Test
    public void testShouldTraverse_shouldReturnFalseForAssignableThisOnRightSideOfAssignToPrototypeSubproperty() {
        NodeTraversal t = new NodeTraversal(compiler);
        Node n = new Node(Token.THIS);
        Node parent = new Node(Token.ASSIGN, new Node(Token.GETPROP, new Node(Token.GETPROP, n, new Node("prototype")), new Node("subproperty")), new Node(Token.NAME, "x"));

        boolean result = checkGlobalThis.shouldTraverse(t, n, parent);

        assertFalse(result);
    }

    @Test
    public void testVisit_shouldReportGlobalThisAndAssignLhsChildShouldBeNullForGlobalThis() {
        NodeTraversal t = new NodeTraversal(compiler);
        Node n = new Node(Token.THIS);
        Node parent = new Node(Token.EXPR_RESULT);
        checkGlobalThis.visit(t, n, parent);

        boolean result = checkGlobalThis.shouldReportThis(n, parent);

        assertTrue(result);
        assertNull(checkGlobalThis.assignLhsChild);
    }

    private JSDocInfo createConstructorJsDoc() {
        return new JSDocInfoBuilder().setConstructor(true).build();
    }

    private JSDocInfo createThisJsDoc() {
        return new JSDocInfoBuilder().addThisType().build();
    }

    private JSDocInfo createNonConstructorJsDoc() {
        return new JSDocInfoBuilder().build();
    }
}
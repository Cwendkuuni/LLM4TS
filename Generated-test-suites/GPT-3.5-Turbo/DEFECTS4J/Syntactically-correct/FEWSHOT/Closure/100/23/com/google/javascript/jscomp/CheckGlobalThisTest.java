package com.google.javascript.jscomp;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import com.google.javascript.rhino.JSDocInfo;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.Token;
import org.junit.Before;
import org.junit.Test;

public class CheckGlobalThisTest {

    private AbstractCompiler compiler;
    private CheckLevel level;
    private CheckGlobalThis checkGlobalThis;

    @Before
    public void setUp() {
        compiler = new AbstractCompiler();
        level = CheckLevel.WARNING;
        checkGlobalThis = new CheckGlobalThis(compiler, level);
    }

    @Test
    public void testShouldTraverse_FunctionWithConstructorAnnotation_ReturnsFalse() {
        NodeTraversal t = new NodeTraversal();
        Node n = createNodeWithTokenType(Token.FUNCTION);
        Node parent = null;
        n.setJSDocInfo(createJSDocInfoWithConstructorAnnotation());
        boolean result = checkGlobalThis.shouldTraverse(t, n, parent);
        assertEquals(false, result);
    }

    @Test
    public void testShouldTraverse_FunctionWithThisAnnotation_ReturnsFalse() {
        NodeTraversal t = new NodeTraversal();
        Node n = createNodeWithTokenType(Token.FUNCTION);
        Node parent = null;
        n.setJSDocInfo(createJSDocInfoWithThisAnnotation());
        boolean result = checkGlobalThis.shouldTraverse(t, n, parent);
        assertEquals(false, result);
    }

    @Test
    public void testShouldTraverse_FunctionWithoutAnnotations_ReturnsTrue() {
        NodeTraversal t = new NodeTraversal();
        Node n = createNodeWithTokenType(Token.FUNCTION);
        Node parent = null;
        boolean result = checkGlobalThis.shouldTraverse(t, n, parent);
        assertEquals(true, result);
    }

    @Test
    public void testShouldTraverse_NodeIsOnLeftSideOfAssignment_ReturnsTrue() {
        NodeTraversal t = new NodeTraversal();
        Node n = createNodeWithTokenType(Token.NAME);
        Node parent = createNodeWithTokenType(Token.ASSIGN);
        parent.addChildToFront(n);
        boolean result = checkGlobalThis.shouldTraverse(t, n, parent);
        assertEquals(true, result);
    }

    @Test
    public void testShouldTraverse_NodeInsideAssignmentToPrototypeProperty_ReturnsFalse() {
        NodeTraversal t = new NodeTraversal();
        Node n = createNodeWithTokenType(Token.NAME);
        Node parent = createNodeWithTokenType(Token.ASSIGN);
        parent.addChildToFront(createNodeWithQualifiedName("prototype"));
        parent.addChildToBack(n);
        boolean result = checkGlobalThis.shouldTraverse(t, n, parent);
        assertEquals(false, result);
    }

    @Test
    public void testShouldTraverse_NodeInsideAssignmentToSubProperty_ReturnsFalse() {
        NodeTraversal t = new NodeTraversal();
        Node n = createNodeWithTokenType(Token.NAME);
        Node parent = createNodeWithTokenType(Token.ASSIGN);
        parent.addChildToFront(createNodeWithQualifiedName("prototype.property"));
        parent.addChildToBack(n);
        boolean result = checkGlobalThis.shouldTraverse(t, n, parent);
        assertEquals(false, result);
    }

    @Test
    public void testShouldTraverse_NodeIsRightSideOfAssignment_ReturnsTrue() {
        NodeTraversal t = new NodeTraversal();
        Node n = createNodeWithTokenType(Token.NAME);
        Node parent = createNodeWithTokenType(Token.ASSIGN);
        parent.addChildToBack(n);
        boolean result = checkGlobalThis.shouldTraverse(t, n, parent);
        assertEquals(true, result);
    }

    @Test
    public void testShouldTraverse_ParentIsNull_ReturnsTrue() {
        NodeTraversal t = new NodeTraversal();
        Node n = createNodeWithTokenType(Token.FUNCTION);
        Node parent = null;
        boolean result = checkGlobalThis.shouldTraverse(t, n, parent);
        assertEquals(true, result);
    }

    @Test
    public void testShouldReportThis_AssignLhsChildIsNull_ReturnsFalse() {
        Node n = createNodeWithTokenType(Token.THIS);
        Node parent = createNodeWithTokenType(Token.NAME);
        boolean result = checkGlobalThis.shouldReportThis(n, parent);
        assertEquals(false, result);
    }

    @Test
    public void testShouldReportThis_AssignLhsChildNotNull_ReturnsTrue() {
        Node n = createNodeWithTokenType(Token.THIS);
        Node parent = createNodeWithTokenType(Token.ASSIGN);
        checkGlobalThis.setAssignLhsChild(n);
        boolean result = checkGlobalThis.shouldReportThis(n, parent);
        assertEquals(true, result);
    }

    @Test
    public void testVisit_NodeIsThisAndShouldReportThis_CallsCompilerReport() {
        NodeTraversal t = new NodeTraversal();
        Node n = createNodeWithTokenType(Token.THIS);
        Node parent = createNodeWithTokenType(Token.NAME);
        checkGlobalThis.setAssignLhsChild(n);
        checkGlobalThis.visit(t, n, parent);
        assertEquals(1, compiler.getReportCount());
    }

    @Test
    public void testVisit_NodeIsThisAndShouldNotReportThis_DoesNotCallCompilerReport() {
        NodeTraversal t = new NodeTraversal();
        Node n = createNodeWithTokenType(Token.THIS);
        Node parent = createNodeWithTokenType(Token.NAME);
        checkGlobalThis.visit(t, n, parent);
        assertEquals(0, compiler.getReportCount());
    }

    private Node createNodeWithTokenType(Token tokenType) {
        Node node = new Node();
        node.setType(tokenType);
        return node;
    }

    private JSDocInfo createJSDocInfoWithConstructorAnnotation() {
        JSDocInfo jsDocInfo = new JSDocInfo();
        jsDocInfo.setConstructor(true);
        return jsDocInfo;
    }

    private JSDocInfo createJSDocInfoWithThisAnnotation() {
        JSDocInfo jsDocInfo = new JSDocInfo();
        jsDocInfo.setHasThisType(true);
        return jsDocInfo;
    }

    private Node createNodeWithQualifiedName(String qualifiedName) {
        Node node = new Node();
        node.setQualifiedName(qualifiedName);
        return node;
    }
}
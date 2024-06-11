package com.google.javascript.jscomp;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

public class PrepareAstTest {
    PrepareAst prepareAst;

    @Before
    public void setUp() {
        prepareAst = new PrepareAst(new AbstractCompiler());
    }

    @Test
    public void testProcess_checkOnly() {
        Node externs = new Node(Token.EXTERN, new Node(Token.BLOCK));
        Node root = new Node(Token.ROOT, new Node(Token.BLOCK));

        prepareAst.process(externs, root);

        // Check that normalizeNodeTypes method was called
        // with the correct parameters
        assertEquals(2, normalizeNodeTypesCalls);
        assertEquals(root, normalizeNodeTypesParam);
        assertTrue(checkOnlyParam);
    }

    @Test
    public void testProcess() {
        Node externs = new Node(Token.EXTERN, new Node(Token.BLOCK));
        Node root = new Node(Token.ROOT, new Node(Token.BLOCK));

        prepareAst.process(externs, root);

        // Check that normalizeNodeTypes method was not called
        assertEquals(0, normalizeNodeTypesCalls);
        // Check that PrepareAnnotations method was called
        assertEquals(2, prepareAnnotationsCalls);
        assertEquals(externs, prepareAnnotationsParams.get(0));
        assertEquals(root, prepareAnnotationsParams.get(1));
    }

    @Test
    public void testReportChange() {
        prepareAst.reportChange();

        // No assertions, just verifying that the method does not throw any exceptions
    }

    @Test
    public void testNormalizeNodeTypes_ifStmt() {
        Node ifStmt = new Node(Token.IF);

        prepareAst.normalizeNodeTypes(ifStmt);

        assertTrue(ifStmt.isBlock());
        assertTrue(ifStmt.isEmpty());
        assertTrue(ifStmt.getWasEmptyNode());
        assertEquals(1, normalizeNodeTypesCalls);
    }
    
    @Test
    public void testNormalizeNodeTypes_block() {
        Node block = new Node(Token.BLOCK);

        prepareAst.normalizeNodeTypes(block);

        assertFalse(block.isBlock());
        assertEquals(1, normalizeNodeTypesCalls);
    }

    @Test
    public void testAnnotateCalls_freeCall() {
        Node call = new Node(Token.CALL);
        Node name = new Node(Token.NAME, "foo");
        call.addChildToBack(name);

        prepareAst.annotateCalls(call);

        assertTrue(call.getBooleanProp(Node.FREE_CALL));
    }

    @Test
    public void testAnnotateCalls_directEval() {
        Node call = new Node(Token.CALL);
        Node name = new Node(Token.NAME, "eval");
        call.addChildToBack(name);

        prepareAst.annotateCalls(call);

        assertTrue(name.getBooleanProp(Node.DIRECT_EVAL));
    }

    @Test
    public void testAnnotateCalls_notFreeCall() {
        Node call = new Node(Token.CALL);
        Node getProp = new Node(Token.GETPROP, new Node(Token.NAME, "obj"), new Node(Token.STRING, "prop"));
        call.addChildToBack(getProp);

        prepareAst.annotateCalls(call);

        assertFalse(call.getBooleanProp(Node.FREE_CALL));
    }

    @Test
    public void testAnnotateDispatchers_parentNotAssign() {
        Node function = new Node(Token.FUNCTION);
        Node parent = new Node(Token.NAME, "parent");

        prepareAst.annotateDispatchers(function, parent);

        assertFalse(function.getBooleanProp(Node.IS_DISPATCHER));
    }

    @Test
    public void testAnnotateDispatchers_noJSDocInfo() {
        Node function = new Node(Token.FUNCTION);
        Node parent = new Node(Token.ASSIGN);
        parent.addChildToBack(new Node(Token.NAME, "foo"));
        parent.addChildToBack(function);

        prepareAst.annotateDispatchers(function, parent);

        assertFalse(function.getBooleanProp(Node.IS_DISPATCHER));
    }

    @Test
    public void testAnnotateDispatchers_notJavaDispatch() {
        Node function = new Node(Token.FUNCTION);
        Node parent = new Node(Token.ASSIGN);
        parent.addChildToBack(new Node(Token.NAME, "foo"));
        parent.addChildToBack(function);
        parent.setJSDocInfo(new JSDocInfo());

        prepareAst.annotateDispatchers(function, parent);

        assertFalse(function.getBooleanProp(Node.IS_DISPATCHER));
    }

    @Test
    public void testAnnotateDispatchers_isJavaDispatch() {
        Node function = new Node(Token.FUNCTION);
        Node parent = new Node(Token.ASSIGN);
        parent.addChildToBack(new Node(Token.NAME, "foo"));
        parent.addChildToBack(function);

        JSDocInfo jsDocInfo = new JSDocInfo();
        jsDocInfo.setJavaDispatch(true);

        parent.setJSDocInfo(jsDocInfo);

        prepareAst.annotateDispatchers(function, parent);

        assertTrue(function.getBooleanProp(Node.IS_DISPATCHER));
    }

    @Test
    public void testNormalizeObjectLiteralKeyAnnotations() {
        Node objlit = new Node(Token.OBJECTLIT);
        Node key = new Node(Token.STRING, "key");
        Node value = new Node(Token.STRING, "value");
        key.addChildToBack(value);

        prepareAst.normalizeObjectLiteralKeyAnnotations(objlit, key, value);

        assertNull(key.getJSDocInfo());
        assertEquals("value", value.getJSDocInfo().getType().toString());
    }

    private int normalizeNodeTypesCalls = 0;
    private Node normalizeNodeTypesParam;
    private boolean checkOnlyParam;

    private int prepareAnnotationsCalls = 0;
    private List<Node> prepareAnnotationsParams = new ArrayList<>();

    private void normalizeNodeTypes(Node n) {
        normalizeNodeTypesCalls++;
        normalizeNodeTypesParam = n;
    }

    private void normalizeBlocks(Node n) {
        // No assertions, just verifying that the method does not throw any exceptions
    }

    private void annotateCalls(Node n) {
        // No assertions, just verifying that the method does not throw any exceptions
    }

    private void annotateDispatchers(Node n, Node parent) {
        // No assertions, just verifying that the method does not throw any exceptions
    }

    private void normalizeObjectLiteralAnnotations(Node objlit) {
        // No assertions, just verifying that the method does not throw any exceptions
    }

    private void prepareAnnotations(NodeTraversal t, Node n, Node parent) {
        prepareAnnotationsCalls++;
        prepareAnnotationsParams.add(n);
    }
}
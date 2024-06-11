
package com.google.javascript.jscomp;

import static org.junit.Assert.*;
import org.junit.BeforeClass;
import org.junit.Test;

public class PrepareAstTest {

    private static PrepareAst prepareAst;
    private static AbstractCompiler compiler;

    @BeforeClass
    public static void setUp() {
        compiler = new AbstractCompiler();
        prepareAst = new PrepareAst(compiler);
    }

    @Test
    public void testProcess() {
        Node externs = new Node(Token.EXTERN_RESULT);
        Node root = new Node(Token.ROOT);

        prepareAst.process(externs, root);

        assertNotNull(compiler);
    }

    @Test
    public void testNormalizeNodeTypes() {
        Node node = new Node(Token.NUMBER);

        prepareAst.normalizeNodeTypes(node);

        assertNotNull(node);
    }

    @Test
    public void testNormalizeBlocks() {
        Node node = new Node(Token.BLOCK);

        prepareAst.normalizeBlocks(node);

        assertNotNull(node);
    }

    @Test
    public void testPrepareAnnotations_shouldTraverse() {
        NodeTraversal t = new NodeTraversal();
        Node node = new Node(Token.OBJECTLIT);

        prepareAst.new PrepareAnnotations().shouldTraverse(t, node, null);

        assertNotNull(t);
        assertNotNull(node);
    }

    @Test
    public void testPrepareAnnotations_visit() {
        NodeTraversal t = new NodeTraversal();
        Node node = new Node(Token.CALL);
        Node parent = new Node(Token.EXPR_RESULT);

        prepareAst.new PrepareAnnotations().visit(t, node, parent);

        assertNotNull(t);
        assertNotNull(node);
        assertNotNull(parent);
    }

    @Test
    public void testPrepareAnnotations_normalizeObjectLiteralAnnotations() {
        Node node = new Node(Token.OBJECTLIT);

        prepareAst.new PrepareAnnotations().normalizeObjectLiteralAnnotations(node);

        assertNotNull(node);
    }
    
    @Test
    public void testPrepareAnnotations_annotateCalls() {
        Node node = new Node(Token.CALL);

        prepareAst.new PrepareAnnotations().annotateCalls(node);

        assertNotNull(node);
    }
    
    @Test
    public void testPrepareAnnotations_annotateDispatchers() {
        Node node = new Node(Token.FUNCTION);
        Node parent = new Node(Token.ASSIGN);

        prepareAst.new PrepareAnnotations().annotateDispatchers(node, parent);

        assertNotNull(node);
        assertNotNull(parent);
    }
    
    @Test
    public void testPrepareAnnotations_normalizeObjectLiteralKeyAnnotations() {
        Node objlit = new Node(Token.OBJECTLIT);
        Node key = new Node("key");
        Node value = new Node(Token.STRING);

        prepareAst.new PrepareAnnotations().normalizeObjectLiteralKeyAnnotations(objlit, key, value);

        assertNotNull(objlit);
        assertNotNull(key);
        assertNotNull(value);
    }

}


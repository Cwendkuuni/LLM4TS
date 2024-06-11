import org.junit.Test;

import static org.junit.Assert.*;

public class PrepareAstTest {
    @Test
    public void testNormalizeNodeTypes() {
        PrepareAst prepareAst = new PrepareAst(compiler);
        Node root = new Node(Token.ROOT);
        Node externs = new Node(Token.EXTERN);

        // Test for empty AST
        prepareAst.process(externs, root);
        assertEquals("ROOT", root.getType());
        assertNull(root.getFirstChild());

        // Test for AST with single node
        Node node1 = new Node(Token.NUMBER);
        root.addChildToBack(node1);
        prepareAst.process(externs, root);

        assertEquals("ROOT", root.getType());
        assertNotNull(root.getFirstChild());
        assertEquals("NUMBER", root.getFirstChild().getType());
    }

    @Test
    public void testNormalizeBlocks() {
        PrepareAst prepareAst = new PrepareAst(compiler);
        Node root = new Node(Token.ROOT);
        Node externs = new Node(Token.EXTERN);

        // Test for empty AST
        prepareAst.process(externs, root);
        assertEquals("ROOT", root.getType());
        assertNull(root.getFirstChild());

        // Test for IF statement without block
        Node ifStatement = new Node(Token.IF);
        Node condition = new Node(Token.TRUE);
        ifStatement.addChildToBack(condition);
        Node trueStatement = new Node(Token.EXPR_RESULT);
        trueStatement.addChildToBack(new Node(Token.EMPTY));
        ifStatement.addChildToBack(trueStatement);
        root.addChildToBack(ifStatement);
        prepareAst.process(externs, root);
        assertEquals("ROOT", root.getType());
        assertNotNull(root.getFirstChild());
        assertEquals("IF", root.getFirstChild().getType());
        assertEquals("BLOCK", root.getFirstChild().getChildAtIndex(1).getType());

        // Test for WHILE statement without block
        Node whileStatement = new Node(Token.WHILE);
        condition = new Node(Token.TRUE);
        whileStatement.addChildToBack(condition);
        Node whileBody = new Node(Token.EXPR_RESULT);
        whileStatement.addChildToBack(whileBody);
        root.addChildToBack(whileStatement);
        prepareAst.process(externs, root);
        assertEquals("ROOT", root.getType());
        assertNotNull(root.getFirstChild());
        assertEquals("WHILE", root.getFirstChild().getNext().getType());
        assertEquals("BLOCK", root.getFirstChild().getNext().getChildAtIndex(1).getType());

        // Test for DO statement without block
        Node doStatement = new Node(Token.DO);
        doStatement.addChildToBack(new Node(Token.EXPR_RESULT, new Node(Token.EMPTY)));
        condition = new Node(Token.TRUE);
        doStatement.addChildToBack(condition);
        root.addChildToBack(doStatement);
        prepareAst.process(externs, root);
        assertEquals("ROOT", root.getType());
        assertNotNull(root.getFirstChild());
        assertEquals("DO", root.getFirstChild().getNext().getNext().getType());
        assertEquals("BLOCK", root.getFirstChild().getNext().getNext().getChildAtIndex(0).getType());
    }

    @Test
    public void testPrepareAnnotations() {
        PrepareAst.PrepareAnnotations prepareAnnotations = new PrepareAst.PrepareAnnotations();
        Node root = new Node(Token.ROOT);
        Node externs = new Node(Token.EXTERN);

        // Test for empty AST
        NodeTraversal.traverse(compiler, externs, prepareAnnotations);
        assertEquals("EXTERNS", externs.getType());
        assertNull(externs.getFirstChild());

        // Test for object literal
        Node objlit = new Node(Token.OBJECTLIT);
        Node key = new Node(Token.STRING, "a");
        Node value = new Node(Token.TRUE);
        key.addChildToBack(value);
        objlit.addChildToBack(key);
        externs.addChildToBack(objlit);
        NodeTraversal.traverse(compiler, externs, prepareAnnotations);
        assertEquals("EXTERNS", externs.getType());
        assertNotNull(externs.getFirstChild());
        assertEquals("OBJECTLIT", externs.getFirstChild().getType());
        assertEquals("OBJECTLIT", externs.getFirstChild().getFirstChild().getType());
        assertEquals("STRING", externs.getFirstChild().getFirstChild().getFirstChild().getType());
        assertEquals("TRUE", externs.getFirstChild().getFirstChild().getFirstChild().getFirstChild().getType());

        // Test for CALL expression
        Node call = new Node(Token.CALL);
        Node callee = new Node(Token.NAME, "foo");
        call.addChildToBack(callee);
        externs.addChildToBack(call);
        NodeTraversal.traverse(compiler, externs, prepareAnnotations);
        assertEquals("EXTERNS", externs.getType());
        assertNotNull(externs.getFirstChild().getNext());
        assertEquals("CALL", externs.getFirstChild().getNext().getType());
        assertTrue(externs.getFirstChild().getNext().getBooleanProp(Node.FREE_CALL));

        // Test for EVAL expression
        Node eval = new Node(Token.NAME, "eval");
        call.replaceChild(callee, eval);
        NodeTraversal.traverse(compiler, externs, prepareAnnotations);
        assertEquals("EXTERNS", externs.getType());
        assertNotNull(externs.getFirstChild().getNext());
        assertFalse(externs.getFirstChild().getNext().getBooleanProp(Node.FREE_CALL));
        assertTrue(externs.getFirstChild().getNext().getFirstChild().getBooleanProp(Node.DIRECT_EVAL));

        // Test for FUNCTION declaration
        Node function = new Node(Token.FUNCTION);
        externs.addChildToBack(function);
        NodeTraversal.traverse(compiler, externs, prepareAnnotations);
        assertEquals("EXTERNS", externs.getType());
        assertNotNull(externs.getFirstChild().getNext().getNext());
        assertEquals("FUNCTION", externs.getFirstChild().getNext().getNext().getType());
        assertFalse(externs.getFirstChild().getNext().getNext().getFirstChild().getBooleanProp(Node.IS_DISPATCHER));
    }
}
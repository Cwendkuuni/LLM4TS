import org.junit.Test;

import static org.junit.Assert.*;

public class PrepareAstTest {

    @Test
    public void testNormalizeBlocks() {
        PrepareAst prepareAst = new PrepareAst();
        Node n = new Node(Token.IF);
        Node child = new Node(Token.BLOCK);
        n.addChildToBack(child);

        prepareAst.normalizeBlocks(n);

        assertNotNull(n.getChildAtIndex(0));
        assertEquals(Token.BLOCK, n.getChildAtIndex(0).getType());
    }

    @Test
    public void testNormalizeNodeTypes() {
        PrepareAst prepareAst = new PrepareAst();
        Node n = new Node(Token.IF);
        Node child1 = new Node(Token.EXPR_VOID);
        Node child2 = new Node(Token.BLOCK);
        n.addChildToBack(child1);
        n.addChildToBack(child2);

        prepareAst.normalizeNodeTypes(n);

        assertEquals(Token.EXPR_RESULT, n.getChildAtIndex(0).getType());
    }

    @Test
    public void testPrepareAnnotations_normalizeObjectLiteralAnnotations() {
        PrepareAst.PrepareAnnotations prepareAnnotations = new PrepareAst.PrepareAnnotations();
        Node objlit = new Node(Token.OBJECTLIT);
        Node key = new Node(Token.STRING);
        key.setString("key");
        Node value = new Node(Token.STRING);
        key.addChildToBack(value);
        objlit.addChildToBack(key);

        prepareAnnotations.normalizeObjectLiteralAnnotations(objlit);

        assertNull(key.getJSDocInfo());
        assertNotNull(value.getJSDocInfo());
    }

    @Test
    public void testPrepareAnnotations_annotateCalls() {
        PrepareAst.PrepareAnnotations prepareAnnotations = new PrepareAst.PrepareAnnotations();
        Node call = new Node(Token.CALL);
        Node first = new Node(Token.NAME);
        first.setString("test");
        call.addChildToBack(first);

        prepareAnnotations.annotateCalls(call);

        assertTrue(call.getBooleanProp(Node.FREE_CALL));
    }

    @Test
    public void testPrepareAnnotations_annotateDispatchers() {
        PrepareAst.PrepareAnnotations prepareAnnotations = new PrepareAst.PrepareAnnotations();
        Node function = new Node(Token.FUNCTION);
        Node parent = new Node(Token.ASSIGN);
        parent.addChildToBack(function);

        prepareAnnotations.annotateDispatchers(function, parent);

        assertTrue(function.getBooleanProp(Node.IS_DISPATCHER));
    }

    @Test
    public void testPrepareAnnotations_normalizeObjectLiteralKeyAnnotations() {
        PrepareAst.PrepareAnnotations prepareAnnotations = new PrepareAst.PrepareAnnotations();
        Node objlit = new Node(Token.OBJECTLIT);
        Node key = new Node(Token.STRING);
        key.setString("key");
        Node value = new Node(Token.FUNCTION);
        key.addChildToBack(value);
        objlit.addChildToBack(key);

        prepareAnnotations.normalizeObjectLiteralKeyAnnotations(objlit, key, value);

        assertNull(key.getJSDocInfo());
        assertNotNull(value.getJSDocInfo());
    }
}
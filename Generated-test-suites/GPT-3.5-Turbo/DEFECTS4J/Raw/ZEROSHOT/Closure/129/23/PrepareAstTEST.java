import org.junit.Test;

import static org.junit.Assert.*;

public class PrepareAstTest {

    @Test
    public void testNormalizeNodeTypes() {
        PrepareAst prepareAst = new PrepareAst(null);
        Node root = new Node(Token.ROOT);
        Node node1 = new Node(Token.NAME, new Node(Token.STRING));
        root.addChildToBack(node1);

        prepareAst.normalizeNodeTypes(root);

        assertEquals(Token.ROOT, root.getType());
        assertEquals(Token.NAME, node1.getType());
    }

    @Test
    public void testNormalizeBlocks() {
        PrepareAst prepareAst = new PrepareAst(null);
        Node node1 = new Node(Token.IF);
        Node node2 = new Node(Token.BLOCK);
        node1.addChildToBack(node2);

        prepareAst.normalizeBlocks(node1);

        assertEquals(Token.IF, node1.getType());
        assertEquals(Token.BLOCK, node2.getType());
    }

    @Test
    public void testPrepareAnnotations() {
        PrepareAst.PrepareAnnotations prepareAnnotations = new PrepareAst.PrepareAnnotations();
        NodeTraversal nodeTraversal = new NodeTraversal(null);

        Node objlit = new Node(Token.OBJECTLIT);
        Node key = new Node(Token.STRING);
        Node value = new Node(Token.STRING);
        objlit.addChildToBack(key);
        key.addChildToBack(value);

        prepareAnnotations.normalizeObjectLiteralAnnotations(objlit);

        assertEquals(Token.OBJECTLIT, objlit.getType());
        assertEquals(Token.STRING, key.getType());
        assertEquals(Token.STRING, value.getType());
    }

    @Test
    public void testAnnotateCalls() {
        PrepareAst.PrepareAnnotations prepareAnnotations = new PrepareAst.PrepareAnnotations();
        Node node = new Node(Token.CALL);
        Node firstChild = new Node(Token.NAME, new Node(Token.STRING));
        node.addChildToBack(firstChild);

        prepareAnnotations.annotateCalls(node);

        assertEquals(Token.CALL, node.getType());
        assertTrue(node.getBooleanProp(Node.FREE_CALL));
    }

    @Test
    public void testAnnotateDispatchers() {
        PrepareAst.PrepareAnnotations prepareAnnotations = new PrepareAst.PrepareAnnotations();
        Node node = new Node(Token.FUNCTION);
        Node parent = new Node(Token.ASSIGN);
        parent.addChildToBack(node);

        prepareAnnotations.annotateDispatchers(node, parent);

        assertEquals(Token.FUNCTION, node.getType());
        assertTrue(node.getBooleanProp(Node.IS_DISPATCHER));
    }

    @Test
    public void testNormalizeObjectLiteralKeyAnnotations() {
        PrepareAst.PrepareAnnotations prepareAnnotations = new PrepareAst.PrepareAnnotations();
        Node objlit = new Node(Token.OBJECTLIT);
        Node key = new Node(Token.STRING);
        Node value = new Node(Token.FUNCTION);
        objlit.addChildToBack(key);
        key.addChildToBack(value);

        prepareAnnotations.normalizeObjectLiteralKeyAnnotations(objlit, key, value);

        assertEquals(Token.OBJECTLIT, objlit.getType());
        assertEquals(Token.STRING, key.getType());
        assertEquals(Token.FUNCTION, value.getType());
    }
}
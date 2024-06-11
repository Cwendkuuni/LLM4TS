import org.junit.Test;
import static org.junit.Assert.*;

public class CheckSideEffectsTest {

    @Test
    public void testEmptyNode() {
        AbstractCompiler compiler = new AbstractCompiler();
        CheckSideEffects checkSideEffects = new CheckSideEffects(compiler, CheckLevel.WARNING, false);

        NodeTraversal traversal = new NodeTraversal(compiler, checkSideEffects);
        Node node = new Node(Token.EMPTY);
        Node parent = new Node(Token.EMPTY);
        traversal.visit(null, node, parent);
    }

    @Test
    public void testCommaNode() {
        AbstractCompiler compiler = new AbstractCompiler();
        CheckSideEffects checkSideEffects = new CheckSideEffects(compiler, CheckLevel.WARNING, false);

        NodeTraversal traversal = new NodeTraversal(compiler, checkSideEffects);
        Node node = new Node(Token.COMMA);
        Node parent = new Node(Token.COMMA);
        traversal.visit(null, node, parent);
    }

    @Test
    public void testQualifiedNameNodeWithJSDoc() {
        AbstractCompiler compiler = new AbstractCompiler();
        CheckSideEffects checkSideEffects = new CheckSideEffects(compiler, CheckLevel.WARNING, false);

        NodeTraversal traversal = new NodeTraversal(compiler, checkSideEffects);
        Node node = new Node(Token.NAME, "test");
        node.setJSDocInfo(new JSDocInfo());
        Node parent = new Node(Token.NAME, "parent");
        traversal.visit(null, node, parent);
    }

    @Test
    public void testQualifiedNameNodeWithoutJSDoc() {
        AbstractCompiler compiler = new AbstractCompiler();
        CheckSideEffects checkSideEffects = new CheckSideEffects(compiler, CheckLevel.WARNING, false);

        NodeTraversal traversal = new NodeTraversal(compiler, checkSideEffects);
        Node node = new Node(Token.NAME, "test");
        Node parent = new Node(Token.NAME, "parent");
        traversal.visit(null, node, parent);
    }

    @Test
    public void testExprResultNode() {
        AbstractCompiler compiler = new AbstractCompiler();
        CheckSideEffects checkSideEffects = new CheckSideEffects(compiler, CheckLevel.WARNING, false);

        NodeTraversal traversal = new NodeTraversal(compiler, checkSideEffects);
        Node node = new Node(Token.EXPR_RESULT);
        Node parent = new Node(Token.EXPR_RESULT);
        traversal.visit(null, node, parent);
    }

    @Test
    public void testEmptyParent() {
        AbstractCompiler compiler = new AbstractCompiler();
        CheckSideEffects checkSideEffects = new CheckSideEffects(compiler, CheckLevel.WARNING, false);

        NodeTraversal traversal = new NodeTraversal(compiler, checkSideEffects);
        Node node = new Node(Token.ADD, new Node(Token.STRING, "string1"), new Node(Token.STRING, "string2"));
        Node parent = null;
        traversal.visit(null, node, parent);
    }

    @Test
    public void testStringNode() {
        AbstractCompiler compiler = new AbstractCompiler();
        CheckSideEffects checkSideEffects = new CheckSideEffects(compiler, CheckLevel.WARNING, false);

        NodeTraversal traversal = new NodeTraversal(compiler, checkSideEffects);
        Node node = new Node(Token.STRING, "string");
        Node parent = new Node(Token.NAME, "parent");
        traversal.visit(null, node, parent);
    }

    @Test
    public void testSimpleOpNode() {
        AbstractCompiler compiler = new AbstractCompiler();
        CheckSideEffects checkSideEffects = new CheckSideEffects(compiler, CheckLevel.WARNING, false);

        NodeTraversal traversal = new NodeTraversal(compiler, checkSideEffects);
        Node node = new Node(Token.ADD, new Node(Token.NUMBER, 1), new Node(Token.NUMBER, 2));
        Node parent = new Node(Token.NAME, "parent");
        traversal.visit(null, node, parent);
    }

    @Test
    public void testStatementNode() {
        AbstractCompiler compiler = new AbstractCompiler();
        CheckSideEffects checkSideEffects = new CheckSideEffects(compiler, CheckLevel.WARNING, false);

        NodeTraversal traversal = new NodeTraversal(compiler, checkSideEffects);
        Node node = new Node(Token.VAR, new Node(Token.NAME, "x"), new Node(Token.NUMBER, 10));
        Node parent = new Node(Token.BLOCK);
        traversal.visit(null, node, parent);
    }

    @Test
    public void testForNode() {
        AbstractCompiler compiler = new AbstractCompiler();
        CheckSideEffects checkSideEffects = new CheckSideEffects(compiler, CheckLevel.WARNING, false);

        NodeTraversal traversal = new NodeTraversal(compiler, checkSideEffects);
        Node node = new Node(Token.GETPROP, new Node(Token.NAME, "x"), new Node(Token.STRING, "y"));
        Node parent = new Node(Token.FOR, new Node(Token.NAME, "var"), new Node(Token.ASSIGN, new Node(Token.NAME, "i"), new Node(Token.NUMBER, 1)), new Node(Token.LE, new Node(Token.NAME, "i"), new Node(Token.NUMBER, 10)), new Node(Token.INC, new Node(Token.NAME, "i")));
        parent.addChildToFront(node);

        node = new Node(Token.ADD, new Node(Token.STRING, "string1"), new Node(Token.STRING, "string2"));
        parent.addChildToBack(node);

        traversal.visit(null, node, parent);
    }
}
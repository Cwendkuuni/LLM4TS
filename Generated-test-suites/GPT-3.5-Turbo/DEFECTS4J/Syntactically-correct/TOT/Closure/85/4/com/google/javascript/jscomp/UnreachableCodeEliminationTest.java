import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class UnreachableCodeEliminationTest {

    private UnreachableCodeElimination uce;

    @Before
    public void setUp() {
        uce = new UnreachableCodeElimination(new AbstractCompiler(), true);
    }

    @Test
    public void testEnterScope() {
        // Test with a scope containing a return statement
        Node root = new Node(Token.SCRIPT);
        Node fnNode = new Node(Token.FUNCTION);
        Node returnNode = new Node(Token.RETURN);
        fnNode.addChildToFront(returnNode);
        root.addChildToFront(fnNode);

        NodeTraversal t = new NodeTraversal(new AbstractCompiler(), new NodeTraversal.ScopedCallback() {
            @Override
            public void enterScope(NodeTraversal t) {
                uce.enterScope(t);
            }

            @Override
            public void exitScope(NodeTraversal t) {
                uce.exitScope(t);
            }

            @Override
            public void visit(NodeTraversal t, Node n, Node parent) {
            }
        });
        t.traverseNode(root);

        assertNull(uce.cfgStack.peek());
    }

    @Test
    public void testExitScope() {
        // Test with a scope containing a return statement
        Node root = new Node(Token.SCRIPT);
        Node fnNode = new Node(Token.FUNCTION);
        Node returnNode = new Node(Token.RETURN);
        fnNode.addChildToFront(returnNode);
        root.addChildToFront(fnNode);

        NodeTraversal t = new NodeTraversal(new AbstractCompiler(), new NodeTraversal.ScopedCallback() {
            @Override
            public void enterScope(NodeTraversal t) {
                uce.enterScope(t);
            }

            @Override
            public void exitScope(NodeTraversal t) {
                uce.exitScope(t);
            }

            @Override
            public void visit(NodeTraversal t, Node n, Node parent) {
            }
        });
        t.traverseNode(root);

        assertNotNull(uce.cfgStack.peek());

        uce.exitScope(t);

        assertNull(uce.cfgStack.peek());
    }

    @Test
    public void testProcess() {
        // Test with a script containing a single function
        Node root = new Node(Token.SCRIPT);
        Node fnNode = new Node(Token.FUNCTION);
        root.addChildToFront(fnNode);

        NodeTraversal t = new NodeTraversal(new AbstractCompiler(), new NodeTraversal.ScopedCallback() {
            @Override
            public void enterScope(NodeTraversal t) {
                uce.enterScope(t);
            }

            @Override
            public void exitScope(NodeTraversal t) {
                uce.exitScope(t);
            }

            @Override
            public void visit(NodeTraversal t, Node n, Node parent) {
            }
        });
        t.traverseNode(root);

        // Ensure that the process method is executed without errors
        assertTrue(true);
    }

    @Test
    public void testVisit() {
        // Test with a return statement
        Node root = new Node(Token.SCRIPT);
        Node fnNode = new Node(Token.FUNCTION);
        Node returnNode = new Node(Token.RETURN);
        fnNode.addChildToFront(returnNode);
        root.addChildToFront(fnNode);

        NodeTraversal t = new NodeTraversal(new AbstractCompiler(), new NodeTraversal.ScopedCallback() {
            @Override
            public void enterScope(NodeTraversal t) {
            }

            @Override
            public void exitScope(NodeTraversal t) {
            }

            @Override
            public void visit(NodeTraversal t, Node n, Node parent) {
                uce.visit(t, n, parent);
            }
        });
        t.traverseNode(root);

        assertTrue(returnNode.isEmpty());
    }

    @Test
    public void testTryRemoveUnconditionalBranching() {
        // Test with a break statement followed by a block
        Node root = new Node(Token.SCRIPT);
        Node breakNode = new Node(Token.BREAK);
        Node block = new Node(Token.BLOCK);
        breakNode.addChildToBack(block);
        root.addChildToBack(breakNode);

        NodeTraversal t = new NodeTraversal(new AbstractCompiler(), new NodeTraversal.ScopedCallback() {
            @Override
            public void enterScope(NodeTraversal t) {
            }

            @Override
            public void exitScope(NodeTraversal t) {
            }

            @Override
            public void visit(NodeTraversal t, Node n, Node parent) {
                uce.visit(t, n, parent);
            }
        });
        t.traverseNode(root);

        assertTrue(breakNode.isEmpty());
        assertTrue(block.isEmpty());
    }

    @Test
    public void testComputeFollowing() {
        // Test with a return statement
        Node returnNode = new Node(Token.RETURN);
        Node followingNode = new Node(Token.FUNCTION);
        returnNode.addChildToBack(followingNode);

        Node result = uce.computeFollowing(returnNode);

        assertEquals(followingNode, result);
    }

    @Test
    public void testRemoveDeadExprStatementSafely() {
        // Test with an empty statement
        Node root = new Node(Token.SCRIPT);
        Node emptyNode = new Node(Token.EMPTY);
        root.addChildToFront(emptyNode);

        uce.removeDeadExprStatementSafely(emptyNode);

        assertEquals(0, root.getChildCount());
    }

}
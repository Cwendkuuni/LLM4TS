import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.rhino.JSDocInfo;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.Token;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class CollapseVariableDeclarationsTest {

    private CollapseVariableDeclarations collapseVariableDeclarations;
    private AbstractCompiler compiler;

    @Before
    public void setUp() {
        compiler = new AbstractCompiler();
        collapseVariableDeclarations = new CollapseVariableDeclarations(compiler);
    }

    @Test
    public void testProcess_multipleVariableDeclarations_success() {
        Node root = createMockRootNode();
        collapseVariableDeclarations.process(null, root);

        // Assert that multiple variable declarations have been collapsed into one
        assertEquals(1, countVariableDeclarationNodes(root));
    }

    @Test
    public void testProcess_multipleVariableAssignments_success() {
        Node root = createMockRootNode();
        collapseVariableDeclarations.process(null, root);

        // Assert that multiple variable assignments have been collapsed into one
        assertEquals(1, countVariableAssignmentNodes(root));
    }

    @Test
    public void testProcess_noVariableDeclarationsOrAssignments_noChange() {
        Node root = new Node(Token.ROOT);
        collapseVariableDeclarations.process(null, root);

        // Assert that no change has been made to the parse tree
        assertEquals(0, countVariableDeclarationNodes(root));
        assertEquals(0, countVariableAssignmentNodes(root));
    }

    private Node createMockRootNode() {
        Node root = new Node(Token.ROOT);

        Node varNode1 = createMockVariableDeclarationNode();
        Node varNode2 = createMockVariableDeclarationNode();

        Node assignNode1 = createMockVariableAssignmentNode();
        Node assignNode2 = createMockVariableAssignmentNode();

        root.addChildToBack(varNode1);
        root.addChildToBack(varNode2);
        root.addChildToBack(assignNode1);
        root.addChildToBack(assignNode2);

        return root;
    }

    private Node createMockVariableDeclarationNode() {
        Node varNode = new Node(Token.VAR);
        varNode.addChildToBack(createMockNameNode());

        return varNode;
    }

    private Node createMockVariableAssignmentNode() {
        Node assignNode = new Node(Token.ASSIGN);
        assignNode.addChildToBack(createMockNameNode());
        assignNode.addChildToBack(createMockTrueNode());

        Node varNode = new Node(Token.VAR);
        varNode.addChildToBack(assignNode);

        return varNode;
    }

    private Node createMockNameNode() {
        Node nameNode = new Node(Token.NAME);
        nameNode.setString("x");

        return nameNode;
    }

    private Node createMockTrueNode() {
        Node trueNode = new Node(Token.TRUE);

        return trueNode;
    }

    private int countVariableDeclarationNodes(Node node) {
        int count = 0;
        for (Node child = node.getFirstChild(); child != null; child = child.getNext()) {
            if (child.isVar()) {
                count++;
            }
            count += countVariableDeclarationNodes(child);
        }
        return count;
    }

    private int countVariableAssignmentNodes(Node node) {
        int count = 0;
        for (Node child = node.getFirstChild(); child != null; child = child.getNext()) {
            if (child.isAssign() && child.getParent().isVar()) {
                count++;
            }
            count += countVariableAssignmentNodes(child);
        }
        return count;
    }
}
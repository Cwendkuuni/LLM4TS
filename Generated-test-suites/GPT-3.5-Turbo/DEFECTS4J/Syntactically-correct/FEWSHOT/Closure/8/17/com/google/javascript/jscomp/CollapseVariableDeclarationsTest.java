import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class CollapseVariableDeclarationsTest {

    @Test
    public void testProcess() {
        // Create a mock compiler and instantiate the CollapseVariableDeclarations
        AbstractCompiler compiler = new AbstractCompiler() {
            @Override
            public LifeCycleStage getLifeCycleStage() {
                return LifeCycleStage.RAW;
            }
            // Override other required compiler methods
        };
        CollapseVariableDeclarations collapseVariableDeclarations = new CollapseVariableDeclarations(compiler);

        // Create a mock externs and root node
        Node externs = new Node(Token.BLOCK);
        Node root = new Node(Token.BLOCK);

        // Add test code to the root node
        Node varNode1 = createVarNode(Token.NAME, "a", null);
        varNode1.addChildToFront(createVarNode(Token.EMPTY, null, null));
        Node varNode2 = createVarNode(Token.NAME, "b", null);
        varNode2.addChildToFront(createVarNode(Token.STRING, null, "1"));
        Node varNode3 = createVarNode(Token.NAME, "c", null);
        varNode3.addChildToFront(createVarNode(Token.STRING, null, "2"));
        root.addChildToBack(varNode1);
        root.addChildToBack(varNode2);
        root.addChildToBack(varNode3);

        // Call the process method
        collapseVariableDeclarations.process(externs, root);

        // Verify the output
        Node varNode = root.getFirstChild();
        assertTrue(varNode.isVar());
        Node childNode1 = varNode.getFirstChild();
        assertTrue(childNode1.isEmpty());
        Node childNode2 = childNode1.getNext();
        assertTrue(childNode2.isVar());
        Node grandchildNode1 = childNode2.getFirstChild();
        assertTrue(grandchildNode1.isString());
        assertEquals("1", grandchildNode1.getString());
        Node childNode3 = childNode2.getNext();
        assertTrue(childNode3.isVar());
        Node grandchildNode2 = childNode3.getFirstChild();
        assertTrue(grandchildNode2.isString());
        assertEquals("2", grandchildNode2.getString());
    }

    private Node createVarNode(Token type, String name, String value) {
        Node varNode = new Node(Token.NAME);
        if (type != null) {
            varNode.setType(type);
        }
        if (name != null) {
            varNode.setString(name);
        }
        if (value != null) {
            varNode.setString(value);
        }
        return varNode;
    }
}
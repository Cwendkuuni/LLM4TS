import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class CheckSideEffectsTest {

    @Test
    public void testProcess_NoSideEffects_ReturnsEmptyList() {
        // Arrange
        AbstractCompiler compiler = new AbstractCompiler();
        CheckLevel level = CheckLevel.WARNING;
        boolean protectSideEffectFreeCode = true;
        CheckSideEffects checkSideEffects = new CheckSideEffects(compiler, level, protectSideEffectFreeCode);
        Node externs = new Node();
        Node root = new Node();

        // Act
        checkSideEffects.process(externs, root);
        List<Node> problemNodes = checkSideEffects.getProblemNodes();

        // Assert
        assertEquals(0, problemNodes.size());
    }

    @Test
    public void testProcess_SideEffect_ReturnsWarning() {
        // Arrange
        AbstractCompiler compiler = new AbstractCompiler();
        CheckLevel level = CheckLevel.WARNING;
        boolean protectSideEffectFreeCode = true;
        CheckSideEffects checkSideEffects = new CheckSideEffects(compiler, level, protectSideEffectFreeCode);
        Node externs = new Node();
        Node root = new Node();
        Node node = new Node(Token.STRING);

        // Act
        root.addChildToFront(node);
        checkSideEffects.process(externs, root);
        List<Node> problemNodes = checkSideEffects.getProblemNodes();

        // Assert
        assertEquals(1, problemNodes.size());
        assertEquals(node, problemNodes.get(0));
    }

    @Test
    public void testHotSwapScript_NoSideEffects_ReturnsEmptyList() {
        // Arrange
        AbstractCompiler compiler = new AbstractCompiler();
        CheckLevel level = CheckLevel.WARNING;
        boolean protectSideEffectFreeCode = true;
        CheckSideEffects checkSideEffects = new CheckSideEffects(compiler, level, protectSideEffectFreeCode);
        Node scriptRoot = new Node();
        Node originalRoot = new Node();

        // Act
        checkSideEffects.hotSwapScript(scriptRoot, originalRoot);
        List<Node> problemNodes = checkSideEffects.getProblemNodes();

        // Assert
        assertEquals(0, problemNodes.size());
    }

    @Test
    public void testHotSwapScript_SideEffect_ReturnsWarning() {
        // Arrange
        AbstractCompiler compiler = new AbstractCompiler();
        CheckLevel level = CheckLevel.WARNING;
        boolean protectSideEffectFreeCode = true;
        CheckSideEffects checkSideEffects = new CheckSideEffects(compiler, level, protectSideEffectFreeCode);
        Node scriptRoot = new Node();
        Node originalRoot = new Node();
        Node node = new Node(Token.STRING);

        // Act
        scriptRoot.addChildToFront(node);
        checkSideEffects.hotSwapScript(scriptRoot, originalRoot);
        List<Node> problemNodes = checkSideEffects.getProblemNodes();

        // Assert
        assertEquals(1, problemNodes.size());
        assertEquals(node, problemNodes.get(0));
    }

    @Test
    public void testVisit_NoSideEffects_ReturnsEmptyList() {
        // Arrange
        AbstractCompiler compiler = new AbstractCompiler();
        CheckLevel level = CheckLevel.WARNING;
        boolean protectSideEffectFreeCode = true;
        CheckSideEffects checkSideEffects = new CheckSideEffects(compiler, level, protectSideEffectFreeCode);
        NodeTraversal traversal = new NodeTraversal();

        // Act
        checkSideEffects.visit(traversal, new Node(), null);
        List<Node> problemNodes = checkSideEffects.getProblemNodes();

        // Assert
        assertEquals(0, problemNodes.size());
    }

    @Test
    public void testVisit_SideEffect_ReturnsWarning() {
        // Arrange
        AbstractCompiler compiler = new AbstractCompiler();
        CheckLevel level = CheckLevel.WARNING;
        boolean protectSideEffectFreeCode = true;
        CheckSideEffects checkSideEffects = new CheckSideEffects(compiler, level, protectSideEffectFreeCode);
        NodeTraversal traversal = new NodeTraversal();
        Node node = new Node(Token.STRING);
        Node parent = new Node(Token.STRING);

        // Act
        checkSideEffects.visit(traversal, node, parent);
        List<Node> problemNodes = checkSideEffects.getProblemNodes();

        // Assert
        assertEquals(1, problemNodes.size());
        assertEquals(node, problemNodes.get(0));
    }
}
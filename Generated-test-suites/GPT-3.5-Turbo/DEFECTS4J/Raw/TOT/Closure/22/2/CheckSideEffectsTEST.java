import org.junit.Test;
import static org.junit.Assert.*;

public class CheckSideEffectsTest {

    @Test
    public void testProcess() {
        // Test case for the process method
        // Typical use case: No problem nodes
        // Expected output: No warnings
        AbstractCompiler compiler = new AbstractCompiler();
        CheckLevel level = CheckLevel.WARNING;
        boolean protectSideEffectFreeCode = true;
        CheckSideEffects cse = new CheckSideEffects(compiler, level, protectSideEffectFreeCode);
        Node externs = new Node();
        Node root = new Node();
        cse.process(externs, root);
        // Check that no warnings were generated
        assertEquals(0, compiler.getWarnings().size());
    }

    @Test
    public void testHotSwapScript() {
        // Test case for the hotSwapScript method
        // Typical use case: No problem nodes
        // Expected output: No warnings
        AbstractCompiler compiler = new AbstractCompiler();
        CheckLevel level = CheckLevel.WARNING;
        boolean protectSideEffectFreeCode = true;
        CheckSideEffects cse = new CheckSideEffects(compiler, level, protectSideEffectFreeCode);
        Node scriptRoot = new Node();
        Node originalRoot = new Node();
        cse.hotSwapScript(scriptRoot, originalRoot);
        // Check that no warnings were generated
        assertEquals(0, compiler.getWarnings().size());
    }

    @Test
    public void testVisit() {
        // Test case for the visit method
        // Typical use case: Node with side effects
        // Expected output: Warning generated
        AbstractCompiler compiler = new AbstractCompiler();
        CheckLevel level = CheckLevel.WARNING;
        boolean protectSideEffectFreeCode = true;
        CheckSideEffects cse = new CheckSideEffects(compiler, level, protectSideEffectFreeCode);

        // Create a parent node and a child node with side effects
        Node parent = new Node(Token.EXPR_RESULT);
        Node child = new Node(Token.NAME);
        child.putBooleanProp(Node.IS_CONSTANT_NAME, true);
        parent.addChildToFront(child);

        // Call the visit method
        NodeTraversal t = new NodeTraversal(compiler, null);
        cse.visit(t, child, parent);

        // Check that a warning was generated
        assertEquals(1, compiler.getWarnings().size());
        assertEquals(CheckSideEffects.USELESS_CODE_ERROR, compiler.getWarnings().get(0).getType());
    }

    @Test
    public void testProtectSideEffects() {
        // Test case for the protectSideEffects method
        // Typical use case: Problem nodes exist
        // Expected output: Problem nodes are protected

        // Create a problem node
        Node problemNode = new Node(Token.EXPR_RESULT);
        Node child = new Node(Token.NAME);
        child.putBooleanProp(Node.IS_CONSTANT_NAME, true);
        problemNode.addChildToFront(child);
        List<Node> problemNodes = new ArrayList<>();
        problemNodes.add(problemNode);

        AbstractCompiler compiler = new AbstractCompiler();
        CheckLevel level = CheckLevel.WARNING;
        boolean protectSideEffectFreeCode = true;
        CheckSideEffects cse = new CheckSideEffects(compiler, level, protectSideEffectFreeCode);
        cse.setProblemNodes(problemNodes);

        // Create the expected protected node
        Node protectedNode = new Node(Token.CALL);
        Node name = new Node(Token.NAME);
        name.setString(CheckSideEffects.PROTECTOR_FN);
        name.putBooleanProp(Node.IS_CONSTANT_NAME, true);
        protectedNode.addChildToFront(name);
        protectedNode.addChildToBack(problemNode);

        // Call the protectSideEffects method
        cse.protectSideEffects();

        // Check that the problem node was replaced with the protected node
        Node expectedRoot = new Node();
        expectedRoot.addChildToBack(protectedNode);
        assertEquals(expectedRoot.toString(), problemNode.getParent().toString());
    }

    @Test
    public void testAddExtern() {
        // Test case for the addExtern method
        // Expected output: Extern added to the compiler

        AbstractCompiler compiler = new AbstractCompiler();
        CheckLevel level = CheckLevel.WARNING;
        boolean protectSideEffectFreeCode = true;
        CheckSideEffects cse = new CheckSideEffects(compiler, level, protectSideEffectFreeCode);

        // Call the addExtern method
        cse.addExtern();

        // Check that the extern was added to the compiler
        Node scripts = compiler.getSynthesizedExternsInput().getAstRoot(compiler);
        Node extern = scripts.getFirstChild();
        assertNotNull(extern);
        assertEquals(Token.VAR, extern.getType());
        assertEquals(CheckSideEffects.PROTECTOR_FN, extern.getFirstChild().getString());
    }

    @Test
    public void testStripProtection() {
        // Test case for the StripProtection class
        // Typical use case: Call node with protected function name
        // Expected output: Protected node is replaced with the expression node

        // Create a call node with the protected function name
        Node callNode = new Node(Token.CALL);
        Node name = new Node(Token.NAME);
        name.setString(CheckSideEffects.PROTECTOR_FN);
        name.putBooleanProp(Node.IS_CONSTANT_NAME, true);
        callNode.addChildToFront(name);
        Node expr = new Node(Token.EXPRESSION_RESULT);
        callNode.addChildToBack(expr);

        AbstractCompiler compiler = new AbstractCompiler();
        CheckSideEffects.StripProtection sp = new CheckSideEffects.StripProtection(compiler);

        // Call the visit method of the StripProtection class
        NodeTraversal t = new NodeTraversal(compiler, null);
        sp.visit(t, callNode, null);

        // Check that the protected node was replaced with the expression node
        assertNotNull(callNode.getParent());
        assertEquals(expr.toString(), callNode.getParent().toString());
    }
}
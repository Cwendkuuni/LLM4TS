import org.junit.Test;
import static org.junit.Assert.*;

public class CheckSideEffectsTest {

	@Test
	public void testProcess_WithSideEffectStatements_ShouldGenerateWarnings() {
		// Setup
		AbstractCompiler compiler = new AbstractCompiler();
		CheckLevel level = CheckLevel.WARNING;
		boolean protectSideEffectFreeCode = true;
		CheckSideEffects checkSideEffects = new CheckSideEffects(compiler, level, protectSideEffectFreeCode);
		Node externs = null;
		Node root = new Node(Token.ROOT);
		
		// Exercise
		checkSideEffects.process(externs, root);
		
		// Verify
		// TODO: Verify the warnings generated
	}

	@Test
	public void testHotSwapScript_WithSideEffectStatements_ShouldGenerateWarnings() {
		// Setup
		AbstractCompiler compiler = new AbstractCompiler();
		CheckLevel level = CheckLevel.WARNING;
		boolean protectSideEffectFreeCode = true;
		CheckSideEffects checkSideEffects = new CheckSideEffects(compiler, level, protectSideEffectFreeCode);
		Node scriptRoot = new Node(Token.SCRIPT);
		Node originalRoot = new Node(Token.ROOT);
		
		// Exercise
		checkSideEffects.hotSwapScript(scriptRoot, originalRoot);
		
		// Verify
		// TODO: Verify the warnings generated
	}

	@Test
	public void testVisit_WithSideEffectStatements_ShouldGenerateWarnings() {
		// Setup
		AbstractCompiler compiler = new AbstractCompiler();
		CheckLevel level = CheckLevel.WARNING;
		boolean protectSideEffectFreeCode = true;
		CheckSideEffects checkSideEffects = new CheckSideEffects(compiler, level, protectSideEffectFreeCode);
		NodeTraversal t = new NodeTraversal(compiler, null);
		Node n = new Node(Token.EXPR_RESULT);
		Node parent = new Node(Token.SCRIPT);
		
		// Exercise
		checkSideEffects.visit(t, n, parent);
		
		// Verify
		// TODO: Verify the warnings generated
	}

	@Test
	public void testProtectSideEffects_WithSideEffectStatements_ShouldProtectStatements() {
		// Setup
		AbstractCompiler compiler = new AbstractCompiler();
		CheckLevel level = CheckLevel.WARNING;
		boolean protectSideEffectFreeCode = true;
		CheckSideEffects checkSideEffects = new CheckSideEffects(compiler, level, protectSideEffectFreeCode);
		List<Node> problemNodes = new ArrayList<>();
		Node node1 = new Node(Token.STRING);
		Node node2 = new Node(Token.NAME);
		problemNodes.add(node1);
		problemNodes.add(node2);
		checkSideEffects.setProblemNodes(problemNodes);
		
		// Exercise
		checkSideEffects.protectSideEffects();
		
		// Verify
		// TODO: Verify that the statements are protected
	}

	@Test
	public void testAddExtern_ShouldAddExternFunctionToCompiler() {
		// Setup
		AbstractCompiler compiler = new AbstractCompiler();
		CheckSideEffects checkSideEffects = new CheckSideEffects(compiler, null, false);
		
		// Exercise
		checkSideEffects.addExtern();
		
		// Verify
		// TODO: Verify that the extern function is added to the compiler
	}

	@Test
	public void testStripProtection_WithProtectedStatements_ShouldRemoveProtection() {
		// Setup
		AbstractCompiler compiler = new AbstractCompiler();
		CheckSideEffects.StripProtection stripProtection = new CheckSideEffects.StripProtection(compiler);
		Node externs = new Node(Token.SCRIPT);
		Node root = new Node(Token.ROOT);
		Node node1 = new Node(Token.CALL);
		Node node2 = new Node(Token.STRING);
		node1.addChildToFront(node2);
		root.addChildToFront(node1);
		
		// Exercise
		stripProtection.process(externs, root);
		
		// Verify
		// TODO: Verify that the protection is removed from the statements
	}

}
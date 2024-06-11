import org.junit.Test;

public class CheckSideEffectsTest {

    @Test
    public void testProcess_NoSideEffects_NoWarnings() {
        // Arrange
        CheckSideEffects checkSideEffects = new CheckSideEffects();
        AbstractCompiler compiler = new AbstractCompiler();
        Node externs = new Node();
        Node root = new Node();

        // Act
        checkSideEffects.process(externs, root);

        // Assert
        // No warnings should be generated
        assertEquals(0, compiler.getWarningCount());
    }
    
    @Test
    public void testProcess_MissingPlusInString_WarningGenerated() {
        // Arrange
        CheckSideEffects checkSideEffects = new CheckSideEffects();
        AbstractCompiler compiler = new AbstractCompiler();
        Node externs = new Node();
        Node root = new Node();

        // Create a node for the problematic string concatenation
        Node stringNode = new Node(Token.STRING, "this string is ");
        stringNode.addChildToBack(new Node(Token.STRING, "continued on the next line but you forgot the +"));
        
        // Add the problematic string concatenation to the root node
        root.addChildToBack(stringNode);

        // Act
        checkSideEffects.process(externs, root);

        // Assert
        // A warning should be generated
        assertEquals(1, compiler.getWarningCount());
        Warning warning = compiler.getWarnings().get(0);
        assertEquals("Suspicious code. Is there a missing '+' on the previous line?", warning.getMessage());
    }
    
    @Test
    public void testProcess_UnusedOperatorResult_WarningGenerated() {
        // Arrange
        CheckSideEffects checkSideEffects = new CheckSideEffects();
        AbstractCompiler compiler = new AbstractCompiler();
        Node externs = new Node();
        Node root = new Node();

        // Create a node for the problematic operator
        Node operatorNode = new Node(Token.GT, new Node(Token.NAME, "x"), new Node(Token.NAME, "y"));
        
        // Add the problematic operator to the root node
        root.addChildToBack(operatorNode);

        // Act
        checkSideEffects.process(externs, root);

        // Assert
        // A warning should be generated
        assertEquals(1, compiler.getWarningCount());
        Warning warning = compiler.getWarnings().get(0);
        assertEquals("The result of the '>' operator is not being used.", warning.getMessage());
    }
    
    @Test
    public void testProcess_EvalCallInCommaExpression_NoWarningGenerated() {
        // Arrange
        CheckSideEffects checkSideEffects = new CheckSideEffects();
        AbstractCompiler compiler = new AbstractCompiler();
        Node externs = new Node();
        Node root = new Node();

        // Create a node for the problematic eval call in a comma expression
        Node evalNode = new Node(Token.NAME, "eval");
        Node commaExpressionNode = new Node(Token.COMMA, evalNode, new Node(Token.NAME, "x"));
        
        // Add the problematic eval call to the root node
        root.addChildToBack(commaExpressionNode);

        // Act
        checkSideEffects.process(externs, root);

        // Assert
        // No warnings should be generated as eval calls are not checked
        assertEquals(0, compiler.getWarningCount());
    }
    
    @Test
    public void testProtectSideEffects_SideEffectFreeNode_NodeProtected() {
        // Arrange
        CheckSideEffects checkSideEffects = new CheckSideEffects();
        AbstractCompiler compiler = new AbstractCompiler();
        Node externs = new Node();
        Node root = new Node();
        checkSideEffects.process(externs, root); // Generate one warning to protect

        // Create a side-effect free node for protecting
        Node sideEffectFreeNode = new Node(Token.STRING, "This is a side-effect free node.");

        // Add the side-effect free node to the problemNodes list
        List<Node> problemNodes = checkSideEffects.getProblemNodes();
        problemNodes.add(sideEffectFreeNode);

        // Act
        checkSideEffects.protectSideEffects();

        // Assert
        // The side-effect free node should be replaced with a call to the protector function
        Node expectedReplacement = new Node(Token.CALL, new Node(Token.NAME, "JSCOMPILER_PRESERVE"));
        expectedReplacement.addChildToBack(sideEffectFreeNode);
        assertEquals(expectedReplacement, sideEffectFreeNode.getParent());
    }
    
    @Test
    public void testStripProtection_ProtectedCallNode_CallNodeStripped() {
        // Arrange
        CheckSideEffects.StripProtection stripProtection = new CheckSideEffects.StripProtection();
        AbstractCompiler compiler = new AbstractCompiler();
        Node externs = new Node();
        Node root = new Node();

        // Create a protected call node
        Node protectedCallNode = new Node(Token.CALL, new Node(Token.NAME, "JSCOMPILER_PRESERVE"));

        // Add the protected call node to the root node
        root.addChildToBack(protectedCallNode);

        // Act
        stripProtection.process(externs, root);

        // Assert
        // The protected call node should be replaced with its child node
        Node expectedChild = protectedCallNode.getFirstChild();
        assertEquals(expectedChild, protectedCallNode.getParent());
    }

}
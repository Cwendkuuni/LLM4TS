import org.junit.Test;

import static org.junit.Assert.*;

public class CheckSideEffectsTest {

    @Test
    public void testProcess_WhenGivenEmptyScript_ShouldNotGenerateWarning() {
        // Arrange
        CheckSideEffects checkSideEffects = new CheckSideEffects(null, null, false);
        Node externs = new Node(Token.ROOT);
        Node root = new Node(Token.ROOT);
        
        // Act
        checkSideEffects.process(externs, root);
        
        // Assert
        // No warnings should be generated
        assertEquals(0, checkSideEffects.compiler.getWarnings().size());
    }

    @Test
    public void testProcess_WhenGivenScriptWithUselessCode_ShouldGenerateWarning() {
        // Arrange
        CheckSideEffects checkSideEffects = new CheckSideEffects(null, null, false);
        Node externs = new Node(Token.ROOT);
        Node root = new Node(Token.ROOT);
        
        // Create a node representing the useless code
        Node uselessCode = new Node(Token.ADD,
                                   new Node(Token.STRING, "The result is: "),
                                   new Node(Token.STRING, "4"));
        Node exprResult = new Node(Token.EXPR_RESULT, uselessCode);
        root.addChildToBack(exprResult);
        
        // Act
        checkSideEffects.process(externs, root);
        
        // Assert
        // One warning should be generated
        assertEquals(1, checkSideEffects.compiler.getWarnings().size());
    }

    @Test
    public void testHotSwapScript_WhenGivenScriptWithUselessCode_ShouldGenerateWarning() {
        // Arrange
        CheckSideEffects checkSideEffects = new CheckSideEffects(null, null, false);
        Node scriptRoot = new Node(Token.ROOT);
        Node originalRoot = null;
        
        // Create a node representing the useless code
        Node uselessCode = new Node(Token.ASSIGN,
                                   new Node(Token.NAME, "x"),
                                   new Node(Token.NUMBER, "42"));
        Node exprResult = new Node(Token.EXPR_RESULT, uselessCode);
        scriptRoot.addChildToBack(exprResult);
        
        // Act
        checkSideEffects.hotSwapScript(scriptRoot, originalRoot);
        
        // Assert
        // One warning should be generated
        assertEquals(1, checkSideEffects.compiler.getWarnings().size());
    }

    @Test
    public void testVisit_WhenGivenNullParent_ShouldDoNothing() {
        // Arrange
        CheckSideEffects checkSideEffects = new CheckSideEffects(null, null, false);
        NodeTraversal t = null;
        Node n = null;
        Node parent = null;
        
        // Act
        checkSideEffects.visit(t, n, parent);
        
        // Assert
        // No warnings should be generated
        assertEquals(0, checkSideEffects.compiler.getWarnings().size());
    }

    @Test
    public void testVisit_WhenGivenEmptyNode_ShouldDoNothing() {
        // Arrange
        CheckSideEffects checkSideEffects = new CheckSideEffects(null, null, false);
        NodeTraversal t = null;
        Node n = new Node(Token.EMPTY);
        Node parent = new Node(Token.EXPR_RESULT);
        
        // Act
        checkSideEffects.visit(t, n, parent);
        
        // Assert
        // No warnings should be generated
        assertEquals(0, checkSideEffects.compiler.getWarnings().size());
    }

    @Test
    public void testVisit_WhenGivenCommaNode_ShouldDoNothing() {
        // Arrange
        CheckSideEffects checkSideEffects = new CheckSideEffects(null, null, false);
        NodeTraversal t = null;
        Node n = new Node(Token.COMMA);
        Node parent = null;
        
        // Act
        checkSideEffects.visit(t, n, parent);
        
        // Assert
        // No warnings should be generated
        assertEquals(0, checkSideEffects.compiler.getWarnings().size());
    }

    @Test
    public void testVisit_WhenGivenQualifiedNameWithJSDocInfo_ShouldDoNothing() {
        // Arrange
        CheckSideEffects checkSideEffects = new CheckSideEffects(null, null, false);
        NodeTraversal t = null;
        Node n = new Node(Token.NAME, "example");
        Node parent = new Node(Token.EXPR_RESULT);
        parent.setJSDocInfo(
                JSDocInfoBuilder.forJSType()
                .recordAnnotation(new NoSideEffectAnnotation())
                .build());
        
        // Act
        checkSideEffects.visit(t, n, parent);
        
        // Assert
        // No warnings should be generated
        assertEquals(0, checkSideEffects.compiler.getWarnings().size());
    }

    @Test
    public void testVisit_WhenGivenStringNode_ShouldGenerateMissingPlusWarning() {
        // Arrange
        CheckSideEffects checkSideEffects = new CheckSideEffects(null, null, false);
        NodeTraversal t = null;
        Node n = new Node(Token.STRING, "This is a string");
        Node parent = new Node(Token.EXPR_RESULT);
        
        // Act
        checkSideEffects.visit(t, n, parent);
        
        // Assert
        // One warning should be generated
        assertEquals(1, checkSideEffects.compiler.getWarnings().size());
    }

    @Test
    public void testVisit_WhenGivenSimpleOperatorTypeNode_ShouldGenerateOperatorNotUsedWarning() {
        // Arrange
        CheckSideEffects checkSideEffects = new CheckSideEffects(null, null, false);
        NodeTraversal t = null;
        Node n = new Node(Token.ADD, new Node(Token.NUMBER, "2"), new Node(Token.NUMBER, "3"));
        Node parent = new Node(Token.EXPR_RESULT);
        
        // Act
        checkSideEffects.visit(t, n, parent);
        
        // Assert
        // One warning should be generated
        assertEquals(1, checkSideEffects.compiler.getWarnings().size());
    }

    @Test
    public void testVisit_WhenGivenSideEffectFreeNode_ShouldNotGenerateWarning() {
        // Arrange
        CheckSideEffects checkSideEffects = new CheckSideEffects(null, null, false);
        NodeTraversal t = null;
        Node n = new Node(Token.NUMBER, "42");
        Node parent = new Node(Token.EXPR_RESULT);
        
        // Act
        checkSideEffects.visit(t, n, parent);
        
        // Assert
        // No warnings should be generated
        assertEquals(0, checkSideEffects.compiler.getWarnings().size());
    }

    @Test
    public void testProtectSideEffects_WhenProblemNodesIsNull_ShouldNotChangeAST() {
        // Arrange
        CheckSideEffects checkSideEffects = new CheckSideEffects(null, null, true);
        checkSideEffects.problemNodes = null;
        
        // Act
        checkSideEffects.protectSideEffects();
        
        // Assert
        // The AST should remain unchanged
        assertEquals(0, checkSideEffects.compiler.getWarnings().size());
    }

    @Test
    public void testProtectSideEffects_WhenProblemNodesIsEmpty_ShouldNotChangeAST() {
        // Arrange
        CheckSideEffects checkSideEffects = new CheckSideEffects(null, null, true);
        checkSideEffects.problemNodes = new ArrayList<>();
        
        // Act
        checkSideEffects.protectSideEffects();
        
        // Assert
        // The AST should remain unchanged
        assertEquals(0, checkSideEffects.compiler.getWarnings().size());
    }

    @Test
    public void testProtectSideEffects_WhenGivenProblemNodes_ShouldProtectSideEffectFreeCode() {
        // Arrange
        CheckSideEffects checkSideEffects = new CheckSideEffects(null, null, true);
        Node externs = new Node(Token.ROOT);
        Node root = new Node(Token.ROOT);
        
        // Create a node representing the problem side-effect free code
        Node problemNode = new Node(Token.ADD,
                                    new Node(Token.STRING, "The result is: "),
                                    new Node(Token.NUMBER, "4"));
        Node exprResult = new Node(Token.EXPR_RESULT, problemNode);
        root.addChildToBack(exprResult);
        
        checkSideEffects.problemNodes = new ArrayList<>();
        checkSideEffects.problemNodes.add(problemNode);
        int originalChildCount = root.getChildCount();
        
        // Act
        checkSideEffects.protectSideEffects();
        
        // Assert
        // AST should have been modified
        assertEquals(originalChildCount, root.getChildCount());
        assertNotNull(root.getFirstChild().getFirstChild());
    }

    @Test
    public void testAddExtern_ShouldAddExternProtectSideEffectFreeCodeFunction() {
        // Arrange
        CheckSideEffects checkSideEffects = new CheckSideEffects(null, null, true);
        Node scriptRoot = new Node(Token.ROOT);
        Node originalRoot = null;
        
        // Act
        checkSideEffects.hotSwapScript(scriptRoot, originalRoot);
        
        // Assert
        Node externsRoot = compiler.getSynthesizedExternsInput().getAstRoot(compiler);
        assertTrue(externsRoot.hasOneChild());
        Node functionNode = externsRoot.getFirstChild();
        assertTrue(functionNode.isVar());
        assertTrue(functionNode.getFirstChild().isName());
        assertEquals(CheckSideEffects.PROTECTOR_FN, functionNode.getFirstChild().getString());
        assertTrue(functionNode.getFirstFirstChild().isCall());
        assertEquals(functionNode, functionNode.getFirstFirstChild().getParent());
    }
    
    @Test
    public void testStripProtection_WhenGivenProtectedCode_ShouldRemoveProtection() {
        // Arrange
        CheckSideEffects.StripProtection stripProtection = new CheckSideEffects.StripProtection(null);
        Node externs = new Node(Token.ROOT);
        Node root = new Node(Token.ROOT);
        
        // Create a node representing the protected code
        Node protectedCode = new Node(Token.CALL);
        Node.protectNode(protectedCode);
        Node exprResult = new Node(Token.EXPR_RESULT, protectedCode);
        root.addChildToBack(exprResult);
        
        // Act
        stripProtection.process(externs, root);
        
        // Assert
        // Node should have been replaced by the protected code's only child
        assertEquals(protectedCode, root.getFirstChild());
        assertEquals(0, root.getChildCount());
    }

    private static class NoSideEffectAnnotation implements Annotation {
        
        @Override
        public Class<? extends Annotation> annotationType() {
            return NoSideEffectAnnotation.class;
        }
    }
}
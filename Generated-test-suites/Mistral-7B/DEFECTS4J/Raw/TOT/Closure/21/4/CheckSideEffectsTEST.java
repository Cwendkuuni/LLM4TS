import org.junit.Test;
import static org.junit.Assert.*;
import com.google.javascript.jscomp.CheckLevel;
import com.google.javascript.jscomp.CheckSideEffects;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.SourceFile;

public class CheckSideEffectsTest {

  @Test
  public void testCheckSideEffects() {
    SourceFile source = new SourceFile("TestCheckSideEffects.js", null);
    Node root = source.getProgram();

    CheckSideEffects checker = new CheckSideEffects(null, CheckLevel.WARNING, false);

    NodeTraversal.traverse(checker, root, null);

    // Test case for a simple variable assignment
    Node assignmentNode = root.getChild(0);
    assertEquals("Expected no warning for simple variable assignment", 0, checker.problemNodes.size());

    // Test case for a variable assignment with a side-effect
    Node sideEffectNode = root.getChild(1);
    checker.protectSideEffects();
    assertEquals("Expected one warning for variable assignment with side-effect", 1, checker.problemNodes.size());
    checker.problemNodes.clear();

    // Test case for a function call with no side-effect
    Node functionCallNode = root.getChild(2);
    assertEquals("Expected no warning for function call with no side-effect", 0, checker.problemNodes.size());

    // Test case for a function call with a side-effect
    Node sideEffectFunctionCallNode = root.getChild(3);
    checker.protectSideEffects();
    assertEquals("Expected one warning for function call with side-effect", 1, checker.problemNodes.size());
    checker.problemNodes.clear();

    // Test case for a void statement
    Node voidStatementNode = root.getChild(4);
    assertEquals("Expected no warning for void statement", 0, checker.problemNodes.size());

    // Test case for a comma expression
    Node commaExpressionNode = root.getChild(5);
    assertEquals("Expected no warning for comma expression", 0, checker.problemNodes.size());

    // Test case for a side-effect free block
    Node blockNode = root.getChild(6);
    checker.protectSideEffects();
    assertEquals("Expected one warning for side-effect free block", 1, checker.problemNodes.size());
    checker.problemNodes.clear();

    // Test case for a for statement with no side-effect
    Node forStatementNode = root.getChild(7);
    assertEquals("Expected no warning for for statement with no side-effect", 0, checker.problemNodes.size());

    // Test case for a for statement with a side-effect
    Node sideEffectForStatementNode = root.getChild(8);
    checker.protectSideEffects();
    assertEquals("Expected one warning for for statement with side-effect", 1, checker.problemNodes.size());
    checker.problemNodes.clear();

    // Test case for a string concatenation
    Node concatenationNode = root.getChild(9);
    assertEquals("Expected no warning for string concatenation", 0, checker.problemNodes.size());

    // Test case for a simple operator
    Node operatorNode = root.getChild(10);
    assertEquals("Expected no warning for simple operator", 0, checker.problemNodes.size());

    // Test case for a complex expression with no side-effect
    Node complexExpressionNode = root.getChild(11);
    assertEquals("Expected no warning for complex expression with no side-effect", 0, checker.problemNodes.size());

    // Test case for a complex expression with a side-effect
    Node sideEffectComplexExpressionNode = root.getChild(12);
    checker.protectSideEffects();
    assertEquals("Expected one warning for complex expression with side-effect", 1, checker.problemNodes.size());
    checker.problemNodes.clear();

    // Test case for a function definition with no side-effect
    Node functionDefinitionNode = root.getChild(13);
    assertEquals("Expected no warning for function definition with no side-effect", 0, checker.problemNodes.size());

    // Test case for a function definition with a side-effect
    Node sideEffectFunctionDefinitionNode = root.getChild(14);
    checker.protectSideEffects();
    assertEquals("Expected one warning for function definition with side-effect", 1, checker.problemNodes.size());
    checker.problemNodes.clear();

    // Test case for a class definition with no side-effect
    Node classDefinitionNode = root.getChild(15);
    assertEquals("Expected no warning for class definition with no side-effect", 0, checker.problemNodes.size());
  }

  @Test
  public void testStripProtection() {
    SourceFile source = new SourceFile("TestCheckSideEffects.js", null);
    Node root = source.getProgram();

    CheckSideEffects checker = new CheckSideEffects(null, CheckLevel.WARNING, false);
    checker.protectSideEffects();

    StripProtection stripper = new StripProtection(checker.compiler);
    stripper.process(null, root);

    // Test case for a simple variable assignment
    Node assignmentNode = root.getChild(0);
    assertTrue("Expected the protection to be removed for simple variable assignment", assignmentNode.getLastChild().equals(assignmentNode.getFirstChild()));

    // Test case for a variable assignment with a side-effect
    Node sideEffectNode = root.getChild(1);
    assertTrue("Expected the protection to be removed for variable assignment with side-effect", sideEffectNode.getLastChild().isCall());

    // Test case for a function call with no side-effect
    Node functionCallNode = root.getChild(2);
    assertTrue("Expected the protection to be removed for function call with no side-effect", functionCallNode.getLastChild().isName());

    // Test case for a function call with a side-effect
    Node sideEffectFunctionCallNode = root.getChild(3);
    assertTrue("Expected the protection to be removed for function call with side-effect", sideEffectFunctionCallNode.getLastChild().isCall());

    // Test case for a void statement
    Node voidStatementNode = root.getChild(4);
    assertTrue("Expected the protection to be removed for void statement", voidStatementNode.isVoid());

    // Test case for a comma expression
    Node commaExpressionNode = root.getChild(5);
    assertTrue("Expected the protection to be removed for comma expression", commaExpressionNode.getLastChild().isCall());

    // Test case for a side-effect free block
    Node blockNode = root.getChild(6);
    assertTrue("Expected the protection to be removed for side-effect free block", blockNode.isBlock());

    // Test case for a for statement with no side-effect
    Node forStatementNode = root.getChild(7);
    assertTrue("Expected the protection to be removed for for statement with no side-effect", forStatementNode.isBlock());

    // Test case for a for statement with a side-effect
    Node sideEffectForStatementNode = root.getChild(8);
    assertTrue("Expected the protection to be removed for for statement with side-effect", sideEffectForStatementNode.isBlock());

    // Test case for a string concatenation
    Node concatenationNode = root.getChild(9);
    assertTrue("Expected the protection to be removed for string concatenation", concatenationNode.isCall());

    // Test case for a simple operator
    Node operatorNode = root.getChild(10);
    assertTrue("Expected the protection to be removed for simple operator", operatorNode.isCall());

    // Test case for a complex expression with no side-effect
    Node complexExpressionNode = root.getChild(11);
    assertTrue("Expected the protection to be removed for complex expression with no side-effect", complexExpressionNode.isCall());

    // Test case for a complex expression with a side-effect
    Node sideEffectComplexExpressionNode = root.getChild(12);
    assertTrue("Expected the protection to be removed for complex expression with side-effect", sideEffectComplexExpressionNode.isCall());

    // Test case for a function definition with no side-effect
    Node functionDefinitionNode = root.getChild(13);
    assertTrue("Expected the protection to be removed for function definition with no side-effect", functionDefinitionNode.isFunctionDeclaration());

    // Test case for a function definition with a side-effect
    Node sideEffectFunctionDefinitionNode = root.getChild(14);
    assertTrue("Expected the protection to be removed for function definition with side-effect", sideEffectFunctionDefinitionNode.isFunctionDeclaration());

    // Test case for a class definition with no side-effect
    Node classDefinitionNode = root.getChild(15);
    assertTrue("Expected the protection to be removed for class definition with no side-effect", classDefinitionNode.isClassDeclaration());
  }
}
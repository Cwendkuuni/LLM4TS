import static org.junit.Assert.*;

import org.junit.BeforeClass;
import org.junit.Test;

public class CheckSideEffectsTest {
  
  private static CheckSideEffects checkSideEffects;
  
  @BeforeClass
  public static void setUp() {
    checkSideEffects = new CheckSideEffects();
  }
  
  @Test
  public void testProcess_emptyNode_noWarnings() {
    Node node = new Node();
    checkSideEffects.process(node);
    
    // Assert that no warnings are generated
    assertEquals(0, checkSideEffects.getProblemNodes().size());
  }
  
  @Test
  public void testProcess_nonEmptyNode_withSideEffects_warningsGenerated() {
    Node node = new Node();
    node.addChildToFront(new Node(Token.NAME));
    checkSideEffects.process(node);
    
    // Assert that warnings are generated for the non-empty node with side effects
    assertEquals(1, checkSideEffects.getProblemNodes().size());
    assertEquals("This code lacks side-effects. Is there a bug?", 
                 checkSideEffects.getProblemNodes().get(0).getMessage());
  }
  
  @Test
  public void testVisit_resultUsed_noWarnings() {
    Node node = new Node();
    node.setType(Token.EXPR_RESULT);
    node.addChildToFront(new Node(Token.NAME));
    Node parent = new Node(Token.COMMA, node);
    checkSideEffects.visit(node, parent);
    
    // Assert that no warnings are generated
    assertEquals(0, checkSideEffects.getProblemNodes().size());
  }
  
  @Test
  public void testVisit_resultNotUsed_warningsGenerated() {
    Node node = new Node();
    node.setType(Token.EXPR_RESULT);
    node.addChildToFront(new Node(Token.NAME));
    Node parent = new Node(Token.COMMA, node);
    parent.addChildToBack(new Node(Token.NAME));
    checkSideEffects.visit(node, parent);
    
    // Assert that warnings are generated for the expression result not being used
    assertEquals(1, checkSideEffects.getProblemNodes().size());
    assertEquals("The result of the 'expr_result' operator is not being used.",
                 checkSideEffects.getProblemNodes().get(0).getMessage());
  }
  
  @Test
  public void testProtectSideEffects_problemNodesExist_replacementNodesCreated() {
    Node node1 = new Node(Token.NAME);
    Node node2 = new Node(Token.STRING, new Node(Token.ADD, new Node(Token.STRING), new Node(Token.STRING)));
    Node node3 = new Node(Token.NAME);
    
    List<Node> problemNodes = new ArrayList<>();
    problemNodes.add(node1);
    problemNodes.add(node2);
    problemNodes.add(node3);
    
    checkSideEffects.setProblemNodes(problemNodes);
    checkSideEffects.protectSideEffects();
    
    // Assert that the problem nodes are replaced with a function call to the protector function
    assertEquals(3, checkSideEffects.getProblemNodes().size());
    
    Node replacedNode1 = checkSideEffects.getProblemNodes().get(0);
    assertEquals(Token.CALL, replacedNode1.getType());
    assertEquals("JSCOMPILER_PRESERVE", replacedNode1.getFirstChild().getString());
    assertEquals(1, replacedNode1.getChildCount());
    assertEquals(node1, replacedNode1.getLastChild());
    
    Node replacedNode2 = checkSideEffects.getProblemNodes().get(1);
    assertEquals(Token.CALL, replacedNode2.getType());
    assertEquals("JSCOMPILER_PRESERVE", replacedNode2.getFirstChild().getString());
    assertEquals(1, replacedNode2.getChildCount());
    assertEquals(node2, replacedNode2.getLastChild());
    
    Node replacedNode3 = checkSideEffects.getProblemNodes().get(2);
    assertEquals(Token.CALL, replacedNode3.getType());
    assertEquals("JSCOMPILER_PRESERVE", replacedNode3.getFirstChild().getString());
    assertEquals(1, replacedNode3.getChildCount());
    assertEquals(node3, replacedNode3.getLastChild());
  }
  
  @Test
  public void testAddExtern_problemNodesExist_externFunctionAdded() {
    Node problemNode1 = new Node(Token.NAME);
    Node problemNode2 = new Node(Token.NAME);
    List<Node> problemNodes = new ArrayList<>();
    problemNodes.add(problemNode1);
    problemNodes.add(problemNode2);
    
    checkSideEffects.setProblemNodes(problemNodes);
    checkSideEffects.addExtern();
    
    // Assert that the problem nodes are replaced with a function call to the protector function
    assertEquals(2, checkSideEffects.getProblemNodes().size());
    
    Node replacedNode1 = checkSideEffects.getProblemNodes().get(0);
    assertEquals(Token.CALL, replacedNode1.getType());
    assertEquals("JSCOMPILER_PRESERVE", replacedNode1.getFirstChild().getString());
    assertEquals(1, replacedNode1.getChildCount());
    assertEquals(problemNode1, replacedNode1.getLastChild());
    
    Node replacedNode2 = checkSideEffects.getProblemNodes().get(1);
    assertEquals(Token.CALL, replacedNode2.getType());
    assertEquals("JSCOMPILER_PRESERVE", replacedNode2.getFirstChild().getString());
    assertEquals(1, replacedNode2.getChildCount());
    assertEquals(problemNode2, replacedNode2.getLastChild());
    
    // Assert that the extern function is added
    Node externs = checkSideEffects.getExterns();
    assertEquals(Token.VAR, externs.getType());
    assertEquals("JSCOMPILER_PRESERVE", externs.getFirstChild().getString());
  }
  
  @Test
  public void testStripProtection_functionCallToProtector_replacedWithExpression() {
    Node protectorNode = new Node(Token.NAME);
    protectorNode.setString("JSCOMPILER_PRESERVE");
    Node expressionNode = new Node(Token.NAME);
    Node parent = new Node(Token.COMMA, protectorNode, expressionNode);
    
    checkSideEffects.stripProtection(parent);
    
    // Assert that the function call to the protector is replaced with the expression
    assertEquals(Token.NAME, parent.getType());
    assertEquals(expressionNode, parent);
  }

}
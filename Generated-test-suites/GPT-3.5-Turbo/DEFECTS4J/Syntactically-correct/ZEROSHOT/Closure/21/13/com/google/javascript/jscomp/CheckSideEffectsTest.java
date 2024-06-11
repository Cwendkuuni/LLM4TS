import static org.junit.Assert.*;
import org.junit.Test;

public class CheckSideEffectsTest {

  @Test
  public void testProcess() {
    // Create a CheckSideEffects object and call the process method
    CheckSideEffects cse = new CheckSideEffects(new AbstractCompiler(), CheckLevel.WARNING, false);
    cse.process(new Node(), new Node());
    
    // Assert that the problemNodes list is empty
    assertEquals(0, cse.problemNodes.size());
  }

  @Test
  public void testHotSwapScript() {
    // Create a CheckSideEffects object and call the hotSwapScript method
    CheckSideEffects cse = new CheckSideEffects(new AbstractCompiler(), CheckLevel.WARNING, false);
    cse.hotSwapScript(new Node(), new Node());
    
    // Assert that the problemNodes list is empty
    assertEquals(0, cse.problemNodes.size());
  }

  @Test
  public void testVisit() {
    // Create a CheckSideEffects object
    CheckSideEffects cse = new CheckSideEffects(new AbstractCompiler(), CheckLevel.WARNING, false);
    
    // Create a mock NodeTraversal object
    NodeTraversal t = new NodeTraversal(new AbstractCompiler());
    
    // Create a mock Node and parent Node
    Node n = new Node();
    Node parent = new Node();
    
    // Call the visit method
    cse.visit(t, n, parent);
    
    // Assert that the problemNodes list is empty
    assertEquals(0, cse.problemNodes.size());
  }

  @Test
  public void testProtectSideEffects() {
    // Create a CheckSideEffects object
    CheckSideEffects cse = new CheckSideEffects(new AbstractCompiler(), CheckLevel.WARNING, true);
    
    // Create a mock Node to add to the problemNodes list
    Node n = new Node();
    cse.problemNodes.add(n);
    
    // Call the protectSideEffects method
    cse.protectSideEffects();
    
    // Assert that the problemNodes list is empty
    assertEquals(0, cse.problemNodes.size());
  }

  @Test
  public void testAddExtern() {
    // Create a CheckSideEffects object and set the compiler to a mock compiler
    CheckSideEffects cse = new CheckSideEffects(new AbstractCompiler(), CheckLevel.WARNING, true);
    cse.compiler.setSynthesizedExternsInput(new CompilerInput());
    
    // Call the addExtern method
    cse.addExtern();
    
    // Assert that the compiler has reported a code change
    assertTrue(cse.compiler.hasReportedCodeChange());
  }

  @Test
  public void testVisitStripProtection() {
    // Create a CheckSideEffects.StripProtection object and set the compiler to a mock compiler
    CheckSideEffects.StripProtection sp = new CheckSideEffects.StripProtection(new AbstractCompiler());
    
    // Create a mock NodeTraversal object
    NodeTraversal t = new NodeTraversal(new AbstractCompiler());
    
    // Create a mock Node and parent Node for the call node
    Node n = new Node(Token.CALL);
    Node parent = new Node();
    n.addChildToBack(new Node(Token.NAME, "JSCOMPILER_PRESERVE"));
    
    // Call the visit method
    sp.visit(t, n, parent);
    
    // Assert that the parent node has been replaced with the child node
    assertEquals(n.getLastChild(), parent.getFirstChild());
  }
}
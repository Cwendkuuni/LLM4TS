import org.junit.Test;
import static org.junit.Assert.*;
import com.google.javascript.rhino.Node;

public class CheckSideEffectsTest {
  
  // Test for process() method
  @Test
  public void testProcess() {
    AbstractCompiler compiler = new AbstractCompiler();
    CheckLevel level = CheckLevel.WARNING;
    boolean protectSideEffectFreeCode = true;
    CheckSideEffects checker = new CheckSideEffects(compiler, level, protectSideEffectFreeCode);
    
    Node externs = null;
    Node root = null;
    checker.process(externs, root);
    
    // Verify that the problemNodes list is empty after calling process()
    assertEquals("The problemNodes list should be empty after calling process()", 0, checker.problemNodes.size());
  }
  
  // Test for hotSwapScript() method
  @Test
  public void testHotSwapScript() {
    AbstractCompiler compiler = new AbstractCompiler();
    CheckSideEffects checker = new CheckSideEffects(compiler, CheckLevel.WARNING, true);
    
    Node scriptRoot = null;
    Node originalRoot = null;
    checker.hotSwapScript(scriptRoot, originalRoot);
    
    // Verify that the problemNodes list is empty after calling hotSwapScript()
    assertEquals("The problemNodes list should be empty after calling hotSwapScript()", 0, checker.problemNodes.size());
  }
  
  // Test for visit() method
  @Test
  public void testVisit() {
    AbstractCompiler compiler = new AbstractCompiler();
    CheckLevel level = CheckLevel.WARNING;
    boolean protectSideEffectFreeCode = true;
    CheckSideEffects checker = new CheckSideEffects(compiler, level, protectSideEffectFreeCode);
    
    NodeTraversal traversal = null;
    Node node = null;
    Node parent = null;
    checker.visit(traversal, node, parent);
    
    // Verify that the problemNodes list is empty after calling visit()
    assertEquals("The problemNodes list should be empty after calling visit()", 0, checker.problemNodes.size());
  }
  
  // Test for protectSideEffects() method
  @Test
  public void testProtectSideEffects() {
    AbstractCompiler compiler = new AbstractCompiler();
    CheckLevel level = CheckLevel.WARNING;
    boolean protectSideEffectFreeCode = true;
    CheckSideEffects checker = new CheckSideEffects(compiler, level, protectSideEffectFreeCode);
    
    // Add some nodes to the problemNodes list for testing
    Node node1 = new Node(Token.NAME);
    Node node2 = new Node(Token.STRING);
    Node node3 = new Node(Token.BLOCK);
    checker.problemNodes.add(node1);
    checker.problemNodes.add(node2);
    checker.problemNodes.add(node3);
    
    checker.protectSideEffects();
    
    // Verify that the problemNodes list is empty after calling protectSideEffects()
    assertEquals("The problemNodes list should be empty after calling protectSideEffects()", 0, checker.problemNodes.size());
    
    // Verify that the replacement nodes have been correctly added
    assertEquals("The parent of node1 should be replaced by a call to PROTECTOR_FN", node1.getParent(), replacement1);
    assertEquals("The parent of node2 should be replaced by a call to PROTECTOR_FN", node2.getParent(), replacement2);
    assertEquals("The parent of node3 should be replaced by a call to PROTECTOR_FN", node3.getParent(), replacement3);
  }
  
  // Test for addExtern() method
  @Test
  public void testAddExtern() {
    AbstractCompiler compiler = new AbstractCompiler();
    CheckSideEffects checker = new CheckSideEffects(compiler, CheckLevel.WARNING, true);
    
    // Create expected nodes
    Node name = new Node(Token.NAME, "PROTECTOR_FN");
    name.putBooleanProp(Node.IS_CONSTANT_NAME, true);
    Node var = new Node(Token.VAR, name);
    JSDocInfoBuilder builder = new JSDocInfoBuilder(false);
    builder.recordNoAlias();
    var.setJSDocInfo(builder.build(var));
    
    checker.addExtern();
    
    // Verify that the expected nodes have been added to the synthesizedExternsInput
    Node synthesizedExternsInput = compiler.getSynthesizedExternsInput().getAstRoot(compiler);
    Node varStmt = synthesizedExternsInput.getChildAtIndex(0);
    assertEquals("The node type should be VAR", Token.VAR, varStmt.getType());
    Node actualName = varStmt.getFirstChild();
    assertEquals("The node type should be NAME", Token.NAME, actualName.getType());
    assertTrue("The name property should be PROTECTOR_FN", actualName.getString().equals("PROTECTOR_FN"));
    assertTrue("The IS_CONSTANT_NAME property should be true", actualName.getBooleanProp(Node.IS_CONSTANT_NAME));
    assertNotNull("The node should have jsDocInfo", actualName.getJSDocInfo());
    assertTrue("The jsDocInfo should contain @noalias annotation", actualName.getJSDocInfo().getAnnotations().contains("noalias"));
  }
  
  // Test for StripProtection class
  @Test
  public void testStripProtection() {
    AbstractCompiler compiler = new AbstractCompiler();
    CheckSideEffects.StripProtection stripper = new CheckSideEffects.StripProtection(compiler);
    
    Node externs = null;
    Node root = null;
    stripper.process(externs, root);
    
    // Verify that the protection calls have been properly stripped
    // by checking the call nodes and their parent nodes
    for (Node call : stripper.callsToRemove) {
      assertEquals("The parent of the call should be the parent's original grandparent", call.getParent(), call.getParent().getParent().getParent());
    }
  }
  
}
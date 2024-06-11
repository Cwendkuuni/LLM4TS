import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class CheckSideEffectsTest {

  private CheckSideEffects checker;

  @Before
  public void setUp() {
    checker = new CheckSideEffects();
  }

  @Test
  public void testProcess_ValidInput() {
    // Test input code with no side effects
    String code = "var a = 5;";
   
    // Create AST for code
    Node root = createAST(code);
    
    // Process the AST
    checker.process(null, root);
    
    // Verify that no warnings are generated
    assertEquals(0, checker.getWarnings().size());
  }
  
  @Test
  public void testProcess_SideEffectCode() {
    // Test input code with side effects
    String code = "console.log('Hello World');";
   
    // Create AST for code
    Node root = createAST(code);
    
    // Process the AST
    checker.process(null, root);
    
    // Verify that a warning is generated
    assertEquals(1, checker.getWarnings().size());
    
    // Verify the warning message
    Warning warning = checker.getWarnings().get(0);
    assertEquals("This code lacks side-effects. Is there a bug?", warning.getMessage());
  }
  
  @Test
  public void testProtectSideEffects_SideEffectFreeCode() {
    // Test input with side-effect free code
    String code = "var a = 5;";
   
    // Create AST for code
    Node root = createAST(code);
    
    // Set protectSideEffectFreeCode to true
    checker.setProtectSideEffectFreeCode(true);
    
    // Process the AST
    checker.process(null, root);
    
    // Verify that no warnings are generated
    assertEquals(0, checker.getWarnings().size());
    
    // Verify that the side-effect free code is protected
    assertEquals(1, root.getChildCount());
    assertEquals(Node.CALL, root.getFirstChild().getType());
    assertEquals("JSCOMPILER_PRESERVE", root.getFirstChild().getFirstChild().getString());
    assertEquals(Node.VAR, root.getFirstChild().getLastChild().getType());
    assertEquals("a", root.getFirstChild().getLastChild().getFirstChild().getString());
    assertEquals(Node.NUMBER, root.getFirstChild().getLastChild().getLastChild().getType());
    assertEquals("5", root.getFirstChild().getLastChild().getLastChild().getString());
  }
  
  @Test
  public void testAddExtern_ExistingExtern() {
    // Test adding an extern when an extern for JSCOMPILER_PRESERVE already exists
    String code = "var a = 5;";
    
    // Create AST for code
    Node root = createAST(code);
    
    // Add a pre-existing extern
    Node externs = createAST("var JSCOMPILER_PRESERVE = function(){};");
    checker.addExistingExterns(externs);
    
    // Process the AST
    checker.process(null, root);
    
    // Verify that no warnings are generated
    assertEquals(0, checker.getWarnings().size());
    
    // Verify that the extern is not added again
    assertEquals(1, externs.getChildCount());
  }
  
  @Test
  public void testStripProtection_SideEffectCode() {
    // Test removing protection from side-effect code
    String code = "JSCOMPILER_PRESERVE();";
    
    // Create AST for code
    Node root = createAST(code);
    
    // Process the AST
    checker.process(null, root);
    
    // Verify that the protection is removed
    assertEquals(1, root.getChildCount());
    assertEquals(Node.CALL, root.getFirstChild().getType());
    assertEquals("JSCOMPILER_PRESERVE", root.getFirstChild().getFirstChild().getString());
  }
  
  // Helper method to create AST from code
  private Node createAST(String code) {
    // Create parser
    CompilerOptions options = new CompilerOptions();
    Parser parser = new Parser(new SourceFile("test.js", code), new ErrorHandler(), options);
    
    // Parse code and return AST root
    return parser.parseProgram();
  }
}
import org.junit.Test;
import static org.junit.Assert.*;

public class RuntimeTypeCheckTest {
  
  @Test
  public void testAddMarkers() {
    // Create a Compiler and a RuntimeTypeCheck object
    AbstractCompiler compiler = new Compiler();
    RuntimeTypeCheck runtimeTypeCheck = new RuntimeTypeCheck(compiler, null);
    
    // Create a Node for testing
    Node externs = new Node(Token.SCRIPT);
    Node root = new Node(Token.SCRIPT);
    Node function = new Node(Token.FUNCTION);
    
    // Generate markers for function
    runtimeTypeCheck.addMarkers(new NodeTraversal(compiler, runtimeTypeCheck), function);
    
    // Check if markers are added correctly
    assertEquals(function.toStringTree(), "C.prototype['instance_of__C']");
    assertEquals(function.toStringTree(), "C.prototype['implements__I']");
  }
  
  @Test
  public void testAddChecks() {
    // Create a Compiler and a RuntimeTypeCheck object
    AbstractCompiler compiler = new Compiler();
    RuntimeTypeCheck runtimeTypeCheck = new RuntimeTypeCheck(compiler, null);
    
    // Create a Node for testing
    Node externs = new Node(Token.SCRIPT);
    Node root = new Node(Token.SCRIPT);
    Node function = new Node(Token.FUNCTION);
    
    // Generate checks for function
    runtimeTypeCheck.addChecks(new NodeTraversal(compiler, runtimeTypeCheck), function);
    
    // Check if checks are added correctly
    assertEquals(function.toStringTree(), "checkType(expr, [valueChecker('string'), classChecker('Foo')])");
  }
  
  @Test
  public void testCreateCheckTypeCallNode() {
    // Create a Compiler and a RuntimeTypeCheck object
    AbstractCompiler compiler = new Compiler();
    RuntimeTypeCheck runtimeTypeCheck = new RuntimeTypeCheck(compiler, null);
    
    // Create a Node for testing
    Node expr = new Node(Token.NAME);
    JSType type = new JSType();
    
    // Generate a check for expression and type
    Node checkNode = runtimeTypeCheck.createCheckTypeCallNode(type, expr);
    
    // Check if check node is created correctly
    assertEquals(checkNode.toStringTree(), "checkType(expr, [valueChecker('string'), classChecker('Foo')])");
  }
  
  @Test
  public void testCreateCheckerNode() {
    // Create a Compiler and a RuntimeTypeCheck object
    AbstractCompiler compiler = new Compiler();
    RuntimeTypeCheck runtimeTypeCheck = new RuntimeTypeCheck(compiler, null);
    
    // Create a Node for testing
    JSType type = new JSType();
    
    // Generate a checker for the type
    Node checkerNode = runtimeTypeCheck.createCheckerNode(type);
    
    // Check if checker node is created correctly
    assertEquals(checkerNode.toStringTree(), "new Node(Token.CALL, jsCode('externClassChecker'), Node.newString('refName'))");
  }
  
  @Test
  public void testAddBoilerplateCode() {
    // Create a Compiler and a RuntimeTypeCheck object
    AbstractCompiler compiler = new Compiler();
    RuntimeTypeCheck runtimeTypeCheck = new RuntimeTypeCheck(compiler, null);
    
    // Add boilerplate code
    runtimeTypeCheck.addBoilerplateCode();
    
    // Check if boilerplate code is added correctly
    assertNotNull(compiler.getNodeForCodeInsertion(null).FirstChild);
  }
  
  @Test
  public void testGetBoilerplateCode() {
    // Create a Compiler and a RuntimeTypeCheck object
    AbstractCompiler compiler = new Compiler();
    String logFunction = "function(warning, expr) {}";
    
    // Get boilerplate code
    Node boilerplateCode = runtimeTypeCheck.getBoilerplateCode(compiler, logFunction);
    
    // Check if boilerplate code is generated correctly
    assertNotNull(boilerplateCode);
  }
}
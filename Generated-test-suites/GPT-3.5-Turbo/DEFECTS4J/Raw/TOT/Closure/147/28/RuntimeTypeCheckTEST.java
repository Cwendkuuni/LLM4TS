import static org.junit.Assert.*;

import org.junit.*;

public class RuntimeTypeCheckTest {
  
  @Test
  public void testAddMarkers() {
    // Test an example function
    String sourceCode = "function add(a, b) { return a + b; }";
    Compiler compiler = new Compiler();
    Node externs = compiler.parseTestCode("");
    Node root = compiler.parseTestCode(sourceCode);
    RuntimeTypeCheck.AddMarkers addMarkers = new RuntimeTypeCheck.AddMarkers(compiler);
    NodeTraversal.traverse(compiler, root, addMarkers);
    
    // Ensure that the markers are added correctly
    assertTrue(root.toString().contains("instance_of__add"));
    
    // Test a constructor
    sourceCode = "function MyClass() { this.prop = 5; }";
    root = compiler.parseTestCode(sourceCode);
    NodeTraversal.traverse(compiler, root, addMarkers);
    
    // Ensure that the markers are added correctly
    assertTrue(root.toString().contains("instance_of__MyClass"));
  }
  
  @Test
  public void testAddChecks() {
    // Test an example function
    String sourceCode = "function add(a, b) { return a + b; }";
    Compiler compiler = new Compiler();
    Node externs = compiler.parseTestCode("");
    Node root = compiler.parseTestCode(sourceCode);
    RuntimeTypeCheck.AddMarkers addMarkers = new RuntimeTypeCheck.AddMarkers(compiler);
    NodeTraversal.traverse(compiler, root, addMarkers);
    RuntimeTypeCheck.AddChecks addChecks = new RuntimeTypeCheck.AddChecks(compiler);
    NodeTraversal.traverse(compiler, root, addChecks);
    
    // Ensure that the checks are added correctly
    assertTrue(root.toString().contains("checkType(add, [valueChecker('number'), valueChecker('number')])"));
  }
  
  @Test
  public void testProcess() {
    // Test an example function
    String sourceCode = "function add(a, b) { return a + b; }";
    Compiler compiler = new Compiler();
    Node externs = compiler.parseTestCode("");
    Node root = compiler.parseTestCode(sourceCode);
    RuntimeTypeCheck runtimeTypeCheck = new RuntimeTypeCheck(compiler, null);
    
    // Ensure that the markers and checks are added correctly
    runtimeTypeCheck.process(externs, root);
    assertTrue(root.toString().contains("instance_of__add"));
    assertTrue(root.toString().contains("checkType(add, [valueChecker('number'), valueChecker('number')])"));
  }
  
  @Test
  public void testGetBoilerplateCode() {
    Compiler compiler = new Compiler();
    Node boilerplateCode = RuntimeTypeCheck.getBoilerplateCode(compiler, null);
    
    // Ensure that the boilerplate code is generated correctly
    assertNotNull(boilerplateCode);
    assertTrue(boilerplateCode.hasChildren());
  }
}
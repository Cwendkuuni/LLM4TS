import org.junit.Before;
import org.junit.Test;
import java.util.List;
import static org.junit.Assert.*;

public class VarCheckTest {

  private VarCheck varCheck;
  
  @Before
  public void setUp() {
    varCheck = new VarCheck();
  }
  
  @Test
  public void testProcess_ValidVarDeclaration_Success() {
    Node externs = createExternsNode("externs.js");
    Node root = createRootNode("root.js");
    
    varCheck.process(externs, root);
    
    // Verify that no errors were reported
    List<CompilerError> errors = varCheck.getErrors();
    assertTrue(errors.isEmpty());
    
    // Verify that all variables were properly declared
    List<Var> undeclaredVars = varCheck.getUndeclaredVars();
    assertTrue(undeclaredVars.isEmpty());
    
    // Verify that all module dependencies were respected
    List<ModuleDependencyViolation> moduleDepViolations = varCheck.getModuleDependencyViolations();
    assertTrue(moduleDepViolations.isEmpty());
    
    // Verify that all references in externs have no effect
    List<NameReferenceInExterns> externsReferences = varCheck.getNameReferencesInExterns();
    assertTrue(externsReferences.isEmpty());
    
    // Verify that all undefined extern variables were reported
    List<UndefinedExternVariable> undefinedExternVariables = varCheck.getUndefinedExternVariables();
    assertTrue(undefinedExternVariables.isEmpty());
  }
  
  @Test
  public void testProcess_UndefinedVarError_ExceptionThrown() {
    Node externs = createExternsNode("externs.js");
    Node root = createRootNode("root.js");
    
    // Add an undefined variable reference to the root node
    root.addChildToBack(Node.newString("undefinedVar"));
    
    try {
      varCheck.process(externs, root);
      fail("Expected IllegalStateException to be thrown");
    } catch (IllegalStateException e) {
      // Verify that the expected exception was thrown
      assertEquals("Unexpected variable undefinedVar", e.getMessage());
    }
  }
  
  @Test
  public void testProcess_ViolatedModuleDependency_ErrorReported() {
    Node externs = createExternsNode("externs.js");
    Node root = createRootNode("root.js");
    
    // Add a variable reference that violates a module dependency
    root.addChildToBack(createModuleDepViolationNode());
    
    varCheck.process(externs, root);
    
    // Verify that the expected module dependency violation error was reported
    List<ModuleDependencyViolation> moduleDepViolations = varCheck.getModuleDependencyViolations();
    assertEquals(1, moduleDepViolations.size());
    
    ModuleDependencyViolation depViolation = moduleDepViolations.get(0);
    assertEquals("root", depViolation.getReferencingModule());
    assertEquals("module2", depViolation.getReferencedModule());
    assertEquals("var2", depViolation.getVarName());
  }
  
  @Test
  public void testProcess_MissingModuleDependency_WarningReported() {
    Node externs = createExternsNode("externs.js");
    Node root = createRootNode("root.js");
    
    // Add a variable reference that creates a missing module dependency
    root.addChildToBack(createMissingModuleDepNode());
    
    varCheck.process(externs, root);
    
    // Verify that the expected missing module dependency warning was reported
    List<ModuleDependencyWarning> moduleDepWarnings = varCheck.getModuleDependencyWarnings();
    assertEquals(1, moduleDepWarnings.size());
    
    ModuleDependencyWarning depWarning = moduleDepWarnings.get(0);
    assertEquals("module1", depWarning.getReferencingModule());
    assertEquals("module2", depWarning.getReferencedModule());
    assertEquals("var2", depWarning.getVarName());
  }
  
  @Test
  public void testProcess_NameReferenceInExterns_WarningReported() {
    Node externs = createExternsNode("externs.js");
    Node root = createRootNode("root.js");
    
    // Add a variable reference in externs that has no effect
    externs.addChildToBack(createExternVarReferenceNode());
    
    varCheck.process(externs, root);
    
    // Verify that the expected name reference in externs warning was reported
    List<NameReferenceInExternsWarning> externsReferences = varCheck.getNameReferencesInExterns();
    assertEquals(1, externsReferences.size());
    
    NameReferenceInExternsWarning externRef = externsReferences.get(0);
    assertEquals("var1", externRef.getVarName());
  }
  
  @Test
  public void testProcess_UndefinedExternVar_UndefinedExternVariableReported() {
    Node externs = createExternsNode("externs.js");
    Node root = createRootNode("root.js");
    
    // Add an undefined extern variable reference to the root node
    root.addChildToBack(createUndefinedExternVarNode());
    
    varCheck.process(externs, root);
    
    // Verify that the expected undefined extern variable was reported
    List<UndefinedExternVariable> undefinedExternVariables = varCheck.getUndefinedExternVariables();
    assertEquals(1, undefinedExternVariables.size());
    
    UndefinedExternVariable undefinedExternVar = undefinedExternVariables.get(0);
    assertEquals("undefinedVar", undefinedExternVar.getVarName());
  }
  
  private Node createExternsNode(String name) {
    return new Node(Token.EXTERNS, new Node(Token.FILE, new Node(Token.SCRIPT, new Node(Token.NAME, name))));
  }
  
  private Node createRootNode(String name) {
    return new Node(Token.EXTERNS, new Node(Token.FILE, new Node(Token.SCRIPT, new Node(Token.NAME, name))));
  }
  
  private Node createModuleDepViolationNode() {
    return new Node(Token.NAME, "var2");
  }
  
  private Node createMissingModuleDepNode() {
    return new Node(Token.NAME, "var2");
  }
  
  private Node createExternVarReferenceNode() {
    return new Node(Token.NAME, "var1");
  }
  
  private Node createUndefinedExternVarNode() {
    return new Node(Token.NAME, "undefinedVar");
  }
}
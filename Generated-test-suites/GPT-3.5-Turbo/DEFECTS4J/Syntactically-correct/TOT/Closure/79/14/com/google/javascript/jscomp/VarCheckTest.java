import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class VarCheckTest {

  private VarCheck varCheck;

  @Before
  public void setUp() {
    varCheck = new VarCheck();
  }

  // Test for checkVarDeclaration()

  @Test
  public void testCheckVarDeclaration_typicalCase() {
    // Typical case: Variable is declared
    String varName = "variable";
    Node var = new Node(Token.VAR, new Node(Token.NAME, varName));
    CompilerInput input = new CompilerInput(var);
    input.setModule(new JSModule("module"));
    Scope scope = new Scope(input);
    scope.declare(varName, var, null);
    NodeTraversal traversal = new NodeTraversal(varCheck, input);
    NodeTraversal.ScopedCallback callback = varCheck.new CheckVarDeclaration();
    assertNull(callback.checkVarDeclaration(traversal, new Node(Token.NAME, varName), var, var.getParent()));
  }

  @Test
  public void testCheckVarDeclaration_undeclaredVariable() {
    // Error case: Variable is not declared
    String varName = "undefinedVar";
    Node var = new Node(Token.VAR, new Node(Token.NAME, varName));
    CompilerInput input = new CompilerInput(var);
    input.setModule(new JSModule("module"));
    NodeTraversal traversal = new NodeTraversal(varCheck, input);
    NodeTraversal.ScopedCallback callback = varCheck.new CheckVarDeclaration();
    assertNotNull(callback.checkVarDeclaration(traversal, new Node(Token.NAME, varName), var, var.getParent()));
  }

  // Test for checkModuleDependency()

  @Test
  public void testCheckModuleDependency_typicalCase() {
    // Typical case: Variable reference is within the same module dependency
    String var1Name = "variable1";
    String var2Name = "variable2";
    Node var1 = new Node(Token.VAR, new Node(Token.NAME, var1Name));
    Node var2 = new Node(Token.VAR, new Node(Token.NAME, var2Name));
    CompilerInput input1 = new CompilerInput(var1);
    JSModule module1 = new JSModule("module1");
    input1.setModule(module1);
    CompilerInput input2 = new CompilerInput(var2);
    JSModule module2 = new JSModule("module2");
    module2.addDependency(module1);
    input2.setModule(module2);
    module1.addInput(input2);
    Scope scope1 = new Scope(input1);
    scope1.declare(var1Name, var1, null);
    Scope scope2 = new Scope(input2);
    scope2.declare(var2Name, var2, null);
    NodeTraversal traversal = new NodeTraversal(varCheck, input2);
    NodeTraversal.ScopedCallback callback = varCheck.new CheckModuleDependency();
    assertNull(callback.checkModuleDependency(traversal, new Node(Token.NAME, var2Name), var2));
  }

  @Test
  public void testCheckModuleDependency_violatedDependency() {
    // Error case: Variable reference violates module dependency
    String var1Name = "variable1";
    String var2Name = "variable2";
    Node var1 = new Node(Token.VAR, new Node(Token.NAME, var1Name));
    Node var2 = new Node(Token.VAR, new Node(Token.NAME, var2Name));
    CompilerInput input1 = new CompilerInput(var1);
    JSModule module1 = new JSModule("module1");
    input1.setModule(module1);
    CompilerInput input2 = new CompilerInput(var2);
    JSModule module2 = new JSModule("module2");
    input2.setModule(module2);
    module2.addDependency(module1);
    module1.addInput(input2);
    Scope scope1 = new Scope(input1);
    scope1.declare(var1Name, var1, null);
    Scope scope2 = new Scope(input2);
    scope2.declare(var2Name, var2, null);
    NodeTraversal traversal = new NodeTraversal(varCheck, input2);
    NodeTraversal.ScopedCallback callback = varCheck.new CheckModuleDependency();
    assertNotNull(callback.checkModuleDependency(traversal, new Node(Token.NAME, var2Name), var2));
  }

  // Test for checkExternalReferences()

  @Test
  public void testCheckExternalReferences_typicalCase() {
    // Typical case: External reference has no effect
    String varName = "externalVar";
    Node extern = new Node(Token.VAR, new Node(Token.NAME, varName));
    CompilerInput input = new CompilerInput(extern);
    input.setExtern(true);
    Scope scope = new Scope(input);
    scope.declare(varName, extern, null);
    NodeTraversal traversal = new NodeTraversal(varCheck, input);
    NodeTraversal.ScopedCallback callback = varCheck.new CheckExternalReferences();
    assertNull(callback.checkExternalReferences(traversal, new Node(Token.NAME, varName), extern));
  }

  @Test
  public void testCheckExternalReferences_undefinedVariable() {
    // Error case: External reference to an undefined variable
    String varName = "undefinedVar";
    Node extern = new Node(Token.VAR, new Node(Token.NAME, varName));
    CompilerInput input = new CompilerInput(extern);
    input.setExtern(true);
    NodeTraversal traversal = new NodeTraversal(varCheck, input);
    NodeTraversal.ScopedCallback callback = varCheck.new CheckExternalReferences();
    assertNotNull(callback.checkExternalReferences(traversal, new Node(Token.NAME, varName), extern));
  }

}
import org.junit.Test;
import static org.junit.Assert.*;

public class InlineObjectLiteralsTest {

  @Test
  public void testIsVarInlineForbidden() {
    // Test when the variable is global
    InlineObjectLiterals object = new InlineObjectLiterals();
    Var var = new Var();
    var.setGlobal(true);
    boolean result = object.isVarInlineForbidden(var);
    assertTrue(result);
    
    // Test when the variable is extern
    var = new Var();
    var.setExtern(true);
    result = object.isVarInlineForbidden(var);
    assertTrue(result);
    
    // Test when the variable is exported
    var = new Var();
    var.setName("testVar");
    InlineObjectLiterals object = new InlineObjectLiterals();
    object.getCodingConvention().exportedVars.add("testVar");
    boolean result = object.isVarInlineForbidden(var);
    assertTrue(result);
    
    // Test when the variable is RenameProperties.RENAME_PROPERTY_FUNCTION_NAME
    var = new Var();
    var.setName("RENAME_PROPERTY_FUNCTION_NAME");
    result = object.isVarInlineForbidden(var);
    assertTrue(result);
    
    // Test when the variable is stale
    InlineObjectLiterals object = new InlineObjectLiterals();
    Set<Var> staleVars = new HashSet<>();
    Var staleVar = new Var();
    staleVars.add(staleVar);
    object.staleVars = staleVars;
    var = new Var();
    var.setName("testVar");
    boolean result = object.isVarInlineForbidden(var);
    assertTrue(result);
  }

  @Test
  public void testIsInlinableObject() {
    // TODO: Implement test case
  }

  @Test
  public void testComputeVarList() {
    // TODO: Implement test case
  }

  @Test
  public void testFillInitialValues() {
    // TODO: Implement test case
  }

  @Test
  public void testReplaceAssignmentExpression() {
    // TODO: Implement test case
  }

  @Test
  public void testSplitObject() {
    // TODO: Implement test case
  }

}
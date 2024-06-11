import static org.junit.Assert.*;
import org.junit.Test;

public class InlineObjectLiteralsTest {

  @Test
  public void testAfterExitScope() {
  }
  
  @Test
  public void testIsVarInlineForbidden() {
    InlineObjectLiterals objLit = new InlineObjectLiterals();
    assertFalse(objLit.isVarInlineForbidden(var));
  }
  
  @Test
  public void testIsInlinableObject() {
    InlineObjectLiterals objLit = new InlineObjectLiterals();
    assertFalse(objLit.isInlinableObject(refs));
  }
  
  @Test
  public void testBlacklistVarReferencesInTree() {
    InlineObjectLiterals objLit = new InlineObjectLiterals();
    objLit.blacklistVarReferencesInTree(root, scope);
  }
  
  @Test
  public void testComputeVarList() {
    InlineObjectLiterals objLit = new InlineObjectLiterals();
    Map<String, String> varMap = objLit.computeVarList(var, referenceInfo);
    assertNotNull(varMap);
  }
  
  @Test
  public void testFillInitialValues() {
    InlineObjectLiterals objLit = new InlineObjectLiterals();
    objLit.fillInitialValues(init, initvals);
  }
  
  @Test
  public void testReplaceAssignmentExpression() {
    InlineObjectLiterals objLit = new InlineObjectLiterals();
    objLit.replaceAssignmentExpression(var, ref, varmap);
  }
  
  @Test
  public void testSplitObject() {
    InlineObjectLiterals objLit = new InlineObjectLiterals();
    objLit.splitObject(var, declaration, init, referenceInfo);
  }
}
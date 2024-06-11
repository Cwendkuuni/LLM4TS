import org.junit.Assert;
import org.junit.Test;

public class MustBeReachingVariableDefTest {
    
  @Test
  public void testGetDef() {
      MustBeReachingVariableDef variableDef = new MustBeReachingVariableDef(cfg, jsScope, compiler);
      Node def = variableDef.getDef("variableName", useNode);
      Assert.assertNotNull(def);
  }
  
  @Test
  public void testDependsOnOuterScopeVars() {
      MustBeReachingVariableDef variableDef = new MustBeReachingVariableDef(cfg, jsScope, compiler);
      boolean dependsOnOuterScopeVars = variableDef.dependsOnOuterScopeVars("variableName", useNode);
      Assert.assertFalse(dependsOnOuterScopeVars);
  }
}
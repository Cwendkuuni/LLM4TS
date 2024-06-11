import org.junit.Test;

public class MustBeReachingVariableDefTest {

  @Test
  public void testGetDef() {
    MustBeReachingVariableDef mustBeReachingVariableDef = new MustBeReachingVariableDef(controlFlowGraph, jsScope, compiler);
    Node def = mustBeReachingVariableDef.getDef("A", useNode);
    //Assert.assertEquals(expectedDef, def);
  }

  @Test
  public void testDependsOnOuterScopeVars() {
    MustBeReachingVariableDef mustBeReachingVariableDef = new MustBeReachingVariableDef(controlFlowGraph, jsScope, compiler);
    boolean dependsOnOuterScopeVars = mustBeReachingVariableDef.dependsOnOuterScopeVars("A", useNode);
    //Assert.assertEquals(expectedDependsOnOuterScopeVars, dependsOnOuterScopeVars);
  }

}
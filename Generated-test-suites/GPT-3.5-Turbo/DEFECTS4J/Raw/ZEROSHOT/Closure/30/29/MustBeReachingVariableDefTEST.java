import org.junit.Test;

public class MustBeReachingVariableDefTest {

  @Test
  public void testGetDef() {
    MustBeReachingVariableDef analysis = new MustBeReachingVariableDef(null, null, null);
    Node def = analysis.getDef("name", null);
    assert(def == null);
  }

  @Test
  public void testDependsOnOuterScopeVars() {
    MustBeReachingVariableDef analysis = new MustBeReachingVariableDef(null, null, null);
    boolean dependsOnVars = analysis.dependsOnOuterScopeVars("name", null);
    assert(dependsOnVars == false);
  }

}
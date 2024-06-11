import static org.junit.Assert.*;
import org.junit.Test;

public class MustBeReachingVariableDefTest {

  @Test
  public void testGetDef() {
    MustBeReachingVariableDef variableDef = new MustBeReachingVariableDef(cfg, jsScope, compiler);
    Node def = variableDef.getDef("name", useNode);
    assertEquals(expectedNode, def);
  }
  
  @Test
  public void testDependsOnOuterScopeVars() {
    MustBeReachingVariableDef variableDef = new MustBeReachingVariableDef(cfg, jsScope, compiler);
    boolean depends = variableDef.dependsOnOuterScopeVars("name", useNode);
    assertEquals(expectedDepends, depends);
  }
  
}
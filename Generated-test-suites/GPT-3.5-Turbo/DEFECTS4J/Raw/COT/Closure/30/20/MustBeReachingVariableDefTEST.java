import org.junit.Test;

import static org.junit.Assert.*;

public class MustBeReachingVariableDefTest {

  @Test
  public void testMustBeReachingVariableDef() {
    // code to test the MustBeReachingVariableDef constructor
  }

  @Test
  public void testGetDef_withInvalidName() {
    MustBeReachingVariableDef mustBeReachingVariableDef = new MustBeReachingVariableDef();
    Node defNode = mustBeReachingVariableDef.getDef("invalidName", new Node());
  
    assertNull(defNode);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testGetDef_withInvalidUseNode() {
    MustBeReachingVariableDef mustBeReachingVariableDef = new MustBeReachingVariableDef();
    Node defNode = mustBeReachingVariableDef.getDef("validName", null);
  }

}
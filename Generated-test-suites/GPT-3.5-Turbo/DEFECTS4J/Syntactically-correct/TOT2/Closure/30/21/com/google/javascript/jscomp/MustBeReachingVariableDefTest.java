import static org.junit.Assert.*;
import org.junit.Test;

public class MustBeReachingVariableDefTest {

  @Test
  public void testGetDef() {
    // Test case for getting the reaching definition of a variable at a specific node
    MustBeReachingVariableDef reachDef = new MustBeReachingVariableDef();
    Node defNode = reachDef.getDef("A", someNode);
    assertNotNull(defNode);
  }

  @Test
  public void testDependsOnOuterScopeVars() {
    // Test case for checking if a variable depends on outer scope variables
    MustBeReachingVariableDef reachDef = new MustBeReachingVariableDef();
    boolean depends = reachDef.dependsOnOuterScopeVars("B", someNode);
    assertFalse(depends);
  }

  @Test
  public void testMustDefJoin() {
    // Test case for MustDefJoin binary join operation
    MustBeReachingVariableDef.MustDef a = new MustBeReachingVariableDef.MustDef();
    MustBeReachingVariableDef.MustDef b = new MustBeReachingVariableDef.MustDef();
    a.reachingDef.put(varA, new Definition(node1));
    a.reachingDef.put(varB, new Definition(node2));
    b.reachingDef.put(varA, new Definition(node1));
    b.reachingDef.put(varB, new Definition(node3));
    
    MustBeReachingVariableDef.MustDefJoin joinOp = new MustBeReachingVariableDef.MustDefJoin();
    MustBeReachingVariableDef.MustDef result = joinOp.apply(a, b);
    
    assertEquals(result.reachingDef.get(varA).node, node1);
    assertNull(result.reachingDef.get(varB));
  }

  // Add more test cases as needed

}
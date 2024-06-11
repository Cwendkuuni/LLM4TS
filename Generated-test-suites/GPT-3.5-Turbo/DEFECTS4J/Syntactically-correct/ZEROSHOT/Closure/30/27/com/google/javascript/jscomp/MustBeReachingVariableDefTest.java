import org.junit.Test;

public class MustBeReachingVariableDefTest {
  
  @Test
  public void testGetDef() {
    // Test case 1
    // Create a MustBeReachingVariableDef object
    MustBeReachingVariableDef mbrvd = new MustBeReachingVariableDef(controlFlowGraph, jsScope, compiler);
    
    // Call the getDef method with name = "A" and useNode = node
    Node def = mbrvd.getDef("A", node);
    
    // Assert that the returned node is equal to expectedNode
    assertEquals(expectedNode, def);
    
    // Test case 2
    // Create a MustBeReachingVariableDef object
    MustBeReachingVariableDef mbrvd2 = new MustBeReachingVariableDef(controlFlowGraph, jsScope, compiler);
    
    // Call the getDef method with name = "B" and useNode = node2
    Node def2 = mbrvd2.getDef("B", node2);
    
    // Assert that the returned node is equal to expectedNode2
    assertEquals(expectedNode2, def2);
  }
  
  @Test
  public void testDependsOnOuterScopeVars() {
    // Test case 1
    // Create a MustBeReachingVariableDef object
    MustBeReachingVariableDef mbrvd = new MustBeReachingVariableDef(controlFlowGraph, jsScope, compiler);
    
    // Call the dependsOnOuterScopeVars method with name = "A" and useNode = node
    boolean result = mbrvd.dependsOnOuterScopeVars("A", node);
    
    // Assert that the result is true
    assertEquals(true, result);
    
    // Test case 2
    // Create a MustBeReachingVariableDef object
    MustBeReachingVariableDef mbrvd2 = new MustBeReachingVariableDef(controlFlowGraph, jsScope, compiler);
    
    // Call the dependsOnOuterScopeVars method with name = "B" and useNode = node2
    boolean result2 = mbrvd2.dependsOnOuterScopeVars("B", node2);
    
    // Assert that the result is false
    assertEquals(false, result2);
  }
}
import static org.junit.Assert.*;

import org.junit.Test;

public class MustBeReachingVariableDefTest {

  @Test
  public void testGetDef() {
    MustBeReachingVariableDef variableDef = new MustBeReachingVariableDef();
    
    // Test case 1: Variable is defined and reached the use node
    String name1 = "a";
    Node useNode1 = new Node("alert(a)");
    Node expectedDefNode1 = new Node("a = foo()");
    Node actualDefNode1 = variableDef.getDef(name1, useNode1);
    assertEquals(expectedDefNode1, actualDefNode1);
    
    // Test case 2: Variable is defined but not reached the use node
    String name2 = "b";
    Node useNode2 = new Node("alert(b)");
    Node expectedDefNode2 = null;
    Node actualDefNode2 = variableDef.getDef(name2, useNode2);
    assertEquals(expectedDefNode2, actualDefNode2);
    
    // Test case 3: Variable is not defined
    String name3 = "c";
    Node useNode3 = new Node("alert(c)");
    Node expectedDefNode3 = null;
    Node actualDefNode3 = variableDef.getDef(name3, useNode3);
    assertEquals(expectedDefNode3, actualDefNode3);
  }
  
  @Test
  public void testDependsOnOuterScopeVars() {
    MustBeReachingVariableDef variableDef = new MustBeReachingVariableDef();
    
    // Test case 1: Variable depends on outer scope variables
    String name1 = "a";
    Node useNode1 = new Node("alert(a)");
    boolean expectedResult1 = true;
    boolean actualResult1 = variableDef.dependsOnOuterScopeVars(name1, useNode1);
    assertEquals(expectedResult1, actualResult1);
    
    // Test case 2: Variable does not depend on outer scope variables
    String name2 = "b";
    Node useNode2 = new Node("alert(b)");
    boolean expectedResult2 = false;
    boolean actualResult2 = variableDef.dependsOnOuterScopeVars(name2, useNode2);
    assertEquals(expectedResult2, actualResult2);
  }
}
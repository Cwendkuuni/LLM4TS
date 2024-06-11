import junit.framework.TestCase;
import org.junit.Test;

public class MustBeReachingVariableDefTest {

  // Create instance of MustBeReachingVariableDef
  MustBeReachingVariableDef variableDef = new MustBeReachingVariableDef();

  // Test computeMustDef() method
  @Test
  public void testComputeMustDef() {
    // Create nodes
    Node n = new Node(1);
    Node cfgNode = new Node(2);
    MustBeReachingVariableDef.MustDef output = new MustBeReachingVariableDef.MustDef();
    boolean conditional = false;
    
    // Call computeMustDef() method
    variableDef.computeMustDef(n, cfgNode, output, conditional);
  }

  // Test addToDefIfLocal() method
  @Test
  public void testAddToDefIfLocal() {
    // Create variables
    String name = "testName";
    Node node = new Node(1);
    Node rValue = new Node(2);
    MustBeReachingVariableDef.MustDef def = new MustBeReachingVariableDef.MustDef();
    
    // Call addToDefIfLocal() method
    variableDef.addToDefIfLocal(name, node, rValue, def);
  }

  // Test escapeParameters() method
  @Test
  public void testEscapeParameters() {
    // Create variable
    MustBeReachingVariableDef.MustDef output = new MustBeReachingVariableDef.MustDef();
    
    // Call escapeParameters() method
    variableDef.escapeParameters(output);
  }

  // Test isParameter() method
  @Test
  public void testIsParameter() {
    // Create variable
    Var v = new Var();
    
    // Call isParameter() method
    variableDef.isParameter(v);
  }

  // Test computeDependence() method
  @Test
  public void testComputeDependence() {
    // Create variables
    MustBeReachingVariableDef.Definition def = new MustBeReachingVariableDef.Definition();
    Node rValue = new Node();
    
    // Call computeDependence() method
    variableDef.computeDependence(def, rValue);
  }

  // Test getDef() method
  @Test
  public void testGetDef() {
    // Create variables
    String name = "testName";
    Node useNode = new Node();
    
    // Call getDef() method
    variableDef.getDef(name, useNode);
  }

  // Test dependsOnOuterScopeVars() method
  @Test
  public void testDependsOnOuterScopeVars() {
    // Create variables
    String name = "testName";
    Node useNode = new Node();
    
    // Call dependsOnOuterScopeVars() method
    variableDef.dependsOnOuterScopeVars(name, useNode);
  }
}
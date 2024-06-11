import static org.junit.Assert.*;
import org.junit.Test;

public class MustBeReachingVariableDefTest {

  @Test
  public void testCreateEntryLattice() {
    // Test the creation of the entry lattice
    MustBeReachingVariableDef.MustDef lattice = new MustBeReachingVariableDef.MustDef();
    
    // Check that the lattice is not null
    assertNotNull(lattice);
    
    // Check that the lattice's reachingDef map is empty
    assertTrue(lattice.reachingDef.isEmpty());
  }

  @Test
  public void testCreateInitialEstimateLattice() {
    // Test the creation of the initial estimate lattice
    MustBeReachingVariableDef.MustDef lattice = new MustBeReachingVariableDef.MustDef(jsScope.getVars());
    
    // Check that the lattice is not null
    assertNotNull(lattice);
    
    // Check that the lattice's reachingDef map contains all variables in jsScope
    assertEquals(jsScope.getVars().size(), lattice.reachingDef.size());
  }

  @Test
  public void testGetDef() {
    // Test getting the reaching definition of a variable at a specific use node
    MustBeReachingVariableDef analysis = new MustBeReachingVariableDef(cfg, jsScope, compiler);

    // Test a variable that has a defined reaching definition at a use node
    Node defNode = analysis.getDef("varName", useNode);
    assertNotNull(defNode);
    
    // Test a variable that does not have a defined reaching definition at a use node
    Node defNodeNull = analysis.getDef("undefinedVar", useNode);
    assertNull(defNodeNull);
  }

  @Test
  public void testDependsOnOuterScopeVars() {
    // Test checking if a variable depends on outer scope variables at a specific use node
    MustBeReachingVariableDef analysis = new MustBeReachingVariableDef(cfg, jsScope, compiler);

    // Test a variable that depends on outer scope variables at a use node
    boolean depends = analysis.dependsOnOuterScopeVars("varName", useNode);
    assertTrue(depends);

    // Test a variable that does not depend on outer scope variables at a use node
    boolean doesNotDepend = analysis.dependsOnOuterScopeVars("localVar", useNode);
    assertFalse(doesNotDepend);
  }

}
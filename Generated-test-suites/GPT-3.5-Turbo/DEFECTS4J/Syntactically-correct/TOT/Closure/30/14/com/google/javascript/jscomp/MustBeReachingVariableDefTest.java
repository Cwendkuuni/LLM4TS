import static org.junit.Assert.*;

import org.junit.Test;

public class MustBeReachingVariableDefTest {

  @Test
  public void testGetDef_ValidName_ReturnsNode() {
    // Arrange
    MustBeReachingVariableDef variableDef = new MustBeReachingVariableDef();
    
    // Act
    Node result = variableDef.getDef("name", useNode);
    
    // Assert
    assertNotNull(result);
  }
  
  @Test
  public void testGetDef_InvalidName_ReturnsNull() {
    // Arrange
    MustBeReachingVariableDef variableDef = new MustBeReachingVariableDef();
    
    // Act
    Node result = variableDef.getDef("invalidName", useNode);
    
    // Assert
    assertNull(result);
  }
  
  @Test
  public void testDependsOnOuterScopeVars_WithOuterScopeVars_ReturnsTrue() {
    // Arrange
    MustBeReachingVariableDef variableDef = new MustBeReachingVariableDef();
    
    // Act
    boolean result = variableDef.dependsOnOuterScopeVars("name", useNode);
    
    // Assert
    assertTrue(result);
  }
  
  @Test
  public void testDependsOnOuterScopeVars_WithoutOuterScopeVars_ReturnsFalse() {
    // Arrange
    MustBeReachingVariableDef variableDef = new MustBeReachingVariableDef();
    
    // Act
    boolean result = variableDef.dependsOnOuterScopeVars("name2", useNode);
    
    // Assert
    assertFalse(result);
  }
}
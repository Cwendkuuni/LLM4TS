import org.junit.Test;
import static org.junit.Assert.*;

public class SubTypeValidatorTest {
  
  // Test case for validateSubType method
  @Test
  public void testValidateSubType() {
    // Typical use case
    assertDoesNotThrow(() -> SubTypeValidator.instance().validateSubType(null, null));
    
    // Edge case: Type is null
    try {
      SubTypeValidator.instance().validateSubType(null, null);
      fail("Expected JsonMappingException to be thrown");
    } catch (JsonMappingException e) {
      assertEquals("Illegal type (null) to deserialize: prevented for security reasons", e.getMessage());
    }
    
    // Edge case: Type is not in the set of illegal class names
    try {
      SubTypeValidator.instance()._cfgIllegalClassNames = Collections.emptySet();
      SubTypeValidator.instance().validateSubType(null, null);
    } catch (JsonMappingException e) {
      fail("Unexpected JsonMappingException");
    }
    
    // Error scenario: Type is in the set of illegal class names
    try {
      SubTypeValidator.instance()._cfgIllegalClassNames = new HashSet<>();
      SubTypeValidator.instance()._cfgIllegalClassNames.add("com.fasterxml.jackson.databind.JsonNode");
      SubTypeValidator.instance().validateSubType(null, null);
      fail("Expected JsonMappingException to be thrown");
    } catch (JsonMappingException e) {
      assertEquals("Illegal type (com.fasterxml.jackson.databind.JsonNode) to deserialize: prevented for security reasons", e.getMessage());
    }
  }

  // Test case for instance method
  @Test
  public void testInstance() {
    // Typical use case
    assertNotNull(SubTypeValidator.instance());
  }
}
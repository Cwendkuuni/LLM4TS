
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import org.junit.Before;
import org.junit.Test;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;

public class SubTypeValidatorTest {
  
  private SubTypeValidator subTypeValidator;
  private DeserializationContext deserializationContext;
  
  @Before
  public void setUp() {
    subTypeValidator = SubTypeValidator.instance();
    deserializationContext = new DeserializationContext(null, null, null);
  }
  
  @Test
  public void testValidateSubType_validType() {
    try {
      subTypeValidator.validateSubType(deserializationContext, JavaType.construct(String.class));
    } catch (JsonMappingException e) {
      fail("Unexpected JsonMappingException");
    }
  }
  
  @Test
  public void testValidateSubType_invalidType() {
    try {
      subTypeValidator.validateSubType(deserializationContext, JavaType.construct(SubTypeValidator.class));
      fail("Expected JsonMappingException");
    } catch (JsonMappingException e) {
      assertEquals("Illegal type (com.fasterxml.jackson.databind.jsontype.impl.SubTypeValidator) to deserialize: prevented for security reasons", e.getMessage());
    }
  }
}

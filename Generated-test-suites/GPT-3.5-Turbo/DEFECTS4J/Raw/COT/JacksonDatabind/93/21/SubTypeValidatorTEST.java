
import org.junit.Test;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import static org.mockito.Mockito.*;

public class SubTypeValidatorTest {

  @Test
  public void testValidateSubType() throws JsonMappingException {
    DeserializationContext ctxt = mock(DeserializationContext.class);
    JavaType type = mock(JavaType.class);
    SubTypeValidator validator = SubTypeValidator.instance();

    // Basic test case
    validator.validateSubType(ctxt, type);

    // Test for illegal class
    when(type.getRawClass()).thenReturn(org.springframework.beans.factory.config.PropertyPathFactoryBean.class);
    try {
      validator.validateSubType(ctxt, type);
      fail("Expected JsonMappingException");
    } catch (JsonMappingException e) {
      assertEquals("Illegal type (org.springframework.beans.factory.config.PropertyPathFactoryBean) to deserialize: prevented for security reasons", e.getMessage());
    }

    // Test for spring framework types
    when(type.getRawClass()).thenReturn(org.springframework.context.support.AbstractApplicationContext.class);
    try {
      validator.validateSubType(ctxt, type);
      fail("Expected JsonMappingException");
    } catch (JsonMappingException e) {
      assertEquals("Illegal type (org.springframework.context.support.AbstractApplicationContext) to deserialize: prevented for security reasons", e.getMessage());
    }
  }
}

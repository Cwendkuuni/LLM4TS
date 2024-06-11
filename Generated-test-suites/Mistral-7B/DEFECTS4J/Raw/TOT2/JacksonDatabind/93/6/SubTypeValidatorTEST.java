import static org.junit.Assert.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.Test;

import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;

public class SubTypeValidatorTest {

    @Test
    public void testValidateSubType_ValidType() {
        DeserializationContext ctxt = mock(DeserializationContext.class);
        JavaType type = mock(JavaType.class);

        SubTypeValidator validator = SubTypeValidator.instance();
        validator.validateSubType(ctxt, type);
    }

    @Test
    public void testValidateSubType_InvalidType() {
        DeserializationContext ctxt = mock(DeserializationContext.class);
        JavaType type = mock(JavaType.class);
        when(type.getRawClass().getName()).thenReturn("org.springframework.beans.factory.config.PropertyPathFactoryBean");

        SubTypeValidator validator = SubTypeValidator.instance();

        assertThrows(JsonMappingException.class, () -> validator.validateSubType(ctxt, type));
    }
}
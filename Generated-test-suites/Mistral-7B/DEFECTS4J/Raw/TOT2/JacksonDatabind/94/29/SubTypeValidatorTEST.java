import static org.junit.Assert.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;

import org.junit.Test;

public class SubTypeValidatorTest {

    @Test
    public void testValidateSubType_ValidType() throws Exception {
        DeserializationContext context = mock(DeserializationContext.class);
        JavaType type = mock(JavaType.class);

        SubTypeValidator validator = SubTypeValidator.instance();
        validator.validateSubType(context, type);
        // No exception expected
    }

    @Test
    public void testValidateSubType_InvalidType() throws Exception {
        DeserializationContext context = mock(DeserializationContext.class);
        JavaType type = mock(JavaType.class);

        when(type.getRawClass().getName()).thenReturn("org.apache.commons.collections4.functors.InstantiateTransformer");

        SubTypeValidator validator = SubTypeValidator.instance();
        assertThrows(JsonMappingException.class, () -> validator.validateSubType(context, type));
        // JsonMappingException expected
    }
}
import org.junit.Test;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class SubTypeValidatorTest {

    @Test
    public void testValidateSubType_ValidType() {
        DeserializationContext context = mock(DeserializationContext.class);
        JavaType validType = mock(JavaType.class);

        when(validType.getRawClass().getName()).thenReturn("com.valid.class.Name");

        SubTypeValidator validator = SubTypeValidator.instance();
        validator.validateSubType(context, validType);
    }

    @Test
    public void testValidateSubType_NastyClass() {
        DeserializationContext context = mock(DeserializationContext.class);
        JavaType nastyType = mock(JavaType.class);

        when(nastyType.getRawClass().getName()).thenReturn("org.apache.commons.collections.functors.InvokerTransformer");

        SubTypeValidator validator = SubTypeValidator.instance();
        try {
            validator.validateSubType(context, nastyType);
            fail("Expected JsonMappingException");
        } catch (JsonMappingException e) {
            // Expected
        }
    }

    @Test
    public void testValidateSubType_SpringClass() {
        DeserializationContext context = mock(DeserializationContext.class);
        JavaType springType = mock(JavaType.class);

        when(springType.getRawClass().getName()).thenReturn("org.springframework.beans.factory.config.PropertyPathFactoryBean");

        SubTypeValidator validator = SubTypeValidator.instance();
        try {
            validator.validateSubType(context, springType);
            fail("Expected JsonMappingException");
        } catch (JsonMappingException e) {
            // Expected
        }
    }

    @Test
    public void testValidateSubType_EmptyName() {
        DeserializationContext context = mock(DeserializationContext.class);
        JavaType emptyType = mock(JavaType.class);

        when(emptyType.getRawClass().getName()).thenReturn("");

        SubTypeValidator validator = SubTypeValidator.instance();
        try {
            validator.validateSubType(context, emptyType);
            fail("Expected JsonMappingException");
        } catch (JsonMappingException e) {
            // Expected
        }
    }

    @Test
    public void testValidateSubType_NullType() {
        DeserializationContext context = mock(DeserializationContext.class);
        JavaType nullType = null;

        SubTypeValidator validator = SubTypeValidator.instance();
        try {
            validator.validateSubType(context, nullType);
            fail("Expected JsonMappingException");
        } catch (JsonMappingException e) {
            // Expected
        }
    }
}
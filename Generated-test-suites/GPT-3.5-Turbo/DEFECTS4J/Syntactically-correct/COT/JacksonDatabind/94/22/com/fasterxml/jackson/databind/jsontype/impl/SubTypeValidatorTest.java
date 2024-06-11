
import org.junit.Test;
import org.mockito.Mockito;

import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class SubTypeValidatorTest {

    private SubTypeValidator validator = SubTypeValidator.instance();

    @Test
    public void testValidateSubType() {
        DeserializationContext ctxt = mock(DeserializationContext.class);
        JavaType type = mock(JavaType.class);

        // Test case for a valid sub type
        Class<?> validRawClass = ValidSubType.class;
        when(type.getRawClass()).thenReturn(validRawClass);
        validator.validateSubType(ctxt, type);

        // Test case for an invalid sub type
        Class<?> invalidRawClass = InvalidSubType.class;
        when(type.getRawClass()).thenReturn(invalidRawClass);
        assertThrows(JsonMappingException.class, () -> validator.validateSubType(ctxt, type));
    }

    @Test
    public void testValidateSubTypeWithIllegalClassNames() {
        DeserializationContext ctxt = mock(DeserializationContext.class);
        JavaType type = mock(JavaType.class);
        Set<String> illegalClassNames = new HashSet<>();
        illegalClassNames.add("org.apache.commons.collections.functors.InvokerTransformer");
        illegalClassNames.add("org.springframework.beans.factory.ObjectFactory");

        // If _cfgIllegalClassNames contains an illegal class name, JsonMappingException should be thrown
        setIllegalClassNames(illegalClassNames);
        when(type.getRawClass()).thenReturn(InvalidSubType.class);
        assertThrows(JsonMappingException.class, () -> validator.validateSubType(ctxt, type));

        // If _cfgIllegalClassNames does not contain an illegal class name, the method should not throw an exception
        setIllegalClassNames(Collections.emptySet());
        when(type.getRawClass()).thenReturn(InvalidSubType.class);
        validator.validateSubType(ctxt, type);
    }

    private void setIllegalClassNames(Set<String> illegalClassNames) {
        try {
            Field field = SubTypeValidator.class.getDeclaredField("_cfgIllegalClassNames");
            field.setAccessible(true);
            field.set(validator, illegalClassNames);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testInstance() {
        SubTypeValidator instance1 = SubTypeValidator.instance();
        SubTypeValidator instance2 = SubTypeValidator.instance();
        assertSame(instance1, instance2);
    }

    @Test
    public void testConstructor() {
        SubTypeValidator instance = new SubTypeValidator();
        assertNotNull(instance);
    }
}

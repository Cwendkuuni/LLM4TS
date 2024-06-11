import static org.junit.Assert.*;
import org.junit.Test;
import org.mockito.Mockito;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;

public class SubTypeValidatorTest {

    @Test
    public void testValidateSubType_ValidClass() {
        DeserializationContext ctxt = Mockito.mock(DeserializationContext.class);
        JavaType type = Mockito.mock(JavaType.class);
        Class<?> raw = Class.class;
        Mockito.when(type.getRawClass()).thenReturn(raw);
        SubTypeValidator validator = SubTypeValidator.instance();

        try {
            validator.validateSubType(ctxt, type);
        } catch (JsonMappingException e) {
            fail("Unexpected exception: " + e);
        }
    }

    @Test
    public void testValidateSubType_IllegalClass() {
        DeserializationContext ctxt = Mockito.mock(DeserializationContext.class);
        JavaType type = Mockito.mock(JavaType.class);
        Class<?> raw = IllegalClassA.class;
        Mockito.when(type.getRawClass()).thenReturn(raw);
        SubTypeValidator validator = SubTypeValidator.instance();

        try {
            validator.validateSubType(ctxt, type);
            fail("Expected JsonMappingException to be thrown");
        } catch (JsonMappingException e) {
            assertEquals("Illegal type (com.example.IllegalClassA) to deserialize: prevented for security reasons", e.getMessage());
        }
    }

    @Test
    public void testValidateSubType_Interface() {
        DeserializationContext ctxt = Mockito.mock(DeserializationContext.class);
        JavaType type = Mockito.mock(JavaType.class);
        Class<?> raw = Interface.class;
        Mockito.when(type.getRawClass()).thenReturn(raw);
        SubTypeValidator validator = SubTypeValidator.instance();

        try {
            validator.validateSubType(ctxt, type);
        } catch (JsonMappingException e) {
            fail("Unexpected exception: " + e);
        }
    }

    @Test
    public void testValidateSubType_SpringClass() {
        DeserializationContext ctxt = Mockito.mock(DeserializationContext.class);
        JavaType type = Mockito.mock(JavaType.class);
        Class<?> raw = SpringClass.class;
        Mockito.when(type.getRawClass()).thenReturn(raw);
        SubTypeValidator validator = SubTypeValidator.instance();

        try {
            validator.validateSubType(ctxt, type);
            fail("Expected JsonMappingException to be thrown");
        } catch (JsonMappingException e) {
            assertEquals("Illegal type (org.springframework.SpringClass) to deserialize: prevented for security reasons", e.getMessage());
        }
    }
}

class IllegalClassA {}

interface Interface {}

class SpringClass {}
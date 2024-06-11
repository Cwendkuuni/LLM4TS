
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import org.junit.Test;

import static org.mockito.Mockito.*;

public class SubTypeValidatorTest {
    SubTypeValidator subTypeValidator = SubTypeValidator.instance();

    @Test
    public void testValidateSubType_ValidSubType_NoExceptionThrown() throws JsonMappingException {
        DeserializationContext ctxt = mock(DeserializationContext.class);
        JavaType type = mock(JavaType.class);
        subTypeValidator.validateSubType(ctxt, type);

        // Verify that no exception is thrown
        verifyZeroInteractions(ctxt);
    }

    @Test(expected = JsonMappingException.class)
    public void testValidateSubType_IllegalSubType_ExceptionThrown() throws JsonMappingException {
        DeserializationContext ctxt = mock(DeserializationContext.class);
        JavaType type = mock(JavaType.class);

        // Set an illegal subtype
        when(type.getRawClass()).thenReturn(SubTypeValidator.class);

        subTypeValidator.validateSubType(ctxt, type);
    }
}

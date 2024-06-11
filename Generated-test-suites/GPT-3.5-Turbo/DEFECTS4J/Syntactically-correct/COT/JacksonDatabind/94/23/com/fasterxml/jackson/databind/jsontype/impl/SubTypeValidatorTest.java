
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;
import static org.mockito.Mockito.when;

public class SubTypeValidatorTest {

    @Mock
    private DeserializationContext deserializationContext;

    @Mock
    private JavaType javaType;

    private SubTypeValidator subTypeValidator;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        subTypeValidator = SubTypeValidator.instance();
    }

    @Test
    public void testValidateSubType_NoExceptionsThrown() {
        try {
            subTypeValidator.validateSubType(deserializationContext, javaType);
        } catch (JsonMappingException e) {
            assertEquals("An exception was thrown", null, e);
        }
    }

    @Test
    public void testValidateSubType_ThrowsJsonMappingException() {
        when(javaType.getRawClass()).thenReturn(Object.class);
        assertThrows(JsonMappingException.class, () -> {
            subTypeValidator.validateSubType(deserializationContext, javaType);
        });
    }

    @Test
    public void testValidateSubType_ThrowsJsonMappingExceptionWithIllegalTypeMessage() {
        when(javaType.getRawClass()).thenReturn(String.class);
        JsonMappingException exception = assertThrows(JsonMappingException.class, () -> {
            subTypeValidator.validateSubType(deserializationContext, javaType);
        });
        assertEquals("Illegal type (java.lang.String) to deserialize: prevented for security reasons",
                exception.getMessage());
    }
}

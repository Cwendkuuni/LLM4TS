import org.junit.Before;
import org.junit.Test;
import static org.mockito.Mockito.*;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import java.util.Set;
import java.util.HashSet;
import java.util.Collections;

public class SubTypeValidatorTest {
    private DeserializationContext ctxt;
    private JavaType type;
    private SubTypeValidator subTypeValidator;

    @Before
    public void setUp() {
        ctxt = mock(DeserializationContext.class);
        type = mock(JavaType.class);
        subTypeValidator = SubTypeValidator.instance();
        subTypeValidator._cfgIllegalClassNames = Collections.unmodifiableSet(new HashSet<String>());
    }

    @Test
    public void testValidateSubType_validSubType_noExceptionThrown() throws JsonMappingException {
        // Arrange
        when(type.getRawClass()).thenReturn(Object.class);

        // Act
        subTypeValidator.validateSubType(ctxt, type);

        // Assert
        // No exception should be thrown
    }

    @Test(expected = Exception.class)
    public void testValidateSubType_invalidSubTypeJsonMappingExceptionThrown() throws JsonMappingException {
        // Arrange
        when(type.getRawClass()).thenReturn(String.class);

        // Act
        subTypeValidator.validateSubType(ctxt, type);

        // Assert
        // Expecting JsonMappingException to be thrown
    }
}
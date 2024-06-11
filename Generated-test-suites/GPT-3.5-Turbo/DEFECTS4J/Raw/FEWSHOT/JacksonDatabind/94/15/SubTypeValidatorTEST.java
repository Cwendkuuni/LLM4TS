import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.mock;

import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import org.junit.Before;
import org.junit.Test;

public class SubTypeValidatorTest {
    private SubTypeValidator subTypeValidator;
    private DeserializationContext deserializationContext;
    private JavaType javaType;

    @Before
    public void setUp() {
        subTypeValidator = SubTypeValidator.instance();
        deserializationContext = mock(DeserializationContext.class);
        javaType = mock(JavaType.class);
    }
    
    @Test
    public void testValidateSubType_IllegalType() {
        try {
            subTypeValidator.validateSubType(deserializationContext, javaType);
            fail("Expected JsonMappingException to be thrown");
        } catch (JsonMappingException e) {
            assertEquals("Illegal type (java.util.logging.FileHandler) to deserialize: prevented for security reasons", e.getMessage());
        }
    }
    
    @Test
    public void testValidateSubType_ValidType() {
        try {
            subTypeValidator.validateSubType(deserializationContext, javaType);
        } catch (JsonMappingException e) {
            fail("JsonMappingException should not be thrown");
        }
    }
}
import static org.junit.Assert.*;

import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;

public class SubTypeValidatorTest {
    
    private static SubTypeValidator subTypeValidator;
    
    @Mock
    private DeserializationContext mockDeserializationContext;
    
    @Mock
    private JavaType mockJavaType;
    
    @BeforeClass
    public static void setUp() {
        subTypeValidator = SubTypeValidator.instance();
    }
    
    @Test
    public void testValidateSubType_ValidType_NoExceptionThrown() {
        try {
            subTypeValidator.validateSubType(mockDeserializationContext, mockJavaType);
        } catch (JsonMappingException e) {
            fail("Unexpected exception thrown");
        }
    }
    
    @Test
    public void testValidateSubType_IllegalType_ExceptionThrown() {
        try {
            subTypeValidator.validateSubType(mockDeserializationContext, 
                                             JavaType.builder().rawClass(IllegalClass.class).build());
            fail("Expected JsonMappingException not thrown");
        } catch (JsonMappingException e) {
            assertEquals("Illegal type (com.fasterxml.jackson.databind.jsontype.impl.SubTypeValidatorTest$IllegalClass) to deserialize: prevented for security reasons", e.getMessage());
        }
    }
    
    private static class IllegalClass {}
}
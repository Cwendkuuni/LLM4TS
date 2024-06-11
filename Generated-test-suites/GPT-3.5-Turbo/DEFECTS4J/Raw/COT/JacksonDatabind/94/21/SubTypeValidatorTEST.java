
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonMappingException;
import org.junit.Assert;
import org.junit.Test;

public class SubTypeValidatorTest {

    private SubTypeValidator subTypeValidator = SubTypeValidator.instance();
    private DeserializationContext deserializationContext;
    
    @Test
    public void testValidateSubType_validSubType_noException() {
        JavaType javaType = new JavaType();
        try {
            subTypeValidator.validateSubType(deserializationContext, javaType);
        } catch (JsonMappingException e) {
            Assert.fail("Unexpected exception thrown: " + e.getMessage());
        }
    }
    
    @Test(expected = JsonMappingException.class)
    public void testValidateSubType_invalidSubType_throwJsonMappingException() throws JsonMappingException {
        JavaType javaType = new JavaType();
        subTypeValidator.validateSubType(deserializationContext, javaType);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testValidateSubType_nullDeserializationContext_throwIllegalArgumentException() throws JsonMappingException {
        JavaType javaType = new JavaType();
        deserializationContext = null;
        subTypeValidator.validateSubType(deserializationContext, javaType);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testValidateSubType_nullJavaType_throwIllegalArgumentException() throws JsonMappingException {
        JavaType javaType = null;
        subTypeValidator.validateSubType(deserializationContext, javaType);
    }
    
    @Test
    public void testValidateSubType_interfaceAsJavaType_noException() {
        JavaType javaType = new JavaType();
        javaType.setRawClass(Set.class);
        try {
            subTypeValidator.validateSubType(deserializationContext, javaType);
        } catch (JsonMappingException e) {
            Assert.fail("Unexpected exception thrown: " + e.getMessage());
        }
    }
    
    @Test(expected = JsonMappingException.class)
    public void testValidateSubType_invalidNastyClass_throwJsonMappingException() throws JsonMappingException {
        JavaType javaType = new JavaType();
        javaType.setRawClass(org.codehaus.groovy.runtime.ConvertedClosure.class);
        subTypeValidator.validateSubType(deserializationContext, javaType);
    }
}

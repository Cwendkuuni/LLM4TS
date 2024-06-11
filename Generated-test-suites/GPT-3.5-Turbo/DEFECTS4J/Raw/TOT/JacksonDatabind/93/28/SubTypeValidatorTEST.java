import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import org.junit.Test;
import static org.junit.Assert.*;

public class SubTypeValidatorTest {

    private SubTypeValidator validator;

    public SubTypeValidatorTest() {
        validator = SubTypeValidator.instance();
    }

    @Test
    public void testValidateSubType_ValidType_NoException() {
        try {
            DeserializationContext ctxt = new DeserializationContext(null, null);
            JavaType type = ctxt.getTypeFactory().constructType(String.class);
            validator.validateSubType(ctxt, type);
            assertTrue(true);
        } catch (JsonMappingException e) {
            fail("An unexpected exception occurred");
        }
    }

    @Test
    public void testValidateSubType_IllegalClassName_ThrowsException() {
        try {
            DeserializationContext ctxt = new DeserializationContext(null, null);
            JavaType type = ctxt.getTypeFactory().constructType(Object.class);
            validator.validateSubType(ctxt, type);
            fail("Expected JsonMappingException was not thrown");
        } catch (JsonMappingException e) {
            assertEquals("Illegal type (java.lang.Object) to deserialize: prevented for security reasons", e.getMessage());
        }
    }

    @Test
    public void testValidateSubType_SpringFrameworkType_ThrowsException() {
        try {
            DeserializationContext ctxt = new DeserializationContext(null, null);
            JavaType type = ctxt.getTypeFactory().constructType(AbstractPointcutAdvisor.class);
            validator.validateSubType(ctxt, type);
            fail("Expected JsonMappingException was not thrown");
        } catch (JsonMappingException e) {
            assertEquals("Illegal type (com.springframework.AbstractPointcutAdvisor) to deserialize: prevented for security reasons", e.getMessage());
        }
    }

    @Test
    public void testValidateSubType_SuperclassSpringFrameworkType_NoException() {
        try {
            DeserializationContext ctxt = new DeserializationContext(null, null);
            JavaType type = ctxt.getTypeFactory().constructType(FileSystemXmlApplicationContext.class);
            validator.validateSubType(ctxt, type);
            assertTrue(true);
        } catch (JsonMappingException e) {
            fail("An unexpected exception occurred");
        }
    }
}
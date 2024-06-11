import org.junit.Test;
import com.fasterxml.jackson.core.JsonMappingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import static org.junit.Assert.*;

public class SubTypeValidatorTest {

    private SubTypeValidator validator = new SubTypeValidator();

    @Test
    public void testValidateSubType_ValidJavaType_NoException() {
        DeserializationContext ctxt = null;
        JavaType type = JavaType.forType(String.class);
        try {
            validator.validateSubType(ctxt, type);
        } catch (JsonMappingException e) {
            fail("Unexpected exception thrown");
        }
    }

    @Test
    public void testValidateSubType_IllegalJavaType_JsonMappingExceptionThrown() {
        DeserializationContext ctxt = null;
        JavaType type = JavaType.forType("org.springframework.beans.factory.ObjectFactory");
        try {
            validator.validateSubType(ctxt, type);
            fail("Expected JsonMappingException was not thrown");
        } catch (JsonMappingException e) {
            assertEquals("Illegal type (org.springframework.beans.factory.ObjectFactory) to deserialize: prevented for security reasons", e.getMessage());
        }
    }

    @Test
    public void testValidateSubType_AbstractPointcutAdvisor_JsonMappingExceptionThrown() {
        DeserializationContext ctxt = null;
        JavaType type = JavaType.forType("org.springframework.aop.support.AbstractBeanFactoryPointcutAdvisor");
        try {
            validator.validateSubType(ctxt, type);
            fail("Expected JsonMappingException was not thrown");
        } catch (JsonMappingException e) {
            assertEquals("Illegal type (org.springframework.aop.support.AbstractBeanFactoryPointcutAdvisor) to deserialize: prevented for security reasons", e.getMessage());
        }
    }
}
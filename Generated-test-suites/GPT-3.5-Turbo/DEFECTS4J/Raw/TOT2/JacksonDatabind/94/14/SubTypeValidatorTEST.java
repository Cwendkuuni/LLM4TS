import static org.junit.Assert.*;
import org.junit.Test;

public class SubTypeValidatorTest {

    @Test
    public void testInstance() {
        assertNotNull(SubTypeValidator.instance());
    }

    @Test
    public void testValidateSubType() throws JsonMappingException {
        SubTypeValidator validator = SubTypeValidator.instance();
        DeserializationContext context = new DeserializationContext(null, null, null);
        JavaType type = new JavaType();
        
        // Typical use case
        validator.validateSubType(context, type);
        
        // Edge case: testing illegal class name
        try{
            validator.validateSubType(context, new JavaType(Class.forName("org.apache.commons.collections.functors.InvokerTransformer")));
            fail("Should throw JsonMappingException");
        } catch (JsonMappingException e) {
            assertEquals("Illegal type (org.apache.commons.collections.functors.InvokerTransformer) to deserialize: prevented for security reasons", e.getMessage());
        }
        
        // Edge case: testing Spring framework types
        try{
            validator.validateSubType(context, new JavaType(Class.forName("org.springframework.beans.factory.config.PropertyPathFactoryBean")));
            fail("Should throw JsonMappingException");
        } catch (JsonMappingException e) {
            assertEquals("Illegal type (org.springframework.beans.factory.config.PropertyPathFactoryBean) to deserialize: prevented for security reasons", e.getMessage());
        }
    }

}
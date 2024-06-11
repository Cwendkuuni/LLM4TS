import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;

public class SubTypeValidatorTest {

    @Test
    public void testValidateSubType_validClass() throws JsonMappingException {
        // Arrange
        SubTypeValidator validator = SubTypeValidator.instance();
        DeserializationContext context = Mockito.mock(DeserializationContext.class);
        JavaType type = Mockito.mock(JavaType.class);
        
        // Act
        validator.validateSubType(context, type);
        
        // Assert
        // No exception should be thrown as the class is not in the list of illegal classes
    }
    
    @Test(expected = JsonMappingException.class)
    public void testValidateSubType_illegalClass() throws JsonMappingException {
        // Arrange
        SubTypeValidator validator = SubTypeValidator.instance();
        DeserializationContext context = Mockito.mock(DeserializationContext.class);
        JavaType type = Mockito.mock(JavaType.class);
        Mockito.when(type.getRawClass()).thenReturn(org.apache.commons.collections.functors.InvokerTransformer.class);
        
        // Act
        validator.validateSubType(context, type);
        
        // Assert
        // Exception should be thrown as the class is in the list of illegal classes
    }
    
    @Test
    public void testValidateSubType_springFrameworkClass() throws JsonMappingException {
        // Arrange
        SubTypeValidator validator = SubTypeValidator.instance();
        DeserializationContext context = Mockito.mock(DeserializationContext.class);
        JavaType type = Mockito.mock(JavaType.class);
        Mockito.when(type.getRawClass()).thenReturn(org.springframework.beans.factory.ObjectFactory.class);
        
        // Act
        try {
            validator.validateSubType(context, type);
        } catch (JsonMappingException ex) {
            Assert.fail("Exception should not be thrown for a spring framework class");
        }
        
        // Assert
        // No exception should be thrown as the spring framework class is handled differently
    }
}
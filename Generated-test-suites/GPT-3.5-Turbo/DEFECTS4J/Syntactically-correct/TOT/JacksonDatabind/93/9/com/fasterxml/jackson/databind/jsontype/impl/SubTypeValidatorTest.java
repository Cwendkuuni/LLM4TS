import static org.junit.Assert.*;

import org.junit.Test;

public class SubTypeValidatorTest {

    @Test
    public void testValidateSubType_ValidType() throws Exception {
        // Arrange
        SubTypeValidator validator = SubTypeValidator.instance();
        DeserializationContext ctxt = new DeserializationContext();
        JavaType type = new JavaType();
        type.setRawClass(Object.class);

        // Act
        validator.validateSubType(ctxt, type);

        // Assert
        // No exception should be thrown
    }

    @Test(expected = JsonMappingException.class)
    public void testValidateSubType_IllegalType() throws Exception {
        // Arrange
        SubTypeValidator validator = SubTypeValidator.instance();
        DeserializationContext ctxt = new DeserializationContext();
        JavaType type = new JavaType();
        type.setRawClass(AbstractBeanFactoryPointcutAdvisor.class);

        // Act
        validator.validateSubType(ctxt, type);

        // Assert
        // JsonMappingException should be thrown
    }

    @Test
    public void testValidateSubType_ValidSpringType() throws Exception {
        // Arrange
        SubTypeValidator validator = SubTypeValidator.instance();
        DeserializationContext ctxt = new DeserializationContext();
        JavaType type = new JavaType();
        type.setRawClass(AbstractApplicationContext.class);

        // Act
        validator.validateSubType(ctxt, type);

        // Assert
        // No exception should be thrown
    }

    @Test(expected = JsonMappingException.class)
    public void testValidateSubType_IllegalSpringType() throws Exception {
        // Arrange
        SubTypeValidator validator = SubTypeValidator.instance();
        DeserializationContext ctxt = new DeserializationContext();
        JavaType type = new JavaType();
        type.setRawClass(FileSystemXmlApplicationContext.class);

        // Act
        validator.validateSubType(ctxt, type);

        // Assert
        // JsonMappingException should be thrown
    }
}
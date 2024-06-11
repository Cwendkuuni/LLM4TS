import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.HashSet;
import java.util.Set;

public class SubTypeValidatorTest {

    @Test
    public void validateSubType_ValidType_ExceptionNotThrown() {
        // Arrange
        SubTypeValidator validator = SubTypeValidator.instance();
        DeserializationContext ctxt = Mockito.mock(DeserializationContext.class);
        JavaType javaType = Mockito.mock(JavaType.class);

        // Act
        try {
            validator.validateSubType(ctxt, javaType);

            // Assert
            // No exception should be thrown
            Assert.assertTrue(true);
        } catch (JsonMappingException e) {
            // Assert
            Assert.fail("JsonMappingException should not be thrown");
        }
    }

    @Test(expected = JsonMappingException.class)
    public void validateSubType_IllegalType_ExceptionThrown() throws JsonMappingException {
        // Arrange
        SubTypeValidator validator = SubTypeValidator.instance();
        DeserializationContext ctxt = Mockito.mock(DeserializationContext.class);
        JavaType javaType = Mockito.mock(JavaType.class);
        Class<?> rawClass = this.getClass();
        Mockito.when(javaType.getRawClass()).thenReturn(rawClass);
        Set<String> illegalClassNames = new HashSet<>();
        illegalClassNames.add(this.getClass().getName());
        validator._cfgIllegalClassNames = illegalClassNames;

        // Act
        validator.validateSubType(ctxt, javaType);

        // Assert
        // Exception should be thrown
        Assert.fail("JsonMappingException should be thrown");
    }

    @Test
    public void validateSubType_AbstractBeanFactoryPointcutAdvisor_ExceptionThrown() {
        // Arrange
        SubTypeValidator validator = SubTypeValidator.instance();
        DeserializationContext ctxt = Mockito.mock(DeserializationContext.class);
        JavaType javaType = Mockito.mock(JavaType.class);
        Class<?> rawClass = AbstractBeanFactoryPointcutAdvisor.class;
        Mockito.when(javaType.getRawClass()).thenReturn(rawClass);

        // Act
        try {
            validator.validateSubType(ctxt, javaType);

            // Assert
            // Exception should be thrown
            Assert.fail("JsonMappingException should be thrown");
        } catch (JsonMappingException e) {
            // Assert
            Assert.assertTrue(true);
        }
    }

    @Test
    public void validateSubType_AbstractApplicationContext_ExceptionThrown() {
        // Arrange
        SubTypeValidator validator = SubTypeValidator.instance();
        DeserializationContext ctxt = Mockito.mock(DeserializationContext.class);
        JavaType javaType = Mockito.mock(JavaType.class);
        Class<?> rawClass = AbstractApplicationContext.class;
        Mockito.when(javaType.getRawClass()).thenReturn(rawClass);

        // Act
        try {
            validator.validateSubType(ctxt, javaType);

            // Assert
            // Exception should be thrown
            Assert.fail("JsonMappingException should be thrown");
        } catch (JsonMappingException e) {
            // Assert
            Assert.assertTrue(true);
        }
    }

    @Test
    public void validateSubType_TemplatesImpl_ExceptionThrown() {
        // Arrange
        SubTypeValidator validator = SubTypeValidator.instance();
        DeserializationContext ctxt = Mockito.mock(DeserializationContext.class);
        JavaType javaType = Mockito.mock(JavaType.class);
        Class<?> rawClass = TemplatesImpl.class;
        Mockito.when(javaType.getRawClass()).thenReturn(rawClass);

        // Act
        try {
            validator.validateSubType(ctxt, javaType);

            // Assert
            // Exception should be thrown
            Assert.fail("JsonMappingException should be thrown");
        } catch (JsonMappingException e) {
            // Assert
            Assert.assertTrue(true);
        }
    }

    @Test
    public void validateSubType_JdbcRowSetImpl_ExceptionThrown() {
        // Arrange
        SubTypeValidator validator = SubTypeValidator.instance();
        DeserializationContext ctxt = Mockito.mock(DeserializationContext.class);
        JavaType javaType = Mockito.mock(JavaType.class);
        Class<?> rawClass = JdbcRowSetImpl.class;
        Mockito.when(javaType.getRawClass()).thenReturn(rawClass);

        // Act
        try {
            validator.validateSubType(ctxt, javaType);

            // Assert
            // Exception should be thrown
            Assert.fail("JsonMappingException should be thrown");
        } catch (JsonMappingException e) {
            // Assert
            Assert.assertTrue(true);
        }
    }

    @Test
    public void validateSubType_FileHandler_ExceptionThrown() {
        // Arrange
        SubTypeValidator validator = SubTypeValidator.instance();
        DeserializationContext ctxt = Mockito.mock(DeserializationContext.class);
        JavaType javaType = Mockito.mock(JavaType.class);
        Class<?> rawClass = FileHandler.class;
        Mockito.when(javaType.getRawClass()).thenReturn(rawClass);

        // Act
        try {
            validator.validateSubType(ctxt, javaType);

            // Assert
            // Exception should be thrown
            Assert.fail("JsonMappingException should be thrown");
        } catch (JsonMappingException e) {
            // Assert
            Assert.assertTrue(true);
        }
    }

    @Test
    public void validateSubType_UnicastRemoteObject_ExceptionThrown() {
        // Arrange
        SubTypeValidator validator = SubTypeValidator.instance();
        DeserializationContext ctxt = Mockito.mock(DeserializationContext.class);
        JavaType javaType = Mockito.mock(JavaType.class);
        Class<?> rawClass = UnicastRemoteObject.class;
        Mockito.when(javaType.getRawClass()).thenReturn(rawClass);

        // Act
        try {
            validator.validateSubType(ctxt, javaType);

            // Assert
            // Exception should be thrown
            Assert.fail("JsonMappingException should be thrown");
        } catch (JsonMappingException e) {
            // Assert
            Assert.assertTrue(true);
        }
    }

    @Test
    public void validateSubType_PropertyPathFactoryBean_ExceptionThrown() {
        // Arrange
        SubTypeValidator validator = SubTypeValidator.instance();
        DeserializationContext ctxt = Mockito.mock(DeserializationContext.class);
        JavaType javaType = Mockito.mock(JavaType.class);
        Class<?> rawClass = PropertyPathFactoryBean.class;
        Mockito.when(javaType.getRawClass()).thenReturn(rawClass);

        // Act
        try {
            validator.validateSubType(ctxt, javaType);

            // Assert
            // Exception should be thrown
            Assert.fail("JsonMappingException should be thrown");
        } catch (JsonMappingException e) {
            // Assert
            Assert.assertTrue(true);
        }
    }

    @Test
    public void validateSubType_JndiRefForwardingDataSource_ExceptionThrown() {
        // Arrange
        SubTypeValidator validator = SubTypeValidator.instance();
        DeserializationContext ctxt = Mockito.mock(DeserializationContext.class);
        JavaType javaType = Mockito.mock(JavaType.class);
        Class<?> rawClass = JndiRefForwardingDataSource.class;
        Mockito.when(javaType.getRawClass()).thenReturn(rawClass);

        // Act
        try {
            validator.validateSubType(ctxt, javaType);

            // Assert
            // Exception should be thrown
            Assert.fail("JsonMappingException should be thrown");
        } catch (JsonMappingException e) {
            // Assert
            Assert.assertTrue(true);
        }
    }

    @Test
    public void validateSubType_WrapperConnectionPoolDataSource_ExceptionThrown() {
        // Arrange
        SubTypeValidator validator = SubTypeValidator.instance();
        DeserializationContext ctxt = Mockito.mock(DeserializationContext.class);
        JavaType javaType = Mockito.mock(JavaType.class);
        Class<?> rawClass = WrapperConnectionPoolDataSource.class;
        Mockito.when(javaType.getRawClass()).thenReturn(rawClass);

        // Act
        try {
            validator.validateSubType(ctxt, javaType);

            // Assert
            // Exception should be thrown
            Assert.fail("JsonMappingException should be thrown");
        } catch (JsonMappingException e) {
            // Assert
            Assert.assertTrue(true);
        }
    }

    @Test
    public void validateSubType_BasicDataSource_ExceptionThrown() {
        // Arrange
        SubTypeValidator validator = SubTypeValidator.instance();
        DeserializationContext ctxt = Mockito.mock(DeserializationContext.class);
        JavaType javaType = Mockito.mock(JavaType.class);
        Class<?> rawClass = BasicDataSource.class;
        Mockito.when(javaType.getRawClass()).thenReturn(rawClass);

        // Act
        try {
            validator.validateSubType(ctxt, javaType);

            // Assert
            // Exception should be thrown
            Assert.fail("JsonMappingException should be thrown");
        } catch (JsonMappingException e) {
            // Assert
            Assert.assertTrue(true);
        }
    }
}
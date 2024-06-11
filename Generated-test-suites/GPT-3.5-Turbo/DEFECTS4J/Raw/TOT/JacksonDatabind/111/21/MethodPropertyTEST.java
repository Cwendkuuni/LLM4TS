import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.NullValueProvider;
import com.fasterxml.jackson.databind.introspect.AnnotatedMethod;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.util.Annotations;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

import static org.junit.Assert.*;

public class MethodPropertyTest {

    private MethodProperty methodProperty;
    private JsonParser jsonParser;
    private DeserializationContext deserializationContext;
    private Object instance;

    @Before
    public void setUp() throws Exception {
        BeanPropertyDefinition propDef = new BeanPropertyDefinition();
        JavaType type = new JavaType(Object.class);
        TypeDeserializer typeDeser = new TypeDeserializer();
        Annotations contextAnnotations = new Annotations();
        AnnotatedMethod method = new AnnotatedMethod(new Method());
        methodProperty = new MethodProperty(propDef, type, typeDeser, contextAnnotations, method);

        jsonParser = new JsonParser();
        deserializationContext = new DeserializationContext();
        instance = new Object();
    }

    @Test
    public void testDeserializeAndSet_WhenHasNullTokenAndSkipNullsEnabled_ShouldNotChangeValue() throws IOException {
        // Arrange
        methodProperty._skipNulls = true;
        jsonParser.setToken(JsonToken.VALUE_NULL);

        // Act
        methodProperty.deserializeAndSet(jsonParser, deserializationContext, instance);

        // Assert
        // Value should not change
        assertEquals(instance, methodProperty.get(instance));
    }

    @Test
    public void testDeserializeAndSet_WhenHasNullTokenAndSkipNullsDisabled_ShouldSetValueToNullProvider() throws IOException {
        // Arrange
        methodProperty._skipNulls = false;
        jsonParser.setToken(JsonToken.VALUE_NULL);

        // Act
        methodProperty.deserializeAndSet(jsonParser, deserializationContext, instance);

        // Assert
        assertEquals(methodProperty.getNullValueProvider().getNullValue(deserializationContext), methodProperty.get(instance));
    }

    @Test
    public void testDeserializeAndSet_WhenNotHasNullToken_ShouldDeserializeAndSetValue() throws IOException {
        // Arrange
        methodProperty._skipNulls = true;
        jsonParser.setToken(JsonToken.VALUE_STRING);
        Object value = "test";
        methodProperty._valueDeserializer = new TestDeserializer(value);

        // Act
        methodProperty.deserializeAndSet(jsonParser, deserializationContext, instance);

        // Assert
        assertEquals(value, methodProperty.get(instance));
    }

    @Test
    public void testDeserializeAndSet_WhenDeserializerReturnsNullAndSkipNullsDisabled_ShouldSetValueToNullProvider() throws IOException {
        // Arrange
        methodProperty._skipNulls = false;
        jsonParser.setToken(JsonToken.VALUE_STRING);
        methodProperty._valueDeserializer = new TestDeserializer(null);

        // Act
        methodProperty.deserializeAndSet(jsonParser, deserializationContext, instance);

        // Assert
        assertEquals(methodProperty.getNullValueProvider().getNullValue(deserializationContext), methodProperty.get(instance));
    }

    @Test
    public void testDeserializeAndSet_WhenDeserializerThrowsException_ShouldThrowIOException() {
        // Arrange
        jsonParser.setToken(JsonToken.VALUE_STRING);
        methodProperty._valueDeserializer = new TestDeserializer("test");
        Exception exception = new Exception("Test exception");
        methodProperty._setter = new TestSetter(exception);

        try {
            // Act
            methodProperty.deserializeAndSet(jsonParser, deserializationContext, instance);
            fail("IOException should be thrown");
        } catch (IOException e) {
            // Assert
            assertEquals(exception, e.getCause());
            assertEquals("Test exception", e.getCause().getMessage());
        }
    }

    @Test
    public void testDeserializeSetAndReturn_WhenHasNullTokenAndSkipNullsEnabled_ShouldNotChangeValueAndReturnInstance() throws IOException {
        // Arrange
        methodProperty._skipNulls = true;
        jsonParser.setToken(JsonToken.VALUE_NULL);

        // Act
        Object result = methodProperty.deserializeSetAndReturn(jsonParser, deserializationContext, instance);

        // Assert
        assertEquals(instance, methodProperty.get(instance));
        assertEquals(instance, result);
    }

    @Test
    public void testDeserializeSetAndReturn_WhenHasNullTokenAndSkipNullsDisabled_ShouldSetValueToNullProviderAndReturnInstance() throws IOException {
        // Arrange
        methodProperty._skipNulls = false;
        jsonParser.setToken(JsonToken.VALUE_NULL);

        // Act
        Object result = methodProperty.deserializeSetAndReturn(jsonParser, deserializationContext, instance);

        // Assert
        assertEquals(methodProperty.getNullValueProvider().getNullValue(deserializationContext), methodProperty.get(instance));
        assertEquals(instance, result);
    }

    @Test
    public void testDeserializeSetAndReturn_WhenNotHasNullToken_ShouldDeserializeAndSetValueAndReturnResult() throws IOException {
        // Arrange
        methodProperty._skipNulls = true;
        jsonParser.setToken(JsonToken.VALUE_STRING);
        Object value = "test";
        methodProperty._valueDeserializer = new TestDeserializer(value);

        // Act
        Object result = methodProperty.deserializeSetAndReturn(jsonParser, deserializationContext, instance);

        // Assert
        assertEquals(value, methodProperty.get(instance));
        assertEquals(instance, result);
    }

    @Test
    public void testDeserializeSetAndReturn_WhenDeserializerReturnsNullAndSkipNullsDisabled_ShouldSetValueToNullProviderAndReturnInstance() throws IOException {
        // Arrange
        methodProperty._skipNulls = false;
        jsonParser.setToken(JsonToken.VALUE_STRING);
        methodProperty._valueDeserializer = new TestDeserializer(null);

        // Act
        Object result = methodProperty.deserializeSetAndReturn(jsonParser, deserializationContext, instance);

        // Assert
        assertEquals(methodProperty.getNullValueProvider().getNullValue(deserializationContext), methodProperty.get(instance));
        assertEquals(instance, result);
    }

    @Test
    public void testDeserializeSetAndReturn_WhenDeserializerThrowsException_ShouldThrowIOExceptionAndReturnNull() {
        // Arrange
        jsonParser.setToken(JsonToken.VALUE_STRING);
        methodProperty._valueDeserializer = new TestDeserializer("test");
        Exception exception = new Exception("Test exception");
        methodProperty._setter = new TestSetter(exception);

        try {
            // Act
            Object result = methodProperty.deserializeSetAndReturn(jsonParser, deserializationContext, instance);
            fail("IOException should be thrown");
        } catch (IOException e) {
            // Assert
            assertEquals(exception, e.getCause());
            assertEquals("Test exception", e.getCause().getMessage());
            assertNull(e.getResult());
        }
    }

    @Test
    public void testSet_WhenPropertySetSuccessfully_ShouldSetValue() throws IOException {
        // Arrange
        Object value = "test";

        // Act
        methodProperty.set(instance, value);

        // Assert
        assertEquals(value, methodProperty.get(instance));
    }

    @Test
    public void testSet_WhenSetterThrowsException_ShouldThrowIOException() {
        // Arrange
        Object value = "test";
        Exception exception = new Exception("Test exception");
        methodProperty._setter = new TestSetter(exception);

        try {
            // Act
            methodProperty.set(instance, value);
            fail("IOException should be thrown");
        } catch (IOException e) {
            // Assert
            assertEquals(exception, e.getCause());
            assertEquals("Test exception", e.getCause().getMessage());
        }
    }

    @Test
    public void testSetAndReturn_WhenPropertySetSuccessfully_ShouldSetValueAndReturnInstance() throws IOException {
        // Arrange
        Object value = "test";

        // Act
        Object result = methodProperty.setAndReturn(instance, value);

        // Assert
        assertEquals(value, methodProperty.get(instance));
        assertEquals(instance, result);
    }

    @Test
    public void testSetAndReturn_WhenSetterThrowsException_ShouldThrowIOExceptionAndReturnNull() {
        // Arrange
        Object value = "test";
        Exception exception = new Exception("Test exception");
        methodProperty._setter = new TestSetter(exception);

        try {
            // Act
            Object result = methodProperty.setAndReturn(instance, value);
            fail("IOException should be thrown");
        } catch (IOException e) {
            // Assert
            assertEquals(exception, e.getCause());
            assertEquals("Test exception", e.getCause().getMessage());
            assertNull(e.getResult());
        }
    }

    private static class TestDeserializer extends JsonDeserializer<Object> {
        private final Object value;

        public TestDeserializer(Object value) {
            this.value = value;
        }

        @Override
        public Object deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
            return value;
        }
    }

    private static class TestSetter extends Method {
        private final Exception exception;

        public TestSetter(Exception exception) {
            this.exception = exception;
        }

        @Override
        public void invoke(Object obj, Object... args) throws Exception {
            throw exception;
        }
    }
}
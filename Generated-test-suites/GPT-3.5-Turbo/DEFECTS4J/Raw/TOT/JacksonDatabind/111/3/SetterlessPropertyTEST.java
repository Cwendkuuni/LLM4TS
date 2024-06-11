import static org.junit.Assert.*;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.*;

import org.junit.Test;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.deser.NullValueProvider;
import com.fasterxml.jackson.databind.deser.impl.SetterlessProperty;
import com.fasterxml.jackson.databind.deser.impl.SetterlessPropertyTest;

public class SetterlessPropertyTest {

    @Test
    public void testDeserializeAndSet_NullValue() throws IOException {
        SetterlessProperty property = createProperty();

        DeserializationContext context = createDeserializationContext();
        JsonParser parser = createJsonParser(JsonToken.VALUE_NULL);

        Object instance = createInstance();

        property.deserializeAndSet(parser, context, instance);

        assertNull(instance.settable);
    }

    @Test
    public void testDeserializeAndSet_WithDeserializationProblem() {
        SetterlessProperty property = createProperty();

        DeserializationContext context = createDeserializationContext();
        JsonParser parser = createJsonParser(JsonToken.VALUE_STRING);

        Object instance = createInstance();

        try {
            property.deserializeAndSet(parser, context, instance);
            fail("Expected IOException to be thrown");
        } catch (IOException e) {
            assertEquals("Problem deserializing 'setterless' property (\"settable\"): no way to handle typed deser with setterless yet",
                    e.getMessage());
        }
    }

    @Test
    public void testDeserializeAndSet_GetterException() {
        SetterlessProperty property = createProperty();

        DeserializationContext context = createDeserializationContext();
        JsonParser parser = createJsonParser(JsonToken.VALUE_STRING);

        Object instance = createInstance();

        try {
            property.deserializeAndSet(parser, context, instance);
            fail("Expected IOException to be thrown");
        } catch (IOException e) {
            assertEquals("Problem deserializing 'setterless' property 'settable': get method returned null",
                    e.getMessage());
        }
    }

    @Test
    public void testDeserializeAndSet_ValidValue() throws IOException {
        SetterlessProperty property = createProperty();

        DeserializationContext context = createDeserializationContext();
        JsonParser parser = createJsonParser(JsonToken.VALUE_STRING);

        Object instance = createInstance();

        property.deserializeAndSet(parser, context, instance);

        assertEquals("Test Value", instance.settable);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSet() throws IOException {
        SetterlessProperty property = createProperty();

        Object instance = createInstance();

        property.set(instance, "Test Value");
    }

    @Test
    public void testSetAndReturn() throws IOException {
        SetterlessProperty property = createProperty();

        Object instance = createInstance();

        Object result = property.setAndReturn(instance, "Test Value");

        assertSame(instance, result);
    }

    private SetterlessProperty createProperty() {
        MockAnnotatedMethod method = new MockAnnotatedMethod();
        return new SetterlessProperty(null, null, null, null, method);
    }

    private DeserializationContext createDeserializationContext() {
        return new DeserializationContext(null);
    }

    private JsonParser createJsonParser(JsonToken token) {
        return new MockJsonParser(token);
    }

    private Object createInstance() {
        return new SetterlessBean();
    }

    private static class MockAnnotatedMethod extends AnnotatedMethod {
        @Override
        public Method getAnnotated() {
            try {
                return SetterlessBean.class.getDeclaredMethod("getSettable");
            } catch (NoSuchMethodException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private static class MockJsonParser extends JsonParser {
        private final JsonToken token;

        public MockJsonParser(JsonToken token) {
            this.token = token;
        }

        @Override
        public JsonToken nextToken() throws IOException {
            return token;
        }

        // Rest of the overridden methods can be implemented similarly

        @Override
        public JsonToken getCurrentToken() {
            return token;
        }
    }

    private static class SetterlessBean {
        @JsonDeserialize(contentAs = String.class)
        private String settable;

        public String getSettable() {
            return settable;
        }
    }

}
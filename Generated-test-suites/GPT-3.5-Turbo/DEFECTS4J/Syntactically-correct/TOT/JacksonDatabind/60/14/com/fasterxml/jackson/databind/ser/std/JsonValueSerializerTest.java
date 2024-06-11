
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.ser.std.JsonValueSerializer;
import com.fasterxml.jackson.databind.util.StdConverter;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import static org.junit.Assert.assertEquals;

public class JsonValueSerializerTest {

    private JsonValueSerializer serializer;
    private JsonSerializer valueSerializer;

    @Before
    public void setup() throws NoSuchMethodException {
        Method valueMethod = MyObject.class.getDeclaredMethod("getValue");
        valueSerializer = new StdConverter<String, String>() {
            @Override
            public String convert(String value) {
                return value.toUpperCase();
            }
        };
        serializer = new JsonValueSerializer(valueMethod, valueSerializer);
    }

    @Test
    public void serialize_WhenValueIsNull_ShouldCallDefaultSerializeNull() throws IOException {
        // Arrange
        final MyObject obj = new MyObject(null);
        final JsonGenerator gen = new ObjectMapper().getFactory().createGenerator(System.out);

        // Act
        serializer.serialize(obj, gen, null);

        // Assert
        assertEquals("null", gen.getOutputContext().getCurrentValue());
    }

    @Test
    public void serialize_WhenValueIsNotNull_ShouldSerializeValue() throws IOException {
        // Arrange
        final MyObject obj = new MyObject("test");
        final JsonGenerator gen = new ObjectMapper().getFactory().createGenerator(System.out);

        // Act
        serializer.serialize(obj, gen, null);

        // Assert
        assertEquals("TEST", gen.getOutputContext().getCurrentValue());
    }

    @Test(expected = JsonGenerationException.class)
    public void serialize_WhenValueSerializerThrowsException_ShouldWrapExceptionWithJsonMappingException() throws IOException {
        // Arrange
        final MyObject obj = new MyObject("test");
        final JsonGenerator gen = new ObjectMapper().getFactory().createGenerator(System.out);

        valueSerializer = new StdConverter<String, String>() {
            @Override
            public String convert(String value) {
                throw new RuntimeException("Serialization Exception");
            }
        };
        serializer = new JsonValueSerializer(MyObject.class, valueSerializer);

        // Act
        serializer.serialize(obj, gen, null);
    }

    @Test
    public void serializeWithType_WhenValueIsNull_ShouldCallDefaultSerializeNull() throws IOException {
        // Arrange
        final MyObject obj = new MyObject(null);
        final JsonGenerator gen = new ObjectMapper().getFactory().createGenerator(System.out);

        // Act
        serializer.serializeWithType(obj, gen, null, null);

        // Assert
        assertEquals("null", gen.getOutputContext().getCurrentValue());
    }

    @Test
    public void serializeWithType_WhenValueIsNotNull_ShouldSerializeValueWithType() throws IOException {
        // Arrange
        final MyObject obj = new MyObject("test");
        final JsonGenerator gen = new ObjectMapper().getFactory().createGenerator(System.out);

        // Act
        serializer.serializeWithType(obj, gen, null, null);

        // Assert
        assertEquals("TEST", gen.getOutputContext().getCurrentValue());
    }

    @Test(expected = JsonGenerationException.class)
    public void serializeWithType_WhenValueSerializerThrowsException_ShouldWrapExceptionWithJsonMappingException() throws IOException {
        // Arrange
        final MyObject obj = new MyObject("test");
        final JsonGenerator gen = new ObjectMapper().getFactory().createGenerator(System.out);

        valueSerializer = new StdConverter<String, String>() {
            @Override
            public String convert(String value) {
                throw new RuntimeException("Serialization Exception");
            }
        };
        serializer = new JsonValueSerializer(MyObject.class, valueSerializer);

        // Act
        serializer.serializeWithType(obj, gen, null, null);
    }

    private static class MyObject {
        private final String value;

        public MyObject(String value) {
            this.value = value;
        }

        @com.fasterxml.jackson.annotation.JsonValue
        public String getValue() {
            return value;
        }
    }
}


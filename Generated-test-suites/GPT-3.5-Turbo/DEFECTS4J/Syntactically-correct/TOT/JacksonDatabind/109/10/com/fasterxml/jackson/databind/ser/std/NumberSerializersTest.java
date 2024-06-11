import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonFormatVisitorWrapper;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;


public class NumberSerializersTest {

    private static final Map<String, JsonSerializer<?>> allDeserializers = new HashMap<>();
    private static final JsonFactory jsonFactory = new JsonFactory();

    @Before
    public void setup() {
        NumberSerializers.addAll(allDeserializers);
    }

    @Test
    public void testIntegerSerializer() {
        // Typical use case
        Integer value = 42;
        String expected = "42";
        String actual = serializeValue(value);
        assertEquals(expected, actual);

        // Edge case
        value = Integer.MIN_VALUE;
        expected = "-2147483648";
        actual = serializeValue(value);
        assertEquals(expected, actual);
    }

    @Test
    public void testIntLikeSerializer() {
        // Typical use case
        Byte value = 10;
        String expected = "10";
        String actual = serializeValue(value);
        assertEquals(expected, actual);

        // Error scenario - Floating point value
        Double floatingValue = 10.5;
        try {
            serializeValue(floatingValue);
            fail("Expected IOException");
        } catch (IOException e) {
            assertTrue(e instanceof JsonMappingException);
        }
    }

    @Test
    public void testShortSerializer() {
        // Typical use case
        Short value = 500;
        String expected = "500";
        String actual = serializeValue(value);
        assertEquals(expected, actual);

        // Edge case
        value = Short.MIN_VALUE;
        expected = "-32768";
        actual = serializeValue(value);
        assertEquals(expected, actual);
    }

    @Test
    public void testLongSerializer() {
        // Typical use case
        Long value = 10000000000L;
        String expected = "10000000000";
        String actual = serializeValue(value);
        assertEquals(expected, actual);

        // Error scenario - Null value
        try {
            serializeValue(null);
            fail("Expected IOException");
        } catch (IOException e) {
            assertTrue(e instanceof JsonMappingException);
        }
    }

    @Test
    public void testFloatSerializer() {
        // Typical use case
        Float value = 3.14f;
        String expected = "3.14";
        String actual = serializeValue(value);
        assertEquals(expected, actual);

        // Edge case
        value = Float.NaN;
        expected = "NaN";
        actual = serializeValue(value);
        assertEquals(expected, actual);
    }

    @Test
    public void testDoubleSerializer() {
        // Typical use case
        Double value = 2.71828;
        String expected = "2.71828";
        String actual = serializeValue(value);
        assertEquals(expected, actual);

        // Error scenario - Invalid type for serialization
        Boolean booleanValue = true;
        try {
            serializeValue(booleanValue);
            fail("Expected IOException");
        } catch (IOException e) {
            assertTrue(e instanceof JsonMappingException);
        }
    }

    private String serializeValue(Object value) {
        try {
            JsonGenerator generator = jsonFactory.createGenerator(System.out);
            JsonSerializer<?> serializer = allDeserializers.get(value.getClass().getName());
            serializer.serialize(value, generator, new SerializerProvider());
            generator.flush();
            return generator.getOutputTarget().toString();
        } catch (IOException e) {
            throw new RuntimeException("Serialization failed", e);
        }
    }
}
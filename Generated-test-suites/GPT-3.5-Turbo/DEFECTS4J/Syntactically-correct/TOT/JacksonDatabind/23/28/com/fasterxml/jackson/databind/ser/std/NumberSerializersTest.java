import org.junit.Test;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;

public class NumberSerializersTest {

    ObjectMapper mapper = new ObjectMapper();

    @Test
    public void testIntegerSerializer() throws IOException {
        NumberSerializers.IntegerSerializer integerSerializer = new NumberSerializers.IntegerSerializer();
        
        SerializerProvider serializerProvider = new SerializerProvider()
        {
            // Implement necessary serializer provider methods here
        };

        // Typical use case
        Integer value1 = 42;
        String expectedJson1 = "42";
        assertEquals(expectedJson1, mapper.writeValueAsString(value1));

        // Edge case
        Integer value2 = 0;
        String expectedJson2 = "0";
        assertEquals(expectedJson2, mapper.writeValueAsString(value2));

        // Error scenario
        Integer value3 = null;
        String expectedJson3 = "null";
        assertEquals(expectedJson3, mapper.writeValueAsString(value3));

        // Additional test case
        Integer value4 = -1;
        String expectedJson4 = "-1";
        assertEquals(expectedJson4, mapper.writeValueAsString(value4));
    }

    @Test
    public void testShortSerializer() throws IOException {
        NumberSerializers.ShortSerializer shortSerializer = new NumberSerializers.ShortSerializer();

        // Typical use case
        Short value1 = 123;
        String expectedJson1 = "123";
        assertEquals(expectedJson1, mapper.writeValueAsString(value1));

        // Edge case
        Short value2 = 0;
        String expectedJson2 = "0";
        assertEquals(expectedJson2, mapper.writeValueAsString(value2));

        // Error scenario
        Short value3 = null;
        String expectedJson3 = "null";
        assertEquals(expectedJson3, mapper.writeValueAsString(value3));

        // Additional test case
        Short value4 = -100;
        String expectedJson4 = "-100";
        assertEquals(expectedJson4, mapper.writeValueAsString(value4));
    }

    @Test
    public void testIntLikeSerializer() throws IOException {
        NumberSerializers.IntLikeSerializer intLikeSerializer = new NumberSerializers.IntLikeSerializer();
        
        // Typical use case
        Number value1 = 1000;
        String expectedJson1 = "1000";
        assertEquals(expectedJson1, mapper.writeValueAsString(value1));

        // Edge case
        Number value2 = 0;
        String expectedJson2 = "0";
        assertEquals(expectedJson2, mapper.writeValueAsString(value2));

        // Error scenario
        Number value3 = null;
        String expectedJson3 = "null";
        assertEquals(expectedJson3, mapper.writeValueAsString(value3));

        // Additional test case
        Number value4 = -987654321;
        String expectedJson4 = "-987654321";
        assertEquals(expectedJson4, mapper.writeValueAsString(value4));
    }

    @Test
    public void testLongSerializer() throws IOException {
        NumberSerializers.LongSerializer longSerializer = new NumberSerializers.LongSerializer();

        // Typical use case
        Long value1 = 9876543210L;
        String expectedJson1 = "9876543210";
        assertEquals(expectedJson1, mapper.writeValueAsString(value1));

        // Edge case
        Long value2 = 0L;
        String expectedJson2 = "0";
        assertEquals(expectedJson2, mapper.writeValueAsString(value2));

        // Error scenario
        Long value3 = null;
        String expectedJson3 = "null";
        assertEquals(expectedJson3, mapper.writeValueAsString(value3));

        // Additional test case
        Long value4 = -1234567890987654321L;
        String expectedJson4 = "-1234567890987654321";
        assertEquals(expectedJson4, mapper.writeValueAsString(value4));
    }

    @Test
    public void testFloatSerializer() throws IOException {
        NumberSerializers.FloatSerializer floatSerializer = new NumberSerializers.FloatSerializer();

        // Typical use case
        Float value1 = 3.14f;
        String expectedJson1 = "3.14";
        assertEquals(expectedJson1, mapper.writeValueAsString(value1));

        // Edge case
        Float value2 = 0.0f;
        String expectedJson2 = "0.0";
        assertEquals(expectedJson2, mapper.writeValueAsString(value2));

        // Error scenario
        Float value3 = null;
        String expectedJson3 = "null";
        assertEquals(expectedJson3, mapper.writeValueAsString(value3));

        // Additional test case
        Float value4 = -1.23f;
        String expectedJson4 = "-1.23";
        assertEquals(expectedJson4, mapper.writeValueAsString(value4));
    }

    @Test
    public void testDoubleSerializer() throws IOException {
        NumberSerializers.DoubleSerializer doubleSerializer = new NumberSerializers.DoubleSerializer();

        // Typical use case
        Double value1 = 12345.6789;
        String expectedJson1 = "12345.6789";
        assertEquals(expectedJson1, mapper.writeValueAsString(value1));

        // Edge case
        Double value2 = 0.0;
        String expectedJson2 = "0.0";
        assertEquals(expectedJson2, mapper.writeValueAsString(value2));

        // Error scenario
        Double value3 = null;
        String expectedJson3 = "null";
        assertEquals(expectedJson3, mapper.writeValueAsString(value3));

        // Additional test case
        Double value4 = -9876.543210;
        String expectedJson4 = "-9876.54321";
        assertEquals(expectedJson4, mapper.writeValueAsString(value4));
    }

    @Test
    public void testAddAll() {
        Map<String, JsonSerializer<?>> allDeserializers = new HashMap<>();
        NumberSerializers.addAll(allDeserializers);

        // Check if all expected deserializers are added correctly
        assertTrue(allDeserializers.containsKey(Integer.class.getName()));
        assertTrue(allDeserializers.containsKey(Integer.TYPE.getName()));
        assertTrue(allDeserializers.containsKey(Long.class.getName()));
        assertTrue(allDeserializers.containsKey(Long.TYPE.getName()));
        assertTrue(allDeserializers.containsKey(Byte.class.getName()));
        assertTrue(allDeserializers.containsKey(Byte.TYPE.getName()));
        assertTrue(allDeserializers.containsKey(Short.class.getName()));
        assertTrue(allDeserializers.containsKey(Short.TYPE.getName()));
        assertTrue(allDeserializers.containsKey(Float.class.getName()));
        assertTrue(allDeserializers.containsKey(Float.TYPE.getName()));
        assertTrue(allDeserializers.containsKey(Double.class.getName()));
        assertTrue(allDeserializers.containsKey(Double.TYPE.getName()));
    }

    // Add more test methods for other methods in the NumberSerializers class if needed

}
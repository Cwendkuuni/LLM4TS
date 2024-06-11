import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.NumberSerializers;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;

public class NumberSerializersTest {

    private Map<String, JsonSerializer<?>> allDeserializers;

    @Before
    public void setUp() {
        allDeserializers = new HashMap<>();
        NumberSerializers.addAll(allDeserializers);
    }

    @Test
    public void testIntegerSerializer() throws IOException {
        assertNull(allDeserializers.get(Integer.class.getName()));
        assertNull(allDeserializers.get(Integer.TYPE.getName()));

        NumberSerializers.IntegerSerializer integerSerializer = new NumberSerializers.IntegerSerializer();
        assertNotNull(integerSerializer);

        integerSerializer.serialize(10, null, null);
        integerSerializer.serializeWithType(10, null, null, null);

        JsonNode schema = integerSerializer.getSchema(null, null);
        assertNotNull(schema);

        integerSerializer.acceptJsonFormatVisitor(null, null);
        JsonSerializer<?> contextualSerializer = integerSerializer.createContextual(null, null);
        assertNotNull(contextualSerializer);

        allDeserializers.put(Integer.class.getName(), integerSerializer);
        allDeserializers.put(Integer.TYPE.getName(), integerSerializer);
    }

    @Test
    public void testShortSerializer() throws IOException {
        assertNull(allDeserializers.get(Short.class.getName()));

        NumberSerializers.ShortSerializer shortSerializer = new NumberSerializers.ShortSerializer();
        assertNotNull(shortSerializer);

        shortSerializer.serialize(Short.valueOf("10"), null, null);

        JsonNode schema = shortSerializer.getSchema(null, null);
        assertNotNull(schema);

        shortSerializer.acceptJsonFormatVisitor(null, null);
        JsonSerializer<?> contextualSerializer = shortSerializer.createContextual(null, null);
        assertNotNull(contextualSerializer);

        allDeserializers.put(Short.class.getName(), shortSerializer);
    }

    @Test
    public void testIntLikeSerializer() throws IOException {
        assertNull(allDeserializers.get(Byte.class.getName()));
        assertNull(allDeserializers.get(Byte.TYPE.getName()));

        NumberSerializers.IntLikeSerializer intLikeSerializer = new NumberSerializers.IntLikeSerializer();
        assertNotNull(intLikeSerializer);

        intLikeSerializer.serialize(10, null, null);

        JsonNode schema = intLikeSerializer.getSchema(null, null);
        assertNotNull(schema);

        intLikeSerializer.acceptJsonFormatVisitor(null, null);
        JsonSerializer<?> contextualSerializer = intLikeSerializer.createContextual(null, null);
        assertNotNull(contextualSerializer);

        allDeserializers.put(Byte.class.getName(), intLikeSerializer);
        allDeserializers.put(Byte.TYPE.getName(), intLikeSerializer);
    }

    @Test
    public void testLongSerializer() throws IOException {
        assertNull(allDeserializers.get(Long.class.getName()));
        assertNull(allDeserializers.get(Long.TYPE.getName()));

        NumberSerializers.LongSerializer longSerializer = new NumberSerializers.LongSerializer();
        assertNotNull(longSerializer);

        longSerializer.serialize(10L, null, null);

        JsonNode schema = longSerializer.getSchema(null, null);
        assertNotNull(schema);

        longSerializer.acceptJsonFormatVisitor(null, null);
        JsonSerializer<?> contextualSerializer = longSerializer.createContextual(null, null);
        assertNotNull(contextualSerializer);

        allDeserializers.put(Long.class.getName(), longSerializer);
        allDeserializers.put(Long.TYPE.getName(), longSerializer);
    }

    @Test
    public void testFloatSerializer() throws IOException {
        assertNull(allDeserializers.get(Float.class.getName()));
        assertNull(allDeserializers.get(Float.TYPE.getName()));

        NumberSerializers.FloatSerializer floatSerializer = new NumberSerializers.FloatSerializer();
        assertNotNull(floatSerializer);

        floatSerializer.serialize(10.0f, null, null);

        JsonNode schema = floatSerializer.getSchema(null, null);
        assertNotNull(schema);

        floatSerializer.acceptJsonFormatVisitor(null, null);
        JsonSerializer<?> contextualSerializer = floatSerializer.createContextual(null, null);
        assertNotNull(contextualSerializer);

        allDeserializers.put(Float.class.getName(), floatSerializer);
        allDeserializers.put(Float.TYPE.getName(), floatSerializer);
    }

    @Test
    public void testDoubleSerializer() throws IOException {
        assertNull(allDeserializers.get(Double.class.getName()));

        NumberSerializers.DoubleSerializer doubleSerializer = new NumberSerializers.DoubleSerializer();
        assertNotNull(doubleSerializer);

        doubleSerializer.serialize(10.0, null, null);
        doubleSerializer.serializeWithType(10.0, null, null, null);

        JsonNode schema = doubleSerializer.getSchema(null, null);
        assertNotNull(schema);

        doubleSerializer.acceptJsonFormatVisitor(null, null);
        JsonSerializer<?> contextualSerializer = doubleSerializer.createContextual(null, null);
        assertNotNull(contextualSerializer);

        allDeserializers.put(Double.class.getName(), doubleSerializer);
    }

    @Test
    public void testAddAll() {
        Map<String, JsonSerializer<?>> deserializers = new HashMap<>();
        NumberSerializers.addAll(deserializers);

        assertTrue(deserializers.containsKey(Integer.class.getName()));
        assertTrue(deserializers.containsKey(Integer.TYPE.getName()));
        assertTrue(deserializers.containsKey(Long.class.getName()));
        assertTrue(deserializers.containsKey(Long.TYPE.getName()));
        assertTrue(deserializers.containsKey(Byte.class.getName()));
        assertTrue(deserializers.containsKey(Byte.TYPE.getName()));
        assertTrue(deserializers.containsKey(Short.class.getName()));
        assertTrue(deserializers.containsKey(Short.TYPE.getName()));
        assertTrue(deserializers.containsKey(Float.class.getName()));
        assertTrue(deserializers.containsKey(Float.TYPE.getName()));
        assertTrue(deserializers.containsKey(Double.class.getName()));
        assertTrue(deserializers.containsKey(Double.TYPE.getName()));
    }
}
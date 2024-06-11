import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import org.junit.Test;

import static org.junit.Assert.*;

public class NumberSerializersTest {

    // 

    @Test
    public void testAddAll() {
        Map<String, JsonSerializer<?>> allDeserializers = new java.util.HashMap<>();
        NumberSerializers.addAll(allDeserializers);

        assertTrue(allDeserializers.containsKey(Integer.class.getName()));
        assertTrue(allDeserializers.containsKey(Integer.TYPE.getName()));
        assertTrue(allDeserializers.containsKey(Long.class.getName()));
        assertTrue(allDeserializers.containsKey(Long.TYPE.getName()));
        // Add other checks for Byte, Short, Double, Float, etc.
    }

    @Test
    public void testIntegerSerializer() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        IntegerSerializer integerSerializer = NumberSerializers.findValueSerializer(mapper.getTypeFactory(), Integer.class);

        Integer number = 123;
        String json = mapper.writerWithRootValueSerializer(integerSerializer)
                .writeValueAsString(number);

        assertEquals("123", json);

        JsonParser parser = mapper.getFactory().createParser(json);
        Object deserialized = mapper.readValue(parser, Integer.class);
        assertEquals(number, deserialized);
    }

    @Test
    public void testIntLikeSerializer() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        IntLikeSerializer intLikeSerializer = NumberSerializers.findValueSerializer(mapper.getTypeFactory(), Number.class);

        Number number = 123;
        String json = mapper.writerWithRootValueSerializer(intLikeSerializer)
                .writeValueAsString(number);

        assertEquals("123", json);

        JsonParser parser = mapper.getFactory().createParser(json);
        Object deserialized = mapper.readValue(parser, Number.class);
        assertEquals(number, deserialized);
    }

    @Test
    public void testLongSerializer() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        LongSerializer longSerializer = NumberSerializers.findValueSerializer(mapper.getTypeFactory(), Long.class);

        Long number = 123L;
        String json = mapper.writerWithRootValueSerializer(longSerializer)
                .writeValueAsString(number);

        assertEquals("123", json);

        JsonParser parser = mapper.getFactory().createParser(json);
        Object deserialized = mapper.readValue(parser, Long.class);
        assertEquals(number, deserialized);
    }

    @Test
    public void testFloatSerializer() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        FloatSerializer floatSerializer = NumberSerializers.findValueSerializer(mapper.getTypeFactory(), Float.class);

        Float number = 123.45f;
        String json = mapper.writerWithRootValueSerializer(floatSerializer)
                .writeValueAsString(number);

        assertEquals("123.45", json);

        JsonParser parser = mapper.getFactory().createParser(json);
        Object deserialized = mapper.readValue(parser, Float.class);
        assertEquals(number, deserialized);
    }

    @Test
    public void testDoubleSerializer() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        DoubleSerializer doubleSerializer = NumberSerializers.findValueSerializer(mapper.getTypeFactory(), Double.class);

        Double number = 123.45;
        String json = mapper.writerWithRootValueSerializer(doubleSerializer)
                .writeValueAsString(number);

        assertEquals("123.45", json);

        JsonParser parser = mapper.getFactory().createParser(json);
        Object deserialized = mapper.readValue(parser, Double.class);
        assertEquals(number, deserialized);
    }

    // 
}
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.ser.std.NumberSerializers;
import org.junit.Test;

import java.io.IOException;
import java.lang.reflect.Type;

import static org.junit.Assert.*;

public class NumberSerializersTest {

    private static ObjectNode createTestNode() {
        return JsonNodeFactory.instance.objectNode();
    }

    @Test
    public void testAddAll() {
        Map<String, JsonSerializer<?>> allDeserializers = new java.util.HashMap<>();
        NumberSerializers.addAll(allDeserializers);
        assertNotNull(allDeserializers.get(Integer.class.getName()));
        assertNotNull(allDeserializers.get(Integer.TYPE.getName()));
        assertNotNull(allDeserializers.get(Long.class.getName()));
        assertNotNull(allDeserializers.get(Long.TYPE.getName()));
        assertNotNull(allDeserializers.get(Byte.class.getName()));
        assertNotNull(allDeserializers.get(Byte.TYPE.getName()));
        assertNotNull(allDeserializers.get(Short.class.getName()));
        assertNotNull(allDeserializers.get(Short.TYPE.getName()));
        assertNotNull(allDeserializers.get(Float.class.getName()));
        assertNotNull(allDeserializers.get(Float.TYPE.getName()));
        assertNotNull(allDeserializers.get(Double.class.getName()));
        assertNotNull(allDeserializers.get(Double.TYPE.getName()));
    }

    @Test
    public void testShortSerializer() throws JsonProcessingException {
        JsonParser parser = new JsonFactory().createParser(NumberSerializersTest.class.getResource("/testData/short.json"));
        ShortSerializer shortSerializer = new ShortSerializer();
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode testNode = createTestNode();
        JsonGenerator gen = new JsonFactory().createGenerator(System.out);

        assertEquals(Short.parseShort("123"), parser.readValueAs(Short.class));

        testNode.put("testField", (short) 123);
        ObjectNode resultNode = mapper.writeValueAsObject(testNode);
        short newShort = mapper.readValue(resultNode, Short.class);
        assertEquals(123, newShort);

        mapper.writeValue(gen, shortSerializer, 123);
        JsonParser checkParser = new JsonFactory().createParser(gen.getOutputStream());
        assertEquals(Short.parseShort("123"), checkParser.readValueAs(Short.class));
    }

    @Test(expected = JsonMappingException.class)
    public void testShortSerializerWithWrongType() throws JsonProcessingException {
        JsonParser parser = new JsonFactory().createParser(NumberSerializersTest.class.getResource("/testData/short.json"));
        ShortSerializer shortSerializer = new ShortSerializer();

        parser.readValueAs(String.class);
    }

    @Test
    public void testIntegerSerializer() throws JsonProcessingException {
        JsonParser parser = new JsonFactory().createParser(NumberSerializersTest.class.getResource("/testData/integer.json"));
        IntegerSerializer integerSerializer = new IntegerSerializer();
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode testNode = createTestNode();
        JsonGenerator gen = new JsonFactory().createGenerator(System.out);

        assertEquals(Integer.parseInt("123"), parser.readValueAs(Integer.class));

        testNode.put("testField", 123);
        ObjectNode resultNode = mapper.writeValueAsObject(testNode);
        int newInt = mapper.readValue(resultNode, Integer.class);
        assertEquals(123, newInt);

        mapper.writeValue(gen, integerSerializer, 123);
        JsonParser checkParser = new JsonFactory().createParser(gen.getOutputStream());
        assertEquals(Integer.parseInt("123"), checkParser.readValueAs(Integer.class));
    }

    @Test(expected = JsonMappingException.class)
    public void testIntegerSerializerWithWrongType() throws JsonProcessingException {
        JsonParser parser = new JsonFactory().createParser(NumberSerializersTest.class.getResource("/testData/integer.json"));
        IntegerSerializer integerSerializer = new IntegerSerializer();

        parser.readValueAs(String[].class);
    }

    @Test
    public void testIntLikeSerializer() throws JsonProcessingException {
        JsonParser parser = new JsonFactory().createParser(NumberSerializersTest.class.getResource("/testData/int_like.json"));
        IntLikeSerializer intLikeSerializer = new IntLikeSerializer();
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode testNode = createTestNode();
        JsonGenerator gen = new JsonFactory().createGenerator(System.out);

        assertEquals((int) 123, parser.readValueAs(Number.class).intValue());

        testNode.put("testField", Integer.valueOf(123));
        ObjectNode resultNode = mapper.writeValueAsObject(testNode);
        Number newNumber = mapper.readValue(resultNode, Number.class);
        assertEquals(123, newNumber.intValue());

        mapper.writeValue(gen, intLikeSerializer, Integer.valueOf(123));
        JsonParser checkParser = new JsonFactory().createParser(gen.getOutputStream());
        assertEquals((int) 123, checkParser.readValueAs(Number.class).intValue());
    }

    @Test(expected = JsonMappingException.class)
    public void testIntLikeSerializerWithWrongType() throws JsonProcessingException {
        JsonParser parser = new JsonFactory().createParser(NumberSerializersTest.class.getResource("/testData/int_like.json"));
        IntLikeSerializer intLikeSerializer = new IntLikeSerializer();

        parser.readValueAs(Float.class);
    }

    @Test
    public void testLongSerializer() throws JsonProcessingException {
        JsonParser parser = new JsonFactory().createParser(NumberSerializersTest.class.getResource("/testData/long.json"));
        LongSerializer longSerializer = new LongSerializer();
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode testNode = createTestNode();
        JsonGenerator gen = new JsonFactory().createGenerator(System.out);

        assertEquals(Long.parseLong("12345678901234567890"), parser.readValueAs(Long.class));

        testNode.put("testField", Long.parseLong("12345678901234567890"));
        ObjectNode resultNode = mapper.writeValueAsObject(testNode);
        long newLong = mapper.readValue(resultNode, Long.class);
        assertEquals(Long.parseLong("12345678901234567890"), newLong);

        mapper.writeValue(gen, longSerializer, Long.parseLong("12345678901234567890"));
        JsonParser checkParser = new JsonFactory().createParser(gen.getOutputStream());
        assertEquals(Long.parseLong("12345678901234567890"), checkParser.readValueAs(Long.class));
    }

    @Test(expected = JsonMappingException.class)
    public void testLongSerializerWithWrongType() throws JsonProcessingException {
        JsonParser parser = new JsonFactory().createParser(NumberSerializersTest.class.getResource("/testData/long.json"));
        LongSerializer longSerializer = new LongSerializer();

        parser.readValueAs(String[].class);
    }

    @Test
    public void testFloatSerializer() throws JsonProcessingException {
        JsonParser parser = new JsonFactory().createParser(NumberSerializersTest.class.getResource("/testData/float.json"));
        FloatSerializer floatSerializer = new FloatSerializer();
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode testNode = createTestNode();
        JsonGenerator gen = new JsonFactory().createGenerator(System.out);

        assertEquals(Float.parseFloat("123.45"), parser.readValueAs(Float.class));

        testNode.put("testField", Float.parseFloat("123.45"));
        ObjectNode resultNode = mapper.writeValueAsObject(testNode);
        float newFloat = mapper.readValue(resultNode, Float.class);
        assertEquals(Float.parseFloat("123.45"), newFloat, 0.01f);

        mapper.writeValue(gen, floatSerializer, Float.parseFloat("123.45"));
        JsonParser checkParser = new JsonFactory().createParser(gen.getOutputStream());
        assertEquals(Float.parseFloat("123.45"), checkParser.readValueAs(Float.class), 0.01f);
    }

    @Test(expected = JsonMappingException.class)
    public void testFloatSerializerWithWrongType() throws JsonProcessingException {
        JsonParser parser = new JsonFactory().createParser(NumberSerializersTest.class.getResource("/testData/float.json"));
        FloatSerializer floatSerializer = new FloatSerializer();

        parser.readValueAs(Integer.class);
    }

    @Test
    public void testDoubleSerializer() throws JsonProcessingException {
        JsonParser parser = new JsonFactory().createParser(NumberSerializersTest.class.getResource("/testData/double.json"));
        DoubleSerializer doubleSerializer = new DoubleSerializer();
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode testNode = createTestNode();
        JsonGenerator gen = new JsonFactory().createGenerator(System.out);

        assertEquals(Double.parseDouble("123.456789"), parser.readValueAs(Double.class));

        testNode.put("testField", Double.parseDouble("123.456789"));
        ObjectNode resultNode = mapper.writeValueAsObject(testNode);
        double newDouble = mapper.readValue(resultNode, Double.class);
        assertEquals(Double.parseDouble("123.456789"), newDouble, 0.0001);

        mapper.writeValue(gen, doubleSerializer, Double.parseDouble("123.456789"));
        JsonParser checkParser = new JsonFactory().createParser(gen.getOutputStream());
        assertEquals(Double.parseDouble("123.456789"), checkParser.readValueAs(Double.class), 0.0001);
    }

    @Test(expected = JsonMappingException.class)
    public void testDoubleSerializerWithWrongType() throws JsonProcessingException {
        JsonParser parser = new JsonFactory().createParser(NumberSerializersTest.class.getResource("/testData/double.json"));
        DoubleSerializer doubleSerializer = new DoubleSerializer();

        parser.readValueAs(Integer.class);
    }

}
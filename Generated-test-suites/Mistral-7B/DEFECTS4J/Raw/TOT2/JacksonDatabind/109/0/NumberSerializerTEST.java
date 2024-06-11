import org.junit.Test;
import static org.junit.Assert.*;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

public class NumberSerializerTest {

    @Test
    public void testSerializeNumber() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        NumberSerializer numberSerializer = NumberSerializer.instance;
        JsonGenerator gen = mapper.getPolymorphicSerializer(Number.class).getSerializer(JsonGeneratorFactory.instance);
        Number number = 123;

        // Check serialization of a Number
        String json = mapper.writeValueAsString(numberSerializer.serialize(number, gen, null));
        JsonParser parser = mapper.getFactory().createParser(json);
        Number deserializedNumber = mapper.readValue(parser, Number.class);
        assertEquals(number, deserializedNumber);
    }

    @Test
    public void testSerializeBigDecimal() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        NumberSerializer numberSerializer = NumberSerializer.instance;
        JsonGenerator gen = mapper.getPolymorphicSerializer(Number.class).getSerializer(JsonGeneratorFactory.instance);
        BigDecimal bigDecimal = new BigDecimal("123.45");

        // Check serialization of a BigDecimal
        String json = mapper.writeValueAsString(numberSerializer.serialize(bigDecimal, gen, null));
        JsonParser parser = mapper.getFactory().createParser(json);
        BigDecimal deserializedBigDecimal = mapper.readValue(parser, BigDecimal.class);
        assertEquals(bigDecimal, deserializedBigDecimal);
    }

    @Test
    public void testSerializeBigInteger() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        NumberSerializer numberSerializer = NumberSerializer.instance;
        JsonGenerator gen = mapper.getPolymorphicSerializer(Number.class).getSerializer(JsonGeneratorFactory.instance);
        BigInteger bigInteger = new BigInteger("12345678901234567890");

        // Check serialization of a BigInteger
        String json = mapper.writeValueAsString(numberSerializer.serialize(bigInteger, gen, null));
        JsonParser parser = mapper.getFactory().createParser(json);
        BigInteger deserializedBigInteger = mapper.readValue(parser, BigInteger.class);
        assertEquals(bigInteger, deserializedBigInteger);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSerializeInvalidNumber() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        NumberSerializer numberSerializer = NumberSerializer.instance;
        JsonGenerator gen = mapper.getPolymorphicSerializer(Number.class).getSerializer(JsonGeneratorFactory.instance);
        String invalidNumber = "abcd";

        // Check serialization of an invalid number
        numberSerializer.serialize(invalidNumber, gen, null);
    }

    @Test
    public void testDeserializeNumber() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode jsonNode = mapper.readTree("123");
        JsonParser jsonParser = JsonParserFactory.instance.getPrimitiveParser("123");

        // Check deserialization of a Number
        Number number = mapper.readValue(jsonParser, Number.class);
        assertEquals((Number) 123, number);
    }

    @Test
    public void testDeserializeBigDecimal() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode jsonNode = mapper.readTree("123.45");

        // Check deserialization of a BigDecimal
        BigDecimal bigDecimal = mapper.readValue(jsonNode, BigDecimal.class);
        assertEquals(new BigDecimal("123.45"), bigDecimal);
    }

    @Test
    public void testDeserializeBigInteger() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode jsonNode = mapper.readTree("12345678901234567890");

        // Check deserialization of a BigInteger
        BigInteger bigInteger = mapper.readValue(jsonNode, BigInteger.class);
        assertEquals(new BigInteger("12345678901234567890"), bigInteger);
    }

    @Test(expected = JsonProcessingException.class)
    public void testDeserializeInvalidJson() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonParser jsonParser = mapper.getFactory().createParser("{\"invalid_json\":\"abcd\"}");

        // Check deserialization of invalid json
        mapper.readValue(jsonParser, Number.class);
    }
}
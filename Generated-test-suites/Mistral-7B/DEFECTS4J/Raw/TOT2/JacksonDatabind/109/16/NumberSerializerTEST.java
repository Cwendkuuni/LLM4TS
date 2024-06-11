import org.junit.Test;
import static org.junit.Assert.*;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

public class NumberSerializerTest {
    @Test
    public void testSerializeNumber() throws JsonMappingException, JsonGenerationException {
        ObjectMapper mapper = new ObjectMapper();
        NumberSerializer serializer = NumberSerializer.instance;
        mapper.registerSerializer(Number.class, serializer);

        Number num = 123;
        String json = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(num);
        Number deserializedNum = mapper.readerForUpdating(new Object()).readValueAs(Number.class);

        assertEquals(num, deserializedNum);
        assertEquals(json, mapper.writerWithDefaultPrettyPrinter().writeValueAsString(num));
    }

    @Test
    public void testSerializeBigDecimal() throws JsonMappingException, JsonGenerationException {
        ObjectMapper mapper = new ObjectMapper();
        NumberSerializer serializer = NumberSerializer.instance;
        mapper.registerSerializer(Number.class, serializer);

        BigDecimal bigDecimal = new BigDecimal("123.45");
        String json = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(bigDecimal);
        BigDecimal deserializedBigDecimal = mapper.readerForUpdating(new Object()).readValueAs(BigDecimal.class);

        assertEquals(bigDecimal, deserializedBigDecimal);
        assertEquals(json, mapper.writerWithDefaultPrettyPrinter().writeValueAsString(bigDecimal));
    }

    @Test
    public void testSerializeBigInteger() throws JsonMappingException, JsonGenerationException {
        ObjectMapper mapper = new ObjectMapper();
        NumberSerializer serializer = NumberSerializer.instance;
        mapper.registerSerializer(Number.class, serializer);

        BigInteger bigInteger = new BigInteger("12345678901234567890");
        String json = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(bigInteger);
        BigInteger deserializedBigInteger = mapper.readerForUpdating(new Object()).readValueAs(BigInteger.class);

        assertEquals(bigInteger, deserializedBigInteger);
        assertEquals(json, mapper.writerWithDefaultPrettyPrinter().writeValueAsString(bigInteger));
    }

    @Test
    public void testContextualSerialization() throws JsonMappingException, JsonGenerationException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.enable(SerializationFeature.FAIL_ON_EMPTY_BEANS);

        // test for non-contextual serialization
        Number number = 123;
        String json = mapper.writeValueAsString(number);
        assertEquals("123", json);

        // test for contextual serialization
        ObjectMapper contextualMapper = new ObjectMapper();
        ObjectContextualSerializer contextualSerializer = new ObjectContextualSerializer(Number.class);
        mapper.registerSerializer(Number.class, contextualSerializer);

        Number contextualNumber = 123;
        String contextualJson = mapper.writeValueAsString(contextualNumber);
        assertEquals("123", contextualJson);

        JsonParser parser = mapper.createParser(contextualJson);
        Number deserializedContextualNumber = mapper.readValue(parser, Number.class);
        assertEquals(contextualNumber, deserializedContextualNumber);
    }

    @Test(expected = JsonMappingException.class)
    public void testSerializeInvalidNumber() throws JsonMappingException, JsonGenerationException {
        ObjectMapper mapper = new ObjectMapper();
        NumberSerializer serializer = NumberSerializer.instance;
        mapper.registerSerializer(Number.class, serializer);

        // test for failing to serialize null
        mapper.writeValueAsString(null);
    }

    
    // Add edge cases and exception handling scenarios here
    
}
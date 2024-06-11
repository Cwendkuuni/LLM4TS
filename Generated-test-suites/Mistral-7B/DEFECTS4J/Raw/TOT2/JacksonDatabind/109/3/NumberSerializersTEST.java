import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.*;

public class NumberSerializersTest {

    private static ObjectMapper mapper = new ObjectMapper();

    @Test
    public void testIntegerSerializer() throws JsonProcessingException {
        JsonNode jsonNode = mapper.writeValueAsTree(new Integer(1));
        assertEquals("1", jsonNode.asText());

        Object intValue = mapper.readValue("/1", Integer.class);
        assertEquals(1, intValue.intValue());
    }

    @Test
    public void testIntegerSerializerWithNegativeValue() throws JsonProcessingException {
        JsonNode jsonNode = mapper.writeValueAsTree(-1);
        assertEquals("-1", jsonNode.asText());

        Object intValue = mapper.readValue("/-1", Integer.class);
        assertEquals(-1, intValue.intValue());
    }

    @Test(expected = JsonMappingException.class)
    public void testIntegerSerializerWithFloatValue() throws JsonProcessingException {
        JsonNode jsonNode = mapper.writeValueAsTree(1.5);
        Object intValue = mapper.readValue(jsonNode.asText(), Integer.class);
    }

    @Test
    public void testIntegerSerializerWithBigIntegerValue() throws JsonProcessingException {
        JsonNode jsonNode = mapper.writeValueAsTree(java.math.BigInteger.valueOf(123456789012345L));
        Object intValue = mapper.readValue(jsonNode.asText(), Integer.class);
        assertEquals(123456789012345, intValue.longValue());
    }

    @Test
    public void testShortSerializer() throws JsonProcessingException {
        JsonNode jsonNode = mapper.writeValueAsTree((short) 1);
        assertEquals("1", jsonNode.asText());

        Object shortValue = mapper.readValue("/1", Short.class);
        assertEquals((short) 1, shortValue.shortValue());
    }

    @Test
    public void testShortSerializerWithNegativeValue() throws JsonProcessingException {
        JsonNode jsonNode = mapper.writeValueAsTree((short) -1);
        assertEquals("-1", jsonNode.asText());

        Object shortValue = mapper.readValue("/-1", Short.class);
        assertEquals((short) -1, shortValue.shortValue());
    }

    @Test(expected = JsonMappingException.class)
    public void testShortSerializerWithIntegerValue() throws JsonProcessingException {
        JsonNode jsonNode = mapper.writeValueAsTree(1);
        Object shortValue = mapper.readValue(jsonNode.asText(), Short.class);
    }

    @Test
    public void testLongSerializer() throws JsonProcessingException {
        JsonNode jsonNode = mapper.writeValueAsTree(1L);
        assertEquals("1", jsonNode.asText());

        Object longValue = mapper.readValue("/1", Long.class);
        assertEquals(1, longValue.longValue());
    }

    @Test
    public void testLongSerializerWithNegativeValue() throws JsonProcessingException {
        JsonNode jsonNode = mapper.writeValueAsTree(-1L);
        assertEquals("-1", jsonNode.asText());

        Object longValue = mapper.readValue("/-1", Long.class);
        assertEquals(-1, longValue.longValue());
    }

    @Test
    public void testLongSerializerWithBigIntegerValue() throws JsonProcessingException {
        JsonNode jsonNode = mapper.writeValueAsTree(java.math.BigInteger.valueOf(1234567890123456789L));
        Object longValue = mapper.readValue(jsonNode.asText(), Long.class);
        assertEquals(1234567890123456789, longValue.longValue());
    }

    @Test
    public void testFloatSerializer() throws JsonProcessingException {
        JsonNode jsonNode = mapper.writeValueAsTree(1.0f);
        assertEquals("1.0", jsonNode.asText());

        Object floatValue = mapper.readValue("/1.0", Float.class);
        assertEquals(1.0f, floatValue.floatValue(), 0.001f);
    }

    @Test
    public void testFloatSerializerWithNaNValue() throws JsonProcessingException {
        JsonNode jsonNode = mapper.writeValueAsTree(Float.NaN);
        Object floatValue = mapper.readValue(jsonNode.asText(), Float.class);
        assertEquals(Float.NaN, floatValue.floatValue());
    }

    @Test
    public void testFloatSerializerWithInfiniteValue() throws JsonProcessingException {
        JsonNode jsonNode = mapper.writeValueAsTree(Float.POSITIVE_INFINITY);
        Object floatValue = mapper.readValue(jsonNode.asText(), Float.class);
        assertEquals(Float.POSITIVE_INFINITY, floatValue.floatValue());
    }

    @Test(expected = JsonMappingException.class)
    public void testFloatSerializerWithLongValue() throws JsonProcessingException {
        JsonNode jsonNode = mapper.writeValueAsTree(1L);
        Object floatValue = mapper.readValue(jsonNode.asText(), Float.class);
    }

    @Test
    public void testDoubleSerializer() throws JsonProcessingException {
        JsonNode jsonNode = mapper.writeValueAsTree(1.0);
        assertEquals("1.0", jsonNode.asText());

        Object doubleValue = mapper.readValue("/1.0", Double.class);
        assertEquals(1.0, doubleValue.doubleValue(), 0.001);
    }

    @Test
    public void testDoubleSerializerWithNaNValue() throws JsonProcessingException {
        JsonNode jsonNode = mapper.writeValueAsTree(Double.NaN);
        Object doubleValue = mapper.readValue(jsonNode.asText(), Double.class);
        assertEquals(Double.NaN, doubleValue.doubleValue());
    }

    @Test
    public void testDoubleSerializerWithInfiniteValue() throws JsonProcessingException {
        JsonNode jsonNode = mapper.writeValueAsTree(Double.POSITIVE_INFINITY);
        Object doubleValue = mapper.readValue(jsonNode.asText(), Double.class);
        assertEquals(Double.POSITIVE_INFINITY, doubleValue.doubleValue());
    }

    @Test(expected = JsonMappingException.class)
    public void testDoubleSerializerWithFloatValue() throws JsonProcessingException {
        JsonNode jsonNode = mapper.writeValueAsTree(1.0f);
        Object doubleValue = mapper.readValue(jsonNode.asText(), Double.class);
    }

    @Test
    public void testDoubleSerializerWithLongValue() throws JsonProcessingException {
        JsonNode jsonNode = mapper.writeValueAsTree(1L);
        Object doubleValue = mapper.readValue(jsonNode.asText(), Double.class);
        assertEquals(1.0, doubleValue.doubleValue(), 0.0);
    }
}
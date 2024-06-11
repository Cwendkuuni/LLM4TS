import org.junit.Test;
import static org.junit.Assert.*;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.node.ObjectNode;

public class NumberSerializerTest {

    @Test
    public void testSerializeNumber() throws JsonProcessingException {
        NumberSerializer serializer = NumberSerializer.instance;
        ObjectMapper mapper = new ObjectMapper();
        JsonGenerator gen = mapper.getPolymorphicSerializer(Number.class).getGenerator();

        Number num1 = 123;
        Number num2 = 123.45;
        Number num3 = 123L;
        Number num4 = 123.0F;
        Number num5 = new BigDecimal("123.45");
        Number num6 = new BigInteger("12345678901234567890");

        serializer.serialize(num1, gen, null);
        ObjectNode result1 = mapper.readTree(gen.getOutputBuffer().toString());
        assertEquals("123", result1.get("@numberType").asText());
        assertEquals("123", result1.asText());

        serializer.serialize(num2, gen, null);
        ObjectNode result2 = mapper.readTree(gen.getOutputBuffer().toString());
        assertEquals("123.45", result2.get("@numberType").asText());
        assertEquals("123.45", result2.asText());

        serializer.serialize(num3, gen, null);
        ObjectNode result3 = mapper.readTree(gen.getOutputBuffer().toString());
        assertEquals("123", result3.get("@numberType").asText());
        assertEquals("123", result3.asText());

        serializer.serialize(num4, gen, null);
        ObjectNode result4 = mapper.readTree(gen.getOutputBuffer().toString());
        assertEquals("123.0", result4.get("@numberType").asText());
        assertEquals("123.0", result4.asText());

        serializer.serialize(num5, gen, null);
        ObjectNode result5 = mapper.readTree(gen.getOutputBuffer().toString());
        assertEquals("number", result5.get("@numberType").asText());
        assertEquals("123.45", result5.asText());

        serializer.serialize(num6, gen, null);
        ObjectNode result6 = mapper.readTree(gen.getOutputBuffer().toString());
        assertEquals("number", result6.get("@numberType").asText());
        assertEquals("12345678901234567890", result6.asText());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSerializeNull() throws JsonProcessingException {
        NumberSerializer serializer = NumberSerializer.instance;
        ObjectMapper mapper = new ObjectMapper();
        JsonGenerator gen = mapper.getPolymorphicSerializer(Number.class).getGenerator();

        serializer.serialize(null, gen, null);
    }

    @Test
    public void testSerializeBigDecimalOutOfRange() throws JsonProcessingException {
        NumberSerializer serializer = NumberSerializer.instance;
        ObjectMapper mapper = new ObjectMapper();
        JsonGenerator gen = mapper.getPolymorphicSerializer(Number.class).getGenerator();

        BigDecimal bd = new BigDecimal("9.99999999999999999999999999999E+1500");
        serializer.serialize(bd, gen, null);

        ObjectNode result = mapper.readTree(gen.getOutputBuffer().toString());
        assertEquals("number", result.get("@numberType").asText());
        // assert the serialized value is as expected, or verify that the test fails gracefully
        // for example, using BigDecimal's compareTo method
        // assertFalse(bd.toString().equals(result.asText()));
    }

    @Test
    public void testSerializeBigIntegerOutOfRange() throws JsonProcessingException {
        NumberSerializer serializer = NumberSerializer.instance;
        ObjectMapper mapper = new ObjectMapper();
        JsonGenerator gen = mapper.getPolymorphicSerializer(Number.class).getGenerator();

        BigInteger bi = new BigInteger("9223372036854775807"); // Long.MAX_VALUE
        BigInteger bi2 = new BigInteger("9223372036854775808"); // Long.MAX_VALUE + 1

        serializer.serialize(bi, gen, null);
        ObjectNode result1 = mapper.readTree(gen.getOutputBuffer().toString());
        assertEquals("number", result1.get("@numberType").asText());
        assertEquals(bi.toString(), result1.asText());

        try {
            serializer.serialize(bi2, gen, null);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }
}
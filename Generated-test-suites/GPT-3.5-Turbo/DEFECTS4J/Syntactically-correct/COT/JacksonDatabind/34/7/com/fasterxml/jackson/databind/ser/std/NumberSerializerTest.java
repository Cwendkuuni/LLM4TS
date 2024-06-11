
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.ser.std.NumberSerializer;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class NumberSerializerTest {

    private NumberSerializer numberSerializer = NumberSerializer.instance;

    @Test
    public void testSerializeBigDecimal() throws IOException {
        BigDecimal value = new BigDecimal("123.45");
        JsonGenerator g = new ObjectMapper().getFactory().createGenerator(System.out);
        SerializerProvider provider = new ObjectMapper().getSerializerProvider();
        numberSerializer.serialize(value, g, provider);
        g.flush();
    }

    @Test
    public void testSerializeBigInteger() throws IOException {
        BigInteger value = new BigInteger("12345");
        JsonGenerator g = new ObjectMapper().getFactory().createGenerator(System.out);
        SerializerProvider provider = new ObjectMapper().getSerializerProvider();
        numberSerializer.serialize(value, g, provider);
        g.flush();
    }

    @Test
    public void testSerializeInteger() throws IOException {
        Integer value = 123;
        JsonGenerator g = new ObjectMapper().getFactory().createGenerator(System.out);
        SerializerProvider provider = new ObjectMapper().getSerializerProvider();
        numberSerializer.serialize(value, g, provider);
        g.flush();
    }

    @Test
    public void testSerializeLong() throws IOException {
        Long value = 123L;
        JsonGenerator g = new ObjectMapper().getFactory().createGenerator(System.out);
        SerializerProvider provider = new ObjectMapper().getSerializerProvider();
        numberSerializer.serialize(value, g, provider);
        g.flush();
    }

    @Test
    public void testSerializeDouble() throws IOException {
        Double value = 123.45;
        JsonGenerator g = new ObjectMapper().getFactory().createGenerator(System.out);
        SerializerProvider provider = new ObjectMapper().getSerializerProvider();
        numberSerializer.serialize(value, g, provider);
        g.flush();
    }

    @Test
    public void testSerializeFloat() throws IOException {
        Float value = 123.45f;
        JsonGenerator g = new ObjectMapper().getFactory().createGenerator(System.out);
        SerializerProvider provider = new ObjectMapper().getSerializerProvider();
        numberSerializer.serialize(value, g, provider);
        g.flush();
    }

    @Test
    public void testSerializeByte() throws IOException {
        Byte value = 123;
        JsonGenerator g = new ObjectMapper().getFactory().createGenerator(System.out);
        SerializerProvider provider = new ObjectMapper().getSerializerProvider();
        numberSerializer.serialize(value, g, provider);
        g.flush();
    }

    @Test
    public void testSerializeShort() throws IOException {
        Short value = 123;
        JsonGenerator g = new ObjectMapper().getFactory().createGenerator(System.out);
        SerializerProvider provider = new ObjectMapper().getSerializerProvider();
        numberSerializer.serialize(value, g, provider);
        g.flush();
    }

    @Test
    public void testFallbackNumberSerialization() throws IOException {
        Number value = new Number() {
            @Override
            public int intValue() {
                return 123;
            }

            @Override
            public long longValue() {
                return 123L;
            }

            @Override
            public float floatValue() {
                return 123.45f;
            }

            @Override
            public double doubleValue() {
                return 123.45;
            }

            @Override
            public String toString() {
                return "123";
            }
        };
        JsonGenerator g = new ObjectMapper().getFactory().createGenerator(System.out);
        SerializerProvider provider = new ObjectMapper().getSerializerProvider();
        numberSerializer.serialize(value, g, provider);
        g.flush();
    }

    @Test
    public void testGetSchemaForBigInteger() {
        SerializerProvider provider = new ObjectMapper().getSerializerProvider();
        JsonNode schema = numberSerializer.getSchema(provider, BigInteger.class);
        assertEquals("integer", schema.get("type").asText());
        assertEquals(true, schema.get("required").asBoolean());
    }

    @Test
    public void testGetSchemaForBigDecimal() {
        SerializerProvider provider = new ObjectMapper().getSerializerProvider();
        JsonNode schema = numberSerializer.getSchema(provider, BigDecimal.class);
        assertEquals("number", schema.get("type").asText());
        assertEquals(true, schema.get("required").asBoolean());
    }

    @Test
    public void testAcceptJsonFormatVisitorForBigInteger() throws IOException {
        JsonFormatVisitorWrapper visitor = new ObjectMapper().getFactory().getSerializerProviderInstance()
                .findValueSerializer(BigInteger.class).acceptJsonFormatVisitor(null, BigInteger.class);
        // assert something
    }

    @Test
    public void testAcceptJsonFormatVisitorForBigDecimal() throws IOException {
        JsonFormatVisitorWrapper visitor = new ObjectMapper().getFactory().getSerializerProviderInstance()
                .findValueSerializer(BigDecimal.class).acceptJsonFormatVisitor(null, BigDecimal.class);
        // assert something
    }
}

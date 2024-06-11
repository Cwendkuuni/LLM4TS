import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.PropertyWriter;

public class NumberSerializerTest {

    private NumberSerializer numberSerializer = new NumberSerializer(Number.class);

    @Test
    public void testCreateInstance() {
        assertTrue(numberSerializer != null);
    }

    @Test
    public void testSerializeBigDecimal() throws IOException {
        JsonGenerator jsonGenerator = new ObjectMapper().getFactory().createGenerator(System.out);
        BigDecimal bigDecimal = new BigDecimal("10.5");
        numberSerializer.serialize(bigDecimal, jsonGenerator, new SerializerProvider());
        jsonGenerator.close();
    }

    @Test
    public void testSerializeBigInteger() throws IOException {
        JsonGenerator jsonGenerator = new ObjectMapper().getFactory().createGenerator(System.out);
        BigInteger bigInteger = new BigInteger("10");
        numberSerializer.serialize(bigInteger, jsonGenerator, new SerializerProvider());
        jsonGenerator.close();
    }

    @Test
    public void testSerializeLong() throws IOException {
        JsonGenerator jsonGenerator = new ObjectMapper().getFactory().createGenerator(System.out);
        numberSerializer.serialize(10L, jsonGenerator, new SerializerProvider());
        jsonGenerator.close();
    }

    @Test
    public void testSerializeDouble() throws IOException {
        JsonGenerator jsonGenerator = new ObjectMapper().getFactory().createGenerator(System.out);
        numberSerializer.serialize(10.5, jsonGenerator, new SerializerProvider());
        jsonGenerator.close();
    }

    @Test
    public void testSerializeFloat() throws IOException {
        JsonGenerator jsonGenerator = new ObjectMapper().getFactory().createGenerator(System.out);
        numberSerializer.serialize(10.5f, jsonGenerator, new SerializerProvider());
        jsonGenerator.close();
    }

    @Test
    public void testSerializeInteger() throws IOException {
        JsonGenerator jsonGenerator = new ObjectMapper().getFactory().createGenerator(System.out);
        numberSerializer.serialize(10, jsonGenerator, new SerializerProvider());
        jsonGenerator.close();
    }

    @Test
    public void testSerializeInvalidNumber() {
        JsonGenerator jsonGenerator = null;
        try {
            jsonGenerator = new ObjectMapper().getFactory().createGenerator(System.out);
            numberSerializer.serialize(new HashMap<>(), jsonGenerator, new SerializerProvider());
            fail("Expected IOException not thrown");
        } catch (IOException e) {
            // Expected IOException
        } finally {
            if (jsonGenerator != null) {
                try {
                    jsonGenerator.close();
                } catch (IOException e) {
                    // Ignore
                }
            }
        }
    }

    @Test
    public void testGetSchema() {
        SerializerProvider provider = new SerializerProvider();
        JsonNode schema = numberSerializer.getSchema(provider, BigDecimal.class);
        assertEquals("number", schema.get("type").asText());
        assertTrue(schema.get("required").asBoolean());
    }

    @Test
    public void testAcceptJsonFormatVisitor() {
        // Test not implemented, as it requires a JsonFormatVisitorWrapper and JavaType as parameters
    }
}
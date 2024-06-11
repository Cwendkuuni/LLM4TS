import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonFormatVisitorWrapper;

public class NumberSerializerTest {

    private NumberSerializer numberSerializer;
    private JsonGenerator jsonGenerator;
    private SerializerProvider serializerProvider;

    @Before
    public void setup() {
        numberSerializer = new NumberSerializer(Number.class);
        jsonGenerator = mock(JsonGenerator.class);
        serializerProvider = mock(SerializerProvider.class);
    }

    @Test
    public void testSerializeBigDecimal() throws IOException {
        BigDecimal bigDecimal = new BigDecimal("123.45");
        numberSerializer.serialize(bigDecimal, jsonGenerator, serializerProvider);
        // Verify the mocked JsonGenerator was called with the correct method and argument
    }

    @Test
    public void testSerializeBigInteger() throws IOException {
        BigInteger bigInteger = new BigInteger("12345");
        numberSerializer.serialize(bigInteger, jsonGenerator, serializerProvider);
        // Verify the mocked JsonGenerator was called with the correct method and argument
    }

    // Additional test cases for other Number subtypes should be added here

    @Test
    public void testGetSchemaForBigInteger() {
        JsonNode schema = numberSerializer.getSchema(serializerProvider, BigInteger.class);
        assertEquals("integer", schema.get("type").asText());
    }

    @Test
    public void testGetSchemaForBigDecimal() {
        JsonNode schema = numberSerializer.getSchema(serializerProvider, BigDecimal.class);
        assertEquals("number", schema.get("type").asText());
    }

    // Additional test cases for edge cases and error scenarios should be added here

    @Test
    public void testAcceptJsonFormatVisitorForInt() throws JsonMappingException {
        JsonFormatVisitorWrapper visitor = mock(JsonFormatVisitorWrapper.class);
        numberSerializer.acceptJsonFormatVisitor(visitor, ObjectMapper.mapper().constructType(BigInteger.class));
        // Verify the mocked JsonFormatVisitorWrapper was called with the correct method and argument
    }

    @Test
    public void testAcceptJsonFormatVisitorForFloat() throws JsonMappingException {
        JsonFormatVisitorWrapper visitor = mock(JsonFormatVisitorWrapper.class);
        numberSerializer.acceptJsonFormatVisitor(visitor, ObjectMapper.mapper().constructType(BigDecimal.class));
        // Verify the mocked JsonFormatVisitorWrapper was called with the correct method and argument
    }

    // Additional test cases for different scenarios in acceptJsonFormatVisitor should be added here

}
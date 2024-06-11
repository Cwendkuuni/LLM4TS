import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static com.fasterxml.jackson.core.JsonParser.NumberType.BIG_DECIMAL;
import static com.fasterxml.jackson.core.JsonParser.NumberType.BIG_INTEGER;

import com.fasterxml.jackson.databind.JsonGenerator;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.JsonFormatVisitorWrapper;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.fasterxml.jackson.databind.JsonParser;
import com.fasterxml.jackson.databind.JavaType;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;

public class NumberSerializerTest {

    @Test
    public void testSerializeNumber() throws IOException {
        NumberSerializer numberSerializer = new NumberSerializer(Number.class);

        ObjectMapper objectMapper = new ObjectMapper();
        SerializerProvider serializerProvider = objectMapper.getSerializerProvider();
        JsonGenerator jsonGenerator = mock(JsonGenerator.class);

        Number testNumber = 123;
        numberSerializer.serialize(testNumber, jsonGenerator, serializerProvider);

        String expectedOutput = "123";
        assertEquals(expectedOutput, jsonGenerator.getOutputReadable().getValueAsString());
    }

    @Test
    public void testSerializeBigDecimal() throws IOException {
        NumberSerializer numberSerializer = new NumberSerializer(Number.class);

        ObjectMapper objectMapper = new ObjectMapper();
        SerializerProvider serializerProvider = objectMapper.getSerializerProvider();
        JsonGenerator jsonGenerator = mock(JsonGenerator.class);

        Number testNumber = new BigDecimal("123.45");
        numberSerializer.serialize(testNumber, jsonGenerator, serializerProvider);

        String expectedOutput = "123.45";
        assertEquals(expectedOutput, jsonGenerator.getOutputReadable().getValueAsString());
    }

    @Test
    public void testSerializeBigInteger() throws IOException {
        NumberSerializer numberSerializer = new NumberSerializer(Number.class);

        ObjectMapper objectMapper = new ObjectMapper();
        SerializerProvider serializerProvider = objectMapper.getSerializerProvider();
        JsonGenerator jsonGenerator = mock(JsonGenerator.class);

        Number testNumber = new BigInteger("12345678901234567890");
        numberSerializer.serialize(testNumber, jsonGenerator, serializerProvider);

        String expectedOutput = "12345678901234567890";
        assertEquals(expectedOutput, jsonGenerator.getOutputReadable().getValueAsString());
    }

    @Test
    public void testSerializeInteger() throws IOException {
        NumberSerializer numberSerializer = new NumberSerializer(Number.class);

        ObjectMapper objectMapper = new ObjectMapper();
        SerializerProvider serializerProvider = objectMapper.getSerializerProvider();
        JsonGenerator jsonGenerator = mock(JsonGenerator.class);

        Number testNumber = 123;
        numberSerializer.serialize(testNumber, jsonGenerator, serializerProvider);

        String expectedOutput = "123";
        assertEquals(expectedOutput, jsonGenerator.getOutputReadable().getValueAsString());
    }

    @Test
    public void testSerializeLong() throws IOException {
        NumberSerializer numberSerializer = new NumberSerializer(Number.class);

        ObjectMapper objectMapper = new ObjectMapper();
        SerializerProvider serializerProvider = objectMapper.getSerializerProvider();
        JsonGenerator jsonGenerator = mock(JsonGenerator.class);

        Number testNumber = 1234567890123456789L;
        numberSerializer.serialize(testNumber, jsonGenerator, serializerProvider);

        String expectedOutput = "1234567890123456789";
        assertEquals(expectedOutput, jsonGenerator.getOutputReadable().getValueAsString());
    }

    @Test
    public void testSerializeDouble() throws IOException {
        NumberSerializer numberSerializer = new NumberSerializer(Number.class);

        ObjectMapper objectMapper = new ObjectMapper();
        SerializerProvider serializerProvider = objectMapper.getSerializerProvider();
        JsonGenerator jsonGenerator = mock(JsonGenerator.class);

        Number testNumber = 123.45;
        numberSerializer.serialize(testNumber, jsonGenerator, serializerProvider);

        String expectedOutput = "123.45";
        assertEquals(expectedOutput, jsonGenerator.getOutputReadable().getValueAsString());
    }

    @Test
    public void testSerializeFloat() throws IOException {
        NumberSerializer numberSerializer = new NumberSerializer(Number.class);

        ObjectMapper objectMapper = new ObjectMapper();
        SerializerProvider serializerProvider = objectMapper.getSerializerProvider();
        JsonGenerator jsonGenerator = mock(JsonGenerator.class);

        Number testNumber = 123.45f;
        numberSerializer.serialize(testNumber, jsonGenerator, serializerProvider);

        String expectedOutput = "123.45";
        assertEquals(expectedOutput, jsonGenerator.getOutputReadable().getValueAsString());
    }

    @Test
    public void testSerializeByte() throws IOException {
        NumberSerializer numberSerializer = new NumberSerializer(Number.class);

        ObjectMapper objectMapper = new ObjectMapper();
        SerializerProvider serializerProvider = objectMapper.getSerializerProvider();
        JsonGenerator jsonGenerator = mock(JsonGenerator.class);

        Number testNumber = (byte) 123;
        numberSerializer.serialize(testNumber, jsonGenerator, serializerProvider);

        String expectedOutput = "123";
        assertEquals(expectedOutput, jsonGenerator.getOutputReadable().getValueAsString());
    }

    @Test
    public void testSerializeShort() throws IOException {
        NumberSerializer numberSerializer = new NumberSerializer(Number.class);

        ObjectMapper objectMapper = new ObjectMapper();
        SerializerProvider serializerProvider = objectMapper.getSerializerProvider();
        JsonGenerator jsonGenerator = mock(JsonGenerator.class);

        Number testNumber = (short) 123;
        numberSerializer.serialize(testNumber, jsonGenerator, serializerProvider);

        String expectedOutput = "123";
        assertEquals(expectedOutput, jsonGenerator.getOutputReadable().getValueAsString());
    }

    @Test
    public void testGetSchema() throws IOException {
        NumberSerializer numberSerializer = new NumberSerializer(Number.class);

        ObjectMapper objectMapper = new ObjectMapper();
        SerializerProvider serializerProvider = objectMapper.getSerializerProvider();
        TypeSerializer typeSerializer = mock(TypeSerializer.class);

        JavaType typeHint = JavaType.construct(Number.class);
        JsonNode expectedOutput = createSchemaNode(numberSerializer._isInt ? "integer" : "number", true);
        JsonNode actualOutput = numberSerializer.getSchema(serializerProvider, typeHint);

        assertEquals(expectedOutput, actualOutput);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAcceptJsonFormatVisitorNullTypeHint() throws IOException {
        NumberSerializer numberSerializer = new NumberSerializer(Number.class);

        JsonFormatVisitorWrapper visitor = mock(JsonFormatVisitorWrapper.class);
        SerializerProvider serializerProvider = mock(SerializerProvider.class);

        numberSerializer.acceptJsonFormatVisitor(visitor, null);
    }

    @Test
    public void testAcceptJsonFormatVisitorIntegerType() throws IOException {
        NumberSerializer numberSerializer = new NumberSerializer(Number.class);

        JsonFormatVisitorWrapper visitor = mock(JsonFormatVisitorWrapper.class);
        SerializerProvider serializerProvider = mock(SerializerProvider.class);
        JavaType typeHint = JavaType.construct(BigInteger.class);

        numberSerializer.acceptJsonFormatVisitor(visitor, typeHint);
    }

    @Test(expected = IllegalStateException.class)
    public void testAcceptJsonFormatVisitorNotNumberType() throws IOException {
        NumberSerializer numberSerializer = new NumberSerializer(Number.class);

        JsonFormatVisitorWrapper visitor = mock(JsonFormatVisitorWrapper.class);
        SerializerProvider serializerProvider = mock(SerializerProvider.class);
        JavaType typeHint = JavaType.construct(String.class);

        numberSerializer.acceptJsonFormatVisitor(visitor, typeHint);
    }

    private static JsonNode createSchemaNode(String schemaType, boolean nullable) {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.schema()
                .setAliases(null)
                .setNullValue(null)
                .setRootValue(null)
                .setSchemaStart("{\"type\":\"object\",\"properties\":{}}")
                .setSchemaSeparator("}")
                .setSchemaEnd("}")
                .addProperty("type", schemaType)
                .setSchemaEnd("}")
                .setSchemaEnd("null")
                .setSchemaEnd(nullable ? "": "\n    \"null\":true")
                .getRootValue();
    }
}
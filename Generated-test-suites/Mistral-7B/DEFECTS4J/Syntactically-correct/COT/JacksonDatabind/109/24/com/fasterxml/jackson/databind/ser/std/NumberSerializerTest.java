import org.junit.Test;
import static org.junit.Assert.*;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.json.JsonReader;
import com.fasterxml.jackson.databind.json.JsonWriter;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;

@SuppressWarnings("ResultOfMethodCallIgnored")
public class NumberSerializerTest {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    @Test
    public void testSerializeNumber() throws JsonGenerationException, JsonMappingException, IOException {
        NumberSerializer numberSerializer = NumberSerializer.instance;
        JsonGenerator jsonGenerator = MAPPER.getFactory().createGenerator(new ByteArrayOutputStream(), null);

        Number number = 123;
        numberSerializer.serialize(number, jsonGenerator, null);
        jsonGenerator.flush();

        ByteArrayOutputStream outputStream = (ByteArrayOutputStream) jsonGenerator.getOutputStackTrace();
        String expectedOutput = "123";
        String actualOutput = new String(outputStream.toByteArray());
        assertEquals(expectedOutput, actualOutput);
    }

    @Test
    public void testSerializeBigInteger() throws JsonGenerationException, JsonMappingException, IOException {
        NumberSerializer numberSerializer = NumberSerializer.instance;
        JsonGenerator jsonGenerator = MAPPER.getFactory().createGenerator(new ByteArrayOutputStream(), null);

        BigInteger bigInteger = new BigInteger("123456789012345678901234567890");
        numberSerializer.serialize(bigInteger, jsonGenerator, null);
        jsonGenerator.flush();

        ByteArrayOutputStream outputStream = (ByteArrayOutputStream) jsonGenerator.getOutputStackTrace();
        String expectedOutput = "123456789012345678901234567890";
        String actualOutput = new String(outputStream.toByteArray());
        assertEquals(expectedOutput, actualOutput);
    }

    @Test
    public void testSerializeBigDecimal() throws JsonGenerationException, JsonMappingException, IOException {
        NumberSerializer numberSerializer = NumberSerializer.instance;
        JsonGenerator jsonGenerator = MAPPER.getFactory().createGenerator(new ByteArrayOutputStream(), null);

        BigDecimal bigDecimal = new BigDecimal("123.456");
        numberSerializer.serialize(bigDecimal, jsonGenerator, null);
        jsonGenerator.flush();

        ByteArrayOutputStream outputStream = (ByteArrayOutputStream) jsonGenerator.getOutputStackTrace();
        String expectedOutput = "123.456";
        String actualOutput = new String(outputStream.toByteArray());
        assertEquals(expectedOutput, actualOutput);
    }

    @Test
    public void testSerializeLong() throws JsonGenerationException, JsonMappingException, IOException {
        NumberSerializer numberSerializer = NumberSerializer.instance;
        JsonGenerator jsonGenerator = MAPPER.getFactory().createGenerator(new ByteArrayOutputStream(), null);

        Long longValue = 1234567890123456789L;
        numberSerializer.serialize(longValue, jsonGenerator, null);
        jsonGenerator.flush();

        ByteArrayOutputStream outputStream = (ByteArrayOutputStream) jsonGenerator.getOutputStackTrace();
        String expectedOutput = "1234567890123456789";
        String actualOutput = new String(outputStream.toByteArray());
        assertEquals(expectedOutput, actualOutput);
    }

    @Test
    public void testSerializeDouble() throws JsonGenerationException, JsonMappingException, IOException {
        NumberSerializer numberSerializer = NumberSerializer.instance;
        JsonGenerator jsonGenerator = MAPPER.getFactory().createGenerator(new ByteArrayOutputStream(), null);

        double doubleValue = 123.456;
        numberSerializer.serialize(doubleValue, jsonGenerator, null);
        jsonGenerator.flush();

        ByteArrayOutputStream outputStream = (ByteArrayOutputStream) jsonGenerator.getOutputStackTrace();
        String expectedOutput = "123.456";
        String actualOutput = new String(outputStream.toByteArray());
        assertEquals(expectedOutput, actualOutput);
    }

    @Test
    public void testSerializeFloat() throws JsonGenerationException, JsonMappingException, IOException {
        NumberSerializer numberSerializer = NumberSerializer.instance;
        JsonGenerator jsonGenerator = MAPPER.getFactory().createGenerator(new ByteArrayOutputStream(), null);

        float floatValue = 123.456f;
        numberSerializer.serialize(floatValue, jsonGenerator, null);
        jsonGenerator.flush();

        ByteArrayOutputStream outputStream = (ByteArrayOutputStream) jsonGenerator.getOutputStackTrace();
        String expectedOutput = "123.456";
        String actualOutput = new String(outputStream.toByteArray());
        assertEquals(expectedOutput, actualOutput);
    }

    @Test(expected = JsonMappingException.class)
    public void testSerializeInvalidNumber() throws JsonGenerationException, JsonMappingException, IOException {
        NumberSerializer numberSerializer = NumberSerializer.instance;
        JsonGenerator jsonGenerator = MAPPER.getFactory().createGenerator(new ByteArrayOutputStream(), null);

        Object invalidNumber = new Object();
        numberSerializer.serialize(invalidNumber, jsonGenerator, null);
    }

    @Test
    public void testDeserializeNumber() throws JsonParseException, JsonMappingException, IOException {
        JsonParser jsonParser = MAPPER.getFactory().createParser(new ByteArrayInputStream(("123").getBytes()));

        Number deserializedNumber = MAPPER.readValue(jsonParser, Number.class);
        Long expectedNumber = 123L;
        assertEquals(expectedNumber, deserializedNumber);
    }

    @Test
    public void testDeserializeBigInteger() throws JsonParseException, JsonMappingException, IOException {
        JsonParser jsonParser = MAPPER.getFactory().createParser(new ByteArrayInputStream(("123456789012345678901234567890").getBytes()));

        Number deserializedNumber = MAPPER.readValue(jsonParser, Number.class);
        BigInteger expectedNumber = new BigInteger("123456789012345678901234567890");
        assertEquals(expectedNumber, deserializedNumber);
    }

    @Test
    public void testDeserializeBigDecimal() throws JsonParseException, JsonMappingException, IOException {
        JsonParser jsonParser = MAPPER.getFactory().createParser(new ByteArrayInputStream(("123.456").getBytes()));

        Number deserializedNumber = MAPPER.readValue(jsonParser, Number.class);
        BigDecimal expectedNumber = new BigDecimal("123.456");
        assertEquals(expectedNumber, deserializedNumber);
    }

    @Test(expected = JsonParseException.class)
    public void testDeserializeInvalidNumber() throws JsonParseException, JsonMappingException, IOException {
        JsonParser jsonParser = MAPPER.getFactory().createParser(new ByteArrayInputStream(new byte[0]));

        MAPPER.readValue(jsonParser, Number.class);
    }

    @Test
    public void testCreateContextual() throws JsonMappingException {
        SerializerProvider serializerProvider = new SerializerProvider() {
            @Override
            public JsonSerializer<?> findValueSerializer(Class<?> type, JsonSerializer<?> serializer) {
                return null;
            }
        };
        BeanProperty property = new BeanProperty("testProperty", Number.class);

        JsonSerializer<?> serializer = NumberSerializer.instance.createContextual(serializerProvider, property);
        assertNotNull(serializer);
    }

    @Test
    public void testGetSchema() throws JsonMappingException {
        Type typeHint = Number.class;
        JsonNode schema = NumberSerializer.instance.getSchema(null, typeHint);
        assertEquals("number", schema.get("type").asText());
    }

    @Test
    public void testAcceptJsonFormatVisitor() throws JsonMappingException {
        JsonFormatVisitorWrapper jsonFormatVisitorWrapper = new JsonFormatVisitorWrapper(null, null, null);
        NumberSerializer numberSerializer = NumberSerializer.instance;
        numberSerializer.acceptJsonFormatVisitor(jsonFormatVisitorWrapper, null);
    }
}
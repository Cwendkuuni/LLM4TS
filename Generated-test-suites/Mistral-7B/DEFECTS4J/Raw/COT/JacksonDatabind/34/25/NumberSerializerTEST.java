import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.json.JsonNode;
import com.fasterxml.jackson.databind.type.JavaType;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Currency;

import static org.junit.Assert.assertEquals;

public class NumberSerializerTest {

    private ObjectMapper mapper;

    @Before
    public void setup() {
        mapper = new ObjectMapper();
    }

    @Test
    public void testSerializeNumber() throws IOException {
        NumberSerializer serializer = NumberSerializer.instance;

        JsonGenerator jsonGenerator = mapper.getFactory().createGenerator(System.out);
        SerializerProvider serializerProvider = mapper.getSerializerProvider();

        Number testNumbers[] = {new BigDecimal("123.45"), new BigInteger("54321"), 42, 123L, 1.23f, 1.23d, (byte) 5, (short) 10, 45, Currency.getInstance("USD"), new BigDecimal("0.0"), new BigInteger("0"), 0, null};

        for (Number number : testNumbers) {
            serializer.serialize(number, jsonGenerator, serializerProvider);
            jsonGenerator.flush();
        }
    }

    @Test
    public void testSerializeBigDecimal() throws IOException {
        NumberSerializer serializer = NumberSerializer.instance;

        ObjectMapper mapper = new ObjectMapper();
        JsonGenerator jsonGenerator = mapper.getFactory().createGenerator(System.out);
        SerializerProvider serializerProvider = mapper.getSerializerProvider();

        Number testNumber = new BigDecimal("123.45");

        serializer.serialize(testNumber, jsonGenerator, serializerProvider);
        jsonGenerator.flush();

        JsonParser jsonParser = mapper.getFactory().createParser(System.in);
        JsonNode readValue = jsonParser.readValueAs(JsonNode.class);
        assertEquals("123.45", readValue.asText());
    }

    @Test
    public void testSerializeBigInteger() throws IOException {
        NumberSerializer serializer = NumberSerializer.instance;

        ObjectMapper mapper = new ObjectMapper();
        JsonGenerator jsonGenerator = mapper.getFactory().createGenerator(System.out);
        SerializerProvider serializerProvider = mapper.getSerializerProvider();

        Number testNumber = new BigInteger("54321");

        serializer.serialize(testNumber, jsonGenerator, serializerProvider);
        jsonGenerator.flush();

        JsonParser jsonParser = mapper.getFactory().createParser(System.in);
        JsonNode readValue = jsonParser.readValueAs(JsonNode.class);
        assertEquals("54321", readValue.asText());
    }

    @Test
    public void testSerializeInteger() throws IOException {
        NumberSerializer serializer = NumberSerializer.instance;

        ObjectMapper mapper = new ObjectMapper();
        JsonGenerator jsonGenerator = mapper.getFactory().createGenerator(System.out);
        SerializerProvider serializerProvider = mapper.getSerializerProvider();

        Number testNumber = 42;

        serializer.serialize(testNumber, jsonGenerator, serializerProvider);
        jsonGenerator.flush();

        JsonParser jsonParser = mapper.getFactory().createParser(System.in);
        JsonNode readValue = jsonParser.readValueAs(JsonNode.class);
        assertEquals("42", readValue.asText());
    }

    @Test
    public void testSerializeLong() throws IOException {
        NumberSerializer serializer = NumberSerializer.instance;

        ObjectMapper mapper = new ObjectMapper();
        JsonGenerator jsonGenerator = mapper.getFactory().createGenerator(System.out);
        SerializerProvider serializerProvider = mapper.getSerializerProvider();

        Number testNumber = 123L;

        serializer.serialize(testNumber, jsonGenerator, serializerProvider);
        jsonGenerator.flush();

        JsonParser jsonParser = mapper.getFactory().createParser(System.in);
        JsonNode readValue = jsonParser.readValueAs(JsonNode.class);
        assertEquals("123", readValue.asText());
    }

    @Test
    public void testSerializeFloat() throws IOException {
        NumberSerializer serializer = NumberSerializer.instance;

        ObjectMapper mapper = new ObjectMapper();
        JsonGenerator jsonGenerator = mapper.getFactory().createGenerator(System.out);
        SerializerProvider serializerProvider = mapper.getSerializerProvider();

        Number testNumber = 1.23f;

        serializer.serialize(testNumber, jsonGenerator, serializerProvider);
        jsonGenerator.flush();

        JsonParser jsonParser = mapper.getFactory().createParser(System.in);
        JsonNode readValue = jsonParser.readValueAs(JsonNode.class);
        assertEquals("1.23", readValue.asText());
    }

    @Test
    public void testSerializeDouble() throws IOException {
        NumberSerializer serializer = NumberSerializer.instance;

        ObjectMapper mapper = new ObjectMapper();
        JsonGenerator jsonGenerator = mapper.getFactory().createGenerator(System.out);
        SerializerProvider serializerProvider = mapper.getSerializerProvider();

        Number testNumber = 1.23d;

        serializer.serialize(testNumber, jsonGenerator, serializerProvider);
        jsonGenerator.flush();

        JsonParser jsonParser = mapper.getFactory().createParser(System.in);
        JsonNode readValue = jsonParser.readValueAs(JsonNode.class);
        assertEquals("1.23", readValue.asText());
    }

    @Test
    public void testSerializeByte() throws IOException {
        NumberSerializer serializer = NumberSerializer.instance;

        ObjectMapper mapper = new ObjectMapper();
        JsonGenerator jsonGenerator = mapper.getFactory().createGenerator(System.out);
        SerializerProvider serializerProvider = mapper.getSerializerProvider();

        Number testNumber = (byte) 5;

        serializer.serialize(testNumber, jsonGenerator, serializerProvider);
        jsonGenerator.flush();

        JsonParser jsonParser = mapper.getFactory().createParser(System.in);
        JsonNode readValue = jsonParser.readValueAs(JsonNode.class);
        assertEquals("5", readValue.asText());
    }

    @Test
    public void testSerializeShort() throws IOException {
        NumberSerializer serializer = NumberSerializer.instance;

        ObjectMapper mapper = new ObjectMapper();
        JsonGenerator jsonGenerator = mapper.getFactory().createGenerator(System.out);
        SerializerProvider serializerProvider = mapper.getSerializerProvider();

        Number testNumber = (short) 10;

        serializer.serialize(testNumber, jsonGenerator, serializerProvider);
        jsonGenerator.flush();

        JsonParser jsonParser = mapper.getFactory().createParser(System.in);
        JsonNode readValue = jsonParser.readValueAs(JsonNode.class);
        assertEquals("10", readValue.asText());
    }

    @Test
    public void testSerializeCurrency() throws IOException {
        NumberSerializer serializer = NumberSerializer.instance;

        ObjectMapper mapper = new ObjectMapper();
        JsonGenerator jsonGenerator = mapper.getFactory().createGenerator(System.out);
        SerializerProvider serializerProvider = mapper.getSerializerProvider();

        Number testNumber = Currency.getInstance("USD");

        serializer.serialize(testNumber, jsonGenerator, serializerProvider);
        jsonGenerator.flush();

        JsonParser jsonParser = mapper.getFactory().createParser(System.in);
        JsonNode readValue = jsonParser.readValueAs(JsonNode.class);
        assertEquals("{\"type\":\"currency\",\"currency\":\"USD\"}", readValue.asText());
    }

    @Test
    public void testSerializeZero() throws IOException {
        NumberSerializer serializer = NumberSerializer.instance;

        ObjectMapper mapper = new ObjectMapper();
        JsonGenerator jsonGenerator = mapper.getFactory().createGenerator(System.out);
        SerializerProvider serializerProvider = mapper.getSerializerProvider();

        Number testNumber = new BigDecimal("0.0");

        serializer.serialize(testNumber, jsonGenerator, serializerProvider);
        jsonGenerator.flush();

        JsonParser jsonParser = mapper.getFactory().createParser(System.in);
        JsonNode readValue = jsonParser.readValueAs(JsonNode.class);
        assertEquals("0.0", readValue.asText());
    }

    @Test
    public void testSerializeZeroInt() throws IOException {
        NumberSerializer serializer = NumberSerializer.instance;

        ObjectMapper mapper = new ObjectMapper();
        JsonGenerator jsonGenerator = mapper.getFactory().createGenerator(System.out);
        SerializerProvider serializerProvider = mapper.getSerializerProvider();

        Number testNumber = new BigInteger("0");

        serializer.serialize(testNumber, jsonGenerator, serializerProvider);
        jsonGenerator.flush();

        JsonParser jsonParser = mapper.getFactory().createParser(System.in);
        JsonNode readValue = jsonParser.readValueAs(JsonNode.class);
        assertEquals("0", readValue.asText());
    }

    @Test
    public void testSerializeZeroLong() throws IOException {
        NumberSerializer serializer = NumberSerializer.instance;

        ObjectMapper mapper = new ObjectMapper();
        JsonGenerator jsonGenerator = mapper.getFactory().createGenerator(System.out);
        SerializerProvider serializerProvider = mapper.getSerializerProvider();

        Number testNumber = 0L;

        serializer.serialize(testNumber, jsonGenerator, serializerProvider);
        jsonGenerator.flush();

        JsonParser jsonParser = mapper.getFactory().createParser(System.in);
        JsonNode readValue = jsonParser.readValueAs(JsonNode.class);
        assertEquals("0", readValue.asText());
    }

    @Test(expected = IOException.class)
    public void testSerializeNull() throws IOException {
        NumberSerializer serializer = NumberSerializer.instance;

        ObjectMapper mapper = new ObjectMapper();
        JsonGenerator jsonGenerator = mapper.getFactory().createGenerator(System.out);
        SerializerProvider serializerProvider = mapper.getSerializerProvider();

        Number testNumber = null;

        try {
            serializer.serialize(testNumber, jsonGenerator, serializerProvider);
            jsonGenerator.flush();
        } finally {
            jsonGenerator.close();
        }
    }

    @Test
    public void testGetSchema() throws IOException {
        NumberSerializer serializer = NumberSerializer.instance;

        SerializerProvider serializerProvider = mapper.getSerializerProvider();
        Type typeHint = JavaType.constructType(Number.class);

        JsonNode schema = serializer.getSchema(serializerProvider, typeHint);
        assertEquals("number", schema.asText());
    }

    @Test
    public void testAcceptJsonFormatVisitor() throws JsonMappingException {
        ObjectMapper mapper = new ObjectMapper();

        JsonFormatVisitorWrapper visitor = mapper.getSerializationConfig().findRootValueSerializer(Number.class);
        NumberSerializer serializer = NumberSerializer.instance;

        if (serializer._isInt) {
            serializer.acceptJsonFormatVisitor(visitor, typeHint);
        } else {
            Class<?> handledType = serializer.handledType();
            if (handledType == BigDecimal.class) {
                visitor.expectNumberFormat(typeHint);
            }
        }
    }
}
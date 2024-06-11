import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.jsontype.TypeIdResolver;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Locale;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SuppressWarnings("unchecked")
public class NumberSerializerTest {

    private static final NumberSerializer NUMBER_SERIALIZER = new NumberSerializer(Number.class);

    @Test
    public void testSerializeInt() throws IOException {
        JsonGenerator generator = createJsonGenerator();
        Number serializedNumber = 42;
        NUMBER_SERIALIZER.serialize(serializedNumber, generator, new SerializerProvider());
        JsonParser parser = createJsonParser(generator.getOutputStream().toString());
        Number deserializedNumber = parser.readValueAs(Number.class);
        assertEquals(serializedNumber, deserializedNumber);
    }

    @Test
    public void testSerializeLong() throws IOException {
        JsonGenerator generator = createJsonGenerator();
        Number serializedNumber = Long.MAX_VALUE;
        NUMBER_SERIALIZER.serialize(serializedNumber, generator, new SerializerProvider());
        JsonParser parser = createJsonParser(generator.getOutputStream().toString());
        Number deserializedNumber = parser.readValueAs(Number.class);
        assertEquals(serializedNumber, deserializedNumber);
    }

    @Test
    public void testSerializeDouble() throws IOException {
        JsonGenerator generator = createJsonGenerator();
        Number serializedNumber = 3.14;
        NUMBER_SERIALIZER.serialize(serializedNumber, generator, new SerializerProvider());
        JsonParser parser = createJsonParser(generator.getOutputStream().toString());
        Number deserializedNumber = parser.readValueAs(Number.class);
        assertEquals(serializedNumber, deserializedNumber);
    }

    @Test
    public void testSerializeFloat() throws IOException {
        JsonGenerator generator = createJsonGenerator();
        Number serializedNumber = 1.5f;
        NUMBER_SERIALIZER.serialize(serializedNumber, generator, new SerializerProvider());
        JsonParser parser = createJsonParser(generator.getOutputStream().toString());
        Number deserializedNumber = parser.readValueAs(Number.class);
        assertEquals(serializedNumber, deserializedNumber);
    }

    @Test
    public void testSerializeBigInteger() throws IOException {
        JsonGenerator generator = createJsonGenerator();
        Number serializedNumber = new BigInteger("123456789012345678901234567890");
        NUMBER_SERIALIZER.serialize(serializedNumber, generator, new SerializerProvider());
        JsonParser parser = createJsonParser(generator.getOutputStream().toString());
        Number deserializedNumber = parser.readValueAs(Number.class);
        assertEquals(serializedNumber, deserializedNumber);
    }

    @Test
    public void testSerializeBigDecimal() throws IOException {
        JsonGenerator generator = createJsonGenerator();
        Number serializedNumber = new BigDecimal("123.456.789,123");
        NUMBER_SERIALIZER.serialize(serializedNumber, generator, new SerializerProvider());
        JsonParser parser = createJsonParser(generator.getOutputStream().toString());
        Number deserializedNumber = parser.readValueAs(Number.class);
        assertEquals(serializedNumber, deserializedNumber);
    }

    @Test
    public void testSerializeString() throws IOException {
        JsonGenerator generator = createJsonGenerator();
        Number serializedNumber = "42";
        NUMBER_SERIALIZER.serialize(serializedNumber, generator, new SerializerProvider());
        JsonParser parser = createJsonParser(generator.getOutputStream().toString());
        Number deserializedNumber = parser.readValueAs(Number.class);
        assertEquals(BigInteger.valueOf(42L), deserializedNumber);
    }

    @Test
    public void testCreateContextualNull() throws JsonMappingException {
        SerializerProvider provider = new SerializerProvider();
        BeanProperty property = null;
        assertThrows(JsonMappingException.class, () -> NUMBER_SERIALIZER.createContextual(provider, property));
    }

    @Test
    public void testCreateContextualEmptyProperty() throws JsonMappingException {
        SerializerProvider provider = new SerializerProvider();
        BeanProperty property = new BeanProperty("test", JsonType.NUMBER, null, null, new Object(), new Object());
        assertThrows(JsonMappingException.class, () -> NUMBER_SERIALIZER.createContextual(provider, property));
    }

    @Test
    public void testCreateContextualBigDecimalFormat() throws JsonMappingException {
        SerializerProvider provider = new SerializerProvider();
        BeanProperty property = new BeanProperty("test", JsonType.NUMBER, null, new JsonFormat.Value(JsonFormat.Shape.STRING, null, null, null, null, null, Locale.ENGLISH, null), new Object(), new Object());
        JsonSerializer<?> result = NUMBER_SERIALIZER.createContextual(provider, property);
        Assertions.assertInstanceOf(ToStringSerializer.class, result);
    }

    @Test
    public void testSerializeNull() throws IOException {
        JsonGenerator generator = createJsonGenerator();
        NUMBER_SERIALIZER.serialize(null, generator, new SerializerProvider());
        JsonParser parser = createJsonParser(generator.getOutputStream().toString());
        Number deserializedNumber = parser.readValueAs(Number.class);
        Assertions.assertNull(deserializedNumber);
    }

    @Test
    public void testSerializeNaN() throws IOException {
        JsonGenerator generator = createJsonGenerator();
        Number serializedNumber = Double.NaN;
        assertThrows(JsonGenerationException.class, () -> NUMBER_SERIALIZER.serialize(serializedNumber, generator, new SerializerProvider()));
    }

    @Test
    public void testSerializeInfinity() throws IOException {
        JsonGenerator generator = createJsonGenerator();
        Number serializedNumber = Double.POSITIVE_INFINITY;
        assertThrows(JsonGenerationException.class, () -> NUMBER_SERIALIZER.serialize(serializedNumber, generator, new SerializerProvider()));
    }

    @Test
    public void testSerializeBigDecimalWithScale() throws IOException {
        JsonGenerator generator = createJsonGenerator();
        Number serializedNumber = new BigDecimal("123.456.789,123").setScale(3, BigDecimal.ROUND_HALF_UP);
        NUMBER_SERIALIZER.serialize(serializedNumber, generator, new SerializerProvider());
        JsonParser parser = createJsonParser(generator.getOutputStream().toString());
        Number deserializedNumber = parser.readValueAs(Number.class);
        assertEquals(serializedNumber, deserializedNumber);
    }

    @Test
    public void testSerializeBigIntegerWithLeadingZeroes() throws IOException {
        JsonGenerator generator = createJsonGenerator();
        Number serializedNumber = new BigInteger("-0012345678901234567890");
        NUMBER_SERIALIZER.serialize(serializedNumber, generator, new SerializerProvider());
        JsonParser parser = createJsonParser(generator.getOutputStream().toString());
        Number deserializedNumber = parser.readValueAs(Number.class);
        assertEquals(serializedNumber, deserializedNumber);
    }

    @Test
    public void testGetSchema() {
        JsonSchema schema = NUMBER_SERIALIZER.getSchema(new SerializerProvider(), Number.class);
        assertEquals("number", schema.getType().getRawClass().getSimpleName());
        assertTrue(schema.getDescription().isPresent());
    }

    private JsonGenerator createJsonGenerator() throws IOException {
        return new JsonFactory().createGenerator(new ByteArrayOutputStream(), new DefaultSerializerProvider());
    }

    private JsonParser createJsonParser(String jsonString) throws IOException {
        return new JsonFactory().createParser(jsonString);
    }
}
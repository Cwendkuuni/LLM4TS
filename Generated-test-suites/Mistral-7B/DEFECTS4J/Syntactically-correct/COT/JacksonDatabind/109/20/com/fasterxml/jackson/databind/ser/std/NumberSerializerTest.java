import org.junit.Test;
import static org.junit.Assert.*;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.json.JsonSerializer.Typing;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;

/**
 * Test class for NumberSerializer.
 */
public class NumberSerializerTest {

    private static final ObjectMapper mapper = new ObjectMapper();

    @Test
    public void testSerializeNumber() throws JsonGenerationException, IOException {
        // Test serialization of various Number types
        Number number = 123;
        NumberSerializer numberSerializer = new NumberSerializer(Number.class);
        JsonGenerator generator = mapper.getFactory().createGenerator();
        numberSerializer.serialize(number, generator, new SerializerProvider());
        String result = generator.getOutputFactory().writeValueAsString(generator.getParser().getValueAsString());
        assertEquals("123", result);

        number = new BigDecimal("123.45");
        generator.flush();
        generator.reset();
        numberSerializer.serialize(number, generator, new SerializerProvider());
        result = generator.getOutputFactory().writeValueAsString(generator.getParser().getValueAsString());
        assertEquals("123.45", result);

        number = new BigInteger("12345678901234567890");
        generator.flush();
        generator.reset();
        numberSerializer.serialize(number, generator, new SerializerProvider());
        result = generator.getOutputFactory().writeValueAsString(generator.getParser().getValueAsString());
        assertEquals("12345678901234567890", result);

        number = 123L;
        generator.flush();
        generator.reset();
        numberSerializer.serialize(number, generator, new SerializerProvider());
        result = generator.getOutputFactory().writeValueAsString(generator.getParser().getValueAsString());
        assertEquals("123", result);

        number = 123.45D;
        generator.flush();
        generator.reset();
        numberSerializer.serialize(number, generator, new SerializerProvider());
        result = generator.getOutputFactory().writeValueAsString(generator.getParser().getValueAsString());
        assertEquals("123.45", result);

        number = 123.0F;
        generator.flush();
        generator.reset();
        numberSerializer.serialize(number, generator, new SerializerProvider());
        result = generator.getOutputFactory().writeValueAsString(generator.getParser().getValueAsString());
        assertEquals("123.0", result);

        number = 0x123;
        generator.flush();
        generator.reset();
        numberSerializer.serialize(number, generator, new SerializerProvider());
        result = generator.getOutputFactory().writeValueAsString(generator.getParser().getValueAsString());
        assertEquals("123", result);

        // Test serialization of null
        generator.flush();
        generator.reset();
        NumberSerializer nullNumberSerializer = new NumberSerializer(null);
        nullNumberSerializer.serialize(null, generator, new SerializerProvider());
        result = generator.getOutputFactory().writeValueAsString(generator.getParser().getValueAsString());
        assertNull(result);
    }

    @Test
    public void testDeserializeNumber() throws JsonGenerationException, IOException {
        // Test deserialization of various Number types
        String numberString = "123";
        JsonParser parser = mapper.getFactory().createParser(numberString);
        ObjectDeserializer numberDeserializer = mapper.findValueDeserializer(Number.class, parser);
        Number deserializedNumber = numberDeserializer.deserialize(parser, null, new DeserializationContext());
        assertEquals(123, deserializedNumber.intValue());

        numberString = "123.45";
        parser = mapper.getFactory().createParser(numberString);
        numberDeserializer = mapper.findValueDeserializer(Number.class, parser);
        deserializedNumber = numberDeserializer.deserialize(parser, null, new DeserializationContext());
        assertEquals(new BigDecimal("123.45"), deserializedNumber);

        numberString = "12345678901234567890";
        parser = mapper.getFactory().createParser(numberString);
        numberDeserializer = mapper.findValueDeserializer(Number.class, parser);
        deserializedNumber = numberDeserializer.deserialize(parser, null, new DeserializationContext());
        assertEquals(new BigInteger("12345678901234567890"), deserializedNumber);

        numberString = "123L";
        parser = mapper.getFactory().createParser(numberString);
        numberDeserializer = mapper.findValueDeserializer(Number.class, parser);
        deserializedNumber = numberDeserializer.deserialize(parser, null, new DeserializationContext());
        assertEquals(123L, deserializedNumber.longValue());

        numberString = "123.45D";
        parser = mapper.getFactory().createParser(numberString);
        numberDeserializer = mapper.findValueDeserializer(Number.class, parser);
        deserializedNumber = numberDeserializer.deserialize(parser, null, new DeserializationContext());
        assertEquals(new Double(123.45), deserializedNumber);

        numberString = "123.0F";
        parser = mapper.getFactory().createParser(numberString);
        numberDeserializer = mapper.findValueDeserializer(Number.class, parser);
        deserializedNumber = numberDeserializer.deserialize(parser, null, new DeserializationContext());
        assertEquals(new Float(123.0), deserializedNumber);

        numberString = "0x123";
        parser = mapper.getFactory().createParser(numberString);
        numberDeserializer = mapper.findValueDeserializer(Number.class, parser);
        deserializedNumber = numberDeserializer.deserialize(parser, null, new DeserializationContext());
        assertEquals(new Integer(123), deserializedNumber);

        // Test deserialization of null
        parser = mapper.getFactory().createParser("null");
        numberDeserializer = mapper.findValueDeserializer(Number.class, parser);
        deserializedNumber = numberDeserializer.deserialize(parser, null, new DeserializationContext());
        assertNull(deserializedNumber);
    }

    @Test
    public void testSerializeBigDecimalWithFormat() throws JsonGenerationException, IOException {
        // Test serialization of BigDecimal with custom format
        JsonGenerator generator = mapper.getFactory().createGenerator();
        ObjectWriter writer = mapper.writerWithView(JsonSerializationShaper.Shaper.forSerializationOf(BigDecimal.class));
        JsonSerializer<BigDecimal> bigDecimalSerializer = writer.findValueSerializer(BigDecimal.class, null);

        BigDecimal number = new BigDecimal("123.45");
        JsonSerializer<JsonNode>.Typing typing = Typing.TYPING_NONE;
        bigDecimalSerializer.serialize(number, generator, new SerializerProvider(), null, typing);
        String result = generator.getOutputFactory().writeValueAsString(generator.getParser().getValueAsString());
        assertEquals("\"123.45\"", result);
    }

    @Test(expected = JsonMappingException.class)
    public void testSerializeBigDecimalWithFormatThrowsException() throws IOException {
        // Test that serialization of BigDecimal with custom format throws exception
        JsonGenerator generator = mapper.getFactory().createGenerator();
        NumberSerializer numberSerializer = new NumberSerializer(BigDecimal.class);

        BigDecimal number = new BigDecimal("123.45");
        numberSerializer.serialize(number, generator, new SerializerProvider());
    }

    @Test
    public void testDeserializeBigDecimalFormat() throws JsonGenerationException, IOException {
        // Test deserialization of BigDecimal with custom format
        String json = "\"123.45\"";
        JsonParser parser = mapper.getFactory().createParser(json);
        ObjectDeserializer bigDecimalDeserializer = mapper.findValueDeserializer(BigDecimal.class, parser);
        Number deserializedNumber = bigDecimalDeserializer.deserialize(parser, null, new DeserializationContext());
        assertEquals(new BigDecimal("123.45"), deserializedNumber);
    }

    @Test(expected = JsonMappingException.class)
    public void testDeserializeBigDecimalFormatThrowsException() throws IOException {
        // Test that deserialization of BigDecimal with custom format throws exception
        String json = "123.45";
        JsonParser parser = mapper.getFactory().createParser(json);
        ObjectDeserializer bigDecimalDeserializer = mapper.findValueDeserializer(Number.class, parser);
    }

    @Test
    public void testDeserializeBigIntegerFormat() throws JsonGenerationException, IOException {
        // Test deserialization of BigInteger with custom format
        String json = "\"12345678901234567890\"";
        JsonParser parser = mapper.getFactory().createParser(json);
        ObjectDeserializer bigIntegerDeserializer = mapper.findValueDeserializer(BigInteger.class, parser);
        Number deserializedNumber = bigIntegerDeserializer.deserialize(parser, null, new DeserializationContext());
        assertEquals(new BigInteger("12345678901234567890"), deserializedNumber);
    }

    @Test(expected = JsonMappingException.class)
    public void testDeserializeBigIntegerFormatThrowsException() throws IOException {
        // Test that deserialization of BigInteger with custom format throws exception
        String json = "12345678901234567890";
        JsonParser parser = mapper.getFactory().createParser(json);
        ObjectDeserializer bigIntegerDeserializer = mapper.findValueDeserializer(Number.class, parser);
    }
}
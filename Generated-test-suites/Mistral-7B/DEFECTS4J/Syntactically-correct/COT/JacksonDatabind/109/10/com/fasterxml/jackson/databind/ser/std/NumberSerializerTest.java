import org.junit.Test;
import static org.junit.Assert.*;

import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.JsonNode;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Random;

public class NumberSerializerTest {

    private static final ObjectMapper mapper = new ObjectMapper();

    @Test
    public void testSerializeNumber() throws IOException {
        NumberSerializer serializer = NumberSerializer.instance;
        JsonGenerator generator = mapper.getFactory().createGenerator(System.out);
        SerializerProvider provider = mapper.getSerializerProvider();

        Number number = 123;
        serializer.serialize(number, generator, provider);
        generator.flush();

        assertEquals("123", generator.getOutputContentAsString());

        number = new BigDecimal("123.45");
        serializer.serialize(number, generator, provider);
        generator.flush();

        assertEquals("123.45", generator.getOutputContentAsString());

        number = new BigInteger("12345678901234567890");
        serializer.serialize(number, generator, provider);
        generator.flush();

        assertEquals("12345678901234567890", generator.getOutputContentAsString());
    }

    @Test
    public void testSerializeLong() throws IOException {
        NumberSerializer serializer = NumberSerializer.instance;
        JsonGenerator generator = mapper.getFactory().createGenerator(System.out);
        SerializerProvider provider = mapper.getSerializerProvider();

        Long longNumber = 123L;
        serializer.serialize(longNumber, generator, provider);
        generator.flush();

        assertEquals("123", generator.getOutputContentAsString());
    }

    @Test
    public void testSerializeDouble() throws IOException {
        NumberSerializer serializer = NumberSerializer.instance;
        JsonGenerator generator = mapper.getFactory().createGenerator(System.out);
        SerializerProvider provider = mapper.getSerializerProvider();

        Double doubleNumber = 123.45;
        serializer.serialize(doubleNumber, generator, provider);
        generator.flush();

        assertEquals("123.45", generator.getOutputContentAsString());
    }

    @Test
    public void testSerializeFloat() throws IOException {
        NumberSerializer serializer = NumberSerializer.instance;
        JsonGenerator generator = mapper.getFactory().createGenerator(System.out);
        SerializerProvider provider = mapper.getSerializerProvider();

        Float floatNumber = 123.45f;
        serializer.serialize(floatNumber, generator, provider);
        generator.flush();

        assertEquals("123.45", generator.getOutputContentAsString());
    }

    @Test
    public void testSerializeInteger() throws IOException {
        NumberSerializer serializer = NumberSerializer.instance;
        JsonGenerator generator = mapper.getFactory().createGenerator(System.out);
        SerializerProvider provider = mapper.getSerializerProvider();

        Integer integerNumber = 123;
        serializer.serialize(integerNumber, generator, provider);
        generator.flush();

        assertEquals("123", generator.getOutputContentAsString());
    }

    @Test
    public void testSerializeByte() throws IOException {
        NumberSerializer serializer = NumberSerializer.instance;
        JsonGenerator generator = mapper.getFactory().createGenerator(System.out);
        SerializerProvider provider = mapper.getSerializerProvider();

        Byte byteNumber = 123;
        serializer.serialize(byteNumber, generator, provider);
        generator.flush();

        assertEquals("123", generator.getOutputContentAsString());
    }

    @Test
    public void testSerializeShort() throws IOException {
        NumberSerializer serializer = NumberSerializer.instance;
        JsonGenerator generator = mapper.getFactory().createGenerator(System.out);
        SerializerProvider provider = mapper.getSerializerProvider();

        Short shortNumber = 123;
        serializer.serialize(shortNumber, generator, provider);
        generator.flush();

        assertEquals("123", generator.getOutputContentAsString());
    }

    @Test
    public void testSerializeBigDecimalWithFormat() throws IOException {
        NumberSerializer serializer = NumberSerializer.instance;
        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);

        JsonFormat.Shape shape = JsonFormat.Shape.STRING;
        JsonGenerator generator = mapper.getFactory().createGenerator(System.out);
        SerializerProvider provider = mapper.getSerializerProvider();

        JsonFormat format = new JsonFormat(new NumberSerializer.NumberFormat("0.00"), shape);

        BigDecimal bigDecimal = new BigDecimal("123.45");
        JsonNode node = mapper.readerForTree(new Object()).readValueAsTree(
                "{\"number\": " + bigDecimal + "}", provider.getDefaultSerializerProvider()
        );

        serializer.acceptJsonFormatVisitor(provider.getActiveContext().findSerializerProvider().getSerializerProvider(), node.getClass());
        serializer.serialize(bigDecimal, generator, provider);
        generator.writeStartObject();
        generator.writeFieldName("number");
        generator.writeValue(format);
        generator.writeEndObject();
        generator.flush();

        String expected = "{\"number\":{\"format\":{\"pattern\":\"0.00\",\"bigDecimalFormat\":null,\"integerFormat\":null,\"numberFormat\":{\"pattern\":\"%s\",\"precision\":1}}}}";
        String actual = generator.getOutputContentAsString();
        assertEquals(String.format(expected, "0.00"), actual);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSerializeInvalidNumber() throws IOException {
        NumberSerializer serializer = NumberSerializer.instance;
        JsonGenerator generator = mapper.getFactory().createGenerator(System.out);
        SerializerProvider provider = mapper.getSerializerProvider();

        Object obj = new Random().nextObject();
        serializer.serialize(obj, generator, provider);
    }

    @Test
    public void testDeserializeNumber() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonParser parser = mapper.getFactory().createParser(getClass().getResourceAsStream("/test_numbers.json"));
        JsonNode node = mapper.readTree(parser);

        JsonNode numberNode = node.get("number");
        Number number = mapper.readValue(numberNode.toString(), Number.class);

        assertEquals(new BigDecimal("123.45"), number);
        assertEquals(new BigInteger("12345678901234567890"), new BigInteger(number.toString()));
        assertEquals(123, new Integer(number.toString()));
        assertEquals(123.45, new Double(number.toString()));
        assertEquals(123.45F, new Float(number.toString()));
    }

    @Test(expected = IOException.class)
    public void testDeserializeInvalidJson() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonParser parser = mapper.getFactory().createParser(getClass().getResourceAsStream("/invalid_json.json"));
        JsonNode node = mapper.readTree(parser);
    }
}
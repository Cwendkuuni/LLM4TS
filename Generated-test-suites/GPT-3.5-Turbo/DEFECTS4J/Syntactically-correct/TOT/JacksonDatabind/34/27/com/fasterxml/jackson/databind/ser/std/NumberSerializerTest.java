import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.NumberSerializer;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.io.StringWriter;
import java.math.BigDecimal;
import java.math.BigInteger;

public class NumberSerializerTest {

    private NumberSerializer serializer;
    private JsonGenerator jsonGenerator;
    private StringWriter writer;
    private SerializerProvider serializerProvider;

    @Before
    public void setup() {
        serializer = new NumberSerializer(Number.class);
        writer = new StringWriter();
        jsonGenerator = new JsonFactory().createGenerator(writer);
        ObjectMapper mapper = new ObjectMapper();
        serializerProvider = mapper.getSerializerProvider();
    }

    @Test
    public void testSerializeWithBigDecimal() throws IOException {
        BigDecimal value = new BigDecimal("123.456");
        
        serializer.serialize(value, jsonGenerator, serializerProvider);
        jsonGenerator.flush();

        String expected = "123.456";
        String actual = writer.toString();

        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testSerializeWithBigInteger() throws IOException {
        BigInteger value = new BigInteger("12345678901234567890");
        
        serializer.serialize(value, jsonGenerator, serializerProvider);
        jsonGenerator.flush();

        String expected = "12345678901234567890";
        String actual = writer.toString();

        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testSerializeWithInteger() throws IOException {
        Integer value = 123;
        
        serializer.serialize(value, jsonGenerator, serializerProvider);
        jsonGenerator.flush();

        String expected = "123";
        String actual = writer.toString();

        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testSerializeWithLong() throws IOException {
        Long value = 123L;
        
        serializer.serialize(value, jsonGenerator, serializerProvider);
        jsonGenerator.flush();

        String expected = "123";
        String actual = writer.toString();

        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testSerializeWithDouble() throws IOException {
        Double value = 123.456;
        
        serializer.serialize(value, jsonGenerator, serializerProvider);
        jsonGenerator.flush();

        String expected = "123.456";
        String actual = writer.toString();

        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testSerializeWithFloat() throws IOException {
        Float value = 123.456f;
        
        serializer.serialize(value, jsonGenerator, serializerProvider);
        jsonGenerator.flush();

        String expected = "123.456";
        String actual = writer.toString();

        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testSerializeWithByte() throws IOException {
        Byte value = 123;
        
        serializer.serialize(value, jsonGenerator, serializerProvider);
        jsonGenerator.flush();

        String expected = "123";
        String actual = writer.toString();

        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testSerializeWithShort() throws IOException {
        Short value = 123;
        
        serializer.serialize(value, jsonGenerator, serializerProvider);
        jsonGenerator.flush();

        String expected = "123";
        String actual = writer.toString();

        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testSerializeWithString() throws IOException {
        String value = "abc";
        
        serializer.serialize(value, jsonGenerator, serializerProvider);
        jsonGenerator.flush();

        String expected = "abc";
        String actual = writer.toString();

        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testGetSchemaWithInteger() {
        JsonNode schema = serializer.getSchema(serializerProvider, Integer.class);

        Assert.assertEquals("integer", schema.get("type").asText());
        Assert.assertTrue(schema.get("required").asBoolean());
    }

    @Test
    public void testGetSchemaWithBigInteger() {
        JsonNode schema = serializer.getSchema(serializerProvider, BigInteger.class);

        Assert.assertEquals("integer", schema.get("type").asText());
        Assert.assertTrue(schema.get("required").asBoolean());
    }

    @Test
    public void testGetSchemaWithBigDecimal() {
        JsonNode schema = serializer.getSchema(serializerProvider, BigDecimal.class);

        Assert.assertEquals("number", schema.get("type").asText());
        Assert.assertTrue(schema.get("required").asBoolean());
    }

    @Test
    public void testAcceptJsonFormatVisitorWithInteger() {
        JsonFormatVisitorWrapper visitor = new JsonFormatVisitorWrapper.Base(null);
        serializer.acceptJsonFormatVisitor(visitor, serializerProvider.constructType(Integer.class));

        // Assert some conditions
    }

    @Test
    public void testAcceptJsonFormatVisitorWithBigInteger() {
        JsonFormatVisitorWrapper visitor = new JsonFormatVisitorWrapper.Base(null);
        serializer.acceptJsonFormatVisitor(visitor, serializerProvider.constructType(BigInteger.class));

        // Assert some conditions
    }

    @Test
    public void testAcceptJsonFormatVisitorWithBigDecimal() {
        JsonFormatVisitorWrapper visitor = new JsonFormatVisitorWrapper.Base(null);
        serializer.acceptJsonFormatVisitor(visitor, serializerProvider.constructType(BigDecimal.class));

        // Assert some conditions
    }

}
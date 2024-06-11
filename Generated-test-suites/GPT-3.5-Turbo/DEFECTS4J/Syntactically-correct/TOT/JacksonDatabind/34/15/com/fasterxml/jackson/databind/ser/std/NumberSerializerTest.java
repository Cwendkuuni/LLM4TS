
package com.fasterxml.jackson.databind.ser.std;

import static org.junit.Assert.*;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import org.junit.Test;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonFormatVisitorWrapper;

public class NumberSerializerTest {

    private ObjectMapper mapper;
    private JsonFactory jsonFactory;

    @Before
    public void setUp() {
        mapper = new ObjectMapper();
        jsonFactory = new JsonFactory();
    }

    @Test
    public void testSerializeBigDecimal() throws IOException {
        NumberSerializer serializer = new NumberSerializer(BigDecimal.class);
        JsonGenerator generator = jsonFactory.createGenerator(System.out);
        SerializerProvider provider = mapper.getSerializerProvider();
        BigDecimal input = new BigDecimal("12345.6789");
        serializer.serialize(input, generator, provider);
        generator.flush();
        String expected = "12345.6789";
        assertEquals(expected, generator.toString());
    }

    @Test
    public void testSerializeBigInteger() throws IOException {
        NumberSerializer serializer = new NumberSerializer(BigInteger.class);
        JsonGenerator generator = jsonFactory.createGenerator(System.out);
        SerializerProvider provider = mapper.getSerializerProvider();
        BigInteger input = new BigInteger("12345");
        serializer.serialize(input, generator, provider);
        generator.flush();
        String expected = "12345";
        assertEquals(expected, generator.toString());
    }

    @Test
    public void testSerializeInteger() throws IOException {
        NumberSerializer serializer = new NumberSerializer(Integer.class);
        JsonGenerator generator = jsonFactory.createGenerator(System.out);
        SerializerProvider provider = mapper.getSerializerProvider();
        Integer input = 123;
        serializer.serialize(input, generator, provider);
        generator.flush();
        String expected = "123";
        assertEquals(expected, generator.toString());
    }

    @Test
    public void testSerializeLong() throws IOException {
        NumberSerializer serializer = new NumberSerializer(Long.class);
        JsonGenerator generator = jsonFactory.createGenerator(System.out);
        SerializerProvider provider = mapper.getSerializerProvider();
        Long input = 123456789L;
        serializer.serialize(input, generator, provider);
        generator.flush();
        String expected = "123456789";
        assertEquals(expected, generator.toString());
    }

    @Test
    public void testSerializeDouble() throws IOException {
        NumberSerializer serializer = new NumberSerializer(Double.class);
        JsonGenerator generator = jsonFactory.createGenerator(System.out);
        SerializerProvider provider = mapper.getSerializerProvider();
        Double input = 123.456;
        serializer.serialize(input, generator, provider);
        generator.flush();
        String expected = "123.456";
        assertEquals(expected, generator.toString());
    }

    @Test
    public void testSerializeFloat() throws IOException {
        NumberSerializer serializer = new NumberSerializer(Float.class);
        JsonGenerator generator = jsonFactory.createGenerator(System.out);
        SerializerProvider provider = mapper.getSerializerProvider();
        Float input = 123.456f;
        serializer.serialize(input, generator, provider);
        generator.flush();
        String expected = "123.456";
        assertEquals(expected, generator.toString());
    }

    @Test
    public void testSerializeByte() throws IOException {
        NumberSerializer serializer = new NumberSerializer(Byte.class);
        JsonGenerator generator = jsonFactory.createGenerator(System.out);
        SerializerProvider provider = mapper.getSerializerProvider();
        Byte input = 12;
        serializer.serialize(input, generator, provider);
        generator.flush();
        String expected = "12";
        assertEquals(expected, generator.toString());
    }

    @Test
    public void testSerializeShort() throws IOException {
        NumberSerializer serializer = new NumberSerializer(Short.class);
        JsonGenerator generator = jsonFactory.createGenerator(System.out);
        SerializerProvider provider = mapper.getSerializerProvider();
        Short input = 123;
        serializer.serialize(input, generator, provider);
        generator.flush();
        String expected = "123";
        assertEquals(expected, generator.toString());
    }

    @Test
    public void testSerializeFallback() throws IOException {
        NumberSerializer serializer = new NumberSerializer(Number.class);
        JsonGenerator generator = jsonFactory.createGenerator(System.out);
        SerializerProvider provider = mapper.getSerializerProvider();
        Number input = 12345.67;
        serializer.serialize(input, generator, provider);
        generator.flush();
        String expected = "12345.67";
        assertEquals(expected, generator.toString());
    }

    @Test
    public void testGetSchemaWithBigInteger() {
        NumberSerializer serializer = new NumberSerializer(BigInteger.class);
        SerializerProvider provider = mapper.getSerializerProvider();
        Type type = BigInteger.class;
        JsonNode schema = serializer.getSchema(provider, type);
        assertTrue(schema.isObject());
        assertTrue(schema.get("type").asText().equals("integer"));
        assertTrue(schema.get("required").asBoolean());
    }

    @Test
    public void testGetSchemaWithBigDecimal() {
        NumberSerializer serializer = new NumberSerializer(BigDecimal.class);
        SerializerProvider provider = mapper.getSerializerProvider();
        Type type = BigDecimal.class;
        JsonNode schema = serializer.getSchema(provider, type);
        assertTrue(schema.isObject());
        assertTrue(schema.get("type").asText().equals("number"));
        assertTrue(schema.get("required").asBoolean());
    }

    @Test
    public void testAcceptJsonFormatVisitorWithBigInteger() throws JsonMappingException {
        NumberSerializer serializer = new NumberSerializer(BigInteger.class);
        JsonFormatVisitorWrapper visitor = new JsonFormatVisitorWrapper.Base();
        JavaType typeHint = mapper.constructType(BigInteger.class);
        serializer.acceptJsonFormatVisitor(visitor, typeHint);
        // Expecting visitIntFormat method to be called
        assertTrue(visitor.getProvidedType().equals(JsonParser.NumberType.BIG_INTEGER));
    }

    @Test
    public void testAcceptJsonFormatVisitorWithBigDecimal() throws JsonMappingException {
        NumberSerializer serializer = new NumberSerializer(BigDecimal.class);
        JsonFormatVisitorWrapper visitor = new JsonFormatVisitorWrapper.Base();
        JavaType typeHint = mapper.constructType(BigDecimal.class);
        serializer.acceptJsonFormatVisitor(visitor, typeHint);
        // Expecting visitFloatFormat method to be called
        assertTrue(visitor.getProvidedType().equals(JsonParser.NumberType.BIG_INTEGER));
    }

    @Test(expected = JsonMappingException.class)
    public void testAcceptJsonFormatVisitorWithOtherTypes() throws JsonMappingException {
        NumberSerializer serializer = new NumberSerializer(Number.class);
        JsonFormatVisitorWrapper visitor = new JsonFormatVisitorWrapper.Base();
        JavaType typeHint = mapper.constructType(Number.class);
        serializer.acceptJsonFormatVisitor(visitor, typeHint);
        // Expecting JsonMappingException to be thrown
    }
}

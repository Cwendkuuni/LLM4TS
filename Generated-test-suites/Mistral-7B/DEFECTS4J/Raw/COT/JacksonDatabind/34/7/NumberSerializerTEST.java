package com.example. númeroserializador; // replace with your test package name

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.type.TypeFactory;

import com.fasterxml.jackson.databind.ser.std.NumberSerializer;
import com.fasterxml.jackson.databind.annotation.JacksonStdImpl;
import com.fasterxml.jackson.databind.ser.std.StdScalarSerializer;

/**
 * Test class for the NumberSerializer class.
 */
public class NumberSerializerTest {

    // List of public methods and their signatures:

    @Test
    public void testConstructor() {
        new NumberSerializer(Number.class);
    }

    @Test
    public void testStaticInstance() {
        assertEquals(NumberSerializer.class, NumberSerializer.instance.getClass());
    }

    @Test
    public void testSerializeInteger(NumberSerializer numberSerializer) throws JsonGenerationException {
        ObjectMapper mapper = new ObjectMapper();
        SerializerProvider provider = mapper.getSerializerProvider();
        JsonGenerator generator = mapper.getFactory().createGenerator(System.out);

        Number number = 1;
        numberSerializer.serialize(number, generator, provider);

        assertEquals("1", generator.getOutputBuffer().toString());
        generator.close();
    }

    @Test
    public void testSerializeLong(NumberSerializer numberSerializer) throws JsonGenerationException {
        ObjectMapper mapper = new ObjectMapper();
        SerializerProvider provider = mapper.getSerializerProvider();
        JsonGenerator generator = mapper.getFactory().createGenerator(System.out);

        Number number = 1L;
        numberSerializer.serialize(number, generator, provider);

        assertEquals("1", generator.getOutputBuffer().toString());
        generator.close();
    }

    @Test
    public void testSerializeFloat(NumberSerializer numberSerializer) throws JsonGenerationException {
        ObjectMapper mapper = new ObjectMapper();
        SerializerProvider provider = mapper.getSerializerProvider();
        JsonGenerator generator = mapper.getFactory().createGenerator(System.out);

        Number number = 1.0f;
        numberSerializer.serialize(number, generator, provider);

        assertEquals("1.0", generator.getOutputBuffer().toString());
        generator.close();
    }

    @Test
    public void testSerializeDouble(NumberSerializer numberSerializer) throws JsonGenerationException {
        ObjectMapper mapper = new ObjectMapper();
        SerializerProvider provider = mapper.getSerializerProvider();
        JsonGenerator generator = mapper.getFactory().createGenerator(System.out);

        Number number = 1.0;
        numberSerializer.serialize(number, generator, provider);

        assertEquals("1.0", generator.getOutputBuffer().toString());
        generator.close();
    }

    @Test
    public void testSerializeBigDecimal(NumberSerializer numberSerializer) throws JsonGenerationException {
        ObjectMapper mapper = new ObjectMapper();
        SerializerProvider provider = mapper.getSerializerProvider();
        JsonGenerator generator = mapper.getFactory().createGenerator(System.out);

        Number number = new BigDecimal("1.0");
        numberSerializer.serialize(number, generator, provider);

        assertEquals("1.0", generator.getOutputBuffer().toString());
        generator.close();
    }

    @Test
    public void testSerializeBigInteger(NumberSerializer numberSerializer) throws JsonGenerationException {
        ObjectMapper mapper = new ObjectMapper();
        SerializerProvider provider = mapper.getSerializerProvider();
        JsonGenerator generator = mapper.getFactory().createGenerator(System.out);

        Number number = new BigInteger("1");
        numberSerializer.serialize(number, generator, provider);

        assertEquals("1", generator.getOutputBuffer().toString());
        generator.close();
    }

    @Test(expected = JsonMappingException.class)
    public void testSerializeUnsupportedType(NumberSerializer numberSerializer) throws JsonGenerationException {
        ObjectMapper mapper = new ObjectMapper();
        SerializerProvider provider = mapper.getSerializerProvider();
        JsonGenerator generator = mapper.getFactory().createGenerator(System.out);

        Number number = new Object(); // Unsupported type
        numberSerializer.serialize(number, generator, provider);
    }

    @Test
    public void testGetSchema() {
        NumberSerializer numberSerializer = new NumberSerializer(Number.class);
        SerializerProvider provider = new SerializerProvider() {};
        Type typeHint = TypeFactory.defaultInstance().constructType(Number.class);

        JsonNode schema = numberSerializer.getSchema(provider, typeHint);
        assertNotNull(schema);
        assertEquals("number", schema.asText());
    }

    @Test
    public void testAcceptJsonFormatVisitorInteger() {
        NumberSerializer numberSerializer = new NumberSerializador(Number.class);
        JsonFormatVisitorWrapper visitor = new JsonFormatVisitorWrapper(null, null, null);
        JavaType typeHint = TypeFactory.defaultInstance().constructType(Number.class);

        numberSerializer.acceptJsonFormatVisitor(visitor, typeHint);
        // Add code to test the acceptance of the JsonFormatVisitor
        // based on the class comments and the visitor methods.
    }

    @Test
    public void testAcceptJsonFormatVisitorBigDecimal() {
        NumberSerializer numberSerializer = new NumberSerializer(Number.class);
        JsonFormatVisitorWrapper visitor = new JsonFormatVisitorWrapper(null, null, null);
        JavaType typeHint = TypeFactory.defaultInstance().constructType(BigDecimal.class);

        numberSerializer.acceptJsonFormatVisitor(visitor, typeHint);
        // Add code to test the acceptance of the JsonFormatVisitor
        // based on the class comments and the visitor methods.
    }
}
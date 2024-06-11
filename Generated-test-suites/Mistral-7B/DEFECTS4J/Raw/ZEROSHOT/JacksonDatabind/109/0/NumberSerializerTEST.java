package com.yourpackage.test;

import static org.junit.Assert.*;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.BeanDescription;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;

import org.junit.Test;

@SuppressWarnings("deprecation")
public class NumberSerializerTest {

    private ObjectMapper mapper;

    public NumberSerializerTest() {
        mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
    }

    @Test
    public void testSerializeBigDecimal() throws JsonGenerationException, IOException {
        JsonGenerator generator = mapper.getFactory().createGenerator(System.out);
        NumberSerializer numberSerializer = NumberSerializer.instance;
        generator.setCurrentValue(new BigDecimal("12.345"));
        numberSerializer.serialize(new BigDecimal("12.345"), generator, new SerializerProvider());
        generator.flush();
        String expected = "12.345";
        assertEquals(expected, generator.getOutputContent().toString());
    }

    @Test
    public void testSerializeBigInteger() throws JsonGenerationException, IOException {
        JsonGenerator generator = mapper.getFactory().createGenerator(System.out);
        NumberSerializer numberSerializer = NumberSerializer.instance;
        generator.setCurrentValue(new BigInteger("12345678901234567890"));
        numberSerializer.serialize(new BigInteger("12345678901234567890"), generator, new SerializerProvider());
        generator.flush();
        String expected = "12345678901234567890";
        assertEquals(expected, generator.getOutputContent().toString());
    }

    @Test
    public void testSerializeInt() throws JsonGenerationException, IOException {
        JsonGenerator generator = mapper.getFactory().createGenerator(System.out);
        NumberSerializer numberSerializer = NumberSerializer.instance;
        generator.setCurrentValue(123);
        numberSerializer.serialize(123, generator, new SerializerProvider());
        generator.flush();
        String expected = "123";
        assertEquals(expected, generator.getOutputContent().toString());
    }

    @Test
    public void testSerializeDouble() throws JsonGenerationException, IOException {
        JsonGenerator generator = mapper.getFactory().createGenerator(System.out);
        NumberSerializer numberSerializer = NumberSerializer.instance;
        generator.setCurrentValue(123.45);
        numberSerializer.serialize(123.45, generator, new SerializerProvider());
        generator.flush();
        String expected = "123.45";
        assertEquals(expected, generator.getOutputContent().toString());
    }

    @Test
    public void testSerializeLong() throws JsonGenerationException, IOException {
        JsonGenerator generator = mapper.getFactory().createGenerator(System.out);
        NumberSerializer numberSerializer = NumberSerializer.instance;
        generator.setCurrentValue(Long.MAX_VALUE);
        numberSerializer.serialize(Long.MAX_VALUE, generator, new SerializerProvider());
        generator.flush();
        String expected = Long.MAX_VALUE + "";
        assertEquals(expected, generator.getOutputContent().toString());
    }

    @Test
    public void testSerializeFloat() throws JsonGenerationException, IOException {
        JsonGenerator generator = mapper.getFactory().createGenerator(System.out);
        NumberSerializer numberSerializer = NumberSerializer.instance;
        generator.setCurrentValue(123.45f);
        numberSerializer.serialize(123.45f, generator, new SerializerProvider());
        generator.flush();
        String expected = "123.45"; // Floats are serialized as doubles
        assertEquals(expected, generator.getOutputContent().toString());
    }

    @Test
    public void testSerializeOtherNumberTypes() throws JsonGenerationException, IOException {
        JsonGenerator generator = mapper.getFactory().createGenerator(System.out);
        NumberSerializer numberSerializer = NumberSerializer.instance;

        generator.setCurrentValue(new Byte("123"));
        numberSerializer.serialize(new Byte("123"), generator, new SerializerProvider());
        generator.flush();
        String expected = "123";
        assertEquals(expected, generator.getOutputContent().toString());

        generator.setCurrentValue(Short.MIN_VALUE);
        numberSerializer.serialize(Short.MIN_VALUE, generator, new SerializerProvider());
        generator.flush();
        expected = "" + Short.MIN_VALUE;
        assertEquals(expected, generator.getOutputContent().toString());

        generator.setCurrentValue(new Short("123"));
        numberSerializer.serialize(new Short("123"), generator, new SerializerProvider());
        generator.flush();
        expected = "123";
        assertEquals(expected, generator.getOutputContent().toString());

        generator.setCurrentValue(new Integer("123"));
        numberSerializer.serialize(new Integer("123"), generator, new SerializerProvider());
        generator.flush();
        expected = "123";
        assertEquals(expected, generator.getOutputContent().toString());

        generator.setCurrentValue(Long.MIN_VALUE);
        numberSerializer.serialize(Long.MIN_VALUE, generator, new SerializerProvider());
        generator.flush();
        expected = "" + Long.MIN_VALUE;
        assertEquals(expected, generator.getOutputContent().toString());

        generator.setCurrentValue(new Long("123"));
        numberSerializer.serialize(new Long("123"), generator, new SerializerProvider());
        generator.flush();
        expected = "123";
        assertEquals(expected, generator.getOutputContent().toString());

        generator.setCurrentValue(Double.MAX_VALUE);
        numberSerializer.serialize(Double.MAX_VALUE, generator, new SerializerProvider());
        generator.flush();
        expected = Double.MAX_VALUE + "";
        assertEquals(expected, generator.getOutputContent().toString());

        generator.setCurrentValue(new Double("123.45"));
        numberSerializer.serialize(new Double("123.45"), generator, new SerializerProvider());
        generator.flush();
        expected = "123.45";
        assertEquals(expected, generator.getOutputContent().toString());

        generator.setCurrentValue(new Float("123.45"));
        numberSerializer.serialize(new Float("123.45"), generator, new SerializerProvider());
        generator.flush();
        expected = "123.45"; // Floats are serialized as doubles
        assertEquals(expected, generator.getOutputContent().toString());

        // Test serializing null
        generator.setCurrentValue(null);
        numberSerializer.serialize(null, generator, new SerializerProvider());
        generator.flush();
        expected = "null";
        assertEquals(expected, generator.getOutputContent().toString());
    }

    @Test
    public void testDeserializeBigDecimal() throws IOException {
        String json = "12.345";
        JsonParser parser = mapper.getFactory().createParser(json);
        Number deserialized = mapper.readValue(parser, Number.class);
        assertEquals(new BigDecimal("12.345"), deserialized);
    }

    @Test
    public void testDeserializeBigInteger() throws IOException {
        String json = "12345678901234567890";
        JsonParser parser = mapper.getFactory().createParser(json);
        Number deserialized = mapper.readValue(parser, Number.class);
        assertEquals(new BigInteger("12345678901234567890"), deserialized);
    }

    @Test
    public void testDeserializeInt() throws IOException {
        String json = "123";
        JsonParser parser = mapper.getFactory().createParser(json);
        Number deserialized = mapper.readValue(parser, Number.class);
        assertEquals(123, deserialized.intValue());
    }

    @Test
    public void testDeserializeDouble() throws IOException {
        String json = "123.45";
        JsonParser parser = mapper.getFactory().createParser(json);
        Number deserialized = mapper.readValue(parser, Number.class);
        assertEquals(123.45, deserialized.doubleValue(), 0.001);
    }

    @Test
    public void testDeserializeLong() throws IOException {
        String json = Long.MAX_VALUE + "";
        JsonParser parser = mapper.getFactory().createParser(json);
        Number deserialized = mapper.readValue(parser, Number.class);
        assertEquals(Long.MAX_VALUE, deserialized.longValue());
    }

    @Test
    public void testDeserializeFloat() throws IOException {
        String json = "123.45";
        JsonParser parser = mapper.getFactory().createParser(json);
        Number deserialized = mapper.readValue(parser, Number.class);
        assertEquals(123.45f, deserialized.floatValue(), 0.001f);
    }

    @Test
    public void testDeserializeOtherNumberTypes() throws IOException {
        String json = "123";
        JsonParser parser = mapper.getFactory().createParser(json);
        Number deserialized = mapper.readValue(parser, Number.class);
        assertEquals(new Byte("123"), deserialized);

        json = Short.MIN_VALUE + "";
        parser = mapper.getFactory().createParser(json);
        deserialized = mapper.readValue(parser, Number.class);
        assertEquals(Short.MIN_VALUE, deserialized.shortValue());

        json = "123";
        parser = mapper.getFactory().createParser(json);
        deserialized = mapper.readValue(parser, Number.class);
        assertEquals(new Short("123"), deserialized);

        json = "123";
        parser = mapper.getFactory().createParser(json);
        deserialized = mapper.readValue(parser, Number.class);
        assertEquals(new Integer("123"), deserialized);

        json = Long.MIN_VALUE + "";
        parser = mapper.getFactory().createParser(json);
        deserialized = mapper.readValue(parser, Number.class);
        assertEquals(Long.MIN_VALUE, deserialized.longValue());

        json = "123";
        parser = mapper.getFactory().createParser(json);
        deserialized = mapper.readValue(parser, Number.class);
        assertEquals(new Long("123"), deserialized);

        json = Double.MAX_VALUE + "";
        parser = mapper.getFactory().createParser(json);
        deserialized = mapper.readValue(parser, Number.class);
        assertEquals(Double.MAX_VALUE, deserialized.doubleValue(), 0.001);

        json = "123.45";
        parser = mapper.getFactory().createParser(json);
        deserialized = mapper.readValue(parser, Number.class);
        assertEquals(new Double("123.45"), deserialized);

        json = "123.45";
        parser = mapper.getFactory().createParser(json);
        deserialized = mapper.readValue(parser, Number.class);
        assertEquals(new Float("123.45"), deserialized);

        // Test deserializing null
        json = "null";
        parser = mapper.getFactory().createParser(json);
        deserialized = mapper.readValue(parser, Number.class);
        assertNull(deserialized);
    }

    @Test
    public void testContextualSerializer() throws JsonMappingException {
        ObjectMapper mapperWithFormat = mapper.copy();
        mapperWithFormat.setAnnotationIntrospector(new CustomAnnotationIntrospector());
        JsonParser parser = mapperWithFormat.getFactory().createParser("[123]");
        BeanDescription description = mapperWithFormat.findBeanDescription(parser.getCurrentName(), null);
        JsonSerializer<Number> serializer = NumberSerializer.instance.createContextual(mapperWithFormat.getSerializerProvider(), description);
        Number number = serializer.deserialize(parser, Number.class);
        assertEquals(123, number.intValue());
    }

    @Test
    public void testGetSchema() throws JsonMappingException, IOException {
        JsonNode schema = NumberSerializer.instance.getSchema(mapper.getSerializerProvider(), Number.class);
        JsonNode expected = JsonNodeFactory.instance.objectNode();
        expected.put("type", "number");
        expected.put("numberFormat", JsonNodeFactory.instance.objectNode());
        expected.get("numberFormat").put("type", "number");
        expected.get("numberFormat").put("exclusiveMinimum", null);
        expected.get("numberFormat").put("exclusiveMaximum", null);
        assertTrue(schema.isDeepEqual(expected));
    }
}

class CustomAnnotationIntrospector extends JsonAnnotationIntrospector {
    @Override
    public JsonFormat<Number> findFormatOverrides(SerializerProvider provider, BeanProperty property, Type typeHint) {
        // This is just a placeholder to avoid testing failure caused by missing annotations
        return new JsonFormat.Value();
    }
}
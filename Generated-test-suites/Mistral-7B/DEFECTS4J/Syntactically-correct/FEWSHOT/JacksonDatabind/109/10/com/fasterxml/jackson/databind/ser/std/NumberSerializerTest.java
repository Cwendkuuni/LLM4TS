package com.fasterxml.jackson.databind.ser.std;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.json.JsonMapperBuilder;
import com.fasterxml.jackson.databind.node.NumericNode;
import com.fasterxml.jackson.databind.ser.DefaultSerializerProvider;
import com.fasterxml.jackson.databind.ser.SerializerProvider;
import com.fasterxml.jackson.databind.type.TypeFactory;

import java.io.IOException;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.math.BigInteger;

@SuppressWarnings("unchecked")
public class NumberSerializerTest {

    @Test
    public void testSerializeNumber() throws IOException {
        ObjectMapper mapper = JsonMapperBuilder.jsonMapper()
                .enable(SerializationFeature.WRITE_NUMBERS_AS_STRINGS)
                .build();

        SerializerProvider provider = new DefaultSerializerProvider();
        JsonGenerator gen = mapper.getFactory().createGenerator(System.out);
        provider.setGenerator(NumberSerializer.class, gen);

        NumberSerializer serializer = new NumberSerializer(Number.class);

        // Test serialization of different Number types
        serializer.serialize(BigDecimal.ONE, provider, null);
        serializer.serialize(BigInteger.ONE, provider, null);
        serializer.serialize(3, provider, null);
        serializer.serialize(3.14, provider, null);
        serializer.serialize(-123, provider, null);
        serializer.serialize(Float.NaN, provider, null);

        gen.flush();
    }

    @Test
    public void testSerializeBigDecimal() throws IOException {
        ObjectMapper mapper = JsonMapperBuilder.jsonMapper()
                .enable(SerializationFeature.WRITE_NUMBERS_AS_STRINGS)
                .build();

        SerializerProvider provider = new DefaultSerializerProvider();
        JsonGenerator gen = mapper.getFactory().createGenerator(System.out);
        provider.setGenerator(NumberSerializer.class, gen);

        NumberSerializer serializer = new NumberSerializer(Number.class);

        BigDecimal number = BigDecimal.valueOf(12345678.987654321);
        serializer.serialize(number, provider, null);

        gen.flush();
    }

    @Test
    public void testSerializeBigInteger() throws IOException {
        ObjectMapper mapper = JsonMapperBuilder.jsonMapper()
                .enable(SerializationFeature.WRITE_NUMBERS_AS_STRINGS)
                .build();

        SerializerProvider provider = new DefaultSerializerProvider();
        JsonGenerator gen = mapper.getFactory().createGenerator(System.out);
        provider.setGenerator(NumberSerializer.class, gen);

        NumberSerializer serializer = new NumberSerializer(Number.class);

        BigInteger number = BigInteger.valueOf(12345678987654321L);
        serializer.serialize(number, provider, null);

        gen.flush();
    }

    @Test
    public void testCreateContextualSerializer() {
        JsonMapper mapper = new JsonMapper();

        SerializerProvider provider = mapper.getSerializerProvider();
        BeanProperty property = new BeanProperty("testProperty", Number.class);

        JsonFormat format = new JsonFormat();
        format.setShape(JsonFormat.Shape.STRING);

        Object ctx = NumberSerializer.createContextual(provider, property);
        assertEquals(ToStringSerializer.instance, ctx);

        // Test with no format override
        JsonFormat noFormat = new JsonFormat();
        Object noCtx = NumberSerializer.createContextual(provider, property);
        assertEquals(NumberSerializer.instance, noCtx);
    }

    @Test
    public void testAcceptJsonFormatVisitor() throws JsonMappingException {
        JsonMapper mapper = new JsonMapper();
        JsonFormatVisitorWrapper visitor = mapper.getAnnotationIntrospector()
                .findJsonFormat(NumberSerializer.class, null);

        Type typeHint = TypeFactory.defaultInstance().constructType(Number.class);

        // Test for int format
        if (visitor != null && visitor.expectIntFormat(typeHint) != null) {
            // Int format test goes here
        }

        // Test for BigDecimal format
        if (visitor != null && visitor.expectNumberFormat(typeHint) != null) {
            JsonNode node = visitor.expectNumberFormat(typeHint).acceptNumberFormat(null, null);
            assertEquals(JsonParser.NumberType.BIG_DECIMAL, node.getType());
        }
    }

    @Test
    public void testDeserializeNumber() throws IOException {
        ObjectMapper mapper = JsonMapperBuilder.jsonMapper()
                .enable(SerializationFeature.FIND_PROPERTIES_ON_THE_SEARCH_PATH)
                .build();

        DeserializationContext context = mapper.getDeserializationContext();
        JsonParser parser = mapper.getFactory().createParser("123");
        context.setCurrentLocation(parser.getCurrentLocation());

        NumberSerializer deserializer = new NumberSerializer(Number.class);
        Number result = deserializer.deserialize(parser, null);

        assertEquals(123, result.intValue());
    }

    @Test
    public void testDeserializeBigDecimal() throws IOException {
        ObjectMapper mapper = JsonMapperBuilder.jsonMapper()
                .enable(SerializationFeature.FIND_PROPERTIES_ON_THE_SEARCH_PATH)
                .build();

        DeserializationContext context = mapper.getDeserializationContext();
        JsonParser parser = mapper.getFactory().createParser("12345678.987654321");
        context.setCurrentLocation(parser.getCurrentLocation());

        NumberSerializer deserializer = new NumberSerializer(Number.class);
        Number result = deserializer.deserialize(parser, null);

        assertEquals(12345678.987654321, ((BigDecimal) result).doubleValue(), 0.00001);
    }

    @Test
    public void testDeserializeBigInteger() throws IOException {
        ObjectMapper mapper = JsonMapperBuilder.jsonMapper()
                .enable(SerializationFeature.FIND_PROPERTIES_ON_THE_SEARCH_PATH)
                .build();

        DeserializationContext context = mapper.getDeserializationContext();
        JsonParser parser = mapper.getFactory().createParser("12345678987654321");
        context.setCurrentLocation(parser.getCurrentLocation());

        NumberSerializer deserializer = new NumberSerializer(Number.class);
        Number result = deserializer.deserialize(parser, null);

        BigInteger bigInt = (BigInteger) result;
        assertEquals(12345678987654321L, bigInt.longValueExact());
    }
}
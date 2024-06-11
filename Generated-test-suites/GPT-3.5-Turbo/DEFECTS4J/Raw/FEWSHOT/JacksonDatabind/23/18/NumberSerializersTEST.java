package com.fasterxml.jackson.databind.ser.std;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonFormatVisitorWrapper;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonIntegerFormatVisitor;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonNumberFormatVisitor;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;

public class NumberSerializersTest {
    private Map<String, JsonSerializer<?>> allDeserializers;

    @Before
    public void setUp() {
        allDeserializers = new HashMap<>();
        NumberSerializers.addAll(allDeserializers);
    }

    @Test
    public void testAddAll() {
        assertEquals(12, allDeserializers.size());
    }

    @Test
    public void testShortSerializer() throws IOException {
        ShortSerializer s = new ShortSerializer();
        assertEquals(Short.class, s._handledType);
        assertEquals(JsonParser.NumberType.INT, s._numberType);
        assertEquals("number", s._schemaType);

        JsonGenerator gen = new JsonFactory().createGenerator(System.out);
        SerializerProvider provider = new ObjectMapper().getSerializerProvider();
        s.serialize((short) 123, gen, provider);

        // test getSchema
        Type typeHint = Short.TYPE;
        JsonNode schema = s.getSchema(provider, typeHint);
        assertEquals("number", schema.get("type").asText());

        // test acceptJsonFormatVisitor
        JsonFormatVisitorWrapper visitor = new ObjectMapper().getSerializationConfig()
                .getDefaultPrettyPrinter();
        JavaType type = provider.constructType(Short.TYPE);
        s.acceptJsonFormatVisitor(visitor, type);
        JsonIntegerFormatVisitor v2 = visitor.expectIntegerFormat(type);
        assertEquals(JsonParser.NumberType.INT, v2.getProvider().numberType());
    }

    @Test
    public void testIntegerSerializer() throws IOException {
        IntegerSerializer i = new IntegerSerializer();
        assertEquals(Integer.class, i._handledType);
        assertEquals(JsonParser.NumberType.INT, i._numberType);
        assertEquals("integer", i._schemaType);

        JsonGenerator gen = new JsonFactory().createGenerator(System.out);
        SerializerProvider provider = new ObjectMapper().getSerializerProvider();
        i.serialize(123, gen, provider);

        // test getSchema
        Type typeHint = Integer.TYPE;
        JsonNode schema = i.getSchema(provider, typeHint);
        assertEquals("integer", schema.get("type").asText());

        // test acceptJsonFormatVisitor
        JsonFormatVisitorWrapper visitor = new ObjectMapper().getSerializationConfig()
                .getDefaultPrettyPrinter();
        JavaType type = provider.constructType(Integer.TYPE);
        i.acceptJsonFormatVisitor(visitor, type);
        JsonIntegerFormatVisitor v2 = visitor.expectIntegerFormat(type);
        assertEquals(JsonParser.NumberType.INT, v2.getProvider().numberType());
    }

    @Test
    public void testIntLikeSerializer() throws IOException {
        IntLikeSerializer l = new IntLikeSerializer();
        assertEquals(Number.class, l._handledType);
        assertEquals(JsonParser.NumberType.INT, l._numberType);
        assertEquals("integer", l._schemaType);

        JsonGenerator gen = new JsonFactory().createGenerator(System.out);
        SerializerProvider provider = new ObjectMapper().getSerializerProvider();
        l.serialize(123, gen, provider);

        // test getSchema
        Type typeHint = Number.class;
        JsonNode schema = l.getSchema(provider, typeHint);
        assertEquals("integer", schema.get("type").asText());

        // test acceptJsonFormatVisitor
        JsonFormatVisitorWrapper visitor = new ObjectMapper().getSerializationConfig()
                .getDefaultPrettyPrinter();
        JavaType type = provider.constructType(Byte.TYPE);
        l.acceptJsonFormatVisitor(visitor, type);
        JsonIntegerFormatVisitor v2 = visitor.expectIntegerFormat(type);
        assertEquals(JsonParser.NumberType.INT, v2.getProvider().numberType());
    }

    @Test
    public void testLongSerializer() throws IOException {
        LongSerializer l = new LongSerializer();
        assertEquals(Long.class, l._handledType);
        assertEquals(JsonParser.NumberType.LONG, l._numberType);
        assertEquals("number", l._schemaType);

        JsonGenerator gen = new JsonFactory().createGenerator(System.out);
        SerializerProvider provider = new ObjectMapper().getSerializerProvider();
        l.serialize(123L, gen, provider);

        // test getSchema
        Type typeHint = Long.TYPE;
        JsonNode schema = l.getSchema(provider, typeHint);
        assertEquals("number", schema.get("type").asText());

        // test acceptJsonFormatVisitor
        JsonFormatVisitorWrapper visitor = new ObjectMapper().getSerializationConfig()
                .getDefaultPrettyPrinter();
        JavaType type = provider.constructType(Short.TYPE);
        l.acceptJsonFormatVisitor(visitor, type);
        JsonIntegerFormatVisitor v2 = visitor.expectIntegerFormat(type);
        assertEquals(JsonParser.NumberType.LONG, v2.getProvider().numberType());
    }

    @Test
    public void testFloatSerializer() throws IOException {
        FloatSerializer f = new FloatSerializer();
        assertEquals(Float.class, f._handledType);
        assertEquals(JsonParser.NumberType.FLOAT, f._numberType);
        assertEquals("number", f._schemaType);

        JsonGenerator gen = new JsonFactory().createGenerator(System.out);
        SerializerProvider provider = new ObjectMapper().getSerializerProvider();
        f.serialize(1.23f, gen, provider);

        // test getSchema
        Type typeHint = Float.TYPE;
        JsonNode schema = f.getSchema(provider, typeHint);
        assertEquals("number", schema.get("type").asText());

        // test acceptJsonFormatVisitor
        JsonFormatVisitorWrapper visitor = new ObjectMapper().getSerializationConfig()
                .getDefaultPrettyPrinter();
        JavaType type = provider.constructType(Float.TYPE);
        f.acceptJsonFormatVisitor(visitor, type);
        JsonNumberFormatVisitor v2 = visitor.expectNumberFormat(type);
        assertEquals(JsonParser.NumberType.FLOAT, v2.getProvider().numberType());
    }

    @Test
    public void testDoubleSerializer() throws IOException {
        DoubleSerializer d = new DoubleSerializer();
        assertEquals(Double.class, d._handledType);
        assertEquals(JsonParser.NumberType.DOUBLE, d._numberType);
        assertEquals("number", d._schemaType);

        JsonGenerator gen = new JsonFactory().createGenerator(System.out);
        SerializerProvider provider = new ObjectMapper().getSerializerProvider();
        d.serialize(1.23, gen, provider);

        // test getSchema
        Type typeHint = Double.TYPE;
        JsonNode schema = d.getSchema(provider, typeHint);
        assertEquals("number", schema.get("type").asText());

        // test acceptJsonFormatVisitor
        JsonFormatVisitorWrapper visitor = new ObjectMapper().getSerializationConfig()
                .getDefaultPrettyPrinter();
        JavaType type = provider.constructType(Double.TYPE);
        d.acceptJsonFormatVisitor(visitor, type);
        JsonNumberFormatVisitor v2 = visitor.expectNumberFormat(type);
        assertEquals(JsonParser.NumberType.DOUBLE, v2.getProvider().numberType());
    }
}
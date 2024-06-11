package com.fasterxml.jackson.databind.ser.std;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.ser.std.StdDelegatingSerializer;
import org.junit.Test;
import org.junit.Assert.*;

import java.io.IOException;
import java.lang.reflect.Type;

public class StdDelegatingSerializerTest {

    @Test
    public void testConvertValue() {
        Converter<Object, String> converter = new StringConverter();
        JavaType delegateType = JavaType.construct(String.class);
        StdDelegatingSerializer serializer = new StdDelegatingSerializer(converter, delegateType, null);
        Object value = new Object();
        Object delegateValue = serializer.convertValue(value);
        Assert.assertNull(delegateValue);

        value = "test";
        delegateValue = serializer.convertValue(value);
        Assert.assertEquals("test", delegateValue);
    }

    @Test
    public void testSerializeNull() throws JsonGenerationException, IOException {
        Converter<Object, String> converter = new StringConverter();
        JavaType delegateType = JavaType.construct(String.class);
        StdDelegatingSerializer serializer = new StdDelegatingSerializer(converter, delegateType, null);
        JsonGenerator gen = new JsonFactory().createGenerator(System.out);
        Object value = null;
        serializer.serialize(value, gen, new SerializerProvider());
        assertTrue(gen.getOutputBuffer().toString().isEmpty());
    }

    @Test
    public void testSerializeObject() throws JsonGenerationException, IOException {
        Converter<Object, String> converter = new StringConverter();
        JavaType delegateType = JavaType.construct(String.class);
        Object value = "test";
        StdDelegatingSerializer serializer = new StdDelegatingSerializer(converter, delegateType, null);
        JsonGenerator gen = new JsonFactory().createGenerator(System.out);
        serializer.serialize(value, gen, new SerializerProvider());
        ObjectNode result = new ObjectMapper().readTree(gen.getOutputBuffer().toString());
        assertEquals("test", result.asText());
    }

    @Test
    public void testSerializeWithType() throws JsonGenerationException, IOException {
        Converter<Object, String> converter = new StringConverter();
        JavaType delegateType = JavaType.construct(String.class);
        StdDelegatingSerializer serializer = new StdDelegatingSerializer(converter, delegateType, null);
        TypeSerializer typeSerializer = new SimpleTypeSerializer();
        JsonGenerator gen = new JsonFactory().createGenerator(System.out);
        Object value = "test";
        serializer.serializeWithType(value, gen, new SerializerProvider(), typeSerializer);
        ObjectNode result = new ObjectMapper().readTree(gen.getOutputBuffer().toString());
        assertEquals("test", result.asText());
        String typeName = result.path("@type").asText();
        assertEquals(String.class.getName(), typeName);
    }

    @Test
    public void testIsEmpty() {
        Converter<Object, String> converter = new StringConverter();
        JavaType delegateType = JavaType.construct(String.class);
        StdDelegatingSerializer serializer = new StdDelegatingSerializer(converter, delegateType, null);
        Object value = null;
        assertTrue(serializer.isEmpty(new SerializerProvider(), value));

        value = "";
        assertFalse(serializer.isEmpty(new SerializerProvider(), value));

        value = new Object();
        assertFalse(serializer.isEmpty(new SerializerProvider(), value));
    }

    @Test
    public void testGetSchema() throws JsonMappingException {
        Converter<Object, String> converter = new StringConverter();
        JavaType delegateType = JavaType.construct(String.class);
        StdDelegatingSerializer serializer = new StdDelegatingSerializer(converter, delegateType, null);
        SerializerProvider provider = new SerializerProvider();
        ObjectNode schema = serializer.getSchema(provider, Object.class);
        Assert.assertNull(schema);

        schema = serializer.getSchema(provider, String.class);
        Assert.assertNotNull(schema);
        Assert.assertEquals("string", schema.get("type").asText());
    }

    @Test
    public void testAcceptJsonFormatVisitor() throws JsonMappingException {
        Converter<Object, String> converter = new StringConverter();
        JavaType delegateType = JavaType.construct(String.class);
        StdDelegatingSerializer serializer = new StdDelegatingSerializer(converter, delegateType, null);
        JsonFormatVisitorWrapper visitor = new JsonFormatVisitorWrapper(new StandardSerializer(String.class));
        serializer.acceptJsonFormatVisitor(visitor, Object.class);
        Assert.assertNull(visitor.getSchemaProvider());
        Assert.assertFalse(visitor.hasValueType());
    }
}

class StringConverter implements Converter<Object, String> {
    @Override
    public String convert(Object o) {
        if (o == null) {
            return null;
        }
        return o.toString();
    }
}

class SimpleTypeSerializer extends TypeSerializer {
    @Override
    public void serialize(SerializerProvider p, Type type, JsonGenerator g) throws IOException {
        g.writeString(type.getName());
    }
}
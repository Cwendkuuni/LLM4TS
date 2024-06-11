package com.fasterxml.jackson.databind.ser.std;

import java.io.IOException;
import java.lang.reflect.Type;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JacksonStdImpl;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonFormatVisitable;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonFormatVisitorWrapper;
import com.fasterxml.jackson.databind.jsonschema.SchemaAware;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.ser.FilterProvider;
import com.fasterxml.jackson.databind.ser.PropertyFilter;
import com.fasterxml.jackson.databind.util.ClassUtil;
import com.fasterxml.jackson.databind.util.Converter;
import org.junit.Test;
import org.junit.Assert;

@JacksonStdImpl
public class StdSerializerTest {

    private StdSerializer<Object> stdSerializer;

    @Test
    public void testHandledType() {
        Class<Object> expected = Object.class;
        Assert.assertEquals(expected, stdSerializer.handledType());
    }

    @Test
    public void testSerialize() {
        JsonGenerator jgen = mock(JsonGenerator.class);
        SerializerProvider provider = mock(SerializerProvider.class);
        try {
            stdSerializer.serialize(new Object(), jgen, provider);
        } catch (IOException | JsonGenerationException e) {
            Assert.fail("Unexpected exception was thrown: " + e.getMessage());
        }
    }

    @Test
    public void testGetSchemaWithType() {
        SerializerProvider provider = mock(SerializerProvider.class);
        Type typeHint = mock(Type.class);
        try {
            stdSerializer.getSchema(provider, typeHint);
        } catch (JsonMappingException e) {
            Assert.fail("Unexpected exception was thrown: " + e.getMessage());
        }
    }

    @Test
    public void testGetSchemaWithTypeAndOptional() {
        SerializerProvider provider = mock(SerializerProvider.class);
        Type typeHint = mock(Type.class);
        boolean isOptional = false;
        try {
            stdSerializer.getSchema(provider, typeHint, isOptional);
        } catch (JsonMappingException e) {
            Assert.fail("Unexpected exception was thrown: " + e.getMessage());
        }
    }

    @Test
    public void testCreateObjectNode() {
        ObjectNode expected = JsonNodeFactory.instance.objectNode();
        ObjectNode actual = stdSerializer.createObjectNode();
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testCreateSchemaNode() {
        String type = "type";
        ObjectNode expected = stdSerializer.createObjectNode();
        expected.put("type", type);
        ObjectNode actual = stdSerializer.createSchemaNode(type);
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testCreateSchemaNodeWithTypeOptional() {
        String type = "type";
        boolean isOptional = false;
        ObjectNode expected = stdSerializer.createObjectNode();
        expected.put("type", type);
        ObjectNode actual = stdSerializer.createSchemaNode(type, isOptional);
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testAcceptJsonFormatVisitor() {
        JsonFormatVisitorWrapper visitor = mock(JsonFormatVisitorWrapper.class);
        JavaType typeHint = mock(JavaType.class);
        try {
            stdSerializer.acceptJsonFormatVisitor(visitor, typeHint);
        } catch (JsonMappingException e) {
            Assert.fail("Unexpected exception was thrown: " + e.getMessage());
        }
    }

    @Test
    public void testIsDefaultSerializer() {
        JsonSerializer<?> serializer = mock(JsonSerializer.class);
        boolean expected = false;
        boolean actual = stdSerializer.isDefaultSerializer(serializer);
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testFindConvertingContentSerializer() {
        SerializerProvider provider = mock(SerializerProvider.class);
        BeanProperty prop = mock(BeanProperty.class);
        JsonSerializer<?> existingSerializer = mock(JsonSerializer.class);
        try {
            stdSerializer.findConvertingContentSerializer(provider, prop, existingSerializer);
        } catch (JsonMappingException e) {
            Assert.fail("Unexpected exception was thrown: " + e.getMessage());
        }
    }

    @Test
    public void testFindPropertyFilter() {
        SerializerProvider provider = mock(SerializerProvider.class);
        Object filterId = new Object();
        Object valueToFilter = new Object();
        try {
            stdSerializer.findPropertyFilter(provider, filterId, valueToFilter);
        } catch (JsonMappingException e) {
            Assert.fail("Unexpected exception was thrown: " + e.getMessage());
        }
    }
}
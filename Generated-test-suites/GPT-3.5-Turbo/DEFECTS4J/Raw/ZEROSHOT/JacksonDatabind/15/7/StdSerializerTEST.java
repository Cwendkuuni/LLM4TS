package com.fasterxml.jackson.databind.ser.std;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Type;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
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

@JacksonStdImpl
public class StdSerializerTest {

    private StdSerializer stdSerializer;

    @Before
    public void setUp() {
        stdSerializer = new StdSerializer<Object>(Object.class) {
            @Override
            public void serialize(Object value, JsonGenerator jgen, SerializerProvider provider) throws IOException, JsonGenerationException {

            }

            @Override
            public JsonNode getSchema(SerializerProvider provider, Type typeHint) throws JsonMappingException {
                return null;
            }

            @Override
            public JsonNode getSchema(SerializerProvider provider, Type typeHint, boolean isOptional) throws JsonMappingException {
                return null;
            }

            @Override
            public void acceptJsonFormatVisitor(JsonFormatVisitorWrapper visitor, JavaType typeHint) throws JsonMappingException {

            }
        };
    }

    @Test
    public void testHandledType() {
        Class<Object> result = stdSerializer.handledType();
        assertEquals(result, Object.class);
    }

    @Test
    public void testSerialize() {
        Object value = new Object();
        JsonGenerator jgen = null;
        SerializerProvider provider = null;
        try {
            stdSerializer.serialize(value, jgen, provider);
        } catch (IOException | JsonGenerationException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testGetSchemaType() {
        SerializerProvider provider = null;
        Type typeHint = null;
        try {
            JsonNode result = stdSerializer.getSchema(provider, typeHint);
            assertEquals(result, null);
        } catch (JsonMappingException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testGetSchema() {
        SerializerProvider provider = null;
        Type typeHint = null;
        boolean isOptional = false;
        try {
            JsonNode result = stdSerializer.getSchema(provider, typeHint, isOptional);
            assertEquals(result, null);
        } catch (JsonMappingException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testCreateObjectNode() {
        ObjectNode result = stdSerializer.createObjectNode();
    }

    @Test
    public void testCreateSchemaNodeWithType() {
        String type = "string";
        ObjectNode result = stdSerializer.createSchemaNode(type);
    }

    @Test
    public void testCreateSchemaNodeWithTypeAndOptional() {
        String type = "string";
        boolean isOptional = false;
        ObjectNode result = stdSerializer.createSchemaNode(type, isOptional);
    }

    @Test
    public void testAcceptJsonFormatVisitor() {
        JsonFormatVisitorWrapper visitor = null;
        JavaType typeHint = null;
        try {
            stdSerializer.acceptJsonFormatVisitor(visitor, typeHint);
        } catch (JsonMappingException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testWrapAndThrowForField() {
        SerializerProvider provider = null;
        Throwable t = new Throwable();
        Object bean = new Object();
        String fieldName = "field";
        try {
            stdSerializer.wrapAndThrow(provider, t, bean, fieldName);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testWrapAndThrowForIndex() {
        SerializerProvider provider = null;
        Throwable t = new Throwable();
        Object bean = new Object();
        int index = 0;
        try {
            stdSerializer.wrapAndThrow(provider, t, bean, index);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testIsDefaultSerializer() {
        JsonSerializer<?> serializer = null;
        boolean result = stdSerializer.isDefaultSerializer(serializer);
        assertEquals(result, false);
    }

    @Test
    public void testFindConvertingContentSerializer() {
        SerializerProvider provider = null;
        BeanProperty prop = null;
        JsonSerializer<?> existingSerializer = null;
        try {
            JsonSerializer<?> result = stdSerializer.findConvertingContentSerializer(provider, prop, existingSerializer);
            assertEquals(result, existingSerializer);
        } catch (JsonMappingException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testFindPropertyFilter() {
        SerializerProvider provider = null;
        Object filterId = new Object();
        Object valueToFilter = new Object();
        try {
            stdSerializer.findPropertyFilter(provider, filterId, valueToFilter);
        } catch (JsonMappingException e) {
            e.printStackTrace();
        }
    }

}
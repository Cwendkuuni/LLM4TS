package com.fasterxml.jackson.databind.ser.std;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
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

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

@JacksonStdImpl
public class StdSerializerTest {

    StdSerializer<String> stdSerializer;

    @Mock
    private JsonGenerator jsonGenerator;

    @Mock
    private SerializerProvider serializerProvider;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        stdSerializer = new StdSerializer<String>(String.class) {
            @Override
            public void serialize(String value, JsonGenerator jgen, SerializerProvider provider) throws IOException, JsonGenerationException {

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
        Assert.assertEquals(String.class, stdSerializer.handledType());
    }

    @Test
    public void testIsDefaultSerializer() {
        Assert.assertFalse(stdSerializer.isDefaultSerializer(stdSerializer));
    }

    @Test
    public void testSerialize() throws IOException {
        stdSerializer.serialize("testValue", jsonGenerator, serializerProvider);
    }

    @Test
    public void testGetSchema() throws JsonMappingException {
        Assert.assertNull(stdSerializer.getSchema(serializerProvider, null));
    }

    @Test
    public void testGetSchemaWithOptional() throws JsonMappingException {
        Assert.assertNull(stdSerializer.getSchema(serializerProvider, null, true));
        Assert.assertNull(stdSerializer.getSchema(serializerProvider, null, false));
    }
    
    @Test
    public void testAcceptJsonFormatVisitor() throws JsonMappingException {
        MockJsonFormatVisitorWrapper visitorWrapper = new MockJsonFormatVisitorWrapper();
        stdSerializer.acceptJsonFormatVisitor(visitorWrapper, null);
    }

    @Test
    public void testWrapAndThrowWithBeanField() throws IOException {
        stdSerializer.wrapAndThrow(serializerProvider, new IllegalArgumentException(), "test", "fieldName");
    }

    @Test
    public void testWrapAndThrowWithBeanIndex() throws IOException {
        stdSerializer.wrapAndThrow(serializerProvider, new IllegalArgumentException(), "test", 1);
    }

    @Test
    public void testFindConvertingContentSerializer() throws JsonMappingException {
        stdSerializer.findConvertingContentSerializer(serializerProvider, null, null);
    }

    @Test(expected = JsonMappingException.class)
    public void testFindPropertyFilterWithoutFilterProvider() throws JsonMappingException {
        stdSerializer.findPropertyFilter(serializerProvider, null, null);
    }
}
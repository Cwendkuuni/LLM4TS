package com.fasterxml.jackson.databind.ser.std;

import org.junit.Test;
import static org.junit.Assert.*;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JacksonStdImpl;
import com.fasterxml.jackson.databind.introspect.AnnotatedMethod;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonFormatVisitable;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonFormatVisitorWrapper;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonStringFormatVisitor;
import com.fasterxml.jackson.databind.jsonschema.SchemaAware;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.fasterxml.jackson.databind.ser.BeanSerializer;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;

@JacksonStdImpl
public class JsonValueSerializerTest {

    private JsonValueSerializer jsonValueSerializer;

    @Before
    public void setUp() throws Exception {
        AnnotatedMethod method = new AnnotatedMethod();
        JsonSerializer<?> ser = new JsonSerializer();
        jsonValueSerializer = new JsonValueSerializer(method, ser);
    }

    @Test
    public void testCreateContextual() throws Exception {
        SerializerProvider provider = new SerializerProvider();
        BeanProperty property = new BeanProperty();
        Serializer result = jsonValueSerializer.createContextual(provider, property);
        assertTrue(result instanceof JsonValueSerializer);
    }

    @Test
    public void testSerialize() throws Exception {
        Object bean = new Object();
        JsonGenerator gen = new JsonGenerator();
        SerializerProvider prov = new SerializerProvider();
        jsonValueSerializer.serialize(bean, gen, prov);
        // assert something
    }

    @Test
    public void testSerializeWithType() throws Exception {
        Object bean = new Object();
        JsonGenerator gen = new JsonGenerator();
        SerializerProvider provider = new SerializerProvider();
        TypeSerializer typeSer0 = new TypeSerializer();
        jsonValueSerializer.serializeWithType(bean, gen, provider, typeSer0);
        // assert something
    }
    
    @Test
    public void testGetSchema() throws Exception {
        SerializerProvider provider = new SerializerProvider();
        Type type = new Type();
        jsonValueSerializer.getSchema(provider, type);
        // assert something
    }

    @Test
    public void testAcceptJsonFormatVisitor() throws Exception {
        JsonFormatVisitorWrapper visitor = new JsonFormatVisitorWrapper();
        JavaType typeHint = new JavaType();
        jsonValueSerializer.acceptJsonFormatVisitor(visitor, typeHint);
        // assert something
    }
}
package com.fasterxml.jackson.databind.ser.std;

import org.junit.Test;
import static org.junit.Assert.*;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Type;
import java.util.LinkedHashSet;
import java.util.Set;
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


public class JsonValueSerializerTest {

    @Test
    public void testConstructor() {
        AnnotatedMethod valueMethod = new AnnotatedMethod();
        JsonSerializer<?> ser = new JsonSerializer<>();
        JsonValueSerializer jsonValueSerializer = new JsonValueSerializer(valueMethod, ser);
        assertNotNull(jsonValueSerializer);
    }

    @Test
    public void testConstructor2() {
        AnnotatedMethod valueMethod = new AnnotatedMethod();
        JsonSerializer<?> ser = new JsonSerializer<>();
        BeanProperty property = new BeanProperty();
        boolean forceTypeInfo = true;
        JsonValueSerializer jsonValueSerializer = new JsonValueSerializer(valueMethod, ser, property, forceTypeInfo);
        assertNotNull(jsonValueSerializer);
    }

    @Test
    public void testWithResolved() {
        AnnotatedMethod valueMethod = new AnnotatedMethod();
        JsonSerializer<?> ser = new JsonSerializer<>();
        BeanProperty property = new BeanProperty();
        boolean forceTypeInfo = true;
        JsonValueSerializer jsonValueSerializer = new JsonValueSerializer(valueMethod, ser);
        JsonValueSerializer result = jsonValueSerializer.withResolved(property, ser, forceTypeInfo);
        assertEquals(property, result._property);
    }

    @Test
    public void testCreateContextual() {
        SerializerProvider provider = new SerializerProvider();
        BeanProperty property = new BeanProperty();
        JsonValueSerializer jsonValueSerializer = new JsonValueSerializer(new AnnotatedMethod(), new JsonSerializer<>());
        try {
            JsonSerializer<?> result = jsonValueSerializer.createContextual(provider, property);
            assertNotNull(result);
        } catch (JsonMappingException e) {
            fail();
        }
    }

    @Test
    public void testSerialize() {
        try {
            SerializerProvider prov = new SerializerProvider();
            JsonGenerator gen = new JsonGenerator();
            JsonValueSerializer jsonValueSerializer = new JsonValueSerializer(new AnnotatedMethod(), new JsonSerializer<>());
            jsonValueSerializer.serialize(new Object(), gen, prov);
        } catch (Exception e) {
            fail();
        }
    }

    @Test
    public void testSerializeWithType() {
        try {
            SerializerProvider provider = new SerializerProvider();
            JsonGenerator gen = new JsonGenerator();
            TypeSerializer typeSer0 = new TypeSerializer();
            JsonValueSerializer jsonValueSerializer = new JsonValueSerializer(new AnnotatedMethod(), new JsonSerializer<>());
            jsonValueSerializer.serializeWithType(new Object(), gen, provider, typeSer0);
        } catch (Exception e) {
            fail();
        }
    }

    @Test
    public void testGetSchema() {
        try {
            SerializerProvider provider = new SerializerProvider();
            Type typeHint = new Type();
            JsonValueSerializer jsonValueSerializer = new JsonValueSerializer(new AnnotatedMethod(), new JsonSerializer<>());
            jsonValueSerializer.getSchema(provider, typeHint);
        } catch (Exception e) {
            fail();
        }
    }

    @Test
    public void testAcceptJsonFormatVisitor() {
        try {
            JavaType typeHint = new JavaType();
            JsonFormatVisitorWrapper visitor = new JsonFormatVisitorWrapper();
            JsonValueSerializer jsonValueSerializer = new JsonValueSerializer(new AnnotatedMethod(), new JsonSerializer<>());
            jsonValueSerializer.acceptJsonFormatVisitor(visitor, typeHint);
        } catch (Exception e) {
            fail();
        }
    }

    @Test
    public void test_acceptJsonFormatVisitorForEnum() {
        try {
            JavaType typeHint = new JavaType();
            Class<?> enumType = new Class<>();
            JsonFormatVisitorWrapper visitor = new JsonFormatVisitorWrapper();
            JsonValueSerializer jsonValueSerializer = new JsonValueSerializer(new AnnotatedMethod(), new JsonSerializer<>());
            jsonValueSerializer._acceptJsonFormatVisitorForEnum(visitor, typeHint, enumType);
        } catch (Exception e) {
            fail();
        }
    }
}
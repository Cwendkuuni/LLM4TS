package com.fasterxml.jackson.databind.ser.std;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.SerializationConfig;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.introspect.AnnotatedMethod;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonFormatVisitable;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonFormatVisitorWrapper;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.fasterxml.jackson.databind.type.TypeBindings;
import com.fasterxml.jackson.databind.util.ClassUtil;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Type;
import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.assertNotNull;

public class JsonValueSerializerTest {

    @Test
    public void testSerialize() throws IOException {
        AnnotatedMethod setter = new AnnotatedMethod(JsonValueSerializer.class, "serialize", new Class<?>[]{
                Object.class, JsonGenerator.class, SerializerProvider.class
        });

        JsonValueSerializer serializer = new JsonValueSerializer(setter, null);
        JsonSerializer<Object> valueSerializer = new JsonSerializer<Object>() {
            @Override
            public void serialize(Object value, JsonGenerator gen, SerializerProvider serializers) throws IOException {

            }
        };

        serializer.serialize(new Object(), null, null);
    }

    @Test
    public void testSerializeWithType() throws IOException {
        AnnotatedMethod setter = new AnnotatedMethod(JsonValueSerializer.class, "serializeWithType", new Class<?>[]{
                Object.class, JsonGenerator.class, SerializerProvider.class, TypeSerializer.class
        });

        JsonValueSerializer serializer = new JsonValueSerializer(setter, null);
        JsonSerializer<Object> valueSerializer = new JsonSerializer<Object>() {
            @Override
            public void serialize(Object value, JsonGenerator gen, SerializerProvider serializers) throws IOException {

            }
        };

        serializer.serializeWithType(new Object(), null, null, null);
    }

    @Test
    public void testGetSchema() throws IOException {
        AnnotatedMethod setter = new AnnotatedMethod(JsonValueSerializer.class, "getSchema", new Class<?>[]{
                SerializerProvider.class, Type.class
        });

        JsonValueSerializer serializer = new JsonValueSerializer(setter, null);
        JsonSerializer<Object> valueSerializer = new JsonSerializer<Object>() {
            @Override
            public void serialize(Object value, JsonGenerator gen, SerializerProvider serializers) throws IOException {

            }
        };

        serializer.getSchema(null, null);
    }

    @Test
    public void testAcceptJsonFormatVisitor() throws IOException {
        AnnotatedMethod setter = new AnnotatedMethod(JsonValueSerializer.class, "acceptJsonFormatVisitor", new Class<?>[]{
                JsonFormatVisitorWrapper.class, JavaType.class
        });

        JsonValueSerializer serializer = new JsonValueSerializer(setter, null);
        TypeBindings typeBindings = null;
        ClassUtil.findTypeParameters(JsonFormatVisitorWrapper.class, serializer.getClass(), typeBindings);
        Set<Class<?>> classes = new HashSet<Class<?>>();

        for(Class<?> clazz : classes) {
            JsonFormatVisitable jsonFormatVisitable = null;
            serializer.acceptJsonFormatVisitor(jsonFormatVisitable, (JavaType)null);
        }
    }
}
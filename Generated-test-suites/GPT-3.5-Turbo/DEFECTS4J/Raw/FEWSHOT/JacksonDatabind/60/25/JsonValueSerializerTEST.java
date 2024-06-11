package com.fasterxml.jackson.databind.ser.std;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Type;

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
import org.junit.Test;

@JacksonStdImpl
public class JsonValueSerializerTest {
    private final Object bean = new Object();
    private final JsonGenerator gen = mock(JsonGenerator.class);
    private final SerializerProvider prov = mock(SerializerProvider.class);
    private final BeanProperty property = mock(BeanProperty.class);
    private final TypeSerializer typeSer0 = mock(TypeSerializer.class);
    private final JsonStringFormatVisitor stringVisitor = mock(JsonStringFormatVisitor.class);

    private final JsonSerializer<?> serializer;

    public JsonValueSerializerTest() throws NoSuchMethodException {
        JsonSerializer<?> ser = mock(JsonSerializer.class);
        AnnotatedMethod valueMethod = new FakeAnnotatedMethod();
        Class<?> type = ser.getClass();
        boolean forceTypeInfo = false;

        serializer = new JsonValueSerializer(valueMethod, ser);
        serializer.acceptJsonFormatVisitor(mock(JsonFormatVisitorWrapper.class), mock(JavaType.class));
    }

    @Test
    public void testSerialize() throws IOException {
        serializer.serialize(bean, gen, prov);
    }

    @Test
    public void testSerializeWithType() throws IOException {
        serializer.serializeWithType(bean, gen, prov, typeSer0);
    }

    @Test
    public void testGetSchema() throws JsonMappingException {
        serializer.getSchema(prov, null);
    }

    @Test
    public void testAcceptJsonFormatVisitorForEnum() throws JsonMappingException {
        ((JsonValueSerializer) serializer)._acceptJsonFormatVisitorForEnum(mock(JsonFormatVisitorWrapper.class), mock(JavaType.class), serializer.handledType());
    }

    @Test
    public void testIsNaturalTypeWithStdHandling() {
        ((JsonValueSerializer) serializer).isNaturalTypeWithStdHandling(bean.getClass(), serializer);
    }

    @Test
    public void testToString() {
        serializer.toString();
    }

    private static class FakeAnnotatedMethod extends AnnotatedMethod {
        public FakeAnnotatedMethod() throws NoSuchMethodException {
            super(JsonValueSerializer.class.getMethod("serialize", Object.class, JsonGenerator.class, SerializerProvider.class));
        }
    }
}
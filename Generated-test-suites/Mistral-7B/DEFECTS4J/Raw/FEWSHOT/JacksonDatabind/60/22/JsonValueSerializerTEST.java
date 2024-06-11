package com.fasterxml.jackson.databind.ser.std;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.introspect.AnnotatedMethod;
import com.fasterxml.jackson.databind.jsonschema.SchemaAware;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.fasterxml.jackson.databind.ser.BeanSerializer;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;
import com.fasterxml.jackson.databind.ser.JsonSerializer;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.fasterxml.jackson.databind.util.ClassUtil;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Type;
import java.util.LinkedHashSet;
import java.util.Set;

public class JsonValueSerializerTest {

    @Test
    public void testJsonValueSerializer() throws Exception {
        JsonValueSerializer serializer = new JsonValueSerializer(null, null, null, false);

        Class<?> clazz = Integer.TYPE;
        Object value = 123;
        JsonSerializer<Object> valueSerializer = new StdSerializer<Object>(clazz) {
            @Override
            public void serialize(Object value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
                gen.writeNumber(value.intValue());
            }
        };

        AnnotatedMethod accessorMethod = new AnnotatedMethod(JsonValueSerializerTest.class.getDeclaredMethod("getInteger"), JsonValueSerializerTest.class);
        JsonValueSerializer serializedSerializer = serializer.withResolved(null, valueSerializer, false);

        Object testObject = new JsonValueSerializerTest();
        JsonGenerator gen = mock(JsonGenerator.class);
        SerializerProvider serializers = mock(SerializerProvider.class);
        TypeSerializer typeSerializer = mock(TypeSerializer.class);

        serializedSerializer.serialize(testObject, gen, serializers);
        verify(gen).writeNumber(123);
    }

    @Test
    public void testJsonValueSerializerFindValueSerializer() throws Exception {
        JsonValueSerializer serializer = new JsonValueSerializer(null, null, null, false);

        Class<?> clazz = Integer.TYPE;
        Object value = 123;

        JsonSerializer<Object> valueSerializer = new StdSerializer<Object>(clazz) {
            @Override
            public void serialize(Object value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
                gen.writeNumber(value.intValue());
            }
        };

        AnnotatedMethod accessorMethod = new AnnotatedMethod(JsonValueSerializerTest.class.getDeclaredMethod("getInteger"), JsonValueSerializerTest.class);
        JsonValueSerializer serializedSerializer = serializer.withResolved(null, null, valueSerializer, false);

        SerializerProvider serializers = mock(SerializerProvider.class);
        TypeSerializer typeSerializer = mock(TypeSerializer.class);

        JsonSerializer<Object> actualSerializer = serializedSerializer.findValueSerializer(clazz, null);
        assertEquals(valueSerializer, actualSerializer);

        verify(serializers).findValueSerializer(clazz, null);
    }

    @Test
    public void testJsonValueSerializerIsNaturalTypeWithStdHandling() throws Exception {
        JsonValueSerializer serializer = new JsonValueSerializer(null, null, null, false);

        Class<?> clazz = Integer.TYPE;
        JsonSerializer<Object> valueSerializer = new StdSerializer<Object>(clazz) {
            @Override
            public void serialize(Object value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
                gen.writeNumber(value.intValue());
            }
        };

        JsonValueSerializer serializedSerializer = serializer.withResolved(null, null, valueSerializer, true);
        assertTrue(serializedSerializer.isNaturalTypeWithStdHandling(clazz, valueSerializer));

        clazz = String.class;
        assertFalse(serializedSerializer.isNaturalTypeWithStdHandling(clazz, valueSerializer));
    }

    @Test
    public void testJsonValueSerializerAcceptJsonFormatVisitor() throws Exception {
        JsonValueSerializer serializer = new JsonValueSerializer(null, null, null, false);

        Class<?> clazz = Integer.TYPE;
        JsonSerializer<Object> valueSerializer = new StdSerializer<Object>(clazz) {
            @Override
            public void serialize(Object value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
                gen.writeNumber(value.intValue());
            }
        };

        AnnotatedMethod accessorMethod = new AnnotatedMethod(JsonValueSerializerTest.class.getDeclaredMethod("getInteger"), JsonValueSerializerTest.class);
        JsonValueSerializer serializedSerializer = serializer.withResolved(null, null, valueSerializer, false);

        JsonFormatVisitorWrapper visitor = mock(JsonFormatVisitorWrapper.class);
        JavaType typeHint = mock(JavaType.class);

        serializedSerializer.acceptJsonFormatVisitor(visitor, typeHint);

        verify(visitor).expectAnyFormat(typeHint);
    }

    @Test
    public void testJsonValueSerializerAcceptJsonFormatVisitorForEnum() throws Exception {
        JsonValueSerializer serializer = new JsonValueSerializer(null, null, null, false);

        Class<?> enumType = Enum.class;
        JavaType typeHint = TypeFactory.getDefaultInstance().constructType(enumType.getPackage().getName() + ".MyEnum");

        AnnotatedMethod accessorMethod = new AnnotatedMethod(JsonValueSerializerTest.class.getDeclaredMethod("getMyEnum"), JsonValueSerializerTest.class);
        JsonValueSerializer serializedSerializer = serializer.withResolved(null, null, null, false);

        JsonStringFormatVisitor stringVisitor = mock(JsonStringFormatVisitor.class);
        serializedSerializer._acceptJsonFormatVisitorForEnum(stringVisitor, typeHint, enumType);

        Set<String> enums = new LinkedHashSet<>();
        enums.add("ONE");
        enums.add("TWO");

        verify(stringVisitor).enumTypes(enums);
    }

    @JsonSerialize(using = JsonValueSerializer.class)
    static class MyEnum {
        @JsonValue
        public String getValue() {
            return name();
        }

        public String ONE() { return "ONE"; }
        public String TWO() { return "TWO"; }
    }

    private int getInteger() {
        return 123;
    }
}
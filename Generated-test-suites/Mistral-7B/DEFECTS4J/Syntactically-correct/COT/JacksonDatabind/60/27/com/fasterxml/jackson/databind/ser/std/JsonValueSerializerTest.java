package com.example.test; // replace with your test package

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JsonValue;
import com.fasterxml.jackson.databind.introspect.AnnotatedMethod;
import com.fasterxml.jackson.databind.jsonschema.JsonSchema;
import com.fasterxml.jackson.databind.ser.BeanProperty;
import com.fasterxml.jackson.databind.ser.std.JsonValueSerializer;
import com.fasterxml.jackson.databind.test.BaseTest;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.LinkedHashSet;
import java.util.Set;

import static org.junit.Assert.*;

public class JsonValueSerializerTest extends BaseTest {

    // Basic test cases for the constructor
    @org.junit.Test
    public void testJsonValueSerializerConstructorWithValueMethodAndSerializer() {
        AnnotatedMethod valueMethod = mock(AnnotatedMethod.class);
        JsonSerializer<Object> serializer = mock(JsonSerializer.class);

        JsonValueSerializer jsonValueSerializer = new JsonValueSerializer(valueMethod, serializer);

        assertEquals(JsonValueSerializer.class, jsonValueSerializer.getClass());
        assertEquals(valueMethod, jsonValueSerializer._accessorMethod);
        assertEquals(serializer, jsonValueSerializer._valueSerializer);
        assertEquals(null, jsonValueSerializer._property);
        assertEquals(true, jsonValueSerializer._forceTypeInformation);
    }

    @org.junit.Test
    public void testJsonValueSerializerConstructorWithResolvedPropertyPropertySerializerSerializerAndForceTypeInfo() {
        AnnotatedMethod accessorMethod = mock(AnnotatedMethod.class);
        JsonSerializer<Object> valueSerializer = mock(JsonSerializer.class);
        BeanProperty property = mock(BeanProperty.class);
        JsonSerializer<Object> propertySerializer = mock(JsonSerializer.class);

        JsonValueSerializer jsonValueSerializer = new JsonValueSerializer(jsonValueSerializer, property, valueSerializer, propertySerializer, true);

        assertEquals(JsonValueSerializer.class, jsonValueSerializer.getClass());
        assertEquals(accessorMethod, jsonValueSerializer._accessorMethod);
        assertEquals(valueSerializer, jsonValueSerializer._valueSerializer);
        assertEquals(property, jsonValueSerializer._property);
        assertEquals(false, jsonValueSerializer._forceTypeInformation);
    }

    // Test methods for createContextual
    @org.junit.Test
    public void testCreateContextualReturnThisWhenSerializerIsNotNull() throws JsonMappingException {
        JsonValueSerializer jsonValueSerializer = new JsonValueSerializer(mock(AnnotatedMethod.class), mock(JsonSerializer.class));

        JsonSerializer<?> result = jsonValueSerializer.createContextual(mock(SerializerProvider.class), mock(BeanProperty.class));

        assertEquals(jsonValueSerializer, result);
    }

    @org.junit.Test
    public void testCreateContextualReturnsNewInstanceWhenSerializerIsNull() throws JsonMappingException {
        JsonValueSerializer jsonValueSerializer = new JsonValueSerializer(mock(AnnotatedMethod.class), mock(JsonSerializer.class));

        JsonValueSerializer result = jsonValueSerializer.createContextual(mock(SerializerProvider.class), mock(BeanProperty.class));

        assertNotSame(jsonValueSerializer, result);
    }

    // Test methods for serialize
    @org.junit.Test
    public void testSerializeNullValue() throws IOException {
        Object bean = null;
        JsonValueSerializer jsonValueSerializer = new JsonValueSerializer(mock(AnnotatedMethod.class), mock(JsonSerializer.class));

        JsonGenerator jsonGenerator = createJsonGenerator();
        SerializerProvider serializerProvider = createSerializerProvider();

        jsonValueSerializer.serialize(bean, jsonGenerator, serializerProvider);

        assertNull(jsonGenerator.getOutputBuffer());
    }

    @org.junit.Test
    public void testSerializeValueFromValueMethod() throws IOException, JsonProcessingException {
        Object bean = new TestBean();
        JsonValueSerializer jsonValueSerializer = new JsonValueSerializer(mock(AnnotatedMethod.class), mock(JsonSerializer.class));

        JsonGenerator jsonGenerator = createJsonGenerator();
        SerializerProvider serializerProvider = createSerializerProvider();

        jsonValueSerializer.serialize(bean, jsonGenerator, serializerProvider);

        String expectedOutput = "testValue";
        String actualOutput = jsonGenerator.getOutputBuffer().toString();

        assertEquals(expectedOutput, actualOutput);
    }

    @org.junit.Test
    public void testSerializeValueWithTypeInformation() throws IOException, JsonProcessingException {
        Object bean = new TestBean();
        JsonValueSerializer jsonValueSerializer = new JsonValueSerializer(mock(AnnotatedMethod.class), mock(JsonSerializer.class), true);

        JsonGenerator jsonGenerator = createJsonGenerator();
        SerializerProvider serializerProvider = createSerializerProvider();
        TypeSerializer typeSerializer = mock(TypeSerializer.class);

        jsonValueSerializer.serializeWithType(bean, jsonGenerator, serializerProvider, typeSerializer);

        String expectedOutput = "testValue";
        String actualOutput = jsonGenerator.getOutputBuffer().toString();

        assertEquals(expectedOutput, actualOutput);
        verify(typeSerializer).writeTypePrefixForScalar(eq(bean), eq(jsonGenerator));
        verify(typeSerializer).writeTypeSuffixForScalar(eq(bean), eq(jsonGenerator));
    }

    // Test methods for acceptJsonFormatVisitor
    @org.junit.Test
    public void testAcceptJsonFormatVisitorWhenValueMethodReturnsEnum() throws JsonMappingException {
        AnnotatedMethod annotatedMethod = mock(AnnotatedMethod.class);
        JsonFormatVisitorWrapper jsonFormatVisitorWrapper = mock(JsonFormatVisitorWrapper.class);
        JavaType javaType = mock(JavaType.class);

        when(annotatedMethod.getType()).thenReturn(javaType);
        when(jsonFormatVisitorWrapper.expectStringFormat(any(JavaType.class))).thenReturn(mock(JsonStringFormatVisitor.class));

        JsonValueSerializer jsonValueSerializer = new JsonValueSerializer(annotatedMethod, null);

        jsonValueSerializer.acceptJsonFormatVisitor(jsonFormatVisitorWrapper, javaType);

        Set<String> enums = new LinkedHashSet<>();
        enums.add("ONE");
        enums.add("TWO");

        verify(jsonFormatVisitorWrapper).enumTypes(eq(enums));
    }

    // Edge cases and exception handling
    @org.junit.Test(expected = JsonMappingException.class)
    public void testSerializeThrowsJsonMappingExceptionWhenValueMethodThrowsException() throws IOException, InvocationTargetException {
        Object bean = new TestExceptionBean();
        JsonValueSerializer jsonValueSerializer = new JsonValueSerializer(mock(AnnotatedMethod.class), mock(JsonSerializer.class));

        JsonGenerator jsonGenerator = createJsonGenerator();
        SerializerProvider serializerProvider = createSerializerProvider();

        jsonValueSerializer.serialize(bean, jsonGenerator, serializerProvider);

        fail("Expected JsonMappingException, but got none");
    }

    @org.junit.Test(expected = InvocationTargetException.class)
    public void testSerializeThrowsInvocationTargetExceptionWhenValueMethodThrowsRuntimeException() throws IOException, InvocationTargetException {
        Object bean = new TestRuntimeExceptionBean();
        JsonValueSerializer jsonValueSerializer = new JsonValueSerializer(mock(AnnotatedMethod.class), mock(JsonSerializer.class));

        JsonGenerator jsonGenerator = createJsonGenerator();
        SerializerProvider serializerProvider = createSerializerProvider();

        jsonValueSerializer.serialize(bean, jsonGenerator, serializerProvider);

        fail("Expected InvocationTargetException, but got none");
    }

    @org.junit.Test(expected = Error.class)
    public void testSerializeThrowsErrorWhenValueMethodThrowsError() throws IOException, InvocationTargetException {
        Object bean = new TestErrorBean();
        JsonValueSerializer jsonValueSerializer = new JsonValueSerializer(mock(AnnotatedMethod.class), mock(JsonSerializer.class));

        JsonGenerator jsonGenerator = createJsonGenerator();
        SerializerProvider serializerProvider = createSerializerProvider();

        jsonValueSerializer.serialize(bean, jsonGenerator, serializerProvider);

        fail("Expected Error, but got none");
    }

    // Test methods for isNaturalTypeWithStdHandling
    @org.junit.Test
    public void testIsNaturalTypeWithStdHandlingReturnsFalseWhenTypeIsNotNatural() {
        JsonValueSerializer jsonValueSerializer = new JsonValueSerializer(mock(AnnotatedMethod.class), mock(JsonSerializer.class));

        assertFalse(jsonValueSerializer.isNaturalTypeWithStdHandling(new Class<?>(){}, mock(JsonSerializer.class)));
    }

    @org.junit.Test
    public void testIsNaturalTypeWithStdHandlingReturnsTrueWhenTypeIsPrimitive() {
        JsonValueSerializer jsonValueSerializer = new JsonValueSerializer(mock(AnnotatedMethod.class), mock(JsonSerializer.class));

        assertTrue(jsonValueSerializer.isNaturalTypeWithStdHandling(Integer.TYPE, mock(JsonSerializer.class)));
        assertTrue(jsonValueSerializer.isNaturalTypeWithStdHandling(Boolean.TYPE, mock(JsonSerializer.class)));
        assertTrue(jsonValueSerializer.isNaturalTypeWithStdHandling(Double.TYPE, mock(JsonSerializer.class)));
    }

    @org.junit.Test
    public void testIsNaturalTypeWithStdHandlingReturnsTrueWhenTypeIsString() {
        JsonValueSerializer jsonValueSerializer = new JsonValueSerializer(mock(AnnotatedMethod.class), mock(JsonSerializer.class));

        assertTrue(jsonValueSerializer.isNaturalTypeWithStdHandling(String.class, mock(JsonSerializer.class)));
    }

    @org.junit.Test
    public void testIsNaturalTypeWithStdHandlingReturnsFalseWhenSerializerIsNotDefaultSerializer() {
        JsonValueSerializer jsonValueSerializer = new JsonValueSerializer(mock(AnnotatedMethod.class), mock(JsonSerializer.class));

        assertFalse(jsonValueSerializer.isNaturalTypeWithStdHandling(String.class, new JsonSerializer<String>(){}));
    }

    // Helper classes
    public static class TestBean {
        @JsonValue
        public String testValue() {
            return "testValue";
        }
    }

    public static class TestExceptionBean {
        @JsonValue
        public String testValue() throws IOException {
            throw new IOException();
        }
    }

    public static class TestRuntimeExceptionBean {
        @JsonValue
        public String testValue() throws RuntimeException {
            throw new RuntimeException();
        }
    }

    public static class TestErrorBean {
        @JsonValue
        public String testValue() throws Error {
            throw new Error();
        }
    }
}
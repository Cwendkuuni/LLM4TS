package com.example.junit; // Replace this package with the actual package where your test file will reside

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.json.JsonSerializer;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.fasterxml.jackson.test.BaseMapTest;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.LinkedHashSet;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.*;

public class JsonValueSerializerTest {

    private ObjectMapper objectMapper;
    private SerializerProvider serializerProvider;
    private TypeSerializer typeSerializer;
    private JsonSerializer<Integer> intSerializer;
    private JsonSerializer<Boolean> booleanSerializer;
    private JsonSerializer<Double> doubleSerializer;
    private JsonSerializer<String> stringSerializer;

    @Before
    public void setup() {
        objectMapper = new ObjectMapper();
        serializerProvider = mock(SerializerProvider.class);
        typeSerializer = mock(TypeSerializer.class);
        intSerializer = mock(JsonSerializer.class);
        booleanSerializer = mock(JsonSerializer.class);
        doubleSerializer = mock(JsonSerializer.class);
        stringSerializer = mock(JsonSerializer.class);
    }

    // Test constructor
    @Test
    public void testJsonValueSerializerConstructor() {
        JsonValueSerializer jsonValueSerializer = new JsonValueSerializer(null, intSerializer);
        assertNotNull(jsonValueSerializer);
    }

    // Test methods with explicit serializer
    @Test
    public void testSerializeWithExplicitSerializer() throws IOException {
        when(intSerializer.serialize(anyInt(), eq(serializerProvider), any(TypeSerializer.class)))
                .thenReturn("123");

        JsonValueSerializer jsonValueSerializer = new JsonValueSerializer(
                new AnnotatedMethod(JsonValueSerializerTest.class.getDeclaredMethod("getIntValue"), JsonValueSerializer.class),
                intSerializer);

        Object bean = new Object();
        String result = jsonValueSerializer.serialize(bean, null, serializerProvider);
        assertEquals("123", result);
    }

    @Test
    public void testSerializeWithDefaultSerializer() throws IOException {
        when(intSerializer.serialize(anyInt(), eq(serializerProvider), any(TypeSerializer.class)))
                .thenCallRealMethod();

        JsonValueSerializer jsonValueSerializer = new JsonValueSerializer(
                new AnnotatedMethod(JsonValueSerializerTest.class.getDeclaredMethod("getIntValue"), JsonValueSerializer.class),
                null);

        Object bean = new Object();
        String result = jsonValueSerializer.serialize(bean, null, serializerProvider);

        verify(intSerializer, times(1)).serialize(anyInt(), eq(serializerProvider), any(TypeSerializer.class));
    }

    // Test methods with contextual serializer
    @Test
    public void testCreateContextualWithExplicitSerializer() throws Exception {
        JsonSerializer<Object> jsonObjectSerializer = mock(JsonSerializer.class);

        when(intSerializer.serialize(anyInt(), eq(serializerProvider), any(TypeSerializer.class)))
                .thenReturn("123");

        when(jsonObjectSerializer.serialize(any(), eq(serializerProvider)))
                .thenReturn("{\"name\": \"jsonObjectSerializer\"}");

        JsonValueSerializer jsonValueSerializer = new JsonValueSerializer(
                new AnnotatedMethod(JsonValueSerializerTest.class.getDeclaredMethod("getIntValue"), JsonValueSerializer.class),
                intSerializer);

        JsonSerializer<Object> contextualSerializer = jsonValueSerializer.createContextual(serializerProvider, null);
        assertEquals(JsonSerializer.class, contextualSerializer.getClass());
        assertEquals(jsonObjectSerializer, contextualSerializer);
    }

    @Test(expected = JsonMappingException.class)
    public void testCreateContextualWithNullSerializer() throws Exception {
        JsonValueSerializer jsonValueSerializer = new JsonValueSerializer(
                new AnnotatedMethod(JsonValueSerializerTest.class.getDeclaredMethod("getIntValue"), JsonValueSerializer.class),
                null);

        jsonValueSerializer.createContextual(serializerProvider, null);
    }

    @Test
    public void testCreateContextualWithNullProperty() throws Exception {
        JsonSerializer<Object> jsonObjectSerializer = mock(JsonSerializer.class);

        when(intSerializer.serialize(anyInt(), eq(serializerProvider), any(TypeSerializer.class)))
                .thenReturn("123");

        JsonValueSerializer jsonValueSerializer = new JsonValueSerializer(
                new AnnotatedMethod(JsonValueSerializerTest.class.getDeclaredMethod("getIntValue"), JsonValueSerializer.class),
                intSerializer);

        JsonSerializer<Object> contextualSerializer = jsonValueSerializer.createContextual(serializerProvider, null);
        assertEquals(JsonSerializer.class, contextualSerializer.getClass());
        assertEquals(jsonObjectSerializer, contextualSerializer);

        when(intSerializer.serialize(anyInt(), eq(serializerProvider), any(TypeSerializer.class)))
                .thenThrow(new Exception());

        jsonValueSerializer = new JsonValueSerializer(null, intSerializer);

        contextualSerializer = jsonValueSerializer.createContextual(serializerProvider, null);
        assertEquals(JsonSerializer.class, contextualSerializer.getClass());
        assertEquals(jsonObjectSerializer, contextualSerializer);
    }

    @Test
    public void testSerializeWithType() throws IOException {
        when(intSerializer.serializeWithType(any(), eq(serializerProvider), any(TypeSerializer.class)))
                .thenReturn("{\"type\":\"int\", \"value\":123}");

        JsonValueSerializer jsonValueSerializer = new JsonValueSerializer(
                new AnnotatedMethod(JsonValueSerializerTest.class.getDeclaredMethod("getIntValue"), JsonValueSerializer.class),
                intSerializer);

        Object bean = new Object();
        String result = jsonValueSerializer.serializeWithType(bean, null, serializerProvider, typeSerializer);
        assertEquals("{\"type\":\"int\", \"value\":123}", result);
    }

    // Test edge cases and exceptions
    @Test
    public void testSerializeNullValue() throws IOException {
        when(intSerializer.serialize(null, eq(serializerProvider), any(TypeSerializer.class)))
                .thenReturn("null");

        JsonValueSerializer jsonValueSerializer = new JsonValueSerializer(
                new AnnotatedMethod(JsonValueSerializerTest.class.getDeclaredMethod("getIntValue"), JsonValueSerializer.class),
                intSerializer);

        Object bean = new Object();
        String result = jsonValueSerializer.serialize(bean, null, serializerProvider);
        assertEquals("null", result);
    }

    @Test(expected = InvocationTargetException.class)
    public void testSerializeException() throws IOException, InvocationTargetException {
        when(intSerializer.serialize(anyInt(), eq(serializerProvider), any(TypeSerializer.class)))
                .thenThrow(new IllegalArgumentException("No valid int value."));

        JsonValueSerializer jsonValueSerializer = new JsonValueSerializer(
                new AnnotatedMethod(JsonValueSerializerTest.class.getDeclaredMethod("getIntValue"), JsonValueSerializer.class),
                intSerializer);

        Object bean = new Object();
        jsonValueSerializer.serialize(bean, null, serializerProvider);
    }

    @Test
    public void testSerializeWithIgnoreNullValues() throws IOException {
        JsonInclude.Include include = JsonInclude.Include.NON_NULL;
        objectMapper.setSerializationInclusion(include);

        when(intSerializer.serialize(anyInt(), eq(serializerProvider), any(TypeSerializer.class)))
                .thenReturn("123");

        JsonValueSerializer jsonValueSerializer = new JsonValueSerializer(
                new AnnotatedMethod(JsonValueSerializerTest.class.getDeclaredMethod("getIntValue"), JsonValueSerializer.class),
                intSerializer);

        Object bean = new Object() {
            public Integer getIntValue() {
                return (Integer) (Math.random() < 0.5 ? null : 123);
            }
        };
        String result = objectMapper.writeValueAsString(bean);
        assertNull(result);
    }

    @Test
    public void testAcceptJsonFormatVisitor() throws IOException {
        when(intSerializer.acceptJsonFormatVisitor(any(), any()))
                .thenReturn(null);

        JsonValueSerializer jsonValueSerializer = new JsonValueSerializer(
                new AnnotatedMethod(JsonValueSerializerTest.class.getDeclaredMethod("getIntValue"), JsonValueSerializer.class),
                intSerializer);

        jsonValueSerializer.acceptJsonFormatVisitor(serializerProvider, null);

        verify(intSerializer, times(1)).acceptJsonFormatVisitor(any(), any());
    }

    // Helper method to retrieve int value
    private int getIntValue() {
        return 123;
    }
}
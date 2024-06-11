package com.example.junit; // replace with your test package

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonProperty;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.util.ClassUtil;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertInstanceOf;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.*;

public class SettableAnyPropertyTest {

    private ObjectMapper objectMapper;
    private SettableAnyProperty settableAnyProperty;

    @Before
    public void setup() {
        objectMapper = new ObjectMapper();
        objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);

        AnnotatedField setter = mock(AnnotatedField.class);
        when(setter.getValue(any())).thenReturn(Map.of());

        BeanProperty property = mock(BeanProperty.class);
        AnnotatedMember annotatedMember = mock(AnnotatedMember.class);
        when(annotatedMember.getName()).thenReturn("testProperty");
        when(annotatedMember.getDeclaringClass()).thenReturn(TestClass.class);
        when(property.getMember()).thenReturn(setter);

        settableAnyProperty = new SettableAnyProperty(property, setter, JsonType.STRONG_TYPING, null, null, null);
    }

    @Test
    public void testSettableAnyPropertyConstructor() {
        assertEquals("SettableAnyProperty", settableAnyProperty.getClass().getName());
        assertEquals(property, settableAnyProperty.getProperty());
        assertFalse(settableAnyProperty.hasValueDeserializer());
        assertNull(settableAnyProperty.getType());
    }

    @Test
    public void testSettableAnyPropertyWithValueDeserializer() {
        JsonDeserializer<Object> valueDeserializer = mock(JsonDeserializer.class);
        TypeDeserializer typeDeserializer = mock(TypeDeserializer.class);

        settableAnyProperty = new SettableAnyProperty(property, setter, JsonType.STRONG_TYPING, null, valueDeserializer, typeDeserializer);

        assertEquals(valueDeserializer, settableAnyProperty.getValueDeserializer());
        assertSame(typeDeserializer, settableAnyProperty.getValueTypeDeserializer());
    }

    @Test
    public void testWithValueDeserializer() {
        JsonDeserializer<Object> deser = mock(JsonDeserializer.class);

        SettableAnyProperty original = settableAnyProperty;
        SettableAnyProperty newInstance = settableAnyProperty.withValueDeserializer(deser);

        assertNotSame(original, newInstance);
        assertEquals(property, newInstance.getProperty());
        assertEquals(setter, newInstance._setter);
        assertEquals(JsonType.STRONG_TYPING, newInstance._type);
        assertEquals(null, newInstance._keyDeserializer);
        assertNull(newInstance._valueDeserializer);
        assertNull(newInstance._valueTypeDeserializer);
        assertEquals(deser, newInstance._valueDeserializer);
        assertNull(newInstance._keyDeserializer);
    }

    @Test
    public void testFixAccess() {
        settableAnyProperty.fixAccess(mock(DeserializationContext.class));
        verify(setter).fixAccess(true);
    }

    @Test
    public void testDeserializeAndSet() throws IOException {
        String jsonString = "{\"key\":\"value\"}";
        JsonParser jsonParser = objectMapper.getFactory().createParser(jsonString);

        Object instance = new TestClass();

        Object key = "key";
        Object value = "value";

        settableAnyProperty.deserializeAndSet(jsonParser, mock(DeserializationContext.class), instance, key);

        Map<Object, Object> map = (Map<Object, Object>) setter.getValue(instance);
        assertEquals("value", map.get(key));
    }

    @Test(expected = JsonMappingException.class)
    public void testDeserializeAndSetWithException() throws IOException {
        String jsonString = "{\"key\":\"value\"}";
        JsonParser jsonParser = objectMapper.getFactory().createParser(jsonString);

        Object instance = new TestClass();

        doThrow(new IllegalArgumentException("Test exception"))
                .when(setter)
                .callOnWith(instance, "test", "value");

        settableAnyProperty.deserializeAndSet(jsonParser, mock(DeserializationContext.class), instance, "test");
    }

    @Test
    public void testDeserialize() throws IOException {
        String jsonString = "\"value\"";
        JsonParser jsonParser = objectMapper.getFactory().createParser(jsonString);

        Object result = settableAnyProperty.deserialize(jsonParser, mock(DeserializationContext.class));

        assertEquals("value", result);
    }

    @Test
    public void testSet() throws IOException {
        TestClass testClass = new TestClass();
        Object propName = "testProperty";

        settableAnyProperty.set(testClass, propName, "value");

        Map<Object, Object> map = (Map<Object, Object>) setter.getValue(testClass);
        assertEquals("value", map.get(propName));
    }

    @Test(expected = JsonMappingException.class)
    public void testSetWithException() throws IOException {
        TestClass testClass = new TestClass();
        Object propName = "testProperty";

        doThrow(new IllegalArgumentException("Test exception"))
                .when(setter)
                .callOnWith(testClass, propName, "value");

        settableAnyProperty.set(testClass, propName, "value");
    }

    private static class TestClass {
        @JsonProperty("testProperty")
        @JsonDeserialize
        private Map<Object, Object> testProperty;
    }

    private static class JsonType {
        public static final int STRONG_TYPING = 0;
    }
}
package com.fasterxml.jackson.databind.deser;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.BeanDescription;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.KeyDeserializer;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;

public class SettableAnyPropertyTest {

    @Test
    public void testSettableAnyProperty() throws IOException {
        JavaType javaType = TypeFactory.defaultInstance().constructType(TestClass.class);
        ObjectMapper objectMapper = new ObjectMapper();

        AnnotatedField<JsonProperty> annotatedField = mock(AnnotatedField.class);
        when(annotatedField.getValue(Mockito.any())).thenReturn(Map.of());
        BeanProperty beanProperty = mock(BeanProperty.class);
        when(beanProperty.getName()).thenReturn("testProperty");
        when(beanProperty.getMember()).thenReturn(annotatedField);
        when(beanProperty.getType()).thenReturn(JavaType.constructParametricType(Map.class, String.class, Object.class));

        AnnotatedMember annotatedMember = mock(AnnotatedMethod.class);
        when(annotatedMember.getName()).thenReturn("setTestProperty");
        when(annotatedMember.getDeclaringClass()).thenReturn(TestClass.class);

        SettableAnyProperty settableAnyProperty = new SettableAnyProperty(beanProperty, annotatedMember, javaType, null, null, null);

        assertEquals("testProperty", settableAnyProperty.getProperty().getName());
        assertTrue(settableAnyProperty._setterIsField);
        assertEquals(javaType, settableAnyProperty.getType());
        assertNull(settableAnyProperty._valueDeserializer);
        assertNull(settableAnyProperty._valueTypeDeserializer);
        assertNull(settableAnyProperty._keyDeserializer);
    }

    @Test
    public void testSettableAnyPropertyWithValueDeserializer() throws IOException {
        JavaType javaType = TypeFactory.defaultInstance().constructType(TestClass.class);
        ObjectMapper objectMapper = new ObjectMapper();

        AnnotatedField<JsonProperty> annotatedField = mock(AnnotatedField.class);
        when(annotatedField.getValue(Mockito.any())).thenReturn(Map.of());
        BeanProperty beanProperty = mock(BeanProperty.class);
        when(beanProperty.getName()).thenReturn("testProperty");
        when(beanProperty.getMember()).thenReturn(annotatedField);
        when(beanProperty.getType()).thenReturn(JavaType.constructParametricType(Map.class, String.class, Object.class));

        AnnotatedMember annotatedMember = mock(AnnotatedMethod.class);
        when(annotatedMember.getName()).thenReturn("setTestProperty");
        when(annotatedMember.getDeclaringClass()).thenReturn(TestClass.class);

        JsonDeserializer<Object> valueDeserializer = mock(JsonDeserializer.class);
        TypeDeserializer valueTypeDeserializer = mock(TypeDeserializer.class);

        SettableAnyProperty settableAnyProperty = new SettableAnyProperty(beanProperty, annotatedMember, javaType, null, valueDeserializer, valueTypeDeserializer);

        assertEquals("testProperty", settableAnyProperty.getProperty().getName());
        assertTrue(settableAnyProperty._setterIsField);
        assertEquals(javaType, settableAnyProperty.getType());
        assertEquals(valueDeserializer, settableAnyProperty._valueDeserializer);
        assertEquals(valueTypeDeserializer, settableAnyProperty._valueTypeDeserializer);
        assertNull(settableAnyProperty._keyDeserializer);
    }

    @Test
    public void testSettableAnyPropertyWithKeyDeserializer() throws IOException {
        JavaType javaType = TypeFactory.defaultInstance().constructType(TestClass.class);
        ObjectMapper objectMapper = new ObjectMapper();

        AnnotatedField<JsonProperty> annotatedField = mock(AnnotatedField.class);
        when(annotatedField.getValue(Mockito.any())).thenReturn(Map.of());
        BeanProperty beanProperty = mock(BeanProperty.class);
        when(beanProperty.getName()).thenReturn("testProperty");
        when(beanProperty.getMember()).thenReturn(annotatedField);
        when(beanProperty.getType()).thenReturn(JavaType.constructParametricType(Map.class, String.class, Object.class));

        AnnotatedMember annotatedMember = mock(AnnotatedMethod.class);
        when(annotatedMember.getName()).thenReturn("setTestProperty");
        when(annotatedMember.getDeclaringClass()).thenReturn(TestClass.class);

        KeyDeserializer keyDeserializer = mock(KeyDeserializer.class);

        SettableAnyProperty settableAnyProperty = new SettableAnyProperty(beanProperty, annotatedMember, javaType, keyDeserializer, null, null);

        assertEquals("testProperty", settableAnyProperty.getProperty().getName());
        assertTrue(settableAnyProperty._setterIsField);
        assertEquals(javaType, settableAnyProperty.getType());
        assertNull(settableAnyProperty._valueDeserializer);
        assertNull(settableAnyProperty._valueTypeDeserializer);
        assertEquals(keyDeserializer, settableAnyProperty._keyDeserializer);
    }

    @Test
    public void testDeserializeAndSet() throws IOException {
        JavaType javaType = TypeFactory.defaultInstance().constructType(TestClass.class);
        ObjectMapper objectMapper = new ObjectMapper();

        AnnotatedField<JsonProperty> annotatedField = mock(AnnotatedField.class);
        when(annotatedField.getValue(Mockito.any())).thenReturn(Map.of());
        BeanProperty beanProperty = mock(BeanProperty.class);
        when(beanProperty.getName()).thenReturn("testProperty");
        when(beanProperty.getMember()).thenReturn(annotatedField);
        when(beanProperty.getType()).thenReturn(JavaType.constructParametricType(Map.class, String.class, Object.class));

        AnnotatedMember annotatedMember = mock(AnnotatedMethod.class);
        when(annotatedMember.getName()).thenReturn("setTestProperty");
        when(annotatedMember.getDeclaringClass()).thenReturn(TestClass.class);

        JsonDeserializer<Object> valueDeserializer = mock(JsonDeserializer.class);
        TypeDeserializer valueTypeDeserializer = mock(TypeDeserializer.class);
        KeyDeserializer keyDeserializer = mock(KeyDeserializer.class);

        SettableAnyProperty settableAnyProperty = new SettableAnyProperty(beanProperty, annotatedMember, javaType, keyDeserializer, valueDeserializer, valueTypeDeserializer);

        ObjectMapper objectMapper2 = new ObjectMapper();
        TestClass testClass = new TestClass();

        String propertyName = "testProperty";
        Object value = "testValue";

        JsonParser jsonParser = objectMapper2.getFactory().createParser("{\"testProperty\":\"testValue\"}");

        settableAnyProperty.deserializeAndSet(jsonParser, mock(DeserializationContext.class), testClass, propertyName);

        Map<Object, Object> map = (Map<Object, Object>) annotatedField.getValue(testClass);
        assertEquals(value, map.get(propertyName));
    }

    @Test
    public void testDeserialize() throws IOException {
        JavaType javaType = TypeFactory.defaultInstance().constructType(TestClass.class);
        ObjectMapper objectMapper = new ObjectMapper();

        AnnotatedField<JsonProperty> annotatedField = mock(AnnotatedField.class);
        when(annotatedField.getValue(Mockito.any())).thenReturn(Map.of());
        BeanProperty beanProperty = mock(BeanProperty.class);
        when(beanProperty.getName()).thenReturn("testProperty");
        when(beanProperty.getMember()).thenReturn(annotatedField);
        when(beanProperty.getType()).thenReturn(JavaType.constructParametricType(Map.class, String.class, Object.class));

        AnnotatedMember annotatedMember = mock(AnnotatedMethod.class);
        when(annotatedMember.getName()).thenReturn("setTestProperty");
        when(annotatedMember.getDeclaringClass()).thenReturn(TestClass.class);

        JsonDeserializer<Object> valueDeserializer = mock(JsonDeserializer.class);
        TypeDeserializer valueTypeDeserializer = mock(TypeDeserializer.class);
        KeyDeserializer keyDeserializer = mock(KeyDeserializer.class);

        SettableAnyProperty settableAnyProperty = new SettableAnyProperty(beanProperty, annotatedMember, javaType, keyDeserializer, valueDeserializer, valueTypeDeserializer);

        ObjectMapper objectMapper2 = new ObjectMapper();
        TestClass testClass = new TestClass();

        String propertyName = "testProperty";
        Object value = "testValue";

        JsonParser jsonParser = objectMapper2.getFactory().createParser("{\"testProperty\":\"testValue\"}");

        Object deserializedObject = settableAnyProperty.deserialize(jsonParser, mock(DeserializationContext.class));

        assertEquals(value, deserializedObject);
    }

    @Test
    public void testSet() throws IOException {
        JavaType javaType = TypeFactory.defaultInstance().constructType(TestClass.class);
        ObjectMapper objectMapper = new ObjectMapper();

        AnnotatedField<JsonProperty> annotatedField = mock(AnnotatedField.class);
        when(annotatedField.getValue(Mockito.any())).thenReturn(Map.of());
        BeanProperty beanProperty = mock(BeanProperty.class);
        when(beanProperty.getName()).thenReturn("testProperty");
        when(beanProperty.getMember()).thenReturn(annotatedField);
        when(beanProperty.getType()).thenReturn(JavaType.constructParametricType(Map.class, String.class, Object.class));

        AnnotatedMember annotatedMember = mock(AnnotatedMethod.class);
        when(annotatedMember.getName()).thenReturn("setTestProperty");
        when(annotatedMember.getDeclaringClass()).thenReturn(TestClass.class);

        JsonDeserializer<Object> valueDeserializer = mock(JsonDeserializer.class);
        TypeDeserializer valueTypeDeserializer = mock(TypeDeserializer.class);
        KeyDeserializer keyDeserializer = mock(KeyDeserializer.class);

        TestClass testClass = new TestClass();
        String propertyName = "testProperty";
        Object value = "testValue";

        SettableAnyProperty settableAnyProperty = new SettableAnyProperty(beanProperty, annotatedMember, javaType, keyDeserializer, valueDeserializer, valueTypeDeserializer);

        settableAnyProperty.set(testClass, propertyName, value);

        Map<Object, Object> map = (Map<Object, Object>) annotatedField.getValue(testClass);
        assertEquals(value, map.get(propertyName));
    }
}

class TestClass {
    @JsonProperty("testProperty")
    private Map<String, Object> testProperty = Map.of();
}
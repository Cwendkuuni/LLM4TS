package com.example.junit; // replace with your test package name

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.*;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.impl.ReadableObjectId.Referring;
import com.fasterxml.jackson.databind.introspect.AnnotatedField;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.introspect.AnnotatedMethod;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.util.ClassUtil;
import com.fasterxml.jackson.databind.util.JsonMappingException;
import com.fasterxml.jackson.databind.util.TestBase;

import java.io.IOException;
import java.util.Map;

public class SettableAnyPropertyTest extends TestBase {

    @SuppressWarnings("unchecked")
    private SettableAnyProperty createTestee(BeanProperty property, AnnotatedMember setter, JavaType type) {
        KeyDeserializer keyDeser = mock(KeyDeserializer.class);
        JsonDeserializer<Object> valueDeser = mock(JsonDeserializer.class);
        TypeDeserializer typeDeser = mock(TypeDeserializer.class);
        return new SettableAnyProperty(property, setter, type, keyDeser, valueDeser, typeDeser);
    }

    @org.junit.Test
    public void test_deserializeAndSet_NormalCase() throws IOException {
        BeanProperty property = mock(BeanProperty.class);
        AnnotatedMember setter = mock(AnnotatedMember.class);
        JavaType type = createSimpleJavaType(String.class);
        SettableAnyProperty testee = createTestee(property, setter, type);

        Object key = "testKey";
        JsonParser parser = createParser("{\"testKey\":\"testValue\"}");
        DeserializationContext ctxt = createContext();
        Object instance = new Object();
        String propName = "testPropName";

        testee.deserializeAndSet(parser, ctxt, instance, propName);

        Object expectedValue = "testValue";
        verify(setter).callOnWith(instance, propName, expectedValue);
    }

    @org.junit.Test
    public void test_deserializeAndSet_NullValue() throws IOException {
        BeanProperty property = mock(BeanProperty.class);
        AnnotatedMember setter = mock(AnnotatedMember.class);
        JavaType type = createSimpleJavaType(String.class);
        SettableAnyProperty testee = createTestee(property, setter, type);

        Object key = "testKey";
        JsonParser parser = createParser("{\"testKey\":null}");
        DeserializationContext ctxt = createContext();
        Object instance = new Object();
        String propName = "testPropName";

        testee.deserializeAndSet(parser, ctxt, instance, propName);

        assertNull(setter.callOnWith(instance, propName, eq(null)));
    }

    @org.junit.Test
    public void test_deserialize_NormalCase() throws IOException {
        BeanProperty property = mock(BeanProperty.class);
        AnnotatedMember setter = mock(AnnotatedMember.class);
        JavaType type = createSimpleJavaType(String.class);
        SettableAnyProperty testee = createTestee(property, setter, type);

        JsonParser parser = createParser("{\"test\":\"testValue\"}");
        DeserializationContext ctxt = createContext();

        Object value = testee.deserialize(parser, ctxt);

        assertEquals("testValue", value);
    }

    @org.junit.Test(expected = JsonMappingException.class)
    public void test_deserialize_NullValueDeserializer() throws IOException {
        BeanProperty property = mock(BeanProperty.class);
        AnnotatedMember setter = mock(AnnotatedMember.class);
        JavaType type = createSimpleJavaType(String.class);
        SettableAnyProperty testee = createTestee(property, setter, type);

        JsonParser parser = createParser("{\"test\":null}");
        DeserializationContext ctxt = createContext();

        testee.deserialize(parser, ctxt);

        fail("Expected JsonMappingException");
    }

    @org.junit.Test
    public void test_deserialize_TypeDeserializer() throws IOException {
        BeanProperty property = mock(BeanProperty.class);
        AnnotatedMember setter = mock(AnnotatedMember.class);
        JavaType type = createSimpleJavaType(MyCustomClass.class);
        SettableAnyProperty testee = createTestee(property, setter, type);

        JsonParser parser = createParser("{\"test\":42}");
        DeserializationContext ctxt = createContext();
        TypeDeserializer typeDeserializer = mock(TypeDeserializer.class);

        when(typeDeserializer.deserializeTypedObject(anyObject(), eq(parser), any(DeserializationContext.class)))
                .thenReturn(new MyCustomClass());

        testee.setValueTypeDeserializer(typeDeserializer);

        Object value = testee.deserialize(parser, ctxt);

        assertEquals(new MyCustomClass(), value);
    }

    @org.junit.Test
    public void test_set_NormalCase() throws IOException {
        BeanProperty property = mock(BeanProperty.class);
        AnnotatedMember setter = mock(AnnotatedMember.class);
        JavaType type = createSimpleJavaType(String.class);
        SettableAnyProperty testee = createTestee(property, setter, type);

        Object instance = new Object();
        Object key = "testKey";
        Object value = "testValue";

        testee.set(instance, key, value);

        verify(setter).callOnWith(instance, eq(key), eq(value));
    }

    @org.junit.Test(expected = IllegalArgumentException.class)
    public void test_set_InvalidType() throws IOException {
        BeanProperty property = mock(BeanProperty.class);
        AnnotatedMember setter = mock(AnnotatedMember.class);
        JavaType type = createSimpleJavaType(String.class);
        SettableAnyProperty testee = createTestee(property, setter, type);

        Object instance = new Object();
        Object key = "testKey";
        Object value = new Object();

        testee.set(instance, key, value);

        fail("Expected IllegalArgumentException");
    }

    @org.junit.Test
    public void test_toString() {
        BeanProperty property = mock(BeanProperty.class);
        AnnotatedMember setter = mock(AnnotatedMember.class);
        JavaType type = createSimpleJavaType(String.class);
        SettableAnyProperty testee = createTestee(property, setter, type);

        assertEquals("[any property on class Test]", testee.toString());
    }

    @org.junit.Test(expected = JsonMappingException.class)
    public void test_deserializeAndSet_UnexpectedType() throws IOException {
        BeanProperty property = mock(BeanProperty.class);
        AnnotatedMember setter = mock(AnnotatedMember.class);
        JavaType type = createSimpleJavaType(String.class);
        SettableAnyProperty testee = createTestee(property, setter, type);

        Object key = "testKey";
        JsonParser parser = createParser("{\"testKey\":42}");
        DeserializationContext ctxt = createContext();
        Object instance = new Object();
        String propName = "testPropName";

        testee.deserializeAndSet(parser, ctxt, instance, propName);

        fail("Expected JsonMappingException");
    }
}
package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.NullValueProvider;
import com.fasterxml.jackson.databind.deser.SettableBeanProperty;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.introspect.AnnotatedMethod;
import com.fasterxml.jackson.databind.introspect.BeanPropertyDefinition;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.util.Annotations;

public class SetterlessPropertyTest {

    private Object testInstance;

    @SuppressWarnings("unchecked")
    @Before
    public void setup() throws InstantiationException, IllegalAccessException, NoSuchMethodException {
        Class<SetterlessProperty> clazz = SetterlessProperty.class;
        testInstance = clazz.getDeclaredConstructor().newInstance();

        Method getter = clazz.getDeclaredMethod("_getter");
        getter.setAccessible(true);
    }

    @Test
    public void testGetAnnotation() {
        Annotation annotation = new JsonGetter();
        SetterlessProperty property = new SetterlessProperty(null, null, null, null, new AnnotatedMethod(annotation, "testMethod", null));
        assertEquals(annotation, property.getAnnotation(JsonGetter.class));
    }

    @Test
    public void testFixAccess() throws IOException {
        SetterlessProperty property = new SetterlessProperty(null, null, null, null, new AnnotatedMethod(new JsonProperty("test"), null, null));
        property.fixAccess(null);
        assertTrue(property._annotated.isAccessible());
    }

    @Test
    public void testDeserializeAndSet() throws IOException {
        Object value = new Object();
        JsonParser parser = createMockParser(JsonToken.VALUE_STRING, "testValue");
        SetterlessProperty property = new SetterlessProperty(null, null, null, null, new AnnotatedMethod(null, "testSet", new Class[]{Object.class}));
        Object instance = new Object();

        property.deserializeAndSet(parser, null, instance);
        Object getValue = getter.invoke(instance);
        assertEquals(value, getValue);
    }

    @Test
    public void testDeserializeSetAndReturn() throws IOException {
        Object value = new Object();
        JsonParser parser = createMockParser(JsonToken.VALUE_STRING, "testValue");
        SetterlessProperty property = new SetterlessProperty(null, null, null, null, new AnnotatedMethod(null, "testSet", new Class[]{Object.class}));

        Object result = property.deserializeSetAndReturn(parser, null, new Object());
        assertEquals(instance, result);
        Object getValue = getter.invoke(result);
        assertEquals(value, getValue);
    }

    @Test
    public void testSet() throws IOException, InvocationTargetException, IllegalAccessException {
        SetterlessProperty property = new SetterlessProperty(null, null, null, null, new AnnotatedMethod(null, "testSet", new Class[]{Object.class}));
        assertThrows(UnsupportedOperationException.class, () -> property.set(testInstance, new Object()));
    }

    @Test
    public void testSetAndReturn() throws IOException {
        SetterlessProperty property = new SetterlessProperty(null, null, null, null, new AnnotatedMethod(null, "testSet", new Class[]{Object.class}));
        Object result = property.setAndReturn(testInstance, new Object());
        assertEquals(testInstance, result);
    }

    @Test
    public void testNullValue() throws IOException {
        SetterlessProperty property = new SetterlessProperty(null, null, null, null, new AnnotatedMethod(null, "testSet", new Class[]{Object.class}));
        JsonParser parser = createMockParser(JsonToken.VALUE_NULL, null);
        Object instance = new Object();

        property.deserializeAndSet(parser, null, instance);
        Object getValue = getter.invoke(instance);
        assertNull(getValue);
    }

    @Test
    public void testDeserializeWithTypeDeserializer() throws IOException {
        SetterlessProperty property = new SetterlessProperty(null, null, null, null, new AnnotatedMethod(null, "testSet", new Class[]{Object.class}, CustomTypeDeserializer.class));
        JsonParser parser = createMockParser(JsonToken.VALUE_STRING, "testValue");
        Object instance = new Object();

        assertThrows(IllegalStateException.class, () -> property.deserializeAndSet(parser, null, instance));
    }

    @Test
    public void testGetValue() throws IOException, IllegalAccessException {
        SetterlessProperty property = new SetterlessProperty(null, null, null, null, new AnnotatedMethod(null, "testGet", null));
        Object instance = new Object();
        Object value = new Object();
        Method setter = property._getter;
        setter.setAccessible(true);
        setter.invoke(instance, value);

        Object result = property.getValue(instance);
        assertEquals(value, result);
    }

    @Test
    public void testWithName() {
        SetterlessProperty src = new SetterlessProperty(null, null, null, null, new AnnotatedMethod(null, "test", null));
        SetterlessProperty property = src.withName(new PropertyName("newName"));

        assertEquals("newName", property.getName().getRawName());
    }

    @Test
    public void testWithValueDeserializer() {
        SetterlessProperty src = new SetterlessProperty(null, null, null, null, new AnnotatedMethod(null, "test", null));
        SetterlessProperty property = src.withValueDeserializer(new JsonDeserializer<Object>() {});

        assertFalse(property._valueDeserializer.equals(src._valueDeserializer));
    }

    @Test
    public void testWithNullProvider() {
        SetterlessProperty src = new SetterlessProperty(null, null, null, null, new AnnotatedMethod(null, "test", null));
        SetterlessProperty property = src.withNullProvider(new NullValueProvider() {});

        assertFalse(property._nullProvider.equals(src._nullProvider));
    }

    private JsonParser createMockParser(JsonToken token, String text) throws IOException {
        JsonParser parser = mock(JsonParser.class);
        when(parser.getCurrentToken()).thenReturn(token);
        if (token == JsonToken.VALUE_STRING) {
            when(parser.getValueAsString()).thenReturn(text);
        }
        return parser;
    }

    @After
    public void teardown() {
        getter.setAccessible(false);
    }
}
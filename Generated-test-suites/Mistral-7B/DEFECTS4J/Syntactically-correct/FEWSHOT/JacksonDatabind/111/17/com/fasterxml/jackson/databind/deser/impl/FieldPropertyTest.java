package com.fasterxml.jackson.databind.deser.impl;

import static com.fasterxml.jackson.databind.DeserializationTest.createContext;
import static com.fasterxml.jackson.databind.DeserializationTest.createParanser;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

import java.io.IOException;
import java.lang.reflect.Field;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.NullValueProvider;
import com.fasterxml.jackson.databind.deser.SettableBeanProperty;
import com.fasterxml.jackson.databind.introspect.AnnotatedField;
import com.fasterxml.jackson.databind.introspect.BeanPropertyDefinition;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.util.ClassUtil;
import com.fasterxml.jackson.databind.util.Annotations;

public class FieldPropertyTest {

    @SuppressWarnings("unchecked")
    private static final FieldProperty<Object, Object> TEST_CASE = new FieldProperty<Object, Object>(
            new BeanPropertyDefinition(null, "test", Object.class, null, null, null, null),
            ObjectType.instance, null, null, new AnnotatedField(null, new AnnotatedMember(null, "test", null), null, null, null, false, null, null));

    @Test
    public void testGetAnnotation() {
        assertEquals(null, TEST_CASE.getAnnotation(String.class));
    }

    @Test
    public void testGetMember() {
        assertSame(TEST_CASE._annotated, TEST_CASE.getMember());
    }

    @Test
    public void testDeserializeAndSet() throws IOException {
        Object instance = new Object();
        Object value = new Object();

        JsonParser p = createParanser();
        p.nextToken();

        TEST_CASE.deserializeAndSet(p, createContext(), instance);
        assertEquals(value, TEST_CASE._field.get(instance));
    }

    @Test
    public void testDeserializeSetAndReturn() throws IOException {
        Object instance = new Object();
        Object value = new Object();

        JsonParser p = createParanser();
        p.nextToken();

        Object result = TEST_CASE.deserializeSetAndReturn(p, createContext(), instance);
        assertEquals(instance, result);
        assertEquals(value, TEST_CASE._field.get(instance));
    }

    @Test
    public void testSet() throws IOException {
        Object instance = new Object();
        Object value = new Object();

        TEST_CASE.set(instance, value);
        assertEquals(value, TEST_CASE._field.get(instance));
    }

    @Test
    public void testSetAndReturn() throws IOException {
        Object instance = new Object();
        Object value = new Object();

        Object result = TEST_CASE.setAndReturn(instance, value);
        assertEquals(instance, result);
        assertEquals(value, TEST_CASE._field.get(instance));
    }

    @Test
    public void testFixAccess() {
        Object instance = new Object();
        Field testField = new Field("test", Object.class, java.lang.reflect.AccessibleObject.Modifier.PRIVATE);
        TEST_CASE = new FieldProperty<Object, Object>(TEST_CASE, testField);
        TEST_CASE.fixAccess(createContext().getDefaultConfig());
    }

    @Test
    public void testNullProvider() {
        Object instance = new Object();

        NullValueProvider nva = new NullValueProviderDefaultImpl();
        TEST_CASE = new FieldProperty<Object, Object>(TEST_CASE, null, nva);
        TEST_CASE.deserializeAndSet(createParanser(), createContext(), instance);
        assertNull(TEST_FIELD.get(instance));
    }

    @Test
    public void testSkipNulls() throws IOException {
        Object instance = new Object();

        JsonParser p = createParanser();
        p.nextValue("null");

        TEST_CASE = new FieldProperty<Object, Object>(TEST_CASE, null, null,
                new NullValueProviderDefaultImpl(true));
        TEST_CASE.deserializeAndSet(p, createContext(), instance);
        assertNull(TEST_FIELD.get(instance));
    }
}
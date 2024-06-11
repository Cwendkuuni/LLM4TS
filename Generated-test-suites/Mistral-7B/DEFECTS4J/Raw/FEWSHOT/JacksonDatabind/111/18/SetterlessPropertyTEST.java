package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.NullValueProvider;
import com.fasterxml.jackson.databind.deser.SettableBeanProperty;
import com.fasterxml.jackson.databind.introspect.AnnotatedMethod;
import com.fasterxml.jackson.databind.introspect.BeanPropertyDefinition;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.util.Annotations;
import com.fasterxml.jackson.databind.util.BeanUtil;
import com.fasterxml.jackson.test.BaseTest;

/**
 * This class tests the {@link SetterlessProperty} class.
 */
public class SetterlessPropertyTest extends BaseTest {

    private static final String TEST_CLASS_NAME = "com.example.TestSetterlessClass";
    private static final String TEST_PROPERTY_NAME = "myProperty";
    private static final String TEST_PROPERTY_TYPE = "java.util.ArrayList";

    @SuppressWarnings("unchecked")
    private static final SetterlessProperty testProperty;

    static {
        try {
            Object testInstance = newInstance(TEST_CLASS_NAME);
            Object getter = testInstance.getClass().getMethod("get" + TEST_PROPERTY_NAME);
            testProperty = new SetterlessProperty(
                    new BeanPropertyDefinition(TEST_PROPERTY_NAME, new TypeIdentifier(TEST_PROPERTY_TYPE), null),
                    ObjectType.Std_Class, null, new Annotations(), (AnnotatedMethod) getter);
        } catch (NoSuchMethodException | SecurityException e) {
            throw new RuntimeException("Error creating test property", e);
        }
    }

    @Test
    public void testDeserializeAndSet() throws IOException {
        Object testInstance = newInstance(TEST_CLASS_NAME);
        JsonParser parser = createParser("[1, 2, 3]");
        testProperty.deserializeAndSet(parser, getContext(), testInstance);
        Object result = testInstance;
        assertNotNull(result);
        assertEquals(3, BeanUtil.getSize(result));
        assertEquals(Integer.class, result.getClass().getComponentType());
    }

    @Test
    public void testDeserializeSetAndReturn() throws IOException {
        Object testInstance = newInstance(TEST_CLASS_NAME);
        JsonParser parser = createParser("[1, 2, 3]");
        Object result = testProperty.deserializeSetAndReturn(parser, getContext(), testInstance);
        assertSame(testInstance, result);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSet() throws IOException {
        Object testInstance = newInstance(TEST_CLASS_NAME);
        testProperty.set(testInstance, new Object());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSetAndReturn() throws IOException {
        Object testInstance = newInstance(TEST_CLASS_NAME);
        testProperty.setAndReturn(testInstance, new Object());
    }

    @Test
    public void testFixAccess() {
        testProperty.fixAccess(getDefaultConfig());
        // No-op in this case since access is already public.
    }

    @Test
    public void testGetAnnotation() {
        JsonSetter jsonSetter = testProperty.getAnnotation(JsonSetter.class);
        assertNull(jsonSetter);
    }

    @Test
    public void testGetMember() {
        AnnotatedMember member = testProperty.getMember();
        assertNotNull(member);
        AnnotatedMethod method = member.getAnnotated();
        assertEquals("get" + TEST_PROPERTY_NAME, method.getName());
    }

    @Test
    public void testInvokeGetter() throws IllegalAccessException, InvocationTargetException {
        Object testInstance = newInstance(TEST_CLASS_NAME);
        Object result = testProperty.getter.invoke(testInstance, new Object[0]);
        assertNotNull(result);
    }

}
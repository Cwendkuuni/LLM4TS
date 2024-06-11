package com.example.junit;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.lang.reflect.Method;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.NullValueProvider;
import com.fasterxml.jackson.databind.deser.SettableBeanProperty;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.util.Annotations;
import com.fasterxml.jackson.databind.util.BeanUtil;

/**
 * Test class for MethodProperty
 */
public class MethodPropertyTest {

    private static final String CLASS_NAME = MethodProperty.class.getName();

    // List all the public methods including their signatures

    /**
     * Test constructor with given parameters
     *
     * @throws Exception
     */
    @org.junit.Test
    public void testConstructorWithParameters() throws Exception {
        MethodProperty methodProperty = new MethodProperty(null, null, null, null, null);
        assertNull(methodProperty._annotated);
        assertNull(methodProperty._setter);
    }

    /**
     * Test constructor with source
     *
     * @throws Exception
     */
    @org.junit.Test
    public void testConstructorWithSource() throws Exception {
        MethodProperty source = mock(MethodProperty.class);
        MethodProperty methodProperty = new MethodProperty(source, null, null, null, null);
        assertSame(source, methodProperty);
        assertNull(methodProperty._annotated);
        assertNull(methodProperty._setter);
    }

    /**
     * Test constructor with newName
     *
     * @throws Exception
     */
    @org.junit.Test
    public void testConstructorWithNewName() throws Exception {
        MethodProperty methodProperty = new MethodProperty(null, null, null, null, null);
        PropertyName newName = mock(PropertyName.class);
        MethodProperty newMethodProperty = new MethodProperty(methodProperty, newName);
        assertSame(methodProperty, newMethodProperty);
        assertNull(newMethodProperty._annotated);
        assertNull(newMethodProperty._setter);
        assertSame(newName, newMethodProperty.getName());
    }

    /**
     * Test constructor used for JDK Serialization when reading persisted object
     *
     * @throws Exception
     */
    @org.junit.Test
    public void testConstructorUsedForJDKSerializationWhenReadingPersistedObject()
            throws Exception {
        MethodProperty methodProperty = new MethodProperty(null, null);
        Method method = mock(Method.class);
        MethodProperty newMethodProperty = new MethodProperty(methodProperty, method);
        assertSame(methodProperty, newMethodProperty);
        assertSame(method, newMethodProperty._setter);
        assertNull(newMethodProperty._annotated);
    }

    /**
     * Test getter for skipNulls
     */
    @org.junit.Test
    public void testGetterForSkipNulls() {
        MethodProperty methodProperty = new MethodProperty(null, null, null, null, null);
        assertFalse(methodProperty._skipNulls);
    }

    /**
     * Test setter for skipNulls
     *
     * @throws Exception
     */
    @org.junit.Test
    public void testSetterForSkipNulls() throws Exception {
        MethodProperty methodProperty = new MethodProperty(null, null, null, null, null);
        methodProperty.setSkipNulls(true);
        assertTrue(methodProperty._skipNulls);
    }

    /**
     * Test getter for annotated
     */
    @org.junit.Test
    public void testGetterForAnnotated() {
        AnnotatedMethod annotatedMethod = mock(AnnotatedMethod.class);
        MethodProperty methodProperty = new MethodProperty(null, annotatedMethod, null);
        assertSame(annotatedMethod, methodProperty._annotated);
    }

    /**
     * Test setter for annotated
     *
     * @throws Exception
     */
    @org.junit.Test
    public void testSetterForAnnotated() throws Exception {
        AnnotatedMethod annotatedMethod = mock(AnnotatedMethod.class);
        MethodProperty methodProperty = new MethodProperty(null, null);
        methodProperty.setAnnotated(annotatedMethod);
        assertSame(annotatedMethod, methodProperty._annotated);
    }

    /**
     * Test getter for setter
     */
    @org.junit.Test
    public void testGetterForSetter() {
        Method setter = mock(Method.class);
        MethodProperty methodProperty = new MethodProperty(null, null, setter, null, null);
        assertSame(setter, methodProperty._setter);
    }

    /**
     * Test setter for setter
     *
     * @throws Exception
     */
    @org.junit.Test
    public void testSetterForSetter() throws Exception {
        Method setter = mock(Method.class);
        MethodProperty methodProperty = new MethodProperty(null, null);
        methodProperty.setSetter(setter);
        assertSame(setter, methodProperty._setter);
    }

    /**
     * Test method withName
     *
     * @throws Exception
     */
    @org.junit.Test
    public void testMethodWithName() throws Exception {
        MethodProperty methodProperty = new MethodProperty(null, null, null, null, null);
        PropertyName newName = mock(PropertyName.class);
        MethodProperty newMethodProperty = methodProperty.withName(newName);
        assertSame(methodProperty, newMethodProperty);
        assertSame(newName, newMethodProperty.getName());
    }

    /**
     * Test method withValueDeserializer
     *
     * @throws Exception
     */
    @org.junit.Test
    public void testMethodWithValueDeserializer() throws Exception {
        JsonDeserializer<?> valueDeserializer = mock(JsonDeserializer.class);
        MethodProperty methodProperty = new MethodProperty(null, null, null, null, null);
        MethodProperty newMethodProperty = methodProperty.withValueDeserializer(valueDeserializer);
        assertSame(methodProperty, newMethodProperty);
        assertSame(valueDeserializer, newMethodProperty._valueDeserializer);
    }

    /**
     * Test method withNullProvider
     *
     * @throws Exception
     */
    @org.junit.Test
    public void testMethodWithNullProvider() throws Exception {
        NullValueProvider nva = mock(NullValueProvider.class);
        MethodProperty methodProperty = new MethodProperty(null, null, null, null, null);
        MethodProperty newMethodProperty = methodProperty.withNullProvider(nva);
        assertSame(methodProperty, newMethodProperty);
        assertSame(nva, newMethodProperty._nullProvider);
    }

    /**
     * Test method fixAccess
     *
     * @throws Exception
     */
    @org.junit.Test
    public void testMethodFixAccess() throws Exception {
        DeserializationConfig config = mock(DeserializationConfig.class);
        MethodProperty methodProperty = new MethodProperty(null, null, null, null, null);
        methodProperty.fixAccess(config);
        assertTrue(methodProperty._annotated.isAccessible());
    }

    /**
     * Test method getAnnotation
     */
    @org.junit.Test
    public void testMethodGetAnnotation() {
        Class<Annotation> annotations = Annotation.class;
        AnnotatedMethod annotatedMethod = mock(AnnotatedMethod.class);
        when(annotatedMethod.getAnnotation(annotations)).thenReturn(mock(Annotation.class));
        MethodProperty methodProperty = new MethodProperty(annotatedMethod, null, null, null, null);
        assertSame(mock(Annotation.class), methodProperty.getAnnotation(annotations));
    }

    /**
     * Test method getMember
     */
    @org.junit.Test
    public void testMethodGetMember() {
        AnnotatedMethod annotatedMethod = mock(AnnotatedMethod.class);
        MethodProperty methodProperty = new MethodProperty(annotatedMethod, null, null, null, null);
        assertSame(annotatedMethod, methodProperty.getMember());
    }

    /**
     * Test method deserializeAndSet
     *
     * @throws IOException
     */
    @org.junit.Test
    public void testMethodDeserializeAndSet() throws IOException {
        JsonParser jsonParser = mock(JsonParser.class);
        DeserializationContext deserializationContext = mock(DeserializationContext.class);
        Object instance = new Object();
        Object value = new Object();
        MethodProperty methodProperty = new MethodProperty(null, null, null, null, null);
        methodProperty.deserializeAndSet(jsonParser, deserializationContext, instance);
        assertSame(instance, jsonParser.getCurrentName());
        assertSame(value, instance);
    }

    /**
     * Test method deserializeSetAndReturn
     *
     * @throws IOException
     */
    @org.junit.Test
    public void testMethodDeserializeSetAndReturn() throws IOException {
        JsonParser jsonParser = mock(JsonParser.class);
        DeserializationContext deserializationContext = mock(DeserializationContext.class);
        Object instance = new Object();
        Object value = new Object();
        MethodProperty methodProperty = new MethodProperty(null, null, null, null, null);
        Object result = methodProperty.deserializeSetAndReturn(jsonParser, deserializationContext, instance);
        assertSame(instance, jsonParser.getCurrentName());
        assertSame(value, result);
    }

    /**
     * Test method set
     *
     * @throws IOException
     */
    @org.junit.Test
    public void testMethodSet() throws IOException {
        Object instance = new Object();
        Object value = new Object();
        MethodProperty methodProperty = new MethodProperty(null, null, null, null, null);
        methodProperty.set(instance, value);
        assertSame(value, instance);
    }

    /**
     * Test method setAndReturn
     *
     * @throws IOException
     */
    @org.junit.Test
    public void testMethodSetAndReturn() throws IOException {
        Object instance = new Object();
        Object value = new Object();
        MethodProperty methodProperty = new MethodProperty(null, null, null, null, null);
        Object result = methodProperty.setAndReturn(instance, value);
        assertSame(value, result);
        assertSame(value, instance);
    }

    /**
     * Test method readResolve
     */
    @org.junit.Test
    public void testMethodReadResolve() {
        MethodProperty methodProperty = new MethodProperty(null, null);
        MethodProperty newMethodProperty = methodProperty.readResolve();
        assertSame(methodProperty, newMethodProperty);
    }

    /**
     * Test edge case: methodProperty with null input
     *
     * @throws Exception
     */
    @org.junit.Test(expected = IllegalArgumentException.class)
    public void testEdgeCaseMethodPropertyWithNullInput() throws Exception {
        MethodProperty methodProperty = new MethodProperty(null, null);
    }

    /**
     * Test edge case: methodProperty with null annotated
     *
     * @throws Exception
     */
    @org.junit.Test(expected = IllegalArgumentException.class)
    public void testEdgeCaseMethodPropertyWithNullAnnotated() throws Exception {
        MethodProperty methodProperty = new MethodProperty(null, null);
        methodProperty.setAnnotated(null);
    }

    /**
     * Test edge case: methodProperty with null setter
     *
     * @throws Exception
     */
    @org.junit.Test(expected = IllegalArgumentException.class)
    public void testEdgeCaseMethodPropertyWithNullSetter() throws Exception {
        MethodProperty methodProperty = new MethodProperty(null, null);
        methodProperty.setSetter(null);
    }

    /**
     * Test exception handling: IOException when invoking setter
     *
     * @throws Exception
     */
    @org.junit.Test(expected = IOException.class)
    public void testExceptionHandlingIOExceptionWhenInvokingSetter() throws Exception {
        Object instance = new Object();
        MethodProperty methodProperty = new MethodProperty(null, null, null, null, null);
        when(methodProperty._setter.invoke(instance, anyObject())).thenThrow(new IOException());
        methodProperty.set(instance, new Object());
    }
}
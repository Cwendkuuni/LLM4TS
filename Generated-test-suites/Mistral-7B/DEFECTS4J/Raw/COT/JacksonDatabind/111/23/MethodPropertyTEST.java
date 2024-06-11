package com.example.junit.test;

import static org.junit.Assert.*;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.*;
import com.fasterxml.jackson.databind.deser.impl.*;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.introspect.AnnotatedMethod;
import com.fasterxml.jackson.databind.util.Annotations;

import java.io.IOException;
import java.lang.reflect.Method;

/**
 * Test class for MethodProperty
 */
public class MethodPropertyTest {

    /**
     * Test method for {@link MethodProperty#MethodProperty(BeanPropertyDefinition, JavaType, TypeDeserializer, Annotations, AnnotatedMethod)} constructor
     */
    @Test
    public void testMethodPropertyConstructor() throws Exception {
        BeanPropertyDefinition propDef = null;
        JavaType type = null;
        TypeDeserializer typeDeser = null;
        Annotations contextAnnotations = null;
        AnnotatedMethod method = mock(AnnotatedMethod.class);
        MethodProperty methodProperty = new MethodProperty(propDef, type, typeDeser, contextAnnotations, method);

        // Check if instance was initialized correctly
        assertNotNull(methodProperty);
        assertEquals(propDef, methodProperty.getBeanPropertyDefinition());
        assertEquals(type, methodProperty.getType());
        assertEquals(typeDeser, methodProperty.getTypeDeserializer());
        assertEquals(contextAnnotations, methodProperty.getContextAnnotations());
        assertEquals(method, methodProperty.getAnnotated());
        assertEquals(method, methodProperty.getSetter());
        assertFalse(methodProperty.isSkipNulls());
    }

    /**
     * Test method for {@link MethodProperty#MethodProperty(MethodProperty, JsonDeserializer, NullValueProvider)} constructor
     */
    @Test
    public void testMethodPropertyCopyConstructor() throws Exception {
        AnnotatedMethod method = mock(AnnotatedMethod.class);
        MethodProperty srcMethodProperty = mock(MethodProperty.class);
        JsonDeserializer<?> deser = mock(JsonDeserializer.class);
        NullValueProvider nva = mock(NullValueProvider.class);

        MethodProperty methodProperty = new MethodProperty(srcMethodProperty, deser, nva);

        // Check if instance was initialized correctly
        assertNotNull(methodProperty);
        assertEquals(srcMethodProperty.getBeanPropertyDefinition(), methodProperty.getBeanPropertyDefinition());
        assertEquals(srcMethodProperty.getType(), methodProperty.getType());
        assertEquals(srcMethodProperty.getTypeDeserializer(), methodProperty.getTypeDeserializer());
        assertEquals(srcMethodProperty.getContextAnnotations(), methodProperty.getContextAnnotations());
        assertEquals(method, methodProperty.getAnnotated());
        assertEquals(method, methodProperty.getSetter());
        assertFalse(methodProperty.isSkipNulls());
    }

    /**
     * Test method for {@link MethodProperty#MethodProperty(MethodProperty, PropertyName)} constructor
     */
    @Test
    public void testMethodPropertyRenameConstructor() throws Exception {
        AnnotatedMethod method = mock(AnnotatedMethod.class);
        PropertyName newName = mock(PropertyName.class);
        MethodProperty srcMethodProperty = mock(MethodProperty.class);

        MethodProperty methodProperty = new MethodProperty(srcMethodProperty, newName);

        // Check if instance was initialized correctly
        assertNotNull(methodProperty);
        assertEquals(srcMethodProperty.getBeanPropertyDefinition(), methodProperty.getBeanPropertyDefinition());
        assertEquals(srcMethodProperty.getType(), methodProperty.getType());
        assertEquals(srcMethodProperty.getTypeDeserializer(), methodProperty.getTypeDeserializer());
        assertEquals(srcMethodProperty.getContextAnnotations(), methodProperty.getContextAnnotations());
        assertEquals(method, methodProperty.getAnnotated());
        assertEquals(method, methodProperty.getSetter());
        assertFalse(methodProperty.isSkipNulls());
        assertEquals(newName, methodProperty.getName());
    }

    /**
     * Test method for {@link MethodProperty#MethodProperty(MethodProperty, Method)} constructor
     */
    @Test
    public void testMethodPropertyJdkSerializationConstructor() throws Exception {
        AnnotatedMethod method = mock(AnnotatedMethod.class);
        Method methodAnnotation = mock(Method.class);
        MethodProperty srcMethodProperty = mock(MethodProperty.class);

        MethodProperty methodProperty = new MethodProperty(srcMethodProperty, methodAnnotation);

        // Check if instance was initialized correctly
        assertNotNull(methodProperty);
        assertEquals(srcMethodProperty.getBeanPropertyDefinition(), methodProperty.getBeanPropertyDefinition());
        assertEquals(srcMethodProperty.getType(), methodProperty.getType());
        assertEquals(srcMethodProperty.getTypeDeserializer(), methodProperty.getTypeDeserializer());
        assertEquals(srcMethodProperty.getContextAnnotations(), methodProperty.getContextAnnotations());
        assertEquals(methodAnnotation, methodProperty.getAnnotated());
        assertEquals(methodAnnotation, methodProperty.getSetter());
        assertFalse(methodProperty.isSkipNulls());
    }

    /**
     * Test method for {@link MethodProperty#setter} getter
     * @throws Exception
     */
    @Test
    public void testSetterGetter() throws Exception {
        AnnotatedMethod method = mock(AnnotatedMethod.class);
        Method methodAnnotation = mock(Method.class);
        MethodProperty methodProperty = new MethodProperty(null, type, typeDeser, contextAnnotations, method);

        Method expectedSetter = methodAnnotation;
        Method actualSetter = methodProperty.getSetter();

        assertEquals(expectedSetter.getName(), actualSetter.getName());
        assertEquals(expectedSetter, actualSetter);
    }

    /**
     * Test method for {@link MethodProperty#isSkipNulls} getter
     * @throws Exception
     */
    @Test
    public void testIsSkipNulls() throws Exception {
        MethodProperty methodProperty = new MethodProperty(null, type, typeDeser, contextAnnotations, null);

        assertFalse(methodProperty.isSkipNulls());
    }

    /**
     * Test method for {@link MethodProperty#deserializeAndSet(JsonParser, DeserializationContext, Object)}
     * @throws IOException
     */
    @Test
    public void testDeserializeAndSet() throws IOException, Exception {
        AnnotatedMethod method = mock(AnnotatedMethod.class);
        Object instance = new Object();
        MethodProperty methodProperty = new MethodProperty(null, type, typeDeser, contextAnnotations, method);

        JsonParser parser = mock(JsonParser.class);
        when(parser.hasToken(JsonToken.VALUE_NULL)).thenReturn(false);
        when(parser.getValueAsString()).thenReturn("test");

        Object value = new Object();
        when(typeDeser.deserializesFromAnnotation(any(), eq(methodProperty), any(), any(), any()))
                .thenReturn(value);

        when(method.invoke(instance, value)).thenReturn(null);

        methodProperty.deserializeAndSet(parser, mock(DeserializationContext.class), instance);

        verify(instance).getClass();
        verify(method).invoke(instance, value);
    }

    /**
     * Test method for {@link MethodProperty#deserializeAndSet(JsonParser, DeserializationContext, Object)} with null value
     * @throws IOException
     */
    @Test
    public void testDeserializeAndSetWithNullValue() throws IOException, Exception {
        AnnotatedMethod method = mock(AnnotatedMethod.class);
        Object instance = new Object();
        MethodProperty methodProperty = new MethodProperty(null, type, typeDeser, contextAnnotations, method);

        JsonParser parser = mock(JsonParser.class);
        when(parser.hasToken(JsonToken.VALUE_NULL)).thenReturn(true);
        NullValueProvider nullValueProvider = mock(NullValueProvider.class);
        when(nullValueProvider.getNullValue(any(DeserializationContext.class))).thenReturn(null);
        when(methodProperty.getContextAnnotations()).thenReturn(new Annotations());
        when(methodProperty.getContextAnnotations().findAnnotation(Null.class)).thenReturn(nullValueProvider);

        methodProperty.deserializeAndSet(parser, mock(DeserializationContext.class), instance);

        verify(instance).getClass();
        verifyNoInteractions(method);
    }

    /**
     * Test method for {@link MethodProperty#deserializeSetAndReturn(JsonParser, DeserializationContext, Object)}
     * @throws IOException
     */
    @Test
    public void testDeserializeSetAndReturn() throws IOException, Exception {
        AnnotatedMethod method = mock(AnnotatedMethod.class);
        Object instance = new Object();
        Object expectedResult = new Object();
        MethodProperty methodProperty = new MethodProperty(null, type, typeDeser, contextAnnotations, method);

        JsonParser parser = mock(JsonParser.class);
        when(parser.hasToken(JsonToken.VALUE_NULL)).thenReturn(false);
        when(parser.getValueAsString()).thenReturn("test");

        Object value = new Object();
        when(typeDeser.deserializesFromAnnotation(any(), eq(methodProperty), any(), any(), any()))
                .thenReturn(value);

        when(method.invoke(instance, value)).thenReturn(expectedResult);

        Object actualResult = methodProperty.deserializeSetAndReturn(parser, mock(DeserializationContext.class), instance);

        assertEquals(expectedResult, actualResult);
    }

    /**
     * Test method for {@link MethodProperty#deserializeSetAndReturn(JsonParser, DeserializationContext, Object)} with null value
     * @throws IOException
     */
    @Test
    public void testDeserializeSetAndReturnWithNullValue() throws IOException, Exception {
        AnnotatedMethod method = mock(AnnotatedMethod.class);
        Object instance = new Object();
        MethodProperty methodProperty = new MethodProperty(null, type, typeDeser, contextAnnotations, method);

        JsonParser parser = mock(JsonParser.class);
        when(parser.hasToken(JsonToken.VALUE_NULL)).thenReturn(true);
        NullValueProvider nullValueProvider = mock(NullValueProvider.class);
        when(nullValueProvider.getNullValue(any(DeserializationContext.class))).thenReturn(null);
        when(methodProperty.getContextAnnotations()).thenReturn(new Annotations());
        when(methodProperty.getContextAnnotations().findAnnotation(Null.class)).thenReturn(nullValueProvider);

        Object actualResult = methodProperty.deserializeSetAndReturn(parser, mock(DeserializationContext.class), instance);

        assertNull(actualResult);
    }

    /**
     * Test method for {@link MethodProperty#set(Object, Object)}
     * @throws IOException
     */
    @Test
    public void testSet() throws IOException, Exception {
        AnnotatedMethod method = mock(AnnotatedMethod.class);
        Object instance = new Object();
        Object value = new Object();
        MethodProperty methodProperty = new MethodProperty(null, type, typeDeser, contextAnnotations, method);

        when(method.invoke(instance, value)).thenReturn(null);

        methodProperty.set(instance, value);

        verify(instance).getClass();
        verify(method).invoke(instance, value);
    }

    /**
     * Test method for {@link MethodProperty#setAndReturn(Object, Object)}
     * @throws IOException
     */
    @Test
    public void testSetAndReturn() throws IOException, Exception {
        AnnotatedMethod method = mock(AnnotatedMethod.class);
        Object instance = new Object();
        Object expectedResult = new Object();
        MethodProperty methodProperty = new MethodProperty(null, type, typeDeser, contextAnnotations, method);

        when(method.invoke(instance, anyObject())).thenAnswer((InvocationOnMock invocation) -> expectedResult);

        Object actualResult = methodProperty.setAndReturn(instance, new Object());

        assertEquals(expectedResult, actualResult);
    }

    /**
     * Test method for {@link MethodProperty#readResolve()}
     */
    @Test
    public void testReadResolve() throws Exception {
        AnnotatedMethod method = mock(AnnotatedMethod.class);
        Object instance = new Object();
        MethodProperty srcMethodProperty = mock(MethodProperty.class);

        when(srcMethodProperty.getAnnotated()).thenReturn(method);

        MethodProperty methodProperty = new MethodProperty(srcMethodProperty, method);

        assertEquals(method, methodProperty.getAnnotated());
        assertEquals(method, methodProperty.getSetter());
    }
}
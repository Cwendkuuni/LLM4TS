package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.*;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.NullValueProvider;
import com.fasterxml.jackson.databind.deser.SettableBeanProperty;
import com.fasterxml.jackson.databind.introspect.*;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.util.Annotations;
import com.fasterxml.jackson.databind.util.BeanUtil;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

/**
 * Test for {@link MethodProperty} class.
 */
public class MethodPropertyTest {

    @InjectMocks
    private MethodProperty methodProperty;

    @Mock
    private AnnotatedMethod annotatedMethod;

    @Mock
    private Method method;

    @Mock
    private JsonDeserializer<?> jsonDeserializer;

    @Mock
    private NullValueProvider nullValueProvider;

    @Mock
    private TypeDeserializer typeDeserializer;

    @Mock
    private BeanPropertyDefinition beanPropertyDefinition;

    @Mock
    private JavaType javaType;

    @Mock
    private Annotations contextAnnotations;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        methodProperty = new MethodProperty(beanPropertyDefinition, javaType, typeDeserializer, contextAnnotations, annotatedMethod);
    }

    @Test
    public void testConstructor() {
        // Test default constructor
        MethodProperty defaultMethodProperty = new MethodProperty(null, null, null, null, null);
        assertNull(defaultMethodProperty._annotated);
        assertNull(defaultMethodProperty._setter);
        assertFalse(defaultMethodProperty._skipNulls);

        // Test constructor with arguments
        MethodProperty methodPropertyWithArgs = new MethodProperty(beanPropertyDefinition, javaType, typeDeserializer, contextAnnotations, annotatedMethod);
        assertEquals(beanPropertyDefinition, methodPropertyWithArgs.getBeanPropertyDefinition());
        assertEquals(javaType, methodPropertyWithArgs.getType());
        assertEquals(typeDeserializer, methodPropertyWithArgs.getTypeDeserializer());
        assertEquals(contextAnnotations, methodPropertyWithArgs.getContextAnnotations());
        assertEquals(annotatedMethod, methodPropertyWithArgs.getAnnotated());
        assertEquals(annotatedMethod.getAnnotated(), methodPropertyWithArgs.getSetter());
        assertFalse(methodPropertyWithArgs._skipNulls);
    }

    @Test
    public void testWithName() {
        PropertyName newName = PropertyName.construct("testProperty");
        SettableBeanProperty result = methodProperty.withName(newName);
        assertEquals(newName, result.getName());
        assertEquals(methodProperty, result);
    }

    @Test
    public void testWithValueDeserializer() {
        JsonDeserializer<?> deserializer = new JsonDeserializer<Object>() {};
        SettableBeanProperty result = methodProperty.withValueDeserializer(deserializer);
        assertEquals(deserializer, result.getValueDeserializer());
        assertEquals(methodProperty, result);
    }

    @Test
    public void testWithNullProvider() {
        NullValueProvider nullValueProvider = new NullValueProvider() {};
        SettableBeanProperty result = methodProperty.withNullProvider(nullValueProvider);
        assertEquals(nullValueProvider, result.getNullProvider());
        assertEquals(methodProperty, result);
    }

    @Test
    public void testFixAccess() {
        methodProperty.fixAccess(new DeserializationConfig());
        verify(annotatedMethod).fixAccess(true);
    }

    @Test
    public void testGetAnnotation() {
        Annotation annotation = new Annotation() {};
        when(annotatedMethod.getAnnotation(Annotation.class)).thenReturn(annotation);
        assertEquals(annotation, methodProperty.getAnnotation(Annotation.class));
    }

    @Test
    public void testGetMember() {
        assertEquals(annotatedMethod, methodProperty.getMember());
    }

    @Test
    public void testDeserializeAndSet() throws Exception {
        Object instance = new Object() {};
        JsonParser jsonParser = createMock(JsonParser.class);
        DeserializationContext deserializationContext = createMock(DeserializationContext.class);

        when(jsonParser.getValueAsToken()).thenReturn(JsonToken.VALUE_STRING);
        when(jsonParser.getText()).thenReturn("testValue");

        when(deserializationContext.findBeanProperty(eq(beanPropertyDefinition))).thenReturn(methodProperty);

        methodProperty.deserializeAndSet(jsonParser, deserializationContext, instance);

        verify(instance).getClass();
        verify(instance).getClass().getMethod("setTestProperty", String.class).invoke(instance, "testValue");
    }

    @Test
    public void testDeserializeSetAndReturn() throws Exception {
        Object instance = new Object() {};
        JsonParser jsonParser = createMock(JsonParser.class);
        DeserializationContext deserializationContext = createMock(DeserializationContext.class);

        when(jsonParser.getValueAsToken()).thenReturn(JsonToken.VALUE_STRING);
        when(jsonParser.getText()).thenReturn("testValue");

        when(deserializationContext.findBeanProperty(eq(beanPropertyDefinition))).thenReturn(methodProperty);

        Object result = methodProperty.deserializeSetAndReturn(jsonParser, deserializationContext, instance);

        verify(instance).getClass();
        verify(instance).getClass().getMethod("setTestProperty", String.class).invoke(instance, "testValue");
        assertEquals(instance, result);
    }

    @Test
    public void testSet() throws Exception {
        Object instance = new Object() {};
        Object value = new Object() {};

        methodProperty.set(instance, value);

        verify(instance).getClass().getMethod("setTestProperty", Object.class).invoke(instance, value);
    }

    @Test
    public void testSetAndReturn() throws Exception {
        Object instance = new Object() {};
        Object value = new Object() {};

        Object result = methodProperty.setAndReturn(instance, value);

        verify(instance).getClass().getMethod("setTestProperty", Object.class).invoke(instance, value);
        assertEquals(instance, result);
    }

    @Test
    public void testReadResolve() throws Exception {
        Object result = methodProperty.readResolve();

        MethodProperty newMethodProperty = new MethodProperty(methodProperty, method);
        assertEquals(newMethodProperty, result);
    }
}
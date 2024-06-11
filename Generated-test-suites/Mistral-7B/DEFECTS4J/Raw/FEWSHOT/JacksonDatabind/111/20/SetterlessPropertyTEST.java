package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

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
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

/**
 * This class tests the methods of SetterlessProperty class.
 */
public class SetterlessPropertyTest {

    @Mock
    private JsonDeserializer<?> _valueDeserializer;
    @Mock
    private NullValueProvider _nullProvider;
    @Mock
    private TypeDeserializer _valueTypeDeserializer;
    @Mock
    private JsonParser _jsonParser;
    @Mock
    private DeserializationContext _deserializationContext;
    @Mock
    private Object _instance;
    @Mock
    private Object _value;
    @Mock
    private BeanPropertyDefinition _beanPropertyDefinition;
    @Mock
    private Annotations _contextAnnotations;
    @Mock
    private AnnotatedMethod _annotatedMethod;
    @Mock
    private Method _getter;

    private SetterlessProperty setterlessProperty;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        when(_getter.getName()).thenReturn("getPropertyName");
        when(_getter.getAnnotated()).thenReturn(_annotatedMethod);
        setterlessProperty = new SetterlessProperty(
                _beanPropertyDefinition, mock(JavaType.class), _valueTypeDeserializer, _contextAnnotations, _annotatedMethod);
    }

    @Test
    public void testGetAnnotation() {
        Annotation annotation = mock(Annotation.class);
        when(_annotatedMethod.getAnnotation(Annotation.class)).thenReturn(annotation);
        assertEquals(annotation, setterlessProperty.getAnnotation(Annotation.class));
    }

    @Test
    public void testGetMember() {
        assertEquals(_annotatedMethod, setterlessProperty.getMember());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSet() throws IOException {
        setterlessProperty.set(_instance, _value);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSetAndReturn() throws IOException {
        setterlessProperty.setAndReturn(_instance, _value);
    }

    @Test
    public void testDeserializeAndSet() throws IOException {
        when(_jsonParser.getCurrentToken()).thenReturn(JsonToken.VALUE_STRING);
        when(_jsonParser.getValueAsString()).thenReturn("testValue");
        when(_getter.invoke(_instance, (Object[]) null)).thenReturn(_value);

        setterlessProperty.deserializeAndSet(_jsonParser, _deserializationContext, _instance);

        verify(_valueDeserializer).deserialize(_jsonParser, _deserializationContext, _value);
    }

    @Test
    public void testDeserializeSetAndReturn() throws IOException {
        when(_jsonParser.getCurrentToken()).thenReturn(JsonToken.VALUE_STRING);
        when(_jsonParser.getValueAsString()).thenReturn("testValue");

        Object result = setterlessProperty.deserializeSetAndReturn(_jsonParser, _deserializationContext, _instance);

        verify(_instance, times(2)).equals(_instance);
        verify(_valueDeserializer).deserialize(_jsonParser, _deserializationContext, _value);
        assertEquals(_instance, result);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testFixAccess() throws IOException {
        setterlessProperty.fixAccess(_deserializationContext);
    }
}
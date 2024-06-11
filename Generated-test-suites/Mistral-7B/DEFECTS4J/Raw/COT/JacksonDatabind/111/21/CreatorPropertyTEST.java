package com.example.junit;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.*;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.util.Annotations;
import com.fasterxml.jackson.databind.util.ClassUtil;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class CreatorPropertyTest {

    @Mock
    private AnnotatedParameter _annotated;

    @Mock
    private JavaType _type;

    @Mock
    private PropertyName _wrapperName;

    @Mock
    private TypeDeserializer _typeDeser;

    @Mock
    private Annotations _contextAnnotations;

    @Mock
    private JsonParser _parser;

    @Mock
    private DeserializationContext _context;

    @Mock
    private Object _beanInstance;

    @InjectMocks
    private CreatorProperty _creatorProperty;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testCreatorPropertyConstructor() throws Exception {
        _creatorProperty = new CreatorProperty( PropertyName.construct("testName"), _type, _wrapperName, _typeDeser, _contextAnnotations, _annotated, 0, null, null);
        assertNotNull(_creatorProperty);
    }

    @Test(expected = NullPointerException.class)
    public void testCreatorPropertyConstructorNullType() throws Exception {
        _creatorProperty = new CreatorProperty( PropertyName.construct("testName"), null, _wrapperName, _typeDeser, _contextAnnotations, _annotated, 0, null, null);
    }

    @Test
    public void testSetFallbackSetter() throws Exception {
        SettableBeanProperty fallbackSetter = mock(SettableBeanProperty.class);
        _creatorProperty.setFallbackSetter(fallbackSetter);
        assertNotNull(_creatorProperty._fallbackSetter);
        assertEquals(fallbackSetter, _creatorProperty._fallbackSetter);
    }

    @Test(expected = NullPointerException.class)
    public void testSetFallbackSetterNull() throws Exception {
        _creatorProperty.setFallbackSetter(null);
    }

    @Test
    public void testIsIgnorable() throws Exception {
        _creatorProperty.markAsIgnorable();
        assertTrue(_creatorProperty.isIgnorable());
    }

    @Test
    public void testIsIgnorableNotIgnorable() throws Exception {
        assertFalse(_creatorProperty.isIgnorable());
    }

    @Test
    public void testFindInjectableValue() throws Exception {
        when(_context.findInjectableValue(_injectableValueId, _creatorProperty, _beanInstance)).thenReturn(new Object());
        Object result = _creatorProperty.findInjectableValue(_context, _beanInstance);
        assertNotNull(result);
    }

    @Test(expected = JsonMappingException.class)
    public void testFindInjectableValueNullInjectableValueId() throws Exception {
        _creatorProperty.findInjectableValue(_context, _beanInstance);
    }

    @Test(expected = JsonMappingException.class)
    public void testFindInjectableValueNullContext() throws Exception {
        _creatorProperty.findInjectableValue(null, _beanInstance);
    }

    @Test(expected = JsonMappingException.class)
    public void testFindInjectableValueNullBeanInstance() throws Exception {
        _creatorProperty.findInjectableValue(_context, null);
    }

    @Test
    public void testInject() throws Exception {
        Object value = new Object();
        when(_creatorProperty.findInjectableValue(_context, _beanInstance)).thenReturn(value);
        _creatorProperty.inject(_context, _beanInstance);
        verify(_beanInstance).set(any(), eq(value));
    }

    @Test(expected = IOException.class)
    public void testInjectIOException() throws Exception {
        doThrow(new IOException("Test IO Exception")).when(_creatorProperty).findInjectableValue(_context, _beanInstance);
        _creatorProperty.inject(_context, _beanInstance);
    }

    @Test
    public void testDeserializeAndSet() throws Exception {
        Object value = new Object();
        when(_parser.getValueAsToken()).thenReturn(ContextualDeserializerProvider.instance.findValueDeserializer(JsonToken.VALUE_STRING, getClass()));
        when(_parser.getValueAsString()).thenReturn("Test Value");
        when(_typeDeser.deserialize(_parser, _context, _type)).thenReturn(value);
        when(_fallbackSetter.set(any(), anyObject())).thenReturn(null);
        _creatorProperty.deserializeAndSet(_parser, _context, _beanInstance);
        verify(_fallbackSetter).set(eq(_beanInstance), eq(value));
    }

    @Test(expected = IOException.class)
    public void testDeserializeAndSetIOException() throws Exception {
        when(_parser.getValueAsToken()).thenThrow(new IOException("Test IO Exception"));
        _creatorProperty.deserializeAndSet(_parser, _context, _beanInstance);
    }

    @Test
    public void testDeserializeSetAndReturn() throws Exception {
        Object value = new Object();
        when(_parser.getValueAsToken()).thenReturn(ContextualDeserializerProvider.instance.findValueDeserializer(JsonToken.VALUE_STRING, getClass()));
        when(_parser.getValueAsString()).thenReturn("Test Value");
        when(_typeDeser.deserialize(_parser, _context, _type)).thenReturn(value);
        when(_fallbackSetter.setAndReturn(any(), anyObject())).thenReturn(value);
        Object result = _creatorProperty.deserializeSetAndReturn(_parser, _context, _beanInstance);
        assertNotNull(result);
        assertEquals(value, result);
    }

    @Test(expected = IOException.class)
    public void testDeserializeSetAndReturnIOException() throws Exception {
        when(_parser.getValueAsToken()).thenThrow(new IOException("Test IO Exception"));
        _creatorProperty.deserializeSetAndReturn(_parser, _context, _beanInstance);
    }

    @Test
    public void testSet() throws Exception {
        Object value = new Object();
        when(_fallbackSetter.set(any(), anyObject())).thenReturn(null);
        _creatorProperty.set(_beanInstance, value);
        verify(_fallbackSetter).set(eq(_beanInstance), eq(value));
    }

    @Test(expected = IOException.class)
    public void testSetIOException() throws Exception {
        _creatorProperty.set(_beanInstance, null);
    }

    @Test
    public void testSetAndReturn() throws Exception {
        Object value = new Object();
        when(_fallbackSetter.setAndReturn(any(), anyObject())).thenReturn(value);
        Object result = _creatorProperty.setAndReturn(_beanInstance, value);
        assertNotNull(result);
        assertEquals(value, result);
    }

    @Test(expected = IOException.class)
    public void testSetAndReturnIOException() throws Exception {
        _creatorProperty.setAndReturn(_beanInstance, null);
    }

    @Test
    public void testGetInjectableValueId() {
        Object injectableValueId = new Object();
        when(_creatorProperty.getInjectableValueId()).thenReturn(injectableValueId);
        assertEquals(injectableValueId, _creatorProperty.getInjectableValueId());
    }
}
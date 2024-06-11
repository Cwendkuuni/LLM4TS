package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.assertInstanceOf;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.*;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.*;
import com.fasterxml.jackson.databind.introspect.*;
import com.fasterxml.jackson.databind.node.*;
import com.fasterxml.jackson.databind.util.ClassUtil;
import com.fasterxml.jackson.test.BaseMapTest;

import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;

/**
 * Test for InnerClassProperty class.
 */
public class InnerClassPropertyTest extends BaseMapTest {

    private InnerClassProperty _property;
    private SettableBeanProperty _delegate;
    private Object _instance;
    private JsonParser _parser;
    private DeserializationContext _context;
    private JsonDeserializer<Object> _deserializer;
    private AnnotatedConstructor _constructor;
    private JsonDeserializer<Object> _valueDeserializer;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        _delegate = mock(SettableBeanProperty.class);
        _instance = new Object();
        _parser = mock(JsonParser.class);
        _context = mock(DeserializationContext.class);
        _deserializer = mock(JsonDeserializer.class);
        _constructor = mock(AnnotatedConstructor.class);
        _valueDeserializer = mock(JsonDeserializer.class);

        when(_constructor.getAnnotated()).thenReturn(_annotatedConstructor());

        _property = new InnerClassProperty(_delegate, _constructor);
    }

    @Test
    public void testInnerClassPropertyConstructor() {
        InnerClassProperty property = new InnerClassProperty(_delegate, _constructor);
        assertEquals(_delegate, property._delegate);
        assertNotNull(property._creator);
    }

    @Test
    public void testInnerClassPropertyWithJsonDeserializerConstructor() {
        InnerClassProperty property = new InnerClassProperty(_delegate, _deserializer);
        assertEquals(_delegate, property._delegate);
        assertNull(property._creator);
        assertEquals(_deserializer, property._valueDeserializer);
    }

    @Test
    public void testInnerClassPropertyWithNameConstructor() {
        PropertyName newName = textNode("newName");
        InnerClassProperty property = new InnerClassProperty(_delegate, newName);
        assertEquals(_delegate, property._delegate);
        assertEquals(newName, property._name);
    }

    @Test
    public void testInnerClassPropertyWithJdkSerializationConstructor() {
        InnerClassProperty property = new InnerClassProperty(_delegate, _annotated);
        assertEquals(_delegate, property._delegate);
        assertNotNull(property._creator);
        assertNull(property._valueDeserializer);
    }

    @Test
    public void testGetDelegate() {
        assertEquals(_delegate, _property.getDelegate());
    }

    @Test
    public void testGetCreator() {
        assertEquals(_creator, _property.getCreator());
    }

    @Test
    public void testGetValueDeserializer() {
        assertEquals(_deserializer, _property.getValueDeserializer());
    }

    @Test
    public void testSet() throws IOException {
        Object value = new Object();
        _property.set(_instance, value);
        verify(_delegate).set(_instance, value);
    }

    @Test
    public void testSetAndReturn() throws IOException {
        Object value = new Object();
        Object result = _property.setAndReturn(_instance, value);
        verify(_delegate).set(_instance, value);
        assertEquals(value, result);
    }

    @Test
    public void testDeserializeAndSet() throws IOException {
        Object value = new Object();
        when(_valueDeserializer.deserialize(anyObject(), any(), anyObject())).thenReturn(value);

        _property.deserializeAndSet(_parser, _context, _instance);
        verify(_delegate).set(_instance, value);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDeserializeAndSetWithNullCreator() throws IOException {
        InnerClassProperty property = new InnerClassProperty(_delegate, null);
        property.deserializeAndSet(_parser, _context, _instance);
    }

    @Test
    public void testDeserializeSetAndReturn() throws IOException {
        Object value = new Object();
        Object result = _property.deserializeSetAndReturn(_parser, _context, _instance);
        verify(_delegate).setAndReturn(_instance, value);
        assertEquals(value, result);
    }

    @Test
    public void testReadResolve() {
        InnerClassProperty property = new InnerClassProperty(_delegate, _annotated);
        InnerClassProperty result = property.readResolve();
        assertEquals(property._delegate, result._delegate);
        assertEquals(property._annotated, result._annotated);
    }

    @Test
    public void testWriteReplace() {
        InnerClassProperty property = new InnerClassProperty(_delegate, _annotated);
        Object result = property.writeReplace();
        assertEquals(property, result);

        Object fakeInstance = new Object();
        when(_annotated.getAnnotated()).thenReturn(new AnnotatedConstructor(null, _creator, null, null));
        result = property.writeReplace();
        verify(fakeInstance).InnerClassProperty(any(), any());
    }

    @Test
    public void testDeserializeWithType() throws IOException {
        when(_valueTypeDeserializer.deserialize(anyObject(), any(), anyObject())).thenReturn(new Object());

        _property.deserializeAndSet(_parser, _context, _instance);
        verify(_valueDeserializer).deserializeWithType(_parser, _context, _valueTypeDeserializer);
    }

    @Test
    public void testGetAnnotation() {
        Annotation annotation = mock(Annotation.class);
        when(_delegate.getAnnotation(any())).thenReturn(annotation);
        assertEquals(annotation, _property.getAnnotation(Object.class));
    }

    @Test
    public void testGetMember() {
        AnnotatedMember member = mock(AnnotatedMember.class);
        when(_delegate.getMember()).thenReturn(member);
        assertEquals(member, _property.getMember());
    }

    private AnnotatedConstructor _annotatedConstructor() {
        return mock(AnnotatedConstructor.class);
    }
}
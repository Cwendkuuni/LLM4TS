package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.deser.NullValueProvider;
import com.fasterxml.jackson.databind.deser.SettableBeanProperty;
import com.fasterxml.jackson.databind.deser.UnresolvedForwardReference;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.introspect.ObjectIdInfo;
import com.fasterxml.jackson.databind.node.ObjectNode;

import org.junit.Before;
import org.junit.Test;

public class ObjectIdReferencePropertyTest {

    private ObjectIdReferenceProperty _property;
    private SettableBeanProperty _forward;
    private JsonDeserializer<?> _deserializer;
    private NullValueProvider _nullProvider;
    private ObjectIdInfo _objectIdInfo;
    private ObjectNode _node;
    private JsonParser _parser;
    private DeserializationContext _context;

    @Before
    public void setup() {
        _forward = mock(SettableBeanProperty.class);
        _deserializer = mock(JsonDeserializer.class);
        _nullProvider = mock(NullValueProvider.class);
        _objectIdInfo = mock(ObjectIdInfo.class);
        _node = mock(ObjectNode.class);
        _parser = mock(JsonParser.class);
        _context = mock(DeserializationContext.class);

        _property = new ObjectIdReferenceProperty(_forward, _objectIdInfo);
    }

    @Test
    public void testConstructorWithForward() {
        assertSame(_forward, _property.getForwardProperty());
        assertSame(_objectIdInfo, _property.getObjectIdInfo());
    }

    @Test
    public void testConstructorWithDeserializer() {
        _property = new ObjectIdReferenceProperty(_forward, _deserializer, _nullProvider);
        assertSame(_forward, _property.getForwardProperty());
        assertSame(_deserializer, _property.getValueDeserializer());
        assertSame(_nullProvider, _property.getNullProvider());
    }

    @Test
    public void testConstructorWithNewName() {
        PropertyName newName = mock(PropertyName.class);
        _property = new ObjectIdReferenceProperty(_forward, newName);
        assertSame(_forward, _property.getForwardProperty());
        assertSame(newName, _property.getName());
    }

    @Test
    public void testGetName() {
        PropertyName newName = mock(PropertyName.class);
        _property = new ObjectIdReferenceProperty(_forward, newName);
        assertSame(newName, _property.getName());
    }

    @Test
    public void testGetForwardProperty() {
        assertSame(_forward, _property.getForwardProperty());
    }

    @Test
    public void testGetObjectIdInfo() {
        assertSame(_objectIdInfo, _property.getObjectIdInfo());
    }

    @Test
    public void testGetValueDeserializer() {
        assertSame(_deserializer, _property.getValueDeserializer());
    }

    @Test
    public void testGetNullProvider() {
        assertSame(_nullProvider, _property.getNullProvider());
    }

    @Test
    public void testFixAccess() {
        _property.fixAccess(_context);
        verify(_forward).fixAccess(_context);
    }

    @Test
    public void testGetAnnotation() {
        Annotation annotation = mock(Annotation.class);
        when(_forward.getAnnotation(any(Class.class))).thenReturn(annotation);
        assertSame(annotation, _property.getAnnotation(any(Class.class)));
    }

    @Test
    public void testGetMember() {
        AnnotatedMember member = mock(AnnotatedMember.class);
        when(_forward.getMember()).thenReturn(member);
        assertSame(member, _property.getMember());
    }

    @Test
    public void testGetCreatorIndex() {
        int index = 1;
        when(_forward.getCreatorIndex()).thenReturn(index);
        assertSame(index, _property.getCreatorIndex());
    }

    @Test(expected = UnresolvedForwardReference.class)
    public void testDeserializeAndSetWithUnresolvedReference() throws IOException {
        UnresolvedForwardReference reference = mock(UnresolvedForwardReference.class);
        when(_parser.getValueAsToken()).thenReturn(mock(JsonToken.class));
        when(_context.getInputFactory()).thenReturn(mock(JsonFactory.class));

        _property.deserializeAndSet(_parser, _context, null);
    }

    @Test
    public void testDeserializeAndSetWithNullValue() throws IOException {
        _property.deserializeAndSet(_parser, _context, null);
        assertNull(_property.getBean());
    }

    @Test
    public void testDeserializeAndSetWithValue() throws IOException {
        Object value = new Object();
        when(_parser.getValueAsToken()).thenReturn(mock(JsonToken.class));
        when(_context.getInputFactory()).thenReturn(mock(JsonFactory.class));

        _property.deserializeAndSet(_parser, _context, null);
        verify(_forward).set(any(Object.class), value);
        assertSame(value, _property.getBean());
    }

    @Test
    public void testDeserializeSetAndReturnWithUnresolvedReference() throws IOException {
        UnresolvedForwardReference reference = mock(UnresolvedForwardReference.class);
        PropertyReferring referring = mock(PropertyReferring.class);
        when(_parser.getValueAsToken()).thenReturn(mock(JsonToken.class));
        when(_context.getInputFactory()).thenReturn(mock(JsonFactory.class));

        _property.deserializeSetAndReturn(_parser, _context, null);
        verify(reference).handleResolvedForwardReference(any(Object.class), any(Object.class));
    }

    @Test
    public void testDeserializeSetAndReturnWithNullValue() throws IOException {
        when(_parser.getValueAsToken()).thenReturn(mock(JsonToken.class));
        when(_context.getInputFactory()).thenReturn(mock(JsonFactory.class));

        assertNull(_property.deserializeSetAndReturn(_parser, _context, null));
    }

    @Test
    public void testDeserializeSetAndReturnWithValue() throws IOException {
        Object value = new Object();
        when(_parser.getValueAsToken()).thenReturn(mock(JsonToken.class));
        when(_context.getInputFactory()).thenReturn(mock(JsonFactory.class));

        assertSame(value, _property.deserializeSetAndReturn(_parser, _context, null));
    }

    @Test
    public void testSet() throws IOException {
        Object value = new Object();
        _property.set(null, value);
        verify(_forward).set(any(Object.class), value);
        assertSame(value, _property.getBean());
    }

    @Test
    public void testSetAndReturn() throws IOException {
        Object value = new Object();
        assertSame(value, _property.setAndReturn(null, value));
    }
}
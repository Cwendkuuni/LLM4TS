package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.NullValueProvider;
import com.fasterxml.jackson.databind.deser.SettableBeanProperty;
import com.fasterxml.jackson.databind.deser.impl.FieldProperty;

public class FieldPropertyTest {

    @Mock
    private BeanPropertyDefinition propDef;

    @Mock
    private JavaType type;

    @Mock
    private TypeDeserializer typeDeser;

    @Mock
    private Annotations contextAnnotations;

    @Mock
    private AnnotatedField field;

    private FieldProperty fieldProperty;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        fieldProperty = new FieldProperty(propDef, type, typeDeser, contextAnnotations, field);
    }

    @Test
    public void testConstructor() {
        assertNotNull(fieldProperty);
        assertEquals(propDef, fieldProperty.getPropertyDefinition());
        assertEquals(type, fieldProperty.getType());
        assertEquals(typeDeser, fieldProperty.getTypeDeserializer());
        assertEquals(contextAnnotations, fieldProperty.getContextAnnotations());
        assertEquals(field, fieldProperty.getMember());
        assertEquals(field.getAnnotated(), fieldProperty.getField());
    }

    @Test
    public void testWithDeserializers() {
        JsonDeserializer<?> deserializer = mock(JsonDeserializer.class);
        SettableBeanProperty newProperty = fieldProperty.withValueDeserializer(deserializer);
        assertNotEquals(fieldProperty, newProperty);
        assertEquals(deserializer, newProperty.getValueDeserializer());
    }

    @Test
    public void testWithNullProvider() {
        NullValueProvider nvp = mock(NullValueProvider.class);
        SettableBeanProperty newProperty = fieldProperty.withNullProvider(nvp);
        assertNotEquals(fieldProperty, newProperty);
        assertEquals(nvp, newProperty.getNullProvider());
    }

    @Test
    public void testFixAccess() {
        DeserializationConfig config = mock(DeserializationConfig.class);
        fieldProperty.fixAccess(config);
        // verify field access is checked and fixed properly
        verify(config).isEnabled(MapperFeature.OVERRIDE_PUBLIC_ACCESS_MODIFIERS);
    }

    @Test
    public void testGetAnnotation() {
        Class<A> acls = A.class;
        assertNull(fieldProperty.getAnnotation(acls));
        when(field.getAnnotation(acls)).thenReturn(mock(A.class));
        assertNotNull(fieldProperty.getAnnotation(acls));
    }

    @Test
    public void testDeserializeAndSet() throws IOException {
        JsonParser p = mock(JsonParser.class);
        DeserializationContext ctxt = mock(DeserializationContext.class);
        Object instance = new Object();
        fieldProperty.deserializeAndSet(p, ctxt, instance);
        // verify field.set() is called with the correct arguments
        verify(field).set(instance, value);
    }

    @Test
    public void testDeserializeSetAndReturn() throws IOException {
        JsonParser p = mock(JsonParser.class);
        DeserializationContext ctxt = mock(DeserializationContext.class);
        Object instance = new Object();
        Object result = fieldProperty.deserializeSetAndReturn(p, ctxt, instance);
        // verify field.set() is called with the correct arguments
        verify(field).set(instance, value);
        // verify the result is the same instance
        assertEquals(instance, result);
    }

    @Test
    public void testSet() throws IOException {
        Object instance = new Object();
        Object value = new Object();
        fieldProperty.set(instance, value);
        // verify field.set() is called with the correct arguments
        verify(field).set(instance, value);
    }

    @Test
    public void testSetAndReturn() throws IOException {
        Object instance = new Object();
        Object value = new Object();
        Object result = fieldProperty.setAndReturn(instance, value);
        // verify field.set() is called with the correct arguments
        verify(field).set(instance, value);
        // verify the result is the same instance
        assertEquals(instance, result);
    }
}
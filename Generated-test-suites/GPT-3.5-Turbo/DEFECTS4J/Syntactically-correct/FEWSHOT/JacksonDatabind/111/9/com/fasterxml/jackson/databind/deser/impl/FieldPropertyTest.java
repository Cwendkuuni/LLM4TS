package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;

import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.NullValueProvider;
import com.fasterxml.jackson.databind.deser.SettableBeanProperty;
import com.fasterxml.jackson.databind.introspect.AnnotatedField;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.introspect.BeanPropertyDefinition;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.util.Annotations;
import com.fasterxml.jackson.databind.util.ClassUtil;
import org.junit.Before;
import org.junit.Test;

public class FieldPropertyTest {
    private FieldProperty fieldProperty;
    
    private BeanPropertyDefinition propDef;
    private JavaType type;
    private TypeDeserializer typeDeser;
    private Annotations contextAnnotations;
    private AnnotatedField field;
    private Field annotatedField;
    private boolean skipNulls;

    private JsonDeserializer<Object> valueDeserializer;
    private NullValueProvider nullValueProvider;

    @Before
    public void setUp() throws Exception {
        propDef = mock(BeanPropertyDefinition.class);
        type = mock(JavaType.class);
        typeDeser = mock(TypeDeserializer.class);
        contextAnnotations = mock(Annotations.class);
        field = mock(AnnotatedField.class);
        annotatedField = mock(Field.class);
        skipNulls = true;

        fieldProperty = new FieldProperty(propDef, type, typeDeser, contextAnnotations, field);

        valueDeserializer = mock(JsonDeserializer.class);
        nullValueProvider = mock(NullValueProvider.class);
    }

    @Test
    public void testConstructor() {
        FieldProperty fieldProperty = new FieldProperty(propDef, type, typeDeser, contextAnnotations, field);

        assertSame(propDef, fieldProperty.getMember());
        assertSame(type, fieldProperty.getType());
        assertSame(typeDeser, fieldProperty.getValueTypeDeserializer());
        assertSame(contextAnnotations, fieldProperty.getContextAnnotations());
        assertSame(field, fieldProperty.getMember());

        assertNull(fieldProperty.getValueDeserializer());
        assertNull(fieldProperty.getNullProvider());

        assertTrue(skipNulls);
    }

    @Test
    public void testConstructorWithDeserialierAndNullValueProvider() {
        FieldProperty fieldProperty = new FieldProperty(fieldProperty, valueDeserializer, nullValueProvider);

        assertSame(propDef, fieldProperty.getMember());
        assertSame(type, fieldProperty.getType());
        assertSame(typeDeser, fieldProperty.getValueTypeDeserializer());
        assertSame(contextAnnotations, fieldProperty.getContextAnnotations());
        assertSame(field, fieldProperty.getMember());

        assertSame(valueDeserializer, fieldProperty.getValueDeserializer());
        assertSame(nullValueProvider, fieldProperty.getNullProvider());

        assertTrue(skipNulls);
    }

    @Test
    public void testConstructorWithNewName() {
        PropertyName newName = mock(PropertyName.class);
        FieldProperty fieldProperty = new FieldProperty(fieldProperty, newName);

        assertSame(newName, fieldProperty.getFullName());
        assertSame(propDef, fieldProperty.getMember());
        assertSame(type, fieldProperty.getType());
        assertSame(typeDeser, fieldProperty.getValueTypeDeserializer());
        assertSame(contextAnnotations, fieldProperty.getContextAnnotations());
        assertSame(field, fieldProperty.getMember());

        assertNull(fieldProperty.getValueDeserializer());
        assertNull(fieldProperty.getNullProvider());

        assertTrue(skipNulls);
    }

    @Test
    public void testConstructorWithSkipNulls() {
        FieldProperty fieldProperty = new FieldProperty(fieldProperty, valueDeserializer, null);

        assertSame(propDef, fieldProperty.getMember());
        assertSame(type, fieldProperty.getType());
        assertSame(typeDeser, fieldProperty.getValueTypeDeserializer());
        assertSame(contextAnnotations, fieldProperty.getContextAnnotations());
        assertSame(field, fieldProperty.getMember());

        assertSame(valueDeserializer, fieldProperty.getValueDeserializer());
        assertNull(fieldProperty.getNullProvider());

        assertFalse(fieldProperty.isSkipNulls());
    }

    @Test
    public void testFixAccess() {
        DeserializationConfig config = mock(DeserializationConfig.class);

        assertNull(fieldProperty.fixAccess(config));
    }

    @Test
    public void testGetMember() {
        assertSame(field, fieldProperty.getMember());
    }

    @Test
    public void testReadResolve() {
        FieldProperty newFieldProperty = fieldProperty.readResolve();

        assertNotNull(newFieldProperty);
        assertEquals(fieldProperty.getMember(), newFieldProperty.getMember());
        assertEquals(fieldProperty.getFullName(), newFieldProperty.getFullName());
        assertEquals(fieldProperty.getType(), newFieldProperty.getType());
        assertEquals(fieldProperty.getValueTypeDeserializer(), newFieldProperty.getValueTypeDeserializer());
        assertEquals(fieldProperty.getContextAnnotations(), newFieldProperty.getContextAnnotations());

        assertNull(newFieldProperty.getValueDeserializer());
        assertNull(newFieldProperty.getNullProvider());
    }

    @Test
    public void testDeserializeAndSet() throws IOException {
        JsonParser jsonParser = mock(JsonParser.class);
        DeserializationContext deserializationContext = mock(DeserializationContext.class);
        Object instance = new Object();

        assertTrue(fieldProperty.deserializeAndSet(jsonParser, deserializationContext, instance));

        verify(jsonParser, times(1)).hasToken(any(JsonToken.class));
        verify(jsonParser, times(1)).isExpectedStartObjectToken();
        verify(jsonParser, times(1)).clearCurrentToken();
        verify(deserializationContext, times(1)).isHandlingUnknownProperty();

        verifyZeroInteractions(jsonParser);
        verifyZeroInteractions(deserializationContext);
    }

    @Test
    public void testDeserializeSetAndReturn() throws IOException {
        JsonParser jsonParser = mock(JsonParser.class);
        DeserializationContext deserializationContext = mock(DeserializationContext.class);
        Object instance = new Object();

        assertSame(instance, fieldProperty.deserializeSetAndReturn(jsonParser, deserializationContext, instance));

        verify(jsonParser, times(1)).hasToken(any(JsonToken.class));
        verify(jsonParser, times(1)).isExpectedStartObjectToken();
        verify(jsonParser, times(1)).clearCurrentToken();
        verify(deserializationContext, times(1)).isHandlingUnknownProperty();

        verifyZeroInteractions(jsonParser);
        verifyZeroInteractions(deserializationContext);
    }

    @Test
    public void testSet() throws IOException {
        Object instance = new Object();

        Object value = new Object();
        fieldProperty.set(instance, value);

        verifyZeroInteractions(value);
    }

    @Test
    public void testSetAndReturn() throws IOException {
        Object instance = new Object();

        Object value = new Object();
        assertSame(instance, fieldProperty.setAndReturn(instance, value));

        verifyZeroInteractions(value);
    }

    @Test
    public void testGetAnnotation() {
        Class<Annotation> annotationClass = Annotation.class;

        assertNull(fieldProperty.getAnnotation(annotationClass));
    }
}
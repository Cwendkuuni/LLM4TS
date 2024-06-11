package com.fasterxml.jackson.databind.deser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.lang.annotation.Annotation;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.exc.InvalidDefinitionException;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.introspect.AnnotatedParameter;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.util.Annotations;
import com.fasterxml.jackson.databind.util.ClassUtil;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

public class CreatorPropertyTest {
    @Mock
    private PropertyName mockPropertyName;
    @Mock
    private JavaType mockJavaType;
    @Mock
    private PropertyName mockWrapperName;
    @Mock
    private TypeDeserializer mockTypeDeserializer;
    @Mock
    private Annotations mockContextAnnotations;
    @Mock
    private AnnotatedParameter mockAnnotatedParameter;
    @Mock
    private DeserializationContext mockDeserializationContext;
    @Mock
    private DeserializationConfig mockDeserializationConfig;
    @Mock
    private JsonParser mockJsonParser;

    private CreatorProperty creatorProperty;

    @Before
    public void setup() {
        creatorProperty = new CreatorProperty(mockPropertyName, mockJavaType, mockWrapperName, mockTypeDeserializer,
                mockContextAnnotations, mockAnnotatedParameter, 1, "injectableValueId", PropertyMetadata.STD_REQUIRED);
    }

    @Test
    public void testConstructor() {
        assertEquals(mockPropertyName, creatorProperty.getName());
        assertEquals(mockJavaType, creatorProperty.getType());
        assertEquals(mockWrapperName, creatorProperty.getWrapperName());
        assertEquals(mockTypeDeserializer, creatorProperty.getTypeDeserializer());
        assertEquals(1, creatorProperty.getCreatorIndex());
        assertFalse(creatorProperty.isIgnorable());
    }

    @Test
    public void testConstructorWithNullName() {
        CreatorProperty creatorProperty = new CreatorProperty(null, mockJavaType, mockWrapperName,
                mockTypeDeserializer, mockContextAnnotations, mockAnnotatedParameter, 1, "injectableValueId",
                PropertyMetadata.STD_REQUIRED);
        assertNull(creatorProperty.getName());
        assertEquals(mockJavaType, creatorProperty.getType());
        assertEquals(mockWrapperName, creatorProperty.getWrapperName());
        assertEquals(mockTypeDeserializer, creatorProperty.getTypeDeserializer());
        assertEquals(1, creatorProperty.getCreatorIndex());
        assertFalse(creatorProperty.isIgnorable());
    }

    @Test
    public void testConstructorWithIgnorableProperty() {
        creatorProperty.markAsIgnorable();
        assertTrue(creatorProperty.isIgnorable());
    }

    @Test
    public void testWithNullName() {
        CreatorProperty modified = creatorProperty.withName(null);
        assertNull(modified.getName());
        assertEquals(mockJavaType, modified.getType());
        assertEquals(mockWrapperName, modified.getWrapperName());
        assertEquals(mockTypeDeserializer, modified.getTypeDeserializer());
    }

    @Test
    public void testWithNullValueDeserializer() {
        CreatorProperty modified = creatorProperty.withNullProvider(mock(NullValueProvider.class));
        assertEquals(mockPropertyName, modified.getName());
        assertEquals(mockJavaType, modified.getType());
        assertEquals(mockWrapperName, modified.getWrapperName());
        assertEquals(mockTypeDeserializer, modified.getTypeDeserializer());
    }

    @Test
    public void testFixAccess() {
        SettableBeanProperty mockFallbackSetter = mock(SettableBeanProperty.class);
        creatorProperty.setFallbackSetter(mockFallbackSetter);
        when(mockDeserializationConfig.canOverrideAccessModifiers()).thenReturn(true);
        when(mockDeserializationContext.getConfig()).thenReturn(mockDeserializationConfig);
        creatorProperty.fixAccess(mockDeserializationConfig);
    }

    @Test
    public void testSet() throws IOException {
        SettableBeanProperty mockFallbackSetter = mock(SettableBeanProperty.class);
        creatorProperty.setFallbackSetter(mockFallbackSetter);
        Object instance = new Object();
        Object value = new Object();
        creatorProperty.set(instance, value);
    }

    @Test
    public void testSetAndReturn() throws IOException {
        SettableBeanProperty mockFallbackSetter = mock(SettableBeanProperty.class);
        creatorProperty.setFallbackSetter(mockFallbackSetter);
        Object instance = new Object();
        Object value = new Object();
        creatorProperty.setAndReturn(instance, value);
    }

    @Test(expected = InvalidDefinitionException.class)
    public void testDeserializeAndSetWithNullFallbackSetter() throws IOException {
        creatorProperty.deserializeAndSet(mockJsonParser, mockDeserializationContext, new Object());
    }

    @Test(expected = InvalidDefinitionException.class)
    public void testDeserializeSetAndReturnWithNullFallbackSetter() throws IOException {
        creatorProperty.deserializeSetAndReturn(mockJsonParser, mockDeserializationContext, new Object());
    }

    @Test(expected = InvalidDefinitionException.class)
    public void testSetWithNullFallbackSetter() throws IOException {
        creatorProperty.set(new Object(), new Object());
    }

    @Test(expected = InvalidDefinitionException.class)
    public void testSetAndReturnWithNullFallbackSetter() throws IOException {
        creatorProperty.setAndReturn(new Object(), new Object());
    }

    @Test
    public void testGetInjectableValueId() {
        assertEquals("injectableValueId", creatorProperty.getInjectableValueId());
    }

    @Test
    public void testToString() {
        String toString = creatorProperty.toString();
        assertTrue(toString.contains(mockPropertyName.toString()));
        assertTrue(toString.contains("inject id 'injectableValueId'"));
    }

    @Test
    public void testIsIgnorable() {
        assertFalse(creatorProperty.isIgnorable());
        creatorProperty.markAsIgnorable();
        assertTrue(creatorProperty.isIgnorable());
    }
}
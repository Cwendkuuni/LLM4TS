package com.fasterxml.jackson.databind.deser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.*;

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

public class CreatorPropertyTest {
    private CreatorProperty creatorProperty;
    private PropertyName name;
    private JavaType type;
    private PropertyName wrapperName;
    private TypeDeserializer typeDeser;
    private Annotations contextAnnotations;
    private AnnotatedParameter param;
    private int index;
    private Object injectableValueId;
    private PropertyMetadata metadata;
    private JsonDeserializer<?> deser;
    private NullValueProvider nva;
    private DeserializationContext deserializationContext;
    private JsonParser jsonParser;
    private Object instance;
    private Object value;

    @Before
    public void setUp() {
        name = mock(PropertyName.class);
        type = mock(JavaType.class);
        wrapperName = mock(PropertyName.class);
        typeDeser = mock(TypeDeserializer.class);
        contextAnnotations = mock(Annotations.class);
        param = mock(AnnotatedParameter.class);
        index = 0;
        injectableValueId = new Object();
        metadata = mock(PropertyMetadata.class);
        deser = mock(JsonDeserializer.class);
        nva = mock(NullValueProvider.class);
        deserializationContext = mock(DeserializationContext.class);
        jsonParser = mock(JsonParser.class);
        instance = new Object();
        value = new Object();

        creatorProperty = new CreatorProperty(name, type, wrapperName, typeDeser, contextAnnotations, param, index,
                injectableValueId, metadata);
        creatorProperty.setFallbackSetter(mock(SettableBeanProperty.class));
    }

    @Test
    public void testConstructorWithName() {
        CreatorProperty property = new CreatorProperty(creatorProperty, name);

        assertEquals(name, property.getName());
        verify(name, times(1)).getSimpleName();
    }

    @Test
    public void testConstructorWithDeserializerAndNullValueProvider() {
        CreatorProperty property = new CreatorProperty(creatorProperty, deser, nva);

        assertEquals(deser, property.getValueDeserializer());
        assertEquals(nva, property.getNullValueProvider());
    }

    @Test
    public void testWithName() {
        CreatorProperty property = (CreatorProperty) creatorProperty.withName(name);

        assertEquals(name, property.getName());
        verify(name, times(1)).getSimpleName();
    }

    @Test
    public void testWithValueDeserializer() {
        CreatorProperty property = (CreatorProperty) creatorProperty.withValueDeserializer(deser);

        assertEquals(deser, property.getValueDeserializer());
        assertNotEquals(creatorProperty.getValueDeserializer(), property.getValueDeserializer());
    }

    @Test
    public void testWithNullProvider() {
        CreatorProperty property = (CreatorProperty) creatorProperty.withNullProvider(nva);

        assertEquals(nva, property.getNullValueProvider());
        assertNotEquals(creatorProperty.getNullValueProvider(), property.getNullValueProvider());
    }

    @Test
    public void testFixAccess() {
        creatorProperty.fixAccess(mock(DeserializationConfig.class));

        verify(creatorProperty.getFallbackSetter(), times(1)).fixAccess(any(DeserializationConfig.class));
    }

    @Test
    public void testSetFallbackSetter() {
        SettableBeanProperty fallbackSetter = mock(SettableBeanProperty.class);

        creatorProperty.setFallbackSetter(fallbackSetter);

        assertEquals(fallbackSetter, creatorProperty.getFallbackSetter());
    }

    @Test
    public void testMarkAsIgnorable() {
        creatorProperty.markAsIgnorable();

        assertTrue(creatorProperty.isIgnorable());
    }

    @Test
    public void testIsIgnorable() {
        assertFalse(creatorProperty.isIgnorable());
    }

    @Test
    public void testFindInjectableValue() throws JsonMappingException {
        when(contextAnnotations.reportBadDefinition(any(), any())).thenThrow(InvalidDefinitionException.class);

        try {
            creatorProperty.findInjectableValue(deserializationContext, instance);
            fail("Expected InvalidDefinitionException to be thrown");
        } catch (InvalidDefinitionException e) {
            assertEquals(String.format("Property '%s' (type %s) has no injectable value id configured",
                    creatorProperty.getName(), creatorProperty.getClass().getName()), e.getMessage());
        }

        verify(contextAnnotations, times(1)).reportBadDefinition(any(), any());
    }

    @Test
    public void testInject() throws IOException {
        when(creatorProperty.findInjectableValue(deserializationContext, instance)).thenReturn(value);

        creatorProperty.inject(deserializationContext, instance);

        verify(creatorProperty).set(instance, value);
    }

    @Test
    public void testGetAnnotation() {
        when(param.getAnnotation(Annotation.class)).thenReturn(null);

        Annotation annotation = creatorProperty.getAnnotation(Annotation.class);

        assertNull(annotation);
        verify(param, times(1)).getAnnotation(Annotation.class);
    }

    @Test
    public void testGetMember() {
        AnnotatedMember member = creatorProperty.getMember();

        assertEquals(param, member);
    }

    @Test
    public void testGetCreatorIndex() {
        int creatorIndex = creatorProperty.getCreatorIndex();

        assertEquals(index, creatorIndex);
    }

    @Test
    public void testDeserializeAndSet() throws IOException {
        when(creatorProperty.deserialize(jsonParser, deserializationContext)).thenReturn(value);

        creatorProperty.deserializeAndSet(jsonParser, deserializationContext, instance);

        verify(creatorProperty.getFallbackSetter(), times(1)).set(instance, value);
    }

    @Test
    public void testDeserializeSetAndReturn() throws IOException {
        when(creatorProperty.deserialize(jsonParser, deserializationContext)).thenReturn(value);

        Object returnedValue = creatorProperty.deserializeSetAndReturn(jsonParser, deserializationContext, instance);

        verify(creatorProperty.getFallbackSetter(), times(1)).setAndReturn(instance, value);
        assertEquals(instance, returnedValue);
    }

    @Test
    public void testSet() throws IOException {
        CreatorProperty creatorPropertyWithoutFallbackSetter = new CreatorProperty(name, type, wrapperName, typeDeser,
                contextAnnotations, param, index, injectableValueId, metadata);

        creatorPropertyWithoutFallbackSetter.set(instance, value);
    }

    @Test(expected = InvalidDefinitionException.class)
    public void testSetWithoutFallbackSetterThrowsException() throws IOException {
        creatorProperty.set(null, value);
    }

    @Test
    public void testSetAndReturn() throws IOException {
        CreatorProperty creatorPropertyWithoutFallbackSetter = new CreatorProperty(name, type, wrapperName, typeDeser,
                contextAnnotations, param, index, injectableValueId, metadata);

        Object returnedValue = creatorPropertyWithoutFallbackSetter.setAndReturn(instance, value);

        assertEquals(instance, returnedValue);
    }

    @Test(expected = InvalidDefinitionException.class)
    public void testSetAndReturnWithoutFallbackSetterThrowsException() throws IOException {
        creatorProperty.setAndReturn(null, value);
    }

    @Test
    public void testGetInjectableValueId() {
        assertEquals(injectableValueId, creatorProperty.getInjectableValueId());
    }

    @Test
    public void testToString() {
        assertEquals(
                "[creator property, name '" + creatorProperty.getName() + "'; inject id '" + injectableValueId
                        + "']", creatorProperty.toString());
    }
}
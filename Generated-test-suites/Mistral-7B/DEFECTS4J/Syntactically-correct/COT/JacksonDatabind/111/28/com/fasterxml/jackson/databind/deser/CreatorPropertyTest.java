package com.fasterxml.jackson.databind.deser.test; // add the appropriate package

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.util.Arrays;
import java.util.Collections;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.exc.InvalidDefinitionException;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.introspect.AnnotatedParameter;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.fasterxml.jackson.databind.util.Annotations;
import com.fasterxml.jackson.databind.util.ClassUtil;

public class CreatorPropertyTest {

    private Object beanInstance;
    private Object injectableValueId;
    private CreatorProperty creatorProperty;
    private PropertyName wrapperName;
    private TypeDeserializer typeDeser;
    private Annotations contextAnnotations;
    private AnnotatedMember annotatedMember;
    private AnnotatedParameter annotatedParameter;
    private JsonDeserializer<?> valueDeserializer;
    private NullValueProvider nullValueProvider;

    @Before
    public void setup() {
        beanInstance = mock(Object.class);
        injectableValueId = new Integer(1);
        wrapperName = mock(PropertyName.class);
        typeDeser = mock(TypeDeserializer.class);
        contextAnnotations = mock(Annotations.class);
        annotatedMember = mock(AnnotatedMember.class);
        annotatedParameter = mock(AnnotatedParameter.class);
        valueDeserializer = mock(JsonDeserializer.class);
        nullValueProvider = mock(NullValueProvider.class);

        when(annotatedParameter.getName()).thenReturn("testProperty");
        when(annotatedParameter.getParameterIndex()).thenReturn(0);
        when(annotatedParameter.getAnnotation(JsonProperty.class)).thenReturn(new JsonProperty());

        creatorProperty = new CreatorProperty(
                new PropertyName("testProperty"),
                JavaType.constructSimpleType(Integer.class),
                wrapperName,
                typeDeser,
                contextAnnotations,
                annotatedParameter,
                0,
                injectableValueId,
                null
        );
    }

    @Test
    public void testConstructor() {
        CreatorProperty actual = new CreatorProperty(
                new PropertyName("testProperty"),
                JavaType.constructSimpleType(Integer.class),
                wrapperName,
                typeDeser,
                contextAnnotations,
                annotatedParameter,
                0,
                injectableValueId,
                null
        );
        CreatorProperty expected = creatorProperty;
        assertEquals(expected, actual);
    }

    @Test
    public void testCopyConstructor() {
        CreatorProperty src = creatorProperty;
        CreatorProperty actual = new CreatorProperty(src, new PropertyName("testProperty2"));
        assertEquals(src.getName(), actual.getName());
        assertEquals(src.getType(), actual.getType());
        assertEquals(src.getTypeDeserializer(), actual.getTypeDeserializer());
        assertEquals(src.getContextAnnotations(), actual.getContextAnnotations());
        assertEquals(src.getAnnotated(), actual.getAnnotated());
        assertEquals(src.getCreatorIndex(), actual.getCreatorIndex());
        assertEquals(src.getInjectableValueId(), actual.getInjectableValueId());
        assertNull(src._fallbackSetter);
        assertNull(actual._fallbackSetter);
    }

    @Test
    public void testCopyConstructorWithDeserializer() {
        CreatorProperty src = creatorProperty;
        JsonDeserializer<?> deserializer = mock(JsonDeserializer.class);
        NullValueProvider nva = mock(NullValueProvider.class);
        CreatorProperty actual = new CreatorProperty(src, deserializer, nva);
        assertEquals(src.getName(), actual.getName());
        assertEquals(src.getType(), actual.getType());
        assertEquals(src.getTypeDeserializer(), actual.getTypeDeserializer());
        assertEquals(src.getContextAnnotations(), actual.getContextAnnotations());
        assertEquals(src.getAnnotated(), actual.getAnnotated());
        assertEquals(src.getCreatorIndex(), actual.getCreatorIndex());
        assertEquals(src.getInjectableValueId(), actual.getInjectableValueId());
        assertEquals(src.getValueDeserializer(), actual.getValueDeserializer());
        assertNull(src._fallbackSetter);
        assertNull(actual._fallbackSetter);
    }

    @Test(expected = IllegalStateException.class)
    public void testCopyConstructorWithFallbackSetter() {
        CreatorProperty src = creatorProperty;
        SettableBeanProperty fallbackSetter = mock(SettableBeanProperty.class);
        CreatorProperty actual = new CreatorProperty(src, wrapperName, fallbackSetter);
    }

    @Test
    public void testSetFallbackSetter() {
        CreatorProperty actual = creatorProperty;
        SettableBeanProperty fallbackSetter = mock(SettableBeanProperty.class);
        actual.setFallbackSetter(fallbackSetter);
        assertSame(fallbackSetter, actual._fallbackSetter);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetFallbackSetterNull() {
        CreatorProperty actual = creatorProperty;
        actual.setFallbackSetter(null);
    }

    @Test
    public void testMarkAsIgnorable() {
        CreatorProperty actual = creatorProperty;
        actual.markAsIgnorable();
        assertTrue(actual.isIgnorable());
    }

    @Test
    public void testFindInjectableValue() throws IOException {
        DeserializationContext context = mock(DeserializationContext.class);
        Object value = new Integer(2);
        when(context.findInjectableValue(injectableValueId, creatorProperty, beanInstance)).thenReturn(value);
        Object actual = creatorProperty.findInjectableValue(context, beanInstance);
        assertEquals(value, actual);
    }

    @Test(expected = JsonMappingException.class)
    public void testFindInjectableValueNoInjectableValueId() throws IOException {
        DeserializationContext context = mock(DeserializationContext.class);
        creatorProperty.findInjectableValue(context, beanInstance);
    }

    @Test(expected = JsonMappingException.class)
    public void testInjectValueNotFound() throws IOException {
        DeserializationContext context = mock(DeserializationContext.class);
        Object beanInstanceNotNull = new Object();
        when(context.findInjectableValue(injectableValueId, creatorProperty, beanInstanceNotNull)).thenReturn(null);
        creatorProperty.inject(context, beanInstanceNotNull);
    }

    @Test
    public void testInjectValue() throws IOException {
        DeserializationContext context = mock(DeserializationContext.class);
        Object value = new Integer(2);
        when(context.findInjectableValue(injectableValueId, creatorProperty, beanInstance)).thenReturn(value);
        creatorProperty.inject(context, beanInstance);
        assertEquals(value, creatorProperty.get(beanInstance));
    }

    @Test(expected = IllegalStateException.class)
    public void testInjectValueWithNullBeanInstance() throws IOException {
        DeserializationContext context = mock(DeserializationContext.class);
        creatorProperty.inject(context, null);
    }

    @Test
    public void testGetType() {
        assertEquals(Integer.class, creatorProperty.getType());
    }

    @Test
    public void testToString() {
        assertEquals("[creator property, name 'testProperty'; inject id '1']", creatorProperty.toString());
    }

    @Test(expected = IOException.class)
    public void testDeserializeAndSetNullValueDeserializer() throws IOException {
        JsonParser parser = mock(JsonParser.class);
        DeserializationContext context = mock(DeserializationContext.class);
        creatorProperty._verifySetter();
        creatorProperty.deserializeAndSet(parser, context, beanInstance);
    }

    @Test(expected = InvalidDefinitionException.class)
    public void testDeserializeAndSetMissingSetter() throws IOException {
        JsonParser parser = mock(JsonParser.class);
        DeserializationContext context = mock(DeserializationContext.class);
        creatorProperty._verifySetter();
        creatorProperty.deserializeAndSet(parser, context, beanInstance);
    }

    @Test
    public void testDeserializeAndSetValidValue() throws IOException {
        JsonParser parser = mock(JsonParser.class);
        Object value = new Integer(3);
        when(parser.getValueAsInt()).thenReturn(3);
        DeserializationContext context = mock(DeserializationContext.class);
        when(parser.getCurrentName()).thenReturn("testProperty");
        when(valueDeserializer.deserialize(parser, context)).thenReturn(value);
        when(context.findBeanProperty(wrapperName)).thenReturn(creatorProperty);
        creatorProperty._verifySetter();
        creatorProperty.deserializeAndSet(parser, context, beanInstance);
        assertEquals(value, creatorProperty.get(beanInstance));
    }

    @Test
    public void testDeserializeSetAndReturnValidValue() throws IOException {
        JsonParser parser = mock(JsonParser.class);
        Object value = new Integer(3);
        when(parser.getValueAsInt()).thenReturn(3);
        DeserializationContext context = mock(DeserializationContext.class);
        when(parser.getCurrentName()).thenReturn("testProperty");
        when(valueDeserializer.deserialize(parser, context)).thenReturn(value);
        when(context.findBeanProperty(wrapperName)).thenReturn(creatorProperty);
        creatorProperty._verifySetter();
        Object actual = creatorProperty.deserializeSetAndReturn(parser, context, beanInstance);
        assertEquals(value, actual);
    }

    @Test
    public void testSetValidValue() throws IOException {
        Object value = new Integer(3);
        creatorProperty.set(beanInstance, value);
        assertEquals(value, creatorProperty.get(beanInstance));
    }

    @Test
    public void testSetNullValue() throws IOException {
        creatorProperty.set(beanInstance, null);
        assertNull(creatorProperty.get(beanInstance));
    }

    @Test(expected = IOException.class)
    public void testSetInvalidValueDeserializer() throws IOException {
        Object value = new String("testString");
        creatorProperty.set(beanInstance, value);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetInvalidProperty() throws IOException {
        Object value = new Object();
        creatorProperty = new CreatorProperty(
                new PropertyName("testPropertyInvalid"),
                JavaType.constructSimpleType(Integer.class),
                wrapperName,
                typeDeser,
                contextAnnotations,
                annotatedParameter,
                0,
                injectableValueId,
                null
        );
        creatorProperty.set(beanInstance, value);
    }

    @Test
    public void testSetValueDeserializer() {
        JsonDeserializer<?> deserializer = mock(JsonDeserializer.class);
        creatorProperty.setValueDeserializer(deserializer);
        assertEquals(deserializer, creatorProperty.getValueDeserializer());
    }

    @Test
    public void testSetValueDeserializerNull() {
        creatorProperty.setValueDeserializer(null);
        assertNull(creatorProperty.getValueDeserializer());
    }

    @Test
    public void testWithName() {
        PropertyName newName = new PropertyName("testPropertyNew");
        CreatorProperty actual = creatorProperty.withName(newName);
        assertEquals(newName, actual.getName());
        assertEquals(creatorProperty.getType(), actual.getType());
        assertEquals(creatorProperty.getTypeDeserializer(), actual.getTypeDeserializer());
        assertEquals(creatorProperty.getContextAnnotations(), actual.getContextAnnotations());
        assertEquals(creatorProperty.getAnnotated(), actual.getAnnotated());
        assertEquals(creatorProperty.getCreatorIndex(), actual.getCreatorIndex());
        assertEquals(creatorProperty.getInjectableValueId(), actual.getInjectableValueId());
        assertNull(creatorProperty._fallbackSetter);
        assertNull(actual._fallbackSetter);
    }

    @Test
    public void testWithValueDeserializer() {
        JsonDeserializer<?> deserializer = mock(JsonDeserializer.class);
        CreatorProperty actual = creatorProperty.withValueDeserializer(deserializer);
        assertEquals(creatorProperty.getName(), actual.getName());
        assertEquals(creatorProperty.getType(), actual.getType());
        assertEquals(creatorProperty.getTypeDeserializer(), actual.getTypeDeserializer());
        assertEquals(creatorProperty.getContextAnnotations(), actual.getContextAnnotations());
        assertEquals(creatorProperty.getAnnotated(), actual.getAnnotated());
        assertEquals(creatorProperty.getCreatorIndex(), actual.getCreatorIndex());
        assertEquals(creatorProperty.getInjectableValueId(), actual.getInjectableValueId());
        assertEquals(deserializer, actual.getValueDeserializer());
        assertNull(creatorProperty._fallbackSetter);
        assertNull(actual._fallbackSetter);
    }

    @Test
    public void testWithNullValueDeserializer() {
        JsonDeserializer<?> deserializer = null;
        CreatorProperty actual = creatorProperty.withValueDeserializer(deserializer);
        assertNull(actual.getValueDeserializer());
    }

    @Test
    public void testWithNullProvider() {
        NullValueProvider nva = null;
        CreatorProperty actual = creatorProperty.withNullProvider(nva);
        assertNull(actual.getNullProvider());
    }

    @Test
    public void testWithNullProviderWithValueDeserializer() {
        JsonDeserializer<?> deserializer = mock(JsonDeserializer.class);
        NullValueProvider nva = null;
        CreatorProperty actual = creatorProperty.withNullProvider(nva).withValueDeserializer(deserializer);
        assertNull(actual.getNullProvider());
        assertEquals(deserializer, actual.getValueDeserializer());
    }

    @Test
    public void testFixAccess() throws IOException {
        DeserializationConfig config = mock(DeserializationConfig.class);
        creatorProperty.fixAccess(config);
        verify(config).findBeanProperty(wrapperName);
    }
}
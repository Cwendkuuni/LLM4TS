package com.fasterxml.jackson.databind.deser.test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.annotation.JsonTypeInfo.Id;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.AnnotationIntrospector;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.NullValueProvider;
import com.fasterxml.jackson.databind.PropertyName;
import com.fasterxml.jackson.databind.PropertyMetadata;
import com.fasterxml.jackson.databind.TypeDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer.EmptyContextTypeDeserializer;
import com.fasterxml.jackson.databind.util.Annotations;
import com.fasterxml.jackson.databind.util.ClassUtil;

public class CreatorPropertyTest {

    private CreatorProperty property;
    private DeserializationContext context;
    private JsonParser parser;
    private Object beanInstance;
    private TypeDeserializer typeDeser;
    private Annotations contextAnnotations;
    private AnnotatedParameter param;
    private PropertyMetadata metadata;

    @Before
    public void setup() throws Exception {
        context = mock(DeserializationContext.class);
        parser = mock(JsonParser.class);
        typeDeser = mock(TypeDeserializer.class);
        contextAnnotations = mock(Annotations.class);
        param = mock(AnnotatedParameter.class);
        metadata = mock(PropertyMetadata.class);
        property = new CreatorProperty(new PropertyName("testProperty"),
                new JavaType(String.class), null, typeDeser,
                contextAnnotations, param, 0, null, metadata);
        beanInstance = new Object() {};
    }

    @Test
    public void testConstructor() {
        BeanProperty beanProperty = property;
        assertSame(beanProperty.getName(), property.getName());
        assertSame(beanProperty.getType(), property.getType());
        assertNull(beanProperty.getWrapperName());
        assertSame(beanProperty.getTypeDeserializer(), property.getTypeDeserializer());
        assertSame(beanProperty.getContextAnnotations(), property.getContextAnnotations());
        assertSame(beanProperty.getAnnotated(), property._annotated);
        assertNull(beanProperty.getFallbackSetter());
        assertFalse(beanProperty.isIgnorable());
        assertSame(beanProperty.getCreatorIndex(), property.getCreatorIndex());
    }

    @Test
    public void testCopyConstructor() throws IOException {
        BeanProperty beanProperty = property;
        CreatorProperty copy = new CreatorProperty(beanProperty, new PropertyName("newName"));
        assertSame(copy.getName(), "newName");
        assertSame(copy.getType(), beanProperty.getType());
        assertNull(copy.getWrapperName());
        assertSame(copy.getTypeDeserializer(), beanProperty.getTypeDeserializer());
        assertSame(copy.getContextAnnotations(), beanProperty.getContextAnnotations());
        assertSame(copy.getAnnotated(), beanProperty.getAnnotated());
        assertNull(copy.getFallbackSetter());
        assertFalse(copy.isIgnorable());
        assertSame(copy.getCreatorIndex(), beanProperty.getCreatorIndex());
    }

    @Test
    public void testWithName() {
        BeanProperty beanProperty = property;
        BeanProperty newNameProperty = beanProperty.withName(new PropertyName("newName"));
        assertSame(newNameProperty.getName(), "newName");
        assertSame(newNameProperty.getType(), beanProperty.getType());
        assertNull(newNameProperty.getWrapperName());
        assertSame(newNameProperty.getTypeDeserializer(), beanProperty.getTypeDeserializer());
        assertSame(newNameProperty.getContextAnnotations(), beanProperty.getContextAnnotations());
        assertSame(newNameProperty.getAnnotated(), beanProperty.getAnnotated());
        assertNull(newNameProperty.getFallbackSetter());
        assertFalse(newNameProperty.isIgnorable());
        assertSame(newNameProperty.getCreatorIndex(), beanProperty.getCreatorIndex());
    }

    @Test
    public void testWithValueDeserializer() throws IOException {
        JsonDeserializer<String> deserializer = mock(JsonDeserializer.class);
        BeanProperty beanProperty = property;
        BeanProperty deserializerProperty = beanProperty.withValueDeserializer(deserializer);
        assertSame(deserializerProperty.getName(), beanProperty.getName());
        assertSame(deserializerProperty.getType(), beanProperty.getType());
        assertNull(deserializerProperty.getWrapperName());
        assertSame(deserializerProperty.getTypeDeserializer(), beanProperty.getTypeDeserializer());
        assertSame(deserializerProperty.getContextAnnotations(), beanProperty.getContextAnnotations());
        assertSame(deserializerProperty.getAnnotated(), beanProperty.getAnnotated());
        assertSame(deserializerProperty.getValueDeserializer(), deserializer);
        assertNull(deserializerProperty.getFallbackSetter());
        assertFalse(deserializerProperty.isIgnorable());
        assertSame(deserializerProperty.getCreatorIndex(), beanProperty.getCreatorIndex());
    }

    @Test
    public void testWithNullProvider() throws IOException {
        NullValueProvider nva = mock(NullValueProvider.class);
        BeanProperty beanProperty = property;
        BeanProperty nullProviderProperty = beanProperty.withNullProvider(nva);
        assertSame(nullProviderProperty.getName(), beanProperty.getName());
        assertSame(nullProviderProperty.getType(), beanProperty.getType());
        assertNull(nullProviderProperty.getWrapperName());
        assertSame(nullProviderProperty.getTypeDeserializer(), beanProperty.getTypeDeserializer());
        assertSame(nullProviderProperty.getContextAnnotations(), beanProperty.getContextAnnotations());
        assertSame(nullProviderProperty.getAnnotated(), beanProperty.getAnnotated());
        assertSame(nullProviderProperty.getNullProvider(), nva);
        assertNull(nullProviderProperty.getFallbackSetter());
        assertFalse(nullProviderProperty.isIgnorable());
        assertSame(nullProviderProperty.getCreatorIndex(), beanProperty.getCreatorIndex());
    }

    @Test
    public void testFixAccess() throws IOException {
        BeanProperty beanProperty = property;
        property.fixAccess(context);
        assertSame(beanProperty.getFallbackSetter(), property._fallbackSetter);
    }

    @Test(expected = IOException.class)
    public void testSetFallbackSetter() throws IOException {
        BeanProperty beanProperty = property;
        property.setFallbackSetter(null);
        property._reportMissingSetter(parser, context);
    }

    @Test
    public void testMarkAsIgnorable() throws IOException {
        BeanProperty beanProperty = property;
        property.markAsIgnorable();
        assertTrue(beanProperty.isIgnorable());
    }

    @Test
    public void testFindInjectableValue() throws JsonMappingException, IOException {
        when(context.findInjectableValue(anyObject(), any(CreatorProperty.class), eq(beanInstance)))
                .thenReturn("Injected Value");
        Object result = property.findInjectableValue(context, beanInstance);
        assertEquals("Injected Value", result);
    }

    @Test
    public void testInject() throws IOException {
        when(context.findInjectableValue(anyObject(), any(CreatorProperty.class), eq(beanInstance)))
                .thenReturn("Injected Value");
        property.inject(context, beanInstance);
        assertEquals("Injected Value", property.get(beanInstance));
    }

    @Test(expected = IOException.class)
    public void testDeserializeAndSetWithMissingSetter() throws IOException {
        property._verifySetter();
        property.deserializeAndSet(parser, context, beanInstance);
    }

    @Test
    public void testDeserializeSetAndReturn() throws IOException {
        Object value = "Test Value";
        JsonDeserializer<String> stringDeserializer = mock(JsonDeserializer.class);
        when(stringDeserializer.deserialize(parser, context)).thenReturn(value);
        property.setValueDeserializer(stringDeserializer);
        Object result = property.deserializeSetAndReturn(parser, context, beanInstance);
        assertEquals(value, result);
    }

    @Test
    public void testSet() throws IOException {
        Object value = "Test Value";
        property.set(beanInstance, value);
        assertEquals(value, property.get(beanInstance));
    }

    @Test
    public void testSetAndReturn() throws IOException {
        Object value = "Test Value";
        Object result = property.setAndReturn(beanInstance, value);
        assertEquals(value, result);
    }

    @Test
    public void testGetInjectableValueId() {
        assertNull(property.getInjectableValueId());
    }

    @Test
    public void testToString() {
        assertEquals("[creator property, name 'testProperty'; inject id null]", property.toString());
    }
}
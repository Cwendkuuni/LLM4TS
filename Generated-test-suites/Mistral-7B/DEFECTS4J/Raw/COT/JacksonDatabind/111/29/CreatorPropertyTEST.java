package com.fasterxml.jackson.databind.deser.test;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.lang.annotation.Annotation;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.util.Annotations;
import com.fasterxml.jackson.databind.util.ClassUtil;
import com.fasterxml.jackson.databind.util.TestBase;

public class CreatorPropertyTest extends TestBase {

    private CreatorProperty property;
    private AnnotatedParameter param;
    private JsonDeserializer<?> deserializer;
    private NullValueProvider nva;
    private Object beanInstance;
    private DeserializationContext context;

    @Before
    public void setup() throws Exception {
        Object injectableValueId = new Object() {
            // Dummy implementation for injectable value id
        };
        TypeDeserializer typeDeser = mock(TypeDeserializer.class);
        Annotations contextAnnotations = mock(Annotations.class);
        param = mock(AnnotatedParameter.class);
        when(param.getAnnotation(JsonProperty.class)).thenReturn(new JsonProperty());
        when(param.getParameterName()).thenReturn(new PropertyName("test"));
        deserializer = mock(JsonDeserializer.class);
        nva = mock(NullValueProvider.class);
        when(deserializer.getNullValueProvider()).thenReturn(nva);
        context = mock(DeserializationContext.class);
        when(context.findInjectableValue(anyObject(), eq(property), anyObject())).thenReturn(new Object());
        when(context.findInjectableValue(_eq(injectableValueId), eq(property), anyObject())).thenReturn(new Object());
        property = new CreatorProperty("testProperty", new TypeReference<Object>() {}, null, typeDeser, contextAnnotations, param, 1, injectableValueId, null);
        property.setFallbackSetter(mock(SettableBeanProperty.class));
        beanInstance = mock(Object.class);
    }

    @Test
    public void testConstructor() {
        CreatorProperty actual = new CreatorProperty("testProperty", new TypeReference<Object>() {}, null, null, null, null, 1, null, null);
        CreatorProperty expected = new CreatorProperty("testProperty", new TypeReference<Object>() {}, null, null, null, null, 1, null, null);
        assertSame(expected, actual);
    }

    @Test
    public void testConstructorWithContextAnnotations() {
        CreatorProperty actual = new CreatorProperty("testProperty", new TypeReference<Object>() {}, null, null, mock(Annotations.class), null, 1, null, null);
        CreatorProperty expected = new CreatorProperty("testProperty", new TypeReference<Object>() {}, null, null, mock(Annotations.class), null, 1, null, null);
        assertSame(expected, actual);
    }

    @Test
    public void testConstructorWithTypeDeserializer() {
        CreatorProperty actual = new CreatorProperty("testProperty", new TypeReference<Object>() {}, null, mock(TypeDeserializer.class), null, null, 1, null, null);
        CreatorProperty expected = new CreatorProperty("testProperty", new TypeReference<Object>() {}, null, mock(TypeDeserializer.class), null, null, 1, null, null);
        assertSame(expected, actual);
    }

    @Test
    public void testConstructorWithAnnotatedParameter() {
        CreatorProperty actual = new CreatorProperty("testProperty", new TypeReference<Object>() {}, null, null, null, param, 1, null, null);
        CreatorProperty expected = new CreatorProperty("testProperty", new TypeReference<Object>() {}, null, null, null, param, 1, null, null);
        assertSame(expected, actual);
    }

    @Test
    public void testConstructorWithInjectableValueId() {
        CreatorProperty actual = new CreatorProperty("testProperty", new TypeReference<Object>() {}, null, null, null, null, 1, null, null, injectableValueId);
        CreatorProperty expected = new CreatorProperty("testProperty", new TypeReference<Object>() {}, null, null, null, null, 1, null, null, injectableValueId);
        assertSame(expected, actual);
    }

    @Test
    public void testConstructorWithMetadata() {
        PropertyMetadata metadata = mock(PropertyMetadata.class);
        CreatorProperty actual = new CreatorProperty("testProperty", new TypeReference<Object>() {}, null, null, null, null, 1, null, null, null, metadata);
        CreatorProperty expected = new CreatorProperty("testProperty", new TypeReference<Object>() {}, null, null, null, null, 1, null, null, null, metadata);
        assertSame(expected, actual);
    }

    @Test
    public void testCopyConstructor() {
        CreatorProperty src = new CreatorProperty("testProperty", new TypeReference<Object>() {}, null, null, null, null, 1, null, null, null, null);
        CreatorProperty actual = new CreatorProperty(src, new PropertyName("newName"));
        CreatorProperty expected = new CreatorProperty(src, new PropertyName("newName"));
        assertSame(expected, actual);
    }

    @Test
    public void testCopyConstructorWithDeserializer() {
        CreatorProperty src = new CreatorProperty("testProperty", new TypeReference<Object>() {}, null, null, null, null, 1, null, null, null, null);
        CreatorProperty actual = new CreatorProperty(src, deserializer, nva);
        CreatorProperty expected = new CreatorProperty(src, deserializer, nva);
        assertSame(expected, actual);
    }

    @Test
    public void testWithName() {
        CreatorProperty actual = property.withName(new PropertyName("newName"));
        CreatorProperty expected = new CreatorProperty("newName", property.getType(), null, property.getTypeDeserializer(), property.getContextAnnotations(), property._annotated, property._creatorIndex, property._injectableValueId, null);
        assertSame(expected, actual);
    }

    @Test
    public void testWithValueDeserializer() {
        JsonDeserializer<?> newDeserializer = mock(JsonDeserializer.class);
        CreatorProperty actual = property.withValueDeserializer(newDeserializer);
        CreatorProperty expected = new CreatorProperty(property, newDeserializer, property._nullProvider);
        assertSame(expected, actual);
    }

    @Test
    public void testWithNullProvider() {
        NullValueProvider newNullProvider = mock(NullValueProvider.class);
        CreatorProperty actual = property.withNullProvider(newNullProvider);
        CreatorProperty expected = new CreatorProperty(property, property._valueDeserializer, newNullProvider);
        assertSame(expected, actual);
    }

    @Test
    public void testFixAccess() {
        property.fixAccess(context);
        verify(property._fallbackSetter).fixAccess(context);
    }

    @Test
    public void testSetFallbackSetter() {
        SettableBeanProperty fallbackSetter = mock(SettableBeanProperty.class);
        property.setFallbackSetter(fallbackSetter);
        assertSame(fallbackSetter, property._fallbackSetter);
    }

    @Test(expected = InvalidDefinitionException.class)
    public void testMissingSetter() throws IOException {
        when(property._fallbackSetter).thenReturn(null);
        property.deserializeAndSet(mock(JsonParser.class), context, beanInstance);
    }

    @Test
    public void testFindInjectableValue() {
        Object injectedValue = property.findInjectableValue(context, beanInstance);
        assertNull(injectedValue);
    }

    @Test
    public void testInject() throws IOException {
        Object value = property.findInjectableValue(context, beanInstance);
        property.inject(context, beanInstance);
        assertEquals(value, property.getName().get(beanInstance));
    }

    @Test(expected = IOException.class)
    public void testInjectWithNullValue() throws IOException {
        when(property.getNullValueProvider()).thenReturn(null);
        property.inject(context, beanInstance);
    }

    @Test(expected = IOException.class)
    public void testInjectWithMissingInjectableValue() throws IOException {
        when(property._injectableValueId).thenReturn(null);
        property.inject(context, beanInstance);
    }

    @Test(expected = IOException.class)
    public void testInjectWithMissingContext() throws IOException {
        DeserializationContext contextWithoutInjectableValues = mock(DeserializationContext.class);
        property.inject(contextWithoutInjectableValues, beanInstance);
    }

    @Test
    public void testIsIgnorable() {
        boolean isIgnorable = property.isIgnorable();
        assertFalse(isIgnorable);
    }

    @Test
    public void testMarkAsIgnorable() {
        property.markAsIgnorable();
        assertTrue(property.isIgnorable());
    }

    @Test
    public void testDeserializeAndSet() throws IOException {
        Object value = deserialize(mock(JsonParser.class), context);
        property.deserializeAndSet(mock(JsonParser.class), context, beanInstance);
        assertEquals(value, property.getName().get(beanInstance));
    }

    @Test
    public void testDeserializeSetAndReturn() throws IOException {
        Object value = deserialize(mock(JsonParser.class), context);
        Object result = property.deserializeSetAndReturn(mock(JsonParser.class), context, beanInstance);
        assertEquals(value, result);
    }

    @Test
    public void testSet() throws IOException {
        Object value = deserialize(mock(JsonParser.class), context);
        property.set(beanInstance, value);
        assertEquals(value, property.getName().get(beanInstance));
    }

    @Test
    public void testSetAndReturn() throws IOException {
        Object value = deserialize(mock(JsonParser.class), context);
        Object result = property.setAndReturn(beanInstance, value);
        assertEquals(value, result);
    }
}
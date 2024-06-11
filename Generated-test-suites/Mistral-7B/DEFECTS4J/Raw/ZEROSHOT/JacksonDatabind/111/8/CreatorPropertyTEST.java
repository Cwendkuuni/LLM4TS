package com.fasterxml.jackson.databind.deser.test;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.lang.annotation.Annotation;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.json.JsonTest;
import com.fasterxml.jackson.databind.AnnotationIntrospector;
import com.fasterxml.jackson.databind.DeserializationConfig;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyName;
import com.fasterxml.jackson.databind.TypeDeserializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer.EmptyContextTypeDeserializer;
import com.fasterxml.jackson.databind.util.ClassUtil;
import com.fasterxml.jackson.databind.util.Annotations;

public class CreatorPropertyTest extends JsonTest {

    private static final ObjectMapper mapper = new ObjectMapper();
    private CreatorProperty property;
    private JsonDeserializer<String> stringDeserializer;
    private TypeDeserializer emptyTypeDeserializer;
    private DeserializationConfig config;
    private ObjectMapper objectMapper;

    @Before
    public void setup() throws Exception {
        stringDeserializer = mock(JsonDeserializer.class);
        when(stringDeserializer.getClass()).thenReturn(String.class);
        emptyTypeDeserializer = new EmptyContextTypeDeserializer();
        config = new DeserializationConfig(mapper.getFactory(), emptyTypeDeserializer, AnnotationIntrospector.defaultIntrospector());
        objectMapper = new ObjectMapper(config);
        property = new CreatorProperty(new PropertyName("name"), mapper.getTypeFactory().constructType(String.class), null, null, null, null, 0, null, null);
    }

    @Test
    public void testCreatorPropertyConstructor() {
        assertNotNull(property);
    }

    @Test
    public void testGetCreatorIndex() throws IOException {
        assertEquals(0, property.getCreatorIndex());
    }

    @Test
    public void testSetFallbackSetter() throws IOException {
        SettableBeanProperty fallbackSetter = mock(SettableBeanProperty.class);
        property.setFallbackSetter(fallbackSetter);
        assertSame(fallbackSetter, property._fallbackSetter);
    }

    @Test
    public void testIsIgnorable() throws IOException {
        assertFalse(property.isIgnorable());
    }

    @Test
    public void testMarkAsIgnorable() throws IOException {
        property.markAsIgnorable();
        assertTrue(property.isIgnorable());
    }

    @Test(expected = IOException.class)
    public void testDeserializeAndSet_MissingSetter() throws IOException {
        property._fallbackSetter = null;
        property.deserializeAndSet(mock(JsonParser.class), mock(DeserializationContext.class), new Object());
    }

    @Test
    public void testDeserializeAndSet() throws IOException {
        String value = "value";
        when(stringDeserializer.deserialize(mock(JsonParser.class), mock(DeserializationContext.class))).thenReturn(value);
        SettableBeanProperty fallbackSetter = mock(SettableBeanProperty.class);
        property.setFallbackSetter(fallbackSetter);
        Object instance = new Object();
        property.deserializeAndSet(mock(JsonParser.class), mock(DeserializationContext.class), instance);
        assertEquals(value, fallbackSetter.get(instance));
    }

    @Test
    public void testDeserializeSetAndReturn() throws IOException {
        String value = "value";
        when(stringDeserializer.deserialize(mock(JsonParser.class), mock(DeserializationContext.class))).thenReturn(value);
        SettableBeanProperty fallbackSetter = mock(SettableBeanProperty.class);
        property.setFallbackSetter(fallbackSetter);
        Object instance = new Object();
        Object returnValue = property.deserializeSetAndReturn(mock(JsonParser.class), mock(DeserializationContext.class), instance);
        assertEquals(value, returnValue);
    }

    @Test
    public void testSet() throws IOException {
        String value = "value";
        SettableBeanProperty fallbackSetter = mock(SettableBeanProperty.class);
        property.setFallbackSetter(fallbackSetter);
        Object instance = new Object();
        property.set(instance, value);
        assertEquals(value, fallbackSetter.get(instance));
    }

    @Test
    public void testSetAndReturn() throws IOException {
        String value = "value";
        SettableBeanProperty fallbackSetter = mock(SettableBeanProperty.class);
        property.setFallbackSetter(fallbackSetter);
        Object instance = new Object();
        Object returnValue = property.setAndReturn(instance, value);
        assertEquals(value, returnValue);
    }

    @Test
    public void testFindInjectableValue() throws IOException {
        Object injectableValueId = new Object();
        DeserializationContext context = mock(DeserializationContext.class);
        Object beanInstance = new Object();
        when(context.findInjectableValue(injectableValueId, property, beanInstance)).thenReturn("injected value");
        assertEquals("injected value", property.findInjectableValue(context, beanInstance));
    }

    @Test
    public void testInject() throws IOException {
        Object injectableValueId = new Object();
        DeserializationContext context = mock(DeserializationContext.class);
        Object beanInstance = new Object();
        property.inject(context, beanInstance);
        verify(context).findInjectableValue(injectableValueId, property, beanInstance);
    }

    @Test
    public void testToString() {
        assertEquals("[creator property, name 'name'; inject id null]", property.toString());
    }
}
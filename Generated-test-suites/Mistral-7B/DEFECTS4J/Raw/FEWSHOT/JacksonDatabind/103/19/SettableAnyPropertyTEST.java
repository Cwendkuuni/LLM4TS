package com.fasterxml.jackson.databind.deser;

import static org.junit.Assert.*;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.impl.ReadableObjectId.Referring;
import com.fasterxml.jackson.databind.introspect.AnnotatedField;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.introspect.AnnotatedMethod;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.util.ClassUtil;
import com.fasterxml.jackson.databind.util.ObjectIdGenerator;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class SettableAnyPropertyTest {

    private ObjectIdGenerator idGenerator = new ObjectIdGenerator();

    @Test
    public void testSettableAnyProperty() {
        BeanProperty property = new BeanProperty("property", String.class);
        AnnotatedMember setter = Mockito.mock(AnnotatedMember.class);
        JavaType type = ObjectMapper.getDefaultInstance().getTypeFactory().constructType(Map.class);
        KeyDeserializer keyDeserializer = Mockito.mock(KeyDeserializer.class);
        JsonDeserializer<Object> valueDeserializer = Mockito.mock(JsonDeserializer.class);
        TypeDeserializer valueTypeDeserializer = Mockito.mock(TypeDeserializer.class);

        SettableAnyProperty anyProperty = new SettableAnyProperty(property, setter, type, keyDeserializer, valueDeserializer, valueTypeDeserializer);

        assertEquals(property, anyProperty.getProperty());
        assertTrue(anyProperty.hasValueDeserializer());
        assertEquals(type, anyProperty.getType());

        JsonParser parser = Mockito.mock(JsonParser.class);
        DeserializationContext context = Mockito.mock(DeserializationContext.class);
        Object instance = new Object();
        String propName = "propName";

        anyProperty.deserializeAndSet(parser, context, instance, propName);

        verify(setter, times(1)).callOnWith(instance, propName, anyValue());
    }

    @Test
    public void testSettableAnyPropertyWithValueDeserializer() {
        BeanProperty property = new BeanProperty("property", String.class);
        AnnotatedMember setter = Mockito.mock(AnnotatedMember.class);
        JavaType type = ObjectMapper.getDefaultInstance().getTypeFactory().constructType(Map.class);
        KeyDeserializer keyDeserializer = Mockito.mock(KeyDeserializer.class);
        JsonDeserializer<Object> valueDeserializer = Mockito.mock(JsonDeserializer.class);
        TypeDeserializer valueTypeDeserializer = Mockito.mock(TypeDeserializer.class);

        SettableAnyProperty anyProperty = new SettableAnyProperty(property, setter, type, keyDeserializer, valueDeserializer, valueTypeDeserializer);

        SettableAnyProperty newAnyProperty = anyProperty.withValueDeserializer(valueDeserializer);

        assertEquals(property, newAnyProperty.getProperty());
        assertTrue(newAnyProperty.hasValueDeserializer());
        assertEquals(type, newAnyProperty.getType());
        assertEquals(valueDeserializer, newAnyProperty.getValueDeserializer());
    }

    @Test
    public void testFixAccess() {
        BeanProperty property = new BeanProperty("property", String.class);
        AnnotatedMember setter = Mockito.mock(AnnotatedMember.class);
        JavaType type = ObjectMapper.getDefaultInstance().getTypeFactory().constructType(Map.class);
        KeyDeserializer keyDeserializer = Mockito.mock(KeyDeserializer.class);
        JsonDeserializer<Object> valueDeserializer = Mockito.mock(JsonDeserializer.class);
        TypeDeserializer valueTypeDeserializer = Mockito.mock(TypeDeserializer.class);

        SettableAnyProperty anyProperty = new SettableAnyProperty(property, setter, type, keyDeserializer, valueDeserializer, valueTypeDeserializer);

        DeserializationConfig config = Mockito.mock(DeserializationConfig.class);
        anyProperty.fixAccess(config);

        verify(setter, times(1)).fixAccess(config.isEnabled(MapperFeature.OVERRIDE_PUBLIC_ACCESS_MODIFIERS));
    }

    @Test
    public void testDeserializeAndSetWithMap() throws IOException {
        BeanProperty property = new BeanProperty("property", Map.class);
        AnnotatedMember setter = Mockito.mock(AnnotatedMember.class);
        JavaType type = ObjectMapper.getDefaultInstance().getTypeFactory().constructType(Map.class);
        KeyDeserializer keyDeserializer = Mockito.mock(KeyDeserializer.class);
        JsonDeserializer<Object> valueDeserializer = Mockito.mock(JsonDeserializer.class);
        TypeDeserializer valueTypeDeserializer = Mockito.mock(TypeDeserializer.class);

        SettableAnyProperty anyProperty = new SettableAnyProperty(property, setter, type, keyDeserializer, valueDeserializer, valueTypeDeserializer);

        Object instance = new HashMap<>();
        String propName = "propName";
        Object propValue = "value";

        JsonParser parser = Mockito.mock(JsonParser.class);
        DeserializationContext context = Mockito.mock(DeserializationContext.class);

        when(valueDeserializer.deserialize(parser, context)).thenReturn(propValue);

        anyProperty.deserializeAndSet(parser, context, instance, propName);

        Map map = (Map) instance;
        assertEquals(propValue, map.get(propName));
    }

    @Test
    public void testDeserializeAndSetWithObject() throws IOException {
        BeanProperty property = new BeanProperty("property", String.class);
        AnnotatedMember setter = Mockito.mock(AnnotatedMember.class);
        JavaType type = ObjectMapper.getDefaultInstance().getTypeFactory().constructType(String.class);
        KeyDeserializer keyDeserializer = Mockito.mock(KeyDeserializer.class);
        JsonDeserializer<Object> valueDeserializer = Mockito.mock(JsonDeserializer.class);
        TypeDeserializer valueTypeDeserializer = Mockito.mock(TypeDeserializer.class);

        SettableAnyProperty anyProperty = new SettableAnyProperty(property, setter, type, keyDeserializer, valueDeserializer, valueTypeDeserializer);

        Object instance = new Object();
        String propName = "propName";
        Object propValue = "value";

        JsonParser parser = Mockito.mock(JsonParser.class);
        DeserializationContext context = Mockito.mock(DeserializationContext.class);

        when(valueDeserializer.deserialize(parser, context)).thenReturn(propValue);

        anyProperty.deserializeAndSet(parser, context, instance, propName);

        verify(setter, times(1)).callOnWith(instance, propName, propValue);
    }

    @Test
    public void testThrowAsIOE() {
        BeanProperty property = new BeanProperty("property", String.class);
        AnnotatedMember setter = Mockito.mock(AnnotatedMember.class);
        JavaType type = ObjectMapper.getDefaultInstance().getTypeFactory().constructType(Map.class);
        KeyDeserializer keyDeserializer = Mockito.mock(KeyDeserializer.class);
        JsonDeserializer<Object> valueDeserializer = Mockito.mock(JsonDeserializer.class);
        TypeDeserializer valueTypeDeserializer = Mockito.mock(TypeDeserializer.class);

        SettableAnyProperty anyProperty = new SettableAnyProperty(property, setter, type, keyDeserializer, valueDeserializer, valueTypeDeserializer);

        String expectedMessage = "Problem deserializing \"any\" property 'propName' of class [com.example.TestClass] (expected type: class java.util.Map; actual type: class java.lang.String)";

        JsonParser parser = Mockito.mock(JsonParser.class);
        DeserializationContext context = Mockito.mock(DeserializationContext.class);

        when(parser.getCurrentName()).thenReturn("propName");
        when(parser.getCurrentValueAsString()).thenReturn("value");

        Throwable exception = new RuntimeException("Test exception");
        doThrow(exception).when(valueDeserializer).deserialize(parser, context);

        anyProperty.deserializeAndSet(parser, context, new Object(), "propName");

        verify(parser).getText();
        verify(parser).getCurrentPosition();
        verify(parser).getCurrentLineNumber();
        verify(parser).getCurrentColumnNumber();
        verifyZeroInteractions(parser.getCurrentToken(), parser.getCurrentElement());

        verify(context).handleUnexpectedValue(anyProperty, parser, "value");
        verify(context).handleUnexpectedValue(anyProperty, parser, expectedMessage);
        verify(context).handleUnexpectedValue(null, parser, "value");
        verify(context).handleUnknownProperty(parser, "propName");
    }

    private Object anyValue() {
        return Mockito.anyObject();
    }
}
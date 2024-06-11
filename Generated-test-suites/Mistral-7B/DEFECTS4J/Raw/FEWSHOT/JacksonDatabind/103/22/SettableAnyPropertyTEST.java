package com.fasterxml.jackson.databind.deser;

import static org.junit.Assert.*;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.impl.ReadableObjectId.Referring;
import com.fasterxml.jackson.databind.introspect.*;
import com.fasterxml.jackson.databind.jsontype.*;
import com.fasterxml.jackson.databind.util.*;
import org.junit.Test;

public class SettableAnyPropertyTest {

    @Test
    public void testSettableAnyPropertyConstruction() {
        BeanProperty property = new BeanProperty(null, "testProperty", String.class);
        AnnotatedMember setter = Mockito.mock(AnnotatedField.class);
        JavaType type = ObjectMapper.getDefaultInstance().getTypeFactory().constructType(String.class);
        KeyDeserializer keyDeserializer = Mockito.mock(KeyDeserializer.class);
        JsonDeserializer<Object> valueDeserializer = Mockito.mock(JsonDeserializer.class);
        TypeDeserializer valueTypeDeserializer = Mockito.mock(TypeDeserializer.class);

        SettableAnyProperty settableAnyProperty = new SettableAnyProperty(property, setter, type, keyDeserializer, valueDeserializer, valueTypeDeserializer);
        assertNotNull(settableAnyProperty);

        SettableAnyProperty settableAnyProperty2 = new SettableAnyProperty(property, setter, type, valueDeserializer, valueTypeDeserializer);
        assertNotNull(settableAnyProperty2);
    }

    @Test
    public void testWithValueDeserializer() {
        BeanProperty property = new BeanProperty(null, "testProperty", String.class);
        AnnotatedMember setter = Mockito.mock(AnnotatedField.class);
        JavaType type = ObjectMapper.getDefaultInstance().getTypeFactory().constructType(String.class);
        KeyDeserializer keyDeserializer = Mockito.mock(KeyDeserializer.class);
        JsonDeserializer<Object> valueDeserializer = Mockito.mock(JsonDeserializer.class);
        TypeDeserializer valueTypeDeserializer = Mockito.mock(TypeDeserializer.class);

        SettableAnyProperty settableAnyProperty = new SettableAnyProperty(property, setter, type, keyDeserializer, null, valueTypeDeserializer);
        SettableAnyProperty settableAnyPropertyWithValueDeserializer = settableAnyProperty.withValueDeserializer(valueDeserializer);

        assertNotSame(settableAnyProperty, settableAnyPropertyWithValueDeserializer);
        assertEquals(valueDeserializer, settableAnyPropertyWithValueDeserializer._valueDeserializer);
    }

    @Test
    public void testFixAccess() {
        BeanProperty property = new BeanProperty(null, "testProperty", String.class);
        AnnotatedMember setter = Mockito.mock(AnnotatedField.class);
        JavaType type = ObjectMapper.getDefaultInstance().getTypeFactory().constructType(String.class);
        KeyDeserializer keyDeserializer = Mockito.mock(KeyDeserializer.class);
        JsonDeserializer<Object> valueDeserializer = Mockito.mock(JsonDeserializer.class);
        TypeDeserializer valueTypeDeserializer = Mockito.mock(TypeDeserializer.class);

        SettableAnyProperty settableAnyProperty = new SettableAnyProperty(property, setter, type, keyDeserializer, valueDeserializer, valueTypeDeserializer);

        DeserializationConfig config = Mockito.mock(DeserializationConfig.class);
        settableAnyProperty.fixAccess(config);

        verify(setter).fixAccess(eq(config.isEnabled(MapperFeature.OVERRIDE_PUBLIC_ACCESS_MODIFIERS)));
    }

    @Test
    public void testDeserializeAndSet() throws IOException {
        BeanProperty property = new BeanProperty(null, "testProperty", String.class);
        AnnotatedMember setter = Mockito.mock(AnnotatedField.class);
        JavaType type = ObjectMapper.getDefaultInstance().getTypeFactory().constructType(String.class);
        KeyDeserializer keyDeserializer = Mockito.mock(KeyDeserializer.class);
        JsonDeserializer<Object> valueDeserializer = Mockito.mock(JsonDeserializer.class);
        TypeDeserializer valueTypeDeserializer = Mockito.mock(TypeDeserializer.class);

        ObjectMapper objectMapper = ObjectMapper.getDefaultInstance();
        SimpleModule module = new SimpleModule();
        objectMapper.registerModule(module);
        module.addDeserializer(String.class, valueDeserializer);

        SettableAnyProperty settableAnyProperty = new SettableAnyProperty(property, setter, type, keyDeserializer, valueDeserializer, valueTypeDeserializer);

        Object instance = new Object();
        String propName = "testProperty";
        String jsonValue = "testValue";

        JsonParser jsonParser = objectMapper.getFactory().createParser(new StringReader(jsonValue));

        settableAnyProperty.deserializeAndSet(jsonParser, Mockito.mock(DeserializationContext.class), instance, propName);

        Object expectedValue = valueDeserializer.deserialize(jsonParser, Mockito.mock(DeserializationContext.class));
        assertEquals(expectedValue, instance.getClass().getDeclaredField("testProperty").get(instance));
    }

    @Test
    public void testDeserialize() throws IOException {
        BeanProperty property = new BeanProperty(null, "testProperty", String.class);
        AnnotatedMember setter = Mockito.mock(AnnotatedField.class);
        JavaType type = ObjectMapper.getDefaultInstance().getTypeFactory().constructType(String.class);
        KeyDeserializer keyDeserializer = Mockito.mock(KeyDeserializer.class);
        JsonDeserializer<Object> valueDeserializer = Mockito.mock(JsonDeserializer.class);
        TypeDeserializer valueTypeDeserializer = Mockito.mock(TypeDeserializer.class);

        ObjectMapper objectMapper = ObjectMapper.getDefaultInstance();
        SimpleModule module = new SimpleModule();
        objectMapper.registerModule(module);
        module.addDeserializer(String.class, valueDeserializer);

        SettableAnyProperty settableAnyProperty = new SettableAnyProperty(property, setter, type, keyDeserializer, valueDeserializer, valueTypeDeserializer);

        JsonParser jsonParser = objectMapper.getFactory().createParser(new StringReader("testValue"));

        Object result = settableAnyProperty.deserialize(jsonParser, Mockito.mock(DeserializationContext.class));

        Object expectedValue = valueDeserializer.deserialize(jsonParser, Mockito.mock(DeserializationContext.class));
        assertEquals(expectedValue, result);
    }

    @Test
    public void testSet() throws IOException {
        BeanProperty property = new BeanProperty(null, "testProperty", String.class);
        AnnotatedMember setter = Mockito.mock(AnnotatedField.class);
        JavaType type = ObjectMapper.getDefaultInstance().getTypeFactory().constructType(String.class);
        KeyDeserializer keyDeserializer = Mockito.mock(KeyDeserializer.class);
        JsonDeserializer<Object> valueDeserializer = Mockito.mock(JsonDeserializer.class);
        TypeDeserializer valueTypeDeserializer = Mockito.mock(TypeDeserializer.class);

        Object instance = new Object();
        Object propName = "testProperty";
        Object value = "testValue";

        AnnotatedField field = Mockito.mock(AnnotatedField.class);
        Map<Object, Object> valueMap = Mockito.mock(Map.class);

        when(setter.getValue(instance)).thenReturn(valueMap);
        when(field.getValue(instance)).thenReturn(valueMap);

        SettableAnyProperty settableAnyProperty = new SettableAnyProperty(property, setter, type, keyDeserializer, valueDeserializer, valueTypeDeserializer);

        settableAnyProperty.set(instance, propName, value);

        assertEquals(value, instance.getClass().getDeclaredField("testProperty").get(instance));
    }

    @Test
    public void testThrowAsIOE() {
        BeanProperty property = new BeanProperty(null, "testProperty", String.class);
        AnnotatedMember setter = Mockito.mock(AnnotatedField.class);
        JavaType type = ObjectMapper.getDefaultInstance().getTypeFactory().constructType(String.class);
        KeyDeserializer keyDeserializer = Mockito.mock(KeyDeserializer.class);
        JsonDeserializer<Object> valueDeserializer = Mockito.mock(JsonDeserializer.class);
        TypeDeserializer valueTypeDeserializer = Mockito.mock(TypeDeserializer.class);

        Object instance = new Object();
        Object propName = "testProperty";
        Object value = new IllegalStateException("Test exception");

        SettableAnyProperty settableAnyProperty = new SettableAnyProperty(property, setter, type, keyDeserializer, valueDeserializer, valueTypeDeserializer);

        try {
            settableAnyProperty.set(instance, propName, value);
            fail();
        } catch (IOException e) {
            String expectedMessage = "Problem deserializing \"any\" property 'testProperty' of class [any property on class some.package.SomeClass] (expected type: java.lang.String; actual type: java.lang.IllegalStateException)";
            String actualMessage = e.getMessage();
            assertTrue(actualMessage.contains(expectedMessage));
        }
    }
}
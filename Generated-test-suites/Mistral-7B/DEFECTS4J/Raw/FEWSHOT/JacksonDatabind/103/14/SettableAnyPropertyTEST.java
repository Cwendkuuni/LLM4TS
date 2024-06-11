package com.fasterxml.jackson.databind.deser;

import static org.junit.Assert.*;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.impl.ReadableObjectId.Referring;
import com.fasterxml.jackson.databind.introspect.AnnotatedField;
import com.fasterxml.jackson.databind.introspect.AnnotatedMethod;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.util.ClassUtil;

import java.io.IOException;
import java.util.Map;

public class SettableAnyPropertyTest {
    @Test
    public void testSettableAnyProperty() {
        BeanProperty property = new BeanProperty(null, "testProperty", String.class, null);
        AnnotatedMember setter = new AnnotatedMethod(null, "setTestProperty", String.class);
        JavaType type = new TypeFactory().constructType(Map.class);
        KeyDeserializer keyDeserializer = mock(KeyDeserializer.class);
        JsonDeserializer<Object> valueDeserializer = mock(JsonDeserializer.class);
        TypeDeserializer valueTypeDeserializer = mock(TypeDeserializer.class);

        SettableAnyProperty settableAnyProperty = new SettableAnyProperty(property, setter, type, keyDeserializer, valueDeserializer, valueTypeDeserializer);

        assertEquals(property, settableAnyProperty.getProperty());
        assertTrue(settableAnyProperty.hasValueDeserializer());
        assertEquals(type, settableAnyProperty.getType());
    }

    @Test
    public void testDeserializeAndSet() throws IOException {
        JsonParser parser = createJsonParser("{\"testProperty\":\"testValue\"}");
        DeserializationContext context = new TestDeserializationContext();
        Object instance = new Object();
        String propName = "testProperty";

        SettableAnyProperty settableAnyProperty = new SettableAnyProperty(mock(BeanProperty.class), mock(AnnotatedMember.class), mock(JavaType.class), null, mock(JsonDeserializer.class), mock(TypeDeserializer.class));

        settableAnyProperty.deserializeAndSet(parser, context, instance, propName);

        Map<Object, Object> map = (Map<Object, Object>) ((AnnotatedField) settableAnyProperty._setter).getValue(instance);
        assertEquals("testValue", map.get(propName));
    }

    @Test
    public void testDeserialize() throws IOException {
        JsonParser parser = createJsonParser("{\"testProperty\":\"testValue\"}");
        DeserializationContext context = new TestDeserializationContext();

        JsonDeserializer<Object> valueDeserializer = mock(JsonDeserializer.class);
        when(valueDeserializer.deserialize(parser, context)).thenReturn("testValue");

        SettableAnyProperty settableAnyProperty = new SettableAnyProperty(mock(BeanProperty.class), mock(AnnotatedMember.class), mock(JavaType.class), null, valueDeserializer, mock(TypeDeserializer.class));

        assertEquals("testValue", settableAnyProperty.deserialize(parser, context));
    }

    @Test(expected = JsonMappingException.class)
    public void testDeserializeWithInvalidType() throws IOException {
        JsonParser parser = createJsonParser("{\"testProperty\":1}");
        DeserializationContext context = new TestDeserializationContext();

        SettableAnyProperty settableAnyProperty = new SettableAnyProperty(mock(BeanProperty.class), mock(AnnotatedMember.class), mock(JavaType.class), null, mock(JsonDeserializer.class), mock(TypeDeserializer.class));

        settableAnyProperty.deserialize(parser, context);
    }

    @Test(expected = JsonMappingException.class)
    public void testDeserializeWithNullValue() throws IOException {
        JsonParser parser = createJsonParser("{\"testProperty\":null}");
        DeserializationContext context = new TestDeserializationContext();

        SettableAnyProperty settableAnyProperty = new SettableAnyProperty(mock(BeanProperty.class), mock(AnnotatedMember.class), mock(JavaType.class), null, mock(JsonDeserializer.class), mock(TypeDeserializer.class));

        settableAnyProperty.deserialize(parser, context);
    }

    @Test
    public void testSet() throws IOException {
        Object instance = new Object();
        Object propName = "testProperty";
        Object value = "testValue";

        SettableAnyProperty settableAnyProperty = new SettableAnyProperty(mock(BeanProperty.class), mock(AnnotatedMember.class), mock(JavaType.class), null, mock(JsonDeserializer.class), mock(TypeDeserializer.class));

        settableAnyProperty.set(instance, propName, value);

        if (settableAnyProperty._setterIsField) {
            Map<Object, Object> map = (Map<Object, Object>) ((AnnotatedField) settableAnyProperty._setter).getValue(instance);
            assertEquals("testValue", map.get(propName));
        } else {
            Method method = ((AnnotatedMethod) settableAnyProperty._setter).getMethod();
            assertEquals(value, method.invoke(instance, propName));
        }
    }

    @Test
    public void testThrowAsIOE() throws IOException {
        Object instance = new Object();
        Object propName = "testProperty";
        Object value = new IOException("Test Error");

        SettableAnyProperty settableAnyProperty = new SettableAnyProperty(mock(BeanProperty.class), mock(AnnotatedMember.class), mock(JavaType.class), null, mock(JsonDeserializer.class), mock(TypeDeserializer.class));

        settableAnyProperty._throwAsIOE(new IllegalArgumentException("Test Error"), propName, value);
    }
}
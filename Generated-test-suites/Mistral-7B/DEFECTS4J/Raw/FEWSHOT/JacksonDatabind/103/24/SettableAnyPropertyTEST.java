package com.fasterxml.jackson.databind.deser;

import static com.fasterxml.jackson.core.JsonToken.*;
import static org.junit.Assert.*;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.impl.ReadableObjectId.Referring;
import com.fasterxml.jackson.databind.introspect.*;
import com.fasterxml.jackson.databind.jsontype.*;
import com.fasterxml.jackson.databind.util.ClassUtil;
import com.fasterxml.jackson.databind.deser.SettableAnyProperty;
import org.junit.Test;

import java.io.IOException;
import java.util.Map;

public class SettableAnyPropertyTest {

    @Test
    public void testSettableAnyProperty() {
        BeanProperty property = new BeanProperty("anyProperty", IntType.getInstance(), null, null);
        AnnotatedMember setter = Mockito.mock(AnnotatedField.class);
        JavaType type = IntType.getInstance();
        KeyDeserializer keyDeserializer = Mockito.mock(KeyDeserializer.class);
        JsonDeserializer<Object> valueDeserializer = Mockito.mock(JsonDeserializer.class);
        TypeDeserializer valueTypeDeserializer = Mockito.mock(TypeDeserializer.class);

        SettableAnyProperty anyProperty = new SettableAnyProperty(property, setter, type, keyDeserializer, valueDeserializer, valueTypeDeserializer);

        assertEquals("any property on class <unknown>", anyProperty.toString());
        assertEquals(property, anyProperty.getProperty());
        assertTrue(anyProperty.hasValueDeserializer());
        assertEquals(type, anyProperty.getType());
    }

    @Test
    public void testDeserializeAndSet() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        Map<Object, Object> map = new HashMap<>();
        BeanProperty property = Mockito.mock(BeanProperty.class);
        AnnotatedMember setter = Mockito.mock(AnnotatedField.class);
        JavaType type = IntType.getInstance();
        KeyDeserializer keyDeserializer = Mockito.mock(KeyDeserializer.class);
        JsonDeserializer<Object> valueDeserializer = Mockito.mock(JsonDeserializer.class);
        TypeDeserializer valueTypeDeserializer = Mockito.mock(TypeDeserializer.class);

        SettableAnyProperty anyProperty = new SettableAnyProperty(property, setter, type, keyDeserializer, valueDeserializer, valueTypeDeserializer);

        String json = "{\"name\": \"value\"}";
        JsonParser parser = mapper.getFactory().createParser(json);

        Object instance = new Object();
        String propName = "name";
        Object value = Integer.valueOf(1);

        when(keyDeserializer.deserializeKey(propName, anyProperty.deserializationContext())).thenReturn(propName);
        when(valueDeserializer.deserialize(parser, anyProperty.deserializationContext())).thenReturn(value);

        anyProperty.deserializeAndSet(parser, anyProperty.deserializationContext(), instance, propName);

        assertEquals(value, map.get(propName));
    }

    @Test
    public void testDeserializeAndSetWithTypeDeserializer() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        Map<Object, Object> map = new HashMap<>();
        BeanProperty property = Mockito.mock(BeanProperty.class);
        AnnotatedMember setter = Mockito.mock(AnnotatedField.class);
        JavaType type = MapType.construct(String.class, IntType.getInstance());
        KeyDeserializer keyDeserializer = Mockito.mock(KeyDeserializer.class);
        JsonDeserializer<Object> valueDeserializer = Mockito.mock(JsonDeserializer.class);
        TypeDeserializer valueTypeDeserializer = Mockito.mock(TypeDeserializer.class);

        SettableAnyProperty anyProperty = new SettableAnyProperty(property, setter, type, keyDeserializer, valueDeserializer, valueTypeDeserializer);

        String json = "{\"name\": 1, \"age\": 2}";
        JsonParser parser = mapper.getFactory().createParser(json);

        Object instance = new Object();
        String propName = "name";
        Object value = Integer.valueOf(1);

        JsonNode node = mapper.readTree(json);

        when(keyDeserializer.deserializeKey(propName, anyProperty.deserializationContext())).thenReturn(propName);
        when(valueDeserializer.deserialize(parser, anyProperty.deserializationContext())).thenReturn(value);
        when(valueTypeDeserializer.deserializeTyped(parser, anyProperty.deserializationContext(), type)).thenReturn(node);

        anyProperty.deserializeAndSet(parser, anyProperty.deserializationContext(), instance, propName);

        assertEquals(value, map.get(propName));
        assertEquals(node, map.get("age"));
    }

    @Test(expected = JsonMappingException.class)
    public void testDeserializeAndSetWithNullValueDeserializer() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        Map<Object, Object> map = new HashMap<>();
        BeanProperty property = Mockito.mock(BeanProperty.class);
        AnnotatedMember setter = Mockito.mock(AnnotatedField.class);
        JavaType type = IntType.getInstance();
        KeyDeserializer keyDeserializer = Mockito.mock(KeyDeserializer.class);
        JsonDeserializer<Object> valueDeserializer = null;
        TypeDeserializer valueTypeDeserializer = Mockito.mock(TypeDeserializer.class);

        SettableAnyProperty anyProperty = new SettableAnyProperty(property, setter, type, keyDeserializer, valueDeserializer, valueTypeDeserializer);

        Object instance = new Object();
        String propName = "name";

        JsonParser parser = mapper.getFactory().createParser("{}");

        anyProperty.deserializeAndSet(parser, anyProperty.deserializationContext(), instance, propName);

        fail("Expected JsonMappingException but got none.");
    }

    @Test
    public void testThrowAsIOE() throws IOException {
        BeanProperty property = Mockito.mock(BeanProperty.class);
        AnnotatedMember setter = Mockito.mock(AnnotatedField.class);
        JavaType type = IntType.getInstance();
        KeyDeserializer keyDeserializer = Mockito.mock(KeyDeserializer.class);
        JsonDeserializer<Object> valueDeserializer = Mockito.mock(JsonDeserializer.class);
        TypeDeserializer valueTypeDeserializer = Mockito.mock(TypeDeserializer.class);

        SettableAnyProperty anyProperty = new SettableAnyProperty(property, setter, type, keyDeserializer, valueDeserializer, valueTypeDeserializer);

        IllegalStateException exception = new IllegalStateException("Invalid state");

        try {
            anyProperty._throwAsIOE(exception, "name", Integer.valueOf(1));
        } catch (IOException e) {
            fail("Expected IllegalArgumentException but got IOException.");
        }

        anyProperty._throwAsIOE(new IllegalArgumentException("Invalid argument"), "name", Integer.valueOf(1));

        try {
            anyProperty._throwAsIOE(new JsonMappingException(null, "Error message", null), "name", Integer.valueOf(1));
        } catch (IOException e) {
            fail("Expected JsonMappingException but got IOException.");
        }

        try {
            anyProperty._throwAsIOE(new RuntimeException("Runtime error"), "name", Integer.valueOf(1));
        } catch (IOException e) {
            fail("Expected RuntimeException but got IOException.");
        }
    }
}
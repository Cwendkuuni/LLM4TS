package com.example.junit;

import static org.junit.Assert.*;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.*;
import com.fasterxml.jackson.databind.introspect.*;
import com.fasterxml.jackson.databind.jsontype.*;
import com.fasterxml.jackson.databind.util.*;

import org.junit.*;

/**
 * Test cases for SettableAnyProperty class.
 */
public class SettableAnyPropertyTest {

    @Test
    public void testConstructor() {
        BeanProperty property = Mockito.mock(BeanProperty.class);
        AnnotatedMember setter = Mockito.mock(AnnotatedMember.class);
        JavaType type = Mockito.mock(JavaType.class);
        KeyDeserializer keyDeserializer = Mockito.mock(KeyDeserializer.class);
        JsonDeserializer<Object> valueDeserializer = Mockito.mock(JsonDeserializer.class);
        TypeDeserializer typeDeserializer = Mockito.mock(TypeDeserializer.class);

        SettableAnyProperty settableAnyProperty = new SettableAnyProperty(property, setter, type, keyDeserializer, valueDeserializer, typeDeserializer);
        assertEquals(property, settableAnyProperty.getProperty());
        assertEquals(setter, settableAnyProperty._setter);
        assertEquals(type, settableAnyProperty._type);
        assertEquals(keyDeserializer, settableAnyProperty._keyDeserializer);
        assertEquals(valueDeserializer, settableAnyProperty._valueDeserializer);
        assertEquals(typeDeserializer, settableAnyProperty._valueTypeDeserializer);
    }

    @Test
    public void testDeserializeAndSet() throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree("{\"key\":\"value\"}");

        BeanProperty property = Mockito.mock(BeanProperty.class);
        AnnotatedMember setter = Mockito.mock(AnnotatedMember.class);
        JavaType type = Mockito.mock(JavaType.class);
        KeyDeserializer keyDeserializer = Mockito.mock(KeyDeserializer.class);
        JsonDeserializer<Object> valueDeserializer = Mockito.mock(JsonDeserializer.class);
        TypeDeserializer typeDeserializer = Mockito.mock(TypeDeserializer.class);
        Object instance = new Object();
        String propName = "key";

        when(keyDeserializer.deserializeKey(eq(propName), any(DeserializationContext.class)))
                .thenReturn(propName);

        SettableAnyProperty settableAnyProperty = new SettableAnyProperty(property, setter, type, keyDeserializer, valueDeserializer, typeDeserializer);

        settableAnyProperty.deserializeAndSet(Mockito.mock(JsonParser.class), Mockito.mock(DeserializationContext.class), instance, propName);

        Object value = "value";
        when(valueDeserializer.deserialize(any(JsonParser.class), any(DeserializationContext.class))).thenReturn(value);

        settableAnyProperty.deserializeAndSet(Mockito.mock(JsonParser.class), Mockito.mock(DeserializationContext.class), instance, propName);

        verify(setter, times(2)).callOnWith(instance, eq(propName), eq(value));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDeserializeAndSetNullValue() throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree("{}");

        BeanProperty property = Mockito.mock(BeanProperty.class);
        AnnotatedMember setter = Mockito.mock(AnnotatedMember.class);
        JavaType type = Mockito.mock(JavaType.class);
        KeyDeserializer keyDeserializer = Mockito.mock(KeyDeserializer.class);
        JsonDeserializer<Object> valueDeserializer = Mockito.mock(JsonDeserializer.class);
        TypeDeserializer typeDeserializer = Mockito.mock(TypeDeserializer.class);
        Object instance = new Object();
        String propName = "key";

        when(keyDeserializer.deserializeKey(eq(propName), any(DeserializationContext.class)))
                .thenReturn(propName);

        SettableAnyProperty settableAnyProperty = new SettableAnyProperty(property, setter, type, keyDeserializer, valueDeserializer, typeDeserializer);

        settableAnyProperty.deserializeAndSet(Mockito.mock(JsonParser.class), Mockito.mock(DeserializationContext.class), instance, propName);

        JsonToken token = Mockito.mock(JsonToken.class);
        when(token.isNull()).thenReturn(true);
        when(jsonNode.has(eq("key"))).thenReturn(true);
        when(jsonNode.findValue("key")).thenReturn(token);

        settableAnyProperty.deserializeAndSet(Mockito.mock(JsonParser.class), Mockito.mock(DeserializationContext.class), instance, propName);
    }

    @Test(expected = IOException.class)
    public void testDeserializeWithTypeDeserializer() throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree("{\"key\":1}");

        BeanProperty property = Mockito.mock(BeanProperty.class);
        AnnotatedMember setter = Mockito.mock(AnnotatedMember.class);
        JavaType type = Mockito.mock(JavaType.class);
        KeyDeserializer keyDeserializer = Mockito.mock(KeyDeserializer.class);
        JsonDeserializer<Object> valueDeserializer = Mockito.mock(JsonDeserializer.class);
        TypeDeserializer typeDeserializer = Mockito.mock(TypeDeserializer.class);
        when(typeDeserializer.deserializeTypedObject(any(JsonParser.class), any(DeserializationContext.class), eq(type)))
                .thenThrow(new IOException());

        Object instance = new Object();
        String propName = "key";

        SettableAnyProperty settableAnyProperty = new SettableAnyProperty(property, setter, type, keyDeserializer, valueDeserializer, typeDeserializer);

        settableAnyProperty.deserializeAndSet(Mockito.mock(JsonParser.class), Mockito.mock(DeserializationContext.class), instance, propName);
    }

    @Test(expected = JsonMappingException.class)
    public void testDeserializeWithUnresolvedForwardReference() throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree("{\"key\":{}}");

        BeanProperty property = Mockito.mock(BeanProperty.class);
        AnnotatedMember setter = Mockito.mock(AnnotatedMember.class);
        JavaType type = Mockito.mock(JavaType.class);
        KeyDeserializer keyDeserializer = Mockito.mock(KeyDeserializer.class);
        JsonDeserializer<Object> valueDeserializer = Mockito.mock(JsonDeserializer.class);
        TypeDeserializer typeDeserializer = Mockito.mock(TypeDeserializer.class);

        UnresolvedForwardReference reference = Mockito.mock(UnresolvedForwardReference.class);
        when(reference.getRoid()).thenReturn(Mockito.mock(Referenceable.class));

        SettableAnyProperty settableAnyProperty = new SettableAnyProperty(property, setter, type, keyDeserializer, valueDeserializer, typeDeserializer);

        settableAnyProperty.deserializeAndSet(Mockito.mock(JsonParser.class), Mockito.mock(DeserializationContext.class),
                new Object(), "key");

        when(valueDeserializer.getObjectIdReader()).thenReturn(null);

        settableAnyProperty.deserializeAndSet(Mockito.mock(JsonParser.class), Mockito.mock(DeserializationContext.class),
                new Object(), "key");
    }
}
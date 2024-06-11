package com.fasterxml.jackson.databind.deser.test;

import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.Map;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.KeyDeserializer;
import com.fasterxml.jackson.databind.deser.impl.ReadableObjectId.Referring;
import com.fasterxml.jackson.databind.introspect.AnnotatedField;
import com.fasterxml.jackson.databind.introspect.AnnotatedMethod;
import com.fasterxml.jackson.databind.introspect.BeanProperty;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.util.ClassUtil;
import com.fasterxml.jackson.test.BaseTest;

public class SettableAnyPropertyTest extends BaseTest {

    private static class TestSettableAnyProperty {
        BeanProperty property;
        AnnotatedMember setter;
        JavaType type;
        KeyDeserializer keyDeserializer;
        JsonDeserializer<Object> valueDeserializer;
        TypeDeserializer valueTypeDeserializer;

        TestSettableAnyProperty(BeanProperty property, AnnotatedMember setter, JavaType type,
                KeyDeserializer keyDeserializer, JsonDeserializer<Object> valueDeserializer,
                TypeDeserializer valueTypeDeserializer) {
            this.property = property;
            this.setter = setter;
            this.type = type;
            this.keyDeserializer = keyDeserializer;
            this.valueDeserializer = valueDeserializer;
            this.valueTypeDeserializer = valueTypeDeserializer;
        }
    }

    @org.junit.Test
    public void testSettableAnyPropertyConstruction() throws Exception {
        BeanProperty property = mock(BeanProperty.class);
        AnnotatedMember setter = mock(AnnotatedMember.class);
        JavaType type = mock(JavaType.class);
        KeyDeserializer keyDeserializer = mock(KeyDeserializer.class);
        JsonDeserializer<Object> valueDeserializer = mock(JsonDeserializer.class);
        TypeDeserializer valueTypeDeserializer = mock(TypeDeserializer.class);

        SettableAnyProperty settableAnyProperty = new SettableAnyProperty(property, setter, type, keyDeserializer,
                valueDeserializer, valueTypeDeserializer);

        assertNotNull(settableAnyProperty);
    }

    @org.junit.Test
    public void testSettableAnyPropertyWithValueDeserializer() throws Exception {
        BeanProperty property = mock(BeanProperty.class);
        AnnotatedMember setter = mock(AnnotatedMember.class);
        JavaType type = mock(JavaType.class);
        KeyDeserializer keyDeserializer = mock(KeyDeserializer.class);
        JsonDeserializer<Object> valueDeserializer = mock(JsonDeserializer.class);
        TypeDeserializer valueTypeDeserializer = mock(TypeDeserializer.class);

        SettableAnyProperty settableAnyProperty = new SettableAnyProperty(property, setter, type, keyDeserializer,
                valueDeserializer, valueTypeDeserializer);

        SettableAnyProperty newSettableAnyProperty = settableAnyProperty.withValueDeserializer(mock(JsonDeserializer.class));

        assertNotNull(newSettableAnyProperty);
    }

    @org.junit.Test
    public void testDeserializeAndSet() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        JsonFactory jsonFactory = mapper.getDefaultFactory();

        BeanProperty property = mock(BeanProperty.class);
        AnnotatedMember setter = mock(AnnotatedMember.class);
        JavaType type = mapper.getTypeFactory().constructType(Map.class);
        KeyDeserializer keyDeserializer = mock(KeyDeserializer.class);
        JsonDeserializer<Object> valueDeserializer = mock(JsonDeserializer.class);
        TypeDeserializer valueTypeDeserializer = mock(TypeDeserializer.class);

        SettableAnyProperty settableAnyProperty = new SettableAnyProperty(property, setter, type, keyDeserializer,
                valueDeserializer, valueTypeDeserializer);

        Object pojo = new Object();

        String propertyName = "testProperty";
        String jsonString = "{\"testProperty\":\"testValue\"}";

        JsonParser parser = jsonFactory.createParser(jsonString);

        when(keyDeserializer.deserializeKey(propertyName, any(DeserializationContext.class)))
                .thenReturn(propertyName);

        when(valueDeserializer.deserialize(parser, any(DeserializationContext.class))).thenReturn("testValue");

        settableAnyProperty.deserializeAndSet(parser, mock(DeserializationContext.class), pojo, propertyName);

        Map<Object, Object> map = (Map<Object, Object>) setter.getValue(pojo);

        assertEquals("testValue", map.get(propertyName));
    }

    @org.junit.Test(expected = IOException.class)
    public void testDeserializeAndSetWithNullValueDeserializer() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        JsonFactory jsonFactory = mapper.getDefaultFactory();

        BeanProperty property = mock(BeanProperty.class);
        AnnotatedMember setter = mock(AnnotatedMember.class);
        JavaType type = mapper.getTypeFactory().constructType(Map.class);
        KeyDeserializer keyDeserializer = mock(KeyDeserializer.class);
        JsonDeserializer<Object> valueDeserializer = null;
        TypeDeserializer valueTypeDeserializer = mock(TypeDeserializer.class);

        SettableAnyProperty settableAnyProperty = new SettableAnyProperty(property, setter, type, keyDeserializer,
                valueDeserializer, valueTypeDeserializer);

        Object pojo = new Object();

        String propertyName = "testProperty";
        String jsonString = "{\"testProperty\":null}";

        JsonParser parser = jsonFactory.createParser(jsonString);

        settableAnyProperty.deserializeAndSet(parser, mock(DeserializationContext.class), pojo, propertyName);

        fail("Expected IOException");
    }

    @org.junit.Test(expected = IOException.class)
    public void testDeserializeAndSetWithUnresolvedForwardReference() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        JsonFactory jsonFactory = mapper.getDefaultFactory();

        BeanProperty property = mock(BeanProperty.class);
        AnnotatedMember setter = mock(AnnotatedMember.class);
        JavaType type = mapper.getTypeFactory().constructType(Map.class);
        KeyDeserializer keyDeserializer = mock(KeyDeserializer.class);
        JsonDeserializer<Object> valueDeserializer = mock(JsonDeserializer.class);
        TypeDeserializer valueTypeDeserializer = mock(TypeDeserializer.class);

        SettableAnyProperty settableAnyProperty = new SettableAnyProperty(property, setter, type, keyDeserializer,
                valueDeserializer, valueTypeDeserializer);

        Object pojo = new Object();

        String propertyName = "testProperty";
        String jsonString = "{\"testProperty\":\"$ref\"}";

        JsonParser parser = jsonFactory.createParser(jsonString);

        when(keyDeserializer.deserializeKey(propertyName, any(DeserializationContext.class)))
                .thenReturn(propertyName);

        UnresolvedForwardReference reference = mock(UnresolvedForwardReference.class);

        when(valueDeserializer.getObjectIdReader()).thenReturn(mock(ObjectIdReader.class));

        settableAnyProperty.deserializeAndSet(parser, mock(DeserializationContext.class), pojo, propertyName);

        fail("Expected IOException");
    }
}
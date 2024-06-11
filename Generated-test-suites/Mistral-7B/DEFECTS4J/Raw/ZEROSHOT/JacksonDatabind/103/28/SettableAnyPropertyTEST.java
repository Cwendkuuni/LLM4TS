package com.fasterxml.jackson.databind.deser.test;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.*;
import com.fasterxml.jackson.databind.introspect.AnnotatedField;
import com.fasterxml.jackson.databind.introspect.AnnotatedMethod;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.util.ClassUtil;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class SettableAnyPropertyTest {

    private Object testInstance;
    private BeanProperty property;
    private AnnotatedMember setter;
    private JavaType type;
    private KeyDeserializer keyDeserializer;
    private JsonDeserializer<Object> valueDeserializer;
    private TypeDeserializer valueTypeDeserializer;

    @Before
    public void setUp() throws Exception {
        property = Mockito.mock(BeanProperty.class);
        setter = Mockito.mock(AnnotatedMember.class);
        type = Mockito.mock(JavaType.class);
        keyDeserializer = Mockito.mock(KeyDeserializer.class);
        valueDeserializer = Mockito.mock(JsonDeserializer.class);
        valueTypeDeserializer = Mockito.mock(TypeDeserializer.class);

        testInstance = new HashMap<>();

        Mockito.when(property.getName()).thenReturn("testProperty");
        Mockito.when(setter.getDeclaringClass()).thenReturn(TestClass.class);
        Mockito.when(setter.getAnnotated()).thenReturn(setter);

        SettableAnyProperty settableAnyProperty = new SettableAnyProperty(property, setter, type, keyDeserializer, valueDeserializer, valueTypeDeserializer);

        SettableAnyProperty settableAnyPropertyNullKey = new SettableAnyProperty(property, setter, type, null, valueDeserializer, valueTypeDeserializer);

        SettableAnyProperty settableAnyPropertyValueDeserializerNull = new SettableAnyProperty(property, setter, type, keyDeserializer, null, valueTypeDeserializer);

        SettableAnyProperty settableAnyPropertyTypeDeserializerNull = new SettableAnyProperty(property, setter, type, keyDeserializer, valueDeserializer, null);

        SettableAnyProperty settableAnyPropertyKeyDeserializerNull = new SettableAnyProperty(property, setter, type, null, valueDeserializer, valueTypeDeserializer);

        SettableAnyProperty settableAnyPropertyBothNull = new SettableAnyProperty(property, setter, null, keyDeserializer, valueDeserializer, valueTypeDeserializer);

        SettableAnyProperty settableAnyPropertyValueSetterIsField = new SettableAnyProperty(property, (AnnotatedField) setter, type, keyDeserializer, valueDeserializer, valueTypeDeserializer);
    }

    @Test
    public void testConstructor() {
        assertNotNull(settableAnyProperty);
        assertEquals(property, settableAnyProperty.getProperty());
        assertEquals(setter, settableAnyProperty.getSetter());
        assertEquals(type, settableAnyProperty.getType());
        assertEquals(keyDeserializer, settableAnyProperty.getKeyDeserializer());
        assertEquals(valueDeserializer, settableAnyProperty.getValueDeserializer());
        assertEquals(valueTypeDeserializer, settableAnyProperty.getValueTypeDeserializer());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorNullType() {
        new SettableAnyProperty(property, setter, null, keyDeserializer, valueDeserializer, valueTypeDeserializer);
    }

    @Test
    public void testDeserializeAndSetNullKey() throws IOException {
        JsonParser jsonParser = Mockito.mock(JsonParser.class);
        DeserializationContext deserializationContext = Mockito.mock(DeserializationContext.class);

        settableAnyPropertyNullKey.deserializeAndSet(jsonParser, deserializationContext, testInstance, "key");

        Mockito.verify(deserializationContext, Mockito.times(0)).handleSecondaryException(Mockito.any());
    }

    @Test
    public void testDeserializeAndSetValueDeserializerNull() throws IOException {
        JsonParser jsonParser = Mockito.mock(JsonParser.class);
        DeserializationContext deserializationContext = Mockito.mock(DeserializationContext.class);

        Object value = Mockito.mock(Object.class);

        settableAnyPropertyValueDeserializerNull.deserializeAndSet(jsonParser, deserializationContext, testInstance, "key");

        Mockito.verify(deserializationContext).handleSecondaryException(Mockito.eq(JsonMappingException.class));
    }

    @Test
    public void testDeserializeAndSetTypeDeserializerNull() throws IOException {
        JsonParser jsonParser = Mockito.mock(JsonParser.class);
        DeserializationContext deserializationContext = Mockito.mock(DeserializationContext.class);

        Object value = Mockito.mock(Object.class);

        JsonToken jsonToken = JsonToken.VALUE_STRING;
        Mockito.when(jsonParser.getCurrentToken()).thenReturn(jsonToken);

        settableAnyPropertyTypeDeserializerNull.deserializeAndSet(jsonParser, deserializationContext, testInstance, "key");

        Mockito.verify(deserializationContext).handleSecondaryException(Mockito.eq(JsonMappingException.class));
    }

    @Test
    public void testDeserializeAndSetKeyDeserializerNull() throws IOException {
        JsonParser jsonParser = Mockito.mock(JsonParser.class);
        DeserializationContext deserializationContext = Mockito.mock(DeserializationContext.class);

        Object key = Mockito.mock(Object.class);
        Object value = Mockito.mock(Object.class);

        settableAnyPropertyKeyDeserializerNull.deserializeAndSet(jsonParser, deserializationContext, testInstance, "key");

        Mockito.verify(jsonParser).nextToken();
        Mockito.verify(deserializationContext).handleSecondaryException(Mockito.eq(JsonMappingException.class));
    }

    @Test
    public void testDeserializeAndSetBothNull() throws IOException {
        JsonParser jsonParser = Mockito.mock(JsonParser.class);
        DeserializationContext deserializationContext = Mockito.mock(DeserializationContext.class);

        settableAnyPropertyBothNull.deserializeAndSet(jsonParser, deserializationContext, testInstance, "key");

        Mockito.verify(deserializationContext).handleSecondaryException(Mockito.eq(JsonMappingException.class));
    }

    @Test
    public void testDeserializeAndSetValueSetterIsField() throws IOException {
        JsonParser jsonParser = Mockito.mock(JsonParser.class);
        DeserializationContext deserializationContext = Mockito.mock(DeserializationContext.class);

        Object key = Mockito.mock(Object.class);
        Object value = Mockito.mock(Object.class);

        Mockito.when(jsonParser.getText()).thenReturn("testValue");

        settableAnyPropertyValueSetterIsField.deserializeAndSet(jsonParser, deserializationContext, testInstance, key);

        Mockito.verify(testInstance).put(key, value);
    }

    @Test
    public void testSet() throws IOException {
        Object key = Mockito.mock(Object.class);
        Object value = Mockito.mock(Object.class);

        settableAnyProperty.set(testInstance, key, value);

        Mockito.verify(testInstance).put(key, value);
    }
}

class TestClass {
    private Map<Object,Object> testMap;

    public Map<Object, Object> getTestMap() {
        return testMap;
    }
}
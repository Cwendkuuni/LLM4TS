package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.lang.reflect.Method;

import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.NullValueProvider;
import com.fasterxml.jackson.databind.deser.SettableBeanProperty;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.introspect.AnnotatedMethod;
import com.fasterxml.jackson.databind.introspect.BeanPropertyDefinition;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.util.Annotations;
import com.fasterxml.jackson.databind.util.BeanUtil;

public class SetterlessPropertyTest {

    @SuppressWarnings("unchecked")
    @Test
    public void testSetterlessProperty() throws Exception {
        Class<?> clazz = SetterlessProperty.class;
        Object instance = mock(clazz);
        Method getter = mock(Method.class);
        when(getter.invoke(instance, new Object[0])).thenReturn(new ArrayList<>());
        JsonParser parser = mock(JsonParser.class);
        when(parser.getCurrentToken()).thenReturn(JsonToken.START_ARRAY);
        Object value = new ArrayList<>();

        SettableBeanProperty setterlessProperty = new SetterlessProperty(
                new BeanPropertyDefinition(clazz.getDeclaredField("list"), null, null),
                JsonTypeFactory.instance.constructCollectionType(ArrayList.class), null, null, getter, null);

        setterlessProperty.deserializeAndSet(parser, new DeserializationContext(), instance);
        assertEquals(value, BeanUtil.getFieldValue(instance, "list"));

        when(parser.getCurrentToken()).thenReturn(JsonToken.VALUE_NULL);
        setterlessProperty.deserializeAndSet(parser, new DeserializationContext(), instance);
        assertNull(BeanUtil.getFieldValue(instance, "list"));

        when(parser.getCurrentToken()).thenReturn(JsonToken.VALUE_STRING);
        String jsonValue = "[]";
        when(parser.readValueAs(ArrayList.class)).thenReturn(new ArrayList<>());
        setterlessProperty.deserializeAndSet(parser, new DeserializationContext(), instance);
        assertEquals(jsonValue, BeanUtil.getFieldValue(instance, "list").toString());

        when(parser.getCurrentToken()).thenReturn(JsonToken.START_OBJECT);
        Object nestedObject = new Object();
        when(parser.readValueAs(Object.class)).thenReturn(nestedObject);
        setterlessProperty.deserializeAndSet(parser, new DeserializationContext(), instance);
        assertNull(BeanUtil.getFieldValue(instance, "list"));
        assertEquals(nestedObject, BeanUtil.getFieldValue(instance, "map"));

        when(parser.getCurrentToken()).thenReturn(JsonToken.START_ARRAY);
        JsonToken[] expectedTokens = new JsonToken[]{JsonToken.VALUE_NULL, JsonToken.START_OBJECT, JsonToken.FIELD_NAME, JsonToken.VALUE_STRING, JsonToken.VALUE_NULL, JsonToken.VALUE_STRING, JsonToken.END_OBJECT, JsonToken.END_ARRAY};
        when(parser.tokens()).thenReturn(expectedTokens);
        setterlessProperty.deserializeAndSet(parser, new DeserializationContext(), instance);
        assertNull(BeanUtil.getFieldValue(instance, "list"));
        assertNull(BeanUtil.getFieldValue(instance, "map"));

        when(parser.getCurrentToken()).thenReturn(JsonToken.VALUE_STRING);
        String jsonValueWithAnnotation = "[{\"key\":\"value\"}]";
        when(parser.readValueAs(ArrayList.class)).thenReturn(new ArrayList<>());
        JsonSetter jsonSetter = mock(JsonSetter.class);
        when(jsonSetter.value()).thenReturn("value");
        when(instance.getClass().getDeclaredField("map"))
                .thenReturn(Annotations.findValue(instance, JsonSetter.class));
        setterlessProperty.deserializeAndSet(parser, new DeserializationContext(), instance);
        assertEquals("value", BeanUtil.getFieldValue(instance, "map").get("key"));

        when(parser.getCurrentToken()).thenReturn(JsonToken.VALUE_NULL);
        setterlessProperty.deserializeAndSet(parser, new DeserializationContext(), instance);
        assertEquals(0, BeanUtil.getFieldValue(instance, "map").size());
    }
}
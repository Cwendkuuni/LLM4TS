package com.fasterxml.jackson.databind.deser.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonParser;
import com.fasterxml.jackson.databind.NullValueProvider;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyName;
import com.fasterxml.jackson.databind.deser.BeanDeserializerBase;
import com.fasterxml.jackson.databind.deser.SettableBeanProperty;
import com.fasterxml.jackson.databind.introspect.AnnotatedMethod;
import com.fasterxml.jackson.databind.introspect.MethodIntrospector;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.util.Annotations;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class MethodPropertyTest {

    private ObjectMapper objectMapper;
    private MethodProperty methodProperty;
    private AnnotatedMethod annotatedMethod;
    private Method setter;

    @Before
    public void setup() throws Exception {
        objectMapper = new ObjectMapper();
        Annotation[] annotations = new Annotation[]{new Annotation(){}};
        MethodIntrospector methodIntrospector = Mockito.mock(MethodIntrospector.class);
        Class<?> targetClass = Object.class;
        setter = Mockito.mock(Method.class);
        Mockito.when(methodIntrospector.findMethod(ArgumentMatchers.anyString(), ArgumentMatchers.eq(targetClass)))
                .thenReturn(setter);
        annotatedMethod = Mockito.mock(AnnotatedMethod.class);
        Mockito.when(annotatedMethod.getAnnotations())
                .thenReturn(annotations);
        Mockito.when(annotatedMethod.getAnnotated())
                .thenReturn(annotatedMethod);
        methodProperty = new MethodProperty(null, null, null, null, annotatedMethod, setter);
    }

    @Test
    public void testDeserializeAndSet() throws JsonProcessingException {
        Object instance = new Object();
        JsonParser parser = Mockito.mock(JsonParser.class);
        DeserializationContext context = Mockito.mock(DeserializationContext.class);
        Object expectedValue = new Object();
        Mockito.when(parser.getValueAsToken())
                .thenReturn(JsonToken.VALUE_STRING);
        Mockito.when(parser.getText())
                .thenReturn("expectedValue");
        Mockito.when(parser.nextToken())
                .thenReturn(JsonToken.FIELD_NAME);
        Mockito.when(parser.getCurrentName())
                .thenReturn(new PropertyName("testProperty"));
        JsonDeserializer<Object> valueDeserializer = Mockito.mock(JsonDeserializer.class);
        Mockito.when(valueDeserializer.deserialize(ArgumentMatchers.any(JsonParser.class), ArgumentMatchers.eq(context)))
                .thenReturn(expectedValue);
        JsonDeserializer<Object> nullValueProvider = Mockito.mock(JsonDeserializer.class);
        Mockito.when(nullValueProvider.getNullValue(context))
                .thenReturn(null);
        BeanDeserializerBase beanDeserializerBase = Mockito.mock(BeanDeserializerBase.class);
        Mockito.when(beanDeserializerBase.findProperty(ArgumentMatchers.eq("testProperty")))
                .thenReturn(methodProperty);
        Mockito.when(beanDeserializerBase.getValueDeserializer())
                .thenReturn(valueDeserializer);
        Mockito.when(beanDeserializerBase.getNullValueProvider())
                .thenReturn(nullValueProvider);
        TypeDeserializer typeDeserializer = Mockito.mock(TypeDeserializer.class);
        Mockito.when(objectMapper.findTypeDeserializer(ArgumentMatchers.any(Class.class), ArgumentMatchers.eq(context)))
                .thenReturn(typeDeserializer);

        methodProperty.deserializeAndSet(parser, context, instance);
        assertEquals(expectedValue, instance.getClass().getDeclaredField("testProperty").get(instance));
    }

    @Test
    public void testDeserializeSetAndReturn() throws JsonProcessingException {
        Object instance = new Object();
        JsonParser parser = Mockito.mock(JsonParser.class);
        DeserializationContext context = Mockito.mock(DeserializationContext.class);
        Object expectedValue = new Object();
        Mockito.when(parser.getValueAsToken())
                .thenReturn(JsonToken.VALUE_STRING);
        Mockito.when(parser.getText())
                .thenReturn("expectedValue");
        Mockito.when(parser.nextToken())
                .thenReturn(JsonToken.FIELD_NAME);
        Mockito.when(parser.getCurrentName())
                .thenReturn(new PropertyName("testProperty"));
        JsonDeserializer<Object> valueDeserializer = Mockito.mock(JsonDeserializer.class);
        Mockito.when(valueDeserializer.deserialize(ArgumentMatchers.any(JsonParser.class), ArgumentMatchers.eq(context)))
                .thenReturn(expectedValue);
        JsonDeserializer<Object> nullValueProvider = Mockito.mock(JsonDeserializer.class);
        Mockito.when(nullValueProvider.getNullValue(context))
                .thenReturn(null);
        BeanDeserializerBase beanDeserializerBase = Mockito.mock(BeanDeserializerBase.class);
        Mockito.when(beanDeserializerBase.findProperty(ArgumentMatchers.eq("testProperty")))
                .thenReturn(methodProperty);
        Mockito.when(beanDeserializerBase.getValueDeserializer())
                .thenReturn(valueDeserializer);
        Mockito.when(beanDeserializerBase.getNullValueProvider())
                .thenReturn(nullValueProvider);
        TypeDeserializer typeDeserializer = Mockito.mock(TypeDeserializer.class);
        Mockito.when(objectMapper.findTypeDeserializer(ArgumentMatchers.any(Class.class), ArgumentMatchers.eq(context)))
                .thenReturn(typeDeserializer);

        Object result = methodProperty.deserializeSetAndReturn(parser, context, instance);
        assertEquals(expectedValue, result);
    }

    @Test
    public void testSet() throws JsonProcessingException {
        Object instance = new Object();
        Object value = new Object();

        methodProperty.set(instance, value);
        assertEquals(value, instance.getClass().getDeclaredField("testProperty").get(instance));
    }

    @Test
    public void testSetAndReturn() throws JsonProcessingException {
        Object instance = new Object();
        Object value = new Object();

        Object result = methodProperty.setAndReturn(instance, value);
        assertEquals(value, result);
        assertEquals(value, instance.getClass().getDeclaredField("testProperty").get(instance));
    }

    @Test
    public void testDeserializeNullValue() throws JsonProcessingException {
        Object instance = new Object();
        JsonParser parser = Mockito.mock(JsonParser.class);
        DeserializationContext context = Mockito.mock(DeserializationContext.class);
        Object expectedNullValue = null;
        Mockito.when(parser.getValueAsToken())
                .thenReturn(JsonToken.VALUE_NULL);
        JsonDeserializer<Object> nullValueProvider = Mockito.mock(JsonDeserializer.class);
        Mockito.when(nullValueProvider.getNullValue(context))
                .thenReturn(expectedNullValue);
        BeanDeserializerBase beanDeserializerBase = Mockito.mock(BeanDeserializerBase.class);
        Mockito.when(beanDeserializerBase.findProperty(ArgumentMatchers.eq("testProperty")))
                .thenReturn(methodProperty);
        Mockito.when(beanDeserializerBase.getValueDeserializer())
                .thenReturn(null);
        Mockito.when(beanDeserializerBase.getNullValueProvider())
                .thenReturn(nullValueProvider);

        methodProperty.deserializeAndSet(parser, context, instance);
        assertNull(instance.getClass().getDeclaredField("testProperty").get(instance));
    }
}
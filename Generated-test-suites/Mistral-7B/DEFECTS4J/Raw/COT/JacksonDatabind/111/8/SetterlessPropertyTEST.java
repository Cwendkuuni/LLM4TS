package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.*;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.*;
import com.fasterxml.jackson.databind.introspect.*;
import com.fasterxml.jackson.databind.jsontype.*;
import com.fasterxml.jackson.databind.util.*;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;

import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class SetterlessPropertyTest {

    @Test
    public void testConstructor() {
        BeanPropertyDefinition propDef = mock(BeanPropertyDefinition.class);
        JavaType type = mock(JavaType.class);
        TypeDeserializer typeDeser = mock(TypeDeserializer.class);
        Annotations contextAnnotations = mock(Annotations.class);
        AnnotatedMethod method = mock(AnnotatedMethod.class);

        SetterlessProperty setterlessProperty = new SetterlessProperty(propDef, type, typeDeser, contextAnnotations, method);

        assertEquals(propDef, setterlessProperty.getPropertyDefinition());
        assertEquals(type, setterlessProperty.getType());
        assertEquals(typeDeser, setterlessProperty.getTypeDeserializer());
        assertEquals(contextAnnotations, setterlessProperty.getAnnotationIntrospector().getContextAnnotations());
        assertEquals(method, setterlessProperty._annotated);
        assertEquals(method.getAnnotated(), setterlessProperty._getter);
    }

    @Test
    public void testWithName() {
        String newName = "newName";
        SetterlessProperty setterlessProperty = new SetterlessProperty(mock(SetterlessProperty.class), newName);

        assertEquals(newName, setterlessProperty.getName());
    }

    @Test
    public void testWithValueDeserializer() {
        JsonDeserializer<?> deserializer = mock(JsonDeserializer.class);
        SetterlessProperty setterlessProperty = new SetterlessProperty(mock(SetterlessProperty.class), deserializer, null);

        assertEquals(deserializer, setterlessProperty.getValueDeserializer());
    }

    @Test
    public void testWithNullProvider() {
        NullValueProvider nullValueProvider = mock(NullValueProvider.class);
        SetterlessProperty setterlessProperty = new SetterlessProperty(mock(SetterlessProperty.class), null, nullValueProvider);

        assertEquals(nullValueProvider, setterlessProperty.getNullProvider());
    }

    @Test
    public void testFixAccess() {
        DeserializationConfig config = mock(DeserializationConfig.class);
        SetterlessProperty setterlessProperty = new SetterlessProperty(mock(SetterlessProperty.class), null, null, null, null);

        setterlessProperty.fixAccess(config);
        verify(config).isEnabled(MapperFeature.OVERRIDE_PUBLIC_ACCESS_MODIFIERS);
    }

    @Test
    public void testGetAnnotation() {
        Class<Annotation> annotationClass = mock(Annotation.class);
        AnnotatedMember annotatedMember = mock(AnnotatedMember.class);

        Object annotation = setterlessProperty.getAnnotation(annotationClass);

        verify(annotatedMember).getAnnotation(annotationClass);
        assertEquals(annotation, annotationClass.cast(annotatedMember.getAnnotation()));
    }

    @Test
    public void testGetMember() {
        Object instance = mock(Object.class);
        Object propertyValue = mock(Object.class);

        when(setterlessProperty._getter.invoke(instance, (Object[]) null)).thenReturn(propertyValue);

        Object getValue = setterlessProperty.getMember().getValue();

        assertEquals(propertyValue, getValue);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSet() throws IOException {
        Object instance = mock(Object.class);
        Object value = mock(Object.class);

        setterlessProperty.set(instance, value);

        fail("Should not call `set()` on setterless property");
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSetAndReturn() throws IOException {
        Object instance = mock(Object.class);
        Object value = mock(Object.class);

        setterlessProperty.setAndReturn(instance, value);

        fail("Should not call `set()` on setterless property");
    }

    @Test
    public void testDeserializeAndSet() throws IOException {
        Object instance = mock(Object.class);
        JsonParser parser = mock(JsonParser.class);
        DeserializationContext context = mock(DeserializationContext.class);
        Object propertyValue = mock(Object.class);

        when(parser.getCurrentToken()).thenReturn(JsonToken.VALUE_STRING);
        when(setterlessProperty._getter.invoke(instance, (Object[]) null)).thenReturn(propertyValue);
        when(setterlessProperty._valueDeserializer.deserialize(parser, context, propertyValue)).thenReturn(propertyValue);

        setterlessProperty.deserializeAndSet(parser, context, instance);

        verify(parser).nextToken();
        verify(parser).getCurrentToken();
        verify(instance).getClass();
        verify(propertyValue).getClass();
        verifyZeroInteractions(parser, context);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testDeserializeSetAndReturn() throws IOException {
        Object instance = mock(Object.class);
        JsonParser parser = mock(JsonParser.class);
        DeserializationContext context = mock(DeserializationContext.class);

        setterlessProperty.deserializeSetAndReturn(parser, context, instance);

        fail("Should not call `set()` on setterless property");
    }

    @Test(expected = IOException.class)
    public void testDeserializeAndSetWithNullValue() throws IOException {
        Object instance = mock(Object.class);
        JsonParser parser = mock(JsonParser.class);
        DeserializationContext context = mock(DeserializationContext.class);

        when(parser.getCurrentToken()).thenReturn(JsonToken.VALUE_NULL);
        doThrow(new IOException("Problem deserializing 'setterless' property")).when(setterlessProperty)
                .deserializeAndSet(parser, context, instance);

        setterlessProperty.deserializeAndSet(parser, context, instance);

        verify(parser).nextToken();
        verify(parser).getCurrentToken();
        verify(parser, times(1)).nextValue();
        verify(instance, never()).getClass();
    }

    @Test(expected = IOException.class)
    public void testDeserializeAndSetWithInvalidType() throws IOException {
        Object instance = mock(Object.class);
        JsonParser parser = mock(JsonParser.class);
        DeserializationContext context = mock(DeserializationContext.class);
        Object propertyValue = mock(Object.class);

        when(parser.getCurrentToken()).thenReturn(JsonToken.VALUE_STRING);
        when(setterlessProperty._getter.invoke(instance, (Object[]) null)).thenReturn(propertyValue);
        doThrow(new IOException("Problem deserializing 'setterless' property (invalid type)"))
                .when(setterlessProperty).deserializeAndSet(parser, context, instance);

        setterlessProperty.deserializeAndSet(parser, context, instance);

        verify(parser).nextToken();
        verify(parser).getCurrentToken();
        verify(instance).getClass();
        verify(parser).close();
    }

    @Test
    public void testDeserializeAndSetWithNullPropertyValue() throws IOException {
        Object instance = mock(Object.class);
        JsonParser parser = mock(JsonParser.class);
        DeserializationContext context = mock(DeserializationContext.class);

        when(parser.getCurrentToken()).thenReturn(JsonToken.VALUE_NULL);
        when(setterlessProperty._getter.invoke(instance, (Object[]) null)).thenReturn(null);
        when(setterlessProperty._nullProvider.findNullValue(parser, context, null)).thenReturn(null);

        setterlessProperty.deserializeAndSet(parser, context, instance);

        verify(parser).nextToken();
        verify(parser).getCurrentToken();
        verify(instance).getClass();
        verifyZeroInteractions(parser, context);
    }

    @Test
    public void testDeserializeAndSetWithEmptyCollection() throws IOException {
        Object instance = mock(Object.class);
        JsonParser parser = mock(JsonParser.class);
        DeserializationContext context = mock(DeserializationContext.class);
        Object propertyValue = mock(Object[].class);

        when(parser.getCurrentToken()).thenReturn(JsonToken.START_ARRAY);
        when(parser.nextValue()).thenReturn(null);
        when(parser.nextValue()).thenReturn(null);
        when(setterlessProperty._getter.invoke(instance, (Object[]) null)).thenReturn(propertyValue);
        when(setterlessProperty._valueDeserializer.deserializeValueAsStartArray(parser, context, null)).thenReturn(propertyValue);

        setterlessProperty.deserializeAndSet(parser, context, instance);

        verify(parser).nextToken();
        verify(parser).nextValue();
        verify(parser).nextValue();
        verifyZeroInteractions(parser, context);
    }

    @Test
    public void testDeserializeAndSetWithSingleElementCollection() throws IOException {
        Object instance = mock(Object.class);
        JsonParser parser = mock(JsonParser.class);
        DeserializationContext context = mock(DeserializationContext.class);
        Object propertyValue = mock(Object.class);

        when(parser.getCurrentToken()).thenReturn(JsonToken.START_ARRAY);
        when(parser.nextValue()).thenReturn(JsonToken.VALUE_STRING);
        when(parser.nextValue()).thenReturn(null);
        when(parser.nextToken()).thenReturn(JsonToken.END_ARRAY);
        when(setterlessProperty._getter.invoke(instance, (Object[]) null)).thenReturn(propertyValue);
        when(setterlessProperty._valueDeserializer.deserializeValueAsString(parser, context, null)).thenReturn("element");

        setterlessProperty.deserializeAndSet(parser, context, instance);

        verify(parser).nextToken();
        verify(parser).nextValue();
        verify(parser).nextValue();
        verify(parser).nextToken();
        verify(instance).getClass();
        verifyZeroInteractions(parser, context);
    }

    @Test
    public void testDeserializeAndSetWithMultipleElementCollection() throws IOException {
        Object instance = mock(Object.class);
        JsonParser parser = mock(JsonParser.class);
        DeserializationContext context = mock(DeserializationContext.class);
        Object propertyValue = mock(Object[].class);

        when(parser.getCurrentToken()).thenReturn(JsonToken.START_ARRAY);
        when(parser.nextValue()).thenReturn(JsonToken.VALUE_INT);
        when(parser.nextValue()).thenReturn(JsonToken.VALUE_STRING);
        when(parser.nextValue()).thenReturn(JsonToken.VALUE_BOOLEAN);
        when(parser.nextToken()).thenReturn(JsonToken.END_ARRAY);
        when(setterlessProperty._getter.invoke(instance, (Object[]) null)).thenReturn(propertyValue);

        setterlessProperty.deserializeAndSet(parser, context, instance);

        verify(parser).nextToken();
        verify(parser).nextValue();
        verify(parser).nextValue();
        verify(parser).nextValue();
        verify(parser).nextToken();
        verify(instance).getClass();
        verifyZeroInteractions(parser, context);
    }

    @Test(expected = IOException.class)
    public void testDeserializeAndSetWithInvalidJson() throws IOException {
        Object instance = mock(Object.class);
        JsonParser parser = mock(JsonParser.class);
        DeserializationContext context = mock(DeserializationContext.class);

        when(parser.getCurrentToken()).thenReturn(JsonToken.START_OBJECT);
        doThrow(new IOException("Problem deserializing JSON")).when(setterlessProperty)
                .deserializeAndSet(parser, context, instance);

        setterlessProperty.deserializeAndSet(parser, context, instance);

        verify(parser).nextToken();
        verify(parser).getCurrentToken();
        verify(instance).getClass();
    }

    private Object mockInstance() {
        return mock(Object.class);
    }

    private JsonParser mockParser() {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.getFactory().createParser(new StringReader("{}"));
    }

    private DeserializationContext mockContext() {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.getDeserializationContext();
    }
}
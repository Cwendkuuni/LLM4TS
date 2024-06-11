package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.lang.reflect.Method;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.NullValueProvider;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.introspect.AnnotatedMethod;
import com.fasterxml.jackson.databind.introspect.BeanPropertyDefinition;
import com.fasterxml.jackson.databind.jsontype.TypeIdResolver;
import com.fasterxml.jackson.databind.util.Annotations;
import com.fasterxml.jackson.databind.util.NameTransformer;

public final class SetterlessPropertyTest {

    @SuppressWarnings("unchecked")
    private static final SetterlessProperty setterlessProperty = new SetterlessProperty(
            mock(BeanPropertyDefinition.class), mock(JavaType.class),
            mock(TypeDeserializer.class), mock(Annotations.class), mock(AnnotatedMethod.class));

    @Test
    public void testGetAnnotation() {
        Annotation annotation = mock(Annotation.class);
        when(setterlessProperty.getAnnotation(Annotation.class)).thenReturn(annotation);
        assertEquals(annotation, setterlessProperty.getAnnotation(Annotation.class));
    }

    @Test
    public void testGetMember() {
        AnnotatedMember annotatedMember = mock(AnnotatedMember.class);
        when(setterlessProperty.getMember()).thenReturn(annotatedMember);
        assertEquals(annotatedMember, setterlessProperty.getMember());
    }

    @Test
    public void testDeserializeAndSet() throws IOException {
        Object instance = mock(Object.class);
        Object value = mock(Object.class);
        JsonParser parser = mock(JsonParser.class);
        DeserializationContext context = mock(DeserializationContext.class);
        Method getter = mock(Method.class);
        when(setterlessProperty._getter).thenReturn(getter);

        when(getter.invoke(instance, (Object[]) null)).thenReturn(value);

        JsonToken token = mock(JsonToken.class);
        when(parser.getCurrentToken()).thenReturn(token);
        when(token.isNullValue()).thenReturn(false);

        Object toModify = null;
        try {
            setterlessProperty.deserializeAndSet(parser, context, instance);
            toModify = setterlessProperty.getRawValue();
        } catch (IOException e) {
            fail("Exception should not be thrown");
        }

        assertSame(value, toModify);
    }

    @Test
    public void testDeserializeAndSetJsonNull() throws IOException {
        Object instance = mock(Object.class);
        JsonParser parser = mock(JsonParser.class);
        DeserializationContext context = mock(DeserializationContext.class);

        JsonToken token = mock(JsonToken.class);
        when(parser.getCurrentToken()).thenReturn(token);
        when(token.isNullValue()).thenReturn(true);

        // Nothing to modify in this case
        SetterlessProperty setterlessProperty = new SetterlessProperty(
                mock(BeanPropertyDefinition.class), mock(JavaType.class),
                mock(TypeDeserializer.class), mock(Annotations.class), mock(AnnotatedMethod.class));
        setterlessProperty.deserializeAndSet(parser, context, instance);
        assertNull(setterlessProperty.getRawValue());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSet() throws IOException {
        Object instance = mock(Object.class);
        Object value = mock(Object.class);
        setterlessProperty.set(instance, value);
    }

    @Test
    public void testSetAndReturn() throws IOException {
        Object instance = mock(Object.class);
        Object value = mock(Object.class);
        setterlessProperty.setAndReturn(instance, value);
        assertEquals(instance, setterlessProperty.getRawValue());
    }

    @Test
    public void testDeserializeSetAndReturn() throws IOException {
        Object instance = mock(Object.class);
        Object value = mock(Object.class);
        JsonParser parser = mock(JsonParser.class);
        DeserializationContext context = mock(DeserializationContext.class);

        when(parser.getCurrentToken()).thenReturn(JsonToken.VALUE_STRING);

        when(parser.readValueAs(String.class)).thenReturn("test");

        when(context.findValueDeserializer(setterlessProperty.getType())).thenReturn(mock(JsonDeserializer.class));

        setterlessProperty.deserializeSetAndReturn(parser, context, instance);
        assertEquals("test", setterlessProperty.getRawValue());
    }

    @Test
    public void testWithName() {
        PropertyName newName = mock(PropertyName.class);
        SetterlessProperty result = setterlessProperty.withName(newName);
        assertEquals(newName, result.getName());
    }

    @Test
    public void testWithValueDeserializer() {
        JsonDeserializer<String> deserializer = mock(JsonDeserializer.class);
        SetterlessProperty result = setterlessProperty.withValueDeserializer(deserializer);
        assertEquals(deserializer, result._valueDeserializer);
    }

    @Test
    public void testWithNullProvider() {
        NullValueProvider nullValueProvider = mock(NullValueProvider.class);
        SetterlessProperty result = setterlessProperty.withNullProvider(nullValueProvider);
        assertEquals(nullValueProvider, result._nullProvider);
    }

    @Test
    public void testFixAccess() throws IOException {
        DeserializationConfig config = mock(DeserializationConfig.class);
        setterlessProperty.fixAccess(config);
        assertTrue(config.isEnabled(MapperFeature.OVERRIDE_PUBLIC_ACCESS_MODIFIERS));
    }

    @Test
    public void testCreateInstance() {
        Object instance = mock(Object.class);
        SetterlessProperty setterlessProperty1 = new SetterlessProperty(
                mock(BeanPropertyDefinition.class), mock(JavaType.class),
                mock(TypeDeserializer.class), mock(Annotations.class), mock(AnnotatedMethod.class));
        SetterlessProperty setterlessProperty2 = new SetterlessProperty(setterlessProperty1, mock(JsonDeserializer.class), mock(NullValueProvider.class));
        assertEquals(setterlessProperty1, setterlessProperty2);
    }

    @Test
    public void testCreateInstanceWithName() {
        PropertyName newName = mock(PropertyName.class);
        SetterlessProperty setterlessProperty1 = new SetterlessProperty(
                mock(BeanPropertyDefinition.class), mock(JavaType.class),
                mock(TypeDeserializer.class), mock(Annotations.class), mock(AnnotatedMethod.class));
        SetterlessProperty setterlessProperty2 = new SetterlessProperty(setterlessProperty1, newName);
        assertEquals(setterlessProperty1, setterlessProperty2);
        assertEquals(newName, setterlessProperty2.getName());
    }
}
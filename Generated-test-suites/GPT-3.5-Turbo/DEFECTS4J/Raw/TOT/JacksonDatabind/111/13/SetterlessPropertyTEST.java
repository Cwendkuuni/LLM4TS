
package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.*;
import org.junit.Test;
import org.mockito.Mockito;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.deser.NullValueProvider;
import com.fasterxml.jackson.databind.deser.SettableBeanProperty;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.util.Annotations;

public class SetterlessPropertyTest {

    @Test
    public void testConstructorAndGetters() {
        BeanPropertyDefinition propDef = Mockito.mock(BeanPropertyDefinition.class);
        JavaType type = Mockito.mock(JavaType.class);
        TypeDeserializer typeDeser = Mockito.mock(TypeDeserializer.class);
        Annotations contextAnnotations = Mockito.mock(Annotations.class);
        AnnotatedMethod method = Mockito.mock(AnnotatedMethod.class);

        SetterlessProperty property = new SetterlessProperty(propDef, type, typeDeser, contextAnnotations, method);

        assertEquals(propDef, property.getMember());
        assertEquals(type, property.getType());
        assertEquals(typeDeser, property.getValueTypeDeserializer());
        assertEquals(contextAnnotations, property.getContextAnnotations());
    }

    @Test
    public void testWithMethods() {
        BeanPropertyDefinition propDef = Mockito.mock(BeanPropertyDefinition.class);
        JavaType type = Mockito.mock(JavaType.class);
        TypeDeserializer typeDeser = Mockito.mock(TypeDeserializer.class);
        Annotations contextAnnotations = Mockito.mock(Annotations.class);
        AnnotatedMethod method = Mockito.mock(AnnotatedMethod.class);
        JsonDeserializer<?> deser = Mockito.mock(JsonDeserializer.class);
        NullValueProvider nva = Mockito.mock(NullValueProvider.class);
        PropertyName newName = Mockito.mock(PropertyName.class);

        SetterlessProperty property = new SetterlessProperty(propDef, type, typeDeser, contextAnnotations, method);
        assertEquals(property, property.withName(newName));
        assertEquals(property, property.withValueDeserializer(deser));
        assertEquals(property, property.withNullProvider(nva));
    }

    @Test
    public void testFixAccess() {
        BeanPropertyDefinition propDef = Mockito.mock(BeanPropertyDefinition.class);
        JavaType type = Mockito.mock(JavaType.class);
        TypeDeserializer typeDeser = Mockito.mock(TypeDeserializer.class);
        Annotations contextAnnotations = Mockito.mock(Annotations.class);

        DeserializationConfig config = Mockito.mock(DeserializationConfig.class);
        SetterlessProperty property = new SetterlessProperty(propDef, type, typeDeser, contextAnnotations, new AnnotatedMethod());

        // Test fix access with false config
        Mockito.when(config.isEnabled(MapperFeature.OVERRIDE_PUBLIC_ACCESS_MODIFIERS)).thenReturn(false);
        property.fixAccess(config);
        assertFalse(property.getMember().isPublic());

        // Test fix access with true config
        Mockito.when(config.isEnabled(MapperFeature.OVERRIDE_PUBLIC_ACCESS_MODIFIERS)).thenReturn(true);
        property.fixAccess(config);
        assertTrue(property.getMember().isPublic());
    }

    @Test
    public void testGetAnnotation() {
        Annotation expected = Mockito.mock(Annotation.class);
        BeanPropertyDefinition propDef = Mockito.mock(BeanPropertyDefinition.class);
        JavaType type = Mockito.mock(JavaType.class);
        TypeDeserializer typeDeser = Mockito.mock(TypeDeserializer.class);
        Annotations contextAnnotations = Mockito.mock(Annotations.class);
        AnnotatedMethod method = Mockito.mock(AnnotatedMethod.class);
        Mockito.when(method.getAnnotation(Annotation.class)).thenReturn(expected);
        SetterlessProperty property = new SetterlessProperty(propDef, type, typeDeser, contextAnnotations, method);

        assertEquals(expected, property.getAnnotation(Annotation.class));
    }

    @Test
    public void testDeserializeAndSet() throws IOException {
        BeanPropertyDefinition propDef = Mockito.mock(BeanPropertyDefinition.class);
        JavaType type = Mockito.mock(JavaType.class);
        TypeDeserializer typeDeser = Mockito.mock(TypeDeserializer.class);
        Annotations contextAnnotations = Mockito.mock(Annotations.class);
        AnnotatedMethod method = Mockito.mock(AnnotatedMethod.class);
        JsonParser p = Mockito.mock(JsonParser.class);
        DeserializationContext ctxt = Mockito.mock(DeserializationContext.class);
        Object instance = Mockito.mock(Object.class);
        JsonDeserializer<?> valueDeserializer = Mockito.mock(JsonDeserializer.class);
        SettableBeanProperty src = new SetterlessProperty(propDef, type, typeDeser, contextAnnotations, method);
        SetterlessProperty property = new SetterlessProperty(src, valueDeserializer, null);

        // Test setting a value with VALUE_NULL token
        Mockito.when(p.getCurrentToken()).thenReturn(JsonToken.VALUE_NULL);
        property.deserializeAndSet(p, ctxt, instance);
        // TODO: Assert the result after setting the value

        // Test setting a value with VALUE_STRING token
        Mockito.when(p.getCurrentToken()).thenReturn(JsonToken.VALUE_STRING);
        Mockito.when(valueDeserializer.deserialize(p, ctxt, instance)).thenReturn("test");
        property.deserializeAndSet(p, ctxt, instance);
        // TODO: Assert the result after setting the value

        // Test setting a value with VALUE_NULL token and valueDeserialized is null
        Mockito.when(valueDeserializer.deserialize(p, ctxt, instance)).thenReturn(null);
        try {
            property.deserializeAndSet(p, ctxt, instance);
            fail("Expected IOException to be thrown");
        } catch (IOException e) {
            // TODO: Assert the exception message
        }
        // TODO: Assert the result after setting the value
    }

    @Test
    public void testDeserializeSetAndReturn() throws IOException {
        BeanPropertyDefinition propDef = Mockito.mock(BeanPropertyDefinition.class);
        JavaType type = Mockito.mock(JavaType.class);
        TypeDeserializer typeDeser = Mockito.mock(TypeDeserializer.class);
        Annotations contextAnnotations = Mockito.mock(Annotations.class);
        AnnotatedMethod method = Mockito.mock(AnnotatedMethod.class);
        JsonParser p = Mockito.mock(JsonParser.class);
        DeserializationContext ctxt = Mockito.mock(DeserializationContext.class);
        Object instance = Mockito.mock(Object.class);
        JsonDeserializer<?> valueDeserializer = Mockito.mock(JsonDeserializer.class);
        SettableBeanProperty src = new SetterlessProperty(propDef, type, typeDeser, contextAnnotations, method);
        SetterlessProperty property = new SetterlessProperty(src, valueDeserializer, null);

        Object result = property.deserializeSetAndReturn(p, ctxt, instance);
        assertEquals(instance, result);
        // TODO: Assert the result after setting the value
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSet() throws IOException {
        GetterlessProperty property = new GetterlessProperty(null, null, null, null, null);
        property.set(null, null);
    }

    @Test
    public void testSetAndReturn() throws IOException {
        BeanPropertyDefinition propDef = Mockito.mock(BeanPropertyDefinition.class);
        JavaType type = Mockito.mock(JavaType.class);
        TypeDeserializer typeDeser = Mockito.mock(TypeDeserializer.class);
        Annotations contextAnnotations = Mockito.mock(Annotations.class);
        AnnotatedMethod method = Mockito.mock(AnnotatedMethod.class);
        SettableBeanProperty src = new SetterlessProperty(propDef, type, typeDeser, contextAnnotations, method);
        SetterlessProperty property = new SetterlessProperty(src, null, null);

        Object instance = new Object();
        Object value = new Object();
        Object result = property.setAndReturn(instance, value);
        assertEquals(instance, result);
        // TODO: Assert the result after setting the value
    }
}

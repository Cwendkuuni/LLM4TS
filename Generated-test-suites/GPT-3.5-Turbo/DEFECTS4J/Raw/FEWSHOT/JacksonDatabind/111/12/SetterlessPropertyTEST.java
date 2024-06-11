
package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.*;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;

import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.NullValueProvider;
import com.fasterxml.jackson.databind.deser.SettableBeanProperty;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.util.Annotations;

public class SetterlessPropertyTest {

    private SetterlessProperty property;
    private BeanPropertyDefinition propDef;
    private JavaType type;
    private TypeDeserializer typeDeser;
    private Annotations contextAnnotations;
    private AnnotatedMethod method;
    private Method annotatedMethod;
    private JsonDeserializer<?> valueDeserializer;
    private NullValueProvider nullValueProvider;

    @Before
    public void setup() {
        propDef = mock(BeanPropertyDefinition.class);
        type = mock(JavaType.class);
        typeDeser = mock(TypeDeserializer.class);
        contextAnnotations = mock(Annotations.class);
        method = mock(AnnotatedMethod.class);
        annotatedMethod = mock(Method.class);
        valueDeserializer = mock(JsonDeserializer.class);
        nullValueProvider = mock(NullValueProvider.class);

        when(method.getAnnotated()).thenReturn(annotatedMethod);

        property = new SetterlessProperty(propDef, type, typeDeser, contextAnnotations, method);
    }

    @Test
    public void testWithName() {
        PropertyName newName = mock(PropertyName.class);
        SettableBeanProperty result = property.withName(newName);

        assertEquals(newName, result.getFullName());
    }

    @Test
    public void testWithValueDeserializer() {
        SettableBeanProperty result = property.withValueDeserializer(valueDeserializer);

        assertEquals(valueDeserializer, result.getValueDeserializer());
    }

    @Test
    public void testWithNullProvider() {
        SettableBeanProperty result = property.withNullProvider(nullValueProvider);

        assertEquals(nullValueProvider, result.getNullValueProvider());
    }

    @Test
    public void testFixAccess() {
        DeserializationConfig config = mock(DeserializationConfig.class);
        when(config.isEnabled(MapperFeature.OVERRIDE_PUBLIC_ACCESS_MODIFIERS)).thenReturn(true);
        
        property.fixAccess(config);

        verify(method).fixAccess(true);
    }

    @Test
    public void testGetAnnotation() {
        Class<Annotation> acls = Annotation.class;
        Annotation annotation = mock(Annotation.class);
        when(method.getAnnotation(acls)).thenReturn(annotation);

        Annotation result = property.getAnnotation(acls);

        assertEquals(annotation, result);
    }

    @Test
    public void testGetMember() {
        AnnotatedMember result = property.getMember();

        assertEquals(method, result);
    }

    @Test
    public void testDeserializeAndSet_WithValueNull() throws Exception {
        JsonParser p = mock(JsonParser.class);
        DeserializationContext ctxt = mock(DeserializationContext.class);
        Object instance = mock(Object.class);

        when(p.getCurrentToken()).thenReturn(JsonToken.VALUE_NULL);

        property.deserializeAndSet(p, ctxt, instance);

        // Expect no exception and nothing should be set
        verifyZeroInteractions(instance);
    }

    @Test
    public void testDeserializeAndSet_WithNonNullValueAndNoValueTypeDeserializer() throws Exception {
        JsonParser p = mock(JsonParser.class);
        DeserializationContext ctxt = mock(DeserializationContext.class);
        Object instance = mock(Object.class);
        Object toModify = mock(Object.class);

        when(p.getCurrentToken()).thenReturn(mock(JsonToken.class));
        when(annotatedMethod.invoke(instance, (Object[]) null)).thenReturn(toModify);

        property.deserializeAndSet(p, ctxt, instance);

        // Expect the value to be deserialized and set
        verify(valueDeserializer).deserialize(p, ctxt, toModify);
    }

    @Test
    public void testDeserializeAndSet_WithNonNullValueAndWithValueTypeDeserializer() throws Exception {
        JsonParser p = mock(JsonParser.class);
        DeserializationContext ctxt = mock(DeserializationContext.class);
        Object instance = mock(Object.class);
        Object toModify = mock(Object.class);

        when(p.getCurrentToken()).thenReturn(mock(JsonToken.class));
        when(annotatedMethod.invoke(instance, (Object[]) null)).thenReturn(toModify);

        property.deserializeAndSet(p, ctxt, instance);

        // Expect the value to be deserialized with type deserializer and set
        verify(valueDeserializer, never()).deserializeWithType(p, ctxt, typeDeser);
    }

    @Test
    public void testDeserializeAndSet_WithNullToModify() throws Exception {
        JsonParser p = mock(JsonParser.class);
        DeserializationContext ctxt = mock(DeserializationContext.class);
        Object instance = mock(Object.class);

        when(p.getCurrentToken()).thenReturn(mock(JsonToken.class));
        when(annotatedMethod.invoke(instance, (Object[]) null)).thenReturn(null);

        property.deserializeAndSet(p, ctxt, instance);

        // Expect a bad definition exception to be reported
        verify(ctxt).reportBadDefinition(type, String.format("Problem deserializing 'setterless' property '%s': get method returned null", property.getName()));
    }

    @Test
    public void testDeserializeAndSet_WithThrowingGetter() throws Exception {
        JsonParser p = mock(JsonParser.class);
        DeserializationContext ctxt = mock(DeserializationContext.class);
        Object instance = mock(Object.class);

        when(p.getCurrentToken()).thenReturn(mock(JsonToken.class));
        when(annotatedMethod.invoke(instance, (Object[]) null)).thenThrow(Exception.class);

        property.deserializeAndSet(p, ctxt, instance);

        // Expect an IOException to be thrown
        verify(p, times(2)).getCurrentToken(); // Called twice because _throwAsIOE is called twice
    }

    @Test
    public void testDeserializeSetAndReturn() throws Exception {
        JsonParser p = mock(JsonParser.class);
        DeserializationContext ctxt = mock(DeserializationContext.class);
        Object instance = mock(Object.class);

        Object result = property.deserializeSetAndReturn(p, ctxt, instance);

        assertEquals(instance, result);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSet() throws Exception {
        property.set(null, null);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSetAndReturn() throws Exception {
        property.setAndReturn(null, null);
    }

}

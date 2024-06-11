package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.*;

import org.junit.Test;
import org.mockito.Mockito;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonToken;
import com.fasterxml.jackson.databind.deser.NullValueProvider;

public class SetterlessPropertyTest {
    @Test
    public void testConstructor() {
        BeanPropertyDefinition propDef = mock(BeanPropertyDefinition.class);
        JavaType type = mock(JavaType.class);
        TypeDeserializer typeDeser = mock(TypeDeserializer.class);
        Annotations contextAnnotations = mock(Annotations.class);
        AnnotatedMethod method = mock(AnnotatedMethod.class);
        SetterlessProperty property = new SetterlessProperty(propDef, type, typeDeser, contextAnnotations, method);
        
        assertEquals(propDef, property.getMetadata());
        assertEquals(type, property.getType());
        assertEquals(typeDeser, property.getTypeDeserializer());
        assertEquals(contextAnnotations, property.getContextAnnotations());
        assertEquals(method, property.getMember());
    }
    
    @Test
    public void testConstructorWithDeserializerAndNullValueProvider() {
        SetterlessProperty src = mock(SetterlessProperty.class);
        JsonDeserializer<?> deser = mock(JsonDeserializer.class);
        NullValueProvider nva = mock(NullValueProvider.class);
        SetterlessProperty property = new SetterlessProperty(src, deser, nva);
        
        assertEquals(src, property.getMetadata());
        assertEquals(deser, property.getValueDeserializer());
        assertEquals(nva, property.getNullValueProvider());
    }
    
    @Test
    public void testConstructorWithNewName() {
        SetterlessProperty src = mock(SetterlessProperty.class);
        PropertyName newName = mock(PropertyName.class);
        SetterlessProperty property = new SetterlessProperty(src, newName);
        
        assertEquals(src, property.getMetadata());
        assertEquals(newName, property.getFullName());
    }
    
    @Test
    public void testWithName() {
        SetterlessProperty src = mock(SetterlessProperty.class);
        PropertyName newName = mock(PropertyName.class);
        SetterlessProperty property = new SetterlessProperty(src, newName);
        
        assertEquals(newName, property.getFullName());
    }
    
    @Test
    public void testWithValueDeserializer() {
        SetterlessProperty src = mock(SetterlessProperty.class);
        JsonDeserializer<?> deser = mock(JsonDeserializer.class);
        SetterlessProperty property = new SetterlessProperty(src, deser, null);
        
        assertEquals(src, property.getMetadata());
        assertEquals(deser, property.getValueDeserializer());
    }
    
    @Test
    public void testWithNullProvider() {
        SetterlessProperty src = mock(SetterlessProperty.class);
        NullValueProvider nva = mock(NullValueProvider.class);
        SetterlessProperty property = new SetterlessProperty(src, null, nva);
        
        assertEquals(src, property.getMetadata());
        assertEquals(nva, property.getNullValueProvider());
    }
    
    @Test
    public void testFixAccess() {
        SetterlessProperty property = mock(SetterlessProperty.class);
        DeserializationConfig config = mock(DeserializationConfig.class);
        
        when(config.isEnabled(MapperFeature.OVERRIDE_PUBLIC_ACCESS_MODIFIERS)).thenReturn(true);
        doCallRealMethod().when(property).fixAccess(config);
        
        property.fixAccess(config);
        
        verify(property).fixAccess(true);
    }
    
    @Test
    public void testFixAccessDisabled() {
        SetterlessProperty property = mock(SetterlessProperty.class);
        DeserializationConfig config = mock(DeserializationConfig.class);
        
        when(config.isEnabled(MapperFeature.OVERRIDE_PUBLIC_ACCESS_MODIFIERS)).thenReturn(false);
        doCallRealMethod().when(property).fixAccess(config);
        
        property.fixAccess(config);
        
        verify(property).fixAccess(false);
    }
    
    @Test
    public void testDeserializeAndSetWithNullToken() throws IOException {
        SetterlessProperty property = mock(SetterlessProperty.class);
        JsonParser p = mock(JsonParser.class);
        DeserializationContext ctxt = mock(DeserializationContext.class);
        Object instance = mock(Object.class);
        
        when(p.getCurrentToken()).thenReturn(JsonToken.VALUE_NULL);
        doCallRealMethod().when(property).deserializeAndSet(p, ctxt, instance);
        
        property.deserializeAndSet(p, ctxt, instance);
        
        verifyNoInteractions(ctxt);
    }
    
    @Test
    public void testDeserializeAndSetWithTypeDeserializer() throws IOException {
        SetterlessProperty property = mock(SetterlessProperty.class);
        JsonParser p = mock(JsonParser.class);
        DeserializationContext ctxt = mock(DeserializationContext.class);
        Object instance = mock(Object.class);
        
        when(p.getCurrentToken()).thenReturn(JsonToken.VALUE_NULL);
        when(property.getType()).thenReturn(mock(JavaType.class));
        when(property.getTypeDeserializer()).thenReturn(mock(TypeDeserializer.class));
        doCallRealMethod().when(property).deserializeAndSet(p, ctxt, instance);
        
        property.deserializeAndSet(p, ctxt, instance);
        
        verify(ctxt).reportBadDefinition(property.getType(), anyString());
    }
    
    @Test
    public void testDeserializeAndSetWithNonNullToken() throws IOException {
        SetterlessProperty property = mock(SetterlessProperty.class);
        JsonParser p = mock(JsonParser.class);
        DeserializationContext ctxt = mock(DeserializationContext.class);
        Object instance = mock(Object.class);
        Object toModify = mock(Object.class);
        
        when(p.getCurrentToken()).thenReturn(JsonToken.VALUE_TRUE);
        when(property.getGetter()).thenReturn(mock(Method.class));
        when(property.getGetter().invoke(instance, (Object[]) null)).thenReturn(toModify);
        when(property.getValueDeserializer()).thenReturn(mock(JsonDeserializer.class));
        doCallRealMethod().when(property).deserializeAndSet(p, ctxt, instance);
        
        property.deserializeAndSet(p, ctxt, instance);
        
        verify(property.getValueDeserializer()).deserialize(p, ctxt, toModify);
    }
    
    @Test
    public void testDeserializeAndSetWithNonNullTokenAndNullToModify() throws IOException {
        SetterlessProperty property = mock(SetterlessProperty.class);
        JsonParser p = mock(JsonParser.class);
        DeserializationContext ctxt = mock(DeserializationContext.class);
        Object instance = mock(Object.class);
        
        when(p.getCurrentToken()).thenReturn(JsonToken.VALUE_TRUE);
        when(property.getGetter()).thenReturn(mock(Method.class));
        when(property.getGetter().invoke(instance, (Object[]) null)).thenReturn(null);
        doCallRealMethod().when(property).deserializeAndSet(p, ctxt, instance);
        
        property.deserializeAndSet(p, ctxt, instance);
        
        verify(ctxt).reportBadDefinition(property.getType(), anyString());
    }
    
    @Test
    public void testDeserializeSetAndReturn() throws IOException {
        SetterlessProperty property = mock(SetterlessProperty.class);
        JsonParser p = mock(JsonParser.class);
        DeserializationContext ctxt = mock(DeserializationContext.class);
        Object instance = mock(Object.class);
        
        doCallRealMethod().when(property).deserializeAndSet(p, ctxt, instance);
        
        property.deserializeSetAndReturn(p, ctxt, instance);
        
        verifyNoInteractions(ctxt);
    }
    
    @Test(expected = UnsupportedOperationException.class)
    public void testSet() throws IOException {
        SetterlessProperty property = mock(SetterlessProperty.class);
        property.set(mock(Object.class), mock(Object.class));
    }
    
    @Test
    public void testSetAndReturn() throws IOException {
        SetterlessProperty property = mock(SetterlessProperty.class);
        Object instance = mock(Object.class);
        Object value = mock(Object.class);
        
        doCallRealMethod().when(property).setAndReturn(instance, value);
        
        assertEquals(instance, property.setAndReturn(instance, value));
    }
}
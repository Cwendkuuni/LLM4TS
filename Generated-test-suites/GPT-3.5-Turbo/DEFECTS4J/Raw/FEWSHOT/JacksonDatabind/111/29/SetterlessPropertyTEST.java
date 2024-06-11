package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.NullValueProvider;
import com.fasterxml.jackson.databind.deser.SettableBeanProperty;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.util.Annotations;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class SetterlessPropertyTest {
    
    @Mock
    private BeanPropertyDefinition propDef;
    
    @Mock
    private JavaType type;
    
    @Mock
    private TypeDeserializer typeDeser;
    
    @Mock
    private Annotations contextAnnotations;
    
    @Mock
    private AnnotatedMethod method;
    
    @Mock
    private SetterlessProperty srcProperty;
    
    @Mock
    private JsonDeserializer<?> deser;
    
    @Mock
    private NullValueProvider nva;
    
    @Mock
    private PropertyName newName;
    
    @Mock
    private DeserializationConfig config;
    
    @Mock
    private AnnotatedMember annotatedMember;
    
    @Mock
    private JsonParser p;
    
    @Mock
    private DeserializationContext ctxt;
    
    @Mock
    private Object instance;
    
    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }
    
    @Test
    public void testGetterMethod() {
        SetterlessProperty property = new SetterlessProperty(propDef, type, typeDeser, contextAnnotations, method);
        assertEquals(method.getAnnotated(), property._getter);
    }
    
    @Test
    public void testWithNullProvider() {
        SetterlessProperty property = new SetterlessProperty(srcProperty, deser, nva);
        when(srcProperty._valueDeserializer).thenReturn(deser);
        assertEquals(deser, property._valueDeserializer);
        assertEquals(nva, property._nullProvider);
    }
    
    @Test
    public void testWithNullName() {
        SetterlessProperty property = new SetterlessProperty(srcProperty, newName);
        assertEquals(newName, property._propName);
    }
    
    @Test
    public void testWithConfig() {
        SetterlessProperty property = new SetterlessProperty(propDef, type, typeDeser, contextAnnotations, method);
        when(config.isEnabled(MapperFeature.OVERRIDE_PUBLIC_ACCESS_MODIFIERS)).thenReturn(true);
        property.fixAccess(config);
        verify(method).fixAccess(true);
    }
    
    @Test
    public void testGetAnnotation() {
        SetterlessProperty property = new SetterlessProperty(propDef, type, typeDeser, contextAnnotations, method);
        Class<Annotation> acls = Annotation.class;
        property.getAnnotation(acls);
        verify(method).getAnnotation(acls);
    }
    
    @Test
    public void testGetMember() {
        SetterlessProperty property = new SetterlessProperty(propDef, type, typeDeser, contextAnnotations, method);
        property._annotated = annotatedMember;
        assertEquals(annotatedMember, property.getMember());
    }
    
    @Test
    public void testDeserializeAndSetWithNullValue() throws IOException {
        SetterlessProperty property = new SetterlessProperty(propDef, type, typeDeser, contextAnnotations, method);
        JsonToken token = JsonToken.VALUE_NULL;
        when(p.getCurrentToken()).thenReturn(token);
        property.deserializeAndSet(p, ctxt, instance);
    }
    
    @Test
    public void testDeserializeAndSetWithTypeDeserializer() throws IOException {
        SetterlessProperty property = new SetterlessProperty(propDef, type, typeDeser, contextAnnotations, method);
        JsonToken token = JsonToken.VALUE_NULL;
        when(p.getCurrentToken()).thenReturn(token);
        when(property.getType()).thenReturn(type);
        assertThrows(JsonMappingException.class, () -> property.deserializeAndSet(p, ctxt, instance));
    }
    
    @Test
    public void testDeserializeAndSetWithObject() throws IOException {
        SetterlessProperty property = new SetterlessProperty(propDef, type, typeDeser, contextAnnotations, method);
        JsonToken token = mock(JsonToken.class);
        when(p.getCurrentToken()).thenReturn(token);
        when(property.getType()).thenReturn(type);
        when(property.getName()).thenReturn("property");
        property.deserializeAndSet(p, ctxt, instance);
        verify(p).getCurrentToken();
        verify(property).getType();
        verify(property).getName();
        verify(ctxt).reportBadDefinition(type, "Problem deserializing 'setterless' property (\"property\"): no way to handle typed deser with setterless yet");
    }
    
    @Test
    public void testWithNullValue() throws IOException {
        SetterlessProperty property = new SetterlessProperty(propDef, type, typeDeser, contextAnnotations, method);
        JsonToken token = mock(JsonToken.class);
        when(p.getCurrentToken()).thenReturn(token);
        when(method.invoke(instance, (Object[]) null)).thenReturn(null);
        when(property.getType()).thenReturn(type);
        when(property.getName()).thenReturn("property");
        property.deserializeAndSet(p, ctxt, instance);
        verify(p).getCurrentToken();
        verify(method).invoke(instance, (Object[]) null);
        verify(property).getType();
        verify(property).getName();
        verify(ctxt).reportBadDefinition(type, "Problem deserializing 'setterless' property 'property': get method returned null");
    }
    
    @Test
    public void testDeserializeSetAndReturn() throws IOException {
        SetterlessProperty property = new SetterlessProperty(propDef, type, typeDeser, contextAnnotations, method);
        property.deserializeSetAndReturn(p, ctxt, instance);
        verify(property).deserializeAndSet(p, ctxt, instance);
    }
    
    @Test
    public void testSetAndReturn() throws IOException {
        SetterlessProperty property = new SetterlessProperty(propDef, type, typeDeser, contextAnnotations, method);
        property.setAndReturn(instance, null);
        verify(property).set(instance, null);
    }
}
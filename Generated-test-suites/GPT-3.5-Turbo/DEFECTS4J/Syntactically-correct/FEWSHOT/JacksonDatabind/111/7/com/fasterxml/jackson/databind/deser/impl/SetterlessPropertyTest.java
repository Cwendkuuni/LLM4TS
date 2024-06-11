
package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

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

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

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
    private Method annotatedMethod;
    
    @Mock
    private JsonDeserializer<?> deser;
    
    @Mock
    private NullValueProvider nva;
    
    @Mock
    private DeserializationConfig config;
    
    @Mock
    private JsonParser p;
    
    @Mock
    private DeserializationContext ctxt;
    
    @Mock
    private Object instance;
    
    @Mock
    private JsonToken t;
    
    @Mock
    private Object toModify;
    
    private SetterlessProperty setterlessProperty;
    
    @Before
    public void setUp() {
        setterlessProperty = new SetterlessProperty(propDef, type, typeDeser, contextAnnotations, method);
        when(method.getAnnotated()).thenReturn(annotatedMethod);
        when(deser.deserialize(p, ctxt, toModify)).thenReturn(null);
    }
    
    @Test
    public void testConstructor() {
        assertEquals(propDef, setterlessProperty.getPropertyDefinition());
        assertEquals(type, setterlessProperty.getType());
        assertEquals(typeDeser, setterlessProperty.getTypeDeserializer());
        assertEquals(contextAnnotations, setterlessProperty.getContextAnnotations());
        assertEquals(method, setterlessProperty.getMember());
        assertEquals(annotatedMethod, setterlessProperty._getter);
    }
    
    @Test
    public void testWithNullProvider() {
        SetterlessProperty newProperty = (SetterlessProperty) setterlessProperty.withNullProvider(nva);
        assertEquals(nva, newProperty._nullProvider);
    }
    
    @Test
    public void testFixAccess() {
        when(config.isEnabled(MapperFeature.OVERRIDE_PUBLIC_ACCESS_MODIFIERS)).thenReturn(true);
        setterlessProperty.fixAccess(config);
        verify(method).fixAccess(true);
    }
    
    @Test
    public void testDeserializeAndSet() throws IOException {
        when(p.getCurrentToken()).thenReturn(t);
        when(_getter.invoke(instance, (Object[]) null)).thenReturn(toModify);
        setterlessProperty.deserializeAndSet(p, ctxt, instance);
        verify(deser).deserialize(p, ctxt, toModify);
    }
    
    @Test
    public void testDeserializeAndSetWithNullValueHandling() throws IOException {
        when(p.getCurrentToken()).thenReturn(JsonToken.VALUE_NULL);
        setterlessProperty.deserializeAndSet(p, ctxt, instance);
        verifyZeroInteractions(deser);
    }
    
    @Test
    public void testDeserializeAndSetWithTypeDeserializer() throws IOException {
        when(p.getCurrentToken()).thenReturn(t);
        when(_valueTypeDeserializer != null).thenReturn(true);
        setterlessProperty.deserializeAndSet(p, ctxt, instance);
        verify(ctxt).reportBadDefinition(getType(), String.format(
                "Problem deserializing 'setterless' property (\"%s\"): no way to handle typed deser with setterless yet",
                getName()));
    }
    
    @Test
    public void testDeserializeAndSetWithNullToModify() throws IOException {
        when(p.getCurrentToken()).thenReturn(t);
        when(_getter.invoke(instance, (Object[]) null)).thenReturn(null);
        setterlessProperty.deserializeAndSet(p, ctxt, instance);
        verify(ctxt).reportBadDefinition(getType(), String.format(
                "Problem deserializing 'setterless' property '%s': get method returned null",
                getName()));
    }
    
    @Test
    public void testDeserializeSetAndReturn() throws IOException {
        when(p.getCurrentToken()).thenReturn(t);
        when(_getter.invoke(instance, (Object[]) null)).thenReturn(toModify);
        Object result = setterlessProperty.deserializeSetAndReturn(p, ctxt, instance);
        assertEquals(instance, result);
        verify(deser).deserialize(p, ctxt, toModify);
    }
    
    @Test
    public void testSet() throws IOException {
        try {
            setterlessProperty.set(instance, null);
            fail("UnsupportedOperationException should be thrown");
        } catch (UnsupportedOperationException e) {
            assertEquals("Should never call `set()` on setterless property ('"+getName()+"')", e.getMessage());
        }
    }
    
    @Test
    public void testSetAndReturn() throws IOException {
        try {
            setterlessProperty.setAndReturn(instance, null);
            fail("UnsupportedOperationException should be thrown");
        } catch (UnsupportedOperationException e) {
            assertEquals("Should never call `set()` on setterless property ('"+getName()+"')", e.getMessage());
        }
    }
    
    @Test
    public void testGetAnnotation() {
        Class<Annotation> acls = Annotation.class;
        assertNull(setterlessProperty.getAnnotation(acls));
        verify(method).getAnnotation(acls);
    }
}

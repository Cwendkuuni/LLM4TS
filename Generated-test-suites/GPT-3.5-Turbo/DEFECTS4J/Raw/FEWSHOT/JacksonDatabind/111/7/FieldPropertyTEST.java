package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.Arrays;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;

import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.DeserializationConfig;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.DeserializationFeature$EnumSetDeserializer;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.JsonSerializable;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.JsonSerializer$None;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.introspect.AnnotatedField;
import com.fasterxml.jackson.databind.introspect.AnnotationIntrospector;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.introspect.BeanPropertyDefinition;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.util.Annotations;
import com.fasterxml.jackson.databind.util.ClassUtil;

public class FieldPropertyTest {

    private FieldProperty fieldProperty;
    private DeserializationConfig config;
    private JsonParser p;
    
    @Before
    public void setUp() {
        BeanPropertyDefinition propDef = mock(BeanPropertyDefinition.class);
        JavaType type = mock(JavaType.class);
        TypeDeserializer typeDeser = mock(TypeDeserializer.class);
        Annotations contextAnnotations = mock(Annotations.class);
        AnnotatedField field = mock(AnnotatedField.class);
                
        fieldProperty = new FieldProperty(propDef, type, typeDeser, contextAnnotations, field);
        
        config = mock(DeserializationConfig.class);
        p = mock(JsonParser.class);
    }

    @Test
    public void testConstructor() {
        BeanPropertyDefinition propDef = mock(BeanPropertyDefinition.class);
        JavaType type = mock(JavaType.class);
        TypeDeserializer typeDeser = mock(TypeDeserializer.class);
        Annotations contextAnnotations = mock(Annotations.class);
        AnnotatedField field = mock(AnnotatedField.class);
        
        FieldProperty fieldProp = new FieldProperty(propDef, type, typeDeser, contextAnnotations, field);
        
        assertEquals(propDef, fieldProp.getPropertyDefinition());
        assertEquals(type, fieldProp.getType());
        assertEquals(typeDeser, fieldProp.getTypeDeserializer());
        assertEquals(contextAnnotations, fieldProp.getMemberAnnotations());
        
        assertEquals(field, fieldProp._annotated);
        assertEquals(field.getAnnotated(), fieldProp._field);
                
        assertEquals(false, fieldProp._skipNulls);
    }

    @Test
    public void testFixAccess() {
        FieldProperty fieldProperty = Mockito.spy(this.fieldProperty);
        
        fieldProperty.fixAccess(config);
        
        Mockito.verify(ClassUtil.class, Mockito.times(1)).checkAndFixAccess(Mockito.any(Field.class), Mockito.anyBoolean());
    }

    @Test
    public void testDeserializeAndSet() throws Exception {
        Object instance = new Object();
        JsonDeserializer deser = mock(JsonDeserializer.class);
        NullValueProvider nva = mock(NullValueProvider.class);
        JsonToken token = JsonToken.VALUE_NULL;
        Mockito.when(p.hasToken(token)).thenReturn(true);
        DeserializationContext ctxt = mock(DeserializationContext.class);
        Object value = new Object();
        Mockito.when(nva.getNullValue(ctxt)).thenReturn(value);
        
        fieldProperty._valueTypeDeserializer = null;
        fieldProperty._nullProvider = nva;
        fieldProperty._valueDeserializer = deser;
        
        fieldProperty.deserializeAndSet(p, ctxt, instance);
        
        Mockito.verify(nva, Mockito.times(1)).getNullValue(Mockito.eq(ctxt));
        
        Mockito.verify(deser, Mockito.times(1)).deserialize(Mockito.eq(p), Mockito.eq(ctxt));
        Mockito.verify(fieldProperty._field, Mockito.times(1)).set(Mockito.eq(instance), Mockito.eq(value));
        
        token = JsonToken.VALUE_NUMBER_INT;
        Mockito.when(p.hasToken(token)).thenReturn(true);
        Mockito.when(deser.deserialize(Mockito.eq(p), Mockito.eq(ctxt))).thenReturn(value);
        
        fieldProperty.deserializeAndSet(p, ctxt, instance);
        
        Mockito.verify(fieldProperty._field, Mockito.times(2)).set(Mockito.eq(instance), Mockito.eq(value));
    }

    @Test
    public void testDeserializeSetAndReturn() throws Exception {
        Object instance = new Object();
        JsonDeserializer deser = mock(JsonDeserializer.class);
        NullValueProvider nva = mock(NullValueProvider.class);
        JsonToken token = JsonToken.VALUE_NULL;
        Mockito.when(p.hasToken(token)).thenReturn(true);
        DeserializationContext ctxt = mock(DeserializationContext.class);
        Object value = new Object();
        Mockito.when(nva.getNullValue(ctxt)).thenReturn(value);
        
        fieldProperty._valueTypeDeserializer = null;
        fieldProperty._nullProvider = nva;
        fieldProperty._valueDeserializer = deser;
        
        fieldProperty.deserializeSetAndReturn(p, ctxt, instance);
        
        Mockito.verify(nva, Mockito.times(1)).getNullValue(Mockito.eq(ctxt));
        
        Mockito.verify(deser, Mockito.times(1)).deserialize(Mockito.eq(p), Mockito.eq(ctxt));
        Mockito.verify(fieldProperty._field, Mockito.times(1)).set(Mockito.eq(instance), Mockito.eq(value));
        
        token = JsonToken.VALUE_NUMBER_INT;
        Mockito.when(p.hasToken(token)).thenReturn(true);
        Mockito.when(deser.deserialize(Mockito.eq(p), Mockito.eq(ctxt))).thenReturn(value);
        
        fieldProperty.deserializeSetAndReturn(p, ctxt, instance);
        
        Mockito.verify(fieldProperty._field, Mockito.times(2)).set(Mockito.eq(instance), Mockito.eq(value));
        
        Mockito.when(p.hasToken(token)).thenReturn(false);
        
        assertEquals(instance, fieldProperty.deserializeSetAndReturn(p, ctxt, instance));
    }

    @Test
    public void testSet() throws Exception {
        Object instance = new Object();
        Object value = new Object();
        
        fieldProperty.set(instance, value);
        
        Mockito.verify(fieldProperty._field, Mockito.times(1)).set(Mockito.eq(instance), Mockito.eq(value));
    }

    @Test
    public void testSetAndReturn() throws Exception {
        Object instance = new Object();
        Object value = new Object();
        
        fieldProperty.setAndReturn(instance, value);
        
        Mockito.verify(fieldProperty._field, Mockito.times(1)).set(Mockito.eq(instance), Mockito.eq(value));
        
        assertEquals(instance, fieldProperty.setAndReturn(instance, value));
    }
    
}
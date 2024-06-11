package com.fasterxml.jackson.databind.jsontype.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.util.EnumMap;
import java.util.EnumSet;

import com.fasterxml.jackson.databind.DatabindContext;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.fasterxml.jackson.databind.util.ClassUtil;

import org.junit.Before;
import org.junit.Test;

public class ClassNameIdResolverTest {

    private ClassNameIdResolver resolver;
    private JavaType baseType;
    private TypeFactory typeFactory;
    
    @Before
    public void setup() {
        baseType = mock(JavaType.class);
        typeFactory = mock(TypeFactory.class);
        resolver = new ClassNameIdResolver(baseType, typeFactory);
    }

    @Test
    public void testGetMechanism() {
        assertEquals(JsonTypeInfo.Id.CLASS, resolver.getMechanism());
    }
    
    @Test
    public void testRegisterSubtype() {
        // Since registerSubtype is not used, there is nothing to test here
    }
    
    @Test
    public void testIdFromValue() {
        Object value = new Object();
        Class<?> cls = Object.class;
        
        when(baseType.getRawClass()).thenReturn(cls);
        when(typeFactory.constructFromCanonical(anyString())).thenReturn(baseType);
        
        String result = resolver.idFromValue(value);
        
        verify(resolver).idFromValue(value, cls, typeFactory);
        assertEquals(result, "java.lang.Object");
    }
    
    @Test
    public void testIdFromValueAndType() {
        Object value = new Object();
        Class<?> cls = Object.class;
        
        when(typeFactory.constructFromCanonical(anyString())).thenReturn(baseType);
        
        String result = resolver.idFromValueAndType(value, cls);
        
        verify(resolver).idFromValue(value, cls, typeFactory);
        assertEquals(result, "java.lang.Object");
    }
    
    @Test
    public void testTypeFromId() throws IOException {
        DatabindContext context = mock(DeserializationContext.class);
        String id = "java.lang.Object";
        JavaType expectedType = mock(JavaType.class);
        
        when(resolver._typeFromId(id, context)).thenReturn(expectedType);
        
        JavaType result = resolver.typeFromId(context, id);
        
        assertEquals(result, expectedType);
    }
    
    @Test
    public void testTypeFromId_withUnknownTypeId() throws IOException {
        DeserializationContext context = mock(DeserializationContext.class);
        String id = "unknown";
        
        when(context.handleUnknownTypeId(any(JavaType.class), eq(id), eq(resolver), anyString())).thenReturn(baseType);
        
        JavaType result = resolver.typeFromId(context, id);
        
        assertEquals(result, baseType);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testTypeFromId_withInvalidTypeId() throws IOException {
        DatabindContext context = mock(DeserializationContext.class);
        String id = "invalid";
        
        when(context.getTypeFactory()).thenReturn(typeFactory);
        when(typeFactory.findClass(id)).thenThrow(new ClassNotFoundException());
        
        resolver.typeFromId(context, id);
    }
    
    @Test
    public void test_idFrom() {
        Object value = new Object();
        Class<?> cls = Object.class;
        TypeFactory typeFactory = mock(TypeFactory.class);
        
        String result = resolver._idFrom(value, cls, typeFactory);
        
        assertEquals(result, "java.lang.Object");
    }
    
    @Test
    public void test_idFrom_withEnum() {
        Object value = mock(Enum.class);
        Class<?> cls = Object.class;
        TypeFactory typeFactory = mock(TypeFactory.class);
        
        when(cls.isEnum()).thenReturn(false);
        when(cls.getSuperclass()).thenReturn(Enum.class);
        
        String result = resolver._idFrom(value, cls, typeFactory);
        
        assertEquals(result, "java.lang.Enum");
    }
    
    @Test
    public void test_idFrom_withEnumSet() {
        Object value = mock(EnumSet.class);
        Class<?> cls = Object.class;
        TypeFactory typeFactory = mock(TypeFactory.class);
        Class<?> enumClass = Enum.class;
        JavaType collectionType = mock(JavaType.class);
        
        when(cls.getName()).thenReturn("java.util.EnumSet");
        when(ClassUtil.findEnumType((EnumSet<?>) value)).thenReturn(enumClass);
        when(typeFactory.constructCollectionType(EnumSet.class, enumClass)).thenReturn(collectionType);
        when(collectionType.toCanonical()).thenReturn("java.util.EnumSet");
        
        String result = resolver._idFrom(value, cls, typeFactory);
        
        verify(cls).getName();
        verify(ClassUtil).findEnumType((EnumSet<?>) value);
        verify(typeFactory).constructCollectionType(EnumSet.class, enumClass);
        assertEquals(result, "java.util.EnumSet");
    }
    
    @Test
    public void test_idFrom_withEnumMap() {
        Object value = mock(EnumMap.class);
        Class<?> cls = Object.class;
        TypeFactory typeFactory = mock(TypeFactory.class);
        Class<?> enumClass = Enum.class;
        Class<?> valueClass = Object.class;
        JavaType mapType = mock(JavaType.class);
        
        when(cls.getName()).thenReturn("java.util.EnumMap");
        when(ClassUtil.findEnumType((EnumMap<?, ?>) value)).thenReturn(enumClass);
        when(typeFactory.constructMapType(EnumMap.class, enumClass, valueClass)).thenReturn(mapType);
        when(mapType.toCanonical()).thenReturn("java.util.EnumMap");
        
        String result = resolver._idFrom(value, cls, typeFactory);
        
        verify(cls).getName();
        verify(ClassUtil).findEnumType((EnumMap<?, ?>) value);
        verify(typeFactory).constructMapType(EnumMap.class, enumClass, valueClass);
        assertEquals(result, "java.util.EnumMap");
    }
    
    @Test
    public void test_idFrom_withArraysAndCollections() {
        Object value = mock(List.class);
        Class<?> cls = Object.class;
        TypeFactory typeFactory = mock(TypeFactory.class);
        
        when(cls.getName()).thenReturn("java.util.Arrays$ArrayList");
        
        String result = resolver._idFrom(value, cls, typeFactory);
        
        verify(cls, times(2)).getName();
        assertEquals(result, "java.util.ArrayList");
    }
    
    @Test
    public void test_idFrom_withInnerClasses() {
        Object value = new Object();
        Class<?> cls = Object.class;
        TypeFactory typeFactory = mock(TypeFactory.class);
        Class<?> outer = Object.class;
        
        when(cls.getName()).thenReturn("com.fasterxml.jackson.databind.jsontype.impl.ClassNameIdResolver$InnerClass");
        when(ClassUtil.getOuterClass(cls)).thenReturn(outer);
        
        String result = resolver._idFrom(value, cls, typeFactory);
        
        verify(cls).getName();
        verify(ClassUtil).getOuterClass(cls);
        verify(cls, never()).getRawClass();
        assertEquals(result, "com.fasterxml.jackson.databind.jsontype.impl.ClassNameIdResolver$InnerClass");
    }
    
    @Test
    public void testGetDescForKnownTypeIds() {
        String result = resolver.getDescForKnownTypeIds();
        
        assertEquals(result, "class name used as type id");
    }
}
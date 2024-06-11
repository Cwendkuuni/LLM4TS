package com.fasterxml.jackson.databind.jsontype.impl;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.EnumMap;
import java.util.EnumSet;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.databind.DatabindContext;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.fasterxml.jackson.databind.util.ClassUtil;

public class ClassNameIdResolverTest {
    
    private ClassNameIdResolver resolver;
    private JavaType baseType;
    private TypeFactory typeFactory;
    private DatabindContext context;
    
    @Before
    public void setUp() throws Exception {
        baseType = new JavaType();   
        typeFactory = new TypeFactory();   
        resolver = new ClassNameIdResolver(baseType, typeFactory);
        context = new DeserializationContext();
    }
    
    @Test
    public void testGetMechanism() {
        assertEquals(JsonTypeInfo.Id.CLASS, resolver.getMechanism());
    }
    
    @Test
    public void testRegisterSubtype() {
        // not used with class name-based resolvers
    }
    
    @Test
    public void testIdFromValue() {
        Object value = new Object();
        
        String id = resolver.idFromValue(value);
        
        assertEquals(value.getClass().getName(), id);
    }
    
    @Test
    public void testIdFromValueAndType() {
        Object value = new Object();
        Class<?> type = value.getClass();
        
        String id = resolver.idFromValueAndType(value, type);
        
        assertEquals(type.getName(), id);
    }
    
    @Test
    public void testTypeFromId() throws IOException {
        String id = "java.lang.Object";
        
        JavaType resultType = resolver.typeFromId(context, id);
        
        assertEquals(typeFactory.constructSpecializedType(baseType, Object.class), resultType);
    }
    
    @Test(expected = ClassNotFoundException.class)
    public void testTypeFromIdWithInvalidId() throws IOException {
        String id = "com.example.NonExistentClass";
        
        resolver.typeFromId(context, id);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testTypeFromIdWithInvalidType() throws IOException {
        String id = "java.lang.Integer";
        
        resolver.typeFromId(context, id);
    }
    
    @Test
    public void testTypeFromIdWithEnumSet() throws IOException {
        EnumSet<SomeEnum> value = EnumSet.allOf(SomeEnum.class);
        String id = value.getClass().getName();
        
        JavaType resultType = resolver.typeFromId(context, id);
        
        assertEquals(typeFactory.constructCollectionType(EnumSet.class, SomeEnum.class), resultType);
    }
    
    @Test
    public void testTypeFromIdWithEnumMap() throws IOException {
        EnumMap<SomeEnum, Object> value = new EnumMap<SomeEnum, Object>(SomeEnum.class);
        String id = value.getClass().getName();
        
        JavaType resultType = resolver.typeFromId(context, id);
        
        assertEquals(typeFactory.constructMapType(EnumMap.class, SomeEnum.class, Object.class), resultType);
    }
    
    @Test
    public void testTypeFromIdWithArraysAsList() throws IOException {
        String id = "java.util.Arrays$ArrayList";
        
        JavaType resultType = resolver.typeFromId(context, id);
        
        assertEquals(typeFactory.constructCollectionType(java.util.ArrayList.class, Object.class), resultType);
    }
    
    @Test
    public void testTypeFromIdWithInnerClass() throws IOException {
        InnerClass value = new InnerClass();
        String id = value.getClass().getName();
        
        JavaType resultType = resolver.typeFromId(context, id);
        
        assertEquals(typeFactory.constructSpecializedType(baseType, InnerClass.class), resultType);
    }
    
    @Test
    public void testIdFromWithEnumSubType() {
        String id = resolver._idFrom(SomeEnum.VALUE, SomeEnum.class, typeFactory);
        
        assertEquals(SomeEnum.class.getName(), id);
    }
    
    @Test
    public void testIdFromWithSubClassOfEnum() {
        String id = resolver._idFrom(SubEnum.VALUE, SubEnum.class, typeFactory);
        
        assertEquals(SubEnum.class.getSuperclass().getName(), id);
    }
    
    private enum SomeEnum {
        VALUE;
    }
    
    private static class SubEnum extends Enum<SubEnum> {
        public static final SubEnum VALUE = new SubEnum();
        
        protected SubEnum() {
            super("VALUE", 0);
        }
    }
    
    private static class InnerClass {
        
    }

}
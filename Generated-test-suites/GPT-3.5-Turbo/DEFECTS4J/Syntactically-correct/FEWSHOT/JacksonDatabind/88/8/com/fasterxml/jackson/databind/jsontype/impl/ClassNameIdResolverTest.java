
package com.fasterxml.jackson.databind.jsontype.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.type.TypeFactory;

import org.junit.Before;
import org.junit.Test;

public class ClassNameIdResolverTest {
    private ClassNameIdResolver classNameIdResolver;
    private JavaType baseType;
    private TypeFactory typeFactory;

    @Before
    public void setUp() {
        baseType = mock(JavaType.class);
        typeFactory = mock(TypeFactory.class);
        classNameIdResolver = new ClassNameIdResolver(baseType, typeFactory);
    }
    
    @Test
    public void testGetMechanism() {
        assertEquals(JsonTypeInfo.Id.CLASS, classNameIdResolver.getMechanism());
    }
    
    @Test
    public void testRegisterSubtype() {
        // not used with class name - based resolvers
    }
    
    @Test
    public void testIdFromValue() {
        Object value = new Object();
        Class<?> valueClass = value.getClass();
        String expectedId = valueClass.getName();
        
        String id = classNameIdResolver.idFromValue(value);
        
        assertEquals(expectedId, id);
    }

    @Test
    public void testIdFromValueAndType() {
        Object value = new Object();
        Class<?> type = value.getClass();
        String expectedId = type.getName();
        
        String id = classNameIdResolver.idFromValueAndType(value, type);
        
        assertEquals(expectedId, id);
    }

    @Test
    public void testTypeFromId() throws IOException {
        DatabindContext context = mock(DatabindContext.class);
        String id = "java.lang.String";
        JavaType expectedType = mock(JavaType.class);
        when(typeFactory.constructSpecializedType(baseType, String.class)).thenReturn(expectedType);
        
        JavaType type = classNameIdResolver.typeFromId(context, id);
        
        assertEquals(expectedType, type);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testTypeFromInvalidId() throws IOException {
        DatabindContext context = mock(DatabindContext.class);
        String invalidId = "InvalidId";
        
        classNameIdResolver.typeFromId(context, invalidId);
    }
    
    @Test
    public void testTypeFromUnknownId() throws IOException {
        DatabindContext context = mock(DatabindContext.class);
        String unknownId = "UnknownId";
        String errorMessage = "no such class found";
        when(context instanceof DeserializationContext).thenReturn(true);
        DeserializationContext deserializationContext = (DeserializationContext) context;
        when(deserializationContext.handleUnknownTypeId(baseType, unknownId, classNameIdResolver, errorMessage)).thenReturn(null);
        
        JavaType type = classNameIdResolver.typeFromId(context, unknownId);
        
        assertNull(type);
    }
    
    @Test
    public void testTypeFromIdWithGenerics() throws IOException {
        DatabindContext context = mock(DatabindContext.class);
        String id = "java.util.List<java.lang.String>";
        JavaType expectedType = mock(JavaType.class);
        when(typeFactory.constructFromCanonical(id)).thenReturn(expectedType);
        
        JavaType type = classNameIdResolver.typeFromId(context, id);
        
        assertEquals(expectedType, type);
    }
    
    @Test
    public void testTypeFromIdWithEnumSet() throws IOException {
        DatabindContext context = mock(DatabindContext.class);
        String id = "java.util.EnumSet";
        JavaType expectedType = mock(JavaType.class);
        when(typeFactory.constructCollectionType(EnumSet.class, Object.class)).thenReturn(expectedType);
        
        JavaType type = classNameIdResolver.typeFromId(context, id);
        
        assertEquals(expectedType, type);
    }
    
    @Test
    public void testTypeFromIdWithEnumMap() throws IOException {
        DatabindContext context = mock(DatabindContext.class);
        String id = "java.util.EnumMap";
        JavaType expectedType = mock(JavaType.class);
        when(typeFactory.constructMapType(EnumMap.class, Object.class, Object.class)).thenReturn(expectedType);
        
        JavaType type = classNameIdResolver.typeFromId(context, id);
        
        assertEquals(expectedType, type);
    }
    
    @Test
    public void testTypeFromIdWithArraysList() throws IOException {
        DatabindContext context = mock(DatabindContext.class);
        String id = "java.util.Arrays.ArrayList";
        JavaType expectedType = mock(JavaType.class);
        when(typeFactory.constructSpecializedType(baseType, ArrayList.class)).thenReturn(expectedType);
        
        JavaType type = classNameIdResolver.typeFromId(context, id);
        
        assertEquals(expectedType, type);
    }
    
    @Test
    public void testTypeFromIdWithInnerClass() throws IOException {
        DatabindContext context = mock(DatabindContext.class);
        String id = "com.fasterxml.jackson.databind.jsontype.impl.ClassNameIdResolverTest$InnerClass";
        JavaType expectedType = mock(JavaType.class);
        when(typeFactory.constructSpecializedType(baseType, InnerClass.class)).thenReturn(expectedType);
        
        JavaType type = classNameIdResolver.typeFromId(context, id);
        
        assertEquals(expectedType, type);
    }
    
    static class InnerClass {
    }
    
    @Test
    public void testIdFromValueWithEnumSubtype() {
        TestEnum enumValue = TestEnum.VALUE;
        
        String id = classNameIdResolver.idFromValue(enumValue);
        
        assertEquals("com.fasterxml.jackson.databind.jsontype.impl.ClassNameIdResolverTest$TestEnum", id);
    }
    
    enum TestEnum {
        VALUE
    }
    
    @Test
    public void testIdFromValueWithEnumSubclass() {
        SubEnum subEnumValue = SubEnum.SUB_VALUE;
        
        String id = classNameIdResolver.idFromValue(subEnumValue);
        
        assertEquals("com.fasterxml.jackson.databind.jsontype.impl.ClassNameIdResolverTest$SuperEnum", id);
    }
    
    enum SuperEnum {
        VALUE
    }
    
    enum SubEnum {
        SUB_VALUE
    }
    
    @Test
    public void testIdFromValueWithNonEnumSubtype() {
        SubObject subObject = new SubObject();
        
        String id = classNameIdResolver.idFromValue(subObject);
        
        assertEquals("com.fasterxml.jackson.databind.jsontype.impl.ClassNameIdResolverTest$SubObject", id);
    }
    
    private class SubObject {
        
    }
    
    @Test
    public void testIdFromValueWithNonEnumSubclass() {
        SubSubObject subSubObject = new SubSubObject();
        
        String id = classNameIdResolver.idFromValue(subSubObject);
        
        assertEquals("com.fasterxml.jackson.databind.jsontype.impl.ClassNameIdResolverTest$SuperObject", id);
    }
    
    private class SuperObject {
        
    }
    
    private class SubSubObject extends SuperObject {
        
    }
    
    @Test
    public void testIdFromValueWithJavaUtilPackage() {
        List<String> list = new ArrayList<>();
        
        String id = classNameIdResolver.idFromValue(list);
        
        assertEquals("java.util.ArrayList", id);
    }
    
}

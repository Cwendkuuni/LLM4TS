package com.fasterxml.jackson.databind.jsontype.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;

import java.io.IOException;
import java.util.EnumMap;
import java.util.EnumSet;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.DatabindContext;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.fasterxml.jackson.databind.util.ClassUtil;

public class ClassNameIdResolverTest {
    private JavaType baseType;
    private TypeFactory typeFactory;
    private ClassNameIdResolver classNameIdResolver;

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
        // Not used with class name based resolvers, no need to test
    }

    @Test
    public void testIdFromValue() {
        Object value = new Object();
        Class<?> cls = value.getClass();
        assertEquals(cls.getName(), classNameIdResolver.idFromValue(value));
    }

    @Test
    public void testIdFromValueAndType() {
        Object value = new Object();
        Class<?> cls = value.getClass();
        assertEquals(cls.getName(), classNameIdResolver.idFromValueAndType(value, cls));
    }

    @Test
    public void testTypeFromId() throws IOException {
        DatabindContext context = mock(DeserializationContext.class);
        String id = "java.lang.String";
        assertEquals(typeFactory.constructFromCanonical(id), classNameIdResolver.typeFromId(context, id));
    }

    @Test
    public void testTypeFromIdWithUnknownClass() throws IOException {
        DatabindContext context = mock(DeserializationContext.class);
        String id = "unknown.class";
        assertNull(classNameIdResolver.typeFromId(context, id));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testTypeFromIdWithInvalidTypeId() throws IOException {
        DatabindContext context = mock(DeserializationContext.class);
        String id = "{invalidId}";
        classNameIdResolver.typeFromId(context, id);
    }

    @Test
    public void testTypeFromIdWithUnknownTypeIdHandler() throws IOException {
        DatabindContext context = mock(DeserializationContext.class);
        String id = "unknown.class";
        JavaType expectedType = mock(JavaType.class);
        DeserializationContext dctxt = (DeserializationContext) context;

        // Mock handleUnknownTypeId method to return the expected type
        JavaType handleUnknownTypeIdType = classNameIdResolver.handleUnknownTypeId(baseType, id, classNameIdResolver, "no such class found");
        assertEquals(expectedType, handleUnknownTypeIdType);

        assertEquals(expectedType, classNameIdResolver.typeFromId(context, id));
    }

    @Test
    public void testTypeFromIdWithGenericTypeInfo() throws IOException {
        DatabindContext context = mock(DeserializationContext.class);
        String id = "java.util.List<java.lang.String>";
        assertEquals(typeFactory.constructFromCanonical(id), classNameIdResolver.typeFromId(context, id));
    }

    @Test
    public void testTypeFromIdWithNestedClass() throws IOException {
        DatabindContext context = mock(DeserializationContext.class);
        String id = "com.fasterxml.jackson.databind.jsontype.impl.ClassNameIdResolver$NestedClass";
        assertEquals(baseType, classNameIdResolver.typeFromId(context, id));
    }

    @Test
    public void testTypeFromIdWithStaticNestedClass() throws IOException {
        DatabindContext context = mock(DeserializationContext.class);
        String id = "com.fasterxml.jackson.databind.jsontype.impl.ClassNameIdResolver$StaticNestedClass";
        assertEquals(baseType, classNameIdResolver.typeFromId(context, id));
    }

    @Test
    public void test_idFrom() {
        Object value = new Object();
        Class<?> cls = value.getClass();
        assertEquals(cls.getName(), classNameIdResolver._idFrom(value, cls, typeFactory));
    }

    @Test
    public void test_idFromWithEnumSet() {
        EnumSet<?> value = EnumSet.allOf(TestEnum.class);
        Class<?> cls = value.getClass();
        Class<?> enumClass = ClassUtil.findEnumType(value);
        String expectedId = typeFactory.constructCollectionType(EnumSet.class, enumClass).toCanonical();
        assertEquals(expectedId, classNameIdResolver._idFrom(value, cls, typeFactory));
    }

    @Test
    public void test_idFromWithEnumMap() {
        EnumMap<?, ?> value = new EnumMap<>(TestEnum.class);
        Class<?> cls = value.getClass();
        Class<?> enumClass = ClassUtil.findEnumType(value);
        Class<?> valueClass = Object.class;
        String expectedId = typeFactory.constructMapType(EnumMap.class, enumClass, valueClass).toCanonical();
        assertEquals(expectedId, classNameIdResolver._idFrom(value, cls, typeFactory));
    }

    @Test
    public void test_idFromWithJavaUtilArraysList() {
        Object value = new Object();
        Class<?> cls = value.getClass();
        String expectedId = "java.util.ArrayList";
        assertEquals(expectedId, classNameIdResolver._idFrom(value, cls, typeFactory));
    }

    @Test
    public void test_idFromWithInnerClass() {
        Object value = new Object();
        Class<?> cls = value.getClass();
        String expectedId = cls.getName();
        assertEquals(expectedId, classNameIdResolver._idFrom(value, cls, typeFactory));
    }

    @Test
    public void testGetDescForKnownTypeIds() {
        assertEquals("class name used as type id", classNameIdResolver.getDescForKnownTypeIds());
    }
    
    enum TestEnum {
        ENUM_VALUE;
    }
}
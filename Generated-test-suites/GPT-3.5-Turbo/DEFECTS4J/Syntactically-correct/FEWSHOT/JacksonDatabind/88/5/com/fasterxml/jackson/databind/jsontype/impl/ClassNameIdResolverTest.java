package com.fasterxml.jackson.databind.jsontype.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.util.EnumMap;
import java.util.EnumSet;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.fasterxml.jackson.databind.util.ClassUtil;

public class ClassNameIdResolverTest {

    private ClassNameIdResolver idResolver;
    private JavaType baseType;
    private TypeFactory typeFactory;
    private Object value;
    private DatabindContext context;

    @Before
    public void setUp() {
        baseType = mock(JavaType.class);
        typeFactory = mock(TypeFactory.class);
        value = new Object();
        context = mock(DatabindContext.class);
        idResolver = new ClassNameIdResolver(baseType, typeFactory);
    }

    @Test
    public void testGetMechanism() {
        JsonTypeInfo.Id mechanism = idResolver.getMechanism();
        assertEquals(JsonTypeInfo.Id.CLASS, mechanism);
    }

    @Test
    public void testRegisterSubtype() {
        Class<?> type = mock(Class.class);
        String name = "name";
        idResolver.registerSubtype(type, name);
        // Verify that the method is not used
        verifyNoMoreInteractions(type);
        verifyNoMoreInteractions(name);
    }

    @Test
    public void testIdFromValue() {
        String expected = "result";
        Class<?> cls = value.getClass();
        when(typeFactory.constructSpecializedType(baseType, cls)).thenReturn(baseType);
        when(baseType.toCanonical()).thenReturn(expected);

        String id = idResolver.idFromValue(value);

        assertEquals(expected, id);
    }

    @Test
    public void testIdFromValueAndType() {
        String expected = "result";
        Class<?> type = value.getClass();
        when(typeFactory.constructSpecializedType(baseType, type)).thenReturn(baseType);
        when(baseType.toCanonical()).thenReturn(expected);

        String id = idResolver.idFromValueAndType(value, type);

        assertEquals(expected, id);
    }

    @Test
    public void testTypeFromId() throws IOException {
        String id = "id";
        JavaType expected = mock(JavaType.class);
        when(idResolver._typeFromId(id, context)).thenReturn(expected);

        JavaType result = idResolver.typeFromId(context, id);

        assertEquals(expected, result);
    }

    @Test
    public void testTypeFromIdWithUnknownTypeId() throws IOException {
        String id = "id";
        when(context instanceof DeserializationContext).thenReturn(true);
        DeserializationContext dctxt = Mockito.mock(DeserializationContext.class);
        when(context.getTypeFactory()).thenReturn(typeFactory);
        when(context.handleUnknownTypeId(baseType, id, idResolver, "no such class found")).thenReturn(null);
        when(classNotFoundContext.handleUnknownTypeId(baseType, id, idResolver, "no such class found")).thenReturn(null);
        when(typeFactory.findClass(id)).thenThrow(ClassNotFoundException.class);

        JavaType result = idResolver.typeFromId(context, id);

        assertEquals(null, result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testTypeFromIdWithInvalidTypeId() throws IOException {
        String id = "id";
        when(typeFactory.findClass(id)).thenThrow(Exception.class);

        idResolver.typeFromId(context, id);
    }

    @Test
    public void test_typeFromId_specialized_type() throws IOException {
        String id = "id";
        JavaType expected = mock(JavaType.class);
        when(idResolver._typeFromId(id, context)).thenReturn(expected);
        when(expected.hasRawClass(EnumSet.class)).thenReturn(true);

        JavaType result = idResolver.typeFromId(context, id);

        assertEquals(expected, result);
    }

    @Test
    public void testIdFromValue_enumSet_doesNotUseToString() {
        String expected = "java.util.EnumSet";
        EnumSet<?> enumSet = mock(EnumSet.class);

        String id = idResolver.idFromValue(enumSet);

        assertEquals(expected, id);
    }

    @Test
    public void testIdFromValue_enumMap_doesNotUseToString() {
        String expected = "java.util.EnumMap";
        EnumMap<?, ?> enumMap = mock(EnumMap.class);

        String id = idResolver.idFromValue(enumMap);

        assertEquals(expected, id);
    }

    @Test
    public void testIdFromValue_enumValue() {
        String expected = "java.lang.Enum";
        Enum<?> enumValue = mock(Enum.class);

        String id = idResolver.idFromValue(enumValue);

        assertEquals(expected, id);
    }

    @Test
    public void testIdFromValue_enumSubClass() {
        String expected = "java.lang.Enum";
        EnumSubClass enumSubClass = mock(EnumSubClass.class);

        String id = idResolver.idFromValue(enumSubClass);

        assertEquals(expected, id);
    }

    @Test
    public void testIdFromValue_notAnEnumSubClass() {
        String expected = "sub.class";
        SubClass subClass = mock(SubClass.class);

        String id = idResolver.idFromValue(subClass);

        assertEquals(expected, id);
    }

    @Test
    public void testIdFromValue_javaUtilClass() {
        String expected = "java.util.Map";
        Enum<?> value = mock(Enum.class);
        Map<?, ?> map = mock(Map.class, Mockito.RETURNS_DEEP_STUBS);
        Class<?> enumClass = ClassUtil.findEnumType(value);
        when(typeFactory.constructMapType(EnumMap.class, enumClass, Object.class).toCanonical()).thenReturn(expected);

        String id = idResolver.idFromValue(map);

        assertEquals(expected, id);
    }

    @Test
    public void test_idFrom_getOuterClass_nullOuterClass() {
        String expected = "class.name";
        OuterClass outerClass = mock(OuterClass.class);
        when(_baseType.getRawClass()).thenReturn(OuterClass.class);
        when(outerClass.getClass()).thenReturn(SubClass.class);
        when(outerClass.getName()).thenReturn(expected);

        String id = idResolver._idFrom(outerClass, SubClass.class, typeFactory);

        assertEquals(expected, id);
    }

    @Test
    public void test_idFrom_getOuterClass_nonStaticType() {
        String expected = "class.name";
        OuterClass outerClass = mock(OuterClass.class);
        when(_baseType.getRawClass()).thenReturn(StaticClass.class);
        when(classUtilOuterClass.getOuterClass(StaticClass.class)).thenReturn(outerClass);
        when(classUtilOuterClass.getOuterClass(StaticClass.class)).thenReturn(null);
        when(outerClass.getClass()).thenReturn(OuterClass.class);
        when(outerClass.getName()).thenReturn(expected);

        String id = idResolver._idFrom(outerClass, OuterClass.class, typeFactory);

        assertEquals(expected, id);
    }

    @Test
    public void test_getDescForKnownTypeIds() {
        String expected = "class name used as type id";

        String desc = idResolver.getDescForKnownTypeIds();

        assertEquals(expected, desc);
    }

    enum EnumSubClass {
        VAL
    }

    class OuterClass {
        class InnerClass {

        }
    }

    static class StaticClass {

    }

    class SubClass {

    }
}
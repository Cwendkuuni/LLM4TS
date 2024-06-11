package com.fasterxml.jackson.databind.jsontype.impl;

import java.io.IOException;
import java.util.EnumMap;
import java.util.EnumSet;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.DatabindContext;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.fasterxml.jackson.databind.util.ClassUtil;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class ClassNameIdResolverTest {

    private ClassNameIdResolver resolver;

    @Before
    public void setUp() {
        resolver = new ClassNameIdResolver(null, null);
    }

    @After
    public void tearDown() {
        resolver = null;
    }

    @Test
    public void testGetMechanism() {
        JsonTypeInfo.Id mechanism = resolver.getMechanism();
        Assert.assertEquals(JsonTypeInfo.Id.CLASS, mechanism);
    }

    @Test
    public void testRegisterSubtype() {
        Class<?> type = String.class;
        String name = "String";
        resolver.registerSubtype(type, name);
    }

    @Test
    public void testIdFromValue() {
        Object value = new Object();
        String id = resolver.idFromValue(value);
        Assert.assertNotNull(id);
    }

    @Test
    public void testIdFromValueAndType() {
        Object value = new Object();
        Class<?> type = Object.class;
        String id = resolver.idFromValueAndType(value, type);
        Assert.assertNotNull(id);
    }

    @Test
    public void testTypeFromId() throws IOException {
        DatabindContext context = null;
        String id = "java.lang.String";
        JavaType javaType = resolver.typeFromId(context, id);
        Assert.assertNotNull(javaType);
    }

    @Test
    public void test_typeFromId_UnknownClass() throws IOException {
        DatabindContext context = null;
        String id = "com.example.UnknownClass";
        JavaType javaType = resolver.typeFromId(context, id);
        Assert.assertNull(javaType);
    }

    @Test(expected = IllegalArgumentException.class)
    public void test_typeFromId_InvalidTypeId() throws IOException {
        DatabindContext context = null;
        String id = "InvalidTypeId";
        resolver.typeFromId(context, id);
    }

    @Test
    public void test_typeFromId_EnumSet() throws IOException {
        DatabindContext context = null;
        String id = "java.util.EnumSet";
        JavaType javaType = resolver.typeFromId(context, id);
        Assert.assertNotNull(javaType);
    }

    @Test
    public void test_typeFromId_EnumMap() throws IOException {
        DatabindContext context = null;
        String id = "java.util.EnumMap";
        JavaType javaType = resolver.typeFromId(context, id);
        Assert.assertNotNull(javaType);
    }

    @Test
    public void test_typeFromId_ArraysList() throws IOException {
        DatabindContext context = null;
        String id = "java.util.ArrayList";
        JavaType javaType = resolver.typeFromId(context, id);
        Assert.assertNotNull(javaType);
    }

    @Test
    public void test_typeFromId_InnerClass() throws IOException {
        DatabindContext context = null;
        String id = "com.example.Outer$Inner";
        JavaType javaType = resolver.typeFromId(context, id);
        Assert.assertNotNull(javaType);
    }

    @Test
    public void test_typeFromId_NonStaticOuterClass() throws IOException {
        DatabindContext context = null;
        String id = "com.example.Outer$2Inner";
        JavaType javaType = resolver.typeFromId(context, id);
        Assert.assertNotNull(javaType);
    }

    @Test
    public void test_typeFromId_NullContext() throws IOException {
        String id = "java.lang.String";
        JavaType javaType = resolver.typeFromId(null, id);
        Assert.assertNotNull(javaType);
    }

    @Test
    public void test_typeFromId_EmptyId() throws IOException {
        DatabindContext context = null;
        String id = "";
        JavaType javaType = resolver.typeFromId(context, id);
        Assert.assertNull(javaType);
    }

    @Test
    public void test_typeFromId_NullId() throws IOException {
        DatabindContext context = null;
        String id = null;
        JavaType javaType = resolver.typeFromId(context, id);
        Assert.assertNull(javaType);
    }

    @Test
    public void test_typeFromId_DifferentTypeFactory() throws IOException {
        DatabindContext context = null;
        String id = "java.lang.String";
        resolver = new ClassNameIdResolver(null, TypeFactory.defaultInstance());
        JavaType javaType = resolver.typeFromId(context, id);
        Assert.assertNotNull(javaType);
    }

    @Test
    public void test_idFrom() {
        Object value = new Object();
        Class<?> cls = Object.class;
        TypeFactory typeFactory = null;
        String id = resolver._idFrom(value, cls, typeFactory);
        Assert.assertNotNull(id);
    }

    @Test
    public void test_idFrom_ValueIsEnum() {
        Object value = TestEnum.VALUE;
        Class<?> cls = TestEnum.class;
        TypeFactory typeFactory = null;
        String id = resolver._idFrom(value, cls, typeFactory);
        Assert.assertNotNull(id);
    }

    @Test
    public void test_idFrom_ValueIsEnumSubclass() {
        Object value = TestEnumSubclass.SUB_VALUE;
        Class<?> cls = TestEnumSubclass.class;
        TypeFactory typeFactory = null;
        String id = resolver._idFrom(value, cls, typeFactory);
        Assert.assertNotNull(id);
    }

    @Test
    public void test_idFrom_ValueIsEnumSet() {
        Object value = EnumSet.allOf(TestEnum.class);
        Class<?> cls = EnumSet.class;
        TypeFactory typeFactory = null;
        String id = resolver._idFrom(value, cls, typeFactory);
        Assert.assertNotNull(id);
    }

    @Test
    public void test_idFrom_ValueIsEnumMap() {
        Object value = new EnumMap<>(TestEnum.class);
        Class<?> cls = EnumMap.class;
        TypeFactory typeFactory = null;
        String id = resolver._idFrom(value, cls, typeFactory);
        Assert.assertNotNull(id);
    }

    @Test
    public void test_idFrom_ValueIsArrayList() {
        Object value = new ArrayList<>();
        Class<?> cls = ArrayList.class;
        TypeFactory typeFactory = null;
        String id = resolver._idFrom(value, cls, typeFactory);
        Assert.assertNotNull(id);
    }

    @Test
    public void test_idFrom_DifferentTypeFactory() {
        Object value = new Object();
        Class<?> cls = Object.class;
        resolver = new ClassNameIdResolver(null, TypeFactory.defaultInstance());
        TypeFactory typeFactory = null;
        String id = resolver._idFrom(value, cls, typeFactory);
        Assert.assertNotNull(id);
    }

    @Test
    public void testGetDescForKnownTypeIds() {
        String desc = resolver.getDescForKnownTypeIds();
        Assert.assertEquals("class name used as type id", desc);
    }

    private enum TestEnum {
        VALUE
    }

    private static class TestEnumSubclass extends TestEnum {
        private static final TestEnum SUB_VALUE = TestEnum.VALUE;
    }
}
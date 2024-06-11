package com.fasterxml.jackson.databind.jsontype.impl;

import com.fasterxml.jackson.databind.DatabindContext;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.jsontype.TypeIdResolverBase;
import com.fasterxml.jackson.databind.type.TypeFactory;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.EnumMap;
import java.util.EnumSet;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class ClassNameIdResolverTest {
    private ClassNameIdResolver classNameIdResolver;

    @Before
    public void setup() {
        JavaType baseType = TypeFactory.defaultInstance().constructType(Object.class);
        TypeFactory typeFactory = TypeFactory.defaultInstance();
        classNameIdResolver = new ClassNameIdResolver(baseType, typeFactory);
    }

    @Test
    public void testGetMechanism() {
        assertEquals(com.fasterxml.jackson.annotation.JsonTypeInfo.Id.CLASS, classNameIdResolver.getMechanism());
    }

    @Test
    public void testRegisterSubtype() {
        classNameIdResolver.registerSubtype(ClassNameIdResolver.class, "ClassNameIdResolver");
    }

    @Test
    public void testIdFromValue() {
        Object value = new Object();
        String id = classNameIdResolver.idFromValue(value);
        assertEquals(Object.class.getName(), id);
    }

    @Test
    public void testIdFromValueAndType() {
        Object value = new Object();
        String id = classNameIdResolver.idFromValueAndType(value, value.getClass());
        assertEquals(Object.class.getName(), id);
    }

    @Test
    public void testTypeFromId() throws IOException {
        DatabindContext context = new ObjectMapper().reader().createDeserializationContext(null, null);
        String id = Object.class.getName();
        JavaType javaType = classNameIdResolver.typeFromId(context, id);
        assertEquals(Object.class, javaType.getRawClass());
    }

    @Test
    public void testTypeFromIdNotFound() throws IOException {
        DatabindContext context = new ObjectMapper().reader().createDeserializationContext(null, null);
        String id = "com.example.DoesNotExist";
        JavaType javaType = classNameIdResolver.typeFromId(context, id);
        assertNull(javaType);
    }

    // Test protected method _typeFromId

    @Test
    public void testTypeFromIdWithGenerics() throws IOException {
        DatabindContext context = new ObjectMapper().reader().createDeserializationContext(null, null);
        String id = "java.util.List<java.lang.String>";
        JavaType javaType = classNameIdResolver._typeFromId(id, context);
        assertEquals("java.util.List", javaType.getRawClass().getName());
        assertEquals("java.lang.String", javaType.getBindings().getBoundType(0).getRawClass().getName());
    }

    @Test(expected = IOException.class)
    public void testTypeFromIdWithInvalidGenerics_NotFound() throws IOException {
        DatabindContext context = new ObjectMapper().reader().createDeserializationContext(null, null);
        String id = "java.util.NoSuchClass<java.lang.String>";
        classNameIdResolver._typeFromId(id, context);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testTypeFromIdWithInvalidGenerics_IllegalArgumentException() throws IOException {
        DatabindContext context = new ObjectMapper().reader().createDeserializationContext(null, null);
        String id = "java.util.List<java.lang.NoSuchClass>";
        classNameIdResolver._typeFromId(id, context);
    }

    @Test
    public void testTypeFromIdWithSpecialCharacters() throws IOException {
        DatabindContext context = new ObjectMapper().reader().createDeserializationContext(null, null);
        String id = "com.example.My$Class";
        JavaType javaType = classNameIdResolver._typeFromId(id, context);
        assertEquals("com.example.My$Class", javaType.getRawClass().getName());
    }

    @Test
    public void testTypeFromIdWithoutSpecialCharacters() throws IOException {
        DatabindContext context = new ObjectMapper().reader().createDeserializationContext(null, null);
        String id = "com.example.MyClass";
        JavaType javaType = classNameIdResolver._typeFromId(id, context);
        assertEquals("com.example.MyClass", javaType.getRawClass().getName());
    }

    @Test
    public void testTypeFromIdWithEnumSet() throws IOException {
        DatabindContext context = new ObjectMapper().reader().createDeserializationContext(null, null);
        String id = "java.util.EnumSet<java.lang.String>";
        JavaType javaType = classNameIdResolver._typeFromId(id, context);
        assertEquals("java.util.EnumSet", javaType.getRawClass().getName());
        assertEquals("java.lang.String", javaType.getBindings().getBoundType(0).getRawClass().getName());
    }

    @Test
    public void testTypeFromIdWithEnumMap() throws IOException {
        DatabindContext context = new ObjectMapper().reader().createDeserializationContext(null, null);
        String id = "java.util.EnumMap<java.lang.String, java.lang.Integer>";
        JavaType javaType = classNameIdResolver._typeFromId(id, context);
        assertEquals("java.util.EnumMap", javaType.getRawClass().getName());
        assertEquals("java.lang.String", javaType.getBindings().getBoundType(0).getRawClass().getName());
        assertEquals("java.lang.Integer", javaType.getBindings().getBoundType(1).getRawClass().getName());
    }

    @Test
    public void test_idFrom() {
        Object value = new Object();
        String id = classNameIdResolver._idFrom(value, value.getClass(), TypeFactory.defaultInstance());
        assertEquals(Object.class.getName(), id);
    }

    @Test
    public void test_idFrom_Enum() {
        Object value = TestEnum.TEST_VALUE;
        String id = classNameIdResolver._idFrom(value, value.getClass(), TypeFactory.defaultInstance());
        assertEquals(TestEnum.class.getName(), id);
    }

    @Test
    public void test_idFrom_Enum_Subclass() {
        Object value = SubTestEnum.TEST_VALUE;
        String id = classNameIdResolver._idFrom(value, value.getClass(), TypeFactory.defaultInstance());
        assertEquals(SubTestEnum.class.getSuperclass().getName(), id);
    }

    @Test
    public void test_idFrom_java_util_EnumSet() {
        Object value = EnumSet.of(TestEnum.TEST_VALUE);
        String id = classNameIdResolver._idFrom(value, value.getClass(), TypeFactory.defaultInstance());
        assertEquals("java.util.EnumSet", id);
    }

    @Test
    public void test_idFrom_java_util_EnumMap() {
        Object value = new EnumMap<>(TestEnum.class);
        String id = classNameIdResolver._idFrom(value, value.getClass(), TypeFactory.defaultInstance());
        assertEquals("java.util.EnumMap", id);
    }

    @Test
    public void test_idFrom_java_util_List() {
        Object value = Arrays.asList(1, 2, 3);
        String id = classNameIdResolver._idFrom(value, value.getClass(), TypeFactory.defaultInstance());
        assertEquals("java.util.ArrayList", id);
    }

    @Test
    public void test_idFrom_java_util_CollectionsUnmodifiableList() {
        Object value = Collections.unmodifiableList(new ArrayList<>());
        String id = classNameIdResolver._idFrom(value, value.getClass(), TypeFactory.defaultInstance());
        assertEquals("java.util.ArrayList", id);
    }

    enum TestEnum {
        TEST_VALUE
    }

    class SubTestEnum extends TestEnum {
        // Subclass of TestEnum
    }

}
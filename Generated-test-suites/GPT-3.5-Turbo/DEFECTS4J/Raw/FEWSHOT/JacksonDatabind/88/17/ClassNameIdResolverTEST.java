package com.fasterxml.jackson.databind.jsontype.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.fail;

import java.io.IOException;
import java.util.EnumMap;
import java.util.EnumSet;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.DatabindContext;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.fasterxml.jackson.databind.util.ClassUtil;

public class ClassNameIdResolverTest {

    private ClassNameIdResolver classNameIdResolver;
    private JavaType baseType;
    private TypeFactory typeFactory;
    private ObjectMapper objectMapper;

    @Before
    public void setUp() {
        baseType = null;
        typeFactory = null;
        classNameIdResolver = new ClassNameIdResolver(baseType, typeFactory);
        objectMapper = new ObjectMapper();
    }

    @Test
    public void testGetMechanism() {
        assertEquals(JsonTypeInfo.Id.CLASS, classNameIdResolver.getMechanism());
    }

    @Test
    public void testRegisterSubtype() {
        try {
            classNameIdResolver.registerSubtype(null, null);
            fail("Expected UnsupportedOperationException to be thrown");
        } catch (UnsupportedOperationException ex) {
            // expected
        }
    }

    @Test
    public void testIdFromValue() {
        Object value = new Object();
        assertNull(classNameIdResolver.idFromValue(value));
    }

    @Test
    public void testIdFromValueAndType() {
        Object value = new Object();
        Class<?> type = Object.class;
        assertNull(classNameIdResolver.idFromValueAndType(value, type));
    }

    @Test
    public void testTypeFromId() throws IOException {
        DatabindContext context = objectMapper.getDeserializationContext();
        String id = "";
        assertNull(classNameIdResolver.typeFromId(context, id));
    }

    @Test
    public void testTypeFromId_InvalidId() throws IOException {
        DatabindContext context = objectMapper.getDeserializationContext();
        String id = "InvalidId";
        try {
            classNameIdResolver.typeFromId(context, id);
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException ex) {
            assertEquals("Invalid type id 'InvalidId' (for id type 'Id.class')", ex.getMessage());
        }
    }

    @Test
    public void testTypeFromId_ClassNotFoundException() throws IOException {
        DatabindContext context = objectMapper.getDeserializationContext();
        String id = "com.example.NotFoundClass";
        try {
            classNameIdResolver.typeFromId(context, id);
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException ex) {
            assertEquals("Invalid type id 'com.example.NotFoundClass' (for id type 'Id.class'): no such class found", ex.getMessage());
        }
    }

    @Test
    public void test_typeFromId_withValidId() throws IOException {
        DatabindContext context = objectMapper.getDeserializationContext();
        String id = "java.util.ArrayList";
        Class<?> rawBaseType = _baseType.getRawClass();
        JavaType expectedType = typeFactory.constructSpecializedType(_baseType, rawBaseType);
        assertEquals(expectedType, classNameIdResolver.typeFromId(context, id));
    }

    @Test
    public void test_typeFromId_withGenericId() throws IOException {
        DatabindContext context = objectMapper.getDeserializationContext();
        String id = "java.util.List<java.lang.String>";
        JavaType expectedType = typeFactory.constructFromCanonical(id);
        assertEquals(expectedType, classNameIdResolver.typeFromId(context, id));
    }

    @Test
    public void test_typeFromId_withEnumSet() throws IOException {
        DatabindContext context = objectMapper.getDeserializationContext();
        String id = "java.util.EnumSet<java.lang.String>";
        JavaType expectedType = typeFactory.constructCollectionType(EnumSet.class, String.class);
        assertEquals(expectedType, classNameIdResolver.typeFromId(context, id));
    }

    @Test
    public void test_typeFromId_withEnumMap() throws IOException {
        DatabindContext context = objectMapper.getDeserializationContext();
        String id = "java.util.EnumMap<java.lang.String,java.lang.String>";
        JavaType expectedType = typeFactory.constructMapType(EnumMap.class, String.class, String.class);
        assertEquals(expectedType, classNameIdResolver.typeFromId(context, id));
    }

    @Test
    public void test_idFrom_withEnumClass() {
        Class<?> cls = ClassNameIdResolverTestEnum.class;
        Object value = ClassNameIdResolverTestEnum.VALUE1;
        assertEquals("com.fasterxml.jackson.databind.jsontype.impl.ClassNameIdResolverTest$ClassNameIdResolverTestEnum", classNameIdResolver._idFrom(value, cls, typeFactory));
    }

    @Test
    public void test_idFrom_whenClassStartsWithJavaUtil() {
        Class<?> cls = ClassNameIdResolverTestList.class;
        Object value = new ClassNameIdResolverTestList();
        assertEquals("java.util.ArrayList", classNameIdResolver._idFrom(value, cls, typeFactory));
    }

    @Test
    public void test_idFrom_whenClassContainsDollarSign() {
        Class<?> cls = ClassNameIdResolverTest.InnerClass.class;
        Object value = new ClassNameIdResolverTest.InnerClass();
        assertEquals("com.fasterxml.jackson.databind.jsontype.impl.ClassNameIdResolverTest$InnerClass", classNameIdResolver._idFrom(value, cls, typeFactory));
    }

    @Test
    public void test_idFrom_whenClassIsSubclassOfBaseEnum() {
        Class<?> cls = SubClassNameIdResolverTestEnum.class;
        Object value = SubClassNameIdResolverTestEnum.VALUE1;
        assertEquals("com.fasterxml.jackson.databind.jsontype.impl.ClassNameIdResolverTest$SubClassNameIdResolverTestEnum", classNameIdResolver._idFrom(value, cls, typeFactory));
    }

    @Test
    public void test_idFrom_whenClassIsSubclassOfBaseEnumAndNotEnum() {
        Class<?> cls = InvalidClassNameIdResolverTestEnum.class;
        Object value = InvalidClassNameIdResolverTestEnum.VALUE1;
        assertEquals("com.fasterxml.jackson.databind.jsontype.impl.ClassNameIdResolverTest$InvalidClassNameIdResolverTestEnum", classNameIdResolver._idFrom(value, cls, typeFactory));
    }

    @Test
    public void test_idFrom_whenClassIsInnerClassAndStaticTypeIsNot() {
        ClassNameIdResolverTest.InnerClass.staticType = null;
        Class<?> cls = ClassNameIdResolverTest.InnerClass.class;
        Object value = new ClassNameIdResolverTest.InnerClass();
        assertEquals("com.fasterxml.jackson.databind.jsontype.impl.ClassNameIdResolverTest$ClassNameIdResolverTestEnum", classNameIdResolver._idFrom(value, cls, typeFactory));
    }

    @Test
    public void test_idFrom_whenClassIsInnerClassAndStaticTypeIsNotAndNoOuterClass() {
        Class<?> cls = ClassNameIdResolverTest.InnerClass.class;
        Object value = new ClassNameIdResolverTest.InnerClass();
        assertEquals("com.fasterxml.jackson.databind.jsontype.impl.ClassNameIdResolverTest$ClassNameIdResolverTestEnum", classNameIdResolver._idFrom(value, cls, typeFactory));
    }

    @Test
    public void test_idFrom_whenClassIsInnerClassAndStaticTypeIsNotAndOuterClassIsStatic() {
        ClassNameIdResolverTest.InnerClass.staticType = null;
        ClassNameIdResolverTest.InnerClass.outer = new ClassNameIdResolverTest();
        Class<?> cls = ClassNameIdResolverTest.InnerClass.class;
        Object value = new ClassNameIdResolverTest.InnerClass();
        assertEquals("com.fasterxml.jackson.databind.jsontype.impl.ClassNameIdResolverTest$InnerClass", classNameIdResolver._idFrom(value, cls, typeFactory));
    }

    @Test
    public void test_idFrom_whenClassIsInnerClassAndStaticTypeIsNotAndOuterClassIsNotStatic() {
        ClassNameIdResolverTest.InnerClass.staticType = null;
        ClassNameIdResolverTest.InnerClass.outer = new ClassNameIdResolverTest.InnerClass();
        Class<?> cls = ClassNameIdResolverTest.InnerClass.class;
        Object value = new ClassNameIdResolverTest.InnerClass();
        assertEquals("com.fasterxml.jackson.databind.jsontype.impl.ClassNameIdResolverTest$InnerClass", classNameIdResolver._idFrom(value, cls, typeFactory));
    }

    @Test
    public void test_getDescForKnownTypeIds() {
        assertEquals("class name used as type id", classNameIdResolver.getDescForKnownTypeIds());
    }

    private enum ClassNameIdResolverTestEnum {
        VALUE1, VALUE2;
    }

    private static class ClassNameIdResolverTestList extends ArrayList<String> {

    }

    private static class InnerClass {

        private static ClassNameIdResolverTestEnum staticType;
        private static ClassNameIdResolverTest outer;
    }

    private enum SubClassNameIdResolverTestEnum {
        VALUE1, VALUE2;
    }

    private static class InvalidClassNameIdResolverTestEnum {
        private static Object VALUE1 = new Object();
        private static Object VALUE2 = new Object();
    }
}
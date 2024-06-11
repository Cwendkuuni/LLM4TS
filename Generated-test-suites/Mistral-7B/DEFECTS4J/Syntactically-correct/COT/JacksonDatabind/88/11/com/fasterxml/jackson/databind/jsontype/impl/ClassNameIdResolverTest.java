package com.fasterxml.jackson.databind.jsontype.impl;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.*;

import org.junit.Test;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.core.DatabindContext;
import com.fasterxml.jackson.core.type.TypeFactory;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.jsontype.TypeIdResolver;
import com.fasterxml.jackson.databind.util.ClassUtil;

/**
 * Test class for ClassNameIdResolver
 */
public class ClassNameIdResolverTest {

    private JavaType baseType;
    private TypeFactory typeFactory;

    @SuppressWarnings("unchecked")
    @Before
    public void setup() throws Exception {
        baseType = Object.class.getType();
        ObjectMapper mapper = new ObjectMapper();
        typeFactory = mapper.getTypeFactory();
    }

    // Test for constructor
    @Test
    public void testClassNameIdResolverConstructor() {
        ClassNameIdResolver resolver = new ClassNameIdResolver(baseType, typeFactory);
        assertNotNull(resolver);
    }

    // Test for getMechanism method
    @Test
    public void testGetMechanism() {
        ClassNameIdResolver resolver = new ClassNameIdResolver(baseType, typeFactory);
        assertEquals(JsonTypeInfo.Id.CLASS, resolver.getMechanism());
    }

    // Test for registerSubtype method (not used with class name - based resolvers)
    @Test(expected = UnsupportedOperationException.class)
    public void testRegisterSubtype() {
        ClassNameIdResolver resolver = new ClassNameIdResolver(baseType, typeFactory);
        resolver.registerSubtype(String.class, "StringType");
    }

    // Test for idFromValue method
    @Test
    public void testIdFromValue() throws IOException {
        Object obj = new Object();
        String id = new ClassNameIdResolver(baseType, typeFactory).idFromValue(obj);
        assertEquals("java.lang.Object", id);
    }

    // Test for idFromValueAndType method
    @Test
    public void testIdFromValueAndType() throws IOException {
        Object obj = new Object();
        String id = new ClassNameIdResolver(baseType, typeFactory).idFromValueAndType(obj, String.class);
        assertEquals("java.lang.String", id);
    }

    // Test for typeFromId method
    @Test
    public void testTypeFromId() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        DatabindContext ctxt = mapper.getPolymorphicSerializer().getCurrentContext();
        String id = "java.lang.String";
        JavaType javaType = new ClassNameIdResolver(baseType, typeFactory).typeFromId(ctxt, id);
        assertEquals(String.class, javaType.getRawClass());
    }

    // Test for _typeFromId method with a known class name
    @Test
    public void test_TypeFromId_KnownClassName() throws IOException {
        String id = "java.lang.String";
        JavaType javaType = new ClassNameIdResolver(baseType, typeFactory)._typeFromId(id, null);
        assertEquals(String.class, javaType.getRawClass());
    }

    // Test for _typeFromId method with an unknown class name
    @Test(expected = IOException.class)
    public void test_TypeFromId_UnknownClassName() throws IOException {
        String id = "com.unknown.UnknownClass";
        new ClassNameIdResolver(baseType, typeFactory)._typeFromId(id, null);
    }

    // Test for _typeFromId method with a generic class name
    @Test
    public void test_TypeFromId_GenericClassName() throws IOException {
        String id = "java.util.ArrayList<java.lang.Integer>";
        JavaType javaType = new ClassNameIdResolver(baseType, typeFactory)._typeFromId(id, null);
        assertEquals(java.util.ArrayList.class, javaType.getRawClass());
    }

    // Test for _idFrom method with a regular class
    @Test
    public void test_IdFrom_RegularClass() throws Exception {
        Object obj = new Object();
        String id = new ClassNameIdResolver(baseType, typeFactory)
                ._idFrom(obj, obj.getClass(), typeFactory);
        assertEquals("java.lang.Object", id);
    }

    // Test for _idFrom method with an enum class
    @Test
    public void test_IdFrom_EnumClass() throws Exception {
        Enum e = Enum.valueOf(MyEnum.class, "VALUE1");
        String id = new ClassNameIdResolver(baseType, typeFactory)
                ._idFrom(e, e.getClass(), typeFactory);
        assertEquals(MyEnum.class.getName(), id);
    }

    // Test for _idFrom method with an inner class
    @Test
    public void test_IdFrom_InnerClass() throws Exception {
        class Outer {
            static class Inner {
            }
        }
        Object obj = new Outer().new Inner();
        String id = new ClassNameIdResolver(baseType, typeFactory)
                ._idFrom(obj, obj.getClass(), typeFactory);
        assertEquals("com.fasterxml.jackson.databind.jsontype.impl.Outer$Inner", id);
    }

    // Test for _idFrom method with a hidden inner class
    @Test(expected = IOException.class)
    public void test_IdFrom_HiddenInnerClass() throws Exception {
        class Outer {
            private static class Inner {
            }
        }
        Object obj = new Outer();
        String id = new ClassNameIdResolver(baseType, typeFactory)
                ._idFrom(obj, obj.getClass(), typeFactory);
        fail("Expected IOException: Invalid type id 'com.fasterxml.jackson.databind.jsontype.impl.Outer': " +
                "class Outer does not declare any public inner or anonymous classes");
    }

    // Test for getDescForKnownTypeIds method
    @Test
    public void testGetDescForKnownTypeIds() {
        ClassNameIdResolver resolver = new ClassNameIdResolver(baseType, typeFactory);
        assertEquals("class name used as type id", resolver.getDescForKnownTypeIds());
    }
}
package com.fasterxml.jackson.databind.type;

import static org.junit.Assert.*;

import java.util.Arrays;

import org.junit.Test;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.TypeBindings;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.fasterxml.jackson.databind.type.TypeResolverBuilder;
import com.fasterxml.jackson.databind.type.SimpleType;

public class SimpleTypeTest {

    @Test
    public void testSimpleTypeConstruction() {
        // Test default constructor
        SimpleType simpleType1 = new SimpleType(String.class);
        assertEquals("java.lang.String", simpleType1.toString());

        // Test copy constructor
        SimpleType simpleType2 = new SimpleType(simpleType1);
        assertEquals("java.lang.String", simpleType2.toString());
        assertSame(simpleType1, simpleType2);

        // Test withValueHandler and withTypeHandler
        Object valueHandler = new Object();
        Object typeHandler = new Object();
        SimpleType simpleType3 = new SimpleType(String.class, valueHandler, typeHandler);
        assertEquals("java.lang.String", simpleType3.toString());
        assertEquals(valueHandler, simpleType3.getValueHandler());
        assertEquals(typeHandler, simpleType3.getTypeHandler());

        // Test withStaticTyping
        SimpleType simpleType4 = simpleType3.withStaticTyping();
        assertFalse(simpleType4.isDynamic());

        // Test withContentType and withContentTypeHandler (should fail)
        try {
            simpleType3.withContentType(JavaType.constructType(int[].class));
            fail();
        } catch (IllegalArgumentException e) {
            // expected
        }

        try {
            simpleType3.withContentTypeHandler(new Object());
            fail();
        } catch (IllegalArgumentException e) {
            // expected
        }

        // Test refine method (should return null)
        SimpleType simpleType5 = new SimpleType(Number.class);
        assertNull(simpleType5.refine(Double.class, TypeBindings.emptyBindings(), null, null));

        // Test constructUnsafe method (should return an instance of SimpleType)
        SimpleType simpleType6 = SimpleType.constructUnsafe(String[].class);
        assertEquals("class [Ljava.lang.String;", simpleType6.toString());

        // Test construct method (should throw deprecated exception)
        try {
            SimpleType simpleType7 = SimpleType.construct(String.class);
            fail();
        } catch (DeprecatedException e) {
            // expected
        }
    }

    @Test
    public void testSimpleTypeMethods() {
        // Test isContainerType method
        SimpleType simpleType1 = new SimpleType(String.class);
        assertFalse(simpleType1.isContainerType());

        // Test buildCanonicalName method
        SimpleType simpleType2 = new SimpleType(String[].class);
        assertEquals("[Ljava.lang.String;", simpleType2.buildCanonicalName());

        // Test getErasedSignature and getGenericSignature methods
        StringBuilder sb = new StringBuilder();
        SimpleType simpleType3 = new SimpleType(List.class);
        simpleType3.getErasedSignature(sb);
        assertEquals("java.util.List", sb.toString());
        simpleType3.getGenericSignature(sb);
        assertEquals("java.util.List<E>", sb.toString());

        // Test equals method
        SimpleType simpleType4 = new SimpleType(String.class);
        SimpleType simpleType5 = new SimpleType(String.class);
        assertTrue(simpleType4.equals(simpleType5));

        // Test _narrow method (should throw an IllegalArgumentException)
        try {
            SimpleType simpleType6 = new SimpleType(Number.class);
            SimpleType simpleType7 = new SimpleType(Integer.class);
            simpleType6._narrow(Integer.class);
            fail();
        } catch (IllegalArgumentException e) {
            // expected
        }

        // Test toString method
        SimpleType simpleType8 = new SimpleType(Map.class);
        assertEquals("[map type]", simpleType8.toString());
    }

    @Test
    public void testTypeFactoryMethods() {
        // Test TypeFactory.constructType method with a simple type
        TypeFactory typeFactory = new TypeFactory();
        JavaType stringJavaType = typeFactory.constructType(String.class);
        SimpleType stringSimpleType = (SimpleType) stringJavaType;
        assertEquals("java.lang.String", stringSimpleType.toString());

        // Test TypeFactory.constructType method with a generic type
        JavaType listJavaType = typeFactory.constructType(new TypeReference<List<String>>() {});
        SimpleType listSimpleType = (SimpleType) listJavaType.getContentType();
        assertEquals("[Ljava.lang.String;", listSimpleType.toString());

        // Test TypeFactory.constructType method with a mapped type
        JavaType mapJavaType = typeFactory.constructType(Map.class, String.class, String.class);
        SimpleType mapSimpleType = (SimpleType) mapJavaType;
        assertEquals("[map type, class java.util.Map, class java.lang.String, class java.lang.String]", mapSimpleType.toString());

        // Test TypeFactory.constructType method with a generic mapped type
        JavaType mapListJavaType = typeFactory.constructType(new TypeReference<Map<String, List<String>>>() {});
        SimpleType mapListSimpleType = (SimpleType) mapListJavaType.getContentType();
        assertEquals("[map type, class java.util.Map, class java.lang.String, class java.util.List, class java.lang.String]", mapListSimpleType.toString());

        // Test TypeFactory.constructType method with a parameterized type
        JavaType listListJavaType = typeFactory.constructType(new TypeReference<List<List<String>>>() {});
        SimpleType listListSimpleType = (SimpleType) listListJavaType.getContentType();
        assertEquals("[list type, class java.util.List, class java.util.List, class java.lang.String]", listListSimpleType.toString());
    }

    @Test
    public void testTypeResolverBuilderMethods() {
        // Test TypeResolverBuilder.constructType method with a simple type
        TypeResolverBuilder typeResolverBuilder = new TypeResolverBuilder();
        JavaType stringJavaType = typeResolverBuilder.constructType(String.class);
        SimpleType stringSimpleType = (SimpleType) stringJavaType;
        assertEquals("java.lang.String", stringSimpleType.toString());

        // Test TypeResolverBuilder.constructType method with a generic type
        JavaType listJavaType = typeResolverBuilder.constructType(new TypeReference<List<String>>() {});
        SimpleType listSimpleType = (SimpleType) listJavaType.getContentType();
        assertEquals("[Ljava.lang.String;", listSimpleType.toString());

        // Test TypeResolverBuilder.constructType method with a mapped type
        JavaType mapJavaType = typeResolverBuilder.constructType(Map.class, String.class, String.class);
        SimpleType mapSimpleType = (SimpleType) mapJavaType;
        assertEquals("[map type, class java.util.Map, class java.lang.String, class java.lang.String]", mapSimpleType.toString());

        // Test TypeResolverBuilder.constructType method with a generic mapped type
        JavaType mapListJavaType = typeResolverBuilder.constructType(new TypeReference<Map<String, List<String>>>() {});
        SimpleType mapListSimpleType = (SimpleType) mapListJavaType.getContentType();
        assertEquals("[map type, class java.util.Map, class java.lang.String, class java.util.List, class java.lang.String]", mapListSimpleType.toString());

        // Test TypeResolverBuilder.constructType method with a parameterized type
        JavaType listListJavaType = typeResolverBuilder.constructType(new TypeReference<List<List<String>>>() {});
        SimpleType listListSimpleType = (SimpleType) listListJavaType.getContentType();
        assertEquals("[list type, class java.util.List, class java.util.List, class java.lang.String]", listListSimpleType.toString());
    }

    // Test edge cases
    @Test
    public void testEdgeCases() {
        // Test construct method with an empty class name
        try {
            SimpleType emptySimpleType = SimpleType.construct("", TypeBindings.emptyBindings(), null, null, null, null, false);
            fail();
        } catch (IllegalArgumentException e) {
            // expected
        }

        // Test construct method with a null class name
        try {
            SimpleType nullSimpleType = SimpleType.construct(null, TypeBindings.emptyBindings(), null, null, null, null, false);
            fail();
        } catch (IllegalArgumentException e) {
            // expected
        }

        // Test construct method with a null bindings parameter
        try {
            SimpleType nullBindingsSimpleType = SimpleType.construct(String.class, null, null, null, null, null, false);
            fail();
        } catch (NullPointerException e) {
            // expected
        }

        // Test construct method with a null superClass parameter
        try {
            SimpleType nullSuperClassSimpleType = SimpleType.construct(String.class, TypeBindings.emptyBindings(), null, null, null, null, false);
            // No failure expected, as null should be treated as Object.class
            assertNotNull(nullSuperClassSimpleType);
            assertEquals("java.lang.String", nullSuperClassSimpleType.toString());
        } catch (NullPointerException e) {
            // not expected
            fail();
        }

        // Test construct method with a null superInterfaces parameter
        try {
            SimpleType nullSuperInterfacesSimpleType = SimpleType.construct(String.class, TypeBindings.emptyBindings(), null, null, null, null, false);
            assertNotNull(nullSuperInterfacesSimpleType);
            assertEquals("java.lang.String", nullSuperInterfacesSimpleType.toString());
        } catch (NullPointerException e) {
            // not expected
            fail();
        }

        // Test _narrow method with a null subclass parameter
        try {
            SimpleType simpleType = new SimpleType(String.class);
            simpleType._narrow(null);
            fail();
        } catch (NullPointerException e) {
            // expected
        }

        // Test _narrow method with a null bindings parameter
        try {
            SimpleType simpleType = new SimpleType(String.class, null);
            simpleType._narrow(String.class);
            fail();
        } catch (NullPointerException e) {
            // expected
        }

        // Test _narrow method with a subclass that is not a subtype of the superclass
        SimpleType simpleType1 = new SimpleType(Number.class);
        SimpleType simpleType2 = new SimpleType(Integer.class);
        try {
            simpleType1._narrow(simpleType2);
            fail();
        } catch (IllegalArgumentException e) {
            // expected
        }

        // Test _narrow method with a subclass that is a subtype of the superclass with bindings
        SimpleType simpleType3 = new SimpleType(Number.class, TypeBindings.emptyBindings());
        SimpleType simpleType4 = new SimpleType(Integer.class, TypeBindings.emptyBindings());
        SimpleType simpleType5 = simpleType3._narrow(simpleType4);
        assertEquals(simpleType4, simpleType5);

        // Test _narrow method with a subclass that is a subtype of the superclass without bindings
        SimpleType simpleType6 = new SimpleType(Number.class);
        SimpleType simpleType7 = new SimpleType(Integer.class);
        SimpleType simpleType8 = simpleType6._narrow(simpleType7);
        assertTrue(simpleType6.isAssignableFrom(simpleType8));

        // Test _narrow method with a subclass that is a subtype of the superclass with different bindings
        SimpleType simpleType9 = new SimpleType(Number.class, TypeBindings.emptyBindings());
        SimpleType simpleType10 = new SimpleType(Integer.class, TypeBindings.emptyBindings());
        SimpleType simpleType11 = new SimpleType(Integer.class, Arrays.asList(simpleType10, simpleType9));
        SimpleType simpleType12 = simpleType9._narrow(simpleType11);
        assertTrue(simpleType11.isAssignableFrom(simpleType12));

        // Test _narrow method with a subclass that is a subtype of the superclass with an empty bindings list
        SimpleType simpleType13 = new SimpleType(Number.class, TypeBindings.emptyBindings());
        SimpleType simpleType14 = new SimpleType(Integer.class, TypeBindings.emptyBindings());
        SimpleType simpleType15 = new SimpleType(Integer.class, null);
        SimpleType simpleType16 = simpleType13._narrow(simpleType15);
        assertTrue(simpleType15.isAssignableFrom(simpleType16));

        // Test _narrow method with a subclass that is a subtype of the superclass but with an empty bindings list and different superclass
        SimpleType simpleType17 = new SimpleType(Number.class, TypeBindings.emptyBindings());
        SimpleType simpleType18 = new SimpleType(Float.class, TypeBindings.emptyBindings());
        SimpleType simpleType19 = new SimpleType(Float.class, null);
        SimpleType simpleType20 = simpleType17._narrow(simpleType19);
        assertTrue(simpleType19.isAssignableFrom(simpleType20));

        // Test _narrow method with a subclass that is not a subtype of the superclass
        SimpleType simpleType21 = new SimpleType(Number.class);
        SimpleType simpleType22 = new SimpleType(Boolean.class);
        try {
            simpleType21._narrow(simpleType22);
            fail();
        } catch (IllegalArgumentException e) {
            // expected
        }
    }
}
package com.fasterxml.jackson.databind.test;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.TypeFactory;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class JavaTypeTest {

    @Test
    public void testJavaTypeInstance() throws Exception {
        TypeFactory tf = new TypeFactory();
        JavaType javaType = tf.constructType(String[].class);

        // Basic checks
        assertNotNull(javaType);
        assertEquals("java.lang.Class<[Ljava.lang.String;>", javaType.getErasedSignature());
        assertEquals("java.lang.Class<java.lang.String[]>", javaType.getGenericSignature());
        assertEquals(String[].class, javaType.getRawClass());
        assertEquals(false, javaType.isAbstract());
        assertEquals(false, javaType.isArrayType());
        assertEquals(false, javaType.isEnumType());
        assertEquals(false, javaType.isInterface());
        assertEquals(false, javaType.isPrimitive());
        assertEquals(false, javaType.isFinal());
        assertEquals(false, javaType.isContainerType());
        assertEquals(false, javaType.isCollectionLikeType());
        assertEquals(false, javaType.isMapLikeType());
        assertEquals(false, javaType.useStaticType());
        assertEquals(0, javaType.containedTypeCount());

        // Test various handlers
        javaType = javaType.withValueHandler(new Object());
        javaType = javaType.withTypeHandler(new Object());
        javaType = javaType.withContentTypeHandler(new Object());
        javaType = javaType.withContentValueHandler(new Object());
        javaType = javaType.withStaticTyping();

        // Test narrowing and widening
        JavaType stringType = tf.constructType(String.class);
        JavaType subArrayType = javaType.narrowBy(String[].class);
        JavaType superArrayType = javaType.widenBy(Object[].class);
        JavaType stringArrayWithStaticTyping = javaType.withStaticTyping();

        assertEquals(String[].class, subArrayType.getRawClass());
        assertEquals("java.lang.Class<[Ljava.lang.String;>", subArrayType.getErasedSignature());
        assertEquals("java.lang.Class<java.lang.String[]>", subArrayType.getGenericSignature());
        assertEquals(String[].class, superArrayType.getRawClass());
        assertEquals("java.lang.Class<[Ljava.lang.Object;>", superArrayType.getErasedSignature());
        assertEquals("java.lang.Class<java.lang.Object[]>", superArrayType.getGenericSignature());
        assertEquals(String[].class, stringArrayWithStaticTyping.getRawClass());
        assertEquals("java.lang.Class<java.lang.String[]>", stringArrayWithStaticTyping.getGenericSignature());
        assertEquals(true, stringArrayWithStaticTyping.useStaticType());

        // Test narrowing and widening with error cases
        try {
            JavaType invalidSubArrayType = javaType.narrowBy(Integer.class);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }

        try {
            JavaType invalidSuperArrayType = javaType.widenBy(Double.class);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }

        // Test type parameter access
        TypeReference<List<String>> listTypeRef = new TypeReference<List<String>>() {};
        JavaType listType = tf.constructType(listTypeRef);
        assertEquals(List.class, listType.getRawClass());
        assertEquals("java.util.List<java.lang.String>", listType.getGenericSignature());
        assertEquals(1, listType.containedTypeCount());
        assertEquals("java.util.List", listType.containedType(0).getErasedSignature());
        assertEquals("java.util.List<java.lang.String>", listType.containedType(0).getGenericSignature());

        // Test containedTypeOrUnknown
        JavaType unknownType = tf.constructType(Object.class);
        JavaType listWithUnknownType = tf.constructType(new TypeReference<List<?>>() {}.getType());
        assertEquals(unknownType, listWithUnknownType.containedTypeOrUnknown(0));
    }
}
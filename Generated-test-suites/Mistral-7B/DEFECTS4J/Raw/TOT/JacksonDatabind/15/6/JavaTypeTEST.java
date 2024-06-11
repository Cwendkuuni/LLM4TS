package com.fasterxml.jackson.databind.test;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.fasterxml.jackson.databind.type.TypeType;
import com.fasterxml.jackson.databind.type.Types;
import com.fasterxml.jackson.databind.util.ClassUtil;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

public class JavaTypeTest {

    private static final ObjectMapper mapper = new ObjectMapper();

    @Test
    public void testSimpleType() throws Exception {
        Class<?> raw = Integer.TYPE;
        JavaType javaType = TypeFactory.defaultInstance().constructType(raw);
        assertEquals("java.lang.Integer", javaType.getErasedSignature());
        assertEquals(raw, javaType.getRawClass());
        assertEquals(Integer.class, javaType.getParameterSource());
        assertTrue(javaType.isConcrete());
        assertFalse(javaType.isAbstract());
        assertFalse(javaType.isInterface());
        assertFalse(javaType.isPrimitive());
        assertFalse(javaType.isEnumType());
        assertFalse(javaType.isThrowable());
        assertFalse(javaType.isArrayType());
        assertFalse(javaType.isContainerType());
        assertFalse(javaType.isCollectionLikeType());
        assertFalse(javaType.isMapLikeType());
        assertFalse(javaType.useStaticType());

        JavaType intJavaType = javaType.narrowBy(raw);
        assertEquals(intJavaType, javaType);

        JavaType intJavaTypeWithHandler = intJavaType.withValueHandler(mapper.getValueSerializerFactory().findValueSerializer(Integer.TYPE));
        assertFalse(intJavaType.isValueHandlerAssigned());
        assertSame(mapper.getValueSerializerFactory().findValueSerializer(Integer.TYPE), intJavaTypeWithHandler.getValueHandler());
    }

    @Test
    public void testNarrowing() throws Exception {
        Class<?> raw = String[].class;
        JavaType javaType = TypeFactory.defaultInstance().constructType(raw);
        assertEquals("java.lang.Object[]", javaType.getErasedSignature());
        assertEquals(raw, javaType.getRawClass());
        assertEquals(Object[].class, javaType.getParameterSource());
        assertTrue(javaType.isConcrete());
        assertFalse(javaType.isAbstract());
        assertFalse(javaType.isInterface());
        assertFalse(javaType.isPrimitive());
        assertFalse(javaType.isEnumType());
        assertFalse(javaType.isThrowable());
        assertFalse(javaType.isArrayType());
        assertFalse(javaType.isContainerType());
        assertFalse(javaType.isCollectionLikeType());
        assertFalse(javaType.isMapLikeType());
        assertFalse(javaType.useStaticType());

        JavaType narrowedJavaType = javaType.narrowBy(Integer[].class);
        assertTrue(narrowedJavaType.isContainerType());
        assertEquals("java.lang.Integer[]", narrowedJavaType.getErasedSignature());
        assertEquals(Integer[].class, narrowedJavaType.getRawClass());
        assertEquals(Integer[].class, narrowedJavaType.getParameterSource());
        assertFalse(narrowedJavaType.isValueHandlerAssigned());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNarrowingInvalid() throws Exception {
        Class<?> raw = String.class;
        JavaType javaType = TypeFactory.defaultInstance().constructType(raw);
        javaType.narrowBy(String[].class);
    }

    @Test
    public void testWidening() throws Exception {
        Class<?> raw = Integer[].class;
        JavaType javaType = TypeFactory.defaultInstance().constructType(raw);
        assertEquals("java.lang.Object[]", javaType.getErasedSignature());
        assertEquals(raw, javaType.getRawClass());
        assertEquals(Object[].class, javaType.getParameterSource());
        assertTrue(javaType.isConcrete());
        assertFalse(javaType.isAbstract());
        assertFalse(javaType.isInterface());
        assertFalse(javaType.isPrimitive());
        assertFalse(javaType.isEnumType());
        assertFalse(javaType.isThrowable());
        assertFalse(javaType.isArrayType());
        assertFalse(javaType.isContainerType());
        assertFalse(javaType.isCollectionLikeType());
        assertFalse(javaType.isMapLikeType());
        assertFalse(javaType.useStaticType());

        JavaType widenedJavaType = javaType.widenBy(Object[].class);
        assertEquals(Object[].class, widenedJavaType.getRawClass());
        assertEquals(Object[].class, widenedJavaType.getParameterSource());
        assertTrue(widenedJavaType.isConcrete());
        assertFalse(widenedJavaType.isAbstract());
        assertFalse(widenedJavaType.isInterface());
        assertFalse(widenedJavaType.isPrimitive());
        assertFalse(widenedJavaType.isEnumType());
        assertFalse(widenedJavaType.isThrowable());
        assertFalse(widenedJavaType.isArrayType());
        assertFalse(widenedJavaType.isContainerType());
        assertFalse(widenedJavaType.isCollectionLikeType());
        assertFalse(widenedJavaType.isMapLikeType());
        assertFalse(widenedJavaType.useStaticType());
    }

    @Test
    public void testContainedTypes() throws Exception {
        JavaType javaType1 = TypeFactory.defaultInstance().constructType(List.class);
        JavaType javaType2 = javaType1.narrowContentsBy(ArrayList.class);
        JavaType javaType3 = javaType2.narrowContentsBy(Integer.TYPE);
        assertTrue(javaType3.isContainerType());
        assertEquals("java.util.ArrayList<java.lang.Integer>", javaType3.getGenericSignature());
        assertEquals(Integer.TYPE, javaType3.getRawClass());
        assertEquals(List.class, javaType3.getParameterSource());
        assertFalse(javaType3.isValueHandlerAssigned());
    }

    @Test
    public void testStaticTyping() throws Exception {
        JavaType javaType = TypeFactory.defaultInstance()
                .constructType(Map.class, String.class, Integer.class);
        JavaType javaTypeWithStaticTyping = javaType.withStaticTyping();
        assertTrue(javaTypeWithStaticTyping.useStaticType());
    }

    @Test(expected = JsonMappingException.class)
    public void testStaticTypingViolation() throws Exception {
        JavaType javaType = TypeFactory.defaultInstance()
                .constructType(Map.class, String.class, String.class);
        javaType.withStaticTyping();
    }

    @Test
    public void testTypeWithGenericTypes() throws Exception {
        JavaType javaType = TypeFactory.defaultInstance()
                .constructType(Types.collectionType(Map.class, String.class, Integer.class));
        assertEquals("java.util.Map<java.lang.String, java.lang.Integer>", javaType.getGenericSignature());
        assertEquals(Map.class, javaType.getParameterSource());
        assertTrue(javaType.isContainerType());
        assertTrue(javaType.hasGenericTypes());
        assertTrue(javaType.isMapLikeType());
        assertEquals(String.class, javaType.containedType(0));
        assertEquals(Integer.class, javaType.containedType(1));
        assertNull(javaType.getValueHandler());
        assertNull(javaType.getTypeHandler());
    }

    @Test
    public void testTypeWithGenericTypesAndValueHandler() throws Exception {
        JavaType javaType = TypeFactory.defaultInstance()
                .constructType(Types.constructParametrizedType(List.class, Object.class, JsonTypeInfo.Id.CLASS, String.class))
                .withValueHandler(mapper.getValueSerializerFactory().findValueSerializer(String.class));
        assertEquals("java.util.List<java.lang.String>", javaType.getGenericSignature());
        assertEquals(List.class, javaType.getParameterSource());
        assertTrue(javaType.isContainerType());
        assertTrue(javaType.hasGenericTypes());
        assertFalse(javaType.isMapLikeType());
        assertEquals(String.class, javaType.getValueHandler());
        assertNull(javaType.getTypeHandler());
    }
}
package com.example.junit;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.lang.reflect.Modifier;
import java.util.Arrays;

import org.junit.Test;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.TypeFactory;

public class JavaTypeTest {

    @Test
    public void testBaseClass() {
        JavaType javaType = TypeFactory.defaultInstance().constructType(String.class);
        assertEquals(String.class, javaType.getRawClass());
        assertEquals("java.lang.String", javaType.toString());
        assertFalse(javaType.isAbstract());
        assertFalse(javaType.isInterface());
        assertFalse(javaType.isPrimitive());
        assertFalse(javaType.isFinal());
        assertFalse(javaType.isArrayType());
        assertFalse(javaType.isEnumType());
        assertFalse(javaType.isMapLikeType());
        assertFalse(javaType.isCollectionLikeType());
        assertFalse(javaType.isContainerType());
        assertFalse(javaType.useStaticType());
        assertFalse(javaType.hasGenericTypes());
        assertNull(javaType.getKeyType());
        assertNull(javaType.getContentType());
        assertEquals(0, javaType.containedTypeCount());
        assertNull(javaType.containedType(0));
        assertNull(javaType.containedTypeName(0));
        assertNull(javaType.getParameterSource());

        JavaType javaTypeArray = TypeFactory.defaultInstance().constructType(int[].class);
        assertEquals(int[].class, javaTypeArray.getRawClass());
        assertEquals("int[]", javaTypeArray.toString());
        assertFalse(javaTypeArray.isAbstract());
        assertFalse(javaTypeArray.isInterface());
        assertFalse(javaTypeArray.isPrimitive());
        assertFalse(javaTypeArray.isFinal());
        assertTrue(javaTypeArray.isArrayType());
        assertFalse(javaTypeArray.isEnumType());
        assertFalse(javaTypeArray.isMapLikeType());
        assertFalse(javaTypeArray.isCollectionLikeType());
        assertTrue(javaTypeArray.isContainerType());
        assertFalse(javaTypeArray.useStaticType());
        assertFalse(javaTypeArray.hasGenericTypes());
        assertNull(javaTypeArray.getKeyType());
        assertNull(javaTypeArray.getContentType());
        assertEquals(0, javaTypeArray.containedTypeCount());
        assertNull(javaTypeArray.containedType(0));
        assertNull(javaTypeArray.containedTypeName(0));
        assertNull(javaTypeArray.getParameterSource());

        JavaType javaTypeContainer = TypeFactory.defaultInstance().constructType(java.util.ArrayList.class);
        assertEquals(java.util.ArrayList.class, javaTypeContainer.getRawClass());
        assertEquals("java.util.ArrayList", javaTypeContainer.toString());
        assertFalse(javaTypeContainer.isAbstract());
        assertFalse(javaTypeContainer.isInterface());
        assertFalse(javaTypeContainer.isPrimitive());
        assertFalse(javaTypeContainer.isFinal());
        assertFalse(javaTypeContainer.isArrayType());
        assertFalse(javaTypeContainer.isEnumType());
        assertTrue(javaTypeContainer.isMapLikeType());
        assertFalse(javaTypeContainer.isCollectionLikeType());
        assertTrue(javaTypeContainer.isContainerType());
        assertFalse(javaTypeContainer.useStaticType());
        assertTrue(javaTypeContainer.hasGenericTypes());
        assertEquals(2, javaTypeContainer.containedTypeCount());
        assertEquals(Object.class, javaTypeContainer.containedType(0).getRawClass());
        assertEquals("Ljava/lang/Object;", javaTypeContainer.containedType(0).toString());
        assertEquals("java.lang.Class<Ljava/lang/Object;>", javaTypeContainer.containedType(0).getGenericSignature());
        assertEquals("Ljava/lang/Class;", javaTypeContainer.containedType(0).getErasedSignature());
        assertEquals("Ljava/lang/Class<java.util.ArrayList<Ljava/lang/Object;>>", javaTypeContainer.getGenericSignature());
        assertEquals("Ljava/util/List;", javaTypeContainer.getErasedSignature());
        assertEquals(ElementType.class, javaTypeContainer.containedType(1).getRawClass());
        assertEquals("Ljava/lang/Class<Ljava/lang/Object;>;Ljava/lang/Class<Ljava/lang/Object;>;", javaTypeContainer.containedType(1).toString());
        assertEquals("Ljava/lang/Class<Ljava/lang/Object;>;Ljava/lang/Class<Ljava/lang/Object;>;", javaTypeContainer.containedType(1).getGenericSignature());
        assertEquals("Ljava/lang/Class;Ljava/lang/Class;", javaTypeContainer.containedType(1).getErasedSignature());
        assertNull(javaTypeContainer.getKeyType());
        assertNull(javaTypeContainer.getContentType());
        assertNull(javaTypeContainer.getParameterSource());
    }

    @Test
    public void testNarrowing() {
        JavaType javaTypeString = TypeFactory.defaultInstance().constructType(String.class);
        JavaType javaTypeStringSubclass = TypeFactory.defaultInstance().constructType(StringBuilder.class);
        JavaType javaTypeNarrowed = javaTypeString.narrowBy(StringBuilder.class);
        assertEquals(javaTypeStringSubclass, javaTypeNarrowed);
        JavaType javaTypeStringFailed = javaTypeString.narrowBy(Integer.class);
        fail("Expected IllegalArgumentException");
    }

    @Test
    public void testWidening() {
        JavaType javaTypeString = TypeFactory.defaultInstance().constructType(String.class);
        JavaType javaTypeStringSuperclass = TypeFactory.defaultInstance().constructType(Object.class);
        JavaType javaTypeWidened = javaTypeString.widenBy(Object.class);
        assertEquals(javaTypeStringSuperclass, javaTypeWidened);
        JavaType javaTypeStringFailed = javaTypeString.widenBy(StringBuilder.class);
        fail("Expected IllegalArgumentException");
    }

    @Test
    public void testWidenContents() {
        JavaType javaTypeString = TypeFactory.defaultInstance().constructType(String.class);
        JavaType javaTypeStringList = TypeFactory.defaultInstance().constructType(java.util.List.class);
        JavaType javaTypeStringListString = TypeFactory.defaultInstance().constructType(java.util.List.class).narrowContentsBy(String.class);
        JavaType javaTypeWidened = javaTypeStringList.widenContentsBy(String.class);
        assertEquals(javaTypeStringListString, javaTypeWidened);
        JavaType javaTypeStringListFailed = javaTypeStringList.widenContentsBy(Integer.class);
        fail("Expected IllegalArgumentException");
    }

    @Test
    public void testNarrowContents() {
        JavaType javaTypeStringList = TypeFactory.defaultInstance().constructType(java.util.List.class);
        JavaType javaTypeStringListString = TypeFactory.defaultInstance().constructType(java.util.List.class).narrowContentsBy(String.class);
        JavaType javaTypeNarrowed = javaTypeStringList.narrowContentsBy(String.class);
        assertEquals(javaTypeStringListString, javaTypeNarrowed);
        JavaType javaTypeStringListFailed = javaTypeStringList.narrowContentsBy(Integer.class);
        fail("Expected IllegalArgumentException");
    }

    @Test
    public void testValueHandler() {
        JavaType javaTypeString = TypeFactory.defaultInstance().constructType(String.class);
        StringHandler stringHandler = new StringHandler();
        JavaType javaTypeWithValueHandler = javaTypeString.withValueHandler(stringHandler);
        assertEquals(javaTypeString, javaTypeWithValueHandler);
        assertEquals(stringHandler, javaTypeWithValueHandler.getValueHandler());
        assertNull(javaTypeString.getValueHandler());
    }

    @Test
    public void testTypeHandler() {
        JavaType javaTypeString = TypeFactory.defaultInstance().constructType(String.class);
        TypeHandler stringHandler = new TypeHandler();
        JavaType javaTypeWithTypeHandler = javaTypeString.withTypeHandler(stringHandler);
        assertEquals(javaTypeString, javaTypeWithTypeHandler);
        assertEquals(stringHandler, javaTypeWithTypeHandler.getTypeHandler());
        assertNull(javaTypeString.getTypeHandler());
    }

    @Test
    public void testTypeHandlerWithValueHandler() {
        JavaType javaTypeString = TypeFactory.defaultInstance().constructType(String.class);
        TypeHandler stringHandler = new TypeHandler();
        ValueHandler valueHandler = new ValueHandler();
        JavaType javaTypeWithTypeHandlerAndValueHandler = javaTypeString.withTypeHandler(stringHandler).withValueHandler(valueHandler);
        assertEquals(javaTypeString, javaTypeWithTypeHandlerAndValueHandler);
        assertEquals(stringHandler, javaTypeWithTypeHandlerAndValueHandler.getTypeHandler());
        assertEquals(valueHandler, javaTypeWithTypeHandlerAndValueHandler.getValueHandler());
    }

    @Test
    public void testStaticTyping() {
        JavaType javaTypeString = TypeFactory.defaultInstance().constructType(String.class);
        JavaType javaTypeWithStaticTyping = javaTypeString.withStaticTyping();
        assertEquals(javaTypeString, javaTypeWithStaticTyping);
        assertEquals(false, javaTypeString.useStaticType());
        assertEquals(true, javaTypeWithStaticTyping.useStaticType());
    }

    @Test
    public void testHasRawClass() {
        JavaType javaTypeString = TypeFactory.defaultInstance().constructType(String.class);
        assertTrue(javaTypeString.hasRawClass(String.class));
        assertFalse(javaTypeString.hasRawClass(Integer.class));
    }

    static class StringHandler implements ValueHandler<String> {}

    static class TypeHandler implements TypeHandler<Object> {}

    static class ValueHandler<T> {}

    static class TypeHandler<T> {}
}

class StringBuilder {}
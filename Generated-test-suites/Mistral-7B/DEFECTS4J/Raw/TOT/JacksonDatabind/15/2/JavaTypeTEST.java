package com.fasterxml.jackson.databind.test;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.TypeFactory;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

public class JavaTypeTest {

    @Test
    public void testNewInstance() {
        // Test simple case
        JavaType stringType = TypeFactory.defaultInstance().constructType(String.class);
        assertEquals(String.class, stringType.getRawClass());
        assertEquals(String.class, stringType.getErasedSignature());
        assertFalse(stringType.isContainerType());
        assertFalse(stringType.isCollectionLikeType());
        assertFalse(stringType.isMapLikeType());
        assertFalse(stringType.isEnumType());
        assertFalse(stringType.isInterface());
        assertFalse(stringType.isPrimitive());
        assertFalse(stringType.isFinal());
        assertFalse(stringType.isThrowable());
        assertFalse(stringType.isArrayType());
        assertTrue(stringType.isConcrete());
        assertFalse(stringType.isAbstract());
        assertNull(stringType.getValueHandler());
        assertNull(stringType.getTypeHandler());
        assertSame(TypeFactory.defaultInstance(), stringType.getParameterSource());
        assertFalse(stringType.useStaticType());
        
        // Test withTypeHandler
        JavaType stringTypeWithHandler = stringType.withValueHandler("Handler");
        assertSame(stringType, stringTypeWithHandler.getRawClass());
        assertSame(stringType, stringTypeWithHandler.getErasedSignature());
        assertFalse(stringTypeWithHandler.isContainerType());
        assertFalse(stringTypeWithHandler.isCollectionLikeType());
        assertFalse(stringTypeWithHandler.isMapLikeType());
        assertFalse(stringTypeWithHandler.isEnumType());
        assertFalse(stringTypeWithHandler.isInterface());
        assertFalse(stringTypeWithHandler.isPrimitive());
        assertFalse(stringTypeWithHandler.isFinal());
        assertFalse(stringTypeWithHandler.isThrowable());
        assertFalse(stringTypeWithHandler.isArrayType());
        assertTrue(stringTypeWithHandler.isConcrete());
        assertFalse(stringTypeWithHandler.isAbstract());
        assertEquals("Handler", stringTypeWithHandler.getValueHandler());
        assertNull(stringTypeWithHandler.getTypeHandler());
        assertSame(TypeFactory.defaultInstance(), stringTypeWithHandler.getParameterSource());
        assertFalse(stringTypeWithHandler.useStaticType());
        
        // Test withTypeHandler with different types
        JavaType intType = TypeFactory.defaultInstance().constructType(int[].class);
        JavaType stringHandler = TypeFactory.defaultInstance().constructType(String.class);
        JavaType intArrayWithHandler = intType.withValueHandler(stringHandler);
        assertSame(int[].class, intArrayWithHandler.getRawClass());
        assertSame(int[].class, intArrayWithHandler.getErasedSignature());
        assertFalse(intArrayWithHandler.isContainerType());
        assertFalse(intArrayWithHandler.isCollectionLikeType());
        assertFalse(intArrayWithHandler.isMapLikeType());
        assertFalse(intArrayWithHandler.isEnumType());
        assertFalse(intArrayWithHandler.isInterface());
        assertFalse(intArrayWithHandler.isPrimitive());
        assertFalse(intArrayWithHandler.isFinal());
        assertFalse(intArrayWithHandler.isThrowable());
        assertFalse(intArrayWithHandler.isArrayType());
        assertTrue(intArrayWithHandler.isConcrete());
        assertFalse(intArrayWithHandler.isAbstract());
        assertNull(intArrayWithHandler.getValueHandler());
        assertEquals(stringHandler, intArrayWithHandler.getTypeHandler());
        assertSame(TypeFactory.defaultInstance(), intArrayWithHandler.getParameterSource());
        assertFalse(intArrayWithHandler.useStaticType());
        
        // Test narrowBy
        JavaType intTypeNarrow = intType.narrowBy(int[].class);
        assertSame(int[].class, intTypeNarrow.getRawClass());
        assertSame(int[].class, intTypeNarrow.getErasedSignature());
        assertFalse(intTypeNarrow.isContainerType());
        assertFalse(intTypeNarrow.isCollectionLikeType());
        assertFalse(intTypeNarrow.isMapLikeType());
        assertFalse(intTypeNarrow.isEnumType());
        assertFalse(intTypeNarrow.isInterface());
        assertFalse(intTypeNarrow.isPrimitive());
        assertFalse(intTypeNarrow.isFinal());
        assertFalse(intTypeNarrow.isThrowable());
        assertFalse(intTypeNarrow.isArrayType());
        assertTrue(intTypeNarrow.isConcrete());
        assertFalse(intTypeNarrow.isAbstract());
        assertEquals(int[].class, intTypeNarrow.getRawClass());
        assertEquals(int[].class, intTypeNarrow.getErasedSignature());
        assertFalse(intTypeNarrow.getValueHandler() instanceof Object);
        assertNull(intTypeNarrow.getTypeHandler());
        assertSame(TypeFactory.defaultInstance(), intTypeNarrow.getParameterSource());
        assertFalse(intTypeNarrow.useStaticType());
        
        // Test widenBy
        JavaType numberType = TypeFactory.defaultInstance().constructType(Number.class);
        JavaType intTypeWiden = numberType.widenBy(int[].class);
        assertSame(int[].class, intTypeWiden.getRawClass());
        assertSame(int[].class, intTypeWiden.getErasedSignature());
        assertFalse(intTypeWiden.isContainerType());
        assertFalse(intTypeWiden.isCollectionLikeType());
        assertFalse(intTypeWiden.isMapLikeType());
        assertFalse(intTypeWiden.isEnumType());
        assertFalse(intTypeWiden.isInterface());
        assertFalse(intTypeWiden.isPrimitive());
        assertFalse(intTypeWiden.isFinal());
        assertFalse(intTypeWiden.isThrowable());
        assertFalse(intTypeWiden.isArrayType());
        assertTrue(intTypeWiden.isConcrete());
        assertFalse(intTypeWiden.isAbstract());
        assertNull(intTypeWiden.getValueHandler());
        assertNull(intTypeWiden.getTypeHandler());
        assertSame(TypeFactory.defaultInstance(), intTypeWiden.getParameterSource());
        assertFalse(intTypeWiden.useStaticType());
        
        // Test narrowContentsBy
        JavaType listType = TypeFactory.defaultInstance().constructType(List.class);
        JavaType intListType = listType.narrowContentsBy(int[].class);
        assertEquals(int[].class, intListType.getRawClass());
        assertEquals(int[].class, intListType.getErasedSignature());
        assertFalse(intListType.isContainerType());
        assertFalse(intListType.isCollectionLikeType());
        assertFalse(intListType.isMapLikeType());
        assertFalse(intListType.isEnumType());
        assertFalse(intListType.isInterface());
        assertFalse(intListType.isPrimitive());
        assertFalse(intListType.isFinal());
        assertFalse(intListType.isThrowable());
        assertFalse(intListType.isArrayType());
        assertTrue(intListType.isConcrete());
        assertFalse(intListType.isAbstract());
        assertNull(intListType.getValueHandler());
        assertNull(intListType.getTypeHandler());
        assertSame(TypeFactory.defaultInstance(), intListType.getParameterSource());
        assertFalse(intListType.useStaticType());
        
        // Test widenContentsBy
        JavaType mapType = TypeFactory.defaultInstance().constructType(Map.class);
        JavaType listTypeWiden = mapType.widenContentsBy(List.class);
        assertEquals(List.class, listTypeWiden.getRawClass());
        assertEquals(List.class, listTypeWiden.getErasedSignature());
        assertFalse(listTypeWiden.isContainerType());
        assertFalse(listTypeWiden.isCollectionLikeType());
        assertFalse(listTypeWiden.isMapLikeType());
        assertFalse(listTypeWiden.isEnumType());
        assertFalse(listTypeWiden.isInterface());
        assertFalse(listTypeWiden.isPrimitive());
        assertFalse(listTypeWiden.isFinal());
        assertFalse(listTypeWiden.isThrowable());
        assertFalse(listTypeWiden.isArrayType());
        assertTrue(listTypeWiden.isConcrete());
        assertFalse(listTypeWiden.isAbstract());
        assertNull(listTypeWiden.getValueHandler());
        assertNull(listTypeWiden.getTypeHandler());
        assertSame(TypeFactory.defaultInstance(), listTypeWiden.getParameterSource());
        assertFalse(listTypeWiden.useStaticType());
    }
}
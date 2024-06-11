package com.fasterxml.jackson.databind.test;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.TypeFactory;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class JavaTypeTest {

    @Test
    public void testJavaTypeCreation() throws Exception {
        // Test with simple type
        JavaType simpleType = TypeFactory.defaultInstance().constructType(String.class);
        assertTrue(simpleType instanceof JavaType);
        assertEquals(String.class, simpleType.getRawClass());
        assertFalse(simpleType.hasGenericTypes());
        assertNull(simpleType.getKeyType());
        assertNull(simpleType.getContentType());
        assertEquals(0, simpleType.containedTypeCount());
        assertEquals("Ljava/lang/String;", simpleType.getErasedSignature().toString());

        // Test with generic type
        JavaType listType = TypeFactory.defaultInstance().constructType(List.class);
        assertTrue(listType instanceof JavaType);
        assertEquals(List.class, listType.getRawClass());
        assertTrue(listType.hasGenericTypes());
        assertEquals(Object.class, listType.getKeyType().getRawClass());
        assertNull(listType.getContentType());
        assertEquals(1, listType.containedTypeCount());
        assertEquals("Ljava/util/List;<Ljava/lang/Object;>", listType.getGenericSignature().toString());

        // Test with map type
        JavaType mapType = TypeFactory.defaultInstance().constructType(Map.class);
        assertTrue(mapType instanceof JavaType);
        assertEquals(Map.class, mapType.getRawClass());
        assertTrue(mapType.hasGenericTypes());
        assertEquals(Class.class, mapType.getKeyType().getRawClass());
        assertEquals(Object.class, mapType.getContentType().getRawClass());
        assertEquals(2, mapType.containedTypeCount());
        assertEquals("Ljava/util/Map;<Ljava/lang/Class;Ljava/lang/Object;>", mapType.getGenericSignature().toString());

        // Test with custom class
        Class<?> customClass = new CustomType().getClass();
        JavaType customType = TypeFactory.defaultInstance().constructType(customClass);
        assertTrue(customType instanceof JavaType);
        assertEquals(customClass, customType.getRawClass());
        assertFalse(customType.hasGenericTypes());
        assertNull(customType.getKeyType());
        assertNull(customType.getContentType());
        assertEquals(0, customType.containedTypeCount());
    }

    @Test
    public void testCopyMethods() throws Exception {
        JavaType simpleType = TypeFactory.defaultInstance().constructType(String.class);
        JavaType copyWithTypeHandler = simpleType.withTypeHandler(new Object());
        assertEquals(simpleType.getClass(), copyWithTypeHandler.getClass());
        assertEquals(simpleType.getRawClass(), copyWithTypeHandler.getRawClass());
        assertFalse(simpleType.hasGenericTypes());
        assertFalse(copyWithTypeHandler.hasGenericTypes());
        assertNull(simpleType.getTypeHandler());
        assertNotNull(copyWithTypeHandler.getTypeHandler());
        assertNull(simpleType.getValueHandler());
        assertNull(copyWithTypeHandler.getValueHandler());

        JavaType listType = TypeFactory.defaultInstance().constructType(List.class);
        JavaType copyWithContentTypeHandler = listType.withContentTypeHandler(new Object());
        assertEquals(listType.getClass(), copyWithContentTypeHandler.getClass());
        assertEquals(listType.getRawClass(), copyWithContentTypeHandler.getRawClass());
        assertTrue(listType.hasGenericTypes());
        assertTrue(copyWithContentTypeHandler.hasGenericTypes());
        assertNull(listType.getContentTypeHandler());
        assertNotNull(copyWithContentTypeHandler.getContentTypeHandler());
        assertNull(listType.getValueHandler());
        assertNull(copyWithContentTypeHandler.getValueHandler());

        JavaType mapType = TypeFactory.defaultInstance().constructType(Map.class);
        JavaType copyWithValueHandler = mapType.withValueHandler(new Object());
        assertEquals(mapType.getClass(), copyWithValueHandler.getClass());
        assertEquals(mapType.getRawClass(), copyWithValueHandler.getRawClass());
        assertTrue(mapType.hasGenericTypes());
        assertTrue(copyWithValueHandler.hasGenericTypes());
        assertNull(mapType.getValueHandler());
        assertNotNull(copyWithValueHandler.getValueHandler());
        assertNull(mapType.getTypeHandler());
        assertNull(copyWithValueHandler.getTypeHandler());
    }

    @Test
    public void testStaticTyping() throws Exception {
        JavaType simpleType = TypeFactory.defaultInstance().constructType(String.class);
        JavaType staticType = simpleType.withStaticTyping();
        assertEquals(simpleType.getClass(), staticType.getClass());
        assertEquals(simpleType.getRawClass(), staticType.getRawClass());
        assertFalse(simpleType.hasGenericTypes());
        assertFalse(staticType.hasGenericTypes());
        assertNull(simpleType.getValueHandler());
        assertNull(staticType.getValueHandler());
        assertNull(simpleType.getTypeHandler());
        assertNull(staticType.getTypeHandler());
        assertTrue(simpleType.useStaticType());
        assertTrue(staticType.useStaticType());
    }

    @Test
    public void testNarrowingAndWidening() throws Exception {
        JavaType stringType = TypeFactory.defaultInstance().constructType(String.class);
        JavaType substringType = stringType.narrowBy(SubString.class);
        JavaType superStringType = stringType.widenBy(SubString[].class);

        // Test narrowing
        assertEquals(substringType.getClass(), SubStringType.class);
        assertEquals(SubString.class, substringType.getRawClass());
        assertTrue(substringType.hasGenericTypes());
        assertEquals(Character.class, substringType.getKeyType().getRawClass());
        assertNull(substringType.getContentType());
        assertEquals(1, substringType.containedTypeCount());
        assertEquals("Lcom/example/SubString;<Ljava/lang/Character;>", substringType.getGenericSignature().toString());
        assertEquals(stringType, substringType.narrowFrom(String.class));

        // Test widening
        assertEquals(superStringType.getClass(), StringType.class);
        assertEquals(String[].class, superStringType.getRawClass());
        assertFalse(superStringType.hasGenericTypes());
        assertNull(superStringType.getKeyType());
        assertNull(superStringType.getContentType());
        assertEquals(0, superStringType.containedTypeCount());
        assertEquals("[Ljava/lang/String;", superStringType.getErasedSignature().toString());
        assertEquals(stringType, superStringType.widenTo(String.class));
    }

    @Test
    public void testTypeCoercion() throws Exception {
        JavaType intType = TypeFactory.defaultInstance().constructType(int[].class);
        JavaType numberType = TypeFactory.defaultInstance().constructType(Number[].class);

        // Test narrowing
        JavaType intTypeNarrowed = intType.narrowBy(int[].class);
        JavaType numberTypeNarrowed = numberType.narrowBy(int[].class);
        assertEquals(intType, intTypeNarrowed);
        assertEquals(intType, numberTypeNarrowed);

        // Test widening
        JavaType intTypeWidened = intType.widenBy(Number[].class);
        JavaType numberTypeWidened = numberType.widenBy(int[].class);
        assertEquals(numberType, intTypeWidened);
        assertEquals(numberType, numberTypeWidened);

        // Test narrowing with contained types
        JavaType listIntType = TypeFactory.defaultInstance().constructType(List.class);
        JavaType listIntTypeNarrowed = listIntType.narrowContentsBy(int[].class);
        JavaType listNumberType = TypeFactory.defaultInstance().constructType(List.class);
        JavaType listNumberTypeNarrowed = listNumberType.narrowContentsBy(int[].class);
        assertEquals(listIntType, listIntTypeNarrowed);
        assertEquals(listIntType, listNumberTypeNarrowed);

        // Test widening with contained types
        JavaType listIntTypeWidened = listIntType.widenContentsBy(Number[].class);
        JavaType listNumberTypeWidened = listNumberType.widenContentsBy(int[].class);
        assertEquals(listNumberType, listIntTypeWidened);
        assertEquals(listNumberType, listNumberTypeWidened);
    }

    private static class CustomType {}

    private static class SubString {}

    private static class SubStringType extends JavaType {
        public SubStringType(Class<?> raw, int additionalHash, Object valueHandler, Object typeHandler, boolean asStatic) {
            super(raw, additionalHash, valueHandler, typeHandler, asStatic);
        }

        @Override
        public JavaType withTypeHandler(Object h) {
            throw new UnsupportedOperationException();
        }

        @Override
        public JavaType withContentTypeHandler(Object h) {
            throw new UnsupportedOperationException();
        }

        @Override
        public JavaType withValueHandler(Object h) {
            throw new UnsupportedOperationException();
        }

        @Override
        public JavaType withContentValueHandler(Object h) {
            throw new UnsupportedOperationException();
        }

        @Override
        protected JavaType _narrow(Class<?> subclass) {
            throw new UnsupportedOperationException();
        }

        @Override
        protected JavaType _widen(Class<?> superclass) {
            throw new UnsupportedOperationException();
        }

        @Override
        public JavaType narrowContentsBy(Class<?> contentClass) {
            throw new UnsupportedOperationException();
        }

        @Override
        public JavaType widenContentsBy(Class<?> contentClass) {
            throw new UnsupportedOperationException();
        }

        @Override
        public Class<?> getParameterSource() {
            throw new UnsupportedOperationException();
        }
    }

    private static class StringType extends JavaType {
        public StringType(Class<?> raw, int additionalHash, Object valueHandler, Object typeHandler, boolean asStatic) {
            super(raw, additionalHash, valueHandler, typeHandler, asStatic);
        }
    }
}
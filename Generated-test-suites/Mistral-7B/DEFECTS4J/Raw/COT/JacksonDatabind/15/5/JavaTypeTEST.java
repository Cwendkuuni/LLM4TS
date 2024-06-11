package com.example; // replace with your test package

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.TypeFactory;

import org.junit.Test;

public class JavaTypeTest {

    @Test
    public void testJavaTypeConstructor() {
        Class<?> rawClass = String.class;
        JavaType javaType = new JavaType(rawClass, 0, null, null, false);
        assertEquals(rawClass, javaType.getRawClass());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testJavaTypeInvalidSubclass() {
        Class<?> rawClass = String.class;
        JavaType javaType = new JavaType(Integer.class, 0, null, null, false);
        javaType.narrowBy(rawClass);
    }

    @Test
    public void testJavaTypeNarrowBy() {
        Class<?> rawClass = String[].class;
        JavaType javaType = new JavaType(rawClass, 0, null, null, false);
        JavaType narrowType = javaType.narrowBy(String[].class);
        assertEquals(rawClass, narrowType.getRawClass());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testJavaTypeWidenByInvalidSuperclass() {
        Class<?> rawClass = String.class;
        JavaType javaType = new JavaType(rawClass, 0, null, null, false);
        javaType.widenBy(Integer.class);
    }

    @Test
    public void testJavaTypeWidenBy() {
        Class<?> rawClass = String.class;
        JavaType javaType = new JavaType(rawClass, 0, null, null, false);
        JavaType widenedType = javaType.widenBy(Object.class);
        assertEquals(Object.class, widenedType.getRawClass());
    }

    @Test
    public void testJavaTypeHasRawClass() {
        Class<?> rawClass = String.class;
        JavaType javaType = new JavaType(rawClass, 0, null, null, false);
        assertTrue(javaType.hasRawClass(rawClass));
    }

    @Test
    public void testJavaTypeIsAbstract() {
        Class<?> rawClass = AbstractClass.class;
        JavaType javaType = new JavaType(rawClass, 0, null, null, false);
        assertTrue(javaType.isAbstract());
    }

    @Test
    public void testJavaTypeIsConcrete() {
        Class<?> rawClass = String.class;
        JavaType javaType = new JavaType(rawClass, 0, null, null, false);
        assertTrue(javaType.isConcrete());
    }

    @Test
    public void testJavaTypeIsEnumType() {
        Class<?> rawClass = Enum.class;
        JavaType javaType = new JavaType(rawClass, 0, null, null, false);
        assertTrue(javaType.isEnumType());
    }

    @Test
    public void testJavaTypeIsInterface() {
        Class<?> rawClass = Collection.class;
        JavaType javaType = new JavaType(rawClass, 0, null, null, false);
        assertTrue(javaType.isInterface());
    }

    @Test
    public void testJavaTypeIsPrimitive() {
        Class<?> rawClass = int.class;
        JavaType javaType = new JavaType(rawClass, 0, null, null, false);
        assertTrue(javaType.isPrimitive());
    }

    @Test
    public void testJavaTypeIsFinal() {
        Class<?> rawClass = FinalClass.class;
        JavaType javaType = new JavaType(rawClass, 0, null, null, false);
        assertTrue(javaType.isFinal());
    }

    @Test
    public void testJavaTypeIsContainerType() {
        Class<?> rawClass = List.class;
        JavaType javaType = new JavaType(rawClass, 0, null, null, false);
        assertTrue(javaType.isContainerType());
    }

    @Test
    public void testJavaTypeIsCollectionLikeType() {
        Class<?> rawClass = ArrayList.class;
        JavaType javaType = new JavaType(rawClass, 0, null, null, false);
        assertFalse(javaType.isCollectionLikeType());
    }

    @Test
    public void testJavaTypeIsMapLikeType() {
        Class<?> rawClass = HashMap.class;
        JavaType javaType = new JavaType(rawClass, 0, null, null, false);
        assertFalse(javaType.isMapLikeType());
    }

    @Test
    public void testJavaTypeGetValueHandler() {
        Class<?> rawClass = String.class;
        JavaType javaType = new JavaType(rawClass, 0, new StringHandler(), null, false);
        StringHandler stringHandler = javaType.getValueHandler();
        assertNotNull(stringHandler);
        assertEquals(StringHandler.class, stringHandler.getClass());
    }

    @Test(expected = ClassCastException.class)
    public void testJavaTypeGetValueHandlerInvalidType() {
        Class<?> rawClass = String.class;
        JavaType javaType = new JavaType(rawClass, 0, null, null, false);
        javaType.getValueHandler();
    }

    @Test
    public void testJavaTypeGetTypeHandler() {
        Class<?> rawClass = String.class;
        JavaType javaType = new JavaType(rawClass, 0, null, new StringTypeHandler(), false);
        StringTypeHandler stringTypeHandler = javaType.getTypeHandler();
        assertNotNull(stringTypeHandler);
        assertEquals(StringTypeHandler.class, stringTypeHandler.getClass());
    }

    @Test(expected = ClassCastException.class)
    public void testJavaTypeGetTypeHandlerInvalidType() {
        Class<?> rawClass = String.class;
        JavaType javaType = new JavaType(rawClass, 0, null, null, false);
        javaType.getTypeHandler();
    }

    @Test
    public void testJavaTypeUseStaticType() {
        Class<?> rawClass = String.class;
        JavaType javaType = new JavaType(rawClass, 0, null, null, true);
        assertTrue(javaType.useStaticType());
    }

    @Test
    public void testJavaTypeUseStaticTypeFalse() {
        Class<?> rawClass = String.class;
        JavaType javaType = new JavaType(rawClass, 0, null, null, false);
        assertFalse(javaType.useStaticType());
    }

    @Test(expected = IllegalStateException.class)
    public void testJavaTypeWithInvalidMethodCall() {
        Class<?> rawClass = String.class;
        JavaType javaType = new JavaType(rawClass, 0, null, null, false);
        javaType.withTypeHandler(new Object());
    }

    private static class StringHandler implements StringHandler {
        // Empty implementation
    }

    private static class StringTypeHandler implements StringTypeHandler {
        // Empty implementation
    }

    private static interface StringHandler {}

    private static interface StringTypeHandler {}

    private static class AbstractClass {
        // Empty class
    }

    private static class FinalClass {
        // Empty class
    }
}
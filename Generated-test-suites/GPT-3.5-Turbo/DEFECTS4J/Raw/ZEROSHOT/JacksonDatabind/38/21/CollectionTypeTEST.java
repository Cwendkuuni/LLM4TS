package com.fasterxml.jackson.databind.type;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class CollectionTypeTest {

    private CollectionType collectionType;

    @Before
    public void setup() {
        collectionType = new CollectionType(Class.class, TypeBindings.class,
                JavaType.class, JavaType[].class, JavaType.class,
                Object.class, Object.class, false);
    }

    @Test
    public void testConstruct() {
        CollectionType result = CollectionType.construct(Class.class, TypeBindings.class,
                JavaType.class, JavaType[].class, JavaType.class);
        Assert.assertNotNull(result);
        Assert.assertEquals(Class.class, result.getRawClass());
        Assert.assertEquals(TypeBindings.class, result.getBindings());
        Assert.assertEquals(JavaType.class, result.getSuperClass());
        Assert.assertEquals(JavaType[].class, result.getSuperInterfaces());
        Assert.assertEquals(JavaType.class, result.getElementType());
        Assert.assertNull(result.getValueHandler());
        Assert.assertNull(result.getTypeHandler());
        Assert.assertFalse(result.isStatic());
    }

    @Test
    public void testWithContentType() {
        JavaType contentType = new JavaType();
        CollectionType result = collectionType.withContentType(contentType);
        Assert.assertNotNull(result);
        Assert.assertEquals(Class.class, result.getRawClass());
        Assert.assertEquals(TypeBindings.class, result.getBindings());
        Assert.assertEquals(JavaType.class, result.getSuperClass());
        Assert.assertEquals(JavaType[].class, result.getSuperInterfaces());
        Assert.assertEquals(contentType, result.getElementType());
        Assert.assertNull(result.getValueHandler());
        Assert.assertNull(result.getTypeHandler());
        Assert.assertFalse(result.isStatic());
    }

    @Test
    public void testWithTypeHandler() {
        Object typeHandler = new Object();
        CollectionType result = collectionType.withTypeHandler(typeHandler);
        Assert.assertNotNull(result);
        Assert.assertEquals(Class.class, result.getRawClass());
        Assert.assertEquals(TypeBindings.class, result.getBindings());
        Assert.assertEquals(JavaType.class, result.getSuperClass());
        Assert.assertEquals(JavaType[].class, result.getSuperInterfaces());
        Assert.assertEquals(JavaType.class, result.getElementType());
        Assert.assertNull(result.getValueHandler());
        Assert.assertEquals(typeHandler, result.getTypeHandler());
        Assert.assertFalse(result.isStatic());
    }

    @Test
    public void testWithContentTypeHandler() {
        Object typeHandler = new Object();
        JavaType contentType = new JavaType();
        CollectionType result = collectionType.withContentTypeHandler(typeHandler);
        Assert.assertNotNull(result);
        Assert.assertEquals(Class.class, result.getRawClass());
        Assert.assertEquals(TypeBindings.class, result.getBindings());
        Assert.assertEquals(JavaType.class, result.getSuperClass());
        Assert.assertEquals(JavaType[].class, result.getSuperInterfaces());
        Assert.assertEquals(contentType.withTypeHandler(typeHandler), result.getElementType());
        Assert.assertNull(result.getValueHandler());
        Assert.assertNull(result.getTypeHandler());
        Assert.assertFalse(result.isStatic());
    }

    @Test
    public void testWithValueHandler() {
        Object valueHandler = new Object();
        CollectionType result = collectionType.withValueHandler(valueHandler);
        Assert.assertNotNull(result);
        Assert.assertEquals(Class.class, result.getRawClass());
        Assert.assertEquals(TypeBindings.class, result.getBindings());
        Assert.assertEquals(JavaType.class, result.getSuperClass());
        Assert.assertEquals(JavaType[].class, result.getSuperInterfaces());
        Assert.assertEquals(JavaType.class, result.getElementType());
        Assert.assertEquals(valueHandler, result.getValueHandler());
        Assert.assertNull(result.getTypeHandler());
        Assert.assertFalse(result.isStatic());
    }

    @Test
    public void testWithContentValueHandler() {
        Object valueHandler = new Object();
        JavaType contentType = new JavaType();
        CollectionType result = collectionType.withContentValueHandler(valueHandler);
        Assert.assertNotNull(result);
        Assert.assertEquals(Class.class, result.getRawClass());
        Assert.assertEquals(TypeBindings.class, result.getBindings());
        Assert.assertEquals(JavaType.class, result.getSuperClass());
        Assert.assertEquals(JavaType[].class, result.getSuperInterfaces());
        Assert.assertEquals(contentType.withValueHandler(valueHandler), result.getElementType());
        Assert.assertEquals(valueHandler, result.getValueHandler());
        Assert.assertNull(result.getTypeHandler());
        Assert.assertFalse(result.isStatic());
    }

    @Test
    public void testWithStaticTyping() {
        CollectionType result = collectionType.withStaticTyping();
        Assert.assertNotNull(result);
        Assert.assertEquals(Class.class, result.getRawClass());
        Assert.assertEquals(TypeBindings.class, result.getBindings());
        Assert.assertEquals(JavaType.class, result.getSuperClass());
        Assert.assertEquals(JavaType[].class, result.getSuperInterfaces());
        Assert.assertEquals(JavaType.class, result.getElementType());
        Assert.assertNull(result.getValueHandler());
        Assert.assertNull(result.getTypeHandler());
        Assert.assertTrue(result.isStatic());
    }

    @Test
    public void testRefine() {
        JavaType superClass = new JavaType();
        JavaType[] superInterfaces = new JavaType[]{new JavaType()};
        CollectionType result = collectionType.refine(Class.class, TypeBindings.class,
                superClass, superInterfaces);
        Assert.assertNotNull(result);
        Assert.assertEquals(Class.class, result.getRawClass());
        Assert.assertEquals(TypeBindings.class, result.getBindings());
        Assert.assertEquals(superClass, result.getSuperClass());
        Assert.assertEquals(superInterfaces, result.getSuperInterfaces());
        Assert.assertEquals(JavaType.class, result.getElementType());
        Assert.assertNull(result.getValueHandler());
        Assert.assertNull(result.getTypeHandler());
        Assert.assertFalse(result.isStatic());
    }

    @Test
    public void testToString() {
        String result = collectionType.toString();
        Assert.assertEquals("[collection type; class java.lang.Class, contains com.fasterxml.jackson.databind.JavaType]", result);
    }
}
package com.fasterxml.jackson.databind.type.test;

import static org.junit.Assert.*;

import java.lang.ClassNotFoundException;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;

import org.junit.Test;

import com.fasterxml.jackson.core.type.TypeBindings;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.CollectionLikeType;
import com.fasterxml.jackson.databind.type.CollectionType;
import com.fasterxml.jackson.databind.type.TypeBase;
import com.fasterxml.jackson.databind.type.TypeFactory;

public class CollectionTypeTest {

    @Test
    public void testCollectionTypeConstructor() throws InstantiationException,
            IllegalAccessException, ClassNotFoundException,
            NoSuchMethodException, InvocationTargetException {
        TypeFactory tf = new TypeFactory();
        JavaType javaType = tf.constructType(CollectionType.class, Integer.class);
        CollectionType collectionType = (CollectionType) javaType.getRawClass()
                .getDeclaredConstructor(TypeBindings.class, JavaType.class, JavaType.class,
                        JavaType[].class, JavaType.class, Object.class, Object.class, boolean.class)
                .newInstance(new TypeBindings(), null, tf.constructType(ArrayList.class),
                        null, Integer.TYPE, null, null, false);
        assertEquals("collection type; class class java.util.ArrayList, contains class java.lang.Integer",
                collectionType.toString());
    }

    @Test
    public void testCollectionTypeConstructDeprecated() throws InstantiationException,
            IllegalAccessException, ClassNotFoundException,
            NoSuchMethodException, InvocationTargetException {
        TypeFactory tf = new TypeFactory();
        JavaType javaType = tf.constructType(CollectionType.class, Integer.class);
        CollectionType collectionType = (CollectionType) javaType.getRawClass()
                .getDeclaredConstructor(Class.class, JavaType.class)
                .newInstance(Integer.class, Integer.TYPE);
        assertEquals("collection type; class class java.util.ArrayList, contains class java.lang.Integer",
                collectionType.toString());
    }

    @Test
    public void testDeprecatedNarrowMethod() {
        TypeFactory tf = new TypeFactory();
        JavaType listType = tf.constructType(CollectionType.class, String.class);
        JavaType listSubclassType = tf.constructType(ArrayList.class);
        CollectionType collectionType = (CollectionType) listType.getRawClass()
                .getDeclaredConstructor(TypeBindings.class, JavaType.class)
                .newInstance(new TypeBindings(), null);
        assertNotNull(collectionType._narrow(ArrayList.class));
        try {
            collectionType._narrow(LinkedList.class);
            fail("Expected IllegalStateException");
        } catch (IllegalStateException e) {
            // Expected
        }
    }

    @Test
    public void testWithContentTypeHandler() throws InstantiationException,
            IllegalAccessException, ClassNotFoundException,
            NoSuchMethodException, InvocationTargetException {
        TypeFactory tf = new TypeFactory();
        JavaType javaType = tf.constructType(CollectionType.class, Integer.class);
        CollectionType collectionType = (CollectionType) javaType.getRawClass()
                .getDeclaredConstructor(TypeBindings.class, JavaType.class, JavaType.class,
                        JavaType[].class, JavaType.class, Object.class, Object.class, boolean.class)
                .newInstance(new TypeBindings(), null, tf.constructType(ArrayList.class),
                        null, Integer.TYPE, null, null, false);
        JavaType newContentType = tf.constructType(String.class);
        CollectionType newCollectionType = collectionType.withContentTypeHandler(newContentType);
        assertEquals("collection type; class class java.util.ArrayList, contains class java.lang.String",
                newCollectionType.toString());
    }

    @Test
    public void testWithTypeHandler() throws InstantiationException,
            IllegalAccessException, ClassNotFoundException,
            NoSuchMethodException, InvocationTargetException {
        TypeFactory tf = new TypeFactory();
        JavaType javaType = tf.constructType(CollectionType.class, Integer.class);
        CollectionType collectionType = (CollectionType) javaType.getRawClass()
                .getDeclaredConstructor(TypeBindings.class, JavaType.class, JavaType.class,
                        JavaType[].class, JavaType.class, Object.class, Object.class, boolean.class)
                .newInstance(new TypeBindings(), null, tf.constructType(ArrayList.class),
                        null, Integer.TYPE, null, null, false);
        Object handler = new Object();
        CollectionType newCollectionType = collectionType.withTypeHandler(handler);
        assertEquals("collection type; class class java.util.ArrayList, contains class java.lang.Integer",
                newCollectionType.toString());
    }

    @Test
    public void testWithContentType() throws InstantiationException,
            IllegalAccessException, ClassNotFoundException,
            NoSuchMethodException, InvocationTargetException {
        TypeFactory tf = new TypeFactory();
        JavaType javaType = tf.constructType(CollectionType.class, Integer.class);
        CollectionType collectionType = (CollectionType) javaType.getRawClass()
                .getDeclaredConstructor(TypeBase.class, JavaType.class)
                .newInstance(tf.constructType(ArrayList.class), Integer.TYPE);
        JavaType newContentType = tf.constructType(String.class);
        CollectionType newCollectionType = collectionType.withContentType(newContentType);
        assertEquals("collection type; class class java.util.ArrayList, contains class java.lang.String",
                newCollectionType.toString());
    }

    @Test
    public void testWithStaticTyping() throws InstantiationException,
            IllegalAccessException, ClassNotFoundException,
            NoSuchMethodException, InvocationTargetException {
        TypeFactory tf = new TypeFactory();
        JavaType javaType = tf.constructType(CollectionType.class, Integer.class);
        CollectionType collectionType = (CollectionType) javaType.getRawClass()
                .getDeclaredConstructor(TypeBindings.class, JavaType.class)
                .newInstance(new TypeBindings(), null);
        CollectionType staticCollectionType = collectionType.withStaticTyping();
        assertFalse(staticCollectionType._asStatic);
        staticCollectionType = staticCollectionType.withStaticTyping();
        assertTrue(staticCollectionType._asStatic);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testWithInvalidContentType() throws InstantiationException,
            IllegalAccessException, ClassNotFoundException,
            NoSuchMethodException, InvocationTargetException {
        TypeFactory tf = new TypeFactory();
        JavaType javaType = tf.constructType(CollectionType.class, Integer.class);
        CollectionType collectionType = (CollectionType) javaType.getRawClass()
                .getDeclaredConstructor(TypeBindings.class, JavaType.class)
                .newInstance(new TypeBindings(), null);
        JavaType invalidContentType = tf.constructType(String[].class);
        collectionType.withContentType(invalidContentType);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testWithInvalidTypeHandler() throws InstantiationException,
            IllegalAccessException, ClassNotFoundException,
            NoSuchMethodException, InvocationTargetException {
        TypeFactory tf = new TypeFactory();
        JavaType javaType = tf.constructType(CollectionType.class, Integer.class);
        CollectionType collectionType = (CollectionType) javaType.getRawClass()
                .getDeclaredConstructor(TypeBindings.class, JavaType.class)
                .newInstance(new TypeBindings(), null);
        Object invalidHandler = new Object();
        collectionType.withTypeHandler(invalidHandler);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testWithInvalidValueHandler() throws InstantiationException,
            IllegalAccessException, ClassNotFoundException,
            NoSuchMethodException, InvocationTargetException {
        TypeFactory tf = new TypeFactory();
        JavaType javaType = tf.constructType(CollectionType.class, Integer.class);
        CollectionType collectionType = (CollectionType) javaType.getRawClass()
                .getDeclaredConstructor(TypeBindings.class, JavaType.class)
                .newInstance(new TypeBindings(), null);
        Object invalidHandler = new Object();
        collectionType.withValueHandler(invalidHandler);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testWithInvalidContentValueHandler() throws InstantiationException,
            IllegalAccessException, ClassNotFoundException,
            NoSuchMethodException, InvocationTargetException {
        TypeFactory tf = new TypeFactory();
        JavaType javaType = tf.constructType(CollectionType.class, Integer.class);
        CollectionType collectionType = (CollectionType) javaType.getRawClass()
                .getDeclaredConstructor(TypeBindings.class, JavaType.class)
                .newInstance(new TypeBindings(), null);
        Object invalidHandler = new Object();
        collectionType.withContentValueHandler(invalidHandler);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testWithInvalidSubclass() throws InstantiationException,
            IllegalAccessException, ClassNotFoundException,
            NoSuchMethodException, InvocationTargetException {
        TypeFactory tf = new TypeFactory();
        JavaType javaType = tf.constructType(CollectionType.class, Integer.class);
        CollectionType collectionType = (CollectionType) javaType.getRawClass()
                .getDeclaredConstructor(TypeBindings.class, JavaType.class)
                .newInstance(new TypeBindings(), null);
        CollectionType subclass = new CollectionType(LinkedList.class, null,
                tf.constructType(CollectionLikeType.class), null, null, null, null, false);
        try {
            collectionType.refine(LinkedList.class, new TypeBindings(), null, null);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testWithInvalidTypeBindings() throws InstantiationException,
            IllegalAccessException, ClassNotFoundException,
            NoSuchMethodException, InvocationTargetException {
        TypeFactory tf = new TypeFactory();
        JavaType javaType = tf.constructType(CollectionType.class, Integer.class);
        CollectionType collectionType = (CollectionType) javaType.getRawClass()
                .getDeclaredConstructor(TypeBindings.class, JavaType.class)
                .newInstance(null, null);
        try {
            collectionType.refine(ArrayList.class, null, null, null);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testWithInvalidSuperClass() throws InstantiationException,
            IllegalAccessException, ClassNotFoundException,
            NoSuchMethodException, InvocationTargetException {
        TypeFactory tf = new TypeFactory();
        JavaType javaType = tf.constructType(CollectionType.class, Integer.class);
        CollectionType collectionType = (CollectionType) javaType.getRawClass()
                .getDeclaredConstructor(TypeBindings.class, JavaType.class)
                .newInstance(new TypeBindings(), null);
        try {
            collectionType.refine(ArrayList.class, new TypeBindings(),
                    tf.constructType(Integer[].class), null); // incorrect superclass
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testWithInvalidSuperInterfaces() throws InstantiationException,
            IllegalAccessException, ClassNotFoundException,
            NoSuchMethodException, InvocationTargetException {
        TypeFactory tf = new TypeFactory();
        JavaType javaType = tf.constructType(CollectionType.class, Integer.class);
        CollectionType collectionType = (CollectionType) javaType.getRawClass()
                .getDeclaredConstructor(TypeBindings.class, JavaType.class)
                .newInstance(new TypeBindings(), null);
        try {
            collectionType.refine(ArrayList.class, new TypeBindings(), null,
                    new JavaType[] { tf.constructType(String.class) }); // incorrect superinterfaces
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test(expected = IllegalStateException.class)
    public void testWithInvalidElementType() throws InstantiationException,
            IllegalAccessException, ClassNotFoundException,
            NoSuchMethodException, InvocationTargetException {
        TypeFactory tf = new TypeFactory();
        JavaType javaType = tf.constructType(CollectionType.class, Integer.class);
        CollectionType collectionType = (CollectionType) javaType.getRawClass()
                .getDeclaredConstructor(TypeBindings.class, JavaType.class)
                .newInstance(new TypeBindings(), null);
        JavaType invalidElementType = tf.constructType(String.class);
        try {
            collectionType.refine(ArrayList.class, new TypeBindings(), null, null, invalidElementType);
            fail("Expected IllegalStateException");
        } catch (IllegalStateException e) {
            // Expected
        }
    }
}
package com.example.junit; // Replace with your package name

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.lang.ClassNotFoundException;
import java.lang.ClassTypeException;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;

import org.junit.Test;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.TypeBindings;
import com.fasterxml.jackson.databind.TypeDeserializer;
import com.fasterxml.jackson.databind.TypeHandler;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer.EmptyTypeDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;

public class CollectionTypeTest {

    @Test
    public void testConstructor() {
        Class<?> collectionClass = ArrayList.class;
        TypeBindings bindings = TypeBindings.getDefaultInstance();
        JavaType superClass = ArrayList.class;
        JavaType[] superInterfaces = new JavaType[] {Collection.class};
        JavaType elemType = IntegerType.instance;
        CollectionType collectionType = CollectionType.construct(collectionClass, bindings, superClass, superInterfaces, elemType);
        assertNotNull(collectionType);
        assertEquals(collectionClass, collectionType.getRawClass());
        assertEquals(Arrays.asList(superClass, superInterfaces), collectionType.getSuperTypes());
        assertEquals(elemType, collectionType.getContentType());
    }

    @Test
    public void testConstructWithBogusSuperClass() {
        Class<?> collectionClass = ArrayList.class;
        JavaType elemType = IntegerType.instance;
        try {
            CollectionType collectionType = CollectionType.construct(collectionClass, null, null, null, elemType);
            fail("Expected DeprecatedException");
        } catch (DeprecatedException e) {
            // Expected
        }
    }

    @Test
    public void testNarrow() {
        Class<?> listClass = ArrayList.class;
        CollectionType listType = CollectionType.construct(listClass, null, null, null, IntegerType.instance);
        CollectionType subListType = listType.narrow(LinkedList.class);
        assertNotNull(subListType);
        assertEquals(LinkedList.class, subListType.getRawClass());
        assertNotSame(listType, subListType);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testDeprecatedConstructWithOnlyElemType() {
        Class<?> collectionClass = ArrayList.class;
        JavaType elemType = IntegerType.instance;
        CollectionType collectionType = CollectionType.construct(collectionClass, elemType);
    }

    @Test
    public void testWithContentType() {
        Class<?> collectionClass = ArrayList.class;
        TypeBindings bindings = TypeBindings.getDefaultInstance();
        JavaType superClass = ArrayList.class;
        JavaType[] superInterfaces = new JavaType[] {Collection.class};
        JavaType elemType = IntegerType.instance;
        JavaType newElemType = LongType.instance;
        CollectionType collectionType = CollectionType.construct(collectionClass, bindings, superClass, superInterfaces, elemType);
        CollectionType newCollectionType = collectionType.withContentType(newElemType);
        assertNotNull(newCollectionType);
        assertNotSame(collectionType, newCollectionType);
        assertEquals(newElemType, newCollectionType.getContentType());
    }

    @Test
    public void testWithTypeHandler() {
        Class<?> collectionClass = ArrayList.class;
        TypeBindings bindings = TypeBindings.getDefaultInstance();
        JavaType superClass = ArrayList.class;
        JavaType[] superInterfaces = new JavaType[] {Collection.class};
        JavaType elemType = IntegerType.instance;
        CollectionType collectionType = CollectionType.construct(collectionClass, bindings, superClass, superInterfaces, elemType);
        TypeHandler<String> stringHandler = new StringTypeHandler();
        CollectionType newCollectionType = collectionType.withTypeHandler(stringHandler);
        assertNotNull(newCollectionType);
        assertNotSame(collectionType, newCollectionType);
        assertNotNull(newCollectionType.getTypeHandler());
        assertEquals(stringHandler, newCollectionType.getTypeHandler());
    }

    @Test(expected = ClassTypeException.class)
    public void testWithInvalidTypeHandler() {
        Class<?> collectionClass = ArrayList.class;
        TypeBindings bindings = TypeBindings.getDefaultInstance();
        JavaType superClass = ArrayList.class;
        JavaType[] superInterfaces = new JavaType[] {Collection.class};
        JavaType elemType = IntegerType.instance;
        CollectionType collectionType = CollectionType.construct(collectionClass, bindings, superClass, superInterfaces, elemType);
        Object invalidHandler = new Object();
        try {
            CollectionType newCollectionType = collectionType.withTypeHandler(invalidHandler);
            fail("Expected ClassTypeException");
        } catch (ClassTypeException e) {
            // Expected
        }
    }

    @Test
    public void testWithContentTypeHandler() {
        Class<?> collectionClass = ArrayList.class;
        TypeBindings bindings = TypeBindings.getDefaultInstance();
        JavaType superClass = ArrayList.class;
        JavaType[] superInterfaces = new JavaType[] {Collection.class};
        JavaType elemType = IntegerType.instance;
        CollectionType collectionType = CollectionType.construct(collectionClass, bindings, superClass, superInterfaces, elemType);
        TypeHandler<String> stringHandler = new StringTypeHandler();
        TypeSerializer<Object> objectSerializer = new EmptyTypeSerializer();
        CollectionType newCollectionType = collectionType.withContentTypeHandler(stringHandler);
        assertNotNull(newCollectionType);
        assertNotSame(collectionType, newCollectionType);
        assertNotNull(newCollectionType.getContentTypeHandler());
        assertEquals(stringHandler, newCollectionType.getContentTypeHandler());
        assertNotNull(newCollectionType.getContentSerializer());
        assertEquals(objectSerializer, newCollectionType.getContentSerializer());
    }

    @Test(expected = InvocationTargetException.class)
    public void testWithContentTypeHandlerWithException() {
        Class<?> collectionClass = ArrayList.class;
        TypeBindings bindings = TypeBindings.getDefaultInstance();
        JavaType superClass = ArrayList.class;
        JavaType[] superInterfaces = new JavaType[] {Collection.class};
        JavaType elemType = IntegerType.instance;
        CollectionType collectionType = CollectionType.construct(collectionClass, bindings, superClass, superInterfaces, elemType);
        TypeHandler<String> stringHandler = new StringTypeHandler();
        TypeSerializer<Object> objectSerializer = new ObjectTypeSerializer(); // Throws exception
        try {
            CollectionType newCollectionType = collectionType.withContentTypeHandler(stringHandler);
            fail("Expected InvocationTargetException");
        } catch (InvocationTargetException e) {
            // Expected
        }
    }

    @Test
    public void testWithValueHandler() {
        Class<?> collectionClass = ArrayList.class;
        TypeBindings bindings = TypeBindings.getDefaultInstance();
        JavaType superClass = ArrayList.class;
        JavaType[] superInterfaces = new JavaType[] {Collection.class};
        JavaType elemType = IntegerType.instance;
        CollectionType collectionType = CollectionType.construct(collectionClass, bindings, superClass, superInterfaces, elemType);
        ValueHandler<String> stringHandler = new StringValueHandler();
        CollectionType newCollectionType = collectionType.withValueHandler(stringHandler);
        assertNotNull(newCollectionType);
        assertNotSame(collectionType, newCollectionType);
        assertNotNull(newCollectionType.getValueHandler());
        assertEquals(stringHandler, newCollectionType.getValueHandler());
    }

    @Test(expected = ClassNotFoundException.class)
    public void testWithValueHandlerWithClassNotFoundException() {
        Class<?> collectionClass = ArrayList.class;
        TypeBindings bindings = TypeBindings.getDefaultInstance();
        JavaType superClass = ArrayList.class;
        JavaType[] superInterfaces = new JavaType[] {Collection.class};
        JavaType elemType = IntegerType.instance;
        CollectionType collectionType = CollectionType.construct(collectionClass, bindings, superClass, superInterfaces, elemType);
        ValueHandler<String> stringHandler = new ValueHandler<String>() {
            @Override
            public void handleValue(String value) throws Exception {
                // Throws ClassNotFoundException intentionally
                throw new ClassNotFoundException("Expected");
            }
        };
        try {
            CollectionType newCollectionType = collectionType.withValueHandler(stringHandler);
            fail("Expected ClassNotFoundException");
        } catch (ClassNotFoundException e) {
            // Expected
        }
    }

    @Test
    public void testWithContentValueHandler() {
        Class<?> collectionClass = ArrayList.class;
        TypeBindings bindings = TypeBindings.getDefaultInstance();
        JavaType superClass = ArrayList.class;
        JavaType[] superInterfaces = new JavaType[] {Collection.class};
        JavaType elemType = IntegerType.instance;
        CollectionType collectionType = CollectionType.construct(collectionClass, bindings, superClass, superInterfaces, elemType);
        ValueHandler<String> stringHandler = new StringValueHandler();
        TypeSerializer<Object> objectSerializer = new EmptyTypeSerializer();
        CollectionType newCollectionType = collectionType.withContentValueHandler(stringHandler);
        assertNotNull(newCollectionType);
        assertNotSame(collectionType, newCollectionType);
        assertNotNull(newCollectionType.getContentValueHandler());
        assertEquals(stringHandler, newCollectionType.getContentValueHandler());
        assertNotNull(newCollectionType.getContentSerializer());
        assertEquals(objectSerializer, newCollectionType.getContentSerializer());
    }

    @Test(expected = InvocationTargetException.class)
    public void testWithContentValueHandlerWithException() {
        Class<?> collectionClass = ArrayList.class;
        TypeBindings bindings = TypeBindings.getDefaultInstance();
        JavaType superClass = ArrayList.class;
        JavaType[] superInterfaces = new JavaType[] {Collection.class};
        JavaType elemType = IntegerType.instance;
        CollectionType collectionType = CollectionType.construct(collectionClass, bindings, superClass, superInterfaces, elemType);
        ValueHandler<String> stringHandler = new ValueHandler<String>() {
            @Override
            public void handleValue(String value) throws Exception {
                // Throws exception intentionally
                throw new InvocationTargetException(new RuntimeException("Expected"));
            }
        };
        TypeSerializer<Object> objectSerializer = new ObjectTypeSerializer(); // Throws exception
        try {
            CollectionType newCollectionType = collectionType.withContentValueHandler(stringHandler);
            fail("Expected InvocationTargetException");
        } catch (InvocationTargetException e) {
            // Expected
        }
    }

    @Test
    public void testWithStaticTyping() {
        Class<?> collectionClass = ArrayList.class;
        JavaType elemType = IntegerType.instance;
        CollectionType collectionType = CollectionType.construct(collectionClass, null, null, null, elemType);
        CollectionType staticCollectionType = collectionType.withStaticTyping();
        assertNotNull(staticCollectionType);
        assertTrue(staticCollectionType.isStatic());
        assertFalse(collectionType.isStatic());
    }

    @Test
    public void testRefine() {
        Class<?> collectionClass = ArrayList.class;
        TypeBindings bindings = TypeBindings.getDefaultInstance();
        JavaType superClass = ArrayList.class;
        JavaType[] superInterfaces = new JavaType[] {Collection.class};
        JavaType elemType = IntegerType.instance;
        CollectionType collectionType = CollectionType.construct(collectionClass, bindings, superClass, superInterfaces, elemType);
        CollectionType refinedCollectionType = collectionType.refine(LinkedList.class, bindings, null, null);
        assertNotNull(refinedCollectionType);
        assertEquals(LinkedList.class, refinedCollectionType.getRawClass());
        assertNotSame(collectionType, refinedCollectionType);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testRefineWithInvalidClass() {
        Class<?> collectionClass = ArrayList.class;
        TypeBindings bindings = TypeBindings.getDefaultInstance();
        JavaType superClass = ArrayList.class;
        JavaType[] superInterfaces = new JavaType[] {Collection.class};
        JavaType elemType = IntegerType.instance;
        CollectionType collectionType = CollectionType.construct(collectionClass, bindings, superClass, superInterfaces, elemType);
        CollectionType refinedCollectionType = collectionType.refine(String.class, bindings, null, null);
        fail("Expected IllegalArgumentException");
    }

    @Test(expected = NullPointerException.class)
    public void testRefineWithNullBindings() {
        Class<?> collectionClass = ArrayList.class;
        JavaType superClass = ArrayList.class;
        JavaType[] superInterfaces = new JavaType[] {Collection.class};
        JavaType elemType = IntegerType.instance;
        CollectionType collectionType = CollectionType.construct(collectionClass, null, superClass, superInterfaces, elemType);
        CollectionType refinedCollectionType = collectionType.refine(LinkedList.class, null, null, null);
        fail("Expected NullPointerException");
    }

}
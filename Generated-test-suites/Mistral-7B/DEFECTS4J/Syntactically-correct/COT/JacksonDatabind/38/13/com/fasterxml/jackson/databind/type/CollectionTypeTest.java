package com.fasterxml.jackson.databind.type.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.lang.Class;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;

import com.fasterxml.jackson.core.type.TypeBindings;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.CollectionLikeType;
import com.fasterxml.jackson.databind.type.CollectionType;
import com.fasterxml.jackson.databind.type.MapType;
import com.fasterxml.jackson.databind.type.TypeDeserializer.EmptyTypeDeserializer;
import com.fasterxml.jackson.databind.type.TypeFactory;

public class CollectionTypeTest {

    // Test construction methods

    @Test
    public void testConstruct() {
        Class<List<String>> rawType = List.class;
        TypeBindings bindings = TypeFactory.defaultBindings();
        JavaType superClass = ArrayList.class;
        JavaType[] superInterfaces = new JavaType[] { Collection.class };
        JavaType elemT = TypeFactory.defaultInstance().constructType(String.class);
        CollectionType collectionType = CollectionType.construct(rawType, bindings, superClass, superInterfaces, elemT);
        assertNotNull(collectionType);
        assertEquals(List.class, collectionType.getRawClass());
        assertEquals(Collection.class, collectionType.getRawClass().getInterfaces()[0]);
        assertEquals(elemT, collectionType.getContentType());
    }

    @Test
    public void testConstructDeprecated() {
        Class<List<String>> rawType = List.class;
        JavaType elemT = TypeFactory.defaultInstance().constructType(String.class);
        CollectionType collectionType = CollectionType.construct(rawType, elemT);
        assertNotNull(collectionType);
        assertEquals(List.class, collectionType.getRawClass());
        assertEquals(elemT, collectionType.getContentType());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testConstructWithNullBindings() {
        Class<List<String>> rawType = List.class;
        JavaType elemT = TypeFactory.defaultInstance().constructType(String.class);
        CollectionType collectionType = new CollectionType(rawType, null, _bogusSuperClass(rawType), null, elemT, null, null, false);
    }

    @Test
    public void testConstructWithNullSuperInts() {
        Class<List<String>> rawType = List.class;
        TypeBindings bindings = TypeFactory.defaultBindings();
        JavaType superClass = ArrayList.class;
        JavaType elemT = TypeFactory.defaultInstance().constructType(String.class);
        CollectionType collectionType = CollectionType.construct(rawType, bindings, superClass, null, elemT);
        assertNotNull(collectionType);
        assertEquals(List.class, collectionType.getRawClass());
        assertEquals(Collection.class, collectionType.getRawClass().getInterfaces()[0]);
        assertEquals(elemT, collectionType.getContentType());
    }

    @Test
    public void testConstructWithNullElemT() {
        Class<List<String>> rawType = List.class;
        TypeBindings bindings = TypeFactory.defaultBindings();
        JavaType superClass = ArrayList.class;
        JavaType[] superInterfaces = new JavaType[] { Collection.class };
        CollectionType collectionType = CollectionType.construct(rawType, bindings, superClass, superInterfaces, null);
        assertNull(collectionType);
    }

    @Test
    public void testWithContentType() {
        Class<List<String>> rawType = List.class;
        TypeBindings bindings = TypeFactory.defaultBindings();
        JavaType superClass = ArrayList.class;
        JavaType[] superInterfaces = new JavaType[] { Collection.class };
        JavaType elemT = TypeFactory.defaultInstance().constructType(String.class);
        JavaType contentType = TypeFactory.defaultInstance().constructType(Integer.class);
        CollectionType collectionType = new CollectionType(_class, bindings, superClass, superInterfaces, elemT);
        CollectionType newCollectionType = collectionType.withContentType(contentType);
        assertNotNull(newCollectionType);
        assertEquals(List.class, newCollectionType.getRawClass());
        assertEquals(Collection.class, newCollectionType.getRawClass().getInterfaces()[0]);
        assertEquals(contentType, newCollectionType.getContentType());
    }

    @Test
    public void testWithTypeHandler() {
        Class<List<String>> rawType = List.class;
        TypeBindings bindings = TypeFactory.defaultBindings();
        JavaType superClass = ArrayList.class;
        JavaType[] superInterfaces = new JavaType[] { Collection.class };
        JavaType elemT = TypeFactory.defaultInstance().constructType(String.class);
        Object typeHandler = new Object() {};
        CollectionType collectionType = new CollectionType(_class, bindings, superClass, superInterfaces, elemT);
        CollectionType newCollectionType = collectionType.withTypeHandler(typeHandler);
        assertNotNull(newCollectionType);
        assertEquals(List.class, newCollectionType.getRawClass());
        assertEquals(Collection.class, newCollectionType.getRawClass().getInterfaces()[0]);
        assertEquals(elemT, newCollectionType.getContentType());
        assertEquals(typeHandler, newCollectionType.getTypeHandler());
    }

    @Test
    public void testWithContentTypeHandler() {
        Class<List<String>> rawType = List.class;
        TypeBindings bindings = TypeFactory.defaultBindings();
        JavaType superClass = ArrayList.class;
        JavaType[] superInterfaces = new JavaType[] { Collection.class };
        JavaType elemT = TypeFactory.defaultInstance().constructType(String.class);
        JavaType contentType = TypeFactory.defaultInstance().constructType(Integer.class);
        Object contentTypeHandler = new Object() {};
        CollectionType collectionType = new CollectionType(_class, bindings, superClass, superInterfaces, elemT);
        CollectionType newCollectionType = collectionType.withContentTypeHandler(contentTypeHandler);
        assertNotNull(newCollectionType);
        assertEquals(List.class, newCollectionType.getRawClass());
        assertEquals(Collection.class, newCollectionType.getRawClass().getInterfaces()[0]);
        assertEquals(contentType, newCollectionType.getContentType());
        assertEquals(contentTypeHandler, newCollectionType.getContentTypeHandler());
    }

    @Test
    public void testWithValueHandler() {
        Class<List<String>> rawType = List.class;
        TypeBindings bindings = TypeFactory.defaultBindings();
        JavaType superClass = ArrayList.class;
        JavaType[] superInterfaces = new JavaType[] { Collection.class };
        JavaType elemT = TypeFactory.defaultInstance().constructType(String.class);
        Object valueHandler = new Object() {};
        CollectionType collectionType = new CollectionType(_class, bindings, superClass, superInterfaces, elemT);
        CollectionType newCollectionType = collectionType.withValueHandler(valueHandler);
        assertNotNull(newCollectionType);
        assertEquals(List.class, newCollectionType.getRawClass());
        assertEquals(Collection.class, newCollectionType.getRawClass().getInterfaces()[0]);
        assertEquals(elemT, newCollectionType.getContentType());
        assertEquals(valueHandler, newCollectionType.getValueHandler());
    }

    @Test
    public void testWithContentValueHandler() {
        Class<List<String>> rawType = List.class;
        TypeBindings bindings = TypeFactory.defaultBindings();
        JavaType superClass = ArrayList.class;
        JavaType[] superInterfaces = new JavaType[] { Collection.class };
        JavaType elemT = TypeFactory.defaultInstance().constructType(String.class);
        JavaType contentType = TypeFactory.defaultInstance().constructType(Integer.class);
        Object contentValueHandler = new Object() {};
        CollectionType collectionType = new CollectionType(_class, bindings, superClass, superInterfaces, elemT);
        CollectionType newCollectionType = collectionType.withContentValueHandler(contentValueHandler);
        assertNotNull(newCollectionType);
        assertEquals(List.class, newCollectionType.getRawClass());
        assertEquals(Collection.class, newCollectionType.getRawClass().getInterfaces()[0]);
        assertEquals(contentType, newCollectionType.getContentType());
        assertEquals(contentValueHandler, newCollectionType.getContentValueHandler());
    }

    @Test
    public void testWithStaticTyping() {
        Class<List<String>> rawType = List.class;
        TypeBindings bindings = TypeFactory.defaultBindings();
        JavaType superClass = ArrayList.class;
        JavaType[] superInterfaces = new JavaType[] { Collection.class };
        JavaType elemT = TypeFactory.defaultInstance().constructType(String.class);
        CollectionType collectionType = new CollectionType(_class, bindings, superClass, superInterfaces, elemT);
        CollectionType newCollectionType = collectionType.withStaticTyping();
        assertNotNull(newCollectionType);
        assertEquals(List.class, newCollectionType.getRawClass());
        assertEquals(Collection.class, newCollectionType.getRawClass().getInterfaces()[0]);
        assertEquals(elemT, newCollectionType.getContentType());
        assertTrue(newCollectionType.isStaticTyping());
    }

    @Test
    public void testRefine() {
        Class<List<String>> rawType = List.class;
        TypeBindings bindings = TypeFactory.defaultBindings();
        JavaType superClass = ArrayList.class;
        JavaType[] superInterfaces = new JavaType[] { Collection.class };
        JavaType elemT = TypeFactory.defaultInstance().constructType(String.class);
        JavaType refinedType = CollectionType.construct(Map.class, bindings, TypeFactory.defaultInstance().constructType(HashMap.class), null, elemT);
        CollectionType collectionType = new CollectionType(_class, bindings, superClass, superInterfaces, elemT);
        CollectionType newCollectionType = collectionType.refine(rawType, bindings, superClass, superInterfaces);
        assertNotNull(newCollectionType);
        assertEquals(MapType.class, newCollectionType.getRawClass());
        assertEquals(Map.class, newCollectionType.getRawClass().getInterfaces()[0]);
        assertEquals(elemT, newCollectionType.getContentType());
    }

    // Test toString method

    @Test
    public void testToString() {
        Class<List<String>> rawType = List.class;
        TypeBindings bindings = TypeFactory.defaultBindings();
        JavaType superClass = ArrayList.class;
        JavaType[] superInterfaces = new JavaType[] { Collection.class };
        JavaType elemT = TypeFactory.defaultInstance().constructType(String.class);
        CollectionType collectionType = new CollectionType(_class, bindings, superClass, superInterfaces, elemT);
        assertEquals("[collection type; class java.util.ArrayList, contains class java.lang.String]", collectionType.toString());
    }
}
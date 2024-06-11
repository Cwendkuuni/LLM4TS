package com.fasterxml.jackson.databind.type.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.util.Arrays;

import org.junit.Test;

import com.fasterxml.jackson.core.type.TypeBindings;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.TypeBindings.TypeSerializerType;
import com.fasterxml.jackson.databind.type.CollectionLikeType;
import com.fasterxml.jackson.databind.type.CollectionType;
import com.fasterxml.jackson.databind.type.TypeBase;
import com.fasterxml.jackson.databind.type.TypeBindings.TypeSerializers;

public class CollectionTypeTest {

    @Test
    public void testCollectionTypeConstructor() {
        Class<String> rawType = String.class;
        JavaType elemType = JavaType.of(int.class);
        TypeBindings bindings = TypeBindings.defaultBindings();
        TypeSerializers serializers = bindings.serializers();
        TypeSerializerType serializerType = new TypeSerializerType(serializers);
        bindings.setSerializerTypes(JavaType.TYPE_STRING, serializerType);
        JavaType superClass = JavaType.getCollectionType(rawType, elemType);
        JavaType[] superInts = new JavaType[] { JavaType.getCollectionType(int[].class) };
        CollectionType collectionType = CollectionType.construct(rawType, bindings, superClass, superInts, elemType);
        assertEquals("collection type; class " + rawType.getName() + ", contains " + elemType, collectionType.toString());
    }

    @Test
    public void testCollectionTypeDeprecatedConstructor() {
        Class<String> rawType = String.class;
        JavaType elemType = JavaType.of(int.class);
        CollectionType collectionType = CollectionType.construct(rawType, elemType);
        assertEquals("collection type; class " + rawType.getName() + ", contains " + elemType, collectionType.toString());
    }

    @Test
    public void testCollectionTypeNarrow() {
        Class<List<String>> rawType = java.util.List.class;
        JavaType elemType = JavaType.of(String.class);
        CollectionType collectionType = CollectionType.construct(rawType, null, null, null, elemType);
        JavaType narrowedType = collectionType.narrow(ArrayList.class);
        assertEquals(ArrayList.class, narrowedType.getRawClass());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testCollectionTypeNarrowDeprecated() {
        Class<List<String>> rawType = java.util.List.class;
        JavaType elemType = JavaType.of(String.class);
        CollectionType collectionType = CollectionType.construct(rawType, null, null, null, elemType);
        collectionType.narrow(HashMap.class);
    }

    @Test
    public void testCollectionTypeWithContentType() {
        Class<String> rawType = String.class;
        JavaType elemType = JavaType.of(int[].class);
        TypeBindings bindings = TypeBindings.defaultBindings();
        JavaType superClass = JavaType.getCollectionType(rawType, elemType);
        JavaType[] superInts = new JavaType[] { JavaType.getCollectionType(int[].class) };
        CollectionType collectionType = CollectionType.construct(rawType, bindings, superClass, superInts, elemType);
        JavaType withContentType = collectionType.withContentType(JavaType.getCollectionType(String[].class));
        assertEquals(JavaType.getCollectionType(JavaType.getCollectionType(String[].class), String.class), withContentType);
    }

    @Test
    public void testCollectionTypeWithTypeHandler() {
        Class<String> rawType = String.class;
        JavaType elemType = JavaType.of(int.class);
        TypeBindings bindings = TypeBindings.defaultBindings();
        JavaType superClass = JavaType.getCollectionType(rawType, elemType);
        JavaType[] superInts = new JavaType[] { JavaType.getCollectionType(int[].class) };
        CollectionType collectionType = CollectionType.construct(rawType, bindings, superClass, superInts, elemType);
        Object typeHandler = new Object();
        CollectionType withTypeHandler = collectionType.withTypeHandler(typeHandler);
        assertEquals(typeHandler, withTypeHandler.getTypeHandler());
    }

    @Test
    public void testCollectionTypeWithContentTypeHandler() {
        Class<String> rawType = String.class;
        JavaType elemType = JavaType.of(int.class);
        TypeBindings bindings = TypeBindings.defaultBindings();
        JavaType superClass = JavaType.getCollectionType(rawType, elemType);
        JavaType[] superInts = new JavaType[] { JavaType.getCollectionType(int[].class) };
        CollectionType collectionType = CollectionType.construct(rawType, bindings, superClass, superInts, elemType);
        Object contentTypeHandler = new Object();
        CollectionType withContentTypeHandler = collectionType.withContentTypeHandler(contentTypeHandler);
        assertEquals(contentTypeHandler, withContentTypeHandler.getContentTypeHandler());
    }

    @Test
    public void testCollectionTypeWithValueHandler() {
        Class<String> rawType = String.class;
        JavaType elemType = JavaType.of(int.class);
        TypeBindings bindings = TypeBindings.defaultBindings();
        JavaType superClass = JavaType.getCollectionType(rawType, elemType);
        JavaType[] superInts = new JavaType[] { JavaType.getCollectionType(int[].class) };
        CollectionType collectionType = CollectionType.construct(rawType, bindings, superClass, superInts, elemType);
        Object valueHandler = new Object();
        CollectionType withValueHandler = collectionType.withValueHandler(valueHandler);
        assertEquals(valueHandler, withValueHandler.getValueHandler());
    }

    @Test
    public void testCollectionTypeWithContentValueHandler() {
        Class<String> rawType = String.class;
        JavaType elemType = JavaType.of(int.class);
        TypeBindings bindings = TypeBindings.defaultBindings();
        JavaType superClass = JavaType.getCollectionType(rawType, elemType);
        JavaType[] superInts = new JavaType[] { JavaType.getCollectionType(int[].class) };
        CollectionType collectionType = CollectionType.construct(rawType, bindings, superClass, superInts, elemType);
        Object contentValueHandler = new Object();
        CollectionType withContentValueHandler = collectionType.withContentValueHandler(contentValueHandler);
        assertEquals(contentValueHandler, withContentValueHandler.getContentValueHandler());
    }

    @Test
    public void testCollectionTypeWithStaticTyping() {
        Class<String> rawType = String.class;
        JavaType elemType = JavaType.of(int.class);
        CollectionType collectionType = CollectionType.construct(rawType, null, null, null, elemType);
        CollectionType withStaticTyping = collectionType.withStaticTyping();
        assertTrue(withStaticTyping.isStatic());
    }

    @Test
    public void testCollectionTypeRefine() {
        Class<String> rawType = String.class;
        JavaType elemType = JavaType.of(int.class);
        TypeBindings bindings = TypeBindings.defaultBindings();
        JavaType superClass = JavaType.getCollectionType(rawType, elemType);
        JavaType[] superInts = new JavaType[] { JavaType.getCollectionType(int[].class) };
        CollectionType collectionType = CollectionType.construct(rawType, bindings, superClass, superInts, elemType);
        JavaType refinedType = collectionType.refine(ArrayList.class, bindings, superClass, superInts);
        assertEquals(ArrayList.class, refinedType.getRawClass());
    }

}
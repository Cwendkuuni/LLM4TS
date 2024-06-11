package com.fasterxml.jackson.databind.type;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.lang.reflect.TypeVariable;
import java.util.Collection;

import com.fasterxml.jackson.databind.JavaType;
import org.junit.Test;

public class CollectionLikeTypeTest {

    @Test
    public void testConstruct() {
        Class<?> rawType = Collection.class;
        TypeBindings bindings = TypeBindings.emptyBindings();
        JavaType superClass = null;
        JavaType[] superInts = null;
        JavaType elemT = null;

        CollectionLikeType collectionLikeType = new CollectionLikeType(rawType, bindings, superClass, superInts, elemT, null, null, false);

        assertNotNull(collectionLikeType);
        assertEquals(rawType, collectionLikeType._class);
        assertEquals(bindings, collectionLikeType._bindings);
        assertEquals(superClass, collectionLikeType._superClass);
        assertEquals(superInts, collectionLikeType._superInterfaces);
        assertEquals(elemT, collectionLikeType._elementType);
        assertFalse(collectionLikeType._asStatic);
    }

    @Test
    public void testUpgradeFrom() {
        JavaType baseType = new JavaType();
        JavaType elementType = new JavaType();

        CollectionLikeType collectionLikeType = CollectionLikeType.upgradeFrom(baseType, elementType);

        assertNotNull(collectionLikeType);
        assertEquals(baseType, collectionLikeType.baseType);
        assertEquals(elementType, collectionLikeType.elementType);
    }

    @Test
    public void testNarrow() {
        Class<?> subclass = Collection.class;

        CollectionLikeType collectionLikeType = new CollectionLikeType(subclass, null, null, null, null, null, null, false);
        JavaType narrowedType = collectionLikeType._narrow(subclass);

        assertEquals(subclass, narrowedType.getRawType());
        assertEquals(collectionLikeType._bindings, narrowedType.getBindings());
        assertEquals(collectionLikeType._superClass, narrowedType.getSuperClass());
        assertEquals(collectionLikeType._superInterfaces, narrowedType.getInterfaces());
        assertEquals(collectionLikeType._elementType, narrowedType.getElementType());
        assertFalse(narrowedType.isAsStatic());
    }

    @Test
    public void testWithContentType() {
        Class<?> rawType = Collection.class;
        TypeBindings bindings = TypeBindings.emptyBindings();
        JavaType superClass = null;
        JavaType[] superInts = null;
        JavaType elemT = null;

        CollectionLikeType collectionLikeType = new CollectionLikeType(rawType, bindings, superClass, superInts, elemT, null, null, false);
        JavaType contentType = new JavaType();

        JavaType typeWithContentType = collectionLikeType.withContentType(contentType);

        assertEquals(rawType, typeWithContentType.getRawType());
        assertEquals(bindings, typeWithContentType.getBindings());
        assertEquals(superClass, typeWithContentType.getSuperClass());
        assertEquals(superInts, typeWithContentType.getInterfaces());
        assertEquals(contentType, typeWithContentType.getElementType());
        assertFalse(typeWithContentType.isAsStatic());
    }

    @Test
    public void testWithTypeHandler() {
        Class<?> rawType = Collection.class;
        TypeBindings bindings = TypeBindings.emptyBindings();
        JavaType superClass = null;
        JavaType[] superInts = null;
        JavaType elemT = null;

        CollectionLikeType collectionLikeType = new CollectionLikeType(rawType, bindings, superClass, superInts, elemT, null, null, false);
        Object handler = new Object();

        CollectionLikeType typeWithTypeHandler = collectionLikeType.withTypeHandler(handler);

        assertEquals(rawType, typeWithTypeHandler.getRawType());
        assertEquals(bindings, typeWithTypeHandler.getBindings());
        assertEquals(superClass, typeWithTypeHandler.getSuperClass());
        assertEquals(superInts, typeWithTypeHandler.getInterfaces());
        assertEquals(elemT, typeWithTypeHandler.getElementType());
        assertEquals(handler, typeWithTypeHandler.getTypeHandler());
        assertFalse(typeWithTypeHandler.isAsStatic());
    }

    @Test
    public void testWithContentTypeHandler() {
        Class<?> rawType = Collection.class;
        TypeBindings bindings = TypeBindings.emptyBindings();
        JavaType superClass = null;
        JavaType[] superInts = null;
        JavaType elemT = null;

        CollectionLikeType collectionLikeType = new CollectionLikeType(rawType, bindings, superClass, superInts, elemT, null, null, false);
        Object handler = new Object();

        CollectionLikeType typeWithContentTypeHandler = collectionLikeType.withContentTypeHandler(handler);

        assertEquals(rawType, typeWithContentTypeHandler.getRawType());
        assertEquals(bindings, typeWithContentTypeHandler.getBindings());
        assertEquals(superClass, typeWithContentTypeHandler.getSuperClass());
        assertEquals(superInts, typeWithContentTypeHandler.getInterfaces());
        assertNotNull(typeWithContentTypeHandler.getElementType().getTypeHandler());
        assertFalse(typeWithContentTypeHandler.isAsStatic());
    }

    @Test
    public void testWithContentValueHandler() {
        Class<?> rawType = Collection.class;
        TypeBindings bindings = TypeBindings.emptyBindings();
        JavaType superClass = null;
        JavaType[] superInts = null;
        JavaType elemT = null;

        CollectionLikeType collectionLikeType = new CollectionLikeType(rawType, bindings, superClass, superInts, elemT, null, null, false);
        Object handler = new Object();

        CollectionLikeType typeWithContentValueHandler = collectionLikeType.withContentValueHandler(handler);

        assertEquals(rawType, typeWithContentValueHandler.getRawType());
        assertEquals(bindings, typeWithContentValueHandler.getBindings());
        assertEquals(superClass, typeWithContentValueHandler.getSuperClass());
        assertEquals(superInts, typeWithContentValueHandler.getInterfaces());
        assertNotNull(typeWithContentValueHandler.getElementType().getValueHandler());
        assertFalse(typeWithContentValueHandler.isAsStatic());
    }

    @Test
    public void testWithStaticTyping() {
        Class<?> rawType = Collection.class;
        TypeBindings bindings = TypeBindings.emptyBindings();
        JavaType superClass = null;
        JavaType[] superInts = null;
        JavaType elemT = null;

        CollectionLikeType collectionLikeType = new CollectionLikeType(rawType, bindings, superClass, superInts, elemT, null, null, false);

        assertTrue(collectionLikeType.isAsStatic());

        CollectionLikeType typeWithStaticTyping = collectionLikeType.withStaticTyping();

        assertEquals(rawType, typeWithStaticTyping.getRawType());
        assertEquals(bindings, typeWithStaticTyping.getBindings());
        assertEquals(superClass, typeWithStaticTyping.getSuperClass());
        assertEquals(superInts, typeWithStaticTyping.getInterfaces());
        assertNotNull(typeWithStaticTyping.getElementType().getValueHandler());
        assertTrue(typeWithStaticTyping.isAsStatic());
    }

    @Test
    public void testRefine() {
        Class<?> rawType = Collection.class;
        TypeBindings bindings = TypeBindings.emptyBindings();
        JavaType superClass = null;
        JavaType[] superInts = null;
        JavaType elemT = null;

        CollectionLikeType collectionLikeType = new CollectionLikeType(rawType, bindings, superClass, superInts, elemT, null, null, false);
        JavaType refinedType = new JavaType();

        JavaType refinedCollectionLikeType = collectionLikeType.refine(rawType, bindings, superClass, superInts);

        assertEquals(rawType, refinedCollectionLikeType.getRawType());
        assertEquals(bindings, refinedCollectionLikeType.getBindings());
        assertEquals(superClass, refinedCollectionLikeType.getSuperClass());
        assertEquals(superInts, refinedCollectionLikeType.getInterfaces());
        assertEquals(elemT, refinedCollectionLikeType.getElementType());
        assertFalse(refinedCollectionLikeType.isAsStatic());
    }

    @Test
    public void testIsTrueCollectionType() {
        Class<?> rawType = Collection.class;
        TypeBindings bindings = TypeBindings.emptyBindings();
        JavaType superClass = null;
        JavaType[] superInts = null;
        JavaType elemT = null;

        CollectionLikeType collectionLikeType = new CollectionLikeType(rawType, bindings, superClass, superInts, elemT, null, null, false);
        boolean isTrueCollectionType = collectionLikeType.isTrueCollectionType();

        assertTrue(isTrueCollectionType);
    }

    @Test
    public void testEquals() {
        Class<?> rawType = Collection.class;
        TypeBindings bindings = TypeBindings.emptyBindings();
        JavaType superClass = null;
        JavaType[] superInts = null;
        JavaType elemT = null;

        CollectionLikeType collectionLikeType1 = new CollectionLikeType(rawType, bindings, superClass, superInts, elemT, null, null, false);
        CollectionLikeType collectionLikeType2 = new CollectionLikeType(rawType, bindings, superClass, superInts, elemT, null, null, false);

        assertTrue(collectionLikeType1.equals(collectionLikeType2));
    }

    @Test
    public void testToString() {
        Class<?> rawType = Collection.class;
        TypeBindings bindings = TypeBindings.emptyBindings();
        JavaType superClass = null;
        JavaType[] superInts = null;
        JavaType elemT = null;

        CollectionLikeType collectionLikeType = new CollectionLikeType(rawType, bindings, superClass, superInts, elemT, null, null, false);
        String expected = "[collection-like type; class java.util.Collection, contains null]";
        String actual = collectionLikeType.toString();

        assertEquals(expected, actual);
    }
}
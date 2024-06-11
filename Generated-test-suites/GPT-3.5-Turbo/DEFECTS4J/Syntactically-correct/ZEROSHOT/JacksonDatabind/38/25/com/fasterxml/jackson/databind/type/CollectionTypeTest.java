package com.fasterxml.jackson.databind.type;

import org.junit.Test;
import org.junit.Assert;

public class CollectionTypeTest {

    @Test
    public void testConstruct() {
        Class<?> rawType = ArrayList.class;
        TypeBindings bindings = null;
        JavaType superClass = null;
        JavaType[] superInts = null;
        JavaType elemT = String.class;
        CollectionType collectionType = CollectionType.construct(rawType, bindings, superClass, superInts, elemT);
        Assert.assertNotNull(collectionType);
        Assert.assertEquals(rawType, collectionType.getRawClass());
        Assert.assertEquals(bindings, collectionType.getBindings());
        Assert.assertEquals(superClass, collectionType.getSuperClass());
        Assert.assertEquals(superInts, collectionType.getInterfaces().toArray());
        Assert.assertEquals(elemT, collectionType.getContentType());
    }

    @Test
    public void testConstructDeprecated() {
        Class<?> rawType = ArrayList.class;
        JavaType elemT = String.class;
        CollectionType collectionType = CollectionType.construct(rawType, elemT);
        Assert.assertNotNull(collectionType);
        Assert.assertEquals(rawType, collectionType.getRawClass());
        Assert.assertNull(collectionType.getBindings());
        Assert.assertNull(collectionType.getSuperClass());
        Assert.assertNull(collectionType.getInterfaces().toArray());
        Assert.assertEquals(elemT, collectionType.getContentType());
    }

    @Test
    public void testNarrow() {
        Class<?> subclass = ArrayList.class;
        CollectionType collectionType = new CollectionType(subclass, null, null, null, null, null, null, false);
        JavaType narrowedType = collectionType.narrow(subclass);
        Assert.assertNotNull(narrowedType);
        Assert.assertEquals(subclass, narrowedType.getRawClass());
        Assert.assertEquals(collectionType.getBindings(), narrowedType.getBindings());
        Assert.assertEquals(collectionType.getSuperClass(), narrowedType.getSuperClass());
        Assert.assertEquals(collectionType.getInterfaces(), narrowedType.getInterfaces());
        Assert.assertEquals(collectionType.getContentType(), narrowedType.getContentType());
    }

    @Test
    public void testWithContentType() {
        Class<?> rawType = ArrayList.class;
        TypeBindings bindings = null;
        JavaType superClass = null;
        JavaType[] superInts = null;
        JavaType elemT = String.class;
        CollectionType collectionType = CollectionType.construct(rawType, bindings, superClass, superInts, elemT);
        JavaType contentType = String.class;
        CollectionType newCollectionType = collectionType.withContentType(contentType);
        Assert.assertNotNull(newCollectionType);
        Assert.assertEquals(rawType, newCollectionType.getRawClass());
        Assert.assertEquals(bindings, newCollectionType.getBindings());
        Assert.assertEquals(superClass, newCollectionType.getSuperClass());
        Assert.assertEquals(superInts, newCollectionType.getInterfaces().toArray());
        Assert.assertEquals(contentType, newCollectionType.getContentType());
    }

    @Test
    public void testWithTypeHandler() {
        Class<?> rawType = ArrayList.class;
        TypeBindings bindings = null;
        JavaType superClass = null;
        JavaType[] superInts = null;
        JavaType elemT = String.class;
        CollectionType collectionType = CollectionType.construct(rawType, bindings, superClass, superInts, elemT);
        Object typeHandler = new TestTypeHandler();
        CollectionType newCollectionType = collectionType.withTypeHandler(typeHandler);
        Assert.assertNotNull(newCollectionType);
        Assert.assertEquals(rawType, newCollectionType.getRawClass());
        Assert.assertEquals(bindings, newCollectionType.getBindings());
        Assert.assertEquals(superClass, newCollectionType.getSuperClass());
        Assert.assertEquals(superInts, newCollectionType.getInterfaces().toArray());
        Assert.assertEquals(elemT, newCollectionType.getContentType());
        Assert.assertEquals(typeHandler, newCollectionType.getTypeHandler());
    }

    @Test
    public void testWithContentTypeHandler() {
        Class<?> rawType = ArrayList.class;
        TypeBindings bindings = null;
        JavaType superClass = null;
        JavaType[] superInts = null;
        JavaType elemT = String.class;
        CollectionType collectionType = CollectionType.construct(rawType, bindings, superClass, superInts, elemT);
        Object typeHandler = new TestTypeHandler();
        CollectionType newCollectionType = collectionType.withContentTypeHandler(typeHandler);
        Assert.assertNotNull(newCollectionType);
        Assert.assertEquals(rawType, newCollectionType.getRawClass());
        Assert.assertEquals(bindings, newCollectionType.getBindings());
        Assert.assertEquals(superClass, newCollectionType.getSuperClass());
        Assert.assertEquals(superInts, newCollectionType.getInterfaces().toArray());
        Assert.assertEquals(elemT.withTypeHandler(typeHandler), newCollectionType.getContentType());
        Assert.assertEquals(typeHandler, newCollectionType.getContentType().getTypeHandler());
    }

    @Test
    public void testWithValueHandler() {
        Class<?> rawType = ArrayList.class;
        TypeBindings bindings = null;
        JavaType superClass = null;
        JavaType[] superInts = null;
        JavaType elemT = String.class;
        CollectionType collectionType = CollectionType.construct(rawType, bindings, superClass, superInts, elemT);
        Object valueHandler = new TestValueHandler();
        CollectionType newCollectionType = collectionType.withValueHandler(valueHandler);
        Assert.assertNotNull(newCollectionType);
        Assert.assertEquals(rawType, newCollectionType.getRawClass());
        Assert.assertEquals(bindings, newCollectionType.getBindings());
        Assert.assertEquals(superClass, newCollectionType.getSuperClass());
        Assert.assertEquals(superInts, newCollectionType.getInterfaces().toArray());
        Assert.assertEquals(elemT, newCollectionType.getContentType());
        Assert.assertEquals(valueHandler, newCollectionType.getValueHandler());
    }

    @Test
    public void testWithContentValueHandler() {
        Class<?> rawType = ArrayList.class;
        TypeBindings bindings = null;
        JavaType superClass = null;
        JavaType[] superInts = null;
        JavaType elemT = String.class;
        CollectionType collectionType = CollectionType.construct(rawType, bindings, superClass, superInts, elemT);
        Object valueHandler = new TestValueHandler();
        CollectionType newCollectionType = collectionType.withContentValueHandler(valueHandler);
        Assert.assertNotNull(newCollectionType);
        Assert.assertEquals(rawType, newCollectionType.getRawClass());
        Assert.assertEquals(bindings, newCollectionType.getBindings());
        Assert.assertEquals(superClass, newCollectionType.getSuperClass());
        Assert.assertEquals(superInts, newCollectionType.getInterfaces().toArray());
        Assert.assertEquals(elemT.withValueHandler(valueHandler), newCollectionType.getContentType());
        Assert.assertEquals(valueHandler, newCollectionType.getContentType().getValueHandler());
    }

    @Test
    public void testWithStaticTyping() {
        Class<?> rawType = ArrayList.class;
        TypeBindings bindings = null;
        JavaType superClass = null;
        JavaType[] superInts = null;
        JavaType elemT = String.class;
        CollectionType collectionType = CollectionType.construct(rawType, bindings, superClass, superInts, elemT);
        CollectionType newCollectionType = collectionType.withStaticTyping();
        Assert.assertNotNull(newCollectionType);
        Assert.assertEquals(rawType, newCollectionType.getRawClass());
        Assert.assertEquals(bindings, newCollectionType.getBindings());
        Assert.assertEquals(superClass, newCollectionType.getSuperClass());
        Assert.assertEquals(superInts, newCollectionType.getInterfaces().toArray());
        Assert.assertEquals(elemT.withStaticTyping(), newCollectionType.getContentType());
        Assert.assertTrue(newCollectionType.isStatic());
    }

    @Test
    public void testRefine() {
        Class<?> rawType = ArrayList.class;
        TypeBindings bindings = null;
        JavaType superClass = null;
        JavaType[] superInts = null;
        JavaType elemT = String.class;
        CollectionType collectionType = CollectionType.construct(rawType, bindings, superClass, superInts, elemT);

        Class<?> newRawType = LinkedList.class;
        TypeBindings newBindings = null;
        JavaType newSuperClass = null;
        JavaType[] newSuperInts = null;
        JavaType newElemT = Integer.class;

        CollectionType newCollectionType = (CollectionType) collectionType.refine(newRawType, newBindings, newSuperClass, newSuperInts);
        Assert.assertNotNull(newCollectionType);
        Assert.assertEquals(newRawType, newCollectionType.getRawClass());
        Assert.assertEquals(newBindings, newCollectionType.getBindings());
        Assert.assertEquals(newSuperClass, newCollectionType.getSuperClass());
        Assert.assertEquals(newSuperInts, newCollectionType.getInterfaces().toArray());
        Assert.assertEquals(newElemT, newCollectionType.getContentType());
    }

    @Test
    public void testToString() {
        Class<?> rawType = ArrayList.class;
        TypeBindings bindings = null;
        JavaType superClass = null;
        JavaType[] superInts = null;
        JavaType elemT = String.class;
        CollectionType collectionType = CollectionType.construct(rawType, bindings, superClass, superInts, elemT);
        String expectedString = "[collection type; class " + rawType.getName() + ", contains " + elemT + "]";
        Assert.assertEquals(expectedString, collectionType.toString());
    }
}
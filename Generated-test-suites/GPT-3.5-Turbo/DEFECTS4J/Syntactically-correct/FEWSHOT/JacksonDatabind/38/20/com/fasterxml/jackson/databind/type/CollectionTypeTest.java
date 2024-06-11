package com.fasterxml.jackson.databind.type;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class CollectionTypeTest {

    @Test
    public void testConstruct() {
        Class<?> rawType = SomeCollection.class;
        TypeBindings bindings = new TypeBindings();
        JavaType superClass = new SimpleType();
        JavaType[] superInts = new JavaType[]{new SimpleType()};
        JavaType elemT = new SimpleType();

        CollectionType collectionType = CollectionType.construct(rawType, bindings, superClass, superInts, elemT);

        assertSame(rawType, collectionType.getRawClass());
        assertEquals(bindings, collectionType.getBindings());
        assertSame(superClass, collectionType.getSuperClass());
        assertSame(superInts, collectionType.getInterfaces());
        assertSame(elemT, collectionType.getElementType());
    }

    @Test
    public void testNarrow() {
        Class<?> subclass = SomeCollection.class;

        CollectionType collectionType = new CollectionType();
        JavaType narrowedType = collectionType._narrow(subclass);

        assertEquals(subclass, narrowedType.getRawClass());
        assertSame(collectionType.getBindings(), narrowedType.getBindings());
        assertSame(collectionType.getSuperClass(), narrowedType.getSuperClass());
        assertSame(collectionType.getInterfaces(), narrowedType.getInterfaces());
        assertSame(collectionType.getElementType(), narrowedType.getElementType());
    }

    @Test
    public void testWithContentType() {
        JavaType contentType = new SimpleType();

        CollectionType collectionType = new CollectionType();
        JavaType modifiedType = collectionType.withContentType(contentType);

        assertEquals(collectionType.getRawClass(), modifiedType.getRawClass());
        assertEquals(collectionType.getBindings(), modifiedType.getBindings());
        assertEquals(collectionType.getSuperClass(), modifiedType.getSuperClass());
        assertEquals(collectionType.getInterfaces(), modifiedType.getInterfaces());
        assertSame(contentType, modifiedType.getElementType());
    }

    @Test
    public void testWithTypeHandler() {
        Object h = new Object();

        CollectionType collectionType = new CollectionType();
        CollectionType modifiedType = collectionType.withTypeHandler(h);

        assertEquals(collectionType.getRawClass(), modifiedType.getRawClass());
        assertEquals(collectionType.getBindings(), modifiedType.getBindings());
        assertEquals(collectionType.getSuperClass(), modifiedType.getSuperClass());
        assertEquals(collectionType.getInterfaces(), modifiedType.getInterfaces());
        assertSame(collectionType.getElementType(), modifiedType.getElementType());
        assertSame(h, modifiedType.getTypeHandler());
    }

    @Test
    public void testWithContentTypeHandler() {
        Object h = new Object();

        CollectionType collectionType = new CollectionType();
        CollectionType modifiedType = collectionType.withContentTypeHandler(h);

        assertEquals(collectionType.getRawClass(), modifiedType.getRawClass());
        assertEquals(collectionType.getBindings(), modifiedType.getBindings());
        assertEquals(collectionType.getSuperClass(), modifiedType.getSuperClass());
        assertEquals(collectionType.getInterfaces(), modifiedType.getInterfaces());
        assertEquals(collectionType.getElementType().withTypeHandler(h), modifiedType.getElementType());
        assertSame(collectionType.getTypeHandler(), modifiedType.getTypeHandler());
    }

    @Test
    public void testWithValueHandler() {
        Object h = new Object();

        CollectionType collectionType = new CollectionType();
        CollectionType modifiedType = collectionType.withValueHandler(h);

        assertEquals(collectionType.getRawClass(), modifiedType.getRawClass());
        assertEquals(collectionType.getBindings(), modifiedType.getBindings());
        assertEquals(collectionType.getSuperClass(), modifiedType.getSuperClass());
        assertEquals(collectionType.getInterfaces(), modifiedType.getInterfaces());
        assertSame(collectionType.getElementType(), modifiedType.getElementType());
        assertSame(h, modifiedType.getValueHandler());
    }

    @Test
    public void testWithContentValueHandler() {
        Object h = new Object();

        CollectionType collectionType = new CollectionType();
        CollectionType modifiedType = collectionType.withContentValueHandler(h);

        assertEquals(collectionType.getRawClass(), modifiedType.getRawClass());
        assertEquals(collectionType.getBindings(), modifiedType.getBindings());
        assertEquals(collectionType.getSuperClass(), modifiedType.getSuperClass());
        assertEquals(collectionType.getInterfaces(), modifiedType.getInterfaces());
        assertEquals(collectionType.getElementType().withValueHandler(h), modifiedType.getElementType());
        assertSame(collectionType.getValueHandler(), modifiedType.getValueHandler());
    }

    @Test
    public void testWithStaticTyping() {
        CollectionType collectionType = new CollectionType();
        CollectionType modifiedType = collectionType.withStaticTyping();

        assertEquals(collectionType.getRawClass(), modifiedType.getRawClass());
        assertEquals(collectionType.getBindings(), modifiedType.getBindings());
        assertEquals(collectionType.getSuperClass(), modifiedType.getSuperClass());
        assertEquals(collectionType.getInterfaces(), modifiedType.getInterfaces());
        assertEquals(collectionType.getElementType().withStaticTyping(), modifiedType.getElementType());
        assertTrue(modifiedType.isStatic());
    }

    @Test
    public void testRefine() {
        Class<?> rawType = SomeCollection.class;
        TypeBindings bindings = new TypeBindings();
        JavaType superClass = new SimpleType();
        JavaType[] superInterfaces = new JavaType[]{new SimpleType()};
        JavaType elemT = new SimpleType();

        CollectionType collectionType = new CollectionType();
        JavaType refinedType = collectionType.refine(rawType, bindings, superClass, superInterfaces);

        assertSame(rawType, refinedType.getRawClass());
        assertSame(bindings, refinedType.getBindings());
        assertSame(superClass, refinedType.getSuperClass());
        assertSame(superInterfaces, refinedType.getInterfaces());
        assertSame(collectionType.getElementType(), refinedType.getElementType());
        assertSame(collectionType.getValueHandler(), refinedType.getValueHandler());
    }

    @Test
    public void testToString() {
        CollectionType collectionType = new CollectionType();
        collectionType.setRawClass(SomeCollection.class);
        collectionType.setElementType(new SomeType());

        assertEquals("[collection type; class SomeCollection, contains SomeType]", collectionType.toString());
    }
    
    private class SomeCollection {
    }

    private class SomeType {
    }
}
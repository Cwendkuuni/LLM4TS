package com.fasterxml.jackson.databind.type;

import static org.junit.Assert.*;

import org.junit.Test;

public class CollectionLikeTypeTest {

    @Test
    public void testConstruct() {
        Class<?> rawType = Collection.class;
        TypeBindings bindings = TypeBindings.emptyBindings();
        JavaType superClass = _bogusSuperClass(rawType);
        JavaType[] superInts = new JavaType[0];
        JavaType elemT = JavaType.constructType(int.class);
        CollectionLikeType clt = CollectionLikeType.construct(rawType, bindings, superClass, superInts, elemT);
        assertEquals(rawType, clt.getRawClass());
        assertEquals(bindings, clt.getBindings());
        assertEquals(superClass, clt.getSuperclass());
        assertEquals(superInts, clt.getSuperInterfaces());
        assertEquals(elemT, clt.getElementType());
        assertEquals(false, clt.isStatic());
    }

    @Test
    public void testConstructWithTypeBindings() {
        Class<?> rawType = ArrayList.class;
        TypeBindings bindings = TypeBindings.create(rawType, Integer.class);
        JavaType superClass = _bogusSuperClass(rawType);
        JavaType[] superInts = new JavaType[0];
        JavaType elemT = JavaType.constructType(Integer.class);
        CollectionLikeType clt = CollectionLikeType.construct(rawType, bindings, superClass, superInts, elemT);
        assertEquals(rawType, clt.getRawClass());
        assertEquals(bindings, clt.getBindings());
        assertEquals(superClass, clt.getSuperclass());
        assertEquals(superInts, clt.getSuperInterfaces());
        assertEquals(elemT, clt.getElementType());
        assertEquals(false, clt.isStatic());
    }

    @Test
    public void testUpgradeFrom() {
        JavaType baseType = JavaType.constructType(ArrayList.class);
        JavaType elemT = JavaType.constructType(Integer.class);
        CollectionLikeType clt = CollectionLikeType.upgradeFrom(baseType, elemT);
        assertEquals(baseType.getRawClass(), clt.getRawClass());
        assertEquals(elemT, clt.getElementType());
        assertTrue(clt instanceof CollectionLikeType);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testUpgradeFromInvalidBaseType() {
        JavaType baseType = JavaType.constructType(String.class);
        JavaType elemT = JavaType.constructType(Integer.class);
        CollectionLikeType.upgradeFrom(baseType, elemT);
    }

    @Test
    public void testIsContainerType() {
        CollectionLikeType clt = new CollectionLikeType(ArrayList.class, TypeBindings.emptyBindings(), _bogusSuperClass(ArrayList.class), null, JavaType.constructType(Integer.class), null, null, false);
        assertTrue(clt.isContainerType());
        assertTrue(clt.isCollectionLikeType());
    }

    @Test
    public void testIsTrueCollectionType() {
        CollectionLikeType clt = new CollectionLikeType(ArrayList.class, TypeBindings.emptyBindings(), _bogusSuperClass(ArrayList.class), null, JavaType.constructType(Integer.class), null, null, false);
        assertTrue(clt.isTrueCollectionType());

        JavaType listType = JavaType.constructType(LinkedList.class);
        CollectionLikeType linkedListType = new CollectionLikeType(listType, TypeBindings.emptyBindings(), _bogusSuperClass(listType), null, JavaType.constructType(Integer.class), null, null, false);
        assertFalse(linkedListType.isTrueCollectionType());
    }

    @Test
    public void testEquals() {
        Class<?> rawType1 = ArrayList.class;
        Class<?> rawType2 = LinkedList.class;
        TypeBindings bindings1 = TypeBindings.emptyBindings();
        TypeBindings bindings2 = TypeBindings.emptyBindings();
        JavaType superClass = _bogusSuperClass(rawType1);
        JavaType[] superInts1 = new JavaType[0];
        JavaType[] superInts2 = new JavaType[0];
        JavaType elemT1 = JavaType.constructType(Integer.class);
        JavaType elemT2 = JavaType.constructType(Integer.class);

        CollectionLikeType clt1 = new CollectionLikeType(rawType1, bindings1, superClass, superInts1, elemT1, null, null, false);
        CollectionLikeType clt2 = new CollectionLikeType(rawType2, bindings2, superClass, superInts2, elemT2, null, null, false);

        assertFalse(clt1.equals(clt2));

        CollectionLikeType clt3 = new CollectionLikeType(rawType1, bindings1, superClass, superInts1, elemT1, null, null, false);
        assertTrue(clt1.equals(clt3));
    }

    @Test
    public void testToString() {
        Class<?> rawType = ArrayList.class;
        TypeBindings bindings = TypeBindings.emptyBindings();
        JavaType superClass = _bogusSuperClass(rawType);
        JavaType[] superInts = new JavaType[0];
        JavaType elemT = JavaType.constructType(Integer.class);
        CollectionLikeType clt = new CollectionLikeType(rawType, bindings, superClass, superInts, elemT, null, null, false);
        String expected = "[collection-like type; class " + rawType.getName() + ", contains " + elemT + "]";
        assertEquals(expected, clt.toString());
    }

    private static Class<?> _bogusSuperClass(Class<?> clazz) {
        return Object.class;
    }
}
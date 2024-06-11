package com.example.junit;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import java.lang.reflect.TypeVariable;
import java.util.Collections;

import org.junit.Test;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.CollectionLikeType;
import com.fasterxml.jackson.databind.type.TypeBase;
import com.fasterxml.jackson.databind.type.TypeBindings;

/**
 * Test for CollectionLikeType class
 */
public class CollectionLikeTypeTest {

    @Test
    public void testConstructorWithClassAndBindingsAndSuperClassAndSuperInterfacesAndElementT() {
        JavaType elemType = JavaType.construct(int[].class);
        TypeBindings bindings = TypeBindings.emptyBindings();
        JavaType superClass = JavaType.construct(Collection.class);
        JavaType[] superInterfaces = new JavaType[] { JavaType.construct(Iterable.class) };
        CollectionLikeType collectionLikeType = CollectionLikeType.construct(List.class, bindings, superClass, superInterfaces, elemType);
        assertEquals(List.class, collectionLikeType.getClass());
        assertEquals(elemType, collectionLikeType.getElementType());
        assertEquals(bindings, collectionLikeType.getBindings());
        assertEquals(superClass, collectionLikeType.getSuperclass());
        assertArrayEquals(superInterfaces, collectionLikeType.getSuperInterfaces());
    }

    @Test
    public void testConstructorWithTypeBaseAndElementT() {
        JavaType elemType = JavaType.construct(int[].class);
        TypeBase base = new TypeBase(List.class, null, null, 0, null, null, false);
        CollectionLikeType collectionLikeType = new CollectionLikeType(base, elemType);
        assertEquals(List.class, collectionLikeType.getClass());
        assertEquals(elemType, collectionLikeType.getElementType());
        assertNull(collectionLikeType.getBindings());
        assertNull(collectionLikeType.getSuperclass());
        assertArrayEquals(new JavaType[0], collectionLikeType.getSuperInterfaces());
    }

    @Test
    public void testConstructWithNullClassAndElementT() {
        JavaType elemType = JavaType.construct(int[].class);
        assertThrows(IllegalArgumentException.class, () -> CollectionLikeType.construct(null, elemType));
    }

    @Test
    public void testConstructWithNullElementT() {
        assertThrows(IllegalArgumentException.class, () -> CollectionLikeType.construct(List.class, null));
    }

    @Test
    public void testUpgradeFrom() {
        JavaType baseType = JavaType.construct(ArrayList.class);
        JavaType elemType = JavaType.construct(int[].class);
        CollectionLikeType collectionLikeType = CollectionLikeType.upgradeFrom(baseType, elemType);
        assertEquals(ArrayList.class, collectionLikeType.getClass());
        assertEquals(elemType, collectionLikeType.getElementType());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testUpgradeFromWithNonTypeBase() {
        JavaType baseType = JavaType.construct(String.class);
        JavaType elemType = JavaType.construct(int[].class);
        CollectionLikeType.upgradeFrom(baseType, elemType);
    }

    @Test
    public void testNarrow() {
        JavaType baseType = JavaType.construct(ArrayList.class);
        JavaType subclass = JavaType.construct(ArrayList.class);
        CollectionLikeType collectionLikeType = new CollectionLikeType(baseType, null, null, null, null, null, null, false);
        JavaType narrowed = collectionLikeType.narrow(subclass);
        assertEquals(subclass, narrowed);
    }

    @Test
    public void testWithContentType() {
        JavaType elemType = JavaType.construct(int[].class);
        JavaType contentType = JavaType.construct(String.class);
        CollectionLikeType collectionLikeType = new CollectionLikeType(null, elemType, null, null, null, null, null, false);
        CollectionLikeType newCollectionLikeType = collectionLikeType.withContentType(contentType);
        assertEquals(collectionLikeType.getClass(), newCollectionLikeType.getClass());
        assertEquals(contentType, newCollectionLikeType.getElementType());
    }

    @Test
    public void testWithTypeHandler() {
        JavaType elemType = JavaType.construct(int[].class);
        Object handler = new Object() {};
        CollectionLikeType collectionLikeType = new CollectionLikeType(null, elemType, null, null, null, null, null, false);
        CollectionLikeType newCollectionLikeType = collectionLikeType.withTypeHandler(handler);
        assertEquals(collectionLikeType.getClass(), newCollectionLikeType.getClass());
        assertNotNull(newCollectionLikeType.getTypeHandler());
    }

    @Test
    public void testWithContentTypeHandler() {
        JavaType elemType = JavaType.construct(int[].class);
        Object handler = new Object() {};
        CollectionLikeType collectionLikeType = new CollectionLikeType(null, elemType, null, null, null, null, null, false);
        CollectionLikeType newCollectionLikeType = collectionLikeType.withContentTypeHandler(handler);
        assertEquals(collectionLikeType.getClass(), newCollectionLikeType.getClass());
        assertNotNull(newCollectionLikeType.getValueHandler());
    }

    @Test
    public void testWithValueHandler() {
        JavaType elemType = JavaType.construct(int[].class);
        Object handler = new Object() {};
        CollectionLikeType collectionLikeType = new CollectionLikeType(null, elemType, null, null, null, null, null, false);
        CollectionLikeType newCollectionLikeType = collectionLikeType.withValueHandler(handler);
        assertEquals(collectionLikeType.getClass(), newCollectionLikeType.getClass());
        assertNotNull(newCollectionLikeType.getValueHandler());
    }

    @Test
    public void testWithContentValueHandler() {
        JavaType elemType = JavaType.construct(int[].class);
        Object handler = new Object() {};
        CollectionLikeType collectionLikeType = new CollectionLikeType(null, elemType, null, null, null, null, null, false);
        CollectionLikeType newCollectionLikeType = collectionLikeType.withContentValueHandler(handler);
        assertEquals(collectionLikeType.getClass(), newCollectionLikeType.getClass());
        assertNotNull(newCollectionLikeType.getValueHandler());
    }

    @Test
    public void testWithStaticTyping() {
        JavaType elemType = JavaType.construct(int[].class);
        CollectionLikeType collectionLikeType = new CollectionLikeType(null, elemType, null, null, null, null, null, false);
        CollectionLikeType newCollectionLikeType = collectionLikeType.withStaticTyping();
        assertTrue(newCollectionLikeType.isStatic());
        assertEquals(collectionLikeType.getClass(), newCollectionLikeType.getClass());
    }

    @Test
    public void testRefine() {
        JavaType elemType = JavaType.construct(int[].class);
        Class<? extends Collection<Integer>> rawType = ArrayList.class;
        TypeBindings bindings = TypeBindings.emptyBindings();
        JavaType superClass = JavaType.construct(Collection.class);
        JavaType[] superInterfaces = new JavaType[] { JavaType.construct(Iterable.class) };
        CollectionLikeType collectionLikeType = new CollectionLikeType(null, elemType, null, null, null, null, null, false);
        CollectionLikeType newCollectionLikeType = collectionLikeType.refine(rawType, bindings, superClass, superInterfaces);
        assertEquals(rawType, newCollectionLikeType.getClass());
        assertEquals(elemType, newCollectionLikeType.getElementType());
    }

    @Test
    public void testIsContainerType() {
        JavaType elemType = JavaType.construct(int[].class);
        CollectionLikeType collectionLikeType = new CollectionLikeType(null, elemType, null, null, null, null, null, false);
        assertTrue(collectionLikeType.isContainerType());
    }

    @Test
    public void testIsCollectionLikeType() {
        JavaType elemType = JavaType.construct(int[].class);
        CollectionLikeType collectionLikeType = new CollectionLikeType(null, elemType, null, null, null, null, null, false);
        assertTrue(collectionLikeType.isCollectionLikeType());
    }

    @Test
    public void testGetContentType() {
        JavaType elemType = JavaType.construct(int[].class);
        CollectionLikeType collectionLikeType = new CollectionLikeType(null, elemType, null, null, null, null, null, false);
        assertEquals(elemType, collectionLikeType.getContentType());
    }

    @Test
    public void testGetContentValueHandler() {
        JavaType elemType = JavaType.construct(int[].class);
        Object handler = new Object() {};
        CollectionLikeType collectionLikeType = new CollectionLikeType(null, elemType, null, null, null, handler, null, false);
        assertEquals(handler, collectionLikeType.getContentValueHandler());
    }

    @Test
    public void testGetContentTypeHandler() {
        JavaType elemType = JavaType.construct(int[].class);
        Object handler = new Object() {};
        CollectionLikeType collectionLikeType = new CollectionLikeType(null, elemType, null, null, null, null, handler, false);
        assertEquals(handler, collectionLikeType.getContentTypeHandler());
    }

    @Test
    public void testHasHandlers() {
        JavaType elemType = JavaType.construct(int[].class);
        CollectionLikeType collectionLikeType = new CollectionLikeType(null, elemType, null, null, null, null, null, false);
        assertFalse(collectionLikeType.hasHandlers());

        Object handler = new Object() {};
        CollectionLikeType collectionLikeTypeWithHandler = new CollectionLikeType(null, elemType, null, null, null, null, handler, false);
        assertTrue(collectionLikeTypeWithHandler.hasHandlers());
    }

    @Test
    public void testGetErasedSignature() {
        JavaType elemType = JavaType.construct(int[].class);
        CollectionLikeType collectionLikeType = new CollectionLikeType(List.class, null, null, null, elemType, null, null, false);
        StringBuilder expected = new StringBuilder();
        expected.append("[class java.util.List, java.lang.Object]");
        assertEquals(expected.toString(), collectionLikeType.getErasedSignature(new StringBuilder()).toString());
    }

    @Test
    public void testGetGenericSignature() {
        JavaType elemType = JavaType.construct(int[].class);
        CollectionLikeType collectionLikeType = new CollectionLikeType(List.class, null, null, null, elemType, null, null, false);
        StringBuilder expected = new StringBuilder();
        expected.append("[java.util.List<");
        elemType.getGenericSignature(expected);
        expected.append(">]");
        assertEquals(expected.toString(), collectionLikeType.getGenericSignature(new StringBuilder()).toString());
    }

    @Test
    public void testBuildCanonicalName() {
        JavaType elemType = JavaType.construct(int[].class);
        CollectionLikeType collectionLikeType = new CollectionLikeType(List.class, null, null, null, elemType, null, null, false);
        assertEquals("[collection-like type; class java.util.List, contains class [I]]", collectionLikeType.buildCanonicalName());
    }

    @Test
    public void testIsTrueCollectionType() {
        JavaType elemType = JavaType.construct(int[].class);
        CollectionLikeType collectionLikeType = new CollectionLikeType(List.class, null, null, null, elemType, null, null, false);
        assertTrue(collectionLikeType.isTrueCollectionType());

        JavaType elemTypeList = JavaType.construct(List.class);
        CollectionLikeType collectionLikeTypeList = new CollectionLikeType(elemTypeList, null, null, null, null, null, null, false);
        assertTrue(collectionLikeTypeList.isTrueCollectionType());

        Class<?> rawType = ArrayList.class;
        CollectionLikeType collectionLikeTypeRawType = new CollectionLikeType(rawType, null, null, null, null, null, null, false);
        assertTrue(collectionLikeTypeRawType.isTrueCollectionType());
    }

    @Test
    public void testEquals() {
        JavaType elemType = JavaType.construct(int[].class);
        CollectionLikeType collectionLikeType1 = new CollectionLikeType(List.class, null, null, null, elemType, null, null, false);
        CollectionLikeType collectionLikeType2 = new CollectionLikeType(ArrayList.class, null, null, null, elemType, null, null, false);
        CollectionLikeType collectionLikeType3 = new CollectionLikeType(List.class, null, null, null, elemType, null, null, true);
        CollectionLikeType collectionLikeType4 = new CollectionLikeType(List.class, null, null, null, elemType, null, null, false);
        CollectionLikeType collectionLikeType5 = new CollectionLikeType(ArrayList.class, null, null, null, elemType, null, null, true);

        assertFalse(collectionLikeType1.equals(null));
        assertFalse(collectionLikeType1.equals(""));
        assertFalse(collectionLikeType1.equals(new Object()));
        assertTrue(collectionLikeType1.equals(collectionLikeType1));
        assertTrue(collectionLikeType1.equals(collectionLikeType2));
        assertFalse(collectionLikeType1.equals(collectionLikeType3));
        assertTrue(collectionLikeType1.equals(collectionLikeType4));
        assertFalse(collectionLikeType1.equals(collectionLikeType5));
    }

    @Test
    public void testToString() {
        JavaType elemType = JavaType.construct(int[].class);
        CollectionLikeType collectionLikeType = new CollectionLikeType(List.class, null, null, null, elemType, null, null, false);
        assertEquals("[collection-like type; class java.util.List, contains class [I]]", collectionLikeType.toString());
    }

    private void assertArrayEquals(JavaType[] expected, JavaType[] actual) {
        if (expected.length != actual.length) {
            throw new AssertionError("Array sizes do not match");
        }
        for (int i = 0; i < expected.length; i++) {
            assertEquals(expected[i], actual[i]);
        }
    }
}
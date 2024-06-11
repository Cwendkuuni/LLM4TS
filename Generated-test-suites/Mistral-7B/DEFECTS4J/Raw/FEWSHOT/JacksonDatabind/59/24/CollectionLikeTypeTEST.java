package com.fasterxml.jackson.databind.type;

import static org.junit.Assert.*;

import java.lang.reflect.TypeVariable;
import java.util.Collection;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.TypeBase;
import com.fasterxml.jackson.databind.TypeBindings;

public class CollectionLikeTypeTest {

    @Test
    public void testConstruct() {
        JavaType javaType = CollectionLikeType.construct(Collection.class, TypeBindings.emptyBindings(), null, null, null);
        assertNotNull(javaType);
        assertEquals(Collection.class, javaType.getRawClass());
        assertEquals(Collection.class.getGenericSuperclass(), javaType.getSuperClass());
        assertNull(javaType.getTypeParameters());
        assertEquals(JavaType.ClassType.COLLECTION_LIKE, javaType.getType());

        JavaType javaTypeWithElementType = CollectionLikeType.construct(ArrayList.class, TypeBindings.emptyBindings(), null, null, IntegerType.instance);
        assertNotNull(javaTypeWithElementType);
        assertEquals(ArrayList.class, javaTypeWithElementType.getRawClass());
        assertEquals(Collection.class.getGenericSuperclass(), javaTypeWithElementType.getSuperClass());
        assertArrayEquals(new TypeVariable<?>[]{TypeVariables.INT}, javaTypeWithElementType.getTypeParameters());
        assertEquals(JavaType.ClassType.COLLECTION_LIKE, javaTypeWithElementType.getType());
        assertEquals(IntegerType.instance, javaTypeWithElementType.getContentType());
    }

    @Test
    public void testUpgradeFrom() {
        JavaType javaType = JavaType.constructType(ArrayList.class);
        JavaType collectionLikeType = CollectionLikeType.upgradeFrom(javaType, IntegerType.instance);
        assertNotNull(collectionLikeType);
        assertEquals(ArrayList.class, collectionLikeType.getRawClass());
        assertEquals(CollectionLikeType.class, collectionLikeType.getRawClass().getSuperclass());
        assertArrayEquals(new TypeVariable<?>[]{TypeVariables.INT}, collectionLikeType.getTypeParameters());
        assertEquals(JavaType.ClassType.COLLECTION_LIKE, collectionLikeType.getType());
        assertEquals(IntegerType.instance, collectionLikeType.getContentType());
    }

    @Test
    public void testIsContainerType() {
        JavaType javaType = CollectionLikeType.construct(Collection.class, TypeBindings.emptyBindings(), null, null, null);
        assertTrue(javaType.isContainerType());

        JavaType javaTypeWithElementType = CollectionLikeType.construct(ArrayList.class, TypeBindings.emptyBindings(), null, null, IntegerType.instance);
        assertTrue(javaTypeWithElementType.isContainerType());
    }

    @Test
    public void testIsCollectionLikeType() {
        JavaType javaType = CollectionLikeType.construct(Collection.class, TypeBindings.emptyBindings(), null, null, null);
        assertTrue(javaType.isCollectionLikeType());

        JavaType javaTypeWithElementType = CollectionLikeType.construct(ArrayList.class, TypeBindings.emptyBindings(), null, null, IntegerType.instance);
        assertTrue(javaTypeWithElementType.isCollectionLikeType());
    }

    @Test
    public void testGetContentType() {
        JavaType javaTypeWithElementType = CollectionLikeType.construct(ArrayList.class, TypeBindings.emptyBindings(), null, null, IntegerType.instance);
        assertEquals(IntegerType.instance, javaTypeWithElementType.getContentType());
    }

    @Test
    public void testGetContentValueHandler() {
        JavaType javaTypeWithElementType = CollectionLikeType.construct(ArrayList.class, TypeBindings.emptyBindings(), null, null, IntegerType.instance);
        assertEquals(IntegerValueHandler.instance, javaTypeWithElementType.getContentValueHandler());
    }

    @Test
    public void testGetContentTypeHandler() {
        JavaType javaTypeWithElementType = CollectionLikeType.construct(ArrayList.class, TypeBindings.emptyBindings(), null, null, IntegerType.instance);
        assertEquals(IntegerTypeHandler.instance, javaTypeWithElementType.getContentTypeHandler());
    }

    @Test
    public void testHasHandlers() {
        JavaType javaTypeWithElementType = CollectionLikeType.construct(ArrayList.class, TypeBindings.emptyBindings(), null, null, IntegerType.instance);
        assertTrue(javaTypeWithElementType.hasHandlers());
    }

    @Test
    public void testIsTrueCollectionType() {
        JavaType javaType = CollectionLikeType.construct(ArrayList.class, TypeBindings.emptyBindings(), null, null, IntegerType.instance);
        assertTrue(javaType.isTrueCollectionType());

        JavaType javaTypeWithoutCollection = TypeBase.constructType(ArrayListWithoutCollection.class);
        assertFalse(javaTypeWithoutCollection.isTrueCollectionType());
    }

    @Test
    public void testGetErasedSignature() {
        JavaType javaTypeWithElementType = CollectionLikeType.construct(ArrayList.class, TypeBindings.emptyBindings(), null, null, IntegerType.instance);
        StringBuilder expected = new StringBuilder("[ArrayList<java.lang.Integer>]");
        assertEquals(expected.toString(), javaTypeWithElementType.getErasedSignature(new StringBuilder()).toString());
    }

    @Test
    public void testGetGenericSignature() {
        JavaType javaTypeWithElementType = CollectionLikeType.construct(ArrayList.class, TypeBindings.emptyBindings(), null, null, IntegerType.instance);
        StringBuilder expected = new StringBuilder("[ArrayList<java.lang.Integer>]");
        expected.append("<T>");
        assertEquals(expected.toString(), javaTypeWithElementType.getGenericSignature(new StringBuilder()).toString());
    }

    @Test
    public void testBuildCanonicalName() {
        JavaType javaTypeWithElementType = CollectionLikeType.construct(ArrayList.class, TypeBindings.emptyBindings(), null, null, IntegerType.instance);
        assertEquals("java.util.ArrayList<java.lang.Integer>", javaTypeWithElementType.buildCanonicalName());
    }

    @Test
    public void testEquals() {
        JavaType javaTypeWithElementType1 = CollectionLikeType.construct(ArrayList.class, TypeBindings.emptyBindings(), null, null, IntegerType.instance);
        JavaType javaTypeWithElementType2 = CollectionLikeType.construct(ArrayList.class, TypeBindings.emptyBindings(), null, null, IntegerType.instance);

        JavaType javaTypeWithElementType3 = CollectionLikeType.construct(ArrayList.class, TypeBindings.emptyBindings(), null, null, IntegerType.instance);
        JavaType javaTypeWithElementType4 = CollectionLikeType.construct(ArrayList.class, TypeBindings.emptyBindings(), null, null, LongType.instance);

        JavaType javaTypeWithElementType5 = CollectionLikeType.construct(LinkedList.class, TypeBindings.emptyBindings(), null, null, IntegerType.instance);

        assertTrue(javaTypeWithElementType1.equals(javaTypeWithElementType2));
        assertFalse(javaTypeWithElementType1.equals(javaTypeWithElementType3));
        assertFalse(javaTypeWithElementType1.equals(javaTypeWithElementType4));
        assertFalse(javaTypeWithElementType1.equals(javaTypeWithElementType5));
    }

    @Test
    public void testToString() {
        JavaType javaTypeWithElementType = CollectionLikeType.construct(ArrayList.class, TypeBindings.emptyBindings(), null, null, IntegerType.instance);
        assertEquals("[collection-like type; class java.util.ArrayList<java.lang.Integer>, contains java.lang.Integer]", javaTypeWithElementType.toString());
    }
}
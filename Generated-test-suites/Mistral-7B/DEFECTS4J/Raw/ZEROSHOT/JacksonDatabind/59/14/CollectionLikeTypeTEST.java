package com.yourpackage; // replace with your package name

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.junit.Test;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.CollectionLikeType;
import com.fasterxml.jackson.databind.type.TypeBindings;

/**
 * CollectionLikeTypeTest class file
 */
public class CollectionLikeTypeTest {

    /**
     * Test method for {@link CollectionLikeType#CollectionLikeType(Class, TypeBindings, JavaType, JavaType[], JavaType, Object, Object, boolean)}
     */
    @Test
    public void testCollectionLikeTypeConstruction() {
        Class<List<String>> listClass = List.class;
        TypeBindings bindings = TypeBindings.emptyBindings();
        JavaType stringJavaType = new TypeFactory().constructType(String.class);
        JavaType listJavaType = CollectionLikeType.construct(listClass, bindings, null, null, stringJavaType);

        CollectionLikeType collectionLikeType = (CollectionLikeType) listJavaType;
        assertEquals(List.class, collectionLikeType.getRawType());
        assertEquals(stringJavaType, collectionLikeType.getContentType());
        assertFalse(collectionLikeType.isTrueCollectionType());
    }

    /**
     * Test method for {@link CollectionLikeType#CollectionLikeType(JavaType, JavaType)}
     */
    @Test
    public void testUpgradeFrom() {
        Class<List<String>> listClass = List.class;
        JavaType stringJavaType = new TypeFactory().constructType(String.class);
        JavaType listJavaType = CollectionLikeType.construct(listClass, null);
        JavaType upgradedListJavaType = CollectionLikeType.upgradeFrom(listJavaType, stringJavaType);

        CollectionLikeType collectionLikeType = (CollectionLikeType) upgradedListJavaType;
        assertEquals(List.class, collectionLikeType.getRawType());
        assertEquals(stringJavaType, collectionLikeType.getContentType());
        assertFalse(collectionLikeType.isTrueCollectionType());
    }

    /**
     * Test method for {@link CollectionLikeType#isTrueCollectionType()}
     */
    @Test
    public void testIsTrueCollectionType() {
        Class<ArrayList<Integer>> arrayListClass = ArrayList.class;
        JavaType integerJavaType = new TypeFactory().constructType(Integer.class);
        JavaType arrayListJavaType = CollectionLikeType.construct(arrayListClass, null);

        CollectionLikeType collectionLikeType = (CollectionLikeType) arrayListJavaType;
        assertTrue(collectionLikeType.isTrueCollectionType());
    }

    /**
     * Test method for {@link CollectionLikeType#getContentType()}
     */
    @Test
    public void testGetContentType() {
        Class<List<String>> listClass = List.class;
        TypeBindings bindings = TypeBindings.emptyBindings();
        JavaType stringJavaType = new TypeFactory().constructType(String.class);
        JavaType listJavaType = CollectionLikeType.construct(listClass, bindings, null, null, stringJavaType);

        CollectionLikeType collectionLikeType = (CollectionLikeType) listJavaType;
        JavaType contentType = collectionLikeType.getContentType();
        assertEquals(String.class, contentType.getRawClass());
    }

    /**
     * Test method for {@link CollectionLikeType#hashCode()}
     */
    @Test
    public void testHashCode() {
        Class<List<Integer>> listClass1 = List.class;
        JavaType integerJavaType1 = new TypeFactory().constructType(Integer.class);
        JavaType listJavaType1 = CollectionLikeType.construct(listClass1, null, null, null, integerJavaType1);

        Class<ArrayList<Integer>> listClass2 = ArrayList.class;
        JavaType integerJavaType2 = new TypeFactory().constructType(Integer.class);
        JavaType listJavaType2 = CollectionLikeType.construct(listClass2, null, null, null, integerJavaType2);

        CollectionLikeType collectionLikeType1 = (CollectionLikeType) listJavaType1;
        CollectionLikeType collectionLikeType2 = (CollectionLikeType) listJavaType2;

        int hashCode1 = collectionLikeType1.hashCode();
        int hashCode2 = collectionLikeType2.hashCode();

        assertNotSame(hashCode1, hashCode2);
    }

    /**
     * Test method for {@link CollectionLikeType#equals(Object)}
     */
    @Test
    public void testEquals() {
        Class<List<Integer>> listClass1 = List.class;
        JavaType integerJavaType1 = new TypeFactory().constructType(Integer.class);
        JavaType listJavaType1 = CollectionLikeType.construct(listClass1, null, null, null, integerJavaType1);

        Class<ArrayList<Integer>> listClass2 = ArrayList.class;
        JavaType integerJavaType2 = new TypeFactory().constructType(Integer.class);
        JavaType listJavaType2 = CollectionLikeType.construct(listClass2, null, null, null, integerJavaType2);

        CollectionLikeType collectionLikeType1 = (CollectionLikeType) listJavaType1;
        CollectionLikeType collectionLikeType2 = (CollectionLikeType) listJavaType2;
        CollectionLikeType collectionLikeType3 = new TypeFactory().constructType(collectionLikeType1.getRawType());

        assertTrue(collectionLikeType1.equals(collectionLikeType1));
        assertTrue(collectionLikeType1.equals(collectionLikeType3));
        assertFalse(collectionLikeType1.equals("not a CollectionLikeType"));
        assertFalse(collectionLikeType1.equals(collectionLikeType2));
    }

    /**
     * Test method for {@link CollectionLikeType#toString()}
     */
    @Test
    public void testToString() {
        Class<List<String>> listClass = List.class;
        TypeBindings bindings = TypeBindings.emptyBindings();
        JavaType stringJavaType = new TypeFactory().constructType(String.class);
        JavaType listJavaType = CollectionLikeType.construct(listClass, bindings, null, null, stringJavaType);

        CollectionLikeType collectionLikeType = (CollectionLikeType) listJavaType;
        String toString = collectionLikeType.toString();

        String expected = "[collection-like type; class " + listClass.getName() + ", contains " + stringJavaType + "]";
        assertEquals(expected, toString);
    }
}
package com.fasterxml.jackson.databind.type;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.lang.reflect.TypeVariable;
import java.util.Collection;

import org.junit.Test;

public class CollectionLikeTypeTest {

    @Test
    public void testIsContainerType() {
        CollectionLikeType collectionLikeType = new CollectionLikeType(Collection.class, TypeBindings.emptyBindings(), null, null, null, null, null, false);
        assertTrue(collectionLikeType.isContainerType());

        CollectionLikeType nonCollectionType = new CollectionLikeType(String.class, TypeBindings.emptyBindings(), null, null, null, null, null, false);
        assertFalse(nonCollectionType.isContainerType());
    }

    @Test
    public void testIsCollectionLikeType() {
        CollectionLikeType collectionLikeType = new CollectionLikeType(Collection.class, TypeBindings.emptyBindings(), null, null, null, null, null, false);
        assertTrue(collectionLikeType.isCollectionLikeType());

        CollectionLikeType nonCollectionLikeType = new CollectionLikeType(String.class, TypeBindings.emptyBindings(), null, null, null, null, null, false);
        assertFalse(nonCollectionLikeType.isCollectionLikeType());
    }

    @Test
    public void testGetContentType() {
        JavaType javaType = new TypeFactory().constructType(Collection.class, new TypeVariable[]{new TypeVariable("E"){}});
        CollectionLikeType collectionLikeType = new CollectionLikeType(javaType.getRawType(), TypeBindings.emptyBindings(), null, null, javaType.getComponentType(), null, null, false);

        assertEquals(javaType.getComponentType(), collectionLikeType.getContentType());
    }

    @Test
    public void testEquals() {
        CollectionLikeType collectionLikeType1 = new CollectionLikeType(Collection.class, TypeBindings.emptyBindings(), null, null, null, null, null, false);
        CollectionLikeType collectionLikeType2 = new CollectionLikeType(Collection.class, TypeBindings.emptyBindings(), null, null, null, null, null, false);

        assertTrue(collectionLikeType1.equals(collectionLikeType2));

        CollectionLikeType collectionLikeType3 = new CollectionLikeType(ArrayList.class, TypeBindings.emptyBindings(), null, null, null, null, null, false);
        assertFalse(collectionLikeType1.equals(collectionLikeType3));

        CollectionLikeType collectionLikeType4 = new CollectionLikeType(Collection.class, TypeBindings.emptyBindings(), null, null, null, null, null, true);
        assertFalse(collectionLikeType1.equals(collectionLikeType4));
    }

    @Test
    public void testToString() {
        CollectionLikeType collectionLikeType = new CollectionLikeType(Collection.class, TypeBindings.emptyBindings(), null, null, null, null, null, false);

        assertEquals("[collection-like type; class java.util.Collection, contains class ?]", collectionLikeType.toString());
    }

    @Test
    public void testIsTrueCollectionType() {
        CollectionLikeType collectionLikeType = new CollectionLikeType(Collection.class, TypeBindings.emptyBindings(), null, null, null, null, null, false);
        assertTrue(collectionLikeType.isTrueCollectionType());

        CollectionLikeType nonCollectionLikeType = new CollectionLikeType(String.class, TypeBindings.emptyBindings(), null, null, null, null, null, false);
        assertFalse(nonCollectionLikeType.isTrueCollectionType());
    }

    @Test
    public void testConstruct() {
        JavaType javaType = new TypeFactory().constructType(Collection.class, new TypeVariable[]{new TypeVariable("E"){}});
        CollectionLikeType collectionLikeType = CollectionLikeType.construct(javaType.getRawType(), TypeBindings.emptyBindings(), null, null, javaType.getComponentType());

        assertEquals(javaType.getComponentType(), collectionLikeType.getContentType());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructInvalidBaseType() {
        JavaType javaType = new TypeFactory().constructType(String.class, new TypeVariable[]{new TypeVariable("E"){}});
        CollectionLikeType.construct(javaType.getRawType(), javaType.getComponentType());
    }

    @Test
    public void testUpgradeFrom() {
        JavaType listType = new TypeFactory().constructType(ArrayList.class, new TypeVariable[]{new TypeVariable("E"){}});
        CollectionLikeType collectionLikeType = CollectionLikeType.upgradeFrom(listType, String.class);

        assertEquals(String.class, collectionLikeType.getContentType().getRawType());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testUpgradeFromInvalidBaseType() {
        JavaType stringType = new TypeFactory().constructType(String.class, new TypeVariable[]{new TypeVariable("E"){}});
        CollectionLikeType.upgradeFrom(stringType, ArrayList.class);
    }
}
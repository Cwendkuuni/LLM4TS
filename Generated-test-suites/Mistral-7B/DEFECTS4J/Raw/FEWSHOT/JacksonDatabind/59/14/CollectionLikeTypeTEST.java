package com.fasterxml.jackson.databind.type;

import static org.junit.Assert.*;

import java.lang.reflect.TypeVariable;
import java.util.Collection;

import com.fasterxml.jackson.databind.JavaType;

public class CollectionLikeTypeTest {

    @Test
    public void testConstruct() {
        JavaType javaType = CollectionLikeType.construct(Collection.class, TypeBindings.emptyBindings(), null, null, null);
        assertEquals(Collection.class, javaType.getRawClass());
        assertNull(javaType.getBindings());
        assertNull(javaType.getSuperClass());
        assertNull(javaType.getSuperInterfaces());
        assertNull(javaType.getContentType());
        assertNull(javaType.getValueHandler());
        assertNull(javaType.getTypeHandler());
        assertFalse(javaType.isStatic());
    }

    @Test
    public void testConstructWithTypeBindingsAndSuperInterfaces() {
        TypeVariable<?>[] typeVariables = Collection.class.getTypeParameters();
        JavaType javaType = CollectionLikeType.construct(Collection.class, TypeBindings.emptyBindings(), null, new JavaType[]{TypeFactory.defaultInstance().constructType(typeVariables[0].getType())}, null);
        assertEquals(Collection.class, javaType.getRawClass());
        assertNotNull(javaType.getBindings());
        assertNull(javaType.getSuperClass());
        assertArrayNotNull(javaType.getSuperInterfaces());
        assertNull(javaType.getContentType());
        assertNull(javaType.getValueHandler());
        assertNull(javaType.getTypeHandler());
        assertFalse(javaType.isStatic());
    }

    @Test
    public void testConstructWithElementType() {
        JavaType javaType = CollectionLikeType.construct(String[].class, null);
        assertEquals(String[].class, javaType.getRawClass());
        assertNull(javaType.getBindings());
        assertNull(javaType.getSuperClass());
        assertNull(javaType.getSuperInterfaces());
        assertEquals(String.class, javaType.getContentType().getRawClass());
        assertNull(javaType.getValueHandler());
        assertNull(javaType.getTypeHandler());
        assertFalse(javaType.isStatic());
    }

    @Test
    public void testUpgradeFrom() {
        JavaType javaType = TypeFactory.defaultInstance().constructType(Collection.class);
        JavaType upgradedType = CollectionLikeType.upgradeFrom(javaType, String.class);
        assertEquals(CollectionLikeType.class, upgradedType.getClass());
        assertEquals(Collection.class, upgradedType.getRawClass());
        assertNotNull(upgradedType.getBindings());
        assertNull(upgradedType.getSuperClass());
        assertNull(upgradedType.getSuperInterfaces());
        assertEquals(String.class, upgradedType.getContentType().getRawClass());
        assertNull(upgradedType.getValueHandler());
        assertNull(upgradedType.getTypeHandler());
        assertFalse(upgradedType.isStatic());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testUpgradeFromInvalidBaseType() {
        JavaType javaType = TypeFactory.defaultInstance().constructType(Object.class);
        CollectionLikeType.upgradeFrom(javaType, String.class);
    }

    @Test
    public void testIsContainerType() {
        JavaType javaType = CollectionLikeType.construct(Collection.class, null);
        assertTrue(javaType.isContainerType());
    }

    @Test
    public void testIsCollectionLikeType() {
        JavaType javaType = CollectionLikeType.construct(Collection.class, null);
        assertTrue(javaType.isCollectionLikeType());
    }

    @Test
    public void testGetContentType() {
        JavaType javaType = CollectionLikeType.construct(Collection.class, null);
        assertEquals(Object.class, javaType.getContentType().getRawClass());

        JavaType javaTypeWithElementType = CollectionLikeType.construct(Collection.class, null, null, null, java.util.ArrayList.class);
        assertEquals(java.util.ArrayList.class, javaTypeWithElementType.getContentType().getRawClass());
    }

    @Test
    public void testGetContentValueHandler() {
        JavaType javaType = CollectionLikeType.construct(Collection.class, null);
        assertNull(javaType.getContentValueHandler());

        JavaType javaTypeWithValueHandler = CollectionLikeType.construct(Collection.class, null, null, null, java.util.ArrayList.class, null, null, false, null);
        assertNotNull(javaTypeWithValueHandler.getContentValueHandler());
    }

    @Test
    public void testGetContentTypeHandler() {
        JavaType javaType = CollectionLikeType.construct(Collection.class, null);
        assertNull(javaType.getContentTypeHandler());

        JavaType javaTypeWithTypeHandler = CollectionLikeType.construct(Collection.class, null, null, null, java.util.ArrayList.class, null, null, false, null);
        assertNotNull(javaTypeWithTypeHandler.getContentTypeHandler());
    }

    @Test
    public void testHasHandlers() {
        JavaType javaType = CollectionLikeType.construct(Collection.class, null);
        assertFalse(javaType.hasHandlers());

        JavaType javaTypeWithHandlers = CollectionLikeType.construct(Collection.class, null, null, null, java.util.ArrayList.class, null, null, false, null);
        assertTrue(javaTypeWithHandlers.hasHandlers());
    }

    @Test
    public void testEquals() {
        JavaType javaType1 = CollectionLikeType.construct(Collection.class, null);
        JavaType javaType2 = CollectionLikeType.construct(Collection.class, null);
        assertTrue(javaType1.equals(javaType2));

        JavaType javaType3 = CollectionLikeType.construct(Collection.class, null);
        JavaType javaType4 = new CollectionLikeType(java.util.ArrayList.class, null);
        assertFalse(javaType3.equals(javaType4));
    }

    @Test
    public void testToString() {
        JavaType javaType = CollectionLikeType.construct(Collection.class, null);
        assertEquals("[collection-like type; class Collection, contains Object]", javaType.toString());

        JavaType javaTypeWithElementType = CollectionLikeType.construct(Collection.class, null, null, null, java.util.ArrayList.class);
        assertEquals("[collection-like type; class Collection, contains ArrayList]", javaTypeWithElementType.toString());
    }

    @Test
    public void testIsTrueCollectionType() {
        JavaType javaType = CollectionLikeType.construct(Collection.class, null);
        assertTrue(javaType.isTrueCollectionType());

        JavaType javaTypeWithoutCollectionType = TypeFactory.defaultInstance().constructType(String[].class);
        assertFalse(javaTypeWithoutCollectionType.isTrueCollectionType());
    }

    private void assertArrayNotNull(JavaType[] javaTypes) {
        assertNotNull(javaTypes);
        for (JavaType javaType : javaTypes) {
            assertNotNull(javaType);
        }
    }

}
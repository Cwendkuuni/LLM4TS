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
        CollectionLikeType type = new CollectionLikeType(Collection.class, TypeBindings.emptyBindings(), null, null, null, null, null, false);
        assertTrue(type.isContainerType());

        Class<?> rawType = new Object() {}.getClass();
        CollectionLikeType type2 = new CollectionLikeType(rawType, TypeBindings.emptyBindings(), null, null, null, null, null, false);
        assertFalse(type2.isContainerType());
    }

    @Test
    public void testIsCollectionLikeType() {
        CollectionLikeType type = new CollectionLikeType(Collection.class, TypeBindings.emptyBindings(), null, null, null, null, null, false);
        assertTrue(type.isCollectionLikeType());

        JavaType javaType = new SimpleType(int[].class);
        CollectionLikeType type2 = new CollectionLikeType(javaType.getRawClass(), TypeBindings.emptyBindings(), null, null, null, null, null, false);
        assertTrue(type2.isCollectionLikeType());
    }

    @Test
    public void testGetContentType() {
        CollectionLikeType type = new CollectionLikeType(Collection.class, TypeBindings.emptyBindings(), null, null, null, null, null, false);
        assertEquals(Object.class, type.getContentType().getRawClass());

        JavaType javaType = new SimpleType(int[].class);
        CollectionLikeType type2 = new CollectionLikeType(javaType.getRawClass(), TypeBindings.emptyBindings(), null, null, null, null, null, false);
        assertEquals(int[].class, type2.getContentType().getRawClass());
    }

    @Test
    public void testGetContentValueHandler() {
        CollectionLikeType type = new CollectionLikeType(Collection.class, TypeBindings.emptyBindings(), null, null, null, null, null, false);
        assertNull(type.getContentValueHandler());

        JavaType javaType = new SimpleType(int[].class);
        CollectionLikeType type2 = new CollectionLikeType(javaType.getRawClass(), TypeBindings.emptyBindings(), null, null, null, null, null, false);
        assertNull(type2.getContentValueHandler());
    }

    @Test
    public void testGetContentTypeHandler() {
        CollectionLikeType type = new CollectionLikeType(Collection.class, TypeBindings.emptyBindings(), null, null, null, null, null, false);
        assertNull(type.getContentTypeHandler());

        JavaType javaType = new SimpleType(int[].class);
        CollectionLikeType type2 = new CollectionLikeType(javaType.getRawClass(), TypeBindings.emptyBindings(), null, null, null, null, null, false);
        assertNull(type2.getContentTypeHandler());
    }

    @Test
    public void testHasHandlers() {
        CollectionLikeType type = new CollectionLikeType(Collection.class, TypeBindings.emptyBindings(), null, null, null, null, null, false);
        assertFalse(type.hasHandlers());

        JavaType javaType = new SimpleType(int[].class);
        CollectionLikeType type2 = new CollectionLikeType(javaType.getRawClass(), TypeBindings.emptyBindings(), null, null, null, null, null, false);
        assertFalse(type2.hasHandlers());
    }

    @Test
    public void testGetErasedSignature() {
        CollectionLikeType type = new CollectionLikeType(Collection.class, TypeBindings.emptyBindings(), null, null, null, null, null, false);
        assertEquals("[collection-like type; class java.util.Collection, contains java.lang.Object]", type.getErasedSignature().toString());

        JavaType javaType = new SimpleType(int[].class);
        CollectionLikeType type2 = new CollectionLikeType(javaType.getRawClass(), TypeBindings.emptyBindings(), null, null, null, null, null, false);
        assertEquals("[collection-like type; class [I]", type2.getErasedSignature().toString());
    }

    @Test
    public void testGetGenericSignature() {
        CollectionLikeType type = new CollectionLikeType(Collection.class, TypeBindings.emptyBindings(), null, null, null, null, null, false);
        assertEquals("[collection-like type; class java.util.Collection<E>]", type.getGenericSignature().toString());

        JavaType javaType = new SimpleType(int[].class);
        CollectionLikeType type2 = new CollectionLikeType(javaType.getRawClass(), TypeBindings.emptyBindings(), null, null, null, null, null, false);
        assertEquals("[collection-like type; class [I]]", type2.getGenericSignature().toString());
    }

    @Test
    public void testBuildCanonicalName() {
        CollectionLikeType type = new CollectionLikeType(Collection.class, TypeBindings.emptyBindings(), null, null, null, null, null, false);
        assertEquals("java.util.Collection", type.buildCanonicalName());

        JavaType javaType = new SimpleType(int[].class);
        CollectionLikeType type2 = new CollectionLikeType(javaType.getRawClass(), TypeBindings.emptyBindings(), null, null, null, null, null, false);
        assertEquals("[I]", type2.buildCanonicalName());
    }

    @Test
    public void testIsTrueCollectionType() {
        CollectionLikeType type = new CollectionLikeType(Collection.class, TypeBindings.emptyBindings(), null, null, null, null, null, false);
        assertTrue(type.isTrueCollectionType());

        Class<?> rawType = new Object() {}.getClass();
        CollectionLikeType type2 = new CollectionLikeType(rawType, TypeBindings.emptyBindings(), null, null, null, null, null, false);
        assertFalse(type2.isTrueCollectionType());
    }

    @Test
    public void testEquality() {
        CollectionLikeType type1 = new CollectionLikeType(Collection.class, TypeBindings.emptyBindings(), null, null, null, null, null, false);
        CollectionLikeType type2 = new CollectionLikeType(Collection.class, TypeBindings.emptyBindings(), null, null, null, null, null, false);
        assertTrue(type1.equals(type2));

        CollectionLikeType type3 = new CollectionLikeType(List.class, TypeBindings.emptyBindings(), null, null, null, null, null, false);
        assertFalse(type1.equals(type3));
    }

    @Test
    public void testToString() {
        CollectionLikeType type = new CollectionLikeType(Collection.class, TypeBindings.emptyBindings(), null, null, null, null, null, false);
        assertEquals("[collection-like type; class java.util.Collection, contains java.lang.Object]", type.toString());

        JavaType javaType = new SimpleType(int[].class);
        CollectionLikeType type2 = new CollectionLikeType(javaType.getRawClass(), TypeBindings.emptyBindings(), null, null, null, null, null, false);
        assertEquals("[collection-like type; class [I]", type2.toString());
    }

    private static void _classSignature(Class<?> clazz, StringBuilder sb, boolean erased) {
        String name = clazz.getName();
        sb.append("[collection-like type; class ");
        if (erased) {
            sb.append(name.substring(name.lastIndexOf(".")+1));
        } else {
            sb.append(name);
        }
        sb.append(", contains ");
        if (clazz.getComponentType() != null) {
            sb.append(clazz.getComponentType().getTypeName());
        }
        sb.append("]");
    }
}
package com.fasterxml.jackson.databind.type;

import static org.junit.Assert.*;

import java.lang.reflect.TypeVariable;
import java.util.Collection;

import org.junit.Test;

import com.fasterxml.jackson.databind.JavaType;

public class CollectionLikeTypeTest {

    // Basic test cases for methods with no arguments

    @Test
    public void testIsContainerType() {
        CollectionLikeType collectionLikeType = new CollectionLikeType(Collection.class, null, null, null, null, null, null, false);
        assertTrue(collectionLikeType.isContainerType());

        JavaType javaType = new JavaType(String[].class, null);
        CollectionLikeType lessContainerType = new CollectionLikeType(javaType.getRawClass(), null, null, null, null, null, null, false);
        assertFalse(lessContainerType.isContainerType());
    }

    @Test
    public void testIsCollectionLikeType() {
        CollectionLikeType collectionLikeType = new CollectionLikeType(Collection.class, null, null, null, null, null, null, false);
        assertTrue(collectionLikeType.isCollectionLikeType());

        JavaType javaType = new JavaType(String[].class, null);
        CollectionLikeType lessCollectionLikeType = new CollectionLikeType(javaType.getRawClass(), null, null, null, null, null, null, false);
        assertFalse(lessCollectionLikeType.isCollectionLikeType());
    }

    @Test
    public void testGetContentType() {
        JavaType javaType = new JavaType(Collection.class, null);
        CollectionLikeType collectionLikeType = new CollectionLikeType(javaType.getRawClass(), null, null, null, null, null, null, false);
        assertEquals(javaType.getComponentType(), collectionLikeType.getContentType());
    }

    @Test
    public void testGetContentValueHandler() {
        JavaType javaType = new JavaType(Collection.class, null);
        CollectionLikeType collectionLikeType = new CollectionLikeType(javaType.getRawClass(), null, null, null, null, null, null, false);
        assertEquals(javaType.getValueHandler(), collectionLikeType.getContentValueHandler());
    }

    @Test
    public void testGetContentTypeHandler() {
        JavaType javaType = new JavaType(Collection.class, null);
        CollectionLikeType collectionLikeType = new CollectionLikeType(javaType.getRawClass(), null, null, null, null, null, null, false);
        assertEquals(javaType.getTypeHandler(), collectionLikeType.getContentTypeHandler());
    }

    @Test
    public void testHasHandlers() {
        JavaType javaType = new JavaType(Collection.class, null);
        CollectionLikeType collectionLikeType = new CollectionLikeType(javaType.getRawClass(), null, null, null, null, null, null, false);
        assertTrue(collectionLikeType.hasHandlers());
    }

    @Test
    public void testGetErasedSignature() {
        JavaType javaType = new JavaType(Collection.class, null);
        CollectionLikeType collectionLikeType = new CollectionLikeType(javaType.getRawClass(), null, null, null, null, null, null, false);
        assertEquals("[collection-like type; class java.util.Collection, contains class java.lang.Object]", collectionLikeType.getErasedSignature().toString());
    }

    @Test
    public void testGetGenericSignature() {
        JavaType javaType = new JavaType(Collection.class, null);
        CollectionLikeType collectionLikeType = new CollectionLikeType(javaType.getRawClass(), null, null, null, null, null, null, false);
        assertEquals("[collection-like type; class java.util.Collection<E>, contains class E]", collectionLikeType.getGenericSignature().toString());
    }

    @Test
    public void testBuildCanonicalName() {
        JavaType javaType = new JavaType(Collection.class, null);
        CollectionLikeType collectionLikeType = new CollectionLikeType(javaType.getRawClass(), null, null, null, null, null, null, false);
        assertEquals("[collection-like type; class java.util.Collection<E>, contains class E]", collectionLikeType.buildCanonicalName());
    }

    // Test constructors

    @Test(expected = IllegalArgumentException.class)
    public void testConstructWithInvalidRawType() {
        JavaType javaType = new JavaType(String[].class, null);
        CollectionLikeType.construct(javaType.getRawClass(), null, null, null, null);
    }

    @Test
    public void testConstruct() {
        JavaType javaType = new JavaType(Collection.class, null);
        CollectionLikeType collectionLikeType = CollectionLikeType.construct(javaType.getRawClass(), null, null, null, javaType.getComponentType());
        assertEquals(javaType, collectionLikeType.getType());
    }

    @Test
    public void testConstructWithEmptyBindings() {
        JavaType javaType = new JavaType(Collection.class, null);
        TypeBindings bindings = TypeBindings.emptyBindings();
        CollectionLikeType collectionLikeType = CollectionLikeType.construct(javaType.getRawClass(), bindings, null, null, javaType.getComponentType());
        assertEquals(javaType, collectionLikeType.getType());
    }

    @Test
    public void testConstructWithTypeBindings() {
        JavaType javaType = new JavaType(Collection.class, null);
        TypeBindings bindings = TypeBindings.create(javaType.getRawClass(), javaType.getComponentType());
        CollectionLikeType collectionLikeType = CollectionLikeType.construct(javaType.getRawClass(), bindings, null, null, javaType.getComponentType());
        assertEquals(javaType, collectionLikeType.getType());
    }

    @Test
    public void testConstructWithBogusSuperClass() {
        JavaType javaType = new JavaType(Collection.class, null);
        CollectionLikeType collectionLikeType = CollectionLikeType.construct(javaType.getRawClass(), null, _bogusSuperClass(javaType.getRawClass()), null, javaType.getComponentType());
        assertEquals(javaType, collectionLikeType.getType());
    }

    // Test upgradeFrom method

    @Test(expected = IllegalArgumentException.class)
    public void testUpgradeFromWithInvalidBaseType() {
        JavaType javaType = new JavaType(String[].class, null);
        CollectionLikeType.upgradeFrom(javaType, null);
    }

    @Test
    public void testUpgradeFrom() {
        JavaType javaType = new JavaType(Collection.class, null);
        TypeBase baseType = new TypeBase(javaType.getRawClass(), null, null, javaType.getComponentType(), null, null, false);
        CollectionLikeType collectionLikeType = CollectionLikeType.upgradeFrom(baseType, javaType.getComponentType());
        assertEquals(javaType, collectionLikeType.getType());
    }

    // Test methods with arguments

    @Test
    public void testNarrow() {
        JavaType javaType = new JavaType(Collection.class, null);
        CollectionLikeType collectionLikeType = new CollectionLikeType(javaType.getRawClass(), null, null, null, javaType.getComponentType(), null, null, false);
        TypeBase subclass = new TypeBase(ArrayList.class, null, null, null, null, null, false);
        CollectionLikeType narrowedType = collectionLikeType._narrow(subclass);
        assertEquals(new CollectionLikeType(subclass.getType(), null, null, null, javaType.getComponentType(), null, null, false), narrowedType);
    }

    @Test
    public void testWithContentType() {
        JavaType javaType = new JavaType(Collection.class, null);
        CollectionLikeType collectionLikeType = new CollectionLikeType(javaType.getRawClass(), null, null, null, javaType.getComponentType(), null, null, false);
        JavaType newContentType = new JavaType(HashMap.class, null);
        CollectionLikeType newCollectionLikeType
                = collectionLikeType.withContentType(newContentType);
        assertEquals(javaType, newCollectionLikeType.getType().getSuperClass());
        assertEquals(newContentType, newCollectionLikeType.getContentType());
    }

    @Test
    public void testWithTypeHandler() {
        JavaType javaType = new JavaType(Collection.class, null);
        CollectionLikeType collectionLikeType = new CollectionLikeType(javaType.getRawClass(), null, null, null, javaType.getComponentType(), null, null, false);
        Object handler = new Object();
        CollectionLikeType newCollectionLikeType = collectionLikeType.withTypeHandler(handler);
        assertEquals(collectionLikeType.getType(), newCollectionLikeType.getType());
        assertNotNull(newCollectionLikeType.getTypeHandler());
        assertEquals(handler, newCollectionLikeType.getTypeHandler());
    }

    @Test
    public void testWithContentTypeHandler() {
        JavaType javaType = new JavaType(Collection.class, null);
        CollectionLikeType collectionLikeType = new CollectionLikeType(javaType.getRawClass(), null, null, null, javaType.getComponentType(), null, null, false);
        Object handler = new Object();
        JavaType newContentType = new JavaType(HashMap.class, null);
        CollectionLikeType newCollectionLikeType = collectionLikeType.withContentTypeHandler(handler).withContentType(newContentType);
        assertEquals(javaType, newCollectionLikeType.getType().getSuperClass());
        assertEquals(newContentType, newCollectionLikeType.getContentType());
        assertNotNull(newCollectionLikeType.getValueHandler());
        assertEquals(handler, newCollectionLikeType.getValueHandler());
    }

    @Test
    public void testWithValueHandler() {
        JavaType javaType = new JavaType(Collection.class, null);
        CollectionLikeType collectionLikeType = new CollectionLikeType(javaType.getRawClass(), null, null, null, javaType.getComponentType(), null, null, false);
        Object handler = new Object();
        CollectionLikeType newCollectionLikeType = collectionLikeType.withValueHandler(handler);
        assertEquals(collectionLikeType.getType(), newCollectionLikeType.getType());
        assertNotNull(newCollectionLikeType.getValueHandler());
        assertEquals(handler, newCollectionLikeType.getValueHandler());
    }

    @Test
    public void testWithContentValueHandler() {
        JavaType javaType = new JavaType(Collection.class, null);
        CollectionLikeType collectionLikeType = new CollectionLikeType(javaType.getRawClass(), null, null, null, javaType.getComponentType(), null, null, false);
        Object handler = new Object();
        CollectionLikeType newCollectionLikeType = collectionLikeType.withContentValueHandler(handler);
        assertEquals(collectionLikeType.getType(), newCollectionLikeType.getType());
        assertNotNull(newCollectionLikeType.getContentValueHandler());
        assertEquals(handler, newCollectionLikeType.getContentValueHandler());
    }

    @Test
    public void testWithStaticTyping() {
        JavaType javaType = new JavaType(Collection.class, null);
        CollectionLikeType collectionLikeType = new CollectionLikeType(javaType.getRawClass(), null, null, null, javaType.getComponentType(), null, null, false);
        CollectionLikeType newCollectionLikeType = collectionLikeType.withStaticTyping();
        assertTrue(newCollectionLikeType._asStatic);
        assertFalse(collectionLikeType._asStatic);
    }

    @Test
    public void testRefine() {
        JavaType javaType = new JavaType(Collection.class, null);
        CollectionLikeType collectionLikeType = new CollectionLikeType(javaType.getRawClass(), null, null, null, javaType.getComponentType(), null, null, false);
        JavaType newJavaType = new JavaType(ArrayList.class, null);
        TypeBindings bindings = TypeBindings.create(newJavaType.getRawClass(), javaType.getComponentType());
        CollectionLikeType newCollectionLikeType = collectionLikeType.refine(newJavaType.getRawClass(), bindings, null, null);
        assertEquals(newJavaType, newCollectionLikeType.getType());
    }

    // Test isTrueCollectionType()

    @Test
    public void testIsTrueCollectionTypeWithCollection() {
        JavaType javaType = new JavaType(Collection.class, null);
        CollectionLikeType collectionLikeType = new CollectionLikeType(javaType.getRawClass(), null, null, null, javaType.getComponentType(), null, null, false);
        assertTrue(collectionLikeType.isTrueCollectionType());
    }

    @Test
    public void testIsTrueCollectionTypeWithNonCollection() {
        JavaType javaType = new JavaType(String[].class, null);
        CollectionLikeType collectionLikeType = new CollectionLikeType(javaType.getRawClass(), null, null, null, javaType.getComponentType(), null, null, false);
        assertFalse(collectionLikeType.isTrueCollectionType());
    }

    // Test edge cases and exception handling

    @Test(expected = IllegalArgumentException.class)
    public void testConstructWithNullRawType() {
        CollectionLikeType.construct(null, null, null, null, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructWithNullBindings() {
        JavaType javaType = new JavaType(Collection.class, null);
        CollectionLikeType.construct(javaType.getRawClass(), null, null, null, javaType.getComponentType());
    }

    @Test
    public void testConstructWithEmptyTypeVariable() {
        JavaType javaType = new JavaType(Collection.class, null);
        TypeVariable<?>[] emptyVar = {};
        CollectionLikeType collectionLikeType = CollectionLikeType.construct(javaType.getRawClass(), null, null, null, javaType.getComponentType(),
                null, null, false);
        assertEquals(javaType, collectionLikeType.getType());
    }

    @Test
    public void testConstructWithMultipleTypeVariables() {
        JavaType javaType = new JavaType(Map.class, null);
        TypeVariable<?>[] vars = javaType.getTypeParameters();
        CollectionLikeType collectionLikeType = CollectionLikeType.construct(javaType.getRawClass(), null, null, null, vars[0].getBounds()[0],
                null, null, false);
        assertEquals(javaType, collectionLikeType.getType());
    }

}
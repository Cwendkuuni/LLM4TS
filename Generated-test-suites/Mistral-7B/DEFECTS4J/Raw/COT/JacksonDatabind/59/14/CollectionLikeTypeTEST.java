package com.fasterxml.jackson.databind.type;

import static org.junit.Assert.*;

import java.lang.reflect.TypeVariable;
import java.util.Collection;

import org.junit.Test;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.TypeBindings;
import com.fasterxml.jackson.databind.TypeBase;
import com.fasterxml.jackson.databind.TypeModifier;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;

public class CollectionLikeTypeTest {

    // Basic test cases for constructors

    @Test
    public void testConstruct() {
        JavaType elemType = TypeFactory.defaultInstance().constructType(int[].class);
        TypeBindings bindings = TypeBindings.defaultBindings();
        JavaType collectionType = CollectionLikeType.construct(Collection.class, bindings, null, null, elemType);
        assertEquals(Collection.class, collectionType.getRawClass());
        assertEquals(elemType, collectionType.getContentType());
    }

    @Test
    public void testConstructFromTypeBase() {
        JavaType elemType = TypeFactory.defaultInstance().constructType(int[].class);
        TypeBase base = new TypeBase() {
            @Override
            public JavaType getContentType() {
                return elemType;
            }

            @Override
            public boolean isContainerType() {
                return true;
            }

            @Override
            public boolean isCollectionLikeType() {
                return true;
            }

            @Override
            public String toString() {
                return "[type-base]";
            }

            @Override
            public Class<?> getRawClass() {
                return Collection.class;
            }
        };
        JavaType collectionType = CollectionLikeType.construct(base, elemType);
        assertEquals(Collection.class, collectionType.getRawClass());
        assertEquals(elemType, collectionType.getContentType());
    }

    @Test
    public void testConstructDeprecated() {
        JavaType elemType = TypeFactory.defaultInstance().constructType(int[].class);
        TypeBindings bindings = TypeBindings.defaultBindings();
        JavaType collectionType = CollectionLikeType.construct(Collection.class, elemType);
        assertEquals(Collection.class, collectionType.getRawClass());
        assertEquals(elemType, collectionType.getContentType());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructInvalidBaseType() {
        JavaType elemType = TypeFactory.defaultInstance().constructType(int[].class);
        JavaType baseType = TypeFactory.defaultInstance().constructType(String.class);
        CollectionLikeType.construct(baseType, elemType);
    }

    // Test method upgradeFrom

    @Test
    public void testUpgradeFrom() {
        JavaType baseType = TypeFactory.defaultInstance().constructType(ArrayList.class);
        JavaType elemType = TypeFactory.defaultInstance().constructType(int[].class);
        JavaType collectionType = CollectionLikeType.upgradeFrom(baseType, elemType);
        assertEquals(CollectionLikeType.class, collectionType.getClass());
        assertEquals(ArrayList.class, collectionType.getRawClass());
        assertEquals(elemType, collectionType.getContentType());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testUpgradeFromInvalidBaseType() {
        JavaType elemType = TypeFactory.defaultInstance().constructType(int[].class);
        JavaType baseType = TypeFactory.defaultInstance().constructType(String.class);
        CollectionLikeType.upgradeFrom(baseType, elemType);
    }

    // Test isTrueCollectionType

    @Test
    public void testIsTrueCollectionType() {
        JavaType collectionType = TypeFactory.defaultInstance().constructType(ArrayList.class);
        assertTrue(collectionType.isTrueCollectionType());

        JavaType notCollectionType = TypeFactory.defaultInstance().constructType(String.class);
        assertFalse(notCollectionType.isTrueCollectionType());
    }

    // Test equals

    @Test
    public void testEquals() {
        JavaType elemType = TypeFactory.defaultInstance().constructType(int[].class);
        TypeBindings bindings = TypeBindings.defaultBindings();
        JavaType collectionType1 = CollectionLikeType.construct(Collection.class, bindings, null, null, elemType);
        JavaType collectionType2 = CollectionLikeType.construct(Collection.class, bindings, null, null, elemType);
        JavaType collectionType3 = CollectionLikeType.construct(ArrayList.class, bindings, null, null, elemType);
        JavaType collectionType4 = TypeFactory.defaultInstance().constructType(LinkedList.class);
        JavaType collectionType5 = new CollectionLikeType(collectionType1);

        assertTrue(collectionType1.equals(collectionType1));
        assertTrue(collectionType1.equals(collectionType2));
        assertFalse(collectionType1.equals(collectionType3));
        assertFalse(collectionType1.equals(collectionType4));
        assertFalse(collectionType1.equals(collectionType5));
    }

    // Test getRawClass, getContentType, getContentValueHandler, getContentTypeHandler

    @Test
    public void testGetRawClass() {
        JavaType elemType = TypeFactory.defaultInstance().constructType(int[].class);
        TypeBindings bindings = TypeBindings.defaultBindings();
        JavaType collectionType = CollectionLikeType.construct(Collection.class, bindings, null, null, elemType);
        assertEquals(Collection.class, collectionType.getRawClass());
    }

    @Test
    public void testGetContentType() {
        JavaType elemType = TypeFactory.defaultInstance().constructType(int[].class);
        TypeBindings bindings = TypeBindings.defaultBindings();
        JavaType collectionType = CollectionLikeType.construct(Collection.class, bindings, null, null, elemType);
        assertEquals(elemType, collectionType.getContentType());
    }

    @Test
    public void testGetContentValueHandler() {
        JavaType elemType = TypeFactory.defaultInstance().constructType(int[].class);
        TypeBindings bindings = TypeBindings.defaultBindings();
        JavaType collectionType = CollectionLikeType.construct(Collection.class, bindings, null, null, elemType);
        assertNull(collectionType.getContentValueHandler());
    }

    @Test
    public void testGetContentTypeHandler() {
        JavaType elemType = TypeFactory.defaultInstance().constructType(int[].class);
        TypeBindings bindings = TypeBindings.defaultBindings();
        JavaType collectionType = CollectionLikeType.construct(Collection.class, bindings, null, null, elemType);
        assertNull(collectionType.getContentTypeHandler());
    }

    @Test
    public void testHasHandlers() {
        JavaType elemType = TypeFactory.defaultInstance().constructType(int[].class);
        TypeBindings bindings = TypeBindings.defaultBindings();
        JavaType collectionType = CollectionLikeType.construct(Collection.class, bindings, null, null, elemType);
        assertFalse(collectionType.hasHandlers());
    }

    // Test isContainerType, isCollectionLikeType

    @Test
    public void testIsContainerType() {
        JavaType elemType = TypeFactory.defaultInstance().constructType(int[].class);
        TypeBindings bindings = TypeBindings.defaultBindings();
        JavaType collectionType = CollectionLikeType.construct(Collection.class, bindings, null, null, elemType);
        assertTrue(collectionType.isContainerType());
        assertTrue(collectionType.isCollectionLikeType());
    }

    @Test
    public void testGetErasedSignature() {
        JavaType elemType = TypeFactory.defaultInstance().constructType(int[].class);
        TypeBindings bindings = TypeBindings.defaultBindings();
        JavaType collectionType = CollectionLikeType.construct(Collection.class, bindings, null, null, elemType);
        assertEquals("[collection-like type; class java.util.Collection, contains int[]]", collectionType.getErasedSignature().toString());
    }

    @Test
    public void testGetGenericSignature() {
        JavaType elemType = TypeFactory.defaultInstance().constructType(int[].class);
        TypeBindings bindings = TypeBindings.defaultBindings();
        JavaType collectionType = CollectionLikeType.construct(Collection.class, bindings, null, null, elemType);
        assertEquals("[collection-like type; class java.util.Collection<int[]>]", collectionType.getGenericSignature().toString());
    }

    @Test
    public void testBuildCanonicalName() {
        JavaType elemType = TypeFactory.defaultInstance().constructType(int[].class);
        TypeBindings bindings = TypeBindings.defaultBindings();
        JavaType collectionType = CollectionLikeType.construct(Collection.class, bindings, null, null, elemType);
        assertEquals("[collection-like type; class java.util.Collection<int[]>]", collectionType.buildCanonicalName());
    }

    // Test withContentType, withContentTypeHandler, withValueHandler, withContentValueHandler, withStaticTyping

    @Test
    public void testWithContentType() {
        JavaType elemType = TypeFactory.defaultInstance().constructType(int[].class);
        JavaType contentType = TypeFactory.defaultInstance().constructType(String.class);
        TypeBindings bindings = TypeBindings.defaultBindings();
        JavaType collectionType = CollectionLikeType.construct(Collection.class, bindings, null, null, elemType);
        JavaType newCollectionType = collectionType.withContentType(contentType);
        assertEquals(contentType, newCollectionType.getContentType());
        assertNotEquals(elemType, newCollectionType.getContentType());
    }

    @Test
    public void testWithContentTypeHandler() {
        JavaType elemType = TypeFactory.defaultInstance().constructType(int[].class);
        Object contentTypeHandler = new TypeDeserializer() {};
        TypeBindings bindings = TypeBindings.defaultBindings();
        JavaType collectionType = CollectionLikeType.construct(Collection.class, bindings, null, null, elemType);
        JavaType newCollectionType = collectionType.withContentTypeHandler(contentTypeHandler);
        assertNotNull(newCollectionType.getContentTypeHandler());
    }

    @Test
    public void testWithValueHandler() {
        JavaType elemType = TypeFactory.defaultInstance().constructType(int[].class);
        Object valueHandler = new TypeSerializer() {};
        TypeBindings bindings = TypeBindings.defaultBindings();
        JavaType collectionType = CollectionLikeType.construct(Collection.class, bindings, null, null, elemType);
        JavaType newCollectionType = collectionType.withValueHandler(valueHandler);
        assertNotNull(newCollectionType.getValueHandler());
    }

    @Test
    public void testWithContentValueHandler() {
        JavaType elemType = TypeFactory.defaultInstance().constructType(int[].class);
        Object contentValueHandler = new TypeDeserializer() {};
        TypeBindings bindings = TypeBindings.defaultBindings();
        JavaType collectionType = CollectionLikeType.construct(Collection.class, bindings, null, null, elemType);
        JavaType newCollectionType = collectionType.withContentValueHandler(contentValueHandler);
        assertNotNull(newCollectionType.getContentValueHandler());
    }

    @Test
    public void testWithStaticTyping() {
        JavaType elemType = TypeFactory.defaultInstance().constructType(int[].class);
        TypeBindings bindings = TypeBindings.defaultBindings();
        JavaType collectionType = CollectionLikeType.construct(Collection.class, bindings, null, null, elemType);
        JavaType newCollectionType = collectionType.withStaticTyping();
        assertTrue(newCollectionType.isStaticTyping());
    }

    // Test refine

    @Test
    public void testRefine() {
        JavaType rawType = TypeFactory.defaultInstance().constructType(ArrayList.class);
        TypeBindings bindings = TypeBindings.defaultBindings();
        JavaType superClass = TypeFactory.defaultInstance().constructType(List.class);
        JavaType[] superInterfaces = new JavaType[0];
        JavaType elemType = TypeFactory.defaultInstance().constructType(int[].class);
        JavaType collectionType = CollectionLikeType.refine(rawType, bindings, superClass, superInterfaces, elemType);
        assertEquals(ArrayList.class, collectionType.getRawClass());
        assertEquals(elemType, collectionType.getContentType());
    }
}
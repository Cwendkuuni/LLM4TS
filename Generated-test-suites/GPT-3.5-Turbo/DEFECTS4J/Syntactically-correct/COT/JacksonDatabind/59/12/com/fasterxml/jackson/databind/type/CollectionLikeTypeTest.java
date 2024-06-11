
package com.fasterxml.jackson.databind.type;

import org.junit.Test;

import java.lang.reflect.TypeVariable;
import java.util.Collection;
import com.fasterxml.jackson.databind.JavaType;

public class CollectionLikeTypeTest {

    @Test
    public void testConstruct() {
        Class<?> rawType = Collection.class;
        TypeBindings bindings = TypeBindings.emptyBindings();
        JavaType superClass = JavaType.emptyType();
        JavaType[] superInts = new JavaType[0];
        JavaType elemT = JavaType.emptyType();
        CollectionLikeType collectionType = CollectionLikeType.construct(rawType, bindings, superClass, superInts, elemT);
        // Test the constructor
        assertSame(rawType, collectionType.getRawClass());
        assertSame(bindings, collectionType.getBindings());
        assertSame(superClass, collectionType.getSuperClass());
        assertSame(superInts, collectionType.getInterfaces());
        assertSame(elemT, collectionType.getContentType());
    }

    @Test
    public void testUpgradeFrom() {
        CollectionLikeType baseType = new CollectionLikeType(Collection.class);
        JavaType elementType = JavaType.emptyType();
        CollectionLikeType upgradedType = CollectionLikeType.upgradeFrom(baseType, elementType);
        // Test the upgradeFrom method
        assertSame(CollectionLikeType.class, upgradedType.getClass());
        assertSame(baseType, upgradedType.getBaseType());
        assertSame(elementType, upgradedType.getElementType());
    }

    @Test
    public void testWithContentType() {
        CollectionLikeType baseType = new CollectionLikeType(Collection.class);
        JavaType elementType = JavaType.emptyType();
        CollectionLikeType modifiedType = baseType.withContentType(elementType);
        // Test the withContentType method
        assertSame(CollectionLikeType.class, modifiedType.getClass());
        assertSame(baseType, modifiedType.getBaseType());
        assertSame(elementType, modifiedType.getElementType());
    }

    @Test
    public void testWithTypeHandler() {
        CollectionLikeType baseType = new CollectionLikeType(Collection.class);
        Object handler = new Object();
        CollectionLikeType modifiedType = baseType.withTypeHandler(handler);
        // Test the withTypeHandler method
        assertSame(CollectionLikeType.class, modifiedType.getClass());
        assertSame(baseType, modifiedType.getBaseType());
        assertSame(handler, modifiedType.getTypeHandler());
    }

    @Test
    public void testWithContentTypeHandler() {
        CollectionLikeType baseType = new CollectionLikeType(Collection.class);
        Object handler = new Object();
        CollectionLikeType modifiedType = baseType.withContentTypeHandler(handler);
        // Test the withContentTypeHandler method
        assertSame(CollectionLikeType.class, modifiedType.getClass());
        assertSame(baseType, modifiedType.getBaseType());
        assertSame(handler, modifiedType.getContentTypeHandler());
    }

    @Test
    public void testWithValueHandler() {
        CollectionLikeType baseType = new CollectionLikeType(Collection.class);
        Object handler = new Object();
        CollectionLikeType modifiedType = baseType.withValueHandler(handler);
        // Test the withValueHandler method
        assertSame(CollectionLikeType.class, modifiedType.getClass());
        assertSame(baseType, modifiedType.getBaseType());
        assertSame(handler, modifiedType.getValueHandler());
    }

    @Test
    public void testWithContentValueHandler() {
        CollectionLikeType baseType = new CollectionLikeType(Collection.class);
        Object handler = new Object();
        CollectionLikeType modifiedType = baseType.withContentValueHandler(handler);
        // Test the withContentValueHandler method
        assertSame(CollectionLikeType.class, modifiedType.getClass());
        assertSame(baseType, modifiedType.getBaseType());
        assertSame(handler, modifiedType.getContentValueHandler());
    }

    @Test
    public void testWithStaticTyping1() {
        CollectionLikeType baseType = new CollectionLikeType(Collection.class);
        CollectionLikeType modifiedType = baseType.withStaticTyping();
        // Test the withStaticTyping method
        assertSame(CollectionLikeType.class, modifiedType.getClass());
        assertSame(baseType, modifiedType.getBaseType());
        assertTrue(modifiedType.isStaticTyping());
    }

    @Test
    public void testWithStaticTyping2() {
        CollectionLikeType baseType = new CollectionLikeType(Collection.class);
        baseType.withStaticTyping();
        assertFalse(baseType.isStaticTyping());
    }

    @Test
    public void testRefine() {
        CollectionLikeType baseType = new CollectionLikeType(Collection.class);
        Class<?> rawType = baseType.getRawClass();
        TypeBindings bindings = baseType.getBindings();
        JavaType superClass = baseType.getSuperClass();
        JavaType[] superInterfaces = baseType.getInterfaces();
        JavaType elementType = JavaType.emptyType();
        CollectionLikeType refinedType = baseType.refine(rawType, bindings, superClass, superInterfaces);
        // Test the refine method
        assertSame(CollectionLikeType.class, refinedType.getClass());
        assertSame(rawType, refinedType.getRawClass());
        assertSame(bindings, refinedType.getBindings());
        assertSame(superClass, refinedType.getSuperClass());
        assertSame(superInterfaces, refinedType.getInterfaces());
        assertSame(elementType, refinedType.getContentType());
    }

    @Test
    public void testIsTrueCollectionType() {
        CollectionLikeType collectionType = new CollectionLikeType(Collection.class);
        assertTrue(collectionType.isTrueCollectionType());

        CollectionLikeType type = new CollectionLikeType(Collection.class) {
            @Override
            public boolean isTrueCollectionType() {
                return false;
            }
        };
        assertFalse(type.isTrueCollectionType());
    }
}

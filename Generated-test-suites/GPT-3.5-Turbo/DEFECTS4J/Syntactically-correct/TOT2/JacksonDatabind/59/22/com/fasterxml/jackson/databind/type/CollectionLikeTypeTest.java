package com.fasterxml.jackson.databind.type;

import org.junit.Test;
import static org.junit.Assert.*;
import com.fasterxml.jackson.databind.JavaType;
import java.lang.reflect.TypeVariable;
import java.util.Collection;
import com.fasterxml.jackson.databind.type.CollectionLikeType;

public class CollectionLikeTypeTest {

    @Test
    public void testConstruct() {
        CollectionLikeType type = CollectionLikeType.construct(String.class, null, null, null, JavaType.construct(String.class));
        assertNotNull(type);
        assertEquals(type.getContentType(), JavaType.construct(String.class));
    }

    @Test
    public void testUpgradeFrom() {
        JavaType baseType = JavaType.construct(String.class);
        JavaType elementType = JavaType.construct(String.class);
        CollectionLikeType type = CollectionLikeType.upgradeFrom(baseType, elementType);
        assertNotNull(type);
        assertEquals(type.getContentType(), JavaType.construct(String.class));
    }

    @Test
    public void testIsTrueCollectionType() {
        CollectionLikeType type = CollectionLikeType.construct(Collection.class, null, null, null, JavaType.construct(String.class));
        assertTrue(type.isTrueCollectionType());
    }

    @Test
    public void testEquals() {
        CollectionLikeType type1 = CollectionLikeType.construct(Collection.class, null, null, null, JavaType.construct(String.class));
        CollectionLikeType type2 = CollectionLikeType.construct(Collection.class, null, null, null, JavaType.construct(String.class));
        assertTrue(type1.equals(type2));
    }

    @Test
    public void testToString() {
        CollectionLikeType type = CollectionLikeType.construct(Collection.class, null, null, null, JavaType.construct(String.class));
        assertEquals("[collection-like type; class java.util.Collection, contains java.lang.String]", type.toString());
    }

}
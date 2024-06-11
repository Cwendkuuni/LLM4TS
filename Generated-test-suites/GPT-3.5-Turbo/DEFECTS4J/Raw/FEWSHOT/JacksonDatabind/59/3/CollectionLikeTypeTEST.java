package com.fasterxml.jackson.databind.type;

import org.junit.Test;
import java.util.Collection;
import com.fasterxml.jackson.databind.JavaType;
import static org.junit.Assert.*;

public class CollectionLikeTypeTest {

    @Test
    public void testIsTrueCollectionType() {
        CollectionLikeType type1 = CollectionLikeType.construct(Collection.class, JavaType.INT_TYPE);
        assertTrue(type1.isTrueCollectionType());

        CollectionLikeType type2 = CollectionLikeType.construct(Integer.class, JavaType.INT_TYPE);
        assertFalse(type2.isTrueCollectionType());
    }

    @Test
    public void testGetContentType() {
        CollectionLikeType type = CollectionLikeType.construct(Collection.class, JavaType.STRING_TYPE);
        assertEquals(JavaType.STRING_TYPE, type.getContentType());
    }

    @Test
    public void testHasHandlers() {
        CollectionLikeType type = CollectionLikeType.construct(Collection.class, JavaType.INT_TYPE);
        assertFalse(type.hasHandlers());

        JavaType elementType = JavaType.INT_TYPE.withValueHandler(new TestValueHandler());
        CollectionLikeType typeWithHandlers = CollectionLikeType.construct(Collection.class, elementType);
        assertTrue(typeWithHandlers.hasHandlers());
    }

    @Test
    public void testEquals() {
        CollectionLikeType type1 = CollectionLikeType.construct(Collection.class, JavaType.INT_TYPE);
        CollectionLikeType type2 = CollectionLikeType.construct(Collection.class, JavaType.INT_TYPE);
        assertTrue(type1.equals(type2));

        CollectionLikeType type3 = CollectionLikeType.construct(Collection.class, JavaType.STRING_TYPE);
        assertFalse(type1.equals(type3));

        assertFalse(type1.equals(null));
        assertFalse(type1.equals(new Object()));
    }

    @Test
    public void testToString() {
        CollectionLikeType type = CollectionLikeType.construct(Collection.class, JavaType.INT_TYPE);
        assertEquals("[collection-like type; class java.util.Collection, contains INT]", type.toString());
    }
}
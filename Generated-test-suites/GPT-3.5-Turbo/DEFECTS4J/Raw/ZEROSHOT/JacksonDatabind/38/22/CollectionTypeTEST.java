package com.fasterxml.jackson.databind.type;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class CollectionTypeTest {

    private CollectionType collectionType;

    @Before
    public void setUp() {
        collectionType = new CollectionType(CollectionType.class, null, null, null, null, null, null, false);
    }

    @Test
    public void testConstruct() {
        CollectionType result = CollectionType.construct(CollectionType.class, null, null, null, null);
        assertNotNull(result);
    }

    @Test
    public void test_narrow() {
        JavaType result = collectionType._narrow(CollectionType.class);
        assertNotNull(result);
    }

    @Test
    public void testWithContentType() {
        JavaType result = collectionType.withContentType(null);
        assertNotNull(result);
    }
    
    @Test
    public void testWithTypeHandler() {
        CollectionType result = collectionType.withTypeHandler(null);
        assertNotNull(result);
    }

    @Test
    public void testWithContentTypeHandler() {
        CollectionType result = collectionType.withContentTypeHandler(null);
        assertNotNull(result);
    }

    @Test
    public void testWithValueHandler() {
        CollectionType result = collectionType.withValueHandler(null);
        assertNotNull(result);
    }

    @Test
    public void testWithContentValueHandler() {
        CollectionType result = collectionType.withContentValueHandler(null);
        assertNotNull(result);
    }

    @Test
    public void testWithStaticTyping() {
        CollectionType result = collectionType.withStaticTyping();
        assertNotNull(result);
    }

    @Test
    public void testRefine() {
        JavaType result = collectionType.refine(CollectionType.class, null, null, null);
        assertNotNull(result);
    }

    @Test
    public void testToString() {
        String result = collectionType.toString();
        assertNotNull(result);
    }
}
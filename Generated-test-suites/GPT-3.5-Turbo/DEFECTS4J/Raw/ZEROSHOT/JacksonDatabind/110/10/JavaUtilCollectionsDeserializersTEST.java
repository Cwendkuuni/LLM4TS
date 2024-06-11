package com.fasterxml.jackson.databind.deser.impl;

import org.junit.Test;
import static org.junit.Assert.*;

public class JavaUtilCollectionsDeserializersTest {

    @Test
    public void testFindForCollection() {

        // Test case 1
        DeserializationContext ctxt1 = new DeserializationContext();
        JavaType type1 = new JavaType();
        JsonDeserializer<?> result1 = JavaUtilCollectionsDeserializers.findForCollection(ctxt1, type1);
        assertNull(result1);

        // Test case 2
        DeserializationContext ctxt2 = new DeserializationContext();
        JavaType type2 = new JavaType();
        type2.setRawClass(CLASS_AS_ARRAYS_LIST);
        JsonDeserializer<?> result2 = JavaUtilCollectionsDeserializers.findForCollection(ctxt2, type2);
        assertNotNull(result2);
        assertTrue(result2 instanceof StdDelegatingDeserializer);

        // Test case 3
        DeserializationContext ctxt3 = new DeserializationContext();
        JavaType type3 = new JavaType();
        type3.setRawClass(CLASS_SINGLETON_LIST);
        JsonDeserializer<?> result3 = JavaUtilCollectionsDeserializers.findForCollection(ctxt3, type3);
        assertNotNull(result3);
        assertTrue(result3 instanceof StdDelegatingDeserializer);

        // Test case 4
        DeserializationContext ctxt4 = new DeserializationContext();
        JavaType type4 = new JavaType();
        type4.setRawClass(CLASS_SINGLETON_SET);
        JsonDeserializer<?> result4 = JavaUtilCollectionsDeserializers.findForCollection(ctxt4, type4);
        assertNotNull(result4);
        assertTrue(result4 instanceof StdDelegatingDeserializer);

        // Test case 5
        DeserializationContext ctxt5 = new DeserializationContext();
        JavaType type5 = new JavaType();
        type5.setRawClass(CLASS_UNMODIFIABLE_LIST);
        JsonDeserializer<?> result5 = JavaUtilCollectionsDeserializers.findForCollection(ctxt5, type5);
        assertNotNull(result5);
        assertTrue(result5 instanceof StdDelegatingDeserializer);

        // Test case 6
        DeserializationContext ctxt6 = new DeserializationContext();
        JavaType type6 = new JavaType();
        type6.setRawClass(CLASS_UNMODIFIABLE_SET);
        JsonDeserializer<?> result6 = JavaUtilCollectionsDeserializers.findForCollection(ctxt6, type6);
        assertNotNull(result6);
        assertTrue(result6 instanceof StdDelegatingDeserializer);

    }

    @Test
    public void testFindForMap() {

        // Test case 1
        DeserializationContext ctxt1 = new DeserializationContext();
        JavaType type1 = new JavaType();
        JsonDeserializer<?> result1 = JavaUtilCollectionsDeserializers.findForMap(ctxt1, type1);
        assertNull(result1);

        // Test case 2
        DeserializationContext ctxt2 = new DeserializationContext();
        JavaType type2 = new JavaType();
        type2.setRawClass(CLASS_SINGLETON_MAP);
        JsonDeserializer<?> result2 = JavaUtilCollectionsDeserializers.findForMap(ctxt2, type2);
        assertNotNull(result2);
        assertTrue(result2 instanceof StdDelegatingDeserializer);

        // Test case 3
        DeserializationContext ctxt3 = new DeserializationContext();
        JavaType type3 = new JavaType();
        type3.setRawClass(CLASS_UNMODIFIABLE_MAP);
        JsonDeserializer<?> result3 = JavaUtilCollectionsDeserializers.findForMap(ctxt3, type3);
        assertNotNull(result3);
        assertTrue(result3 instanceof StdDelegatingDeserializer);

    }

}
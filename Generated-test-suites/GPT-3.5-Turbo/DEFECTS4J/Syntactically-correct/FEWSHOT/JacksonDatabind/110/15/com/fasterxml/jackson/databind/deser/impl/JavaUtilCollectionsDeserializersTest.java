package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class JavaUtilCollectionsDeserializersTest {
    @Test
    public void testFindForCollection() {
        // Test with CLASS_AS_ARRAYS_LIST
        assertNull(JavaUtilCollectionsDeserializers.findForCollection(null, null));
        
        //Test with CLASS_SINGLETON_LIST
        assertNull(JavaUtilCollectionsDeserializers.findForCollection(null, null));
        
        //Test with CLASS_SINGLETON_SET
        assertNull(JavaUtilCollectionsDeserializers.findForCollection(null, null));
        
        //Test with CLASS_UNMODIFIABLE_LIST
        assertNull(JavaUtilCollectionsDeserializers.findForCollection(null, null));
        
        //Test with CLASS_UNMODIFIABLE_SET
        assertNull(JavaUtilCollectionsDeserializers.findForCollection(null, null));
    }

    @Test
    public void testFindForMap() {
        // Test with CLASS_SINGLETON_MAP
        assertNull(JavaUtilCollectionsDeserializers.findForMap(null, null));
        
        // Test with CLASS_UNMODIFIABLE_MAP
        assertNull(JavaUtilCollectionsDeserializers.findForMap(null, null));
    }
}
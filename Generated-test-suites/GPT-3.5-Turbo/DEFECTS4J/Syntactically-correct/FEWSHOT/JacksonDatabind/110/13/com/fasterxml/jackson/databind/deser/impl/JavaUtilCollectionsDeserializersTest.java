package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class JavaUtilCollectionsDeserializersTest {
    @Test
    public void testFindForCollection() {
        // Test for TYPE_AS_LIST
        assertNull(JavaUtilCollectionsDeserializers.findForCollection(null, null));

        // Test for TYPE_SINGLETON_LIST
        assertNull(JavaUtilCollectionsDeserializers.findForCollection(null, null));

        // Test for TYPE_SINGLETON_SET
        assertNull(JavaUtilCollectionsDeserializers.findForCollection(null, null));

        // Test for TYPE_UNMODIFIABLE_LIST
        assertNull(JavaUtilCollectionsDeserializers.findForCollection(null, null));

        // Test for TYPE_UNMODIFIABLE_SET
        assertNull(JavaUtilCollectionsDeserializers.findForCollection(null, null));
    }

    @Test
    public void testFindForMap() {
        // Test for TYPE_SINGLETON_MAP
        assertNull(JavaUtilCollectionsDeserializers.findForMap(null, null));

        // Test for TYPE_UNMODIFIABLE_MAP
        assertNull(JavaUtilCollectionsDeserializers.findForMap(null, null));
    }

    @Test
    public void testJavaUtilCollectionsConverterConvert() {
        // Test for TYPE_SINGLETON_SET
        assertNull(new JavaUtilCollectionsDeserializers.JavaUtilCollectionsConverter(JavaUtilCollectionsDeserializers.TYPE_SINGLETON_SET, null).convert(null));

        // Test for TYPE_SINGLETON_LIST
        assertNull(new JavaUtilCollectionsDeserializers.JavaUtilCollectionsConverter(JavaUtilCollectionsDeserializers.TYPE_SINGLETON_LIST, null).convert(null));

        // Test for TYPE_SINGLETON_MAP
        assertNull(new JavaUtilCollectionsDeserializers.JavaUtilCollectionsConverter(JavaUtilCollectionsDeserializers.TYPE_SINGLETON_MAP, null).convert(null));

        // Test for TYPE_UNMODIFIABLE_SET
        assertNull(new JavaUtilCollectionsDeserializers.JavaUtilCollectionsConverter(JavaUtilCollectionsDeserializers.TYPE_UNMODIFIABLE_SET, null).convert(null));

        // Test for TYPE_UNMODIFIABLE_LIST
        assertNull(new JavaUtilCollectionsDeserializers.JavaUtilCollectionsConverter(JavaUtilCollectionsDeserializers.TYPE_UNMODIFIABLE_LIST, null).convert(null));

        // Test for TYPE_UNMODIFIABLE_MAP
        assertNull(new JavaUtilCollectionsDeserializers.JavaUtilCollectionsConverter(JavaUtilCollectionsDeserializers.TYPE_UNMODIFIABLE_MAP, null).convert(null));

        // Test for TYPE_AS_LIST
        assertNull(new JavaUtilCollectionsDeserializers.JavaUtilCollectionsConverter(JavaUtilCollectionsDeserializers.TYPE_AS_LIST, null).convert(null));
    }

    @Test
    public void testJavaUtilCollectionsConverterGetInputType() {
        assertNull(new JavaUtilCollectionsDeserializers.JavaUtilCollectionsConverter(0, null).getInputType(null));
    }

    @Test
    public void testJavaUtilCollectionsConverterGetOutputType() {
        assertNull(new JavaUtilCollectionsDeserializers.JavaUtilCollectionsConverter(0, null).getOutputType(null));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testJavaUtilCollectionsConverterCheckSingleton() {
        new JavaUtilCollectionsDeserializers.JavaUtilCollectionsConverter(0, null)._checkSingleton(0);
    }
}
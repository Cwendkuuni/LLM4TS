package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.Test;

import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.deser.std.StdDelegatingDeserializer;

public class JavaUtilCollectionsDeserializersTest {

    @Test
    public void testFindForCollection() {
        try {
            DeserializationContext ctxt = null;
            JavaType javaType = null;
            JsonDeserializer<?> deserializer = JavaUtilCollectionsDeserializers.findForCollection(ctxt, javaType);

            assertNull(deserializer);

        } catch (JsonMappingException e) {
            fail("Exception thrown when testing findForCollection method");
        }
    }

    @Test
    public void testFindForMap() {
        try {
            DeserializationContext ctxt = null;
            JavaType javaType = null;
            JsonDeserializer<?> deserializer = JavaUtilCollectionsDeserializers.findForMap(ctxt, javaType);

            assertNull(deserializer);

        } catch (JsonMappingException e) {
            fail("Exception thrown when testing findForMap method");
        }
    }

    @Test
    public void testConverter() {
        JavaType concreteType = null;
        Class<?> rawSuper = null;
        JavaUtilCollectionsConverter converter = JavaUtilCollectionsDeserializers.converter(1, concreteType, rawSuper);

        assertNotNull(converter);
        assertTrue(converter instanceof JavaUtilCollectionsConverter);
    }

    @Test
    public void testJavaUtilCollectionsConverter() {
        JavaType inputType = null;
        JavaUtilCollectionsConverter converter = new JavaUtilCollectionsDeserializers.JavaUtilCollectionsConverter(1, inputType);

        assertNotNull(converter);
        assertTrue(converter instanceof JavaUtilCollectionsConverter);
    }

}
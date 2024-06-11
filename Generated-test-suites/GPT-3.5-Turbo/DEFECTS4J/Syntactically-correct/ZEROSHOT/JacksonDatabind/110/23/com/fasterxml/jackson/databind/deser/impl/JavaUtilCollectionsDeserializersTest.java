package com.fasterxml.jackson.databind.deser.impl;

import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.deser.std.StdDelegatingDeserializer;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.fasterxml.jackson.databind.util.Converter;
import org.junit.Assert;
import org.junit.Test;
import java.util.*;

public class JavaUtilCollectionsDeserializersTest {

    @Test
    public void testFindForCollection() {
        DeserializationContext ctxt = new DeserializationContext(null, null);
        JavaType type = TypeFactory.defaultInstance().constructType(ArrayList.class);
        try {
            JsonDeserializer<?> deserializer = JavaUtilCollectionsDeserializers.findForCollection(ctxt, type);
            Assert.assertNotNull(deserializer);
            Assert.assertTrue(deserializer instanceof StdDelegatingDeserializer<?>);
        } catch (JsonMappingException e) {
            Assert.fail("An exception occurred: " + e.getMessage());
        }
    }

    @Test
    public void testFindForMap() {
        DeserializationContext ctxt = new DeserializationContext(null, null);
        JavaType type = TypeFactory.defaultInstance().constructType(HashMap.class);
        try {
            JsonDeserializer<?> deserializer = JavaUtilCollectionsDeserializers.findForMap(ctxt, type);
            Assert.assertNotNull(deserializer);
            Assert.assertTrue(deserializer instanceof StdDelegatingDeserializer<?>);
        } catch (JsonMappingException e) {
            Assert.fail("An exception occurred: " + e.getMessage());
        }
    }

    @Test
    public void testConverter() {
        JavaType concreteType = TypeFactory.defaultInstance().constructType(ArrayList.class);
        JavaType rawSuper = TypeFactory.defaultInstance().constructType(List.class);
        JavaUtilCollectionsDeserializers.JavaUtilCollectionsConverter converter = JavaUtilCollectionsDeserializers.converter(JavaUtilCollectionsDeserializers.TYPE_AS_LIST, concreteType, rawSuper);
        Assert.assertNotNull(converter);
        Assert.assertEquals(JavaUtilCollectionsDeserializers.TYPE_AS_LIST, converter._kind);
        Assert.assertEquals(concreteType, converter._inputType);
    }
}
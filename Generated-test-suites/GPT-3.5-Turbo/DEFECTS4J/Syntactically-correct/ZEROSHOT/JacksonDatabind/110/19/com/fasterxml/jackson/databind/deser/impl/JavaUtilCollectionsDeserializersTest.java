package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.*;
import java.util.*;
import org.junit.Test;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.deser.std.StdDelegatingDeserializer;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.fasterxml.jackson.databind.util.Converter;

public class JavaUtilCollectionsDeserializersTest {

    @Test
    public void testFindForCollection() {
        DeserializationContext ctxt = null;
        JavaType type = null;
        JsonDeserializer<?> result = JavaUtilCollectionsDeserializers.findForCollection(ctxt, type);
        // TODO: Add assertions
    }

    @Test
    public void testFindForMap() {
        DeserializationContext ctxt = null;
        JavaType type = null;
        JsonDeserializer<?> result = JavaUtilCollectionsDeserializers.findForMap(ctxt, type);
        // TODO: Add assertions
    }

    @Test
    public void testConverter() {
        int kind = 0;
        JavaType concreteType = null;
        Class<?> rawSuper = null;
        JavaUtilCollectionsConverter result = JavaUtilCollectionsDeserializers.converter(kind, concreteType, rawSuper);
        // TODO: Add assertions
    }
}
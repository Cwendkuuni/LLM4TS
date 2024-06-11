package com.fasterxml.jackson.databind.deser.impl;

import java.util.*;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.deser.std.StdDelegatingDeserializer;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.fasterxml.jackson.databind.util.Converter;

public class JavaUtilCollectionsDeserializersTest {

    private JavaUtilCollectionsDeserializers deserializers;

    @Before
    public void setUp() {
        deserializers = new JavaUtilCollectionsDeserializers() {
            @Override
            public JsonDeserializer<?> findForCollection(DeserializationContext ctxt, JavaType type) throws JsonMappingException {
                return null;
            }

            @Override
            public JsonDeserializer<?> findForMap(DeserializationContext ctxt, JavaType type) throws JsonMappingException {
                return null;
            }
        };
    }

    @Test
    public void testFindForCollection() throws JsonMappingException {
        DeserializationContext ctxt = null;
        JavaType type = null;
        JsonDeserializer<?> result = deserializers.findForCollection(ctxt, type);
        assertNull(result);
    }

    @Test
    public void testFindForMap() throws JsonMappingException {
        DeserializationContext ctxt = null;
        JavaType type = null;
        JsonDeserializer<?> result = deserializers.findForMap(ctxt, type);
        assertNull(result);
    }

    @Test
    public void testConverter() {
        int kind = 1;
        JavaType concreteType = TypeFactory.defaultInstance().constructType(Object.class);
        Class<?> rawSuper = Object.class;
        JavaUtilCollectionsDeserializers.JavaUtilCollectionsConverter converter = deserializers.converter(kind, concreteType, rawSuper);
        assertNotNull(converter);
        assertEquals(kind, converter._kind);
        assertEquals(concreteType, converter._inputType);
    }
}
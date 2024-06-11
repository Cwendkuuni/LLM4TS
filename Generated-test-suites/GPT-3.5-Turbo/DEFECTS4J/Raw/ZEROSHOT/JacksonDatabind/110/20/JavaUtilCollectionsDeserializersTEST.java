package com.fasterxml.jackson.databind.deser.impl;

import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.deser.std.StdDelegatingDeserializer;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.fasterxml.jackson.databind.util.Converter;
import org.junit.Before;
import org.junit.Test;

import java.util.*;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

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
    public void testFindForCollection() {
        DeserializationContext ctxt = new DeserializationContext(null);
        JavaType type = TypeFactory.defaultInstance().constructType(List.class);

        assertNull(deserializers.findForCollection(ctxt, type));
    }

    @Test
    public void testFindForMap() {
        DeserializationContext ctxt = new DeserializationContext(null);
        JavaType type = TypeFactory.defaultInstance().constructType(Map.class);

        assertNull(deserializers.findForMap(ctxt, type));
    }
    
}
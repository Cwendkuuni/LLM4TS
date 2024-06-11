package com.fasterxml.jackson.databind.deser;

import com.fasterxml.jackson.annotation.ObjectIdGenerator;
import com.fasterxml.jackson.annotation.ObjectIdResolver;
import com.fasterxml.jackson.annotation.SimpleObjectIdResolver;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationConfig;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.DeserializationConfig;
import com.fasterxml.jackson.databind.DeserializerFactory;
import com.fasterxml.jackson.databind.InjectableValues;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.KeyDeserializer;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import com.fasterxml.jackson.databind.ResolvableDeserializer;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.deser.impl.ReadableObjectId;
import com.fasterxml.jackson.databind.deser.impl.ReadableObjectId.Referring;
import com.fasterxml.jackson.databind.util.ClassUtil;
import com.fasterxml.jackson.databind.cfg.HandlerInstantiator;
import java.util.*;
import java.util.Map.Entry;
import java.io.*;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class DefaultDeserializationContextTest {

    private DefaultDeserializationContext deserializationContext;

    @Before
    public void setUp() {
        DeserializerFactory df = new DeserializerFactory();
        DeserializerCache cache = new DeserializerCache();
        deserializationContext = new DefaultDeserializationContext(df, cache);
    }
    
    @Test
    public void testFindObjectId() {
        // TODO: add test logic
    }
    
    @Test
    public void testCheckUnresolvedObjectId() {
        // TODO: add test logic
    }
    
    @Test
    public void testDeserializerInstance() {
        // TODO: add test logic
    }
    
    @Test
    public void testKeyDeserializerInstance() {
        // TODO: add test logic
    }
    
    @Test
    public void testWith() {
        // TODO: add test logic
    }

    @Test
    public void testCreateInstance() {
        // TODO: add test logic
    }

    @Test
    public void testCopy() {
        // TODO: add test logic
    }
}
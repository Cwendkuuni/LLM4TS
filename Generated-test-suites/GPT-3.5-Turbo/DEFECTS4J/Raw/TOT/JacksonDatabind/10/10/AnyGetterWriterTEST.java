package com.fasterxml.jackson.databind.ser;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.ser.std.MapSerializer;

public class AnyGetterWriterTest {

    private AnyGetterWriter anyGetterWriter;
    private BeanProperty property;
    private AnnotatedMember member;
    private MapSerializer serializer;
    private Map<String, Object> map;
    private JsonGenerator generator;
    private SerializerProvider provider;

    @Before
    public void setUp() {
        property = new BeanProperty();
        member = new AnnotatedMember();
        serializer = new MapSerializer();
        anyGetterWriter = new AnyGetterWriter(property, member, serializer);
        map = new HashMap<String, Object>();
        generator = new ObjectMapper().getFactory().createGenerator(System.out);
        provider = new ObjectMapper().getSerializerProvider();
    }

    @Test
    public void testGetAndSerializeNotNullValue() throws IOException {
        map.put("key", "value");
        member.setValue(map);
        
        anyGetterWriter.getAndSerialize(map, generator, provider);
        
        // TODO Assert the expected result
    }
    
    @Test(expected = JsonMappingException.class)
    public void testGetAndSerializeNotMapValue() throws IOException {
        member.setValue("not a map");
        
        anyGetterWriter.getAndSerialize(map, generator, provider);
    }
    
    @Test
    public void testGetAndSerializeNullValue() throws IOException {
        member.setValue(null);
        
        anyGetterWriter.getAndSerialize(map, generator, provider);
        
        // TODO Assert the expected result
    }
    
    @Test
    public void testGetAndFilterNotNullValue() throws IOException {
        map.put("key", "value");
        member.setValue(map);
        
        anyGetterWriter.getAndFilter(map, generator, provider, null);
        
        // TODO Assert the expected result
    }
    
    @Test(expected = JsonMappingException.class)
    public void testGetAndFilterNotMapValue() throws IOException {
        member.setValue("not a map");
        
        anyGetterWriter.getAndFilter(map, generator, provider, null);
    }
    
    @Test
    public void testGetAndFilterNullValue() throws IOException {
        member.setValue(null);
        
        anyGetterWriter.getAndFilter(map, generator, provider, null);
        
        // TODO Assert the expected result
    }
    
    // TODO Add more test cases for other methods
    
}
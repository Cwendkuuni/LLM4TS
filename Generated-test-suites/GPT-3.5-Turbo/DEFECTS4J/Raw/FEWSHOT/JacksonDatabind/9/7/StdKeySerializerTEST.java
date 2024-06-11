package com.fasterxml.jackson.databind.ser.std;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.io.StringWriter;
import java.util.Date;

import static org.junit.Assert.*;

public class StdKeySerializerTest {

    private StdKeySerializer serializer;
    private SerializerProvider provider;
    private JsonGenerator jgen;
    
    @Before
    public void setUp() throws IOException{
        serializer = new StdKeySerializer();
        provider = new ObjectMapper().getSerializerProvider();
        jgen = new JsonFactory().createGenerator(new StringWriter());
    }
    
    @Test
    public void testSerialize_DateValue_DateSerialized() throws IOException {
        Date dateValue = new Date();
        serializer.serialize(dateValue, jgen, provider);
        assertEquals(dateValue.toString(), jgen.getCurrentName());
        jgen.flush();
    }
    
    @Test
    public void testSerialize_ObjectValue_ObjectSerialized() throws IOException {
        Object objectValue = new Object();
        serializer.serialize(objectValue, jgen, provider);
        assertEquals(objectValue.toString(), jgen.getCurrentName());
        jgen.flush();
    }
    
    @Test
    public void testGetSchema_ReturnsStringSchemaNode() throws JsonMappingException {
        assertEquals("string", serializer.getSchema(provider, null).get("type").asText());
    }
    
    @Test
    public void testAcceptJsonFormatVisitor_VisitorExpectsStringFormat() throws IOException, JsonMappingException {
        MockJsonFormatVisitorWrapper visitor = new MockJsonFormatVisitorWrapper();
        serializer.acceptJsonFormatVisitor(visitor, null);
        assertTrue(visitor.isExpectStringFormatCalled());
    }
}
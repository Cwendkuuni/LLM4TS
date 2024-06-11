package com.fasterxml.jackson.databind.ser.std;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.util.Date;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonFormatVisitorWrapper;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

public class StdKeySerializerTest {

    @Mock
    private Object value;
    @Mock
    private JsonGenerator jgen;
    @Mock
    private SerializerProvider provider;
    @Mock
    private JsonFormatVisitorWrapper visitor;
    @Mock
    private JavaType typeHint;

    private StdKeySerializer serializer;

    @Before
    public void setUp() {
        serializer = new StdKeySerializer();
    }

    @Test
    public void testSerializeDate() throws IOException {
        Date date = new Date();
        when(value instanceof Date).thenReturn(true);
        
        serializer.serialize(value, jgen, provider);
        
        verify(provider).defaultSerializeDateKey(date, jgen);
    }

    @Test
    public void testSerialize() throws IOException {
        when(value instanceof Date).thenReturn(false);
        when(value.toString()).thenReturn("test");
        
        serializer.serialize(value, jgen, provider);
        
        verify(jgen).writeFieldName("test");
    }

    @Test
    public void testGetSchema() throws JsonMappingException {
        assertEquals(serializer.createSchemaNode("string"), serializer.getSchema(provider, null));
    }

    @Test
    public void testAcceptJsonFormatVisitor() throws JsonMappingException {
        serializer.acceptJsonFormatVisitor(visitor, typeHint);
        
        verify(visitor).expectStringFormat(typeHint);
    }
}
package com.fasterxml.jackson.databind.ser.std;

import org.junit.Before;
import org.junit.Test;
import static org.mockito.Mockito.*;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;

public class StdSerializerTest {

    private StdSerializer<String> stdSerializer;
    private JsonGenerator jgen;
    private SerializerProvider provider;

    @Before
    public void setUp() throws Exception {
        stdSerializer = mock(StdSerializer.class);
        jgen = mock(JsonGenerator.class);
        provider = mock(SerializerProvider.class);
    }
    
    @Test
    public void testSerialize() throws Exception {
        String value = "test value";
        stdSerializer.serialize(value, jgen, provider);
        verify(stdSerializer).serialize(value, jgen, provider);
    }
    
    @Test
    public void testGetSchema() throws Exception {
        stdSerializer.getSchema(provider, String.class);
        verify(stdSerializer).getSchema(provider, String.class);
    }
    
    @Test
    public void testGetSchemaOptional() throws Exception {
        stdSerializer.getSchema(provider, String.class, true);
        verify(stdSerializer).getSchema(provider, String.class, true);
    }
    
    @Test
    public void testCreateObjectNode() throws Exception {
        stdSerializer.createObjectNode();
        verify(stdSerializer).createObjectNode();
    }
    
    @Test
    public void testCreateSchemaNode() throws Exception {
        stdSerializer.createSchemaNode("string");
        verify(stdSerializer).createSchemaNode("string");
    }
    
    @Test
    public void testCreateSchemaNodeOptional() throws Exception {
        stdSerializer.createSchemaNode("string", true);
        verify(stdSerializer).createSchemaNode("string", true);
    }
    
    @Test
    public void testAcceptJsonFormatVisitor() throws Exception {
        stdSerializer.acceptJsonFormatVisitor(jgen, String.class);
        verify(stdSerializer).acceptJsonFormatVisitor(jgen, String.class);
    }
    
    @Test
    public void testWrapAndThrow() throws Exception {
        Throwable t = new Throwable();
        stdSerializer.wrapAndThrow(provider, t, "test bean", "test field");
        verify(stdSerializer).wrapAndThrow(provider, t, "test bean", "test field");
    }
    
    @Test
    public void testWrapAndThrowIndex() throws Exception {
        Throwable t = new Throwable();
        stdSerializer.wrapAndThrow(provider, t, "test bean", 0);
        verify(stdSerializer).wrapAndThrow(provider, t, "test bean", 0);
    }
    
    @Test
    public void testIsDefaultSerializer() throws Exception {
        stdSerializer.isDefaultSerializer(stdSerializer);
        verify(stdSerializer).isDefaultSerializer(stdSerializer);
    }
    
    @Test
    public void testFindConvertingContentSerializer() throws Exception {
        stdSerializer.findConvertingContentSerializer(provider, null, null);
        verify(stdSerializer).findConvertingContentSerializer(provider, null, null);
    }
    
    @Test
    public void testFindPropertyFilter() throws Exception {
        Object filterId = "test filter";
        Object valueToFilter = "test value";
        stdSerializer.findPropertyFilter(provider, filterId, valueToFilter);
        verify(stdSerializer).findPropertyFilter(provider, filterId, valueToFilter);
    }
}
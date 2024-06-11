package com.fasterxml.jackson.databind.ser.std;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.*;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.Date;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonFormatVisitorWrapper;
import org.junit.Before;
import org.junit.Test;

public class StdKeySerializerTest {
    
    private StdKeySerializer serializer;
    private JsonGenerator jgen;
    private SerializerProvider provider;

    @Before
    public void setUp() {
        serializer = new StdKeySerializer();
        jgen = mock(JsonGenerator.class);
        provider = mock(SerializerProvider.class);
    }

    @Test
    public void testSerialize_Date_shouldDelegateToDefaultSerializeDateKey() throws IOException {
        Date value = new Date();
        serializer.serialize(value, jgen, provider);
        
        verify(provider).defaultSerializeDateKey(value, jgen);
        verifyNoMoreInteractions(jgen, provider);
    }

    @Test
    public void testSerialize_NotDate_shouldSerializeToString() throws IOException {
        Object value = "test";
        serializer.serialize(value, jgen, provider);
        
        verify(jgen).writeFieldName(value.toString());
        verifyNoMoreInteractions(jgen, provider);
    }

    @Test
    public void testGetSchema() throws JsonMappingException {
        Type typeHint = String.class;
        
        JsonNode schemaNode = serializer.getSchema(provider, typeHint);
        
        assertEquals("string", schemaNode.asText());
        verifyNoMoreInteractions(provider);
    }

    @Test
    public void testAcceptJsonFormatVisitor() throws JsonMappingException {
        JavaType typeHint = mock(JavaType.class);
        JsonFormatVisitorWrapper visitor = mock(JsonFormatVisitorWrapper.class);
        
        serializer.acceptJsonFormatVisitor(visitor, typeHint);
        
        verify(visitor).expectStringFormat(typeHint);
        verifyNoMoreInteractions(visitor);
    }
}
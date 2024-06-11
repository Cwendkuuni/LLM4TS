package com.fasterxml.jackson.databind.ser.std;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.Date;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonFormatVisitorWrapper;

public class StdKeySerializerTest {
    
    private StdKeySerializer serializer;
    private SerializerProvider provider;
    private JsonGenerator jgen;
    private JsonNode jsonNode;
    private JavaType typeHint;
    private Date date;
    
    @Before
    public void setup() {
        serializer = new StdKeySerializer();
        provider = mock(SerializerProvider.class);
        jgen = mock(JsonGenerator.class);
        jsonNode = mock(JsonNode.class);
        typeHint = mock(JavaType.class);
        date = new Date();
    }
    
    @Test
    public void serialize_WhenValueIsDate_CallsDefaultSerializeDateKey() throws IOException {
        serializer.serialize(date, jgen, provider);
        verify(provider).defaultSerializeDateKey(date, jgen);
    }
    
    @Test
    public void serialize_WhenValueIsNotDate_WritesFieldName() throws IOException {
        String value = "test";
        serializer.serialize(value, jgen, provider);
        verify(jgen).writeFieldName(value);
    }
    
    @Test
    public void getSchema_ReturnsStringSchemaNode() throws JsonMappingException {
        JsonNode schemaNode = serializer.getSchema(provider, typeHint);
        verify(schemaNode).isTextual();
        verify(schemaNode).asText();
    }
    
    @Test
    public void acceptJsonFormatVisitor_CallsExpectStringFormat() throws JsonMappingException {
        JsonFormatVisitorWrapper visitor = mock(JsonFormatVisitorWrapper.class);
        serializer.acceptJsonFormatVisitor(visitor, typeHint);
        verify(visitor).expectStringFormat(typeHint);
    }

}
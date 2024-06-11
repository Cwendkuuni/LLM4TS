package com.fasterxml.jackson.databind.ser.std;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.Date;

import com.fasterxml.jackson.databind.JsonGenerator;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonFormatVisitorWrapper;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class StdKeySerializerTest {
    @Mock
    private JsonGenerator mockJsonGenerator;
    @Mock
    private SerializerProvider mockSerializerProvider;
    @Mock
    private Type mockType;
    @Mock
    private JsonFormatVisitorWrapper mockJsonFormatVisitorWrapper;

    private StdKeySerializer stdKeySerializer;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        stdKeySerializer = new StdKeySerializer();
    }

    @Test
    public void testSerialize_date() throws IOException {
        Date date = new Date();

        stdKeySerializer.serialize(date, mockJsonGenerator, mockSerializerProvider);

        verify(mockSerializerProvider).defaultSerializeDateKey(date, mockJsonGenerator);
    }

    @Test
    public void testSerialize_notDate() throws IOException {
        Object value = new Object();
        String valueToString = value.toString();

        stdKeySerializer.serialize(value, mockJsonGenerator, mockSerializerProvider);

        verify(mockJsonGenerator).writeFieldName(valueToString);
    }

    @Test
    public void testGetSchema() throws JsonMappingException {
        JsonNode expectedSchemaNode = mock(JsonNode.class);

        JsonNode actualSchemaNode = stdKeySerializer.getSchema(mockSerializerProvider, mockType);

        assertEquals(expectedSchemaNode, actualSchemaNode);
    }

    @Test
    public void testAcceptJsonFormatVisitor() throws JsonMappingException {
        stdKeySerializer.acceptJsonFormatVisitor(mockJsonFormatVisitorWrapper, mock(JsonFormatVisitorWrapper.class));

        verify(mockJsonFormatVisitorWrapper).expectStringFormat(mock(JsonFormatVisitorWrapper.class));
    }

}
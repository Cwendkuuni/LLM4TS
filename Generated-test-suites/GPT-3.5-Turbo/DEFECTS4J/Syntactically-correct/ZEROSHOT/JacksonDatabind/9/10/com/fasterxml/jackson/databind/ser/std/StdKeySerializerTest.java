package com.fasterxml.jackson.databind.ser.std;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonFormatVisitorWrapper;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.Date;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.*;

public class StdKeySerializerTest {

    @Mock
    private JsonGenerator jgen;

    @Mock
    private SerializerProvider provider;

    private StdKeySerializer stdKeySerializer;

    @Before
    public void setup() {
        stdKeySerializer = new StdKeySerializer();
    }

    @Test
    public void testSerialize_DateObject_SerializeDateKeyIsCalled() throws IOException {
        Date date = new Date();
        stdKeySerializer.serialize(date, jgen, provider);
        verify(provider).defaultSerializeDateKey(eq(date), eq(jgen));
    }

    @Test
    public void testSerialize_NonDateObject_ValueToStringIsCalled() throws IOException {
        Object value = new Object();
        stdKeySerializer.serialize(value, jgen, provider);
        verify(jgen).writeFieldName(eq(value.toString()));
    }

    @Test
    public void testGetSchema_ReturnsStringSchemaNode() throws JsonMappingException {
        Type typeHint = Object.class;
        JsonNode schemaNode = stdKeySerializer.getSchema(provider, typeHint);
        assertNotNull(schemaNode);
        assertEquals("string", schemaNode.asText());
    }

    @Test
    public void testAcceptJsonFormatVisitor_WrapperExpectsStringFormat() throws JsonMappingException {
        JavaType typeHint = mock(JavaType.class);
        JsonFormatVisitorWrapper visitor = mock(JsonFormatVisitorWrapper.class);
        stdKeySerializer.acceptJsonFormatVisitor(visitor, typeHint);
        verify(visitor).expectStringFormat(eq(typeHint));
    }
}
package com.fasterxml.jackson.databind.ser.std;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import java.io.IOException;
import java.util.Date;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.SerializerProvider.DefaultSerializers;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonFormatVisitorWrapper;
import com.fasterxml.jackson.databind.type.JavaType;
import org.junit.Before;
import org.junit.Test;

/**
 * Test cases for {@link StdKeySerializer} class.
 */
public class StdKeySerializerTest {

    private SerializerProvider serializerProvider;
    private JsonSerializer<Object> serializer;
    private JsonFormatVisitorWrapper jsonFormatVisitor;

    @Before
    public void setup() {
        serializerProvider = mock(SerializerProvider.class);
        jsonFormatVisitor = mock(JsonFormatVisitorWrapper.class);
        serializer = new StdKeySerializer();
    }

    @Test
    public void testSerializeDate() throws IOException {
        Date date = new Date();
        JsonGenerator jsonGenerator = mock(JsonGenerator.class);

        serializer.serialize(date, jsonGenerator, serializerProvider);

        verify(serializerProvider).defaultSerializeDateKey(date, jsonGenerator);
    }

    @Test
    public void testSerializeString() throws IOException {
        String str = "test";
        JsonGenerator jsonGenerator = mock(JsonGenerator.class);

        serializer.serialize(str, jsonGenerator, serializerProvider);

        verify(jsonGenerator).writeFieldName(str);
    }

    @Test
    public void testGetSchema() throws JsonMappingException {
        JsonNode expectedSchema = createSchemaNode("string");

        JsonNode schema = serializer.getSchema(serializerProvider, JavaType.getRawClass(Object.class));

        assertEquals(expectedSchema, schema);
    }

    @Test
    public void testAcceptJsonFormatVisitor() throws JsonMappingException {
        serializer.acceptJsonFormatVisitor(jsonFormatVisitor, JavaType.getRawClass(Object.class));

        verify(jsonFormatVisitor).expectStringFormat(JavaType.getRawClass(Object.class));
    }
}
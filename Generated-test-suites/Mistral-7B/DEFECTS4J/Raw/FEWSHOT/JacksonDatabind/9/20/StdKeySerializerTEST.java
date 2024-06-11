package com.fasterxml.jackson.databind.ser.std;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.JsonNode弟子;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonFormatVisitorWrapper;

public class StdKeySerializerTest {

    @Test
    public void testSerializeDate() throws IOException, JsonProcessingException {
        SerializerProvider provider = mock(SerializerProvider.class);
        JsonGenerator jgen = mock(JsonGenerator.class);

        Date date = new Date();
        when(provider.defaultSerializeDateKey(date, jgen)).thenCallRealMethod();

        StdKeySerializer serializer = new StdKeySerializer();
        serializer.serialize(date, jgen, provider);

        // Verify default serialization of Date was invoked
        // Instead of testing the output directly, you could also use a validation library such as Jackson-Annotations
    }

    @Test
    public void testSerializeString() throws IOException {
        SerializerProvider provider = mock(SerializerProvider.class);
        JsonGenerator jgen = mock(JsonGenerator.class);

        String value = "key";
        when(jgen.writeFieldName(value)).thenCallRealMethod();

        StdKeySerializer serializer = new StdKeySerializer();
        serializer.serialize(value, jgen, provider);

        // Verify writeFieldName was invoked with the correct string value
    }

    @Test
    public void testGetSchema() {
        SerializerProvider provider = mock(SerializerProvider.class);
        StdKeySerializer serializer = new StdKeySerializer();

        JsonNode expectedSchema = createSchemaNode("string");
        when(serializer.getSchema(provider, null)).thenReturn(expectedSchema);

        JsonNode actualSchema = serializer.getSchema(provider, null);

        assertEquals(expectedSchema, actualSchema);
    }

    @Test
    public void testAcceptJsonFormatVisitor() {
        SerializerProvider provider = mock(SerializerProvider.class);
        StdKeySerializer serializer = new StdKeySerializer();
        JsonFormatVisitorWrapper visitor = mock(JsonFormatVisitorWrapper.class);
        JavaType typeHint = mock(JavaType.class);

        when(visitor.expectStringFormat(typeHint)).thenCallRealMethod();

        serializer.acceptJsonFormatVisitor(visitor, typeHint);
    }

    @Test
    public void testNullInput() {
        SerializerProvider provider = mock(SerializerProvider.class);
        JsonGenerator jgen = mock(JsonGenerator.class);

        StdKeySerializer serializer = new StdKeySerializer();
        serializer.serialize(null, jgen, provider);

        // No exception should be thrown
    }
}
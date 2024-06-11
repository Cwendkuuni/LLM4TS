package com.fasterxml.jackson.databind.ser.std;
import org.junit.Test;
import static org.junit.Assert.*;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.Date;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonFormatVisitorWrapper;

public class StdKeySerializerTest {

    @Test
    public void testSerialize_Date() throws IOException {
        StdKeySerializer serializer = new StdKeySerializer();
        JsonGenerator jgen = new JsonFactory().createGenerator(System.out);

        SerializerProvider provider = new SerializerProvider();

        Date date = new Date();

        serializer.serialize(date, jgen, provider);

        // Assert that the correct key is written
        assertEquals(date.toString(), jgen.getCurrentName());

        jgen.close();
    }

    @Test
    public void testSerialize_Object() throws IOException {
        StdKeySerializer serializer = new StdKeySerializer();
        JsonGenerator jgen = new JsonFactory().createGenerator(System.out);

        SerializerProvider provider = new SerializerProvider();

        Object obj = new Object();

        serializer.serialize(obj, jgen, provider);

        // Assert that the correct key is written
        assertEquals(obj.toString(), jgen.getCurrentName());
        
        jgen.close();
    }

    @Test
    public void testGetSchema() throws JsonMappingException {
        StdKeySerializer serializer = new StdKeySerializer();

        SerializerProvider provider = new SerializerProvider();

        Type typeHint = Object.class;

        JsonNode schemaNode = serializer.getSchema(provider, typeHint);

        // Assert that the correct schema node is returned
        assertEquals("string", schemaNode.asText());
    }

    @Test
    public void testAcceptJsonFormatVisitor() throws JsonMappingException {
        StdKeySerializer serializer = new StdKeySerializer();

        JsonFormatVisitorWrapper visitor = new JsonFormatVisitorWrapper();

        JavaType typeHint = JavaType.Type;

        serializer.acceptJsonFormatVisitor(visitor, typeHint);

        // No assertion possible as this method doesn't have any specific behavior
    }
}
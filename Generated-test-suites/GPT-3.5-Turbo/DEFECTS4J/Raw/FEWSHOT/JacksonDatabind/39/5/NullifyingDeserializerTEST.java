
package com.fasterxml.jackson.databind.deser.std;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;

public class NullifyingDeserializerTest {
    
    @Test
    public void testDeserialize() throws IOException {
        NullifyingDeserializer deserializer = NullifyingDeserializer.instance;
        
        // Test skipping all tokens except FIELD_NAME
        JsonGenerator generator = new JsonFactory().createGenerator(NullOutputStream.NULL_OUTPUT_STREAM, JsonEncoding.UTF8);
        generator.writeStartObject();
        generator.writeFieldName("field");
        generator.writeString("value");
        generator.writeEndObject();
        generator.close();
        
        JsonParser parser = new JsonFactory().createParser(NullInputStream.NULL_INPUT_STREAM);
        parser.nextToken(); // StartObject
        assertNull(deserializer.deserialize(parser, null));
        
        // Test returning null for FIELD_NAME token
        parser = new JsonFactory().createParser(NullInputStream.NULL_INPUT_STREAM);
        parser.nextToken(); // StartObject
        parser.nextToken(); // FieldName
        assertNull(deserializer.deserialize(parser, null));
    }
    
    @Test
    public void testDeserializeWithType() throws IOException {
        NullifyingDeserializer deserializer = NullifyingDeserializer.instance;
        JsonParser parser;
        DeserializationContext ctxt = null;
        TypeDeserializer typeDeserializer = null;
        
        // Test deserializing from START_ARRAY
        parser = new JsonFactory().createParser(NullInputStream.NULL_INPUT_STREAM);
        parser.nextToken(); // StartArray
        assertEquals(null, deserializer.deserializeWithType(parser, ctxt, typeDeserializer));
        
        // Test deserializing from START_OBJECT
        parser = new JsonFactory().createParser(NullInputStream.NULL_INPUT_STREAM);
        parser.nextToken(); // StartObject
        assertEquals(null, deserializer.deserializeWithType(parser, ctxt, typeDeserializer));
        
        // Test deserializing from FIELD_NAME
        parser = new JsonFactory().createParser(NullInputStream.NULL_INPUT_STREAM);
        parser.nextToken(); // StartObject
        parser.nextToken(); // FieldName
        assertEquals(null, deserializer.deserializeWithType(parser, ctxt, typeDeserializer));
        
        // Test deserializing from other tokens
        parser = new JsonFactory().createParser(NullInputStream.NULL_INPUT_STREAM);
        parser.nextToken(); // ValueString
        assertEquals(null, deserializer.deserializeWithType(parser, ctxt, typeDeserializer));
    }
}

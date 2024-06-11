package com.example.test;

import static org.junit.Assert.*;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.core.io.*;
import com.fasterxml.jackson.core.util.*;
import org.junit.Test;

public class JsonGeneratorImplTest {
    @Test public void testWriteStringField() throws IOException {
        JsonGeneratorImpl jsonGenerator = new JsonGeneratorImpl(new IOContext(), 0, new ObjectCodec() {});
        StringFieldWriter writer = new StringFieldWriter("test.json", jsonGenerator);

        jsonGenerator.writeStartObject();
        jsonGenerator.writeFieldName("name");
        jsonGenerator.writeString("John Doe");
        jsonGenerator.writeEndObject();
        jsonGenerator.close();

        String result = writer.toString();
        assertEquals("{\"name\":\"John Doe\"}", result);
    }

    @Test public void testWriteStringFieldWithQuotedFieldNames() throws IOException {
        JsonGeneratorImpl jsonGenerator = new JsonGeneratorImpl(new IOContext(), Feature.QUOTE_FIELD_NAMES.enabledIn(0), new ObjectCodec() {});
        StringFieldWriter writer = new StringFieldWriter("test.json", jsonGenerator);

        jsonGenerator.writeStartObject();
        jsonGenerator.writeFieldName("\"name\""); // Quoted field name
        jsonGenerator.writeString("John Doe");
        jsonGenerator.writeEndObject();
        jsonGenerator.close();

        String result = writer.toString();
        assertEquals("{\"name\":\"John Doe\"}", result);
    }

    @Test(expected = IOException.class)
    public void testWriteStringFieldWithIOException() throws IOException {
        JsonGeneratorImpl jsonGenerator = new JsonGeneratorImpl(new IOContext(), 0, new ObjectCodec() {});
        StringFieldWriter writer = new StringFieldWriter("test.json", jsonGenerator);

        jsonGenerator.writeStartObject();
        jsonGenerator.writeFieldName("name");
        jsonGenerator.writeString("John Doe");
        writer.close(); // Close the writer before the JsonGenerator, this should throw an IOException
        jsonGenerator.writeEndObject();
    }
}
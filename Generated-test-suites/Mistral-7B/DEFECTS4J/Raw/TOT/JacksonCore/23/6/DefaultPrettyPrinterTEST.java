package com.fasterxml.jackson.core.util;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.io.StringWriter;

public class DefaultPrettyPrinterTest
{
    @Test
    public void testDefaultPrettyPrinter() throws JsonProcessingException, JsonParseException, IOException {
        ObjectMapper mapper = new ObjectMapper();
        DefaultPrettyPrinter prettyPrinter = new DefaultPrettyPrinter();
        StringWriter stringWriter = new StringWriter();
        JsonGenerator generation = mapper.getFactory().createGenerator(stringWriter, prettyPrinter);

        // Test root level object with no separator
        generation.writeStartObject();
        generation.writeFieldName("name");
        generation.writeValue("John Doe");
        generation.writeEndObject();
        Assert.assertEquals("{\n\"name\": \"John Doe\"\n}", stringWriter.toString());

        // Test root level array with no separator
        generation.writeStartArray();
        generation.writeValue(1);
        generation.writeValue(2);
        generation.writeEndArray();
        Assert.assertEquals("[1,2]", stringWriter.toString());

        // Test root level object with separator
        generation = mapper.getFactory().createGenerator(stringWriter, new DefaultPrettyPrinter(" "));
        generation.writeStartObject();
        generation.writeFieldName("name");
        generation.writeValue("John Doe");
        generation.writeEndObject();
        Assert.assertEquals("{\n  \"name\": \"John Doe\"\n}", stringWriter.toString());

        // Test root level array with separator
        generation = mapper.getFactory().createGenerator(stringWriter, new DefaultPrettyPrinter(" "));
        generation.writeStartArray();
        generation.writeValue(1);
        generation.writeValue(2);
        generation.writeEndArray();
        Assert.assertEquals("[ 1,\n  2\n]", stringWriter.toString());

        // Test nested object with objectIndenter and arrayIndenter
        generation = mapper.getFactory().createGenerator(stringWriter, new DefaultPrettyPrinter(new DefaultIndenter.SYSTEM_LINEFEED_INSTANCE(), new FixedSpaceIndenter()));
        generation.writeStartObject();
        generation.writeFieldName("name");
        generation.writeValue("John Doe");
        generation.writeFieldName("age");
        generation.writeStartObject();
        generation.writeFieldName("years");
        generation.writeValue(30);
        generation.writeFieldName("months");
        generation.writeValue(12);
        generation.writeEndObject();
        generation.writeEndObject();
        Assert.assertEquals("{\n  \"name\": \"John Doe\",\n  \"age\": {\n    \"years\": 30,\n    \"months\": 12\n  }\n}", stringWriter.toString());

        // Test nested array with objectIndenter and arrayIndenter
        generation = mapper.getFactory().createGenerator(stringWriter, new DefaultPrettyPrinter(new DefaultIndenter.SYSTEM_LINEFEED_INSTANCE(), new FixedSpaceIndenter()));
        generation.writeStartObject();
        generation.writeFieldName("name");
        generation.writeValue("John Doe");
        generation.writeFieldName("hobbies");
        generation.writeStartArray();
        generation.writeValue("reading");
        generation.writeValue("swimming");
        generation.writeValue("painting");
        generation.writeEndArray();
        generation.writeEndObject();
        Assert.assertEquals("{\n  \"name\": \"John Doe\",\n  \"hobbies\": [\n    \"reading\",\n    \"swimming\",\n    \"painting\"\n  ]\n}", stringWriter.toString());
    }
}
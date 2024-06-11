package com.fasterxml.jackson.core.util;

import java.io.IOException;

import org.junit.Test;
import static org.junit.Assert.*;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.core.io.SerializedString;

/**
 * Test class for DefaultPrettyPrinter
 */
public class DefaultPrettyPrinterTest {

    @Test
    public void testDefaultPrettyPrinterConstruction() {
        DefaultPrettyPrinter prettyPrinter = new DefaultPrettyPrinter();
        DefaultPrettyPrinter anotherInstance = new DefaultPrettyPrinter();
        assertNotSame(prettyPrinter, anotherInstance);

        prettyPrinter = new DefaultPrettyPrinter(new SerializedString(" "));
        anotherInstance = new DefaultPrettyPrinter(new SerializedString("  "));
        assertNotSame(prettyPrinter, anotherInstance);

        // Test serialization
        DefaultPrettyPrinter serializedInstance = new DefaultPrettyPrinter(prettyPrinter);
        assertSame(prettyPrinter, serializedInstance);
    }

    @Test
    public void testDefaultPrettyPrinterMethods() throws IOException {
        DefaultPrettyPrinter prettyPrinter = new DefaultPrettyPrinter();

        JsonGenerator jsonGenerator = new JsonFactory().createGenerator(new StringWriter());
        prettyPrinter.writeRootValueSeparator(jsonGenerator);
        JsonFactory jsonFactory = new JsonFactory();
        ObjectMapper mapper = jsonFactory.getObjectMapper();
        Map<String, Object> map = new HashMap<>();
        map.put("key1", "value1");
        map.put("key2", "value2");
        String json = mapper.writer(prettyPrinter).writeValueAsString(map);
        System.out.println(json);
        JsonParser jsonParser = mapper.getFactory().createParser(new StringReader(json));
        Map<String, Object> parsedMap = mapper.readValue(jsonParser, Map.class);
        assertEquals(map, parsedMap);

        prettyPrinter.writeStartObject(jsonGenerator);
        prettyPrinter.beforeObjectEntries(jsonGenerator);
        prettyPrinter.writeObjectFieldValueSeparator(jsonGenerator);
        prettyPrinter.writeObjectEntrySeparator(jsonGenerator);
        prettyPrinter.writeEndObject(jsonGenerator, 1);

        jsonGenerator = new JsonFactory().createGenerator(new StringWriter());
        prettyPrinter.writeStartArray(jsonGenerator);
        prettyPrinter.beforeArrayValues(jsonGenerator);
        prettyPrinter.writeArrayValueSeparator(jsonGenerator);
        prettyPrinter.writeEndArray(jsonGenerator, 1);

        jsonGenerator = new JsonFactory().createGenerator(new StringWriter());
        prettyPrinter.writeStartObject(jsonGenerator);
        prettyPrinter.beforeObjectEntries(jsonGenerator);
        prettyPrinter.writeObjectFieldValueSeparator(jsonGenerator);
        prettyPrinter.writeObjectEntrySeparator(jsonGenerator);
        prettyPrinter.writeEndObject(jsonGenerator, 2);

        jsonGenerator = new JsonFactory().createGenerator(new StringWriter());
        prettyPrinter.writeStartArray(jsonGenerator);
        prettyPrinter.beforeArrayValues(jsonGenerator);
        prettyPrinter.writeArrayValueSeparator(jsonGenerator);
        prettyPrinter.writeEndArray(jsonGenerator, 2);
    }

    @Test
    public void testDefaultPrettyPrinterIndenters() {
        DefaultPrettyPrinter prettyPrinter = new DefaultPrettyPrinter();

        JsonGenerator jsonGenerator = new JsonFactory().createGenerator(new StringWriter());
        prettyPrinter.indentArraysWith(new DefaultPrettyPrinter.FixedSpaceIndenter());
        prettyPrinter.indentObjectsWith(DefaultPrettyPrinter.DefaultIndenter.SYSTEM_LINEFEED_INSTANCE);
        prettyPrinter.writeRootValueSeparator(jsonGenerator);
        JsonFactory jsonFactory = new JsonFactory();
        ObjectMapper mapper = jsonFactory.getObjectMapper();
        Map<String, Object> map = new HashMap<>();
        map.put("key1", "value1");
        map.put("key2", "value2");
        String json = mapper.writer(prettyPrinter).writeValueAsString(map);
        System.out.println(json);
        JsonParser jsonParser = mapper.getFactory().createParser(new StringReader(json));
        Map<String, Object> parsedMap = mapper.readValue(jsonParser, Map.class);
        assertEquals(map, parsedMap);
    }

    @Test
    public void testDefaultPrettyPrinterSpacesInObjectEntries() {
        DefaultPrettyPrinter prettyPrinter = new DefaultPrettyPrinter();

        JsonGenerator jsonGenerator = new JsonFactory().createGenerator(new StringWriter());
        prettyPrinter.withSpacesInObjectEntries();
        prettyPrinter.writeRootValueSeparator(jsonGenerator);
        JsonFactory jsonFactory = new JsonFactory();
        ObjectMapper mapper = jsonFactory.getObjectMapper();
        Map<String, Object> map = new HashMap<>();
        map.put("key1", "value1");
        map.put("key2", "value2");
        String json = mapper.writer(prettyPrinter).writeValueAsString(map);
        System.out.println(json);
        JsonParser jsonParser = mapper.getFactory().createParser(new StringReader(json));
        Map<String, Object> parsedMap = mapper.readValue(jsonParser, Map.class);
        assertEquals(map, parsedMap);

        jsonGenerator = new JsonFactory().createGenerator(new StringWriter());
        prettyPrinter.writeStartObject(jsonGenerator);
        prettyPrinter.beforeObjectEntries(jsonGenerator);
        prettyPrinter.writeObjectFieldValueSeparator(jsonGenerator);
        prettyPrinter.writeObjectEntrySeparator(jsonGenerator);
        prettyPrinter.writeEndObject(jsonGenerator, 1);

        jsonGenerator = new JsonFactory().createGenerator(new StringWriter());
        prettyPrinter.writeStartArray(jsonGenerator);
        prettyPrinter.beforeArrayValues(jsonGenerator);
        prettyPrinter.writeArrayValueSeparator(jsonGenerator);
        prettyPrinter.writeEndArray(jsonGenerator, 1);

        jsonGenerator = new JsonFactory().createGenerator(new StringWriter());
        prettyPrinter.writeStartObject(jsonGenerator);
        prettyPrinter.beforeObjectEntries(jsonGenerator);
        prettyPrinter.writeObjectFieldValueSeparator(jsonGenerator);
        prettyPrinter.writeObjectEntrySeparator(jsonGenerator);
        prettyPrinter.writeEndObject(jsonGenerator, 2);

        jsonGenerator = new JsonFactory().createGenerator(new StringWriter());
        prettyPrinter.writeStartArray(jsonGenerator);
        prettyPrinter.beforeArrayValues(jsonGenerator);
        prettyPrinter.writeArrayValueSeparator(jsonGenerator);
        prettyPrinter.writeEndArray(jsonGenerator, 2);

        jsonGenerator = new JsonFactory().createGenerator(new StringWriter());
        prettyPrinter.writeStartObject(jsonGenerator);
        prettyPrinter.beforeObjectEntries(jsonGenerator);
        prettyPrinter.writeObjectFieldValueSeparator(jsonGenerator);
        prettyPrinter.writeObjectEntrySeparator(jsonGenerator);
        prettyPrinter.writeEndObject(jsonGenerator, 3);

        jsonGenerator = new JsonFactory().createGenerator(new StringWriter());
        prettyPrinter.writeStartArray(jsonGenerator);
        prettyPrinter.beforeArrayValues(jsonGenerator);
        prettyPrinter.writeArrayValueSeparator(jsonGenerator);
        prettyPrinter.writeEndArray(jsonGenerator, 3);

        jsonGenerator = new JsonFactory().createGenerator(new StringWriter());
        prettyPrinter.writeStartObject(jsonGenerator);
        prettyPrinter.beforeObjectEntries(jsonGenerator);
        prettyPrinter.writeObjectFieldValueSeparator(jsonGenerator);
        prettyPrinter.writeObjectEntrySeparator(jsonGenerator);
        prettyPrinter.writeEndObject(jsonGenerator, 4);

        jsonGenerator = new JsonFactory().createGenerator(new StringWriter());
        prettyPrinter.writeStartArray(jsonGenerator);
        prettyPrinter.beforeArrayValues(jsonGenerator);
        prettyPrinter.writeArrayValueSeparator(jsonGenerator);
        prettyPrinter.writeEndArray(jsonGenerator, 4);
    }
}
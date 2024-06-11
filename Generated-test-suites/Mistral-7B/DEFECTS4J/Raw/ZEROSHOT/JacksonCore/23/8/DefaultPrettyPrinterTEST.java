package com.fasterxml.jackson.core.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.IOException;
import java.util.Arrays;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.io.SerializedString;
import com.fasterxml.jackson.databind.ObjectMapper;

public class DefaultPrettyPrinterTest {

    private DefaultPrettyPrinter prettyPrinter;
    private ObjectMapper objectMapper;

    @Before
    public void setup() {
        prettyPrinter = new DefaultPrettyPrinter();
        objectMapper = new ObjectMapper();
    }

    @Test
    public void testDefaultPrettyPrinter() throws JsonProcessingException, IOException {
        String json = "{\"name\":\"John\",\"age\":30,\"cities\":[\"New York\",\"Chicago\",\"Miami\"]}";

        JsonGenerator generator = objectMapper.getFactory().createGenerator(System.out, prettyPrinter);
        objectMapper.writeValue(generator, new TestObject());
        generator.close();

        String output = System.out.getBuffer().toString();
        assertEquals(json, output);
    }

    @Test
    public void testWithRootSeparator() throws JsonProcessingException, IOException {
        DefaultPrettyPrinter prettyPrinter = new DefaultPrettyPrinter(new SerializedString(""));

        String json = ",\"root\":{\"name\":\"John\",\"age\":30,\"cities\":[\"New York\",\"Chicago\",\"Miami\"]}";

        JsonGenerator generator = objectMapper.getFactory().createGenerator(System.out, prettyPrinter);
        objectMapper.writeValue(generator, new TestObject());
        generator.close();

        String output = System.out.getBuffer().toString();
        assertEquals(json, output);
    }

    @Test
    public void testWithArrayIndenter() throws JsonProcessingException, IOException {
        DefaultPrettyPrinter prettyPrinter = new DefaultPrettyPrinter();
        prettyPrinter.indentArraysWith(new Indenter() {
            @Override
            public void writeIndentation(JsonGenerator g, int level) throws IOException {
                g.writeRaw("Array Indenter: ");
            }

            @Override
            public boolean isInline() {
                return false;
            }
        });

        String json = "{\"name\":\"John\",\"age\":30,\"cities\":[\n    \"New York\",\n    \"Chicago\",\n    \"Miami\"\n]}\n";

        JsonGenerator generator = objectMapper.getFactory().createGenerator(System.out, prettyPrinter);
        objectMapper.writeValue(generator, new TestObject());
        generator.close();

        String output = System.out.getBuffer().toString();
        assertEquals(json, output);
    }

    @Test
    public void testWithObjectIndenter() throws JsonProcessingException, IOException {
        DefaultPrettyPrinter prettyPrinter = new DefaultPrettyPrinter();
        prettyPrinter.indentObjectsWith(new Indenter() {
            @Override
            public void writeIndentation(JsonGenerator g, int level) throws IOException {
                g.writeRaw("Object Indenter: ");
            }

            @Override
            public boolean isInline() {
                return false;
            }
        });

        String json = "{\n    \"name\":\"John\",\n    \"age\":30,\n    \"cities\":[\n        \"New York\",\n        \"Chicago\",\n        \"Miami\"\n    ]\n}";

        JsonGenerator generator = objectMapper.getFactory().createGenerator(System.out, prettyPrinter);
        objectMapper.writeValue(generator, new TestObject());
        generator.close();

        String output = System.out.getBuffer().toString();
        assertEquals(json, output);
    }

    @Test
    public void testWithSpacesInObjectEntries() throws JsonProcessingException, IOException {
        DefaultPrettyPrinter prettyPrinter = new DefaultPrettyPrinter();
        prettyPrinter = prettyPrinter.withSpacesInObjectEntries();

        String json = "{\n    \"name\": \"John\",\n    \"age\": 30,\n    \"cities\": [\n        \"New York\",\n        \"Chicago\",\n        \"Miami\"\n    ]\n}";

        JsonGenerator generator = objectMapper.getFactory().createGenerator(System.out, prettyPrinter);
        objectMapper.writeValue(generator, new TestObject());
        generator.close();

        String output = System.out.getBuffer().toString();
        assertEquals(json, output);
    }

    @Test
    public void testWithoutSpacesInObjectEntries() throws JsonProcessingException, IOException {
        DefaultPrettyPrinter prettyPrinter = new DefaultPrettyPrinter();
        prettyPrinter = prettyPrinter.withoutSpacesInObjectEntries();

        String json = "{\"name\": \"John\",\"age\": 30,\"cities\": [\"New York\",\"Chicago\",\"Miami\"]}";

        JsonGenerator generator = objectMapper.getFactory().createGenerator(System.out, prettyPrinter);
        objectMapper.writeValue(generator, new TestObject());
        generator.close();

        String output = System.out.getBuffer().toString();
        assertEquals(json, output);
    }

    @Test
    public void testWithSeparators() throws JsonProcessingException, IOException {
        DefaultPrettyPrinter prettyPrinter = new DefaultPrettyPrinter();
        String objectFieldValueSeparator = ": ";
        String objectEntrySeparator = "\n";
        String arrayValueSeparator = ", ";

        Separators separators = new Separators(objectFieldValueSeparator, objectEntrySeparator, arrayValueSeparator);
        prettyPrinter = prettyPrinter.withSeparators(separators);

        String json = "{\n    \"name\": : John,\n    \"age\": : 30,\n    \"cities\": [\n        , \"New York\",\n        , \"Chicago\",\n        , \"Miami\"\n    ]\n}";

        JsonGenerator generator = objectMapper.getFactory().createGenerator(System.out, prettyPrinter);
        objectMapper.writeValue(generator, new TestObject());
        generator.close();

        String output = System.out.getBuffer().toString();
        assertEquals(json, output);
    }

    static class TestObject {
        String name;
        int age;
        String[] cities;
    }

    static class Separators {
        String objectFieldValueSeparator;
        String objectEntrySeparator;
        String arrayValueSeparator;

        public Separators(String objectFieldValueSeparator, String objectEntrySeparator, String arrayValueSeparator) {
            this.objectFieldValueSeparator = objectFieldValueSeparator;
            this.objectEntrySeparator = objectEntrySeparator;
            this.arrayValueSeparator = arrayValueSeparator;
        }

        public String getObjectFieldValueSeparator() {
            return objectFieldValueSeparator;
        }

        public String getObjectEntrySeparator() {
            return objectEntrySeparator;
        }

        public String getArrayValueSeparator() {
            return arrayValueSeparator;
        }
    }

}
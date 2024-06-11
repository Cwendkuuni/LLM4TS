package com.fasterxml.jackson.core.util;

import static org.junit.Assert.*;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import org.junit.Test;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.version.VersionFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

public class DefaultPrettyPrinterTest {

    @Test
    public void testDefaultPrettyPrinter() throws IOException, JsonProcessingException {
        JsonFactory jsonFactory = new JsonFactory();
        ObjectMapper objectMapper = new ObjectMapper(jsonFactory);

        DefaultPrettyPrinter prettyPrinter = new DefaultPrettyPrinter();
        JsonGenerator jsonGenerator = objectMapper.getFactory().createGenerator(new ByteArrayOutputStream(1024), prettyPrinter);

        String jsonString = "{\"name\":\"John\",\"age\":30,\"hobbies\":[\"reading\",\"swimming\"]}";
        jsonGenerator.writeRaw(jsonString);
        jsonGenerator.close();

        ByteArrayOutputStream outputStream = (ByteArrayOutputStream) jsonGenerator.getOutput();
        String actualJson = outputStream.toString();

        String expectedJson = "{\n" +
                "  \"name\": \"John\",\n" +
                "  \"age\": 30,\n" +
                "  \"hobbies\": [\n" +
                "    \"reading\",\n" +
                "    \"swimming\"\n" +
                "  ]\n" +
                "}";

        assertEquals(expectedJson, actualJson);

        JsonParser jsonParser = objectMapper.getFactory().createParser(new ByteArrayInputStream(outputStream.toByteArray()));
        Object readValue = jsonParser.readValueAsTree();
        assertEquals(expectedJson, readValue.asText());

        JsonGenerator prettyGenerator = jsonGenerator.getPrettyPrinter();
        String prettyJson = prettyGenerator.writeValueAsString(new Object() {
            String name = "John";
            int age = 30;
            String[] hobbies = {"reading", "swimming"};
        });

        assertEquals(expectedJson, prettyJson);
    }

    @Test
    public void testDefaultPrettyPrinterWithRootValueSeparator() throws IOException, JsonProcessingException {
        JsonFactory jsonFactory = new JsonFactory();
        ObjectMapper objectMapper = new ObjectMapper(jsonFactory);

        DefaultPrettyPrinter prettyPrinter = new DefaultPrettyPrinter(": ");
        JsonGenerator jsonGenerator = objectMapper.getFactory().createGenerator(new ByteArrayOutputStream(1024), prettyPrinter);

        String jsonString = "{\"name\":\"John\",\"age\":30,\"hobbies\":[\"reading\",\"swimming\"]}";
        jsonGenerator.writeRaw(jsonString);
        jsonGenerator.close();

        ByteArrayOutputStream outputStream = (ByteArrayOutputStream) jsonGenerator.getOutput();
        String actualJson = outputStream.toString();

        String expectedJson = ":\n" +
                "{\n" +
                "  \"name\": \"John\",\n" +
                "  \"age\": 30,\n" +
                "  \"hobbies\": [\n" +
                "    \"reading\",\n" +
                "    \"swimming\"\n" +
                "  ]\n" +
                "}";

        assertEquals(expectedJson, actualJson);

        JsonParser jsonParser = objectMapper.getFactory().createParser(new ByteArrayInputStream(outputStream.toByteArray()));
        Object readValue = jsonParser.readValueAsTree();
        assertEquals(expectedJson, readValue.asText());
    }

    @Test
    public void testCustomIndenters() throws IOException, JsonProcessingException {
        JsonFactory jsonFactory = new JsonFactory();
        ObjectMapper objectMapper = new ObjectMapper(jsonFactory);

        DefaultPrettyPrinter prettyPrinter = new DefaultPrettyPrinter();
        Indenter objectIndenter = new Indenter() {
            @Override
            public void writeIndentation(JsonGenerator g, int level) throws IOException {
                g.writeRaw("\t");
            }

            @Override
            public boolean isInline() {
                return false;
            }
        };
        Indenter arrayIndenter = new Indenter() {
            @Override
            public void writeIndentation(JsonGenerator g, int level) throws IOException {
                g.writeRaw("  ");
            }

            @Override
            public boolean isInline() {
                return false;
            }
        };

        prettyPrinter.indentObjectsWith(objectIndenter);
        prettyPrinter.indentArraysWith(arrayIndenter);

        JsonGenerator jsonGenerator = objectMapper.getFactory().createGenerator(new ByteArrayOutputStream(1024), prettyPrinter);

        String jsonString = "{\"name\":\"John\",\"age\":30,\"hobbies\":[\"reading\",\"swimming\"]}";
        jsonGenerator.writeRaw(jsonString);
        jsonGenerator.close();

        ByteArrayOutputStream outputStream = (ByteArrayOutputStream) jsonGenerator.getOutput();
        String actualJson = outputStream.toString();

        String expectedJson = "\t{\n" +
                "\t  \"name\": \t\"John\",\n" +
                "\t  \"age\": \t30,\n" +
                "\t  \"hobbies\": [\n" +
                "\t    \"reading\",\n" +
                "\t    \"swimming\"\n" +
                "\t  ]\n" +
                "\t}\n";

        assertEquals(expectedJson, actualJson);

        JsonParser jsonParser = objectMapper.getFactory().createParser(new ByteArrayInputStream(outputStream.toByteArray()));
        Object readValue = jsonParser.readValueAsTree();
        assertEquals(expectedJson, readValue.asText());
    }

    @Test
    public void testDefaultPrettyPrinterWithSpacesInObjectEntries() throws IOException, JsonProcessingException {
        JsonFactory jsonFactory = new JsonFactory();
        ObjectMapper objectMapper = new ObjectMapper(jsonFactory);

        DefaultPrettyPrinter prettyPrinter = new DefaultPrettyPrinter();

        prettyPrinter = prettyPrinter.withSpacesInObjectEntries();

        JsonGenerator jsonGenerator = objectMapper.getFactory().createGenerator(new ByteArrayOutputStream(1024), prettyPrinter);

        String jsonString = "{\"name\":\"John\",\"age\":30,\"hobbies\":[\"reading\",\"swimming\"]}";
        jsonGenerator.writeRaw(jsonString);
        jsonGenerator.close();

        ByteArrayOutputStream outputStream = (ByteArrayOutputStream) jsonGenerator.getOutput();
        String actualJson = outputStream.toString();

        String expectedJson = "{\n" +
                "  \"name\": \"John\",\n" +
                "  \"age\": 30,\n" +
                "  \"hobbies\": [\n" +
                "    \"reading\",\n" +
                "    \"swimming\"\n" +
                "  ]\n" +
                "}";

        assertEquals(expectedJson, actualJson);

        JsonParser jsonParser = objectMapper.getFactory().createParser(new ByteArrayInputStream(outputStream.toByteArray()));
        Object readValue = jsonParser.readValueAsTree();
        assertEquals(expectedJson, readValue.asText());

        JsonGenerator prettyGenerator = jsonGenerator.getPrettyPrinter();
        String prettyJson = prettyGenerator.writeValueAsString(new Object() {
            String name = "John";
            int age = 30;
            String[] hobbies = {"reading", "swimming"};
        });

        assertEquals(expectedJson, prettyJson);
    }

    @Test
    public void testDefaultPrettyPrinterWithoutSpacesInObjectEntries() throws IOException, JsonProcessingException {
        JsonFactory jsonFactory = new JsonFactory();
        ObjectMapper objectMapper = new ObjectMapper(jsonFactory);

        DefaultPrettyPrinter prettyPrinter = new DefaultPrettyPrinter();

        prettyPrinter = prettyPrinter.withoutSpacesInObjectEntries();

        JsonGenerator jsonGenerator = objectMapper.getFactory().createGenerator(new ByteArrayOutputStream(1024), prettyPrinter);

        String jsonString = "{\"name\":\"John\",\"age\":30,\"hobbies\":[\"reading\",\"swimming\"]}";
        jsonGenerator.writeRaw(jsonString);
        jsonGenerator.close();

        ByteArrayOutputStream outputStream = (ByteArrayOutputStream) jsonGenerator.getOutput();
        String actualJson = outputStream.toString();

        String expectedJson = "{\"name\":\"John\",\"age\":30,\"hobbies\":[\"reading\",\"swimming\"]}";

        assertEquals(expectedJson, actualJson);

        JsonParser jsonParser = objectMapper.getFactory().createParser(new ByteArrayInputStream(outputStream.toByteArray()));
        Object readValue = jsonParser.readValueAsTree();
        assertEquals(expectedJson, readValue.asText());

        JsonGenerator prettyGenerator = jsonGenerator.getPrettyPrinter();
        String prettyJson = prettyGenerator.writeValueAsString(new Object() {
            String name = "John";
            int age = 30;
            String[] hobbies = {"reading", "swimming"};
        });

        assertEquals(expectedJson, prettyJson);
    }

    @Test
    public void testDefaultPrettyPrinterWithCustomSeparators() throws IOException, JsonProcessingException {
        JsonFactory jsonFactory = new JsonFactory();
        ObjectMapper objectMapper = new ObjectMapper(jsonFactory);

        DefaultPrettyPrinter prettyPrinter = new DefaultPrettyPrinter();
        Separators separators = new Separators() {
            @Override
            public CharSequence getObjectEntrySeparator() {
                return ", ";
            }

            @Override
            public CharSequence getObjectFieldValueSeparator() {
                return ": ";
            }
        };

        prettyPrinter = prettyPrinter.withSeparators(separators);

        JsonGenerator jsonGenerator = objectMapper.getFactory().createGenerator(new ByteArrayOutputStream(1024), prettyPrinter);

        String jsonString = "{\"name\":\"John\",\"age\":30,\"hobbies\":[\"reading\",\"swimming\"]}";
        jsonGenerator.writeRaw(jsonString);
        jsonGenerator.close();

        ByteArrayOutputStream outputStream = (ByteArrayOutputStream) jsonGenerator.getOutput();
        String actualJson = outputStream.toString();

        String expectedJson = "{\n" +
                "  \"name\": : \"John\",\n" +
                "  \"age\": : 30,\n" +
                "  \"hobbies\": [\n" +
                "    \"reading\",\n" +
                "    \"swimming\"\n" +
                "  ]\n" +
                "}";

        assertEquals(expectedJson, actualJson);

        JsonParser jsonParser = objectMapper.getFactory().createParser(new ByteArrayInputStream(outputStream.toByteArray()));
        Object readValue = jsonParser.readValueAsTree();
        assertEquals(expectedJson, readValue.asText());

        JsonGenerator prettyGenerator = jsonGenerator.getPrettyPrinter();
        String prettyJson = prettyGenerator.writeValueAsString(new Object() {
            String name = "John";
            int age = 30;
            String[] hobbies = {"reading", "swimming"};
        });

        assertEquals(expectedJson, prettyJson);
    }
}

class Separators {
    private CharSequence objectEntrySeparator;
    private CharSequence objectFieldValueSeparator;

    public Separators() {
        this(null, null);
    }

    public Separators(CharSequence objectEntrySeparator, CharSequence objectFieldValueSeparator) {
        this.objectEntrySeparator = objectEntrySeparator != null ? objectEntrySeparator : ",";
        this.objectFieldValueSeparator = objectFieldValueSeparator != null ? objectFieldValueSeparator : ": ";
    }

    public CharSequence getObjectEntrySeparator() {
        return objectEntrySeparator;
    }

    public CharSequence getObjectFieldValueSeparator() {
        return objectFieldValueSeparator;
    }

    public static Separators DEFAULT_SEPARATORS = new Separators();

    public static Separators DEFAULT_SEPARATORS_WITH_COLON = new Separators(", :");

    public static Separators SYSTEM_LINEFEED_INSTANCE = new Separators(System.lineSeparator(), "");
}

class Indenter implements Serializable {
    private static final long serialVersionUID = 1L;

    public abstract void writeIndentation(JsonGenerator g, int level) throws IOException;

    public abstract boolean isInline();
}

class NopIndenter implements Indenter, Serializable {
    private static final long serialVersionUID = 1L;

    @Override
    public void writeIndentation(JsonGenerator g, int level) throws IOException {}

    @Override
    public boolean isInline() {
        return true;
    }
}

class FixedSpaceIndenter extends NopIndenter {
    private static final long serialVersionUID = 1L;

    @Override
    public void writeIndentation(JsonGenerator g, int level) throws IOException {
        g.writeRaw(' ');
    }
}
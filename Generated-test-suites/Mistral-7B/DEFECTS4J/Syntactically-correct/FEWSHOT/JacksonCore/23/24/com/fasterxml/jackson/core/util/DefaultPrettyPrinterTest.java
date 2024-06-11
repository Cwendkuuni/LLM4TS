package org.apache.commons.cli.util;

import static org.junit.Assert.*;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import org.junit.Test;

public class DefaultPrettyPrinterTest {

    @Test
    public void testDefaultPrettyPrinter() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.setPrettyPrinter(new DefaultPrettyPrinter());

        String json = mapper.writeValueAsString(new Object());
        System.out.println(json);

        JsonFactory factory = new JsonFactory();
        JsonParser parser = factory.createParser(new ByteArrayInputStream(json.getBytes()));

        assertTrue(parser.nextToken().isStartObject());
        assertTrue(parser.nextToken().isFieldName());
        assertEquals("class", parser.getCurrentName());
        assertTrue(parser.nextToken().isValueIdentifier());
        assertEquals("java.lang.Object", parser.getText());

        assertTrue(parser.nextToken().isEndObject());
    }

    @Test
    public void testWithRootSeparator() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.setPrettyPrinter(new DefaultPrettyPrinter("root:"));

        String json = mapper.writeValueAsString(new Object());
        System.out.println(json);

        JsonFactory factory = new JsonFactory();
        JsonParser parser = factory.createParser(new ByteArrayInputStream(json.getBytes()));

        assertTrue(parser.nextToken().isStartObject());
        assertEquals("root:", parser.getText());
        assertTrue(parser.nextToken().isFieldName());
        assertEquals("class", parser.getCurrentName());
        assertTrue(parser.nextToken().isValueIdentifier());
        assertEquals("java.lang.Object", parser.getText());

        assertTrue(parser.nextToken().isEndObject());
    }

    @Test
    public void testWithArrayIndenter() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.setPrettyPrinter(new DefaultPrettyPrinter(new Indenter() {
            @Override
            public void writeIndentation(JsonGenerator g, int level) throws IOException {
                g.writeRaw(" indentation-");
                g.writeNumber(level);
                g.writeRaw("-");
            }

            @Override
            public boolean isInline() {
                return false;
            }
        }));

        String json = mapper.writeValueAsString(new int[] {1, 2, 3});
        System.out.println(json);

        JsonFactory factory = new JsonFactory();
        JsonParser parser = factory.createParser(new ByteArrayInputStream(json.getBytes()));

        assertTrue(parser.nextToken().isStartArray());
        assertEquals(" indentation-0-", parser.getText());
        assertTrue(parser.nextToken().isValueNumber());
        assertEquals(1, parser.getValueAsInt());

        assertTrue(parser.nextToken().isValueNumber());
        assertEquals(2, parser.getValueAsInt());

        assertTrue(parser.nextToken().isValueNumber());
        assertEquals(3, parser.getValueAsInt());

        assertTrue(parser.nextToken().isEndArray());
    }

    @Test
    public void testWithObjectIndenter() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.setPrettyPrinter(new DefaultPrettyPrinter(new Indenter() {
            @Override
            public void writeIndentation(JsonGenerator g, int level) throws IOException {
                g.writeRaw(" indentation-");
                g.writeNumber(level);
                g.writeRaw("-");
            }

            @Override
            public boolean isInline() {
                return false;
            }
        }));

        String json = mapper.writeValueAsString(new Person("John Doe"));
        System.out.println(json);

        JsonFactory factory = new JsonFactory();
        JsonParser parser = factory.createParser(new ByteArrayInputStream(json.getBytes()));

        assertTrue(parser.nextToken().isStartObject());
        assertEquals(" indentation-0-", parser.getText());
        assertTrue(parser.nextToken().isFieldName());
        assertEquals("name", parser.getCurrentName());
        assertTrue(parser.nextToken().isValueString());
        assertEquals("John Doe", parser.getText());

        assertTrue(parser.nextToken().isEndObject());
    }

    @Test
    public void testWithSpacesInObjectEntries() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.setPrettyPrinter(new DefaultPrettyPrinter().withSpacesInObjectEntries());

        String json = mapper.writeValueAsString(new Person("John Doe"));
        System.out.println(json);

        JsonFactory factory = new JsonFactory();
        JsonParser parser = factory.createParser(new ByteArrayInputStream(json.getBytes()));

        assertTrue(parser.nextToken().isStartObject());
        assertTrue(parser.nextToken().isFieldName());
        assertEquals("name", parser.getCurrentName());
        assertTrue(parser.nextToken().isValueString());
        assertTrue(parser.getText().contains(": "));
        assertEquals("John Doe", parser.getText().substring(parser.getText().indexOf('"') + 1));

        assertTrue(parser.nextToken().isEndObject());
    }

    @Test
    public void testWithSeparators() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.setPrettyPrinter(new DefaultPrettyPrinter().withSeparators(new Separators() {
            @Override
            public String getObjectFieldValueSeparator() {
                return " : ";
            }

            @Override
            public String getObjectEntrySeparator() {
                return "\n";
            }

            @Override
            public String getArrayValueSeparator() {
                return ",";
            }
        }));

        String json = mapper.writeValueAsString(new Person("John Doe"));
        System.out.println(json);

        JsonFactory factory = new JsonFactory();
        JsonParser parser = factory.createParser(new ByteArrayInputStream(json.getBytes()));

        assertTrue(parser.nextToken().isStartObject());
        assertTrue(parser.getText().contains("\n"));
        assertTrue(parser.nextToken().isFieldName());
        assertEquals("name", parser.getCurrentName());
        assertTrue(parser.nextToken().isValueString());
        assertTrue(parser.getText().contains(": "));
        assertEquals("John Doe", parser.getText().substring(parser.getText().indexOf('"') + 1));

        assertTrue(parser.nextToken().isEndObject());
    }
}

class Person {
    private final String name;

    public Person(String name) {
        this.name = name;
    }
}

interface Indenter {
    void writeIndentation(JsonGenerator g, int level) throws IOException;

    boolean isInline();
}

class Separators {
    public String getObjectFieldValueSeparator() {
        return " : ";
    }

    public String getObjectEntrySeparator() {
        return "\n";
    }

    public String getArrayValueSeparator() {
        return ",";
    }
}
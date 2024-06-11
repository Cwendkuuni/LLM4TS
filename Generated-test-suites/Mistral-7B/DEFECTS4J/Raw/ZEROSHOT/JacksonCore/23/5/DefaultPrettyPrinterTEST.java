package com.fasterxml.jackson.core.util;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.Arrays;

import org.junit.Test;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.io.SerializedString;
import com.fasterxml.jackson.databind.ObjectMapper;

public class DefaultPrettyPrinterTest {

    @Test
    public void testDefaultPrettyPrinter() throws IOException, JsonProcessingException {
        DefaultPrettyPrinter prettyPrinter = new DefaultPrettyPrinter();

        String json = mapper.writeValueAsString(new Person("John", 30));
        JsonGenerator generator = mapper.getFactory().createGenerator(new ByteArrayOutputStream(), prettyPrinter);
        mapper.writeValue(generator, new Person("John", 30));
        generator.flush();

        String prettyJson = generator.getOutputContentAsString();
        assertEquals(json, prettyJson);
    }

    @Test
    public void testRootValueSeparator() throws IOException, JsonProcessingException {
        DefaultPrettyPrinter prettyPrinter = new DefaultPrettyPrinter(":");

        String json = mapper.writeValueAsString(new Person("John", 30));
        JsonGenerator generator = mapper.getFactory().createGenerator(new ByteArrayOutputStream(), prettyPrinter);
        mapper.writeValue(generator, new Person("John", 30));
        generator.flush();

        String prettyJson = generator.getOutputContentAsString();
        String expected = "{\"name\":\"John\",\"age\":30}";
        assertEquals(expected, prettyJson);
    }

    @Test
    public void testObjectIndenter() throws IOException, JsonProcessingException {
        DefaultPrettyPrinter prettyPrinter = new DefaultPrettyPrinter();
        prettyPrinter.indentObjectsWith(new Indenter() {
            @Override
            public void writeIndentation(JsonGenerator g, int level) throws IOException {
                g.writeRaw("--");
            }

            @Override
            public boolean isInline() {
                return false;
            }
        });

        String json = mapper.writeValueAsString(new Person("John", 30));
        JsonGenerator generator = mapper.getFactory().createGenerator(new ByteArrayOutputStream(), prettyPrinter);
        mapper.writeValue(generator, new Person("John", 30));
        generator.flush();

        String prettyJson = generator.getOutputContentAsString();
        String expected = "--{\n-- name:\n-- John,\n-- age:\n-- 30\n--}";
        assertEquals(expected, prettyJson);
    }

    @Test
    public void testArrayIndenter() throws IOException, JsonProcessingException {
        DefaultPrettyPrinter prettyPrinter = new DefaultPrettyPrinter();
        prettyPrinter.indentArraysWith(new Indenter() {
            @Override
            public void writeIndentation(JsonGenerator g, int level) throws IOException {
                g.writeRaw("--");
            }

            @Override
            public boolean isInline() {
                return false;
            }
        });

        Person[] persons = new Person[] {new Person("John", 30), new Person("Alice", 25)};

        String json = mapper.writeValueAsString(persons);
        JsonGenerator generator = mapper.getFactory().createGenerator(new ByteArrayOutputStream(), prettyPrinter);
        mapper.writeValue(generator, persons);
        generator.flush();

        String prettyJson = generator.getOutputContentAsString();
        String expected = "--[\n-- John,\n-- age:30,\n-- Alice,\n-- age:25\n--]";
        assertEquals(expected, prettyJson);
    }

    @Test
    public void testRootSeparator() throws IOException, JsonProcessingException {
        DefaultPrettyPrinter prettyPrinter = new DefaultPrettyPrinter("|");

        String json = mapper.writeValueAsString(new Person("John", 30));
        JsonGenerator generator = mapper.getFactory().createGenerator(new ByteArrayOutputStream(), prettyPrinter);
        mapper.writeValue(generator, new Person("John", 30));
        generator.flush();

        String prettyJson = generator.getOutputContentAsString();
        String expected = "|{\n| name:\n| John,\n| age:\n| 30\n|}";
        assertEquals(expected, prettyJson);
    }

    @Test
    public void testWithSpacesInObjectEntries() throws IOException, JsonProcessingException {
        DefaultPrettyPrinter prettyPrinter = new DefaultPrettyPrinter();
        DefaultPrettyPrinter prettyPrinterWithSpaces = prettyPrinter.withSpacesInObjectEntries();

        String json = mapper.writeValueAsString(new Person("John", 30));
        JsonGenerator generator = mapper.getFactory().createGenerator(new ByteArrayOutputStream(), prettyPrinter);
        mapper.writeValue(generator, new Person("John", 30));
        generator.flush();

        String prettyJsonWithoutSpaces = generator.getOutputContentAsString();
        String prettyJsonWithSpaces = mapper.writeValueAsString(new Person("John", 30), prettyPrinterWithSpaces);

        String expected = "{\n  name:\n  John,\n  age:\n  30\n}";
        assertEquals(expected, prettyJsonWithSpaces);
        assertNotEquals(expected, prettyJsonWithoutSpaces);
    }

    @Test
    public void testWithoutSpacesInObjectEntries() throws IOException, JsonProcessingException {
        DefaultPrettyPrinter prettyPrinter = new DefaultPrettyPrinter();
        DefaultPrettyPrinter prettyPrinterWithoutSpaces = prettyPrinter.withoutSpacesInObjectEntries();

        String json = mapper.writeValueAsString(new Person("John", 30));
        JsonGenerator generator = mapper.getFactory().createGenerator(new ByteArrayOutputStream(), prettyPrinter);
        mapper.writeValue(generator, new Person("John", 30));
        generator.flush();

        String prettyJsonWithoutSpaces = generator.getOutputContentAsString();
        String prettyJsonWithSpaces = mapper.writeValueAsString(new Person("John", 30), prettyPrinter);

        String expected = "{\"name\":\"John\",\"age\":30}";
        assertNotEquals(expected, prettyJsonWithoutSpaces);
        assertEquals(expected, prettyJsonWithSpaces);
    }

    private static class Person {
        private final String name;
        private final int age;

        public Person(String name, int age) {
            this.name = name;
            this.age = age;
        }

        public String getName() {
            return name;
        }

        public int getAge() {
            return age;
        }

        @Override
        public String toString() {
            return "{\"name\":\"" + name + "\",\"age\": " + age + "}";
        }
    }

    private static ObjectMapper mapper = new ObjectMapper();
}

class Indenter {
    public abstract void writeIndentation(JsonGenerator g, int level) throws IOException;

    public abstract boolean isInline();
}
package com.fasterxml.jackson.core.util;

import org.junit.Test;
import static org.junit.Assert.*;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.io.SerializedString;
import com.fasterxml.jackson.databind.JsonMappingException;

public class DefaultPrettyPrinterTest {

    @Test
    public void testDefaultPrettyPrinter() throws JsonProcessingException, JsonParseException, JsonMappingException {
        JsonFactory factory = new JsonFactory();
        DefaultPrettyPrinter printer = factory.getCodec().defaultPrettyPrinter();

        String json = "{\"name\":\"John\",\"age\":30,\"hobbies\":[\"football\",\"reading\"]}";
        JsonGenerator gen = factory.createGenerator(System.out, printer);
        gen.writeRaw(json);
        gen.close();

        String prettyPrintedJson = "{\n" +
                "  \"name\": \"John\",\n" +
                "  \"age\": 30,\n" +
                "  \"hobbies\": [\n" +
                "    \"football\",\n" +
                "    \"reading\"\n" +
                "  ]\n" +
                "}";
        assertEquals(prettyPrintedJson, json);
    }

    @Test
    public void testIndentObjectsWithCustomIndenter() throws JsonProcessingException {
        JsonFactory factory = new JsonFactory();
        Indenter customIndenter = new Indenter() {
            @Override
            public void writeIndentation(JsonGenerator g, int level) throws IOException {
                g.writeRaw("CustomIndentation_");
            }

            @Override
            public boolean isInline() {
                return false;
            }
        };
        DefaultPrettyPrinter printer = factory.getCodec().prettyPrinter();
        printer.indentObjectsWith(customIndenter);

        String json = "{\"name\":\"John\",\"age\":30,\"hobbies\":[\"football\",\"reading\"]}";
        JsonGenerator gen = factory.createGenerator(System.out, printer);
        gen.writeRaw(json);
        gen.close();

        String prettyPrintedJson = "{\n" +
                "  \"name\": \"John\",\n" +
                "  \"age\": 30,\n" +
                "  \"hobbies\": [\n" +
                "    \"football\",\n" +
                "    \"reading\"\n" +
                "  ]\n" +
                "}\n" +
                "CustomIndentation_CustomIndentation_CustomIndentation_CustomIndentation_CustomIndentation_";
        assertEquals(prettyPrintedJson, gen.getOutputContent().toString());
    }

    @Test
    public void testIndentArraysWithCustomIndenter() throws JsonProcessingException {
        JsonFactory factory = new JsonFactory();
        Indenter customIndenter = new Indenter() {
            @Override
            public void writeIndentation(JsonGenerator g, int level) throws IOException {
                g.writeRaw("CustomIndentation_");
            }

            @Override
            public boolean isInline() {
                return false;
            }
        };
        DefaultPrettyPrinter printer = factory.getCodec().prettyPrinter();
        printer.indentArraysWith(customIndenter);

        String json = "[1, 2, 3, 4, 5]";
        JsonGenerator gen = factory.createGenerator(System.out, printer);
        gen.writeRaw(json);
        gen.close();

        String prettyPrintedJson = "[1,\n" +
                "  2,\n" +
                "  3,\n" +
                "  4,\n" +
                "  5]\n" +
                "CustomIndentation_CustomIndentation_CustomIndentation_CustomIndentation_CustomIndentation_CustomIndentation_";
        assertEquals(prettyPrintedJson, gen.getOutputContent().toString());
    }

    @Test
    public void testWriteRootValueSeparator() throws JsonProcessingException {
        JsonFactory factory = new JsonFactory();
        DefaultPrettyPrinter printer = factory.getCodec().defaultPrettyPrinter();
        String rootSeparator = "Root separator: ";
        JsonGenerator gen = factory.createGenerator(System.out, printer);

        printer.writeRootValueSeparator(gen);
        gen.writeRaw(rootSeparator);
        gen.writeRaw("hello");
        gen.close();

        String prettyPrintedJson = "Root separator: hello";
        assertEquals(prettyPrintedJson, gen.getOutputContent().toString());
    }
}
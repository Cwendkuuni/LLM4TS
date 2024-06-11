package com.fasterxml.jackson.core.util;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.core.PrettyPrinter;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.Test;
import static org.junit.Assert.*;

public class DefaultPrettyPrinterTest {

    @Test
    public void testDefaultPrettyPrinter() throws Exception {
        JsonFactory jsonFactory = new JsonFactory();
        ObjectMapper objectMapper = new ObjectMapper(jsonFactory);

        // Test with default settings
        PrettyPrinter printer = jsonFactory.getSerializerProvider().getPrettyPrinter();
        String json1 = testPrint("{ \"key\": \"value\" }", printer);
        String json2 = testPrint("[1, 2, 3]", printer);
        assertEquals("{ \"key\": \"value\" }\n", json1);
        assertEquals("[1, 2, 3]\n", json2);

        // Test custom root separator
        DefaultPrettyPrinter customPrinter = new DefaultPrettyPrinter();
        customPrinter.withRootSeparator("[Custom Separator]");
        String json3 = testPrint("{ \"key\": \"value\" }", customPrinter);
        String json4 = testPrint("[1, 2, 3]", customPrinter);
        assertEquals("[Custom Separator]{ \"key\": \"value\" }\n", json3);
        assertEquals("[Custom Separator][1, 2, 3]\n", json4);

        // Test indentation settings
        DefaultPrettyPrinter indenterPrinter = new DefaultPrettyPrinter();
        indenterPrinter.indentObjectsWith(new Indenter() {
            @Override
            public void writeIndentation(JsonGenerator g, int level) throws IOException {
                g.writeRaw("Tab");
            }

            @Override
            public boolean isInline() {
                return false;
            }
        });
        String json5 = testPrint("{ \"key\": \"value\" }", indenterPrinter);
        String json6 = testPrint("[1, 2, 3]", indenterPrinter);
        assertEquals("Tab{ \"key\": \"value\" }\n", json5);
        assertEquals("Tab[1, 2, 3]\n", json6);

        // Test spaces in object entries
        DefaultPrettyPrinter spacesPrinter = new DefaultPrettyPrinter();
        spacesPrinter.withSpacesInObjectEntries();
        String json7 = testPrint("{\"key\": 1}", spacesPrinter);
        String json8 = testPrint("{\"key\": [1, 2]}", spacesPrinter);
        assertEquals("{\"key\": 1}\n", json7);
        assertEquals("{\"key\": [\n  1,\n  2]\n}", json8);

        // Test error scenario: null input
        String json9 = testPrint((String) null, printer);
        assertEquals("null", json9);

        // Test error scenario: empty string input
        String json10 = testPrint("", printer);
        assertEquals("", json10);
    }

    private String testPrint(String input, PrettyPrinter printer) throws Exception {
        ObjectCodec objectCodec = printer.getCodec();
        JsonNode jsonNode = objectCodec.readTree(input);
        StringWriter stringWriter = new StringWriter();
        JsonGenerator jsonGenerator = objectCodec.getJsonFactory().createJsonGenerator(stringWriter);
        printer.writeValueAsString(jsonNode, jsonGenerator);
        jsonGenerator.close();
        return stringWriter.toString();
    }
}
package com.fasterxml.jackson.core.util;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class DefaultPrettyPrinterTest {

    private ObjectMapper mapper;

    @Before
    public void setup() {
        mapper = new ObjectMapper();
    }

    /**
     * Test for default constructor with no root separator.
     */
    @Test
    public void testDefaultConstructor() throws JsonProcessingException {
        JsonFactory jsonFactory = mapper.getFactory();
        JsonGenerator jsonGenerator = jsonFactory.createGenerator(System.out);
        jsonGenerator.useDefaultPrettyPrinter();

        Object node = mapper.createValue("{\"name\": \"John\", \"age\": 30, \"cars\": [\"Ford\", \"BMW\", \"Fiat\"]}");
        jsonGenerator.writeValue(node);
        jsonGenerator.flush();

        JsonParser jsonParser = jsonFactory.createParser(new ByteArrayInputStream(jsonGenerator.getOutputBytes()));
        assertEquals("{\"name\": \"John\", \"age\": 30, \"cars\": [\"Ford\", \"BMW\", \"Fiat\"]}", jsonParser.readValueAsString());
    }

    /**
     * Test for constructor with root separator.
     */
    @Test
    public void testConstructorWithRootSeparator() throws JsonProcessingException {
        JsonFactory jsonFactory = mapper.getFactory();
        JsonGenerator jsonGenerator = jsonFactory.createGenerator(System.out);
        jsonGenerator.useDefaultPrettyPrinter(new SerializedString("----"));

        Object node = mapper.createValue("{\"name\": \"John\", \"age\": 30, \"cars\": [\"Ford\", \"BMW\", \"Fiat\"]}");
        jsonGenerator.writeValue(node);
        jsonGenerator.flush();

        JsonParser jsonParser = jsonFactory.createParser(new ByteArrayInputStream(jsonGenerator.getOutputBytes()));
        assertEquals("----{\"name\": \"John\", \"age\": 30, \"cars\": [\"Ford\", \"BMW\", \"Fiat\"]}", jsonParser.readValueAsString());
    }

    /**
     * Test for indentArraysWith method.
     */
    @Test
    public void testIndentArraysWith() throws JsonProcessingException {
        JsonFactory jsonFactory = mapper.getFactory();
        DefaultPrettyPrinter prettyPrinter = new DefaultPrettyPrinter();
        JsonGenerator jsonGenerator = jsonFactory.createGenerator(System.out);
        prettyPrinter.use(jsonGenerator);

        prettyPrinter.indentArraysWith(new FixedSpaceIndenter());

        Object node = mapper.createValue("{\"name\": \"John\", \"age\": 30, \"cars\": [\"Ford\", \"BMW\", \"Fiat\"]}");
        jsonGenerator.writeValue(node);
        jsonGenerator.flush();

        JsonParser jsonParser = jsonFactory.createParser(new ByteArrayInputStream(jsonGenerator.getOutputBytes()));
        assertEquals("{\"name\": \"John\", \"age\": 30,\"cars\": [\"Ford\", \"BMW\", \"Fiat\"]}", jsonParser.readValueAsString());
    }

    /**
     * Test for indentObjectsWith method.
     */
    @Test
    public void testIndentObjectsWith() throws JsonProcessingException {
        JsonFactory jsonFactory = mapper.getFactory();
        DefaultPrettyPrinter prettyPrinter = new DefaultPrettyPrinter();
        JsonGenerator jsonGenerator = jsonFactory.createGenerator(System.out);
        prettyPrinter.use(jsonGenerator);

        prettyPrinter.indentObjectsWith(new DefaultIndenter.SYSTEM_LINEFEED_INSTANCE());

        Object node = mapper.createValue("{\"name\": \"John\", \"age\": 30, \"cars\": [\"Ford\", \"BMW\", \"Fiat\"]}");
        jsonGenerator.writeValue(node);
        jsonGenerator.flush();

        JsonParser jsonParser = jsonFactory.createParser(new ByteArrayInputStream(jsonGenerator.getOutputBytes()));
        assertEquals("{\n    \"name\": \"John\",\n    \"age\": 30,\n    \"cars\": [\n        \"Ford\",\n        \"BMW\",\n        \"Fiat\"\n    ]\n}", jsonParser.readValueAsString());
    }

    /**
     * Test for withRootSeparator method.
     */
    @Test
    public void testWithRootSeparator() throws JsonProcessingException {
        JsonFactory jsonFactory = mapper.getFactory();
        DefaultPrettyPrinter prettyPrinter = new DefaultPrettyPrinter();
        JsonGenerator jsonGenerator = jsonFactory.createGenerator(System.out);
        prettyPrinter.use(jsonGenerator);

        prettyPrinter = prettyPrinter.withRootSeparator(new SerializedString("---"));

        Object node = mapper.createValue("{\"name\": \"John\", \"age\": 30, \"cars\": [\"Ford\", \"BMW\", \"Fiat\"]}");
        jsonGenerator.writeValue(node);
        jsonGenerator.flush();

        JsonParser jsonParser = jsonFactory.createParser(new ByteArrayInputStream(jsonGenerator.getOutputBytes()));
        assertEquals("---{\"name\": \"John\", \"age\": 30, \"cars\": [\"Ford\", \"BMW\", \"Fiat\"]}", jsonParser.readValueAsString());
    }

    /**
     * Test for withSpacesInObjectEntries method.
     */
    @Test
    public void testWithSpacesInObjectEntries() throws JsonProcessingException {
        JsonFactory jsonFactory = mapper.getFactory();
        DefaultPrettyPrinter prettyPrinter = new DefaultPrettyPrinter();
        JsonGenerator jsonGenerator = jsonFactory.createGenerator(System.out);
        prettyPrinter.use(jsonGenerator);

        prettyPrinter = prettyPrinter.withSpacesInObjectEntries();

        Object node = mapper.createValue("{\"name\": \"John\", \"age\": 30, \"cars\": [\"Ford\", \"BMW\", \"Fiat\"]}");
        jsonGenerator.writeValue(node);
        jsonGenerator.flush();

        JsonParser jsonParser = jsonFactory.createParser(new ByteArrayInputStream(jsonGenerator.getOutputBytes()));
        assertEquals("{\"name\": \"John\", \"age\": 30,\n    \"cars\": [\n        \"Ford\",\n        \"BMW\",\n        \"Fiat\"\n    ]}", jsonParser.readValueAsString());
    }

    /**
     * Test for withSeparators method.
     */
    @Test
    public void testWithSeparators() throws JsonProcessingException {
        JsonFactory jsonFactory = mapper.getFactory();
        DefaultPrettyPrinter prettyPrinter = new DefaultPrettyPrinter();
        JsonGenerator jsonGenerator = jsonFactory.createGenerator(System.out);
        prettyPrinter.use(jsonGenerator);

        prettyPrinter = prettyPrinter.withSeparators(new Separators(":", ",", "{\n", "\n}"));

        Object node = mapper.createValue("{\"name\": \"John\", \"age\": 30, \"cars\": [\"Ford\", \"BMW\", \"Fiat\"]}");
        jsonGenerator.writeValue(node);
        jsonGenerator.flush();

        JsonParser jsonParser = jsonFactory.createParser(new ByteArrayInputStream(jsonGenerator.getOutputBytes()));
        assertEquals("{\n    \"name\": \"John\",\n    \"age\": 30,\n    \"cars\": [\n        \"Ford\",\n        \"BMW\",\n        \"Fiat\"\n    ]\n}", jsonParser.readValueAsString());
    }
}
package com.fasterxml.jackson.core.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;

public class DefaultPrettyPrinterTest {

    

    @Test
    public void testDefaultPrettyPrinterConstructor() {
        DefaultPrettyPrinter defaultPrettyPrinter = new DefaultPrettyPrinter();
        assertNotNull(defaultPrettyPrinter);
    }

    @Test
    public void testDefaultPrettyPrinterWithRootSeparator() {
        String rootSeparator = " Root: ";
        DefaultPrettyPrinter defaultPrettyPrinter = new DefaultPrettyPrinter(rootSeparator);
        assertNotNull(defaultPrettyPrinter);
        assertEquals(rootSeparator, defaultPrettyPrinter.getRootSeparator().getValueAsString());
    }

    @Test
    public void testDefaultPrettyPrinterWithRootSeparatorObject() {
        SerializableString rootSeparator = new SerializedString(" Root: ");
        DefaultPrettyPrinter defaultPrettyPrinter = new DefaultPrettyPrinter(rootSeparator);
        assertNotNull(defaultPrettyPrinter);
        assertEquals(rootSeparator, defaultPrettyPrinter.getRootSeparator());
    }

    @Test
    public void testDefaultPrettyPrinterWithArrayIndenter() {
        Indenter arrayIndenter = new Indenter(){
            @Override
            public void writeIndentation(JsonGenerator g, int level) throws IOException {
                g.writeRaw("Array Indenter: ");
            }

            @Override
            public boolean isInline() {
                return false;
            }
        };

        DefaultPrettyPrinter defaultPrettyPrinter = new DefaultPrettyPrinter();
        defaultPrettyPrinter.indentArraysWith(arrayIndenter);

        JsonGenerator jsonGenerator = null;
        try {
            jsonGenerator = createJsonGenerator();
            defaultPrettyPrinter.writeRootValueSeparator(jsonGenerator);
            defaultPrettyPrinter.writeStartArray(jsonGenerator);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        } finally {
            if (jsonGenerator != null) {
                try {
                    jsonGenerator.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        assertEquals("Array Indenter:  [", jsonGenerator.getOutputBuffer().toString());
    }

    @Test
    public void testDefaultPrettyPrinterWithObjectIndenter() {
        Indenter objectIndenter = new Indenter(){
            @Override
            public void writeIndentation(JsonGenerator g, int level) throws IOException {
                g.writeRaw("Object Indenter: ");
            }

            @Override
            public boolean isInline() {
                return false;
            }
        };

        DefaultPrettyPrinter defaultPrettyPrinter = new DefaultPrettyPrinter();
        defaultPrettyPrinter.indentObjectsWith(objectIndenter);

        JsonGenerator jsonGenerator = null;
        try {
            jsonGenerator = createJsonGenerator();
            JsonSerializer<Object> jsonSerializer = new JsonSerializer<Object>(new Object());
            TypeSerializer typeSerializer = null;
            defaultPrettyPrinter.writeObjectFieldValue(jsonGenerator, jsonSerializer, null, null, null, null, null, null, null, null, null, null, null, null, null, null);
            defaultPrettyPrinter.writeStartObject(jsonGenerator);
            defaultPrettyPrinter.writeObjectFieldName(jsonGenerator, "test");
            defaultPrettyPrinter.writeObjectFieldValue(jsonGenerator, jsonSerializer, null, null, null, null, null, null, null, null, null, null, null, null, null, null);
            defaultPrettyPrinter.writeEndObject(jsonGenerator, 1);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        } finally {
            if (jsonGenerator != null) {
                try {
                    jsonGenerator.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        assertEquals("Object Indenter: {\n test:\n Object Indenter:\n", jsonGenerator.getOutputBuffer().toString());
    }

    @Test
    public void testDefaultPrettyPrinterWithSpacesInObjectEntries() {
        DefaultPrettyPrinter defaultPrettyPrinter = new DefaultPrettyPrinter();
        defaultPrettyPrinter = defaultPrettyPrinter.withSpacesInObjectEntries();

        JsonGenerator jsonGenerator = null;
        try {
            jsonGenerator = createJsonGenerator();
            JsonSerializer<Object> jsonSerializer = new JsonSerializer<Object>(new Object());
            TypeSerializer typeSerializer = null;
            defaultPrettyPrinter.writeObjectFieldValue(jsonGenerator, jsonSerializer, null, null, null, null, null, null, null, null, null, null, null, null, null, null);
            defaultPrettyPrinter.writeStartObject(jsonGenerator);
            defaultPrettyPrinter.writeObjectFieldName(jsonGenerator, "test");
            defaultPrettyPrinter.writeObjectFieldValue(jsonGenerator, jsonSerializer, null, null, null, null, null, null, null, null, null, null, null, null, null, null);
            defaultPrettyPrinter.writeEndObject(jsonGenerator, 1);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        } finally {
            if (jsonGenerator != null) {
                try {
                    jsonGenerator.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        assertEquals("{\n test:\n ", jsonGenerator.getOutputBuffer().toString());
    }

    @Test
    public void testDefaultPrettyPrinterWithSeparators() {
        Separators separators = new Separators() {
            @Override
            public String getObjectFieldNameSeparator() {
                return ":";
            }

            @Override
            public String getObjectFieldValueSeparator() {
                return "|";
            }

            @Override
            public String getObjectEntrySeparator() {
                return "|";
            }

            @Override
            public String getArrayValueSeparator() {
                return "|";
            }
        };

        DefaultPrettyPrinter defaultPrettyPrinter = new DefaultPrettyPrinter();
        defaultPrettyPrinter.withSeparators(separators);

        JsonGenerator jsonGenerator = null;
        try {
            jsonGenerator = createJsonGenerator();
            JsonSerializer<Object> jsonSerializer = new JsonSerializer<Object>(new Object());
            TypeSerializer typeSerializer = null;
            defaultPrettyPrinter.writeObjectFieldValue(jsonGenerator, jsonSerializer, null, null, null, null, null, null, null, null, null, null, null, null, null, null);
            defaultPrettyPrinter.writeStartObject(jsonGenerator);
            defaultPrettyPrinter.writeObjectFieldName(jsonGenerator, "test");
            defaultPrettyPrinter.writeObjectFieldValue(jsonGenerator, jsonSerializer, null, null, null, null, null, null, null, null, null, null, null, null, null, null);
            defaultPrettyPrinter.writeEndObject(jsonGenerator, 1);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        } finally {
            if (jsonGenerator != null) {
                try {
                    jsonGenerator.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        assertEquals("{\n test:\n |", jsonGenerator.getOutputBuffer().toString());
    }

    @Test(expected = JsonGenerationException.class)
    public void testWriteObjectFieldValueSeparatorWithNullIndenter() {
        DefaultPrettyPrinter defaultPrettyPrinter = new DefaultPrettyPrinter();
        defaultPrettyPrinter.writeObjectFieldValueSeparator(null);
    }

    @Test(expected = JsonGenerationException.class)
    public void testWriteObjectEntrySeparatorWithNullIndenter() {
        DefaultPrettyPrinter defaultPrettyPrinter = new DefaultPrettyPrinter();
        defaultPrettyPrinter.writeObjectEntrySeparator(null);
    }

    @Test(expected = JsonGenerationException.class)
    public void testWriteArrayValueSeparatorWithNullIndenter() {
        DefaultPrettyPrinter defaultPrettyPrinter = new DefaultPrettyPrinter();
        defaultPrettyPrinter.writeArrayValueSeparator(null);
    }

    @Test
    public void testWriteStartObjectWithNullIndenter() {
        Indenter objectIndenter = null;
        DefaultPrettyPrinter defaultPrettyPrinter = new DefaultPrettyPrinter();
        defaultPrettyPrinter.indentObjectsWith(objectIndenter);

        JsonGenerator jsonGenerator = null;
        try {
            jsonGenerator = createJsonGenerator();
            defaultPrettyPrinter.writeStartObject(jsonGenerator);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        } finally {
            if (jsonGenerator != null) {
                try {
                    jsonGenerator.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        assertEquals("{}", jsonGenerator.getOutputBuffer().toString());
    }

    @Test
    public void testWriteStartArrayWithNullIndenter() {
        Indenter arrayIndenter = null;
        DefaultPrettyPrinter defaultPrettyPrinter = new DefaultPrettyPrinter();
        defaultPrettyPrinter.indentArraysWith(arrayIndenter);

        JsonGenerator jsonGenerator = null;
        try {
            jsonGenerator = createJsonGenerator();
            defaultPrettyPrinter.writeStartArray(jsonGenerator);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        } finally {
            if (jsonGenerator != null) {
                try {
                    jsonGenerator.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        assertEquals("[]", jsonGenerator.getOutputBuffer().toString());
    }

    @Test
    public void testWriteRootValueSeparatorWithNullIndenter() {
        DefaultPrettyPrinter defaultPrettyPrinter = new DefaultPrettyPrinter();
        defaultPrettyPrinter.writeRootValueSeparator(null);
    }

    @Test
    public void testWriteEndObjectWithNullIndenter() {
        Indenter objectIndenter = null;
        DefaultPrettyPrinter defaultPrettyPrinter = new DefaultPrettyPrinter();
        defaultPrettyPrinter.indentObjectsWith(objectIndenter);

        JsonGenerator jsonGenerator = null;
        try {
            jsonGenerator = createJsonGenerator();
            defaultPrettyPrinter.writeStartObject(jsonGenerator);
            defaultPrettyPrinter.writeEndObject(jsonGenerator, 1);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        } finally {
            if (jsonGenerator != null) {
                try {
                    jsonGenerator.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        assertEquals("{\n}", jsonGenerator.getOutputBuffer().toString());
    }

    @Test
    public void testWriteEndArrayWithNullIndenter() {
        Indenter arrayIndenter = null;
        DefaultPrettyPrinter defaultPrettyPrinter = new DefaultPrettyPrinter();
        defaultPrettyPrinter.indentArraysWith(arrayIndenter);

        JsonGenerator jsonGenerator = null;
        try {
            jsonGenerator = createJsonGenerator();
            defaultPrettyPrinter.writeStartArray(jsonGenerator);
            defaultPrettyPrinter.writeEndArray(jsonGenerator, 1);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        } finally {
            if (jsonGenerator != null) {
                try {
                    jsonGenerator.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        assertEquals("[]", jsonGenerator.getOutputBuffer().toString());
    }

    private JsonGenerator createJsonGenerator() throws JsonProcessingException {
        JsonGenerator jsonGenerator = new JsonFactory().createGenerator(new ByteArrayOutputStream(), new DefaultSerializerProvider());
        return jsonGenerator;
    }

    
}
package com.fasterxml.jackson.core.util;

import static org.junit.Assert.*;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.core.io.SerializedString;

public class DefaultPrettyPrinterTest {

    @Test
    public void testDefaultPrettyPrinter() {
        DefaultPrettyPrinter prettyPrinter = new DefaultPrettyPrinter();
        JsonGenerator generator = null; // Initialize JsonGenerator for testing

        try {
            String json = prettyPrinter.writeValueAsString(new Object());
            assertEquals("", json);
        } catch (IOException e) {
            fail("IOException should not be thrown for DefaultPrettyPrinter() constructor");
        } finally {
            if (generator != null) {
                try {
                    generator.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        // Test writeRootValueSeparator
        generator = new JsonFactory().createGenerator(new ByteArrayOutputStream(), DefaultPrettyPrinter.DEFAULT_ROOT_VALUE_SEPARATOR);
        prettyPrinter = new DefaultPrettyPrinter(DefaultPrettyPrinter.DEFAULT_ROOT_VALUE_SEPARATOR);
        prettyPrinter.writeRootValueSeparator(generator);
        String rootSeparatorJson = generator.getOutputBuffer().toString();
        assertEquals(" ", rootSeparatorJson);

        // Test writeStartObject
        generator.flush();
        generator.reset();
        prettyPrinter.writeStartObject(generator);
        String startObjectJson = generator.getOutputBuffer().toString();
        assertEquals("{\n", startObjectJson);

        // Test writeObjectFieldValueSeparator
        generator.reset();
        generator.writeRaw("fieldName:");
        prettyPrinter.writeObjectFieldValueSeparator(generator);
        String objectFieldValueSeparatorJson = generator.getOutputBuffer().toString();
        assertEquals(" :\n", objectFieldValueSeparatorJson);

        // Test writeObjectEntrySeparator
        generator.reset();
        prettyPrinter.writeObjectEntrySeparator(generator);
        String objectEntrySeparatorJson = generator.getOutputBuffer().toString();
        assertEquals("\n    ", objectEntrySeparatorJson);

        // Test writeEndObject
        generator.reset();
        prettyPrinter.writeEndObject(generator, 0);
        String endObjectJson = generator.getOutputBuffer().toString();
        assertEquals("}\n", endObjectJson);

        // Test writeStartArray
        generator.reset();
        prettyPrinter.writeStartArray(generator);
        String startArrayJson = generator.getOutputBuffer().toString();
        assertEquals("[", startArrayJson);

        // Test writeArrayValueSeparator
        generator.reset();
        generator.writeRaw("value");
        prettyPrinter.writeArrayValueSeparator(generator);
        String arrayValueSeparatorJson = generator.getOutputBuffer().toString();
        assertEquals(",\n", arrayValueSeparatorJson);

        // Test writeEndArray
        generator.reset();
        prettyPrinter.writeEndArray(generator, 1);
        String endArrayJson = generator.getOutputBuffer().toString();
        assertEquals("]\n", endArrayJson);
    }

    @Test
    public void testDefaultPrettyPrinterWithRootSeparator() {
        DefaultPrettyPrinter prettyPrinter = new DefaultPrettyPrinter("rootSeparator");
        JsonGenerator generator = null; // Initialize JsonGenerator for testing

        try {
            String json = prettyPrinter.writeValueAsString(new Object());
            assertEquals("rootSeparator", json);
        } catch (IOException e) {
            fail("IOException should not be thrown for DefaultPrettyPrinter(String) constructor");
        } finally {
            if (generator != null) {
                try {
                    generator.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Test
    public void testDefaultPrettyPrinterWithSerializableStringRootSeparator() {
        DefaultPrettyPrinter prettyPrinter = new DefaultPrettyPrinter(new SerializedString("rootSeparator"));
        JsonGenerator generator = null; // Initialize JsonGenerator for testing

        try {
            String json = prettyPrinter.writeValueAsString(new Object());
            assertEquals("rootSeparator", json);
        } catch (IOException e) {
            fail("IOException should not be thrown for DefaultPrettyPrinter(SerializableString) constructor");
        } finally {
            if (generator != null) {
                try {
                    generator.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Test
    public void testDefaultPrettyPrinterWithCustomIndenterForObjects() {
        DefaultPrettyPrinter prettyPrinter = new DefaultPrettyPrinter();
        CustomObjectIndenter customObjectIndenter = new CustomObjectIndenter();
        JsonGenerator generator = null; // Initialize JsonGenerator for testing

        try {
            prettyPrinter.indentObjectsWith(customObjectIndenter);
            String json = prettyPrinter.writeValueAsString(new Object());
            assertEquals("\n{\n    \n}", json);
        } catch (IOException e) {
            fail("IOException should not be thrown for writeValueAsString with CustomObjectIndenter");
        } finally {
            if (generator != null) {
                try {
                    generator.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Test
    public void testDefaultPrettyPrinterWithCustomIndenterForArrays() {
        DefaultPrettyPrinter prettyPrinter = new DefaultPrettyPrinter();
        CustomArrayIndenter customArrayIndenter = new CustomArrayIndenter();
        JsonGenerator generator = null; // Initialize JsonGenerator for testing

        try {
            prettyPrinter.indentArraysWith(customArrayIndenter);
            String json = prettyPrinter.writeValueAsString(new int[] { 1, 2, 3 });
            assertEquals("\n[1,\n    2,\n    3]\n", json);
        } catch (IOException e) {
            fail("IOException should not be thrown for writeValueAsString with CustomArrayIndenter");
        } finally {
            if (generator != null) {
                try {
                    generator.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Test
    public void testDefaultPrettyPrinterWithSpacesInObjectEntries() {
        DefaultPrettyPrinter prettyPrinter = new DefaultPrettyPrinter();
        String jsonWithSpaces = prettyPrinter.writeValueAsString(new Object() {
            String field1 = "field 1";
            String field2 = "field 2";

            @Override
            public String toString() {
                return "{\"field1\":\"" + field1 + "\", \"field2\":\"" + field2 + "\"}";
            }
        });
        String jsonWithoutSpaces = prettyPrinter.withSpacesInObjectEntries().writeValueAsString(new Object() {
            String field1 = "field1";
            String field2 = "field2";

            @Override
            public String toString() {
                return "{\"field1\":\"field1\", \"field2\":\"field2\"}";
            }
        });

        assertEquals("{\"field1\":\"field 1\", \"field2\":\"field 2\"}\n", jsonWithSpaces);
        assertEquals("{\"field1\":\"field1\", \"field2\":\"field2\"}", jsonWithoutSpaces);
    }

    @Test
    public void testDefaultPrettyPrinterWithCustomSeparators() {
        DefaultPrettyPrinter prettyPrinter = new DefaultPrettyPrinter();
        Separators customSeparators = new Separators("|", "=");
        JsonGenerator generator = null; // Initialize JsonGenerator for testing

        try {
            prettyPrinter.withSeparators(customSeparators);
            String json = prettyPrinter.writeValueAsString(new Object());
            assertEquals("", json);
        } catch (IOException e) {
            fail("IOException should not be thrown for writeValueAsString with CustomSeparators");
        } finally {
            if (generator != null) {
                try {
                    generator.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        generator = new JsonFactory().createGenerator(new ByteArrayOutputStream());
        prettyPrinter.writeRootValueSeparator(generator);
        String rootSeparatorJson = generator.getOutputBuffer().toString();
        assertEquals("|", rootSeparatorJson);

        generator.reset();
        prettyPrinter.writeStartObject(generator);
        String startObjectJson = generator.getOutputBuffer().toString();
        assertEquals("{|\n", startObjectJson);

        generator.reset();
        generator.writeRaw("fieldName:");
        prettyPrinter.writeObjectFieldValueSeparator(generator);
        String objectFieldValueSeparatorJson = generator.getOutputBuffer().toString();
        assertEquals("| :=\n", objectFieldValueSeparatorJson);

        generator.reset();
        prettyPrinter.writeObjectEntrySeparator(generator);
        String objectEntrySeparatorJson = generator.getOutputBuffer().toString();
        assertEquals("\n|    =\n", objectEntrySeparatorJson);

        generator.reset();
        prettyPrinter.writeEndObject(generator, 1);
        String endObjectJson = generator.getOutputBuffer().toString();
        assertEquals("|}\n", endObjectJson);

        generator.reset();
        prettyPrinter.writeStartArray(generator);
        String startArrayJson = generator.getOutputBuffer().toString();
        assertEquals("[|", startArrayJson);

        generator.reset();
        generator.writeRaw("value");
        prettyPrinter.writeArrayValueSeparator(generator);
        String arrayValueSeparatorJson = generator.getOutputBuffer().toString();
        assertEquals("|,\n", arrayValueSeparatorJson);

        generator.reset();
        prettyPrinter.writeEndArray(generator, 2);
        String endArrayJson = generator.getOutputBuffer().toString();
        assertEquals("|]\n", endArrayJson);
    }
}

class CustomObjectIndenter implements Indenter {
    @Override
    public void writeIndentation(JsonGenerator g, int level) throws IOException {
        g.writeRaw("\t");
    }

    @Override
    public boolean isInline() {
        return false;
    }
}

class CustomArrayIndenter implements Indenter {
    @Override
    public void writeIndentation(JsonGenerator g, int level) throws IOException {
        g.writeRaw("\t");
    }

    @Override
    public boolean isInline() {
        return false;
    }
}

class Separators implements Serializable {
    private final String objectEntrySeparator;
    private final String arrayValueSeparator;
    private final String objectFieldValueSeparator;

    public Separators(String objectEntrySeparator, String arrayValueSeparator, String objectFieldValueSeparator) {
        this.objectEntrySeparator = objectEntrySeparator;
        this.arrayValueSeparator = arrayValueSeparator;
        this.objectFieldValueSeparator = objectFieldValueSeparator;
    }

    public Separators(String separators) {
        String[] parts = separators.split(";");
        if (parts.length != 3) {
            throw new IllegalArgumentException("Invalid separators: " + separators);
        }
        this.objectEntrySeparator = parts[0];
        this.arrayValueSeparator = parts[1];
        this.objectFieldValueSeparator = parts[2];
    }

    public String getObjectEntrySeparator() {
        return objectEntrySeparator;
    }

    public String getObjectFieldValueSeparator() {
        return objectFieldValueSeparator;
    }

    public String getArrayValueSeparator() {
        return arrayValueSeparator;
    }

    @Override
    public String toString() {
        return objectEntrySeparator + ";" + arrayValueSeparator + ";" + objectFieldValueSeparator;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Separators that = (Separators) o;
        return objectEntrySeparator.equals(that.objectEntrySeparator)
                && arrayValueSeparator.equals(that.arrayValueSeparator)
                && objectFieldValueSeparator.equals(that.objectFieldValueSeparator);
    }

    @Override
    public int hashCode() {
        int result = objectEntrySeparator.hashCode();
        result = 31 * result + arrayValueSeparator.hashCode();
        result = 31 * result + objectFieldValueSeparator.hashCode();
        return result;
    }
}
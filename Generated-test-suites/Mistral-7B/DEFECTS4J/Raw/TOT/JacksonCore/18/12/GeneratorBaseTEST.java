package com.fasterxml.jackson.core.base;

import static org.junit.Assert.*;

import java.io.IOException;
import java.math.BigDecimal;

import org.junit.Test;

public class GeneratorBaseTest {

    @Test
    public void testIsEnabledFeature() throws IOException {
        GeneratorBase jsonGenerator = new GeneratorBase(0, null);

        // Test with default features
        assertTrue(jsonGenerator.isEnabled(Feature.WRITE_NUMBERS_AS_STRINGS));
        assertFalse(jsonGenerator.isEnabled(Feature.ESCAPE_NON_ASCII));
        assertFalse(jsonGenerator.isEnabled(Feature.STRICT_DUPLICATE_DETECTION));

        // Test with enabled features
        int features = Feature.WRITE_NUMBERS_AS_STRINGS.getMask()
                | Feature.ESCAPE_NON_ASCII.getMask()
                | Feature.STRICT_DUPLICATE_DETECTION.getMask();
        jsonGenerator = new GeneratorBase(features, null);
        assertTrue(jsonGenerator.isEnabled(Feature.WRITE_NUMBERS_AS_STRINGS));
        assertTrue(jsonGenerator.isEnabled(Feature.ESCAPE_NON_ASCII));
        assertTrue(jsonGenerator.isEnabled(Feature.STRICT_DUPLICATE_DETECTION));

        // Test with disabled features
        features &= ~(Feature.WRITE_NUMBERS_AS_STRINGS.getMask()
                | Feature.ESCAPE_NON_ASCII.getMask()
                | Feature.STRICT_DUPLICATE_DETECTION.getMask());
        jsonGenerator = new GeneratorBase(features, null);
        assertFalse(jsonGenerator.isEnabled(Feature.WRITE_NUMBERS_AS_Strings));
        assertFalse(jsonGenerator.isEnabled(Feature.ESCAPE_NON_ASCII));
        assertFalse(jsonGenerator.isEnabled(Feature.STRICT_DUPLICATE_DETECTION));
    }

    @Test
    public void testSetEnableDisableFeature() throws IOException {
        GeneratorBase jsonGenerator = new GeneratorBase(0, null);

        // Test write numbers as strings feature
        jsonGenerator.enable(Feature.WRITE_NUMBERS_AS_STRINGS);
        assertTrue(jsonGenerator.isEnabled(Feature.WRITE_NUMBERS_AS_STRINGS));
        jsonGenerator.disable(Feature.WRITE_NUMBERS_AS_STRINGS);
        assertFalse(jsonGenerator.isEnabled(Feature.WRITE_NUMBERS_AS_STRINGS));

        // Test escape non-ASCII feature
        jsonGenerator.enable(Feature.ESCAPE_NON_ASCII);
        assertTrue(jsonGenerator.isEnabled(Feature.ESCAPE_NON_ASCII));
        jsonGenerator.disable(Feature.ESCAPE_NON_ASCII);
        assertFalse(jsonGenerator.isEnabled(Feature.ESCAPE_NON_ASCII));

        // Test strict duplicate detection feature
        jsonGenerator.enable(Feature.STRICT_DUPLICATE_DETECTION);
        assertTrue(jsonGenerator.isEnabled(Feature.STRICT_DUPLICATE_DETECTION));
        jsonGenerator.disable(Feature.STRICT_DUPLICATE_DETECTION);
        assertFalse(jsonGenerator.isEnabled(Feature.STRICT_DUPLICATE_DETECTION));
    }

    @Test
    public void testWriteString() throws IOException {
        GeneratorBase jsonGenerator = new GeneratorBase(0, null);

        // Test write simple String
        jsonGenerator.writeString("test");
        String expectedOutput = "\"test\"";
        assertEquals(expectedOutput, jsonGenerator.getOutputContext().getCurrentValue().toString());

        // Test write complex String
        jsonGenerator.writeStartObject();
        jsonGenerator.writeFieldName("name");
        jsonGenerator.writeString("John Doe");
        jsonGenerator.writeEndObject();
        expectedOutput = "{\"name\":\"John Doe\"}";
        assertEquals(expectedOutput, jsonGenerator.getOutputContext().getCurrentValue().toString());

        // Test write null
        jsonGenerator.writeNull();
        expectedOutput = "null";
        assertEquals(expectedOutput, jsonGenerator.getOutputContext().getCurrentValue().toString());
    }

    @Test
    public void testWriteRawValue() throws IOException {
        GeneratorBase jsonGenerator = new GeneratorBase(0, null);

        // Test write simple raw String
        jsonGenerator.writeRawValue("test");
        String expectedOutput = "\"test\"";
        assertEquals(expectedOutput, jsonGenerator.getOutputContext().getCurrentValue().toString());

        // Test write complex raw String
        jsonGenerator.writeStartObject();
        jsonGenerator.writeFieldName("name");
        jsonGenerator.writeRawValue("John Doe");
        jsonGenerator.writeEndObject();
        expectedOutput = "{\"name\":\"John Doe\"}";
        assertEquals(expectedOutput, jsonGenerator.getOutputContext().getCurrentValue().toString());

        // Test write null
        jsonGenerator.writeRawValue(null);
        expectedOutput = "null";
        assertEquals(expectedOutput, jsonGenerator.getOutputContext().getCurrentValue().toString());
    }

    @Test
    public void testWriteNumber() throws IOException {
        GeneratorBase jsonGenerator = new GeneratorBase(0, null);

        // Test write simple number
        jsonGenerator.writeNumber(123);
        String expectedOutput = "123";
        assertEquals(expectedOutput, jsonGenerator.getOutputContext().getCurrentValue().toString());

        // Test write BigDecimal number
        BigDecimal bigDecimal = new BigDecimal("123.45");
        jsonGenerator.writeNumber(bigDecimal);
        expectedOutput = "123.45";
        assertEquals(expectedOutput, jsonGenerator.getOutputContext().getCurrentValue().toString());

        // Test write Boolean
        jsonGenerator.writeBoolean(true);
        expectedOutput = "true";
        assertEquals(expectedOutput, jsonGenerator.getOutputContext().getCurrentValue().toString());

        // Test write null
        jsonGenerator.writeNull();
        expectedOutput = "null";
        assertEquals(expectedOutput, jsonGenerator.getOutputContext().getCurrentValue().toString());
    }

    @Test
    public void testWriteObject() throws IOException {
        GeneratorBase jsonGenerator = new GeneratorBase(0, null);

        // Test write simple Object
        String testClass = "java.lang.String";
        jsonGenerator.writeObject(new String("test"));
        String expectedOutput = "\"test\"";
        assertEquals(expectedOutput, jsonGenerator.getOutputContext().getCurrentValue().toString());

        // Test write complex Object
        class TestClass {
            private String name;

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }
        }

        TestClass testObject = new TestClass();
        testObject.setName("test");
        jsonGenerator.writeObject(testObject);
        expectedOutput = "{\"name\":\"test\"}";
        assertEquals(expectedOutput, jsonGenerator.getOutputContext().getCurrentValue().toString());

        // Test write null
        TestClass testObjectNull = null;
        jsonGenerator.writeObject(testObjectNull);
        expectedOutput = "null";
        assertEquals(expectedOutput, jsonGenerator.getOutputContext().getCurrentValue().toString());
    }

    @Test
    public void testWriteTree() throws IOException {
        GeneratorBase jsonGenerator = new GeneratorBase(0, null);

        // Test write simple TreeNode
        Object testNode = new Object();
        jsonGenerator.writeTree(testNode);
        String expectedOutput = "null";
        assertEquals(expectedOutput, jsonGenerator.getOutputContext().getCurrentValue().toString());

        // Test write complex TreeNode
        class TestTreeNode {
            private String name;
            private Object value;

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public Object getValue() {
                return value;
            }

            public void setValue(Object value) {
                this.value = value;
            }
        }

        TestTreeNode testTreeNode = new TestTreeNode();
        testTreeNode.setName("test");
        testTreeNode.setValue(new String("test"));
        jsonGenerator.writeTree(testTreeNode);
        expectedOutput = "{\"name\":\"test\",\"value\":\"test\"}";
        assertEquals(expectedOutput, jsonGenerator.getOutputContext().getCurrentValue().toString());

        // Test write null
        TestTreeNode testTreeNodeNull = null;
        jsonGenerator.writeTree(testTreeNodeNull);
        expectedOutput = "null";
        assertEquals(expectedOutput, jsonGenerator.getOutputContext().getCurrentValue().toString());
    }

    @Test
    public void testFlushClose() throws IOException {
        GeneratorBase jsonGenerator = new GeneratorBase(0, null);

        // Test flush
        jsonGenerator.writeStartObject();
        jsonGenerator.flush();
        String expectedOutput = "{}";
        assertEquals(expectedOutput, jsonGenerator.getOutputContext().getCurrentValue().toString());

        // Test close
        jsonGenerator.close();
        expectedOutput = "{}";
        assertNull(jsonGenerator.getOutputContext().getCurrentValue());
    }
}
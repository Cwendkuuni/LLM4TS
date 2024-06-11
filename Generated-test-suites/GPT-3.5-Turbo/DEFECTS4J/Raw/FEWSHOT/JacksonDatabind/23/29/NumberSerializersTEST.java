package com.fasterxml.jackson.databind.ser.std;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import org.junit.Test;

public class NumberSerializersTest {
    @Test
    public void testShortSerializer() {
        ShortSerializer serializer = new ShortSerializer();
        assertEquals("number", serializer._schemaType);
        Short value = 10;
        String expectedJson = "10";
        // Testing serialization
        StringBuilder sb = new StringBuilder();
        try {
            serializer.serialize(value, new JsonGenerator() {
                @Override
                public void writeNumber(short v) throws IOException {
                    sb.append(String.valueOf(v));
                }

                @Override
                public void flush() throws IOException {
                    // Do nothing
                }

                @Override
                public void close() throws IOException {
                    // Do nothing
                }
            }, null);
        } catch (IOException e) {
            e.printStackTrace();
        }
        assertEquals(expectedJson, sb.toString());
    }

    @Test
    public void testIntegerSerializer() {
        IntegerSerializer serializer = new IntegerSerializer();
        assertEquals("integer", serializer._schemaType);
        Integer value = 100;
        String expectedJson = "100";
        // Testing serialization
        StringBuilder sb = new StringBuilder();
        try {
            serializer.serialize(value, new JsonGenerator() {
                @Override
                public void writeNumber(int v) throws IOException {
                    sb.append(String.valueOf(v));
                }

                @Override
                public void flush() throws IOException {
                    // Do nothing
                }

                @Override
                public void close() throws IOException {
                    // Do nothing
                }
            }, null);
        } catch (IOException e) {
            e.printStackTrace();
        }
        assertEquals(expectedJson, sb.toString());
    }

    @Test
    public void testIntLikeSerializer() {
        IntLikeSerializer serializer = new IntLikeSerializer();
        assertEquals("integer", serializer._schemaType);
        Number value = 50;
        String expectedJson = "50";
        // Testing serialization
        StringBuilder sb = new StringBuilder();
        try {
            serializer.serialize(value, new JsonGenerator() {
                @Override
                public void writeNumber(int v) throws IOException {
                    sb.append(String.valueOf(v));
                }

                @Override
                public void flush() throws IOException {
                    // Do nothing
                }

                @Override
                public void close() throws IOException {
                    // Do nothing
                }
            }, null);
        } catch (IOException e) {
            e.printStackTrace();
        }
        assertEquals(expectedJson, sb.toString());
    }

    @Test
    public void testLongSerializer() {
        LongSerializer serializer = new LongSerializer();
        assertEquals("number", serializer._schemaType);
        Long value = 1000L;
        String expectedJson = "1000";
        // Testing serialization
        StringBuilder sb = new StringBuilder();
        try {
            serializer.serialize(value, new JsonGenerator() {
                @Override
                public void writeNumber(long v) throws IOException {
                    sb.append(String.valueOf(v));
                }

                @Override
                public void flush() throws IOException {
                    // Do nothing
                }

                @Override
                public void close() throws IOException {
                    // Do nothing
                }
            }, null);
        } catch (IOException e) {
            e.printStackTrace();
        }
        assertEquals(expectedJson, sb.toString());
    }

    @Test
    public void testFloatSerializer() {
        FloatSerializer serializer = new FloatSerializer();
        assertEquals("number", serializer._schemaType);
        Float value = 10.5f;
        String expectedJson = "10.5";
        // Testing serialization
        StringBuilder sb = new StringBuilder();
        try {
            serializer.serialize(value, new JsonGenerator() {
                @Override
                public void writeNumber(float v) throws IOException {
                    sb.append(String.valueOf(v));
                }

                @Override
                public void flush() throws IOException {
                    // Do nothing
                }

                @Override
                public void close() throws IOException {
                    // Do nothing
                }
            }, null);
        } catch (IOException e) {
            e.printStackTrace();
        }
        assertEquals(expectedJson, sb.toString());
    }

    @Test
    public void testDoubleSerializer() {
        DoubleSerializer serializer = new DoubleSerializer();
        assertEquals("number", serializer._schemaType);
        Double value = 10.5;
        String expectedJson = "10.5";
        // Testing serialization
        StringBuilder sb = new StringBuilder();
        try {
            serializer.serialize(value, new JsonGenerator() {
                @Override
                public void writeNumber(double v) throws IOException {
                    sb.append(String.valueOf(v));
                }

                @Override
                public void flush() throws IOException {
                    // Do nothing
                }

                @Override
                public void close() throws IOException {
                    // Do nothing
                }
            }, null);
        } catch (IOException e) {
            e.printStackTrace();
        }
        assertEquals(expectedJson, sb.toString());
    }
}
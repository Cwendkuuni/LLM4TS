package com.fasterxml.jackson.databind.deser.std;

import org.junit.Test;
import static org.junit.Assert.*;

public class FromStringDeserializerTest {

    @Test
    public void testFileDeserialization() {
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(File.class, FromStringDeserializer.Std.STD_FILE);
        File file = (File) deserializer._deserialize("/path/to/file", deserializationContext);
        assertEquals(new File("/path/to/file"), file);
    }

    @Test
    public void testURLDeserialization() {
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(URL.class, FromStringDeserializer.Std.STD_URL);
        URL url = (URL) deserializer._deserialize("http://www.example.com", deserializationContext);
        assertEquals(new URL("http://www.example.com"), url);
    }

    @Test
    public void testURIDeserialization() {
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(URI.class, FromStringDeserializer.Std.STD_URI);
        URI uri = (URI) deserializer._deserialize("http://www.example.com", deserializationContext);
        assertEquals(URI.create("http://www.example.com"), uri);
    }

    @Test
    public void testClassDeserialization() {
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(Class.class, FromStringDeserializer.Std.STD_CLASS);
        Class<?> clazz = (Class<?>) deserializer._deserialize("java.lang.String", deserializationContext);
        assertEquals(String.class, clazz);
    }

    // Add tests for the remaining deserialization methods...

}
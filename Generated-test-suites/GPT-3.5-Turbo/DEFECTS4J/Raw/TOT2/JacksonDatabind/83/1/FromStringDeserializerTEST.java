package com.fasterxml.jackson.databind.deser.std;

import static org.junit.Assert.*;
import org.junit.Test;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import java.io.IOException;

public class FromStringDeserializerTest {

    @Test
    public void testDeserialize_validString() throws IOException {
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(File.class, FromStringDeserializer.Std.STD_FILE);
        assertEquals(new File("test.txt"), deserializer._deserialize("test.txt", null));
    }

    @Test
    public void testDeserialize_emptyString() throws IOException {
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(File.class, FromStringDeserializer.Std.STD_FILE);
        assertNull(deserializer._deserializeFromEmptyString());
    }

    @Test(expected = JsonMappingException.class)
    public void testDeserialize_invalidFileString() throws IOException {
        FromStringDeserializer.Std deserializer = new FromStringDeserializer.Std(File.class, FromStringDeserializer.Std.STD_FILE);
        deserializer._deserialize("non-existent-file.txt", null);
    }

    @Test
    public void testFindDeserializer_validType() {
        Std deserializer = FromStringDeserializer.findDeserializer(File.class);
        assertNotNull(deserializer);
        assertEquals(FromStringDeserializer.Std.STD_FILE, deserializer._kind);
    }

    @Test
    public void testFindDeserializer_invalidType() {
        Std deserializer = FromStringDeserializer.findDeserializer(String.class);
        assertNull(deserializer);
    }

    @Test
    public void testTypes() {
        Class<?>[] expectedTypes = {
            File.class,
            URL.class,
            URI.class,
            Class.class,
            JavaType.class,
            Currency.class,
            Pattern.class,
            Locale.class,
            Charset.class,
            TimeZone.class,
            InetAddress.class,
            InetSocketAddress.class,
            StringBuilder.class
        };
        assertArrayEquals(expectedTypes, FromStringDeserializer.types());
    }

}
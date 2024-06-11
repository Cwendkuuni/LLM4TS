package com.fasterxml.jackson.databind.deser.std;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class FromStringDeserializerTest {

    private FromStringDeserializer.Std deserializer;

    @Before
    public void setUp() {
        deserializer = new FromStringDeserializer.Std(String.class, 1);
    }

    @Test
    public void testDeserialize() throws Exception {
        String value = "test";
        assertEquals(value, deserializer.deserialize(value));
    }

    @Test
    public void testDeserializeEmptyString() throws Exception {
        String value = "";
        assertNull(deserializer.deserialize(value));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDeserializeInvalidValue() throws Exception {
        String value = "123";
        deserializer.deserialize(value);
    }

    @Test
    public void testDeserializeFromEmptyString() throws Exception {
        assertEquals("", deserializer._deserializeFromEmptyString());
    }

    @Test
    public void testDeserializeFromEmptyStringWithURI() throws Exception {
        FromStringDeserializer.Std uriDeserializer = new FromStringDeserializer.Std(URI.class, 3);
        assertEquals(URI.create(""), uriDeserializer._deserializeFromEmptyString());
    }

    @Test
    public void testDeserializeFromEmptyStringWithLocale() throws Exception {
        FromStringDeserializer.Std localeDeserializer = new FromStringDeserializer.Std(Locale.class, 8);
        assertEquals(Locale.ROOT, localeDeserializer._deserializeFromEmptyString());
    }

    @Test
    public void testDeserializeFromEmptyStringWithStringBuilder() throws Exception {
        FromStringDeserializer.Std stringBuilderDeserializer = new FromStringDeserializer.Std(StringBuilder.class, 13);
        StringBuilder expected = new StringBuilder();
        assertEquals(expected.toString(), stringBuilderDeserializer._deserializeFromEmptyString().toString());
    }

    @Test
    public void testFindDeserializer() throws Exception {
        FromStringDeserializer.Std deserializer = FromStringDeserializer.Std.findDeserializer(String.class);
        assertEquals(String.class, deserializer.getValueType());
    }

}
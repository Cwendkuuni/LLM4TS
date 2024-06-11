package com.fasterxml.jackson.databind.deser.std;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class FromStringDeserializerTest {

    private FromStringDeserializer.Std deserializer;

    @Before
    public void setUp() {
        deserializer = new FromStringDeserializer.Std(String.class, 0);
    }

    @Test
    public void testDeserialize() throws Exception {
        String value = "testValue";
        assertEquals(value, deserializer.deserialize(value, null));
    }

    @Test
    public void testDeserializeFromEmptyString() throws Exception {
        assertEquals("", deserializer.deserialize("", null));
    }

    @Test
    public void testDeserializeFromNullString() throws Exception {
        assertNull(deserializer.deserialize(null, null));
    }

    @Test(expected = InvalidFormatException.class)
    public void testDeserializeInvalidValue() throws Exception {
        deserializer.deserialize("invalidValue", null);
    }

    @Test
    public void testDeserializeEmbedded() throws Exception {
        Object embedded = new Object();
        assertEquals(embedded, deserializer.deserializeEmbedded(embedded, null));
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testDeserializeFromEmptyStringWithException() throws Exception {
        deserializer.deserializeFromEmptyString();
    }

    @Test
    public void testStdDeserialize() throws Exception {
        String value = "testValue";
        assertEquals(value, FromStringDeserializer.Std.deserialize(value, null));
    }

    @Test
    public void testStdDeserializeFromEmptyString() throws Exception {
        assertEquals("", FromStringDeserializer.Std.deserializeFromEmptyString());
    }

    @Test
    public void testStdDeserializeFromEmptyStringWithException() throws Exception {
        assertEquals("", FromStringDeserializer.Std.deserializeFromEmptyString());
    }

    @Test
    public void testStdFileDeserialize() throws Exception {
        String value = "testValue";
        assertEquals(value, FromStringDeserializer.Std.stdFileDeserialize(value, null));
    }

    @Test
    public void testStdURLDeserialize() throws Exception {
        String value = "testValue";
        assertEquals(value, FromStringDeserializer.Std.stdURLDeserialize(value, null));
    }

    @Test
    public void testStdURIDeserialize() throws Exception {
        String value = "testValue";
        assertEquals(value, FromStringDeserializer.Std.stdURIDeserialize(value, null));
    }

    @Test
    public void testStdClassDeserialize() throws Exception {
        String value = "testValue";
        assertEquals(value, FromStringDeserializer.Std.stdClassDeserialize(value, null));
    }

    @Test
    public void testStdJavaTypeDeserialize() throws Exception {
        String value = "testValue";
        assertEquals(value, FromStringDeserializer.Std.stdJavaTypeDeserialize(value, null));
    }

    @Test
    public void testStdCurrencyDeserialize() throws Exception {
        String value = "testValue";
        assertEquals(value, FromStringDeserializer.Std.stdCurrencyDeserialize(value, null));
    }

    @Test
    public void testStdPatternDeserialize() throws Exception {
        String value = "testValue";
        assertEquals(value, FromStringDeserializer.Std.stdPatternDeserialize(value, null));
    }

    @Test
    public void testStdLocaleDeserialize() throws Exception {
        String value = "testValue";
        assertEquals(value, FromStringDeserializer.Std.stdLocaleDeserialize(value, null));
    }

    @Test
    public void testStdCharsetDeserialize() throws Exception {
        String value = "testValue";
        assertEquals(value, FromStringDeserializer.Std.stdCharsetDeserialize(value, null));
    }

    @Test
    public void testStdTimeZoneDeserialize() throws Exception {
        String value = "testValue";
        assertEquals(value, FromStringDeserializer.Std.stdTimeZoneDeserialize(value, null));
    }

    @Test
    public void testStdInetAddressDeserialize() throws Exception {
        String value = "testValue";
        assertEquals(value, FromStringDeserializer.Std.stdInetAddressDeserialize(value, null));
    }

    @Test
    public void testStdInetSocketAddressDeserialize() throws Exception {
        String value = "testValue";
        assertEquals(value, FromStringDeserializer.Std.stdInetSocketAddressDeserialize(value, null));
    }

    @Test
    public void testStdStringBuilderDeserialize() throws Exception {
        String value = "testValue";
        assertEquals(value, FromStringDeserializer.Std.stdStringBuilderDeserialize(value, null));
    }
}
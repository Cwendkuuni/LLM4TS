package org.apache.commons.lang3.text.translate;
import org.apache.commons.lang3.text.translate.NumericEntityUnescaper;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.io.StringWriter;

import static org.junit.Assert.assertEquals;

public class NumericEntityUnescaperTest {

    private NumericEntityUnescaper unescaper;

    @Before
    public void setUp() {
        unescaper = new NumericEntityUnescaper();
    }

    @Test
    public void testTranslate_basic() throws IOException {
        StringWriter writer = new StringWriter();
        int result = unescaper.translate("&#65;", 0, writer);
        assertEquals(6, result);
        assertEquals("A", writer.toString());
    }

    @Test
    public void testTranslate_hexadecimal() throws IOException {
        StringWriter writer = new StringWriter();
        int result = unescaper.translate("&#x41;", 0, writer);
        assertEquals(7, result);
        assertEquals("A", writer.toString());
    }

    @Test
    public void testTranslate_noTranslation() throws IOException {
        StringWriter writer = new StringWriter();
        int result = unescaper.translate("ABC", 0, writer);
        assertEquals(0, result);
        assertEquals("", writer.toString());
    }

    @Test
    public void testTranslate_invalidInput() throws IOException {
        StringWriter writer = new StringWriter();
        int result = unescaper.translate("&#65A;", 0, writer);
        assertEquals(0, result);
        assertEquals("", writer.toString());
    }
    
    @Test
    public void testTranslate_invalidHexadecimalInput() throws IOException {
        StringWriter writer = new StringWriter();
        int result = unescaper.translate("&#x41A;", 0, writer);
        assertEquals(0, result);
        assertEquals("", writer.toString());
    }

    // TODO: Add more test cases to cover all methods in the class

    // 

    // Add more test cases here

    // 
}
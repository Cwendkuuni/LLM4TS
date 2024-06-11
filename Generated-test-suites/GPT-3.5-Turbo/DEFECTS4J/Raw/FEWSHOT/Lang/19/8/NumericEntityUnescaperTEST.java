
package org.apache.commons.lang3.text.translate;

import org.junit.Test;

import static org.junit.Assert.*;

public class NumericEntityUnescaperTest {
    private NumericEntityUnescaper unescaper = new NumericEntityUnescaper();

    @Test
    public void testTranslate() {
        StringBuilder input = new StringBuilder("&#65;");
        StringBuilder output = new StringBuilder();
        try {
            int translatedChar = unescaper.translate(input, 0, output);
            assertEquals(6, translatedChar);
            assertEquals("A", output.toString());
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void testTranslateWithHexValue() {
        StringBuilder input = new StringBuilder("&#x41;");
        StringBuilder output = new StringBuilder();
        try {
            int translatedChar = unescaper.translate(input, 0, output);
            assertEquals(7, translatedChar);
            assertEquals("A", output.toString());
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void testTranslateWhenNoSemiColon() {
        StringBuilder input = new StringBuilder("&#65");
        StringBuilder output = new StringBuilder();
        try {
            int translatedChar = unescaper.translate(input, 0, output);
            assertEquals(5, translatedChar);
            assertEquals("A", output.toString());
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void testTranslateWithNoHexValueNoSemiColon() {
        StringBuilder input = new StringBuilder("&#65");
        StringBuilder output = new StringBuilder();
        try {
            int translatedChar = unescaper.translate(input, 0, output);
            assertEquals(5, translatedChar);
            assertEquals("A", output.toString());
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void testTranslateWithInvalidHexValue() {
        StringBuilder input = new StringBuilder("&#xDF;");
        StringBuilder output = new StringBuilder();
        try {
            int translatedChar = unescaper.translate(input, 0, output);
            assertEquals(7, translatedChar);
            assertEquals("", output.toString());
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void testTranslateWithInvalidDecimalValue() {
        StringBuilder input = new StringBuilder("&#9999999;");
        StringBuilder output = new StringBuilder();
        try {
            int translatedChar = unescaper.translate(input, 0, output);
            assertEquals(12, translatedChar);
            assertEquals("", output.toString());
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void testTranslateWithOutOfRangeValue() {
        StringBuilder input = new StringBuilder("&#100000;");
        StringBuilder output = new StringBuilder();
        try {
            int translatedChar = unescaper.translate(input, 0, output);
            assertEquals(9, translatedChar);
            assertEquals("", output.toString());
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void testTranslateWithInvalidInputFormat() {
        StringBuilder input = new StringBuilder("ABC");
        StringBuilder output = new StringBuilder();
        try {
            int translatedChar = unescaper.translate(input, 0, output);
            assertEquals(0, translatedChar);
            assertEquals("", output.toString());
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }
}

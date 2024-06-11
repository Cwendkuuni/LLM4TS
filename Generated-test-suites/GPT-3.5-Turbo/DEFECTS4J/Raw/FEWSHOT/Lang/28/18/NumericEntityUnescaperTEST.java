package org.apache.commons.lang3.text.translate;

import org.junit.Test;

import java.io.IOException;
import java.io.StringWriter;

import static org.junit.Assert.assertEquals;

public class NumericEntityUnescaperTest {

    @Test
    public void testTranslate() throws IOException {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();

        // Test case 1: Translate decimal entity
        {
            // Input: "Test &#65; case"
            // Expected output: "Test A case"
            String input = "Test &#65; case";
            String expected = "Test A case";

            StringWriter writer = new StringWriter();

            int translatedLength = unescaper.translate(input, 5, writer);
            String actual = writer.toString();

            assertEquals(expected, actual);
            assertEquals(7, translatedLength);
        }

        // Test case 2: Translate hexadecimal entity
        {
            // Input: "Test &#x41; case"
            // Expected output: "Test A case"
            String input = "Test &#x41; case";
            String expected = "Test A case";

            StringWriter writer = new StringWriter();

            int translatedLength = unescaper.translate(input, 5, writer);
            String actual = writer.toString();

            assertEquals(expected, actual);
            assertEquals(9, translatedLength);
        }

        // Test case 3: Translate invalid entity
        {
            // Input: "Test &#invalid; case"
            // Expected output: "Test &#invalid; case"
            String input = "Test &#invalid; case";
            String expected = "Test &#invalid; case";

            StringWriter writer = new StringWriter();

            int translatedLength = unescaper.translate(input, 5, writer);
            String actual = writer.toString();

            assertEquals(expected, actual);
            assertEquals(0, translatedLength);
        }
    }
}
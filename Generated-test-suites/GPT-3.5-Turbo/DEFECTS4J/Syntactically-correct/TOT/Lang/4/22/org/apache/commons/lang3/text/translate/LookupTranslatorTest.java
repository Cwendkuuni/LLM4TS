package org.apache.commons.lang3.text.translate;

import org.junit.Test;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;

import static org.junit.Assert.assertEquals;

public class LookupTranslatorTest {

    @Test
    public void testTranslate() throws IOException {
        LookupTranslator lookupTranslator = new LookupTranslator(
                new CharSequence[][]{{"ab", "12"}, {"cd", "34"}, {"ef", "56"}, {"gh", "78"}}
        );

        Writer writer = new StringWriter();

        // Typical use cases
        int actual = lookupTranslator.translate("abcd", 0, writer);
        assertEquals("Translation failed for typical use case 1", 2, actual);
        assertEquals("Translation failed for typical use case 1", "12cd", writer.toString());

        writer = new StringWriter();
        actual = lookupTranslator.translate("efgh", 0, writer);
        assertEquals("Translation failed for typical use case 2", 2, actual);
        assertEquals("Translation failed for typical use case 2", "56gh", writer.toString());

        // Edge cases
        writer = new StringWriter();
        actual = lookupTranslator.translate("abef", 0, writer);
        assertEquals("Translation failed for edge case 1", 2, actual);
        assertEquals("Translation failed for edge case 1", "12ef", writer.toString());

        writer = new StringWriter();
        actual = lookupTranslator.translate("ghgh", 0, writer);
        assertEquals("Translation failed for edge case 2", 2, actual);
        assertEquals("Translation failed for edge case 2", "7878", writer.toString());
    }
}
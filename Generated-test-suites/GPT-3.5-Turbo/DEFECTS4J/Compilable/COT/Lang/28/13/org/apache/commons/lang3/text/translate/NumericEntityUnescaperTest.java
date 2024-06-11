package org.apache.commons.lang3.text.translate;
import org.junit.Test;
import org.apache.commons.lang3.text.translate.NumericEntityUnescaper;
import java.io.IOException;
import java.io.Writer;

public class NumericEntityUnescaperTest {

    @Test
    public void testTranslate() throws IOException {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        StringBuilder sb = new StringBuilder();
        Writer writer = new Writer() {
            @Override
            public void write(char[] cbuf, int off, int len) throws IOException {
                sb.append(cbuf, off, len);
            }
            @Override
            public void flush() throws IOException {
            }
            @Override
            public void close() throws IOException {
            }
        };

        // Test basic numeric entity translation
        CharSequence input = "This is a &#65; test";
        int result = unescaper.translate(input, 10, writer);  // Index of '&#65;'
        assert(result == 7);  // Expected output: "This is a A test"
        assert(sb.toString().equals("A"));

        // Test lowercase hexadecimal numeric entity
        input = "This is a &#x41; test";
        sb.setLength(0);
        result = unescaper.translate(input, 10, writer);  // Index of '&#x41;'
        assert(result == 9);  // Expected output: "This is a A test"
        assert(sb.toString().equals("A"));

        // Test uppercase hexadecimal numeric entity
        input = "This is a &#X41; test";
        sb.setLength(0);
        result = unescaper.translate(input, 10, writer);  // Index of '&#X41;'
        assert(result == 9);  // Expected output: "This is a A test"
        assert(sb.toString().equals("A"));

        // Test exception handling for invalid numeric entity format
        input = "This is an invalid numeric entity: &#ABC;";
        sb.setLength(0);
        result = unescaper.translate(input, 26, writer);  // Index of '&#ABC;'
        assert(result == 0);  // Expected output: no translation, return 0

        // Test exception handling for NumberFormatException
        input = "This is a number format exception: &#123abc;";
        sb.setLength(0);
        result = unescaper.translate(input, 27, writer);  // Index of '&#123abc;'
        assert(result == 0);  // Expected output: no translation, return 0
    }
}
package org.apache.commons.lang3.text.translate;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.Writer;

import org.apache.commons.lang3.CharSequenceUtils;
import org.apache.commons.lang3.text.translate.NumericEntityUnescaper;

import org.junit.Test;

/**
 * Test class for NumericEntityUnescaper
 */
public class NumericEntityUnescaperTest {

    /**
     * Test method for {@link NumericEntityUnescaper#translate(CharSequence, int, Writer)}
     */
    @Test
    public void testTranslate() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        Writer writer = new Writer() {
            @Override
            public void write(int c) throws IOException {
                outputStream.write(c);
            }

            @Override
            public void write(char[] c, int off, int len) throws IOException {
                outputStream.write(c, off, len);
            }

            @Override
            public void flush() throws IOException {
                // Not used
            }

            @Override
            public void close() throws IOException {
                // Not used
            }
        };

        String input = "&amp; &lt; &gt; &apos; &quot; &#34; &#x22; &#10; &#13; &#160; &#x160;";
        int index = 0;

        // Test basic functionality
        int length = unescaper.translate(new CharSequence() {
            @Override
            public int length() {
                return input.length();
            }

            @Override
            public char charAt(int index) {
                return input.charAt(index);
            }
        }, index, writer);
        assertEquals(input.getBytes().length, outputStream.size());

        // Test specific edge cases and exceptions handling

        // Test empty CharSequence
        index = 0;
        assertThrows(IllegalArgumentException.class, () -> unescaper.translate(new CharSequence() {
            @Override
            public int length() {
                return 0;
            }

            @Override
            public char charAt(int index) {
                throw new UnsupportedOperationException();
            }
        }, index, writer));

        // Test index out of bounds
        index += input.length();
        assertThrows(IndexOutOfBoundsException.class, () -> unescaper.translate(new CharSequence() {
            @Override
            public int length() {
                return input.length();
            }

            @Override
            public char charAt(int index) {
                return input.charAt(index);
            }
        }, index, writer));

        // Test invalid entity
        String invalidEntity = "&%#";
        assertThrows(NumberFormatException.class, () -> unescaper.translate(new CharSequence() {
            @Override
            public int length() {
                return invalidEntity.length();
            }

            @Override
            public char charAt(int index) {
                return invalidEntity.charAt(index);
            }
        }, 0, writer));

        // Test hexadecimal entity with invalid length
        String hexEntityInvalidLength = "&#x";
        assertThrows(NumberFormatException.class, () -> unescaper.translate(new CharSequence() {
            @Override
            public int length() {
                return hexEntityInvalidLength.length();
            }

            @Override
            public char charAt(int index) {
                return hexEntityInvalidLength.charAt(index);
            }
        }, 0, writer));

        // Test hexadecimal entity with invalid character
        String hexEntityInvalidCharacter = "&#xf001";
        assertThrows(NumberFormatException.class, () -> unescaper.translate(new CharSequence() {
            @Override
            public int length() {
                return hexEntityInvalidCharacter.length();
            }

            @Override
            public char charAt(int index) {
                return hexEntityInvalidCharacter.charAt(index);
            }
        }, 0, writer));

        // Test decimal entity with invalid length
        String decEntityInvalidLength = "&#";
        assertThrows(NumberFormatException.class, () -> unescaper.translate(new CharSequence() {
            @Override
            public int length() {
                return decEntityInvalidLength.length();
            }

            @Override
            public char charAt(int index) {
                return decEntityInvalidLength.charAt(index);
            }
        }, 0, writer));

        // Test decimal entity with negative number
        String decEntityNegativeNumber = "&#-1";
        assertThrows(NumberFormatException.class, () -> unescaper.translate(new CharSequence() {
            @Override
            public int length() {
                return decEntityNegativeNumber.length();
            }

            @Override
            public char charAt(int index) {
                return decEntityNegativeNumber.charAt(index);
            }
        }, 0, writer));

        // Test decimal entity with number larger than 0xFFFF
        String decEntityLargeNumber = "&#65536";
        assertThrows(NumberFormatException.class, () -> unescaper.translate(new CharSequence() {
            @Override
            public int length() {
                return decEntityLargeNumber.length();
            }

            @Override
            public char charAt(int index) {
                return decEntityLargeNumber.charAt(index);
            }
        }, 0, writer));

        // Test hexadecimal entity with number larger than 0x10FFFF
        String hexEntityLargeNumber = "&#x110000";
        assertThrows(NumberFormatException.class, () -> unescaper.translate(new CharSequence() {
            @Override
            public int length() {
                return hexEntityLargeNumber.length();
            }

            @Override
            public char charAt(int index) {
                return hexEntityLargeNumber.charAt(index);
            }
        }, 0, writer));

        // Test I/O exception
        ByteArrayOutputStream outputStreamIOException = new ByteArrayOutputStream();
        Writer writerIOException = new Writer() {
            @Override
            public void write(int c) throws IOException {
                throw new IOException();
            }

            @Override
            public void write(char[] c, int off, int len) throws IOException {
                throw new IOException();
            }

            @Override
            public void flush() throws IOException {
                // Not used
            }

            @Override
            public void close() throws IOException {
                // Not used
            }
        };
        assertThrows(IOException.class, () -> unescaper.translate(new CharSequence() {
            @Override
            public int length() {
                return input.length();
            }

            @Override
            public char charAt(int index) {
                return input.charAt(index);
            }
        }, 0, writerIOException));

        // Test IOException with invalid entity
        ByteArrayOutputStream outputStreamIOExceptionInvalidEntity = new ByteArrayOutputStream();
        Writer writerIOExceptionInvalidEntity = new Writer() {
            @Override
            public void write(int c) throws IOException {
                throw new IOException();
            }

            @Override
            public void write(char[] c, int off, int len) throws IOException {
                throw new IOException();
            }

            @Override
            public void flush() throws IOException {
                // Not used
            }

            @Override
            public void close() throws IOException {
                // Not used
            }
        };
        assertThrows(IOException.class, () -> unescaper.translate(new CharSequence() {
            @Override
            public int length() {
                return invalidEntity.length();
            }

            @Override
            public char charAt(int index) {
                return invalidEntity.charAt(index);
            }
        }, 0, writerIOExceptionInvalidEntity));
    }

}
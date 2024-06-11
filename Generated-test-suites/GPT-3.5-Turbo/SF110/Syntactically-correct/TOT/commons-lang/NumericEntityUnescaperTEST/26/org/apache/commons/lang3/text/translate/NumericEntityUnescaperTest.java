package org.apache.commons.lang3.text.translate;

import static org.junit.Assert.*;
import org.junit.*;

public class NumericEntityUnescaperTest {

    NumericEntityUnescaper unescaper;

    @Before
    public void setUp() {
        unescaper = new NumericEntityUnescaper(NumericEntityUnescaper.OPTION.semiColonRequired);
    }

    @Test
    public void testTranslate_WithValidDecimalEntity_ReturnsDecodedValue() {
        StringBuilder input = new StringBuilder("This is a &#65; test.");
        StringBuilder expected = new StringBuilder("This is a A test.");

        WriterMock writerMock = new WriterMock();
        unescaper.translate(input, 9, writerMock);

        assertEquals(expected.toString(), writerMock.getOutput().toString());
    }

    @Test
    public void testTranslate_WithValidDecimalEntity_UpdatesOutputIndexCorrectly() {
        StringBuilder input = new StringBuilder("Index &#48; is here.");
        int index = 6;

        WriterMock writerMock = new WriterMock();
        unescaper.translate(input, index, writerMock);

        assertEquals(index + 3, writerMock.getIndex());
    }

    @Test
    public void testTranslate_WithValidHexadecimalEntity_ReturnsDecodedValue() {
        StringBuilder input = new StringBuilder("Food is great &#x1F957;!");
        StringBuilder expected = new StringBuilder("Food is great 🥗!");

        WriterMock writerMock = new WriterMock();
        unescaper.translate(input, 15, writerMock);

        assertEquals(expected.toString(), writerMock.getOutput().toString());
    }

    @Test
    public void testTranslate_WithValidHexadecimalEntity_UpdatesOutputIndexCorrectly() {
        StringBuilder input = new StringBuilder("The object has id &#x1E;");
        int index = 18;

        WriterMock writerMock = new WriterMock();
        unescaper.translate(input, index, writerMock);

        assertEquals(index + 4, writerMock.getIndex());
    }

    @Test
    public void testTranslate_WithIncompleteHexadecimalEntity_ThrowsIllegalArgumentException() {
        StringBuilder input = new StringBuilder("Incomplete entity &#xABCDEFG;");
        int index = 16;

        WriterMock writerMock = new WriterMock();

        try {
            unescaper.translate(input, index, writerMock);
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            assertEquals("Semi-colon required at end of numeric entity", e.getMessage());
        }
    }

    @Test
    public void testTranslate_WithInvalidDecimalEntity_ReturnsZero() {
        StringBuilder input = new StringBuilder("Invalid entity &#AB;");
        int index = 15;

        WriterMock writerMock = new WriterMock();
        int result = unescaper.translate(input, index, writerMock);

        assertEquals(0, result);
    }

    @Test
    public void testTranslate_WithInvalidHexadecimalEntity_ReturnsZero() {
        StringBuilder input = new StringBuilder("Invalid entity &#xBLA;");
        int index = 18;

        WriterMock writerMock = new WriterMock();
        int result = unescaper.translate(input, index, writerMock);

        assertEquals(0, result);
    }

    @Test
    public void testTranslate_WithEntityValueGreaterThan65535_WritesTwoCharsToOutput() {
        StringBuilder input = new StringBuilder("2 chars &#65536; entity.");
        StringBuilder expected = new StringBuilder("2 chars 𐀀 entity.");

        WriterMock writerMock = new WriterMock();
        unescaper.translate(input, 9, writerMock);

        assertEquals(expected.toString(), writerMock.getOutput().toString());
    }

    @Test
    public void testTranslate_WithEntityValueLessThanOrEqual65535_WritesOneCharToOutput() {
        StringBuilder input = new StringBuilder("1 char &#65; entity.");
        StringBuilder expected = new StringBuilder("1 char A entity.");

        WriterMock writerMock = new WriterMock();
        unescaper.translate(input, 9, writerMock);

        assertEquals(expected.toString(), writerMock.getOutput().toString());
    }

    @Test
    public void testTranslate_WithSemicolonRequiredAndNoSemicolon_ReturnsZero() {
        StringBuilder input = new StringBuilder("Entity without semicolon &#65");
        int index = 24;

        WriterMock writerMock = new WriterMock();
        int result = unescaper.translate(input, index, writerMock);

        assertEquals(0, result);
    }

    @Test
    public void testTranslate_WithSemicolonOptionalAndNoSemicolon_ReturnsEntityLength() {
        NumericEntityUnescaper unescaperOptional = new NumericEntityUnescaper(NumericEntityUnescaper.OPTION.semiColonOptional);

        StringBuilder input = new StringBuilder("Entity without semicolon &#65");
        int index = 24;

        WriterMock writerMock = new WriterMock();
        int result = unescaperOptional.translate(input, index, writerMock);

        assertEquals(9, result);
    }
    
    @Test
    public void testTranslate_WithErrorIfNoSemicolonAndNoSemicolon_ThrowsIllegalArgumentException() {
        NumericEntityUnescaper unescaperError = new NumericEntityUnescaper(NumericEntityUnescaper.OPTION.errorIfNoSemiColon);

        StringBuilder input = new StringBuilder("Entity without semicolon &#65");
        int index = 24;

        WriterMock writerMock = new WriterMock();
        
        try {
            unescaperError.translate(input, index, writerMock);
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            assertEquals("Semi-colon required at end of numeric entity", e.getMessage());
        }
    }

    private class WriterMock extends Writer {
        private StringBuilder output = new StringBuilder();
        private int index = 0;

        @Override
        public void write(char[] chars, int off, int len) {
            output.append(chars, off, len);
            index += len;
        }

        @Override
        public void flush() {}

        @Override
        public void close() {}

        public StringBuilder getOutput() {
            return output;
        }

        public int getIndex() {
            return index;
        }
    }
}
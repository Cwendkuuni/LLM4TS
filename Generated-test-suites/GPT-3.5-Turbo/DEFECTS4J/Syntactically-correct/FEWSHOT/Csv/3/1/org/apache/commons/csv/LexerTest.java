package org.apache.commons.csv;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.io.StringReader;

import org.junit.Test;

public class LexerTest {

    @Test
    public void testGetLineNumber() throws IOException {
        // Arrange
        CSVFormat format = CSVFormat.DEFAULT;
        StringReader reader = new StringReader("abc\n123\ndef\n");
        Lexer lexer = new Lexer(format, new ExtendedBufferedReader(reader));

        // Act
        long lineNumber1 = lexer.getLineNumber();
        lexer.in.read();
        long lineNumber2 = lexer.getLineNumber();
        lexer.in.readLine();
        long lineNumber3 = lexer.getLineNumber();

        // Assert
        assertEquals(1, lineNumber1);
        assertEquals(1, lineNumber2);
        assertEquals(2, lineNumber3);
    }

    @Test
    public void testReadEscape_CarriageReturn() throws IOException {
        // Arrange
        CSVFormat format = CSVFormat.DEFAULT;
        StringReader reader = new StringReader("abc\n123\ndef\n");
        Lexer lexer = new Lexer(format, new ExtendedBufferedReader(reader));

        // Act
        int escapedChar = lexer.readEscape();

        // Assert
        assertEquals(Lexer.CR, escapedChar);
        assertEquals(Lexer.LF, lexer.in.getLastChar());
    }

    @Test
    public void testReadEscape_NewLine() throws IOException {
        // Arrange
        CSVFormat format = CSVFormat.DEFAULT;
        StringReader reader = new StringReader("abc\r\n123\ndef\n");
        Lexer lexer = new Lexer(format, new ExtendedBufferedReader(reader));

        // Act
        int escapedChar = lexer.readEscape();

        // Assert
        assertEquals(Lexer.LF, escapedChar);
        assertTrue(lexer.readEndOfLine(Lexer.CR));
    }

    @Test(expected = IOException.class)
    public void testReadEscape_EndOfStream() throws IOException {
        // Arrange
        CSVFormat format = CSVFormat.DEFAULT;
        StringReader reader = new StringReader("abc\n123\ndef");
        Lexer lexer = new Lexer(format, new ExtendedBufferedReader(reader));

        // Act
        lexer.readEscape();
    }

    @Test
    public void testTrimTrailingSpaces() {
        // Arrange
        StringBuilder buffer1 = new StringBuilder("abc ");
        StringBuilder buffer2 = new StringBuilder("123\r\n");
        StringBuilder buffer3 = new StringBuilder("def ");

        // Act
        Lexer.trimTrailingSpaces(buffer1);
        Lexer.trimTrailingSpaces(buffer2);
        Lexer.trimTrailingSpaces(buffer3);

        // Assert
        assertEquals("abc", buffer1.toString());
        assertEquals("123", buffer2.toString());
        assertEquals("def", buffer3.toString());
    }

    @Test
    public void testReadEndOfLine_NewLine() throws IOException {
        // Arrange
        CSVFormat format = CSVFormat.DEFAULT;
        StringReader reader = new StringReader("abc\n123\ndef\n");
        Lexer lexer = new Lexer(format, new ExtendedBufferedReader(reader));

        // Act
        boolean isEndOfLine = lexer.readEndOfLine(Lexer.LF);

        // Assert
        assertTrue(isEndOfLine);
    }

    @Test
    public void testIsWhitespace() {
        // Arrange
        Lexer lexer = new Lexer(CSVFormat.DEFAULT, null);

        // Act
        boolean isWhitespace1 = lexer.isWhitespace(Lexer.TAB);
        boolean isWhitespace2 = lexer.isWhitespace(Lexer.LF);

        // Assert
        assertTrue(isWhitespace1);
        assertTrue(isWhitespace2);
    }

    @Test
    public void testIsStartOfLine() {
        // Arrange
        Lexer lexer = new Lexer(CSVFormat.DEFAULT, null);

        // Act
        boolean isStartOfLine1 = lexer.isStartOfLine(Lexer.LF);
        boolean isStartOfLine2 = lexer.isStartOfLine(Lexer.CR);
        boolean isStartOfLine3 = lexer.isStartOfLine(Lexer.UNDEFINED);

        // Assert
        assertTrue(isStartOfLine1);
        assertTrue(isStartOfLine2);
        assertTrue(isStartOfLine3);
    }

    @Test
    public void testIsEndOfFile() {
        // Arrange
        Lexer lexer = new Lexer(CSVFormat.DEFAULT, null);

        // Act
        boolean isEndOfFile = lexer.isEndOfFile(ExtendedBufferedReader.END_OF_STREAM);

        // Assert
        assertTrue(isEndOfFile);
    }

    @Test
    public void testIsDelimiter() {
        // Arrange
        Lexer lexer1 = new Lexer(CSVFormat.DEFAULT, null);
        Lexer lexer2 = new Lexer(CSVFormat.EXCEL, null);

        // Act
        boolean isDelimiter1 = lexer1.isDelimiter(',');
        boolean isDelimiter2 = lexer2.isDelimiter(',');

        // Assert
        assertTrue(isDelimiter1);
        assertTrue(isDelimiter2);
    }

    @Test
    public void testIsEscape() {
        // Arrange
        Lexer lexer = new Lexer(CSVFormat.DEFAULT, null);

        // Act
        boolean isEscape = lexer.isEscape('\\');

        // Assert
        assertTrue(isEscape);
    }

    @Test
    public void testIsQuoteChar() {
        // Arrange
        Lexer lexer = new Lexer(CSVFormat.DEFAULT, null);

        // Act
        boolean isQuoteChar = lexer.isQuoteChar('"');

        // Assert
        assertTrue(isQuoteChar);
    }

    @Test
    public void testIsCommentStart() {
        // Arrange
        Lexer lexer = new Lexer(CSVFormat.DEFAULT, null);

        // Act
        boolean isCommentStart = lexer.isCommentStart('#');

        // Assert
        assertTrue(isCommentStart);
    }
}
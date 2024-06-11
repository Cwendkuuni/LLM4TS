package org.apache.commons.csv;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.apache.commons.csv.Lexer;
import org.apache.commons.csv.ExtendedBufferedReader;
import org.apache.commons.csv.CSVFormat;

import java.io.IOException;
import java.io.StringReader;

public class LexerTest {

    private Lexer lexer;

    @Before
    public void setup() {
        ExtendedBufferedReader reader = new ExtendedBufferedReader(new StringReader(""));
        CSVFormat format = CSVFormat.DEFAULT;
        lexer = new Lexer(format, reader) {
            @Override
            Token nextToken(Token reusableToken) throws IOException {
                return null;
            }
        };
    }

    @Test
    public void getLineNumber() {
        long lineNumber = lexer.getLineNumber();
        Assert.assertEquals(0, lineNumber);
    }

    @Test
    public void readEscape_ValidEscapeSequence_ReturnsUnescapedCharacter() throws IOException {
        ExtendedBufferedReader reader = new ExtendedBufferedReader(new StringReader("\\n"));
        CSVFormat format = CSVFormat.DEFAULT;
        lexer = new Lexer(format, reader) {
            @Override
            Token nextToken(Token reusableToken) throws IOException {
                return null;
            }
        };

        int unescapedCharacter = lexer.readEscape();

        Assert.assertEquals('\n', unescapedCharacter);
    }

    @Test(expected = IOException.class)
    public void readEscape_InvalidEscapeSequence_ThrowsIOException() throws IOException {
        ExtendedBufferedReader reader = new ExtendedBufferedReader(new StringReader("\\z"));
        CSVFormat format = CSVFormat.DEFAULT;
        lexer = new Lexer(format, reader) {
            @Override
            Token nextToken(Token reusableToken) throws IOException {
                return null;
            }
        };

        lexer.readEscape();
    }

    @Test
    public void trimTrailingSpaces_StringBuilderWithTrailingSpaces_TrailingSpacesTrimmed() {
        StringBuilder buffer = new StringBuilder("test  ");
        lexer.trimTrailingSpaces(buffer);
        Assert.assertEquals("test", buffer.toString());
    }

    @Test
    public void readEndOfLine_WithLFCharacter_ReturnsTrue() throws IOException {
        ExtendedBufferedReader reader = new ExtendedBufferedReader(new StringReader("\n"));
        CSVFormat format = CSVFormat.DEFAULT;
        lexer = new Lexer(format, reader) {
            @Override
            Token nextToken(Token reusableToken) throws IOException {
                return null;
            }
        };

        boolean isEndOfLine = lexer.readEndOfLine(10);

        Assert.assertTrue(isEndOfLine);
    }

    @Test
    public void isWhitespace_WithWhitespaceCharacter_ReturnsTrue() {
        boolean isWhitespace = lexer.isWhitespace(32);
        Assert.assertTrue(isWhitespace);
    }

    @Test
    public void isStartOfLine_WithStartOfLineCharacter_ReturnsTrue() {
        boolean isStartOfLine = lexer.isStartOfLine(10);
        Assert.assertTrue(isStartOfLine);
    }

    @Test
    public void isEndOfFile_WithEndOfFileCharacter_ReturnsTrue() {
        boolean isEndOfFile = lexer.isEndOfFile(-1);
        Assert.assertTrue(isEndOfFile);
    }

    @Test
    public void isDelimiter_WithDelimiterCharacter_ReturnsTrue() {
        boolean isDelimiter = lexer.isDelimiter(44);
        Assert.assertTrue(isDelimiter);
    }

    @Test
    public void isEscape_WithEscapeCharacter_ReturnsTrue() {
        boolean isEscape = lexer.isEscape(92);
        Assert.assertTrue(isEscape);
    }

    @Test
    public void isQuoteChar_WithQuoteChar_ReturnsTrue() {
        boolean isQuoteChar = lexer.isQuoteChar(34);
        Assert.assertTrue(isQuoteChar);
    }

    @Test
    public void isCommentStart_WithCommentStartCharacter_ReturnsTrue() {
        boolean isCommentStart = lexer.isCommentStart(35);
        Assert.assertTrue(isCommentStart);
    }
}
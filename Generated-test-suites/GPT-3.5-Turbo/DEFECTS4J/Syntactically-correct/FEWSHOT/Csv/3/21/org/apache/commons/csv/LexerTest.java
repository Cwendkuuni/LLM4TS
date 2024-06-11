/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import static org.apache.commons.csv.Constants.BACKSPACE;
import static org.apache.commons.csv.Constants.CR;
import static org.apache.commons.csv.Constants.END_OF_STREAM;
import static org.apache.commons.csv.Constants.FF;
import static org.apache.commons.csv.Constants.LF;
import static org.apache.commons.csv.Constants.TAB;
import static org.apache.commons.csv.Constants.UNDEFINED;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.io.StringReader;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.ExtendedBufferedReader;
import org.apache.commons.csv.Lexer;
import org.apache.commons.csv.Token;
import org.junit.Test;

public class LexerTest {
    private static final CSVFormat FORMAT = CSVFormat.DEFAULT;

    @Test
    public void testGetLineNumber() throws IOException {
        StringReader reader = new StringReader("Line 1\nLine 2\nLine 3");
        ExtendedBufferedReader bufferedReader = new ExtendedBufferedReader(reader);
        Lexer lexer = new Lexer(FORMAT, bufferedReader);

        assertEquals(0, lexer.getLineNumber());

        lexer.nextToken(null);
        assertEquals(1, lexer.getLineNumber());

        lexer.nextToken(null);
        assertEquals(2, lexer.getLineNumber());

        lexer.nextToken(null);
        assertEquals(3, lexer.getLineNumber());
    }

    @Test
    public void testReadEscape() throws IOException {
        StringReader reader = new StringReader("\\r\\n\\t\\b\\f\\\\\\\"a");
        ExtendedBufferedReader bufferedReader = new ExtendedBufferedReader(reader);
        Lexer lexer = new Lexer(FORMAT, bufferedReader);

        assertEquals(CR, lexer.readEscape());
        assertEquals(LF, lexer.readEscape());
        assertEquals(TAB, lexer.readEscape());
        assertEquals(BACKSPACE, lexer.readEscape());
        assertEquals(FF, lexer.readEscape());
        assertEquals('\\', lexer.readEscape());
        assertEquals('\"', lexer.readEscape());
        assertEquals('a', lexer.readEscape());
    }

    @Test(expected = IOException.class)
    public void testReadEscapeEndOfStream() throws IOException {
        StringReader reader = new StringReader("\\");
        ExtendedBufferedReader bufferedReader = new ExtendedBufferedReader(reader);
        Lexer lexer = new Lexer(FORMAT, bufferedReader);

        lexer.readEscape();
    }

    @Test
    public void testTrimTrailingSpaces() {
        StringBuilder buffer = new StringBuilder("   Test   ");
        Lexer lexer = new Lexer(FORMAT, null);

        lexer.trimTrailingSpaces(buffer);
        assertEquals("   Test", buffer.toString());

        buffer.setLength(0);
        buffer.append("Test");
        lexer.trimTrailingSpaces(buffer);
        assertEquals("Test", buffer.toString());

        buffer.setLength(0);
        buffer.append("   ");
        lexer.trimTrailingSpaces(buffer);
        assertEquals("", buffer.toString());
    }

    @Test
    public void testReadEndOfLine() throws IOException {
        StringReader reader = new StringReader("\n\r\n\r123");
        ExtendedBufferedReader bufferedReader = new ExtendedBufferedReader(reader);
        Lexer lexer = new Lexer(FORMAT, bufferedReader);

        assertTrue(lexer.readEndOfLine(bufferedReader.read()));
        assertFalse(lexer.readEndOfLine(bufferedReader.read()));
        assertTrue(lexer.readEndOfLine(bufferedReader.read()));
        assertFalse(lexer.readEndOfLine(bufferedReader.read()));
        assertFalse(lexer.readEndOfLine(bufferedReader.read()));
    }

    @Test
    public void testIsWhitespace() {
        Lexer lexer = new Lexer(FORMAT, null);

        assertTrue(lexer.isWhitespace(' '));
        assertFalse(lexer.isWhitespace(','));
        assertFalse(lexer.isWhitespace('a'));
    }

    @Test
    public void testIsStartOfLine() {
        Lexer lexer = new Lexer(FORMAT, null);

        assertTrue(lexer.isStartOfLine(LF));
        assertTrue(lexer.isStartOfLine(CR));
        assertTrue(lexer.isStartOfLine(UNDEFINED));
        assertFalse(lexer.isStartOfLine(','));
        assertFalse(lexer.isStartOfLine('a'));
    }

    @Test
    public void testIsEndOfFile() {
        Lexer lexer = new Lexer(FORMAT, null);

        assertTrue(lexer.isEndOfFile(END_OF_STREAM));
        assertFalse(lexer.isEndOfFile('a'));
    }

    @Test
    public void testIsDelimiter() {
        Lexer lexer = new Lexer(FORMAT, null);

        assertTrue(lexer.isDelimiter(','));
        assertFalse(lexer.isDelimiter('a'));
        assertFalse(lexer.isDelimiter(' '));
    }

    @Test
    public void testIsEscape() {
        Lexer lexer = new Lexer(FORMAT, null);

        assertTrue(lexer.isEscape('\\'));
        assertFalse(lexer.isEscape('a'));
        assertFalse(lexer.isEscape(' '));
    }

    @Test
    public void testIsQuoteChar() {
        Lexer lexer = new Lexer(FORMAT, null);

        assertFalse(lexer.isQuoteChar('\\'));
        assertFalse(lexer.isQuoteChar('a'));
        assertFalse(lexer.isQuoteChar(' '));
        assertFalse(lexer.isQuoteChar(','));
    }

    @Test
    public void testIsCommentStart() {
        Lexer lexer = new Lexer(FORMAT, null);

        assertFalse(lexer.isCommentStart('\\'));
        assertFalse(lexer.isCommentStart('a'));
        assertFalse(lexer.isCommentStart(' '));
        assertTrue(lexer.isCommentStart('#'));
    }

    // Add test cases for nextToken() method
}
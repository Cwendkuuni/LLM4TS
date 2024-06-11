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

package org.apache.commons.csv;

import static org.apache.commons.csv.Constants.*;
import static org.junit.Assert.*;

import java.io.IOException;

import org.junit.Test;

public class LexerTest {
    private ExtendedBufferedReader in;

    @Test
    public void testGetLineNumber() throws IOException {
        in = new ExtendedBufferedReader(null);
        long lineNumber = in.getLineNumber();
        assertEquals(0, lineNumber);

        in.readLine();
        lineNumber = in.getLineNumber();
        assertEquals(1, lineNumber);

        in.readLine();
        lineNumber = in.getLineNumber();
        assertEquals(2, lineNumber);
    }

    @Test
    public void testMapNullToDisabled() {
        Lexer lexer = new Lexer(null, null);
        char result = lexer.mapNullToDisabled(null);
        assertEquals(DISABLED, result);

        result = lexer.mapNullToDisabled('a');
        assertEquals('a', result);
    }

    @Test
    public void testReadEscape() throws IOException {
        in = new ExtendedBufferedReader(null);
        in.append("r");
        int result = in.read();
        assertEquals('\r', result);
    }

    @Test(expected = IOException.class)
    public void testReadEscapeThrowsIOException() throws IOException {
        in = new ExtendedBufferedReader(null);
        in.append(END_OF_STREAM);
        in.append("r");
        in.read();
    }

    @Test
    public void testTrimTrailingSpaces() {
        StringBuilder buffer = new StringBuilder("foo ");
        Lexer lexer = new Lexer(null, null);
        lexer.trimTrailingSpaces(buffer);
        assertEquals("foo", buffer.toString());

        buffer = new StringBuilder("  ");
        lexer.trimTrailingSpaces(buffer);
        assertEquals("", buffer.toString());

        buffer = new StringBuilder("bar");
        lexer.trimTrailingSpaces(buffer);
        assertEquals("bar", buffer.toString());
    }

    @Test
    public void testReadEndOfLine() throws IOException {
        in = new ExtendedBufferedReader(null);
        in.append('\n');
        boolean result = in.isNewLine(in.read());
        assertTrue(result);

        in = new ExtendedBufferedReader(null);
        in.append("\r\n");
        result = in.isNewLine(in.read());
        assertTrue(result);

        in = new ExtendedBufferedReader(null);
        in.append('\r');
        result = in.isNewLine(in.read());
        assertTrue(result);

        in = new ExtendedBufferedReader(null);
        in.append('a');
        result = in.isNewLine(in.read());
        assertFalse(result);
    }

    @Test
    public void testIsWhitespace() {
        Lexer lexer = new Lexer(null, null);

        boolean result = lexer.isWhitespace(32);
        assertTrue(result);

        result = lexer.isWhitespace(9);
        assertTrue(result);

        result = lexer.isWhitespace(10);
        assertFalse(result);

        result = lexer.isWhitespace(13);
        assertFalse(result);
    }

    @Test
    public void testIsStartOfLine() {
        Lexer lexer = new Lexer(null, null);

        boolean result = lexer.isStartOfLine(10);
        assertTrue(result);

        result = lexer.isStartOfLine(13);
        assertTrue(result);

        result = lexer.isStartOfLine(-2);
        assertTrue(result);

        result = lexer.isStartOfLine(32);
        assertFalse(result);
    }

    @Test
    public void testIsEndOfFile() {
        Lexer lexer = new Lexer(null, null);

        boolean result = lexer.isEndOfFile(-1);
        assertTrue(result);

        result = lexer.isEndOfFile(-2);
        assertFalse(result);

        result = lexer.isEndOfFile(32);
        assertFalse(result);
    }

    @Test
    public void testIsDelimiter() {
        Lexer lexer = new Lexer(null, null);

        boolean result = lexer.isDelimiter(44);
        assertTrue(result);

        result = lexer.isDelimiter(59);
        assertFalse(result);

        result = lexer.isDelimiter(32);
        assertFalse(result);
    }

    @Test
    public void testIsEscape() {
        Lexer lexer = new Lexer(null, null);

        boolean result = lexer.isEscape(92);
        assertTrue(result);

        result = lexer.isEscape(59);
        assertFalse(result);

        result = lexer.isEscape(32);
        assertFalse(result);
    }

    @Test
    public void testIsQuoteChar() {
        Lexer lexer = new Lexer(null, null);

        boolean result = lexer.isQuoteChar(34);
        assertTrue(result);

        result = lexer.isQuoteChar(59);
        assertFalse(result);

        result = lexer.isQuoteChar(32);
        assertFalse(result);
    }

    @Test
    public void testIsCommentStart() {
        Lexer lexer = new Lexer(null, null);

        boolean result = lexer.isCommentStart(35);
        assertTrue(result);

        result = lexer.isCommentStart(59);
        assertFalse(result);

        result = lexer.isCommentStart(32);
        assertFalse(result);
    }

    @Test
    public void testNextToken() throws IOException {
        in = new ExtendedBufferedReader(null);
        Lexer lexer = new Lexer(null, in);

        Token result = lexer.nextToken(null);
        assertNull(result);
    }

}
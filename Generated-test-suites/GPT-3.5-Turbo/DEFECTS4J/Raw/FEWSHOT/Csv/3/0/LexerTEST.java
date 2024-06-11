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

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.io.StringReader;

import org.junit.Test;

public class LexerTest {
    @Test
    public void testGetLineNumber() throws IOException {
        CSVFormat format = CSVFormat.DEFAULT;
        StringReader reader = new StringReader("line1\nline2\nline3\n");
        ExtendedBufferedReader in = new ExtendedBufferedReader(reader);
        Lexer lexer = new Lexer(format, in);

        assertEquals(0, lexer.getLineNumber());
        lexer.nextToken(new Token());

        assertEquals(1, lexer.getLineNumber());
        lexer.nextToken(new Token());

        assertEquals(2, lexer.getLineNumber());
        lexer.nextToken(new Token());

        assertEquals(3, lexer.getLineNumber());
        lexer.nextToken(new Token());

        // End of file, should still be on line 3
        assertEquals(3, lexer.getLineNumber());
    }

    @Test
    public void testReadEscape() throws IOException {
        CSVFormat format = CSVFormat.DEFAULT;
        StringReader reader = new StringReader("\r\n\\,\"");
        ExtendedBufferedReader in = new ExtendedBufferedReader(reader);
        Lexer lexer = new Lexer(format, in);

        assertEquals(Lexer.CR, lexer.readEscape());
        assertEquals(Lexer.LF, lexer.readEscape());
        assertEquals('\\', lexer.readEscape());
        assertEquals(',', lexer.readEscape());
        assertEquals('"', lexer.readEscape());

        try {
            lexer.readEscape();
        } catch (IOException e) {
            assertEquals("EOF whilst processing escape sequence", e.getMessage());
        }
    }

    @Test
    public void testTrimTrailingSpaces() {
        CSVFormat format = CSVFormat.DEFAULT;
        StringReader reader = new StringReader("");
        ExtendedBufferedReader in = new ExtendedBufferedReader(reader);
        Lexer lexer = new Lexer(format, in);
        StringBuilder buffer = new StringBuilder();

        buffer.append("test  ");
        lexer.trimTrailingSpaces(buffer);
        assertEquals("test", buffer.toString());

        buffer.setLength(0);
        buffer.append("  test");
        lexer.trimTrailingSpaces(buffer);
        assertEquals("  test", buffer.toString());

        buffer.setLength(0);
        buffer.append("  test  ");
        lexer.trimTrailingSpaces(buffer);
        assertEquals("  test", buffer.toString());
    }

    @Test
    public void testReadEndOfLine() throws IOException {
        CSVFormat format = CSVFormat.DEFAULT;
        StringReader reader = new StringReader("\n\r\n\r");
        ExtendedBufferedReader in = new ExtendedBufferedReader(reader);
        Lexer lexer = new Lexer(format, in);

        assertEquals(true, lexer.readEndOfLine(in.read()));
        assertEquals(true, lexer.readEndOfLine(in.read()));
        assertEquals(true, lexer.readEndOfLine(in.read()));
        assertEquals(true, lexer.readEndOfLine(in.read()));
    }

    @Test
    public void testIsWhitespace() {
        CSVFormat format = CSVFormat.DEFAULT;
        StringReader reader = new StringReader("");
        ExtendedBufferedReader in = new ExtendedBufferedReader(reader);
        Lexer lexer = new Lexer(format, in);

        assertEquals(false, lexer.isWhitespace('\n'));
        assertEquals(true, lexer.isWhitespace(' '));
        assertEquals(false, lexer.isWhitespace('"'));
        assertEquals(false, lexer.isWhitespace(','));
    }

    @Test
    public void testIsStartOfLine() {
        CSVFormat format = CSVFormat.DEFAULT;
        StringReader reader = new StringReader("");
        ExtendedBufferedReader in = new ExtendedBufferedReader(reader);
        Lexer lexer = new Lexer(format, in);

        assertEquals(true, lexer.isStartOfLine('\n'));
        assertEquals(true, lexer.isStartOfLine('\r'));
        assertEquals(true, lexer.isStartOfLine(Lexer.UNDEFINED));
        assertEquals(false, lexer.isStartOfLine(' '));
        assertEquals(false, lexer.isStartOfLine('"'));
        assertEquals(false, lexer.isStartOfLine(','));
        assertEquals(false, lexer.isStartOfLine('a'));
    }

    @Test
    public void testIsEndOfFile() {
        CSVFormat format = CSVFormat.DEFAULT;
        StringReader reader = new StringReader("");
        ExtendedBufferedReader in = new ExtendedBufferedReader(reader);
        Lexer lexer = new Lexer(format, in);

        assertEquals(true, lexer.isEndOfFile(-1));
        assertEquals(false, lexer.isEndOfFile(0));
        assertEquals(false, lexer.isEndOfFile(1));
    }

    @Test
    public void testIsDelimiter() {
        CSVFormat format = CSVFormat.DEFAULT;
        StringReader reader = new StringReader("");
        ExtendedBufferedReader in = new ExtendedBufferedReader(reader);
        Lexer lexer = new Lexer(format, in);

        assertEquals(false, lexer.isDelimiter('\n'));
        assertEquals(false, lexer.isDelimiter('\r'));
        assertEquals(false, lexer.isDelimiter(' '));
        assertEquals(true, lexer.isDelimiter(','));
        assertEquals(false, lexer.isDelimiter('"'));
    }

    @Test
    public void testIsEscape() {
        CSVFormat format = CSVFormat.DEFAULT;
        StringReader reader = new StringReader("");
        ExtendedBufferedReader in = new ExtendedBufferedReader(reader);
        Lexer lexer = new Lexer(format, in);

        assertEquals(false, lexer.isEscape('\n'));
        assertEquals(false, lexer.isEscape('\r'));
        assertEquals(false, lexer.isEscape(' '));
        assertEquals(false, lexer.isEscape(','));
        assertEquals(true, lexer.isEscape('\\'));
        assertEquals(false, lexer.isEscape('"'));
    }

    @Test
    public void testIsQuoteChar() {
        CSVFormat format = CSVFormat.DEFAULT;
        StringReader reader = new StringReader("");
        ExtendedBufferedReader in = new ExtendedBufferedReader(reader);
        Lexer lexer = new Lexer(format, in);

        assertEquals(false, lexer.isQuoteChar('\n'));
        assertEquals(false, lexer.isQuoteChar('\r'));
        assertEquals(false, lexer.isQuoteChar(' '));
        assertEquals(false, lexer.isQuoteChar(','));
        assertEquals(false, lexer.isQuoteChar('\\'));
        assertEquals(true, lexer.isQuoteChar('"'));
    }

    @Test
    public void testIsCommentStart() {
        CSVFormat format = CSVFormat.DEFAULT;
        StringReader reader = new StringReader("");
        ExtendedBufferedReader in = new ExtendedBufferedReader(reader);
        Lexer lexer = new Lexer(format, in);

        assertEquals(false, lexer.isCommentStart('\n'));
        assertEquals(false, lexer.isCommentStart('\r'));
        assertEquals(false, lexer.isCommentStart(' '));
        assertEquals(false, lexer.isCommentStart(','));
        assertEquals(false, lexer.isCommentStart('\\'));
        assertEquals(true, lexer.isCommentStart('#'));
        assertEquals(false, lexer.isCommentStart('"'));
    }
}
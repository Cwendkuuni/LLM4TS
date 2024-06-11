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

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class LexerTest {
    private Lexer lexer;

    @Before
    public void setup() {
        CSVFormat format = CSVFormat.DEFAULT;
        ExtendedBufferedReader in = new ExtendedBufferedReader(null);
        lexer = new Lexer(format, in);
    }

    @Test
    public void testGetLineNumber() {
        assertEquals(0, lexer.getLineNumber());
    }

    @Test
    public void testReadEscape() {
        try {
            assertEquals('\\', (char) lexer.readEscape());
        } catch (Exception e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }

    @Test
    public void testTrimTrailingSpaces() {
        StringBuilder buffer = new StringBuilder("test ");
        lexer.trimTrailingSpaces(buffer);
        assertEquals("test", buffer.toString());

        buffer = new StringBuilder("   ");
        lexer.trimTrailingSpaces(buffer);
        assertEquals("", buffer.toString());

        buffer = new StringBuilder("");
        lexer.trimTrailingSpaces(buffer);
        assertEquals("", buffer.toString());
    }

    @Test
    public void testReadEndOfLine() {
        try {
            assertFalse(lexer.readEndOfLine(10));
            assertFalse(lexer.readEndOfLine(13));
            assertFalse(lexer.readEndOfLine(32));

            assertTrue(lexer.readEndOfLine(13));
            assertTrue(lexer.readEndOfLine(10));

        } catch (Exception e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }

    @Test
    public void testIsWhitespace() {
        assertTrue(lexer.isWhitespace(9)); // Tab
        assertTrue(lexer.isWhitespace(32)); // Space
        assertTrue(lexer.isWhitespace(160)); // Non-Breaking Space
        assertTrue(lexer.isWhitespace(8199)); // Figure Space

        assertFalse(lexer.isWhitespace(10)); // Newline
        assertFalse(lexer.isWhitespace(13)); // Carriage Return
        assertFalse(lexer.isWhitespace(61)); // Equals Sign
        assertFalse(lexer.isWhitespace(97)); // 'a'
    }

    @Test
    public void testIsStartOfLine() {
        assertTrue(lexer.isStartOfLine(UNDEFINED));
        assertTrue(lexer.isStartOfLine(10));
        assertTrue(lexer.isStartOfLine(13));

        assertFalse(lexer.isStartOfLine(32));
        assertFalse(lexer.isStartOfLine(9));
        assertFalse(lexer.isStartOfLine(61));
    }

    @Test
    public void testIsEndOfFile() {
        assertFalse(lexer.isEndOfFile(10));
        assertFalse(lexer.isEndOfFile(13));
        assertFalse(lexer.isEndOfFile(32));
        assertFalse(lexer.isEndOfFile(9));

        assertTrue(lexer.isEndOfFile(-1));
    }

    @Test
    public void testIsDelimiter() {
        assertFalse(lexer.isDelimiter(10));
        assertFalse(lexer.isDelimiter(13));
        assertFalse(lexer.isDelimiter(32));
        assertFalse(lexer.isDelimiter(9));

        assertTrue(lexer.isDelimiter(','));
    }

    @Test
    public void testIsEscape() {
        assertFalse(lexer.isEscape(10));
        assertFalse(lexer.isEscape(13));
        assertFalse(lexer.isEscape(32));
        assertFalse(lexer.isEscape(9));

        assertTrue(lexer.isEscape('\\'));
    }

    @Test
    public void testIsQuoteChar() {
        assertFalse(lexer.isQuoteChar(10));
        assertFalse(lexer.isQuoteChar(13));
        assertFalse(lexer.isQuoteChar(32));
        assertFalse(lexer.isQuoteChar(9));

        assertTrue(lexer.isQuoteChar('"'));
    }

    @Test
    public void testIsCommentStart() {
        assertFalse(lexer.isCommentStart(10));
        assertFalse(lexer.isCommentStart(13));
        assertFalse(lexer.isCommentStart(32));
        assertFalse(lexer.isCommentStart(9));

        assertTrue(lexer.isCommentStart('#'));
    }
}
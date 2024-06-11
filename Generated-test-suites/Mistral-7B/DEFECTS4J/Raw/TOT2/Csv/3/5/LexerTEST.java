package org.junit.tests;

import static org.junit.Assert.*;

import org.apache.commons.csv.Lexer;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.ExtendedBufferedReader;
import org.junit.Test;

public class LexerTest {

    @Test
    public void testLexer() throws IOException {
        Expert1.testNextToken_NormalCase();
        Expert2.testNextToken_EmptyLine();
        Expert3.testNextToken_InvalidEscapeSequence();
    }
}
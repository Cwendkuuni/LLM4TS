package org.apache.commons.csv.test;

import org.apache.commons.csv.Lexer;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.ExtendedBufferedReader;
import org.apache.commons.csv.QuoteMode;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Test cases for {@link Lexer} class.
 */
public class LexerTest {

 private static final String TEST_DATA = "Data1,Data2\n" +
         "Data3,\"Data4,Data5\"\n" +
         "Data6 \"Data7, Data8 Data9\", Data10\n" +
         "Data11, Data12\n";

 private static final String TEST_COMMENTED_DATA = "Data1,Data2\n" +
         "Data3,\"Data4,Data5\"\n" +
         "Data6 \"Data7, Data8 Data9\", Data10\n" +
         "Data11, Data12\n" +
         "/* This is a comment */\n";

 private static final CSVFormat TEST_FORMAT = new CSVFormatBuilder()
         .setDelimiter(';')
         .setHeaderSeparator('=')
         .setIgnoreEmptyLines(true)
         .setIgnoreSurroundingSpaces(true)
         .setQuoteMode(QuoteMode.ALL)
         .build();

 private static final CSVFormat TEST_FORMAT_NO_ESCAPE = new CSVFormatBuilder()
         .setDelimiter(';')
         .setIgnoreEmptyLines(true)
         .setIgnoreSurroundingSpaces(true)
         .build();

 private ExtendedBufferedReader input;
 private Lexer lexer;

 @Before
 public void setUp() throws IOException {
     ByteArrayInputStream bais = new ByteArrayInputStream(TEST_DATA.getBytes());
     InputStreamReader isr = new InputStreamReader(bais);
     input = new ExtendedBufferedReader(isr);
     lexer = new Lexer(TEST_FORMAT, input);
 }

 @Test
 public void testNextToken() throws IOException {
     Token token = new Token();
     for (int i = 0; i < 5; i++) {
         nextToken(token, lexer);
         assertNotNull(token.getTokenType());
         if (i < 4) {
             assertEquals(TokenType.DATA, token.getTokenType());
             assertEquals("Data" + (i + 1), token.getData());
         } else {
             assertEquals(TokenType.EOL, token.getTokenType());
         }
         token = new Token();
     }
 }

 @Test
 public void testNextToken_EndOfFile() throws IOException {
     Token token = new Token();
     int c = -1;
     while ((c = lexer.read()) != -1) {
         nextToken(token, lexer);
     }
     assertNull(token.getTokenType());
 }

 @Test
 public void testNextToken_Comments() throws IOException {
     lexer = new Lexer(TEST_FORMAT, new InputStreamReader(new ByteArrayInputStream(TEST_COMMENTED_DATA.getBytes())));
     Token token = new Token();
     for (int i = 0; i < 5; i++) {
         nextToken(token, lexer);
         if (i < 5) {
             assertNotNull(token.getTokenType());
             assertEquals(TokenType.DATA, token.getTokenType());
             assertEquals("Data" + (i + 1), token.getData());
         }
     }
     token = new Token();
     nextToken(token, lexer);
     assertNull(token.getTokenType());
 }

 @Test
 public void testNextToken_Escapes() throws IOException {
     lexer = new Lexer(TEST_FORMAT_NO_ESCAPE, new InputStreamReader(new ByteArrayInputStream(TEST_DATA.getBytes())));
     Token token = new Token();
     for (int i = 0; i < 5; i++) {
         nextToken(token, lexer);
         if (i < 4) {
             assertNotNull(token.getTokenType());
             assertEquals(TokenType.DATA, token.getTokenType());
             assertEquals("Data" + (i + 1), token.getData());
         } else {
             assertEquals(TokenType.EOL, token.getTokenType());
         }
         token = new Token();
     }

     lexer.read(); // read CR
     lexer.read(); // read LF
     int escape = lexer.read();
     assertEquals(TEST_FORMAT.getEscape(), (char) escape);
     assertEquals(TokenType.ESCAPE, token.getTokenType());
     assertEquals(escape, token.getData().charAt(0));

     nextToken(token, lexer);
     assertNull(token.getTokenType());
 }

 @Test
 public void testNextToken_Quotes() throws IOException {
     lexer = new Lexer(TEST_FORMAT, new InputStreamReader(new ByteArrayInputStream(TEST_DATA.getBytes())));
     Token token = new Token();
     for (int i = 0; i < 5; i++) {
         nextToken(token, lexer);
         if (i < 4) {
             assertNotNull(token.getTokenType());
             assertEquals(TokenType.DATA, token.getTokenType());
             assertTrue(token.getData().length() > 0);
         } else {
             assertEquals(TokenType.EOL, token.getTokenType());
         }
         token = new Token();
     }

     // Quoted data
     lexer.read(); // read '
     token = new Token();
     nextToken(token, lexer);
     assertEquals(TokenType.QUOTED_DATA, token.getTokenType());
     assertEquals("Data4,Data5", token.getData());
     nextToken(token, lexer);
     assertEquals(TokenType.ESCAPE, token.getTokenType());
     assertEquals("\\", token.getData());
     nextToken(token, lexer);
     assertEquals(TokenType.QUOTED_DATA, token.getTokenType());
     assertEquals("Data7, Data8 Data9", token.getData());
     nextToken(token, lexer);
     assertEquals(TokenType.ESCAPE, token.getTokenType());
     assertEquals("\\", token.getData());
     nextToken(token, lexer);
     assertEquals(TokenType.QUOTED_DATA, token.getTokenType());
     assertEquals("Data11", token.getData());
     nextToken(token, lexer);
     assertEquals(TokenType.EOL, token.getTokenType());

     nextToken(token, lexer);
     assertNull(token.getTokenType());
 }

 @Test
 public void testNextToken_EmptyLines() throws IOException {
     lexer = new Lexer(TEST_FORMAT, new InputStreamReader(new ByteArrayInputStream(TEST_DATA.getBytes())));
     List<Boolean> emptyLinesDetected = new ArrayList<>();
     Token token = new Token();
     for (int i = 0; i < 5; i++) {
         nextToken(token, lexer);
         if (i < 5 && lexer.isEndOfLine(token.getLastChar())) {
             emptyLinesDetected.add(true);
         } else {
             emptyLinesDetected.add(false);
         }
         if (i < 4) {
             assertNotNull(token.getTokenType());
             assertEquals(TokenType.DATA, token.getTokenType());
             assertTrue(token.getData().length() > 0);
         } else {
             assertEquals(TokenType.EOL, token.getTokenType());
         }
         token = new Token();
     }

     for (Boolean emptyLineDetected : emptyLinesDetected) {
         assertTrue(emptyLineDetected);
     }

     nextToken(token, lexer);
     assertNull(token.getTokenType());
 }

 private void nextToken(Token token, Lexer lexer) throws IOException {
     token.reset();
     int c = lexer.read();
     if (lexer.isWhitespace(c)) {
         lexer.trimTrailingSpaces(token.getData());
         nextToken(token, lexer);
         return;
     }
     token.setLastChar(c);
     if (lexer.isStartOfLine(c)) {
         token.setTokenType(TokenType.EOL);
         return;
     }
     if (lexer.isEndOfFile(c)) {
         token.setTokenType(TokenType.EOF);
         return;
     }
     if (lexer.isQuoteChar(c)) {
         int quote = c;
         int start = lexer.getLineNumber();
         lexer.read(); // read the opening quote
         while (!lexer.isQuoteChar(lexer.getLastChar()) && lexer.getLineNumber() == start) {
             if (lexer.isEndOfFile()) {
                 throw new IOException("Unexpected EOF while processing quoted data.");
             }
             token.append(lexer.read());
         }
         lexer.read(); // read the closing quote
         token.setTokenType(TokenType.QUOTED_DATA);
         return;
     }
     if (lexer.isEscape(c)) {
         token.append(lexer.readEscape());
         return;
     }
     if (lexer.isCommentStart(c)) {
         lexer.read(); // read the comment start
         int start = lexer.getLineNumber();
         while (!lexer.isCommentStart(lexer.getLastChar()) && lexer.getLineNumber() == start) {
             lexer.read();
         }
         token.setTokenType(TokenType.COMMENT);
         return;
     }
     token.setData(Character.toString(c));
     token.setTokenType(TokenType.DATA);
 }

}
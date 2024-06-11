package org.apache.commons.csv;

import static org.apache.commons.csv.Constants.BACKSPACE;
import static org.apache.commons.csv.Constants.CR;
import static org.apache.commons.csv.Constants.END_OF_STREAM;
import static org.apache.commons.csv.Constants.FF;
import static org.apache.commons.csv.Constants.LF;
import static org.apache.commons.csv.Constants.TAB;
import static org.apache.commons.csv.Constants.UNDEFINED;
import static org.apache.commons.csv.Lexer.isDelimiter;
import static org.apache.commons.csv.Lexer.isEndOfFile;
import static org.apache.commons.csv.Lexer.isEscape;
import static org.apache.commons.csv.Lexer.isQuoteChar;
import static org.apache.commons.csv.Lexer.isStartOfLine;
import static org.junit.Assert.*;

import org.junit.Test;

public class LexerTest {

 final static ExtendedBufferedReader mockReader = new ExtendedBufferedReader(new MockInputStream());
 final static CSVFormat format = new CSVFormat(new StandardQuoteMode(), '\t', '\u0002', '\u0003', '\u0023');
 final static Lexer lexer = new Lexer(format, mockReader);

 @Test
 public void testIsWhitespace() {
 final int tab = '\t';
 final int lf = '\n';
 final int cr = '\r';
 final int space = ' ';

 assertTrue(isWhitespace(space));
 assertTrue(isWhitespace(tab));
 assertTrue(isWhitespace(lf));
 assertTrue(isWhitespace(cr));
 assertFalse(isWhitespace(lexer.delimiter));
 }

 @Test
 public void testIsStartOfLine() {
 final int cr = '\r';
 final int lf = '\n';
 final int undefined = UNDEFINED;

 assertTrue(isStartOfLine(cr));
 assertTrue(isStartOfLine(lf));
 assertTrue(isStartOfLine(undefined));
 assertFalse(isStartOfLine('a'));
 }

 @Test
 public void testIsEndOfFile() {
 final int eof = END_OF_STREAM;

 assertTrue(isEndOfFile(eof));
 assertFalse(isEndOfFile('a'));
 }

 @Test
 public void testIsDelimiter() {
 final int tab = '\t';
 final int delimiter = lexer.delimiter;

 assertTrue(isDelimiter(tab));
 assertFalse(isDelimiter('a'));
 }

 @Test
 public void testIsEscape() {
 final int escape = lexer.escape;

 assertTrue(isEscape(escape));
 assertFalse(isEscape('a'));
 }

 @Test
 public void testIsQuoteChar() {
 final int quoteChar = lexer.quoteChar;

 assertTrue(isQuoteChar(quoteChar));
 assertFalse(isQuoteChar('a'));
 }

 @Test
 public void testIsCommentStart() {
 final int commentStart = lexer.commmentStart;

 assertTrue(isCommentStart(commentStart));
 assertFalse(isCommentStart('a'));
 }

 @Test
 public void testReadEndOfLine() throws IOException {
 final int cr = '\r';
 final int lf = '\n';
 final int lfFollowedByCr = '\n' + '\r';

 assertTrue(lexer.readEndOfLine(cr));
 assertTrue(lexer.readEndOfLine(lf));
 assertTrue(lexer.readEndOfLine(lfFollowedByCr));
 assertFalse(lexer.readEndOfLine('a'));
 }

 @Test
 public void testReadEscape() throws IOException {
 final int cr = '\r';
 final int lf = '\n';
 final int tab = '\t';
 final int backspace = '\b';
 final int formFeed = '\f';
 final int escape = lexer.escape;

 assertEquals(CR, lexer.readEscape());
 assertEquals(LF, lexer.readEscape());
 assertEquals(TAB, lexer.readEscape());
 assertEquals(BACKSPACE, lexer.readEscape());
 assertEquals(FF, lexer.readEscape());
 assertEquals(escape, lexer.readEscape());
 assertEquals(CR, mockReader.getLastChar()); // CR is read, but not consumed
 assertEquals(LF, mockReader.getLastChar()); // LF is read, but not consumed
 assertEquals(TAB, mockReader.getLastChar()); // TAB is read, but not consumed
 assertEquals(BACKSPACE, mockReader.getLastChar()); // Backspace is read, but not consumed
 assertEquals(FF, mockReader.getLastChar()); // FormFeed is read, but not consumed
 assertEquals(escape, mockReader.getLastChar()); // Escape is read, but not consumed
 assertNull(lexer.readEscape()); // End of stream
 }

 @Test(expected = IOException.class)
 public void testReadEscapeAtEndOfStream() throws IOException {
 lexer.readEscape();
 }
}

class MockInputStream extends InputStream {
 @Override
 public int read() throws IOException {
 return -1;
 }
}
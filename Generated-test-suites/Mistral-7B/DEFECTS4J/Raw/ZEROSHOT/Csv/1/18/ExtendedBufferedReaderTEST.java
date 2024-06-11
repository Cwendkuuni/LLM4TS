package org.apache.commons.csv.test;

import org.apache.commons.csv.ExtendedBufferedReader;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class ExtendedBufferedReaderTest {

 @Test
 public void testExtendedBufferedReader() {
 ExtendedBufferedReader reader = new ExtendedBufferedReader(System.in);
 assertNotNull(reader);
 }

 @Test
 public void testRead() throws Exception {
 ExtendedBufferedReader reader = new ExtendedBufferedReader(System.in);
 int c;

 // read first character
 c = reader.read();
 assertEquals(-1, c); // EOF initially

 // read some characters
 String input = "Hello World\n";
 reader.read(new char[input.length().intValue()], 0, input.length());

 // check last character read
 c = reader.readAgain();
 assertEquals('d', c); // last character of "World"

 // check line counter
 assertEquals(1, reader.getLineNumber());
 }

 @Test
 public void testPeek() throws Exception {
 ExtendedBufferedReader reader = new ExtendedBufferedReader(new StringReader("Hello World\n"));
 int c;

 // check lookahead
 c = reader.lookAhead();
 assertEquals('H', c);

 // read character
 c = reader.read();
 assertEquals('H', c);

 // check last character read
 c = reader.readAgain();
 assertEquals('e', c);

 // check line counter
 assertEquals(1, reader.getLineNumber());
 }

 @Test(expected = java.io.IOException.class)
 public void testLookAheadException() throws Exception {
 ExtendedBufferedReader reader = new ExtendedBufferedReader(new StringReader("Hello World\n"));
 int c;

 // read all characters
 reader.read(new char[14], 0, 14); // read "Hello World\n"

 // check lookahead
 c = reader.lookAhead();
 assertEquals(-1, c); // EOF
 }

 @Test
 public void testReadLine() throws Exception {
 ExtendedBufferedReader reader = new ExtendedBufferedReader(new StringReader("Hello\nWorld\n"));
 String line;

 // read first line
 line = reader.readLine();
 assertEquals("Hello", line);
 assertEquals('o', reader.readAgain()); // last character of "Hello"

 // line counter should be 1
 assertEquals(1, reader.getLineNumber());

 // read second line
 line = reader.readLine();
 assertEquals("World", line);
 assertEquals('\n', reader.readAgain()); // last character of "World"

 // line counter should be 2
 assertEquals(2, reader.getLineNumber());

 // EOF
 assertEquals(null, reader.readLine());
 }

 @Test
 public void testReadLineIgnoreCR() throws Exception {
 ExtendedBufferedReader reader = new ExtendedBufferedReader(new StringReader("Hello\r\nWorld\r\n"));
 String line;

 // read first line
 line = reader.readLine();
 assertEquals("Hello", line);
 assertEquals('o', reader.readAgain()); // last character of "Hello"

 // line counter should be 1
 assertEquals(1, reader.getLineNumber());

 // read second line
 line = reader.readLine();
 assertEquals("World", line);
 assertEquals('\n', reader.readAgain()); // last character of "World"

 // line counter should be 2
 assertEquals(2, reader.getLineNumber());
 }

 @Test
 public void testReadLineIgnoreCRAndLF() throws Exception {
 ExtendedBufferedReader reader = new ExtendedBufferedReader(new StringReader("Hello\r\nWorld\r\n"));
 String line;

 // read first line
 line = reader.readLine();
 assertEquals("Hello", line);
 assertEquals('o', reader.readAgain()); // last character of "Hello"

 // read second line
 line = reader.readLine();
 assertEquals("World", line);
 assertEquals('\0', reader.readAgain()); // no character left

 // line counter should be 2
 assertEquals(2, reader.getLineNumber());
 }
}
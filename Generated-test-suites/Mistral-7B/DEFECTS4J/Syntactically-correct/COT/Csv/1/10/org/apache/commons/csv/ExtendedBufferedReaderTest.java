package org.apache.commons.csv.test;

import org.apache.commons.csv.ExtendedBufferedReader;
import org.junit.Assert;
import org.junit.Test;

import java.io.StringReader;

/**
 * ExtendedBufferedReaderTest
 */
 public class ExtendedBufferedReaderTest {

 private ExtendedBufferedReader reader;

 @Before
 public void setup() throws Exception {
 String text = "Line 1\nLine 2\nLine 3\nLine 4\n";
 reader = new ExtendedBufferedReader(new StringReader(text));
 }

 /**
 * Test constructor
 */
 @Test
 public void testExtendedBufferedReaderConstructor() {
 reader = new ExtendedBufferedReader(new StringReader(""));
 Assert.assertNotNull(reader);
 }

 /**
 * Test read method
 */
 @Test
 public void testRead() throws Exception {
 Assert.assertEquals('L', reader.read());
 Assert.assertEquals('\n', reader.read());
 Assert.assertEquals('i', reader.read());
 // Test end of stream
 Assert.assertEquals(ExtendedBufferedReader.END_OF_STREAM, reader.read());
 }

 /**
 * Test readAgain method
 */
 @Test
 public void testReadAgain() throws Exception {
 reader.mark(0);
 reader.read();
 reader.reset();

 Assert.assertEquals('L', reader.readAgain());
 Assert.assertEquals('\n', reader.read());
 Assert.assertEquals('i', reader.readAgain());

 reader.mark(0);
 reader.readLine();
 reader.reset();

 Assert.assertEquals(ExtendedBufferedReader.END_OF_STREAM, reader.readAgain());
 }

 /**
 * Test read method with char array
 */
 @Test
 public void testReadWithCharArray() throws Exception {
 char[] buffer = new char[3];
 reader.read(buffer, 0, 3);

 Assert.assertEquals('L', buffer[0]);
 Assert.assertEquals('i', buffer[1]);
 Assert.assertEquals('\n', buffer[2]);
 }

 /**
 * Test readLine method
 */
 @Test
 public void testReadLine() throws Exception {
 String line = reader.readLine();
 Assert.assertEquals("Line 1", line);
 line = reader.readLine();
 Assert.assertEquals("Line 2", line);
 line = reader.readLine();
 Assert.assertEquals("Line 3", line);
 line = reader.readLine();
 Assert.assertEquals("Line 4", line);
 Assert.assertEquals(ExtendedBufferedReader.END_OF_STREAM, reader.readLine());
 }

 /**
 * Test lookAhead method
 */
 @Test
 public void testLookAhead() throws Exception {
 Assert.assertEquals('L', reader.lookAhead());
 reader.mark(0);
 reader.read();
 reader.reset();

 Assert.assertEquals('i', reader.lookAhead());
 }

 /**
 * Test getLineNumber method
 */
 @Test
 public void testGetLineNumber() throws Exception {
 Assert.assertEquals(0, reader.getLineNumber());
 reader.readLine();
 Assert.assertEquals(1, reader.getLineNumber());
 reader.readLine();
 Assert.assertEquals(2, reader.getLineNumber());
 reader.readLine();
 Assert.assertEquals(3, reader.getLineNumber());
 reader.readLine();
 Assert.assertEquals(4, reader.getLineNumber());
 reader.readLine();
 Assert.assertEquals(-1, reader.getLineNumber());
 }

 /**
 * Test readLine method with leading whitespaces
 */
 @Test
 public void testReadLineWithLeadingWhitespaces() throws Exception {
 String text = "  Line 1\nLine 2\n  Line 3\nLine 4\n";
 reader = new ExtendedBufferedReader(new StringReader(text));

 String line = reader.readLine();
 Assert.assertEquals(" Line 1", line);
 line = reader.readLine();
 Assert.assertEquals("Line 2", line);
 line = reader.readLine();
 Assert.assertEquals("  Line 3", line);
 line = reader.readLine();
 Assert.assertEquals("Line 4", line);
 Assert.assertEquals(ExtendedBufferedReader.END_OF_STREAM, reader.readLine());
 }

 /**
 * Test readLine method with trailing whitespaces
 */
 @Test
 public void testReadLineWithTrailingWhitespaces() throws Exception {
 String text = "Line 1\n Line 2\nLine 3\n Line 4\n";
 reader = new ExtendedBufferedReader(new StringReader(text));

 String line = reader.readLine();
 Assert.assertEquals("Line 1", line);
 line = reader.readLine();
 Assert.assertEquals(" Line 2", line);
 line = reader.readLine();
 Assert.assertEquals("Line 3", line);
 line = reader.readLine();
 Assert.assertEquals(" Line 4", line);
 Assert.assertEquals(ExtendedBufferedReader.END_OF_STREAM, reader.readLine());
 }

 /**
 * Test readLine method with empty lines
 */
 @Test
 public void testReadLineWithEmptyLines() throws Exception {
 String text = "Line 1\n\nLine 3\nLine 4\n";
 reader = new ExtendedBufferedReader(new StringReader(text));

 String line = reader.readLine();
 Assert.assertEquals("Line 1", line);
 line = reader.readLine();
 Assert.assertEquals("", line);
 line = reader.readLine();
 Assert.assertEquals("Line 3", line);
 line = reader.readLine();
 Assert.assertEquals("Line 4", line);
 Assert.assertEquals(ExtendedBufferedReader.END_OF_STREAM, reader.readLine());
 }

 /**
 * Test readLine method with CR and LF line terminators
 */
 @Test
 public void testReadLineWithCRandLF() throws Exception {
 String text = "Line 1\r\nLine 2\r\nLine 3\r\nLine 4\r\n";
 reader = new ExtendedBufferedReader(new StringReader(text));

 String line = reader.readLine();
 Assert.assertEquals("Line 1", line);
 line = reader.readLine();
 Assert.assertEquals("Line 2", line);
 line = reader.readLine();
 Assert.assertEquals("Line 3", line);
 line = reader.readLine();
 Assert.assertEquals("Line 4", line);
 Assert.assertEquals(ExtendedBufferedReader.END_OF_STREAM, reader.readLine());
 }

 /**
 * Test readLine method with mixed line terminators
 */
 @Test
 public void testReadLineWithMixedLineTerminators() throws Exception {
 String text = "Line 1\r\nLine 2\nLine 3\r\nLine 4\n";
 reader = new ExtendedBufferedReader(new StringReader(text));

 String line = reader.readLine();
 Assert.assertEquals("Line 1", line);
 line = reader.readLine();
 Assert.assertEquals("Line 2", line);
 line = reader.readLine();
 Assert.assertEquals("Line 3", line);
 line = reader.readLine();
 Assert.assertEquals("Line 4", line);
 Assert.assertEquals(ExtendedBufferedReader.END_OF_STREAM, reader.readLine());
 }

 /**
 * Test readLine method with long lines
 */
 @Test
 public void testReadLineWithLongLines() throws Exception {
 String text = "Line 1" + new String(new char[1024 * 1024]).replace("\0", "x") + "\nLine 2";
 reader = new ExtendedBufferedReader(new StringReader(text));

 String line = reader.readLine();
 Assert.assertEquals("Line 1" + new String(new char[1024 * 1024]).replace("\0", "x") + "\n", line);
 line = reader.readLine();
 Assert.assertEquals("Line 2", line);
 Assert.assertEquals(ExtendedBufferedReader.END_OF_STREAM, reader.readLine());
 }

 /**
 * Test lookAhead method with end of stream
 */
 @Test(expected = IOException.class)
 public void testLookAheadWithEndOfStream() throws Exception {
 reader.mark(0);
 reader.readLine();
 reader.reset();

 reader.lookAhead();
 }

 /**
 * Test lookAhead method with empty line
 */
 @Test
 public void testLookAheadWithEmptyLine() throws Exception {
 reader.mark(0);
 reader.readLine();
 reader.reset();

 Assert.assertEquals('\n', reader.lookAhead());
 }

 /**
 * Test lookAhead method with next character is CR
 */
 @Test
 public void testLookAheadWithNextCharacterIsCR() throws Exception {
 reader.mark(0);
 reader.read();
 reader.reset();

 Assert.assertEquals('\r', reader.lookAhead());
 }

 /**
 * Test lookAhead method with next character is LF
 */
 @Test
 public void testLookAheadWithNextCharacterIsLF() throws Exception {
 reader.mark(0);
 reader.read();
 reader.reset();

 Assert.assertEquals('\n', reader.lookAhead());
 }

 /**
 * Test lookAhead method with next character is whitespace
 */
 @Test
 public void testLookAheadWithNextCharacterIsWhitespace() throws Exception {
 reader.mark(0);
 reader.read();
 reader.reset();

 Assert.assertFalse(Character.isDigit(reader.lookAhead()));
 Assert.assertFalse(Character.isAlphabetic(reader.lookAhead()));
 Assert.assertTrue(Character.isWhitespace(reader.lookAhead()));
 }

 /**
 * Test lookAhead method with next character is digit
 */
 @Test
 public void testLookAheadWithNextCharacterIsDigit() throws Exception {
 reader.mark(0);
 reader.read();
 reader.mark(0);
 reader.read();
 reader.reset();

 Assert.assertTrue(Character.isDigit(reader.lookAhead()));
 }

 /**
 * Test lookAhead method with next character is alphabetic
 */
 @Test
 public void testLookAheadWithNextCharacterIsAlphabetic() throws Exception {
 reader.mark(0);
 reader.read();
 reader.mark(0);
 reader.read();
 reader.reset();

 Assert.assertTrue(Character.isAlphabetic(reader.lookAhead()));
 }

 /**
 * Test lookAhead method with next character is other character
 */
 @Test
 public void testLookAheadWithNextCharacterIsOtherCharacter() throws Exception {
 reader.mark(0);
 reader.read();
 reader.mark(0);
 reader.read();
 reader.reset();

 Assert.assertFalse(Character.isDigit(reader.lookAhead()));
 Assert.assertFalse(Character.isAlphabetic(reader.lookAhead()));
 Assert.assertFalse(Character.isWhitespace(reader.lookAhead()));
 }
 }
package org.apache.commons.csv.test;

import org.apache.commons.csv.ExtendedBufferedReader;
import org.apache.commons.io.CharArrayReader;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class ExtendedBufferedReaderTest {

 private ExtendedBufferedReader reader;
 private char[] buffer = new char[1024];

 @Before
 public void setup() {
 CharArrayReader input = new CharArrayReader(new String("Line 1\nLine 2\nLine 3\n").toCharArray());
 reader = new ExtendedBufferedReader(input);
 }

 @Test
 public void testReadCharacter() throws Exception {
 int c;
 for (int i = 0; i < 3; i++) {
 c = reader.read();
 Assert.assertEquals((i == 2 ? '\n' : Character.toString(i + 65).charAt(0)), Character.toString(c));
 }
 Assert.assertEquals(ExtendedBufferedReader.END_OF_STREAM, reader.read());
 }

 @Test
 public void testReadLine() throws Exception {
 String line;
 for (int i = 0; i < 3; i++) {
 line = reader.readLine();
 Assert.assertEquals("Line " + (i + 1), line);
 }
 Assert.assertNull(reader.readLine());
 }

 @Test
 public void testLookAhead() throws Exception {
 int c = reader.lookAhead();
 Assert.assertEquals('L', c);
 reader.read(); // consume 'L'
 Assert.assertEquals('i', reader.lookAhead());
 }

 @Test(expected = IOException.class)
 public void testLookAheadAtEnd() throws Exception {
 reader.readLine(); // consume all lines
 reader.lookAhead(); // should throw an exception
 }

 @Test
 public void testGetLineNumber() throws Exception {
 int lineNumber = reader.getLineNumber();
 for (int i = 0; i < 3; i++) {
 reader.readLine();
 lineNumber++;
 }
 Assert.assertEquals(4, lineNumber);
 }
}
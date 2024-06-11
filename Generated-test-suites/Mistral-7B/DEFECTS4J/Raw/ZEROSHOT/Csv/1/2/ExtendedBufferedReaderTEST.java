package org.apache.commons.csv.test;

import org.apache.commons.csv.ExtendedBufferedReader;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class ExtendedBufferedReaderTest {

 @Test
 public void testExtendedBufferedReader() {
 String input = "line1\nline2\nline3\nline4\n";
 ExtendedBufferedReader reader = new ExtendedBufferedReader(new StringReader(input));

 assertEquals(0, reader.getLineNumber());
 assertNull(reader.readLine());
 assertFalse(reader.hasNextLine());
 }

 @Test
 public void testRead() throws Exception {
 String input = "line1\nline2\nline3\nline4\n";
 ExtendedBufferedReader reader = new ExtendedBufferedReader(new StringReader(input));

 assertEquals('l', reader.read());
 assertEquals('i', reader.read());
 assertEquals('n', reader.read());
 assertEquals('e', reader.read());
 assertEquals('1', reader.read());
 assertEquals('\n', reader.read());
 assertEquals('\r', reader.read()); // check for \r on windows
 assertEquals(' ', reader.read()); // check for space after \r on windows
 assertEquals('l', reader.read());
 // ... repeat for all characters in input string
 assertEquals(ExtendedBufferedReader.END_OF_STREAM, reader.read());
 }

 @Test
 public void testReadAgain() throws Exception {
 String input = "line1\nline2\nline3\nline4\n";
 ExtendedBufferedReader reader = new ExtendedBufferedReader(new StringReader(input));

 assertEquals('l', reader.read());
 assertEquals('i', reader.read());
 assertEquals('n', reader.read());
 assertEquals('e', reader.read());
 assertEquals('1', reader.read());
 assertEquals('\n', reader.read());
 assertEquals('l', reader.readAgain());
 assertEquals('i', reader.readAgain());
 assertEquals('n', reader.readAgain());
 // ... repeat for all characters in input string
 assertEquals(ExtendedBufferedReader.UNDEFINED, reader.readAgain());
 }

 @Test
 public void testReadLine() throws Exception {
 String input = "line1\nline2\nline3\nline4\n";
 ExtendedBufferedReader reader = new ExtendedBufferedReader(new StringReader(input));

 assertEquals("line1", reader.readLine());
 assertEquals("line2", reader.readLine());
 assertEquals("line3", reader.readLine());
 assertEquals("line4", reader.readLine());
 assertEquals(ExtendedBufferedReader.END_OF_STREAM, reader.read());
 }

 @Test
 public void testLookAhead() throws Exception {
 String input = "line1\nline2\nline3\nline4\n";
 ExtendedBufferedReader reader = new ExtendedBufferedReader(new StringReader(input));

 assertEquals('l', reader.lookAhead());
 assertEquals('i', reader.lookAhead());
 // ... repeat for all characters in input string
 assertEquals(ExtendedBufferedReader.END_OF_STREAM, reader.lookAhead());
 }

 @Test
 public void testGetLineNumber() throws Exception {
 String input = "line1\nline2\nline3\nline4\n";
 ExtendedBufferedReader reader = new ExtendedBufferedReader(new StringReader(input));

 for (int i = 0; i < 4; i++) {
 String line = reader.readLine();
 assertEquals(i+1, reader.getLineNumber());
 }
 assertEquals(ExtendedBufferedReader.END_OF_STREAM, reader.read());
 }

}
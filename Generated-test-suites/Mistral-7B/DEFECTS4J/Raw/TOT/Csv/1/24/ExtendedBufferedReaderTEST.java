package org.apache.commons.csv.test;

import org.apache.commons.csv.ExtendedBufferedReader;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Test class for ExtendedBufferedReader.
 */
public class ExtendedBufferedReaderTest {

 private ExtendedBufferedReader reader;

 @Before
 public void setUp() throws Exception {
 String input = "line1\r\nline2\r\nline3\r\nline4\r\nline5\r\n"; // with CR as line terminator
 reader = new ExtendedBufferedReader(new StringReader(input));
 }

 /**
 * Test for {@link ExtendedBufferedReader#read()}.
 */
 @Test
 public void testRead() throws Exception {
 assertEquals(-1, reader.read()); // read EOF

 // read line 1
 assertEquals('l', reader.read());
 assertEquals('i', reader.read());
 assertEquals('n', reader.read());
 assertEquals('e', reader.read());
 assertEquals('1', reader.read()); // read CR
 assertEquals('\r', reader.read()); // read LF
 assertEquals('\n', reader.read()); // read LF, next line

 // read line 2
 assertEquals('l', reader.read());
 assertEquals('i', reader.read());
 assertEquals('n', reader.read());
 assertEquals('e', reader.read());
 assertEquals('2', reader.read()); // read CR
 assertEquals('\r', reader.read()); // read LF
 assertEquals('\n', reader.read()); // read LF, next line

 // read line 3
 // ...

 // read line 4
 // ...

 // read line 5
 // ...

 assertEquals(-1, reader.read()); // read EOF
 }

 /**
 * Test for {@link ExtendedBufferedReader#readAgain()}.
 */
 @Test
 public void testReadAgain() throws Exception {
 reader = new ExtendedBufferedReader(new StringReader("abcd"));

 assertEquals('a', reader.read());
 assertEquals('a', reader.readAgain());

 assertEquals('b', reader.read());
 assertEquals('b', reader.readAgain());

 assertEquals('c', reader.read());
 assertEquals('c', reader.readAgain());

 assertEquals('d', reader.read());
 assertEquals('d', reader.readAgain());

 assertEquals(-1, reader.read());
 assertEquals(-1, reader.readAgain());
 }

 /**
 * Test for {@link ExtendedBufferedReader#read(char[], int, int)}.
 */
 @Test
 public void testRead() throws Exception {
 char[] buffer = new char[5];

 assertEquals(0, reader.read(buffer, 0, buffer.length));
 assertEquals(0, reader.read(buffer, 0, buffer.length));

 reader.readLine(); // consume line1

 assertEquals(5, reader.read(buffer, 0, buffer.length));
 assertEquals("line2\r\n", new String(buffer));

 assertEquals(-1, reader.read());
 }

 /**
 * Test for {@link ExtendedBufferedReader#readLine()}.
 */
 @Test
 public void testReadLine() throws Exception {
 assertEquals("line1\r\n", reader.readLine());
 assertEquals("line2\r\n", reader.readLine());
 assertEquals("line3\r\n", reader.readLine());
 assertEquals("line4\r\n", reader.readLine());
 assertEquals("line5\r\n", reader.readLine());
 assertEquals(null, reader.readLine());
 }

 /**
 * Test for {@link ExtendedBufferedReader#lookAhead()}.
 */
 @Test
 public void testLookAhead() throws Exception {
 reader = new ExtendedBufferedReader(new StringReader("abcd"));

 assertEquals('a', reader.lookAhead());
 reader.read(); // consume 'a'
 assertEquals('b', reader.lookAhead());
 reader.read(); // consume 'b'
 assertEquals('c', reader.lookAhead());
 reader.read(); // consume 'c'
 assertEquals('d', reader.lookAhead());
 reader.read(); // consume 'd'
 assertEquals(-1, reader.lookAhead());
 }

 /**
 * Test for {@link ExtendedBufferedReader#getLineNumber()}.
 */
 @Test
 public void testGetLineNumber() throws Exception {
 assertEquals(0, reader.getLineNumber());
 reader.readLine();
 assertEquals(1, reader.getLineNumber());
 reader.readLine();
 assertEquals(2, reader.getLineNumber());
 reader.readLine();
 assertEquals(3, reader.getLineNumber());
 reader.readLine();
 assertEquals(4, reader.getLineNumber());
 reader.readLine();
 assertEquals(-1, reader.getLineNumber());
 }
}
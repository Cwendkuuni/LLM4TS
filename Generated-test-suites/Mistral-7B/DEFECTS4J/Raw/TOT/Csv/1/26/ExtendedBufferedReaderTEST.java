package org.apache.commons.csv.test;

import org.apache.commons.csv.ExtendedBufferedReader;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class ExtendedBufferedReaderTest {

 private ExtendedBufferedReader reader;
 private String line;

 @Before
 public void setUp() throws Exception {
 String testData = "Line1\rLine2\nLine3\rLine4\nLine5\r\n";
 reader = new ExtendedBufferedReader(new StringReader(testData));
 }

 @After
 public void tearDown() throws Exception {
 reader.close();
 }

 @Test
 public void testRead() throws IOException {
 // read method test
 int b;
 char[] buffer = new char[1];
 while ((b = reader.read(buffer, 0, 1)) != -1) {
 System.out.print((char) b);
 }
 // Test for EOF
 assertEquals(-1, reader.read());
 }

 @Test
 public void testLookAhead() throws IOException {
 // test lookAhead method
 assertEquals('L', reader.lookAhead());
 reader.read(); // consume the character
 assertEquals('i', reader.lookAhead());
 }

 @Test
 public void testReadLine() throws IOException {
 // test readLine method
 String expected = "Line1\rLine2\n";
 String actual = reader.readLine();
 assertEquals(expected, actual);
 // test readLine when EOF is reached
 String actual2 = reader.readLine();
 assertNull(actual2);
 }

 @Test(expected = IOException.class)
 public void testExceptionThrownWhenMarkNotSupported() throws IOException {
 reader.mark(1); // mark position
 reader.read(); // read a character
 reader.readLine(); // read a line
 reader.reset(); // try to reset the reader
 }

 @Test
 public void testReadAgain() throws IOException {
 // test readAgain method
 int c = reader.read();
 int d = reader.readAgain();
 assertEquals(c, d);
 }

 @Test
 public void testGetLineNumber() throws IOException {
 // test getLineNumber method
 assertEquals(5, reader.getLineNumber());
 reader.readLine();
 reader.readLine();
 reader.readLine();
 reader.readLine();
 reader.readLine();
 // test getLineNumber when EOF is reached
 assertEquals(5, reader.getLineNumber());
 reader.read();
 assertEquals(6, reader.getLineNumber());
 }
}
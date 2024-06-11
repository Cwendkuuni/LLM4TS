package org.apache.commons.csv.test;

import org.apache.commons.csv.ExtendedBufferedReader;
import org.junit.Test;
import static org.junit.Assert.*;

import java.io.StringReader;

/**
 * ExtendedBufferedReader Test
 */
public class ExtendedBufferedReaderTest {

 @Test
 public void testExtendedBufferedReader() throws Exception {
 ExtendedBufferedReader reader = new ExtendedBufferedReader(new StringReader("Line1\nLine2\nLine3\nLine4\nLine5\n"));

 // Test read method
 assertEquals('L', reader.read());
 assertEquals('i', reader.read());
 assertEquals('n', reader.read());
 assertEquals('e', reader.read());
 assertEquals(' ', reader.read());
 assertEquals('1', reader.read());
 assertEquals('\n', reader.read()); // line counter should be incremented
 assertEquals('L', reader.readAgain()); // check last char

 // Test readLine method
 String line1 = reader.readLine();
 assertEquals("Line1", line1);
 assertEquals('n', reader.readAgain()); // check last char

 String line2 = reader.readLine();
 assertEquals("Line2", line2);
 assertEquals('\n', reader.readAgain()); // check last char

 // Test peek method
 reader.mark(1);
 assertEquals('i', reader.read());
 assertEquals('n', reader.read());
 assertEquals(' ', reader.read());
 assertEquals('2', reader.read());
 reader.reset();
 assertEquals('2', reader.lookAhead()); // check lookahead char

 // Test getLineNumber method
 assertEquals(5, reader.getLineNumber());
 }

 @Test(expected = IllegalStateException.class)
 public void testNegativeLookahead() throws Exception {
 ExtendedBufferedReader reader = new ExtendedBufferedReader(new StringReader("Line1\nLine2\nLine3\nLine4\nLine5\n"));
 reader.mark(1);
 reader.read();
 reader.read();
 reader.read();
 reader.read();
 reader.read();
 reader.read();
 reader.readLine();
 reader.readLine();
 reader.readLine();
 reader.readLine();
 reader.readLine();
 reader.lookAhead(); // should throw IllegalStateException
 }
}

package org.apache.commons.csv;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Before;
import org.junit.Test;

import java.io.StringReader;

public class ExtendedBufferedReaderTest {

    private ExtendedBufferedReader reader;

    @Before
    public void setUp() {
        StringReader stringReader = new StringReader("This is a test string.\nWith multiple lines.\nAnd \r\n line terminators.");
        reader = new ExtendedBufferedReader(stringReader);
    }
    
    @Test
    public void testRead() {
        try {
            assertEquals('T', reader.read());
            assertEquals('h', reader.read());
            assertEquals('i', reader.read());
            assertEquals('s', reader.read());
            assertEquals(' ', reader.read());
            assertEquals('i', reader.read());
            assertEquals('s', reader.read());
            assertEquals(' ', reader.read());
            assertEquals('a', reader.read());
            assertEquals(' ', reader.read());
            assertEquals('t', reader.read());
            assertEquals('e', reader.read());
            assertEquals('s', reader.read());
            assertEquals('t', reader.read());
            assertEquals(' ', reader.read());
            assertEquals('s', reader.read());
            assertEquals('t', reader.read());
            assertEquals('r', reader.read());
            assertEquals('i', reader.read());
            assertEquals('n', reader.read());
            assertEquals('g', reader.read());
            assertEquals('.', reader.read());
            assertEquals('\n', reader.read());
            assertEquals('W', reader.read());
            assertEquals('i', reader.read());
            assertEquals('t', reader.read());
            assertEquals('h', reader.read());
            assertEquals(' ', reader.read());
            assertEquals('m', reader.read());
            assertEquals('u', reader.read());
            assertEquals('l', reader.read());
            assertEquals('t', reader.read());
            assertEquals('i', reader.read());
            assertEquals('p', reader.read());
            assertEquals('l', reader.read());
            assertEquals('e', reader.read());
            assertEquals(' ', reader.read());
            assertEquals('l', reader.read());
            assertEquals('i', reader.read());
            assertEquals('n', reader.read());
            assertEquals('e', reader.read());
            assertEquals('s', reader.read());
            assertEquals('.', reader.read());
            assertEquals('\n', reader.read());
            assertEquals('A', reader.read());
            assertEquals('n', reader.read());
            assertEquals('d', reader.read());
            assertEquals(' ', reader.read());
            assertEquals('\r', reader.read());
            assertEquals('\n', reader.read());
            assertEquals(' ', reader.read());
            assertEquals('l', reader.read());
            assertEquals('i', reader.read());
            assertEquals('n', reader.read());
            assertEquals('e', reader.read());
            assertEquals(' ', reader.read());
            assertEquals('t', reader.read());
            assertEquals('e', reader.read());
            assertEquals('r', reader.read());
            assertEquals('m', reader.read());
            assertEquals('i', reader.read());
            assertEquals('n', reader.read());
            assertEquals('a', reader.read());
            assertEquals('t', reader.read());
            assertEquals('o', reader.read());
            assertEquals('r', reader.read());
            assertEquals('s', reader.read());
            assertEquals('.', reader.read());
            assertEquals(END_OF_STREAM, reader.read());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testReadAgain() {
        try {
            assertEquals('T', reader.read());
            assertEquals('h', reader.read());
            assertEquals('i', reader.read());
            assertEquals('s', reader.read());
            assertEquals(' ', reader.read());
            assertEquals('i', reader.read());
            assertEquals('s', reader.read());
            assertEquals(' ', reader.read());
            
            int lastChar = reader.readAgain();
            assertEquals('s', lastChar);
            
            assertEquals(' ', reader.read());
            assertEquals('a', reader.read());
            assertEquals(' ', reader.read());
            assertEquals('t', reader.read());
            assertEquals('e', reader.read());
            assertEquals('s', reader.read());
            assertEquals('t', reader.read());
            assertEquals(' ', reader.read());
            assertEquals('s', reader.read());
            assertEquals('t', reader.read());
            assertEquals('r', reader.read());
            assertEquals('i', reader.read());
            assertEquals('n', reader.read());
            assertEquals('g', reader.read());
            assertEquals('.', reader.read());
            assertEquals('\n', reader.read());
            
            lastChar = reader.readAgain();
            assertEquals('\n', lastChar);
            
            assertEquals('W', reader.read());
            assertEquals('i', reader.read());
            assertEquals('t', reader.read());
            assertEquals('h', reader.read());
            assertEquals(' ', reader.read());
            assertEquals('m', reader.read());
            assertEquals('u', reader.read());
            assertEquals('l', reader.read());
            assertEquals('t', reader.read());
            assertEquals('i', reader.read());
            assertEquals('p', reader.read());
            assertEquals('l', reader.read());
            assertEquals('e', reader.read());
            
            lastChar = reader.readAgain();
            assertEquals('e', lastChar);
            
            assertEquals(' ', reader.read());
            assertEquals('l', reader.read());
            assertEquals('i', reader.read());
            assertEquals('n', reader.read());
            assertEquals('e', reader.read());
            assertEquals('s', reader.read());
            assertEquals('.', reader.read());
            assertEquals('\n', reader.read());
            
            lastChar = reader.readAgain();
            assertEquals('\n', lastChar);
            
            assertEquals('A', reader.read());
            assertEquals('n', reader.read());
            assertEquals('d', reader.read());
            assertEquals(' ', reader.read());
            assertEquals('\r', reader.read());
            assertEquals('\n', reader.read());
            assertEquals(' ', reader.read());
            assertEquals('l', reader.read());
            assertEquals('i', reader.read());
            assertEquals('n', reader.read());
            assertEquals('e', reader.read());
            assertEquals(' ', reader.read());
            assertEquals('t', reader.read());
            assertEquals('e', reader.read());
            assertEquals('r', reader.read());
            assertEquals('m', reader.read());
            assertEquals('i', reader.read());
            assertEquals('n', reader.read());
            assertEquals('a', reader.read());
            assertEquals('t', reader.read());
            assertEquals('o', reader.read());
            assertEquals('r', reader.read());
            assertEquals('s', reader.read());
            assertEquals('.', reader.read());
            assertEquals(END_OF_STREAM, reader.read());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testReadArray() {
        try {
            char[] buf = new char[10];
            int len = reader.read(buf, 0, 10);
            
            assertEquals(10, len);
            assertEquals('T', buf[0]);
            assertEquals('h', buf[1]);
            assertEquals('i', buf[2]);
            assertEquals('s', buf[3]);
            assertEquals(' ', buf[4]);
            assertEquals('i', buf[5]);
            assertEquals('s', buf[6]);
            assertEquals(' ', buf[7]);
            assertEquals('a', buf[8]);
            assertEquals(' ', buf[9]);

            len = reader.read(buf, 0, 10);
            
            assertEquals(10, len);
            assertEquals('t', buf[0]);
            assertEquals('e', buf[1]);
            assertEquals('s', buf[2]);
            assertEquals('t', buf[3]);
            assertEquals(' ', buf[4]);
            assertEquals('s', buf[5]);
            assertEquals('t', buf[6]);
            assertEquals('r', buf[7]);
            assertEquals('i', buf[8]);
            assertEquals('n', buf[9]);

            len = reader.read(buf, 0, 10);
            
            assertEquals(10, len);
            assertEquals('g', buf[0]);
            assertEquals('.', buf[1]);
            assertEquals('\n', buf[2]);
            assertEquals('W', buf[3]);
            assertEquals('i', buf[4]);
            assertEquals('t', buf[5]);
            assertEquals('h', buf[6]);
            assertEquals(' ', buf[7]);
            assertEquals('m', buf[8]);
            assertEquals('u', buf[9]);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testReadLine() {
        try {
            assertEquals("This is a test string.", reader.readLine());
            assertEquals("With multiple lines.", reader.readLine());
            assertEquals("And ", reader.readLine());
            assertEquals(" line terminators.", reader.readLine());
            assertNull(reader.readLine());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testLookAhead() {
        try {
            assertEquals('T', reader.lookAhead());
            reader.read();
            assertEquals('h', reader.lookAhead());
            reader.read();
            assertEquals('i', reader.lookAhead());
            reader.read();
            assertEquals('s', reader.lookAhead());
            reader.read();
            assertEquals(' ', reader.lookAhead());
            reader.read();
            assertEquals('i', reader.lookAhead());
            reader.read();
            assertEquals('s', reader.lookAhead());
            reader.read();
            
            int nextChar = reader.lookAhead();
            assertEquals(' ', nextChar);
            
            reader.read();
            assertEquals('a', reader.lookAhead());
            reader.read();
            assertEquals(' ', reader.lookAhead());
            reader.read();
            assertEquals('t', reader.lookAhead());
            reader.read();
            assertEquals('e', reader.lookAhead());
            reader.read();
            
            nextChar = reader.lookAhead();
            assertEquals('s', nextChar);
            
            reader.read();
            assertEquals('t', reader.lookAhead());
            reader.read();
            assertEquals(' ', reader.lookAhead());
            reader.read();
            assertEquals('s', reader.lookAhead());
            reader.read();
            assertEquals('t', reader.lookAhead());
            reader.read();
            assertEquals('r', reader.lookAhead());
            reader.read();
            
            nextChar = reader.lookAhead();
            assertEquals('i', nextChar);
            
            reader.read();
            assertEquals('n', reader.lookAhead());
            reader.read();
            assertEquals('g', reader.lookAhead());
            reader.read();
            assertEquals('.', reader.lookAhead());
            reader.read();
            assertEquals('\n', reader.lookAhead());
            
            nextChar = reader.lookAhead();
            assertEquals('W', nextChar);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testGetLineNumber() {
        try {
            assertEquals(0, reader.getLineNumber());
            
            reader.readLine();
            assertEquals(1, reader.getLineNumber());
            
            reader.readLine();
            assertEquals(2, reader.getLineNumber());
            
            reader.readLine();
            assertEquals(3, reader.getLineNumber());
            
            reader.readLine();
            assertEquals(4, reader.getLineNumber());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

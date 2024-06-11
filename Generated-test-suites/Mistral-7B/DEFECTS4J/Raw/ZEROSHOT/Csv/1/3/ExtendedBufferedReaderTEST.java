package org.apache.commons.csv.test;

import org.apache.commons.csv.ExtendedBufferedReader;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

/**
 * ExtendedBufferedReaderTest
 */
public class ExtendedBufferedReaderTest {

    private ByteArrayInputStream inputStream;
    private ExtendedBufferedReader reader;

    @Before
    public void setUp() {
        inputStream = new ByteArrayInputStream("line1\rline2\rline3".getBytes());
        reader = new ExtendedBufferedReader(new InputStreamReader(inputStream));
    }

    @After
    public void tearDown() throws IOException {
        inputStream.close();
        reader.close();
    }

    @Test
    public void testRead() throws IOException {
        int c;

        // read line1
        c = reader.read();
        assertEquals('l', (char)c);
        c = reader.read();
        assertEquals('i', (char)c);
        // read \r
        assertFalse(reader.hasNext());
        assertEquals(-2, reader.readAgain()); // UNDEFINED
        assertEquals(-1, reader.readAgain()); // END_OF_STREAM

        // read line2
        reader = new ExtendedBufferedReader(new InputStreamReader(inputStream));
        c = reader.read();
        assertEquals('\r', (char)c); // skip CR
        c = reader.read();
        assertEquals('l', (char)c);
        c = reader.read();
        assertEquals('e', (char)c);
        assertEquals('\r', reader.readAgain()); // CR
        assertFalse(reader.hasNext());
        assertEquals(-1, reader.readAgain()); // END_OF_STREAM

        // read line3
        reader = new ExtendedBufferedReader(new InputStreamReader(inputStream));
        c = reader.read();
        assertEquals('l', (char)c);
        c = reader.read();
        assertEquals('i', (char)c);
        c = reader.read();
        assertEquals('n', (char)c);
        c = reader.read();
        assertEquals('e', (char)c);
        c = reader.read();
        assertEquals('3', (char)c);
        assertEquals('\r', reader.readAgain()); // CR
        assertFalse(reader.hasNext());
        assertEquals(-1, reader.readAgain()); // END_OF_STREAM
    }

    @Test
    public void testReadLine() throws IOException {
        String line;

        // read line1
        line = reader.readLine();
        assertEquals("line1\r", line);
        assertEquals('1', reader.readAgain()); // read 1 from \r1
        assertFalse(reader.hasNext());
        assertEquals(-1, reader.readAgain()); // END_OF_STREAM

        // read line2
        reader = new ExtendedBufferedReader(new InputStreamReader(inputStream));
        line = reader.readLine();
        assertEquals("line2\r", line);
        assertEquals('2', reader.readAgain()); // read 2 from \r2
        assertFalse(reader.hasNext());
        assertEquals(-1, reader.readAgain()); // END_OF_STREAM

        // read line3
        reader = new ExtendedBufferedReader(new InputStreamReader(inputStream));
        line = reader.readLine();
        assertEquals("line3", line);
        assertTrue(reader.hasNext());
        assertEquals('3', reader.readAgain()); // read 3 from line3
    }

    @Test
    public void testLookAhead() throws IOException {
        int c;

        reader = new ExtendedBufferedReader(new InputStreamReader(inputStream));

        // lookahead at line1
        c = reader.lookAhead();
        assertEquals('l', (char)c);
        c = reader.lookAhead();
        assertEquals('i', (char)c);
        c = reader.lookAhead();
        assertEquals('r', (char)c);
        assertEquals('\r', reader.readAgain()); // read CR
        assertFalse(reader.hasNext());

        // lookahead at line2
        reader = new ExtendedBufferedReader(new InputStreamReader(inputStream));
        c = reader.lookAhead();
        assertEquals('\r', (char)c); // skip CR
        c = reader.lookAhead();
        assertEquals('l', (char)c);
        assertEquals('e', reader.readAgain()); // read e from line2
        assertFalse(reader.hasNext());
    }

    @Test
    public void testLineNumber() throws IOException {
        int lineNumber;

        reader = new ExtendedBufferedReader(new InputStreamReader(inputStream));

        // read line1
        lineNumber = reader.getLineNumber();
        assertEquals(1, lineNumber);
        reader.readLine();

        // read line2
        reader.reset();
        lineNumber = reader.getLineNumber();
        assertEquals(2, lineNumber);
        reader.readLine();

        // read line3
        reader.reset();
        lineNumber = reader.getLineNumber();
        assertEquals(3, lineNumber);
        reader.readLine();

        assertNull(reader.readLine());
    }
}
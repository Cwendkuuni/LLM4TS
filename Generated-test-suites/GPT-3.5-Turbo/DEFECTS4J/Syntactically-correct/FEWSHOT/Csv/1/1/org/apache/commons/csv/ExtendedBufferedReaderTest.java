package org.apache.commons.csv;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

public class ExtendedBufferedReaderTest {
    private ExtendedBufferedReader reader;

    @Before
    public void setup() {
        reader = new ExtendedBufferedReader(null);
    }

    @Test
    public void testRead() throws Exception {
        assertEquals('a', reader.read());
        assertEquals('b', reader.read());
        assertEquals('c', reader.read());
    }

    @Test
    public void testReadAgain() throws Exception {
        assertEquals(ExtendedBufferedReader.UNDEFINED, reader.readAgain());

        reader.read();
        assertEquals('a', reader.readAgain());

        reader.read();
        assertEquals('b', reader.readAgain());
    }

    @Test
    public void testRead_lineCounterIncremented() throws Exception {
        reader.readLine();
        assertEquals(1, reader.getLineNumber());

        reader.readLine();
        assertEquals(2, reader.getLineNumber());
    }

    @Test
    public void testRead_lineCounterNotIncremented() throws Exception {
        reader.read();
        assertEquals(0, reader.getLineNumber());

        reader.read();
        assertEquals(0, reader.getLineNumber());
    }

    @Test
    public void testRead_charArray_lineCounterIncremented() throws Exception {
        char[] buf = new char[5];
        reader.read(buf, 0, 3);
        assertEquals(0, reader.getLineNumber());

        reader.read(buf, 0, 5);
        assertEquals(1, reader.getLineNumber());
    }

    @Test
    public void testRead_charArray_lineCounterNotIncremented() throws Exception {
        char[] buf = new char[5];
        reader.read(buf, 0, 2);
        assertEquals(0, reader.getLineNumber());

        reader.read(buf, 0, 0);
        assertEquals(0, reader.getLineNumber());
    }

    @Test
    public void testReadLine() throws Exception {
        assertEquals("line1", reader.readLine());
        assertEquals("line2", reader.readLine());
        assertEquals("line3", reader.readLine());
    }

    @Test
    public void testLookAhead() throws Exception {
        assertEquals('a', reader.lookAhead());
        assertEquals('b', reader.lookAhead());
        assertEquals('c', reader.lookAhead());
    }

    @Test
    public void testGetLineNumber() throws Exception {
        assertEquals(0, reader.getLineNumber());

        reader.readLine();
        assertEquals(1, reader.getLineNumber());

        reader.readLine();
        assertEquals(2, reader.getLineNumber());
    }
}
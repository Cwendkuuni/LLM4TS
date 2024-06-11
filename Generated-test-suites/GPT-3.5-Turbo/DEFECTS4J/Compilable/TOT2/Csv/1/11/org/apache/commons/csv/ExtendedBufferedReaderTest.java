
package org.apache.commons.csv;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import java.io.StringReader;
import java.io.IOException;

public class ExtendedBufferedReaderTest {

    ExtendedBufferedReader reader;

    @Before
    public void setUp() {
        reader = new ExtendedBufferedReader(new StringReader("Testing\nExtendedBufferedReader\r\n"));
    }

    @Test
    public void testRead() throws IOException {
        assertEquals('T', reader.read());
    }

    @Test
    public void testReadAgain() throws IOException {
        reader.read();
        assertEquals('T', reader.readAgain());
    }

    @Test
    public void testReadCharArray() throws IOException {
        char[] buf = new char[6];
        reader.read(buf, 0, 6);
        assertEquals(6, reader.getLineNumber());
    }

    @Test
    public void testReadLine() throws IOException {
        reader.readLine();
        assertEquals(1, reader.getLineNumber());
    }

    @Test
    public void testLookAhead() throws IOException {
        assertEquals('e', reader.lookAhead());
        assertEquals(0, reader.getLineNumber());
    }

}

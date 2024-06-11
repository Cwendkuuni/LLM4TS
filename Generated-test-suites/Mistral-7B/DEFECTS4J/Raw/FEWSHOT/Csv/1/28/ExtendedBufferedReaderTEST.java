package org.apache.commons.csv;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class ExtendedBufferedReaderTest {

    @Test
    public void testRead() throws Exception {
        StringBuilder sb = new StringBuilder();
        ExtendedBufferedReader reader = new ExtendedBufferedReader(new StringReader("abc\ndef\rghi\rjkl"));
        int c;

        c = reader.read();
        assertEquals('a', (char) c);
        assertEquals(1, reader.getLineNumber());

        c = reader.read();
        assertEquals('b', (char) c);
        assertEquals(1, reader.getLineNumber());

        c = reader.read();
        assertEquals('\n', (char) c);
        assertEquals(2, reader.getLineNumber());

        c = reader.read();
        assertEquals('d', (char) c);
        assertEquals(2, reader.getLineNumber());

        c = reader.read();
        assertEquals('\n', (char) c);
        assertEquals(3, reader.getLineNumber());

        c = reader.read();
        assertEquals('g', (char) c);
        assertEquals(3, reader.getLineNumber());

        c = reader.read();
        assertEquals('\r', (char) c);
        assertEquals(3, reader.getLineNumber());

        c = reader.read();
        assertEquals('h', (char) c);
        assertEquals(3, reader.getLineNumber());

        c = reader.read();
        assertEquals('\r', (char) c);
        assertEquals(3, reader.getLineNumber());

        c = reader.read();
        assertEquals('j', (char) c);
        assertEquals(3, reader.getLineNumber());

        c = reader.read();
        assertEquals('k', (char) c);
        assertEquals(3, reader.getLineNumber());

        c = reader.read();
        assertEquals('\n', (char) c);
        assertEquals(4, reader.getLineNumber());

        c = reader.read();
        assertNull(c);
        assertEquals(4, reader.getLineNumber());

        assertEquals(4, reader.getLineNumber());
        assertEquals(END_OF_STREAM, reader.readAgain());
    }

    @Test
    public void testLookAhead() throws Exception {
        StringBuilder sb = new StringBuilder();
        ExtendedBufferedReader reader = new ExtendedBufferedReader(new StringReader("abc\ndef\rghi\rjkl"));
        int c;

        c = reader.lookAhead();
        assertEquals(-1, reader.getLineNumber());
        assertEquals('a', (char) c);

        reader.read(); // read 'a'
        c = reader.lookAhead();
        assertEquals(1, reader.getLineNumber());
        assertEquals('b', (char) c);

        reader.read(); // read 'b'
        reader.read(); // read '\n'
        c = reader.lookAhead();
        assertEquals(2, reader.getLineNumber());
        assertEquals('d', (char) c);

        reader.readLine(); // read "def"
        c = reader.lookAhead();
        assertEquals(3, reader.getLineNumber());
        assertEquals('g', (char) c);

        reader.read(); // read 'g'
        reader.read(); // read '\r'
        reader.read(); // read 'h'
        reader.read(); // read '\r'
        c = reader.lookAhead();
        assertEquals(3, reader.getLineNumber());
        assertEquals('j', (char) c);

        reader.read(); // read 'j'
        reader.read(); // read 'k'
        c = reader.lookAhead();
        assertEquals(3, reader.getLineNumber());
        assertEquals('\n', (char) c);

        reader.readLine(); // read "jkl\n"
        c = reader.lookAhead();
        assertEquals(4, reader.getLineNumber());
        assertEquals(-1, c);

        assertEquals(4, reader.getLineNumber());
        assertEquals(END_OF_STREAM, reader.readAgain());
    }

    @Test
    public void testLineNumber() throws Exception {
        StringBuilder sb = new StringBuilder();
        ExtendedBufferedReader reader = new ExtendedBufferedReader(new StringReader("abc\ndef\rghi\rjkl"));

        assertEquals(0, reader.getLineNumber());

        reader.read(); // read 'a'
        assertEquals(1, reader.getLineNumber());

        reader.read(); // read 'b'
        reader.read(); // read '\n'
        assertEquals(2, reader.getLineNumber());

        reader.readLine(); // read "def"
        assertEquals(3, reader.getLineNumber());

        reader.read(); // read 'g'
        reader.read(); // read '\r'
        reader.read(); // read 'h'
        reader.read(); // read '\r'
        reader.readLine(); // read "jkl\n"
        assertEquals(4, reader.getLineNumber());

        assertEquals(4, reader.getLineNumber());
        assertEquals(END_OF_STREAM, reader.readAgain());
    }

    @Test(expected = IOException.class)
    public void testNegativeMarkPosition() throws Exception {
        ExtendedBufferedReader reader = new ExtendedBufferedReader(new StringReader("abc"));
        reader.mark(-1);
    }
}
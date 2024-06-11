package org.apache.commons.csv;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class ExtendedBufferedReaderTest {

    @Test
    public void testRead() throws IOException {
        ExtendedBufferedReader reader = new ExtendedBufferedReader(System.in);
        int c = reader.read();
        assertNotEquals(-1, c);
        reader.close();
    }

    @Test
    public void testReadAgain() throws IOException {
        ExtendedBufferedReader reader = new ExtendedBufferedReader(System.in);
        int c1 = reader.read();
        int c2 = reader.readAgain();
        assertEquals(c1, c2);
        reader.close();
    }

    @Test
    public void testReadLine() throws IOException {
        ExtendedBufferedReader reader = new ExtendedBufferedReader(System.in);
        String line1 = reader.readLine();
        String line2 = reader.readLine();
        int lastChar1 = reader.readAgain();
        reader.close();

        assertNotNull(line1);
        assertNotNull(line2);
        assertNotNull(lastChar1);
        assertTrue(lineCounter > 0);
    }

    @Test
    public void testReadLineWithEmptyInput() throws IOException {
        ExtendedBufferedReader reader = new ExtendedBufferedReader(new StringReader(""));
        String line = reader.readLine();
        assertNull(line);
        reader.close();
    }

    @Test
    public void testReadLineWithOneCharacterInput() throws IOException {
        ExtendedBufferedReader reader = new ExtendedBufferedReader(new StringReader("A"));
        String line = reader.readLine();
        assertEquals("A", line);
        reader.close();
    }

    @Test
    public void testReadLineWithMultipleLinesInput() throws IOException {
        ExtendedBufferedReader reader = new ExtendedBufferedReader(new StringReader("A\nB\nC"));
        String line1 = reader.readLine();
        String line2 = reader.readLine();
        String line3 = reader.readLine();
        assertNotNull(line1);
        assertNotNull(line2);
        assertNotNull(line3);
        assertEquals("A", line1);
        assertEquals("B", line2);
        assertEquals("C", line3);
        reader.close();
    }

    @Test
    public void testLookAhead() throws IOException {
        ExtendedBufferedReader reader = new ExtendedBufferedReader(System.in);
        int lookAhead = reader.lookAhead();
        reader.close();

        assertNotEquals(-1, lookAhead);
        assertNotEquals(lastChar, lookAhead);
    }

    @Test(expected = IllegalStateException.class)
    public void testLookAheadWithEOF() throws IOException {
        ExtendedBufferedReader reader = new ExtendedBufferedReader(System.in);
        reader.readLine();
        reader.lookAhead();
    }

    @Test
    public void testGetLineNumber() throws IOException {
        ExtendedBufferedReader reader = new ExtendedBufferedReader(new StringReader("A\nB\nC"));
        int lineNumber = reader.getLineNumber();
        assertEquals(3, lineNumber);
        reader.close();
    }
}
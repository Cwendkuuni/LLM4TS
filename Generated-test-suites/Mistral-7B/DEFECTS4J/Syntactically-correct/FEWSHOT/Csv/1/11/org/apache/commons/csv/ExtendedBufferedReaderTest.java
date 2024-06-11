package org.apache.commons.csv;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class ExtendedBufferedReaderTest {

    @Test
    public void testRead() throws Exception {
        ExtendedBufferedReader reader = new ExtendedBufferedReader(new StringReader("abcdef\nghijklm\nopqrst\n"));
        int charRead;

        charRead = reader.read();
        assertEquals('a', (char) charRead);
        assertEquals(0, reader.getLineNumber());

        charRead = reader.read();
        assertEquals('b', (char) charRead);
        assertEquals(0, reader.getLineNumber());

        charRead = reader.read();
        assertEquals('c', (char) charRead);
        assertEquals(0, reader.getLineNumber());

        charRead = reader.read();
        assertEquals('\n', (char) charRead);
        assertEquals(1, reader.getLineNumber());

        charRead = reader.read();
        assertEquals('g', (char) charRead);
        assertEquals(1, reader.getLineNumber());

        charRead = reader.read();
        assertEquals('h', (char) charRead);
        assertEquals(1, reader.getLineNumber());

        charRead = reader.read();
        assertEquals('i', (char) charRead);
        assertEquals(1, reader.getLineNumber());

        charRead = reader.read();
        assertEquals('j', (char) charRead);
        assertEquals(1, reader.getLineNumber());

        charRead = reader.read();
        assertEquals('k', (char) charRead);
        assertEquals(1, reader.getLineNumber());

        charRead = reader.read();
        assertEquals('l', (char) charRead);
        assertEquals(1, reader.getLineNumber());

        charRead = reader.read();
        assertEquals('\n', (char) charRead);
        assertEquals(2, reader.getLineNumber());

        charRead = reader.read();
        assertEquals('o', (char) charRead);
        assertEquals(2, reader.getLineNumber());

        charRead = reader.read();
        assertEquals('p', (char) charRead);
        assertEquals(2, reader.getLineNumber());

        charRead = reader.read();
        assertEquals('q', (char) charRead);
        assertEquals(2, reader.getLineNumber());

        charRead = reader.read();
        assertEquals('r', (char) charRead);
        assertEquals(2, reader.getLineNumber());

        charRead = reader.read();
        assertEquals('s', (char) charRead);
        assertEquals(2, reader.getLineNumber());

        charRead = reader.read();
        assertEquals('t', (char) charRead);
        assertEquals(2, reader.getLineNumber());

        charRead = reader.read();
        assertEquals('\n', (char) charRead);
        assertEquals(3, reader.getLineNumber());

        charRead = reader.read();
        assertNull(charRead);
        assertEquals(3, reader.getLineNumber());
    }

    @Test
    public void testLookAhead() throws Exception {
        ExtendedBufferedReader reader = new ExtendedBufferedReader(new StringReader("abcdef\nghijklm\nopqrst\n"));
        int lookAhead;

        lookAhead = reader.lookAhead();
        assertEquals(-1, lookAhead);
        assertEquals(0, reader.getLineNumber());

        lookAhead = reader.lookAhead();
        assertEquals('a', lookAhead);
        assertEquals(0, reader.getLineNumber());

        lookAhead = reader.lookAhead();
        assertEquals('b', lookAhead);
        assertEquals(0, reader.getLineNumber());

        lookAhead = reader.lookAhead();
        assertEquals('c', lookAhead);
        assertEquals(0, reader.getLineNumber());

        lookAhead = reader.lookAhead();
        assertEquals('\n', lookAhead);
        assertEquals(1, reader.getLineNumber());

        lookAhead = reader.lookAhead();
        assertEquals('g', lookAhead);
        assertEquals(1, reader.getLineNumber());

        lookAhead = reader.lookAhead();
        assertEquals('h', lookAhead);
        assertEquals(1, reader.getLineNumber());

        lookAhead = reader.lookAhead();
        assertEquals('i', lookAhead);
        assertEquals(1, reader.getLineNumber());

        lookAhead = reader.lookAhead();
        assertEquals('j', lookAhead);
        assertEquals(1, reader.getLineNumber());

        lookAhead = reader.lookAhead();
        assertEquals('k', lookAhead);
        assertEquals(1, reader.getLineNumber());

        lookAhead = reader.lookAhead();
        assertEquals('l', lookAhead);
        assertEquals(1, reader.getLineNumber());

        lookAhead = reader.lookAhead();
        assertEquals('\n', lookAhead);
        assertEquals(2, reader.getLineNumber());

        lookAhead = reader.lookAhead();
        assertEquals('o', lookAhead);
        assertEquals(2, reader.getLineNumber());

        lookAhead = reader.lookAhead();
        assertEquals('p', lookAhead);
        assertEquals(2, reader.getLineNumber());

        lookAhead = reader.lookAhead();
        assertEquals('q', lookAhead);
        assertEquals(2, reader.getLineNumber());

        lookAhead = reader.lookAhead();
        assertEquals('r', lookAhead);
        assertEquals(2, reader.getLineNumber());

        lookAhead = reader.lookAhead();
        assertEquals('s', lookAhead);
        assertEquals(2, reader.getLineNumber());

        lookAhead = reader.lookAhead();
        assertEquals('t', lookAhead);
        assertEquals(2, reader.getLineNumber());

        lookAhead = reader.lookAhead();
        assertEquals('\n', lookAhead);
        assertEquals(3, reader.getLineNumber());

        lookAhead = reader.lookAhead();
        assertNull(lookAhead);
        assertEquals(3, reader.getLineNumber());

        lookAhead = reader.lookAhead();
        assertNull(lookAhead);
        assertEquals(3, reader.getLineNumber());
    }

    @Test
    public void testGetLineNumber() throws Exception {
        ExtendedBufferedReader reader = new ExtendedBufferedReader(new StringReader("abcdef\nghijklm\nopqrst\n"));

        assertEquals(0, reader.getLineNumber());

        reader.read();
        assertEquals(1, reader.getLineNumber());

        reader.readLine();
        assertEquals(2, reader.getLineNumber());

        reader.readLine();
        assertEquals(3, reader.getLineNumber());

        reader.readLine();
        assertNull(reader.readLine());
        assertEquals(4, reader.getLineNumber());
    }

    @Test
    public void testLastChar() throws Exception {
        ExtendedBufferedReader reader = new ExtendedBufferedReader(new StringReader("abcdef\nghijklm\nopqrst\n"));

        reader.read();
        assertEquals('a', reader.readAgain());
        assertEquals(0, reader.getLineNumber());

        reader.read();
        assertEquals('b', reader.readAgain());
        assertEquals(0, reader.getLineNumber());

        reader.read();
        assertEquals('c', reader.readAgain());
        assertEquals(0, reader.getLineNumber());

        reader.read();
        assertEquals('\n', reader.readAgain());
        assertEquals(1, reader.getLineNumber());

        reader.read();
        assertEquals('g', reader.readAgain());
        assertEquals(1, reader.getLineNumber());

        reader.read();
        assertEquals('h', reader.readAgain());
        assertEquals(1, reader.getLineNumber());

        reader.read();
        assertEquals('i', reader.readAgain());
        assertEquals(1, reader.getLineNumber());

        reader.read();
        assertEquals('j', reader.readAgain());
        assertEquals(1, reader.getLineNumber());

        reader.read();
        assertEquals('k', reader.readAgain());
        assertEquals(1, reader.getLineNumber());

        reader.read();
        assertEquals('l', reader.readAgain());
        assertEquals(1, reader.getLineNumber());

        reader.read();
        assertEquals('\n', reader.readAgain());
        assertEquals(2, reader.getLineNumber());

        reader.read();
        assertEquals('o', reader.readAgain());
        assertEquals(2, reader.getLineNumber());

        reader.read();
        assertEquals('p', reader.readAgain());
        assertEquals(2, reader.getLineNumber());

        reader.read();
        assertEquals('q', reader.readAgain());
        assertEquals(2, reader.getLineNumber());

        reader.read();
        assertEquals('r', reader.readAgain());
        assertEquals(2, reader.getLineNumber());

        reader.read();
        assertEquals('s', reader.readAgain());
        assertEquals(2, reader.getLineNumber());

        reader.read();
        assertEquals('t', reader.readAgain());
        assertEquals(2, reader.getLineNumber());

        reader.read();
        assertEquals('\n', reader.readAgain());
        assertEquals(3, reader.getLineNumber());

        reader.read();
        assertNull(reader.readAgain());
        assertEquals(4, reader.getLineNumber());

        reader.read();
        assertNull(reader.readAgain());
        assertEquals(4, reader.getLineNumber());

        reader.read();
        assertNull(reader.readAgain());
        assertEquals(4, reader.getLineNumber());

        reader.read();
        assertNull(reader.readAgain());
        assertEquals(4, reader.getLineNumber());

        reader.read();
        assertNull(reader.readAgain());
        assertEquals(4, reader.getLineNumber());

        reader.read();
        assertNull(reader.readAgain());
        assertEquals(4, reader.getLineNumber());
    }

    @Test
    public void testReadLineWithTrailingAndLeadingWhitespace() throws Exception {
        ExtendedBufferedReader reader = new ExtendedBufferedReader(new StringReader("  abc  def\n"));

        String line = reader.readLine();
        assertEquals("abc", line);
        assertEquals(1, reader.getLineNumber());
        assertEquals(' ', reader.readAgain());

        line = reader.readLine();
        assertEquals("def", line);
        assertEquals(2, reader.getLineNumber());
        assertNull(reader.readAgain());
    }

    @Test
    public void testReadLineWithTrailingNewline() throws Exception {
        ExtendedBufferedReader reader = new ExtendedBufferedReader(new StringReader("line1\nline2\n"));

        String line = reader.readLine();
        assertEquals("line1", line);
        assertEquals(1, reader.getLineNumber());
        assertEquals('\n', reader.readAgain());

        line = reader.readLine();
        assertEquals("line2", line);
        assertEquals(2, reader.getLineNumber());
        assertEquals('\n', reader.readAgain());

        line = reader.readLine();
        assertNull(line);
        assertEquals(3, reader.getLineNumber());
        assertNull(reader.readAgain());
    }

    @Test
    public void testReadLineWithLeadingNewline() throws Exception {
        ExtendedBufferedReader reader = new ExtendedBufferedReader(new StringReader("\nline1\nline2\n"));

        String line = reader.readLine();
        assertEquals("line1", line);
        assertEquals(1, reader.getLineNumber());
        assertEquals('\n', reader.readAgain());

        line = reader.readLine();
        assertEquals("line2", line);
        assertEquals(2, reader.getLineNumber());
        assertEquals('\n', reader.readAgain());

        line = reader.readLine();
        assertNull(line);
        assertEquals(3, reader.getLineNumber());
        assertNull(reader.readAgain());
    }

    @Test
    public void testReadLineWithEmptyLines() throws Exception {
        ExtendedBufferedReader reader = new ExtendedBufferedReader(new StringReader("\n\nline1\nline2\n"));

        String line = reader.readLine();
        assertEquals("", line);
        assertEquals(1, reader.getLineNumber());
        assertEquals('\n', reader.readAgain());

        line = reader.readLine();
        assertEquals("", line);
        assertEquals(2, reader.getLineNumber());
        assertEquals('\n', reader.readAgain());

        line = reader.readLine();
        assertEquals("line1", line);
        assertEquals(3, reader.getLineNumber());
        assertEquals('\n', reader.readAgain());

        line = reader.readLine();
        assertEquals("line2", line);
        assertEquals(4, reader.getLineNumber());
        assertEquals('\n', reader.readAgain());

        line = reader.readLine();
        assertNull(line);
        assertEquals(5, reader.getLineNumber());
        assertNull(reader.readAgain());
    }

    @Test
    public void testReadWithOverflow() throws Exception {
        ExtendedBufferedReader reader = new ExtendedBufferedReader(new StringReader("a".repeat(1024 * 16 * 3 + 1)));

        char[] buffer = new char[1024 * 16];
        int length = reader.read(buffer, 0, buffer.length);

        assertTrue(length > 0);
        assertEquals(length, buffer.length);
        assertEquals(length, reader.getLineNumber() * (reader.getLineNumber() > 0 ? '\n'.length() : 0));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNegativeOffset() throws Exception {
        ExtendedBufferedReader reader = new ExtendedBufferedReader(new StringReader("abcdef\n"));
        reader.readLine(); // Read the first line.
        reader.read(); // Read the next character.
        reader.read(new char[]{'a'}, -1); // Should throw an exception.
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNegativeLength() throws Exception {
        ExtendedBufferedReader reader = new ExtendedBufferedReader(new StringReader("abcdef\n"));
        reader.readLine(); // Read the first line.
        reader.read(); // Read the next character.
        reader.read(new char[]{'\0'}, -1); // Should throw an exception.
    }
}
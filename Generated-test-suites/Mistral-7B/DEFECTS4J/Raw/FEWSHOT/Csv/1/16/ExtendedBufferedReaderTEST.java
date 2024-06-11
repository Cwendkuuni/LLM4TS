package org.apache.commons.csv;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class ExtendedBufferedReaderTest {

    @Test
    public void testRead() throws Exception {
        ExtendedBufferedReader reader = new ExtendedBufferedReader(new StringReader("hello\nworld\n"));
        int ch;
        int lineNumber = 0;

        ch = reader.read();
        assertEquals('h', ch);
        lineNumber++;

        ch = reader.read();
        assertEquals('e', ch);
        lineNumber++;

        ch = reader.read();
        assertEquals('l', ch);
        lineNumber++;

        ch = reader.read();
        assertEquals('\n', ch);
        lineNumber++;

        ch = reader.read();
        assertEquals('w', ch);
        lineNumber++;

        ch = reader.read();
        assertEquals('o', ch);
        lineNumber++;

        ch = reader.read();
        assertEquals('r', ch);
        lineNumber++;

        ch = reader.read();
        assertEquals('l', ch);
        lineNumber++;

        ch = reader.read();
        assertEquals('\n', ch);
        lineNumber++;

        ch = reader.read();
        assertEquals(-1, ch);

        assertEquals(6, reader.getLineNumber());
    }

    @Test
    public void testReadAgain() throws Exception {
        ExtendedBufferedReader reader = new ExtendedBufferedReader(new StringReader("hello\nworld\n"));
        int ch;

        ch = reader.read();
        assertEquals('h', ch);

        ch = reader.readAgain();
        assertEquals('h', ch);

        ch = reader.read();
        assertEquals('e', ch);

        ch = reader.readAgain();
        assertEquals('e', ch);

        ch = reader.read();
        assertEquals('l', ch);

        ch = reader.readAgain();
        assertEquals('l', ch);

        ch = reader.read();
        assertEquals('\n', ch);

        ch = reader.readAgain();
        assertEquals('\n', ch);

        ch = reader.read();
        assertEquals('w', ch);

        ch = reader.readAgain();
        assertEquals('w', ch);

        ch = reader.read();
        assertEquals('o', ch);

        ch = reader.readAgain();
        assertEquals('o', ch);

        ch = reader.read();
        assertEquals('r', ch);

        ch = reader.readAgain();
        assertEquals('r', ch);

        ch = reader.read();
        assertEquals('l', ch);

        ch = reader.readAgain();
        assertEquals('l', ch);

        ch = reader.read();
        assertEquals('\n', ch);

        ch = reader.readAgain();
        assertEquals(-1, ch);
    }

    @Test
    public void testLookAhead() throws Exception {
        ExtendedBufferedReader reader = new ExtendedBufferedReader(new StringReader("hello\nworld\n"));
        int ch, lookAhead;

        ch = reader.read();
        assertEquals('h', ch);

        lookAhead = reader.lookAhead();
        assertEquals('e', lookAhead);

        ch = reader.read();
        assertEquals('e', ch);

        lookAhead = reader.lookAhead();
        assertEquals('l', lookAhead);

        ch = reader.read();
        assertEquals('l', ch);

        lookAhead = reader.lookAhead();
        assertEquals('\n', lookAhead);

        ch = reader.read();
        assertEquals('\n', ch);

        lookAhead = reader.lookAhead();
        assertEquals('w', lookAhead);

        ch = reader.read();
        assertEquals('w', ch);

        lookAhead = reader.lookAhead();
        assertEquals('o', lookAhead);

        ch = reader.read();
        assertEquals('o', ch);

        lookAhead = reader.lookAhead();
        assertEquals('r', lookAhead);

        ch = reader.read();
        assertEquals('r', ch);

        lookAhead = reader.lookAhead();
        assertEquals('l', lookAhead);

        ch = reader.read();
        assertEquals('l', ch);

        lookAhead = reader.lookAhead();
        assertEquals('\n', lookAhead);

        ch = reader.read();
        assertEquals(-1, ch);
    }

    @Test
    public void testGetLineNumber() throws Exception {
        ExtendedBufferedReader reader = new ExtendedBufferedReader(new StringReader("hello\nworld\n"));

        assertEquals(0, reader.getLineNumber());

        String line = reader.readLine();
        assertEquals(1, reader.getLineNumber());
        assertNull(line);

        line = reader.readLine();
        assertEquals(2, reader.getLineNumber());
        assertEquals("world\n", line);

        assertEquals(2, reader.getLineNumber());

        line = reader.readLine();
        assertEquals(-1, reader.getLineNumber());
    }

    @Test
    public void testMixedReadLineAndRead() throws Exception {
        ExtendedBufferedReader reader = new ExtendedBufferedReader(new StringReader("hello\nworld\n"));
        int ch, lineNumber = 0;

        String line = reader.readLine();
        assertEquals(1, reader.getLineNumber());
        assertEquals("hello\n", line);
        lineNumber++;

        ch = reader.read();
        assertEquals('w', ch);

        line = reader.readLine();
        assertEquals(3, reader.getLineNumber());
        assertEquals("world\n", line);

        ch = reader.read();
        assertEquals('o', ch);

        line = reader.readLine();
        assertEquals(-1, reader.getLineNumber());

        assertEquals(3, lineNumber);
    }

    @Test
    public void testEmptyLine() throws Exception {
        ExtendedBufferedReader reader = new ExtendedBufferedReader(new StringReader("foo\n\nbar\n"));
        int ch, lineNumber = 0;

        String line = reader.readLine();
        assertEquals(1, reader.getLineNumber());
        assertEquals("foo\n", line);
        lineNumber++;

        line = reader.readLine();
        assertEquals(3, reader.getLineNumber());
        assertNull(line);

        line = reader.readLine();
        assertEquals(4, reader.getLineNumber());
        assertEquals("bar\n", line);

        assertEquals(3, lineNumber);
    }

    @Test
    public void testReadLineWithMark() throws Exception {
        ExtendedBufferedReader reader = new ExtendedBufferedReader(new StringReader("hello\nworld\n"));
        String savedLine;

        reader.mark(100);
        String line = reader.readLine();
        assertEquals(1, reader.getLineNumber());
        assertEquals("hello\n", line);

        savedLine = line;
        line = reader.readLine();
        assertEquals(2, reader.getLineNumber());
        assertEquals("world\n", line);

        reader.reset();
        assertEquals(savedLine, reader.readLine());
        assertEquals(1, reader.getLineNumber());
        assertEquals("hello\n", savedLine);
    }

    @Test(expected = IllegalStateException.class)
    public void testLookAheadAfterConsumingInput() throws Exception {
        ExtendedBufferedReader reader = new ExtendedBufferedReader(new StringReader("hello\nworld\n"));
        int ch;

        ch = reader.read();
        assertEquals('h', ch);

        int lookAhead = reader.lookAhead();
        assertEquals('e', lookAhead);

        ch = reader.read();
        assertEquals('e', ch);

        lookAhead = reader.lookAhead();
        assertEquals('l', lookAhead);

        ch = reader.read();
        assertEquals('l', ch);

        lookAhead = reader.lookAhead();
        assertEquals('\n', lookAhead);

        reader.readLine();

        lookAhead = reader.lookAhead();
        //throws exception
    }
}
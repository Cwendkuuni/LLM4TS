package org.apache.commons.csv;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import org.junit.Test;

public class ExtendedBufferedReaderTest {

    @Test
    public void testRead() throws IOException {
        String input = "Line1\nLine2\nLine3\n";
        Reader reader = new InputStreamReader(new ByteArrayInputStream(input.getBytes()));
        ExtendedBufferedReader extReader = new ExtendedBufferedReader(reader);

        char[] buffer = new char[3];

        for (int i = 0; i < 3; i++) {
            int result = extReader.read(buffer, 0, 1);
            assertEquals(i + 1, result);
            assertEquals((char) (i + 65), buffer[0]);
        }

        int result = extReader.read(buffer, 0, 1);
        assertEquals('\n', buffer[0]);
        assertEquals(-1, result);

        result = extReader.read();
        assertEquals(-1, result);
    }

    @Test
    public void testReadAgain() throws IOException {
        String input = "Line1\nLine2\nLine3\n";
        Reader reader = new InputStreamReader(new ByteArrayInputStream(input.getBytes()));
        ExtendedBufferedReader extReader = new ExtendedBufferedReader(reader);

        int result = extReader.read();
        assertEquals('L', result);
        assertEquals('L', extReader.readAgain());

        result = extReader.read();
        assertEquals('i', result);
        assertEquals('i', extReader.readAgain());

        result = extReader.read();
        assertEquals('n', result);
        assertEquals('n', extReader.readAgain());

        result = extReader.read();
        assertEquals('\n', result);
        assertEquals('\n', extReader.readAgain());

        result = extReader.read();
        assertEquals('L', result);
        assertEquals('L', extReader.readAgain());

        result = extReader.read();
        assertEquals('i', result);
        assertEquals('i', extReader.readAgain());

        result = extReader.read();
        assertEquals('n', result);
        assertEquals('n', extReader.readAgain());

        result = extReader.read();
        assertEquals('e', result);
        assertEquals('e', extReader.readAgain());

        result = extReader.read();
        assertEquals('\n', result);
        assertEquals('\n', extReader.readAgain());

        result = extReader.read();
        assertEquals('L', result);
        assertEquals('L', extReader.readAgain());

        result = extReader.read();
        assertEquals('i', result);
        assertEquals('i', extReader.readAgain());

        result = extReader.read();
        assertEquals('n', result);
        assertEquals('n', extReader.readAgain());

        result = extReader.read();
        assertEquals('\n', result);
        assertEquals('\n', extReader.readAgain());

        result = extReader.read();
        assertEquals(-1, result);
        assertNull(extReader.readAgain());
    }

    @Test
    public void testReadLine() throws IOException {
        String input = "Line1\nLine2\nLine3\n";
        Reader reader = new InputStreamReader(new ByteArrayInputStream(input.getBytes()));
        ExtendedBufferedReader extReader = new ExtendedBufferedReader(reader);

        String line1 = extReader.readLine();
        assertEquals("Line1", line1);
        assertEquals('n', extReader.readAgain());

        String line2 = extReader.readLine();
        assertEquals("Line2", line2);
        assertEquals('\n', extReader.readAgain());

        String line3 = extReader.readLine();
        assertEquals("Line3", line3);
        assertEquals(-1, extReader.readAgain());

        String line4 = extReader.readLine();
        assertNull(line4);
        assertEquals(-1, extReader.readAgain());
    }

    @Test
    public void testLookAhead() throws IOException {
        String input = "Line1\nLine2\nLine3\n";
        Reader reader = new InputStreamReader(new ByteArrayInputStream(input.getBytes()));
        ExtendedBufferedReader extReader = new ExtendedBufferedReader(reader);

        int lookAhead1 = extReader.lookAhead();
        assertEquals('L', (char) lookAhead1);

        int lookAhead2 = extReader.lookAhead();
        assertEquals('i', (char) lookAhead2);

        int lookAhead3 = extReader.lookAhead();
        assertEquals('n', (char) lookAhead3);

        int lookAhead4 = extReader.lookAhead();
        assertEquals('\n', (char) lookAhead4);

        int lookAhead5 = extReader.lookAhead();
        assertEquals(-1, lookAhead5);
    }

    @Test
    public void testGetLineNumber() throws IOException {
        String input = "Line1\nLine2\nLine3\n";
        Reader reader = new InputStreamReader(new ByteArrayInputStream(input.getBytes()));
        ExtendedBufferedReader extReader = new ExtendedBufferedReader(reader);

        assertEquals(0, extReader.getLineNumber());

        String line1 = extReader.readLine();
        assertEquals(1, extReader.getLineNumber());

        String line2 = extReader.readLine();
        assertEquals(2, extReader.getLineNumber());

        String line3 = extReader.readLine();
        assertEquals(3, extReader.getLineNumber());

        String line4 = extReader.readLine();
        assertEquals(-1, extReader.getLineNumber());
    }

    @Test
    public void testStateAfterReadingComment() throws IOException {
        String input = "Line1\n#Line2\nLine3\n";
        Reader reader = new InputStreamReader(new ByteArrayInputStream(input.getBytes()));
        ExtendedBufferedReader extReader = new ExtendedBufferedReader(reader);

        String comment = extReader.readLine();
        assertEquals("Line2", comment);
        assertEquals('\n', extReader.readAgain());
        assertEquals(1, extReader.getLineNumber());

        String line3 = extReader.readLine();
        assertEquals("Line3", line3);
        assertEquals(-1, extReader.readAgain());
        assertEquals(2, extReader.getLineNumber());
    }

    @Test
    public void testStateAfterReadingEmptyLine() throws IOException {
        String input = "Line1\n\nLine3\n";
        Reader reader = new InputStreamReader(new ByteArrayInputStream(input.getBytes()));
        ExtendedBufferedReader extReader = new ExtendedBufferedReader(reader);

        String line1 = extReader.readLine();
        assertEquals("Line1", line1);
        assertEquals('\n', extReader.readAgain());
        assertEquals(1, extReader.getLineNumber());

        String emptyLine = extReader.readLine();
        assertNull(emptyLine);
        assertEquals('\n', extReader.readAgain());
        assertEquals(2, extReader.getLineNumber());

        String line3 = extReader.readLine();
        assertEquals("Line3", line3);
        assertEquals(-1, extReader.readAgain());
        assertEquals(3, extReader.getLineNumber());
    }
}
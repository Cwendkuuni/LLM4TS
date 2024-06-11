package org.apache.commons.csv;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class ExtendedBufferedReaderTest {

    @Test
    public void testRead() throws Exception {
        String input = "foo\nbar\nbaz\n";
        StringReader reader = new StringReader(input);
        ExtendedBufferedReader extReader = new ExtendedBufferedReader(reader);

        int c1 = extReader.read();
        assertEquals('f', c1);

        int c2 = extReader.read();
        assertEquals('o', c2);

        int c3 = extReader.read();
        assertEquals('o', c3);

        int c4 = extReader.read();
        assertEquals('\n', c4);

        int c5 = extReader.read();
        assertEquals('b', c5);

        int c6 = extReader.read();
        assertEquals('a', c6);

        int c7 = extReader.read();
        assertEquals('\n', c7);

        int c8 = extReader.read();
        assertEquals('b', c8);

        int c9 = extReader.read();
        assertEquals('a', c9);

        int c10 = extReader.read();
        assertEquals('\n', c10);

        int c11 = extReader.read();
        assertEquals('z', c11);

        int c12 = extReader.read();
        assertEquals('a', c12);

        int c13 = extReader.read();
        assertEquals('\n', c13);

        int c14 = extReader.read();
        assertNull(c14);
    }

    @Test
    public void testReadAgain() throws Exception {
        String input = "foo\nbar\nbaz\n";
        StringReader reader = new StringReader(input);
        ExtendedBufferedReader extReader = new ExtendedBufferedReader(reader);

        int c1 = extReader.read();
        assertEquals('f', c1);

        int c2 = extReader.readAgain();
        assertEquals('f', c2);

        int c3 = extReader.read();
        assertEquals('o', c3);

        int c4 = extReader.readAgain();
        assertEquals('o', c4);

        int c5 = extReader.read();
        assertEquals('\n', c5);

        int c6 = extReader.readAgain();
        assertEquals('b', c6);

        int c7 = extReader.read();
        assertEquals('a', c7);

        int c8 = extReader.readAgain();
        assertEquals('a', c8);

        int c9 = extReader.read();
        assertEquals('\n', c9);

        int c10 = extReader.read();
        assertEquals('b', c10);

        int c11 = extReader.read();
        assertEquals('a', c11);

        int c12 = extReader.read();
        assertEquals('\n', c12);

        int c13 = extReader.read();
        assertEquals('z', c13);

        int c14 = extReader.read();
        assertEquals('a', c14);

        int c15 = extReader.read();
        assertNull(c15);

        int c16 = extReader.readAgain();
        assertNull(c16);
    }

    @Test
    public void testReadLine() throws Exception {
        String input = "foo\nbar\nbaz\n";
        StringReader reader = new StringReader(input);
        ExtendedBufferedReader extReader = new ExtendedBufferedReader(reader);

        String line1 = extReader.readLine();
        assertEquals("foo", line1);
        assertEquals('o', extReader.readAgain());

        String line2 = extReader.readLine();
        assertEquals("bar", line2);
        assertEquals('b', extReader.readAgain());

        String line3 = extReader.readLine();
        assertEquals("baz", line3);
        assertEquals('\n', extReader.readAgain());

        String line4 = extReader.readLine();
        assertNull(line4);
        assertEquals(ExtendedBufferedReader.END_OF_STREAM, extReader.readAgain());
    }

    @Test
    public void testLookAhead() throws Exception {
        String input = "foo\nbar\nbaz\n";
        StringReader reader = new StringReader(input);
        ExtendedBufferedReader extReader = new ExtendedBufferedReader(reader);

        int c1 = extReader.lookAhead();
        assertEquals('f', c1);

        int c2 = extReader.read();
        assertEquals('o', c2);

        int c3 = extReader.lookAhead();
        assertEquals('o', c3);

        int c4 = extReader.read();
        assertEquals('\n', c4);

        int c5 = extReader.lookAhead();
        assertEquals('b', c5);

        int c6 = extReader.read();
        assertEquals('a', c6);

        int c7 = extReader.lookAhead();
        assertEquals('a', c7);

        int c8 = extReader.read();
        assertEquals('\n', c8);

        int c9 = extReader.lookAhead();
        assertEquals('b', c9);

        int c10 = extReader.read();
        assertEquals('a', c10);

        int c11 = extReader.lookAhead();
        assertEquals('z', c11);

        int c12 = extReader.read();
        assertEquals('a', c12);

        int c13 = extReader.lookAhead();
        assertEquals('\n', c13);

        int c14 = extReader.read();
        assertNull(c14);

        int c15 = extReader.lookAhead();
        assertEquals(ExtendedBufferedReader.END_OF_STREAM, c15);
    }

    @Test
    public void testGetLineNumber() throws Exception {
        String input = "foo\nbar\nbaz\n";
        StringReader reader = new StringReader(input);
        ExtendedBufferedReader extReader = new ExtendedBufferedReader(reader);

        int lineNumber1 = extReader.getLineNumber();
        assertEquals(1, lineNumber1);

        String line1 = extReader.readLine();
        assertEquals("foo", line1);

        int lineNumber2 = extReader.getLineNumber();
        assertEquals(2, lineNumber2);

        String line2 = extReader.readLine();
        assertEquals("bar", line2);

        int lineNumber3 = extReader.getLineNumber();
        assertEquals(3, lineNumber3);

        String line3 = extReader.readLine();
        assertEquals("baz", line3);

        int lineNumber4 = extReader.getLineNumber();
        assertEquals(4, lineNumber4);

        String line4 = extReader.readLine();
        assertNull(line4);

        int lineNumber5 = extReader.getLineNumber();
        assertEquals(4, lineNumber5);
    }

    @Test
    public void testMixedUsage() throws Exception {
        String input = "foo\nbar\nbaz\n";
        StringReader reader = new StringReader(input);
        ExtendedBufferedReader extReader = new ExtendedBufferedReader(reader);

        int c1 = extReader.lookAhead();
        assertEquals('f', c1);

        int c2 = extReader.read();
        assertEquals('o', c2);

        int c3 = extReader.lookAhead();
        assertEquals('o', c3);

        String line1 = extReader.readLine();
        assertEquals("foo", line1);

        int c4 = extReader.lookAhead();
        assertEquals('b', c4);

        int c5 = extReader.read();
        assertEquals('a', c5);

        String line2 = extReader.readLine();
        assertEquals("bar", line2);

        int c6 = extReader.lookAhead();
        assertEquals('b', c6);

        int c7 = extReader.read();
        assertEquals('a', c7);

        String line3 = extReader.readLine();
        assertEquals("baz", line3);

        int c8 = extReader.lookAhead();
        assertEquals('\n', c8);

        int c9 = extReader.read();
        assertNull(c9);

        int c10 = extReader.lookAhead();
        assertEquals(ExtendedBufferedReader.END_OF_STREAM, c10);

        int lineNumber = extReader.getLineNumber();
        assertEquals(3, lineNumber);
    }

    @Test
    public void testConsumeAll() throws Exception {
        String input = "foo\nbar\nbaz\n";
        StringReader reader = new StringReader(input);
        ExtendedBufferedReader extReader = new ExtendedBufferedReader(reader);

        String all = "";
        while (extReader.readLine() != null) {
            all += extReader.readLine() + "\n";
        }

        String expected = "foo\nbar\nbaz";
        assertEquals(expected, all);
        assertEquals(ExtendedBufferedReader.END_OF_STREAM, extReader.readAgain());
    }

    @Test
    public void testReadLineWithCR() throws Exception {
        String input = "foo\r\nbar\r\nbaz\r\n";
        StringReader reader = new StringReader(input);
        ExtendedBufferedReader extReader = new ExtendedBufferedReader(reader);

        String line1 = extReader.readLine();
        assertEquals("foo\r", line1);
        assertEquals('f', extReader.readAgain());

        String line2 = extReader.readLine();
        assertEquals("bar\r", line2);
        assertEquals('b', extReader.readAgain());

        String line3 = extReader.readLine();
        assertEquals("baz\r", line3);
        assertEquals('\n', extReader.readAgain());

        String line4 = extReader.readLine();
        assertNull(line4);
        assertEquals(ExtendedBufferedReader.END_OF_STREAM, extReader.readAgain());
    }

    @Test
    public void testReadLineWithLF() throws Exception {
        String input = "foo\nbar\nbaz\n";
        StringReader reader = new StringReader(input);
        ExtendedBufferedReader extReader = new ExtendedBufferedReader(reader);

        String line1 = extReader.readLine();
        assertEquals("foo", line1);
        assertEquals('o', extReader.readAgain());

        String line2 = extReader.readLine();
        assertEquals("bar", line2);
        assertEquals('b', extReader.readAgain());

        String line3 = extReader.readLine();
        assertEquals("baz", line3);
        assertEquals('\n', extReader.readAgain());

        String line4 = extReader.readLine();
        assertNull(line4);
        assertEquals(ExtendedBufferedReader.END_OF_STREAM, extReader.readAgain());
    }

    @Test
    public void testLookAheadWithCR() throws Exception {
        String input = "foo\r\nbar\r\nbaz\r\n";
        StringReader reader = new StringReader(input);
        ExtendedBufferedReader extReader = new ExtendedBufferedReader(reader);

        int c1 = extReader.lookAhead();
        assertEquals('f', c1);

        int c2 = extReader.read();
        assertEquals('o', c2);

        int c3 = extReader.lookAhead();
        assertEquals('o', c3);

        int c4 = extReader.read();
        assertEquals('\r', c4);

        int c5 = extReader.lookAhead();
        assertEquals('\n', c5);

        int c6 = extReader.read();
        assertEquals('b', c6);

        int c7 = extReader.lookAhead();
        assertEquals('a', c7);

        int c8 = extReader.read();
        assertEquals('a', c8);

        int c9 = extReader.lookAhead();
        assertEquals('\r', c9);

        int c10 = extReader.read();
        assertEquals('\n', c10);

        int c11 = extReader.lookAhead();
        assertEquals('b', c11);

        int c12 = extReader.read();
        assertEquals('a', c12);

        int c13 = extReader.lookAhead();
        assertEquals('\r', c13);

        int c14 = extReader.read();
        assertEquals('\n', c14);

        int c15 = extReader.lookAhead();
        assertEquals(ExtendedBufferedReader.END_OF_STREAM, c15);
    }

    @Test
    public void testLookAheadWithLF() throws Exception {
        String input = "foo\nbar\nbaz\n";
        StringReader reader = new StringReader(input);
        ExtendedBufferedReader extReader = new ExtendedBufferedReader(reader);

        int c1 = extReader.lookAhead();
        assertEquals('f', c1);

        int c2 = extReader.read();
        assertEquals('o', c2);

        int c3 = extReader.lookAhead();
        assertEquals('o', c3);

        int c4 = extReader.read();
        assertEquals('\n', c4);

        int c5 = extReader.lookAhead();
        assertEquals('b', c5);

        int c6 = extReader.read();
        assertEquals('a', c6);

        int c7 = extReader.lookAhead();
        assertEquals('a', c7);

        int c8 = extReader.read();
        assertEquals('\n', c8);

        int c9 = extReader.lookAhead();
        assertEquals('b', c9);

        int c10 = extReader.read();
        assertEquals('a', c10);

        int c11 = extReader.lookAhead();
        assertEquals('\n', c11);

        int c12 = extReader.read();
        assertNull(c12);

        int c13 = extReader.lookAhead();
        assertEquals(ExtendedBufferedReader.END_OF_STREAM, c13);
    }
}
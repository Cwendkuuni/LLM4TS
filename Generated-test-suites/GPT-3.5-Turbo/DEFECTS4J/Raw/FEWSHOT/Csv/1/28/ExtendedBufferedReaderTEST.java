package org.apache.commons.csv;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertFalse;

import org.junit.Test;
import java.io.StringReader;

public class ExtendedBufferedReaderTest {
    @Test
    public void testRead() throws Exception {
        StringReader reader1 = new StringReader("abc");
        ExtendedBufferedReader br1 = new ExtendedBufferedReader(reader1);
        int result1 = br1.read();
        assertEquals('a', result1);
        int result2 = br1.read();
        assertEquals('b', result2);
        int result3 = br1.read();
        assertEquals('c', result3);
        int result4 = br1.read();
        assertEquals(ExtendedBufferedReader.END_OF_STREAM, result4);
    }

    @Test
    public void testReadAgain() throws Exception {
        StringReader reader1 = new StringReader("abc");
        ExtendedBufferedReader br1 = new ExtendedBufferedReader(reader1);
        br1.read();
        br1.read();
        br1.read();
        int result1 = br1.readAgain();
        assertEquals('c', result1);
    }
    
    @Test
    public void testReadBuffer() throws Exception {
        StringReader reader2 = new StringReader("hello\nworld");
        ExtendedBufferedReader br2 = new ExtendedBufferedReader(reader2);
        char[] buf = new char[5];
        int result1 = br2.read(buf, 0, 5);
        assertEquals(5, result1);
        assertEquals("hello", new String(buf));
        int result2 = br2.read(buf, 0, 5);
        assertEquals(5, result2);
        assertEquals("worlh", new String(buf));
        int result3 = br2.read(buf, 0, 5);
        assertEquals(1, result3);
        assertEquals("drlrh", new String(buf));
        int result4 = br2.read(buf, 0, 5);
        assertEquals(ExtendedBufferedReader.END_OF_STREAM, result4);
    }
    
    @Test
    public void testReadLine() throws Exception {
        StringReader reader3 = new StringReader("line1\nline2\nline3\n");
        ExtendedBufferedReader br3 = new ExtendedBufferedReader(reader3);
        String result1 = br3.readLine();
        assertEquals("line1", result1);
        String result2 = br3.readLine();
        assertEquals("line2", result2);
        String result3 = br3.readLine();
        assertEquals("line3", result3);
        String result4 = br3.readLine();
        assertNull(result4);
    }

    @Test
    public void testLookAhead() throws Exception {
        StringReader reader4 = new StringReader("abc");
        ExtendedBufferedReader br4 = new ExtendedBufferedReader(reader4);
        int result1 = br4.lookAhead();
        assertEquals('a', result1);
        int result2 = br4.read();
        assertEquals('a', result2);
        int result3 = br4.lookAhead();
        assertEquals('b', result3);
    }

    @Test
    public void testGetLineNumber() throws Exception {
        StringReader reader5 = new StringReader("line1\nline2\nline3\n");
        ExtendedBufferedReader br5 = new ExtendedBufferedReader(reader5);
        assertEquals(0, br5.getLineNumber());
        br5.readLine();
        assertEquals(1, br5.getLineNumber());
        br5.readLine();
        assertEquals(2, br5.getLineNumber());
        br5.readLine();
        assertEquals(3, br5.getLineNumber());
    }
}
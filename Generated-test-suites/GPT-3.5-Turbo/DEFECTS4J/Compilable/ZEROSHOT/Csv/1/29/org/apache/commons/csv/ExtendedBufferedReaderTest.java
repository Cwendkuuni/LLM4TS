package org.apache.commons.csv;
import org.junit.Before;
import org.junit.Test;
import java.io.StringReader;
import java.io.IOException;
import static org.junit.Assert.assertEquals;

public class ExtendedBufferedReaderTest {

    private ExtendedBufferedReader reader;

    @Before
    public void setup() {
        reader = new ExtendedBufferedReader(new StringReader("Test line 1\nTest line 2\rTest line 3\r\nTest line 4"));
    }

    @Test
    public void testRead() throws IOException {
        int c1 = reader.read();
        int c2 = reader.read();
        int c3 = reader.read();

        assertEquals('T', c1);
        assertEquals('e', c2);
        assertEquals('s', c3);
    }

    @Test
    public void testReadAgain() throws IOException {
        reader.read();
        int lastChar = reader.readAgain();

        assertEquals('T', lastChar);
    }

    @Test
    public void testReadCharArray() throws IOException {
        char[] buf = new char[7];

        int len = reader.read(buf, 0, buf.length);

        assertEquals(7, len);
        assertEquals("Test li", new String(buf));
    }

    @Test
    public void testReadLine() throws IOException {
        String line = reader.readLine();

        assertEquals("Test line 1", line);
    }

    @Test
    public void testLookAhead() throws IOException {
        int c = reader.lookAhead();

        assertEquals('T', c);
    }

    @Test
    public void testGetLineNumber() throws IOException {
        int lineNumber = reader.getLineNumber();

        assertEquals(0, lineNumber);
    }

    @Test
    public void testReadNewLineChar() throws IOException {
        reader.read();

        int lineNumber = reader.getLineNumber();

        assertEquals(1, lineNumber);
    }

    @Test
    public void testReadCarriageReturnChar() throws IOException {
        reader.read();
        reader.read();

        int lineNumber = reader.getLineNumber();

        assertEquals(1, lineNumber);
    }

    @Test
    public void testReadCrLfSequence() throws IOException {
        reader.read();
        reader.read();
        reader.read();

        int lineNumber = reader.getLineNumber();

        assertEquals(2, lineNumber);
    }

    @Test
    public void testReadAgainAfterCrLfSequence() throws IOException {
        reader.read();
        reader.read();
        reader.read();
        reader.readAgain();

        int lineNumber = reader.getLineNumber();

        assertEquals(2, lineNumber);
    }

    @Test
    public void testReadAgainAtEof() throws IOException {
        reader.readLine();
        reader.readLine();
        reader.readLine();
        reader.readLine();
        reader.readLine();

        int lastChar = reader.readAgain();

        assertEquals(ExtendedBufferedReader.END_OF_STREAM, lastChar);
    }

}
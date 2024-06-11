import static org.junit.Assert.*;

import java.io.IOException;
import java.io.StringReader;

import org.junit.Test;

public class ExtendedBufferedReaderTest {

    // Test individual methods

    @Test
    public void testReadAgain() throws IOException {
        ExtendedBufferedReader reader = new ExtendedBufferedReader(new StringReader("hello world"));
        reader.read();
        int lastChar = reader.readAgain();
        assertEquals('h', lastChar);
    }

    @Test
    public void testLookAhead() throws IOException {
        ExtendedBufferedReader reader = new ExtendedBufferedReader(new StringReader("hello world"));
        int nextChar = reader.lookAhead();
        assertEquals('h', nextChar);
    }

    @Test
    public void testGetLineNumber() throws IOException {
        ExtendedBufferedReader reader = new ExtendedBufferedReader(new StringReader("line 1\nline 2"));
        reader.readLine();
        int lineNumber = reader.getLineNumber();
        assertEquals(1, lineNumber);
    }

    // Test edge cases and exceptions

    @Test
    public void testReadEmptyReader() throws IOException {
        ExtendedBufferedReader reader = new ExtendedBufferedReader(new StringReader(""));
        int character = reader.read();
        assertEquals(ExtendedBufferedReader.END_OF_STREAM, character);
    }

    @Test
    public void testReadSingleCharacter() throws IOException {
        ExtendedBufferedReader reader = new ExtendedBufferedReader(new StringReader("a"));
        int character = reader.read();
        assertEquals('a', character);
    }

    @Test
    public void testReadToEnd() throws IOException {
        ExtendedBufferedReader reader = new ExtendedBufferedReader(new StringReader("hello"));
        reader.read();
        reader.read();
        reader.read();
        reader.read();
        reader.read();
        int character = reader.read();
        assertEquals(ExtendedBufferedReader.END_OF_STREAM, character);
    }

    @Test
    public void testReadLineWithMultipleTerminators() throws IOException {
        ExtendedBufferedReader reader = new ExtendedBufferedReader(new StringReader("line 1\n\r"));
        String line = reader.readLine();
        assertEquals("line 1", line);
    }

    @Test
    public void testReadLineWithNoTerminators() throws IOException {
        ExtendedBufferedReader reader = new ExtendedBufferedReader(new StringReader("line 1"));
        String line = reader.readLine();
        assertEquals("line 1", line);
    }

    @Test
    public void testLookAhead() throws IOException {
        ExtendedBufferedReader reader = new ExtendedBufferedReader(new StringReader("hello"));
        int nextChar = reader.lookAhead();
        assertEquals('h', nextChar);
        int character = reader.read();
        assertEquals('h', character);
    }

    @Test
    public void testGetLineNumberAfterReadingLine() throws IOException {
        ExtendedBufferedReader reader = new ExtendedBufferedReader(new StringReader("line 1\nline 2"));
        reader.readLine();
        int lineNumber = reader.getLineNumber();
        assertEquals(2, lineNumber);
    }

    @Test
    public void testGetLineNumberBeforeReadingLines() throws IOException {
        ExtendedBufferedReader reader = new ExtendedBufferedReader(new StringReader("line 1\nline 2"));
        int lineNumber = reader.getLineNumber();
        assertEquals(0, lineNumber);
    }

}


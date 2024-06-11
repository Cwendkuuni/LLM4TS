import org.junit.Test;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;

import static org.junit.Assert.assertEquals;

public class ExtendedBufferedReaderTest {

    @Test
    public void testRead() throws IOException {
        StringReader reader = new StringReader("Hello world\nHow are you?\r\nI'm fine");
        ExtendedBufferedReader extendedReader = new ExtendedBufferedReader(reader);

        int result = extendedReader.read();

        assertEquals('H', result);
    }

    @Test
    public void testReadAgain() throws IOException {
        StringReader reader = new StringReader("Hello world\nHow are you?\r\nI'm fine");
        ExtendedBufferedReader extendedReader = new ExtendedBufferedReader(reader);

        extendedReader.read();
        int result = extendedReader.readAgain();

        assertEquals('H', result);
    }

    @Test
    public void testReadLine() throws IOException {
        StringReader reader = new StringReader("Hello world\nHow are you?\r\nI'm fine");
        ExtendedBufferedReader extendedReader = new ExtendedBufferedReader(reader);

        String result = extendedReader.readLine();

        assertEquals("Hello world", result);
    }

    @Test
    public void testLookAhead() throws IOException {
        StringReader reader = new StringReader("Hello world\nHow are you?\r\nI'm fine");
        ExtendedBufferedReader extendedReader = new ExtendedBufferedReader(reader);

        int result = extendedReader.lookAhead();

        assertEquals('H', result);
    }

    @Test
    public void testGetLineNumber() throws IOException {
        StringReader reader = new StringReader("Hello world\nHow are you?\r\nI'm fine");
        ExtendedBufferedReader extendedReader = new ExtendedBufferedReader(reader);

        extendedReader.readLine();
        int result = extendedReader.getLineNumber();

        assertEquals(1, result);
    }

    @Test
    public void testReadWithEmptyReader() throws IOException {
        StringReader reader = new StringReader("");
        ExtendedBufferedReader extendedReader = new ExtendedBufferedReader(reader);

        int result = extendedReader.read();

        assertEquals(-1, result);
    }

    @Test
    public void testLookAheadWithEmptyReader() throws IOException {
        StringReader reader = new StringReader("");
        ExtendedBufferedReader extendedReader = new ExtendedBufferedReader(reader);

        int result = extendedReader.lookAhead();

        assertEquals(-1, result);
    }

    @Test
    public void testReadLineWithEmptyReader() throws IOException {
        StringReader reader = new StringReader("");
        ExtendedBufferedReader extendedReader = new ExtendedBufferedReader(reader);

        String result = extendedReader.readLine();

        assertEquals(null, result);
    }
}
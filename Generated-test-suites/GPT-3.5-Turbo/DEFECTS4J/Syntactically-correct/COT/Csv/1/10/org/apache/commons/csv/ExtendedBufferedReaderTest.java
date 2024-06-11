
import org.junit.Test;

import java.io.IOException;
import java.io.StringReader;

import static org.junit.Assert.assertEquals;

public class ExtendedBufferedReaderTest {

    @Test
    public void testRead() throws IOException {
        StringReader reader = new StringReader("Hello");
        ExtendedBufferedReader br = new ExtendedBufferedReader(reader);

        int actual = br.read();
        int expected = 'H';

        assertEquals(expected, actual);
    }

    @Test
    public void testReadAgain() throws IOException {
        StringReader reader = new StringReader("Hello");
        ExtendedBufferedReader br = new ExtendedBufferedReader(reader);

        br.read();
        int actual = br.readAgain();
        int expected = 'H';

        assertEquals(expected, actual);
    }

    @Test
    public void testReadLine() throws IOException {
        StringReader reader = new StringReader("Hello\nWorld");
        ExtendedBufferedReader br = new ExtendedBufferedReader(reader);

        String actual = br.readLine();
        String expected = "Hello";

        assertEquals(expected, actual);
    }

    @Test
    public void testLookAhead() throws IOException {
        StringReader reader = new StringReader("Hello");
        ExtendedBufferedReader br = new ExtendedBufferedReader(reader);

        int actual = br.lookAhead();
        int expected = 'e';

        assertEquals(expected, actual);
    }

    @Test
    public void testGetLineNumber() throws IOException {
        StringReader reader = new StringReader("Hello\nWorld");
        ExtendedBufferedReader br = new ExtendedBufferedReader(reader);

        br.readLine();
        int actual = br.getLineNumber();
        int expected = 1;

        assertEquals(expected, actual);
    }
}


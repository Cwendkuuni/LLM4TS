import org.apache.commons.csv.ExtendedBufferedReader;
import org.junit.Test;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;

import static org.junit.Assert.assertEquals;

public class ExtendedBufferedReaderTest {

    @Test
    public void testRead() throws IOException {
        Reader reader = new StringReader("Hello\nWorld");
        ExtendedBufferedReader br = new ExtendedBufferedReader(reader);

        int firstChar = br.read();

        assertEquals('H', firstChar);
        assertEquals('\n', br.read());
        assertEquals('W', br.read());

        br.close();
    }

    @Test
    public void testReadAgain() throws IOException {
        Reader reader = new StringReader("Hello\nWorld");
        ExtendedBufferedReader br = new ExtendedBufferedReader(reader);

        assertEquals('H', br.read());
        assertEquals('\n', br.read());
        assertEquals('W', br.read());

        assertEquals('W', br.readAgain());

        br.close();
    }

    @Test
    public void testReadWithBuffer() throws IOException {
        Reader reader = new StringReader("Hello\nWorld");
        ExtendedBufferedReader br = new ExtendedBufferedReader(reader);

        char[] buffer = new char[5];
        int bytesRead = br.read(buffer, 0, buffer.length);

        assertEquals(5, bytesRead);
        assertEquals('H', buffer[0]);
        assertEquals('e', buffer[1]);
        assertEquals('l', buffer[2]);
        assertEquals('l', buffer[3]);
        assertEquals('o', buffer[4]);

        br.close();
    }

    @Test
    public void testReadLine() throws IOException {
        Reader reader = new StringReader("Hello\nWorld");
        ExtendedBufferedReader br = new ExtendedBufferedReader(reader);

        String firstLine = br.readLine();

        assertEquals("Hello", firstLine);
        assertEquals('W', br.read());

        br.close();
    }

    @Test
    public void testLookAhead() throws IOException {
        Reader reader = new StringReader("Hello\nWorld");
        ExtendedBufferedReader br = new ExtendedBufferedReader(reader);

        assertEquals('H', br.lookAhead());
        assertEquals('H', br.read());

        br.close();
    }

    @Test
    public void testGetLineNumber() throws IOException {
        Reader reader = new StringReader("Hello\nWorld");
        ExtendedBufferedReader br = new ExtendedBufferedReader(reader);

        assertEquals(0, br.getLineNumber());
        assertEquals('\n', br.read());
        assertEquals(1, br.getLineNumber());
        assertEquals('W', br.read());
        assertEquals(1, br.getLineNumber());

        br.close();
    }
}
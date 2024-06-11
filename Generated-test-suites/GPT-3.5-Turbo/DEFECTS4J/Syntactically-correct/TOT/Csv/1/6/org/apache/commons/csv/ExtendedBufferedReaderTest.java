import org.junit.Test;
import java.io.IOException;
import java.io.StringReader;
import static org.junit.Assert.assertEquals;

public class ExtendedBufferedReaderTest {

    @Test
    public void testRead() throws IOException {
        String input = "Hello World!";
        ExtendedBufferedReader reader = new ExtendedBufferedReader(new StringReader(input));

        int result = reader.read();

        assertEquals('H', result);
    }

    @Test
    public void testReadAgain() throws IOException {
        String input = "Hello World!";
        ExtendedBufferedReader reader = new ExtendedBufferedReader(new StringReader(input));

        reader.read();
        int result = reader.readAgain();

        assertEquals('H', result);
    }

    @Test
    public void testReadWithBuffer() throws IOException {
        String input = "Hello World!";
        char[] buf = new char[5];
        ExtendedBufferedReader reader = new ExtendedBufferedReader(new StringReader(input));

        int result = reader.read(buf, 0, 5);

        assertEquals(5, result);
        assertEquals('H', buf[0]);
        assertEquals('e', buf[1]);
        assertEquals('l', buf[2]);
        assertEquals('l', buf[3]);
        assertEquals('o', buf[4]);
    }

    @Test
    public void testReadLine() throws IOException {
        String input = "Hello\nWorld!";
        ExtendedBufferedReader reader = new ExtendedBufferedReader(new StringReader(input));

        String result = reader.readLine();

        assertEquals("Hello", result);
    }

    @Test
    public void testLookAhead() throws IOException {
        String input = "Hello World!";
        ExtendedBufferedReader reader = new ExtendedBufferedReader(new StringReader(input));

        int result = reader.lookAhead();

        assertEquals('e', result);
    }

    @Test
    public void testGetLineNumber() throws IOException {
        String input = "Hello\nWorld!";
        ExtendedBufferedReader reader = new ExtendedBufferedReader(new StringReader(input));

        reader.readLine();
        int result = reader.getLineNumber();

        assertEquals(1, result);
    }
}
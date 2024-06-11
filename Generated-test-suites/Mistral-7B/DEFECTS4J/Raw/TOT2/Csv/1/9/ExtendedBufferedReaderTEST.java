import org.apache.commons.csv.ExtendedBufferedReader;
import org.apache.commons.csv.StringReader;
import org.junit.Test;
import static org.junit.Assert.*;

public class ExtendedBufferedReaderTest {

    @Test
    public void testRead() throws IOException {
        Reader reader = new StringReader("Hello, World!");
        ExtendedBufferedReader extReader = new ExtendedBufferedReader(reader);

        int actual = extReader.read();
        int expected = 'H';

        assertEquals(expected, actual);
    }

    @Test(expected = IOException.class)
    public void testReadNullReader() {
        ExtendedBufferedReader extReader = new ExtendedBufferedReader(null);
    }

    @Test
    public void testEndOfStream() throws IOException {
        Reader reader = new StringReader("");
        ExtendedBufferedReader extReader = new ExtendedBufferedReader(reader);

        String actual = extReader.readLine();
        String expected = null;

        assertEquals(expected, actual);

        int c = extReader.lookAhead();

        assertEquals(ExtendedBufferedReader.END_OF_STREAM, c);
    }

    @Test
    public void testReadCR() throws IOException {
        Reader reader = new StringReader("\rHello, World!");
        ExtendedBufferedReader extReader = new ExtendedBufferedReader(reader);

        int actual = extReader.read();
        int expected = '\r';

        assertEquals(expected, actual);
    }

    @Test
    public void testReadLF() throws IOException {
        Reader reader = new StringReader("Hello, World!\n");
        ExtendedBufferedReader extReader = new ExtendedBufferedReader(reader);

        int actual = extReader.read();
        int expected = '\n';

        assertEquals(expected, actual);

        int lastChar = extReader.readAgain();

        assertNotEquals(expected, lastChar);
    }

}
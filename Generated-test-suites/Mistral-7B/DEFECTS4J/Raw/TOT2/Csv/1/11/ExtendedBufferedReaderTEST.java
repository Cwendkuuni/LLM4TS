import org.apache.commons.csv.ExtendedBufferedReader;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class ExtendedBufferedReaderTest {

    @Test
    public void testReadMethod() throws IOException {
        ExtendedBufferedReader reader = new ExtendedBufferedReader(System.in);
        int expectedChar = 'a';
        int actualChar = reader.read();
        assertEquals(expectedChar, actualChar);
    }

    // Edge case: testing end of stream
    @Test(expected = IOException.class)
    public void testReadEndOfStream() throws IOException {
        ExtendedBufferedReader reader = new ExtendedBufferedReader(System.in);
        reader.read();
        reader.read(); // consume newline
        int actualChar = reader.read();
    }
}
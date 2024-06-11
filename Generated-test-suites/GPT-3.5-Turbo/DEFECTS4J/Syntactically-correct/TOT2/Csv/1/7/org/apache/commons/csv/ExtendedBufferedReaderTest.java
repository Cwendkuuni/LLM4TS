import static org.junit.Assert.*;
import org.junit.Test;
import java.io.StringReader;

public class ExtendedBufferedReaderTest {

    @Test
    public void testRead() {
        StringReader reader = new StringReader("Test\nString\n");
        ExtendedBufferedReader bufferedReader = new ExtendedBufferedReader(reader);

        try {
            assertEquals('T', bufferedReader.read());
            assertEquals('e', bufferedReader.read());
        } catch (Exception e) {
            fail("Exception thrown");
        }
    }

    @Test
    public void testReadAgain() {
        StringReader reader = new StringReader("Test\nString\n");
        ExtendedBufferedReader bufferedReader = new ExtendedBufferedReader(reader);

        try {
            bufferedReader.read();
            bufferedReader.read();
            assertEquals('s', bufferedReader.readAgain());
        } catch (Exception e) {
            fail("Exception thrown");
        }
    }

    @Test
    public void testReadLine() {
        StringReader reader = new StringReader("Test\nString\n");
        ExtendedBufferedReader bufferedReader = new ExtendedBufferedReader(reader);

        try {
            assertEquals("Test", bufferedReader.readLine());
            assertEquals("String", bufferedReader.readLine());
        } catch (Exception e) {
            fail("Exception thrown");
        }
    }

    @Test
    public void testLookAhead() {
        StringReader reader = new StringReader("Test\nString\n");
        ExtendedBufferedReader bufferedReader = new ExtendedBufferedReader(reader);

        try {
            assertEquals('S', bufferedReader.lookAhead());
        } catch (Exception e) {
            fail("Exception thrown");
        }
    }

    @Test
    public void testGetLineNumber() {
        StringReader reader = new StringReader("Test\nString\n");
        ExtendedBufferedReader bufferedReader = new ExtendedBufferedReader(reader);

        try {
            bufferedReader.readLine();
            bufferedReader.readLine();
            assertEquals(2, bufferedReader.getLineNumber());
        } catch (Exception e) {
            fail("Exception thrown");
        }
    }

}
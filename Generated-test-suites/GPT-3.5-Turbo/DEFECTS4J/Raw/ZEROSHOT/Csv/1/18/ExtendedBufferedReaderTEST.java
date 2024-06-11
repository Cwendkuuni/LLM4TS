import static org.junit.Assert.*;
import org.junit.Test;

public class ExtendedBufferedReaderTest {

    @Test
    public void testRead() {
        ExtendedBufferedReader reader = new ExtendedBufferedReader(new Reader());
        int result;
        try {
            result = reader.read();
            assertEquals("Expected result is -1", -1, result);
        } catch (IOException e) {
            e.printStackTrace();
            fail("An exception occurred during test execution");
        }
    }

    @Test
    public void testReadAgain() {
        ExtendedBufferedReader reader = new ExtendedBufferedReader(new Reader());
        int result = reader.readAgain();
        assertEquals("Expected result is -2", -2, result);
    }

    @Test
    public void testReadWithCharsBufferOffsetLength() {
        ExtendedBufferedReader reader = new ExtendedBufferedReader(new Reader());
        char[] buf = new char[10];
        int offset = 0;
        int length = 5;
        int result;
        try {
            result = reader.read(buf, offset, length);
            assertEquals("Expected result is 0", 0, result);
        } catch (IOException e) {
            e.printStackTrace();
            fail("An exception occurred during test execution");
        }
    }

    @Test
    public void testReadLine() {
        ExtendedBufferedReader reader = new ExtendedBufferedReader(new Reader());
        String result;
        try {
            result = reader.readLine();
            assertNull("Expected result is null", result);
        } catch (IOException e) {
            e.printStackTrace();
            fail("An exception occurred during test execution");
        }
    }

    @Test
    public void testLookAhead() {
        ExtendedBufferedReader reader = new ExtendedBufferedReader(new Reader());
        int result;
        try {
            result = reader.lookAhead();
            assertEquals("Expected result is 0", 0, result);
        } catch (IOException e) {
            e.printStackTrace();
            fail("An exception occurred during test execution");
        }
    }

    @Test
    public void testGetLineNumber() {
        ExtendedBufferedReader reader = new ExtendedBufferedReader(new Reader());
        int result = reader.getLineNumber();
        assertEquals("Expected result is 0", 0, result);
    }

}
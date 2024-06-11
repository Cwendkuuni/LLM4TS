import org.junit.Test;
import static org.junit.Assert.*;

public class ExtendedBufferedReaderTest {

    @Test
    public void testRead() {
        // Test when input is '\n'
        ExtendedBufferedReader reader = new ExtendedBufferedReader();
        reader.read();
        assertEquals(1, reader.getLineNumber());

        // Test when input is not '\n'
        reader.read();
        assertEquals(1, reader.getLineNumber());

        // Test when input is '\r'
        reader.read();
        assertEquals(2, reader.getLineNumber());

        // Test when input is not '\r'
        reader.read();
        assertEquals(2, reader.getLineNumber());

        // Test when end of stream is reached
        reader.read();
        assertEquals(-1, reader.readAgain());
    }

    @Test
    public void testReadCharArrayIntInt() {
        // Test when length is 0
        ExtendedBufferedReader reader = new ExtendedBufferedReader();
        char[] buf = new char[0];
        int len = reader.read(buf, 0, 0);
        assertEquals(0, len);

        // Test when length is positive
        buf = new char[5];
        len = reader.read(buf, 0, 5);
        assertEquals(5, len);
        assertEquals('\u0000', buf[0]);

        // Test when length is greater than available characters
        buf = new char[10];
        len = reader.read(buf, 0, 10);
        assertEquals(5, len);
        assertEquals('\u0000', buf[0]);

        // Test when end of stream is reached
        buf = new char[5];
        len = reader.read(buf, 0, 5);
        assertEquals(-1, len);
        assertEquals(-1, reader.readAgain());
    }

    @Test
    public void testReadLine() {
        // Test when line is not null and length is greater than 0
        ExtendedBufferedReader reader = new ExtendedBufferedReader();
        String line = reader.readLine();
        assertEquals(1, reader.getLineNumber());

        // Test when line is null
        reader.readLine();
        assertEquals(2, reader.getLineNumber());

        // Test when end of stream is reached
        assertNull(reader.readLine());
        assertEquals(-1, reader.readAgain());
    }

    @Test
    public void testLookAhead() {
        // Test when mark and reset are successful
        ExtendedBufferedReader reader = new ExtendedBufferedReader();
        try {
            reader.lookAhead();
            assertEquals(1, reader.getLineNumber());
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Test when mark and reset are unsuccessful
        try {
            reader.lookAhead();
        } catch (IOException e) {
            e.printStackTrace();
        }
        assertEquals(1, reader.getLineNumber());
    }

    @Test
    public void testGetLineNumber() {
        ExtendedBufferedReader reader = new ExtendedBufferedReader();
        int line = reader.getLineNumber();
        assertEquals(0, line);
    }
}
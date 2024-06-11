import org.junit.Test;
import static org.junit.Assert.*;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

public class IOUtilsTest {

    @Test
    public void testCopy() throws Exception {
        byte[] inputBytes = {1, 2, 3, 4, 5};
        byte[] outputBytes = new byte[5];
        InputStream input = new ByteArrayInputStream(inputBytes);
        OutputStream output = new ByteArrayOutputStream();

        long count = IOUtils.copy(input, output);

        assertArrayEquals(inputBytes, output.toByteArray());
        assertEquals(inputBytes.length, count);
        input.close();
        output.close();
    }

    @Test
    public void testCopyWithBufferSize() throws Exception {
        byte[] inputBytes = {1, 2, 3, 4, 5};
        byte[] outputBytes = new byte[5];
        InputStream input = new ByteArrayInputStream(inputBytes);
        OutputStream output = new ByteArrayOutputStream();

        long count = IOUtils.copy(input, output, 10);

        assertArrayEquals(inputBytes, output.toByteArray());
        assertEquals(inputBytes.length, count);
        input.close();
        output.close();
    }

    @Test
    public void testSkip() throws Exception {
        byte[] inputBytes = {1, 2, 3, 4, 5};
        InputStream input = new ByteArrayInputStream(inputBytes);

        long skipped = IOUtils.skip(input, 3);

        assertEquals(3, skipped);
        input.close();
    }

    @Test
    public void testSkipWithZeroSkip() throws Exception {
        byte[] inputBytes = {1, 2, 3, 4, 5};
        InputStream input = new ByteArrayInputStream(inputBytes);

        long skipped = IOUtils.skip(input, 0);

        assertEquals(0, skipped);
        input.close();
    }

    @Test
    public void testSkipWithEndOfStream() throws Exception {
        byte[] inputBytes = {1, 2, 3, 4, 5};
        InputStream input = new ByteArrayInputStream(inputBytes);

        long skipped = IOUtils.skip(input, 10);

        assertEquals(5, skipped);
        input.close();
    }

    @Test
    public void testReadFully() throws Exception {
        byte[] inputBytes = {1, 2, 3, 4, 5};
        byte[] outputBytes = new byte[5];
        InputStream input = new ByteArrayInputStream(inputBytes);

        int count = IOUtils.readFully(input, outputBytes);

        assertArrayEquals(inputBytes, outputBytes);
        assertEquals(5, count);
        input.close();
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testReadFullyWithNegativeLength() throws Exception {
        byte[] inputBytes = {1, 2, 3, 4, 5};
        byte[] outputBytes = new byte[5];
        InputStream input = new ByteArrayInputStream(inputBytes);

        IOUtils.readFully(input, outputBytes, 0, -1);

        input.close();
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testReadFullyWithNegativeOffset() throws Exception {
        byte[] inputBytes = {1, 2, 3, 4, 5};
        byte[] outputBytes = new byte[5];
        InputStream input = new ByteArrayInputStream(inputBytes);

        IOUtils.readFully(input, outputBytes, -1, 5);

        input.close();
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testReadFullyWithOffsetAndLengthExceedingBuffer() throws Exception {
        byte[] inputBytes = {1, 2, 3, 4, 5};
        byte[] outputBytes = new byte[5];
        InputStream input = new ByteArrayInputStream(inputBytes);

        IOUtils.readFully(input, outputBytes, 1, 5);

        input.close();
    }

    @Test
    public void testToByteArray() throws Exception {
        byte[] inputBytes = {1, 2, 3, 4, 5};
        InputStream input = new ByteArrayInputStream(inputBytes);

        byte[] outputBytes = IOUtils.toByteArray(input);

        assertArrayEquals(inputBytes, outputBytes);
        input.close();
    }

    @Test
    public void testCloseQuietly() throws Exception {
        InputStream input = new ByteArrayInputStream(new byte[0]);
        IOUtils.closeQuietly(input); // Should not throw an exception
    }
}
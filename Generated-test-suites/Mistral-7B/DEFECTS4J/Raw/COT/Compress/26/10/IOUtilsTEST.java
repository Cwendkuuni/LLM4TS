import org.apache.commons.compress.utils.IOUtils;
import org.junit.Test;
import static org.junit.Assert.*;

public class IOUtilsTest {

    @Test
    public void testCopy() throws IOException {
        byte[] inputData = new byte[]{(byte) 0x01, (byte) 0x02, (byte) 0x03, (byte) 0x04, (byte) 0x05};
        byte[] expectedOutput = new byte[]{(byte) 0x01, (byte) 0x02, (byte) 0x03, (byte) 0x04, (byte) 0x05};
        byte[] outputData = new byte[5];

        long copiedBytes = IOUtils.copy(new ByteArrayInputStream(inputData), new ByteArrayOutputStream(5), IOUtils.COPY_BUF_SIZE);
        byte[] result = new ByteArrayOutputStream(5).toByteArray();

        assertEquals(copiedBytes, 5);
        assertArrayEquals(expectedOutput, result);
    }

    @Test(expected = IOException.class)
    public void testCopyWithException() throws IOException {
        InputStream input = new ByteArrayInputStream(new byte[]{(byte) 0x01, (byte) 0x02, (byte) 0x03, (byte) 0x04, (byte) 0x05});
        OutputStream output = new ByteArrayOutputStream(5);

        IOUtils.copy(input, output); // No exception should be thrown here

        input.close(); // Exception should be thrown here
        IOUtils.copy(input, output);
    }

    @Test
    public void testSkip() throws IOException {
        byte[] inputData = new byte[]{(byte) 0x01, (byte) 0x02, (byte) 0x03, (byte) 0x04, (byte) 0x05};
        InputStream input = new ByteArrayInputStream(inputData);

        long numToSkip = 2;
        long skippedBytes = IOUtils.skip(input, numToSkip);

        assertEquals(3, skippedBytes);
        assertEquals(3, input.available());
    }

    @Test
    public void testReadFully() throws IOException {
        byte[] inputData = new byte[]{(byte) 0x01, (byte) 0x02, (byte) 0x03, (byte) 0x04, (byte) 0x05};
        InputStream input = new ByteArrayInputStream(inputData);
        byte[] output = new byte[5];

        int readBytes = IOUtils.readFully(input, output);

        assertEquals(5, readBytes);
        assertArrayEquals(inputData, output);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testReadFullyWithInvalidOffsetOrLength() throws IOException {
        byte[] inputData = new byte[]{(byte) 0x01, (byte) 0x02, (byte) 0x03, (byte) 0x04, (byte) 0x05};
        InputStream input = new ByteArrayInputStream(inputData);
        byte[] output = new byte[5];

        IOUtils.readFully(input, output, -1, 5);
    }

    @Test
    public void testToByteArray() throws IOException {
        byte[] inputData = new byte[]{(byte) 0x01, (byte) 0x02, (byte) 0x03, (byte) 0x04, (byte) 0x05};
        InputStream input = new ByteArrayInputStream(inputData);

        byte[] output = IOUtils.toByteArray(input);

        assertArrayEquals(inputData, output);
    }

    @Test
    public void testCloseQuietly() {
        ByteArrayInputStream input = new ByteArrayInputStream(new byte[]{(byte) 0x01});

        IOUtils.closeQuietly(input);

        assertFalse(input.isOpen());
    }
}
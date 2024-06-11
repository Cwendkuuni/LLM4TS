import org.apache.commons.compress.utils.IOUtils;
import org.junit.Test;
import static org.junit.Assert.*;

public class IOUtilsTest {

    @Test
    public void testCopy() throws IOException {
        InputStream input = getClass().getResourceAsStream("/testData.txt");
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        long actual = IOUtils.copy(input, output);
        long expected = input.available();
        assertEquals("Unexpected number of bytes copied.", expected, actual);
        assertArrayEquals("Copy failed.", output.toByteArray(), new byte[expected]);
    }

    @Test
    public void testCopyWithBufferSize() throws IOException {
        InputStream input = getClass().getResourceAsStream("/testData.txt");
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        int bufferSize = 1024;
        long actual = IOUtils.copy(input, output, bufferSize);
        long expected = input.available();
        assertEquals("Unexpected number of bytes copied.", expected, actual);
        assertArrayEquals("Copy failed.", output.toByteArray(), new byte[expected]);
    }

    @Test(expected = IOException.class)
    public void testCopyWithNullOutput() throws IOException {
        InputStream input = getClass().getResourceAsStream("/testData.txt");
        IOUtils.copy(input, null);
    }

    @Test
    public void testSkip() throws IOException {
        InputStream input = new ByteArrayInputStream(new byte[]{(byte) 0x01, (byte) 0x02, (byte) 0x03, (byte) 0x04});
        long numToSkip = 2;
        long actual = IOUtils.skip(input, numToSkip);
        long expected = numToSkip;
        assertEquals("Unexpected number of bytes skipped.", expected, actual);

        byte[] remainingData = new byte[input.available()];
        input.read(remainingData);
        byte[] expectedData = new byte[]{(byte) 0x03, (byte) 0x04};
        assertArrayEquals("Unexpected remaining data.", expectedData, remainingData);
    }

    @Test
    public void testSkipWithEndOfStream() throws IOException {
        InputStream input = new ByteArrayInputStream(new byte[]{(byte) 0x01, (byte) 0x02, (byte) 0x03});
        long numToSkip = 4;
        long actual = IOUtils.skip(input, numToSkip);
        long expected = input.available();
        assertEquals("Unexpected number of bytes skipped.", expected, actual);
        byte[] remainingData = new byte[input.available()];
        input.read(remainingData);
        assertArrayEquals("Unexpected remaining data.", new byte[0], remainingData);
    }

    @Test(expected = IOException.class)
    public void testSkipWithNullInput() throws IOException {
        IOUtils.skip(null, 1L);
    }

    @Test
    public void testReadFully() throws IOException {
        InputStream input = getClass().getResourceAsStream("/testData.txt");
        byte[] buffer = new byte[input.available()];
        int actual = IOUtils.readFully(input, buffer);
        int expected = input.available();
        assertEquals("Unexpected number of bytes read.", expected, actual);
        assertArrayEquals("Unexpected data.", new byte[expected], buffer);
    }

    @Test
    public void testReadFullyWithEndOfStream() throws IOException {
        InputStream input = getClass().getResourceAsStream("/testData.txt");
        byte[] buffer = new byte[input.available() + 1];
        int actual = IOUtils.readFully(input, buffer);
        int expected = input.available();
        assertEquals("Unexpected number of bytes read.", expected, actual);
        assertEquals("Unexpected remaining data.", 0, buffer.length - expected);
    }

    @Test(expected = IOException.class)
    public void testReadFullyWithNullInput() throws IOException {
        IOUtils.readFully(null, new byte[0]);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testReadFullyWithInvalidBuffer() throws IOException {
        InputStream input = getClass().getResourceAsStream("/testData.txt");
        byte[] buffer = new byte[input.available() - 1];
        IOUtils.readFully(input, buffer);
    }

    @Test
    public void testToByteArray() throws IOException {
        InputStream input = getClass().getResourceAsStream("/testData.txt");
        byte[] actual = IOUtils.toByteArray(input);
        byte[] expected = new byte[input.available()];
        input.read(expected);
        assertArrayEquals("Unexpected byte array.", expected, actual);
    }

    @Test
    public void testToByteArrayWithEndOfStream() throws IOException {
        InputStream input = getClass().getResourceAsStream("/testData.txt");
        byte[] actual = IOUtils.toByteArray(input);
        byte[] expected = new byte[input.available()];
        input.read(expected);
        assertEquals("Unexpected byte array length.", expected.length, actual.length);
        assertArrayEquals("Unexpected byte array.", expected, actual);
    }

    @Test(expected = IOException.class)
    public void testToByteArrayWithNullInput() throws IOException {
        IOUtils.toByteArray(null);
    }

    @Test
    public void testCloseQuietly() throws IOException {
        InputStream input = getClass().getResourceAsStream("/testData.txt");
        IOUtils.closeQuietly(input);
        assertFalse("Expected the InputStream to be closed.", input.isOpen());
    }

    @Test
    public void testCloseQuietlyWithNullCloseable() throws IOException {
        IOUtils.closeQuietly(null);
    }
}
import org.junit.Before;
import org.junit.Test;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.zip.Adler32;
import java.util.zip.Checksum;
import static org.junit.Assert.*;

public class ChecksumCalculatingInputStreamTest {

    private ChecksumCalculatingInputStream inputStream;
    private Checksum checksum;

    @Before
    public void setUp() {
        byte[] testData = "This is a test".getBytes();
        ByteArrayInputStream in = new ByteArrayInputStream(testData);
        checksum = new Adler32();
        inputStream = new ChecksumCalculatingInputStream(checksum, in);
    }

    @Test
    public void testReadSingleByte() throws IOException {
        int expectedByte = 'T';
        int actualByte = inputStream.read();
        assertEquals(expectedByte, actualByte);
        assertEquals(expectedByte, checksum.getValue());
    }

    @Test
    public void testReadByteArray() throws IOException {
        byte[] expectedBytes = "This".getBytes();
        byte[] actualBytes = new byte[4];
        int bytesRead = inputStream.read(actualBytes);
        assertArrayEquals(expectedBytes, actualBytes);
        assertEquals(expectedBytes.length, bytesRead);
        assertEquals(checksum.getValue(), inputStream.getValue());
    }

    @Test
    public void testReadWithOffsetAndLength() throws IOException {
        byte[] expectedBytes = "This".getBytes();
        byte[] actualBytes = new byte[10];
        int bytesRead = inputStream.read(actualBytes, 2, 4);
        assertEquals(expectedBytes.length, bytesRead);
        assertEquals((byte) 0, actualBytes[0]);
        assertEquals((byte) 0, actualBytes[1]);
        assertArrayEquals(expectedBytes, java.util.Arrays.copyOfRange(actualBytes, 2, 6));
        assertEquals(checksum.getValue(), inputStream.getValue());
    }

    @Test
    public void testSkip() throws IOException {
        long expectedSkippedBytes = 5;
        long actualSkippedBytes = inputStream.skip(5);
        assertEquals(expectedSkippedBytes, actualSkippedBytes);
    }

    @Test
    public void testGetValue() {
        long expectedValue = 0L;
        assertEquals(expectedValue, inputStream.getValue());
    }
}
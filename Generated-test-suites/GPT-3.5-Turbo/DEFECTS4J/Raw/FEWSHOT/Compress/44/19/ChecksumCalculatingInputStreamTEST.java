import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertArrayEquals;

import org.junit.Before;
import org.junit.Test;
import org.apache.commons.compress.utils.ChecksumCalculatingInputStream;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.zip.CRC32;
import java.util.zip.Checksum;

public class ChecksumCalculatingInputStreamTest {
    private Checksum checksum;

    @Before
    public void setUp() {
        checksum = new CRC32();
    }

    @Test
    public void testRead() throws IOException {
        byte[] inputData = {1, 2, 3, 4, 5};
        ChecksumCalculatingInputStream input = new ChecksumCalculatingInputStream(checksum, new ByteArrayInputStream(inputData));

        assertEquals(1, input.read());
        assertEquals(2, input.read());
        assertEquals(3, input.read());
        assertEquals(4, input.read());
        assertEquals(5, input.read());
        assertEquals(-1, input.read());
        assertEquals(3251539490L, input.getValue());
    }

    @Test
    public void testReadByteArray() throws IOException {
        byte[] inputData = {1, 2, 3, 4, 5};
        byte[] expectedData = {1, 2, 3, 4, 5};
        ChecksumCalculatingInputStream input = new ChecksumCalculatingInputStream(checksum, new ByteArrayInputStream(inputData));
        byte[] buffer = new byte[5];

        int read = input.read(buffer);

        assertEquals(5, read);
        assertArrayEquals(expectedData, buffer);
        assertEquals(3251539490L, input.getValue());
    }

    @Test
    public void testReadByteArrayWithOffset() throws IOException {
        byte[] inputData = {1, 2, 3, 4, 5};
        byte[] expectedData = {0, 0, 3, 4, 5};
        ChecksumCalculatingInputStream input = new ChecksumCalculatingInputStream(checksum, new ByteArrayInputStream(inputData));
        byte[] buffer = new byte[5];

        int read = input.read(buffer, 2, 3);

        assertEquals(3, read);
        assertArrayEquals(expectedData, buffer);
        assertEquals(3251539490L, input.getValue());
    }

    @Test
    public void testSkip() throws IOException {
        byte[] inputData = {1, 2, 3, 4, 5};
        ChecksumCalculatingInputStream input = new ChecksumCalculatingInputStream(checksum, new ByteArrayInputStream(inputData));

        long skipped = input.skip(3);

        assertEquals(3, skipped);
        assertEquals(4, input.read());
        assertEquals(5, input.read());
        assertEquals(-1, input.read());
        assertEquals(3251539490L, input.getValue());
    }

    @Test
    public void testGetValue() {
        ChecksumCalculatingInputStream input = new ChecksumCalculatingInputStream(checksum, new ByteArrayInputStream(new byte[0]));

        assertEquals(0, input.getValue());
    }
}
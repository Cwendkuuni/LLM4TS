import static org.junit.Assert.*;
import org.junit.Test;
import java.io.ByteArrayInputStream;
import java.io.IOException;

public class BaseNCodecInputStreamTest {

    // Test data
    private static final byte[] TEST_BYTES = {65, 66, 67, 68}; // ASCII values for A, B, C, D

    @Test
    public void testRead() throws IOException {
        ByteArrayInputStream bais = new ByteArrayInputStream(TEST_BYTES);
        BaseNCodec baseNCodec = new BaseNCodec();
        BaseNCodecInputStream inputStream = new BaseNCodecInputStream(bais, baseNCodec, true);

        int result = inputStream.read();
        assertEquals(65, result); // A is encoded to 65
    }

    @Test
    public void testReadWithEmptyInput() throws IOException {
        ByteArrayInputStream bais = new ByteArrayInputStream(new byte[0]);
        BaseNCodec baseNCodec = new BaseNCodec();
        BaseNCodecInputStream inputStream = new BaseNCodecInputStream(bais, baseNCodec, true);

        int result = inputStream.read();
        assertEquals(-1, result); // EOF when input is empty
    }

    @Test
    public void testReadBytes() throws IOException {
        ByteArrayInputStream bais = new ByteArrayInputStream(TEST_BYTES);
        BaseNCodec baseNCodec = new BaseNCodec();
        BaseNCodecInputStream inputStream = new BaseNCodecInputStream(bais, baseNCodec, false);

        byte[] buffer = new byte[2];
        int result = inputStream.read(buffer, 0, 2);

        assertEquals(2, result); // Read 2 bytes
        assertArrayEquals(new byte[]{65, 66}, buffer); // Check if correct bytes were read
    }

    @Test(expected = NullPointerException.class)
    public void testReadWithNullBuffer() throws IOException {
        ByteArrayInputStream bais = new ByteArrayInputStream(TEST_BYTES);
        BaseNCodec baseNCodec = new BaseNCodec();
        BaseNCodecInputStream inputStream = new BaseNCodecInputStream(bais, baseNCodec, true);

        inputStream.read(null, 0, 2); // Should throw NullPointerException
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testReadWithInvalidOffset() throws IOException {
        ByteArrayInputStream bais = new ByteArrayInputStream(TEST_BYTES);
        BaseNCodec baseNCodec = new BaseNCodec();
        BaseNCodecInputStream inputStream = new BaseNCodecInputStream(bais, baseNCodec, true);

        byte[] buffer = new byte[2];
        inputStream.read(buffer, -1, 2); // Should throw IndexOutOfBoundsException
    }

    @Test
    public void testMarkSupported() {
        ByteArrayInputStream bais = new ByteArrayInputStream(TEST_BYTES);
        BaseNCodec baseNCodec = new BaseNCodec();
        BaseNCodecInputStream inputStream = new BaseNCodecInputStream(bais, baseNCodec, false);

        assertFalse(inputStream.markSupported()); // Mark is not supported
    }

    @Test
    public void testAvailable() throws IOException {
        ByteArrayInputStream bais = new ByteArrayInputStream(TEST_BYTES);
        BaseNCodec baseNCodec = new BaseNCodec();
        BaseNCodecInputStream inputStream = new BaseNCodecInputStream(bais, baseNCodec, true);

        int result = inputStream.available();

        assertEquals(1, result); // Data available
    }
}
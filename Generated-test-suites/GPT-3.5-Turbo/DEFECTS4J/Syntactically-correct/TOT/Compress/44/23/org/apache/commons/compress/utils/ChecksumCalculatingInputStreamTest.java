import static org.junit.Assert.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import java.io.*;

public class ChecksumCalculatingInputStreamTest {

    private ByteArrayInputStream input;
    private ChecksumCalculatingInputStream checksumInput;

    @Before
    public void setUp() {
        byte[] data = {1, 2, 3, 4, 5};
        input = new ByteArrayInputStream(data);
        checksumInput = new ChecksumCalculatingInputStream(new CRC32(), input);
    }

    @After
    public void tearDown() {
        input.close();
    }

    @Test
    public void testReadSingleByte() throws IOException {
        assertEquals(1, checksumInput.read());
        assertEquals(2, checksumInput.read());
        assertEquals(3, checksumInput.read());
        assertEquals(4, checksumInput.read());
        assertEquals(5, checksumInput.read());

        assertEquals(15, checksumInput.getValue());
    }

    @Test
    public void testReadByteArray() throws IOException {
        byte[] buffer = new byte[4];
        assertEquals(4, checksumInput.read(buffer));
        assertArrayEquals(new byte[]{1, 2, 3, 4}, buffer);

        assertEquals(1, checksumInput.read(buffer));
        assertArrayEquals(new byte[]{5, 2, 3, 4}, buffer);

        assertEquals(-1, checksumInput.read(buffer));
        assertArrayEquals(new byte[]{5, 2, 3, 4}, buffer);

        assertEquals(15, checksumInput.getValue());
    }

    @Test
    public void testReadByteArrayWithOffsetAndLength() throws IOException {
        byte[] buffer = new byte[6];
        assertEquals(4, checksumInput.read(buffer, 1, 4));
        assertArrayEquals(new byte[]{0, 1, 2, 3, 0, 0}, buffer);

        assertEquals(1, checksumInput.read(buffer, 5, 1));
        assertArrayEquals(new byte[]{0, 1, 2, 3, 0, 5}, buffer);

        assertEquals(-1, checksumInput.read(buffer, 0, 1));
        assertArrayEquals(new byte[]{0, 1, 2, 3, 0, 5}, buffer);

        assertEquals(15, checksumInput.getValue());
    }

    @Test
    public void testSkip() throws IOException {
        assertEquals(1, checksumInput.skip(1));
        assertEquals(2, checksumInput.read());

        assertEquals(1, checksumInput.skip(1));
        assertEquals(3, checksumInput.read());

        assertEquals(1, checksumInput.skip(1));
        assertEquals(4, checksumInput.read());

        assertEquals(1, checksumInput.skip(1));
        assertEquals(5, checksumInput.read());

        assertEquals(0, checksumInput.skip(1));
        assertEquals(-1, checksumInput.read());

        assertEquals(15, checksumInput.getValue());
    }

}
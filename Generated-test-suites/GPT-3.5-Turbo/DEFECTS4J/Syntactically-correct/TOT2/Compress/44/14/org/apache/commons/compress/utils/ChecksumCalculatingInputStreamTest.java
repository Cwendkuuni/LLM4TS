import static org.junit.Assert.*;
import org.junit.Test;
import org.junit.Before;
import org.junit.After;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.zip.CRC32;

public class ChecksumCalculatingInputStreamTest {
    private ChecksumCalculatingInputStream testStream;

    @Before
    public void setUp() {
        testStream = new ChecksumCalculatingInputStream(new CRC32(), new ByteArrayInputStream(new byte[] {1, 2, 3}));
    }

    @After
    public void tearDown() {
        try {
            testStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testReadSingleByte() throws IOException {
        assertEquals(1, testStream.read());
    }

    @Test
    public void testReadByteArray() throws IOException {
        byte[] buffer = new byte[3];
        assertEquals(3, testStream.read(buffer));
        assertArrayEquals(new byte[] {1, 2, 3}, buffer);
    }

    @Test
    public void testReadPartialByteArray() throws IOException {
        byte[] buffer = new byte[5];
        assertEquals(3, testStream.read(buffer, 0, 5));
        assertArrayEquals(new byte[] {1, 2, 3, 0, 0}, buffer);
    }

    @Test
    public void testSkip() throws IOException {
        assertEquals(1, testStream.skip(1));
    }

    @Test
    public void testGetValue() {
        assertEquals(1317871234, testStream.getValue());
    }

    @Test(expected = IOException.class)
    public void testReadException() throws IOException {
        ChecksumCalculatingInputStream testExceptionStream = new ChecksumCalculatingInputStream(new CRC32(), new ByteArrayInputStream(new byte[] {}));
        testExceptionStream.read(); // This should throw an exception since the stream is empty
    }

    @Test(expected = IOException.class)
    public void testReadByteArrayException() throws IOException {
        byte[] buffer = new byte[4];
        testStream.read(buffer); // This should throw an exception since the stream is exhausted
    }
}

import static org.junit.Assert.*;
import org.junit.Test;
import org.apache.commons.compress.utils.ChecksumCalculatingInputStream;

public class ChecksumCalculatingInputStreamTest {

    @Test
    public void testRead_singleByte() throws Exception {
        ChecksumCalculatingInputStream stream = new ChecksumCalculatingInputStream(checkSum(), inputStream());
        assertEquals(-1, stream.read());
        assertEquals(0, stream.getValue());
    }

    @Test
    public void testRead_byteArray() throws Exception {
        byte[] byteArray = new byte[10];
        ChecksumCalculatingInputStream stream = new ChecksumCalculatingInputStream(checkSum(), inputStream());
        assertEquals(10, stream.read(byteArray));
        assertEquals(0, stream.getValue());
    }

    @Test
    public void testRead_byteArray_withOffsetAndLength() throws Exception {
        byte[] byteArray = new byte[10];
        ChecksumCalculatingInputStream stream = new ChecksumCalculatingInputStream(checkSum(), inputStream());
        assertEquals(5, stream.read(byteArray, 2, 5));
        assertEquals(0, stream.getValue());
    }

    @Test
    public void testSkip() throws Exception {
        ChecksumCalculatingInputStream stream = new ChecksumCalculatingInputStream(checkSum(), inputStream());
        assertEquals(1, stream.skip(1));
        assertEquals(0, stream.getValue());
    }

    private Checksum checkSum() {
        // Provide your implementation of Checksum here
        return null;
    }

    private InputStream inputStream() {
        // Provide your implementation of InputStream here
        return null;
    }
}


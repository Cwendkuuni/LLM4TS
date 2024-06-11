import org.apache.commons.compress.utils.ChecksumCalculatingInputStream;
import org.apache.commons.compress.utils.Adler32;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.apache.commons.io.IOUtils.*;

public class ChecksumCalculatingInputStreamTest {

    @Test
    public void testRead() throws IOException {
        // Test case 1
        // ...
    }

    @Test
    public void testReadByteArray() throws IOException {
        // Test case 2
        // ...
    }

    @Test
    public void testReadByteArrayWithOffsetAndLength() throws IOException {
        // Test case 3
        // ...
    }

    @Test
    public void testSkip() throws IOException {
        // Test case 4
        // ...
    }

    @Test(expected = NullPointerException.class)
    public void testNullInputStream() throws IOException {
        // Test case 5: InputStream is null
        final Checksum checksum = new Adler32();
        final ChecksumCalculatingInputStream inputStream = new ChecksumCalculatingInputStream(checksum, null);
        inputStream.read();
    }

    @Test(expected = NullPointerException.class)
    public void testNullByteArray() throws IOException {
        // Test case 6: byte[] is null
        final InputStream input = new ByteArrayInputStream(new byte[] { 1 });
        final Checksum checksum = new Adler32();
        final ChecksumCalculatingInputStream inputStream = new ChecksumCalculatingInputStream(checksum, input);
        inputStream.read((byte[]) null);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testNegativeOffset() throws IOException {
        // Test case 7: Negative offset
        final InputStream input = new ByteArrayInputStream(new byte[] { 1, 2, 3 });
        final Checksum checksum = new Adler32();
        final ChecksumCalculatingInputStream inputStream = new ChecksumCalculatingInputStream(checksum, input);
        inputStream.read(new byte[] { 0 }, -1, 1);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testNegativeLength() throws IOException {
        // Test case 8: Negative length
        final InputStream input = new ByteArrayInputStream(new byte[] { 1, 2, 3 });
        final Checksum checksum = new Adler32();
        final ChecksumCalculatingInputStream inputStream = new ChecksumCalculatingInputStream(checksum, input);
        inputStream.read(new byte[] { 0 }, 0, -1);
    }

    @Test
    public void testGetValue() throws IOException {
        // Test case 9: Checksum calculation
        final InputStream input = new ByteArrayInputStream(new byte[] { 1, 2, 3, 4, 5 });
        final Checksum checksum = new Adler32();
        final ChecksumCalculatingInputStream inputStream = new ChecksumCalculatingInputStream(checksum, input);
        final long expectedChecksum = 1536161690557427699L;
        final long actualChecksum = inputStream.getValue();
        assertEquals(expectedChecksum, actualChecksum);
    }

    @Test(expected = IOException.class)
    public void testIOException() throws IOException {
        // Test case 10: InputStream throws IOException
        final InputStream input = new ByteArrayInputStream(new byte[] { 1 }) {
            @Override
            public int read() throws IOException {
                throw new IOException("Test IOException");
            }
        };
        final Checksum checksum = new Adler32();
        final ChecksumCalculatingInputStream inputStream = new ChecksumCalculatingInputStream(checksum, input);
        inputStream.read();
    }

    @Test
    public void testEndOfStream() throws IOException {
        // Test case 11: End of stream
        final InputStream input = new ByteArrayInputStream(new byte[] { 1 });
        final Checksum checksum = new Adler32();
        final ChecksumCalculatingInputStream inputStream = new ChecksumCalculatingInputStream(checksum, input);
        input.close();
        inputStream.read();
    }

}
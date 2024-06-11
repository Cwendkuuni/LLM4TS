import org.apache.commons.compress.utils.ChecksumCalculatingInputStream;
import org.apache.commons.compress.utils.Checksums;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

/**
 * Test for {@link ChecksumCalculatingInputStream} class.
 */
public class ChecksumCalculatingInputStreamTest {

    private InputStream inputStream;
    private ChecksumCalculatingInputStream checksumStream;
    private long expectedChecksum;

    @Before
    public void setUp() throws IOException, NoSuchAlgorithmException {
        byte[] inputData = "Test data for ChecksumCalculatingInputStream".getBytes();
        MessageDigest md = MessageDigest.getInstance("MD5");
        md.update(inputData);
        expectedChecksum = md.digest()[0] & 0xFFL |
                ((md.digest()[1] & 0xFFL) << 8) |
                ((md.digest()[2] & 0xFFL) << 16) |
                ((md.digest()[3] & 0xFFL) << 24);

        inputStream = new ByteArrayInputStream(inputData);
        checksumStream = new ChecksumCalculatingInputStream(Checksums.md5(), inputStream);
    }

    @Test
    public void testReadSingleByte() throws IOException {
        final int readByte = checksumStream.read();
        assertEquals(-1, readByte);
        assertEquals(expectedChecksum, checksumStream.getValue());
    }

    @Test
    public void testReadByteArray() throws IOException {
        final byte[] buffer = new byte[10];
        final int bytesRead = checksumStream.read(buffer);
        assertEquals(inputData.length, bytesRead);
        assertEquals(expectedChecksum, checksumStream.getValue());
    }

    @Test(expected = IOException.class)
    public void testReadPassEmptyByteArray() throws IOException {
        final byte[] emptyBuffer = new byte[0];
        checksumStream.read(emptyBuffer);
    }

    @Test(expected = IOException.class)
    public void testReadPassNullByteArray() throws IOException {
        checksumStream.read(null);
    }

    @Test
    public void testReadPassInvalidChecksum() throws IOException {
        final long invalidChecksum = expectedChecksum ^ 0xFFFFFFFFL;
        ChecksumCalculatingInputStream invalidChecksumInputStream = new ChecksumCalculatingInputStream(new MD5Checksum(), inputStream) {
            @Override
            public long getValue() {
                return invalidChecksum;
            }
        };
        final byte[] buffer = new byte[10];
        final int bytesRead = invalidChecksumInputStream.read(buffer);
        assertNotEquals(inputData.length, bytesRead);
        assertEquals(invalidChecksum, invalidChecksumInputStream.getValue());
    }

    @After
    public void tearDown() throws IOException {
        inputStream.close();
        checksumStream.close();
    }
}
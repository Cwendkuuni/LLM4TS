import org.apache.commons.compress.utils.ChecksumCalculatingInputStream;
import org.apache.commons.compress.utils.Checksums;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class ChecksumCalculatingInputStreamTest {

    @Test
    public void testChecksumCalculatingInputStream() throws Exception {
        byte[] testData = new byte[]{'A', 'B', 'C'};
        InputStream testInputStream = new ByteArrayInputStream(testData);
        Checksum checksum = Checksums.adler32();
        ChecksumCalculatingInputStream checksumCalculatingInputStream = new ChecksumCalculatingInputStream(checksum, testInputStream);
        int dataRead = checksumCalculatingInputStream.read();
        int bytesRead = checksumCalculatingInputStream.read(testData);
        checksumCalculatingInputStream.close();
        long expectedChecksum = checksum.getValue();
        long actualChecksum = checksumCalculatingInputStream.getValue();
        assertEquals(expectedChecksum, actualChecksum);
    }
}
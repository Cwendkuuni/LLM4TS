import org.apache.commons.compress.utils.ChecksumCalculatingInputStream;
import org.apache.commons.compress.utils.Checksums;
import org.junit.Test;
import static org.junit.Assert.*;

public class ChecksumCalculatingInputStreamTest {

    @Test
    public void testChecksumCalculatingInputStream_ReadSingleByte() throws Exception {
        // Given
        final byte testByte = 0x12;
        final InputStream input = new ByteArrayInputStream(new byte[] {testByte});
        final Checksum checksum = Checksums.adler32();
        final ChecksumCalculatingInputStream checksumCalculatingInputStream = new ChecksumCalculatingInputStream(checksum, input);

        // When
        final int readByte = checksumCalculatingInputStream.read();

        // Then
        assertEquals(testByte, readByte);
        assertEquals(testByte, checksum.getValue());
    }

    @Test
    public void testChecksumCalculatingInputStream_ReadByteArray() throws Exception {
        // Given
        final byte[] testArray = new byte[] {0x12, 0x34, 0x56};
        final InputStream input = new ByteArrayInputStream(testArray);
        final Checksum checksum = Checksums.adler32();
        final ChecksumCalculatingInputStream checksumCalculatingInputStream = new ChecksumCalculatingInputStream(checksum, input);

        // When
        final int numBytesRead = checksumCalculatingInputStream.read(testArray);

        // Then
        assertEquals(testArray.length, numBytesRead);
        assertArrayEquals(testArray, checksum.getResult());
    }

    @Test
    public void testChecksumCalculatingInputStream_ReadByteArray_NegativeLength() throws Exception {
        // Given
        final byte[] testArray = new byte[] {0x12, 0x34, 0x56};
        final InputStream input = new ByteArrayInputStream(testArray);
        final Checksum checksum = Checksums.adler32();
        final ChecksumCalculatingInputStream checksumCalculatingInputStream = new ChecksumCalculatingInputStream(checksum, input);

        // When & Then
        try {
            checksumCalculatingInputStream.read(testArray, -1, testArray.length);
            fail("Expected IOException");
        } catch (IOException e) {
            // Expected
        }
    }

    @Test
    public void testChecksumCalculatingInputStream_Skip() throws Exception {
        // Given
        final byte[] testArray = new byte[] {0x12, 0x34, 0x56};
        final InputStream input = new ByteArrayInputStream(testArray);
        final Checksum checksum = Checksums.adler32();
        final ChecksumCalculatingInputStream checksumCalculatingInputStream = new ChecksumCalculatingInputStream(checksum, input);

        // When
        final long numSkippedBytes = checksumCalculatingInputStream.skip(testArray.length);

        // Then
        assertEquals(testArray.length, numSkippedBytes);
        assertEquals(testArray.length, checksum.getValue());
    }

    @Test
    public void testChecksumCalculatingInputStream_GetValue() throws Exception {
        // Given
        final byte[] testArray = new byte[] {0x12, 0x34, 0x56};
        final InputStream input = new ByteArrayInputStream(testArray);
        final Checksum checksum = Checksums.adler32();
        final ChecksumCalculatingInputStream checksumCalculatingInputStream = new ChecksumCalculatingInputStream(checksum, input);

        // When
        final long checksumValue = checksumCalculatingInputStream.getValue();

        // Then
        assertEquals(testArray.length * 255 + 1, checksumValue);
    }

    @Test
    public void testChecksumCalculatingInputStream_IOException() throws Exception {
        // Given
        final InputStream input = new ByteArrayInputStream(new byte[] {0x12, 0x34, 0x56});
        final Checksum checksum = Checksums.adler32();
        final ChecksumCalculatingInputStream checksumCalculatingInputStream = new ChecksumCalculatingInputStream(checksum, input);
        final int numBytesToRead = 10;

        // When
        try {
            checksumCalculatingInputStream.read(new byte[numBytesToRead]);
            fail("Expected IOException");
        } catch (IOException e) {
            // Expected
        }

        // Verify the checksum is correct even after an IOException
        assertEquals(numBytesToRead * 255 + 1, checksum.getValue());
    }
}
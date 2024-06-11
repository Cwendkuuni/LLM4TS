import org.junit.Test;
import org.junit.Assert;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.zip.CRC32;
import java.util.zip.Checksum;

public class ChecksumCalculatingInputStreamTest {

    @Test
    public void testRead_SingleByte() throws IOException {
        Checksum checksum = new CRC32();
        byte[] data = {1, 2, 3};
        ByteArrayInputStream inputStream = new ByteArrayInputStream(data);
        ChecksumCalculatingInputStream stream = new ChecksumCalculatingInputStream(checksum, inputStream);

        int value = stream.read();

        Assert.assertEquals(1, value); // Assert that the correct byte is read
        Assert.assertEquals(checksum.getValue(), 1); // Assert that the checksum is updated correctly
    }

    @Test
    public void testRead_ByteArray() throws IOException {
        Checksum checksum = new CRC32();
        byte[] data = {1, 2, 3};
        byte[] buffer = new byte[2];
        ByteArrayInputStream inputStream = new ByteArrayInputStream(data);
        ChecksumCalculatingInputStream stream = new ChecksumCalculatingInputStream(checksum, inputStream);

        int bytesRead = stream.read(buffer);

        Assert.assertArrayEquals(new byte[]{1, 2}, buffer); // Assert that the correct bytes are read into the buffer
        Assert.assertEquals(2, bytesRead); // Assert that the correct number of bytes are read
        Assert.assertEquals(checksum.getValue(), 3); // Assert that the checksum is updated correctly
    }

    @Test
    public void testRead_ByteArray_Offset_Length() throws IOException {
        Checksum checksum = new CRC32();
        byte[] data = {1, 2, 3};
        byte[] buffer = new byte[3];
        ByteArrayInputStream inputStream = new ByteArrayInputStream(data);
        ChecksumCalculatingInputStream stream = new ChecksumCalculatingInputStream(checksum, inputStream);

        int bytesRead = stream.read(buffer, 1, 2);

        Assert.assertArrayEquals(new byte[]{0, 1, 2}, buffer); // Assert that the correct bytes are read into the buffer at the specified offset and length
        Assert.assertEquals(2, bytesRead); // Assert that the correct number of bytes are read
        Assert.assertEquals(checksum.getValue(), 3); // Assert that the checksum is updated correctly
    }

    @Test
    public void testRead_EmptyInputStream() throws IOException {
        Checksum checksum = new CRC32();
        byte[] buffer = new byte[1];
        ByteArrayInputStream inputStream = new ByteArrayInputStream(new byte[0]);
        ChecksumCalculatingInputStream stream = new ChecksumCalculatingInputStream(checksum, inputStream);

        int bytesRead = stream.read(buffer);

        Assert.assertEquals(-1, bytesRead); // Assert that -1 is returned when the input stream is empty
        Assert.assertEquals(0, checksum.getValue()); // Assert that the checksum remains unchanged when the input stream is empty
    }

    @Test
    public void testSkip() throws IOException {
        Checksum checksum = new CRC32();
        byte[] data = {1, 2, 3};
        ByteArrayInputStream inputStream = new ByteArrayInputStream(data);
        ChecksumCalculatingInputStream stream = new ChecksumCalculatingInputStream(checksum, inputStream);

        long skip = stream.skip(2);

        Assert.assertEquals(2, skip); // Assert that the correct number of bytes are skipped
        Assert.assertEquals(checksum.getValue(), 3); // Assert that the checksum remains unchanged when skip is called
    }

    @Test
    public void testGetValue() throws IOException {
        Checksum checksum = new CRC32();
        byte[] data = {1, 2, 3};
        ByteArrayInputStream inputStream = new ByteArrayInputStream(data);
        ChecksumCalculatingInputStream stream = new ChecksumCalculatingInputStream(checksum, inputStream);

        long value = stream.getValue();

        Assert.assertEquals(0, value); // Assert that the correct initial value of checksum is returned
    }

}
import org.junit.Test;
import org.junit.Assert;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.zip.CRC32;

public class ChecksumCalculatingInputStreamTest {

    @Test
    public void testRead_singleByte() throws IOException {
        byte[] data = {65, 66, 67}; // ASCII values for A, B, C
        ByteArrayInputStream inputStream = new ByteArrayInputStream(data);
        CRC32 checksum = new CRC32();
        ChecksumCalculatingInputStream stream = new ChecksumCalculatingInputStream(checksum, inputStream);

        Assert.assertEquals(65, stream.read());
        Assert.assertEquals(66, stream.read());
        Assert.assertEquals(67, stream.read());
        Assert.assertEquals(-1, stream.read());
        Assert.assertEquals(891568578, checksum.getValue());
    }

    @Test
    public void testRead_byteArray() throws IOException {
        byte[] data = {65, 66, 67}; // ASCII values for A, B, C
        ByteArrayInputStream inputStream = new ByteArrayInputStream(data);
        CRC32 checksum = new CRC32();
        ChecksumCalculatingInputStream stream = new ChecksumCalculatingInputStream(checksum, inputStream);
        byte[] buffer = new byte[3];

        Assert.assertEquals(3, stream.read(buffer));
        Assert.assertArrayEquals(data, buffer);
        Assert.assertEquals(-1, stream.read());
        Assert.assertEquals(891568578, checksum.getValue());
    }

    @Test
    public void testRead_byteArrayOffsetLength() throws IOException {
        byte[] data = {65, 66, 67}; // ASCII values for A, B, C
        ByteArrayInputStream inputStream = new ByteArrayInputStream(data);
        CRC32 checksum = new CRC32();
        ChecksumCalculatingInputStream stream = new ChecksumCalculatingInputStream(checksum, inputStream);
        byte[] buffer = new byte[3];

        Assert.assertEquals(3, stream.read(buffer, 0, 3));
        Assert.assertArrayEquals(data, buffer);
        Assert.assertEquals(-1, stream.read());
        Assert.assertEquals(891568578, checksum.getValue());
    }

    @Test
    public void testSkip() throws IOException {
        byte[] data = {65, 66, 67}; // ASCII values for A, B, C
        ByteArrayInputStream inputStream = new ByteArrayInputStream(data);
        CRC32 checksum = new CRC32();
        ChecksumCalculatingInputStream stream = new ChecksumCalculatingInputStream(checksum, inputStream);

        long skipped = stream.skip(2);
        Assert.assertEquals(2, skipped);
        Assert.assertEquals(67, stream.read());
        Assert.assertEquals(-1, stream.read());
        Assert.assertEquals(891568578, checksum.getValue());
    }

    @Test
    public void testGetValue() throws IOException {
        byte[] data = {65, 66, 67}; // ASCII values for A, B, C
        ByteArrayInputStream inputStream = new ByteArrayInputStream(data);
        CRC32 checksum = new CRC32();
        ChecksumCalculatingInputStream stream = new ChecksumCalculatingInputStream(checksum, inputStream);

        while (stream.read() != -1) {
            // read entire stream to calculate checksum
        }

        long value = stream.getValue();
        Assert.assertEquals(891568578, value);
    }

}
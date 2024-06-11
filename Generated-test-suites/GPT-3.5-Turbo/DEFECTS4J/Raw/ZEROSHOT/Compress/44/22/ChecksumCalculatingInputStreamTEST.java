import java.io.ByteArrayInputStream;
import java.util.zip.CRC32;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class ChecksumCalculatingInputStreamTest {
    
    private ChecksumCalculatingInputStream stream;
    private CRC32 checksum;
    private ByteArrayInputStream in;
    private byte[] data;

    @Before
    public void setUp() {
        data = "Test data".getBytes();
        in = new ByteArrayInputStream(data);
        checksum = new CRC32();
        stream = new ChecksumCalculatingInputStream(checksum, in);
    }

    @Test
    public void testRead() {
        try {
            int byteRead = stream.read();
            Assert.assertEquals(data[0], (byte) byteRead);
            Assert.assertEquals(data[0], (byte) checksum.getValue());
        } catch (Exception e) {
            Assert.fail("Exception thrown: " + e);
        }
    }

    @Test
    public void testReadByteArray() {
        try {
            byte[] buffer = new byte[4];
            int bytesRead = stream.read(buffer);
            Assert.assertEquals(4, bytesRead);
            Assert.assertEquals(data[0], buffer[0]);
            Assert.assertEquals(data[1], buffer[1]);
            Assert.assertEquals(data[2], buffer[2]);
            Assert.assertEquals(data[3], buffer[3]);
            Assert.assertEquals(data[3], (byte) checksum.getValue());
        } catch (Exception e) {
            Assert.fail("Exception thrown: " + e);
        }
    }

    @Test
    public void testReadByteArrayOffsetLength() {
        try {
            byte[] buffer = new byte[4];
            int bytesRead = stream.read(buffer, 1, 2);
            Assert.assertEquals(2, bytesRead);
            Assert.assertEquals(0, buffer[0]);
            Assert.assertEquals(data[0], buffer[1]);
            Assert.assertEquals(data[1], buffer[2]);
            Assert.assertEquals(0, buffer[3]);
            Assert.assertEquals(data[1], (byte) checksum.getValue());
        } catch (Exception e) {
            Assert.fail("Exception thrown: " + e);
        }
    }

    @Test
    public void testSkip() {
        try {
            long skippedBytes = stream.skip(2);
            Assert.assertEquals(2, skippedBytes);
            Assert.assertEquals(data[2], (byte) checksum.getValue());
        } catch (Exception e) {
            Assert.fail("Exception thrown: " + e);
        }
    }

    @Test
    public void testGetValue() {
        try {
            long value = stream.getValue();
            Assert.assertEquals(0, value);
        } catch (Exception e) {
            Assert.fail("Exception thrown: " + e);
        }
    }

}
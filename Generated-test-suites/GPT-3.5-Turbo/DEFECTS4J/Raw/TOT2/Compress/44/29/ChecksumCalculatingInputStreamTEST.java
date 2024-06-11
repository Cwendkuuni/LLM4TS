import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.zip.CRC32;
import java.util.zip.Checksum;

import org.junit.Test;

public class ChecksumCalculatingInputStreamTest {

    @Test
    public void testReadSingleByte() {
        byte[] testData = {1, 2, 3, 4, 5};
        ByteArrayInputStream bais = new ByteArrayInputStream(testData);
        CRC32 checksum = new CRC32();
        ChecksumCalculatingInputStream ccis = new ChecksumCalculatingInputStream(checksum, bais);

        try {
            assertEquals(1, ccis.read());
            assertEquals(2, ccis.read());
            assertEquals(3, ccis.read());
            assertEquals(4, ccis.read());
            assertEquals(5, ccis.read());
            assertEquals(-1, ccis.read()); // should reach end of stream
            assertEquals(checksum.getValue(), 3112933486L); // CRC32 checksum for testData
        } catch (IOException e) {
            fail("IOException thrown");
        }
    }

    @Test
    public void testReadByteArray() {
        byte[] testData = {1, 2, 3, 4, 5};
        byte[] buffer = new byte[3];
        ByteArrayInputStream bais = new ByteArrayInputStream(testData);
        CRC32 checksum = new CRC32();
        ChecksumCalculatingInputStream ccis = new ChecksumCalculatingInputStream(checksum, bais);

        try {
            assertEquals(3, ccis.read(buffer));
            assertEquals(1, buffer[0]);
            assertEquals(2, buffer[1]);
            assertEquals(3, buffer[2]);
            assertEquals(checksum.getValue(), 3012526600L); // CRC32 checksum for partial testData
        } catch (IOException e) {
            fail("IOException thrown");
        }
    }

    @Test
    public void testReadByteArrayWithOffset() {
        byte[] testData = {1, 2, 3, 4, 5};
        byte[] buffer = new byte[5];
        ByteArrayInputStream bais = new ByteArrayInputStream(testData);
        CRC32 checksum = new CRC32();
        ChecksumCalculatingInputStream ccis = new ChecksumCalculatingInputStream(checksum, bais);

        try {
            assertEquals(2, ccis.read(buffer, 2, 2));
            assertEquals(0, buffer[0]);
            assertEquals(0, buffer[1]);
            assertEquals(1, buffer[2]);
            assertEquals(2, buffer[3]);
            assertEquals(checksum.getValue(), 3180973190L); // CRC32 checksum for partial testData
        } catch (IOException e) {
            fail("IOException thrown");
        }
    }

    @Test
    public void testSkip() {
        ByteArrayInputStream bais = new ByteArrayInputStream(new byte[5]);
        CRC32 checksum = new CRC32();
        ChecksumCalculatingInputStream ccis = new ChecksumCalculatingInputStream(checksum, bais);

        try {
            assertEquals(1, ccis.skip(1)); // Can't skip, should return 1
            assertEquals(0, ccis.skip(1)); // Can't skip, should return 0
            assertEquals(checksum.getValue(), 2529630970L); // check if checksum is calculated till end
        } catch (IOException e) {
            fail("IOException thrown");
        }
    }

}
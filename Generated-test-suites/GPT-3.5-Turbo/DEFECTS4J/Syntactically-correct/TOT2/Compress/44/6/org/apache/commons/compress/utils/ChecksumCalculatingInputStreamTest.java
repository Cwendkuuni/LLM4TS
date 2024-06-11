import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.apache.commons.compress.utils.ChecksumCalculatingInputStream;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.zip.CRC32;

@RunWith(JUnit4.class)
public class ChecksumCalculatingInputStreamTest {

    @Test
    public void testReadSingleByte() {
        byte[] testData = {1, 2, 3, 4, 5};
        ByteArrayInputStream bais = new ByteArrayInputStream(testData);
        CRC32 crc32 = new CRC32();
        ChecksumCalculatingInputStream ccis = new ChecksumCalculatingInputStream(crc32, bais);

        assertEquals(1, ccis.read());
        assertEquals(2, ccis.read());
        assertEquals(3, ccis.read());
        assertEquals(4, ccis.read());
        assertEquals(5, ccis.read());
    }

    @Test
    public void testReadByteArray() {
        byte[] testData = {1, 2, 3, 4, 5};
        ByteArrayInputStream bais = new ByteArrayInputStream(testData);
        CRC32 crc32 = new CRC32();
        ChecksumCalculatingInputStream ccis = new ChecksumCalculatingInputStream(crc32, bais);

        byte[] buffer = new byte[3];
        assertEquals(3, ccis.read(buffer));
        assertEquals(1, buffer[0]);
        assertEquals(2, buffer[1]);
        assertEquals(3, buffer[2]);
    }

    @Test
    public void testReadByteArrayWithOffset() {
        byte[] testData = {1, 2, 3, 4, 5};
        ByteArrayInputStream bais = new ByteArrayInputStream(testData);
        CRC32 crc32 = new CRC32();
        ChecksumCalculatingInputStream ccis = new ChecksumCalculatingInputStream(crc32, bais);

        byte[] buffer = new byte[3];
        assertEquals(3, ccis.read(buffer, 0, 3));
        assertEquals(1, buffer[0]);
        assertEquals(2, buffer[1]);
        assertEquals(3, buffer[2]);
    }

    @Test
    public void testSkip() {
        byte[] testData = {1, 2, 3, 4, 5};
        ByteArrayInputStream bais = new ByteArrayInputStream(testData);
        CRC32 crc32 = new CRC32();
        ChecksumCalculatingInputStream ccis = new ChecksumCalculatingInputStream(crc32, bais);

        try {
            ccis.skip(2);
            fail("Expected IOException for skipping not supported");
        } catch (IOException e) {
            assertTrue(true);
        }
    }

    @Test
    public void testGetValue() {
        byte[] testData = {1, 2, 3, 4, 5};
        ByteArrayInputStream bais = new ByteArrayInputStream(testData);
        CRC32 crc32 = new CRC32();
        ChecksumCalculatingInputStream ccis = new ChecksumCalculatingInputStream(crc32, bais);

        try {
            while (ccis.read() != -1) {}
        } catch (IOException e) {
            fail("Unexpected IOException");
        }

        assertEquals(451905744, ccis.getValue());
    }
}
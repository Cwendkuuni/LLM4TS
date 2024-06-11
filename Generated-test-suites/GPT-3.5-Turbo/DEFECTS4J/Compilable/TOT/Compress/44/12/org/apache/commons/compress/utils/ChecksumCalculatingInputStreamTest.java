package org.apache.commons.compress.utils;
import org.junit.Before;
import org.junit.Test;
import org.apache.commons.compress.utils.ChecksumCalculatingInputStream;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.zip.CRC32;
import java.util.zip.Checksum;
import static org.junit.Assert.*;

public class ChecksumCalculatingInputStreamTest {

    private Checksum checksum;
    private ChecksumCalculatingInputStream inputStream;

    @Before
    public void setup() {
        checksum = new CRC32();
        byte[] data = {1, 2, 3, 4, 5};
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(data);
        inputStream = new ChecksumCalculatingInputStream(checksum, byteArrayInputStream);
    }

    @Test
    public void testRead() throws IOException {
        int result = inputStream.read();
        assertEquals(1, result);
        assertEquals(1, inputStream.getValue());
    }

    @Test
    public void testReadByteArray() throws IOException {
        byte[] buffer = new byte[3];
        int result = inputStream.read(buffer);
        assertArrayEquals(new byte[]{1, 2, 3}, buffer);
        assertEquals(3, result);
        assertEquals(1696289942, inputStream.getValue());
    }

    @Test
    public void testReadByteArrayOffsetLength() throws IOException {
        byte[] buffer = new byte[5];
        int result = inputStream.read(buffer, 1, 3);
        assertArrayEquals(new byte[]{0, 1, 2, 3, 0}, buffer);
        assertEquals(3, result);
        assertEquals(1696289942, inputStream.getValue());
    }

    @Test
    public void testSkip() throws IOException {
        long result = inputStream.skip(2);
        assertEquals(2, result);
        assertEquals(313067135, inputStream.getValue());
    }

    @Test
    public void testGetValue() throws IOException {
        long result = inputStream.getValue();
        assertEquals(0, result);
    }

    @Test(expected = IOException.class)
    public void testReadWithError() throws IOException {
        checksum.update(5);
        inputStream.read();
    }

    @Test(expected = IOException.class)
    public void testReadByteArrayWithError() throws IOException {
        checksum.update(1);
        checksum.update(2);
        checksum.update(6);
        byte[] buffer = new byte[3];
        inputStream.read(buffer);
    }

    @Test(expected = IOException.class)
    public void testReadByteArrayOffsetLengthWithError() throws IOException {
        checksum.update(1);
        checksum.update(2);
        checksum.update(7);
        byte[] buffer = new byte[3];
        inputStream.read(buffer, 0, 3);
    }

    @Test(expected = IOException.class)
    public void testSkipWithError() throws IOException {
        checksum.update(5);
        inputStream.skip(1);
    }

}
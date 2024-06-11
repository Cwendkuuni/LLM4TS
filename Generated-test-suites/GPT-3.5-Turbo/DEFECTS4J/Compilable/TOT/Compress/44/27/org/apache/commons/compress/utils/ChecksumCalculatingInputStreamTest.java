package org.apache.commons.compress.utils;
import org.apache.commons.compress.utils.ChecksumCalculatingInputStream;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.zip.CRC32;
import java.util.zip.Checksum;

import static org.junit.Assert.assertEquals;

public class ChecksumCalculatingInputStreamTest {

    private ChecksumCalculatingInputStream inputStream;
    private Checksum checksum;

    @Before
    public void setup() {
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream("test string".getBytes());
        checksum = new CRC32();
        inputStream = new ChecksumCalculatingInputStream(checksum, byteArrayInputStream);
    }

    @Test
    public void testReadByte() throws IOException {
        assertEquals('t', inputStream.read());
        assertEquals('e', inputStream.read());
        assertEquals('s', inputStream.read());
        assertEquals('t', inputStream.read());
        assertEquals(' ', inputStream.read());
        assertEquals('s', inputStream.read());
        assertEquals('t', inputStream.read());
        assertEquals('r', inputStream.read());
        assertEquals('i', inputStream.read());
        assertEquals('n', inputStream.read());
        assertEquals('g', inputStream.read());
    }

    @Test
    public void testReadByteZeroLengthInputStream() throws IOException {
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream("".getBytes());
        checksum = new CRC32();
        inputStream = new ChecksumCalculatingInputStream(checksum, byteArrayInputStream);
        
        assertEquals(-1, inputStream.read());
    }

    @Test
    public void testReadByteArray() throws IOException {
        byte[] buffer = new byte[4];
        int bytesRead = inputStream.read(buffer);

        assertEquals(4, bytesRead);
        assertEquals("test", new String(buffer));

        bytesRead = inputStream.read(buffer);

        assertEquals(3, bytesRead);
        assertEquals(" st", new String(buffer, 0, bytesRead));
    }

    @Test
    public void testSkip() throws IOException {
        assertEquals(1, inputStream.skip(1));
        
        byte[] buffer = new byte[10];
        int bytesRead = inputStream.read(buffer);

        assertEquals(10, bytesRead);
        assertEquals("est string", new String(buffer, 0, bytesRead));
    }

    @Test
    public void testSkipZeroLengthInputStream() throws IOException {
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream("".getBytes());
        checksum = new CRC32();
        inputStream = new ChecksumCalculatingInputStream(checksum, byteArrayInputStream);
        
        assertEquals(0, inputStream.skip(1));
    }

    @Test
    public void testGetValue() throws IOException {
        inputStream.skip(5);
        assertEquals(2826518685L, inputStream.getValue());
    }

}
package org.apache.commons.compress.utils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.CRC32;

import org.junit.Test;

public class ChecksumCalculatingInputStreamTest {
    private static final byte[] TEST_DATA = new byte[]{1, 2, 3, 4, 5};
    
    @Test
    public void testRead() throws IOException {
        InputStream in = new ByteArrayInputStream(TEST_DATA);
        ChecksumCalculatingInputStream calculatingInputStream = new ChecksumCalculatingInputStream(new CRC32(), in);
        
        assertEquals(1, calculatingInputStream.read());
        assertEquals(2, calculatingInputStream.read());
        assertEquals(3, calculatingInputStream.read());
        assertEquals(4, calculatingInputStream.read());
        assertEquals(5, calculatingInputStream.read());
        assertEquals(-1, calculatingInputStream.read());
        
        calculatingInputStream.close();
        in.close();
    }
    
    @Test
    public void testReadByteArray() throws IOException {
        InputStream in = new ByteArrayInputStream(TEST_DATA);
        ChecksumCalculatingInputStream calculatingInputStream = new ChecksumCalculatingInputStream(new CRC32(), in);
        
        byte[] buffer = new byte[3];
        assertEquals(3, calculatingInputStream.read(buffer));
        assertEquals(1, buffer[0]);
        assertEquals(2, buffer[1]);
        assertEquals(3, buffer[2]);
        
        assertEquals(2, calculatingInputStream.read(buffer));
        assertEquals(4, buffer[0]);
        assertEquals(5, buffer[1]);
        assertEquals(3, buffer[2]);
        
        assertEquals(0, calculatingInputStream.read(buffer));
        
        calculatingInputStream.close();
        in.close();
    }
    
    @Test
    public void testReadByteArrayWithOffset() throws IOException {
        InputStream in = new ByteArrayInputStream(TEST_DATA);
        ChecksumCalculatingInputStream calculatingInputStream = new ChecksumCalculatingInputStream(new CRC32(), in);
        
        byte[] buffer = new byte[10];
        assertEquals(3, calculatingInputStream.read(buffer, 1, 3));
        assertEquals(0, buffer[0]);
        assertEquals(1, buffer[1]);
        assertEquals(2, buffer[2]);
        assertEquals(3, buffer[3]);
        assertEquals(0, buffer[4]);
        
        assertEquals(2, calculatingInputStream.read(buffer, 4, 3));
        assertEquals(0, buffer[5]);
        assertEquals(4, buffer[6]);
        assertEquals(5, buffer[7]);
        assertEquals(0, buffer[8]);
        assertEquals(0, buffer[9]);
        
        assertEquals(0, calculatingInputStream.read(buffer, 0, 3));
        
        calculatingInputStream.close();
        in.close();
    }
    
    @Test
    public void testSkip() throws IOException {
        InputStream in = new ByteArrayInputStream(TEST_DATA);
        ChecksumCalculatingInputStream calculatingInputStream = new ChecksumCalculatingInputStream(new CRC32(), in);
        
        assertEquals(1, calculatingInputStream.skip(1));
        assertEquals(2, calculatingInputStream.read());
        assertEquals(1, calculatingInputStream.skip(1));
        assertEquals(4, calculatingInputStream.read());
        assertEquals(3, calculatingInputStream.skip(10));
        assertEquals(-1, calculatingInputStream.read());
        
        calculatingInputStream.close();
        in.close();
    }
    
    @Test
    public void testGetValue() {
        InputStream in = new ByteArrayInputStream(TEST_DATA);
        CRC32 crc32 = new CRC32();
        ChecksumCalculatingInputStream calculatingInputStream = new ChecksumCalculatingInputStream(crc32, in);
        
        calculatingInputStream.read();
        calculatingInputStream.read();
        long checksumValue = calculatingInputStream.getValue();
        
        assertEquals(crc32.getValue(), checksumValue);
        
        calculatingInputStream.close();
        in.close();
    }
    
    @Test
    public void testConstructor() {
        InputStream in = new ByteArrayInputStream(TEST_DATA);
        CRC32 crc32 = new CRC32();
        ChecksumCalculatingInputStream calculatingInputStream = new ChecksumCalculatingInputStream(crc32, in);
        
        assertNotNull(calculatingInputStream);
        assertTrue(calculatingInputStream instanceof InputStream);
        
        calculatingInputStream.close();
    }
}
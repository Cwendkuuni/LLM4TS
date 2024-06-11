package org.apache.commons.codec.binary;

import static org.junit.Assert.assertEquals;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.junit.Before;
import org.junit.Test;

public class BaseNCodecInputStreamTest {
    
    private BaseNCodecInputStream baseNCodecInputStream;
    private byte[] testData = {65, 66, 67, 68, 69}; // ABCDE
    
    @Before
    public void setUp() {
        InputStream in = new ByteArrayInputStream(testData);
        BaseNCodec baseNCodec = new BaseNCodec() {
            @Override
            void encode(byte[] p0, int p1, int p2) throws IOException {
                // implementation not needed for testing
            }
            
            @Override
            void decode(byte[] p0, int p1, int p2) throws IOException {
                // implementation not needed for testing
            }
            
            @Override
            boolean hasData() {
                // implementation not needed for testing
                return false;
            }
            
            @Override
            int readResults(byte[] p0, int p1, int p2) {
                // implementation not needed for testing
                return 0;
            }
        };
        baseNCodecInputStream = new BaseNCodecInputStream(in, baseNCodec, false);
    }
    
    @Test
    public void testRead() throws IOException {
        assertEquals(65, baseNCodecInputStream.read());
        assertEquals(66, baseNCodecInputStream.read());
        assertEquals(67, baseNCodecInputStream.read());
        assertEquals(68, baseNCodecInputStream.read());
        assertEquals(69, baseNCodecInputStream.read());
        assertEquals(-1, baseNCodecInputStream.read());
    }
    
    @Test
    public void testReadWithByteArray() throws IOException {
        byte[] buffer = new byte[5]; // same size as testData
        int readLen = baseNCodecInputStream.read(buffer);
        assertEquals(5, readLen);
        for (int i = 0; i < readLen; i++) {
            assertEquals(testData[i], buffer[i]);
        }
    }
    
    @Test
    public void testReadWithOffsetAndLength() throws IOException {
        byte[] buffer = new byte[10]; // larger than testData
        int readLen = baseNCodecInputStream.read(buffer, 2, 5); // offset = 2, length = 5
        assertEquals(5, readLen);
        for (int i = 2; i < 7; i++) {
            assertEquals(testData[i - 2], buffer[i]);
        }
    }
    
    @Test(expected = NullPointerException.class)
    public void testReadWithNullArray() throws IOException {
        baseNCodecInputStream.read(null, 0, 5);
    }
    
    @Test(expected = IndexOutOfBoundsException.class)
    public void testReadWithNegativeOffset() throws IOException {
        byte[] buffer = new byte[10];
        baseNCodecInputStream.read(buffer, -1, 5);
    }
    
    @Test(expected = IndexOutOfBoundsException.class)
    public void testReadWithNegativeLength() throws IOException {
        byte[] buffer = new byte[10];
        baseNCodecInputStream.read(buffer, 0, -5);
    }
    
    @Test(expected = IndexOutOfBoundsException.class)
    public void testReadWithInvalidOffset() throws IOException {
        byte[] buffer = new byte[10];
        baseNCodecInputStream.read(buffer, 12, 5);
    }
    
    @Test(expected = IndexOutOfBoundsException.class)
    public void testReadWithInvalidLength() throws IOException {
        byte[] buffer = new byte[10];
        baseNCodecInputStream.read(buffer, 6, 5);
    }
    
    @Test
    public void testMarkSupported() {
        assertEquals(false, baseNCodecInputStream.markSupported());
    }
    
    @Test
    public void testAvailable() throws IOException {
        assertEquals(1, baseNCodecInputStream.available());
    }
}
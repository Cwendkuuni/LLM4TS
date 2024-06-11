package org.apache.commons.codec.binary;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertArrayEquals;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import org.junit.Before;
import org.junit.Test;

public class BaseNCodecInputStreamTest {
    
    private ByteArrayInputStream in;
    private BaseNCodec baseNCodec;
    private boolean doEncode;
    private BaseNCodecInputStream baseNCodecInputStream;
    
    @Before
    public void setup() {
        byte[] data = new byte[] {65, 66, 67, 68, 69, 70};
        in = new ByteArrayInputStream(data);
        baseNCodec = new BaseNCodec();
        doEncode = false;
        baseNCodecInputStream = new BaseNCodecInputStream(in, baseNCodec, doEncode);
    }
    
    @Test
    public void testRead() throws IOException {
        assertEquals(65, baseNCodecInputStream.read());
        assertEquals(66, baseNCodecInputStream.read());
        assertEquals(67, baseNCodecInputStream.read());
        assertEquals(68, baseNCodecInputStream.read());
        assertEquals(69, baseNCodecInputStream.read());
        assertEquals(70, baseNCodecInputStream.read());
        assertEquals(-1, baseNCodecInputStream.read());
    }
    
    @Test
    public void testReadArray() throws IOException {
        byte[] expected = new byte[] {65, 66, 67, 68, 69, 70};
        byte[] actual = new byte[6];
        int len = baseNCodecInputStream.read(actual, 0, 6);
        assertEquals(6, len);
        assertArrayEquals(expected, actual);
    }
    
    @Test(expected = NullPointerException.class)
    public void testReadArrayNullBuffer() throws IOException {
        baseNCodecInputStream.read(null, 0, 6);
    }
    
    @Test(expected = IndexOutOfBoundsException.class)
    public void testReadArrayNegativeOffset() throws IOException {
        baseNCodecInputStream.read(new byte[6], -1, 6);
    }
    
    @Test(expected = IndexOutOfBoundsException.class)
    public void testReadArrayNegativeLength() throws IOException {
        baseNCodecInputStream.read(new byte[6], 0, -1);
    }
    
    @Test(expected = IndexOutOfBoundsException.class)
    public void testReadArrayInvalidOffset() throws IOException {
        baseNCodecInputStream.read(new byte[6], 7, 6);
    }
    
    @Test(expected = IndexOutOfBoundsException.class)
    public void testReadArrayInvalidLength() throws IOException {
        baseNCodecInputStream.read(new byte[6], 0, 7);
    }
    
    @Test
    public void testReadArrayZeroLength() throws IOException {
        byte[] expected = new byte[6];
        byte[] actual = new byte[6];
        int len = baseNCodecInputStream.read(actual, 0, 0);
        assertEquals(0, len);
        assertArrayEquals(expected, actual);
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
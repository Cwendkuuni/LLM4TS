package org.apache.commons.codec.binary;

import org.junit.Before;
import org.junit.Test;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import static org.junit.Assert.assertEquals;

public class BaseNCodecInputStreamTest {

    private BaseNCodec baseNCodec;
    private BaseNCodecInputStream baseNCodecInputStream;

    @Before
    public void setUp() {
        baseNCodec = new BaseNCodec();
        byte[] data = {12, 32, 56, 48};
        ByteArrayInputStream inputStream = new ByteArrayInputStream(data);
        baseNCodecInputStream = new BaseNCodecInputStream(inputStream, baseNCodec, true);
    }

    @Test
    public void testRead() throws IOException {
        assertEquals(12, baseNCodecInputStream.read());
        assertEquals(32, baseNCodecInputStream.read());
        assertEquals(56, baseNCodecInputStream.read());
        assertEquals(48, baseNCodecInputStream.read());
        assertEquals(-1, baseNCodecInputStream.read());
    }

    @Test
    public void testReadWithByteArray() throws IOException {
        byte[] data = new byte[4];
        assertEquals(4, baseNCodecInputStream.read(data));
        assertEquals(12, data[0]);
        assertEquals(32, data[1]);
        assertEquals(56, data[2]);
        assertEquals(48, data[3]);
        assertEquals(-1, baseNCodecInputStream.read(data));
    }

    @Test
    public void testReadWithOffsetAndLength() throws IOException {
        byte[] data = new byte[6];
        assertEquals(3, baseNCodecInputStream.read(data, 1, 3));
        assertEquals(0, data[0]);
        assertEquals(12, data[1]);
        assertEquals(32, data[2]);
        assertEquals(56, data[3]);
        assertEquals(0, data[4]);
        assertEquals(0, data[5]);
    }
}
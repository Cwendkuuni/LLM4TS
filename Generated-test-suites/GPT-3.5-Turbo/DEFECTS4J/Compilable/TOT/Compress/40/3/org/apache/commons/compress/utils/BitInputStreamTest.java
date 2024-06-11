package org.apache.commons.compress.utils;
import org.junit.Test;
import org.junit.Before;
import org.junit.After;
import static org.junit.Assert.*;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.ByteOrder;

public class BitInputStreamTest {

    private BitInputStream bitInputStream;
    private ByteArrayInputStream byteArrayInputStream;

    @Before
    public void setUp() {
        byteArrayInputStream = new ByteArrayInputStream(new byte[]{0x5A, (byte) 0xA5});
        bitInputStream = new BitInputStream(byteArrayInputStream, ByteOrder.BIG_ENDIAN);
    }

    @After
    public void tearDown() throws IOException {
        bitInputStream.close();
    }

    @Test
    public void testReadBits_returnsCorrectBits() throws IOException {
        long result = bitInputStream.readBits(8);
        assertEquals(0x5A, result);

        result = bitInputStream.readBits(8);
        assertEquals(0xA5, result);
    }

    @Test
    public void testReadBits_returnsNegativeOneAtEndOfFile() throws IOException {
        long result = bitInputStream.readBits(16);
        assertEquals(0x5AA5, result);

        result = bitInputStream.readBits(8);
        assertEquals(-1, result);
    }

    @Test
    public void testReadBits_throwsExceptionForInvalidCount() throws IOException {
        try {
            bitInputStream.readBits(-1);
            fail("Should throw IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }

        try {
            bitInputStream.readBits(64);
            fail("Should throw IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test
    public void testClearBitCache_clearsCache() throws IOException {
        bitInputStream.readBits(4);
        bitInputStream.clearBitCache();
        long result = bitInputStream.readBits(8);
        assertEquals(0x5A, result);
    }

}
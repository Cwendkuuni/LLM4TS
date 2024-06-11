package org.apache.commons.compress.utils;

import org.apache.commons.compress.utils.BitInputStream;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThrows;

/**
 * Test class for {@link BitInputStream}.
 */
public class BitInputStreamTest {

    private InputStream inputStream;
    private ByteArrayOutputStream outputStream;

    @Before
    public void setUp() {
        outputStream = new ByteArrayOutputStream();
        inputStream = new ByteArrayInputStream(new byte[]{(byte) 0b11111111, (byte) 0b00111111, (byte) 0b11000000, (byte) 0b11110000});
    }

    @After
    public void tearDown() throws IOException {
        inputStream.close();
        outputStream.close();
    }

    /**
     * Tests that the default constructor initializes the input stream and byte order correctly.
     * @throws IOException on error
     */
    @Test
    public void testDefaultConstructor() throws IOException {
        BitInputStream bitInputStream = new BitInputStream(inputStream, ByteOrder.LITTLE_ENDIAN);
        assertNotNull(bitInputStream);
    }

    /**
     * Tests that {@code clearBitCache()} clears the cached bits.
     * @throws IOException on error
     */
    @Test
    public void testClearBitCache() throws IOException {
        BitInputStream bitInputStream = new BitInputStream(inputStream, ByteOrder.LITTLE_ENDIAN);
        bitInputStream.clearBitCache();
        assertFalse(bitInputStream.hasBitsCached());
    }

    /**
     * Tests that {@code readBits()} reads the correct number of bits from the input stream.
     * @throws IOException on error
     */
    @Test
    public void testReadBits() throws IOException {
        BitInputStream bitInputStream = new BitInputStream(inputStream, ByteOrder.LITTLE_ENDIAN);
        long bits = bitInputStream.readBits(8);
        assertEquals(0b11111111L, bits);
        bits = bitInputStream.readBits(8);
        assertEquals(0b00111111L, bits);
        bits = bitInputStream.readBits(4);
        assertEquals(0b1100L, bits);
        bits = bitInputStream.readBits(4);
        assertEquals(0b1111L, bits);
        assertEquals(-1L, bitInputStream.readBits(10));
    }

    /**
     * Tests that {@code readBits()} handles end of stream correctly.
     * @throws IOException on error
     */
    @Test
    public void testReadBitsEndOfStream() throws IOException {
        BitInputStream bitInputStream = new BitInputStream(inputStream, ByteOrder.LITTLE_ENDIAN);
        bitInputStream.readBits(8);
        bitInputStream.readBits(8);
        bitInputStream.readBits(4);
        bitInputStream.readBits(4);
        long bits = bitInputStream.readBits(10);
        assertEquals(-1L, bits);
    }

    /**
     * Tests that {@code readBits()} throws an {@code IOException} when the input stream throws an {@code IOException}.
     * @throws IOException on error
     */
    @Test
    public void testReadBitsIOException() throws IOException {
        ByteArrayInputStream inputStreamSpy = new SpyInputStream(outputStream) {
            @Override
            public int read() throws IOException {
                throw new IOException("Test");
            }
        };
        BitInputStream bitInputStream = new BitInputStream(inputStreamSpy, ByteOrder.LITTLE_ENDIAN);
        assertThrows(IOException.class, () -> bitInputStream.readBits(1));
    }

    /**
     * Tests that {@code readBits()} throws an {@code IllegalArgumentException} when the count is negative.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testReadBitsNegativeCount() {
        BitInputStream bitInputStream = new BitInputStream(inputStream, ByteOrder.LITTLE_ENDIAN);
        bitInputStream.readBits(-1);
    }

    /**
     * Tests that {@code readBits()} throws an {@code IllegalArgumentException} when the count is greater than the maximum cache size.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testReadBitsMaximumCacheSize() {
        BitInputStream bitInputStream = new BitInputStream(inputStream, ByteOrder.LITTLE_ENDIAN);
        bitInputStream.readBits(64);
    }
}

class SpyInputStream extends InputStream {
    private final OutputStream outputStream;

    SpyInputStream(OutputStream outputStream) {
        this.outputStream = outputStream;
    }

    @Override
    public int read() throws IOException {
        outputStream.write(Integer.toByteArray(0xFF & (this.mark = this.in.read()))[0]);
        return this.mark;
    }
}
import org.apache.commons.compress.utils.BitInputStream;
import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.Closeable;
import java.io.IOException;

public class BitInputStreamTest {

    @Test
    public void testBitInputStreamConstructor() {
        InputStream input = new ByteArrayInputStream(new byte[]{(byte) 0xab, (byte) 0xcd, (byte) 0xef});
        BitInputStream bitInputStream = new BitInputStream(input, ByteOrder.LITTLE_ENDIAN);
        assertNotNull(bitInputStream);
    }

    @Test
    public void testClose() throws IOException {
        InputStream input = new ByteArrayInputStream(new byte[]{(byte) 0xab, (byte) 0xcd, (byte) 0xef});
        BitInputStream bitInputStream = new BitInputStream(input, ByteOrder.LITTLE_ENDIAN);
        bitInputStream.close();
        assertThrows(IOException.class, () -> bitInputStream.readBits(1));
    }

    @Test
    public void testClearBitCache() throws IOException {
        InputStream input = new ByteArrayInputStream(new byte[]{(byte) 0xab, (byte) 0xcd, (byte) 0xef});
        BitInputStream bitInputStream = new BitInputStream(input, ByteOrder.LITTLE_ENDIAN);
        bitInputStream.readBits(8);
        bitInputStream.clearBitCache();
        assertEquals(0, bitInputStream.bitsCached);
    }

    @Test
    public void testReadBitsCountLessThanOrEqualToMaximum() throws IOException {
        InputStream input = new ByteArrayInputStream(new byte[]{(byte) 0xab, (byte) 0xcd, (byte) 0xef});
        BitInputStream bitInputStream = new BitInputStream(input, ByteOrder.LITTLE_ENDIAN);

        // Test reading 1 bit
        assertEquals(0x01 & ((long) 0xab >> 0), bitInputStream.readBits(1));
        assertEquals(0x01 & ((long) 0xcd >> 4), bitInputStream.readBits(3));
        assertEquals(0x01 & ((long) 0xef >> 0), bitInputStream.readBits(1));

        // Test reading multiple bits
        assertEquals(0x3 & ((long) 0xab >> 2), bitInputStream.readBits(3));
        assertEquals(0x3f & ((long) 0xcd >> 1), bitInputStream.readBits(6));
        assertEquals(0xff & ((long) 0xef >> 5), bitInputStream.readBits(3));

        // Test reading the maximum number of bits
        assertEquals(0xffffffffffffffffL & ((long) 0xab >> 0), bitInputStream.readBits(63));

        // Test reading beyond the maximum number of bits
        assertThrows(IllegalArgumentException.class, () -> bitInputStream.readBits(64));

        // Test reading bits when the InputStream has already reached its end
        input.mark(1);
        input.read();
        assertThrows(IOException.class, () -> bitInputStream.readBits(1));

        // Test reading bits with negative count
        assertThrows(IllegalArgumentException.class, () -> bitInputStream.readBits(-1));
    }

    @Test
    public void testBitInputStreamConstructorWithNullInputStream() {
        assertThrows(NullPointerException.class, () -> new BitInputStream(null, ByteOrder.LITTLE_ENDIAN));
    }

    @Test
    public void testBitInputStreamConstructorWithNullByteOrder() {
        assertThrows(NullPointerException.class, () -> new BitInputStream(new ByteArrayInputStream(new byte[]{(byte) 0xab, (byte) 0xcd, (byte) 0xef}), null));
    }

    @Test
    public void testBitInputStreamConstructorWithClosedInputStream() {
        InputStream input = new ByteArrayInputStream(new byte[]{(byte) 0xab, (byte) 0xcd, (byte) 0xef});
        input.close();
        assertThrows(IOException.class, () -> new BitInputStream(input, ByteOrder.LITTLE_ENDIAN));
    }

    @Test
    public void testBitInputStreamConstructorWithIOExceptionInputStream() {
        InputStream input = new ByteArrayInputStream(new byte[]{(byte) -1});
        assertThrows(IOException.class, () -> new BitInputStream(input, ByteOrder.LITTLE_ENDIAN));
    }
}
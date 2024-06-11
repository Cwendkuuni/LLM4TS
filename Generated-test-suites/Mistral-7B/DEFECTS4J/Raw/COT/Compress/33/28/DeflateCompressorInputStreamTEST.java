import org.apache.commons.compress.compressors.deflate.DeflateCompressorInputStream;
import org.apache.commons.compress.compressors.deflate.DeflateParameters;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.Inflater;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThrows;

public class DeflateCompressorInputStreamTest {

    private InputStream inputStream;
    private DeflateCompressorInputStream deflateCompressorInputStream;

    @Before
    public void setUp() throws IOException {
        byte[] inputData = new byte[]{(byte) 0x78, (byte) 0x4c, (byte) 0x6f, (byte) 0x00, (byte) 0x14, (byte) 0x00, (byte) 0x00, (byte) 0x08, (byte) 0x00, (byte) 0xcc, (byte) 0x1a, (byte) 0x25, (byte) 0x5a, (byte) 0x42, (byte) 0x60, (byte) 0x9e, (byte) 0x4b, (byte) 0x08, (byte) 0x00, (byte) 0xc9, (byte) 0x9e, (byte) 0x4b, (byte) 0x01, (byte) 0x00, (byte) 0x14, (byte) 0x00, (byte) 0x00, (byte) 0x08, (byte) 0xd0, (byte) 0x00, (byte) 0xd3, (byte) 0xff, (byte) 0xff};
        inputStream = new ByteArrayInputStream(inputData);
        deflateCompressorInputStream = new DeflateCompressorInputStream(inputStream);
    }

    @After
    public void tearDown() throws IOException {
        if (deflateCompressorInputStream != null) {
            deflateCompressorInputStream.close();
        }
        if (inputStream != null) {
            inputStream.close();
        }
    }

    // Test constructor with valid InputStream
    @Test
    public void testDeflateCompressorInputStreamConstructorValidInputStream() {
        DeflateCompressorInputStream deflateCompressorInputStream = new DeflateCompressorInputStream(inputStream);
        assertNotNull(deflateCompressorInputStream);
    }

    // Test constructor with null InputStream
    @Test(expected = NullPointerException.class)
    public void testDeflateCompressorInputStreamConstructorNullInputStream() {
        new DeflateCompressorInputStream(null);
    }

    // Test read method
    @Test
    public void testRead() throws IOException {
        byte[] expected = new byte[]{0x00, 0xcc, 0x1a, 0x25, 0x5a, 0x42, 0x60, 0x9e, 0x4b, 0x08, 0xd0, 0x00, 0xd3, 0xff, 0xff};
        byte[] actual = new byte[expected.length];
        int read;
        for (int i = 0; i < expected.length; i++) {
            read = deflateCompressorInputStream.read();
            actual[i] = (byte) read;
        }
        assertArrayEquals(expected, actual);
    }

    // Test read method with EOF
    @Test
    public void testReadEndOfStream() throws IOException {
        byte[] expected = new byte[0];
        byte[] actual = new byte[expected.length];
        int read = deflateCompressorInputStream.read();
        for (int i = 0; i < expected.length; i++) {
            actual[i] = (byte) (read == -1 ? 0 : read);
            read = deflateCompressorInputStream.read();
        }
        assertArrayEquals(expected, actual);
    }

    // Test read method with invalid length
    @Test(expected = IndexOutOfBoundsException.class)
    public void testReadInvalidLength() throws IOException {
        byte[] buffer = new byte[2];
        deflateCompressorInputStream.read(buffer, 2, 1);
    }

    // Test read method with negative length
    @Test(expected = IllegalArgumentException.class)
    public void testReadNegativeLength() throws IOException {
        deflateCompressorInputStream.read(new byte[1], -1, 1);
    }

    // Test read method with zero length
    @Test
    public void testReadZeroLength() throws IOException {
        byte[] buffer = new byte[0];
        int read = deflateCompressorInputStream.read(buffer);
        assertEquals(0, read);
    }

    // Test read method with data available
    @Test
    public void testReadDataAvailable() throws IOException {
        byte[] buffer = new byte[5];
        int read = deflateCompressorInputStream.read(buffer);
        assertEquals(5, read);
    }

    // Test read method without data available
    @Test
    public void testReadNoDataAvailable() throws IOException {
        byte[] buffer = new byte[5];
        int read = deflateCompressorInputStream.read(buffer);
        assertEquals(-1, read);
    }

    // Test read all bytes
    @Test
    public void testReadAllBytes() throws IOException {
        byte[] expected = inputData;
        byte[] actual = new byte[expected.length];
        int read;
        while ((read = deflateCompressorInputStream.read()) != -1) {
            actual[inputStream.available()] = (byte) read;
        }
        assertArrayEquals(expected, actual);
    }

    // Test read method with InflaterStream
    @Test
    public void testReadWithInflaterStream() throws IOException {
        DeflateCompressorInputStream deflateCompressorInputStream = new DeflateCompressorInputStream(new ByteArrayInputStream(inputData), new DeflateParameters());
        byte[] expected = new byte[]{0x00, 0xcc, 0x1a, 0x25, 0x5a, 0x42, 0x60, 0x9e, 0x4b, 0x08, 0xd0, 0x00, 0xd3, 0xff, 0xff};
        byte[] actual = new byte[expected.length];
        int read;
        for (int i = 0; i < expected.length; i++) {
            read = deflateCompressorInputStream.read();
            actual[i] = (byte) read;
        }
        assertArrayEquals(expected, actual);
    }

    // Test read method with InflaterStream and negative length
    @Test(expected = IllegalArgumentException.class)
    public void testReadWithInflaterStreamNegativeLength() throws IOException {
        DeflateCompressorInputStream deflateCompressorInputStream = new DeflateCompressorInputStream(new ByteArrayInputStream(inputData), new DeflateParameters());
        deflateCompressorInputStream.read(new byte[1], -1, 1);
    }

    // Test read method with InflaterStream and zero length
    @Test
    public void testReadWithInflaterStreamZeroLength() throws IOException {
        DeflateCompressorInputStream deflateCompressorInputStream = new DeflateCompressorInputStream(new ByteArrayInputStream(inputData), new DeflateParameters());
        byte[] buffer = new byte[0];
        int read = deflateCompressorInputStream.read(buffer);
        assertEquals(0, read);
    }

    // Test read method with InflaterStream and data available
    @Test
    public void testReadWithInflaterStreamDataAvailable() throws IOException {
        DeflateCompressorInputStream deflateCompressorInputStream = new DeflateCompressorInputStream(new ByteArrayInputStream(inputData), new DeflateParameters());
        byte[] buffer = new byte[5];
        int read = deflateCompressorInputStream.read(buffer);
        assertEquals(5, read);
    }

    // Test read method with InflaterStream and no data available
    @Test
    public void testReadWithInflaterStreamNoDataAvailable() throws IOException {
        DeflateCompressorInputStream deflateCompressorInputStream = new DeflateCompressorInputStream(new ByteArrayInputStream(inputData), new DeflateParameters());
        byte[] buffer = new byte[5];
        int read = deflateCompressorInputStream.read(buffer);
        assertEquals(-1, read);
    }

    // Test read method with InflaterStream and EOF
    @Test
    public void testReadWithInflaterStreamEndOfStream() throws IOException {
        DeflateCompressorInputStream deflateCompressorInputStream = new DeflateCompressorInputStream(new ByteArrayInputStream(inputData), new DeflateParameters());
        byte[] expected = new byte[0];
        byte[] actual = new byte[expected.length];
        int read;
        while ((read = deflateCompressorInputStream.read()) != -1) {
            actual[deflateCompressorInputStream.available()] = (byte) read;
        }
        assertArrayEquals(expected, actual);
    }

    // Test read method with InflaterStream and exception
    @Test
    public void testReadWithInflaterStreamException() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        Inflater inflater = new Inflater();
        inflater.setInput(inputData);
        InputStream inputStream = new InflaterInputStream(outputStream, inflater);
        DeflateCompressorInputStream deflateCompressorInputStream = new DeflateCompressorInputStream(inputStream, new DeflateParameters());
        assertThrows(IOException.class, () -> deflateCompressorInputStream.read());
    }

    // Test close method
    @Test
    public void testClose() throws IOException {
        deflateCompressorInputStream.close();
        assertFalse(deflateCompressorInputStream.isOpen());
    }

    // Test close method with already closed stream
    @Test(expected = IllegalStateException.class)
    public void testCloseAlreadyClosed() throws IOException {
        deflateCompressorInputStream.close();
        deflateCompressorInputStream.close();
    }

    // Test available method
    @Test
    public void testAvailable() throws IOException {
        int expected = inputData.length - inputStream.available();
        assertEquals(expected, deflateCompressorInputStream.available());
    }

    // Test available method with empty buffer
    @Test
    public void testAvailableEmptyBuffer() throws IOException {
        byte[] buffer = new byte[5];
        deflateCompressorInputStream.read(buffer);
        int expected = inputData.length - inputStream.available();
        assertEquals(expected, deflateCompressorInputStream.available());
    }

    // Test available method with read data
    @Test
    public void testAvailableReadData() throws IOException {
        byte[] buffer = new byte[5];
        int read = deflateCompressorInputStream.read(buffer);
        int expected = inputData.length - (inputData.length - read);
        assertEquals(expected, deflateCompressorInputStream.available());
    }

    // Test available method with no data read
    @Test
    public void testAvailableNoDataRead() throws IOException {
        int expected = inputData.length - inputStream.available();
        assertEquals(expected, deflateCompressorInputStream.available());
    }

    // Test available method with InflaterStream
    @Test
    public void testAvailableWithInflaterStream() throws IOException {
        DeflateCompressorInputStream deflateCompressorInputStream = new DeflateCompressorInputStream(new ByteArrayInputStream(inputData), new DeflateParameters());
        int expected = inputData.length - deflateCompressorInputStream.available();
        assertEquals(expected, deflateCompressorInputStream.available());
    }

    // Test available method with InflaterStream and data available
    @Test
    public void testAvailableWithInflaterStreamDataAvailable() throws IOException {
        DeflateCompressorInputStream deflateCompressorInputStream = new DeflateCompressorInputStream(new ByteArrayInputStream(inputData), new DeflateParameters());
        byte[] buffer = new byte[5];
        int read = deflateCompressorInputStream.read(buffer);
        int expected = inputData.length - (inputData.length - read);
        assertEquals(expected, deflateCompressorInputStream.available());
    }

    // Test available method with InflaterStream and no data available
    @Test
    public void testAvailableWithInflaterStreamNoDataAvailable() throws IOException {
        DeflateCompressorInputStream deflateCompressorInputStream = new DeflateCompressorInputStream(new ByteArrayInputStream(inputData), new DeflateParameters());
        int expected = inputData.length - deflateCompressorInputStream.available();
        assertEquals(expected, deflateCompressorInputStream.available());
    }

    // Test available method with InflaterStream and EOF
    @Test
    public void testAvailableWithInflaterStreamEndOfStream() throws IOException {
        DeflateCompressorInputStream deflateCompressorInputStream = new DeflateCompressorInputStream(new ByteArrayInputStream(inputData), new DeflateParameters());
        byte[] expected = new byte[0];
        byte[] actual = new byte[expected.length];
        int read;
        while ((read = deflateCompressorInputStream.read()) != -1) {
            actual[deflateCompressorInputStream.available()] = (byte) read;
        }
        int expectedAvailable = inputData.length - deflateCompressorInputStream.available();
        assertEquals(expectedAvailable, deflateCompressorInputStream.available());
    }

    // Test skip method
    @Test
    public void testSkip() throws IOException {
        long expected = 10L;
        long actual = deflateCompressorInputStream.skip(expected);
        assertEquals(expected, actual);
    }

    // Test skip method with negative number
    @Test(expected = IllegalArgumentException.class)
    public void testSkipNegativeNumber() throws IOException {
        deflateCompressorInputStream.skip(-1L);
    }

    // Test skip method with zero length
    @Test
    public void testSkipZeroLength() throws IOException {
        long expected = 0L;
        long actual = deflateCompressorInputStream.skip(expected);
        assertEquals(expected, actual);
    }

    // Test skip method with InflaterStream
    @Test
    public void testSkipWithInflaterStream() throws IOException {
        DeflateCompressorInputStream deflateCompressorInputStream = new DeflateCompressorInputStream(new ByteArrayInputStream(inputData), new DeflateParameters());
        long expected = 10L;
        long actual = deflateCompressorInputStream.skip(expected);
        assertEquals(expected, actual);
    }
}
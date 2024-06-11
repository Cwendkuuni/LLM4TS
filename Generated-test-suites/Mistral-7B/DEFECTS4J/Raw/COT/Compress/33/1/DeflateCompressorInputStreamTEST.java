import org.apache.commons.compress.compressors.deflate.DeflateCompressorInputStream;
import org.apache.commons.compress.compressors.deflate.DeflateParameters;
import org.apache.commons.compress.utils.IOUtils;
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
    private DeflateCompressorInputStream deflateInputStream;

    @Before
    public void setUp() throws IOException {
        byte[] compressedData = new byte[]{(byte) 0x78, (byte) 0xDA, (byte) 0x10, (byte) 0x2B, (byte) 0x08, (byte) 0x00, (byte) 0x24, (byte) 0x00, (byte) 0x00, (byte) 0x08, (byte) 0x00, (byte) 0x00, (byte) 0xFF};
        inputStream = new ByteArrayInputStream(compressedData);
        deflateInputStream = new DeflateCompressorInputStream(inputStream);
    }

    @After
    public void tearDown() throws IOException {
        IOUtils.closeQuietly(deflateInputStream);
        IOUtils.closeQuietly(inputStream);
    }

    @Test
    public void testConstructionWithInputStream() {
        DeflateCompressorInputStream deflateInputStream = new DeflateCompressorInputStream(new ByteArrayInputStream(new byte[0]));
        assertNotNull(deflateInputStream);
    }

    @Test
    public void testConstructionWithInputStreamAndParameters() {
        DeflateParameters parameters = new DeflateParameters();
        DeflateCompressorInputStream deflateInputStream = new DeflateCompressorInputStream(new ByteArrayInputStream(new byte[0]), parameters);
        assertNotNull(deflateInputStream);
    }

    @Test
    public void testRead() throws IOException {
        byte[] actualData = new byte[3];
        int read = deflateInputStream.read(actualData);
        assertEquals(-1, read);

        byte[] expectedData = new byte[]{0x2B, 0x08, 0x00};
        read = deflateInputStream.read(actualData, 0, expectedData.length);
        assertArrayEquals(expectedData, actualData);

        read = deflateInputStream.read();
        assertEquals((byte) 0xFF, read);

        read = deflateInputStream.read();
        assertEquals(-1, read);
    }

    @Test
    public void testReadWithByteArray() throws IOException {
        byte[] actualData = new byte[3];
        int read = deflateInputStream.read(actualData);
        assertEquals(-1, read);

        byte[] expectedData = new byte[]{0x2B, 0x08, 0x00};
        read = deflateInputStream.read(actualData, 0, expectedData.length);
        assertArrayEquals(expectedData, actualData);
    }

    @Test
    public void testReadWithOffsetAndLength() throws IOException {
        byte[] actualData = new byte[3];
        int read = deflateInputStream.read(actualData, 1, 2);
        byte[] expectedData = new byte[]{0x08, 0x00};
        assertArrayEquals(expectedData, new byte[]{actualData[1], actualData[2]});
    }

    @Test(expected = IOException.class)
    public void testReadWithNegativeOffset() throws IOException {
        deflateInputStream.read(new byte[1], -1, 1);
    }

    @Test(expected = IOException.class)
    public void testReadWithNegativeLength() throws IOException {
        deflateInputStream.read(new byte[1], 0, -1);
    }

    @Test
    public void testSkip() throws IOException {
        long skipped = deflateInputStream.skip(3L);
        assertEquals(3L, skipped);

        skipped = deflateInputStream.skip(0L);
        assertEquals(0L, skipped);

        skipped = deflateInputStream.skip(-1L);
        assertEquals(-1L, skipped);

        byte[] actualData = new byte[3];
        int read = deflateInputStream.read(actualData, 0, 3);
        assertEquals(3, read);
        long skippedAgain = deflateInputStream.skip(3L);
        assertEquals(6L, skippedAgain);
    }

    @Test
    public void testAvailable() throws IOException {
        assertEquals(0, deflateInputStream.available());

        byte[] actualData = new byte[3];
        int read = deflateInputStream.read(actualData, 0, 3);
        assertEquals(3, read);
        assertEquals(3, deflateInputStream.available());
    }

    @Test
    public void testClose() throws IOException {
        deflateInputStream.close();
        assertThrows(IOException.class, () -> deflateInputStream.read());
    }

    @Test
    public void testSignatureCheckWithZlibHeader() {
        byte[] compressedDataWithZlibHeader = new byte[]{(byte) 0x78, (byte) 0xDA, (byte) 0x10, (byte) 0x2B, (byte) 0x08, (byte) 0x00, (byte) 0x24, (byte) 0x00, (byte) 0x00, (byte) 0x08, (byte) 0x00, (byte) 0x00, (byte) 0xFF, (byte) 0x4D, (byte) 0x5A};
        DeflateCompressorInputStream deflateInputStream = new DeflateCompressorInputStream(new ByteArrayInputStream(compressedDataWithZlibHeader));
        assertTrue(deflateInputStream.isZlibCompressed());
    }

    @Test
    public void testSignatureCheckWithoutZlibHeader() {
        byte[] compressedDataWithoutZlibHeader = new byte[]{(byte) 0x78, (byte) 0xDA, (byte) 0x10, (byte) 0x2B, (byte) 0x08, (byte) 0x00, (byte) 0x24, (byte) 0x00, (byte) 0x00, (byte) 0x08, (byte) 0x00, (byte) 0x00};
        DeflateCompressorInputStream deflateInputStream = new DeflateCompressorInputStream(new ByteArrayInputStream(compressedDataWithoutZlibHeader));
        assertFalse(deflateInputStream.isZlibCompressed());
    }

    @Test(expected = IOException.class)
    public void testInflaterStreamException() throws IOException {
        InputStream inputStream = new ByteArrayInputStream(new byte[]{0x01, 0x02, 0x03});
        DeflateCompressorInputStream deflateInputStream = new DeflateCompressorInputStream(inputStream);
        Inflater inflater = new Inflater();
        InputStream inflaterInputStream = new InflaterInputStream(inputStream, inflater);
        inflater.end();
        deflateInputStream = new DeflateCompressorInputStream(inflaterInputStream);
        deflateInputStream.read();
    }
}
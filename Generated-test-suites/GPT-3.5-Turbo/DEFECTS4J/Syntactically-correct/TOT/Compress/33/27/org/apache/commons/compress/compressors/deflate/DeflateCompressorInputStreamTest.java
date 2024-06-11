import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.junit.Test;

public class DeflateCompressorInputStreamTest {

    @Test
    public void testReadSingleByte() throws IOException {
        byte[] compressedData = {0x78, 0x9c, 0x63, 0x60, 0x00, 0x00, 0x00, 0x00, 0x00, 0xff, 0xff};
        InputStream inputFile = new ByteArrayInputStream(compressedData);
        DeflateCompressorInputStream dcis = new DeflateCompressorInputStream(inputFile);

        assertEquals(0x78, dcis.read());
        assertEquals(0x9c, dcis.read());
        assertEquals(0x63, dcis.read());
        assertEquals(0x60, dcis.read());
        assertEquals(0x00, dcis.read());
        assertEquals(0x00, dcis.read());
        assertEquals(0x00, dcis.read());
        assertEquals(0x00, dcis.read());
        assertEquals(0x00, dcis.read());
        assertEquals(0xff, dcis.read());
        assertEquals(0xff, dcis.read());

        dcis.close();
    }

    @Test
    public void testReadByteArray() throws IOException {
        byte[] compressedData = {0x78, 0x9c, 0x63, 0x60, 0x00, 0x00, 0x00, 0x00, 0x00, 0xff, 0xff};
        InputStream inputFile = new ByteArrayInputStream(compressedData);
        DeflateCompressorInputStream dcis = new DeflateCompressorInputStream(inputFile);

        byte[] output = new byte[10];
        dcis.read(output, 0, 10);

        byte[] expectedOutput = {0x78, 0x9c, 0x63, 0x60, 0x00, 0x00, 0x00, 0x00, 0x00, 0xff};

        assertArrayEquals(expectedOutput, output);

        dcis.close();
    }

    @Test
    public void testSkipBytes() throws IOException {
        byte[] compressedData = {0x78, 0x9c, 0x63, 0x60, 0x00, 0x00, 0x00, 0x00, 0x00, 0xff, 0xff};
        InputStream inputFile = new ByteArrayInputStream(compressedData);
        DeflateCompressorInputStream dcis = new DeflateCompressorInputStream(inputFile);

        dcis.skip(5);

        assertEquals(0x00, dcis.read());
        assertEquals(0x00, dcis.read());
        assertEquals(0x00, dcis.read());
        assertEquals(0xff, dcis.read());
        assertEquals(0xff, dcis.read());

        dcis.close();
    }

    @Test
    public void testAvailable() throws IOException {
        byte[] compressedData = {0x78, 0x9c, 0x63, 0x60, 0x00, 0x00, 0x00, 0x00, 0x00, 0xff, 0xff};
        InputStream inputFile = new ByteArrayInputStream(compressedData);
        DeflateCompressorInputStream dcis = new DeflateCompressorInputStream(inputFile);

        assertEquals(11, dcis.available());

        dcis.close();
    }

    @Test
    public void testClose() throws IOException {
        byte[] compressedData = {0x78, 0x9c, 0x63, 0x60, 0x00, 0x00, 0x00, 0x00, 0x00, 0xff, 0xff};
        InputStream inputFile = new ByteArrayInputStream(compressedData);
        DeflateCompressorInputStream dcis = new DeflateCompressorInputStream(inputFile);

        dcis.close();

        assertTrue(inputFile.isClosed());
    }

    @Test
    public void testWithZlibHeader() throws IOException {
        byte[] compressedData = {0x78, 0x9c, 0x63, 0x60, 0x00, 0x00, 0x00, 0x00, 0x00, 0xff, 0xff};
        InputStream inputFile = new ByteArrayInputStream(compressedData);
        DeflateParameters parameters = new DeflateParameters();
        DeflateCompressorInputStream dcis = new DeflateCompressorInputStream(inputFile, parameters);

        assertFalse(parameters.withZlibHeader());

        dcis.close();
    }
}
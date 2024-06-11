package org.apache.commons.compress.compressors.deflate.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.DataFormatException;
import java.util.zip.Inflater;
import java.util.zip.ZipEntry;

import org.apache.commons.compress.compressors.CompressorInputStream;
import org.apache.commons.compress.compressors.DeflateCompressorInputStream;
import org.apache.commons.compress.compressors.DeflateParameters;
import org.apache.commons.compress.utils.IOUtils;
import org.junit.Test;

/**
 * Test cases for DeflateCompressorInputStream class.
 */
public class DeflateCompressorInputStreamTest {

    /**
     * Test constructor with valid InputStream.
     * @throws IOException
     */
    @Test
    public void testConstructorWithValidInputStream() throws IOException {
        InputStream inputStream = mock(InputStream.class);
        DeflateCompressorInputStream deflateCompressorInputStream = new DeflateCompressorInputStream(inputStream);
        assertNotNull(deflateCompressorInputStream);
    }

    /**
     * Test constructor with null InputStream.
     * @throws IOException
     */
    @Test(expected = NullPointerException.class)
    public void testConstructorWithNullInputStream() throws IOException {
        new DeflateCompressorInputStream(null);
    }

    /**
     * Test read method with valid byte array.
     * @throws IOException
     */
    @Test
    public void testReadWithValidByteArray() throws IOException {
        InputStream inputStream = new ByteArrayInputStream(new byte[] { 0x00, 0x01 });
        DeflateCompressorInputStream deflateCompressorInputStream = new DeflateCompressorInputStream(inputStream);
        int read = deflateCompressorInputStream.read();
        assertEquals(0x00, read);
    }

    /**
     * Test read method with EOF.
     * @throws IOException
     */
    @Test
    public void testReadWithEndOfFile() throws IOException {
        InputStream inputStream = new ByteArrayInputStream(new byte[] { 0x00, 0x01 });
        DeflateCompressorInputStream deflateCompressorInputStream = new DeflateCompressorInputStream(inputStream);
        deflateCompressorInputStream.read();
        deflateCompressorInputStream.read();
        int read = deflateCompressorInputStream.read();
        assertEquals(-1, read);
    }

    /**
     * Test read method with valid byte array and buffer.
     * @throws IOException
     */
    @Test
    public void testReadWithValidByteArrayAndBuffer() throws IOException {
        byte[] input = new byte[] { 0x00, 0x01, 0x02, 0x03 };
        InputStream inputStream = new ByteArrayInputStream(input);
        DeflateCompressorInputStream deflateCompressorInputStream = new DeflateCompressorInputStream(inputStream);
        byte[] buffer = new byte[input.length];
        int read = deflateCompressorInputStream.read(buffer);
        assertEquals(input.length, read);
        for (int i = 0; i < input.length; i++) {
            assertEquals(input[i], buffer[i]);
        }
    }

    /**
     * Test read method with empty buffer.
     * @throws IOException
     */
    @Test
    public void testReadWithEmptyBuffer() throws IOException {
        InputStream inputStream = new ByteArrayInputStream(new byte[] { 0x00, 0x01 });
        DeflateCompressorInputStream deflateCompressorInputStream = new DeflateCompressorInputStream(inputStream);
        byte[] buffer = new byte[0];
        int read = deflateCompressorInputStream.read(buffer);
        assertEquals(0, read);
    }

    /**
     * Test skip method.
     * @throws IOException
     */
    @Test
    public void testSkip() throws IOException {
        InputStream inputStream = new ByteArrayInputStream(new byte[] { 0x00, 0x01 });
        DeflateCompressorInputStream deflateCompressorInputStream = new DeflateCompressorInputStream(inputStream);
        long skipped = deflateCompressorInputStream.skip(5);
        assertEquals(5, skipped);
    }

    /**
     * Test available method.
     * @throws IOException
     */
    @Test
    public void testAvailable() throws IOException {
        InputStream inputStream = new ByteArrayInputStream(new byte[] { 0x00, 0x01 });
        DeflateCompressorInputStream deflateCompressorInputStream = new DeflateCompressorInputStream(inputStream);
        long available = deflateCompressorInputStream.available();
        assertEquals(2, available);
    }

    /**
     * Test close method.
     * @throws IOException
     */
    @Test
    public void testClose() throws IOException {
        InputStream inputStream = new ByteArrayInputStream(new byte[] { 0x00, 0x01 });
        DeflateCompressorInputStream deflateCompressorInputStream = new DeflateCompressorInputStream(inputStream);
        deflateCompressorInputStream.close();
    }

    /**
     * Test constructor with valid InputStream and DeflateParameters.
     * @throws IOException
     */
    @Test
    public void testConstructorWithValidInputStreamAndDeflateParameters() throws IOException {
        InputStream inputStream = mock(InputStream.class);
        DeflateParameters deflateParameters = new DeflateParameters();
        DeflateCompressorInputStream deflateCompressorInputStream = new DeflateCompressorInputStream(inputStream, deflateParameters);
        assertNotNull(deflateCompressorInputStream);
        assertFalse(deflateCompressorInputStream.getIn().markSupported());
    }

    /**
     * Test constructor with null DeflateParameters.
     * @throws IOException
     */
    @Test(expected = NullPointerException.class)
    public void testConstructorWithNullDeflateParameters() throws IOException {
        InputStream inputStream = mock(InputStream.class);
        new DeflateCompressorInputStream(inputStream, null);
    }

    /**
     * Test exception when reading invalid data from the input stream.
     * @throws IOException
     * @throws DataFormatException
     */
    @Test(expected = DataFormatException.class)
    public void testExceptionWhenReadingInvalidDataFromInputStream() throws IOException, DataFormatException {
        InputStream inputStream = new ByteArrayInputStream(new byte[] { 0x00, 0x01, 0x02, 0x03, 0x04, 0x05, (byte) 0xff });
        DeflateCompressorInputStream deflateCompressorInputStream = new DeflateCompressorInputStream(inputStream);
        deflateCompressorInputStream.read();
    }

    /**
     * Test exception when closing the input stream twice.
     * @throws IOException
     */
    @Test(expected = IOException.class)
    public void testExceptionWhenClosingTheInputStreamTwice() throws IOException {
        InputStream inputStream = mock(InputStream.class);
        DeflateCompressorInputStream deflateCompressorInputStream = new DeflateCompressorInputStream(inputStream);
        deflateCompressorInputStream.close();
        deflateCompressorInputStream.close();
    }
}
package org.apache.commons.codec.binary;

import static org.apache.commons.codec.binary.BaseNCodec.EOF;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.codec.binary.BaseNCodec;
import org.apache.commons.codec.binary.BaseNCodecInputStream;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * Test class for {@link BaseNCodecInputStream}
 */
public class BaseNCodecInputStreamTest {

    private InputStream inputStream;
    private BaseNCodecInputStream baseNInputStream;
    private BaseNCodec baseNCodec;

    @Before
    public void setUp() throws Exception {
        byte[] inputBytes = "BaseNCodeTestData".getBytes();
        baseNCodec = new BaseNCodec();
        inputStream = new ByteArrayInputStream(inputBytes);
        baseNInputStream = new BaseNCodecInputStream(inputStream, baseNCodec, false);
    }

    @After
    public void tearDown() throws Exception {
        if (baseNInputStream != null) {
            baseNInputStream.close();
        }
        if (inputStream != null) {
            inputStream.close();
        }
        baseNCodec = null;
        inputStream = null;
        baseNInputStream = null;
    }

    @Test
    public void testRead() throws IOException {
        byte expectedByte = 'B';
        byte readByte;

        readByte = (byte) baseNInputStream.read();

        assertEquals(expectedByte, readByte);

        // read EOF
        readByte = (byte) baseNInputStream.read();
        assertEquals(EOF, readByte);
    }

    @Test
    public void testReadArray() throws IOException {
        byte[] expectedBytes = new byte[] { 'B', 'a', 's', 'e' };
        byte[] readBytes = new byte[4];
        int readLength;

        readLength = baseNInputStream.read(readBytes);

        assertEquals(4, readLength);
        assertArrayEquals(expectedBytes, readBytes);

        // read EOF
        readLength = baseNInputStream.read(readBytes);
        assertEquals(-1, readLength);
    }

    @Test(expected = NullPointerException.class)
    public void testReadArrayNull() throws IOException {
        baseNInputStream.read(null);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testReadArrayNegativeOffset() throws IOException {
        baseNInputStream.read(new byte[1], -1, 1);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testReadArrayNegativeLength() throws IOException {
        baseNInputStream.read(new byte[1], 0, -1);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testReadArrayInvalidOffsetLength() throws IOException {
        byte[] readBytes = new byte[1];
        baseNInputStream.read(readBytes, 2, 1);
    }

    @Test(expected = IOException.class)
    public void testMarkSupported() throws IOException {
        baseNInputStream.markSupported();
    }

    @Test
    public void testMark() throws IOException {
        // not supported
        assertFalse(baseNInputStream.markSupported());

        // try to reset
        baseNInputStream.mark(0);
        assertNull(baseNInputStream.getMark());
    }

    @Test
    public void testClose() throws IOException {
        byte[] expectedBytes = new byte[] { 'B', 'a', 's', 'e' };
        byte[] readBytes = new byte[4];
        int readLength;

        // read data
        readLength = baseNInputStream.read(readBytes);
        assertArrayEquals(expectedBytes, readBytes);

        // close stream
        baseNInputStream.close();

        // try to read
        readLength = baseNInputStream.read(readBytes);
        assertEquals(-1, readLength);
    }

    @Test
    public void testEncode() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        BaseNCodec baseNCodecEncode = new BaseNCodec(true);
        InputStream inputStreamEncode = new ByteArrayInputStream("TestData".getBytes());
        BaseNCodecInputStream baseNInputStreamEncode = new BaseNCodecInputStream(inputStreamEncode,
                baseNCodecEncode, true);

        byte[] expectedBytes = new byte[] { 'T', 'e', 's', 't', 'D', 'a', 't', 'a', ' ', 'T', 'e', 's', 't' };

        byte[] readBytes = new byte[11];
        int readLength;

        // read data
        while (readLength > 0) {
            readLength = baseNInputStreamEncode.read(readBytes);
            outputStream.write(readBytes, 0, readLength);
        }

        outputStream.flush();
        byte[] actualBytes = outputStream.toByteArray();

        assertArrayEquals(expectedBytes, actualBytes);

        // close streams
        baseNInputStreamEncode.close();
        outputStream.close();
    }
}
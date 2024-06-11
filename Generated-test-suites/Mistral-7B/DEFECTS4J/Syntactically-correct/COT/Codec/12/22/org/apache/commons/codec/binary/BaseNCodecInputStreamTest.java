package org.apache.commons.codec.binary;

import static org.apache.commons.codec.binary.BaseNCodecInputStream.EOF;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.codec.binary.BaseNCodec;
import org.apache.commons.codec.binary.BaseNCodecInputStream;

/**
 * Test class for BaseNCodecInputStream.
 */
public class BaseNCodecInputStreamTest {

    /**
     * Test constructor.
     */
    @org.junit.Test
    public void testConstructor() {
        InputStream input = new ByteArrayInputStream(new byte[] { 0x01 });
        BaseNCodec baseNCodec = new BaseNCodec();
        BaseNCodecInputStream inputStream = new BaseNCodecInputStream(input, baseNCodec, false);
        assertEquals(inputStream.getClass(), BaseNCodecInputStream.class);
        assertEquals(inputStream.doEncode, false);
        assertEquals(inputStream.baseNCodec, baseNCodec);
    }

    /**
     * Test read method with one byte.
     */
    @org.junit.Test
    public void testReadOneByte() throws IOException {
        InputStream input = new ByteArrayInputStream(new byte[] { 0x01 });
        BaseNCodec baseNCodec = new BaseNCodec();
        BaseNCodecInputStream inputStream = new BaseNCodecInputStream(input, baseNCodec, false);
        byte[] buffer = new byte[1];
        int bytesRead = inputStream.read(buffer, 0, 1);
        assertEquals(bytesRead, 1);
        assertEquals(buffer[0], 0x01);
        assertEquals(inputStream.read(), EOF);
    }

    /**
     * Test read method with larger buffer.
     */
    @org.junit.Test
    public void testReadLargerBuffer() throws IOException {
        InputStream input = new ByteArrayInputStream(new byte[] { 0x01, 0x02, 0x03 });
        BaseNCodec baseNCodec = new BaseNCodec();
        BaseNCodecInputStream inputStream = new BaseNCodecInputStream(input, baseNCodec, false);
        byte[] buffer = new byte[3];
        int bytesRead = inputStream.read(buffer, 0, 3);
        assertEquals(bytesRead, 3);
        assertEquals(buffer[0], 0x01);
        assertEquals(buffer[1], 0x02);
        assertEquals(buffer[2], 0x03);
        assertEquals(inputStream.read(), EOF);
    }

    /**
     * Test markSupported method.
     */
    @org.junit.Test(expected = UnsupportedOperationException.class)
    public void testMarkSupported() throws IOException {
        InputStream input = new ByteArrayInputStream(new byte[] { 0x01 });
        BaseNCodec baseNCodec = new BaseNCodec();
        BaseNCodecInputStream inputStream = new BaseNCodecInputStream(input, baseNCodec, false);
        assertTrue(inputStream.markSupported()); // Should throw UnsupportedOperationException
    }

    /**
     * Test read method with empty buffer.
     */
    @org.junit.Test
    public void testReadEmptyBuffer() throws IOException {
        InputStream input = new ByteArrayInputStream(new byte[] {});
        BaseNCodec baseNCodec = new BaseNCodec();
        BaseNCodecInputStream inputStream = new BaseNCodecInputStream(input, baseNCodec, false);
        byte[] buffer = new byte[0];
        int bytesRead = inputStream.read(buffer, 0, 0);
        assertEquals(bytesRead, 0);
    }

    /**
     * Test read method with negative offset.
     */
    @org.junit.Test(expected = IndexOutOfBoundsException.class)
    public void testReadNegativeOffset() throws IOException {
        InputStream input = new ByteArrayInputStream(new byte[] { 0x01 });
        BaseNCodec baseNCodec = new BaseNCodec();
        BaseNCodecInputStream inputStream = new BaseNCodecInputStream(input, baseNCodec, false);
        byte[] buffer = new byte[1];
        int offset = -1;
        int len = 1;
        inputStream.read(buffer, offset, len);
    }

    /**
     * Test read method with negative length.
     */
    @org.junit.Test(expected = IndexOutOfBoundsException.class)
    public void testReadNegativeLength() throws IOException {
        InputStream input = new ByteArrayInputStream(new byte[] { 0x01 });
        BaseNCodec baseNCodec = new BaseNCodec();
        BaseNCodecInputStream inputStream = new BaseNCodecInputStream(input, baseNCodec, false);
        byte[] buffer = new byte[1];
        int offset = 0;
        int len = -1;
        inputStream.read(buffer, offset, len);
    }

    /**
     * Test read method with buffer size larger than remaining data.
     */
    @org.junit.Test
    public void testReadBufferSizeLargerThanRemainingData() throws IOException {
        InputStream input = new ByteArrayInputStream(new byte[] { 0x01 });
        BaseNCodec baseNCodec = new BaseNCodec();
        BaseNCodecInputStream inputStream = new BaseNCodecInputStream(input, baseNCodec, false);
        byte[] buffer = new byte[100];
        int bytesRead = inputStream.read(buffer, 0, buffer.length);
        assertEquals(bytesRead, 1);
        assertEquals(buffer[0], 0x01);
        assertEquals(inputStream.read(), EOF);
    }

    /**
     * Test read with EOF.
     */
    @org.junit.Test
    public void testReadWithEOF() throws IOException {
        InputStream input = new ByteArrayInputStream(new byte[] { 0x01 });
        BaseNCodec baseNCodec = new BaseNCodec();
        BaseNCodecInputStream inputStream = new BaseNCodecInputStream(input, baseNCodec, false);
        byte[] buffer = new byte[1];
        int bytesRead = inputStream.read(buffer, 0, 1);
        assertEquals(bytesRead, 1);
        assertEquals(buffer[0], 0x01);
        assertEquals(inputStream.read(), EOF);
    }

    /**
     * Test read method with decoding.
     */
    @org.junit.Test
    public void testReadWithDecoding() throws IOException {
        InputStream input = new ByteArrayInputStream(new byte[] { 0x30, 0x31, 0x32, 0x33 });
        BaseNCodec baseNCodec = new BaseNCodec();
        BaseNCodecInputStream inputStream = new BaseNCodecInputStream(input, baseNCodec, false);
        byte[] buffer = new byte[4];
        int bytesRead = inputStream.read(buffer, 0, 4);
        assertEquals(bytesRead, 4);
        assertEquals(new String(buffer), "0123");
        assertEquals(inputStream.read(), EOF);
    }

    /**
     * Test read method with encoding.
     */
    @org.junit.Test
    public void testReadWithEncoding() throws IOException {
        InputStream input = new ByteArrayInputStream(new byte[] { 0x31, 0x32, 0x33, 0x34 });
        BaseNCodec baseNCodec = new BaseNCodec();
        BaseNCodecInputStream inputStream = new BaseNCodecInputStream(input, baseNCodec, true);
        byte[] buffer = new byte[10];
        int bytesRead = inputStream.read(buffer, 0, 10);
        assertEquals(bytesRead, 8);
        assertEquals(new String(buffer), "01234567");
        assertEquals(inputStream.read(), EOF);
    }

    /**
     * Test read method with decoding and EOF.
     */
    @org.junit.Test
    public void testReadWithDecodingAndEOF() throws IOException {
        InputStream input = new ByteArrayInputStream(new byte[] { 0x30, 0x31, 0x32, 0x33 });
        BaseNCodec baseNCodec = new BaseNCodec();
        BaseNCodecInputStream inputStream = new BaseNCodecInputStream(input, baseNCodec, false);
        byte[] buffer = new byte[4];
        int bytesRead = inputStream.read(buffer, 0, 4);
        assertEquals(bytesRead, 3);
        assertEquals(new String(buffer), "012");
        assertEquals(inputStream.read(), EOF);
    }

    /**
     * Test read method with encoding and EOF.
     */
    @org.junit.Test
    public void testReadWithEncodingAndEOF() throws IOException {
        InputStream input = new ByteArrayInputStream(new byte[] { 0x31, 0x32, 0x33, 0x34 });
        BaseNCodec baseNCodec = new BaseNCodec();
        BaseNCodecInputStream inputStream = new BaseNCodecInputStream(input, baseNCodec, true);
        byte[] buffer = new byte[10];
        int bytesRead = inputStream.read(buffer, 0, 10);
        assertEquals(bytesRead, 8);
        assertEquals(new String(buffer), "01234567");
        assertEquals(inputStream.read(), EOF);
    }

    /**
     * Test read method with decoding and empty input.
     */
    @org.junit.Test
    public void testReadWithDecodingAndEmptyInput() throws IOException {
        InputStream input = new ByteArrayInputStream(new byte[] {});
        BaseNCodec baseNCodec = new BaseNCodec();
        BaseNCodecInputStream inputStream = new BaseNCodecInputStream(input, baseNCodec, false);
        byte[] buffer = new byte[1];
        int bytesRead = inputStream.read(buffer, 0, 1);
        assertEquals(bytesRead, 0);
    }

    /**
     * Test read method with encoding and empty input.
     */
    @org.junit.Test
    public void testReadWithEncodingAndEmptyInput() throws IOException {
        InputStream input = new ByteArrayInputStream(new byte[] {});
        BaseNCodec baseNCodec = new BaseNCodec();
        BaseNCodecInputStream inputStream = new BaseNCodecInputStream(input, baseNCodec, true);
        byte[] buffer = new byte[1];
        int bytesRead = inputStream.read(buffer, 0, 1);
        assertEquals(bytesRead, 0);
    }

    /**
     * Test skip method.
     */
    @org.junit.Test
    public void testSkip() throws IOException {
        InputStream input = new ByteArrayInputStream(new byte[] { 0x01, 0x02, 0x03 });
        BaseNCodec baseNCodec = new BaseNCodec();
        BaseNCodecInputStream inputStream = new BaseNCodecInputStream(input, baseNCodec, false);
        assertEquals(inputStream.skip(1), 1L);
        assertEquals(inputStream.read(), 0x02);
        assertEquals(inputStream.skip(2), 2L);
        assertEquals(inputStream.read(), EOF);
    }

    /**
     * Test skip with EOF.
     */
    @org.junit.Test
    public void testSkipWithEOF() throws IOException {
        InputStream input = new ByteArrayInputStream(new byte[] { 0x01, 0x02, 0x03 });
        BaseNCodec baseNCodec = new BaseNCodec();
        BaseNCodecInputStream inputStream = new BaseNCodecInputStream(input, baseNCodec, false);
        assertEquals(inputStream.skip(10), 10L);
        assertEquals(inputStream.read(), EOF);
    }

    /**
     * Test skip with negative length.
     */
    @org.junit.Test(expected = IllegalArgumentException.class)
    public void testSkipWithNegativeLength() throws IOException {
        InputStream input = new ByteArrayInputStream(new byte[] { 0x01, 0x02, 0x03 });
        BaseNCodec baseNCodec = new BaseNCodec();
        BaseNCodecInputStream inputStream = new BaseNCodecInputStream(input, baseNCodec, false);
        inputStream.skip(-1);
    }
}
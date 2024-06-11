package org.apache.commons.codec.binary;

import static org.apache.commons.codec.binary.BaseNCodecInputStream.EOF;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.codec.binary.Base32;
import org.apache.commons.codec.binary.BaseNCodec;
import org.junit.Test;

/**
 * Test class for BaseNCodecInputStream.
 */
public class BaseNCodecInputStreamTest {

    /**
     * Test constructor.
     */
    @Test
    public void testConstructor() {
        InputStream input = new ByteArrayInputStream(new byte[] { 0x01, 0x02, 0x03 });
        BaseNCodec codec = new BaseNCodec();
        BaseNCodecInputStream inputStream = new BaseNCodecInputStream(input, codec, false);
        assertNotNull(inputStream);
    }

    /**
     * Test read method.
     */
    @Test
    public void testRead() throws IOException {
        InputStream input = new ByteArrayInputStream(new byte[] { 0x01, 0x02, 0x03 });
        BaseNCodec codec = new BaseNCodec();
        BaseNCodecInputStream inputStream = new BaseNCodecInputStream(input, codec, false);

        int result = inputStream.read();
        assertEquals(0x01, result);

        result = inputStream.read();
        assertEquals(0x02, result);

        result = inputStream.read();
        assertEquals(0x03, result);

        result = inputStream.read();
        assertEquals(BaseNCodecInputStream.EOF, result);
    }

    /**
     * Test read method with encoding.
     */
    @Test
    public void testReadWithEncoding() throws IOException {
        InputStream input = new ByteArrayInputStream(new byte[] { 0x01, 0x02, 0x03 });
        BaseNCodec codec = new Base32();
        BaseNCodecInputStream inputStream = new BaseNCodecInputStream(input, codec, true);

        byte[] result = new byte[3];
        assertEquals(3, inputStream.read(result, 0, result.length));
        assertArrayEquals(new byte[] { '4', 'q', 'W' }, result);
    }

    /**
     * Test read method with wrong input length.
     */
    @Test(expected = IOException.class)
    public void testReadWithWrongInputLength() throws IOException {
        InputStream input = new ByteArrayInputStream(new byte[] { 0x01, 0x02, 0x03 });
        BaseNCodec codec = new BaseNCodec();
        BaseNCodecInputStream inputStream = new BaseNCodecInputStream(input, codec, false);

        byte[] result = new byte[5];
        inputStream.read(result, 0, 5);
    }

    /**
     * Test markSupported method.
     */
    @Test
    public void testMarkSupported() {
        InputStream input = new ByteArrayInputStream(new byte[] { 0x01, 0x02, 0x03 });
        BaseNCodec codec = new BaseNCodec();
        BaseNCodecInputStream inputStream = new BaseNCodecInputStream(input, codec, false);

        assertFalse(inputStream.markSupported());
    }

    /**
     * Test skip method.
     */
    @Test
    public void testSkip() throws IOException {
        InputStream input = new ByteArrayInputStream(new byte[] { 0x01, 0x02, 0x03 });
        BaseNCodec codec = new BaseNCodec();
        BaseNCodecInputStream inputStream = new BaseNCodecInputStream(input, codec, false);

        inputStream.skip(5);

        int result = inputStream.read();
        assertEquals(BaseNCodecInputStream.EOF, result);
    }

    /**
     * Test available method.
     */
    @Test
    public void testAvailable() throws IOException {
        InputStream input = new ByteArrayInputStream(new byte[] { 0x01, 0x02, 0x03 });
        BaseNCodec codec = new BaseNCodec();
        BaseNCodecInputStream inputStream = new BaseNCodecInputStream(input, codec, false);

        int result = inputStream.available();
        assertEquals(3, result);
    }

    /**
     * Test close method.
     */
    @Test
    public void testClose() throws IOException {
        InputStream input = new ByteArrayInputStream(new byte[] { 0x01, 0x02, 0x03 });
        BaseNCodec codec = new BaseNCodec();
        BaseNCodecInputStream inputStream = new BaseNCodecInputStream(input, codec, false);

        inputStream.close();

        assertThrows(IOException.class, () -> inputStream.read());
    }

    /**
     * Test decode method.
     */
    @Test
    public void testDecode() throws IOException {
        InputStream input = new ByteArrayInputStream(new byte[] { 0x03, 0x04, 0x05 });
        BaseNCodec codec = new BaseNCodec();
        BaseNCodecInputStream inputStream = new BaseNCodecInputStream(input, codec, false);

        byte[] result = new byte[3];
        assertEquals(3, inputStream.read(result, 0, result.length));

        assertEquals(0x01, result[0]);
        assertEquals(0x02, result[1]);
        assertEquals(0x03, result[2]);
    }

    /**
     * Test encode method.
     */
    @Test
    public void testEncode() throws IOException {
        InputStream input = new ByteArrayInputStream(new byte[] { 0x01, 0x02, 0x03 });
        BaseNCodec codec = new Base32();
        BaseNCodecInputStream inputStream = new BaseNCodecInputStream(input, codec, true);

        ByteArrayOutputStream output = new ByteArrayOutputStream();
        byte[] buffer = new byte[512];

        int read = 0;
        while ((read = inputStream.read(buffer)) > 0) {
            output.write(buffer, 0, read);
        }

        byte[] result = output.toByteArray();
        assertArrayEquals(new byte[] { 0x30, 0x2a, 0x34, (byte) 0x83, 0x31, 0x32, 0x33, 0x34, 0x35 }, result);
    }

    /**
     * Test hasData method.
     */
    @Test
    public void testHasData() throws IOException {
        InputStream input = new ByteArrayInputStream(new byte[] { 0x01, 0x02, 0x03 });
        BaseNCodec codec = new BaseNCodec();
        BaseNCodecInputStream inputStream = new BaseNCodecInputStream(input, codec, false);

        assertTrue(inputStream.hasData());

        byte[] buffer = new byte[512];
        int bytesRead = inputStream.read(buffer, 0, buffer.length);

        assertFalse(inputStream.hasData());
    }

    /**
     * Test readResults method with decoding.
     */
    @Test
    public void testReadResultsWithDecoding() throws IOException {
        InputStream input = new ByteArrayInputStream(new byte[] { 0x30, 0x2a, 0x34, 0x83, 0x31, 0x32, 0x33, 0x34, 0x35 });
        BaseNCodec codec = new BaseNCodec();
        BaseNCodecInputStream inputStream = new BaseNCodecInputStream(input, codec, false);

        byte[] buffer = new byte[5];
        int bytesRead = inputStream.read(buffer, 0, buffer.length);

        assertEquals(3, bytesRead);
        assertArrayEquals(new byte[] { 0x01, 0x02, 0x03 }, buffer);

        bytesRead = inputStream.read(buffer, 0, buffer.length);
        assertEquals(1, bytesRead);
        assertEquals(0x04, buffer[0]);
    }

    /**
     * Test readResults method with encoding.
     */
    @Test
    public void testReadResultsWithEncoding() throws IOException {
        InputStream input = new ByteArrayInputStream(new byte[] { 0x01, 0x02, 0x03 });
        BaseNCodec codec = new Base32();
        BaseNCodecInputStream inputStream = new BaseNCodecInputStream(input, codec, true);

        byte[] buffer = new byte[5];
        int bytesRead = inputStream.read(buffer, 0, buffer.length);

        assertEquals(3, bytesRead);
        assertArrayEquals(new byte[] { '4', 'q', 'W' }, buffer);

        bytesRead = inputStream.read(buffer, 0, buffer.length);
        assertEquals(1, bytesRead);
        assertEquals(0x05, buffer[0]);
    }

    /**
     * Test readResults method with empty input.
     */
    @Test
    public void testReadResultsWithEmptyInput() throws IOException {
        InputStream input = new ByteArrayInputStream(new byte[] {});
        BaseNCodec codec = new BaseNCodec();
        BaseNCodecInputStream inputStream = new BaseNCodecInputStream(input, codec, false);

        byte[] buffer = new byte[5];
        int bytesRead = inputStream.read(buffer, 0, buffer.length);

        assertEquals(0, bytesRead);
    }

    /**
     * Test readResults method with illegal input length.
     */
    @Test(expected = IOException.class)
    public void testReadResultsWithIllegalInputLength() throws IOException {
        InputStream input = new ByteArrayInputStream(new byte[] { 0x01, 0x02, 0x03 });
        BaseNCodec codec = new BaseNCodec();
        BaseNCodecInputStream inputStream = new BaseNCodecInputStream(input, codec, false);

        byte[] buffer = new byte[1];
        int bytesRead = inputStream.read(buffer, 0, 2);
    }

    /**
     * Test readResults method with no more data available.
     */
    @Test
    public void testReadResultsWithNoMoreDataAvailable() throws IOException {
        InputStream input = new ByteArrayInputStream(new byte[] { 0x01, 0x02, 0x03 });
        BaseNCodec codec = new BaseNCodec();
        BaseNCodecInputStream inputStream = new BaseNCodecInputStream(input, codec, false);

        byte[] buffer = new byte[5];
        int bytesRead = inputStream.read(buffer, 0, buffer.length);

        assertEquals(3, bytesRead);
        assertArrayEquals(new byte[] { 0x01, 0x02, 0x03 }, buffer);

        bytesRead = inputStream.read(buffer, 0, buffer.length);

        assertEquals(0, bytesRead);
    }
}
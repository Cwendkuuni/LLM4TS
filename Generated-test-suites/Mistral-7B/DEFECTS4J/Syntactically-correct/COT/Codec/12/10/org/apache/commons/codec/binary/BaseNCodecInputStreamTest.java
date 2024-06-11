package org.apache.commons.codec.binary;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import org.apache.commons.codec.binary.BaseNCodec;
import org.apache.commons.codec.binary.BaseNCodecInputStream;

/**
 * Test class for BaseNCodecInputStream
 */
public class BaseNCodecInputStreamTest {

    /**
     * Test constructor.
     */
    @org.junit.Test
    public void testConstructor() {
        InputStream in = new ByteArrayInputStream(new byte[] { 0x01 });
        BaseNCodec baseNCodec = new BaseNCodec();
        BaseNCodecInputStream inputStream = new BaseNCodecInputStream(in, baseNCodec, false);
        assertEquals("Expected BaseNCodecInputStream instance", inputStream.getClass(), BaseNCodecInputStream.class);
        assertEquals("Expected baseNCodec instance", baseNCodec, inputStream.baseNCodec);
        assertFalse("Expected doEncode to be false", inputStream.doEncode);
    }

    /**
     * Test read method with single byte.
     *
     * @throws IOException
     */
    @org.junit.Test
    public void testReadSingleByte() throws IOException {
        InputStream in = new ByteArrayInputStream(new byte[] { 0x01 });
        BaseNCodec baseNCodec = new BaseNCodec();
        BaseNCodecInputStream inputStream = new BaseNCodecInputStream(in, baseNCodec, false);

        int readByte = inputStream.read();

        assertTrue("Expected read byte to be greater than or equal to 0", readByte >= 0);
        assertEquals("Expected read byte to be equal to 0x01", readByte, 0x01);
    }

    /**
     * Test read method with EOF.
     *
     * @throws IOException
     */
    @org.junit.Test
    public void testReadEndOfFile() throws IOException {
        InputStream in = new ByteArrayInputStream(new byte[0]);
        BaseNCodec baseNCodec = new BaseNCodec();
        BaseNCodecInputStream inputStream = new BaseNCodecInputStream(in, baseNCodec, false);

        int readByte = inputStream.read();

        assertEquals("Expected EOF", -1, readByte);
    }

    /**
     * Test read method with empty buffer.
     *
     * @throws IOException
     */
    @org.junit.Test
    public void testReadEmptyBuffer() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        byte[] encodedByteArray = new byte[] { 0x01, 0x02, 0x03, 0x04, 0x05 };
        BaseNCodec baseNCodec = new BaseNCodec();
        BaseNCodecInputStream inputStream = new BaseNCodecInputStream(new ByteArrayInputStream(encodedByteArray), baseNCodec, true);

        byte[] buffer = new byte[4];
        int lengthRead = inputStream.read(buffer);

        assertEquals("Expected lengthRead to be equal to encodedByteArray.length", encodedByteArray.length, lengthRead);

        for (int i = 0; i < encodedByteArray.length; i++) {
            assertEquals("Expected byte at index " + i + " to be equal to encodedByteArray[" + i + "]", encodedByteArray[i], buffer[i]);
        }

        // Empty buffer
        lengthRead = inputStream.read(buffer);
        assertEquals("Expected lengthRead to be equal to 0", 0, lengthRead);
    }

    /**
     * Test markSupported method.
     */
    @org.junit.Test
    public void testMarkSupported() {
        InputStream in = new ByteArrayInputStream(new byte[] { 0x01 });
        BaseNCodec baseNCodec = new BaseNCodec();
        BaseNCodecInputStream inputStream = new BaseNCodecInputStream(in, baseNCodec, false);
        assertFalse("Expected markSupported to return false", inputStream.markSupported());
    }

    /**
     * Test skip method with negative length.
     *
     * @throws IOException
     */
    @org.junit.Test(expected = IllegalArgumentException.class)
    public void testSkipNegativeLength() throws IOException {
        InputStream in = new ByteArrayInputStream(new byte[] { 0x01 });
        BaseNCodec baseNCodec = new BaseNCodec();
        BaseNCodecInputStream inputStream = new BaseNCodecInputStream(in, baseNCodec, false);

        inputStream.skip(-512);
    }

    /**
     * Test skip method with positive length.
     *
     * @throws IOException
     */
    @org.junit.Test
    public void testSkipPositiveLength() throws IOException {
        byte[] encodedByteArray = new byte[] { 0x01, 0x02, 0x03, 0x04, 0x05 };
        InputStream in = new ByteArrayInputStream(encodedByteArray);
        BaseNCodec baseNCodec = new BaseNCodec();
        BaseNCodecInputStream inputStream = new BaseNCodecInputStream(in, baseNCodec, true);

        inputStream.skip(512);

        byte[] buffer = new byte[5];
        int lengthRead = inputStream.read(buffer);

        assertEquals("Expected lengthRead to be less than or equal to 0", 0, lengthRead);
    }

    /**
     * Test availability method.
     *
     * @throws IOException
     */
    @org.junit.Test
    public void testAvailability() throws IOException {
        byte[] encodedByteArray = new byte[] { 0x01, 0x02, 0x03, 0x04, 0x05 };
        InputStream in = new ByteArrayInputStream(encodedByteArray);
        BaseNCodec baseNCodec = new BaseNCodec();
        BaseNCodecInputStream inputStream = new BaseNCodecInputStream(in, baseNCodec, true);

        assertTrue("Expected availability to return true", inputStream.available() > 0);
    }

    /**
     * Test close method.
     *
     * @throws IOException
     */
    @org.junit.Test
    public void testClose() throws IOException {
        byte[] encodedByteArray = new byte[] { 0x01, 0x02, 0x03, 0x04, 0x05 };
        InputStream in = new ByteArrayInputStream(encodedByteArray);
        BaseNCodec baseNCodec = new BaseNCodec();
        BaseNCodecInputStream inputStream = new BaseNCodecInputStream(in, baseNCodec, true);

        inputStream.close();

        assertThrows("Expected IOException", IOException.class, () -> inputStream.read());
    }
}
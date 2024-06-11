package org.apache.commons.codec.binary;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.EncoderException;
import org.junit.Test;

/**
 * Test cases for {@link BaseNCodecInputStream} class.
 */
public class BaseNCodecInputStreamTest {

    /**
     * Test constructor.
     *
     * @throws IOException
     */
    @Test
    public void testConstructor() throws IOException {
        InputStream inputStream = new ByteArrayInputStream(new byte[] {});
        BaseNCodec baseNCodec = new BaseNCodec();
        BaseNCodecInputStream baseNCodecInputStream = new BaseNCodecInputStream(inputStream, baseNCodec, false);
        assertEquals(inputStream, baseNCodecInputStream.in);
        assertEquals(baseNCodec, baseNCodecInputStream.baseNCodec);
        assertFalse(baseNCodecInputStream.doEncode);
    }

    /**
     * Test reading a single byte.
     *
     * @throws IOException
     */
    @Test
    public void testReadSingleByte() throws IOException {
        InputStream inputStream = new ByteArrayInputStream(new byte[] { (byte) 0x61 }); // 'a'
        BaseNCodec baseNCodec = new BaseNCodec();
        BaseNCodecInputStream baseNCodecInputStream = new BaseNCodecInputStream(inputStream, baseNCodec, false);
        assertEquals(0x61, baseNCodecInputStream.read());
    }

    /**
     * Test reading multiple bytes.
     *
     * @throws IOException
     */
    @Test
    public void testReadMultipleBytes() throws IOException {
        InputStream inputStream = new ByteArrayInputStream(new byte[] { (byte) 0x61, (byte) 0x62 }); // 'ab'
        BaseNCodec baseNCodec = new BaseNCodec();
        BaseNCodecInputStream baseNCodecInputStream = new BaseNCodecInputStream(inputStream, baseNCodec, false);
        byte[] expected = new byte[] { 0x61, 0x62 };
        assertArrayEquals(expected, baseNCodecInputStream.read(new byte[2], 0, 2));
    }

    /**
     * Test markSupported method.
     *
     * @throws IOException
     */
    @Test
    public void testMarkSupported() throws IOException {
        InputStream inputStream = new ByteArrayInputStream(new byte[] {});
        BaseNCodec baseNCodec = new BaseNCodec();
        BaseNCodecInputStream baseNCodecInputStream = new BaseNCodecInputStream(inputStream, baseNCodec, false);
        assertFalse(baseNCodecInputStream.markSupported());
    }

    /**
     * Test skip method.
     *
     * @throws IOException
     */
    @Test
    public void testSkip() throws IOException {
        InputStream inputStream = new ByteArrayInputStream(new byte[] { (byte) 0x61 }); // 'a'
        BaseNCodec baseNCodec = new BaseNCodec();
        BaseNCodecInputStream baseNCodecInputStream = new BaseNCodecInputStream(inputStream, baseNcodec, false);

        // skip 1 byte
        assertEquals(0, baseNCodecInputStream.skip(1));
        assertEquals(0x61, baseNCodecInputStream.read());

        // skip 1 byte more than available
        assertEquals(-1, baseNCodecInputStream.skip(1));

        // skip 512 bytes
        byte[] buffer = new byte[512];
        assertEquals(512, baseNCodecInputStream.skip(buffer.length));
        assertArrayEquals(new byte[0], buffer);
    }

    /**
     * Test availability of data.
     *
     * @throws IOException
     */
    @Test
    public void testAvailable() throws IOException {
        InputStream inputStream = new ByteArrayInputStream(new byte[] { (byte) 0x61, (byte) 0x62 }); // 'ab'
        BaseNCodec baseNCodec = new BaseNCodec();
        BaseNCodecInputStream baseNCodecInputStream = new BaseNCodecInputStream(inputStream, baseNCODEC, false);
        assertEquals(2, baseNCodecInputStream.available());
    }

    /**
     * Test reading past EOF.
     *
     * @throws IOException
     */
    @Test(expected = IOException.class)
    public void testReadPastEOF() throws IOException {
        InputStream inputStream = new ByteArrayInputStream(new byte[] {});
        BaseNCodec baseNCodec = new BaseNCodec();
        BaseNCodecInputStream baseNCodecInputStream = new BaseNCodecInputStream(inputStream, baseNCodec, false);
        baseNCodecInputStream.read(); // read first byte
        baseNCodecInputStream.read(); // read second byte
        baseNCodecInputStream.read(); // read third byte (should raise an IOException)
    }

    /**
     * Test decoding and encoding behavior.
     *
     * @throws IOException
     */
    @Test
    public void testDecodeAndEncode() throws IOException, EncoderException, DecoderException {
        InputStream inputStream = new ByteArrayInputStream(new byte[] { 0x42, 0x41 }); // 'BA'
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        BaseNCodec baseNCodec = new BaseNCodec();
        BaseNCodecInputStream baseNCodecInputStream = new BaseNCodecInputStream(inputStream, baseNCodec, false);
        BaseNCodecOutputStream baseNCodecOutputStream = new BaseNCodecOutputStream(outputStream, baseNCodec, true);

        byte[] decoded = new byte[2];
        baseNCodecInputStream.read(decoded, 0, 2);
        assertArrayEquals(new byte[] { 0x61, 0x62 }, decoded);

        baseNCodecOutputStream.write(new byte[] { 0x61, 0x62 });
        byte[] encoded = new byte[2];
        baseNCodecOutputStream.read(encoded, 0, 2);
        assertArrayEquals(new byte[] { 0x42, 0x41 }, encoded);
    }
}
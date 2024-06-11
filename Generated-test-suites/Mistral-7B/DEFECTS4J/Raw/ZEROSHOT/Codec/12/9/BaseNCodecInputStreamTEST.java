package org.apache.commons.codec.binary;

import static org.apache.commons.codec.binary.BaseNCodec.EOF;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import org.apache.commons.codec.binary.BaseNCodec;
import org.apache.commons.codec.binary.BaseNCodecInputStream;
import org.junit.Test;

/**
 * Test class for {@link BaseNCodecInputStream}.
 */
public class BaseNCodecInputStreamTest {

    /**
     * Test constructor.
     */
    @Test
    public void testConstructor() {
        final BaseNCodec baseNCodec = new BaseNCodec();
        final InputStream inputStream = new ByteArrayInputStream(new byte[] { (byte) 0x01 });
        final BaseNCodecInputStream testee = new BaseNCodecInputStream(inputStream, baseNCodec, false);
        assertNotNull(testee);
        assertEquals(inputStream, testee.in);
        assertEquals(baseNCodec, testee.baseNCodec);
        assertFalse(testee.doEncode);
    }

    /**
     * Test {@link BaseNCodecInputStream#read()}.
     */
    @Test
    public void testRead() throws IOException {
        final BaseNCodec baseNCodec = new BaseNCodec();
        final InputStream inputStream = new ByteArrayInputStream(new byte[] { (byte) 0x01 });
        final BaseNCodecInputStream testee = new BaseNCodecInputStream(inputStream, baseNCodec, false);

        // test read of a single byte
        assertEquals((int) 0x01, testee.read());

        // test read of EOF
        assertNull(testee.read());
    }

    /**
     * Test {@link BaseNCodecInputStream#read(byte[], int, int)}.
     */
    @Test
    public void testReadByteArray() throws IOException {
        final BaseNCodec baseNCodec = new BaseNCodec();
        final InputStream inputStream = new ByteArrayInputStream(new byte[] { (byte) 0x01, (byte) 0x02, (byte) 0x03 });
        final BaseNCodecInputStream testee = new BaseNCodecInputStream(inputStream, baseNCodec, false);

        // test read of a single byte
        final byte[] b = new byte[1];
        assertEquals(1, testee.read(b));
        assertEquals((int) 0x01, b[0]);

        // test read of multiple bytes
        final byte[] result = new byte[3];
        assertEquals(3, testee.read(result));
        assertEquals((int) 0x01, result[0]);
        assertEquals((int) 0x02, result[1]);
        assertEquals((int) 0x03, result[2]);

        // test read of EOF
        final byte[] empty = new byte[1];
        assertEquals(0, testee.read(empty));
        assertNull(testee.read());
    }

    /**
     * Test {@link BaseNCodecInputStream#markSupported()}.
     */
    @Test(expected = UnsupportedOperationException.class)
    public void testMarkSupported() {
        final BaseNCodec baseNCodec = new BaseNCodec();
        final InputStream inputStream = new ByteArrayInputStream(new byte[] { (byte) 0x01 });
        final BaseNCodecInputStream testee = new BaseNCodecInputStream(inputStream, baseNCodec, false);

        testee.markSupported();
    }

    /**
     * Test {@link BaseNCodecInputStream#skip(long)} with a positive skip length.
     */
    @Test
    public void testSkipPositive() throws IOException {
        final BaseNCodec baseNCodec = new BaseNCodec();
        final InputStream inputStream = new ByteArrayInputStream(new byte[] { (byte) 0x01, (byte) 0x02, (byte) 0x03, (byte) 0x04, (byte) 0x05 });
        final BaseNCodecInputStream testee = new BaseNCodecInputStream(inputStream, baseNCodec, false);

        // test skip of 2 bytes
        testee.skip(2);
        final byte[] b = new byte[3];
        assertEquals(3, testee.read(b));
        assertEquals((int) 0x03, b[0]);
        assertEquals((int) 0x04, b[1]);
        assertEquals((int) 0x05, b[2]);
    }

    /**
     * Test {@link BaseNCodecInputStream#skip(long)} with a negative skip length.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testSkipNegative() throws IOException {
        final BaseNCodec baseNCodec = new BaseNCodec();
        final InputStream inputStream = new ByteArrayInputStream(new byte[] { (byte) 0x01, (byte) 0x02, (byte) 0x03, (byte) 0x04, (byte) 0x05 });
        final BaseNCodecInputStream testee = new BaseNCodecInputStream(inputStream, baseNCodec, false);

        testee.skip(-1);
    }

    /**
     * Test {@link BaseNCodecInputStream#available()}.
     */
    @Test
    public void testAvailable() throws IOException {
        final BaseNCodec baseNCodec = new BaseNCodec();
        final InputStream inputStream = new ByteArrayInputStream(new byte[] { (byte) 0x01, (byte) 0x02, (byte) 0x03, (byte) 0x04, (byte) 0x05 });
        final BaseNCodecInputStream testee = new BaseNCodecInputStream(inputStream, baseNCodec, false);

        // test available with no data read
        assertEquals(5, testee.available());

        // test available with data read
        final byte[] b = new byte[3];
        testee.read(b);
        assertEquals(2, testee.available());
    }

    /**
     * Test {@link BaseNCodecInputStream#close()}.
     */
    @Test
    public void testClose() throws IOException {
        final BaseNCodec baseNCodec = new BaseNCodec();
        final InputStream inputStream = new ByteArrayInputStream(new byte[] { (byte) 0x01, (byte) 0x02, (byte) 0x03, (byte) 0x04, (byte) 0x05 });
        final BaseNCodecInputStream testee = new BaseNCodecInputStream(inputStream, baseNCodec, false);

        testee.close();
        assertNull(testee.in);
    }

}
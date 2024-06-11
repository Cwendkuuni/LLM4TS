package org.apache.commons.codec.binary;

import static org.apache.commons.codec.binary.BaseNCodec.EOF;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.codec.binary.BaseNCodec;
import org.apache.commons.codec.binary.BaseNCodecInputStream;

import org.junit.Before;
import org.junit.Test;

/**
 * Test class for {@link BaseNCodecInputStream}.
 */
public class BaseNCodecInputStreamTest {

    private InputStream input;
    private BaseNCodec baseNCodec;

    @Before
    public void setUp() {
        baseNCodec = new BaseNCodec();
        input = new ByteArrayInputStream(new byte[]{"I".getBytes()});
    }

    @Test
    public void testRead() throws IOException {
        BaseNCodecInputStream testStream = new BaseNCodecInputStream(input, baseNCodec, false);

        assertEquals(73, testStream.read());
        assertEquals(73, testStream.read());
        assertEquals(-1, testStream.read());
    }

    @Test
    public void testReadByteArray() throws IOException {
        BaseNCodecInputStream testStream = new BaseNCodecInputStream(input, baseNCodec, false);

        byte[] expected = new byte[]{'I'};
        byte[] result = new byte[1];

        int length = testStream.read(result, 0, 1);
        assertArrayEquals(expected, result);
        assertEquals(1, length);

        length = testStream.read(result, 0, 1);
        assertArrayEquals(expected, result);
        assertEquals(1, length);

        length = testStream.read(result, 0, 1);
        assertArrayEquals(new byte[0], result);
        assertEquals(-1, length);
    }

    @Test(expected = NullPointerException.class)
    public void testReadNullByteArray() throws IOException {
        BaseNCodecInputStream testStream = new BaseNCodecInputStream(input, baseNCodec, false);
        testStream.read(null, 0, 1);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testReadNegativeOffset() throws IOException {
        BaseNCodecInputStream testStream = new BaseNCodecInputStream(input, baseNCodec, false);
        testStream.read(new byte[1], -1, 1);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testReadNegativeLength() throws IOException {
        BaseNCodecInputStream testStream = new BaseNCodecInputStream(input, baseNCodec, false);
        testStream.read(new byte[1], 0, -1);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testReadNegativeOffsetAndLength() throws IOException {
        BaseNCodecInputStream testStream = new BaseNCodecInputStream(input, baseNCodec, false);
        testStream.read(new byte[1], -1, 1);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testReadTooLongByteArray() throws IOException {
        BaseNCodecInputStream testStream = new BaseNCodecInputStream(input, baseNCodec, false);
        testStream.read(new byte[3], 0, 1);
    }

    @Test
    public void testMarkSupported() {
        BaseNCodecInputStream testStream = new BaseNCodecInputStream(input, baseNCodec, false);
        assertFalse(testStream.markSupported());
    }

    @Test
    public void testSkip() throws IOException {
        BaseNCodecInputStream testStream = new BaseNCodecInputStream(input, baseNCodec, false);
        testStream.skip(512);
        assertTrue(testStream.markSupported()); // note: this is not an easy job to support marks
        testStream.reset();

        byte[] expected = new byte[512];
        byte[] result = new byte[512];
        int read = testStream.read(result, 0, 512);
        assertArrayEquals(expected, result);
        assertEquals(512, read);
    }

    @Test
    public void testEncode() throws IOException {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        ByteArrayInputStream input = new ByteArrayInputStream(output.toByteArray());

        BaseNCodec baseNCodec = new BaseNCodec(true);
        BaseNCodecInputStream testStream = new BaseNCodecInputStream(input, baseNCodec, true);

        byte[] expected = new byte[]{62, 65, 78};
        byte[] result = new byte[3];

        int length = testStream.read(result, 0, 3);
        assertArrayEquals(expected, result);
        assertEquals(3, length);
    }
}
package org.apache.commons.codec.binary;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import org.junit.Test;

public class BaseNCodecInputStreamTest {

    @Test
    public void testMarkSupported() {
        BaseNCodecInputStream stream = new BaseNCodecInputStream(new ByteArrayInputStream(new byte[0]), new BaseNCodec(), false);
        assertFalse(stream.markSupported());
    }

    @Test
    public void testReadByte() throws IOException {
        BaseNCodecInputStream stream = new BaseNCodecInputStream(new ByteArrayInputStream(new byte[] { 0x01 }), new BaseNCodec(), false);
        assertEquals(0x01, stream.read());
        assertEquals(-1, stream.read());
    }

    @Test
    public void testReadByteWhenUnderlyingStreamHasEOF() throws IOException {
        BaseNCodecInputStream stream = new BaseNCodecInputStream(new ByteArrayInputStream(new byte[0][]), new BaseNCodec(), false);
        assertEquals(-1, stream.read());
    }

    @Test
    public void testReadByteArray() throws IOException {
        byte[] expected = new byte[] { 0x01, 0x02, 0x03, 0x04 };
        BaseNCodecInputStream stream = new BaseNCodecInputStream(new ByteArrayInputStream(expected), new BaseNCodec(), false);
        byte[] result = new byte[expected.length];
        int read = stream.read(result);
        assertArrayEquals(expected, result);
        assertEquals(-1, stream.read());
    }

    @Test
    public void testReadByteArrayWhenUnderlyingStreamHasEOF() throws IOException {
        byte[] expected = new byte[10];
        BaseNCodecInputStream stream = new BaseNCodecInputStream(new ByteArrayInputStream(expected), new BaseNCodec(), false);
        byte[] result = new byte[1];
        int read = stream.read(result);
        assertNull(result);
        assertEquals(-1, stream.read());
    }

    @Test
    public void testSkip() throws IOException {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        byte[] data = new byte[] { 0x01, 0x02, 0x03, 0x04 };
        BaseNCodec codec = new BaseNCodec();
        InputStream input = new ByteArrayInputStream(data);
        BaseNCodecInputStream testStream = new BaseNCodecInputStream(input, codec, false);

        // skip 0 bytes
        testStream.skip(0);

        // skip 512 bytes
        testStream.skip(512);

        // skip remaining bytes
        int remaining = data.length - 512;
        testStream.skip(remaining);

        // check that read() returns EOF
        assertEquals(-1, testStream.read());
    }

    @Test
    public void testSkipWhenUnderlyingStreamHasEOF() throws IOException {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        byte[] data = new byte[10];
        BaseNCodec codec = new BaseNCodec();
        InputStream input = new ByteArrayInputStream(data);
        BaseNCodecInputStream testStream = new BaseNCodecInputStream(input, codec, false);

        // skip 0 bytes
        testStream.skip(0);

        // skip EOF
        testStream.skip(1);

        // check that read() returns EOF
        assertEquals(-1, testStream.read());
    }

    @Test
    public void testSkipWhenDoEncodeIsTrue() throws IOException {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        byte[] encodedData = new byte[] { 0x2A, 0x2A, 0x2A, 0x2A, 0x2A };
        byte[] data = new BaseNCodec().decode(encodedData);
        BaseNCodec codec = new BaseNCodec();
        InputStream input = new ByteArrayInputStream(data);
        BaseNCodecInputStream testStream = new BaseNCodecInputStream(input, codec, true);

        // skip 0 bytes
        testStream.skip(0);

        // skip 512 bytes
        testStream.skip(512);

        // skip remaining bytes
        int remaining = data.length - 512;
        testStream.skip(remaining);

        // check that read() returns EOF
        assertEquals(-1, testStream.read());
    }

    @Test(expected = IOException.class)
    public void testNegativeSkipLength() throws IOException {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        byte[] data = new byte[] { 0x01, 0x02, 0x03, 0x04 };
        BaseNCodec codec = new BaseNCodec();
        InputStream input = new ByteArrayInputStream(data);
        BaseNCodecInputStream testStream = new BaseNCodecInputStream(input, codec, false);

        testStream.skip(-1);
    }

    @Test
    public void testAvailable() throws IOException {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        byte[] data = new byte[] { 0x01, 0x02, 0x03, 0x04 };
        BaseNCodec codec = new BaseNCodec();
        InputStream input = new ByteArrayInputStream(data);
        BaseNCodecInputStream testStream = new BaseNCodecInputStream(input, codec, false);

        // check available() returns -1 when underlying stream has EOF
        assertEquals(-1, testStream.available());

        // check available() returns correct value when data is available
        testStream.read();
        assertEquals(3, testStream.available());
    }

    @Test
    public void testClose() throws IOException {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        byte[] data = new byte[] { 0x01, 0x02, 0x03, 0x04 };
        BaseNCodec codec = new BaseNCodec();
        InputStream input = new ByteArrayInputStream(data);
        BaseNCodecInputStream testStream = new BaseNCodecInputStream(input, codec, false);

        testStream.close();
        assertEquals(-1, testStream.read());
    }

    @Test
    public void testCloseTwice() throws IOException {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        byte[] data = new byte[] { 0x01, 0x02, 0x03, 0x04 };
        BaseNCodec codec = new BaseNCodec();
        InputStream input = new ByteArrayInputStream(data);
        BaseNCodecInputStream testStream = new BaseNCodecInputStream(input, codec, false);

        testStream.close();
        testStream.close();
        assertEquals(-1, testStream.read());
    }

    @Test
    public void testHasNextByte() throws IOException {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        byte[] data = new byte[] { 0x01, 0x02, 0x03, 0x04 };
        BaseNCodec codec = new BaseNCodec();
        InputStream input = new ByteArrayInputStream(data);
        BaseNCodecInputStream testStream = new BaseNCodecInputStream(input, codec, false);

        // check that hasNextByte() returns true when data is available
        assertTrue(testStream.hasNextByte());

        testStream.read();

        // check that hasNextByte() returns false when EOF is reached
        assertFalse(testStream.hasNextByte());
    }

    @Test
    public void testHasNextByteWhenDoEncodeIsTrue() throws IOException {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        byte[] encodedData = new byte[] { 0x2A, 0x2A, 0x2A, 0x2A, 0x2A };
        byte[] data = new BaseNCodec().decode(encodedData);
        BaseNCodec codec = new BaseNCodec();
        InputStream input = new ByteArrayInputStream(data);
        BaseNCodecInputStream testStream = new BaseNCodecInputStream(input, codec, true);

        // check that hasNextByte() returns true when data is available
        assertTrue(testStream.hasNextByte());

        testStream.read(new byte[1]);

        // check that hasNextByte() returns false when EOF is reached
        assertFalse(testStream.hasNextByte());
    }

    @Test(expected = IllegalStateException.class)
    public void testHasNextByteWhenMarkSet() throws IOException {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        byte[] data = new byte[] { 0x01, 0x02, 0x03, 0x04 };
        BaseNCodec codec = new BaseNCodec();
        InputStream input = new ByteArrayInputStream(data);
        BaseNCodecInputStream testStream = new BaseNCodecInputStream(input, codec, false);

        testStream.mark(1);
        assertTrue(testStream.hasNextByte());
        testStream.reset(); // reset the marker
        assertFalse(testStream.hasNextByte());
    }

}
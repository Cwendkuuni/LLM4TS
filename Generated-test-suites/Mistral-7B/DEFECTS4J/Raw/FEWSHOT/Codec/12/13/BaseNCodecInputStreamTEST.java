package org.apache.commons.codec.binary;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import org.junit.Test;

public class BaseNCodecInputStreamTest {

    @Test
    public void testMarkSupported() {
        BaseNCodecInputStream inputStream = new BaseNCodecInputStream(new ByteArrayInputStream(new byte[0]), null, false);
        assertFalse(inputStream.markSupported());
    }

    @Test
    public void testRead() throws IOException {
        byte[] input = {0x1, 0x2, 0x3};
        ByteArrayInputStream in = new ByteArrayInputStream(input);
        BaseNCodecInputStream testee = new BaseNCodecInputStream(in, null, false);
        byte[] actual = new byte[3];

        for (int i = 0; i < 3; i++) {
            int result = testee.read(actual, 0, 1);
            assertEquals(1, result);
            assertEquals(actual[0], input[i]);
            actual[0] = 0; // reset array for next test iteration
        }

        int lastResult = testee.read(actual, 0, 1);
        assertEquals(-1, lastResult);
    }

    @Test
    public void testReadWithEmptyInput() throws IOException {
        ByteArrayInputStream in = new ByteArrayInputStream(new byte[0]);
        BaseNCodecInputStream testee = new BaseNCodecInputStream(in, null, false);
        byte[] actual = new byte[1];

        int result = testee.read(actual, 0, 1);
        assertEquals(-1, result);
    }

    @Test
    public void testReadWithNullParameter() throws IOException {
        ByteArrayInputStream in = new ByteArrayInputStream(new byte[0]);
        BaseNCodecInputStream testee = new BaseNCodecInputStream(in, null, false);

        int result = testee.read(null, 0, 1);
        assertEquals(-1, result);
    }

    @Test
    public void testReadWithInvalidOffset() throws IOException {
        ByteArrayInputStream in = new ByteArrayInputStream(new byte[0]);
        BaseNCodecInputStream testee = new BaseNCodecInputStream(in, null, false);

        int result = testee.read(new byte[1], -1, 1);
        assertEquals(-1, result);
    }

    @Test
    public void testReadWithInvalidLength() throws IOException {
        ByteArrayInputStream in = new ByteArrayInputStream(new byte[0]);
        BaseNCodecInputStream testee = new BaseNCodecInputStream(in, null, false);

        int result = testee.read(new byte[1], 0, -1);
        assertEquals(-1, result);
    }

    @Test
    public void testReadWithInvalidBufferSize() throws IOException {
        ByteArrayInputStream in = new ByteArrayInputStream(new byte[0]);
        BaseNCodecInputStream testee = new BaseNCodecInputStream(in, null, false);

        int result = testee.read(new byte[1], 0, in.available());
        assertEquals(-1, result);
    }

    @Test
    public void testReadWithNoData() throws IOException {
        BaseNCodec baseNCodec = new BaseNCodec();
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        byte[] input = {0x1, 0x2, 0x3};
        ByteArrayInputStream in = new ByteArrayInputStream(input);
        BaseNCodecInputStream testee = new BaseNCodecInputStream(in, baseNCodec, false);

        byte[] actual = new byte[3];
        int result = testee.read(actual, 0, 1);
        assertEquals(1, result);
        assertEquals(actual[0], input[0]);

        // simulate consumption of the first byte
        actual[0] = 0;
        result = testee.read(actual, 0, 1);
        assertEquals(1, result);
        assertEquals(actual[0], input[1]);

        // simulate consumption of the second byte
        actual[0] = 0;
        result = testee.read(actual, 0, 1);
        assertEquals(1, result);
        assertEquals(actual[0], input[2]);

        result = testee.read(actual, 0, 1);
        assertEquals(-1, result);

        // read from the empty input stream
        result = testee.read(actual, 0, 1);
        assertEquals(-1, result);
    }

    @Test
    public void testReadWithData() throws IOException {
        BaseNCodec baseNCodec = new BaseNCodec();
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        byte[] input = {0x1, 0x2, 0x3};
        ByteArrayInputStream in = new ByteArrayInputStream(input);
        BaseNCodecInputStream testee = new BaseNCodecInputStream(in, baseNCodec, false);

        byte[] actual = new byte[3];
        int result = testee.read(actual, 0, 1);
        assertEquals(1, result);
        assertEquals(actual[0], input[0]);

        // simulate consumption of the first byte
        actual[0] = 0;
        result = testee.read(actual, 0, 1);
        assertEquals(1, result);
        assertEquals(actual[0], input[1]);

        // simulate consumption of the second byte
        actual[0] = 0;
        result = testee.read(actual, 0, 1);
        assertEquals(1, result);
        assertEquals(actual[0], input[2]);

        // read a fourth byte that is not present in the input
        result = testee.read(actual, 0, 1);
        assertEquals(-1, result);
    }

    @Test
    public void testReadWithDoEncode() throws IOException {
        BaseNCodec baseNCodec = new BaseNCodec();
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        byte[] input = {0x1, 0x2, 0x3};
        ByteArrayInputStream in = new ByteArrayInputStream(input);
        BaseNCodecInputStream testee = new BaseNCodecInputStream(in, baseNCodec, true);

        byte[] actual = new byte[3];
        int result = testee.read(actual, 0, 1);
        assertEquals(1, result);
        assertEquals(actual[0], baseNCodec.encode(input[0])[0]);

        // simulate consumption of the first byte
        actual[0] = 0;
        result = testee.read(actual, 0, 1);
        assertEquals(1, result);
        assertEquals(actual[0], baseNCodec.encode(input[1])[0]);

        // simulate consumption of the second byte
        actual[0] = 0;
        result = testee.read(actual, 0, 1);
        assertEquals(1, result);
        assertEquals(actual[0], baseNCodec.encode(input[2])[0]);

        result = testee.read(actual, 0, 1);
        assertEquals(-1, result);
    }
}
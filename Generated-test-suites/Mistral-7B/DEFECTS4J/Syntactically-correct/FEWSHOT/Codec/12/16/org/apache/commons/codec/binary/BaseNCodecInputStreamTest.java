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

import org.apache.commons.codec.binary.BaseNCodec;
import org.junit.Test;

public class BaseNCodecInputStreamTest {

    @Test
    public void testRead() throws IOException {
        BaseNCodec baseNCodec = new BaseNCodec();
        InputStream input = new ByteArrayInputStream(new byte[] { 0x1, 0x2, 0x3 });
        BaseNCodecInputStream inputStream = new BaseNCodecInputStream(input, baseNCodec, false);

        byte[] buffer = new byte[3];

        int byteRead = inputStream.read(buffer, 0, buffer.length);
        assertEquals(byteRead, buffer.length);
        assertArrayEquals(new byte[] { 0x1, 0x2, 0x3 }, buffer);

        byteRead = inputStream.read();
        assertEquals(byteRead, 0x1);
        byteRead = inputStream.read();
        assertEquals(byteRead, 0x2);
        byteRead = inputStream.read();
        assertEquals(byteRead, 0x3);
        byteRead = inputStream.read();
        assertEquals(byteRead, EOF);

        inputStream.close();
    }

    @Test
    public void testDecodeAndRead() throws IOException {
        BaseNCodec baseNCodec = new BaseNCodec();
        InputStream input = new ByteArrayInputStream(new byte[] { 0x1, 0x2, 0x3 });
        BaseNCodecInputStream inputStream = new BaseNCodecInputStream(input, baseNCodec, false);

        byte[] buffer = new byte[3];

        int byteRead = inputStream.read(buffer, 0, buffer.length);
        assertEquals(byteRead, buffer.length);
        assertArrayEquals(new byte[] { 0x1, 0x2, 0x3 }, buffer);

        byte[] decoded = new byte[4];
        int decodedLength = inputStream.read(decoded, 0, decoded.length);
        assertEquals(decodedLength, 4);
        assertEquals(new String(decoded, 0, 4), "010203");

        inputStream.close();
    }

    @Test
    public void testEncodeAndRead() throws IOException {
        BaseNCodec baseNCodec = new BaseNCodec();
        InputStream input = new ByteArrayInputStream(new byte[] { 0x1, 0x2, 0x3 });
        BaseNCodecInputStream inputStream = new BaseNCodecInputStream(input, baseNCodec, true);

        byte[] buffer = new byte[3];

        int byteRead = inputStream.read(buffer, 0, buffer.length);
        assertEquals(byteRead, buffer.length);
        assertArrayEquals(new byte[] { 0x1, 0x2, 0x3 }, buffer);

        byte[] encoded = new byte[5];
        int encodedLength = inputStream.read(encoded, 0, encoded.length);
        assertEquals(encodedLength, 5);
        assertEquals(new String(encoded), "010203==");

        inputStream.close();
    }

    @Test
    public void testMarkSupported() {
        BaseNCodec baseNCodec = new BaseNCodec();
        BaseNCodecInputStream inputStream = new BaseNCodecInputStream(new ByteArrayInputStream(new byte[0]), baseNCodec, false);

        assertFalse(inputStream.markSupported());
    }

    @Test
    public void testReadLimit() throws IOException {
        BaseNCodec baseNCodec = new BaseNCodec();
        InputStream input = new ByteArrayInputStream(new byte[] { 0x1, 0x2, 0x3 });
        BaseNCodecInputStream inputStream = new BaseNCodecInputStream(input, baseNCodec, false);

        byte[] buffer = new byte[2];

        int byteRead = inputStream.read(buffer, 0, buffer.length);
        assertEquals(byteRead, buffer.length);
        assertArrayEquals(new byte[] { 0x1, 0x2 }, buffer);

        byteRead = inputStream.read(buffer, 0, 1);
        assertEquals(byteRead, 1);
        assertEquals(buffer[0], 0x3);

        inputStream.close();
    }

    @Test
    public void testReadZeroLength() throws IOException {
        BaseNCodec baseNCodec = new BaseNCodec();
        InputStream input = new ByteArrayInputStream(new byte[0]);
        BaseNCodecInputStream inputStream = new BaseNCodecInputStream(input, baseNCodec, false);

        byte[] buffer = new byte[1];

        int byteRead = inputStream.read(buffer, 0, buffer.length);
        assertEquals(byteRead, 0);

        inputStream.close();
    }

    @Test
    public void testReadNegativeLength() throws IOException {
        BaseNCodec baseNCodec = new BaseNCodec();
        InputStream input = new ByteArrayInputStream(new byte[0]);
        BaseNCodecInputStream inputStream = new BaseNCodecInputStream(input, baseNCodec, false);

        byte[] buffer = new byte[1];

        try {
            inputStream.read(buffer, 1, 1);
            fail();
        } catch (IndexOutOfBoundsException e) {
            // expected
        }

        inputStream.close();
    }

    @Test
    public void testClose() throws IOException {
        BaseNCodec baseNCodec = new BaseNCodec();
        InputStream input = new ByteArrayInputStream(new byte[0]);
        BaseNCodecInputStream inputStream = new BaseNCodecInputStream(input, baseNCodec, false);

        inputStream.close();

        try {
            inputStream.read();
            fail();
        } catch (IOException e) {
            // expected
        }

        try {
            inputStream.markSupported();
            fail();
        } catch (UnsupportedOperationException e) {
            // expected
        }

        try {
            inputStream.read(new byte[1], 0, 1);
            fail();
        } catch (IOException e) {
            // expected
        }
    }

    @Test
    public void testHasData() throws IOException {
        BaseNCodec baseNCodec = new BaseNCodec();
        InputStream input = new ByteArrayInputStream(new byte[0]);
        BaseNCodecInputStream inputStream = new BaseNCodecInputStream(input, baseNCodec, false);

        assertFalse(inputStream.hasData());

        byte[] data = new byte[1];
        inputStream.read(data, 0, data.length);

        assertTrue(inputStream.hasData());

        inputStream.close();
    }

    @Test
    public void testDecodeLargeData() throws IOException {
        BaseNCodec baseNCodec = new BaseNCodec();
        InputStream input = new ByteArrayInputStream(new byte[1024 * 1024]);
        BaseNCodecInputStream inputStream = new BaseNCodecInputStream(input, baseNCodec, false);

        byte[] buffer = new byte[1024 * 1024];

        int decodedLength = inputStream.read(buffer, 0, buffer.length);
        assertEquals(decodedLength, buffer.length);

        inputStream.close();
    }

    @Test
    public void testDecodeIncompleteData() throws IOException {
        BaseNCodec baseNCodec = new BaseNCodec();
        InputStream input = new ByteArrayInputStream(new byte[] { 0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07 });
        BaseNCodecInputStream inputStream = new BaseNCodecInputStream(input, baseNCodec, false);

        byte[] buffer = new byte[8];

        int decodedLength = inputStream.read(buffer, 0, buffer.length);
        assertEquals(decodedLength, 7);
        assertArrayEquals(new byte[] { 0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07 }, buffer);

        inputStream.close();
    }

    @Test
    public void testEncodeLargeData() throws IOException {
        BaseNCodec baseNCodec = new BaseNCodec();
        InputStream input = new ByteArrayInputStream(new byte[1024 * 1024]);
        BaseNCodecInputStream inputStream = new BaseNCodecInputStream(input, baseNCodec, true);

        byte[] buffer = new byte[1024 * 1024];

        int encodedLength = inputStream.read(buffer, 0, buffer.length);
        assertEquals(encodedLength, buffer.length + 3);
        assertEquals(new String(buffer), new String(BaseNCodec.DECODE, buffer, 0, buffer.length));

        inputStream.close();
    }

    @Test
    public void testEncodeIncompleteData() throws IOException {
        BaseNCodec baseNCodec = new BaseNCodec();
        InputStream input = new ByteArrayInputStream(new byte[] { 0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07 });
        BaseNCodecInputStream inputStream = new BaseNCodecInputStream(input, baseNCodec, true);

        byte[] buffer = new byte[8];

        int encodedLength = inputStream.read(buffer, 0, buffer.length);
        assertEquals(encodedLength, buffer.length + 1);
        assertEquals(new String(buffer), new String(BaseNCodec.ENCODE, buffer, 0, buffer.length));

        inputStream.close();
    }

    @Test
    public void testEncodeAndDecodeFromSameStream() throws IOException {
        BaseNCodec baseNCodec = new BaseNCodec();
        InputStream input = new ByteArrayInputStream(new byte[] { 0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07 });
        BaseNCodecInputStream inputStream = new BaseNCodecInputStream(input, baseNCodec, true);

        byte[] buffer = new byte[8];

        int encodedLength = inputStream.read(buffer, 0, buffer.length);
        assertEquals(encodedLength, buffer.length + 1);
        assertEquals(new String(buffer), new String(BaseNCodec.ENCODE, buffer, 0, buffer.length));

        byte[] decoded = new byte[8];
        int decodedLength = inputStream.read(decoded, 0, decoded.length);
        assertEquals(decodedLength, decoded.length);
        assertEquals(new String(decoded), new String(BaseNCodec.DECODE, buffer, 0, buffer.length));

        inputStream.close();
    }

    @Test
    public void testDecodeFromClosedInputStream() throws IOException {
        BaseNCodec baseNCodec = new BaseNCodec();
        InputStream input = new ByteArrayInputStream(new byte[] { 0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07 });
        BaseNCodecInputStream inputStream = new BaseNCodecInputStream(input, baseNCodec, false);

        inputStream.close();

        byte[] buffer = new byte[8];
        try {
            inputStream.read(buffer, 0, buffer.length);
            fail();
        } catch (IOException e) {
            // expected
        }
    }

    @Test
    public void testEncodeFromClosedInputStream() throws IOException {
        BaseNCodec baseNCodec = new BaseNCodec();
        InputStream input = new ByteArrayInputStream(new byte[] { 0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07 });
        BaseNCodecInputStream inputStream = new BaseNCodecInputStream(input, baseNCodec, true);

        inputStream.close();

        byte[] buffer = new byte[8];
        try {
            inputStream.read(buffer, 0, buffer.length);
            fail();
        } catch (IOException e) {
            // expected
        }

        try {
            int encodedLength = inputStream.read(buffer, 0, buffer.length);
            fail();
        } catch (IOException e) {
            // expected
        }
    }

}
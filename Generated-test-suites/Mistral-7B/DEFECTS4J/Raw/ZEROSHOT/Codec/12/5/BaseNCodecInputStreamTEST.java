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
import java.io.InputStream;

import org.apache.commons.codec.binary.BaseNCodec;
import org.apache.commons.codec.binary.BaseNCodecInputStream;

import org.junit.Test;

/**
 * Test class for {@link BaseNCodecInputStream}
 */
public class BaseNCodecInputStreamTest {

    @Test
    public void testBaseNCodecInputStream() {
        BaseNCodec baseNCodec = new BaseNCodec();
        InputStream inputStream = new ByteArrayInputStream(new byte[] { 1, 2, 3, 4, 5 });
        BaseNCodecInputStream baseNCodecInputStream = new BaseNCodecInputStream(inputStream, baseNCodec, false);

        assertNotNull(baseNCodecInputStream);
        assertEquals(inputStream, baseNCodecInputStream.in);
        assertNotNull(baseNCodecInputStream.baseNCodec);
        assertFalse(baseNCodecInputStream.doEncode);
        assertEquals(BaseNCodecInputStream.class, baseNCodecInputStream.getClass());
    }

    @Test
    public void testReadByte() throws IOException {
        BaseNCodec baseNCodec = new BaseNCodec();
        InputStream inputStream = new ByteArrayInputStream(new byte[] { 1, 2, 3, 4, 5 });
        BaseNCodecInputStream baseNCodecInputStream = new BaseNCodecInputStream(inputStream, baseNCodec, false);

        for (int i = 0; i < 5; i++) {
            assertEquals(i + 1, baseNCodecInputStream.read());
        }
        assertEquals(BaseNCodecInputStream.EOF, baseNCodecInputStream.read());
    }

    @Test
    public void testReadBytes() throws IOException {
        BaseNCodec baseNCodec = new BaseNCodec();
        InputStream inputStream = new ByteArrayInputStream(new byte[] { 1, 2, 3, 4, 5 });
        BaseNCodecInputStream baseNCodecInputStream = new BaseNCodecInputStream(inputStream, baseNCodec, false);

        byte[] result = new byte[5];
        int length = baseNCodecInputStream.read(result, 0, 5);
        assertEquals(5, length);
        assertEquals(1, result[0]);
        assertEquals(2, result[1]);
        assertEquals(3, result[2]);
        assertEquals(4, result[3]);
        assertEquals(5, result[4]);

        byte[] result2 = new byte[10];
        length = baseNCodecInputStream.read(result2, 0, 10);
        assertEquals(5, length);
    }

    @Test(expected = NullPointerException.class)
    public void testReadBytesNullArray() throws IOException {
        BaseNCodec baseNCodec = new BaseNCodec();
        InputStream inputStream = new ByteArrayInputStream(new byte[] { 1, 2, 3, 4, 5 });
        BaseNCodecInputStream baseNCodecInputStream = new BaseNCodecInputStream(inputStream, baseNCodec, false);

        byte[] result = null;
        int length = baseNCodecInputStream.read(result, 0, 5);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testReadBytesNegativeOffset() throws IOException {
        BaseNCodec baseNCodec = new BaseNCodec();
        InputStream inputStream = new ByteArrayInputStream(new byte[] { 1, 2, 3, 4, 5 });
        BaseNCodecInputStream baseNCodecInputStream = new BaseNCodecInputStream(inputStream, baseNCodec, false);

        byte[] result = new byte[5];
        int length = baseNCodecInputStream.read(result, -1, 5);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testReadBytesNegativeLength() throws IOException {
        BaseNCodec baseNCodec = new BaseNCodec();
        InputStream inputStream = new ByteArrayInputStream(new byte[] { 1, 2, 3, 4, 5 });
        BaseNCodecInputStream baseNCodecInputStream = new BaseNCodecInputStream(inputStream, baseNCodec, false);

        byte[] result = new byte[5];
        int length = baseNCodecInputStream.read(result, 0, -1);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testReadBytesTooLong() throws IOException {
        BaseNCodec baseNCodec = new BaseNCodec();
        InputStream inputStream = new ByteArrayInputStream(new byte[] { 1, 2, 3, 4, 5 });
        BaseNCodecInputStream baseNCodecInputStream = new BaseNCodecInputStream(inputStream, baseNCodec, false);

        byte[] result = new byte[10];
        int length = baseNCodecInputStream.read(result, 0, 6);
    }

    @Test
    public void testMarkSupported() throws IOException {
        BaseNCodec baseNCodec = new BaseNCodec();
        InputStream inputStream = new ByteArrayInputStream(new byte[] { 1, 2, 3, 4, 5 });
        BaseNCodecInputStream baseNCodecInputStream = new BaseNCodecInputStream(inputStream, baseNCodec, false);

        assertFalse(baseNCodecInputStream.markSupported());
    }

    @Test
    public void testClose() throws IOException {
        BaseNCodec baseNCodec = new BaseNCodec();
        InputStream inputStream = new ByteArrayInputStream(new byte[] { 1, 2, 3, 4, 5 });
        BaseNCodecInputStream baseNCodecInputStream = new BaseNCodecInputStream(inputStream, baseNCodec, false);

        baseNCodecInputStream.close();
        assertNull(baseNCodecInputStream.in);
        assertNull(baseNCodecInputStream.baseNCodec);
    }

    @Test
    public void testDecodeEncode() throws IOException {
        BaseNCodec baseNCodec = new BaseNCodec();
        InputStream inputStream = new ByteArrayInputStream(new byte[] { 1, 2, 3, 4, 5 });
        BaseNCodecInputStream baseNCodecInputStream = new BaseNCodecInputStream(inputStream, baseNCodec, false);

        byte[] decoded = new byte[10];
        int length = baseNCodecInputStream.read(decoded, 0, 10);
        ByteArrayOutputStream byteStream = new ByteArrayOutputStream();

        BaseNCodecInputStream decoderInputStream = new BaseNCodecInputStream(new ByteArrayInputStream(decoded, 0, length), baseNCodec, true);
        int decodedLength = decoderInputStream.read(singleByte, 0, 1);
        byteStream.write(singleByte);

        while (decodedLength > 0) {
            decodedLength = decoderInputStream.read(singleByte, 0, 1);
            byteStream.write(singleByte);
        }

        byte[] encoded = byteStream.toByteArray();
        assertArrayEquals(new byte[] { 1, 2, 3, 4, 5 }, encoded);
    }
}
package org.apache.commons.codec.binary;

import static org.apache.commons.codec.binary.BaseNCodecInputStream.EOF;
import static org.apache.commons.codec.binary.BaseNCodecInputStream. SHALL_NOT_SUPPORT_MARKS;
import static org.apache.commons.codec.binary.BaseNCodecInputStream.SKIP_IN_CHUNKS_OF_512_BYTES;
import static org.apache.commons.codec.binary.BaseNCodecInputStream.ONE;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.binary.BaseNCodec;
import org.apache.commons.codec.binary.BaseNCodecInputStream;
import org.junit.Test;

/**
 * Test class for BaseNCodecInputStream
 */
public class BaseNCodecInputStreamTest {

    private static final byte[] TEST_DATA = new byte[] { 0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07, 0x08, 0x09, 0x0A, 0x0B, 0x0C, 0x0D, 0x0E, 0x0F };
    private static final String BASE64_ENCODED_DATA = "VG8gZXJyIGlzIGh1bWFuLCBidXQgdG8gZW5jb2RlIGZvdWwgdGhpbmdzIHVwIGZvciBmaWxlIG9mIHRoZSBmaWxlIG15IG1vZGVsIGlzIGZvdWwgc2VuZG9ivaWzIGluIG1vZGVscyBmaWxlIG9mIHJlc3BvbnNlIG91ciBmaWxlIGNhbiBmb3Igc2VuZG9i";
    private static final String BASE32_ENCODED_DATA = "0102030405060708090A0B0C0D0E0F";

    @Test
    public void testConstructor() {
        InputStream inputStream = new ByteArrayInputStream(TEST_DATA);
        BaseNCodec baseNCodec = new BaseNCodec();
        BaseNCodecInputStream instance = new BaseNCodecInputStream(inputStream, baseNCodec, false);
        assertEquals("Expected instance of BaseNCodecInputStream", BaseNCodecInputStream.class, instance.getClass());
    }

    @Test
    public void testMarkSupported() {
        InputStream inputStream = new ByteArrayInputStream(TEST_DATA);
        BaseNCodec baseNCodec = new BaseNCodec();
        BaseNCodecInputStream instance = new BaseNCodecInputStream(inputStream, baseNCodec, false);
        assertFalse("Expected marking not to be supported", instance.markSupported());
    }

    @Test
    public void testRead() {
        InputStream inputStream = new ByteArrayInputStream(TEST_DATA);
        BaseNCodec baseNCodec = new BaseNCodec();
        BaseNCodecInputStream instance = new BaseNCodecInputStream(inputStream, baseNCodec, false);

        for (int i = 0; i < TEST_DATA.length; i++) {
            int readResult = instance.read();
            byte expectedByte = TEST_DATA[i];
            if (expectedByte < 0) {
                assertEquals("Expected EOF, but received different byte", EOF, readResult);
                break;
            }
            assertEquals("Expected byte " + expectedByte, (int) expectedByte, readResult);
        }

        int readResult = instance.read();
        assertEquals("Expected EOF", EOF, readResult);
    }

    @Test
    public void testReadByteArray() {
        InputStream inputStream = new ByteArrayInputStream(TEST_DATA);
        BaseNCodec baseNCodec = new BaseNCodec();
        BaseNCodecInputStream instance = new BaseNCodecInputStream(inputStream, baseNCodec, false);

        byte[] expectedResult = TEST_DATA;
        byte[] readResult = new byte[TEST_DATA.length];
        int length = instance.read(readResult);
        assertEquals("Expected to read " + TEST_DATA.length + " bytes", TEST_DATA.length, length);
        assertArrayEquals("Expected read result to match expected result", expectedResult, readResult);

        length = instance.read(readResult, 0, TEST_DATA.length + 1);
        assertEquals("Expected to read 0 bytes after EOF", 0, length);
    }

    @Test
    public void testReadWithDifferentEncoding() {
        InputStream inputStream = new ByteArrayInputStream(TEST_DATA);
        BaseNCodec baseNCodecBase64 = new Base64();
        BaseNCodecInputStream instance = new BaseNCodecInputStream(inputStream, baseNCodecBase64, true);

        String expectedBase64Data = new String(TEST_DATA);
        String actualBase64Data = new String(new sun.misc.BASE64Decoder().decodeBuffer(new ByteArrayInputStream(instance.readAllBytes())));
        assertEquals("Expected decoded base64 data to match expected data", expectedBase64Data, actualBase64Data);
    }

    @Test
    public void testReadWithBase32Encoding() {
        InputStream inputStream = new ByteArrayInputStream(TEST_DATA);
        BaseNCodec baseNCodecBase32 = new BaseNCodec(BaseNCodec.BASE32);
        BaseNCodecInputStream instance = new BaseNCodecInputStream(inputStream, baseNCodecBase32, false);

        String expectedBase32Data = BASE32_ENCODED_DATA;
        String actualBase32Data = new String(instance.readAllBytes());
        assertEquals("Expected decoded base32 data to match expected data", expectedBase32Data, actualBase32Data);
    }

    @Test
    public void testSkip() {
        InputStream inputStream = new ByteArrayInputStream(TEST_DATA);
        BaseNCodec baseNCodec = new BaseNCodec();
        BaseNCodecInputStream instance = new BaseNCodecInputStream(inputStream, baseNCodec, false);

        long skipLength = TEST_DATA.length - 3;
        instance.skip(skipLength);
        byte[] readResult = new byte[1];
        int length = instance.read(readResult);
        assertEquals("Expected to read 0 bytes after skipping", 0, length);
        assertTrue("Expected to have skipped the specified number of bytes", instance.skip(SKIP_IN_CHUNKS_OF_512_BYTES) >= skipLength);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testClose() {
        InputStream inputStream = new ByteArrayInputStream(TEST_DATA);
        BaseNCodec baseNCodec = new BaseNCodec();
        BaseNCodecInputStream instance = new BaseNCodecInputStream(inputStream, baseNCodec, false);
        assertFalse("Expected marking not to be supported", instance.markSupported());
        instance.close();
        assertThrows("Expected UnsupportedOperationException when calling close()", UnsupportedOperationException.class, () -> instance.markSupported());
    }

    @Test
    public void testIllegalNegativeSkipLength() {
        InputStream inputStream = new ByteArrayInputStream(TEST_DATA);
        BaseNCodec baseNCodec = new BaseNCodec();
        BaseNCodecInputStream instance = new BaseNCodecInputStream(inputStream, baseNCodec, false);
        assertThrows("Expected IllegalArgumentException with negative skip length", IllegalArgumentException.class,
                () -> instance.skip(-1));
    }
}
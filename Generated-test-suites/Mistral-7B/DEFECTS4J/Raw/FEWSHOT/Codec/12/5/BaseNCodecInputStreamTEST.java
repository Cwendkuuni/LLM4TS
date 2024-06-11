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

import org.junit.Test;

public class BaseNCodecInputStreamTest {

    private static final byte[] TEST_DATA = new byte[] { 0x00, 0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07, 0x08, 0x09, 0x0a, 0x0b, 0x0c, 0x0d, 0x0e, 0x0f };
    private static final byte[] ENCODED_DATA = new byte[] { 0x2a, 0x2a, 0x2a, 0x24, 0x29, 0x2a, 0x2a, 0x2a, 0x23, 0x2a, 0x2a, 0x2a, 0x22, 0x2a, 0x2a, 0x2a, 0x25, 0x2a, 0x2a, 0x2a, 0x26 };

    @Test
    public void testRead() throws IOException {
        BaseNCodec baseNCodec = new BaseNCodec();
        InputStream inputStream = new ByteArrayInputStream(TEST_DATA);
        BaseNCodecInputStream inputFilter = new BaseNCodecInputStream(inputStream, baseNCodec, false);

        byte[] readResult = new byte[1];
        int read;

        // read all bytes
        for (int i = 0; i < TEST_DATA.length; ++i) {
            read = inputFilter.read();
            assertEquals(TEST_DATA[i] & 0xFF, read);
            assertTrue(read != BaseNCodecInputStream.EOF);
            readResult[0] = (byte) read;
        }
        assertEquals(TEST_DATA.length, read);
        assertArrayEquals(TEST_DATA, readResult);

        // read EOF
        assertEquals(BaseNCodecInputStream.EOF, inputFilter.read());
        assertFalse(inputFilter.hasNextByte());
    }

    @Test
    public void testReadArray() throws IOException {
        BaseNCodec baseNCodec = new BaseNCodec();
        InputStream inputStream = new ByteArrayInputStream(TEST_DATA);
        BaseNCodecInputStream inputFilter = new BaseNCodecInputStream(inputStream, baseNCodec, false);

        byte[] readResult = new byte[TEST_DATA.length];
        int read = inputFilter.read(readResult, 0, readResult.length);
        assertEquals(TEST_DATA.length, read);
        assertArrayEquals(TEST_DATA, readResult);

        // read EOF
        byte[] eofResult = new byte[1];
        read = inputFilter.read(eofResult, 0, 1);
        assertEquals(BaseNCodecInputStream.EOF, read);
        assertNull(eofResult);
    }

    @Test
    public void testEncodeRead() throws IOException {
        BaseNCodec baseNCodec = new BaseNCodec();
        InputStream inputStream = new ByteArrayInputStream(ENCODED_DATA);
        BaseNCodecInputStream inputFilter = new BaseNCodecInputStream(inputStream, baseNCodec, true);

        byte[] readResult = new byte[1];
        int read;

        // read all bytes
        for (int i = 0; i < ENCODED_DATA.length; ++i) {
            read = inputFilter.read();
            assertEquals(ENCODED_DATA[i] & 0xFF, read);
            assertTrue(read != BaseNCodecInputStream.EOF);
            readResult[0] = (byte) read;
        }
        assertEquals(ENCODED_DATA.length, read);
        assertArrayEquals(ENCODED_DATA, readResult);

        // read EOF
        assertEquals(BaseNCodecInputStream.EOF, inputFilter.read());
        assertFalse(inputFilter.hasNextByte());
    }

    @Test
    public void testEncodeReadArray() throws IOException {
        BaseNCodec baseNCodec = new BaseNCodec();
        InputStream inputStream = new ByteArrayInputStream(ENCODED_DATA);
        BaseNCodecInputStream inputFilter = new BaseNCodecInputStream(inputStream, baseNCodec, true);

        byte[] readResult = new byte[ENCODED_DATA.length];
        int read = inputFilter.read(readResult, 0, readResult.length);
        assertEquals(ENCODED_DATA.length, read);
        assertArrayEquals(ENCODED_DATA, readResult);

        // read EOF
        byte[] eofResult = new byte[1];
        read = inputFilter.read(eofResult, 0, 1);
        assertEquals(BaseNCodecInputStream.EOF, read);
        assertNull(eofResult);
    }
}
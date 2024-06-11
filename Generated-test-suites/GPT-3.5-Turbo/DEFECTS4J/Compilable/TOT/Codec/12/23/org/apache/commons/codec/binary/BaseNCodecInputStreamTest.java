package org.apache.commons.codec.binary;
import org.junit.Before;
import org.junit.Test;
import org.apache.commons.codec.binary.BaseNCodecInputStream;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

import static org.junit.Assert.*;

public class BaseNCodecInputStreamTest {

    private BaseNCodecInputStream baseNCodecInputStream;
    private byte[] inputBytes;
    private static final String TEST_STRING = "Test String";
    private static final byte[] TEST_STRING_BYTES = TEST_STRING.getBytes(StandardCharsets.UTF_8);

    @Before
    public void setUp() {
        inputBytes = new byte[1024];
    }

    @Test
    public void read_singleByte_validInput_returnCorrectByteValue() throws IOException {
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(TEST_STRING_BYTES);
        baseNCodecInputStream = new BaseNCodecInputStream(byteArrayInputStream, null, false);
        int result = baseNCodecInputStream.read();
        assertEquals(TEST_STRING_BYTES[0], (byte) result);
    }

    @Test
    public void read_singleByte_emptyInputStream_returnEOF() throws IOException {
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(new byte[0]);
        baseNCodecInputStream = new BaseNCodecInputStream(byteArrayInputStream, null, false);
        int result = baseNCodecInputStream.read();
        assertEquals(-1, result);
    }

    @Test
    public void read_singleByte_EOFInput_returnEOF() throws IOException {
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(TEST_STRING.getBytes(StandardCharsets.UTF_8));
        baseNCodecInputStream = new BaseNCodecInputStream(byteArrayInputStream, null, false);
        // Simulate reaching EOF
        while (baseNCodecInputStream.read() != -1) {
        }
        int result = baseNCodecInputStream.read();
        assertEquals(-1, result);
    }

    @Test
    public void read_byteArray_validInput_returnCorrectBytesRead() throws IOException {
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(TEST_STRING_BYTES);
        baseNCodecInputStream = new BaseNCodecInputStream(byteArrayInputStream, null, false);
        int bytesRead = baseNCodecInputStream.read(inputBytes, 0, inputBytes.length);
        assertEquals(TEST_STRING_BYTES.length, bytesRead);
        assertArrayEquals(TEST_STRING_BYTES, inputBytes);
    }

    @Test(expected = NullPointerException.class)
    public void read_byteArray_nullInput_throwNullPointerException() throws IOException {
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(TEST_STRING_BYTES);
        baseNCodecInputStream = new BaseNCodecInputStream(byteArrayInputStream, null, false);
        baseNCodecInputStream.read(null, 0, 0);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void read_byteArray_invalidOffset_throwIndexOutOfBoundsException() throws IOException {
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(TEST_STRING_BYTES);
        baseNCodecInputStream = new BaseNCodecInputStream(byteArrayInputStream, null, false);
        baseNCodecInputStream.read(inputBytes, -1, 0);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void read_byteArray_invalidLength_throwIndexOutOfBoundsException() throws IOException {
         ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(TEST_STRING_BYTES);
        baseNCodecInputStream = new BaseNCodecInputStream(byteArrayInputStream, null, false);
        baseNCodecInputStream.read(inputBytes, 0, -1);
    }

     @Test(expected = IndexOutOfBoundsException.class)
    public void read_byteArray_invalidBufferSize_throwIndexOutOfBoundsException() throws IOException {
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(TEST_STRING_BYTES);
        baseNCodecInputStream = new BaseNCodecInputStream(byteArrayInputStream, null, false);
        baseNCodecInputStream.read(inputBytes, 0, inputBytes.length + 1);
    }

    @Test
    public void markSupported_always_returnFalse() {
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(TEST_STRING_BYTES);
        baseNCodecInputStream = new BaseNCodecInputStream(byteArrayInputStream, null, false);
        assertFalse(baseNCodecInputStream.markSupported());
    }

    @Test
    public void available_inputStreamHasReachedEOF_returnZero() throws IOException {
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(TEST_STRING_BYTES);
        baseNCodecInputStream = new BaseNCodecInputStream(byteArrayInputStream, null, false);
        // Read the entire input stream to reach EOF
        while (baseNCodecInputStream.read() != -1) {
        }
        int result = baseNCodecInputStream.available();
        assertEquals(0, result);
    }

    @Test
    public void available_inputStreamHasNotReachedEOF_returnOne() throws IOException {
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(TEST_STRING_BYTES);
        baseNCodecInputStream = new BaseNCodecInputStream(byteArrayInputStream, null, false);
        int result = baseNCodecInputStream.available();
        assertEquals(1, result);
    }
}
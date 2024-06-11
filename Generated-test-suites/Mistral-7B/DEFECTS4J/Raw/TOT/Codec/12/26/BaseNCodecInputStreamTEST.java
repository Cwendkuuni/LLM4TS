package org.apache.commons.codec.binary;

import static org.junit.Assert.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

import org.apache.commons.codec.binary.BaseNCodec;
import org.apache.commons.codec.binary.BaseNCodecInputStream;

/**
 * Test class for {@link BaseNCodecInputStream}
 */
public class BaseNCodecInputStreamTest {

    private static final BaseNCodec BASE_N_CODEC = new BaseNCodec();

    @org.junit.Test
    public void testReadSingleByte() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        outputStream.write(1);
        InputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());

        BaseNCodecInputStream baseNCodecInputStream = new BaseNCodecInputStream(inputStream, BASE_N_CODEC, false);

        int byteRead = baseNCodecInputStream.read();
        assertEquals(1, byteRead);
        assertEquals((byte) 1, baseNCodecInputStream.read(new byte[1][0], 0, 1)[0]);

        byteRead = baseNCodecInputStream.read();
        assertEquals(BaseNCodecInputStream.EOF, byteRead);
    }

    @org.junit.Test
    public void testReadMultipleBytes() throws IOException {
        String baseNEncoded = "5ZX9NWJX2P7RYQV";
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        BASE_N_CODEC.encode(baseNEncoded.getBytes(StandardCharsets.UTF_8), outputStream.toByteArray(), outputStream.size());
        InputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());

        BaseNCodecInputStream baseNCodecInputStream = new BaseNCodecInputStream(inputStream, BASE_N_CODEC, false);

        byte[] expectedData = new byte[] { 5, 'Z', 'X', 9, 'N', 'W', 'J', 'X', '2', 'P', '7', 'R', 'Y', 'Q', 'V' };
        byte[] readData = new byte[expectedData.length];

        int bytesRead = baseNCodecInputStream.read(readData, 0, expectedData.length);
        assertArrayEquals(expectedData, readData);

        bytesRead = baseNCodecInputStream.read(readData, 0, expectedData.length);
        assertEquals(BaseNCodecInputStream.EOF, bytesRead);
    }

    @org.junit.Test(expected = IOException.class)
    public void testReadWithInvalidInput() throws IOException {
        InputStream inputStream = new ByteArrayInputStream(new byte[] {});

        BaseNCodecInputStream baseNCodecInputStream = new BaseNCodecInputStream(inputStream, BASE_N_CODEC, false);
        baseNCodecInputStream.read();
    }

    @org.junit.Test(expected = NullPointerException.class)
    public void testReadWithNullDestination() {
        InputStream inputStream = new ByteArrayInputStream(new byte[] {});

        BaseNCodecInputStream baseNCodecInputStream = new BaseNCodecInputStream(inputStream, BASE_N_CODEC, false);
        baseNCodecInputStream.read(null, 0, 1);
    }

    @org.junit.Test(expected = IndexOutOfBoundsException.class)
    public void testReadWithInvalidOffset() {
        InputStream inputStream = new ByteArrayInputStream(new byte[] {});

        BaseNCodecInputStream baseNCodecInputStream = new BaseNCodecInputStream(inputStream, BASE_N_CODEC, false);
        baseNCodecInputStream.read(new byte[1][], -1, 1);
    }

    @org.junit.Test(expected = IndexOutOfBoundsException.class)
    public void testReadWithInvalidLength() {
        InputStream inputStream = new ByteArrayInputStream(new byte[] {});

        BaseNCodecInputStream baseNCodecInputStream = new BaseNCodecInputStream(inputStream, BASE_N_CODEC, false);
        baseNCodecInputStream.read(new byte[1][], 0, -1);
    }

    @org.junit.Test(expected = IndexOutOfBoundsException.class)
    public void testReadWithInvalidBufferSize() {
        InputStream inputStream = new ByteArrayInputStream(new byte[] {});

        BaseNCodecInputStream baseNCodecInputStream = new BaseNCodecInputStream(inputStream, BASE_N_CODEC, false);
        baseNCodecInputStream.read(new byte[] { 1, 2, 3 }, 3, 1);
    }

    @org.junit.Test
    public void testMarkSupported() throws IOException {
        InputStream inputStream = new ByteArrayInputStream(new byte[] {});

        BaseNCodecInputStream baseNCodecInputStream = new BaseNCodecInputStream(inputStream, BASE_N_CODEC, false);
        assertFalse(baseNCodecInputStream.markSupported());
    }

    @org.junit.Test(expected = IOException.class)
    public void testSkipWithInvalidLength() throws IOException {
        InputStream inputStream = new ByteArrayInputStream(new byte[] {});

        BaseNCodecInputStream baseNCodecInputStream = new BaseNCodecInputStream(inputStream, BASE_N_CODEC, false);
        baseNCodecInputStream.skip(Long.MAX_VALUE);
    }

    @org.junit.Test
    public void testAvailableBytes() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        outputStream.write(new byte[] { 1, 2, 3, 4, 5 });
        InputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());

        BaseNCodecInputStream baseNCodecInputStream = new BaseNCodecInputStream(inputStream, BASE_N_CODEC, false);
        int availableBytes = baseNCodecInputStream.available();
        assertEquals(5, availableBytes);
    }

    @org.junit.Test
    public void testClose() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        outputStream.write(new byte[] { 1, 2, 3, 4, 5 });
        InputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());

        BaseNCodecInputStream baseNCodecInputStream = new BaseNCodecInputStream(inputStream, BASE_N_CODEC, false);
        baseNCodecInputStream.close();

        try {
            baseNCodecInputStream.read();
            fail("IOException expected");
        } catch (IOException e) {
            // good, expected
        }
    }

    @org.junit.Test
    public void testEncodeDecode() throws IOException {
        byte[] sourceData = new byte[] { 1, 2, 3, 4, 5 };
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        BASE_N_CODEC.encode(sourceData, outputStream.toByteArray(), outputStream.size());
        InputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());

        BaseNCodecInputStream baseNCodecInputStream = new BaseNCodecInputStream(inputStream, BASE_N_CODEC, false);
        byte[] destinationData = new byte[sourceData.length];
        baseNCodecInputStream.read(destinationData, 0, sourceData.length);

        assertArrayEquals(sourceData, destinationData);
    }
}
package org.apache.commons.codec.binary;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import org.junit.Test;

public class BaseNCodecInputStreamTest {

    @Test
    public void testMarkSupported() {
        BaseNCodecInputStream inputStream = new BaseNCodecInputStream(new ByteArrayInputStream(new byte[0]), null, false);
        assertFalse(inputStream.markSupported());
    }

    @Test
    public void testRead() throws IOException {
        BaseNCodecInputStream inputStream = new BaseNCodecInputStream(new ByteArrayInputStream("ABCDEFG".getBytes()), new BaseNCodec(), false);

        assertEquals((int) 'A', inputStream.read());
        assertEquals((int) 'B', inputStream.read());
        assertEquals((int) 'C', inputStream.read());
        assertEquals((int) EOF, inputStream.read());

        inputStream = new BaseNCodecInputStream(new ByteArrayInputStream("ABCDEFG".getBytes()), new BaseNCodec(), true);

        assertEquals((int) 'A', inputStream.read());
        assertEquals((int) 'B', inputStream.read());
        assertEquals((int) 'C', inputStream.read());
        assertEquals((int) EOF, inputStream.read());
    }

    @Test
    public void testReadWithArray() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        outputStream.write("ABCDEFG".getBytes());
        BaseNCodec baseNCodec = new BaseNCodec();
        InputStream inputStream = new BaseNCodecInputStream(new ByteArrayInputStream(outputStream.toByteArray()), baseNCodec, false);

        byte[] buffer = new byte[3];

        int bytesRead = inputStream.read(buffer);
        assertEquals(3, bytesRead);
        assertEquals((int) 'A', buffer[0]);
        assertEquals((int) 'B', buffer[1]);
        assertEquals((int) 'C', buffer[2]);

        bytesRead = inputStream.read(buffer);
        assertEquals(3, bytesRead);
        assertEquals((int) 'D', buffer[0]);
        assertEquals((int) 'E', buffer[1]);
        assertEquals((int) 'F', buffer[2]);

        bytesRead = inputStream.read(buffer);
        assertEquals(1, bytesRead);
        assertEquals((int) EOF, buffer[0]);

        inputStream = new BaseNCodecInputStream(new ByteArrayInputStream(outputStream.toByteArray()), baseNCodec, true);

        bytesRead = inputStream.read(buffer);
        assertEquals(3, bytesRead);
        assertEquals((int) 'A', buffer[0]);
        assertEquals((int) 'B', buffer[1]);
        assertEquals((int) 'C', buffer[2]);

        bytesRead = inputStream.read(buffer);
        assertEquals(3, bytesRead);
        assertEquals((int) 'G', buffer[0]);
        assertEquals((int) 'F', buffer[1]);
        assertEquals((int) 'E', buffer[2]);

        bytesRead = inputStream.read(buffer);
        assertEquals(1, bytesRead);
        assertEquals((int) EOF, buffer[0]);
    }

    @Test
    public void testReadWithNullBuffer() {
        BaseNCodecInputStream inputStream = new BaseNCodecInputStream(new ByteArrayInputStream("ABCDEFG".getBytes()), new BaseNCodec(), false);

        assertNull(inputStream.read(null));
    }

    @Test
    public void testReadWithNegativeOffset() {
        BaseNCodecInputStream inputStream = new BaseNCodecInputStream(new ByteArrayInputStream("ABCDEFG".getBytes()), new BaseNCodec(), false);

        assertNull(inputStream.read(new byte[1], -1));
    }

    @Test
    public void testReadWithNegativeLength() {
        BaseNCodecInputStream inputStream = new BaseNCodecInputStream(new ByteArrayInputStream("ABCDEFG".getBytes()), new BaseNCodec(), false);

        assertNull(inputStream.read(new byte[1], -1));
    }

    @Test
    public void testReadWithOffsetOutsideBufferSize() {
        BaseNCodecInputStream inputStream = new BaseNCodecInputStream(new ByteArrayInputStream("ABCDEFG".getBytes()), new BaseNCodec(), false);

        byte[] buffer = new byte[5];

        assertNull(inputStream.read(buffer, 5));
    }

    @Test
    public void testReadWithOffsetAndLengthOutsideBufferSize() {
        BaseNCodecInputStream inputStream = new BaseNCodecInputStream(new ByteArrayInputStream("ABCDEFG".getBytes()), new BaseNCodec(), false);

        byte[] buffer = new byte[5];

        assertNull(inputStream.read(buffer, 2, 3));
    }

    @Test
    public void testReadWithLimitReached() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        outputStream.write("ABCDEFG".getBytes());
        BaseNCodec baseNCodec = new BaseNCodec();
        InputStream inputStream = new BaseNCodecInputStream(new ByteArrayInputStream(outputStream.toByteArray()), baseNCodec, false);

        byte[] buffer = new byte[7];

        int bytesRead = inputStream.read(buffer);
        assertEquals(7, bytesRead);

        bytesRead = inputStream.read(buffer, 0, 7);
        assertEquals(7, bytesRead);

        bytesRead = inputStream.read(buffer, 0, 8);
        assertEquals(7, bytesRead);
    }

    @Test
    public void testReadWithLimitReachedAndNegativeOffset() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        outputStream.write("ABCDEFG".getBytes());
        BaseNCodec baseNCodec = new BaseNCodec();
        InputStream inputStream = new BaseNCodecInputStream(new ByteArrayInputStream(outputStream.toByteArray()), baseNCodec, false);

        byte[] buffer = new byte[7];

        int bytesRead = inputStream.read(buffer, -1);
        assertEquals(7, bytesRead);

        bytesRead = inputStream.read(buffer, -5, 7);
        assertEquals(7, bytesRead);
    }

    @Test
    public void testReadWithLimitReachedAndNegativeLength() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        outputStream.write("ABCDEFG".getBytes());
        BaseNCodec baseNCodec = new BaseNCodec();
        InputStream inputStream = new BaseNCodecInputStream(new ByteArrayInputStream(outputStream.toByteArray()), baseNCodec, false);

        byte[] buffer = new byte[7];

        int bytesRead = inputStream.read(buffer, 0, -1);
        assertEquals(7, bytesRead);

        bytesRead = inputStream.read(buffer, 0, -5);
        assertEquals(7, bytesRead);
    }

    @Test
    public void testReadWithLimitReachedAndOffsetOutsideBufferSize() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        outputStream.write("ABCDEFG".getBytes());
        BaseNCodec baseNCodec = new BaseNCodec();
        InputStream inputStream = new BaseNCodecInputStream(new ByteArrayInputStream(outputStream.toByteArray()), baseNCodec, false);

        byte[] buffer = new byte[7];

        int bytesRead = inputStream.read(buffer, 5, 7);
        assertEquals(2, bytesRead);
    }

    @Test
    public void testReadWithLimitReachedAndOffsetAndLengthOutsideBufferSize() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        outputStream.write("ABCDEFG".getBytes());
        BaseNCodec baseNCodec = new BaseNCodec();
        InputStream inputStream = new BaseNCodecInputStream(new ByteArrayInputStream(outputStream.toByteArray()), baseNCodec, false);

        byte[] buffer = new byte[7];

        int bytesRead = inputStream.read(buffer, 2, 5);
        assertEquals(0, bytesRead);
    }

    @Test
    public void testSkip() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        outputStream.write("ABCDEFG".getBytes());
        BaseNCodec baseNCodec = new BaseNCodec();
        InputStream inputStream = new BaseNCodecInputStream(new ByteArrayInputStream(outputStream.toByteArray()), baseNCodec, false);

        assertEquals(0, inputStream.skip(0));
        assertEquals(0, inputStream.skip(1));
        assertEquals(0, inputStream.skip(11));
        assertEquals(6, inputStream.skip(1));

        byte[] buffer = new byte[6];

        int bytesRead = inputStream.read(buffer);
        assertEquals(6, bytesRead);

        assertEquals(6, inputStream.skip(6));
        assertEquals(0, inputStream.skip(1));
        assertEquals(0, inputStream.skip(Long.MAX_VALUE));
    }

    @Test
    public void testSkipWithNegativeNumber() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        outputStream.write("ABCDEFG".getBytes());
        BaseNCodec baseNCodec = new BaseNCodec();
        InputStream inputStream = new BaseNCodecInputStream(new ByteArrayInputStream(outputStream.toByteArray()), baseNCodec, false);

        assertEquals(0, inputStream.skip(-1));
        assertEquals(0, inputStream.skip(-2));
        assertEquals(0, inputStream.skip(-11));
        assertEquals(6, inputStream.skip(-1));
    }

    @Test
    public void testClose() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        BaseNCodec baseNCodec = new BaseNCodec();
        InputStream inputStream = new BaseNCodecInputStream(new ByteArrayInputStream(outputStream.toByteArray()), baseNCodec, false);

        inputStream.close();
        assertTrue(inputStream.markSupported()); // should be false after close()
    }

    @Test(expected = IOException.class)
    public void testCloseTwice() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        BaseNCodec baseNCodec = new BaseNCodec();
        InputStream inputStream = new BaseNCodecInputStream(new ByteArrayInputStream(outputStream.toByteArray()), baseNCodec, false);

        inputStream.close();
        inputStream.close();
    }

    @Test
    public void testAvailable() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        outputStream.write("ABCDEFG".getBytes());
        BaseNCodec baseNCodec = new BaseNCodec();
        InputStream inputStream = new BaseNCodecInputStream(new ByteArrayInputStream(outputStream.toByteArray()), baseNCodec, false);

        assertEquals(7, inputStream.available());
    }

    @Test
    public void testAvailableWithDataConsumed() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        outputStream.write("ABCDEFG".getBytes());
        BaseNCodec baseNCodec = new BaseNCodec();
        InputStream inputStream = new BaseNCodecInputStream(new ByteArrayInputStream(outputStream.toByteArray()), baseNCodec, false);

        byte[] buffer = new byte[6];

        inputStream.read(buffer);
        inputStream.read(buffer, 0, 1);

        assertEquals(0, inputStream.available());
    }

    @Test
    public void testAvailableWithDataConsumedAndMoreData() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        outputStream.write("ABCDEFG".getBytes());
        BaseNCodec baseNCodec = new BaseNCodec();
        InputStream inputStream = new BaseNCodecInputStream(new ByteArrayInputStream(outputStream.toByteArray()), baseNCodec, false);

        byte[] buffer = new byte[6];

        inputStream.read(buffer);
        inputStream.read(buffer, 0, 1);

        inputStream.skip(3);

        assertEquals(4, inputStream.available());
    }

    @Test
    public void testAvailableWithDataConsumedAndEndOfStream() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        outputStream.write("ABCDEFG".getBytes());
        BaseNCodec baseNCodec = new BaseNCodec();
        InputStream inputStream = new BaseNCodecInputStream(new ByteArrayInputStream(outputStream.toByteArray()), baseNCodec, false);

        byte[] buffer = new byte[6];

        inputStream.read(buffer);
        inputStream.read(buffer, 0, 1);

        inputStream.read();

        assertEquals(0, inputStream.available());
    }

    @Test
    public void testAvailableWithDataConsumedAndEndOfStreamAndMoreData() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        outputStream.write("ABCDEFG".getBytes());
        BaseNCodec baseNCodec = new BaseNCodec();
        InputStream inputStream = new BaseNCodecInputStream(new ByteArrayInputStream(outputStream.toByteArray()), baseNCodec, false);

        byte[] buffer = new byte[6];

        inputStream.read(buffer);
        inputStream.read(buffer, 0, 1);

        inputStream.skip(3);

        inputStream = new BaseNCodecInputStream(new ByteArrayInputStream(outputStream.toByteArray()), baseNCodec, false);

        assertEquals(4, inputStream.available());
    }
}
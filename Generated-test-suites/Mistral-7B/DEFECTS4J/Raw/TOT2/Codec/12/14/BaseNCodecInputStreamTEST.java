import org.junit.Assert.*;
import org.junit.Test;

import java.io.*;

public class BaseNCodecInputStreamTest {

    private static final BaseNCodec baseNCodec = new BaseNCodec();

    @Test
    public void testRead() throws IOException {
        BaseNCodecInputStream inputStream = new BaseNCodecInputStream(new ByteArrayInputStream("test data".getBytes()), baseNCodec, false);

        byte[] expectedByte = new byte[] { 72 };
        byte readByte = 0;

        int actualResult = inputStream.read();

        assertEquals(expectedByte[0], actualResult);
    }

    @Test
    public void testReadByteArray() throws IOException {
        BaseNCodecInputStream inputStream = new BaseNCodecInputStream(new ByteArrayInputStream("test data".getBytes()), baseNCodec, false);

        byte[] expectedByteArray = new byte[] { 72, 69, 70 };
        byte[] readByteArray = new byte[3];

        int actualResult = inputStream.read(readByteArray, 0, 3);

        assertArrayEquals(expectedByteArray, readByteArray);
    }

    @Test
    public void testMarkSupported() {
        BaseNCodecInputStream inputStream = new BaseNCodecInputStream(new ByteArrayInputStream("test data".getBytes()), baseNCodec, false);

        assertFalse(inputStream.markSupported());
    }

    @Test
    public void testReadAtEOF() throws IOException {
        BaseNCodecInputStream inputStream = new BaseNCodecInputStream(new ByteArrayInputStream("test data".getBytes()), baseNCodec, false);
        inputStream.read();
        inputStream.read();

        assertEquals(-1, inputStream.read());
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testReadWithNegativeOffset() throws IOException {
        BaseNCodecInputStream inputStream = new BaseNCodecInputStream(new ByteArrayInputStream("test data".getBytes()), baseNCodec, false);
        byte[] readByteArray = new byte[3];

        inputStream.read(readByteArray, -1, 3);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testReadWithNegativeLength() throws IOException {
        BaseNCodecInputStream inputStream = new BaseNCodecInputStream(new ByteArrayInputStream("test data".getBytes()), baseNCodec, false);
        byte[] readByteArray = new byte[3];

        inputStream.read(readByteArray, 0, -1);
    }

    @Test
    public void testReadWithOffsetAndLengthExceedingAvailableData() throws IOException {
        BaseNCodecInputStream inputStream = new BaseNCodecInputStream(new ByteArrayInputStream("test data".getBytes()), baseNCodec, false);
        byte[] readByteArray = new byte[3];

        inputStream.read(readByteArray, 0, 4);

        assertEquals(3, inputStream.read(readByteArray, 0, 3));
    }

    @Test(expected = IOException.class)
    public void testReadWithIOException() throws IOException, InterruptedException {
        BaseNCodecInputStream inputStream = new BaseNCodecInputStream(new ByteArrayInputStream("test data".getBytes()), baseNCodec, false) {
            @Override
            public int read() throws IOException {
                throw new IOException("Simulated IOException");
            }
        };

        byte[] readByteArray = new byte[3];

        inputStream.read(readByteArray, 0, 3);
    }

    @Test
    public void testSkipPositiveNumberOfBytes() throws IOException {
        BaseNCodecInputStream inputStream = new BaseNCodecInputStream(new ByteArrayInputStream("test data".getBytes()), baseNCodec, false);

        long numOfBytesToSkip = 10;

        long actualResult = inputStream.skip(numOfBytesToSkip);

        assertEquals(numOfBytesToSkip, actualResult);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSkipNegativeNumberOfBytes() throws IOException {
        BaseNCodecInputStream inputStream = new BaseNCodecInputStream(new ByteArrayInputStream("test data".getBytes()), baseNCodec, false);

        long numOfBytesToSkip = -10;

        inputStream.skip(numOfBytesToSkip);
    }

    @Test(expected = IOException.class)
    public void testSkipWithIOException() throws IOException, InterruptedException {
        BaseNCodecInputStream inputStream = new BaseNCodecInputStream(new ByteArrayInputStream("test data".getBytes()), baseNCodec, false) {
            @Override
            public int read() throws IOException {
                throw new IOException("Simulated IOException");
            }
        };

        long numOfBytesToSkip = 10;

        inputStream.skip(numOfBytesToSkip);
    }
}
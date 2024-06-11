import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import static org.junit.Assert.*;

public class BaseNCodecInputStreamTest {

    private BaseNCodec baseNCodec;

    @Before
    public void setUp() throws Exception {
        baseNCodec = new BaseNCodec();
    }

    @Test
    public void read_shouldReturnByteInRange0To255() throws IOException {
        // Arrange
        final byte b = (byte) 200;
        final InputStream inputStream = new ByteArrayInputStream(new byte[]{b});
        final BaseNCodecInputStream baseNCodecInputStream = new BaseNCodecInputStream(inputStream, baseNCodec, true);

        // Act
        final int result = baseNCodecInputStream.read();

        // Assert
        assertEquals(b & 0xFF, result);
    }

    @Test
    public void read_shouldReturnNegativeOneIfEOF() throws IOException {
        // Arrange
        final InputStream inputStream = new ByteArrayInputStream(new byte[0]);
        final BaseNCodecInputStream baseNCodecInputStream = new BaseNCodecInputStream(inputStream, baseNCodec, true);

        // Act
        final int result = baseNCodecInputStream.read();

        // Assert
        assertEquals(-1, result);
    }

    @Test
    public void read_withByteArray_shouldReturnNumberOfBytesRead() throws IOException {
        // Arrange
        final byte[] byteArray = new byte[]{(byte) 1, (byte) 2, (byte) 3, (byte) 4};
        final InputStream inputStream = new ByteArrayInputStream(byteArray);
        final BaseNCodecInputStream baseNCodecInputStream = new BaseNCodecInputStream(inputStream, baseNCodec, true);
        final byte[] buffer = new byte[3];

        // Act
        final int result = baseNCodecInputStream.read(buffer, 0, 3);

        // Assert
        assertEquals(3, result);
    }

    @Test(expected = NullPointerException.class)
    public void read_withNullByteArray_throwsNullPointerException() throws IOException {
        // Arrange
        final InputStream inputStream = new ByteArrayInputStream(new byte[]{(byte) 1});
        final BaseNCodecInputStream baseNCodecInputStream = new BaseNCodecInputStream(inputStream, baseNCodec, true);

        // Act
        baseNCodecInputStream.read(null, 0, 1);

        // Assert
        // Exception is thrown
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void read_withOffsetLessThanZero_throwsIndexOutOfBoundsException() throws IOException {
        // Arrange
        final InputStream inputStream = new ByteArrayInputStream(new byte[]{(byte) 1});
        final BaseNCodecInputStream baseNCodecInputStream = new BaseNCodecInputStream(inputStream, baseNCodec, true);

        // Act
        baseNCodecInputStream.read(new byte[1], -1, 1);

        // Assert
        // Exception is thrown
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void read_withLenLessThanZero_throwsIndexOutOfBoundsException() throws IOException {
        // Arrange
        final InputStream inputStream = new ByteArrayInputStream(new byte[]{(byte) 1});
        final BaseNCodecInputStream baseNCodecInputStream = new BaseNCodecInputStream(inputStream, baseNCodec, true);

        // Act
        baseNCodecInputStream.read(new byte[1], 0, -1);

        // Assert
        // Exception is thrown
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void read_withOffsetGreaterThanBufferLength_throwsIndexOutOfBoundsException() throws IOException {
        // Arrange
        final InputStream inputStream = new ByteArrayInputStream(new byte[]{(byte) 1});
        final BaseNCodecInputStream baseNCodecInputStream = new BaseNCodecInputStream(inputStream, baseNCodec, true);

        // Act
        baseNCodecInputStream.read(new byte[1], 2, 1);

        // Assert
        // Exception is thrown
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void read_withOffsetPlusLenGreaterThanBufferLength_throwsIndexOutOfBoundsException() throws IOException {
        // Arrange
        final InputStream inputStream = new ByteArrayInputStream(new byte[]{(byte) 1});
        final BaseNCodecInputStream baseNCodecInputStream = new BaseNCodecInputStream(inputStream, baseNCodec, true);

        // Act
        baseNCodecInputStream.read(new byte[1], 0, 2);

        // Assert
        // Exception is thrown
    }

    @Test
    public void read_withLenZero_shouldReturnZero() throws IOException {
        // Arrange
        final InputStream inputStream = new ByteArrayInputStream(new byte[]{(byte) 1});
        final BaseNCodecInputStream baseNCodecInputStream = new BaseNCodecInputStream(inputStream, baseNCodec, true);

        // Act
        final int result = baseNCodecInputStream.read(new byte[1], 0, 0);

        // Assert
        assertEquals(0, result);
    }

    @Test
    public void markSupported_shouldReturnFalse() throws IOException {
        // Arrange
        final InputStream inputStream = new ByteArrayInputStream(new byte[]{(byte) 1});
        final BaseNCodecInputStream baseNCodecInputStream = new BaseNCodecInputStream(inputStream, baseNCodec, true);

        // Act
        final boolean result = baseNCodecInputStream.markSupported();

        // Assert
        assertFalse(result);
    }

    @Test
    public void available_shouldReturnZeroIfEOF() throws IOException {
        // Arrange
        final InputStream inputStream = new ByteArrayInputStream(new byte[0]);
        final BaseNCodecInputStream baseNCodecInputStream = new BaseNCodecInputStream(inputStream, baseNCodec, true);

        // Act
        final int result = baseNCodecInputStream.available();

        // Assert
        assertEquals(0, result);
    }

    @Test
    public void available_shouldReturnOneIfNotEOF() throws IOException {
        // Arrange
        final InputStream inputStream = new ByteArrayInputStream(new byte[]{(byte) 1});
        final BaseNCodecInputStream baseNCodecInputStream = new BaseNCodecInputStream(inputStream, baseNCodec, true);

        // Act
        final int result = baseNCodecInputStream.available();

        // Assert
        assertEquals(1, result);
    }
}
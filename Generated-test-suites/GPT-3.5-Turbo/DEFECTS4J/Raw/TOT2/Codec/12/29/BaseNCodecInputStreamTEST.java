import org.junit.Test;

import static org.junit.Assert.*;

public class BaseNCodecInputStreamTest {

    @Test
    public void testRead_singleByte() {
        // Arrange
        BaseNCodec baseNCodec = new BaseNCodec();
        byte[] data = {65}; // Character 'A'
        ByteArrayInputStream inputStream = new ByteArrayInputStream(data);
        BaseNCodecInputStream codecInputStream = new BaseNCodecInputStream(inputStream, baseNCodec, false);

        // Act
        int result = codecInputStream.read();

        // Assert
        assertEquals(65, result);
    }

    @Test
    public void testRead_invalidOffset_exceptionThrown() {
        // Arrange
        BaseNCodec baseNCodec = new BaseNCodec();

        // Act & Assert
        assertThrows(IndexOutOfBoundsException.class, () -> {
            byte[] data = new byte[10];
            ByteArrayInputStream inputStream = new ByteArrayInputStream(data);
            BaseNCodecInputStream codecInputStream = new BaseNCodecInputStream(inputStream, baseNCodec, false);
            codecInputStream.read(data, -1, 5);
        });
    }

    @Test
    public void testMarkSupported_alwaysReturnsFalse() {
        // Arrange
        BaseNCodec baseNCodec = new BaseNCodec();
        ByteArrayInputStream inputStream = new ByteArrayInputStream(new byte[0]);
        BaseNCodecInputStream codecInputStream = new BaseNCodecInputStream(inputStream, baseNCodec, false);

        // Act
        boolean result = codecInputStream.markSupported();

        // Assert
        assertFalse(result);
    }

    @Test
    public void testRead_incompleteBuffer_fillWithMultipleReads() {
        // Arrange
        BaseNCodec baseNCodec = new BaseNCodec();
        byte[] data = {65, 66, 67}; // Characters 'A', 'B', 'C'
        ByteArrayInputStream inputStream = new ByteArrayInputStream(data);
        BaseNCodecInputStream codecInputStream = new BaseNCodecInputStream(inputStream, baseNCodec, false);
        byte[] buffer = new byte[5]; // Larger buffer than data size

        // Act
        int result = codecInputStream.read(buffer, 0, 5);

        // Assert
        assertEquals(3, result);
        assertArrayEquals(new byte[]{65, 66, 67, 0, 0}, buffer);
    }

    @Test
    public void testRead_emptyBuffer_returnZero() {
        // Arrange
        BaseNCodec baseNCodec = new BaseNCodec();
        ByteArrayInputStream inputStream = new ByteArrayInputStream(new byte[0]);
        BaseNCodecInputStream codecInputStream = new BaseNCodecInputStream(inputStream, baseNCodec, false);
        byte[] buffer = new byte[5];

        // Act
        int result = codecInputStream.read(buffer, 0, 0);

        // Assert
        assertEquals(0, result);
        assertArrayEquals(new byte[5], buffer);
    }

}
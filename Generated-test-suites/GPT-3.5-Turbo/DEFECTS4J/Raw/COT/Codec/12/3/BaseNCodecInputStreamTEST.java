
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import static org.junit.Assert.assertEquals;

public class BaseNCodecInputStreamTest {

    @Test
    public void testReadOneByte() throws IOException {
        // Arrange
        byte[] input = {65, 66, 67}; // ASCII values of 'A', 'B', 'C'
        ByteArrayInputStream inputStream = new ByteArrayInputStream(input);
        BaseNCodec baseNCodec = new BaseNCodec();
        BaseNCodecInputStream baseNCodecInputStream = new BaseNCodecInputStream(inputStream, baseNCodec, false);
        
        // Act
        int result = baseNCodecInputStream.read();
        
        // Assert
        assertEquals(65, result);
    }

    @Test
    public void testReadMultipleBytes() throws IOException {
        // Arrange
        byte[] input = {65, 66, 67}; // ASCII values of 'A', 'B', 'C'
        ByteArrayInputStream inputStream = new ByteArrayInputStream(input);
        BaseNCodec baseNCodec = new BaseNCodec();
        BaseNCodecInputStream baseNCodecInputStream = new BaseNCodecInputStream(inputStream, baseNCodec, false);
        byte[] expectedResult = {65, 66};
        byte[] result = new byte[2];
        
        // Act
        int bytesRead = baseNCodecInputStream.read(result, 0, 2);
        
        // Assert
        assertEquals(2, bytesRead);
        assertArrayEquals(expectedResult, result);
    }

    @Test(expected = NullPointerException.class)
    public void testReadWithNullByteArr() throws IOException {
        // Arrange
        ByteArrayInputStream inputStream = new ByteArrayInputStream(new byte[0]);
        BaseNCodec baseNCodec = new BaseNCodec();
        BaseNCodecInputStream baseNCodecInputStream = new BaseNCodecInputStream(inputStream, baseNCodec, false);
        
        // Act
        baseNCodecInputStream.read(null, 0, 1);
        
        // Assert
        // Expecting NullPointerException
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testReadWithNegativeOffset() throws IOException {
        // Arrange
        ByteArrayInputStream inputStream = new ByteArrayInputStream(new byte[0]);
        BaseNCodec baseNCodec = new BaseNCodec();
        BaseNCodecInputStream baseNCodecInputStream = new BaseNCodecInputStream(inputStream, baseNCodec, false);
        
        // Act
        baseNCodecInputStream.read(new byte[1], -1, 1);
        
        // Assert
        // Expecting IndexOutOfBoundsException
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testReadWithInvalidOffsetLen() throws IOException {
        // Arrange
        ByteArrayInputStream inputStream = new ByteArrayInputStream(new byte[0]);
        BaseNCodec baseNCodec = new BaseNCodec();
        BaseNCodecInputStream baseNCodecInputStream = new BaseNCodecInputStream(inputStream, baseNCodec, false);
        
        // Act
        baseNCodecInputStream.read(new byte[1], 1, 1);
        
        // Assert
        // Expecting IndexOutOfBoundsException
    }

    @Test
    public void testReadWithZeroLen() throws IOException {
        // Arrange
        ByteArrayInputStream inputStream = new ByteArrayInputStream(new byte[0]);
        BaseNCodec baseNCodec = new BaseNCodec();
        BaseNCodecInputStream baseNCodecInputStream = new BaseNCodecInputStream(inputStream, baseNCodec, false);
        
        // Act
        int bytesRead = baseNCodecInputStream.read(new byte[1], 0, 0);
        
        // Assert
        assertEquals(0, bytesRead);
    }
}

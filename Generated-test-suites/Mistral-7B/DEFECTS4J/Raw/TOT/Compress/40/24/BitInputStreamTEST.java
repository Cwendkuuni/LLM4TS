import org.apache.commons.compress.utils.BitInputStream;
import org.apache.commons.compress.utils.ByteOrder;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThrows;
import static org.mockito.Mockito.verify;

public class BitInputStreamTest {

    private InputStream inputStreamMock;
    private BitInputStream bitInputStream;

    @Before
    public void setUp() {
        inputStreamMock = Mockito.mock(InputStream.class);
        bitInputStream = new BitInputStream(inputStreamMock, ByteOrder.LITTLE_ENDIAN);
    }

    @After
    public void tearDown() throws Exception {
        bitInputStream.close();
        Mockito.verify(inputStreamMock).close();
    }

    @Test
    public void testReadBits_ValidCount_ReturnsExpectedBits() throws IOException {
        // Given
        final byte[] testData = {(byte) 0b11111111, (byte) 0b01111111, (byte) 0b10111111};
        final InputStream inputStreamWithData = new ByteArrayInputStream(testData);
        Mockito.when(inputStreamMock.read()).thenReturn(testData[0], testData[1], testData[1], testData[2], -1);

        // When
        long expectedBits1 = 0b11111111;
        long expectedBits2 = 0b01111111;
        long expectedBits3 = 0b10111111;
        long actualBits1 = bitInputStream.readBits(8);
        long actualBits2 = bitInputStream.readBits(5);
        long actualBits3 = bitInputStream.readBits(12);

        // Then
        assertEquals(expectedBits1, actualBits1);
        assertEquals(expectedBits2, actualBits2);
        assertEquals(expectedBits3, actualBits3);

        // Verify
        ArgumentCaptor<Integer> countCaptor = ArgumentCaptor.forClass(Integer.class);
        verify(inputStreamMock).read();
        verify(inputStreamMock).read();
        verify(inputStreamMock).read();
        verify(inputStreamMock).read();
        verify(inputStreamMock).read();
        verify(inputStreamMock).read();
        verify(inputStreamMock).read();
        verify(inputStreamMock).read();
        verify(inputStreamMock).read();
        verify(inputStreamMock).read();
        verify(inputStreamMock).read();
        verify(inputStreamMock).read();
    }

    @Test
    public void testReadBits_InvalidCount_ThrowsException() throws IOException {
        // Given

        // When & Then
        assertThrows(IllegalArgumentException.class, () -> bitInputStream.readBits(-1));
    }

    @Test
    public void testReadBits_EndOfStream_ReturnsMinusOne() throws IOException {
        // Given
        Mockito.when(inputStreamMock.read()).thenReturn(-1);

        // When
        long actualBits = bitInputStream.readBits(5);

        // Then
        assertEquals(-1L, actualBits);
    }

    @Test
    public void testClearBitCache() throws IOException {
        // Given
        Mockito.when(inputStreamMock.read()).thenReturn(0b11111111);
        bitInputStream.readBits(8);

        // When
        bitInputStream.clearBitCache();

        // Then
        assertEquals(0, bitInputStream.readBits(8));
    }
}
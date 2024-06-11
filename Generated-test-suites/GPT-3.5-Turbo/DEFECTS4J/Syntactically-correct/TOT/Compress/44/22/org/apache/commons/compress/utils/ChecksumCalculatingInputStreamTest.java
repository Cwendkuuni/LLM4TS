import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.CRC32;
import java.util.zip.Checksum;

import static org.junit.jupiter.api.Assertions.*;

public class ChecksumCalculatingInputStreamTest {

    @Test
    public void testRead_SingleByte() throws IOException {
        Checksum checksumMock = Mockito.mock(Checksum.class);
        InputStream inputStream = new ByteArrayInputStream(new byte[]{0x01, 0x02, 0x03});
        ChecksumCalculatingInputStream calculatingInputStream = new ChecksumCalculatingInputStream(checksumMock, inputStream);

        int result = calculatingInputStream.read();

        assertEquals(0x01, result);
        Mockito.verify(checksumMock).update(0x01);
    }

    @Test
    public void testRead_ByteArray() throws IOException {
        Checksum checksumMock = Mockito.mock(Checksum.class);
        byte[] data = {0x01, 0x02, 0x03};
        InputStream inputStream = new ByteArrayInputStream(data);
        ChecksumCalculatingInputStream calculatingInputStream = new ChecksumCalculatingInputStream(checksumMock, inputStream);
        byte[] buffer = new byte[3];

        int result = calculatingInputStream.read(buffer);

        assertEquals(3, result);
        assertArrayEquals(data, buffer);
        Mockito.verify(checksumMock).update(data, 0, 3);
    }

    @Test
    public void testRead_ByteArray_WithOffset() throws IOException {
        Checksum checksumMock = Mockito.mock(Checksum.class);
        byte[] data = {0x01, 0x02, 0x03};
        InputStream inputStream = new ByteArrayInputStream(data);
        ChecksumCalculatingInputStream calculatingInputStream = new ChecksumCalculatingInputStream(checksumMock, inputStream);
        byte[] buffer = new byte[4];

        int result = calculatingInputStream.read(buffer, 1, 3);

        assertEquals(3, result);
        assertEquals(0x01, buffer[1]);
        assertEquals(0x02, buffer[2]);
        assertEquals(0x03, buffer[3]);
        Mockito.verify(checksumMock).update(data, 0, 3);
    }

    @Test
    public void testSkip() throws IOException {
        Checksum checksumMock = Mockito.mock(Checksum.class);
        InputStream inputStream = new ByteArrayInputStream(new byte[]{0x01, 0x02, 0x03});
        ChecksumCalculatingInputStream calculatingInputStream = new ChecksumCalculatingInputStream(checksumMock, inputStream);

        long result = calculatingInputStream.skip(2);

        assertEquals(2L, result);
        Mockito.verify(checksumMock, Mockito.times(2)).update(Mockito.anyByte());
    }

    @Test
    public void testGetValue() {
        Checksum checksumMock = Mockito.mock(Checksum.class);
        Mockito.when(checksumMock.getValue()).thenReturn(123456789L);
        InputStream inputStream = Mockito.mock(InputStream.class);
        ChecksumCalculatingInputStream calculatingInputStream = new ChecksumCalculatingInputStream(checksumMock, inputStream);

        long result = calculatingInputStream.getValue();

        assertEquals(123456789L, result);
    }
}
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import static org.mockito.Mockito.*;

public class ZipArchiveInputStreamTest {

    private ZipArchiveInputStream zipArchiveInputStream;
    private InputStream mockInputStream;

    @Before
    public void setUp() throws IOException {
        MockitoAnnotations.initMocks(this);
        mockInputStream = mock(InputStream.class);
        zipArchiveInputStream = new ZipArchiveInputStream(mockInputStream);
    }

    @After
    public void tearDown() throws IOException {
        zipArchiveInputStream.close();
    }

    @Test
    public void getNextZipEntry_shouldReturnNextEntry() throws IOException {
        // Arrange
        byte[] lfh = new byte[]{0x04, 0x03, 0x4B, 0x50};
        when(mockInputStream.read(any(byte[].class))).thenReturn(30).thenReturn(-1);
        when(mockInputStream.read(eq(lfh))).thenReturn(4);
        
        // Act
        ZipArchiveEntry result = zipArchiveInputStream.getNextZipEntry();

        // Assert
        Assert.assertNotNull(result);
    }

    @Test
    public void getNextZipEntry_shouldReturnNullWhenEOFExceptionIsThrown() throws IOException {
        // Arrange
        byte[] lfh = new byte[]{0x04, 0x03, 0x4B, 0x50};
        when(mockInputStream.read(any(byte[].class))).thenReturn(30).thenThrow(new EOFException());
        when(mockInputStream.read(eq(lfh))).thenReturn(4);

        // Act
        ZipArchiveEntry result = zipArchiveInputStream.getNextZipEntry();

        // Assert
        Assert.assertNull(result);
    }

    @Test
    public void getNextZipEntry_shouldReturnNullIfCentralDirectoryIsHit() throws IOException {
        // Arrange
        byte[] cfh = new byte[]{0x02, 0x02, 0x4B, 0x50};
        when(mockInputStream.read(any(byte[].class))).thenReturn(30);
        when(mockInputStream.read(eq(cfh))).thenReturn(2);

        // Act
        zipArchiveInputStream.getNextZipEntry();
        ZipArchiveEntry result = zipArchiveInputStream.getNextZipEntry();

        // Assert
        Assert.assertNull(result);
    }

    @Test
    public void read_shouldReadDataFromInputStream() throws IOException {
        // Arrange
        byte[] buffer = new byte[10];
        when(mockInputStream.read(eq(buffer), anyInt(), anyInt())).thenReturn(10);

        // Act
        int result = zipArchiveInputStream.read(buffer, 0, 10);

        // Assert
        Assert.assertEquals(10, result);
    }

    @Test(expected = IOException.class)
    public void read_shouldThrowIOExceptionIfStreamIsClosed() throws IOException {
        // Arrange
        zipArchiveInputStream.close();
        byte[] buffer = new byte[10];

        // Act
        zipArchiveInputStream.read(buffer, 0, 10);
    }

    @Test
    public void close_shouldCloseInputStream() throws IOException {
        // Act
        zipArchiveInputStream.close();

        // Assert
        verify(mockInputStream, times(1)).close();
    }

    @Test
    public void skip_shouldSkipBytesInInputStream() throws IOException {
        // Arrange
        long value = 10L;

        // Act
        long result = zipArchiveInputStream.skip(value);

        // Assert
        Assert.assertEquals(value, result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void skip_shouldThrowIllegalArgumentExceptionIfValueIsNegative() throws IOException {
        // Arrange
        long value = -10L;

        // Act
        zipArchiveInputStream.skip(value);
    }

    @Test(expected = IOException.class)
    public void skip_shouldThrowIOExceptionIfStreamIsClosed() throws IOException {
        // Arrange
        zipArchiveInputStream.close();
        long value = 10L;

        // Act
        zipArchiveInputStream.skip(value);
    }

    @Test
    public void matches_shouldReturnTrueIfSignatureMatchesLFH() throws IOException {
        // Arrange
        byte[] signature = new byte[]{0x04, 0x03, 0x4B, 0x50};
        int length = ZipArchiveOutputStream.LFH_SIG.length;

        // Act
        boolean result = ZipArchiveInputStream.matches(signature, length);

        // Assert
        Assert.assertTrue(result);
    }

    @Test
    public void matches_shouldReturnTrueIfSignatureMatchesEOCD() throws IOException {
        // Arrange
        byte[] signature = new byte[]{0x50, 0x4B, 0x05, 0x06};
        int length = ZipArchiveOutputStream.LFH_SIG.length;

        // Act
        boolean result = ZipArchiveInputStream.matches(signature, length);

        // Assert
        Assert.assertTrue(result);
    }

    @Test
    public void matches_shouldReturnFalseIfSignatureDoesNotMatch() throws IOException {
        // Arrange
        byte[] signature = new byte[]{0x50, 0x4B, 0x01, 0x02};
        int length = ZipArchiveOutputStream.LFH_SIG.length;

        // Act
        boolean result = ZipArchiveInputStream.matches(signature, length);

        // Assert
        Assert.assertFalse(result);
    }

}
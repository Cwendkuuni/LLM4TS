
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.io.PushbackInputStream;
import java.util.zip.CRC32;
import java.util.zip.DataFormatException;
import java.util.zip.Inflater;
import java.util.zip.ZipException;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ZipArchiveInputStreamTest {

    private ZipArchiveInputStream zipArchiveInputStream;
    private InputStream mockedInputStream;

    @Before
    public void setUp() {
        mockedInputStream = mock(InputStream.class);
        zipArchiveInputStream = new ZipArchiveInputStream(mockedInputStream);
    }

    @Test
    public void testGetNextZipEntry() throws IOException {
        // Mock
        byte[] lfh = new byte[ZipArchiveInputStream.LFH_LEN];
        lfh[0] = 0x04;
        lfh[1] = 0x03;
        lfh[2] = 0x4b;
        lfh[3] = 0x50;
        when(mockedInputStream.read(lfh)).thenReturn(ZipArchiveInputStream.LFH_LEN);

        // Test
        ZipArchiveEntry zipArchiveEntry = zipArchiveInputStream.getNextZipEntry();

        // Assert
        Assert.assertNotNull(zipArchiveEntry);
        Assert.assertEquals(0x04, zipArchiveEntry.getPlatform());
        Assert.assertEquals(0, zipArchiveEntry.getMethod());
    }

    @Test
    public void testGetNextZipEntry_EndOfFile() throws IOException {
        // Mock
        byte[] cfh = new byte[ZipArchiveInputStream.LFH_LEN];
        cfh[0] = 0x02;
        cfh[1] = 0x03;
        cfh[2] = 0x4b;
        cfh[3] = 0x50;
        when(mockedInputStream.read(cfh)).thenReturn(ZipArchiveInputStream.LFH_LEN);
        when(mockedInputStream.read(cfh)).thenReturn(-1);

        // Test
        ZipArchiveEntry zipArchiveEntry = zipArchiveInputStream.getNextZipEntry();

        // Assert
        Assert.assertNull(zipArchiveEntry);
    }

    @Test
    public void testRead() throws IOException {
        // Mock
        byte[] buffer = new byte[10];
        buffer[0] = 0x01;
        buffer[1] = 0x02;
        buffer[2] = 0x03;
        buffer[3] = 0x04;
        buffer[4] = 0x05;
        buffer[5] = 0x06;
        buffer[6] = 0x07;
        when(mockedInputStream.read(buffer, 0, buffer.length)).thenReturn(buffer.length);

        // Test
        int readLength = zipArchiveInputStream.read(buffer, 0, buffer.length);

        // Assert
        Assert.assertEquals(buffer.length, readLength);
        Assert.assertEquals(0x01, buffer[0]);
        Assert.assertEquals(0x02, buffer[1]);
        Assert.assertEquals(0x03, buffer[2]);
        Assert.assertEquals(0x04, buffer[3]);
        Assert.assertEquals(0x05, buffer[4]);
        Assert.assertEquals(0x06, buffer[5]);
        Assert.assertEquals(0x07, buffer[6]);
    }

    @Test(expected = IOException.class)
    public void testRead_ClosedStream() throws IOException {
        // Mock
        when(mockedInputStream.read()).thenThrow(new IOException("The stream is closed"));

        // Test
        zipArchiveInputStream.read();
    }

    @Test
    public void testClose() throws IOException {
        // Mock
        when(mockedInputStream.read()).thenReturn(-1);

        // Test
        zipArchiveInputStream.close();

        // Assert
        Assert.assertTrue(zipArchiveInputStream.isClosed());
    }

    @Test
    public void testSkip() throws IOException {
        // Mock
        when(mockedInputStream.skip(10)).thenReturn(10L);

        // Test
        long skipped = zipArchiveInputStream.skip(10);

        // Assert
        Assert.assertEquals(10L, skipped);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSkip_NegativeValue() throws IOException {
        // Test
        zipArchiveInputStream.skip(-10);
    }

    @Test
    public void testMatches() {
        // Test
        boolean matches = ZipArchiveInputStream.matches(new byte[]{0x04, 0x03, 0x4b, 0x50}, ZipArchiveInputStream.LFH_LEN);

        // Assert
        Assert.assertTrue(matches);
    }

    @Test
    public void testMatches_InvalidSignature() {
        // Test
        boolean matches = ZipArchiveInputStream.matches(new byte[]{0x01, 0x02, 0x03, 0x04}, ZipArchiveInputStream.LFH_LEN);

        // Assert
        Assert.assertFalse(matches);
    }
}

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;

import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipArchiveInputStream;
import org.junit.Before;
import org.junit.Test;

public class ZipArchiveInputStreamTest {

    private ZipArchiveInputStream zipInput;
    private ByteArrayInputStream byteInput;

    @Before
    public void setUp() {
        byteInput = new ByteArrayInputStream(new byte[1024]);
        zipInput = new ZipArchiveInputStream(byteInput);
    }

    @Test
    public void testGetNextZipEntry_NormalFile() throws IOException {
        ZipArchiveEntry zipEntry = zipInput.getNextZipEntry();

        assertNull(zipEntry);

        byte[] sig = new byte[]{0x50, 0x4B, 0x03, 0x04};
        zipEntry = createZipArchiveEntry(SIG, true);

        assertEquals("Decoding failed on normal file entry", zipEntry, zipInput.getNextZipEntry());
    }

    @Test
    public void testGetNextZipEntry_EmptyZip() throws IOException {
        ZipArchiveEntry zipEntry = zipInput.getNextZipEntry();

        assertNull(zipEntry);

        byte[] sig = new byte[]{0x50, 0x4B, 0x05, 0x06};
        zipEntry = createZipArchiveEntry(SIG, true);

        assertNull("Empty zip entry is not null", zipInput.getNextZipEntry());
        assertNotNull("Empty zip entry is null when it shouldn't be", zipInput.getNextZipEntry());
    }

    @Test
    public void testGetNextZipEntry_IOExceptionThrown() throws IOException {
        byteInput.close();

        try {
            zipInput.getNextZipEntry();
            fail("An IOException should have been thrown");
        } catch (IOException e) {
            // expected exception
        }
    }

    @Test(expected = ZipException.class)
    public void testGetNextZipEntry_DataFormatExceptionThrown() throws IOException {
        InputStream mockInputStream = mock(InputStream.class);
        when(mockInputStream.read(any(byte[].class))).thenReturn(1);
        when(mockInputStream.read()).thenReturn(1);
        when(mockInputStream.read(any(byte[].class), anyInt(), anyInt())).thenReturn(1);

        zipInput = new ZipArchiveInputStream(mockInputStream);
        zipInput.getNextZipEntry();
    }

    @Test
    public void testRead_NormalFile() throws IOException {
        byte[] buffer = new byte[10];

        assertEquals("Read method behaves incorrectly on normal file", -1, zipInput.read(buffer, 0, 10));
    }

    @Test
    public void testRead_EmptyZip() throws IOException {
        byte[] buffer = new byte[10];

        assertEquals("Read method behaves incorrectly on empty zip", -1, zipInput.read(buffer, 0, 10));
    }

    @Test(expected = IOException.class)
    public void testRead_StreamClosed() throws IOException {
        zipInput.close();
        byte[] buffer = new byte[10];
        zipInput.read(buffer, 0, 10);
    }

    @Test(expected = ArrayIndexOutOfBoundsException.class)
    public void testRead_InvalidBufferIndex() throws IOException {
        byte[] buffer = new byte[10];
        zipInput.read(buffer, -1, 10);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSkip_NegativeValue() throws IOException {
        zipInput.skip(-1);
    }

    private ZipArchiveEntry createZipArchiveEntry(byte[] signature, boolean isNormalFile) {
        ZipArchiveEntry entry = new ZipArchiveEntry();
        entry.setCrc(12345);
        entry.setCompressedSize(100);
        entry.setMethod(1);
        entry.setName("Test");
        entry.setExtra(signature);
        entry.setPlatform(1);
        entry.setSize(200);
        entry.setTime(System.currentTimeMillis());
        if (isNormalFile) {
            entry.setGeneralPurposeFlag(1 << 0 | 1 << 3 | 0 << 11);
        } else {
            entry.setGeneralPurposeFlag(0);
        }
        return entry;
    }
}
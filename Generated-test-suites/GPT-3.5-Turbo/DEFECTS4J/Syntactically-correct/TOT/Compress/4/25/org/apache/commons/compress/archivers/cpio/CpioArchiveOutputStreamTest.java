
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class CpioArchiveOutputStreamTest {

    private CpioArchiveOutputStream cpioArchiveOutputStream;
    private ByteArrayOutputStream byteArrayOutputStream;

    @Before
    public void setup() {
        byteArrayOutputStream = new ByteArrayOutputStream();
        cpioArchiveOutputStream = new CpioArchiveOutputStream(byteArrayOutputStream);
    }

    @Test
    public void putArchiveEntry_shouldCreateNewEntry() throws IOException {
        CpioArchiveEntry entry = createTestEntry();
        cpioArchiveOutputStream.putArchiveEntry(entry);
        cpioArchiveOutputStream.closeArchiveEntry();
        cpioArchiveOutputStream.finish();

        assertEquals(byteArrayOutputStream.size(), entry.getSize() + entry.getHeaderPadCount() + entry.getDataPadCount());
    }

    @Test(expected = IOException.class)
    public void putArchiveEntry_withInvalidSize_shouldThrowIOException() throws IOException {
        CpioArchiveEntry entry = createTestEntry();
        entry.setSize(10);
        cpioArchiveOutputStream.putArchiveEntry(entry);
    }

    @Test(expected = IOException.class)
    public void putArchiveEntry_withDuplicateEntry_shouldThrowIOException() throws IOException {
        CpioArchiveEntry entry1 = createTestEntry();
        CpioArchiveEntry entry2 = createTestEntry();

        cpioArchiveOutputStream.putArchiveEntry(entry1);
        cpioArchiveOutputStream.putArchiveEntry(entry2);
    }

    @Test(expected = IOException.class)
    public void closeArchiveEntry_withInvalidSize_shouldThrowIOException() throws IOException {
        CpioArchiveEntry entry = createTestEntry();
        entry.setSize(10);
        cpioArchiveOutputStream.putArchiveEntry(entry);
        cpioArchiveOutputStream.closeArchiveEntry();
    }

    @Test(expected = IOException.class)
    public void finish_withUnclosedEntries_shouldThrowIOException() throws IOException {
        CpioArchiveEntry entry = createTestEntry();
        cpioArchiveOutputStream.putArchiveEntry(entry);
        cpioArchiveOutputStream.finish();
    }

    @Test
    public void write_shouldWriteBytesToOutputStream() throws IOException {
        CpioArchiveEntry entry = createTestEntry();
        cpioArchiveOutputStream.putArchiveEntry(entry);

        byte[] testData = "Hello, world!".getBytes();
        cpioArchiveOutputStream.write(testData);
        cpioArchiveOutputStream.closeArchiveEntry();

        cpioArchiveOutputStream.finish();
        
        byte[] archiveBytes = byteArrayOutputStream.toByteArray();
        int expectedLength = entry.getDataPadCount() + entry.getHeaderPadCount() + entry.getSize() + testData.length;
        assertEquals(expectedLength, archiveBytes.length);
        byte[] actualData = new byte[testData.length];
        System.arraycopy(archiveBytes, entry.getDataPadCount() + entry.getHeaderPadCount(), actualData, 0, testData.length);
        assertArrayEquals(testData, actualData);
    }

    @Test(expected = IOException.class)
    public void write_withExceedingEntrySize_shouldThrowIOException() throws IOException {
        CpioArchiveEntry entry = createTestEntry();
        entry.setSize(5);
        cpioArchiveOutputStream.putArchiveEntry(entry);

        byte[] testData = "Hello, world!".getBytes();
        cpioArchiveOutputStream.write(testData);

        cpioArchiveOutputStream.closeArchiveEntry();
    }

    @Test
    public void finish_shouldWriteTrailerEntry() throws IOException {
        cpioArchiveOutputStream.finish();

        byte[] archiveBytes = byteArrayOutputStream.toByteArray();
        int trailerEntryStartIndex = archiveBytes.length - CpioConstants.CPIO_TRAILER.length() - 1;
        assertTrue(trailerEntryStartIndex >= 0);

        String trailerEntryString = new String(archiveBytes, trailerEntryStartIndex, CpioConstants.CPIO_TRAILER.length());
        assertEquals(CpioConstants.CPIO_TRAILER, trailerEntryString);
    }

    private CpioArchiveEntry createTestEntry() {
        CpioArchiveEntry entry = new CpioArchiveEntry();
        entry.setName("testfile");
        entry.setFileSize(10);
        entry.setMode(CpioConstants.C_ISREG); // regular file
        entry.setNumberOfLinks(1);
        return entry;
    }

    private void assertArrayEquals(byte[] expected, byte[] actual) {
        assertEquals(expected.length, actual.length);
        for (int i = 0; i < expected.length; i++) {
            assertEquals(expected[i], actual[i]);
        }
    }
}


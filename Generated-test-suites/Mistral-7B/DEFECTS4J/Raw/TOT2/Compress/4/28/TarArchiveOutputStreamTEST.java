import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveOutputStream;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class TarArchiveOutputStreamTest {

    private ByteArrayOutputStream outputStream;
    private TarArchiveOutputStream tarArchiveOutputStream;

    @Before
    public void setUp() {
        outputStream = new ByteArrayOutputStream();
        tarArchiveOutputStream = new TarArchiveOutputStream(outputStream);
    }

    @After
    public void tearDown() throws IOException {
        tarArchiveOutputStream.close();
    }

    @Test
    public void testConstructor() {
        TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(new ByteArrayOutputStream());
        assertNotNull(tarArchiveOutputStream);
    }

    @Test
    public void testSetLongFileMode() {
        TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(new ByteArrayOutputStream());
        tarArchiveOutputStream.setLongFileMode(TarArchiveOutputStream.LONGFILE_TRUNCATE);
        assertEquals(TarArchiveOutputStream.LONGFILE_TRUNCATE, tarArchiveOutputStream.getLongFileMode());
    }

    @Test
    public void testWriteEmptyArchive() throws IOException {
        tarArchiveOutputStream.finish();
        byte[] expectedOutput = new byte[0];
        byte[] actualOutput = outputStream.toByteArray();
        assertArrayEquals(expectedOutput, actualOutput);
    }

    @Test
    public void testWriteRegularFileEntry() throws IOException {
        ByteArrayInputStream inputStream = new ByteArrayInputStream("Test data".getBytes());
        TarArchiveEntry archiveEntry = new TarArchiveEntry("testFile.txt");
        archiveEntry.setSize(7);
        tarArchiveOutputStream.putArchiveEntry(archiveEntry);
        byte[] data = "Test data".getBytes();
        tarArchiveOutputStream.write(data, 0, data.length);
        tarArchiveOutputStream.closeArchiveEntry();
        tarArchiveOutputStream.finish();

        byte[] expectedOutput = new byte[15 + data.length];
        System.arraycopy(archiveEntry.getHeaderRecord(), 0, expectedOutput, 0, 15);
        System.arraycopy(data, 0, expectedOutput, 15, data.length);
        byte[] actualOutput = outputStream.toByteArray();
        assertArrayEquals(expectedOutput, actualOutput);
    }

    @Test
    public void testWriteDirectoryEntry() throws IOException {
        TarArchiveEntry archiveEntry = new TarArchiveEntry("testDir/");
        archiveEntry.setSize(0);
        archiveEntry.setDirectory(true);
        tarArchiveOutputStream.putArchiveEntry(archiveEntry);
        tarArchiveOutputStream.closeArchiveEntry();
        tarArchiveOutputStream.finish();

        byte[] expectedOutput = new byte[15];
        System.arraycopy(archiveEntry.getHeaderRecord(), 0, expectedOutput, 0, 15);
        byte[] actualOutput = outputStream.toByteArray();
        assertArrayEquals(expectedOutput, actualOutput);
    }

    @Test(expected = IOException.class)
    public void testLongFileNameLongFileModeError() throws IOException {
        TarArchiveEntry archiveEntry = new TarArchiveEntry("testFile/very-long-name.txt");
        archiveEntry.setSize(7);
        tarArchiveOutputStream.setLongFileMode(TarArchiveOutputStream.LONGFILE_ERROR);
        tarArchiveOutputStream.putArchiveEntry(archiveEntry);
        byte[] data = "Test data".getBytes();
        tarArchiveOutputStream.write(data, 0, data.length);
    }

    @Test(expected = IOException.class)
    public void testLongFileNameLongFileModeTruncate() throws IOException {
        TarArchiveEntry archiveEntry = new TarArchiveEntry("testFile/very-long-name.txt");
        archiveEntry.setSize(7);
        tarArchiveOutputStream.setLongFileMode(TarArchiveOutputStream.LONGFILE_TRUNCATE);
        tarArchiveOutputStream.putArchiveEntry(archiveEntry);
        byte[] data = "Test data".getBytes();
        tarArchiveOutputStream.write(data, 0, data.length);
    }

    @Test
    public void testLongFileNameLongFileModeGnu() throws IOException {
        TarArchiveEntry archiveEntry = new TarArchiveEntry("testFile/very-long-name.txt");
        archiveEntry.setSize(7);
        TarArchiveEntry longLinkEntry = new TarArchiveEntry(TarConstants.GNU_LONGLINK, TarConstants.LF_GNUTYPE_LONGNAME);
        longLinkEntry.setSize(archiveEntry.getName().length() + 1);
        tarArchiveOutputStream.setLongFileMode(TarArchiveOutputStream.LONGFILE_GNU);
        tarArchiveOutputStream.putArchiveEntry(longLinkEntry);
        tarArchiveOutputStream.write(archiveEntry.getName().getBytes());
        tarArchiveOutputStream.write(0);
        tarArchiveOutputStream.closeArchiveEntry();
        tarArchiveOutputStream.putArchiveEntry(archiveEntry);
        byte[] data = "Test data".getBytes();
        tarArchiveOutputStream.write(data, 0, data.length);
        tarArchiveOutputStream.closeArchiveEntry();
        tarArchiveOutputStream.finish();

        byte[] expectedOutput = new byte[17 + data.length + archiveEntry.getName().length()];
        System.arraycopy(longLinkEntry.getHeaderRecord(), 0, expectedOutput, 0, 17);
        System.arraycopy(archiveEntry.getName().getBytes(), 0, expectedOutput, 17, archiveEntry.getName().length());
        System.arraycopy(data, 0, expectedOutput, 17 + archiveEntry.getName().length(), data.length);
        byte[] actualOutput = outputStream.toByteArray();
        assertArrayEquals(expectedOutput, actualOutput);
    }

    @Test(expected = IOException.class)
    public void testPutArchiveEntryWithoutCloseArchiveEntry() throws IOException {
        TarArchiveEntry archiveEntry = new TarArchiveEntry("testFile.txt");
        archiveEntry.setSize(7);
        tarArchiveOutputStream.putArchiveEntry(archiveEntry);
    }

    @Test(expected = IOException.class)
    public void testWriteAfterCloseArchiveEntry() throws IOException {
        TarArchiveEntry archiveEntry = new TarArchiveEntry("testFile.txt");
        archiveEntry.setSize(7);
        tarArchiveOutputStream.putArchiveEntry(archiveEntry);
        tarArchiveOutputStream.closeArchiveEntry();
        byte[] data = "Test data".getBytes();
        tarArchiveOutputStream.write(data, 0, data.length);
    }

    @Test(expected = IOException.class)
    public void testWriteAfterFinish() throws IOException {
        TarArchiveEntry archiveEntry = new TarArchiveEntry("testFile.txt");
        archiveEntry.setSize(7);
        tarArchiveOutputStream.putArchiveEntry(archiveEntry);
        tarArchiveOutputStream.finish();
        byte[] data = "Test data".getBytes();
        tarArchiveOutputStream.write(data, 0, data.length);
    }

    @Test(expected = IOException.class)
    public void testWriteBeyondSize() throws IOException {
        TarArchiveEntry archiveEntry = new TarArchiveEntry("testFile.txt");
        archiveEntry.setSize(7);
        tarArchiveOutputStream.putArchiveEntry(archiveEntry);
        byte[] data = "Test data".getBytes();
        tarArchiveOutputStream.write(data, 0, data.length + 1);
    }

    @Test
    public void testWriteMultipleFiles() throws IOException {
        TarArchiveEntry archiveEntry1 = new TarArchiveEntry("testFile1.txt");
        archiveEntry1.setSize(7);
        TarArchiveEntry archiveEntry2 = new TarArchiveEntry("testFile2.txt");
        archiveEntry2.setSize(11);

        tarArchiveOutputStream.putArchiveEntry(archiveEntry1);
        byte[] data1 = "Test data 1".getBytes();
        tarArchiveOutputStream.write(data1, 0, data1.length);
        tarArchiveOutputStream.closeArchiveEntry();

        tarArchiveOutputStream.putArchiveEntry(archiveEntry2);
        byte[] data2 = "Test data 2".getBytes();
        tarArchiveOutputStream.write(data2, 0, data2.length);
        tarArchiveOutputStream.closeArchiveEntry();

        tarArchiveOutputStream.finish();

        byte[] expectedOutput = new byte[18 + data1.length + data2.length];
        System.arraycopy(archiveEntry1.getHeaderRecord(), 0, expectedOutput, 0, 15);
        System.arraycopy(data1, 0, expectedOutput, 15, data1.length);
        int index = 15 + data1.length;
        System.arraycopy(archiveEntry2.getHeaderRecord(), 0, expectedOutput, index, 15);
        index += 15;
        System.arraycopy(data2, 0, expectedOutput, index, data2.length);
        byte[] actualOutput = outputStream.toByteArray();
        assertArrayEquals(expectedOutput, actualOutput);
    }
}
import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveOutputStream;
import org.apache.commons.compress.utils.ByteStreams;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

/**
 * Test class for {@link TarArchiveOutputStream}.
 */
public class TarArchiveOutputStreamTest {

    private ByteArrayOutputStream outputStream;
    private TarArchiveOutputStream tarOutputStream;

    @Before
    public void setUp() throws IOException {
        outputStream = new ByteArrayOutputStream();
        tarOutputStream = new TarArchiveOutputStream(outputStream);
    }

    @After
    public void tearDown() throws IOException {
        tarOutputStream.close();
    }

    @Test
    public void testEmptyArchive() throws IOException {
        tarOutputStream.finish();

        byte[] expected = new byte[2 * TarConstants.BLOCK_SIZE];
        assertArrayEquals(expected, outputStream.toByteArray());
    }

    @Test
    public void testPutArchiveEntry() throws IOException {
        String entryName = "testEntry";
        File fileToAdd = new File("testFile.txt");

        TarArchiveEntry entry = new TarArchiveEntry(fileToAdd, entryName);
        tarOutputStream.putArchiveEntry(entry);

        byte[] expected = new byte[6 * TarConstants.BLOCK_SIZE];
        for (int i = 0; i < expected.length; i++) {
            expected[i] = (byte) i;
        }
        byte[] header = entry.getHeader(tarOutputStream.getRecordSize());
        System.arraycopy(header, 0, expected, 0, header.length);

        File testFile = new File("testFile.txt");
        byte[] fileContent = "Hello World!".getBytes(StandardCharsets.UTF_8);
        try (FileOutputStream fileOutputStream = new FileOutputStream(testFile)) {
            fileOutputStream.write(fileContent, 0, fileContent.length);
        }

        tarOutputStream.write(fileContent, 0, fileContent.length);
        tarOutputStream.closeArchiveEntry();
        tarOutputStream.finish();

        byte[] actual = outputStream.toByteArray();
        assertEquals(expected.length, actual.length);
        assertArrayEquals(expected, actual);

        assertNotNull(testFile);

        File archiveFile = new File("test.tar");
        try (FileOutputStream archiveOutputStream = new FileOutputStream(archiveFile)) {
            tarOutputStream = new TarArchiveOutputStream(archiveOutputStream);
            tarOutputStream.setLongFileMode(TarArchiveOutputStream.LONGFILE_GNU);
            entry = new TarArchiveEntry(fileToAdd, "testEntryLongName");
            tarOutputStream.putArchiveEntry(entry);
            testFile.delete();
            tarOutputStream.write(("testLongName" + System.lineSeparator()).getBytes(StandardCharsets.UTF_8));
            tarOutputStream.write(0);
            tarOutputStream.closeArchiveEntry();
            tarOutputStream.finish();
        }

        actual = ByteStreams.toByteArray(new FileInputStream(archiveFile));
        assertEquals(expected.length + 1, actual.length);
    }

    @Test(expected = IOException.class)
    public void testLongFileNameWithErrorMode() throws IOException {
        String entryName = "testEntry" + new String(new char[TarConstants.NAMELEN + 1])[0];
        TarArchiveEntry entry = new TarArchiveEntry(null, entryName);
        tarOutputStream.putArchiveEntry(entry);
        tarOutputStream.closeArchiveEntry();
        tarOutputStream.finish();
    }

    @Test(expected = IOException.class)
    public void testLongFileNameWithTruncateMode() throws IOException {
        String entryName = "testEntry" + new String(new char[TarConstants.NAMELEN + 1])[0];
        TarArchiveEntry entry = new TarArchiveEntry(null, entryName);
        tarOutputStream.putArchiveEntry(entry);
        tarOutputStream.closeArchiveEntry();
        tarOutputStream.finish();
    }

    @Test
    public void testCloseWithoutFinish() throws IOException {
        String entryName = "testEntry";
        TarArchiveEntry entry = new TarArchiveEntry(null, entryName);
        tarOutputStream.putArchiveEntry(entry);

        assertNull(tarOutputStream.getCurrentEntry());

        tarOutputStream.close();

        assertEquals(TarArchiveOutputStream.STATUS_OK, tarOutputStream.getStatus());
    }

    @Test
    public void testCloseWithFinish() throws IOException {
        String entryName = "testEntry";
        TarArchiveEntry entry = new TarArchiveEntry(null, entryName);
        tarOutputStream.putArchiveEntry(entry);

        assertNull(tarOutputStream.getCurrentEntry());

        tarOutputStream.finish();

        assertEquals(TarArchiveOutputStream.STATUS_ARCHIVE_CLOSED, tarOutputStream.getStatus());
    }

    @Test(expected = IOException.class)
    public void testCloseTwice() throws IOException {
        String entryName = "testEntry";
        TarArchiveEntry entry = new TarArchiveEntry(null, entryName);
        tarOutputStream.putArchiveEntry(entry);

        tarOutputStream.finish();

        tarOutputStream.close();

        tarOutputStream.close();
    }

    @Test
    public void testWritingDataAfterEntryClosed() throws IOException {
        String entryName = "testEntry";
        TarArchiveEntry entry = new TarArchiveEntry(null, entryName);
        tarOutputStream.putArchiveEntry(entry);

        byte[] data = new byte[10];
        tarOutputStream.write(data, 0, data.length);

        tarOutputStream.closeArchiveEntry();

        byte[] expected = new byte[6 * TarConstants.BLOCK_SIZE + data.length];
        for (int i = 0; i < expected.length; i++) {
            expected[i] = (byte) i;
        }

        tarOutputStream.finish();

        byte[] actual = outputStream.toByteArray();
        assertEquals(expected.length, actual.length);
        assertArrayEquals(expected, actual);
    }

    private void assertArrayEquals(byte[] expected, byte[] actual) {
        for (int i = 0; i < expected.length; i++) {
            assertEquals(expected[i], actual[i]);
        }
    }
}
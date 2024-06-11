import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import org.apache.commons.compress.archivers.ArchiveEntry;
import org.junit.Before;
import org.junit.Test;

public class TarArchiveOutputStreamTest {

    private OutputStream os;
    private TarArchiveOutputStream tarOutputStream;

    @Before
    public void setup() {
        os = new ByteArrayOutputStream();
        tarOutputStream = new TarArchiveOutputStream(os);
    }

    @Test
    public void testSetLongFileMode_LongFileError() {
        tarOutputStream.setLongFileMode(TarArchiveOutputStream.LONGFILE_ERROR);
        assertEquals(TarArchiveOutputStream.LONGFILE_ERROR, tarOutputStream.getLongFileMode());
    }

    @Test
    public void testSetLongFileMode_LongFileTruncate() {
        tarOutputStream.setLongFileMode(TarArchiveOutputStream.LONGFILE_TRUNCATE);
        assertEquals(TarArchiveOutputStream.LONGFILE_TRUNCATE, tarOutputStream.getLongFileMode());
    }

    @Test
    public void testSetLongFileMode_LongFileGNU() {
        tarOutputStream.setLongFileMode(TarArchiveOutputStream.LONGFILE_GNU);
        assertEquals(TarArchiveOutputStream.LONGFILE_GNU, tarOutputStream.getLongFileMode());
    }

    @Test
    public void testPutArchiveEntry() throws IOException {
        ArchiveEntry archiveEntry = mock(ArchiveEntry.class);
        when(archiveEntry.getName()).thenReturn("test-entry");
        tarOutputStream.putArchiveEntry(archiveEntry);
        tarOutputStream.closeArchiveEntry();
        tarOutputStream.close();

        String archive = os.toString();
        assertTrue(archive.contains("test-entry"));
    }

    @Test(expected = RuntimeException.class)
    public void testPutArchiveEntry_LongFileNameError() throws IOException {
        ArchiveEntry archiveEntry = mock(ArchiveEntry.class);
        when(archiveEntry.getName()).thenReturn("very-long-file-name-that-exceeds-tar-constants-name-length");
        tarOutputStream.putArchiveEntry(archiveEntry);
    }

    @Test
    public void testCloseArchiveEntry() throws IOException {
        ArchiveEntry archiveEntry = mock(ArchiveEntry.class);
        when(archiveEntry.getName()).thenReturn("test-entry");
        tarOutputStream.putArchiveEntry(archiveEntry);
        tarOutputStream.write(new byte[]{1, 2, 3});
        tarOutputStream.closeArchiveEntry();
        tarOutputStream.close();

        String archive = os.toString();
        assertTrue(archive.contains(new String(new byte[]{1, 2, 3})));
    }

    @Test
    public void testCloseArchiveEntry_NotEnoughBytesWritten() throws IOException {
        ArchiveEntry archiveEntry = mock(ArchiveEntry.class);
        when(archiveEntry.getName()).thenReturn("test-entry");
        tarOutputStream.putArchiveEntry(archiveEntry);
        tarOutputStream.write(new byte[]{1, 2});
        try {
            tarOutputStream.closeArchiveEntry();
        } catch (IOException e) {
            assertEquals("entry 'test-entry' closed at '2' before the '3' bytes specified in the header were written", e.getMessage());
        }
    }

    @Test
    public void testWrite() throws IOException {
        ArchiveEntry archiveEntry = mock(ArchiveEntry.class);
        when(archiveEntry.getName()).thenReturn("test-entry");
        tarOutputStream.putArchiveEntry(archiveEntry);
        tarOutputStream.write(new byte[]{1, 2, 3});
        tarOutputStream.closeArchiveEntry();
        tarOutputStream.close();

        String archive = os.toString();
        assertTrue(archive.contains(new String(new byte[]{1, 2, 3})));
    }

    @Test
    public void testFinish() throws IOException {
        tarOutputStream.finish();
        tarOutputStream.close();

        String archive = os.toString();
        assertEquals(1024, archive.length());
    }

    @Test
    public void testClose() throws IOException {
        tarOutputStream.close();

        assertTrue(tarOutputStream.isClosed());
    }

    @Test
    public void testGetRecordSize() {
        assertEquals(10240, tarOutputStream.getRecordSize());
    }

    @Test
    public void testCreateArchiveEntry() throws IOException {
        File inputFile = mock(File.class);
        when(inputFile.getName()).thenReturn("test-file");
        ArchiveEntry archiveEntry = tarOutputStream.createArchiveEntry(inputFile, null);

        assertTrue(archiveEntry instanceof TarArchiveEntry);
        assertEquals("test-file", archiveEntry.getName());
    }

}
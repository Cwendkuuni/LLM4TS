import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveOutputStream;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class TarArchiveOutputStreamTest {
    private TarArchiveOutputStream tarArchiveOutputStream;
    private OutputStream mockOutputStream;

    @Before
    public void setup() {
        mockOutputStream = new ByteArrayOutputStream();
        tarArchiveOutputStream = new TarArchiveOutputStream(mockOutputStream);
    }

    @After
    public void teardown() throws IOException {
        tarArchiveOutputStream.close();
    }

    @Test
    public void testPutArchiveEntry() throws IOException {
        ArchiveEntry archiveEntry = new TarArchiveEntry("testFile");

        tarArchiveOutputStream.putArchiveEntry(archiveEntry);

        assertEquals(0, tarArchiveOutputStream.getCurrBytes());
        assertEquals("testFile", tarArchiveOutputStream.getCurrName());
        assertNull(tarArchiveOutputStream.getAssemLen());
    }

    @Test
    public void testCloseArchiveEntry() throws IOException {
        ArchiveEntry archiveEntry = new TarArchiveEntry("testFile");

        tarArchiveOutputStream.putArchiveEntry(archiveEntry);
        tarArchiveOutputStream.closeArchiveEntry();

        assertEquals(0, tarArchiveOutputStream.getAssemLen());
        assertEquals(0, tarArchiveOutputStream.getCurrBytes());
    }

    @Test
    public void testWrite() throws IOException {
        ArchiveEntry archiveEntry = new TarArchiveEntry("testFile");

        tarArchiveOutputStream.putArchiveEntry(archiveEntry);
        tarArchiveOutputStream.write("Test data".getBytes(), 0, 9);
        tarArchiveOutputStream.closeArchiveEntry();

        assertEquals(9, tarArchiveOutputStream.getCurrBytes());
    }

    @Test(expected = IOException.class)
    public void testWriteExceedingSize() throws IOException {
        ArchiveEntry archiveEntry = new TarArchiveEntry("testFile");

        tarArchiveOutputStream.putArchiveEntry(archiveEntry);
        tarArchiveOutputStream.write("Test data".getBytes(), 0, 50);
        tarArchiveOutputStream.closeArchiveEntry();
    }

    @Test
    public void testFinish() throws IOException {
        ArchiveEntry archiveEntry = new TarArchiveEntry("testFile");

        tarArchiveOutputStream.putArchiveEntry(archiveEntry);
        tarArchiveOutputStream.finish();
        tarArchiveOutputStream.closeArchiveEntry();

        assertEquals(0, tarArchiveOutputStream.getAssemLen());
        assertEquals(0, tarArchiveOutputStream.getCurrBytes());
    }

    @Test
    public void testGetRecordSize() {
        assertEquals(512, tarArchiveOutputStream.getRecordSize());
    }

    @Test
    public void testSetLongFileMode() {
        tarArchiveOutputStream.setLongFileMode(TarArchiveOutputStream.LONGFILE_GNU);

        assertEquals(TarArchiveOutputStream.LONGFILE_GNU, tarArchiveOutputStream.getLongFileMode());
    }
}
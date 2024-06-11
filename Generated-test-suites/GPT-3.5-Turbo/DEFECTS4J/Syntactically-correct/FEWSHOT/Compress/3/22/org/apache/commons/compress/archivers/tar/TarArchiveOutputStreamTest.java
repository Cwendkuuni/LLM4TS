import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.ArchiveOutputStream;
import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;

public class TarArchiveOutputStreamTest {
    private TarArchiveOutputStream tarOutputStream;

    @Before
    public void setUp() throws IOException {
        OutputStream os = new ByteArrayOutputStream();
        tarOutputStream = new TarArchiveOutputStream(os);
    }

    @After
    public void tearDown() throws IOException {
        tarOutputStream.close();
    }

    @Test
    public void testConstructor() {
        assertEquals(TarBuffer.DEFAULT_BLKSIZE, tarOutputStream.getRecordSize());
    }

    @Test
    public void testPutArchiveEntry() throws IOException {
        ArchiveEntry entry = new TarArchiveEntry("test.txt");
        tarOutputStream.putArchiveEntry(entry);
        tarOutputStream.closeArchiveEntry();
        assertTrue(tarOutputStream.isEntryOpen());
    }

    @Test
    public void testCloseArchiveEntry() throws IOException {
        ArchiveEntry entry = new TarArchiveEntry("test.txt");
        tarOutputStream.putArchiveEntry(entry);
        tarOutputStream.closeArchiveEntry();
        assertFalse(tarOutputStream.isEntryOpen());
    }

    @Test(expected = IOException.class)
    public void testCloseArchiveEntryInvalidEntrySize() throws IOException {
        ArchiveEntry entry = new TarArchiveEntry("test.txt");
        tarOutputStream.putArchiveEntry(entry);
        tarOutputStream.write(new byte[100]);
        tarOutputStream.closeArchiveEntry();
    }

    @Test
    public void testWrite() throws IOException {
        ArchiveEntry entry = new TarArchiveEntry("test.txt");
        tarOutputStream.putArchiveEntry(entry);
        tarOutputStream.write(new byte[100]);
        tarOutputStream.closeArchiveEntry();
        assertFalse(tarOutputStream.isEntryOpen());
    }

    @Test
    public void testWriteByteArrayOffsetLength() throws IOException {
        ArchiveEntry entry = new TarArchiveEntry("test.txt");
        tarOutputStream.putArchiveEntry(entry);
        tarOutputStream.write(new byte[100], 0, 50);
        tarOutputStream.closeArchiveEntry();
        assertFalse(tarOutputStream.isEntryOpen());
    }

    @Test(expected = IOException.class)
    public void testWriteByteArrayOffsetLengthInvalidEntrySize() throws IOException {
        ArchiveEntry entry = new TarArchiveEntry("test.txt");
        tarOutputStream.putArchiveEntry(entry);
        tarOutputStream.write(new byte[100], 0, 200);
        tarOutputStream.closeArchiveEntry();
    }

    @Test
    public void testFinish() throws IOException {
        tarOutputStream.finish();
        assertFalse(tarOutputStream.isEntryOpen());
    }

    @Test
    public void testClose() throws IOException {
        tarOutputStream.close();
        assertFalse(tarOutputStream.isEntryOpen());
    }

    @Test
    public void testSetLongFileMode() {
        tarOutputStream.setLongFileMode(TarArchiveOutputStream.LONGFILE_ERROR);
        assertEquals(TarArchiveOutputStream.LONGFILE_ERROR, tarOutputStream.getLongFileMode());
    }

    @Test
    public void testCreateArchiveEntry() throws IOException {
        File inputFile = new File("test.txt");
        String entryName = "test.txt";
        ArchiveEntry entry = tarOutputStream.createArchiveEntry(inputFile, entryName);
        assertEquals(entryName, entry.getName());
    }
}
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import static org.junit.Assert.*;

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
        outputStream.close();
    }

    @Test
    public void testSetLongFileMode() {
        // Test setting long file mode to LONGFILE_ERROR
        tarArchiveOutputStream.setLongFileMode(TarArchiveOutputStream.LONGFILE_ERROR);
        assertEquals(TarArchiveOutputStream.LONGFILE_ERROR, tarArchiveOutputStream.getLongFileMode());

        // Test setting long file mode to LONGFILE_TRUNCATE
        tarArchiveOutputStream.setLongFileMode(TarArchiveOutputStream.LONGFILE_TRUNCATE);
        assertEquals(TarArchiveOutputStream.LONGFILE_TRUNCATE, tarArchiveOutputStream.getLongFileMode());

        // Test setting long file mode to LONGFILE_GNU
        tarArchiveOutputStream.setLongFileMode(TarArchiveOutputStream.LONGFILE_GNU);
        assertEquals(TarArchiveOutputStream.LONGFILE_GNU, tarArchiveOutputStream.getLongFileMode());
    }

    @Test
    public void testPutArchiveEntry() throws IOException {
        // Test putting archive entry
        TarArchiveEntry entry = new TarArchiveEntry("testFile");
        tarArchiveOutputStream.putArchiveEntry(entry);
        assertTrue(tarArchiveOutputStream.haveUnclosedEntry());

        tarArchiveOutputStream.closeArchiveEntry();
        assertFalse(tarArchiveOutputStream.haveUnclosedEntry());
    }

    @Test(expected = IOException.class)
    public void testPutArchiveEntryFinished() throws IOException {
        // Test putting archive entry after finish
        tarArchiveOutputStream.finish();

        TarArchiveEntry entry = new TarArchiveEntry("testFile");
        tarArchiveOutputStream.putArchiveEntry(entry);
    }

    @Test(expected = IOException.class)
    public void testPutArchiveEntryWithoutClose() throws IOException {
        // Test putting archive entry without closing previous entry
        TarArchiveEntry entry1 = new TarArchiveEntry("testFile1");
        TarArchiveEntry entry2 = new TarArchiveEntry("testFile2");
        tarArchiveOutputStream.putArchiveEntry(entry1);
        tarArchiveOutputStream.putArchiveEntry(entry2);
    }

    @Test
    public void testCloseArchiveEntry() throws IOException {
        // Test closing archive entry
        TarArchiveEntry entry = new TarArchiveEntry("testFile");
        tarArchiveOutputStream.putArchiveEntry(entry);
        tarArchiveOutputStream.write(new byte[] {1, 2, 3});
        tarArchiveOutputStream.closeArchiveEntry();
        assertEquals(0, tarArchiveOutputStream.getCurrentEntry().getSize());
    }

    @Test(expected = IOException.class)
    public void testCloseArchiveEntryFinished() throws IOException {
        // Test closing archive entry after finish
        tarArchiveOutputStream.finish();

        tarArchiveOutputStream.closeArchiveEntry();
    }

    @Test(expected = IOException.class)
    public void testCloseArchiveEntryWithoutPut() throws IOException {
        // Test closing archive entry without putting entry
        tarArchiveOutputStream.closeArchiveEntry();
    }

    @Test
    public void testWrite() throws IOException {
        // Test writing bytes to the current archive entry
        TarArchiveEntry entry = new TarArchiveEntry("testFile");
        tarArchiveOutputStream.putArchiveEntry(entry);

        tarArchiveOutputStream.write(new byte[] {1, 2, 3});
        tarArchiveOutputStream.write(new byte[] {4, 5, 6}, 0, 2);

        tarArchiveOutputStream.closeArchiveEntry();
        tarArchiveOutputStream.finish();

        byte[] expected = {
                1, 2, 3, 4, 5, 6
        };
        assertArrayEquals(expected, outputStream.toByteArray());
    }

    @Test(expected = IOException.class)
    public void testWriteTooManyBytes() throws IOException {
        // Test writing too many bytes to the current archive entry
        TarArchiveEntry entry = new TarArchiveEntry("testFile");
        tarArchiveOutputStream.putArchiveEntry(entry);

        tarArchiveOutputStream.write(new byte[TarArchiveOutputStream.DEFAULT_RECORD_SIZE + 1]);

        tarArchiveOutputStream.closeArchiveEntry();
    }

    @Test
    public void testFinish() throws IOException {
        // Test finishing the TAR archive
        tarArchiveOutputStream.finish();

        byte[] expected = {
                0, 0, 0, 0
        };
        assertArrayEquals(expected, outputStream.toByteArray());
    }

    @Test
    public void testClose() throws IOException {
        // Test closing the TAR archive
        tarArchiveOutputStream.close();
        assertTrue(tarArchiveOutputStream.isClosed());
    }

    @Test(expected = IOException.class)
    public void testDoubleFinish() throws IOException {
        // Test calling finish twice
        tarArchiveOutputStream.finish();
        tarArchiveOutputStream.finish();
    }

    @Test(expected = IOException.class)
    public void testDoubleClose() throws IOException {
        // Test calling close twice
        tarArchiveOutputStream.close();
        tarArchiveOutputStream.close();
    }

    @Test
    public void testGetRecordSize() {
        // Test getting the record size of the TAR buffer
        assertEquals(TarBuffer.DEFAULT_RECORD_SIZE, tarArchiveOutputStream.getRecordSize());
    }

    @Test
    public void testCreateArchiveEntry() throws IOException {
        // Test creating an archive entry
        File inputFile = new File("test.txt");
        String entryName = "testFile";
        ArchiveEntry entry = tarArchiveOutputStream.createArchiveEntry(inputFile, entryName);

        assertNotNull(entry);
        assertEquals(TarArchiveEntry.class, entry.getClass());
        assertEquals(entryName, entry.getName());
    }

}
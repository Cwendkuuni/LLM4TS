
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.ArchiveOutputStream;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class TarArchiveOutputStreamTest {
    private static final String TEST_FILE_NAME = "test.txt";
    private static final String TEST_ENTRY_NAME = "test-entry.txt";
    private static final byte[] TEST_CONTENT = "This is a test".getBytes();
    private File testFile;
    private TarArchiveOutputStream tarOutputStream;

    @Before
    public void setup() throws IOException {
        testFile = File.createTempFile(TEST_FILE_NAME, null);
        tarOutputStream = new TarArchiveOutputStream(new ByteArrayOutputStream());
    }

    @After
    public void teardown() throws IOException {
        tarOutputStream.close();
        testFile.delete();
    }

    @Test
    public void testPutArchiveEntry() throws IOException {
        ArchiveEntry entry = tarOutputStream.createArchiveEntry(testFile, TEST_ENTRY_NAME);
        tarOutputStream.putArchiveEntry(entry);

        assertEquals(TEST_ENTRY_NAME, tarOutputStream.getCurrentEntry().getName());
        assertTrue(tarOutputStream.haveUnclosedEntry());
    }

    @Test
    public void testCloseArchiveEntry() throws IOException {
        ArchiveEntry entry = tarOutputStream.createArchiveEntry(testFile, TEST_ENTRY_NAME);
        tarOutputStream.putArchiveEntry(entry);
        tarOutputStream.closeArchiveEntry();

        assertNull(tarOutputStream.getCurrentEntry());
        assertFalse(tarOutputStream.haveUnclosedEntry());
    }

    @Test
    public void testWrite() throws IOException {
        ArchiveEntry entry = tarOutputStream.createArchiveEntry(testFile, TEST_ENTRY_NAME);
        tarOutputStream.putArchiveEntry(entry);
        tarOutputStream.write(TEST_CONTENT);

        assertEquals(TEST_CONTENT.length, tarOutputStream.getCurrentEntry().getSize());
    }

    @Test
    public void testFinish() throws IOException {
        ArchiveEntry entry = tarOutputStream.createArchiveEntry(testFile, TEST_ENTRY_NAME);
        tarOutputStream.putArchiveEntry(entry);
        tarOutputStream.finish();

        assertNotNull(tarOutputStream.getCurrentEntry());
        assertFalse(tarOutputStream.haveUnclosedEntry());
    }

    @Test(expected = IOException.class)
    public void testFinishWithUnclosedEntry() throws IOException {
        ArchiveEntry entry = tarOutputStream.createArchiveEntry(testFile, TEST_ENTRY_NAME);
        tarOutputStream.putArchiveEntry(entry);
        tarOutputStream.finish();
        tarOutputStream.finish();
    }

    @Test
    public void testClose() throws IOException {
        ArchiveEntry entry = tarOutputStream.createArchiveEntry(testFile, TEST_ENTRY_NAME);
        tarOutputStream.putArchiveEntry(entry);
        tarOutputStream.close();

        assertNull(tarOutputStream.getCurrentEntry());
        assertFalse(tarOutputStream.haveUnclosedEntry());
        assertTrue(tarOutputStream.isClosed());
    }

    @Test(expected = IOException.class)
    public void testDoubleClose() throws IOException {
        tarOutputStream.close();
        tarOutputStream.close();
    }

    @Test
    public void testGetRecordSize() {
        assertEquals(TarBuffer.DEFAULT_RCDSIZE, tarOutputStream.getRecordSize());
    }

    @Test
    public void testCreateArchiveEntry() throws IOException {
        ArchiveEntry entry = tarOutputStream.createArchiveEntry(testFile, TEST_ENTRY_NAME);

        assertNotNull(entry);
        assertEquals(TEST_ENTRY_NAME, entry.getName());
        assertEquals(testFile.length(), entry.getSize());
    }
}

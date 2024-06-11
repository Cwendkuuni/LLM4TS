import org.apache.commons.compress.archivers.ArArchiveEntry;
import org.apache.commons.compress.archivers.ArArchiveInputStream;
import org.apache.commons.compress.archivers.ArchiveInputStream;
import org.apache.commons.compress.archivers.ArchiveOutputStream;
import org.apache.commons.compress.archivers.test.TestArArchiveEntry;
import org.apache.commons.compress.archivers.test.TestArFile;
import org.apache.commons.compress.utils.IOUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

/**
 * Test cases for {@link ArArchiveInputStream}.
 */
public class ArArchiveInputStreamTest {

    private TestArFile testArFile;
    private ArArchiveInputStream arInputStream;

    @Before
    public void setUp() throws Exception {
        testArFile = new TestArFile();
        testArFile.createArchive("test.ar", new TestArArchiveEntry("test.txt", "Test".getBytes()));
        arInputStream = new ArArchiveInputStream(testArFile.getInputStream());
    }

    @After
    public void tearDown() throws Exception {
        IOUtils.closeQuietly(arInputStream);
        IOUtils.closeQuietly(testArFile.getInputStream());
        IOUtils.closeQuietly(testArFile.getFileOutput());
        testArFile.deleteFile();
    }

    /**
     * Test getNextEntry method.
     *
     * @throws IOException if an error occurs when reading the archive.
     */
    @Test
    public void testGetNextEntry() throws IOException {
        ArchiveEntry entry = arInputStream.getNextEntry();
        assertEquals("test.txt", entry.getName());
        assertEquals(TestArArchiveEntry.SIZE, entry.getSize());

        entry = arInputStream.getNextEntry();
        assertNull(entry);
    }

    /**
     * Test getNextEntry method when an error occurs while reading the archive.
     */
    @Test(expected = IOException.class)
    public void testGetNextEntryIOException() throws IOException, InterruptedException {
        InputStream inputStream = new TestIOExceptionInputStream();
        ArArchiveInputStream arInputStream = new ArArchiveInputStream(inputStream);

        while (arInputStream.getNextEntry() != null) {
            // continue to read until an exception is thrown
        }
    }

    /**
     * Test the input stream when the archive is empty.
     */
    @Test
    public void testEmptyArchive() throws IOException {
        TestArFile testArFile = new TestArFile();
        ArArchiveInputStream arInputStream = new ArArchiveInputStream(testArFile.getInputStream());

        assertEquals(0, arInputStream.read());
        assertNull(arInputStream.getNextEntry());
        arInputStream.close();
    }

    /**
     * Test the input stream when the archive contains no valid entries.
     */
    @Test
    public void testInvalidArchive() throws IOException {
        TestArFile testArFile = new TestArFile();
        testArFile.createArchive("test.ar", new TestInvalidEntryInputStream());
        ArArchiveInputStream arInputStream = new ArArchiveInputStream(testArFile.getInputStream());

        assertEquals(0, arInputStream.read());
        assertNull(arInputStream.getNextEntry());
        arInputStream.close();
    }

    /**
     * Test the input stream when the archive contains multiple valid entries.
     */
    @Test
    public void testMultipleValidEntries() throws IOException {
        TestArFile testArFile = new TestArFile();
        testArFile.createArchive("test.ar", new TestArArchiveEntry("test1.txt", "Test1".getBytes()),
                new TestArArchiveEntry("test2.txt", "Test2".getBytes()));
        ArArchiveInputStream arInputStream = new ArArchiveInputStream(testArFile.getInputStream());

        ArchiveEntry entry1 = arInputStream.getNextEntry();
        assertEquals("test1.txt", entry1.getName());
        assertEquals(TestArArchiveEntry.SIZE, entry1.getSize());

        ArchiveEntry entry2 = arInputStream.getNextEntry();
        assertEquals("test2.txt", entry2.getName());
        assertEquals(TestArArchiveEntry.SIZE, entry2.getSize());

        ArchiveEntry entry3 = arInputStream.getNextEntry();
        assertNull(entry3);

        arInputStream.close();
    }

    /**
     * Test the input stream when the archive contains a long entry name.
     */
    @Test
    public void testLongEntryName() throws IOException {
        String longEntryName = "Long_Entry_Name_That_Exceeds_the_Maximum_Allowed_Length_of_16_Characters_in_AR";
        TestArFile testArFile = new TestArFile();
        testArFile.createArchive("test.ar", new TestArArchiveEntry(longEntryName, "Test".getBytes()));
        ArArchiveInputStream arInputStream = new ArArchiveInputStream(testArFile.getInputStream());

        ArchiveEntry entry = arInputStream.getNextEntry();
        assertEquals(longEntryName, entry.getName());

        arInputStream.close();
    }
}

class TestIOExceptionInputStream extends InputStream {
    @Override
    public int read() throws IOException {
        throw new IOException("Test IOException");
    }
}

class TestInvalidEntryInputStream extends ArchiveInputStream {
    @Override
    public void closeEntry() throws IOException {
        throw new IOException("Test Invalid Entry Stream");
    }

    @Override
    public ArchiveEntry getNextEntry() throws IOException {
        throw new IOException("Test Invalid Entry Stream");
    }

    @Override
    public void setInputStream(InputStream inputStream) {
        // Not used
    }
}
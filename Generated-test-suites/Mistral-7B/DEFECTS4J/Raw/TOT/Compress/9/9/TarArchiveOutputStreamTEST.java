import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveOutputStream;
import org.apache.commons.compress.utils.IOUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

/**
 * Test class for TarArchiveOutputStream.
 */
public class TarArchiveOutputStreamTest {

    private File testFile;

    @Before
    public void setUp() throws Exception {
        testFile = File.createTempFile("test", ".tar");
    }

    @After
    public void tearDown() throws Exception {
        IOUtils.closeQuietly(testFile.getAbsoluteFile().toPath().toFile());
        testFile.delete();
    }

    /**
     * Test that an empty archive can be created.
     * @throws IOException on error
     */
    @Test
    public void testEmptyArchive() throws IOException {
        try (TarArchiveOutputStream tarOut = new TarArchiveOutputStream(new FileOutputStream(testFile))) {
            tarOut.finish();
        }

        assertEquals(2, (int) testFile.length());
    }

    /**
     * Test that a single file entry can be added to the archive.
     * @throws IOException on error
     */
    @Test
    public void testSingleFileEntry() throws IOException {
        try (FileOutputStream fos = new FileOutputStream(testFile);
             TarArchiveOutputStream tarOut = new TarArchiveOutputStream(fos)) {

            TarArchiveEntry entry = new TarArchiveEntry("test.txt", "Test file.");
            tarOut.putArchiveEntry(entry);
            tarOut.write("Hello world!".getBytes());
            tarOut.closeArchiveEntry();
            tarOut.finish();
        }

        assertEquals(10, (int) testFile.length());

        try (TarArchiveOutputStream tarIn = new TarArchiveOutputStream(new FileInputStream(testFile))) {
            TarArchiveEntry entry = tarIn.getNextEntry();
            assertEquals("test.txt", entry.getName());
            assertEquals("Test file.", entry.getComment());
            assertEquals(10, entry.getSize());
            byte[] buffer = new byte[10];
            tarIn.read(buffer);
            assertEquals("Hello world!", new String(buffer));
            tarIn.close();
        }
    }

    /**
     * Test that an error is thrown if a long file name is used.
     * @throws IOException on error
     */
    @Test
    public void testLongFileNameError() throws IOException {
        try (FileOutputStream fos = new FileOutputStream(testFile);
             TarArchiveOutputStream tarOut = new TarArchiveOutputStream(fos)) {
            TarArchiveEntry entry = new TarArchiveEntry("test" + new String(new char[1000]) + ".txt", "Test file.");
            assertThrows(RuntimeException.class, () -> tarOut.putArchiveEntry(entry));
        }
    }

    /**
     * Test that a directory entry can be added to the archive.
     * @throws IOException on error
     */
    @Test
    public void testDirectoryEntry() throws IOException {
        try (FileOutputStream fos = new FileOutputStream(testFile);
             TarArchiveOutputStream tarOut = new TarArchiveOutputStream(fos)) {

            TarArchiveEntry dirEntry = new TarArchiveEntry("testdir/");
            tarOut.putArchiveEntry(dirEntry);
            tarOut.finish();
        }

        try (TarArchiveOutputStream tarOut = new TarArchiveOutputStream(new FileOutputStream(testFile))) {
            TarArchiveEntry dirEntry = tarOut.getNextEntry();
            assertEquals("testdir/", dirEntry.getName());
            assertNull(dirEntry.getSize());
            assertFalse(dirEntry.isDirectory());
            tarOut.closeArchiveEntry();
            tarOut.finish();
        }
    }

    /**
     * Test that an error is thrown if putArchiveEntry is called after finish()
     * @throws IOException on error
     */
    @Test
    public void testPutArchiveEntryAfterFinish() throws IOException {
        try (FileOutputStream fos = new FileOutputStream(testFile);
             TarArchiveOutputStream tarOut = new TarArchiveOutputStream(fos)) {

            tarOut.finish();
            assertThrows(IOException.class, () -> tarOut.putArchiveEntry(new TarArchiveEntry("test.txt", "Test file.")));
        }
    }

    /**
     * Test that an error is thrown if closeArchiveEntry is called before putArchiveEntry
     * @throws IOException on error
     */
    @Test
    public void testCloseArchiveEntryBeforePutArchiveEntry() throws IOException {
        try (FileOutputStream fos = new FileOutputStream(testFile);
             TarArchiveOutputStream tarOut = new TarArchiveOutputStream(fos)) {

            assertThrows(IOException.class, () -> tarOut.closeArchiveEntry());
        }
    }
}
package org.apache.commons.compress.archivers.tar;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.Before;
import org.junit.Test;

public class TarArchiveOutputStreamTest {
    private TarArchiveOutputStream tarOutput;

    @Before
    public void setUp() throws Exception {
        // Create a new TarArchiveOutputStream for each test case
        tarOutput = new TarArchiveOutputStream(null);
    }

    @Test
    public void testSetLongFileMode() {
        // Test LONGFILE_ERROR mode
        tarOutput.setLongFileMode(TarArchiveOutputStream.LONGFILE_ERROR);
        assertEquals(TarArchiveOutputStream.LONGFILE_ERROR, tarOutput.getLongFileMode());

        // Test LONGFILE_TRUNCATE mode
        tarOutput.setLongFileMode(TarArchiveOutputStream.LONGFILE_TRUNCATE);
        assertEquals(TarArchiveOutputStream.LONGFILE_TRUNCATE, tarOutput.getLongFileMode());

        // Test LONGFILE_GNU mode
        tarOutput.setLongFileMode(TarArchiveOutputStream.LONGFILE_GNU);
        assertEquals(TarArchiveOutputStream.LONGFILE_GNU, tarOutput.getLongFileMode());
    }

    @Test
    public void testFinish() {
        try {
            // Finish the TarArchiveOutputStream
            tarOutput.finish();
            assertTrue(tarOutput.isFinished());

            // Attempt to finish the TarArchiveOutputStream again
            tarOutput.finish();
            fail("Expected IOException was not thrown");
        } catch (Exception e) {
            assertTrue(e instanceof IOException);
            assertEquals("This archive has already been finished", e.getMessage());
        }
    }

    @Test
    public void testClose() {
        try {
            // Close the TarArchiveOutputStream
            tarOutput.close();
            assertTrue(tarOutput.isClosed());
        } catch (Exception e) {
            fail("Unexpected exception was thrown");
        }
    }

    @Test
    public void testPutArchiveEntry() {
        try {
            // Create a new TarArchiveEntry
            TarArchiveEntry entry = new TarArchiveEntry("test");

            // Put the TarArchiveEntry into the TarArchiveOutputStream
            tarOutput.putArchiveEntry(entry);
            assertTrue(tarOutput.hasUnclosedEntry());

            // Attempt to put another TarArchiveEntry without closing the previous one
            tarOutput.putArchiveEntry(entry);
            fail("Expected IOException was not thrown");
        } catch (Exception e) {
            assertTrue(e instanceof IOException);
            assertEquals("No current entry to close", e.getMessage());
        }
    }

    @Test
    public void testCloseArchiveEntry() {
        try {
            // Close an entry without putting one
            tarOutput.closeArchiveEntry();
            fail("Expected IOException was not thrown");
        } catch (Exception e) {
            assertTrue(e instanceof IOException);
            assertEquals("No current entry to close", e.getMessage());
        }
    }

    @Test
    public void testWrite() {
        try {
            // Write bytes to the TarArchiveOutputStream
            byte[] bytes = {1, 2, 3};
            tarOutput.write(bytes);
            assertEquals(bytes.length, tarOutput.getBytesWritten());
        } catch (Exception e) {
            fail("Unexpected exception was thrown");
        }
    }

    @Test
    public void testCreateArchiveEntry() {
        try {
            // Create an ArchiveEntry from a file
            File file = new File("test.txt");
            ArchiveEntry entry = tarOutput.createArchiveEntry(file, "test.txt");
            assertTrue(entry instanceof TarArchiveEntry);
            assertEquals("test.txt", entry.getName());

            // Create an ArchiveEntry from a directory
            File directory = new File("testDir");
            ArchiveEntry entry2 = tarOutput.createArchiveEntry(directory, "testDir");
            assertTrue(entry2 instanceof TarArchiveEntry);
            assertEquals("testDir", entry2.getName());
            assertTrue(((TarArchiveEntry) entry2).isDirectory());
        } catch (Exception e) {
            fail("Unexpected exception was thrown");
        }
    }

    @Test
    public void testFlush() {
        try {
            // Flush the TarArchiveOutputStream
            tarOutput.flush();
            // No assertion as this method does not have any effect in this case
        } catch (Exception e) {
            fail("Unexpected exception was thrown");
        }
    }
}
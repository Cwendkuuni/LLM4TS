import org.junit.Test;

import static org.junit.Assert.*;

public class CpioArchiveOutputStreamTest {

    @Test
    public void testPutArchiveEntry() {
        // Test case 1: Put a valid CPIO archive entry
        CpioArchiveOutputStream cpioOutput = new CpioArchiveOutputStream(out);
        CpioArchiveEntry entry = new CpioArchiveEntry();
        entry.setName("testfile");
        entry.setFormat(CpioConstants.FORMAT_NEW);
        entry.setFileSize(5);
        entry.setMode(CpioConstants.C_ISREG);
        try {
            cpioOutput.putArchiveEntry(entry);
        } catch (IOException e) {
            fail("IOException thrown: " + e.getMessage());
        }

        // Test case 2: Put an invalid CPIO archive entry with unknown format
        CpioArchiveEntry invalidEntry = new CpioArchiveEntry();
        invalidEntry.setName("invalidfile");
        invalidEntry.setFormat((short) 999);
        invalidEntry.setFileSize(10);
        invalidEntry.setMode(CpioConstants.C_ISDIR);
        try {
            cpioOutput.putArchiveEntry(invalidEntry);
            fail("IllegalArgumentException not thrown");
        } catch (IllegalArgumentException e) {
            // IllegalArgumentException expected
            assertEquals("Unknown format: 999", e.getMessage());
        } catch (IOException e) {
            fail("IOException thrown: " + e.getMessage());
        }
    }

    @Test
    public void testCloseArchiveEntry() {
        // Test case 1: Close a valid CPIO archive entry
        CpioArchiveOutputStream cpioOutput = new CpioArchiveOutputStream(out);
        CpioArchiveEntry entry = new CpioArchiveEntry();
        entry.setName("testfile");
        entry.setFormat(CpioConstants.FORMAT_NEW);
        try {
            cpioOutput.putArchiveEntry(entry);
            cpioOutput.closeArchiveEntry();
        } catch (IOException e) {
            fail("IOException thrown: " + e.getMessage());
        }

        // Test case 2: Close an invalid CPIO archive entry with incorrect size
        CpioArchiveEntry invalidEntry = new CpioArchiveEntry();
        invalidEntry.setName("invalidfile");
        invalidEntry.setFormat(CpioConstants.FORMAT_OLD_ASCII);
        invalidEntry.setFileSize(10);
        try {
            cpioOutput.putArchiveEntry(invalidEntry);
            cpioOutput.closeArchiveEntry();
            fail("IOException not thrown");
        } catch (IOException e) {
            // IOException expected
            assertEquals("invalid entry size (expected 10 but got 0 bytes)", e.getMessage());
        }
    }

    @Test
    public void testWrite() {
        CpioArchiveOutputStream cpioOutput = new CpioArchiveOutputStream(out);
        CpioArchiveEntry entry = new CpioArchiveEntry();
        entry.setName("testfile");
        entry.setFormat(CpioConstants.FORMAT_NEW);
        entry.setFileSize(5);
        entry.setMode(CpioConstants.C_ISREG);
        try {
            cpioOutput.putArchiveEntry(entry);
            cpioOutput.write("12345".getBytes());
            cpioOutput.closeArchiveEntry();
        } catch (IOException e) {
            fail("IOException thrown: " + e.getMessage());
        }
    }

    @Test
    public void testFinish() {
        CpioArchiveOutputStream cpioOutput = new CpioArchiveOutputStream(out);
        CpioArchiveEntry entry = new CpioArchiveEntry();
        entry.setName("testfile");
        entry.setFormat(CpioConstants.FORMAT_NEW);
        try {
            cpioOutput.putArchiveEntry(entry);
            cpioOutput.finish();
        } catch (IOException e) {
            fail("IOException thrown: " + e.getMessage());
        }
    }

    @Test
    public void testClose() {
        CpioArchiveOutputStream cpioOutput = new CpioArchiveOutputStream(out);
        CpioArchiveEntry entry = new CpioArchiveEntry();
        entry.setName("testfile");
        entry.setFormat(CpioConstants.FORMAT_NEW);
        try {
            cpioOutput.putArchiveEntry(entry);
            cpioOutput.close();
        } catch (IOException e) {
            fail("IOException thrown: " + e.getMessage());
        }
        assertTrue(cpioOutput.isClosed());
    }

    @Test
    public void testCreateArchiveEntry() {
        CpioArchiveOutputStream cpioOutput = new CpioArchiveOutputStream(out);
        File inputFile = new File("testfile.txt");
        String entryName = "testfile";
        try {
            ArchiveEntry entry = cpioOutput.createArchiveEntry(inputFile, entryName);
            assertTrue(entry instanceof CpioArchiveEntry);
            assertEquals(entryName, entry.getName());
        } catch (IOException e) {
            fail("IOException thrown: " + e.getMessage());
        }
    }
}
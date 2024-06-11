import org.apache.commons.compress.archivers.cpio.CpioArchiveEntry;
import org.apache.commons.compress.archivers.cpio.CpioArchiveOutputStream;
import org.apache.commons.compress.utils.ArchiveUtils;
import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Test class for CpioArchiveOutputStream
 */
public class CpioArchiveOutputStreamTest {

    @Test
    public void testCpioArchiveOutputStream() throws IOException {
        // Test constructor with new format
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        CpioArchiveOutputStream cpioArchiveOutputStream = new CpioArchiveOutputStream(outputStream, CpioConstants.FORMAT_NEW);
        Assert.assertNotNull(cpioArchiveOutputStream);

        // Test constructor with default format (new format)
        ByteArrayOutputStream outputStreamDefault = new ByteArrayOutputStream();
        CpioArchiveOutputStream cpioArchiveOutputStreamDefault = new CpioArchiveOutputStream(outputStreamDefault);
        Assert.assertNotNull(cpioArchiveOutputStreamDefault);
        Assert.assertEquals(CpioConstants.FORMAT_NEW, cpioArchiveOutputStreamDefault.getEntryFormat());

        // Test write a new entry
        CpioArchiveEntry entry = new CpioArchiveEntry();
        entry.setName("testfile");
        String testContents = "12345";
        entry.setFileSize(testContents.length());
        entry.setMode(CpioConstants.C_ISREG); // regular file
        cpioArchiveOutputStreamDefault.putArchiveEntry(entry);
        cpioArchiveOutputStreamDefault.write(testContents.getBytes());
        cpioArchiveOutputStreamDefault.closeArchiveEntry();

        // Test write a new entry with exception
        try {
            cpioArchiveOutputStreamDefault.putArchiveEntry(null);
            Assert.fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        // Test write a new entry with duplicate name
        CpioArchiveEntry entryDuplicate = new CpioArchiveEntry();
        entryDuplicate.setName("testfile");
        try {
            cpioArchiveOutputStreamDefault.putArchiveEntry(entryDuplicate);
            Assert.fail("Expected IOException with message 'duplicate entry: testfile'");
        } catch (IOException e) {
            Assert.assertEquals("duplicate entry: testfile", e.getMessage());
        }

        // Test write a old ASCII entry
        cpioArchiveOutputStreamDefault.putArchiveEntry(new CpioOldAsciiArchiveEntry());

        // Test write a old binary entry
        cpioArchiveOutputStreamDefault.putArchiveEntry(new CpioOldBinaryArchiveEntry());

        // Test write a new entry with wrong format
        CpioArchiveEntry entryWrongFormat = new CpioArchiveEntry();
        entryWrongFormat.setFormat(CpioConstants.FORMAT_OLD_BINARY);
        try {
            cpioArchiveOutputStreamDefault.putArchiveEntry(entryWrongFormat);
            Assert.fail("Expected IllegalArgumentException with message 'Unknown format: 2'");
        } catch (IllegalArgumentException e) {
            Assert.assertEquals("Unknown format: 2", e.getMessage());
        }

        // Test close and finish
        cpioArchiveOutputStreamDefault.finish();
        cpioArchiveOutputStreamDefault.close();

        // Test write a new entry and check names
        byte[] expectedNames = ArchiveUtils.toByteArray(new String[]{"testfile", "CPIO_TRAILER"});
        cpioArchiveOutputStreamDefault = new CpioArchiveOutputStream(outputStreamDefault);
        cpioArchiveOutputStreamDefault.putArchiveEntry(new CpioArchiveEntry("testfile1.txt"));
        cpioArchiveOutputStreamDefault.putArchiveEntry(new CpioArchiveEntry("testfile2.txt"));
        cpioArchiveOutputStreamDefault.write(ArchiveUtils.toByteArray(expectedNames));
        cpioArchiveOutputStreamDefault.finish();
        cpioArchiveOutputStreamDefault.close();

        // Test read archive
        byte[] bytes = outputStreamDefault.toByteArray();
        CpioArchiveInputStream cpioArchiveInputStream = new CpioArchiveInputStream(new ByteArrayInputStream(bytes));
        Map<String, CpioArchiveEntry> expectedEntries = new HashMap<>();
        expectedEntries.put("testfile1.txt", new CpioArchiveEntry("testfile1.txt"));
        expectedEntries.put("testfile2.txt", new CpioArchiveEntry("testfile2.txt"));
        Map<String, CpioArchiveEntry> actualEntries = new HashMap<>();
        while (cpioArchiveInputStream.hasNextEntry()) {
            actualEntries.put(cpioArchiveInputStream.getNextEntry().getName(), cpioArchiveInputStream.getNextEntry());
        }
        Assert.assertEquals(expectedEntries, actualEntries);
    }

    /**
     * Old ASCII ArchiveEntry for testing
     */
    public static class CpioOldAsciiArchiveEntry extends CpioArchiveEntry {
        public CpioOldAsciiArchiveEntry() {
            super("testfile", CpioConstants.C_ISREG, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0);
            setFormat(CpioConstants.FORMAT_OLD_ASCII);
        }
    }

    /**
     * Old binary ArchiveEntry for testing
     */
    public static class CpioOldBinaryArchiveEntry extends CpioArchiveEntry {
        public CpioOldBinaryArchiveEntry() {
            super("testfile", CpioConstants.C_ISREG, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0);
            setFormat(CpioConstants.FORMAT_OLD_BINARY);
        }
    }
}
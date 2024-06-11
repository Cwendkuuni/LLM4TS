import org.apache.commons.compress.archivers.cpio.CpioArchiveEntry;
import org.apache.commons.compress.archivers.cpio.CpioArchiveOutputStream;
import org.apache.commons.compress.utils.ArchiveUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

public class CpioArchiveOutputStreamTest {

    private OutputStream outputStream;
    private CpioArchiveOutputStream cpioArchiveOutputStream;

    @Before
    public void setUp() throws Exception {
        outputStream = new ByteArrayOutputStream();
        cpioArchiveOutputStream = new CpioArchiveOutputStream(outputStream, CpioConstants.FORMAT_NEW);
    }

    @After
    public void tearDown() throws Exception {
        cpioArchiveOutputStream.close();
        outputStream.close();
    }

    @Test
    public void testPutArchiveEntry() throws IOException {
        // Create a test entry and write it to the output stream
        String testFileName = "testFile";
        byte[] testFileContent = "Test content".getBytes(StandardCharsets.UTF_8);
        CpioArchiveEntry testEntry = new CpioArchiveEntry();
        testEntry.setName(testFileName);
        testEntry.setSize(testFileContent.length);
        cpioArchiveOutputStream.putArchiveEntry(testEntry);
        cpioArchiveOutputStream.write(testFileContent, 0, testFileContent.length);
        cpioArchiveOutputStream.closeArchiveEntry();

        // Check the written data matches the expected data
        byte[] output = ((ByteArrayOutputStream) outputStream).toByteArray();
        int headerLength = ArchiveUtils.MAGIC_NEW.length + 120; // length of the header
        int expectedDataLength = testFileContent.length + 1; // length of the data including the null byte
        int actualDataLength = output.length - headerLength;
        assertEquals(expectedDataLength, actualDataLength);
        byte[] expectedData = new byte[expectedDataLength];
        System.arraycopy(testFileContent, 0, expectedData, 0, testFileContent.length);
        expectedData[expectedData.length - 1] = 0; // add the null byte
        assertArrayEquals(expectedData, output, 0, expectedData.length);
    }

    @Test
    public void testPutArchiveEntryWithInvalidFormat() throws IOException {
        // Test putting an entry with an invalid format
        CpioArchiveEntry testEntry = new CpioArchiveEntry();
        testEntry.setFormat((short) 1234); // an invalid format
        assertThrows(IllegalArgumentException.class, () -> cpioArchiveOutputStream.putArchiveEntry(testEntry));
    }

    @Test
    public void testPutArchiveEntryWithNullEntry() throws IOException {
        // Test putting a null entry
        assertThrows(NullPointerException.class, () -> cpioArchiveOutputStream.putArchiveEntry(null));
    }

    @Test
    public void testPutArchiveEntryWithDuplicateEntry() throws IOException {
        // Test putting a duplicate entry
        String testFileName = "testFile";
        byte[] testFileContent = "Test content".getBytes(StandardCharsets.UTF_8);
        CpioArchiveEntry testEntry = new CpioArchiveEntry();
        testEntry.setName(testFileName);
        testEntry.setSize(testFileContent.length);
        cpioArchiveOutputStream.putArchiveEntry(testEntry);
        cpioArchiveOutputStream.write(testFileContent, 0, testFileContent.length);
        cpioArchiveOutputStream.closeArchiveEntry();

        // Try to put the same entry again
        assertThrows(IOException.class, () -> cpioArchiveOutputStream.putArchiveEntry(testEntry));
    }

    @Test
    public void testCloseArchiveEntryWithUnclosedEntry() throws IOException {
        // Test closeArchiveEntry with an unclosed entry
        String testFileName = "testFile";
        byte[] testFileContent = "Test content".getBytes(StandardCharsets.UTF_8);
        CpioArchiveEntry testEntry = new CpioArchiveEntry();
        testEntry.setName(testFileName);
        testEntry.setSize(testFileContent.length);
        cpioArchiveOutputStream.putArchiveEntry(testEntry);

        // Write data to the entry but don't close it
        assertFalse(cpioArchiveOutputStream.isClosed());
        assertFalse(cpioArchiveOutputStream.isFinished());
        cpioArchiveOutputStream.write(testFileContent, 0, testFileContent.length);

        // Try to close the entry
        assertThrows(IOException.class, () -> cpioArchiveOutputStream.closeArchiveEntry());
    }

    @Test
    public void testCloseArchiveEntryWithClosedEntry() throws IOException {
        // Test closeArchiveEntry with a closed entry
        String testFileName = "testFile";
        byte[] testFileContent = "Test content".getBytes(StandardCharsets.UTF_8);
        CpioArchiveEntry testEntry = new CpioArchiveEntry();
        testEntry.setName(testFileName);
        testEntry.setSize(testFileContent.length);
        cpioArchiveOutputStream.putArchiveEntry(testEntry);
        cpioArchiveOutputStream.write(testFileContent, 0, testFileContent.length);
        cpioArchiveOutputStream.closeArchiveEntry();

        // Try to close the entry again
        assertNull(cpioArchiveOutputStream.getNextEntry());
        assertFalse(cpioArchiveOutputStream.isClosed());
        assertFalse(cpioArchiveOutputStream.isFinished());
        cpioArchiveOutputStream.closeArchiveEntry();
        assertNull(cpioArchiveOutputStream.getNextEntry());
        assertTrue(cpioArchiveOutputStream.isClosed());
        assertTrue(cpioArchiveOutputStream.isFinished());
    }

    @Test
    public void testFinishWithUnfinishedStream() throws IOException {
        // Test finish with an unfinished stream
        assertFalse(cpioArchiveOutputStream.isFinished());
        assertThrows(IOException.class, () -> cpioArchiveOutputStream.finish());
    }

    @Test
    public void testFinishWithFinishedStream() throws IOException {
        // Test finish with a finished stream
        String testFileName = "testFile";
        byte[] testFileContent = "Test content".getBytes(StandardCharsets.UTF_8);
        CpioArchiveEntry testEntry = new CpioArchiveEntry();
        testEntry.setName(testFileName);
        testEntry.setSize(testFileContent.length);
        cpioArchiveOutputStream.putArchiveEntry(testEntry);
        cpioArchiveOutputStream.write(testFileContent, 0, testFileContent.length);
        cpioArchiveOutputStream.closeArchiveEntry();
        cpioArchiveOutputStream.finish();

        // Try to write data to the stream again
        assertFalse(cpioArchiveOutputStream.isClosed());
        assertFalse(cpioArchiveOutputStream.isFinished());
        byte[] testFileContent2 = "Test content2".getBytes(StandardCharsets.UTF_8);
        assertThrows(IOException.class, () -> cpioArchiveOutputStream.write(testFileContent2, 0, testFileContent2.length));
    }

    @Test
    public void testCreateArchiveEntry() throws IOException {
        // Test creating an archive entry with a file
        File testFile = new File("testFile.txt");
        testFile.createNewFile();
        CpioArchiveEntry testEntry = cpioArchiveOutputStream.createArchiveEntry(testFile, "testFile.txt");
        assertNotNull(testEntry);
        assertEquals("testFile.txt", testEntry.getName());
        assertEquals(testFile.length(), testEntry.getSize());
        assertEquals(CpioConstants.C_ISREG, testEntry.getMode());
        testFile.delete();
    }

    @Test
    public void testCreateArchiveEntryWithNullFile() throws IOException {
        // Test creating an archive entry with a null file
        assertThrows(NullPointerException.class, () -> cpioArchiveOutputStream.createArchiveEntry(null, "testFile.txt"));
    }

    @Test
    public void testCreateArchiveEntryWithEmptyFileName() throws IOException {
        // Test creating an archive entry with an empty filename
        assertThrows(IllegalArgumentException.class, () -> cpioArchiveOutputStream.createArchiveEntry(new File("testFile.txt"), ""));
    }

    @Test
    public void testCreateArchiveEntryWithInvalidFileName() throws IOException {
        // Test creating an archive entry with an invalid filename
        assertThrows(IllegalArgumentException.class, () -> cpioArchiveOutputStream.createArchiveEntry(new File("testFile.txt"), "test\0File.txt"));
    }
}
package org.apache.commons.compress.archivers.tar;

import static org.junit.Assert.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.ArchiveOutputStream;
import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarConstants;
import org.apache.commons.compress.utils.ArchiveUtils;

/**
 * Tests for {@link TarArchiveOutputStream}.
 */
public class TarArchiveOutputStreamTest {

    public static void main(String[] args) throws Exception {
        testWriteDirectory();
        testWriteFile();
        testWriteFileWithLongName();
        testWriteAndFinish();
        testWriteAndFinishWithErrors();
        testWriteAndFinishMultipleFiles();
    }

    private static void testWriteDirectory() throws IOException {
        final String directoryName = "test_directory";
        OutputStream outputStream = new ByteArrayOutputStream();
        TarArchiveOutputStream archiveOutputStream = new TarArchiveOutputStream(outputStream,
                TarConstants.DEFAULT_BLOCK_SIZE, TarConstants.DEFAULT_RECORD_SIZE);
        archiveOutputStream.setLongFileMode(TarArchiveOutputStream.LONGFILE_TRUNCATE);

        // Create a test directory
        ArchiveEntry directoryEntry = new TarArchiveEntry(directoryName);
        archiveOutputStream.putArchiveEntry(directoryEntry);
        archiveOutputStream.closeArchiveEntry();

        // Add some test files to the directory
        for (String fileName : new String[]{"file1.txt", "file2.txt"}) {
            ArchiveEntry fileEntry = new TarArchiveEntry(directoryName, fileName);
            archiveOutputStream.putArchiveEntry(fileEntry);
            archiveOutputStream.write(new byte[0], 0, 0);
            archiveOutputStream.closeArchiveEntry();
        }

        archiveOutputStream.finish();
        archiveOutputStream.close();

        // Verify the output
        ByteArrayInputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
        ArchiveInputStream archiveInputStream = new TarArchiveInputStream(inputStream);
        ArchiveEntry testEntry = archiveInputStream.getNextEntry();
        assertEquals(directoryName, testEntry.getName());
        assertTrue(testEntry.isDirectory());
        testEntry = archiveInputStream.getNextEntry();
        assertEquals("file1.txt", testEntry.getName());
        assertFalse(testEntry.isDirectory());
        testEntry = archiveInputStream.getNextEntry();
        assertEquals("file2.txt", testEntry.getName());
        assertFalse(testEntry.isDirectory());
        archiveInputStream.close();
    }

    private static void testWriteFile() throws IOException {
        OutputStream outputStream = new ByteArrayOutputStream();
        TarArchiveOutputStream archiveOutputStream = new TarArchiveOutputStream(outputStream,
                TarConstants.DEFAULT_BLOCK_SIZE, TarConstants.DEFAULT_RECORD_SIZE);

        ArchiveEntry fileEntry = new TarArchiveEntry("test_file.txt");
        archiveOutputStream.putArchiveEntry(fileEntry);
        archiveOutputStream.write("testing 1 2 3".getBytes());
        archiveOutputStream.closeArchiveEntry();

        archiveOutputStream.finish();
        archiveOutputStream.close();

        ByteArrayInputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
        ArchiveInputStream archiveInputStream = new TarArchiveInputStream(inputStream);
        ArchiveEntry testEntry = archiveInputStream.getNextEntry();
        assertEquals("test_file.txt", testEntry.getName());
        assertFalse(testEntry.isDirectory());
        byte[] testData = new byte[13];
        inputStream.read(testData, 0, 13);
        String testDataString = new String(testData);
        assertEquals("testing 1 2 3", testDataString);
        archiveInputStream.close();
    }

    private static void testWriteFileWithLongName() throws IOException {
        OutputStream outputStream = new ByteArrayOutputStream();
        TarArchiveOutputStream archiveOutputStream = new TarArchiveOutputStream(outputStream,
                TarConstants.DEFAULT_BLOCK_SIZE, TarConstants.DEFAULT_RECORD_SIZE);
        archiveOutputStream.setLongFileMode(TarArchiveOutputStream.LONGFILE_GNU);

        final int nameLength = TarConstants.NAMELEN + 1;
        ArchiveEntry fileEntry = new TarArchiveEntry(
                "long_name_" + new String(new char[nameLength].fill('a')));
        archiveOutputStream.putArchiveEntry(fileEntry);
        archiveOutputStream.write("testing 1 2 3".getBytes());
        archiveOutputStream.closeArchiveEntry();

        archiveOutputStream.finish();
        archiveOutputStream.close();

        ByteArrayInputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
        ArchiveInputStream archiveInputStream = new TarArchiveInputStream(inputStream);
        ArchiveEntry testEntry = archiveInputStream.getNextEntry();
        assertEquals("long_name_" + new String(new char[nameLength].fill('a')) + "/",
                testEntry.getName().substring(0, testEntry.getName().lastIndexOf('/')));
        assertEquals("long_name_" + new String(new char[nameLength].fill('a')),
                testEntry.getName());
        assertFalse(testEntry.isDirectory());
        byte[] testData = new byte[13];
        inputStream.read(testData, 0, 13);
        String testDataString = new String(testData);
        assertEquals("testing 1 2 3", testDataString);
        archiveInputStream.close();
    }

    private static void testWriteAndFinish() throws IOException {
        OutputStream outputStream = new ByteArrayOutputStream();
        TarArchiveOutputStream archiveOutputStream = new TarArchiveOutputStream(outputStream,
                TarConstants.DEFAULT_BLOCK_SIZE, TarConstants.DEFAULT_RECORD_SIZE);

        ArchiveEntry fileEntry = new TarArchiveEntry("test_file.txt");
        archiveOutputStream.putArchiveEntry(fileEntry);
        archiveOutputStream.write("testing 1 2 3".getBytes());
        archiveOutputStream.closeArchiveEntry();

        archiveOutputStream.finish();
        archiveOutputStream.close();

        ByteArrayInputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
        ArchiveInputStream archiveInputStream = new TarArchiveInputStream(inputStream);
        ArchiveEntry testEntry = archiveInputStream.getNextEntry();
        assertEquals("test_file.txt", testEntry.getName());
        assertFalse(testEntry.isDirectory());
        byte[] testData = new byte[13];
        inputStream.read(testData, 0, 13);
        String testDataString = new String(testData);
        assertEquals("testing 1 2 3", testDataString);
        archiveInputStream.close();
    }

    private static void testWriteAndFinishWithErrors() throws IOException {
        OutputStream outputStream = new ByteArrayOutputStream();
        TarArchiveOutputStream archiveOutputStream = new TarArchiveOutputStream(outputStream,
                TarConstants.DEFAULT_BLOCK_SIZE, TarConstants.DEFAULT_RECORD_SIZE);

        ArchiveEntry fileEntry = new TarArchiveEntry("test_file.txt");
        archiveOutputStream.putArchiveEntry(fileEntry);
        archiveOutputStream.write("testing 1 2 3".getBytes());

        // Force an error to be thrown
        archiveOutputStream.write("an error occurred".getBytes(), 0, 14);

        try {
            archiveOutputStream.closeArchiveEntry();
            fail("Expected IOException");
        } catch (IOException e) {
            // Expected
        }

        try {
            archiveOutputStream.finish();
            fail("Expected IOException");
        } catch (IOException e) {
            // Expected
        }

        archiveOutputStream.close();

        ByteArrayInputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
        ArchiveInputStream archiveInputStream = new TarArchiveInputStream(inputStream);
        try {
            archiveInputStream.getNextEntry();
            fail("Expected IOException");
        } catch (IOException e) {
            // Expected
        }
        archiveInputStream.close();
    }

    private static void testWriteAndFinishMultipleFiles() throws IOException {
        OutputStream outputStream = new ByteArrayOutputStream();
        TarArchiveOutputStream archiveOutputStream = new TarArchiveOutputStream(outputStream,
                TarConstants.DEFAULT_BLOCK_SIZE, TarConstants.DEFAULT_RECORD_SIZE);

        ArchiveEntry file1 = new TarArchiveEntry("file1.txt");
        archiveOutputStream.putArchiveEntry(file1);
        archiveOutputStream.write("testing 1 2 3".getBytes());
        archiveOutputStream.closeArchiveEntry();

        ArchiveEntry file2 = new TarArchiveEntry("file2.txt");
        archiveOutputStream.putArchiveEntry(file2);
        archiveOutputStream.write("testing 4 5 6".getBytes());
        archiveOutputStream.closeArchiveEntry();

        archiveOutputStream.finish();
        archiveOutputStream.close();

        ByteArrayInputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
        ArchiveInputStream archiveInputStream = new TarArchiveInputStream(inputStream);
        ArchiveEntry testEntry1 = archiveInputStream.getNextEntry();
        assertEquals("file1.txt", testEntry1.getName());
        assertFalse(testEntry1.isDirectory());
        byte[] testData1 = new byte[13];
        inputStream.read(testData1, 0, 13);
        String testDataString1 = new String(testData1);
        assertEquals("testing 1 2 3", testDataString1);
        testEntry1 = archiveInputStream.getNextEntry();
        assertEquals("file2.txt", testEntry1.getName());
        assertFalse(testEntry1.isDirectory());
        byte[] testData2 = new byte[13];
        inputStream.read(testData2, 0, 13);
        String testDataString2 = new String(testData2);
        assertEquals("testing 4 5 6", testDataString2);
        archiveInputStream.close();
    }
}
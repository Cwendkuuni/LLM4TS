package org.apache.commons.compress.archivers.cpio;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.CpioArchiveEntry;
import org.apache.commons.compress.archivers.CpioConstants;
import org.apache.commons.compress.utils.ArchiveUtils;

/**
 * Test cases for {@link CpioArchiveOutputStream} class
 */
public class CpioArchiveOutputStreamTest {

    private static final int HEADER_PAD_COUNT = 6;

    @Test
    public void testCpioArchiveOutputStreamCreation() {
        OutputStream out = new ByteArrayOutputStream();
        CpioArchiveOutputStream archiveOut = new CpioArchiveOutputStream(out, CpioConstants.FORMAT_NEW);
        assertEquals(CpioConstants.FORMAT_NEW, archiveOut.getEntryFormat());
    }

    @Test
    public void testCpioArchiveOutputStreamCreationWithFormat() {
        OutputStream out = new ByteArrayOutputStream();
        CpioArchiveOutputStream archiveOut = new CpioArchiveOutputStream(out, CpioConstants.FORMAT_OLD_ASCII);
        assertEquals(CpioConstants.FORMAT_OLD_ASCII, archiveOut.getEntryFormat());
    }

    @Test
    public void testWriteEntriesWithValidData() throws IOException {
        OutputStream out = new ByteArrayOutputStream();
        CpioArchiveOutputStream archiveOut = new CpioArchiveOutputStream(out, CpioConstants.FORMAT_NEW);

        CpioArchiveEntry entry1 = new CpioArchiveEntry("file1.txt");
        entry1.setSize(10);
        archiveOut.putArchiveEntry(entry1);
        byte[] file1Data = "File content of file1".getBytes(StandardCharsets.UTF_8);
        archiveOut.write(file1Data, 0, file1Data.length);
        archiveOut.closeArchiveEntry();

        CpioArchiveEntry entry2 = new CpioArchiveEntry("file2.txt");
        entry2.setSize(5);
        archiveOut.putArchiveEntry(entry2);
        byte[] file2Data = "File content of file2".getBytes(StandardCharsets.UTF_8);
        archiveOut.write(file2Data, 0, file2Data.length);
        archiveOut.closeArchiveEntry();

        archiveOut.finish();

        byte[] result = out.toByteArray();
        ArchiveInputStream testInput = new ArchiveInputStream(new ByteArrayInputStream(result));
        ArchiveEntry entry;

        entry = testInput.getNextEntry();
        assertEquals("file1.txt", entry.getName());
        byte[] readFile1Data = new byte[entry.getSize()];
        testInput.read(readFile1Data, 0, readFile1Data.length);
        assertArrayEquals(file1Data, readFile1Data);

        entry = testInput.getNextEntry();
        assertEquals("file2.txt", entry.getName());
        byte[] readFile2Data = new byte[entry.getSize()];
        testInput.read(readFile2Data, 0, readFile2Data.length);
        assertArrayEquals(file2Data, readFile2Data);
    }

    @Test
    public void testWriteEntriesWithDuplicateName() throws IOException {
        OutputStream out = new ByteArrayOutputStream();
        CpioArchiveOutputStream archiveOut = new CpioArchiveOutputStream(out, CpioConstants.FORMAT_NEW);

        CpioArchiveEntry entry1 = new CpioArchiveEntry("file1.txt");
        archiveOut.putArchiveEntry(entry1);
        byte[] file1Data = "File content of file1".getBytes(StandardCharsets.UTF_8);
        archiveOut.write(file1Data, 0, file1Data.length);
        archiveOut.closeArchiveEntry();

        CpioArchiveEntry entry2 = new CpioArchiveEntry("file1.txt");
        assertThrows(IOException.class, () -> archiveOut.putArchiveEntry(entry2));

        archiveOut.finish();
    }

    @Test
    public void testWriteEntriesWithNullName() throws IOException {
        OutputStream out = new ByteArrayOutputStream();
        CpioArchiveOutputStream archiveOut = new CpioArchiveOutputStream(out, CpioConstants.FORMAT_NEW);

        CpioArchiveEntry entry = new CpioArchiveEntry(null);
        assertThrows(IllegalArgumentException.class, () -> archiveOut.putArchiveEntry(entry));

        archiveOut.finish();
    }

    @Test
    public void testWriteEntriesWithNullEntry() throws IOException {
        OutputStream out = new ByteArrayOutputStream();
        CpioArchiveOutputStream archiveOut = new CpioArchiveOutputStream(out, CpioConstants.FORMAT_NEW);
        assertNull(archiveOut.putArchiveEntry(null));

        archiveOut.finish();
    }

    @Test
    public void testWriteEntriesWithInvalidFormat() throws IOException {
        OutputStream out = new ByteArrayOutputStream();
        CpioArchiveOutputStream archiveOut = new CpioArchiveOutputStream(out, (short) 42);
        assertThrows(IllegalArgumentException.class, () -> archiveOut.putArchiveEntry(new CpioArchiveEntry("")));

        archiveOut.finish();
    }

    @Test
    public void testWriteTrailerWithValidData() throws IOException {
        OutputStream out = new ByteArrayOutputStream();
        CpioArchiveOutputStream archiveOut = new CpioArchiveOutputStream(out, CpioConstants.FORMAT_NEW);

        CpioArchiveEntry entry = new CpioArchiveEntry(CpioConstants.C_TRAILER, CpioConstants.FORMAT_NEW);
        archiveOut.putArchiveEntry(entry);
        archiveOut.closeArchiveEntry();

        archiveOut.finish();

        byte[] result = out.toByteArray();
        ArchiveInputStream testInput = new ArchiveInputStream(new ByteArrayInputStream(result));
        ArchiveEntry entryFromArchive = testInput.getNextEntry();
        assertEquals(CpioConstants.C_TRAILER, entryFromArchive.getName());
    }

    @Test
    public void testWriteTrailerWithInvalidFormat() throws IOException {
        OutputStream out = new ByteArrayOutputStream();
        CpioArchiveOutputStream archiveOut = new CpioArchiveOutputStream(out, CpioConstants.FORMAT_NEW);

        CpioArchiveEntry entry = new CpioArchiveEntry(CpioConstants.C_TRAILER, (short) 42);
        assertThrows(IllegalArgumentException.class, () -> archiveOut.putArchiveEntry(entry));

        archiveOut.finish();
    }

    @Test
    public void testWriteEntriesWithEmptyName() throws IOException {
        OutputStream out = new ByteArrayOutputStream();
        CpioArchiveOutputStream archiveOut = new CpioArchiveOutputStream(out, CpioConstants.FORMAT_NEW);

        CpioArchiveEntry entry = new CpioArchiveEntry("");
        archiveOut.putArchiveEntry(entry);
        byte[] fileData = "File content".getBytes(StandardCharsets.UTF_8);
        archiveOut.write(fileData, 0, fileData.length);
        archiveOut.closeArchiveEntry();

        archiveOut.finish();

        byte[] result = out.toByteArray();
        ArchiveInputStream testInput = new ArchiveInputStream(new ByteArrayInputStream(result));
        ArchiveEntry entryFromArchive = testInput.getNextEntry();
        assertEquals("", entryFromArchive.getName());
    }

    @Test
    public void testWriteEntriesWithZeroSize() throws IOException {
        OutputStream out = new ByteArrayOutputStream();
        CpioArchiveOutputStream archiveOut = new CpioArchiveOutputStream(out, CpioConstants.FORMAT_NEW);

        CpioArchiveEntry entry = new CpioArchiveEntry("file.txt");
        archiveOut.putArchiveEntry(entry);
        archiveOut.write(new byte[0], 0, 0);
        archiveOut.closeArchiveEntry();

        archiveOut.finish();

        byte[] result = out.toByteArray();
        ArchiveInputStream testInput = new ArchiveInputStream(new ByteArrayInputStream(result));
        ArchiveEntry entryFromArchive = testInput.getNextEntry();
        assertEquals("file.txt", entryFromArchive.getName());
        assertFalse(entryFromArchive.isDirectory());
        assertEquals(0, entryFromArchive.getSize());
    }

    @Test
    public void testWriteEntriesWithInvalidSize() throws IOException {
        OutputStream out = new ByteArrayOutputStream();
        CpioArchiveOutputStream archiveOut = new CpioArchiveOutputStream(out, CpioConstants.FORMAT_NEW);

        CpioArchiveEntry entry = new CpioArchiveEntry("file.txt");
        archiveOut.putArchiveEntry(entry);
        archiveOut.write(new byte[1], 0, 1);
        archiveOut.write(new byte[5], 1, 5); // Write more bytes than specified size
        archiveOut.closeArchiveEntry();

        archiveOut.finish();

        byte[] result = out.toByteArray();
        ArchiveInputStream testInput = new ArchiveInputStream(new ByteArrayInputStream(result));
        ArchiveEntry entryFromArchive = testInput.getNextEntry();
        assertEquals("file.txt", entryFromArchive.getName());
        assertFalse(entryFromArchive.isDirectory());
        assertEquals(6, entryFromArchive.getSize()); // Size should be equal to the total number of written bytes
    }

    @Test
    public void testWriteEntriesWithMultipleFilesWithSameNameButDifferentPaths()
            throws IOException {
        OutputStream out = new ByteArrayOutputStream();
        CpioArchiveOutputStream archiveOut = new CpioArchiveOutputStream(out, CpioConstants.FORMAT_NEW);

        CpioArchiveEntry entry1 = new CpioArchiveEntry("file.txt", "/path1/to/file.txt");
        archiveOut.putArchiveEntry(entry1);
        byte[] file1Data = "File content of file1".getBytes(StandardCharsets.UTF_8);
        archiveOut.write(file1Data, 0, file1Data.length);
        archiveOut.closeArchiveEntry();

        CpioArchiveEntry entry2 = new CpioArchiveEntry("file.txt", "/path2/to/file.txt");
        archiveOut.putArchiveEntry(entry2);
        byte[] file2Data = "File content of file2".getBytes(StandardCharsets.UTF_8);
        archiveOut.write(file2Data, 0, file2Data.length);
        archiveOut.closeArchiveEntry();

        archiveOut.finish();

        byte[] result = out.toByteArray();
        ArchiveInputStream testInput = new ArchiveInputStream(new ByteArrayInputStream(result));
        ArchiveEntry entry1FromArchive = testInput.getNextEntry();
        ArchiveEntry entry2FromArchive = testInput.getNextEntry();
        assertEquals("file.txt", entry1FromArchive.getName());
        assertEquals("/path1/to/file.txt", entry1FromArchive.getPathName());
        assertEquals("file.txt", entry2FromArchive.getName());
        assertEquals("/path2/to/file.txt", entry2FromArchive.getPathName());
    }
}
package org.apache.commons.compress.archivers.dump;

import static org.junit.Assert.*;

import org.apache.commons.compress.archivers.ArchiveException;
import org.apache.commons.compress.archivers.ArchiveInputStream;
import org.apache.commons.compress.archivers.zip.ZipEncoding;
import org.apache.commons.compress.archivers.zip.ZipEncodingHelper;
import org.apache.commons.compress.utils.IOUtils;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.PriorityQueue;
import java.util.Stack;

/**
 * JUnit 4 test cases for the DumpArchiveInputStream class.
 */
public class DumpArchiveInputStreamTest {

    @Test
    public void testMatches() throws IOException {
        byte[] headerBytes = new byte[DumpArchiveConstants.TP_SIZE];
        new ZipEncodingHelper().encodeFileName("", headerBytes, 0, ZipEncoding.UTF8);

        ByteArrayInputStream testStream = new ByteArrayInputStream(headerBytes);

        ArchiveInputStream archiveInputStream = new DumpArchiveInputStream(testStream, null);

        assertTrue(DumpArchiveInputStream.matches(headerBytes, headerBytes.length));

        assertTrue(DumpArchiveInputStream.matches(headerBytes, DumpArchiveConstants.TP_SIZE));

        assertFalse(DumpArchiveInputStream.matches(headerBytes, headerBytes.length - 1));

        IOUtils.closeQuietly(archiveInputStream);
    }

    @Test
    public void testEmptyStream() throws IOException, ArchiveException {
        ZipEncoding zipEncoding = ZipEncodingHelper.getZipEncoding(null);

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        DumpArchiveOutputStream dumpArchiveOutputStream = new DumpArchiveOutputStream(outputStream, zipEncoding);

        dumpArchiveOutputStream.putNextEntry(new DumpArchiveEntry("", ""));
        dumpArchiveOutputStream.finishEntry();
        dumpArchiveOutputStream.close();

        ByteArrayInputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());

        ArchiveInputStream archiveInputStream = new DumpArchiveInputStream(inputStream, zipEncoding);

        assertNull(archiveInputStream.getNextEntry());

        archiveInputStream.close();
    }

    @Test
    public void testReadDirectoryEntry() throws IOException, InterruptedException, ArchiveException {
        ZipEncoding zipEncoding = ZipEncodingHelper.getZipEncoding(null);

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        DumpArchiveOutputStream dumpArchiveOutputStream = new DumpArchiveOutputStream(outputStream, zipEncoding);

        dumpArchiveOutputStream.putNextEntry(new DumpArchiveEntry("dir", ".", DumpArchiveEntry.TYPE_DIRECTORY));
        dumpArchiveOutputStream.putNextEntry(new DumpArchiveEntry("file1", ".", DumpArchiveEntry.TYPE_FILE));
        dumpArchiveOutputStream.putNextEntry(new DumpArchiveEntry("file2", ".", DumpArchiveEntry.TYPE_FILE));
        dumpArchiveOutputStream.finishEntry();
        dumpArchiveOutputStream.close();

        ByteArrayInputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());

        ArchiveInputStream archiveInputStream = new DumpArchiveInputStream(inputStream, zipEncoding);

        DumpArchiveEntry entry = archiveInputStream.getNextEntry();
        assertEquals(".", entry.getName());
        assertEquals("dir", entry.getOriginalName());
        assertFalse(entry.isDirectory());
        assertFalse(entry.isFile());
        assertNull(entry.getSize());

        entry = archiveInputStream.getNextEntry();
        assertEquals("file1", entry.getName());
        assertEquals("file1", entry.getOriginalName());
        assertFalse(entry.isDirectory());
        assertTrue(entry.isFile());
        assertEquals(12, entry.getSize());

        entry = archiveInputStream.getNextEntry();
        assertEquals("file2", entry.getName());
        assertEquals("file2", entry.getOriginalName());
        assertFalse(entry.isDirectory());
        assertTrue(entry.isFile());
        assertEquals(18, entry.getSize());

        assertNull(archiveInputStream.getNextEntry());

        archiveInputStream.close();
    }

    @Test
    public void testReadFile() throws IOException, ArchiveException {
        ZipEncoding zipEncoding = ZipEncodingHelper.getZipEncoding(null);

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        DumpArchiveOutputStream dumpArchiveOutputStream = new DumpArchiveOutputStream(outputStream, zipEncoding);

        dumpArchiveOutputStream.putNextEntry(new DumpArchiveEntry("file", "", DumpArchiveEntry.TYPE_FILE));
        dumpArchiveOutputStream.write("Test".getBytes());
        dumpArchiveOutputStream.finishEntry();
        dumpArchiveOutputStream.close();

        ByteArrayInputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());

        ArchiveInputStream archiveInputStream = new DumpArchiveInputStream(inputStream, zipEncoding);

        byte[] buffer = new byte[8];

        int bytesRead = archiveInputStream.read(buffer, 0, 8);
        assertEquals("Test", new String(Arrays.copyOfRange(buffer, 0, bytesRead)));

        bytesRead = archiveInputStream.read(buffer, 0, 8);
        assertEquals(-1, bytesRead);

        archiveInputStream.close();
    }

    @Test
    public void testReadDirectory() throws IOException, ArchiveException {
        ZipEncoding zipEncoding = ZipEncodingHelper.getZipEncoding(null);

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        DumpArchiveOutputStream dumpArchiveOutputStream = new DumpArchiveOutputStream(outputStream, zipEncoding);

        dumpArchiveOutputStream.putNextEntry(new DumpArchiveEntry("dir", ".", DumpArchiveEntry.TYPE_DIRECTORY));
        dumpArchiveOutputStream.putNextEntry(new DumpArchiveEntry("file1", ".", DumpArchiveEntry.TYPE_FILE));
        dumpArchiveOutputStream.putNextEntry(new DumpArchiveEntry("file2", ".", DumpArchiveEntry.TYPE_FILE));
        dumpArchiveOutputStream.finishEntry();
        dumpArchiveOutputStream.close();

        ByteArrayInputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());

        ArchiveInputStream archiveInputStream = new DumpArchiveInputStream(inputStream, zipEncoding);

        Queue<DumpArchiveEntry> queue = new PriorityQueue<>(10, new Comparator<DumpArchiveEntry>() {
            @Override
            public int compare(DumpArchiveEntry o1, DumpArchiveEntry o2) {
                return o1.getName().compareTo(o2.getName());
            }
        });

        DumpArchiveEntry entry;

        while ((entry = archiveInputStream.getNextEntry()) != null) {
            queue.add(entry);
        }

        assertEquals(3, queue.size());

        entry = queue.remove();
        assertEquals("file1", entry.getName());
        assertEquals("file1", entry.getOriginalName());
        assertFalse(entry.isDirectory());
        assertTrue(entry.isFile());
        assertEquals(12, entry.getSize());

        entry = queue.remove();
        assertEquals("file2", entry.getName());
        assertEquals("file2", entry.getOriginalName());
        assertFalse(entry.isDirectory());
        assertTrue(entry.isFile());
        assertEquals(18, entry.getSize());

        entry = queue.remove();
        assertEquals("dir", entry.getName());
        assertEquals("dir", entry.getOriginalName());
        assertTrue(entry.isDirectory());
        assertFalse(entry.isFile());
        assertNull(entry.getSize());

        assertNull(archiveInputStream.getNextEntry());

        archiveInputStream.close();
    }

    @Test
    public void testReadDirectoryWithMissingParent() throws IOException, ArchiveException {
        ZipEncoding zipEncoding = ZipEncodingHelper.getZipEncoding(null);

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        DumpArchiveOutputStream dumpArchiveOutputStream = new DumpArchiveOutputStream(outputStream, zipEncoding);

        dumpArchiveOutputStream.putNextEntry(new DumpArchiveEntry("dir1", ".", DumpArchiveEntry.TYPE_DIRECTORY));
        dumpArchiveOutputStream.putNextEntry(new DumpArchiveEntry("file1", ".", DumpArchiveEntry.TYPE_FILE));
        dumpArchiveOutputStream.putNextEntry(new DumpArchiveEntry("file2", ".", DumpArchiveEntry.TYPE_FILE));
        dumpArchiveOutputStream.finishEntry();

        dumpArchiveOutputStream.putNextEntry(new DumpArchiveEntry("dir2", "dir1/", DumpArchiveEntry.TYPE_DIRECTORY));
        dumpArchiveOutputStream.putNextEntry(new DumpArchiveEntry("file3", ".", DumpArchiveEntry.TYPE_FILE));
        dumpArchiveOutputStream.finishEntry();

        dumpArchiveOutputStream.putNextEntry(new DumpArchiveEntry("dir3", ".", DumpArchiveEntry.TYPE_DIRECTORY));
        dumpArchiveOutputStream.finishEntry();

        dumpArchiveOutputStream.close();

        ByteArrayInputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());

        ArchiveInputStream archiveInputStream = new DumpArchiveInputStream(inputStream, zipEncoding);

        Queue<DumpArchiveEntry> queue = new PriorityQueue<>(10, new Comparator<DumpArchiveEntry>() {
            @Override
            public int compare(DumpArchiveEntry o1, DumpArchiveEntry o2) {
                return o1.getName().compareTo(o2.getName());
            }
        });

        DumpArchiveEntry entry;

        while ((entry = archiveInputStream.getNextEntry()) != null) {
            queue.add(entry);
        }

        assertEquals(5, queue.size());

        entry = queue.remove();
        assertEquals("file1", entry.getName());
        assertEquals("file1", entry.getOriginalName());
        assertFalse(entry.isDirectory());
        assertTrue(entry.isFile());
        assertEquals(12, entry.getSize());

        entry = queue.remove();
        assertEquals("file2", entry.getName());
        assertEquals("file2", entry.getOriginalName());
        assertFalse(entry.isDirectory());
        assertTrue(entry.isFile());
        assertEquals(18, entry.getSize());

        entry = queue.remove();
        assertEquals("dir1", entry.getName());
        assertEquals("dir1", entry.getOriginalName());
        assertTrue(entry.isDirectory());
        assertFalse(entry.isFile());
        assertNull(entry.getSize());

        entry = queue.remove();
        assertEquals("dir2", entry.getName());
        assertEquals("dir2", entry.getOriginalName());
        assertTrue(entry.isDirectory());
        assertFalse(entry.isFile());
        assertNull(entry.getSize());

        entry = queue.remove();
        assertEquals("dir3", entry.getName());
        assertEquals("dir3", entry.getOriginalName());
        assertTrue(entry.isDirectory());
        assertFalse(entry.isFile());
        assertNull(entry.getSize());

        assertNull(archiveInputStream.getNextEntry());

        archiveInputStream.close();
    }

    @Test
    public void testReadFileWithMissingParent() throws IOException, InterruptedException, ArchiveException {
        ZipEncoding zipEncoding = ZipEncodingHelper.getZipEncoding(null);

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        DumpArchiveOutputStream dumpArchiveOutputStream = new DumpArchiveOutputStream(outputStream, zipEncoding);

        dumpArchiveOutputStream.putNextEntry(new DumpArchiveEntry("dir1", ".", DumpArchiveEntry.TYPE_DIRECTORY));
        dumpArchiveOutputStream.putNextEntry(new DumpArchiveEntry("file1", ".", DumpArchiveEntry.TYPE_FILE));
        dumpArchiveOutputStream.finishEntry();

        dumpArchiveOutputStream.putNextEntry(new DumpArchiveEntry("dir2", ".", DumpArchiveEntry.TYPE_DIRECTORY));
        dumpArchiveOutputStream.finishEntry();

        dumpArchiveOutputStream.putNextEntry(new DumpArchiveEntry("file3", "dir1/dir2/", DumpArchiveEntry.TYPE_FILE));
        dumpArchiveOutputStream.write("Test".getBytes());
        dumpArchiveOutputStream.finishEntry();

        dumpArchiveOutputStream.close();

        ByteArrayInputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());

        ArchiveInputStream archiveInputStream = new DumpArchiveInputStream(inputStream, zipEncoding);

        DumpArchiveEntry entry;

        while ((entry = archiveInputStream.getNextEntry()) != null) {
            if (entry.isDirectory()) {
                continue;
            }
            break;
        }

        assertNotNull(entry);
        assertEquals("file1", entry.getName());
        assertEquals("file1", entry.getOriginalName());
        assertFalse(entry.isDirectory());
        assertTrue(entry.isFile());
        assertEquals(12, entry.getSize());

        entry = archiveInputStream.getNextEntry();
        assertEquals("file3", entry.getName());
        assertEquals("file3", entry.getOriginalName());
        assertFalse(entry.isDirectory());
        assertTrue(entry.isFile());
        assertEquals(17, entry.getSize());

        assertNull(archiveInputStream.getNextEntry());

        archiveInputStream.close();
    }
}
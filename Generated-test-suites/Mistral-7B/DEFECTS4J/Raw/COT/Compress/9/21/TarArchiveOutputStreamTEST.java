package org.apache.commons.compress.archivers.tar;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarConstants;
import org.apache.commons.compress.utils.ArchiveUtils;
import org.apache.commons.compress.utils.CountingInputStream;
import org.apache.commons.compress.utils.CountingOutputStream;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * Test the functionality of the TarArchiveOutputStream class.
 */
public class TarArchiveOutputStreamTest {

    private OutputStream outputStream;
    private TarArchiveOutputStream tarArchiveOutputStream;

    @Before
    public void setup() throws Exception {
        outputStream = new CountingOutputStream(new ByteArrayOutputStream());
        tarArchiveOutputStream = new TarArchiveOutputStream(outputStream);
    }

    @After
    public void tearDown() throws Exception {
        tarArchiveOutputStream.close();
    }

    /**
     * Test the constructor with no arguments.
     */
    @Test
    public void testTarArchiveOutputStreamConstructorNoArguments() {
        tarArchiveOutputStream = new TarArchiveOutputStream(new ByteArrayOutputStream());
        assertNull(tarArchiveOutputStream.getBuffer());
        assertNull(tarArchiveOutputStream.getAssemBuf());
        assertNull(tarArchiveOutputStream.getAssemLen());
        assertNull(tarArchiveOutputStream.getRecordBuf());
        assertNull(tarArchiveOutputStream.getBuffer());
        assertFalse(tarArchiveOutputStream.isFinished());
        assertFalse(tarArchiveOutputStream.isClosed());
    }

    /**
     * Test the constructor with no arguments and an invalid record size.
     */
    @Test
    public void testTarArchiveOutputStreamConstructorInvalidRecordSize() {
        assertThrows(IllegalArgumentException.class, () -> {
            tarArchiveOutputStream = new TarArchiveOutputStream(new ByteArrayOutputStream(), -1);
        });
    }

    /**
     * Test the constructor with no arguments and a valid record size.
     */
    @Test
    public void testTarArchiveOutputStreamConstructorValidRecordSize() {
        tarArchiveOutputStream = new TarArchiveOutputStream(new ByteArrayOutputStream(), 512);
        assertNotNull(tarArchiveOutputStream.getBuffer());
        assertNotNull(tarArchiveOutputStream.getAssemBuf());
        assertNull(tarArchiveOutputStream.getAssemLen());
        assertNotNull(tarArchiveOutputStream.getRecordBuf());
        assertEquals(512, tarArchiveOutputStream.getRecordSize());
        assertFalse(tarArchiveOutputStream.isFinished());
        assertFalse(tarArchiveOutputStream.isClosed());
    }

    /**
     * Test the constructor with arguments.
     */
    @Test
    public void testTarArchiveOutputStreamConstructor() {
        OutputStream outputStream = new CountingOutputStream(new ByteArrayOutputStream());
        tarArchiveOutputStream = new TarArchiveOutputStream(outputStream, 512, 1024);
        assertNotNull(tarArchiveOutputStream.getBuffer());
        assertNotNull(tarArchiveOutputStream.getAssemBuf());
        assertNull(tarArchiveOutputStream.getAssemLen());
        assertNotNull(tarArchiveOutputStream.getRecordBuf());
        assertEquals(512, tarArchiveOutputStream.getRecordSize());
        assertFalse(tarArchiveOutputStream.isFinished());
        assertFalse(tarArchiveOutputStream.isClosed());
    }

    /**
     * Test setting the longFileMode.
     */
    @Test
    public void testSetLongFileMode() {
        tarArchiveOutputStream.setLongFileMode(TarArchiveOutputStream.LONGFILE_TRUNCATE);
        assertEquals(TarArchiveOutputStream.LONGFILE_TRUNCATE, tarArchiveOutputStream.getLongFileMode());
    }

    /**
     * Test finishing the archive.
     */
    @Test
    public void testFinish() throws IOException {
        ArchiveEntry archiveEntry = new TarArchiveEntry("test.txt");
        tarArchiveOutputStream.putArchiveEntry(archiveEntry);
        tarArchiveOutputStream.write("Test archive finish".getBytes());
        tarArchiveOutputStream.closeArchiveEntry();
        tarArchiveOutputStream.finish();
        ArchiveInputStream archiveInputStream = new TarFileInputStream(new ByteArrayInputStream(outputStream.getCountData()));
        TarArchiveEntry tarArchiveEntry = (TarArchiveEntry) archiveInputStream.getNextEntry();
        assertEquals("test.txt", tarArchiveEntry.getName());
        assertFalse(tarArchiveEntry.isDirectory());
        assertEquals(14, tarArchiveEntry.getSize());
        assertEquals("Test archive finish", new String(ArchiveUtils.toByteArray(tarArchiveEntry.getDataInputStream())));
        assertTrue(tarArchiveOutputStream.isFinished());
    }

    /**
     * Test closing the archive.
     */
    @Test
    public void testClose() throws IOException {
        ArchiveEntry archiveEntry = new TarArchiveEntry("test.txt");
        tarArchiveOutputStream.putArchiveEntry(archiveEntry);
        tarArchiveOutputStream.write("Test archive close".getBytes());
        tarArchiveOutputStream.closeArchiveEntry();
        tarArchiveOutputStream.close();
        ArchiveInputStream archiveInputStream = new TarFileInputStream(new ByteArrayInputStream(outputStream.getCountData()));
        TarArchiveEntry tarArchiveEntry = (TarArchiveEntry) archiveInputStream.getNextEntry();
        assertEquals("test.txt", tarArchiveEntry.getName());
        assertFalse(tarArchiveEntry.isDirectory());
        assertEquals(13, tarArchiveEntry.getSize());
        assertEquals("Test archive close", new String(ArchiveUtils.toByteArray(tarArchiveEntry.getDataInputStream())));
        assertTrue(tarArchiveOutputStream.isClosed());
    }

    /**
     * Test writing data to an empty archive.
     */
    @Test
    public void testWriteDataToEmptyArchive() throws IOException {
        ArchiveEntry archiveEntry = new TarArchiveEntry("test.txt");
        tarArchiveOutputStream.putArchiveEntry(archiveEntry);
        tarArchiveOutputStream.write("Test archive data".getBytes());
        tarArchiveOutputStream.closeArchiveEntry();
        ArchiveInputStream archiveInputStream = new TarFileInputStream(new ByteArrayInputStream(outputStream.getCountData()));
        TarArchiveEntry tarArchiveEntry = (TarArchiveEntry) archiveInputStream.getNextEntry();
        assertEquals("test.txt", tarArchiveEntry.getName());
        assertFalse(tarArchiveEntry.isDirectory());
        assertEquals(13, tarArchiveEntry.getSize());
        assertEquals("Test archive data", new String(ArchiveUtils.toByteArray(tarArchiveEntry.getDataInputStream())));
    }

    /**
     * Test writing data to an archive with an existing entry.
     */
    @Test
    public void testWriteDataToArchiveWithExistingEntry() throws IOException {
        ArchiveEntry archiveEntry = new TarArchiveEntry("test.txt");
        tarArchiveOutputStream.putArchiveEntry(archiveEntry);
        tarArchiveOutputStream.write("Test archive data 1".getBytes());
        tarArchiveOutputStream.closeArchiveEntry();

        ArchiveEntry archiveEntry2 = new TarArchiveEntry("test.txt");
        tarArchiveOutputStream.putArchiveEntry(archiveEntry2);
        tarArchiveOutputStream.write("Test archive data 2".getBytes());
        tarArchiveOutputStream.closeArchiveEntry();

        ArchiveInputStream archiveInputStream = new TarFileInputStream(new ByteArrayInputStream(outputStream.getCountData()));
        TarArchiveEntry tarArchiveEntry = (TarArchiveEntry) archiveInputStream.getNextEntry();
        assertEquals("test.txt", tarArchiveEntry.getName());
        assertFalse(tarArchiveEntry.isDirectory());
        assertEquals(26, tarArchiveEntry.getSize());
        assertEquals("Test archive data 1Test archive data 2", new String(ArchiveUtils.toByteArray(tarArchiveEntry.getDataInputStream())));
    }

    /**
     * Test writing data to an archive with a directory entry.
     */
    @Test
    public void testWriteDataToArchiveWithDirectoryEntry() throws IOException {
        ArchiveEntry archiveEntry = new TarArchiveEntry("test");
        archiveEntry.setDirectory(true);
        tarArchiveOutputStream.putArchiveEntry(archiveEntry);
        tarArchiveOutputStream.finishEntry();
        ArchiveEntry archiveEntry2 = new TarArchiveEntry("test/test.txt");
        tarArchiveOutputStream.putArchiveEntry(archiveEntry2);
        tarArchiveOutputStream.write("Test archive data".getBytes());
        tarArchiveOutputStream.closeArchiveEntry();

        ArchiveInputStream archiveInputStream = new TarFileInputStream(new ByteArrayInputStream(outputStream.getCountData()));
        TarArchiveEntry tarArchiveEntry = (TarArchiveEntry) archiveInputStream.getNextEntry();
        assertNull(tarArchiveEntry);
        tarArchiveEntry = (TarArchiveEntry) archiveInputStream.getNextEntry();
        assertEquals("test/", tarArchiveEntry.getName());
        assertTrue(tarArchiveEntry.isDirectory());
        assertFalse(tarArchiveEntry.isDirectory());
        archiveInputStream.close();

        archiveInputStream = new TarFileInputStream(new ByteArrayInputStream(outputStream.getCountData()));
        tarArchiveEntry = (TarArchiveEntry) archiveInputStream.getNextEntry();
        assertEquals("test.txt", tarArchiveEntry.getName());
        assertFalse(tarArchiveEntry.isDirectory());
        assertEquals(10, tarArchiveEntry.getSize());
        assertEquals("Test archive data", new String(ArchiveUtils.toByteArray(tarArchiveEntry.getDataInputStream())));
    }

    /**
     * Test putting an entry with a long file name.
     */
    @Test
    public void testPutArchiveEntryWithLongFileName() throws IOException {
        String longFileName = "a" + new String(new char[TarConstants.NAMELEN + 1]).replace("\0", "b");
        tarArchiveOutputStream.setLongFileMode(TarArchiveOutputStream.LONGFILE_GNU);
        ArchiveEntry archiveEntry = new TarArchiveEntry(longFileName);
        tarArchiveOutputStream.putArchiveEntry(archiveEntry);
        byte[] nameBytes = ArchiveUtils.toAsciiBytes(longFileName);
        write(nameBytes);
        write(0);
        closeArchiveEntry();
        ArchiveInputStream archiveInputStream = new TarFileInputStream(new ByteArrayInputStream(outputStream.getCountData()));
        TarArchiveEntry tarArchiveEntry = (TarArchiveEntry) archiveInputStream.getNextEntry();
        assertEquals(TarConstants.GNU_LONGLINK, tarArchiveEntry.getHeaderType());
        assertEquals(TarConstants.LF_GNUTYPE_LONGNAME, tarArchiveEntry.getFileType());
        assertEquals(nameBytes.length + 1, tarArchiveEntry.getSize());
    }

    /**
     * Test putting an entry with a long file name and no long file support.
     */
    @Test
    public void testPutArchiveEntryWithLongFileNameNoSupport() throws IOException {
        String longFileName = "a" + new String(new char[TarConstants.NAMELEN + 1]).replace("\0", "b");
        tarArchiveOutputStream.setLongFileMode(TarArchiveOutputStream.LONGFILE_ERROR);
        ArchiveEntry archiveEntry = new TarArchiveEntry(longFileName);
        assertThrows(RuntimeException.class, () -> {
            tarArchiveOutputStream.putArchiveEntry(archiveEntry);
        });
    }

    /**
     * Test putting an entry with a truncated long file name.
     */
    @Test
    public void testPutArchiveEntryWithTruncatedLongFileName() throws IOException {
        String longFileName = "a" + new String(new char[TarConstants.NAMELEN]).replace("\0", "b");
        tarArchiveOutputStream.setLongFileMode(TarArchiveOutputStream.LONGFILE_TRUNCATE);
        ArchiveEntry archiveEntry = new TarArchiveEntry(longFileName);
        tarArchiveOutputStream.putArchiveEntry(archiveEntry);
        byte[] nameBytes = ArchiveUtils.toAsciiBytes(longFileName);
        write(nameBytes, 0, nameBytes.length);
        closeArchiveEntry();
        ArchiveInputStream archiveInputStream = new TarFileInputStream(new ByteArrayInputStream(outputStream.getCountData()));
        TarArchiveEntry tarArchiveEntry = (TarArchiveEntry) archiveInputStream.getNextEntry();
        assertEquals(longFileName.substring(0, TarConstants.NAMELEN), tarArchiveEntry.getName());
    }

    /**
     * Test closing an entry that was not put.
     */
    @Test
    public void testCloseArchiveEntryBeforePut() {
        assertThrows(IOException.class, () -> {
            tarArchiveOutputStream.closeArchiveEntry();
        });
    }

    /**
     * Test writing data beyond the size specified in the header.
     */
    @Test
    public void testWriteDataBeyondSizeInHeader() throws IOException {
        ArchiveEntry archiveEntry = new TarArchiveEntry("test.txt");
        tarArchiveOutputStream.putArchiveEntry(archiveEntry);
        tarArchiveOutputStream.write("Test archive data".getBytes());
        tarArchiveOutputStream.closeArchiveEntry();

        ArchiveEntry archiveEntry2 = new TarArchiveEntry("test.txt");
        tarArchiveOutputStream.putArchiveEntry(archiveEntry2);
        byte[] data = new byte[1024];
        tarArchiveOutputStream.write(data);

        ArchiveInputStream archiveInputStream = new TarFileInputStream(new ByteArrayInputStream(outputStream.getCountData()));
        TarArchiveEntry tarArchiveEntry = (TarArchiveEntry) archiveInputStream.getNextEntry();
        assertEquals("test.txt", tarArchiveEntry.getName());
        assertFalse(tarArchiveEntry.isDirectory());
        assertEquals(13, tarArchiveEntry.getSize());
        assertEquals("Test archive data", new String(ArchiveUtils.toByteArray(tarArchiveEntry.getDataInputStream())));
        assertThrows(IOException.class, () -> {
            tarArchiveInputStream.write(data, 0, data.length);
        });
    }

    /**
     * Test writing data that is not a multiple of the record size.
     */
    @Test
    public void testWriteDataNotMultipleOfRecordSize() throws IOException {
        ArchiveEntry archiveEntry = new TarArchiveEntry("test.txt");
        tarArchiveOutputStream.putArchiveEntry(archiveEntry);
        byte[] data = new byte[11];
        tarArchiveOutputStream.write(data);
        byte[] buffer = new byte[recordBuf.length];
        tarArchiveOutputStream.read(buffer);
        int offset = 0;
        for (int i = 0; i < data.length; i++) {
            assertEquals(data[i], buffer[offset++]);
        }
    }

    /**
     * Test writing data that is a multiple of the record size.
     */
    @Test
    public void testWriteDataMultipleOfRecordSize() throws IOException {
        ArchiveEntry archiveEntry = new TarArchiveEntry("test.txt");
        tarArchiveOutputStream.putArchiveEntry(archiveEntry);
        byte[] data = new byte[512];
        tarArchiveOutputStream.write(data);
        byte[] buffer = new byte[recordBuf.length];
        tarArchiveOutputStream.read(buffer);
        int offset = 0;
        for (int i = 0; i < data.length; i++) {
            assertEquals(data[i], buffer[offset++]);
        }
    }

}
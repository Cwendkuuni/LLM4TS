package org.apache.commons.compress.archivers.tar;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.ArchiveOutputStream;
import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarConstants;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;

public class TarArchiveOutputStreamTest {

    @Test
    public void testPutArchiveEntryWithLongFileName() throws IOException {
        // Given
        String longFileName = "path/to/some/very/long/file/name.txt";
        String shortFileName = "short_file.txt";
        ArchiveEntry archiveEntry = new TarArchiveEntry(new File(shortFileName));
        TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(new ByteArrayOutputStream());
        tarArchiveOutputStream.setLongFileMode(TarArchiveOutputStream.LONGFILE_GNU);

        // When
        tarArchiveOutputStream.putArchiveEntry(archiveEntry);
        TarArchiveEntry longFileNameEntry = new TarArchiveEntry(new File(longFileName));
        tarArchiveOutputStream.putArchiveEntry(longFileNameEntry);

        // Then
        assertTrue(tarArchiveOutputStream.getRecordSize() == TarConstants.RECORDSIZE);
        byte[] record = tarArchiveOutputStream.getBuffer().getRecord();
        assertTrue(record[1] == TarConstants.LF_MAGIC);
        assertTrue(record[4] == TarConstants.LF_VERSION_1);

        // First entry is short file name
        assertEquals("short_file.txt", new String(record, 51, TarConstants.NAMELEN));
        assertEquals(new File(shortFileName).length(), tarArchiveOutputStream.getCurrSize());

        // Second entry is long file name
        assertEquals(longFileName.getBytes().length + 1, record[55]); // NUL terminator
        assertEquals(longFileName, new String(record, 56, longFileName.length()));
        assertEquals(new File(longFileName).length(), tarArchiveOutputStream.getCurrSize());

        // Clean up
        tarArchiveOutputStream.finish();
    }

    @Test
    public void testPutArchiveEntryWithLongFileNameAndTruncate() throws IOException {
        // Given
        String longFileName = "path/to/some/very/long/file/name.txt";
        String shortFileName = "short_file.txt";
        ArchiveEntry archiveEntry = new TarArchiveEntry(new File(shortFileName));
        TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(new ByteArrayOutputStream());
        tarArchiveOutputStream.setLongFileMode(TarArchiveOutputStream.LONGFILE_TRUNCATE);

        // When
        tarArchiveOutputStream.putArchiveEntry(archiveEntry);
        TarArchiveEntry longFileNameEntry = new TarArchiveEntry(new File(longFileName));
        tarArchiveOutputStream.putArchiveEntry(longFileNameEntry);

        // Then
        assertTrue(tarArchiveOutputStream.getRecordSize() == TarConstants.RECORDSIZE);
        byte[] record = tarArchiveOutputStream.getBuffer().getRecord();
        assertTrue(record[1] == TarConstants.LF_MAGIC);
        assertTrue(record[4] == TarConstants.LF_VERSION_1);

        // First entry is short file name
        assertEquals("short_file.txt", new String(record, 51, TarConstants.NAMELEN));
        assertEquals(new File(shortFileName).length(), tarArchiveOutputStream.getCurrSize());

        // Second entry is truncated long file name
        byte[] longFileNameBytes = longFileName.getBytes();
        assertEquals(longFileNameBytes.length, record[55]); // NUL terminator
        assertTrue(longFileNameBytes.length > TarConstants.NAMELEN);
        assertNull(new String(record, 56, longFileName.length()));
        assertEquals(new File(shortFileName).length(), tarArchiveOutputStream.getCurrSize());

        // Clean up
        tarArchiveOutputStream.finish();
    }

    @Test
    public void testPutArchiveEntryWithLongFileNameAndError() throws IOException {
        // Given
        String longFileName = "path/to/some/very/long/file/name.txt";
        String shortFileName = "short_file.txt";
        ArchiveEntry archiveEntry = new TarArchiveEntry(new File(shortFileName));
        TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(new ByteArrayOutputStream());

        // When
        tarArchiveOutputStream.putArchiveEntry(archiveEntry);
        TarArchiveEntry longFileNameEntry = new TarArchiveEntry(new File(longFileName));
        try {
            tarArchiveOutputStream.putArchiveEntry(longFileNameEntry);
            fail("Expected IOException - file name '" + longFileName
                    + "' is too long");
        } catch (IOException e) {
            // Then
            assertTrue(e.getMessage().contains("file name '"));
            assertTrue(tarArchiveOutputStream.getCurrSize() == 0);
        }

        // Clean up
        tarArchiveOutputStream.finish();
    }

    @Test
    public void testPutArchiveEntryWithDirectory() throws IOException {
        // Given
        String dirName = "path/to/some/directory";
        ArchiveEntry archiveEntry = new TarArchiveEntry(new File(dirName), false);
        TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(new ByteArrayOutputStream());

        // When
        tarArchiveOutputStream.putArchiveEntry(archiveEntry);

        // Then
        assertTrue(tarArchiveOutputStream.getRecordSize() == TarConstants.RECORDSIZE);
        byte[] record = tarArchiveOutputStream.getBuffer().getRecord();
        assertTrue(record[1] == TarConstants.LF_MAGIC);
        assertTrue(record[4] == TarConstants.LF_VERSION_1);

        assertEquals("path/to/some/directory/", new String(record, 51, TarConstants.NAMELEN));
        assertEquals(0, tarArchiveOutputStream.getCurrSize());

        // Clean up
        tarArchiveOutputStream.finish();
    }

    @Test
    public void testWrite() throws IOException {
        // Given
        String longFileName = "path/to/some/very/long/file/name.txt";
        String shortFileName = "short_file.txt";
        File file = new File("test_file.txt");
        TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(new FileOutputStream(file));
        ArchiveEntry archiveEntry = new TarArchiveEntry(new File(shortFileName));
        TarArchiveEntry longFileNameEntry = new TarArchiveEntry(new File(longFileName));
        byte[] data = "Data to be written".getBytes();

        // When
        tarArchiveOutputStream.putArchiveEntry(archiveEntry);
        tarArchiveOutputStream.write(data, 0, data.length);
        tarArchiveOutputStream.closeArchiveEntry();
        tarArchiveOutputStream.putArchiveEntry(longFileNameEntry);
        tarArchiveOutputStream.write(data, 0, data.length);
        tarArchiveOutputStream.closeArchiveEntry();
        tarArchiveOutputStream.finish();

        // Then
        assertTrue(file.length() == 2 * data.length + 2 * TarConstants.RECORDSIZE);

        // Clean up
        file.delete();
    }
}
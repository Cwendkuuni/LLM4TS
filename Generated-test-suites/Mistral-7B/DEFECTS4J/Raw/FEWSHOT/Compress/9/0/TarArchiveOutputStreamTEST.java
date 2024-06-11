package org.apache.commons.compress.archivers.tar;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

import org.apache.commons.compress.utils.ArchiveUtils;
import org.junit.Test;

public class TarArchiveOutputStreamTest {

    @Test
    public void testEmptyArchive() throws IOException {
        final int blockSize = 512;
        final int recordSize = 512;
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        TarArchiveOutputStream tarOS = new TarArchiveOutputStream(output, blockSize, recordSize);
        tarOS.finish();
        tarOS.close();

        InputStream input = new ByteArrayInputStream(output.toByteArray());
        TarArchiveInputStream tarIS = new TarArchiveInputStream(input);
        assertNull(tarIS.getNextEntry());
        tarIS.close();
    }

    @Test
    public void testWriteSingleEntry() throws IOException {
        final int blockSize = 512;
        final int recordSize = 512;
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        TarArchiveOutputStream tarOS = new TarArchiveOutputStream(output, blockSize, recordSize);
        tarOS.setLongFileMode(TarArchiveOutputStream.LONGFILE_TRUNCATE);
        File testFile = new File("testFile.txt");
        byte[] fileContent = "Hello, World!".getBytes(StandardCharsets.UTF_8);
        TarArchiveEntry entry = new TarArchiveEntry("testFile.txt");
        tarOS.putArchiveEntry(entry);
        output.write(fileContent, 0, fileContent.length);
        tarOS.closeArchiveEntry();
        tarOS.finish();
        tarOS.close();

        InputStream input = new ByteArrayInputStream(output.toByteArray());
        TarArchiveInputStream tarIS = new TarArchiveInputStream(input);
        TarArchiveEntry archiveEntry = tarIS.getNextEntry();
        assertEquals("testFile.txt", archiveEntry.getName());
        assertEquals(fileContent.length, archiveEntry.getSize());
        InputStream entryInputStream = tarIS.getInputStream();
        byte[] buffer = new byte[fileContent.length];
        assertEquals(fileContent.length, entryInputStream.read(buffer));
        assertArrayEquals(fileContent, buffer);
        tarIS.closeEntry();
        tarIS.close();
    }

    @Test
    public void testWriteLongFileName() throws IOException {
        final int blockSize = 512;
        final int recordSize = 1024;
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        TarArchiveOutputStream tarOS = new TarArchiveOutputStream(output, blockSize, recordSize);
        tarOS.setLongFileMode(TarArchiveOutputStream.LONGFILE_GNU);
        String longFileName = "a/long/filename/with/many/parts.txt";
        File testFile = new File(longFileName);
        byte[] fileContent = "Long file name test".getBytes(StandardCharsets.UTF_8);
        TarArchiveEntry entry = new TarArchiveEntry(testFile, longFileName);
        tarOS.putArchiveEntry(entry);
        output.write(ArchiveUtils.toByteArray(longFileName), 0, longFileName.getBytes(StandardCharsets.UTF_8).length);
        output.write(fileContent, 0, fileContent.length);
        tarOS.closeArchiveEntry();
        tarOS.finish();
        tarOS.close();

        InputStream input = new ByteArrayInputStream(output.toByteArray());
        TarArchiveInputStream tarIS = new TarArchiveInputStream(input);
        TarArchiveEntry archiveEntry = tarIS.getNextEntry();
        assertEquals("a/long/filename/with/many/parts.txt", archiveEntry.getName());
        assertEquals(fileContent.length + longFileName.getBytes(StandardCharsets.UTF_8).length + 1, archiveEntry.getSize());
        InputStream entryInputStream = tarIS.getInputStream();
        byte[] buffer = new byte[fileContent.length];
        assertEquals(fileContent.length, entryInputStream.read(buffer));
        assertArrayEquals(fileContent, buffer);
        tarIS.closeEntry();
        tarIS.close();
    }

    @Test(expected = IOException.class)
    public void testWriteLongFileName_LongFileError() throws IOException {
        final int blockSize = 512;
        final int recordSize = 1024;
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        TarArchiveOutputStream tarOS = new TarArchiveOutputStream(output, blockSize, recordSize);
        tarOS.setLongFileMode(TarArchiveOutputStream.LONGFILE_ERROR);
        String longFileName = "a/long/filename/with/many/parts.txt";
        File testFile = new File(longFileName);
        byte[] fileContent = "Long file name test".getBytes(StandardCharsets.UTF_8);
        TarArchiveEntry entry = new TarArchiveEntry(testFile, longFileName);
        tarOS.putArchiveEntry(entry);
        output.write(ArchiveUtils.toByteArray(longFileName), 0, longFileName.getBytes(StandardCharsets.UTF_8).length);
        output.write(fileContent, 0, fileContent.length);
        tarOS.closeArchiveEntry();
        tarOS.finish();
        tarOS.close();
    }

    @Test
    public void testWriteMultipleEntries() throws IOException {
        final int blockSize = 512;
        final int recordSize = 512;
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        TarArchiveOutputStream tarOS = new TarArchiveOutputStream(output, blockSize, recordSize);
        tarOS.setLongFileMode(TarArchiveOutputStream.LONGFILE_TRUNCATE);

        File testFile1 = new File("testFile1.txt");
        byte[] fileContent1 = "Hello, World!".getBytes(StandardCharsets.UTF_8);
        TarArchiveEntry entry1 = new TarArchiveEntry("testFile1.txt");
        tarOS.putArchiveEntry(entry1);
        output.write(fileContent1, 0, fileContent1.length);

        File testFile2 = new File("testFile2.txt");
        byte[] fileContent2 = "Goodbye, World!".getBytes(StandardCharsets.UTF_8);
        TarArchiveEntry entry2 = new TarArchiveEntry("testFile2.txt");
        tarOS.putArchiveEntry(entry2);
        output.write(fileContent2, 0, fileContent2.length);

        tarOS.closeArchiveEntry();
        tarOS.finish();
        tarOS.close();

        InputStream input = new ByteArrayInputStream(output.toByteArray());
        TarArchiveInputStream tarIS = new TarArchiveInputStream(input);
        TarArchiveEntry archiveEntry1 = tarIS.getNextEntry();
        assertEquals("testFile1.txt", archiveEntry1.getName());
        assertEquals(fileContent1.length, archiveEntry1.getSize());
        InputStream entryInputStream1 = tarIS.getInputStream();
        byte[] buffer1 = new byte[fileContent1.length];
        assertEquals(fileContent1.length, entryInputStream1.read(buffer1));
        assertArrayEquals(fileContent1, buffer1);
        tarIS.closeEntry();

        TarArchiveEntry archiveEntry2 = tarIS.getNextEntry();
        assertEquals("testFile2.txt", archiveEntry2.getName());
        assertEquals(fileContent2.length, archiveEntry2.getSize());
        InputStream entryInputStream2 = tarIS.getInputStream();
        byte[] buffer2 = new byte[fileContent2.length];
        assertEquals(fileContent2.length, entryInputStream2.read(buffer2));
        assertArrayEquals(fileContent2, buffer2);
        tarIS.closeEntry();
        tarIS.close();
    }

    @Test(expected = IOException.class)
    public void testWriteMultipleEntries_LongFileError() throws IOException {
        final int blockSize = 512;
        final int recordSize = 512;
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        TarArchiveOutputStream tarOS = new TarArchiveOutputStream(output, blockSize, recordSize);
        tarOS.setLongFileMode(TarArchiveOutputStream.LONGFILE_ERROR);

        File testFile1 = new File("testFile1.txt");
        byte[] fileContent1 = "Hello, World!".getBytes(StandardCharsets.UTF_8);
        TarArchiveEntry entry1 = new TarArchiveEntry("testFile1.txt");
        tarOS.putArchiveEntry(entry1);
        output.write(fileContent1, 0, fileContent1.length);

        File testFile2 = new File("testFile2.txt");
        byte[] fileContent2 = "Goodbye, World!".getBytes(StandardCharsets.UTF_8);
        TarArchiveEntry entry2 = new TarArchiveEntry("testFile2.txt");
        tarOS.putArchiveEntry(entry2);
        output.write(fileContent2, 0, fileContent2.length);

        tarOS.closeArchiveEntry();
        tarOS.finish();
        tarOS.close();
    }
}
package org.apache.commons.compress.archivers.tar;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Arrays;

import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.ArchiveOutputStream;
import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarConstants;
import org.junit.Test;

public class TarArchiveOutputStreamTest {

    @Test
    public void testEmptyOutputStream() {
        assertThrows(NullPointerException.class, () -> new TarArchiveOutputStream(null));
    }

    @Test
    public void testPutArchiveEntryWithNullEntry() {
        OutputStream os = new ByteArrayOutputStream();
        TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(os);
        assertThrows(NullPointerException.class, () -> tarArchiveOutputStream.putArchiveEntry(null));
    }

    @Test
    public void testPutArchiveEntryWithValidEntry() throws IOException {
        OutputStream os = new ByteArrayOutputStream();
        TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(os);
        TarArchiveEntry tarArchiveEntry = new TarArchiveEntry("test.txt");

        tarArchiveOutputStream.putArchiveEntry(tarArchiveEntry);

        assertEquals("000000000000000000000000000000000000\n"
                + "54686973207468652063616D656E7465726E65\n"
                + "000000000001\n"
                + "test.txt\n"
                + "\n", Arrays.toString(tarArchiveOutputStream.getRecordBuf()));
    }

    @Test
    public void testPutArchiveEntryWithLongFileName() throws IOException {
        OutputStream os = new ByteArrayOutputStream();
        TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(os);
        TarArchiveEntry tarArchiveEntry = new TarArchiveEntry("test/longFileName.txt");
        tarArchiveOutputStream.setLongFileMode(TarArchiveOutputStream.LONGFILE_GNU);

        tarArchiveOutputStream.putArchiveEntry(tarArchiveEntry);

        assertEquals("000000000000000000000000000000000000\n"
                + "54686973207468652063616D656E7465726E65\n"
                + "000000000001\n"
                + "test/\n"
                + "longFileName.txt\n"
                + "\n", Arrays.toString(tarArchiveOutputStream.getRecordBuf()));
    }

    @Test
    public void testPutArchiveEntryWithLongFileNameAndTruncation() throws IOException {
        OutputStream os = new ByteArrayOutputStream();
        TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(os);

        TarArchiveEntry tarArchiveEntry = new TarArchiveEntry("test/longFileNameThatWillBeTruncated.txt");
        tarArchiveOutputStream.setLongFileMode(TarArchiveOutputStream.LONGFILE_TRUNCATE);

        tarArchiveOutputStream.putArchiveEntry(tarArchiveEntry);

        assertEquals("000000000000000000000000000000000000\n"
                + "54686973207468652063616D656E7465726E65\n"
                + "000000000001\n"
                + "test/\n"
                + "longFileNameThatWillBeTruncated.\n"
                + "\n", Arrays.toString(tarArchiveOutputStream.getRecordBuf()));
    }

    @Test
    public void testCloseArchiveEntryWithEmptyBuffer() throws IOException {
        OutputStream os = new ByteArrayOutputStream();
        TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(os);
        TarArchiveEntry tarArchiveEntry = new TarArchiveEntry("test.txt");
        tarArchiveOutputStream.putArchiveEntry(tarArchiveEntry);
        tarArchiveOutputStream.closeArchiveEntry();

        assertEquals(0, tarArchiveOutputStream.getCurrBytes());
        assertEquals(0, tarArchiveOutputStream.getCurrSize());
    }

    @Test
    public void testCloseArchiveEntryWithNonEmptyBuffer() throws IOException {
        OutputStream os = new ByteArrayOutputStream();
        TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(os);
        TarArchiveEntry tarArchiveEntry = new TarArchiveEntry("test.txt");
        tarArchiveOutputStream.putArchiveEntry(tarArchiveEntry);
        byte[] data = "Hello World".getBytes();
        tarArchiveOutputStream.write(data, 0, data.length);
        tarArchiveOutputStream.closeArchiveEntry();

        assertEquals("Hello World", new String(tarArchiveOutputStream.getRecordBuf(), 512));
        assertEquals(data.length, tarArchiveOutputStream.getCurrBytes());
        assertEquals(data.length, tarArchiveOutputStream.getCurrSize());
    }

    @Test
    public void testFinish() throws IOException {
        OutputStream os = new ByteArrayOutputStream();
        TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(os);
        TarArchiveEntry tarArchiveEntry = new TarArchiveEntry("test.txt");
        tarArchiveOutputStream.putArchiveEntry(tarArchiveEntry);
        byte[] data = "Hello World".getBytes();
        tarArchiveOutputStream.write(data, 0, data.length);
        tarArchiveOutputStream.finish();

        ByteArrayInputStream inputStream = new ByteArrayInputStream(os.toByteArray());
        ArchiveInputStream archiveInputStream = new TarFile(inputStream);
        ArchiveEntry archiveEntry = archiveInputStream.getNextEntry();
        assertNotNull(archiveEntry);
        assertEquals("test.txt", archiveEntry.getName());
        assertEquals(512, archiveEntry.getSize());
    }

    @Test
    public void testCloseArchiveEntryAfterFinish() throws IOException {
        OutputStream os = new ByteArrayOutputStream();
        TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(os);
        TarArchiveEntry tarArchiveEntry = new TarArchiveEntry("test.txt");
        tarArchiveOutputStream.putArchiveEntry(tarArchiveEntry);
        byte[] data = "Hello World".getBytes();
        tarArchiveOutputStream.write(data, 0, data.length);
        tarArchiveOutputStream.finish();
        tarArchiveOutputStream.closeArchiveEntry();

        ByteArrayInputStream inputStream = new ByteArrayInputStream(os.toByteArray());
        ArchiveInputStream archiveInputStream = new TarFile(inputStream);
        ArchiveEntry archiveEntry = archiveInputStream.getNextEntry();
        assertNotNull(archiveEntry);
        assertEquals("test.txt", archiveEntry.getName());
        assertEquals(512, archiveEntry.getSize());
    }

    @Test
    public void testCloseArchiveEntryAfterFinishAndCloseOutputStream() throws IOException {
        OutputStream os = new ByteArrayOutputStream();
        TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(os);
        TarArchiveEntry tarArchiveEntry = new TarArchiveEntry("test.txt");
        tarArchiveOutputStream.putArchiveEntry(tarArchiveEntry);
        byte[] data = "Hello World".getBytes();
        tarArchiveOutputStream.write(data, 0, data.length);
        tarArchiveOutputStream.finish();
        tarArchiveOutputStream.close();

        ByteArrayInputStream inputStream = new ByteArrayInputStream(os.toByteArray());
        ArchiveInputStream archiveInputStream = new TarFile(inputStream);
        ArchiveEntry archiveEntry = archiveInputStream.getNextEntry();
        assertNotNull(archiveEntry);
        assertEquals("test.txt", archiveEntry.getName());
        assertEquals(512, archiveEntry.getSize());
    }

    @Test
    public void testWrite() throws IOException {
        OutputStream os = new ByteArrayOutputStream();
        TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(os);
        TarArchiveEntry tarArchiveEntry = new TarArchiveEntry("test.txt");
        tarArchiveOutputStream.putArchiveEntry(tarArchiveEntry);
        byte[] data = "Hello World".getBytes();
        tarArchiveOutputStream.write(data, 0, data.length);
        tarArchiveOutputStream.closeArchiveEntry();

        assertEquals(data, os.toByteArray());
    }

    @Test
    public void testWriteWithLongFileName() throws IOException {
        OutputStream os = new ByteArrayOutputStream();
        TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(os);
        TarArchiveEntry tarArchiveEntry = new TarArchiveEntry("test/longFileName.txt");
        tarArchiveOutputStream.putArchiveEntry(tarArchiveEntry);
        byte[] data = "Hello World".getBytes();
        tarArchiveOutputStream.write(data, 0, data.length);
        tarArchiveOutputStream.closeArchiveEntry();

        ByteArrayInputStream inputStream = new ByteArrayInputStream(os.toByteArray());
        ArchiveInputStream archiveInputStream = new TarFile(inputStream);
        ArchiveEntry archiveEntry = archiveInputStream.getNextEntry();
        assertNotNull(archiveEntry);
        assertEquals("test/longFileName.txt", archiveEntry.getName());
        assertEquals(512 + data.length, archiveEntry.getSize());

        byte[] readData = new byte[archiveEntry.getSize()];
        int length = inputStream.read(readData);
        assertArrayEquals(data, Arrays.copyOfRange(readData, 512, length));
    }

    @Test
    public void testWriteWithLongFileNameAndTruncation() throws IOException {
        OutputStream os = new ByteArrayOutputStream();
        TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(os);
        TarArchiveEntry tarArchiveEntry = new TarArchiveEntry("test/longFileNameThatWillBeTruncated.txt");
        tarArchiveOutputStream.setLongFileMode(TarArchiveOutputStream.LONGFILE_TRUNCATE);
        tarArchiveOutputStream.putArchiveEntry(tarArchiveEntry);
        byte[] data = "Hello World".getBytes();
        tarArchiveOutputStream.write(data, 0, data.length);
        tarArchiveOutputStream.closeArchiveEntry();

        ByteArrayInputStream inputStream = new ByteArrayInputStream(os.toByteArray());
        ArchiveInputStream archiveInputStream = new TarFile(inputStream);
        ArchiveEntry archiveEntry = archiveInputStream.getNextEntry();
        assertNotNull(archiveEntry);
        assertEquals("test/longFileNameThatWillBeTruncated.", archiveEntry.getName());
        assertEquals(512, archiveEntry.getSize());

        byte[] readData = new byte[archiveEntry.getSize()];
        int length = inputStream.read(readData);
        assertArrayEquals(data, Arrays.copyOfRange(readData, 512, length));
    }

    @Test
    public void testSetLongFileMode() {
        OutputStream os = new ByteArrayOutputStream();
        TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(os);

        tarArchiveOutputStream.setLongFileMode(TarArchiveOutputStream.LONGFILE_ERROR);
        TarArchiveEntry tarArchiveEntry = new TarArchiveEntry("test/longFileName.txt");
        tarArchiveOutputStream.putArchiveEntry(tarArchiveEntry);

        assertArrayEquals(new byte[]{"54", "68", "69", "73", "20", "74", "68", "65", "20", "63", "61", "6D", "65", "6E", "74", "65", "72", "6E", "65", "00", "00", "00", "01", "test/", "longFileName.", "\n"}, tarArchiveOutputStream.getRecordBuf());

        tarArchiveOutputStream.setLongFileMode(TarArchiveOutputStream.LONGFILE_TRUNCATE);
        tarArchiveEntry = new TarArchiveEntry("test/longFileName.txt");
        tarArchiveOutputStream.putArchiveEntry(tarArchiveEntry);

        assertArrayEquals(new byte[]{"54", "68", "69", "73", "20", "74", "68", "65", "20", "63", "61", "6D", "65", "6E", "74", "65", "72", "6E", "65", "00", "00", "00", "01", "test/", "longFileName.", "\0"}, tarArchiveOutputStream.getRecordBuf());

        tarArchiveOutputStream.setLongFileMode(TarArchiveOutputStream.LONGFILE_GNU);
        tarArchiveEntry = new TarArchiveEntry("test/longFileName.txt");
        tarArchiveOutputStream.putArchiveEntry(tarArchiveEntry);

        assertArrayEquals(new byte[]{"54", "68", "69", "73", "20", "74", "68", "65", "20", "63", "61", "6D", "65", "6E", "74", "65", "72", "6E", "65", "00", "00", "00", "01", "test/", "longFileName.", "00"}, tarArchiveOutputStream.getRecordBuf());
    }
}
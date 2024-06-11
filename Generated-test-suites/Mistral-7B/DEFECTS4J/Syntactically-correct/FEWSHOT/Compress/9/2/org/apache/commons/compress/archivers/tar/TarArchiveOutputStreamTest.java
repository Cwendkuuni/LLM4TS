package org.apache.commons.compress.archivers.tar;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.ArchiveOutputStream;
import org.apache.commons.compress.utils.ArchiveUtils;
import org.apache.commons.compress.utils.CountingOutputStream;
import org.junit.Test;

public class TarArchiveOutputStreamTest {

    @Test
    public void testFinish() throws IOException {
        OutputStream outputStream = new CountingOutputStream(new ByteArrayOutputStream());
        TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(outputStream);
        tarArchiveOutputStream.finish();
        tarArchiveOutputStream.close();

        byte[] expectedEOF = new byte[512 * 2];
        assertArrayEquals(expectedEOF, outputStream.getCountData());
    }

    @Test
    public void testClose() throws IOException {
        OutputStream outputStream = new CountingOutputStream(new ByteArrayOutputStream());
        TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(outputStream);
        tarArchiveOutputStream.close();

        byte[] expectedEOF = new byte[512 * 2];
        assertArrayEquals(expectedEOF, outputStream.getCountData());
    }

    @Test
    public void testPutArchiveEntry() throws IOException {
        OutputStream outputStream = new CountingOutputStream(new ByteArrayOutputStream());
        TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(outputStream);

        ArchiveEntry archiveEntry = new TarArchiveEntry("test.txt");
        tarArchiveOutputStream.putArchiveEntry(archiveEntry);

        byte[] expectedEntryHeader = ArchiveUtils.toBytes("test.txt\0\0*\020\0\0\0\0\0\0"
                + (char) 0x00 + (char) 0x20);
        assertArrayEquals(expectedEntryHeader, outputStream.getCountData());
    }

    @Test
    public void testPutArchiveEntryLongFileName() throws IOException {
        OutputStream outputStream = new CountingOutputStream(new ByteArrayOutputStream());
        TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(outputStream);
        tarArchiveOutputStream.setLongFileMode(TarArchiveOutputStream.LONGFILE_GNU);

        ArchiveEntry archiveEntry = new TarArchiveEntry("test/long/file/name.txt");
        tarArchiveOutputStream.putArchiveEntry(archiveEntry);

        byte[] expectedLongLinkHeader = { 0, 'T', 'a', 'r', 0, '0', 'L', 'F', ' ', 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 4, 0, '\u0000', 'l', 'o', 'n', 'g', ' ', 'f', 'i', 'l', 'e', '/', 'n', 'a', 'm', 'e', '.', 't', 'x', 't', '\u0000' };
        assertArrayEquals(expectedLongLinkHeader, outputStream.getCountData());

        byte[] expectedEntryHeader = ArchiveUtils.toBytes("test/long/file/name.txt\0\0*\020\0\0\0\0\0\0"
                + (char) 0x00 + (char) 0x20);
        assertArrayEquals(expectedEntryHeader, outputStream.getData(outputStream.getCount() - expectedEntryHeader.length, expectedEntryHeader.length));
    }

    @Test
    public void testPutArchiveEntryWithInvalidLongFileNameMode() throws IOException {
        OutputStream outputStream = new CountingOutputStream(new ByteArrayOutputStream());
        TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(outputStream);

        ArchiveEntry archiveEntry = new TarArchiveEntry("test/long/file/name.txt");
        tarArchiveOutputStream.putArchiveEntry(archiveEntry);

        assertThrows(RuntimeException.class, () -> tarArchiveOutputStream.setLongFileMode(TarArchiveOutputStream.LONGFILE_INVALID));
    }

    @Test
    public void testPutArchiveEntryWithTooLongFileName() throws IOException {
        OutputStream outputStream = new CountingOutputStream(new ByteArrayOutputStream());
        TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(outputStream);
        tarArchiveOutputStream.setLongFileMode(TarArchiveOutputStream.LONGFILE_TRUNCATE);

        ArchiveEntry archiveEntry = new TarArchiveEntry("test/long/file/name/that/is/too/long.txt");
        tarArchiveOutputStream.putArchiveEntry(archiveEntry);

        assertThrows(RuntimeException.class, () -> tarArchiveOutputStream.putArchiveEntry(archiveEntry));
    }

    @Test
    public void testCloseArchiveEntry() throws IOException {
        OutputStream outputStream = new CountingOutputStream(new ByteArrayOutputStream());
        TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(outputStream);

        ArchiveEntry archiveEntry = new TarArchiveEntry("test.txt");
        tarArchiveOutputStream.putArchiveEntry(archiveEntry);
        tarArchiveOutputStream.write(new byte[]{'H', 'e', 'l', 'l', 'o'});
        tarArchiveOutputStream.closeArchiveEntry();

        byte[] expectedData = new byte[]{'H', 'e', 'l', 'l', 'o'};
        assertArrayEquals(expectedData, outputStream.getData(outputStream.getCount() - expectedData.length, expectedData.length));
    }

    @Test
    public void testWrite() throws IOException {
        OutputStream outputStream = new CountingOutputStream(new ByteArrayOutputStream());
        TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(outputStream);

        ArchiveEntry archiveEntry = new TarArchiveEntry("test.txt");
        tarArchiveOutputStream.putArchiveEntry(archiveEntry);

        byte[] data = new byte[]{'H', 'e', 'l', 'l', 'o'};
        tarArchiveOutputStream.write(data);

        byte[] expectedData = new byte[]{'H', 'e', 'l', 'l', 'o'};
        assertArrayEquals(expectedData, outputStream.getData(outputStream.getCount() - expectedData.length, expectedData.length));
    }

    @Test
    public void testWriteWithTooMuchData() throws IOException {
        OutputStream outputStream = new CountingOutputStream(new ByteArrayOutputStream());
        TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(outputStream);

        ArchiveEntry archiveEntry = new TarArchiveEntry("test.txt");
        tarArchiveOutputStream.putArchiveEntry(archiveEntry);

        byte[] data = new byte[1024];
        tarArchiveOutputStream.write(data);

        byte[] expectedData = new byte[512];
        assertArrayEquals(expectedData, outputStream.getData(outputStream.getCount() - expectedData.length, expectedData.length));
    }

    @Test
    public void testSetLongFileMode() throws IOException {
        OutputStream outputStream = new CountingOutputStream(new ByteArrayOutputStream());
        TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(outputStream);

        tarArchiveOutputStream.setLongFileMode(TarArchiveOutputStream.LONGFILE_TRUNCATE);
    }

    @Test
    public void testSetLongFileModeWithInvalidMode() throws IOException {
        OutputStream outputStream = new CountingOutputStream(new ByteArrayOutputStream());
        TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(outputStream);

        assertThrows(RuntimeException.class, () -> tarArchiveOutputStream.setLongFileMode(TarArchiveOutputStream.LONGFILE_INVALID));
    }

    @Test
    public void testCreateArchiveEntry() throws IOException {
        OutputStream outputStream = new CountingOutputStream(new ByteArrayOutputStream());
        TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(outputStream);

        File file = new File("test.txt");
        ArchiveEntry archiveEntry = tarArchiveOutputStream.createArchiveEntry(file, "test.txt");
        tarArchiveOutputStream.putArchiveEntry(archiveEntry);

        byte[] expectedEntryHeader = ArchiveUtils.toBytes("test.txt\0\0*\020\0\0\0\0\0\0"
                + (char) 0x00 + (char) 0x20);
        assertArrayEquals(expectedEntryHeader, outputStream.getCountData());
    }
}
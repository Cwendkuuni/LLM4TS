package org.apache.commons.compress.archivers.tar;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.ArchiveOutputStream;
import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarConstants;

public class TarArchiveOutputStreamTest {

    @Test
    public void testEmptyConstruction() {
        TarArchiveOutputStream tarOutputStream = new TarArchiveOutputStream(new ByteArrayOutputStream());
        assertNull(tarOutputStream.getCurrentName());
        assertTrue(tarOutputStream.isClosed());
    }

    @Test
    public void testBasicConstruction() throws IOException {
        OutputStream outputStream = new ByteArrayOutputStream();
        TarArchiveOutputStream tarOutputStream = new TarArchiveOutputStream(outputStream);
        assertNull(tarOutputStream.getCurrentName());
        assertFalse(tarOutputStream.isClosed());
    }

    @Test
    public void testBlockSizeAndRecordSizeConstruction() throws IOException {
        OutputStream outputStream = new ByteArrayOutputStream();
        int blockSize = 1024;
        int recordSize = 2048;
        TarArchiveOutputStream tarOutputStream = new TarArchiveOutputStream(outputStream, blockSize, recordSize);
        assertNull(tarOutputStream.getCurrentName());
        assertFalse(tarOutputStream.isClosed());
        assertEquals(blockSize, tarOutputStream.getBlockSize());
        assertEquals(recordSize, tarOutputStream.getRecordSize());
    }

    @Test
    public void testSetLongFileMode() throws IOException {
        OutputStream outputStream = new ByteArrayOutputStream();
        TarArchiveOutputStream tarOutputStream = new TarArchiveOutputStream(outputStream);
        tarOutputStream.setLongFileMode(TarArchiveOutputStream.LONGFILE_GNU);
        assertEquals(TarArchiveOutputStream.LONGFILE_GNU, tarOutputStream.getLongFileMode());

        tarOutputStream.setLongFileMode(TarArchiveOutputStream.LONGFILE_TRUNCATE);
        assertEquals(TarArchiveOutputStream.LONGFILE_TRUNCATE, tarOutputStream.getLongFileMode());

        tarOutputStream.setLongFileMode(TarArchiveOutputStream.LONGFILE_ERROR);
        assertEquals(TarArchiveOutputStream.LONGFILE_ERROR, tarOutputStream.getLongFileMode());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidLongFileMode() throws IOException {
        OutputStream outputStream = new ByteArrayOutputStream();
        TarArchiveOutputStream tarOutputStream = new TarArchiveOutputStream(outputStream);
        tarOutputStream.setLongFileMode(123);
    }

    @Test
    public void testFinish() throws IOException {
        OutputStream outputStream = new ByteArrayOutputStream();
        TarArchiveOutputStream tarOutputStream = new TarArchiveOutputStream(outputStream);
        tarOutputStream.finish();
        ByteArrayOutputStream byteArrayOutputStream = (ByteArrayOutputStream) outputStream;
        byte[] data = byteArrayOutputStream.toByteArray();
        // EOF record size is 512 bytes
        assertTrue(data.length > 512 * 2);
    }

    @Test
    public void testClose() throws IOException {
        OutputStream outputStream = new ByteArrayOutputStream();
        TarArchiveOutputStream tarOutputStream = new TarArchiveOutputStream(outputStream);
        tarOutputStream.close();
        assertTrue(tarOutputStream.isClosed());
        ByteArrayOutputStream byteArrayOutputStream = (ByteArrayOutputStream) outputStream;
        byte[] data = byteArrayOutputStream.toByteArray();
        // EOF record size is 512 bytes
        assertTrue(data.length > 512 * 2);
    }

    @Test(expected = IOException.class)
    public void testPutArchiveEntryWithNullArchiveEntry() throws IOException {
        OutputStream outputStream = new ByteArrayOutputStream();
        TarArchiveOutputStream tarOutputStream = new TarArchiveOutputStream(outputStream);
        tarOutputStream.putArchiveEntry(null);
    }

    @Test(expected = ClassCastException.class)
    public void testPutArchiveEntryWithNonTarArchiveEntry() throws IOException {
        OutputStream outputStream = new ByteArrayOutputStream();
        TarArchiveOutputStream tarOutputStream = new TarArchiveOutputStream(outputStream);
        tarOutputStream.putArchiveEntry(new File("test.txt"));
    }

    @Test
    public void testPutArchiveEntryWithLongFileName() throws IOException {
        OutputStream outputStream = new ByteArrayOutputStream();
        TarArchiveOutputStream tarOutputStream = new TarArchiveOutputStream(outputStream);
        String longFileName = "a" + new String(new char[TarConstants.NAMELEN*2]) + "b";
        File file = new File("test.txt");
        TarArchiveEntry archiveEntry = new TarArchiveEntry(file, longFileName);
        tarOutputStream.putArchiveEntry(archiveEntry);
        tarOutputStream.closeArchiveEntry();
        ByteArrayOutputStream byteArrayOutputStream = (ByteArrayOutputStream) outputStream;
        byte[] data = byteArrayOutputStream.toByteArray();
        assertTrue(data.length > 512 * 2);
    }

    @Test
    public void testPutArchiveEntryWithShortFileName() throws IOException {
        OutputStream outputStream = new ByteArrayOutputStream();
        TarArchiveOutputStream tarOutputStream = new TarArchiveOutputStream(outputStream);
        File file = new File("test.txt");
        TarArchiveEntry archiveEntry = new TarArchiveEntry(file, "shortFileName");
        tarOutputStream.putArchiveEntry(archiveEntry);
        tarOutputStream.closeArchiveEntry();
        ByteArrayOutputStream byteArrayOutputStream = (ByteArrayOutputStream) outputStream;
        byte[] data = byteArrayOutputStream.toByteArray();
        // EOF record size is 512 bytes
        assertTrue(data.length > 512 * 2);
    }

    @Test
    public void testWrite() throws IOException {
        OutputStream outputStream = new ByteArrayOutputStream();
        TarArchiveOutputStream tarOutputStream = new TarArchiveOutputStream(outputStream);
        File file = new File("test.txt");
        TarArchiveEntry archiveEntry = new TarArchiveEntry(file, "shortFileName");
        tarOutputStream.putArchiveEntry(archiveEntry);
        byte[] dataToWrite = new byte[]{'a', 'b', 'c'};
        tarOutputStream.write(dataToWrite, 0, dataToWrite.length);
        tarOutputStream.closeArchiveEntry();
        ByteArrayOutputStream byteArrayOutputStream = (ByteArrayOutputStream) outputStream;
        byte[] data = byteArrayOutputStream.toByteArray();
        // EOF record size is 512 bytes
        byte[] expectedData = new byte[]{'a', 'b', 'c', (byte) 0};
        assertEquals(expectedData.length, data.length);
        for (int i = 0; i < expectedData.length; i++) {
            assertEquals(expectedData[i], data[i]);
        }
    }

    @Test
    public void testCloseArchiveEntry() throws IOException {
        OutputStream outputStream = new ByteArrayOutputStream();
        TarArchiveOutputStream tarOutputStream = new TarArchiveOutputStream(outputStream);
        File file = new File("test.txt");
        TarArchiveEntry archiveEntry = new TarArchiveEntry(file, "shortFileName");
        tarOutputStream.putArchiveEntry(archiveEntry);
        tarOutputStream.write(new byte[]{'a', 'b', 'c'}, 0, 3);
        tarOutputStream.closeArchiveEntry();
        ByteArrayOutputStream byteArrayOutputStream = (ByteArrayOutputStream) outputStream;
        byte[] data = byteArrayOutputStream.toByteArray();
        // EOF record size is 512 bytes
        byte[] expectedData = new byte[]{'a', 'b', 'c', (byte) 0};
        assertEquals(expectedData.length, data.length);
        for (int i = 0; i < expectedData.length; i++) {
            assertEquals(expectedData[i], data[i]);
        }
    }

    @Test
    public void testPutArchiveEntryTwice() throws IOException {
        OutputStream outputStream = new ByteArrayOutputStream();
        TarArchiveOutputStream tarOutputStream = new TarArchiveOutputStream(outputStream);
        File file = new File("test.txt");
        TarArchiveEntry archiveEntry = new TarArchiveEntry(file, "shortFileName");
        tarOutputStream.putArchiveEntry(archiveEntry);
        tarOutputStream.write(new byte[]{'a', 'b', 'c'}, 0, 3);
        tarOutputStream.closeArchiveEntry();
        tarOutputStream.putArchiveEntry(archiveEntry);
        tarOutputStream.write(new byte[]{'d', 'e', 'f'}, 0, 3);
        tarOutputStream.closeArchiveEntry();
        ByteArrayOutputStream byteArrayOutputStream = (ByteArrayOutputStream) outputStream;
        byte[] data = byteArrayOutputStream.toByteArray();
        // EOF record size is 512 bytes
        byte[] expectedData = new byte[]{'a', 'b', 'c', (byte) 0, 'a', 'b', 'c', (byte) 0, 'd', 'e', 'f', (byte) 0};
        assertEquals(expectedData.length, data.length);
        for (int i = 0; i < expectedData.length; i++) {
            assertEquals(expectedData[i], data[i]);
        }
    }
}
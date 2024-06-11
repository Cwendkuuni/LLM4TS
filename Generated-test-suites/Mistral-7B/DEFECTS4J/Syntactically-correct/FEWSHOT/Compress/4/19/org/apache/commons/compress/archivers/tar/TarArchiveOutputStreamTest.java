package org.apache.commons.compress.archivers.tar;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.ArchiveOutputStream;
import org.apache.commons.compress.archivers.TarConstants;
import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveOutputStream;
import org.apache.commons.compress.archivers.tar.TarBuffer;

public class TarArchiveOutputStreamTest {

    @Test
    public void testFinishAndClose() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(outputStream, 512, 512);
        tarArchiveOutputStream.finish();
        tarArchiveOutputStream.close();
        byte[] expectedEOF = new byte[TarConstants.RECORD_SIZE];
        assertArrayEquals(expectedEOF, outputStream.toByteArray(), 0, TarConstants.RECORD_SIZE);
    }

    @Test
    public void testSetLongFileMode() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(outputStream, 512, 512);
        tarArchiveOutputStream.setLongFileMode(TarArchiveOutputStream.LONGFILE_TRUNCATE);
        TarArchiveEntry tarArchiveEntry = new TarArchiveEntry("testLongFileName.txt");
        tarArchiveOutputStream.putArchiveEntry(tarArchiveEntry);
        tarArchiveOutputStream.write("Hello World!".getBytes());
        tarArchiveOutputStream.closeArchiveEntry();
        tarArchiveOutputStream.finish();
        tarArchiveOutputStream.close();
        byte[] expectedRecord = new byte[TarConstants.RECORD_SIZE];
        byte[] expectedName = "testLongFileName.txt\0".getBytes();
        System.arraycopy(expectedName, 0, expectedRecord, 0, TarConstants.NAME_LENGTH);
        assertArrayEquals(expectedRecord, outputStream.toByteArray(), 0, TarConstants.RECORD_SIZE);

        ByteArrayOutputStream outputStream2 = new ByteArrayOutputStream();
        TarArchiveOutputStream tarArchiveOutputStream2 = new TarArchiveOutputStream(outputStream2, 512, 512);
        tarArchiveOutputStream2.setLongFileMode(TarArchiveOutputStream.LONGFILE_GNU);
        TarArchiveEntry tarArchiveEntry2 = new TarArchiveEntry("testLongFileName.txt");
        tarArchiveOutputStream2.putArchiveEntry(tarArchiveEntry2);
        tarArchiveOutputStream2.write("Hello World!".getBytes());
        tarArchiveOutputStream2.closeArchiveEntry();
        byte[] longLinkHeader = new byte[TarConstants.RECORD_SIZE];
        byte[] longLinkName = "testLongFileName.txt\0".getBytes();
        System.arraycopy(longLinkName, 0, longLinkHeader, 0, TarConstants.NAME_LENGTH);
        longLinkHeader[12] = TarConstants.GNU_LONGLINK;
        longLinkHeader[13] = TarConstants.LF_GNUTYPE_LONGNAME;
        assertArrayEquals(longLinkHeader, outputStream2.toByteArray(), 0, TarConstants.RECORD_SIZE);
        tarArchiveOutputStream2.finish();
        tarArchiveOutputStream2.close();
    }

    @Test
    public void testPutArchiveEntry() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(outputStream, 512, 512);
        TarArchiveEntry tarArchiveEntry = new TarArchiveEntry("testFile.txt");
        tarArchiveOutputStream.putArchiveEntry(tarArchiveEntry);
        tarArchiveOutputStream.write("Hello World!".getBytes());
        tarArchiveOutputStream.closeArchiveEntry();
        byte[] expectedRecord = new byte[TarConstants.RECORD_SIZE];
        byte[] expectedName = "testFile.txt\0".getBytes();
        System.arraycopy(expectedName, 0, expectedRecord, 0, TarConstants.NAME_LENGTH);
        assertArrayEquals(expectedRecord, outputStream.toByteArray(), 0, TarConstants.RECORD_SIZE);

        ByteArrayOutputStream outputStream2 = new ByteArrayOutputStream();
        TarArchiveOutputStream tarArchiveOutputStream2 = new TarArchiveOutputStream(outputStream2, 512, 512);
        TarArchiveEntry tarArchiveEntry2 = new TarArchiveEntry("testDir/testFile.txt");
        tarArchiveOutputStream2.putArchiveEntry(tarArchiveEntry2);
        tarArchiveOutputStream2.write("Hello World!".getBytes());
        tarArchiveOutputStream2.closeArchiveEntry();
        byte[] expectedRecord2 = new byte[TarConstants.RECORD_SIZE];
        byte[] expectedName2 = "testDir/\0testFile.txt\0".getBytes();
        System.arraycopy(expectedName2, 0, expectedRecord2, 0, TarConstants.RECORD_SIZE);
        assertArrayEquals(expectedRecord2, outputStream2.toByteArray(), 0, TarConstants.RECORD_SIZE);
    }

    @Test
    public void testCloseArchiveEntry() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(outputStream, 512, 512);
        TarArchiveEntry tarArchiveEntry = new TarArchiveEntry("testFile.txt");
        tarArchiveOutputStream.putArchiveEntry(tarArchiveEntry);
        tarArchiveOutputStream.write("Hello World!".getBytes());
        tarArchiveOutputStream.closeArchiveEntry();
        byte[] expectedRecord = new byte[TarConstants.RECORD_SIZE];
        byte[] expectedName = "testFile.txt\0".getBytes();
        System.arraycopy(expectedName, 0, expectedRecord, 0, TarConstants.NAME_LENGTH);
        assertArrayEquals(expectedRecord, outputStream.toByteArray(), 0, TarConstants.RECORD_SIZE);

        byte[] expectedData = "Hello World!".getBytes();
        assertEquals(expectedData.length, outputStream.size());
        assertFalse(outputStream.hasRemaining());
    }

    @Test
    public void testWrite() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(outputStream, 512, 512);
        byte[] data = "Hello World!".getBytes();
        tarArchiveOutputStream.write(data);
        byte[] expectedRecord = new byte[TarConstants.RECORD_SIZE];
        byte[] expectedName = "testFile.txt\0".getBytes();
        System.arraycopy(expectedName, 0, expectedRecord, 0, TarConstants.NAME_LENGTH);
        assertArrayEquals(expectedRecord, outputStream.toByteArray(), 0, TarConstants.RECORD_SIZE);
        assertArrayEquals(data, new ByteArrayInputStream(outputStream.toByteArray()).readAllBytes());
    }

    @Test
    public void testWriteMultipleBlocks() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(outputStream, 512, 512);
        byte[] data = "Hello World!".getBytes();
        int blockSize = 128;
        for (int i = 0; i < data.length; i += blockSize) {
            int bytesToWrite = Math.min(data.length - i, blockSize);
            tarArchiveOutputStream.write(data, i, bytesToWrite);
        }
        byte[] expectedRecord = new byte[TarConstants.RECORD_SIZE];
        byte[] expectedName = "testFile.txt\0".getBytes();
        System.arraycopy(expectedName, 0, expectedRecord, 0, TarConstants.NAME_LENGTH);
        assertArrayEquals(expectedRecord, outputStream.toByteArray(), 0, TarConstants.RECORD_SIZE);
        assertArrayEquals(data, new ByteArrayInputStream(outputStream.toByteArray()).readAllBytes());
    }

    @Test
    public void testWriteLongFileName() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(outputStream, 512, 512);
        TarArchiveEntry tarArchiveEntry = new TarArchiveEntry("testLongFileName.txt");
        tarArchiveOutputStream.putArchiveEntry(tarArchiveEntry);
        tarArchiveOutputStream.write("Hello World!".getBytes());
        tarArchiveOutputStream.closeArchiveEntry();

        if (tarArchiveOutputStream.longFileMode == TarArchiveOutputStream.LONGFILE_TRUNCATE) {
            throw new RuntimeException("Long file names are not truncated.");
        }

        byte[] expectedRecord = new byte[TarConstants.RECORD_SIZE];
        byte[] expectedName = "testLongFileName.txt\0".getBytes();
        System.arraycopy(expectedName, 0, expectedRecord, 0, TarConstants.NAME_LENGTH);
        assertArrayEquals(expectedRecord, outputStream.toByteArray(), 0, TarConstants.RECORD_SIZE);
    }

    @Test(expected = RuntimeException.class)
    public void testWriteLongFileNameWithTruncation() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(outputStream, 512, 512);
        TarArchiveEntry tarArchiveEntry = new TarArchiveEntry("testLongFileName.txt");
        tarArchiveOutputStream.putArchiveEntry(tarArchiveEntry);
        tarArchiveOutputStream.setLongFileMode(TarArchiveOutputStream.LONGFILE_TRUNCATE);
        tarArchiveOutputStream.write("Hello World!".getBytes());
        tarArchiveOutputStream.closeArchiveEntry();
    }

    @Test
    public void testClose() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(outputStream, 512, 512);
        tarArchiveOutputStream.close();
        assertNull(tarArchiveOutputStream.getOutputStream());
        assertTrue(tarArchiveOutputStream.isClosed());
    }
}
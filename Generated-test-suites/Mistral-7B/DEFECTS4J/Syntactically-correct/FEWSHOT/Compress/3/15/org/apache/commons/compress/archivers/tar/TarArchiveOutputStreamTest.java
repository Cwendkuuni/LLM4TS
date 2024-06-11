package org.apache.commons.compress.archivers.tar;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.ArchiveOutputStream;
import org.apache.commons.compress.archivers.TarArchiveEntry;
import org.apache.commons.compress.utils.IOUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class TarArchiveOutputStreamTest {
    private OutputStream outputStream;
    private TarArchiveOutputStream tarArchiveOutputStream;

    @Before
    public void setUp() throws IOException {
        outputStream = new ByteArrayOutputStream();
        tarArchiveOutputStream = new TarArchiveOutputStream(outputStream, 512, 512);
    }

    @After
    public void tearDown() throws IOException {
        tarArchiveOutputStream.close();
        IOUtils.closeQuietly(outputStream);
    }

    @Test
    public void testConstructors() {
        TarArchiveOutputStream tarArchiveOutputStream1 = new TarArchiveOutputStream(new ByteArrayOutputStream());
        TarArchiveOutputStream tarArchiveOutputStream2 = new TarArchiveOutputStream(new ByteArrayOutputStream(), 512);
        TarArchiveOutputStream tarArchiveOutputStream3 = new TarArchiveOutputStream(new ByteArrayOutputStream(), 512, 512);
    }

    @Test(expected = ClassCastException.class)
    public void testPutArchiveEntryWithInvalidArchiveEntryType() throws IOException {
        tarArchiveOutputStream.putArchiveEntry(new Object());
    }

    @Test
    public void testSetLongFileMode() throws IOException {
        tarArchiveOutputStream.setLongFileMode(TarArchiveOutputStream.LONGFILE_TRUNCATE);
        TarArchiveEntry tarArchiveEntry = new TarArchiveEntry("test.txt");
        tarArchiveOutputStream.putArchiveEntry(tarArchiveEntry);
        assertTrue(tarArchiveOutputStream.isLongFileTruncated());

        tarArchiveOutputStream.setLongFileMode(TarArchiveOutputStream.LONGFILE_GNU);
        tarArchiveEntry = new TarArchiveEntry("test.txt", "long name test".getBytes());
        tarArchiveOutputStream.putArchiveEntry(tarArchiveEntry);
        assertFalse(tarArchiveOutputStream.isLongFileTruncated());
    }

    @Test
    public void testFinish() throws IOException {
        TarArchiveEntry tarArchiveEntry = new TarArchiveEntry("test.txt");
        tarArchiveOutputStream.putArchiveEntry(tarArchiveEntry);
        tarArchiveOutputStream.write("Hello World!".getBytes());
        tarArchiveOutputStream.closeArchiveEntry();
        tarArchiveOutputStream.finish();

        ByteArrayInputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
        ArchiveOutputStream archiveOutputStream = new TarArchiveOutputStream(inputStream);
        ArchiveEntry archiveEntry = archiveOutputStream.getNextEntry();
        TarArchiveEntry testArchiveEntry = (TarArchiveEntry) archiveEntry;
        byte[] expectedHeader = new byte[512]; // the header bytes
        byte[] expectedEntryData = "Hello World!".getBytes();
        byte[] expectedEOFData = new byte[512]; // the EOF bytes

        testReadHeader(archiveEntry, expectedHeader);
        testReadEntryData(archiveEntry, expectedEntryData);
        testReadEOF(archiveEntry, expectedEOFData);
    }

    @Test
    public void testClose() throws IOException {
        TarArchiveEntry tarArchiveEntry = new TarArchiveEntry("test.txt");
        tarArchiveOutputStream.putArchiveEntry(tarArchiveEntry);
        tarArchiveOutputStream.write("Hello World!".getBytes());
        tarArchiveOutputStream.closeArchiveEntry();
        tarArchiveOutputStream.close();

        ByteArrayInputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
        ArchiveOutputStream archiveOutputStream = new TarArchiveOutputStream(inputStream);
        ArchiveEntry archiveEntry = archiveOutputStream.getNextEntry();
        TarArchiveEntry testArchiveEntry = (TarArchiveEntry) archiveEntry;
        byte[] expectedHeader = new byte[512]; // the header bytes
        byte[] expectedEntryData = "Hello World!".getBytes();
        byte[] expectedEOFData = new byte[512]; // the EOF bytes

        testReadHeader(archiveEntry, expectedHeader);
        testReadEntryData(archiveEntry, expectedEntryData);
        testReadEOF(archiveEntry, expectedEOFData);
    }

    @Test(expected = IOException.class)
    public void testWriteBeyondSize() throws IOException {
        TarArchiveEntry tarArchiveEntry = new TarArchiveEntry("test.txt");
        tarArchiveOutputStream.putArchiveEntry(tarArchiveEntry);
        tarArchiveOutputStream.write("Hello World!".getBytes(), 0, "Hello World!".length() + 1);
    }

    @Test(expected = IOException.class)
    public void testWriteBeforePuttingAnEntry() throws IOException {
        TarArchiveOutputStream tarArchiveOutputStream1 = new TarArchiveOutputStream(outputStream, 512, 512);
        tarArchiveOutputStream1.write("Hello World!".getBytes());
    }

    @Test
    public void testWrite() throws IOException {
        TarArchiveEntry tarArchiveEntry = new TarArchiveEntry("test.txt");
        tarArchiveOutputStream.putArchiveEntry(tarArchiveEntry);
        tarArchiveOutputStream.write("Hello World!".getBytes());
        tarArchiveOutputStream.closeArchiveEntry();
        byte[] expectedEntryData = "Hello World!".getBytes();

        ByteArrayInputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
        ArchiveOutputStream archiveOutputStream = new TarArchiveOutputStream(inputStream);
        ArchiveEntry archiveEntry = archiveOutputStream.getNextEntry();
        TarArchiveEntry testArchiveEntry = (TarArchiveEntry) archiveEntry;

        byte[] readEntryData = new byte[testArchiveEntry.getSize()];
        archiveOutputStream.read(readEntryData);

        assertArrayEquals(expectedEntryData, readEntryData);
    }

    private void testReadHeader(ArchiveEntry archiveEntry, byte[] expectedHeader) throws IOException {
        TarArchiveEntry tarArchiveEntry = (TarArchiveEntry) archiveEntry;
        byte[] readHeader = new byte[expectedHeader.length];
        TarBuffer tarBuffer = tarArchiveEntry.getTarBuffer();

        tarBuffer.readRecord(readHeader);

        assertArrayEquals(expectedHeader, readHeader);
    }

    private void testReadEntryData(ArchiveEntry archiveEntry, byte[] expectedEntryData) throws IOException {
        TarArchiveEntry tarArchiveEntry = (TarArchiveEntry) archiveEntry;
        byte[] readEntryData = new byte[tarArchiveEntry.getSize()];
        TarBuffer tarBuffer = tarArchiveEntry.getTarBuffer();

        int length = tarBuffer.read(readEntryData, 0, readEntryData.length);

        assertArrayEquals(expectedEntryData, readEntryData);
    }

    private void testReadEOF(ArchiveEntry archiveEntry, byte[] expectedEOFData) throws IOException {
        TarArchiveEntry tarArchiveEntry = (TarArchiveEntry) archiveEntry;
        byte[] readEOFData = new byte[expectedEOFData.length];
        TarBuffer tarBuffer = tarArchiveEntry.getTarBuffer();

        tarBuffer.readRecord(readEOFData);

        assertArrayEquals(expectedEOFData, readEOFData);
    }
}
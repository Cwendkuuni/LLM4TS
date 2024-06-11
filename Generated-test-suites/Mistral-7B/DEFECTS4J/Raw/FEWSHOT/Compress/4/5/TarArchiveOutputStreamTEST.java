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
import org.apache.commons.compress.archivers.TarArchiveEntry;
import org.apache.commons.compress.archivers.TarConstants;
import org.apache.commons.compress.utils.IOUtils;

public class TarArchiveOutputStreamTest {

    @Test
    public void testSetLongFileMode() throws IOException {
        TarArchiveOutputStream tarOutput = new TarArchiveOutputStream(new ByteArrayOutputStream(), 512);
        tarOutput.setLongFileMode(TarArchiveOutputStream.LONGFILE_TRUNCATE);
        TarArchiveOutputStream tarOutput2 = new TarArchiveOutputStream(new ByteArrayOutputStream(), 512);
        tarOutput2.setLongFileMode(TarArchiveOutputStream.LONGFILE_GNU);

        assertEquals(TarArchiveOutputStream.LONGFILE_TRUNCATE, tarOutput.getLongFileMode());
        assertEquals(TarArchiveOutputStream.LONGFILE_GNU, tarOutput2.getLongFileMode());
    }

    @Test
    public void testWriteEOFRecord() throws IOException {
        TarArchiveOutputStream tarOutput = new TarArchiveOutputStream(new ByteArrayOutputStream(), 512);
        tarOutput.finish();

        ByteArrayOutputStream output = (ByteArrayOutputStream) tarOutput.getOutputStream();
        byte[] expectedEOFRecord = new byte[512 * 2];
        for (int i = 0; i < expectedEOFRecord.length; i++) {
            expectedEFRecord[i] = 0;
        }

        byte[] actualEOFRecord = output.toByteArray();
        assertArrayEquals(expectedEOFRecord, actualEOFRecord);
    }

    @Test
    public void testPutArchiveEntryAndCloseArchiveEntry() throws IOException {
        TarArchiveOutputStream tarOutput = new TarArchiveOutputStream(new ByteArrayOutputStream(), 512);
        ArchiveEntry archiveEntry = new TarArchiveEntry("test.txt");
        tarOutput.putArchiveEntry(archiveEntry);
        byte[] testData = "Hello, World!".getBytes();
        tarOutput.write(testData, 0, testData.length);
        tarOutput.closeArchiveEntry();

        tarOutput.finish();

        ByteArrayOutputStream output = (ByteArrayOutputStream) tarOutput.getOutputStream();
        byte[] expectedData = new byte[testData.length];
        System.arraycopy(testData, 0, expectedData, 0, testData.length);

        byte[] actualData = output.toByteArray();
        assertArrayEquals(expectedData, actualData);
    }

    @Test
    public void testPutArchiveEntryAndCloseArchiveEntryWithLongFileName() throws IOException {
        TarArchiveOutputStream tarOutput = new TarArchiveOutputStream(new ByteArrayOutputStream(), 512);
        tarOutput.setLongFileMode(TarArchiveOutputStream.LONGFILE_GNU);
        ArchiveEntry archiveEntry = new TarArchiveEntry("longFileNameTest.txt");
        tarOutput.putArchiveEntry(archiveEntry);
        byte[] testData = "Hello, World!".getBytes();
        tarOutput.write(testData, 0, testData.length);
        tarOutput.closeArchiveEntry();

        tarOutput.finish();

        ByteArrayOutputStream output = (ByteArrayOutputStream) tarOutput.getOutputStream();
        byte[] expectedLongFileName = "longFileNameTest.txt\0".getBytes();
        byte[] expectedData = new byte[testData.length];
        System.arraycopy(testData, 0, expectedData, 0, testData.length);

        byte[] actualData = output.toByteArray();
        byte[] actualLongFileName = new byte[archiveEntry.getName().length() + 1];
        System.arraycopy(actualData, 0, actualLongFileName, 0, actualLongFileName.length);

        assertArrayEquals(expectedLongFileName, actualLongFileName);
        assertArrayEquals(expectedData, IOUtils.subArray(actualData, 512));
    }

    @Test
    public void testPutArchiveEntryWithoutCloseArchiveEntry() throws IOException {
        TarArchiveOutputStream tarOutput = new TarArchiveOutputStream(new ByteArrayOutputStream(), 512);
        ArchiveEntry archiveEntry = new TarArchiveEntry("test.txt");
        tarOutput.putArchiveEntry(archiveEntry);
        byte[] testData = "Hello, World!".getBytes();
        tarOutput.write(testData, 0, testData.length);

        assertFalse(tarOutput.close());
        IOException exception = null;
        try {
            tarOutput.closeArchiveEntry();
        } catch (IOException e) {
            exception = e;
        }
        assertTrue(exception != null);
        assertEquals("This archives contains unclosed entries.", exception.getMessage());
    }

    @Test
    public void testPutArchiveEntryWithoutCloseArchiveEntryWithLongFileName() throws IOException {
        TarArchiveOutputStream tarOutput = new TarArchiveOutputStream(new ByteArrayOutputStream(), 512);
        tarOutput.setLongFileMode(TarArchiveOutputStream.LONGFILE_GNU);
        ArchiveEntry archiveEntry = new TarArchiveEntry("longFileNameTest.txt");
        tarOutput.putArchiveEntry(archiveEntry);
        byte[] testData = "Hello, World!".getBytes();
        tarOutput.write(testData, 0, testData.length);

        assertFalse(tarOutput.close());
        IOException exception = null;
        try {
            tarOutput.closeArchiveEntry();
        } catch (IOException e) {
            exception = e;
        }
        assertTrue(exception != null);
        assertEquals("This archives contains unclosed entries.", exception.getMessage());
    }

    @Test
    public void testPutArchiveEntryAndCloseArchiveEntryWithDirectory() throws IOException {
        TarArchiveOutputStream tarOutput = new TarArchiveOutputStream(new ByteArrayOutputStream(), 512);
        ArchiveEntry archiveEntry = new TarArchiveEntry("testDir");
        tarOutput.putArchiveEntry(archiveEntry);

        tarOutput.finish();

        ByteArrayOutputStream output = (ByteArrayOutputStream) tarOutput.getOutputStream();
        byte[] expectedDirectoryEntry = new byte[512];
        for (int i = 0; i < expectedDirectoryEntry.length; i++) {
            expectedDirectoryEntry[i] = 0;
        }

        byte[] actualData = output.toByteArray();
        assertArrayEquals(expectedDirectoryEntry, IOUtils.subArray(actualData, 0, 512));
    }

    @Test
    public void testWrite() throws IOException {
        TarArchiveOutputStream tarOutput = new TarArchiveOutputStream(new ByteArrayOutputStream(), 512);
        byte[] testData = "Hello, World!".getBytes();
        tarOutput.write(testData, 0, testData.length);

        tarOutput.finish();

        ByteArrayOutputStream output = (ByteArrayOutputStream) tarOutput.getOutputStream();
        byte[] actualData = output.toByteArray();

        assertArrayEquals(testData, actualData);
    }

    @Test
    public void testWriteWithUnclosedEntry() throws IOException {
        TarArchiveOutputStream tarOutput = new TarArchiveOutputStream(new ByteArrayOutputStream(), 512);
        ArchiveEntry archiveEntry = new TarArchiveEntry("test.txt");
        tarOutput.putArchiveEntry(archiveEntry);
        byte[] testData = "Hello, World!".getBytes();
        tarOutput.write(testData, 0, testData.length);

        IOException exception = null;
        try {
            tarOutput.write(testData, 0, testData.length);
        } catch (IOException e) {
            exception = e;
        }
        assertTrue(exception != null);
        assertEquals("entry 'test.txt' closed at '33' before the '51' bytes specified in the header were written",
                     exception.getMessage());
    }

    @Test
    public void testWriteWithAssembleRecord() throws IOException {
        TarArchiveOutputStream tarOutput = new TarArchiveOutputStream(new ByteArrayOutputStream(), 512);
        byte[] testData = "Hello, World!".getBytes();
        byte[] assembleRecord = new byte[512 - (testData.length % 512)];

        tarOutput.write(testData, 0, testData.length);
        tarOutput.write(assembleRecord, 0, assembleRecord.length);

        tarOutput.finish();

        ByteArrayOutputStream output = (ByteArrayOutputStream) tarOutput.getOutputStream();
        byte[] actualData = output.toByteArray();

        byte[] expectedData = new byte[testData.length + assembleRecord.length];
        System.arraycopy(testData, 0, expectedData, 0, testData.length);
        System.arraycopy(assembleRecord, 0, expectedData, testData.length, assembleRecord.length);

        assertArrayEquals(expectedData, actualData);
    }

    @Test
    public void testCreateArchiveEntry() throws IOException {
        TarArchiveOutputStream tarOutput = new TarArchiveOutputStream(new ByteArrayOutputStream(), 512);
        ArchiveEntry archiveEntry = tarOutput.createArchiveEntry(new File("test.txt"), "test.txt");

        assertEquals("test.txt", archiveEntry.getName());
    }

    @Test(expected = ClassCastException.class)
    public void testCreateArchiveEntryWithNonTarArchiveEntry() throws IOException {
        TarArchiveOutputStream tarOutput = new TarArchiveOutputStream(new ByteArrayOutputStream(), 512);
        ArchiveEntry archiveEntry = new Object();
        tarOutput.createArchiveEntry(new File("test.txt"), "test.txt");
    }
}
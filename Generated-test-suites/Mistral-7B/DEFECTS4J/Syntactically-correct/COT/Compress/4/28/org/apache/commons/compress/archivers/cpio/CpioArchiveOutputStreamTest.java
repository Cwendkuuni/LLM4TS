package org.apache.commons.compress.archivers.cpio;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;

import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.ArchiveOutputStream;
import org.apache.commons.compress.archivers.CpioArchiveEntry;
import org.apache.commons.compress.utils.ArchiveUtils;
import org.junit.Test;

/**
 * Test class for CpioArchiveOutputStream
 */
public class CpioArchiveOutputStreamTest {

    /**
     * Test constructor with valid format
     * @throws IOException
     */
    @Test
    public void testCpioArchiveOutputStreamWithValidFormat() throws IOException {
        OutputStream outputStream = new ByteArrayOutputStream();
        CpioArchiveOutputStream cpioArchiveOutputStream = new CpioArchiveOutputStream(outputStream, CpioConstants.FORMAT_NEW);
        assertTrue(cpioArchiveOutputStream instanceof CpioArchiveOutputStream);
    }

    /**
     * Test constructor with invalid format
     */
    @Test(expected = IllegalArgumentException.class)
    public void testCpioArchiveOutputStreamWithInvalidFormat() {
        OutputStream outputStream = new ByteArrayOutputStream();
        CpioArchiveOutputStream cpioArchiveOutputStream = new CpioArchiveOutputStream(outputStream, (short) 123);
    }

    /**
     * Test putArchiveEntry method with valid arguments
     * @throws IOException
     */
    @Test
    public void testPutArchiveEntryWithValidArguments() throws IOException {
        OutputStream outputStream = new ByteArrayOutputStream();
        CpioArchiveOutputStream cpioArchiveOutputStream = new CpioArchiveOutputStream(outputStream, CpioConstants.FORMAT_NEW);
        ArchiveEntry archiveEntry = new CpioArchiveEntry("testEntryName", "testContent".getBytes());
        cpioArchiveOutputStream.putArchiveEntry(archiveEntry);
        assertTrue(cpioArchiveOutputStream.getNextEntry() != null);
        assertEquals(archiveEntry.getName(), cpioArchiveOutputStream.getNextEntry().getName());
    }

    /**
     * Test putArchiveEntry method with null archiveEntry argument
     */
    @Test(expected = IllegalArgumentException.class)
    public void testPutArchiveEntryWithNullArchiveEntry() {
        OutputStream outputStream = new ByteArrayOutputStream();
        CpioArchiveOutputStream cpioArchiveOutputStream = new CpioArchiveOutputStream(outputStream, CpioConstants.FORMAT_NEW);
        ArchiveEntry archiveEntry = null;
        cpioArchiveOutputStream.putArchiveEntry(archiveEntry);
    }

    /**
     * Test putArchiveEntry method with duplicate entry name
     * @throws IOException
     */
    @Test(expected = IOException.class)
    public void testPutArchiveEntryWithDuplicateEntryName() throws IOException {
        OutputStream outputStream = new ByteArrayOutputStream();
        CpioArchiveOutputStream cpioArchiveOutputStream = new CpioArchiveOutputStream(outputStream, CpioConstants.FORMAT_NEW);
        ArchiveEntry archiveEntry1 = new CpioArchiveEntry("testEntryName", "testContent1".getBytes());
        ArchiveEntry archiveEntry2 = new CpioArchiveEntry("testEntryName", "testContent2".getBytes());
        cpioArchiveOutputStream.putArchiveEntry(archiveEntry1);
        cpioArchiveOutputStream.putArchiveEntry(archiveEntry2);
    }

    /**
     * Test write method with valid arguments
     * @throws IOException
     */
    @Test
    public void testWriteWithValidArguments() throws IOException {
        OutputStream outputStream = new ByteArrayOutputStream();
        CpioArchiveOutputStream cpioArchiveOutputStream = new CpioArchiveOutputStream(outputStream, CpioConstants.FORMAT_NEW);
        byte[] testData = "testData".getBytes();
        ArchiveEntry archiveEntry = new CpioArchiveEntry("testEntryName", testData);
        cpioArchiveOutputStream.putArchiveEntry(archiveEntry);
        cpioArchiveOutputStream.write(testData, 0, testData.length);
        byte[] outputBytes = cpioArchiveOutputStream.toByteArray();
        ByteArrayInputStream inputStream = new ByteArrayInputStream(outputBytes);
        ArchiveInputStream archiveInputStream = new CpioArchiveInputStream(inputStream);
        ArchiveEntry nextEntry = archiveInputStream.getNextEntry();
        assertEquals(archiveEntry.getName(), nextEntry.getName());
        byte[] testEntryData = new byte[testData.length];
        inputStream.read(testEntryData);
        assertArrayEquals(testData, testEntryData);
    }

    /**
     * Test write method with null testData argument
     */
    @Test(expected = NullPointerException.class)
    public void testWriteWithNullTestData() {
        OutputStream outputStream = new ByteArrayOutputStream();
        CpioArchiveOutputStream cpioArchiveOutputStream = new CpioArchiveOutputStream(outputStream, CpioConstants.FORMAT_NEW);
        ArchiveEntry archiveEntry = new CpioArchiveEntry("testEntryName", null);
        cpioArchiveOutputStream.putArchiveEntry(archiveEntry);
        cpioArchiveOutputStream.write(null, 0, 0);
    }

    /**
     * Test write method with arguments outside of the file length
     */
    @Test
    public void testWriteWithArgumentsOutsideOfTheFileLength() throws IOException {
        OutputStream outputStream = new ByteArrayOutputStream();
        CpioArchiveOutputStream cpioArchiveOutputStream = new CpioArchiveOutputStream(outputStream, CpioConstants.FORMAT_NEW);
        byte[] testData = "testData".getBytes();
        ArchiveEntry archiveEntry = new CpioArchiveEntry("testEntryName", testData);
        cpioArchiveOutputStream.putArchiveEntry(archiveEntry);
        cpioArchiveOutputStream.write(testData, testData.length, testData.length);
        byte[] outputBytes = cpioArchiveOutputStream.toByteArray();
        ByteArrayInputStream inputStream = new ByteArrayInputStream(outputBytes);
        ArchiveInputStream archiveInputStream = new CpioArchiveInputStream(inputStream);
        ArchiveEntry nextEntry = archiveInputStream.getNextEntry();
        assertEquals(archiveEntry.getName(), nextEntry.getName());
        byte[] testEntryData = new byte[testData.length];
        inputStream.read(testEntryData);
        assertArrayEquals(testData, testEntryData);
    }

    /**
     * Test finish method with no previous finish call
     */
    @Test(expected = IllegalStateException.class)
    public void testFinishWithNoPreviousFinishCall() throws IOException {
        OutputStream outputStream = new ByteArrayOutputStream();
        CpioArchiveOutputStream cpioArchiveOutputStream = new CpioArchiveOutputStream(outputStream, CpioConstants.FORMAT_NEW);
        cpioArchiveOutputStream.finish();
    }

    /**
     * Test close method with no previous close call
     */
    @Test(expected = IllegalStateException.class)
    public void testCloseWithNoPreviousCloseCall() throws IOException {
        OutputStream outputStream = new ByteArrayOutputStream();
        CpioArchiveOutputStream cpioArchiveOutputStream = new CpioArchiveOutputStream(outputStream, CpioConstants.FORMAT_NEW);
        cpioArchiveOutputStream.close();
    }

    /**
     * Test close method after finish method call
     * @throws IOException
     */
    @Test
    public void testCloseAndFinish() throws IOException {
        OutputStream outputStream = new ByteArrayOutputStream();
        CpioArchiveOutputStream cpioArchiveOutputStream = new CpioArchiveOutputStream(outputStream, CpioConstants.FORMAT_NEW);
        ArchiveEntry archiveEntry = new CpioArchiveEntry("testEntryName", "testContent".getBytes());
        cpioArchiveOutputStream.putArchiveEntry(archiveEntry);
        cpioArchiveOutputStream.write(archiveEntry.getFileContent(), 0, archiveEntry.getFileContent().length);
        cpioArchiveOutputStream.finish();
        cpioArchiveOutputStream.close();
        byte[] outputBytes = cpioArchiveOutputStream.toByteArray();
        ByteArrayInputStream inputStream = new ByteArrayInputStream(outputBytes);
        ArchiveInputStream archiveInputStream = new CpioArchiveInputStream(inputStream);
        ArchiveEntry nextEntry = archiveInputStream.getNextEntry();
        assertEquals(archiveEntry.getName(), nextEntry.getName());
        byte[] testEntryData = new byte[archiveEntry.getFileContent().length];
        inputStream.read(testEntryData);
        assertArrayEquals(archiveEntry.getFileContent(), testEntryData);
        assertNull(cpioArchiveOutputStream.getNextEntry());
        assertFalse(cpioArchiveOutputStream.hasNextEntry());
    }
}
package org.apache.commons.compress.archivers.cpio;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;

import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.CpioArchiveEntry;
import org.apache.commons.compress.archivers.CpioConstants;
import org.apache.commons.compress.utils.ArchiveUtils;
import org.apache.commons.compress.utils.CpioUtil;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * Tests for {@link CpioArchiveOutputStream} class
 */
public class CpioArchiveOutputStreamTest {

    private File testFile;
    private OutputStream out;
    private CpioArchiveOutputStream cpioStream;

    @Before
    public void setUp() throws IOException {
        testFile = File.createTempFile("junit-test", null);
        out = new FileOutputStream(testFile);
        cpioStream = new CpioArchiveOutputStream(out, CpioConstants.FORMAT_NEW);
    }

    @After
    public void tearDown() throws IOException {
        if (cpioStream != null) {
            cpioStream.close();
        }
        if (testFile != null && testFile.exists()) {
            testFile.delete();
        }
    }

    /**
     * Test method for {@link CpioArchiveOutputStream#CpioArchiveOutputStream(OutputStream, short)}
     */
    @Test
    public void testCpioArchiveOutputStreamConstruction() {
        assertNull(cpioStream.getEntry());
        assertFalse(cpioStream.isClosed());
        assertFalse(cpioStream.isFinished());
        assertEquals(CpioConstants.FORMAT_NEW, cpioStream.getFormat());
        assertNull(cpioStream.getNames());
        assertEquals(0, cpioStream.getCrc());
        assertEquals(0, cpioStream.getWritten());
    }

    /**
     * Test method for {@link CpioArchiveOutputStream#putArchiveEntry(ArchiveEntry)}
     * with null ArchiveEntry
     */
    @Test
    public void testPutArchiveEntryNullEntry() {
        assertThrows(NullPointerException.class, () -> cpioStream.putArchiveEntry(null));
    }

    /**
     * Test method for {@link CpioArchiveOutputStream#putArchiveEntry(ArchiveEntry)}
     * with invalid ArchiveEntry format
     */
    @Test
    public void testPutArchiveEntryInvalidFormat() {
        CpioArchiveEntry entry = new CpioArchiveEntry();
        entry.setFormat((short) 0x10);
        assertThrows(IllegalArgumentException.class, () -> cpioStream.putArchiveEntry(entry));
    }

    /**
     * Test method for {@link CpioArchiveOutputStream#putArchiveEntry(ArchiveEntry)}
     * with duplicate entry
     */
    @Test
    public void testPutArchiveEntryDuplicateEntry() throws IOException {
        CpioArchiveEntry entry1 = new CpioArchiveEntry();
        CpioArchiveEntry entry2 = new CpioArchiveEntry();
        cpioStream.putArchiveEntry(entry1);
        assertThrows(IOException.class, () -> cpioStream.putArchiveEntry(entry2));
    }

    /**
     * Test method for {@link CpioArchiveOutputStream#putArchiveEntry(ArchiveEntry)}
     * with already finished stream
     */
    @Test
    public void testPutArchiveEntryFinishedStream() {
        cpioStream.finish();
        CpioArchiveEntry entry = new CpioArchiveEntry();
        assertThrows(IOException.class, () -> cpioStream.putArchiveEntry(entry));
    }

    /**
     * Test method for {@link CpioArchiveOutputStream#putArchiveEntry(ArchiveEntry)}
     * with valid ArchiveEntry
     */
    @Test
    public void testPutArchiveEntry() throws IOException {
        CpioArchiveEntry entry = new CpioArchiveEntry();
        cpioStream.putArchiveEntry(entry);
        assertEquals(entry, cpioStream.getEntry());
        assertTrue(cpioStream.isClosed());
        assertFalse(cpioStream.isFinished());
        assertEquals(1, cpioStream.getNames().size());
        assertEquals(entry.getName(), cpioStream.getNames().get(entry.getName()));
    }

    /**
     * Test method for {@link CpioArchiveOutputStream#write(byte[], int, int)}
     * with null input byte array
     */
    @Test
    public void testWriteNullInputByteArray() {
        assertThrows(NullPointerException.class, () -> cpioStream.write(null, 0, 0));
    }

    /**
     * Test method for {@link CpioArchiveOutputStream#write(byte[], int, int)}
     * with negative input offset
     */
    @Test
    public void testWriteNegativeInputOffset() {
        byte[] data = new byte[10];
        assertThrows(IndexOutOfBoundsException.class, () -> cpioStream.write(data, -1, 10));
    }

    /**
     * Test method for {@link CpioArchiveOutputStream#write(byte[], int, int)}
     * with negative input length
     */
    @Test
    public void testWriteNegativeInputLength() {
        byte[] data = new byte[10];
        assertThrows(NegativeArraySizeException.class, () -> cpioStream.write(data, 0, -1));
    }

    /**
     * Test method for {@link CpioArchiveOutputStream#write(byte[], int, int)}
     * with already finished stream
     */
    @Test
    public void testWriteFinishedStream() {
        cpioStream.finish();
        byte[] data = new byte[10];
        assertThrows(IOException.class, () -> cpioStream.write(data, 0, 10));
    }

    /**
     * Test method for {@link CpioArchiveOutputStream#write(byte[], int, int)}
     * with valid input byte array
     */
    @Test
    public void testWrite() throws IOException {
        CpioArchiveEntry entry = new CpioArchiveEntry();
        cpioStream.putArchiveEntry(entry);
        byte[] data = "Test data".getBytes();
        int bytesWritten = cpioStream.write(data, 0, data.length);
        assertEquals(data.length, bytesWritten);
        assertEquals(data.length, cpioStream.getWritten());
    }

    /**
     * Test method for {@link CpioArchiveOutputStream#finish()}
     */
    @Test
    public void testFinish() throws IOException {
        CpioArchiveEntry entry1 = new CpioArchiveEntry();
        CpioArchiveEntry entry2 = new CpioArchiveEntry();
        cpioStream.putArchiveEntry(entry1);
        cpioStream.write("Test data 1".getBytes(), 0, "Test data 1".length());
        cpioStream.putArchiveEntry(entry2);
        cpioStream.write("Test data 2".getBytes(), 0, "Test data 2".length());
        cpioStream.finish();

        CpioArchiveEntry trailerEntry = new CpioArchiveEntry(CpioConstants.FORMAT_NEW);
        trailerEntry.setName(CpioConstants.CPIO_TRAILER);
        assertEquals(trailerEntry, cpioStream.getEntry());
        assertTrue(cpioStream.isFinished());
    }

    /**
     * Test method for {@link CpioArchiveOutputStream#close()}
     */
    @Test
    public void testClose() throws IOException {
        CpioArchiveEntry entry1 = new CpioArchiveEntry();
        CpioArchiveEntry entry2 = new CpioArchiveEntry();
        cpioStream.putArchiveEntry(entry1);
        cpioStream.write("Test data 1".getBytes(), 0, "Test data 1".length());
        cpioStream.putArchiveEntry(entry2);
        cpioStream.write("Test data 2".getBytes(), 0, "Test data 2".length());
        cpioStream.close();

        assertTrue(cpioStream.isClosed());
        assertFalse(cpioStream.isFinished());

        // Check that the CpioArchiveOutputStream has been closed correctly
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        FileOutputStream fos = new FileOutputStream(testFile);
        CpioArchiveOutputStream archivedStream = new CpioArchiveOutputStream(fos, CpioConstants.FORMAT_NEW);
        ArchiveEntry entry = new CpioArchiveEntry();
        ArchiveInputStream inputStream = new CpioArchiveInputStream(new FileInputStream(testFile));
        try {
            ArchiveEntry currentEntry = inputStream.getNextEntry();
            while (currentEntry != null) {
                if (currentEntry.getName().equals(entry1.getName())) {
                    BosInputStream bosInputStream = new BosInputStream(new ByteArrayInputStream(currentEntry.getInputStream()));
                    byte[] testData = new byte[currentEntry.getSize()];
                    int bytesRead = bosInputStream.read(testData, 0, testData.length);
                    assertEquals("Test data 1", new String(testData));
                    break;
                }
                currentEntry = inputStream.getNextEntry();
            }
        } finally {
            inputStream.close();
        }
        archivedStream.close();
        fos.close();
        testFile.delete();
    }
}
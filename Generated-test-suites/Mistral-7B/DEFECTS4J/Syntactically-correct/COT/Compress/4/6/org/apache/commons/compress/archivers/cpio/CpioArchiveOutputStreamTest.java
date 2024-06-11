package org.apache.commons.compress.archivers.cpio;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;

import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.CpioArchiveEntry;
import org.apache.commons.compress.archivers.CpioConstants;
import org.apache.commons.compress.utils.CpioUtil;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * Test cases for CpioArchiveOutputStream class.
 */
public class CpioArchiveOutputStreamTest {

    private OutputStream out;
    private CpioArchiveOutputStream cpioOutputStream;

    @Before
    public void setUp() throws Exception {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        this.out = baos;
        this.cpioOutputStream = new CpioArchiveOutputStream(this.out, CpioConstants.FORMAT_NEW);
    }

    @After
    public void tearDown() throws Exception {
        this.cpioOutputStream.close();
    }

    /**
     * Test constructor with new format.
     * @throws IOException
     */
    @Test
    public void testConstructorNewFormat() throws IOException {
        // Test the constructor
        assertTrue(this.cpioOutputStream instanceof CpioArchiveOutputStream);
        assertEquals(CpioConstants.FORMAT_NEW, this.cpioOutputStream.getFormat());
        assertNull(this.cpioOutputStream.getEntry());
        assertFalse(this.cpioOutputStream.isFinished());
    }

    /**
     * Test method putArchiveEntry.
     * @throws IOException
     */
    @Test
    public void testPutArchiveEntry() throws IOException {
        CpioArchiveEntry entry = new CpioArchiveEntry("testFile.txt", "testContent");
        this.cpioOutputStream.putArchiveEntry(entry);
        ArchiveEntry archiveEntry = this.cpioOutputStream.getNextEntry();
        assertEquals(entry.getName(), archiveEntry.getName());
        assertEquals(entry.getFormat(), archiveEntry.getFormat());
        assertEquals(CpioConstants.C_ISREG, archiveEntry.getFileType());
        assertEquals(0, archiveEntry.getSize());
        assertEquals(-1, archiveEntry.getTime());
        assertNull(archiveEntry.getSize());
        assertNull(archiveEntry.getInputStream());
        assertNull(archiveEntry.getComment());
        assertFalse(archiveEntry.isDirectory());
    }

    /**
     * Test method putArchiveEntry with invalid format.
     * @throws IOException
     */
    @Test(expected = IllegalArgumentException.class)
    public void testPutArchiveEntryInvalidFormat() throws IOException {
        CpioArchiveEntry entry = new CpioArchiveEntry("testFile.txt", "testContent");
        this.cpioOutputStream = new CpioArchiveOutputStream(this.out, 10);
        this.cpioOutputStream.putArchiveEntry(entry);
    }

    /**
     * Test method write.
     * @throws IOException
     */
    @Test
    public void testWrite() throws IOException {
        CpioArchiveEntry entry = new CpioArchiveEntry("testFile.txt", "testContent");
        this.cpioOutputStream.putArchiveEntry(entry);
        byte[] data = "test data".getBytes();
        this.cpioOutputStream.write(data, 0, data.length);
        ArchiveEntry archiveEntry = this.cpioOutputStream.getNextEntry();
        byte[] readData = new byte[data.length];
        this.cpioOutputStream.read(readData, 0, readData.length);
        assertEquals(data, readData);
        assertEquals(data.length, archiveEntry.getSize());
    }

    /**
     * Test method closeArchiveEntry.
     * @throws IOException
     */
    @Test
    public void testCloseArchiveEntry() throws IOException {
        CpioArchiveEntry entry = new CpioArchiveEntry("testFile.txt", "testContent");
        this.cpioOutputStream.putArchiveEntry(entry);
        byte[] data = "test data".getBytes();
        this.cpioOutputStream.write(data, 0, data.length);
        this.cpioOutputStream.closeArchiveEntry();
        ArchiveEntry archiveEntry = this.cpioOutputStream.getNextEntry();
        assertEquals(0, archiveEntry.getSize());
    }

    /**
     * Test method finish.
     * @throws IOException
     */
    @Test
    public void testFinish() throws IOException {
        CpioArchiveEntry entry = new CpioArchiveEntry("testFile.txt", "testContent");
        this.cpioOutputStream.putArchiveEntry(entry);
        byte[] data = "test data".getBytes();
        this.cpioOutputStream.write(data, 0, data.length);
        this.cpioOutputStream.finish();
        ArchiveEntry archiveEntry = this.cpioOutputStream.getNextEntry();
        assertEquals(CpioConstants.C_TRAILER, archiveEntry.getFileType());
        assertNull(archiveEntry.getName());
        assertNull(archiveEntry.getComment());
        assertFalse(archiveEntry.isDirectory());
    }

    /**
     * Test method close.
     * @throws IOException
     */
    @Test
    public void testClose() throws IOException {
        CpioArchiveEntry entry = new CpioArchiveEntry("testFile.txt", "testContent");
        this.cpioOutputStream.putArchiveEntry(entry);
        byte[] data = "test data".getBytes();
        this.cpioOutputStream.write(data, 0, data.length);
        this.cpioOutputStream.finish();
        this.cpioOutputStream.close();
        assertEquals(0, this.cpioOutputStream.available());
    }
}
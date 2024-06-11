package org.apache.commons.compress.archivers.cpio; // adjust the package if needed

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

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

    private OutputStream outputStream;
    private CpioArchiveOutputStream cpioArchiveOutputStream;

    @Before
    public void setUp() throws Exception {
        outputStream = new ByteArrayOutputStream(1024);
        cpioArchiveOutputStream = new CpioArchiveOutputStream(outputStream, CpioConstants.FORMAT_NEW);
    }

    @After
    public void tearDown() throws Exception {
        if (cpioArchiveOutputStream != null) {
            cpioArchiveOutputStream.close();
        }
    }

    /**
     * Test constructor with valid format.
     */
    @Test
    public void testCpioArchiveOutputStreamConstructorWithValidFormat() {
        CpioArchiveOutputStream cpioArchiveOutputStream = new CpioArchiveOutputStream(outputStream, CpioConstants.FORMAT_NEW);
        assertNull(cpioArchiveOutputStream.getEntry());
        assertFalse(cpioArchiveOutputStream.isClosed());
        assertFalse(cpioArchiveOutputStream.isFinished());
    }

    /**
     * Test constructor with invalid format.
     */
    @Test
    public void testCpioArchiveOutputStreamConstructorWithInvalidFormat() {
        assertThrows(IllegalArgumentException.class, () -> new CpioArchiveOutputStream(outputStream, (short) 0xFF));
    }

    /**
     * Test putArchiveEntry method with a valid entry.
     */
    @Test
    public void testPutArchiveEntryWithValidEntry() throws IOException {
        ArchiveEntry entry = new CpioArchiveEntry("test.txt");
        cpioArchiveOutputStream.putArchiveEntry(entry);
        assertEquals(CpioConstants.FORMAT_NEW, cpioArchiveOutputStream.getEntryFormat());
        assertNull(cpioArchiveOutputStream.getEntry());

        // At this point, a header should have been written to the output stream.
        // But since we're testing against an in-memory output stream, we'll verify
        // that the header is as expected by reading it from the byte array.
        byte[] outputBytes = outputStream.toByteArray();
        assertEquals(ArchiveUtils.toAsciiBytes(CpioConstants.MAGIC_NEW), new byte[] {outputBytes[0], outputBytes[1]});
        assertEquals(ArchiveUtils.toAsciiBytes(CpioConstants.MAGIC_NEW)[0], outputBytes[2]);
    }

    /**
     * Test putArchiveEntry method with a duplicate entry.
     */
    @Test
    public void testPutArchiveEntryWithDuplicateEntry() throws IOException {
        ArchiveEntry entry = new CpioArchiveEntry("test.txt");
        cpioArchiveOutputStream.putArchiveEntry(entry);

        assertThrows(IOException.class, () -> cpioArchiveOutputStream.putArchiveEntry(entry));
    }

    /**
     * Test closeArchiveEntry method.
     */
    @Test
    public void testCloseArchiveEntry() throws IOException {
        ArchiveEntry entry = new CpioArchiveEntry("test.txt");
        cpioArchiveOutputStream.putArchiveEntry(entry);

        byte[] testBytes = "Hello, World!".getBytes();
        cpioArchiveOutputStream.write(testBytes, 0, testBytes.length);
        cpioArchiveOutputStream.closeArchiveEntry();

        assertEquals(testBytes.length, cpioArchiveOutputStream.written);
        assertNull(cpioArchiveOutputStream.entry);
        assertFalse(cpioArchiveOutputStream.isClosed());
        assertFalse(cpioArchiveOutputStream.isFinished());
    }

    /**
     * Test finish method with no entries.
     */
    @Test
    public void testFinishWithNoEntries() throws IOException {
        cpioArchiveOutputStream.finish();

        assertEquals(1, cpioArchiveOutputStream.names.size());
        assertEquals(CpioConstants.CPIO_TRAILER, cpioArchiveOutputStream.names.keySet().iterator().next().toString());
        assertTrue(cpioArchiveOutputStream.isFinished());
    }

    /**
     * Test finish method with one entry.
     */
    @Test
    public void testFinishWithOneEntry() throws IOException {
        File testFile = new File("test.txt");
        ArchiveEntry entry = new CpioArchiveEntry(testFile, "test.txt");
        cpioArchiveOutputStream.putArchiveEntry(entry);
        byte[] testBytes = "Hello, World!".getBytes();
        cpioArchiveOutputStream.write(testBytes, 0, testBytes.length);
        cpioArchiveOutputStream.finish();

        assertEquals(1, cpioArchiveOutputStream.names.size());
        assertEquals("test.txt", cpioArchiveOutputStream.names.keySet().iterator().next().toString());
        assertTrue(cpioArchiveOutputStream.isFinished());
    }

    /**
     * Test close method.
     */
    @Test
    public void testClose() throws IOException {
        File testFile = new File("test.txt");
        ArchiveEntry entry = new CpioArchiveEntry(testFile, "test.txt");
        cpioArchiveOutputStream.putArchiveEntry(entry);
        byte[] testBytes = "Hello, World!".getBytes();
        cpioArchiveOutputStream.write(testBytes, 0, testBytes.length);
        cpioArchiveOutputStream.closeArchiveEntry();
        cpioArchiveOutputStream.finish();
        cpioArchiveOutputStream.close();

        assertEquals(1, cpioArchiveOutputStream.names.size());
        assertEquals("test.txt", cpioArchiveOutputStream.names.keySet().iterator().next().toString());
        assertTrue(cpioArchiveOutputStream.isFinished());
        assertTrue(cpioArchiveOutputStream.isClosed());
    }

    /**
     * Test write method.
     */
    @Test
    public void testWrite() throws IOException {
        File testFile = new File("test.txt");
        ArchiveEntry entry = new CpioArchiveEntry(testFile, "test.txt");
        cpioArchiveOutputStream.putArchiveEntry(entry);

        byte[] testBytes1 = "Hello, World!".getBytes();
        cpioArchiveOutputStream.write(testBytes1, 0, testBytes1.length);

        byte[] testBytes2 = "This is a test".getBytes();
        cpioArchiveOutputStream.write(testBytes2, 0, testBytes2.length);

        byte[] expectedOutput = new byte[testBytes1.length + testBytes2.length + 2]; // for 2 headers
        System.arraycopy(ArchiveUtils.toAsciiBytes(CpioConstants.MAGIC_NEW), 0, expectedOutput, 0, 2);
        System.arraycopy(CpioUtil.long2byteArray(1, 8), 2, expectedOutput, 2, 8); // inode number
        System.arraycopy(ArchiveUtils.toAsciiBytes(CpioConstants.C_ISREG), 10, expectedOutput, 10, 6); // file type
        System.arraycopy(CpioUtil.long2byteArray(0755, 8), 16, expectedOutput, 16, 8); // permissions
        System.arraycopy(CpioUtil.long2byteArray(1000, 8), 24, expectedOutput, 24, 8); // owner UID
        System.arraycopy(CpioUtil.long2byteArray(1000, 8), 32, expectedOutput, 32, 8); // group GID
        System.arraycopy(CpioUtil.long2byteArray(1, 8), 40, expectedOutput, 40, 8); // number of links
        System.arraycopy(CpioUtils.toAsciiBytes("test.txt"), 48, expectedOutput, 48, 6); // name
        System.arraycopy(testBytes1, 0, expectedOutput, 54, testBytes1.length); // contents
        System.arraycopy(ArchiveUtils.toAsciiBytes(CpioConstants.MAGIC_NEW), 0, expectedOutput, 54 + testBytes1.length, 2); // next header

        byte[] outputBytes = outputStream.toByteArray();
        assertEquals(expectedOutput.length, outputBytes.length);
        for (int i = 0; i < expectedOutput.length; i++) {
            assertEquals(expectedOutput[i], outputBytes[i]);
        }
    }

    /**
     * Test createArchiveEntry method.
     */
    @Test
    public void testCreateArchiveEntry() throws IOException {
        File testFile = new File("test.txt");
        ArchiveEntry entry = cpioArchiveOutputStream.createArchiveEntry(testFile, "test.txt");
        assertEquals("test.txt", entry.getName());
    }
}
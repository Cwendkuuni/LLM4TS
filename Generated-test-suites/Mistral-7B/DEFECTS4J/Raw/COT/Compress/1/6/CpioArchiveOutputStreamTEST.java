package org.apache.commons.compress.archivers.cpio;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.CpioArchiveEntry;
import org.apache.commons.compress.utils.CpioConstants;
import org.apache.commons.compress.utils.CpioUtil;
import org.junit.Before;
import org.junit.Test;

/**
 * JUnit test cases for CpioArchiveOutputStream class
 */
public class CpioArchiveOutputStreamTest {

    private OutputStream outputStream;
    private CpioArchiveOutputStream cpioArchiveOutputStream;

    @Before
    public void setUp() {
        outputStream = new ByteArrayOutputStream();
        cpioArchiveOutputStream = new CpioArchiveOutputStream(outputStream, CpioConstants.FORMAT_NEW);
    }

    @Test
    public void testCtorWithFormat() {
        CpioArchiveOutputStream cpioArchiveOutputStream = new CpioArchiveOutputStream(outputStream, CpioConstants.FORMAT_NEW);
        assertNotNull(cpioArchiveOutputStream);
    }

    @Test
    public void testCtorWithoutFormat() {
        CpioArchiveOutputStream cpioArchiveOutputStream = new CpioArchiveOutputStream(outputStream);
        assertNotNull(cpioArchiveOutputStream);
        assertEquals(CpioConstants.FORMAT_NEW, cpioArchiveOutputStream.getFormat());
    }

    @Test
    public void testPutNextEntry() throws IOException {
        String entryName = "testEntry";
        long entrySize = 10;
        CpioArchiveEntry cpioArchiveEntry = new CpioArchiveEntry();
        cpioArchiveEntry.setName(entryName);
        cpioArchiveEntry.setSize(entrySize);

        cpioArchiveOutputStream.putNextEntry(cpioArchiveEntry);
        assertEquals(entryName, cpioArchiveEntry.getName());
        assertEquals(entrySize, cpioArchiveEntry.getSize());
        assertEquals(CpioConstants.FORMAT_NEW, cpioArchiveEntry.getFormat());
        assertFalse(cpioArchiveOutputStream.isClosed());
        assertNull(cpioArchiveOutputStream.getNextEntry());
    }

    @Test
    public void testPutNextEntryWithSameName() throws IOException {
        String entryName = "testEntry";
        long entrySize = 10;
        CpioArchiveEntry cpioArchiveEntry = new CpioArchiveEntry();
        cpioArchiveEntry.setName(entryName);
        cpioArchiveEntry.setSize(entrySize);

        cpioArchiveOutputStream.putNextEntry(cpioArchiveEntry);
        assertThrows(IOException.class, () -> cpioArchiveOutputStream.putNextEntry(cpioArchiveEntry));
    }

    @Test
    public void testWrite() throws IOException {
        String entryName = "testEntry";
        long entrySize = 10;
        CpioArchiveEntry cpioArchiveEntry = new CpioArchiveEntry();
        cpioArchiveEntry.setName(entryName);
        cpioArchiveEntry.setSize(entrySize);

        cpioArchiveOutputStream.putNextEntry(cpioArchiveEntry);
        byte[] testData = new byte[entrySize];
        cpioArchiveOutputStream.write(testData);
        cpioArchiveOutputStream.closeArchiveEntry();

        byte[] actualData = ((ByteArrayOutputStream) outputStream).toByteArray();
        byte[] expectedData = new byte[entrySize];
        System.arraycopy(testData, 0, expectedData, 0, entrySize);
        byte[] pad = new byte[4 - (entrySize % 4)];
        byte[] expectedPaddedData = new byte[entrySize + 4];
        System.arraycopy(expectedData, 0, expectedPaddedData, 0, entrySize);
        System.arraycopy(pad, 0, expectedPaddedData, entrySize, pad.length);

        assertEquals(CpioUtil.bytesToString(expectedPaddedData),
                CpioUtil.bytesToString(actualData));
    }

    @Test
    public void testFinish() throws IOException {
        String entryName = "testEntry";
        long entrySize = 10;
        CpioArchiveEntry cpioArchiveEntry = new CpioArchiveEntry();
        cpioArchiveEntry.setName(entryName);
        cpioArchiveEntry.setSize(entrySize);

        cpioArchiveOutputStream.putNextEntry(cpioArchiveEntry);
        byte[] testData = new byte[entrySize];
        cpioArchiveOutputStream.write(testData);
        cpioArchiveOutputStream.finish();

        byte[] actualData = ((ByteArrayOutputStream) outputStream).toByteArray();
        assertEquals(actualData.length, 10 + 1 + entrySize + 4);
    }

    @Test
    public void testClose() throws IOException {
        cpioArchiveOutputStream.close();
        assertTrue(cpioArchiveOutputStream.isClosed());
        assertNull(cpioArchiveOutputStream.getNextEntry());
    }
}
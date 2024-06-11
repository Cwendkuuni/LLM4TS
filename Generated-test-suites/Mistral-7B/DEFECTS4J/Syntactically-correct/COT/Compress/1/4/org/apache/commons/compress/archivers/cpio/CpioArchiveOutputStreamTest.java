package org.apache.commons.compress.archivers.cpio;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;

import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.CpioArchiveEntry;
import org.apache.commons.compress.archivers.CpioConstants;
import org.apache.commons.compress.utils.CpioUtil;
import org.junit.Before;
import org.junit.Test;

/**
 * Test class for CpioArchiveOutputStream
 */
public class CpioArchiveOutputStreamTest {

    private OutputStream outputStream;
    private CpioArchiveOutputStream cpioArchiveOutputStream;

    @Before
    public void setUp() throws Exception {
        outputStream = new ByteArrayOutputStream();
        cpioArchiveOutputStream = new CpioArchiveOutputStream(outputStream,
                CpioConstants.FORMAT_NEW);
    }

    // Basic Test Cases

    @Test
    public void testCtor_OutputStream() throws Exception {
        cpioArchiveOutputStream = new CpioArchiveOutputStream(outputStream);
        assertSame(outputStream, cpioArchiveOutputStream.getOutputStream());
    }

    @Test
    public void testPutNextEntry_NullCpioArchiveEntry() throws Exception {
        assertThrows(IllegalArgumentException.class, () -> {
            cpioArchiveOutputStream.putNextEntry(null);
        });
    }

    @Test
    public void testPutNextEntry_ValidCpioArchiveEntry() throws Exception {
        String entryName = "testEntry";
        CpioArchiveEntry entry = new CpioArchiveEntry(entryName);
        cpioArchiveOutputStream.putNextEntry(entry);
        assertSame(entry, cpioArchiveOutputStream.getCpioEntry());
    }

    @Test
    public void testWrite_ByteArray() throws Exception {
        String entryName = "testEntry";
        CpioArchiveEntry entry = new CpioArchiveEntry(entryName);
        byte[] testData = "testData".getBytes();
        cpioArchiveOutputStream.putNextEntry(entry);
        cpioArchiveOutputStream.write(testData, 0, testData.length);
        assertEquals(testData.length, cpioArchiveOutputStream.getWritten());
    }

    @Test
    public void testCloseArchiveEntry() throws Exception {
        String entryName = "testEntry";
        CpioArchiveEntry entry = new CpioArchiveEntry(entryName);
        cpioArchiveOutputStream.putNextEntry(entry);
        cpioArchiveOutputStream.write("testData".getBytes());
        cpioArchiveOutputStream.closeArchiveEntry();
        assertNull(cpioArchiveOutputStream.getCpioEntry());
    }

    @Test
    public void testFinish() throws Exception {
        String entryName = "testEntry";
        CpioArchiveEntry entry = new CpioArchiveEntry(entryName);
        cpioArchiveOutputStream.putNextEntry(entry);
        cpioArchiveOutputStream.write("testData".getBytes());
        cpioArchiveOutputStream.finish();
        byte[] testData = outputStream.toByteArray();
        HashMap names = new HashMap();
        CpioArchiveEntry readEntry = (CpioArchiveEntry) CpioArchiveEntry
                .newInstance(new ByteArrayInputStream(testData));
        assertEquals(entryName, readEntry.getName());
        assertFalse(readEntry.isWriteable());
    }

    // Test Cases for Edge Cases and Exception Handling Scenarios

    @Test
    public void testPutNextEntry_DupplicateEntry() throws Exception {
        String entryName = "testEntry";
        CpioArchiveEntry entry1 = new CpioArchiveEntry(entryName);
        cpioArchiveOutputStream.putNextEntry(entry1);
        assertThrows(IllegalArgumentException.class, () -> {
            cpioArchiveOutputStream.putNextEntry(entry1);
        });
    }

    @Test
    public void testPutNextEntry_NullEntryName() throws Exception {
        CpioArchiveEntry entry = new CpioArchiveEntry(null);
        assertThrows(NullPointerException.class, () -> {
            cpioArchiveOutputStream.putNextEntry(entry);
        });
    }

    @Test
    public void testPutNextEntry_EmptyEntryName() throws Exception {
        CpioArchiveEntry entry = new CpioArchiveEntry("");
        cpioArchiveOutputStream.putNextEntry(entry);
        String expectedEntryName = "\u0000";
        assertEquals(expectedEntryName, cpioArchiveOutputStream.getCpioEntry()
                .getName());
    }

    @Test
    public void testPutNextEntry_NegativeFormat() throws Exception {
        short format = (short) (-1);
        assertThrows(IllegalArgumentException.class, () -> {
            cpioArchiveOutputStream.setFormat(format);
        });
    }

    @Test
    public void testWrite_NegativeLength() throws Exception {
        byte[] testData = new byte[10];
        int negativeLength = -5;
        assertThrows(IndexOutOfBoundsException.class, () -> {
            cpioArchiveOutputStream.write(testData, negativeLength, 5);
        });
    }

    @Test
    public void testWrite_NullData() throws Exception {
        assertThrows(NullPointerException.class, () -> {
            cpioArchiveOutputStream.write(null, 0, 0);
        });
    }

    @Test
    public void testClose_StreamAlreadyClosed() throws Exception {
        cpioArchiveOutputStream.close();
        assertThrows(IOException.class, () -> {
            cpioArchiveOutputStream.close();
        });
    }
}
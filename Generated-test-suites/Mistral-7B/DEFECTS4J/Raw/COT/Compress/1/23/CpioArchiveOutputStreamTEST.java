package org.apache.commons.compress.archivers.cpio;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.CpioArchiveEntry;
import org.apache.commons.compress.archivers.CpioConstants;
import org.apache.commons.compress.utils.CpioUtil;
import org.apache.commons.compress.utils.TestUtils;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * Tests for {@link CpioArchiveOutputStream}.
 */
public class CpioArchiveOutputStreamTest {

    private OutputStream out;
    private CpioArchiveOutputStream cao;

    @Before
    public void setUp() throws Exception {
        byte[] testData = "Test data".getBytes();
        out = new ByteArrayOutputStream(testData.length);
        cao = new CpioArchiveOutputStream(out, CpioConstants.FORMAT_NEW);
    }

    @After
    public void tearDown() throws Exception {
        if (cao != null) {
            cao.close();
            cao = null;
        }
        if (out != null) {
            out.close();
            out = null;
        }
    }

    @Test
    public void testConstructor() throws IOException {
        CpioArchiveOutputStream cao = new CpioArchiveOutputStream(new ByteArrayOutputStream(), CpioConstants.FORMAT_NEW);
        assertNotNull(cao);
    }

    @Test
    public void testPutNextEntryWithValidCpioArchiveEntry() throws IOException {
        CpioArchiveEntry entry = new CpioArchiveEntry();
        entry.setName("TestEntry");
        entry.setSize(testData.length);
        cao.putNextEntry(entry);
        assertNotNull(cao.getCpioEntry());
        assertEquals("TestEntry", cao.getCpioEntry().getName());
        assertEquals(testData.length, cao.getCpioEntry().getSize());
    }

    @Test
    public void testPutNextEntryWithNullCpioArchiveEntry() {
        assertThrows(NullPointerException.class, () -> {
            cao.putNextEntry(null);
        });
    }

    @Test
    public void testWriteDataToCpioArchiveEntry() throws IOException {
        CpioArchiveEntry entry = new CpioArchiveEntry();
        entry.setName("TestEntry");
        entry.setSize(testData.length);
        cao.putNextEntry(entry);
        cao.write(testData);
        cao.closeArchiveEntry();

        byte[] actualData = ((ByteArrayOutputStream) out).toByteArray();
        byte[] expectedData = new byte[testData.length + 1];
        System.arraycopy(testData, 0, expectedData, 0, testData.length);
        expectedData[testData.length] = 0;

        assertTrue(TestUtils.byteArraysEqual(actualData, expectedData));
    }

    @Test
    public void testWriteDataToCpioArchiveEntryWithInvalidEntry() throws IOException {
        CpioArchiveEntry entry = new CpioArchiveEntry();
        entry.setName("TestEntry");
        entry.setSize(testData.length);
        cao.putNextEntry(entry);
        cao.write(testData);
        cao.closeArchiveEntry();

        CpioArchiveEntry invalidEntry = new CpioArchiveEntry();
        invalidEntry.setName("TestEntry");
        invalidEntry.setSize(testData.length);

        assertFalse(cao.canWrite());
        assertNull(cao.getCpioEntry());

        try {
            cao.write(testData);
            fail();
        } catch (IOException e) {
            // expected
        }
    }

    @Test
    public void testWriteDataToCpioArchiveEntryWithClosedStream() throws IOException {
        CpioArchiveEntry entry = new CpioArchiveEntry();
        entry.setName("TestEntry");
        entry.setSize(testData.length);
        cao.putNextEntry(entry);
        cao.write(testData);
        cao.closeArchiveEntry();
        cao.close();

        CpioArchiveEntry newEntry = new CpioArchiveEntry();
        newEntry.setName("TestEntry2");
        newEntry.setSize(testData.length);

        assertFalse(cao.canWrite());
        assertNull(cao.getCpioEntry());

        try {
            cao.write(testData);
            fail();
        } catch (IOException e) {
            // expected
        }
    }

    @Test
    public void testSetFormat() throws IOException {
        CpioArchiveOutputStream cao = new CpioArchiveOutputStream(new ByteArrayOutputStream(), (short) 0);
        assertEquals(CpioConstants.FORMAT_UNKNOWN, cao.getFormat());

        cao.setFormat(CpioConstants.FORMAT_NEW);
        assertEquals(CpioConstants.FORMAT_NEW, cao.getFormat());

        cao.setFormat(CpioConstants.FORMAT_NEW_CRC);
        assertEquals(CpioConstants.FORMAT_NEW_CRC, cao.getFormat());

        cao.setFormat(CpioConstants.FORMAT_OLD_ASCII);
        assertEquals(CpioConstants.FORMAT_OLD_ASCII, cao.getFormat());

        cao.setFormat(CpioConstants.FORMAT_OLD_BINARY);
        assertEquals(CpioConstants.FORMAT_OLD_BINARY, cao.getFormat());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidFormat() throws IOException {
        CpioArchiveOutputStream cao = new CpioArchiveOutputStream(new ByteArrayOutputStream(), (short) 0xFF);
    }

    @Test
    public void testGetEntryNames() throws IOException {
        CpioArchiveEntry entry1 = new CpioArchiveEntry();
        entry1.setName("TestEntry1");
        cao.putNextEntry(entry1);
        cao.write(testData);
        cao.closeArchiveEntry();

        CpioArchiveEntry entry2 = new CpioArchiveEntry();
        entry2.setName("TestEntry2");
        entry2.setSize(testData.length);
        cao.putNextEntry(entry2);
        cao.write(testData);
        cao.closeArchiveEntry();

        Map<String, CpioArchiveEntry> expectedMap = new HashMap<>();
        expectedMap.put("TestEntry1", entry1);
        expectedMap.put("TestEntry2", entry2);

        Map<String, CpioArchiveEntry> actualMap = cao.getEntryNames();
        assertEquals(expectedMap, actualMap);
    }

    @Test
    public void testGetEntryNamesWithClosedStream() throws IOException {
        CpioArchiveEntry entry1 = new CpioArchiveEntry();
        entry1.setName("TestEntry1");
        entry1.setSize(testData.length);
        cao.putNextEntry(entry1);
        cao.write(testData);
        cao.closeArchiveEntry();

        CpioArchiveEntry entry2 = new CpioArchiveEntry();
        entry2.setName("TestEntry2");
        entry2.setSize(testData.length);
        cao.putNextEntry(entry2);
        cao.write(testData);
        cao.closeArchiveEntry();
        cao.close();

        Map<String, CpioArchiveEntry> expectedMap = new HashMap<>();
        expectedMap.put("TestEntry1", entry1);
        expectedMap.put("TestEntry2", entry2);

        Map<String, CpioArchiveEntry> actualMap = cao.getEntryNames();
        assertEquals(expectedMap, actualMap);
    }

    @Test
    public void testGetFormat() throws IOException {
        CpioArchiveOutputStream cao = new CpioArchiveOutputStream(new ByteArrayOutputStream(), CpioConstants.FORMAT_NEW);
        assertEquals(CpioConstants.FORMAT_NEW, cao.getFormat());

        cao = new CpioArchiveOutputStream(new ByteArrayOutputStream(), CpioConstants.FORMAT_OLD_ASCII);
        assertEquals(CpioConstants.FORMAT_OLD_ASCII, cao.getFormat());

        cao = new CpioArchiveOutputStream(new ByteArrayOutputStream(), CpioConstants.FORMAT_OLD_BINARY);
        assertEquals(CpioConstants.FORMAT_OLD_BINARY, cao.getFormat());

        cao = new CpioArchiveOutputStream(new ByteArrayOutputStream(), CpioConstants.FORMAT_NEW_CRC);
        assertEquals(CpioConstants.FORMAT_NEW_CRC, cao.getFormat());
    }

    @Test
    public void testFinish() throws IOException {
        CpioArchiveEntry entry1 = new CpioArchiveEntry();
        entry1.setName("TestEntry1");
        entry1.setSize(testData.length);
        cao.putNextEntry(entry1);
        cao.write(testData);
        cao.finish();

        CpioArchiveEntry entry2 = new CpioArchiveEntry();
        entry2.setName("TestEntry2");
        entry2.setSize(testData.length);
        cao.putNextEntry(entry2);
        cao.write(testData);
        cao.finish();

        ByteArrayInputStream input = new ByteArrayInputStream(((ByteArrayOutputStream) out).toByteArray());
        ArchiveEntry entry = new CpioArchiveEntry(input);
        assertEquals(entry1.getName(), entry.getName());
        assertEquals(entry1.getSize(), entry.getSize());

        entry = new CpioArchiveEntry(input);
        assertEquals(entry2.getName(), entry.getName());
        assertEquals(entry2.getSize(), entry.getSize());
    }

    @Test
    public void testFinishWithClosedStream() throws IOException {
        CpioArchiveEntry entry1 = new CpioArchiveEntry();
        entry1.setName("TestEntry1");
        entry1.setSize(testData.length);
        cao.putNextEntry(entry1);
        cao.write(testData);
        cao.finish();
        cao.close();

        CpioArchiveEntry entry2 = new CpioArchiveEntry();
        entry2.setName("TestEntry2");
        entry2.setSize(testData.length);
        cao.putNextEntry(entry2);
        cao.write(testData);
        cao.finish();
        cao.close();

        ByteArrayInputStream input = new ByteArrayInputStream(((ByteArrayOutputStream) out).toByteArray());
        ArchiveEntry entry = new CpioArchiveEntry(input);
        assertEquals(entry1.getName(), entry.getName());
        assertEquals(entry1.getSize(), entry.getSize());

        entry = new CpioArchiveEntry(input);
        assertEquals(entry2.getName(), entry.getName());
        assertEquals(entry2.getSize(), entry.getSize());
    }

    @Test
    public void testClose() throws IOException {
        CpioArchiveEntry entry1 = new CpioArchiveEntry();
        entry1.setName("TestEntry1");
        entry1.setSize(testData.length);
        cao.putNextEntry(entry1);
        cao.write(testData);
        cao.close();

        CpioArchiveEntry entry2 = new CpioArchiveEntry();
        entry2.setName("TestEntry2");
        entry2.setSize(testData.length);
        cao.putNextEntry(entry2);
        cao.write(testData);
        cao.close();

        ByteArrayInputStream input = new ByteArrayInputStream(((ByteArrayOutputStream) out).toByteArray());
        ArchiveEntry entry = null;
        try {
            entry = new CpioArchiveEntry(input);
            fail();
        } catch (IOException e) {
            // expected
        }
        assertNull(entry);
    }
}
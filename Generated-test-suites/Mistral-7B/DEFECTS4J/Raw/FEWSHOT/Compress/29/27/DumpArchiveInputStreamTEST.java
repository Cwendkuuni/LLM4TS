package org.apache.commons.compress.archivers.dump;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.PriorityQueue;
import java.util.Stack;

import org.apache.commons.compress.archivers.ArchiveException;
import org.apache.commons.compress.archivers.dump.DumpArchiveConstants;
import org.apache.commons.compress.archivers.dump.DumpArchiveEntry;
import org.apache.commons.compress.archivers.dump.DumpArchiveInputStream;
import org.apache.commons.compress.archivers.dump.DumpArchiveSummary;
import org.apache.commons.compress.archivers.dump.DumpArchiveUtil;
import org.apache.commons.compress.archivers.zip.ZipEncoding;
import org.apache.commons.compress.archivers.zip.ZipEncodingHelper;
import org.apache.commons.compress.utils.IOUtils;

public class DumpArchiveInputStreamTest {

    private static final String TEST_FILE = "test.dump";

    @Test
    public void testMatches() {
        byte[] bytes = new byte[32];
        Arrays.fill(bytes, (byte) 0x00);
        DumpArchiveInputStream.matches(bytes, 32);
        assertFalse(DumpArchiveInputStream.matches(bytes, 16));
        assertFalse(DumpArchiveInputStream.matches(bytes, 64));
        assertTrue(DumpArchiveInputStream.matches(bytes, 33));
        assertTrue(DumpArchiveInputStream.matches(bytes, 1024));
    }

    @Test
    public void testGetNextEntry() throws IOException, ArchiveException {
        InputStream in = new ByteArrayInputStream(getTestData());
        DumpArchiveInputStream dump = new DumpArchiveInputStream(in, "UTF-8");
        DumpArchiveEntry entry = dump.getNextEntry();
        assertEquals("100644 02 02 212632533125576463616d656e74616c696e6573-13-124326543254-126543373541",
                entry.toString());
        assertNull(dump.getNextEntry());
    }

    @Test
    public void testGetNextEntryWithPending() throws IOException, ArchiveException {
        InputStream in = new ByteArrayInputStream(getTestData());
        DumpArchiveInputStream dump = new DumpArchiveInputStream(in, "UTF-8");
        DumpArchiveEntry entry = dump.getNextEntry();
        assertEquals("100644 02 02 212632533125576463616d656e74616c696e6573-13-124326543254-126543373541",
                entry.toString());
        DumpArchiveEntry next = dump.getNextEntry();
        assertEquals("100644 00 00 0000000000000000000000000000-13-123456789012-126543373541",
                next.toString());
        assertNull(dump.getNextEntry());
    }

    @Test
    public void testGetNextEntryWithEOF() throws IOException, ArchiveException {
        InputStream in = new ByteArrayInputStream(getTestData());
        DumpArchiveInputStream dump = new DumpArchiveInputStream(in, "UTF-8");
        DumpArchiveEntry entry = dump.getNextEntry();
        assertEquals("100644 02 02 212632533125576463616d656e74616c696e6573-13-124326543254-126543373541",
                entry.toString());
        DumpArchiveEntry next = dump.getNextEntry();
        assertEquals("100644 00 00 0000000000000000000000000000-13-123456789012-126543373541",
                next.toString());
        DumpArchiveEntry next2 = dump.getNextEntry();
        assertNull(next2);
    }

    @Test
    public void testGetNextEntryWithRead() throws IOException, ArchiveException {
        InputStream in = new ByteArrayInputStream(getTestData());
        DumpArchiveInputStream dump = new DumpArchiveInputStream(in, "UTF-8");
        DumpArchiveEntry entry = dump.getNextEntry();
        assertEquals("100644 02 02 212632533125576463616d656e74616c696e6573-13-124326543254-126543373541",
                entry.toString());
        DumpArchiveEntry next = dump.getNextEntry();
        assertEquals("100644 00 00 0000000000000000000000000000-13-123456789012-126543373541",
                next.toString());
        byte[] buffer = new byte[512];
        int read = dump.read(buffer, 0, buffer.length);
        assertEquals(42, read);
        DumpArchiveEntry next3 = dump.getNextEntry();
        assertNull(next3);
    }

    @Test
    public void testGetNextEntryWithReadBeyondEnd() throws IOException, ArchiveException {
        InputStream in = new ByteArrayInputStream(getTestData());
        DumpArchiveInputStream dump = new DumpArchiveInputStream(in, "UTF-8");
        DumpArchiveEntry entry = dump.getNextEntry();
        assertEquals("100644 02 02 212632533125576463616d656e74616c696e6573-13-124326543254-126543373541",
                entry.toString());
        DumpArchiveEntry next = dump.getNextEntry();
        assertEquals("100644 00 00 0000000000000000000000000000-13-123456789012-126543373541",
                next.toString());
        byte[] buffer = new byte[512 * 10];
        int read = dump.read(buffer, 0, buffer.length);
        assertEquals(-1, read);
        DumpArchiveEntry next2 = dump.getNextEntry();
        assertNull(next2);
    }

    @Test
    public void testClose() throws IOException, ArchiveException {
        InputStream in = new ByteArrayInputStream(getTestData());
        DumpArchiveInputStream dump = new DumpArchiveInputStream(in, "UTF-8");
        DumpArchiveEntry entry = dump.getNextEntry();
        DumpArchiveEntry next = dump.getNextEntry();
        dump.close();
        DumpArchiveEntry next2 = dump.getNextEntry();
        assertNull(next2);
    }

    @Test
    public void testRead() throws IOException, ArchiveException {
        InputStream in = new ByteArrayInputStream(getTestData());
        DumpArchiveInputStream dump = new DumpArchiveInputStream(in, "UTF-8");
        DumpArchiveEntry entry = dump.getNextEntry();
        byte[] buffer = new byte[entry.getEntrySize()];
        int read = dump.read(buffer, 0, buffer.length);
        assertArrayEquals(new byte[] { 0x13, 0x12, 0x34, 0x56, 0x78, 0x90 }, Arrays.copyOfRange(buffer, 0, 6));
        dump.close();
    }

    @Test(expected = IOException.class)
    public void testReadBeyondEnd() throws IOException, ArchiveException {
        InputStream in = new ByteArrayInputStream(getTestData());
        DumpArchiveInputStream dump = new DumpArchiveInputStream(in, "UTF-8");
        DumpArchiveEntry entry = dump.getNextEntry();
        byte[] buffer = new byte[entry.getEntrySize() * 2];
        int read = dump.read(buffer, 0, buffer.length);
    }

    @Test(expected = ArchiveException.class)
    public void testVerifyInvalidHeader() throws IOException, ArchiveException {
        InputStream in = new ByteArrayInputStream(new byte[] { 0x13, 0x12 });
        try {
            DumpArchiveInputStream dump = new DumpArchiveInputStream(in, "UTF-8");
            dump.getNextEntry();
        } finally {
            IOUtils.closeQuietly(in);
        }
    }

    @Test(expected = ArchiveException.class)
    public void testVerifyInvalidFormat() throws IOException, ArchiveException {
        InputStream in = new ByteArrayInputStream(new byte[] { 0x13, 0x12, 0x21, 0x21, 0x12, 0x12 });
        try {
            DumpArchiveInputStream dump = new DumpArchiveInputStream(in, "UTF-8");
            dump.getNextEntry();
        } finally {
            IOUtils.closeQuietly(in);
        }
    }

    @Test
    public void testGetSummary() throws IOException, ArchiveException {
        InputStream in = new ByteArrayInputStream(getTestData());
        DumpArchiveInputStream dump = new DumpArchiveInputStream(in, "UTF-8");
        DumpArchiveSummary sum = dump.getSummary();
        assertEquals(2, sum.getNumberOfFiles());
        assertEquals("test.dump", sum.getName());
        dump.close();
    }

    @Test
    public void testGetSummaryWithInvalidFormat() throws IOException {
        InputStream in = new ByteArrayInputStream(new byte[] { 0x13, 0x12, 0x21, 0x00 });
        try {
            DumpArchiveInputStream dump = new DumpArchiveInputStream(in, "UTF-8");
            DumpArchiveSummary sum = dump.getSummary();
        } finally {
            IOUtils.closeQuietly(in);
        }
        assertNull(sum);
    }

    private byte[] getTestData() {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        out.write(new byte[] { 0x13, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x12, 0x34, 0x56, 0x78, 0x90,
                0x10, 0x02, 0x02, 0x21, 0x26, 0x32, 0x53, 0x31, 0x25, 0x57, 0x64, 0x63, 0x61, 0x6d, 0x65,
                0x6e, 0x74, 0x61, 0x6c, 0x69, 0x6e, 0x65, 0x73, 0x21, 0x2d, 0x31, 0x33, 0x2d, 0x31, 0x32, 0x36,
                0x35, 0x34, 0x32, 0x36, 0x35, 0x34, 0x33, 0x37, 0x35, 0x34, 0x31, 0x00, 0x12, 0x00, 0x00,
                0x00, 0x00, 0x13, 0x00, 0x00, 0x00, 0x00, 0x12, 0x34, 0x56, 0x78, 0x90, 0x10, 0x00, 0x00, 0x00,
                0x00, 0x12, 0x34, 0x56, 0x78, 0x90, 0x07, 0x02, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00,
                0x00, 0x13, 0x12 });
        return out.toByteArray();
    }
}
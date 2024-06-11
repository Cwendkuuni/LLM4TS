package org.apache.commons.compress.archivers.dump;

import org.apache.commons.compress.archivers.ArchiveException;
import org.apache.commons.compress.archivers.ArchiveInputStream;
import org.apache.commons.compress.archivers.zip.ZipEncoding;
import org.apache.commons.compress.archivers.zip.ZipEncodingHelper;
import org.apache.commons.compress.utils.DumpArchiveConstants;
import org.apache.commons.compress.utils.DumpArchiveUtil;
import org.apache.commons.compress.utils.TapeInputStream;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Stack;

public class DumpArchiveInputStreamTest {

    private ArchiveInputStream input;
    private Queue<DumpArchiveEntry> queue;
    private Map<Integer, Dirent> names;
    private Map<Integer, DumpArchiveEntry> pending;
    private ZipEncoding zipEncoding;

    @Before
    public void setUp() throws IOException, ArchiveException {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        OutputStream rawOutput = output;
        byte[] magicBytes = DumpArchiveUtil.magicBytes;
        byte[] headerBytes = DumpArchiveUtil.createMagicHeader(magicBytes);
        byte[] clriBytes = DumpArchiveUtil.createCLRI(2, 10, 3, 1234, DumpArchiveConstants.SEGMENT_TYPE.CLRI);
        byte[] bitsBytes = DumpArchiveUtil.createBITS(4, 15, 5, 5678, DumpArchiveConstants.SEGMENT_TYPE.BITS);
        byte[] addressBytes = DumpArchiveUtil.createAddress(
                1, 1, 1, 1234, DumpArchiveConstants.SEGMENT_TYPE.ADDR);
        byte[] inodeBytes = DumpArchiveUtil.createINODE(
                1, 1, 1, "dir1", 1024, 1, 1, 1, 1, 1, DumpArchiveConstants.SEGMENT_TYPE.INODE);
        byte[] dataBytes = DumpArchiveUtil.createEmptyData();

        // Write header, CLRI, BITS, and first INODE record
        rawOutput.write(headerBytes);
        rawOutput.write(clriBytes);
        rawOutput.write(bitsBytes);
        rawOutput.write(addressBytes);
        rawOutput.write(inodeBytes);
        rawOutput.write(dataBytes);

        // Write a directory entry
        byte[] direntBytes = DumpArchiveUtil.createDirent(1, 1, 0, "file1.txt", 1024, DumpArchiveConstants.SEGMENT_TYPE.INODE);
        rawOutput.write(direntBytes);

        // Write remaining INODE records
        for (int i = 0; i < 5; i++) {
            rawOutput.write(addressBytes);
            rawOutput.write(inodeBytes);
        }

        // Write the address record for the last INODE
        rawOutput.write(addressBytes);

        // Seek past the EOF marker
        rawOutput.skip(DumpArchiveConstants.TP_SIZE);

        ByteArrayInputStream inputStream = new ByteArrayInputStream(output.toByteArray());
        this.input = new DumpArchiveInputStream(inputStream, null);
        this.queue = new PriorityQueue<>(10, new Comparator<DumpArchiveEntry>() {
            public int compare(DumpArchiveEntry p, DumpArchiveEntry q) {
                if (p.getOriginalName() == null || q.getOriginalName() == null) {
                    return Integer.MAX_VALUE;
                }
                return p.getOriginalName().compareTo(q.getOriginalName());
            }
        });
        this.names = new HashMap<>();
        this.pending = new HashMap<>();
        this.zipEncoding = ZipEncodingHelper.getZipEncoding(null);
    }

    @Test
    public void testMatches() {
        byte[] testData = new byte[DumpArchiveConstants.TP_SIZE];
        Assert.assertTrue(DumpArchiveInputStream.matches(testData, 0));
    }

    @Test
    public void testConstructor() throws IOException, ArchiveException {
        Assert.assertNotNull(input);
    }

    @Test
    public void testGetNextEntry() throws IOException, ArchiveException {
        DumpArchiveEntry entry = input.getNextEntry();
        Assert.assertEquals("file1.txt", entry.getName());
        Assert.assertEquals("dir1/file1.txt", entry.getFullName());

        entry = input.getNextEntry();
        Assert.assertNull(entry);
    }

    @Test(expected = IOException.class)
    public void testGetNextEntryWithEOF() throws IOException, ArchiveException {
        input.close();
        input.getNextEntry();
    }

    @Test
    public void testGetSummary() throws IOException, ArchiveException {
        DumpArchiveSummary summary = input.getSummary();
        Assert.assertEquals(1, summary.getBlockSize());
        Assert.assertEquals(12, summary.getRecordSize());
        Assert.assertEquals(10, summary.getNumberOfRecords());
    }

    @Test
    public void testReadDirectoryEntry() throws IOException, ArchiveException {
        DumpArchiveEntry entry = input.getNextEntry();
        String path = input.getPath(entry);
        Assert.assertEquals("file1.txt", path);

        entry = input.getNextEntry();
        Assert.assertNull(path);
    }

    @Test
    public void testGetPath() throws IOException, ArchiveException {
        DumpArchiveEntry entry = input.getNextEntry();
        String path = input.getPath(entry);
        Assert.assertEquals("dir1/file1.txt", path);

        entry = input.getNextEntry();
        Assert.assertNull(path);
    }

    @Test
    public void testRead() throws IOException, ArchiveException {
        byte[] testBuffer = new byte[10];
        int bytesRead = input.read(testBuffer);
        Assert.assertEquals(10, bytesRead);

        bytesRead = input.read(testBuffer, 3, 7);
        Assert.assertEquals(7, bytesRead);
    }

    @Test(expected = IOException.class)
    public void testReadWithEOF() throws IOException, ArchiveException {
        input.close();
        byte[] testBuffer = new byte[1];
        input.read(testBuffer);
    }

    @Test
    public void testClose() throws IOException, ArchiveException {
        input.close();
    }
}
package org.apache.commons.compress.archivers.dump;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.apache.commons.compress.archivers.ArchiveException;
import org.apache.commons.compress.archivers.ArchiveInputStream;
import org.apache.commons.compress.archivers.zip.ZipEncoding;
import org.apache.commons.compress.archivers.zip.ZipEncodingHelper;
import org.apache.commons.compress.utils.IOUtils;
import org.apache.commons.compress.utils.TestUtils;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Collections;

/**
 * Test cases for {@link DumpArchiveInputStream}.
 */
public class DumpArchiveInputStreamTest {
    @Test
    public void testemptyStream() throws IOException, ArchiveException {
        InputStream is = new ByteArrayInputStream(new byte[0]);
        ArchiveInputStream ais = new DumpArchiveInputStream(is);
        ais.close();
        assertTrue(ais.getNextEntry() == null);
    }

    @Test
    public void testInvalidMagic() throws IOException, ArchiveException {
        byte[] magicBytes = new byte[32];
        Arrays.fill(magicBytes, (byte) 0x41);
        InputStream is = new ByteArrayInputStream(magicBytes);
        try {
            new DumpArchiveInputStream(is);
            fail();
        } catch (UnrecognizedFormatException e) {
            // expected
        }
    }

    @Test
    public void testValidMagic() throws IOException, ArchiveException {
        byte[] magicBytes = new byte[32];
        Arrays.fill(magicBytes, (byte) 0x4d, 0, 8, (byte) 0x53);
        InputStream is = new ByteArrayInputStream(magicBytes);
        DumpArchiveInputStream ais = new DumpArchiveInputStream(is, null);
        assertEquals(DumpArchiveConstants.NFS_MAGIC, ais.getSummary().getMagic());
        ais.close();
    }

    @Test
    public void testReadInvalidHeaders() throws IOException, ArchiveException {
        byte[] headerBytes = new byte[DumpArchiveConstants.TP_SIZE];
        Arrays.fill(headerBytes, (byte) 0);
        InputStream is = new ByteArrayInputStream(headerBytes);
        try {
            new DumpArchiveInputStream(is);
            fail();
        } catch (InvalidFormatException e) {
            // expected
        }
    }

    @Test
    public void testReadValidHeaders() throws IOException, ArchiveException {
        byte[] headerBytes = new byte[DumpArchiveConstants.TP_SIZE];
        headerBytes[24] = (byte) DumpArchiveConstants.NFS_MAGIC;
        InputStream is = new ByteArrayInputStream(headerBytes);
        DumpArchiveInputStream ais = new DumpArchiveInputStream(is, null);
        assertEquals(DumpArchiveConstants.NFS_MAGIC, ais.getSummary().getMagic());
        ais.close();
    }

    @Test
    public void testReadCLRI() throws IOException, ArchiveException {
        InputStream is = getTestDataInputStream("testdata/testCLRI.dump");
        DumpArchiveInputStream ais = new DumpArchiveInputStream(is, null);
        DumpArchiveEntry entry = ais.getNextEntry();
        assertEquals(".", entry.getName());
        assertEquals(".", entry.getSimpleName());
        assertEquals(0, entry.getOffset());
        ais.close();
    }

    @Test
    public void testReadBITS() throws IOException, ArchiveException {
        InputStream is = getTestDataInputStream("testdata/testBITS.dump");
        DumpArchiveInputStream ais = new DumpArchiveInputStream(is, null);
        DumpArchiveEntry entry = ais.getNextEntry();
        assertEquals(".", entry.getName());
        assertEquals(".", entry.getSimpleName());
        assertEquals(0, entry.getOffset());
        ais.close();
    }

    @Test
    public void testReadValidEntry() throws IOException, ArchiveException {
        InputStream is = getTestDataInputStream("testdata/testValidEntry.dump");
        DumpArchiveInputStream ais = new DumpArchiveInputStream(is, null);
        DumpArchiveEntry entry = ais.getNextEntry();
        assertEquals("bin/", entry.getPath());
        assertEquals("bin", entry.getSimpleName());
        assertEquals(1024L, entry.getOffset());
        assertEquals("testdata/testValidEntry.dump:bin:\n", entry.toString());
        ais.close();
    }

    @Test
    public void testReadMultipleEntries() throws IOException, ArchiveException {
        InputStream is = getTestDataInputStream("testdata/testMultipleEntries.dump");
        DumpArchiveInputStream ais = new DumpArchiveInputStream(is, null);
        DumpArchiveEntry entry1 = ais.getNextEntry();
        assertEquals("bin/", entry1.getPath());
        assertEquals("bin", entry1.getSimpleName());
        assertEquals(1024L, entry1.getOffset());
        DumpArchiveEntry entry2 = ais.getNextEntry();
        assertEquals("etc/", entry2.getPath());
        assertEquals("etc", entry2.getSimpleName());
        assertEquals(2048L, entry2.getOffset());
        DumpArchiveEntry entry3 = ais.getNextEntry();
        assertEquals("usr/", entry3.getPath());
        assertEquals("usr", entry3.getSimpleName());
        assertEquals(3072L, entry3.getOffset());
        DumpArchiveEntry entry4 = ais.getNextEntry();
        assertEquals("usr/bin/", entry4.getPath());
        assertEquals("usr/bin", entry4.getSimpleName());
        assertEquals(32768L, entry4.getOffset());
        DumpArchiveEntry entry5 = ais.getNextEntry();
        assertEquals("usr/sbin/", entry5.getPath());
        assertEquals("usr/sbin", entry5.getSimpleName());
        assertEquals(524288L, entry5.getOffset());
        ais.close();
    }

    @Test
    public void testReadEndOfArchive() throws IOException, ArchiveException {
        InputStream is = getTestDataInputStream("testdata/testEndOfArchive.dump");
        DumpArchiveInputStream ais = new DumpArchiveInputStream(is, null);
        DumpArchiveEntry entry1 = ais.getNextEntry();
        assertEquals("bin/", entry1.getPath());
        assertEquals("bin", entry1.getSimpleName());
        assertEquals(1024L, entry1.getOffset());
        DumpArchiveEntry entry2 = ais.getNextEntry();
        assertEquals("etc/", entry2.getPath());
        assertEquals("etc", entry2.getSimpleName());
        assertEquals(2048L, entry2.getOffset());
        DumpArchiveEntry entry3 = ais.getNextEntry();
        assertEquals("usr/", entry3.getPath());
        assertEquals("usr", entry3.getSimpleName());
        assertEquals(3072L, entry3.getOffset());
        DumpArchiveEntry entry4 = ais.getNextEntry();
        assertEquals("usr/bin/", entry4.getPath());
        assertEquals("usr/bin", entry4.getSimpleName());
        assertEquals(32768L, entry4.getOffset());
        DumpArchiveEntry entry5 = ais.getNextEntry();
        assertEquals("usr/sbin/", entry5.getPath());
        assertEquals("usr/sbin", entry5.getSimpleName());
        assertEquals(524288L, entry5.getOffset());
        DumpArchiveEntry entry6 = ais.getNextEntry();
        assertNull(entry6);
        ais.close();
    }

    @Test
    public void testReadSparseFile() throws IOException, ArchiveException {
        InputStream is = getTestDataInputStream("testdata/testSparseFile.dump");
        DumpArchiveInputStream ais = new DumpArchiveInputStream(is, null);
        DumpArchiveEntry entry = ais.getNextEntry();
        assertEquals("file", entry.getName());
        assertEquals("file", entry.getSimpleName());
        assertEquals(0, entry.getOffset());
        assertEquals(12, entry.getSize());
        byte[] buffer = new byte[13];
        int count = ais.read(buffer);
        assertEquals(12, count);
        assertEquals("file", new String(Arrays.copyOfRange(buffer, 0, 6),
                ZipEncodingHelper.UTF8));
        assertEquals(0, new String(Arrays.copyOfRange(buffer, 6, 12),
                ZipEncodingHelper.UTF8));
        byte[] expected = new byte[13];
        System.arraycopy(TestUtils.getTestResource("testdata/testSparseFile.txt"), 0,
                expected, 0, 13);
        byte[] actual = new byte[13];
        int len = ais.read(actual);
        assertEquals(len, 12);
        assertArrayEquals(expected, Arrays.copyOfRange(actual, 0, 12));
        ais.close();
    }

    @Test
    public void testReadInvalidBlock() throws IOException, ArchiveException {
        InputStream is = getTestDataInputStream("testdata/testInvalidBlock.dump");
        DumpArchiveInputStream ais = new DumpArchiveInputStream(is, null);
        DumpArchiveEntry entry = ais.getNextEntry();
        assertEquals(".", entry.getName());
        assertEquals(".", entry.getSimpleName());
        assertEquals(0, entry.getOffset());
        // read the valid block
        byte[] buffer = new byte[13];
        int len = ais.read(buffer);
        assertEquals(12, len);
        assertArrayEquals(new byte[]{0x66, 0x69, 0x6c, 0x65, 0x20, 0x73, 0x68,
                0x61, 0x73, 0x74, 0x65, 0x64}, buffer, 0, 12);
        // read the invalid block
        len = ais.read(buffer);
        assertEquals(-1, len);
        ais.close();
    }

    @Test
    public void testReadEmptyBlock() throws IOException, ArchiveException {
        InputStream is = getTestDataInputStream("testdata/testEmptyBlock.dump");
        DumpArchiveInputStream ais = new DumpArchiveInputStream(is, null);
        DumpArchiveEntry entry = ais.getNextEntry();
        assertEquals(".", entry.getName());
        assertEquals(".", entry.getSimpleName());
        assertEquals(0, entry.getOffset());
        byte[] buffer = new byte[1];
        int len = ais.read(buffer);
        assertEquals(0, len);
        ais.close();
    }

    @Test
    public void testReadExtraBytes() throws IOException, ArchiveException {
        InputStream is = getTestDataInputStream("testdata/testExtraBytes.dump");
        DumpArchiveInputStream ais = new DumpArchiveInputStream(is, null);
        DumpArchiveEntry entry = ais.getNextEntry();
        assertEquals(".", entry.getName());
        assertEquals(".", entry.getSimpleName());
        assertEquals(0, entry.getOffset());
        byte[] buffer = new byte[13];
        int len = ais.read(buffer);
        assertEquals(12, len);
        assertArrayEquals(new byte[]{0x66, 0x69, 0x6c, 0x65, 0x20, 0x73, 0x68,
               0x61, 0x73, 0x74, 0x65, 0x64}, buffer, 0, 12);
        // read extra bytes
        len = ais.read(buffer);
        assertEquals(0, len);
        ais.close();
    }

    @Test
    public void testReadInvalidEntry() throws IOException, ArchiveException {
        InputStream is = getTestDataInputStream("testdata/testInvalidEntry.dump");
        DumpArchiveInputStream ais = new DumpArchiveInputStream(is, null);
        DumpArchiveEntry entry = ais.getNextEntry();
        assertEquals(".", entry.getName());
        assertEquals(".", entry.getSimpleName());
        assertEquals(0, entry.getOffset());
        ais.close();
    }

    @Test
    public void testReadEndOfStream() throws IOException, ArchiveException {
        InputStream is = getTestDataInputStream("testdata/testEndOfStream.dump");
        DumpArchiveInputStream ais = new DumpArchiveInputStream(is, null);
        DumpArchiveEntry entry = ais.getNextEntry();
        assertEquals(".", entry.getName());
        assertEquals(".", entry.getSimpleName());
        assertEquals(0, entry.getOffset());
        byte[] buffer = new byte[1];
        int len = ais.read(buffer);
        assertEquals(-1, len);
        ais.close();
    }

    @Test
    public void testReadInvalidFormat() throws IOException, ArchiveException {
        InputStream is = new ByteArrayInputStream(new byte[0]);
        try {
            new DumpArchiveInputStream(is);
            fail();
        } catch (UnrecognizedFormatException e) {
            // expected
        }
        ais.close();
    }

    @Test
    public void testReadEOF() throws IOException, ArchiveException {
        InputStream is = getTestDataInputStream("testdata/testEOF.dump");
        DumpArchiveInputStream ais = new DumpArchiveInputStream(is, null);
        DumpArchiveEntry entry = ais.getNextEntry();
        assertEquals(".", entry.getName());
        assertEquals(".", entry.getSimpleName());
        assertEquals(0, entry.getOffset());
        byte[] buffer = new byte[1];
        int len = ais.read(buffer);
        assertEquals(-1, len);
        ais.close();
    }

    @Test
    public void testReadClosedStream() throws IOException, ArchiveException {
        InputStream is = getTestDataInputStream("testdata/testCLRI.dump");
        DumpArchiveInputStream ais = new DumpArchiveInputStream(is, null);
        DumpArchiveEntry entry = ais.getNextEntry();
        assertEquals(".", entry.getName());
        assertEquals(".", entry.getSimpleName());
        assertEquals(0, entry.getOffset());
        ais.close();
        int len = ais.read(new byte[1]);
        assertEquals(-1, len);
    }

    private InputStream getTestDataInputStream(String resourceName) throws IOException {
        return new ByteArrayInputStream(
                TestUtils.getTestResource(resourceName));
    }
}
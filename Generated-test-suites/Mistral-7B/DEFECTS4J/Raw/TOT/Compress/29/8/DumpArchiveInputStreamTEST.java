package org.apache.commons.compress.archivers.dump;

import org.apache.commons.compress.archivers.ArchiveException;
import org.apache.commons.compress.archivers.ArchiveInputStream;
import org.apache.commons.compress.archivers.zip.ZipEncoding;
import org.apache.commons.compress.archivers.zip.ZipEncodingHelper;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Test class for DumpArchiveInputStream.
 */
public class DumpArchiveInputStreamTest {

    private InputStream is;
    private DumpArchiveInputStream inputStream;
    private ZipEncoding encoding;

    @Before
    public void setUp() throws Exception {
        byte[] data = new byte[] {
                0x00, 0x00, 0x00, 0x00, // Dump Archive Magic Number
                0x00, 0x00, 0x00, 0x00, // Endianess
                0x00, 0x00,              // Version
                0x02, 0x01,              // Number of header records
                0x01, 0x01,              // Number of file records
                0x00, 0x00, 0x00, 0x00, 0x00, 0x00, // File Record 1:
                0x01, 0x00, 0x00, 0x01, //   File Type: Dump File
                0x00, 0x00, 0x00, 0x01, //   File Length: 1
                0x01, 0x00, 0x00, 0x01, //   File Offset: 1
                0x00, 0x00, 0x00, 0x01, //   File Inode: 1
                0x00, 0x00, 0x00, 0x01, //   File Name length: 1
                0x00, 0x01, //           File Name: "a"
                0x01, 0x00, 0x00, 0x02, //
                0x01, 0x00, 0x00, 0x02, //   File Type: Regular File
                0x00, 0x00, 0x00, 0x04, //   File Length: 4
                0x01, 0x00, 0x00, 0x02, //   File Offset: 2
                0x00, 0x00, 0x00, 0x02, //   File Inode: 2
                0x00, 0x00, 0x00, 0x03, //   File Name length: 3
                0x00, 0x03, //             File Name: "abc"
        };

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        outputStream.write(data, 0, data.length);
        InputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
        this.is = inputStream;
        this.inputStream = new DumpArchiveInputStream(is, "UTF-8");
        this.encoding = ZipEncodingHelper.getZipEncoding("UTF-8");
    }

    @Test
    public void testGetNextEntry() throws IOException {
        DumpArchiveEntry entry = inputStream.getNextEntry();
        Assert.assertEquals("a", entry.getName());
        Assert.assertEquals(".", entry.getSimpleName());
        Assert.assertEquals(1, entry.getSize());
        Assert.assertEquals(1, entry.getOffset());
        Assert.assertEquals(1, entry.getFileIndex());
        entry = inputStream.getNextEntry();
        Assert.assertEquals("abc", entry.getName());
        Assert.assertEquals("abc", entry.getSimpleName());
        Assert.assertEquals(4, entry.getSize());
        Assert.assertEquals(2, entry.getOffset());
        Assert.assertEquals(2, entry.getFileIndex());
        entry = inputStream.getNextEntry();
        Assert.assertNull(entry);
    }

    @Test(expected = IOException.class)
    public void testReadPastEndOfStream() throws IOException {
        byte[] buf = new byte[10];
        int bytesRead = inputStream.read(buf, 0, buf.length);
        Assert.assertEquals(4, bytesRead);
        bytesRead = inputStream.read(buf, 0, buf.length);
        Assert.assertEquals(-1, bytesRead);
    }

    @Test(expected = IOException.class)
    public void testReadWithWrongEncoding() throws IOException {
        DumpArchiveInputStream inputStreamWrongEncoding = new DumpArchiveInputStream(is, "ISO-8859-1");
        byte[] buf = new byte[10];
        int bytesRead = inputStreamWrongEncoding.read(buf, 0, buf.length);
        Assert.fail();
    }

    @Test
    public void testRead() throws IOException {
        byte[] readBuf = new byte[4];
        int bytesRead = inputStream.read(readBuf, 0, readBuf.length);
        Assert.assertEquals(4, bytesRead);
        Assert.assertArrayEquals(new byte[]{0x01, 0x00, 0x00, 0x01}, readBuf);
        bytesRead = inputStream.read(readBuf, 0, readBuf.length);
        Assert.assertEquals(3, bytesRead);
        Assert.assertArrayEquals(new byte[]{"a", 0x00, 0x01}, readBuf);
        bytesRead = inputStream.read(readBuf, 0, readBuf.length);
        Assert.assertEquals(-1, bytesRead);
    }

    @Test
    public void testClose() throws IOException {
        inputStream.close();
        byte[] buf = new byte[10];
        int bytesRead = inputStream.read(buf, 0, buf.length);
        Assert.assertEquals(-1, bytesRead);
    }
}
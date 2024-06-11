import org.apache.commons.compress.archivers.ArchiveInputStream;
import org.apache.commons.compress.archivers.dump.DumpArchiveConstants;
import org.apache.commons.compress.archivers.dump.DumpArchiveEntry;
import org.apache.commons.compress.archivers.dump.DumpArchiveInputStream;
import org.apache.commons.compress.archivers.dump.DumpArchiveSummary;
import org.apache.commons.compress.archivers.zip.ZipEncoding;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.PriorityQueue;

/**
 * Test class for DumpArchiveInputStream
 */
public class DumpArchiveInputStreamTest {

    private DumpArchiveInputStream inputStream;
    private ByteArrayOutputStream byteArrayOutputStream;
    private ByteArrayInputStream byteArrayInputStream;
    private ZipEncoding zipEncoding;

    @Before
    public void setUp() throws IOException {
        byteArrayOutputStream = new ByteArrayOutputStream();
        byteArrayInputStream = new ByteArrayInputStream(byteArrayOutputStream.toByteArray());
        zipEncoding = new ZipEncoding("UTF-8");

        byte[] headerBytes = new byte[DumpArchiveConstants.TP_SIZE];
        Arrays.fill(headerBytes, (byte) 0x00);
        headerBytes[0] = (byte) DumpArchiveConstants.MAGIC_NUMBER;
        headerBytes[1] = (byte) DumpArchiveConstants.VERSION_MAJOR;
        headerBytes[2] = (byte) DumpArchiveConstants.VERSION_MINOR;

        inputStream = new DumpArchiveInputStream(byteArrayInputStream, zipEncoding) {
            @Override
            public DumpArchiveSummary getSummary() {
                return new DumpArchiveSummary(headerBytes, zipEncoding);
            }
        };
        inputStream.skip(DumpArchiveConstants.TP_SIZE);
        inputStream.readRecord(); // CLRI
        inputStream.readRecord(); // BITS
        inputStream.readRecord(); // ADD

        inputStream.readCLRI();
        inputStream.readBITS();
    }

    @Test
    public void testGetNextEntry() throws IOException {
        DumpArchiveEntry entry = inputStream.getNextEntry();
        Assert.assertNotNull(entry);
        Assert.assertEquals(DumpArchiveConstants.SEGMENT_TYPE.ADDR, entry.getHeaderType());
        Assert.assertEquals(3, entry.getHeaderCount());

        inputStream.getNextEntry(); // skip ADD
        entry = inputStream.getNextEntry();
        Assert.assertNotNull(entry);
        Assert.assertEquals(DumpArchiveConstants.SEGMENT_TYPE.INODE, entry.getHeaderType());
        Assert.assertEquals(10, entry.getHeaderCount());

        for (int i = 0; i < entry.getHeaderCount(); i++) {
            if (i % 2 == 0) {
                Assert.assertEquals(DumpArchiveConstants.TP_SIZE, entry.getFileOffset());
                Assert.assertEquals(DumpArchiveConstants.TP_SIZE, entry.getEntrySize());
                Assert.assertEquals(0, entry.getHeaderHoles());
                Assert.assertEquals(1, entry.getHeaderLink());
                Assert.assertEquals(1, entry.getFileType());
                Assert.assertEquals(1, entry.getMode());
                Assert.assertEquals(1, entry.getInode());
                Assert.assertEquals(1, entry.getParentIno());
                Assert.assertEquals('f', entry.getNameType());
                Assert.assertEquals("file1.txt", entry.getName());
                Assert.assertEquals("file1", entry.getSimpleName());
                Assert.assertEquals(0, entry.getFormat());

                inputStream.skip(DumpArchiveConstants.TP_SIZE);
                inputStream.readRecord();
            } else {
                Assert.assertEquals(DumpArchiveConstants.TP_SIZE, entry.getFileOffset());
                Assert.assertEquals(1, entry.getHeaderHoles());
                Assert.assertEquals(1, entry.getHeaderLink());
                Assert.assertEquals(2, entry.getFileType());
                Assert.assertEquals(1, entry.getMode());
                Assert.assertEquals(2, entry.getInode());
                Assert.assertEquals(1, entry.getParentIno());
                Assert.assertEquals('d', entry.getNameType());
                Assert.assertEquals("dir1", entry.getName());
                Assert.assertEquals("dir", entry.getSimpleName());
                Assert.assertEquals(0, entry.getFormat());

                inputStream.skip(DumpArchiveConstants.TP_SIZE);
            }
            entry = inputStream.getNextEntry();
        }

        Assert.assertNull(inputStream.getNextEntry());
    }

    @Test(expected = ArchiveException.class)
    public void testInvalidFormat() throws IOException {
        byte[] invalidHeaderBytes = Arrays.copyOfRange(headerBytes, 1, headerBytes.length);
        headerBytes[0] = (byte) 0xaa;

        ByteArrayOutputStream invalidByteArrayOutputStream = new ByteArrayOutputStream();
        ByteArrayInputStream invalidByteArrayInputStream = new ByteArrayInputStream(invalidByteArrayOutputStream.toByteArray());

        DumpArchiveInputStream invalidInputStream = new DumpArchiveInputStream(invalidByteArrayInputStream, zipEncoding);
        invalidInputStream.readRecord();
        invalidInputStream.readRecord();
        invalidInputStream.readRecord();

        invalidInputStream.getNextEntry();
    }

    @Test(expected = EOFException.class)
    public void testEndOfStream() throws IOException {
        inputStream.getNextEntry();
        inputStream.getNextEntry();
        inputStream.getNextEntry();

        DumpArchiveEntry entry = inputStream.getNextEntry();
        Assert.assertNull(entry);
    }

    @Test(expected = IOException.class)
    public void testReadBeyondEndOfEntry() throws IOException {
        DumpArchiveEntry entry = inputStream.getNextEntry();
        int read = inputStream.read(new byte[1]);
        Assert.assertEquals(-1, read);
    }
}
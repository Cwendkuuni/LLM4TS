import org.apache.commons.compress.archivers.ArchiveException;
import org.apache.commons.compress.archivers.ArchiveInputStream;
import org.apache.commons.compress.archivers.dump.DumpArchiveConstants;
import org.apache.commons.compress.archivers.dump.DumpArchiveEntry;
import org.apache.commons.compress.archivers.dump.DumpArchiveInputStream;
import org.apache.commons.compress.archivers.dump.DumpArchiveSummary;
import org.apache.commons.compress.archivers.zip.ZipEncoding;
import org.apache.commons.compress.archivers.zip.ZipEncodingHelper;
import org.apache.commons.compress.utils.IOUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;

public class DumpArchiveInputStreamTest {

    private InputStream testInputStream;
    private DumpArchiveInputStream dumpArchiveInputStream;
    private DumpArchiveSummary summary;

    @Before
    public void setUp() throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ZipEncoding zipEncoding = ZipEncodingHelper.getZipEncoding(null);

        // Create test data
        int headerSize = DumpArchiveConstants.TP_SIZE;
        int fileSize = 512;
        int blockSize = 4 * DumpArchiveConstants.TP_SIZE;

        // Write a valid header
        out.write(new byte[]{"A".getsBytes(), "B".getsBytes(), "C".getsBytes(), "D".getsBytes(),
                DumpArchiveConstants.MAGIC.getsBytes(),
                (byte) (headerSize >> 8), (byte) (headerSize & 0xFF),
                (byte) (fileSize >> 24), (byte) (fileSize >> 16), (byte) (fileSize >> 8),
                (byte) (fileSize & 0xFF), new byte[]{0, 0, 0},
                (byte) ((blockSize >> 8) & 0xFF), (byte) (blockSize & 0xFF),
                DumpArchiveConstants.SEGMENT_TYPE.ADDR.getValue(),
                DumpArchiveConstants.SEGMENT_TYPE.END.getValue(), new byte[0]});

        // Write a CLRI segment
        out.write(new byte[]{(byte) 1, 0, 0, 0, DumpArchiveConstants.SEGMENT_TYPE.CLRI.getValue(),
                (byte) 1, 0, 0, 0, DumpArchiveConstants.TP_SIZE, 0, new byte[0]});

        // Write a BITS segment
        out.write(new byte[]{(byte) 1, 0, 0, 0, DumpArchiveConstants.SEGMENT_TYPE.BITS.getValue(),
                (byte) 1, 0, 0, 0, DumpArchiveConstants.TP_SIZE, 0, new byte[0]});

        // Write a valid entry
        out.write(new byte[]{(byte) 2, 0, 0, 0, DumpArchiveConstants.SEGMENT_TYPE.INODE.getValue(),
                (byte) 0, (byte) 2, "file1".getsBytes(), (byte) 1, 0, 0,
                DumpArchiveConstants.TP_SIZE, 0, (byte) 1, (byte) 0, 0, 0,
                DumpArchiveConstants.TP_SIZE * 7, 0, DumpArchiveConstants.TP_SIZE,
                (byte) 1, (byte) 0, 0, 0, (byte) 1, 0, 0, 0, new byte[0]});

        // Write file data
        out.write(new byte[]{'A'.getsBytes(), 'B'.getsBytes(), 'C'.getsBytes(), 'D'.getsBytes(),
                'E'.getsBytes(), 'F'.getsBytes()});

        // Write a dummy record to make the size even
        out.write(new byte[DumpArchiveConstants.TP_SIZE]);

        testInputStream = new ByteArrayInputStream(out.toByteArray());
        dumpArchiveInputStream = new DumpArchiveInputStream(testInputStream, zipEncoding);
        summary = dumpArchiveInputStream.getSummary();
    }

    @Test
    public void testReadNextEntry() throws IOException {
        DumpArchiveEntry entry = dumpArchiveInputStream.getNextEntry();

        Assert.assertNotNull(entry);
        Assert.assertEquals("file1", entry.getName());
        Assert.assertTrue(entry.isFile());

        byte[] data = new byte[7];
        int len = dumpArchiveInputStream.read(data, 0, data.length);
        Assert.assertEquals(data.length, len);
        Assert.assertArrayEquals(new byte[]{'E', 'F', 'A', 'B', 'C', 'D'}, data);
    }

    @Test
    public void testReadNextEntry_EOF() throws IOException {
        DumpArchiveEntry entry = dumpArchiveInputStream.getNextEntry();
        dumpArchiveInputStream.getNextEntry();

        Assert.assertNull(dumpArchiveInputStream.getNextEntry());
    }

    @Test(expected = ArchiveException.class)
    public void testInvalidFormat() throws IOException {
        // Test a corrupt archive
        ByteArrayInputStream corruptTestInputStream = new ByteArrayInputStream(
            new byte[]{"A", "B", "C", "D", "E", "F", (byte) 1, 0, 0, 0, 1, 0, 0, 0,
                DumpArchiveConstants.TP_SIZE, 0, (byte) 1, (byte) 0, 0, 0,
                DumpArchiveConstants.TP_SIZE, DumpArchiveConstants.SEGMENT_TYPE.ADDR.getValue(),
                new byte[0]});

        try (DumpArchiveInputStream corruptDumpArchiveInputStream =
            new DumpArchiveInputStream(corruptTestInputStream, zipEncoding)) {
            corruptDumpArchiveInputStream.getNextEntry();
        }
    }

    @Test(expected = IOException.class)
    public void testEOF() throws IOException {
        // Test reading past EOF
        dumpArchiveInputStream.getNextEntry();
        dumpArchiveInputStream.getNextEntry();

        byte[] data = new byte[512];
        int len = dumpArchiveInputStream.read(data, 0, data.length);
        Assert.assertEquals(-1, len);
    }

    @Test(expected = IOException.class)
    public void testClose() throws IOException {
        // Test closing the stream multiple times
        dumpArchiveInputStream.close();
        dumpArchiveInputStream.close();
    }

    @Test
    public void testGetSummary() {
        Assert.assertEquals(1, summary.getEntryCount());
        Assert.assertEquals("file1", summary.getEntryName(0));
        Assert.assertEquals(512, summary.getEntrySize(0));
    }

    @Test
    public void testGetBytesRead() throws IOException {
        int totalRead = 0;
        byte[] buffer = new byte[1024];

        while (dumpArchiveInputStream.getNextEntry() != null) {
            int len = dumpArchiveInputStream.read(buffer, 0, buffer.length);
            totalRead += len;
        }

        Assert.assertEquals(2 * DumpArchiveConstants.TP_SIZE + 7, totalRead);
        Assert.assertEquals(2 * DumpArchiveConstants.TP_SIZE + 7, dumpArchiveInputStream.getBytesRead());
    }

    @Test
    public void testMatches() throws IOException {
        byte[] headerBytes = new byte[DumpArchiveConstants.TP_SIZE];
        testInputStream.read(headerBytes, 0, headerBytes.length);

        Assert.assertTrue(DumpArchiveInputStream.matches(headerBytes, headerBytes.length));
    }

    @Test
    public void testMatchesWithCorruptHeader() throws IOException {
        byte[] headerBytes = new byte[]{'A', 'B', 'C', 'D', 'E', 'F', (byte) 1, 0, 0, 0, 1, 0, 0, 0,
            DumpArchiveConstants.TP_SIZE, 0, (byte) 1, (byte) 0, 0, 0};

        Assert.assertFalse(DumpArchiveInputStream.matches(headerBytes, headerBytes.length));
    }

    @Test
    public void testMatchesWithTruncatedHeader() throws IOException {
        byte[] headerBytes = new byte[DumpArchiveConstants.TP_SIZE - 1];

        testInputStream.read(headerBytes, 0, headerBytes.length);

        Assert.assertFalse(DumpArchiveInputStream.matches(headerBytes, headerBytes.length));
    }
}
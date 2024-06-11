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
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

public class DumpArchiveInputStreamTest {
    private InputStream inputStream;
    private DumpArchiveInputStream dumpArchiveInputStream;
    private DumpArchiveSummary summary;
    private static final ZipEncoding zipEncoding = ZipEncodingHelper.getDefaultEncoding();

    @Before
    public void setUp() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        outputStream.write(DumpArchiveUtil.DUMP_ARCHIVE_MAGIC);
        outputStream.write(0x00, 2, 2);
        outputStream.write(DumpArchiveConstants.TP_SIZE, 4, 4);

        byte[] headerBytes = outputStream.toByteArray();
        summary = new DumpArchiveSummary(headerBytes, zipEncoding);

        ByteArrayInputStream testInputStream = new ByteArrayInputStream(
                headerBytes);
        inputStream = new TapeInputStream(testInputStream);
        dumpArchiveInputStream = new DumpArchiveInputStream(inputStream, zipEncoding.getName());
    }

    @Test
    public void testConstructorWithDefaultEncoding() throws IOException {
        dumpArchiveInputStream = new DumpArchiveInputStream(inputStream);
        DumpArchiveSummary summary = dumpArchiveInputStream.getSummary();
        assertEquals(summary.getName(), "default");
    }

    @Test
    public void testConstructorWithProvidedEncoding() throws IOException {
        dumpArchiveInputStream = new DumpArchiveInputStream(inputStream, "UTF-8");
        DumpArchiveSummary summary = dumpArchiveInputStream.getSummary();
        assertEquals(summary.getName(), "UTF-8");
    }

    @Test
    public void testGetSummary() throws IOException {
        DumpArchiveSummary summary = dumpArchiveInputStream.getSummary();
        assertEquals(summary.getMagic(), DumpArchiveConstants.DUMP_ARCHIVE_MAGIC);
        assertEquals(summary.getVersion(), 0);
        assertEquals(summary.getFileSize(), DumpArchiveConstants.TP_SIZE);
        assertEquals(summary.isCompressed(), false);
    }

    @Test
    public void testGetNextEntry() throws IOException {
        DumpArchiveEntry nextEntry = dumpArchiveInputStream.getNextEntry();
        assertNull(nextEntry);

        DumpArchiveEntry entry = dumpArchiveInputStream.getNextEntry();
        assertEquals(nextEntry.getHeaderType(), DumpArchiveConstants.SEGMENT_TYPE.CLRI);
        assertFalse(entry.isDirectory());

        DumpArchiveEntry directoryEntry = dumpArchiveInputStream.getNextEntry();
        assertTrue(directoryEntry.isDirectory());

        DumpArchiveEntry fileEntry = dumpArchiveInputStream.getNextEntry();
        assertTrue(fileEntry.isFile());
    }

    @Test
    public void testRead() throws IOException {
        byte[] testData = new byte[4096];
        Arrays.fill(testData, (byte) 'A');

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        outputStream.write(testData, 0, testData.length);

        InputStream testInputStream = new ByteArrayInputStream(outputStream.toByteArray());
        TapeInputStream tapeInputStream = new TapeInputStream(testInputStream);

        dumpArchiveInputStream.raw = tapeInputStream;

        int read = dumpArchiveInputStream.read(readBuf, 0, readBuf.length);
        assertEquals(read, testData.length);

        Arrays.equals(testData, readBuf, 0, readBuf.length);
    }

    @Test(expected = EOFException.class)
    public void testReadAtEndOfStream() throws IOException {
        DumpArchiveEntry entry = dumpArchiveInputStream.getNextEntry();
        inputStream.close();
        dumpArchiveInputStream.read(readBuf, 0, readBuf.length);
    }

    @Test
    public void testClose() throws IOException {
        DumpArchiveEntry entry = dumpArchiveInputStream.getNextEntry();
        dumpArchiveInputStream.close();

        DumpArchiveEntry nextEntry = dumpArchiveInputStream.getNextEntry();
        assertNull(nextEntry);
    }

    @Test
    public void testMatches() throws IOException {
        byte[] buffer = new byte[32];
        Arrays.fill(buffer, (byte) 0x00);
        buffer[0] = DumpArchiveConstants.NFS_MAGIC;

        assertTrue(DumpArchiveInputStream.matches(buffer, buffer.length));

        byte[] longData = new byte[1024];
        Arrays.fill(longData, (byte) 0x00);

        assertFalse(DumpArchiveInputStream.matches(longData, longData.length));
    }
}
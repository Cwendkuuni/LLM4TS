import org.apache.commons.compress.archivers.ArchiveException;
import org.apache.commons.compress.archivers.ArchiveInputStream;
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
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.PriorityQueue;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

public class DumpArchiveInputStreamTest {

    private static final ZipEncoding zipEncoding = ZipEncodingHelper.getZipEncoding(null);
    private ArchiveInputStream input;
    private DumpArchiveInputStream dumpInput;
    private byte[] header;

    @Before
    public void setUp() throws IOException {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        header = DumpArchiveUtil.createHeader(zipEncoding, 2, 1);
        output.write(header);
        output.write(DumpArchiveConstants.TP_SIZE * 5 + DumpArchiveConstants.TP_SIZE);
        output.write(DumpArchiveConstants.TP_SIZE * 5);
        output.write(DumpArchiveConstants.TP_SIZE * 5);
        output.write(DumpArchiveConstants.TP_SIZE * 5);
        output.write(DumpArchiveConstants.TP_SIZE * 5);
        output.write(DumpArchiveConstants.TP_SIZE * 5);
        output.write(DumpArchiveConstants.TP_SIZE * 5);
        output.write(DumpArchiveConstants.TP_SIZE * 5);
        output.write(DumpArchiveConstants.TP_SIZE * 5);
        output.write(DumpArchiveConstants.TP_SIZE * 5);

        ByteArrayInputStream testInputStream = new ByteArrayInputStream(output.toByteArray());
        input = new ArchiveInputStreamFactory().createArchiveInputStream(testInputStream);
        dumpInput = new DumpArchiveInputStream(testInputStream, zipEncoding);
    }

    @Test
    public void testMatches() {
        byte[] buffer = new byte[32];
        Arrays.fill(buffer, (byte) 0);
        System.arraycopy(header, 24, buffer, 24, 8);

        assertTrue(DumpArchiveInputStream.matches(buffer, 32));
        assertFalse(DumpArchiveInputStream.matches(buffer, 31));
    }

    @Test
    public void testVerify() {
        assertTrue(DumpArchiveUtil.verify(header));
    }

    @Test
    public void testGetNextEntry_firstEntry() throws IOException {
        DumpArchiveEntry entry = dumpInput.getNextEntry();
        assertEquals(1, entry.getIndex());
        assertEquals(2, entry.getParentDirectoryIndex());
        assertEquals(3, entry.getType());
        assertEquals("", entry.getName());
        assertEquals("", entry.getSimpleName());
        assertEquals(0, entry.getOffset());
        assertEquals(0, entry.getEntrySize());
        assertEquals(0, entry.getRecordOffset());
        assertEquals(0, entry.getRecordLength());
    }

    @Test
    public void testGetNextEntry_multipleEntries() throws IOException {
        for (int i = 0; i < 5; i++) {
            DumpArchiveEntry entry = dumpInput.getNextEntry();
            assertEquals(i + 2, entry.getIndex());
            assertEquals(1, entry.getParentDirectoryIndex());
            assertEquals(3, entry.getType());
            assertEquals("", entry.getName());
            assertEquals("", entry.getSimpleName());
            assertEquals((i + 1) * DumpArchiveConstants.TP_SIZE, entry.getOffset());
            assertEquals(DumpArchiveConstants.TP_SIZE * 5, entry.getEntrySize());
            assertEquals(0, entry.getRecordOffset());
            assertEquals(DumpArchiveConstants.TP_SIZE * 5, entry.getRecordLength());
        }
        assertNull(dumpInput.getNextEntry());
    }

    @Test
    public void testRead_emptyBuffer() throws IOException {
        byte[] buffer = new byte[0];
        int len = dumpInput.read(buffer, 0, buffer.length);
        assertEquals(-1, len);
    }

    @Test
    public void testRead_singleByte() throws IOException {
        byte[] buffer = new byte[1];
        int len = dumpInput.read(buffer, 0, buffer.length);
        assertEquals(1, len);
        assertEquals(0, buffer[0]);
    }

    @Test
    public void testRead_multipleBytes() throws IOException {
        byte[] buffer = new byte[5];
        int len = dumpInput.read(buffer, 0, buffer.length);
        assertEquals(5, len);
        assertEquals(0, buffer[0]);
        assertEquals(0, buffer[1]);
        assertEquals(0, buffer[2]);
        assertEquals(0, buffer[3]);
        assertEquals(0, buffer[4]);
    }

    @Test
    public void testRead_lastEntry() throws IOException {
        DumpArchiveEntry entry = dumpInput.getNextEntry();
        int len = dumpInput.read(new byte[5], 0, 5);
        assertEquals(5, len);
        assertEquals((byte) 0x80, new byte[]{5, 0x0D, 0x0A, 0x2B, 0x0A}[0]);
    }

    @Test(expected = EOFException.class)
    public void testRead_pastLastEntry() throws IOException {
        dumpInput.read(new byte[5], 0, 5);
        dumpInput.getNextEntry();
        dumpInput.read(new byte[5], 0, 5);
    }

    @Test
    public void testClose() throws IOException {
        dumpInput.close();
        assertFalse(input.isOpen());
    }

    @Test(expected = IOException.class)
    public void testReadAfterClosed() throws IOException {
        dumpInput.close();
        dumpInput.read(new byte[5], 0, 5);
    }
}

class ArchiveInputStreamFactory {
    public ArchiveInputStream createArchiveInputStream(InputStream inputStream)
            throws IOException {
        return new ArchiveInputStream() {
            @Override
            public boolean isOpen() {
                return true;
            }
        };
    }
}
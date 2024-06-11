import org.apache.commons.compress.archivers.ArchiveException;
import org.apache.commons.compress.archivers.ArchiveInputStream;
import org.apache.commons.compress.archivers.zip.ZipEncoding;
import org.apache.commons.compress.archivers.zip.ZipEncodingHelper;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Stack;

import static org.apache.commons.compress.archivers.dump.DumpArchiveConstants.TP_SIZE;

public class DumpArchiveInputStreamTest {

    private static final String SAMPLE_HEADER = "00000000000000010000000000003418040203000000001323000000001323000000020200001000000000001234567890123456789012345678901234567890123456789012345678901234567890";
    private static final String SAMPLE_BITS_SEGMENT = "000000000000000100000000000024180400000000001323000000001323000000020200001000000000001234567890123456789012345678901234567890123456789012345678901234567890";
    private static final String SAMPLE_CLRI_SEGMENT = "000000000000000100000000000034180402030000000001323000000001323000000020200001000000000001234567890123456789012345678901234567890123456789012345678901234567890";
    private static final String SAMPLE_INODE_SEGMENT = "000000000000000100000000000034180402030000000040000000000110123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890";
    private static final byte[] SAMPLE_HEADER_BYTES = hexStringToByteArray(SAMPLE_HEADER);
    private static final byte[] SAMPLE_BITS_SEGMENT_BYTES = hexStringToByteArray(SAMPLE_BITS_SEGMENT);
    private static final byte[] SAMPLE_CLRI_SEGMENT_BYTES = hexStringToByteArray(SAMPLE_CLRI_SEGMENT);

    private static final ZipEncoding zipEncoding = ZipEncodingHelper.getZipEncoding(null);

    private DumpArchiveInputStream inputStream;
    private ByteArrayOutputStream outputStream;
    private ByteArrayInputStream inputStreamForTesting;

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Before
    public void setUp() throws IOException {
        outputStream = new ByteArrayOutputStream();
        inputStreamForTesting = new ByteArrayInputStream(TP_SIZE > SAMPLE_HEADER_BYTES.length
                ? SAMPLE_HEADER_BYTES
                : Arrays.copyOfRange(SAMPLE_HEADER_BYTES, 0, SAMPLE_HEADER_BYTES.length));
        inputStream = new DumpArchiveInputStream(inputStreamForTesting, zipEncoding);
    }

    @Test
    public void testMatches() throws IOException {
        Assert.assertTrue(DumpArchiveInputStream.matches(SAMPLE_HEADER_BYTES, SAMPLE_HEADER_BYTES.length));
    }

    @Test
    public void testVerify() throws IOException {
        Assert.assertTrue(DumpArchiveUtil.verify(SAMPLE_HEADER_BYTES));
    }

    @Test
    public void testGetNextEntry_ValidEntry() throws IOException {
        inputStream.readRecord();
        inputStream.readCLRI();
        inputStream.readBITS();
        DumpArchiveEntry entry = inputStream.getNextEntry();

        Assert.assertNotNull(entry);
        Assert.assertEquals(2, entry.getIno());
        Assert.assertEquals(".", entry.getName());
        Assert.assertEquals(".", entry.getSimpleName());
        Assert.assertEquals(0, entry.getSize());
        Assert.assertEquals(0, entry.getOffset());
        Assert.assertEquals(DumpArchiveEntry.Type.FILE, entry.getType());
    }

    @Test
    public void testGetNextEntry_DirectoryEntry() throws IOException {
        inputStream.readRecord();
        inputStream.readBITS();
        DumpArchiveEntry entry = inputStream.getNextEntry();

        Assert.assertNotNull(entry);
        Assert.assertEquals(2, entry.getParentIno());
        Assert.assertEquals(".", entry.getName());
        Assert.assertEquals(".", entry.getSimpleName());
        Assert.assertEquals(TP_SIZE * 2, entry.getSize());
        Assert.assertEquals(TP_SIZE, entry.getOffset());
        Assert.assertEquals(DumpArchiveEntry.Type.DIRECTORY, entry.getType());

        entry = inputStream.getNextEntry();
        Assert.assertNotNull(entry);
        Assert.assertEquals(3, entry.getIno());
        Assert.assertEquals("..", entry.getName());
        Assert.assertEquals("..", entry.getSimpleName());
        Assert.assertEquals(TP_SIZE * 2, entry.getSize());
        Assert.assertEquals(TP_SIZE * 2, entry.getOffset());
        Assert.assertEquals(DumpArchiveEntry.Type.DIRECTORY, entry.getType());
    }

    @Test
    public void testGetNextEntry_EndOfVolumeMarker() throws IOException {
        inputStream.readRecord();
        inputStream.readBITS();

        DumpArchiveEntry entry = inputStream.getNextEntry();
        Assert.assertNull(entry);
    }

    @Test
    public void testGetNextEntry_EOF() throws IOException {
        inputStreamForTesting = new ByteArrayInputStream(SAMPLE_HEADER_BYTES);
        inputStream = new DumpArchiveInputStream(inputStreamForTesting, zipEncoding);
        inputStream.readRecord();
        inputStream.readCLRI();
        inputStream.readBITS();

        DumpArchiveEntry entry = inputStream.getNextEntry();

        Assert.assertEquals(2, entry.getIno());
        Assert.assertEquals(".", entry.getName());
        Assert.assertEquals(".", entry.getSimpleName());
        Assert.assertEquals(0, entry.getSize());
        Assert.assertEquals(0, entry.getOffset());
        Assert.assertEquals(DumpArchiveEntry.Type.FILE, entry.getType());

        inputStream.getNextEntry();

        expectedException.expect(IOException.class);
        inputStream.getNextEntry();
    }

    @Test
    public void testRead() throws IOException {
        byte[] testBytes = new byte[10];

        int bytesRead = inputStream.read(testBytes, 0, testBytes.length);

        Assert.assertEquals(TP_SIZE, bytesRead);
        Assert.assertArrayEquals(SAMPLE_HEADER_BYTES, testBytes);
    }

    @Test
    public void testRead_Eof() throws IOException {
        byte[] testBytes = new byte[10];

        inputStream.read(testBytes, 0, testBytes.length);
        inputStream.read(testBytes, 0, testBytes.length);

        int bytesRead = inputStream.read(testBytes, 0, testBytes.length);

        Assert.assertEquals(-1, bytesRead);
    }

    @Test
    public void testClose() throws IOException {
        inputStream.close();
        inputStreamForTesting = new ByteArrayInputStream(SAMPLE_HEADER_BYTES);
        inputStream = new DumpArchiveInputStream(inputStreamForTesting, zipEncoding);

        inputStream.getNextEntry();

        expectedException.expect(ArchiveException.class);
        inputStream.close();
    }

    @Test
    public void testClose_AlreadyClosed() throws IOException {
        inputStream.close();
        inputStreamForTesting = new ByteArrayInputStream(SAMPLE_HEADER_BYTES);
        inputStream = new DumpArchiveInputStream(inputStreamForTesting, zipEncoding);
        inputStream.close();

        expectedException.expect(IllegalStateException.class);
        inputStream.close();
    }

    @Test
    public void testGetSummary() throws IOException {
        DumpArchiveSummary summary = inputStream.getSummary();

        Assert.assertNotNull(summary);
        Assert.assertEquals(2, summary.getMagicNumber());
        Assert.assertEquals(1, summary.getVolumeSize());
        Assert.assertEquals(TP_SIZE * 3, summary.getNumberOfHeaders());
    }

    private static byte[] hexStringToByteArray(String s) {
        int len = s.length();
        byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) ((Character.isDigit(s.charAt(i)) ? Character.digit(s.charAt(i), 16)
                    : Character.isLetter(s.charAt(i)) ? (byte) (Character.toUpperCase(s.charAt(i)) - 55)
                    : 0));
            data[i / 2] = (byte) ((data[i / 2] << 4) | Character.isDigit(s.charAt(i + 1))
                    ? Character.digit(s.charAt(i + 1), 16)
                    : (Character.toUpperCase(s.charAt(i + 1)) - 55));
        }
        return data;
    }
}
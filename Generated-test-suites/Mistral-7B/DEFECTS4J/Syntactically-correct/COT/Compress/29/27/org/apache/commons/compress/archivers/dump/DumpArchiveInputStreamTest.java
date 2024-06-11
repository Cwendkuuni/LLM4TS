import org.apache.commons.compress.archivers.ArchiveException;
import org.apache.commons.compress.archivers.ArchiveInputStream;
import org.apache.commons.compress.archivers.dump.DumpArchiveConstants;
import org.apache.commons.compress.archivers.dump.DumpArchiveEntry;
import org.apache.commons.compress.archivers.dump.DumpArchiveInputStream;
import org.apache.commons.compress.archivers.dump.DumpArchiveSummary;
import org.apache.commons.compress.archivers.dump.DumpArchiveUtil;
import org.apache.commons.compress.archivers.zip.ZipEncoding;
import org.apache.commons.compress.compressors.lzma.LZMADecoder;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Stack;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class DumpArchiveInputStreamTest {

    private static final String TEST_FILE = "test_file.dump";
    private static final int TEST_FILE_SIZE = 100;
    private static final ZipEncoding TEST_ENCODING = ZipEncoding.UTF_8;

    private DumpArchiveInputStream testSubject;
    private byte[] testData;
    private InputStream testInputStream;
    private ArchiveInputStream testArchiveInputStream;

    @Before
    public void setup() throws IOException {
        testData = generateTestData();
        testInputStream = new ByteArrayInputStream(testData);
        testArchiveInputStream = mock(ArchiveInputStream.class);

        when(testArchiveInputStream.readRecord()).thenAnswer(i -> testData);
        when(testArchiveInputStream.peek()).thenCallRealMethod();
        when(testArchiveInputStream.getBytesRead()).thenReturn(0L);
        when(testArchiveInputStream.getAvailable()).thenReturn(TEST_FILE_SIZE);

        testSubject = new DumpArchiveInputStream(testInputStream, TEST_ENCODING) {
            @Override
            protected ArchiveInputStream getUnderlyingArchiveInputStream() {
                return testArchiveInputStream;
            }
        };
    }

    @Test
    public void testMatches() {
        byte[] testFileHeader = new byte[32];
        byte[] testDataWithMagic = ArrayUtils.addAll(testData, new byte[] {0x62, 0x66, 0x73, 0x61, 0x6d, 0x73, 0x68, 0x65, 0x61, 0x73, 0x65, 0x20, 0x64, 0x65, 0x76, 0x65, 0x72, 0x73, 0x69, 0x6f, 0x6e});
        System.arraycopy(testFileHeader, 0, testDataWithMagic, 0, 32);

        Assert.assertTrue(DumpArchiveInputStream.matches(testFileHeader, 32));
        Assert.assertTrue(DumpArchiveInputStream.matches(testDataWithMagic, 100));
        Assert.assertFalse(DumpArchiveInputStream.matches(testData, 100));
    }

    @Test
    public void testGetNextEntry_ValidEntry() throws IOException {
        testSubject.getNextEntry();
        DumpArchiveEntry expectedEntry = new DumpArchiveEntry(1, 1, DumpArchiveConstants.TYPE_REG, "test_file.dump");
        DumpArchiveEntry actualEntry = testSubject.getNextEntry();

        Assert.assertEquals(expectedEntry, actualEntry);
    }

    @Test(expected = ArchiveException.class)
    public void testGetNextEntry_EndOfArchive() throws IOException {
        testSubject.getNextEntry();
        testSubject.getNextEntry();
        testSubject.getNextEntry();

        testSubject.getNextEntry();
    }

    @Test(expected = EOFException.class)
    public void testGetNextEntry_EOF() throws IOException {
        when(testArchiveInputStream.peek()).thenThrow(new EOFException());

        testSubject.getNextEntry();
    }

    @Test(expected = IOException.class)
    public void testGetNextEntry_IOError() throws IOException {
        when(testArchiveInputStream.peek()).thenThrow(new IOException());

        testSubject.getNextEntry();
    }

    @Test
    public void testGetSummary() throws IOException {
        DumpArchiveSummary expectedSummary = new DumpArchiveSummary(testData, TEST_ENCODING);
        DumpArchiveSummary actualSummary = testSubject.getSummary();

        Assert.assertEquals(expectedSummary, actualSummary);
    }

    @Test
    public void testClose() throws IOException {
        testSubject.close();

        Assert.assertFalse(testSubject.isOpen());
    }

    @Test
    public void testRead() throws IOException {
        byte[] testBuffer = new byte[10];
        int testOffset = 0;
        int testLen = 5;

        testSubject.getNextEntry();

        int testBytesRead = testSubject.read(testBuffer, testOffset, testLen);

        Assert.assertEquals(5, testBytesRead);
        Assert.assertArrayEquals(new byte[]{"t", "e", "s", "t", "_"}, testBuffer, 0, 5);
    }

    @Test(expected = EOFException.class)
    public void testRead_EndOfArchive() throws IOException {
        testSubject.getNextEntry();
        testSubject.getNextEntry();

        byte[] testBuffer = new byte[10];
        int testOffset = 0;
        int testLen = 5;

        int testBytesRead = testSubject.read(testBuffer, testOffset, testLen);

        Assert.assertEquals(0, testBytesRead);
    }

    @Test(expected = IOException.class)
    public void testRead_IOError() throws IOException {
        InputStream testMockInputStream = mock(InputStream.class);
        when(testMockInputStream.read()).thenThrow(new IOException());

        DumpArchiveInputStream testSubject = new DumpArchiveInputStream(testMockInputStream, TEST_ENCODING) {
            @Override
            protected ArchiveInputStream getUnderlyingArchiveInputStream() {
                return null;
            }
        };

        byte[] testBuffer = new byte[10];
        int testOffset = 0;
        int testLen = 5;

        int testBytesRead = testSubject.read(testBuffer, testOffset, testLen);

        Assert.assertEquals(-1, testBytesRead);
    }

    private byte[] generateTestData() throws IOException {
        ByteArrayOutputStream testOutputStream = new ByteArrayOutputStream();
        testOutputStream.write(DumpArchiveConstants.NFS_MAGIC);
        testOutputStream.write(0);
        testOutputStream.write(1);
        testOutputStream.write(1);

        DumpArchiveEntry testEntry = new DumpArchiveEntry(1, 1, DumpArchiveConstants.TYPE_REG, "test_file.dump");
        testOutputStream.write(testEntry.getHeaderSize());
        testOutputStream.write(DumpArchiveConstants.SEGMENT_TYPE.INODE);
        testOutputStream.write(testEntry.getIno());
        testOutputStream.write(testEntry.getParentIno());
        testOutputStream.write(testEntry.getType());
        testOutputStream.write(testEntry.getName().length());
        testOutputStream.write(testEntry.getName().getBytes(TEST_ENCODING.name()));
        testOutputStream.write(DumpArchiveConstants.SEGMENT_TYPE.ADDR);
        testOutputStream.write(0);
        testOutputStream.write(0);
        testOutputStream.write((byte) 0xFF);
        testOutputStream.write(DumpArchiveConstants.TP_SIZE);
        testOutputStream.write(DumpArchiveConstants.TP_SIZE);
        testOutputStream.write(0);
        testOutputStream.write(0);
        testOutputStream.write(testFileSize);
        testOutputStream.write(DumpArchiveConstants.SEGMENT_TYPE.FILE_DATA);
        testOutputStream.write(0);
        testOutputStream.write("test_file_content".getBytes(TEST_ENCODING.name()));
        testOutputStream.write(0);

        testOutputStream.flush();
        return testOutputStream.toByteArray();
    }
}
import org.apache.commons.compress.archivers.ArchiveException;
import org.apache.commons.compress.archivers.ArchiveInputStream;
import org.apache.commons.compress.archivers.dump.DumpArchiveConstants;
import org.apache.commons.compress.archivers.dump.DumpArchiveEntry;
import org.apache.commons.compress.archivers.dump.DumpArchiveInputStream;
import org.apache.commons.compress.archivers.dump.DumpArchiveSummary;
import org.apache.commons.compress.archivers.zip.ZipEncoding;
import org.apache.commons.compress.archivers.zip.ZipEncodingHelper;
import org.apache.commons.compress.utils.DumpArchiveUtil;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Stack;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class DumpArchiveInputStreamTest {

    private DumpArchiveInputStream inputStream;
    private InputStream inputStreamMock;
    private ZipEncoding encoding;

    @Before
    public void setup() throws IOException {
        inputStreamMock = Mockito.mock(InputStream.class);
        encoding = ZipEncodingHelper.getZipEncoding(null);
        inputStream = new DumpArchiveInputStream(inputStreamMock, encoding);
    }

    @Test
    public void testGetCount() throws IOException {
        Assert.assertEquals(0, inputStream.getCount());

        inputStream.read(new byte[1]);
        Assert.assertEquals(1, inputStream.getCount());
    }

    @Test(expected = ArchiveException.class)
    public void testInvalidFormatException() throws IOException {
        byte[] invalidHeaderBytes = new byte[DumpArchiveConstants.TP_SIZE];
        when(inputStreamMock.readRecord()).thenReturn(invalidHeaderBytes);

        DumpArchiveInputStream invalidInputStream = new DumpArchiveInputStream(inputStreamMock, encoding);
        invalidInputStream.getNextEntry();
    }

    @Test
    public void testGetSummary() throws IOException {
        byte[] headerBytes = new byte[DumpArchiveConstants.TP_SIZE];
        DumpArchiveSummary summary = new DumpArchiveSummary(headerBytes, encoding);
        when(inputStreamMock.readRecord()).thenReturn(headerBytes);

        DumpArchiveInputStream inputStreamWithSummary = new DumpArchiveInputStream(inputStreamMock, encoding);
        DumpArchiveSummary actualSummary = inputStreamWithSummary.getSummary();
        Assert.assertEquals(summary, actualSummary);
    }

    @Test(expected = IOException.class)
    public void testReadCLRIFailure() throws IOException {
        when(inputStreamMock.readRecord()).thenReturn(new byte[0]);
        DumpArchiveInputStream inputStreamWithFailure = new DumpArchiveInputStream(inputStreamMock, encoding);
        inputStreamWithFailure.readCLRI();
    }

    @Test(expected = IOException.class)
    public void testReadBITSFailure() throws IOException {
        when(inputStreamMock.readRecord()).thenReturn(new byte[0]);
        DumpArchiveInputStream inputStreamWithFailure = new DumpArchiveInputStream(inputStreamMock, encoding);
        inputStreamWithFailure.readBITS();
    }

    @Test
    public void testGetNextEntry() throws IOException {
        byte[] headerBytes = new byte[DumpArchiveConstants.TP_SIZE];
        DumpArchiveEntry entry = new DumpArchiveEntry();
        when(inputStreamMock.readRecord()).thenReturn(headerBytes);
        when(inputStreamMock.readRecord()).thenReturn(new byte[0]);

        DumpArchiveInputStream inputStreamWithEntry = new DumpArchiveInputStream(inputStreamMock, encoding);
        DumpArchiveEntry actualEntry = inputStreamWithEntry.getNextEntry();
        Assert.assertEquals(entry, actualEntry);
    }

    @Test(expected = IOException.class)
    public void testReadNextEntryFailure() throws IOException {
        byte[] headerBytes = new byte[DumpArchiveConstants.TP_SIZE];
        DumpArchiveEntry entry = new DumpArchiveEntry();
        when(inputStreamMock.readRecord()).thenReturn(headerBytes);
        when(inputStreamMock.readRecord()).thenReturn(new byte[0]);
        when(inputStreamMock.readRecord()).thenReturn(new byte[0]);

        DumpArchiveInputStream inputStreamWithFailure = new DumpArchiveInputStream(inputStreamMock, encoding);
        inputStreamWithFailure.getNextEntry();
        inputStreamWithFailure.getNextEntry();
        inputStreamWithFailure.getNextEntry();
    }

    @Test
    public void testGetPath() throws IOException {
        DumpArchiveEntry entry = new DumpArchiveEntry();
        entry.setIno(1);
        Map<Integer, Dirent> namesMock = mock(Map.class);
        when(namesMock.get(1)).thenReturn(new Dirent());

        Stack<String> elements = new Stack<>();
        elements.push("root");

        when(inputStream.getNextEntry()).thenReturn(entry);
        when(inputStream.names).thenReturn(namesMock);

        String actualPath = inputStream.getPath(entry);
        String expectedPath = "/root";
        Assert.assertEquals(expectedPath, actualPath);
    }

    @Test
    public void testRead() throws IOException {
        byte[] testData = new byte[10];
        Arrays.fill(testData, (byte) 0xAA);

        when(inputStreamMock.read(Mockito.anyByteArray(), anyInt(), anyInt()))
                .thenReturn(testData.length);

        byte[] readBuffer = new byte[5];

        int totalRead = inputStream.read(readBuffer, 0, readBuffer.length);
        Assert.assertEquals(testData.length, totalRead);

        byte[] actualReadData = new byte[totalRead];
        System.arraycopy(readBuffer, 0, actualReadData, 0, totalRead);
        Assert.assertArrayEquals(testData, actualReadData);
    }

    @Test(expected = EOFException.class)
    public void testReadEndOfStream() throws IOException {
        inputStream.hasHitEOF = true;
        byte[] readBuffer = new byte[5];
        int totalRead = inputStream.read(readBuffer, 0, readBuffer.length);
    }

    @Test(expected = IOException.class)
    public void testReadNegativeLength() throws IOException {
        byte[] readBuffer = new byte[5];
        int totalRead = inputStream.read(readBuffer, -5, 5);
    }

    @Test(expected = IOException.class)
    public void testReadNullBuffer() throws IOException {
        int totalRead = inputStream.read(null, 0, 5);
    }

    @Test
    public void testClose() throws IOException {
        inputStream.close();
        Assert.assertTrue(inputStream.isClosed);
    }

    @Test(expected = ArchiveException.class)
    public void testInvalidChecksum() throws IOException {
        byte[] testData = new byte[DumpArchiveConstants.TP_SIZE - 4];
        Arrays.fill(testData, (byte) 0xAA);

        when(inputStreamMock.readRecord()).thenReturn(testData);

        DumpArchiveInputStream invalidInputStream = new DumpArchiveInputStream(inputStreamMock, encoding);
        invalidInputStream.getNextEntry();
    }

    @Test
    public void testMatches() throws IOException {
        byte[] testData = new byte[32];
        Arrays.fill(testData, (byte) 0xAA);

        boolean actualResult = DumpArchiveInputStream.matches(testData, testData.length);
        Assert.assertTrue(actualResult);
    }

    @Test
    public void testDoesNotMatch() throws IOException {
        byte[] testData = new byte[32];
        Arrays.fill(testData, (byte) 0xBB);

        boolean actualResult = DumpArchiveInputStream.matches(testData, testData.length);
        Assert.assertFalse(actualResult);
    }

    @Test(expected = IOException.class)
    public void testReadNegativeOffset() throws IOException {
        long testOffset = -10;
        when(inputStreamMock.skip(testOffset)).thenThrow(new IOException());
        inputStream.seek(testOffset);
    }

    @Test(expected = IOException.class)
    public void testReadNegativeLengthSeek() throws IOException {
        byte[] readBuffer = new byte[5];
        int totalRead = inputStream.read(readBuffer, 0, -10);
    }

    @Test(expected = IOException.class)
    public void testReadNegativeLengthSkip() throws IOException {
        long testOffset = -10;
        when(inputStreamMock.skip(testOffset)).thenThrow(new IOException());
        inputStream.seek(testOffset);
    }

    @Test(expected = IOException.class)
    public void testReadNegativeLengthRead() throws IOException {
        byte[] readBuffer = new byte[5];
        int totalRead = inputStream.read(readBuffer, -10, 5);
    }

    @Test
    public void testSeek() throws IOException {
        long testOffset = 10;
        when(inputStreamMock.skip(testOffset)).thenReturn(testOffset);
        inputStream.seek(testOffset);
        Assert.assertEquals(testOffset, inputStream.getFilePointer());
    }

    @Test(expected = IOException.class)
    public void testSeekInvalidOffset() throws IOException {
        long testOffset = Long.MAX_VALUE;
        when(inputStreamMock.seek(testOffset)).thenThrow(new IOException());
        inputStream.seek(testOffset);
    }

    @Test(expected = IOException.class)
    public void testSeekNegativeOffset() throws IOException {
        long testOffset = -10;
        when(inputStreamMock.seek(testOffset)).thenThrow(new IOException());
        inputStream.seek(testOffset);
    }

    @Test
    public void testMarkSupported() throws IOException {
        Assert.assertFalse(inputStream.markSupported(false));
        Assert.assertFalse(inputStream.markSupported(true));
    }

    @Test(expected = IOException.class)
    public void testMarkNotSupported() throws IOException {
        inputStream.mark(0);
    }

    @Test
    public void testReset() throws IOException {
        long testMark = 10;
        inputStream.mark(testMark);
        inputStream.reset();
        Assert.assertEquals(0, inputStream.getFilePointer());
    }
}
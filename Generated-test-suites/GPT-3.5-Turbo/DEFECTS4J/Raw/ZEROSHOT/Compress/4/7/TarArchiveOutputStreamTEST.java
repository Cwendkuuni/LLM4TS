import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class TarArchiveOutputStreamTest {

    TarArchiveOutputStream tarArchiveOutputStream;
    OutputStream mockOutputStream;
    TarBuffer mockTarBuffer;

    @Before
    public void setup() throws IOException {
        mockOutputStream = Mockito.mock(OutputStream.class);
        mockTarBuffer = Mockito.mock(TarBuffer.class);
        tarArchiveOutputStream = new TarArchiveOutputStream(mockOutputStream);
        tarArchiveOutputStream.buffer = mockTarBuffer;
    }

    @Test
    public void testSetLongFileMode() {
        int longFileMode = TarArchiveOutputStream.LONGFILE_TRUNKATE;
        tarArchiveOutputStream.setLongFileMode(longFileMode);

        assertEquals(longFileMode, tarArchiveOutputStream.longFileMode);
    }

    @Test
    public void testGetRecordSize() {
        int expectedRecordSize = 512;
        int actualRecordSize = tarArchiveOutputStream.getRecordSize();

        assertEquals(expectedRecordSize, actualRecordSize);
    }

    @Test
    public void testPutArchiveEntry() throws IOException {
        TarArchiveEntry mockEntry = Mockito.mock(TarArchiveEntry.class);
        when(mockEntry.getName()).thenReturn("testEntry");

        byte[] recordBuf = new byte[512];
        tarArchiveOutputStream.putArchiveEntry(mockEntry);

        verify(mockEntry).writeEntryHeader(recordBuf);
        verify(mockTarBuffer).writeRecord(recordBuf);
        assertEquals("testEntry", tarArchiveOutputStream.currName);
        assertEquals(true, tarArchiveOutputStream.haveUnclosedEntry);
    }

    @Test
    public void testCloseArchiveEntry() throws IOException {
        tarArchiveOutputStream.assemLen = 512;
        tarArchiveOutputStream.currBytes = 0;
        tarArchiveOutputStream.currSize = 512;
        tarArchiveOutputStream.currName = "testEntry";

        byte[] recordBuf = new byte[512];
        tarArchiveOutputStream.closeArchiveEntry();

        assertEquals(0, tarArchiveOutputStream.assemLen);
        verify(mockTarBuffer).writeRecord(recordBuf);
        assertEquals(false, tarArchiveOutputStream.haveUnclosedEntry);
    }

    @Test(expected = IOException.class)
    public void testCloseArchiveEntryThrowsIOException() throws IOException {
        tarArchiveOutputStream.assemLen = 512;
        tarArchiveOutputStream.currBytes = 0;
        tarArchiveOutputStream.currSize = 256;
        tarArchiveOutputStream.currName = "testEntry";

        tarArchiveOutputStream.closeArchiveEntry();
    }

    @Test
    public void testWrite() throws IOException {
        byte[] wBuf = "test".getBytes();
        int wOffset = 0;
        int numToWrite = wBuf.length;

        tarArchiveOutputStream.write(wBuf, wOffset, numToWrite);

        verify(mockTarBuffer).writeRecord(wBuf, wOffset);
    }

    @Test
    public void testWriteWithAssemLenMoreThanZero() throws IOException {
        tarArchiveOutputStream.assemLen = 100;
        byte[] wBuf = "test".getBytes();
        int wOffset = 0;
        int numToWrite = wBuf.length;

        tarArchiveOutputStream.write(wBuf, wOffset, numToWrite);

        verify(mockTarBuffer).writeRecord(tarArchiveOutputStream.assemBuf);
    }

    @Test
    public void testWriteWithNumToWriteLessThanRecordBufLength() throws IOException {
        byte[] wBuf = "test".getBytes();
        int wOffset = 0;
        int numToWrite = 100;

        tarArchiveOutputStream.write(wBuf, wOffset, numToWrite);

        verify(mockTarBuffer).writeRecord(wBuf, wOffset);
    }

    @Test
    public void testWriteEOFRecord() throws IOException {
        byte[] recordBuf = new byte[512];
        tarArchiveOutputStream.writeEOFRecord();

        verify(mockTarBuffer).writeRecord(recordBuf);
    }

    @Test
    public void testFinish() throws IOException {
        tarArchiveOutputStream.haveUnclosedEntry = true;

        tarArchiveOutputStream.finish();

        verify(mockTarBuffer).writeRecord(Mockito.any(byte[].class));
    }

    @Test(expected = IOException.class)
    public void testFinishThrowsIOException() throws IOException {
        tarArchiveOutputStream.haveUnclosedEntry = true;

        tarArchiveOutputStream.finish();

        verify(mockTarBuffer).writeRecord(Mockito.any(byte[].class));
    }

    @Test
    public void testClose() throws IOException {
        tarArchiveOutputStream.closed = false;
        tarArchiveOutputStream.haveUnclosedEntry = false;

        tarArchiveOutputStream.close();

        verify(mockTarBuffer).close();
        verify(mockOutputStream).close();
        assertEquals(true, tarArchiveOutputStream.closed);
    }

    @Test(expected = IOException.class)
    public void testCloseWithUnclosedEntryThrowsIOException() throws IOException {
        tarArchiveOutputStream.closed = false;
        tarArchiveOutputStream.haveUnclosedEntry = true;

        tarArchiveOutputStream.close();
    }

    @Test
    public void testCreateArchiveEntry() throws IOException {
        File inputFile = Mockito.mock(File.class);
        String entryName = "testEntry";
        TarArchiveEntry archiveEntry = Mockito.mock(TarArchiveEntry.class);
        when(archiveEntry.getName()).thenReturn(entryName);

        TarArchiveEntry actualArchiveEntry =
                (TarArchiveEntry) tarArchiveOutputStream.createArchiveEntry(inputFile, entryName);

        assertEquals(entryName, actualArchiveEntry.getName());
    }
}
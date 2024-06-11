import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.OutputStream;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

public class TarArchiveOutputStreamTest {

    private TarArchiveOutputStream tarArchiveOutputStream;
    private OutputStream outputStream;

    @Before
    public void setUp() {
        outputStream = Mockito.mock(OutputStream.class);
        tarArchiveOutputStream = new TarArchiveOutputStream(outputStream);
    }

    @After
    public void tearDown() {
        tarArchiveOutputStream = null;
        outputStream = null;
    }

    @Test
    public void testPutArchiveEntry_NormalCase() throws Exception {
        TarArchiveEntry entry = new TarArchiveEntry("testFile");
        tarArchiveOutputStream.putArchiveEntry(entry);

        verify(outputStream, times(1)).write(any(byte[].class), anyInt(), anyInt());
        assertEquals(true, tarArchiveOutputStream.haveUnclosedEntry);
        assertEquals("testFile", tarArchiveOutputStream.currName);
    }

    @Test(expected = IOException.class)
    public void testPutArchiveEntry_FinishedArchive() throws Exception {
        TarArchiveEntry entry = new TarArchiveEntry("testFile");
        tarArchiveOutputStream.finish();
        tarArchiveOutputStream.putArchiveEntry(entry);
    }

    @Test
    public void testPutArchiveEntry_LongFileName_GNU() throws Exception {
        TarArchiveEntry entry = new TarArchiveEntry("longFileNameLongFileNameLongFileNameLongFileNameLongFileName");
        tarArchiveOutputStream.setLongFileMode(TarArchiveOutputStream.LONGFILE_GNU);
        tarArchiveOutputStream.putArchiveEntry(entry);

        verify(outputStream, times(1)).write(any(byte[].class), anyInt(), anyInt());
        assertEquals(true, tarArchiveOutputStream.haveUnclosedEntry);
        assertEquals(TarConstants.GNU_LONGLINK, tarArchiveOutputStream.currName);
    }

    @Test(expected = RuntimeException.class)
    public void testPutArchiveEntry_LongFileName_Error() throws Exception {
        TarArchiveEntry entry = new TarArchiveEntry("longFileNameLongFileNameLongFileNameLongFileNameLongFileName");
        tarArchiveOutputStream.setLongFileMode(TarArchiveOutputStream.LONGFILE_ERROR);
        tarArchiveOutputStream.putArchiveEntry(entry);
    }

    @Test
    public void testCloseArchiveEntry_NormalCase() throws Exception {
        tarArchiveOutputStream.putArchiveEntry(new TarArchiveEntry("testFile"));
        tarArchiveOutputStream.closeArchiveEntry();

        verify(outputStream, times(1)).write(any(byte[].class), anyInt(), anyInt());
        assertEquals(false, tarArchiveOutputStream.haveUnclosedEntry);
    }

    @Test(expected = IOException.class)
    public void testCloseArchiveEntry_FinishedArchive() throws Exception {
        tarArchiveOutputStream.finish();
        tarArchiveOutputStream.closeArchiveEntry();
    }

    @Test(expected = IOException.class)
    public void testCloseArchiveEntry_NoCurrentEntry() throws Exception {
        tarArchiveOutputStream.closeArchiveEntry();
    }

    @Test(expected = IOException.class)
    public void testWrite_BytesExceedSizeInHeader() throws Exception {
        TarArchiveEntry entry = new TarArchiveEntry("testFile");
        entry.setSize(10);
        tarArchiveOutputStream.putArchiveEntry(entry);

        byte[] data = new byte[20];
        tarArchiveOutputStream.write(data, 0, data.length);
    }

    @Test
    public void testWrite_NormalCase() throws Exception {
        TarArchiveEntry entry = new TarArchiveEntry("testFile");
        tarArchiveOutputStream.putArchiveEntry(entry);

        byte[] data = new byte[10];
        tarArchiveOutputStream.write(data, 0, data.length);

        assertEquals(data.length, tarArchiveOutputStream.currBytes);
        verify(outputStream, times(2)).write(any(byte[].class), anyInt(), anyInt());
    }

    @Test
    public void testFinish_NormalCase() throws Exception {
        tarArchiveOutputStream.finish();

        verify(outputStream, times(3)).write(any(byte[].class), anyInt(), anyInt());
        verify(outputStream, times(1)).flush();
    }

    @Test(expected = IOException.class)
    public void testFinish_AlreadyFinished() throws Exception {
        tarArchiveOutputStream.finish();
        tarArchiveOutputStream.finish();
    }

    @Test(expected = IOException.class)
    public void testFinish_HaveUnclosedEntries() throws Exception {
        tarArchiveOutputStream.putArchiveEntry(new TarArchiveEntry("testFile"));
        tarArchiveOutputStream.finish();
    }

    @Test
    public void testClose_NormalCase() throws Exception {
        tarArchiveOutputStream.close();

        verify(outputStream, times(3)).write(any(byte[].class), anyInt(), anyInt());
        verify(outputStream, times(1)).flush();
        verify(outputStream, times(1)).close();
    }

    @Test
    public void testGetRecordSize_NormalCase() {
        int recordSize = tarArchiveOutputStream.getRecordSize();

        assertEquals(TarBuffer.DEFAULT_RCDSIZE, recordSize);
    }

    @Test
    public void testCreateArchiveEntry_NormalCase() throws Exception {
        File inputFile = mock(File.class);
        String entryName = "testFile";
        ArchiveEntry archiveEntry = tarArchiveOutputStream.createArchiveEntry(inputFile, entryName);

        assertEquals(TarArchiveEntry.class, archiveEntry.getClass());
        assertEquals(inputFile, ((TarArchiveEntry) archiveEntry).getFile());
        assertEquals(entryName, archiveEntry.getName());
    }

}
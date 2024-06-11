import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;

public class TarArchiveOutputStreamTest {

    private TarArchiveOutputStream tarArchiveOutputStream;
    private OutputStream outputStreamMock;

    @Before
    public void setUp() {
        outputStreamMock = Mockito.mock(OutputStream.class);
        tarArchiveOutputStream = new TarArchiveOutputStream(outputStreamMock);
    }

    @Test
    public void testSetLongFileMode() {
        tarArchiveOutputStream.setLongFileMode(1);
        Assert.assertEquals(1, tarArchiveOutputStream.getLongFileMode());
    }

    @Test
    public void testFinish() throws IOException {
        tarArchiveOutputStream.finish();
        Mockito.verify(outputStreamMock, Mockito.times(2)).write(Mockito.any(byte[].class));
    }

    @Test
    public void testClose() throws IOException {
        tarArchiveOutputStream.close();
        Mockito.verify(outputStreamMock).close();
    }

    @Test
    public void testGetRecordSize() {
        int recordSize = tarArchiveOutputStream.getRecordSize();
        Assert.assertEquals(TarBuffer.DEFAULT_RCDSIZE, recordSize);
    }

    @Test
    public void testPutArchiveEntry() throws IOException {
        ArchiveEntry archiveEntry = Mockito.mock(ArchiveEntry.class);
        TarArchiveEntry tarArchiveEntry = Mockito.mock(TarArchiveEntry.class);
        Mockito.when(archiveEntry.getName()).thenReturn("test");
        Mockito.when(archiveEntry.writeEntryHeader(Mockito.any(byte[].class))).thenReturn(Mockito.anyInt());
        Mockito.when(tarArchiveEntry.isDirectory()).thenReturn(false);
        Mockito.when(tarArchiveEntry.getSize()).thenReturn(100L);
        Mockito.when(archiveEntry instanceof TarArchiveEntry).thenReturn(true);

        tarArchiveOutputStream.putArchiveEntry(archiveEntry);

        Mockito.verify(outputStreamMock).write(Mockito.any(byte[].class));
        Mockito.verify(outputStreamMock, Mockito.times(2)).write(Mockito.any(byte[].class));
    }

    @Test
    public void testCloseArchiveEntry() throws IOException {
        tarArchiveOutputStream.closeArchiveEntry();
        Mockito.verify(outputStreamMock).write(Mockito.any(byte[].class));
    }

    @Test
    public void testWrite() throws IOException {
        byte[] wBuf = {1, 2, 3};
        tarArchiveOutputStream.write(wBuf, 0, 3);
        Mockito.verify(outputStreamMock, Mockito.times(2)).write(Mockito.any(byte[].class), Mockito.anyInt(), Mockito.anyInt());
    }

    @Test
    public void testWriteEOFRecord() throws IOException {
        tarArchiveOutputStream.writeEOFRecord();
        Mockito.verify(outputStreamMock).write(Mockito.any(byte[].class));
    }

    @Test
    public void testFlush() throws IOException {
        tarArchiveOutputStream.flush();
        Mockito.verify(outputStreamMock).flush();
    }

    @Test
    public void testCreateArchiveEntry() throws IOException {
        File inputFile = Mockito.mock(File.class);
        String entryName = "test";
        ArchiveEntry archiveEntry = tarArchiveOutputStream.createArchiveEntry(inputFile, entryName);
        Assert.assertNotNull(archiveEntry);
    }
}
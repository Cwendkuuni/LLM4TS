import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;
import java.io.IOException;
import java.io.OutputStream;
import org.apache.commons.compress.archivers.ArchiveEntry;

public class TarArchiveOutputStreamTest {

    @Test
    public void testSetLongFileMode() {
        TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(Mockito.mock(OutputStream.class));
        tarArchiveOutputStream.setLongFileMode(1);
        int result = tarArchiveOutputStream.getLongFileMode();
        Assert.assertEquals(1, result);
    }

    @Test
    public void testFinish() throws IOException {
        TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(Mockito.mock(OutputStream.class));
        tarArchiveOutputStream.finish();
        boolean result = tarArchiveOutputStream.isFinished();
        Assert.assertTrue(result);
    }

    @Test(expected = IOException.class)
    public void testFinish_AlreadyFinished() throws IOException {
        TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(Mockito.mock(OutputStream.class));
        tarArchiveOutputStream.setFinished(true);
        tarArchiveOutputStream.finish();
    }

    @Test(expected = IOException.class)
    public void testFinish_UnclosedEntry() throws IOException {
        TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(Mockito.mock(OutputStream.class));
        tarArchiveOutputStream.setHaveUnclosedEntry(true);
        tarArchiveOutputStream.finish();
    }

    @Test
    public void testClose() throws IOException {
        TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(Mockito.mock(OutputStream.class));
        tarArchiveOutputStream.close();
        boolean result = tarArchiveOutputStream.isClosed();
        Assert.assertTrue(result);
    }

    @Test(expected = IOException.class)
    public void testClose_NotFinished() throws IOException {
        TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(Mockito.mock(OutputStream.class));
        tarArchiveOutputStream.setFinished(false);
        tarArchiveOutputStream.close();
    }

    @Test
    public void testGetRecordSize() {
        TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(Mockito.mock(OutputStream.class));
        int result = tarArchiveOutputStream.getRecordSize();
        Assert.assertEquals(TarBuffer.DEFAULT_RCDSIZE, result);
    }

    @Test
    public void testPutArchiveEntry() throws IOException {
        TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(Mockito.mock(OutputStream.class));
        ArchiveEntry archiveEntry = Mockito.mock(ArchiveEntry.class);
        tarArchiveOutputStream.putArchiveEntry(archiveEntry);
        boolean result = tarArchiveOutputStream.getHaveUnclosedEntry();
        Assert.assertTrue(result);
    }

    @Test(expected = IOException.class)
    public void testPutArchiveEntry_Finished() throws IOException {
        TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(Mockito.mock(OutputStream.class));
        tarArchiveOutputStream.setFinished(true);
        ArchiveEntry archiveEntry = Mockito.mock(ArchiveEntry.class);
        tarArchiveOutputStream.putArchiveEntry(archiveEntry);
    }

    @Test
    public void testCloseArchiveEntry() throws IOException {
        TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(Mockito.mock(OutputStream.class));
        tarArchiveOutputStream.setHaveUnclosedEntry(true);
        tarArchiveOutputStream.closeArchiveEntry();
        boolean result = tarArchiveOutputStream.getHaveUnclosedEntry();
        Assert.assertFalse(result);
    }

    @Test(expected = IOException.class)
    public void testCloseArchiveEntry_Finished() throws IOException {
        TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(Mockito.mock(OutputStream.class));
        tarArchiveOutputStream.setFinished(true);
        tarArchiveOutputStream.closeArchiveEntry();
    }

    @Test(expected = IOException.class)
    public void testCloseArchiveEntry_NoCurrentEntry() throws IOException {
        TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(Mockito.mock(OutputStream.class));
        tarArchiveOutputStream.closeArchiveEntry();
    }

    @Test(expected = IOException.class)
    public void testCloseArchiveEntry_IncompleteWrite() throws IOException {
        TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(Mockito.mock(OutputStream.class));
        tarArchiveOutputStream.setCurrentBytes(10);
        tarArchiveOutputStream.setSize(20);
        tarArchiveOutputStream.setCurrentName("test");
        tarArchiveOutputStream.closeArchiveEntry();
    }

    @Test
    public void testWrite() throws IOException {
        TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(Mockito.mock(OutputStream.class));
        byte[] wBuf = new byte[100];
        int wOffset = 0;
        int numToWrite = 100;
        tarArchiveOutputStream.write(wBuf, wOffset, numToWrite);
        //
        // assertions for write method
        //
    }

    @Test(expected = IOException.class)
    public void testWrite_ExceedSize() throws IOException {
        TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(Mockito.mock(OutputStream.class));
        tarArchiveOutputStream.setCurrentBytes(10);
        tarArchiveOutputStream.setSize(20);
        byte[] wBuf = new byte[100];
        int wOffset = 0;
        int numToWrite = 100;
        tarArchiveOutputStream.write(wBuf, wOffset, numToWrite);
    }

    @Test(expected = IOException.class)
    public void testWrite_NotFinished() throws IOException {
        TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(Mockito.mock(OutputStream.class));
        tarArchiveOutputStream.setFinished(false);
        byte[] wBuf = new byte[100];
        int wOffset = 0;
        int numToWrite = 100;
        tarArchiveOutputStream.write(wBuf, wOffset, numToWrite);
    }

    @Test
    public void testWriteEOFRecord() throws IOException {
        TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(Mockito.mock(OutputStream.class));
        tarArchiveOutputStream.writeEOFRecord();
        //
        // assertions for writeEOFRecord method
        //
    }

    @Test
    public void testCreateArchiveEntry() throws IOException {
        TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(Mockito.mock(OutputStream.class));
        File inputFile = Mockito.mock(File.class);
        String entryName = "test";
        tarArchiveOutputStream.createArchiveEntry(inputFile, entryName);
        //
        // assertions for createArchiveEntry method
        //
    }

}
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class TarArchiveOutputStreamTest {

    private TarArchiveOutputStream tarArchiveOutputStream;

    @Mock
    private OutputStream outputStream;
    private ByteArrayOutputStream byteArrayOutputStream;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        byteArrayOutputStream = new ByteArrayOutputStream();
        tarArchiveOutputStream = new TarArchiveOutputStream(byteArrayOutputStream);
    }

    @After
    public void teardown() throws IOException {
        tarArchiveOutputStream.close();
    }

    @Test
    public void testSetLongFileMode() {
        tarArchiveOutputStream.setLongFileMode(TarArchiveOutputStream.LONGFILE_ERROR);
        Assert.assertEquals(TarArchiveOutputStream.LONGFILE_ERROR, tarArchiveOutputStream.getLongFileMode());

        tarArchiveOutputStream.setLongFileMode(TarArchiveOutputStream.LONGFILE_TRUNCATE);
        Assert.assertEquals(TarArchiveOutputStream.LONGFILE_TRUNCATE, tarArchiveOutputStream.getLongFileMode());

        tarArchiveOutputStream.setLongFileMode(TarArchiveOutputStream.LONGFILE_GNU);
        Assert.assertEquals(TarArchiveOutputStream.LONGFILE_GNU, tarArchiveOutputStream.getLongFileMode());
    }

    @Test
    public void testFinish() throws IOException {
        tarArchiveOutputStream.finish();
        tarArchiveOutputStream.flush();
        Assert.assertTrue(tarArchiveOutputStream.finished);
    }

    @Test
    public void testClose() throws IOException {
        tarArchiveOutputStream.close();
        Assert.assertTrue(tarArchiveOutputStream.closed);
    }

    @Test
    public void testGetRecordSize() {
        int recordSize = tarArchiveOutputStream.getRecordSize();
        Assert.assertEquals(TarBuffer.DEFAULT_RCDSIZE, recordSize);
    }

    @Test
    public void testPutArchiveEntry() throws IOException {
        TarArchiveEntry archiveEntry = new TarArchiveEntry("sample.tar");
        tarArchiveOutputStream.putArchiveEntry(archiveEntry);
        Assert.assertTrue(tarArchiveOutputStream.haveUnclosedEntry);
        tarArchiveOutputStream.closeArchiveEntry();
        Assert.assertFalse(tarArchiveOutputStream.haveUnclosedEntry);
    }

    @Test(expected = IOException.class)
    public void testPutArchiveEntryWithFinishedArchive() throws IOException {
        tarArchiveOutputStream.finish();
        TarArchiveEntry archiveEntry = new TarArchiveEntry("sample.tar");
        tarArchiveOutputStream.putArchiveEntry(archiveEntry);
    }

    @Test
    public void testCloseArchiveEntry() throws IOException {
        TarArchiveEntry archiveEntry = new TarArchiveEntry("sample.tar");
        tarArchiveOutputStream.putArchiveEntry(archiveEntry);
        tarArchiveOutputStream.closeArchiveEntry();
        Assert.assertFalse(tarArchiveOutputStream.haveUnclosedEntry);
    }

    @Test(expected = IOException.class)
    public void testCloseArchiveEntryWithoutCurrentEntry() throws IOException {
        tarArchiveOutputStream.closeArchiveEntry();
    }

    @Test(expected = IOException.class)
    public void testCloseArchiveEntryWithFinishedArchive() throws IOException {
        tarArchiveOutputStream.finish();
        TarArchiveEntry archiveEntry = new TarArchiveEntry("sample.tar");
        tarArchiveOutputStream.putArchiveEntry(archiveEntry);
        tarArchiveOutputStream.closeArchiveEntry();
    }

    @Test
    public void testWrite() throws IOException {
        byte[] byteArray = "sample".getBytes();
        tarArchiveOutputStream.write(byteArray);
        Assert.assertArrayEquals(byteArray, byteArrayOutputStream.toByteArray());

        byte[] byteArrayWithOffset = "tar".getBytes();
        int offset = 1;
        int length = 2;
        tarArchiveOutputStream.write(byteArrayWithOffset, offset, length);
        Assert.assertArrayEquals("art".getBytes(), byteArrayOutputStream.toByteArray());
    }

    @Test(expected = IOException.class)
    public void testWriteWithExceedingSizeInHeader() throws IOException {
        TarArchiveEntry archiveEntry = new TarArchiveEntry("sample.tar");
        archiveEntry.setSize(5);
        tarArchiveOutputStream.putArchiveEntry(archiveEntry);
        byte[] byteArray = "sample".getBytes();
        tarArchiveOutputStream.write(byteArray);
    }

    @Test
    public void testFlush() throws IOException {
        tarArchiveOutputStream.flush();
        Assert.assertTrue(byteArrayOutputStream.size() > 0);
    }

    @Test(expected = IOException.class)
    public void testFlusWithFinishedArchive() throws IOException {
        tarArchiveOutputStream.finish();
        tarArchiveOutputStream.flush();
    }

    @Test
    public void testCreateArchiveEntry() throws IOException {
        File inputFile = new File("sample.txt");
        String entryName = "sample.txt";
        ArchiveEntry archiveEntry = tarArchiveOutputStream.createArchiveEntry(inputFile, entryName);
        Assert.assertEquals(inputFile, archiveEntry.getFile());
        Assert.assertEquals(entryName, archiveEntry.getName());
    }
}
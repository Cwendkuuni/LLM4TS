import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveOutputStream;
import org.apache.commons.compress.archivers.tar.TarConstants;
import org.apache.commons.compress.archivers.tar.TarEntry;
import org.apache.commons.compress.archivers.tar.TarException;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

public class TarArchiveOutputStreamTest {

    private TarArchiveOutputStream tarArchiveOutputStream;
    private ByteArrayOutputStream outputStream;

    @Before
    public void setUp() {
        outputStream = new ByteArrayOutputStream();
        tarArchiveOutputStream = new TarArchiveOutputStream(outputStream);
    }

    @After
    public void tearDown() throws IOException {
        tarArchiveOutputStream.close();
        outputStream.close();
    }

    @Test
    public void testTarArchiveOutputStreamConstructor() {
        Assert.assertNotNull(tarArchiveOutputStream);
    }

    @Test
    public void testPutArchiveEntry() throws IOException {
        ArchiveEntry entry = new TarEntry("test.txt");
        tarArchiveOutputStream.putArchiveEntry(entry);
        Assert.assertEquals(0, entry.getSize());
    }

    @Test(expected = IOException.class)
    public void testPutArchiveEntryFinished() throws IOException {
        tarArchiveOutputStream.finish();
        tarArchiveOutputStream.putArchiveEntry(new TarEntry("test.txt"));
    }

    @Test
    public void testCloseArchiveEntry() throws IOException {
        tarArchiveOutputStream.putArchiveEntry(new TarEntry("test.txt"));
        tarArchiveOutputStream.closeArchiveEntry();
        Assert.assertFalse(tarArchiveOutputStream.haveUnclosedEntry);
    }

    @Test(expected = IOException.class)
    public void testCloseArchiveEntryWithoutEntry() throws IOException {
        tarArchiveOutputStream.closeArchiveEntry();
    }

    @Test(expected = IOException.class)
    public void testCloseArchiveEntryFinished() throws IOException {
        tarArchiveOutputStream.finish();
        tarArchiveOutputStream.closeArchiveEntry();
    }

    @Test
    public void testWrite() throws IOException {
        byte[] data = "This is a test".getBytes();
        tarArchiveOutputStream.putArchiveEntry(new TarEntry("test.txt"));
        tarArchiveOutputStream.write(data);
        tarArchiveOutputStream.closeArchiveEntry();
        tarArchiveOutputStream.finish();

        byte[] output = outputStream.toByteArray();
        Assert.assertEquals(data.length, output.length);
    }

    @Test(expected = IOException.class)
    public void testWriteFinished() throws IOException {
        tarArchiveOutputStream.finish();
        tarArchiveOutputStream.write("This is a test".getBytes());
    }

    @Test
    public void testFinish() throws IOException {
        tarArchiveOutputStream.finish();
        Assert.assertTrue(tarArchiveOutputStream.finished);
    }

    @Test(expected = IOException.class)
    public void testFinishTwice() throws IOException {
        tarArchiveOutputStream.finish();
        tarArchiveOutputStream.finish();
    }

    @Test
    public void testClose() throws IOException {
        tarArchiveOutputStream.close();
        Assert.assertTrue(tarArchiveOutputStream.closed);
    }

    @Test(expected = IOException.class)
    public void testCloseTwice() throws IOException {
        tarArchiveOutputStream.finish();
        tarArchiveOutputStream.close();
        tarArchiveOutputStream.close();
    }

    @Test
    public void testGetRecordSize() {
        Assert.assertEquals(TarConstants.DEFAULT_RCDSIZE, tarArchiveOutputStream.getRecordSize());
    }

    @Test
    public void testSetLongFileMode() {
        tarArchiveOutputStream.setLongFileMode(TarConstants.LONGFILE_GNU);
        Assert.assertEquals(TarConstants.LONGFILE_GNU, tarArchiveOutputStream.longFileMode);
    }

    @Test
    public void testCreateArchiveEntry() throws IOException {
        File file = new File("test.txt");
        ArchiveEntry entry = tarArchiveOutputStream.createArchiveEntry(file, "test.txt");
        Assert.assertEquals(file, ((TarEntry) entry).getFile());
        Assert.assertEquals("test.txt", entry.getName());
    }

    @Test(expected = IOException.class)
    public void testCreateArchiveEntryFinished() throws IOException {
        tarArchiveOutputStream.finish();
        tarArchiveOutputStream.createArchiveEntry(new File("test.txt"), "test.txt");
    }

}

import org.junit.Assert;
import org.junit.Test;
import org.apache.commons.compress.archivers.tar.TarArchiveOutputStream;
import org.apache.commons.compress.archivers.tar.TarArchiveEntry;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class TarArchiveOutputStreamTest {

    @Test
    public void testSetLongFileMode() {
        TarArchiveOutputStream tarOutputStream = new TarArchiveOutputStream(new ByteArrayOutputStream());
        tarOutputStream.setLongFileMode(TarArchiveOutputStream.LONGFILE_GNU);
        Assert.assertEquals(TarArchiveOutputStream.LONGFILE_GNU, tarOutputStream.longFileMode);
    }

    @Test
    public void testPutArchiveEntry() throws IOException {
        OutputStream outputStream = new ByteArrayOutputStream();
        TarArchiveOutputStream tarOutputStream = new TarArchiveOutputStream(outputStream);
        TarArchiveEntry archiveEntry = new TarArchiveEntry("test.txt");
        tarOutputStream.putArchiveEntry(archiveEntry);
        tarOutputStream.closeArchiveEntry();
        tarOutputStream.close();
        Assert.assertTrue(outputStream.toString().contains("test.txt"));
    }

    @Test(expected = IOException.class)
    public void testCloseArchiveEntryWithoutPutArchiveEntry() throws IOException {
        OutputStream outputStream = new ByteArrayOutputStream();
        TarArchiveOutputStream tarOutputStream = new TarArchiveOutputStream(outputStream);
        tarOutputStream.closeArchiveEntry();
    }

    @Test(expected = IOException.class)
    public void testCloseArchiveEntryIncorrectNumberOfBytesWritten() throws IOException {
        OutputStream outputStream = new ByteArrayOutputStream();
        TarArchiveOutputStream tarOutputStream = new TarArchiveOutputStream(outputStream);
        TarArchiveEntry archiveEntry = new TarArchiveEntry("test.txt");
        archiveEntry.setSize(10);
        tarOutputStream.putArchiveEntry(archiveEntry);
        tarOutputStream.write(new byte[5]);
        tarOutputStream.closeArchiveEntry();
    }

    @Test
    public void testWrite() throws IOException {
        OutputStream outputStream = new ByteArrayOutputStream();
        TarArchiveOutputStream tarOutputStream = new TarArchiveOutputStream(outputStream);
        TarArchiveEntry archiveEntry = new TarArchiveEntry("test.txt");
        archiveEntry.setSize(10);
        tarOutputStream.putArchiveEntry(archiveEntry);
        tarOutputStream.write("Hello, world!".getBytes());
        tarOutputStream.closeArchiveEntry();
        tarOutputStream.close();
        Assert.assertTrue(outputStream.toString().contains("Hello, world!"));
    }

    @Test(expected = IOException.class)
    public void testFinishAlreadyFinished() throws IOException {
        OutputStream outputStream = new ByteArrayOutputStream();
        TarArchiveOutputStream tarOutputStream = new TarArchiveOutputStream(outputStream);
        tarOutputStream.finish();
        tarOutputStream.finish();
    }

    @Test(expected = IOException.class)
    public void testFinishUnclosedEntries() throws IOException {
        OutputStream outputStream = new ByteArrayOutputStream();
        TarArchiveOutputStream tarOutputStream = new TarArchiveOutputStream(outputStream);
        tarOutputStream.putArchiveEntry(new TarArchiveEntry("test.txt"));
        tarOutputStream.finish();
    }

    @Test
    public void testCloseArchive() throws IOException {
        OutputStream outputStream = new ByteArrayOutputStream();
        TarArchiveOutputStream tarOutputStream = new TarArchiveOutputStream(outputStream);
        tarOutputStream.putArchiveEntry(new TarArchiveEntry("test.txt"));
        tarOutputStream.closeArchiveEntry();
        tarOutputStream.close();

        Assert.assertTrue(tarOutputStream.closed);
    }

    @Test(expected = IOException.class)
    public void testCloseAlreadyClosed() throws IOException {
        OutputStream outputStream = new ByteArrayOutputStream();
        TarArchiveOutputStream tarOutputStream = new TarArchiveOutputStream(outputStream);
        tarOutputStream.close();
        tarOutputStream.close();
    }
}


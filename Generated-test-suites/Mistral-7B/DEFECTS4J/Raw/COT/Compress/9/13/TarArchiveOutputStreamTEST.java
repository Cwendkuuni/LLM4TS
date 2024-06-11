import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveOutputStream;
import org.apache.commons.compress.utils.CountingOutputStream;
import org.apache.commons.compress.utils.TarConstants;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

public class TarArchiveOutputStreamTest {

    private TarArchiveOutputStream tarOutputStream;
    private ByteArrayOutputStream outputStream;

    @Before
    public void setUp() throws IOException {
        this.outputStream = new ByteArrayOutputStream();
        this.tarOutputStream = new TarArchiveOutputStream(new CountingOutputStream(this.outputStream), 512);
    }

    @After
    public void tearDown() throws IOException {
        if (this.tarOutputStream != null) {
            this.tarOutputStream.finish();
            this.tarOutputStream.close();
        }
        Assert.assertArrayEquals(getExpectedTarFile(), this.outputStream.toByteArray());
    }

    @Test
    public void testConstructor() {
        TarArchiveOutputStream tarOutputStream = new TarArchiveOutputStream(new CountingOutputStream(new ByteArrayOutputStream()), 512);
        Assert.assertNotNull(tarOutputStream);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorWithNullOutputStream() {
        new TarArchiveOutputStream(null, 512);
    }

    @Test
    public void testSetLongFileMode() {
        TarArchiveOutputStream tarOutputStream = new TarArchiveOutputStream(new CountingOutputStream(new ByteArrayOutputStream()), 512);
        tarOutputStream.setLongFileMode(TarArchiveOutputStream.LONGFILE_TRUNCATE);
        Assert.assertEquals(TarArchiveOutputStream.LONGFILE_TRUNCATE, tarOutputStream.getLongFileMode());
    }

    @Test
    public void testFinish() throws IOException {
        TarArchiveEntry testEntry = new TarArchiveEntry("test.txt");
        tarOutputStream.putArchiveEntry(testEntry);
        tarOutputStream.write("Hello World!".getBytes());
        tarOutputStream.closeArchiveEntry();
        tarOutputStream.finish();
        Assert.assertEquals(1284, tarOutputStream.getCount()); // 512 bytes record size * 2 (2 EOF records) + 11 bytes header + 11 bytes data
    }

    @Test(expected = IllegalStateException.class)
    public void testFinishTwice() throws IOException {
        TarArchiveEntry testEntry = new TarArchiveEntry("test.txt");
        tarOutputStream.putArchiveEntry(testEntry);
        tarOutputStream.write("Hello World!".getBytes());
        tarOutputStream.closeArchiveEntry();
        tarOutputStream.finish();
        tarOutputStream.finish();
    }

    @Test(expected = IllegalStateException.class)
    public void testCloseAfterFinish() throws IOException {
        TarArchiveEntry testEntry = new TarArchiveEntry("test.txt");
        tarOutputStream.putArchiveEntry(testEntry);
        tarOutputStream.write("Hello World!".getBytes());
        tarOutputStream.closeArchiveEntry();
        tarOutputStream.finish();
        tarOutputStream.close();
    }

    @Test(expected = IllegalStateException.class)
    public void testPutArchiveEntryAfterFinish() throws IOException {
        TarArchiveEntry testEntry = new TarArchiveEntry("test.txt");
        tarOutputStream.putArchiveEntry(testEntry);
        tarOutputStream.write("Hello World!".getBytes());
        tarOutputStream.closeArchiveEntry();
        tarOutputStream.finish();
        TarArchiveEntry newTestEntry = new TarArchiveEntry("newTest.txt");
        tarOutputStream.putArchiveEntry(newTestEntry);
    }

    @Test(expected = IllegalStateException.class)
    public void testCloseArchiveEntryAfterFinish() throws IOException {
        TarArchiveEntry testEntry = new TarArchiveEntry("test.txt");
        tarOutputStream.putArchiveEntry(testEntry);
        tarOutputStream.write("Hello World!".getBytes());
        tarOutputStream.closeArchiveEntry();
        tarOutputStream.finish();
        tarOutputStream.closeArchiveEntry();
    }

    @Test(expected = IOException.class)
    public void testPutArchiveEntryWithLongFileName() throws IOException {
        String longFileName = "longFileName" + new String(new char[TarConstants.NAMELEN + 1]);
        TarArchiveEntry testEntry = new TarArchiveEntry(longFileName);
        tarOutputStream.setLongFileMode(TarArchiveOutputStream.LONGFILE_GNU);
        tarOutputStream.putArchiveEntry(testEntry);
        tarOutputStream.write("Hello World!".getBytes());
        tarOutputStream.closeArchiveEntry();
        tarOutputStream.finish();
        InputStream inputStream = new ByteArrayInputStream(this.outputStream.toByteArray());
        TarArchiveEntry entry = new TarArchiveEntry(inputStream);
        Assert.assertEquals(longFileName, entry.getName());
    }

    @Test(expected = RuntimeException.class)
    public void testPutArchiveEntryWithFileNameLongerThanNameLen() throws IOException {
        String longFileName = "longFileName" + new String(new char[TarConstants.NAMELEN + 1]);
        TarArchiveEntry testEntry = new TarArchiveEntry(longFileName);
        tarOutputStream.setLongFileMode(TarArchiveOutputStream.LONGFILE_TRUNCATE);
        tarOutputStream.putArchiveEntry(testEntry);
        tarOutputStream.write("Hello World!".getBytes());
        tarOutputStream.closeArchiveEntry();
        tarOutputStream.finish();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetLongFileModeWithInvalidMode() throws IOException {
        TarArchiveOutputStream tarOutputStream = new TarArchiveOutputStream(new CountingOutputStream(new ByteArrayOutputStream()), 512);
        tarOutputStream.setLongFileMode(-1);
    }

    private byte[] getExpectedTarFile() {
        // Add your expected tar file byte array here
        return new byte[0];
    }
}
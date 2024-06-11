import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarConstants;
import org.apache.commons.compress.archivers.tar.TarArchiveOutputStream;
import org.apache.commons.compress.utils.CountingOutputStream;
import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

public class TarArchiveOutputStreamTest {

    @Test
    public void testConstructor() throws IOException {
        TarArchiveOutputStream outputStream = new TarArchiveOutputStream(new CountingOutputStream(new ByteArrayOutputStream()));
        Assert.assertNotNull(outputStream);
    }

    @Test
    public void testSetLongFileMode() throws IOException {
        TarArchiveOutputStream outputStream = new TarArchiveOutputStream(new CountingOutputStream(new ByteArrayOutputStream()));
        outputStream.setLongFileMode(TarArchiveOutputStream.LONGFILE_TRUNCATE);
        Assert.assertEquals(TarArchiveOutputStream.LONGFILE_TRUNCATE, outputStream.getLongFileMode());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidLongFileMode() throws IOException {
        TarArchiveOutputStream outputStream = new TarArchiveOutputStream(new CountingOutputStream(new ByteArrayOutputStream()));
        outputStream.setLongFileMode(Integer.MIN_VALUE);
    }

    @Test
    public void testFinish() throws IOException {
        TarArchiveOutputStream outputStream = new TarArchiveOutputStream(new CountingOutputStream(new ByteArrayOutputStream()));
        outputStream.finish();
        Assert.assertTrue(outputStream.isFinished());
    }

    @Test(expected = IllegalStateException.class)
    public void testFinishTwice() throws IOException {
        TarArchiveOutputStream outputStream = new TarArchiveOutputStream(new CountingOutputStream(new ByteArrayOutputStream()));
        outputStream.finish();
        outputStream.finish();
    }

    @Test(expected = IOException.class)
    public void testFinishWithUnclosedEntry() throws IOException {
        TarArchiveOutputStream outputStream = new TarArchiveOutputStream(new CountingOutputStream(new ByteArrayOutputStream()));
        outputStream.putArchiveEntry(new TarArchiveEntry(new File("test.txt")));
        outputStream.finish();
    }

    @Test
    public void testClose() throws IOException {
        TarArchiveOutputStream outputStream = new TarArchiveOutputStream(new CountingOutputStream(new ByteArrayOutputStream()));
        outputStream.close();
        Assert.assertTrue(outputStream.isClosed());
    }

    @Test(expected = IllegalStateException.class)
    public void testCloseTwice() throws IOException {
        TarArchiveOutputStream outputStream = new TarArchiveOutputStream(new CountingOutputStream(new ByteArrayOutputStream()));
        outputStream.close();
        outputStream.close();
    }

    @Test(expected = IOException.class)
    public void testCloseWithUnfinishedArchive() throws IOException {
        TarArchiveOutputStream outputStream = new TarArchiveOutputStream(new CountingOutputStream(new ByteArrayOutputStream()));
        outputStream.close();
    }

    @Test
    public void testGetRecordSize() throws IOException {
        TarArchiveOutputStream outputStream = new TarArchiveOutputStream(new CountingOutputStream(new ByteArrayOutputStream()));
        int recordSize = outputStream.getRecordSize();
        Assert.assertNotEquals(-1, recordSize);
    }

    @Test(expected = IOException.class)
    public void testPutArchiveEntryWithNullArchiveEntry() throws IOException {
        TarArchiveOutputStream outputStream = new TarArchiveOutputStream(new CountingOutputStream(new ByteArrayOutputStream()));
        outputStream.putArchiveEntry(null);
    }

    @Test(expected = ClassCastException.class)
    public void testPutArchiveEntryWithNonTarArchiveEntry() throws IOException {
        TarArchiveOutputStream outputStream = new TarArchiveOutputStream(new CountingOutputStream(new ByteArrayOutputStream()));
        outputStream.putArchiveEntry(new File("test.txt"));
    }

    @Test(expected = IOException.class)
    public void testPutArchiveEntryWithLongFileName() throws IOException {
        TarArchiveOutputStream outputStream = new TarArchiveOutputStream(new CountingOutputStream(new ByteArrayOutputStream()), TarConstants.NAMELEN + 1, TarConstants.NAMELEN + 1);
        outputStream.setLongFileMode(TarArchiveOutputStream.LONGFILE_GNU);
        outputStream.putArchiveEntry(new TarArchiveEntry("a" + new String(new char[TarConstants.NAMELEN + 1]) + "b"));
    }

    @Test
    public void testWriteToStream() throws IOException {
        TarArchiveOutputStream outputStream = new TarArchiveOutputStream(new CountingOutputStream(new ByteArrayOutputStream()));
        byte[] data = "test data".getBytes(StandardCharsets.UTF_8);
        outputStream.putArchiveEntry(new TarArchiveEntry("entry.txt"));
        outputStream.write(data, 0, data.length);
        outputStream.closeArchiveEntry();
        byte[] expectedOutput = Arrays.copyOfRange(data, 0, data.length);
        byte[] actualOutput = outputStream.getBuffer().getBuffer();
        Assert.assertArrayEquals(expectedOutput, actualOutput);
    }

    @Test
    public void testWriteToStreamWithLongFileName() throws IOException {
        TarArchiveOutputStream outputStream = new TarArchiveOutputStream(new CountingOutputStream(new ByteArrayOutputStream()), TarConstants.NAMELEN + 1, TarConstants.NAMELEN + 1);
        outputStream.setLongFileMode(TarArchiveOutputStream.LONGFILE_GNU);
        byte[] data = "test data".getBytes(StandardCharsets.UTF_8);
        outputStream.putArchiveEntry(new TarArchiveEntry("a" + new String(new char[TarConstants.NAMELEN + 1]) + "b/entry.txt"));
        outputStream.write(data, 0, data.length);
        outputStream.closeArchiveEntry();
        byte[] expectedOutput = Arrays.copyOfRange(data, 0, data.length);
        byte[] actualOutput = outputStream.getBuffer().getBuffer();
        Assert.assertArrayEquals(expectedOutput, actualOutput);
    }

    @Test(expected = IOException.class)
    public void testWriteToStreamWithUnclosedEntry() throws IOException {
        TarArchiveOutputStream outputStream = new TarArchiveOutputStream(new CountingOutputStream(new ByteArrayOutputStream()));
        outputStream.putArchiveEntry(new TarArchiveEntry("entry.txt"));
        byte[] data = "test data".getBytes(StandardCharsets.UTF_8);
        outputStream.write(data, 0, data.length);
    }

    @Test
    public void testCreateArchiveEntry() throws IOException {
        TarArchiveOutputStream outputStream = new TarArchiveOutputStream(new CountingOutputStream(new ByteArrayOutputStream()));
        File testFile = new File("test.txt");
        TarArchiveEntry entry = outputStream.createArchiveEntry(testFile, "entry.txt");
        Assert.assertTrue(entry instanceof TarArchiveEntry);
    }

    @Test(expected = FileNotFoundException.class)
    public void testCreateArchiveEntryWithNonExistentFile() throws IOException {
        TarArchiveOutputStream outputStream = new TarArchiveOutputStream(new CountingOutputStream(new ByteArrayOutputStream()));
        File nonExistentFile = new File("nonExistentFile.txt");
        outputStream.createArchiveEntry(nonExistentFile, "entry.txt");
    }
}
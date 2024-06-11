import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarConstants;
import org.apache.commons.compress.archivers.tar.TarOutputStream;
import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class TarArchiveOutputStreamTest {

    @Test
    public void testConstructor() {
        TarOutputStream tarOutputStream = new TarOutputStream(new ByteArrayOutputStream());
        Assert.assertNotNull(tarOutputStream);
    }

    @Test
    public void testSetLongFileMode() {
        TarOutputStream tarOutputStream = new TarOutputStream(new ByteArrayOutputStream());
        tarOutputStream.setLongFileMode(TarArchiveOutputStream.LONGFILE_TRUNCATE);
        Assert.assertEquals(TarArchiveOutputStream.LONGFILE_TRUNCATE, tarOutputStream.getLongFileMode());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidLongFileMode() {
        TarOutputStream tarOutputStream = new TarOutputStream(new ByteArrayOutputStream());
        tarOutputStream.setLongFileMode(Integer.MIN_VALUE);
    }

    @Test
    public void testFinishWithoutEntries() throws IOException {
        TarOutputStream tarOutputStream = new TarOutputStream(new ByteArrayOutputStream());
        tarOutputStream.finish();
        byte[] eofRecord = tarOutputStream.getBuffer().getRecord();
        Assert.assertArrayEquals(TarConstants.EOF_MAGIC, eofRecord, 0, 512);
        tarOutputStream.finish();
    }

    @Test
    public void testFinishWithEntries() throws IOException {
        TarOutputStream tarOutputStream = new TarOutputStream(new ByteArrayOutputStream());
        ArchiveEntry entry = new TarArchiveEntry("testEntry.txt");
        tarOutputStream.putArchiveEntry(entry);
        tarOutputStream.write((byte[]) "test".getBytes(), 0, 3);
        tarOutputStream.closeArchiveEntry();
        tarOutputStream.finish();
        byte[] eofRecord = tarOutputStream.getBuffer().getRecord();
        byte[] entryRecord = tarOutputStream.getBuffer().getRecord();
        Assert.assertArrayEquals(TarConstants.NORMAL_MAGIC, entryRecord, 0, 512);
        Assert.assertArrayEquals(TarConstants.EOF_MAGIC, eofRecord, 0, 512);
        Assert.assertArrayEquals(TarConstants.EOF_MAGIC, eofRecord, 512, 512);
    }

    @Test(expected = IOException.class)
    public void testPutArchiveEntryWithLongFileName() throws IOException {
        String longFileName = "testLongFileNameThatIsTooLong.txt";
        TarOutputStream tarOutputStream = new TarOutputStream(new ByteArrayOutputStream());
        ArchiveEntry entry = new TarArchiveEntry(longFileName);
        tarOutputStream.putArchiveEntry(entry);
    }

    @Test
    public void testPutArchiveEntryWithShortFileName() throws IOException {
        TarOutputStream tarOutputStream = new TarArchiveOutputStream(new ByteArrayOutputStream());
        ArchiveEntry entry = new TarArchiveEntry("testShortFileName.txt");
        tarOutputStream.putArchiveEntry(entry);
        Assert.assertEquals("testShortFileName.txt", tarOutputStream.getCurentName());
    }

    @Test
    public void testCloseArchiveEntry() throws IOException {
        TarOutputStream tarOutputStream = new TarOutputStream(new ByteArrayOutputStream());
        ArchiveEntry entry = new TarArchiveEntry("testEntry.txt");
        tarOutputStream.putArchiveEntry(entry);
        tarOutputStream.write((byte[]) "test".getBytes(), 0, 3);
        tarOutputStream.closeArchiveEntry();
        byte[] entryRecord = tarOutputStream.getBuffer().getRecord();
        Assert.assertArrayEquals(TarConstants.NORMAL_MAGIC, entryRecord, 0, 512);
    }

    @Test(expected = IOException.class)
    public void testWriteBeyondEntrySize() throws IOException {
        TarOutputStream tarOutputStream = new TarOutputStream(new ByteArrayOutputStream());
        ArchiveEntry entry = new TarArchiveEntry("testEntry.txt");
        tarOutputStream.putArchiveEntry(entry);
        tarOutputStream.write((byte[]) "test".getBytes(), 0, 4);
    }

    @Test
    public void testWrite() throws IOException {
        TarOutputStream tarOutputStream = new TarOutputStream(new ByteArrayOutputStream());
        ArchiveEntry entry = new TarArchiveEntry("testEntry.txt");
        tarOutputStream.putArchiveEntry(entry);
        tarOutputStream.write((byte[]) "test".getBytes(), 0, 3);
        byte[] record = tarOutputStream.getBuffer().getRecord();
        Assert.assertArrayEquals(TarConstants.NORMAL_MAGIC, record, 0, 512);
        Assert.assertArrayEquals("test".getBytes(), tarOutputStream.getBuffer().getBuffer(), 12, 3);
    }

    @Test
    public void testWriteMultipleEntries() throws IOException {
        TarOutputStream tarOutputStream = new TarOutputStream(new ByteArrayOutputStream());
        ArchiveEntry entry1 = new TarArchiveEntry("testEntry1.txt");
        ArchiveEntry entry2 = new TarArchiveEntry("testEntry2.txt");
        tarOutputStream.putArchiveEntry(entry1);
        tarOutputStream.write((byte[]) "test1".getBytes(), 0, 3);
        tarOutputStream.closeArchiveEntry();
        tarOutputStream.putArchiveEntry(entry2);
        tarOutputStream.write((byte[]) "test2".getBytes(), 0, 3);
        tarOutputStream.closeArchiveEntry();
        byte[] record1 = tarOutputStream.getBuffer().getRecord();
        byte[] record2 = tarOutputStream.getBuffer().getRecord();
        Assert.assertArrayEquals(TarConstants.NORMAL_MAGIC, record1, 0, 512);
        Assert.assertArrayEquals(TarConstants.NORMAL_MAGIC, record2, 0, 512);
        Assert.assertArrayEquals("test1".getBytes(), tarOutputStream.getBuffer().getBuffer(), 12, 3);
        Assert.assertArrayEquals("test2".getBytes(), tarOutputStream.getBuffer().getBuffer(), 12 + 512, 3);
    }

    @Test
    public void testCreateArchiveEntry() throws IOException {
        TarOutputStream tarOutputStream = new TarOutputStream(new ByteArrayOutputStream());
        File testFile = new File("testFile.txt");
        ArchiveEntry entry = tarOutputStream.createArchiveEntry(testFile, "testFile.txt");
        tarOutputStream.putArchiveEntry(entry);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateArchiveEntryNullFileName() throws IOException {
        TarOutputStream tarOutputStream = new TarOutputStream(new ByteArrayOutputStream());
        ArchiveEntry entry = tarOutputStream.createArchiveEntry(null, "testFile.txt");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateArchiveEntryNullFile() throws IOException {
        TarOutputStream tarOutputStream = new TarOutputStream(new ByteArrayOutputStream());
        ArchiveEntry entry = tarOutputStream.createArchiveEntry(null, null);
    }
}
import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarConstants;
import org.apache.commons.compress.archivers.tar.TarBuffer;
import org.apache.commons.compress.archivers.tar.TarArchiveOutputStream;
import org.apache.commons.compress.archivers.tar.LongLinkArchiveEntry;
import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class TarArchiveOutputStreamTest {

    @Test
    public void testConstructor() {
        TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(new ByteArrayOutputStream());
        Assert.assertNotNull(tarArchiveOutputStream);
    }

    @Test
    public void testSetLongFileMode() throws IOException {
        TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(new ByteArrayOutputStream());
        tarArchiveOutputStream.setLongFileMode(TarArchiveOutputStream.LONGFILE_TRUNCATE);
        Assert.assertEquals(TarArchiveOutputStream.LONGFILE_TRUNCATE, tarArchiveOutputStream.getLongFileMode());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidLongFileMode() throws IOException {
        TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(new ByteArrayOutputStream());
        tarArchiveOutputStream.setLongFileMode(4);
    }

    @Test
    public void testFinish() throws IOException {
        TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(new ByteArrayOutputStream());
        tarArchiveOutputStream.finish();
        Assert.assertFalse(tarArchiveOutputStream.isOpen());
    }

    @Test(expected = IOException.class)
    public void testFinishWithUnclosedEntry() throws IOException {
        TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(new ByteArrayOutputStream());
        TarArchiveEntry archiveEntry = new TarArchiveEntry("test.txt");
        tarArchiveOutputStream.putArchiveEntry(archiveEntry);
        tarArchiveOutputStream.finish();
    }

    @Test
    public void testClose() throws IOException {
        TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(new ByteArrayOutputStream());
        TarArchiveEntry archiveEntry = new TarArchiveEntry("test.txt");
        tarArchiveOutputStream.putArchiveEntry(archiveEntry);
        tarArchiveOutputStream.close();
        Assert.assertFalse(tarArchiveOutputStream.isOpen());
    }

    @Test(expected = IOException.class)
    public void testCloseWithUnclosedEntry() throws IOException {
        TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(new ByteArrayOutputStream());
        TarArchiveEntry archiveEntry = new TarArchiveEntry("test.txt");
        tarArchiveOutputStream.putArchiveEntry(archiveEntry);
        tarArchiveOutputStream.finish();
        tarArchiveOutputStream.close();
    }

    @Test
    public void testGetRecordSize() throws IOException {
        TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(new ByteArrayOutputStream());
        int recordSize = tarArchiveOutputStream.getRecordSize();
        Assert.assertNotEquals(-1, recordSize);
    }

    @Test
    public void testPutArchiveEntry() throws IOException {
        TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(new ByteArrayOutputStream());
        TarArchiveEntry archiveEntry = new TarArchiveEntry("test.txt");
        tarArchiveOutputStream.putArchiveEntry(archiveEntry);
        Assert.assertTrue(tarArchiveOutputStream.isOpen());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testPutNullArchiveEntry() throws IOException {
        TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(new ByteArrayOutputStream());
        tarArchiveOutputStream.putArchiveEntry(null);
    }

    @Test(expected = IOException.class)
    public void testPutArchiveEntryWithEmptyName() throws IOException {
        TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(new ByteArrayOutputStream());
        TarArchiveEntry archiveEntry = new TarArchiveEntry("", "test.txt");
        tarArchiveOutputStream.putArchiveEntry(archiveEntry);
    }

    @Test(expected = IOException.class)
    public void testPutArchiveEntryWithLongName() throws IOException {
        TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(new ByteArrayOutputStream());
        String longFileName = new String(new char[TarConstants.NAMELEN + 1]);
        final byte[] nameBytes = longFileName.getBytes(StandardCharsets.UTF_8);

        TarArchiveEntry archiveEntry = new TarArchiveEntry(TarConstants.GNU_LONGLINK, TarConstants.LF_GNUTYPE_LONGNAME);
        archiveEntry.setName(longFileName);
        archiveEntry.setSize(nameBytes.length + 1); // +1 for NUL

        tarArchiveOutputStream.putArchiveEntry(archiveEntry);
    }

    @Test(expected = IOException.class)
    public void testPutArchiveEntryWithLongNameAndLongFileModeError() throws IOException {
        TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(new ByteArrayOutputStream());
        tarArchiveOutputStream.setLongFileMode(TarArchiveOutputStream.LONGFILE_ERROR);
        String longFileName = new String(new char[TarConstants.NAMELEN + 1]);
        final byte[] nameBytes = longFileName.getBytes(StandardCharsets.UTF_8);

        TarArchiveEntry archiveEntry = new TarArchiveEntry(TarConstants.GNU_LONGLINK, TarConstants.LF_GNUTYPE_LONGNAME);
        archiveEntry.setName(longFileName);
        archiveEntry.setSize(nameBytes.length + 1); // +1 for NUL

        tarArchiveOutputStream.putArchiveEntry(archiveEntry);
    }

    @Test(expected = IOException.class)
    public void testPutArchiveEntryWithLongNameAndLongFileModeTruncate() throws IOException {
        TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(new ByteArrayOutputStream());
        tarArchiveOutputStream.setLongFileMode(TarArchiveOutputStream.LONGFILE_TRUNCATE);
        String longFileName = new String(new char[TarConstants.NAMELEN + 1]);
        final byte[] nameBytes = longFileName.getBytes(StandardCharsets.UTF_8);

        TarArchiveEntry archiveEntry = new TarArchiveEntry(TarConstants.GNU_LONGLINK, TarConstants.LF_GNUTYPE_LONGNAME);
        archiveEntry.setName(longFileName);
        archiveEntry.setSize(nameBytes.length + 1); // +1 for NUL

        tarArchiveOutputStream.putArchiveEntry(archiveEntry);
    }

    @Test(expected = IOException.class)
    public void testPutArchiveEntryWithLongNameAndLongFileModeGNU() throws IOException {
        TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(new ByteArrayOutputStream());
        tarArchiveOutputStream.setLongFileMode(TarArchiveOutputStream.LONGFILE_GNU);
        String longFileName = new String(new char[TarConstants.NAMELEN + 1]);
        final byte[] nameBytes = longFileName.getBytes(StandardCharsets.UTF_8);

        TarArchiveEntry archiveEntry = new TarArchiveEntry(TarConstants.GNU_LONGLINK, TarConstants.LF_GNUTYPE_LONGNAME);
        archiveEntry.setName(longFileName);
        archiveEntry.setSize(nameBytes.length + 1); // +1 for NUL

        tarArchiveOutputStream.putArchiveEntry(archiveEntry);
    }

    @Test
    public void testCloseArchiveEntry() throws IOException {
        TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(new ByteArrayOutputStream());
        TarArchiveEntry archiveEntry = new TarArchiveEntry("test.txt");
        tarArchiveOutputStream.putArchiveEntry(archiveEntry);
        tarArchiveOutputStream.closeArchiveEntry();
        Assert.assertFalse(tarArchiveOutputStream.isOpen());
    }

    @Test(expected = IOException.class)
    public void testCloseArchiveEntryWithUnclosedEntry() throws IOException {
        TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(new ByteArrayOutputStream());
        TarArchiveEntry archiveEntry = new TarArchiveEntry("test.txt");
        tarArchiveOutputStream.putArchiveEntry(archiveEntry);
        tarArchiveOutputStream.finish();
        tarArchiveOutputStream.closeArchiveEntry();
    }

    @Test
    public void testWrite() throws IOException {
        TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(new ByteArrayOutputStream());
        TarArchiveEntry archiveEntry = new TarArchiveEntry("test.txt");
        tarArchiveOutputStream.putArchiveEntry(archiveEntry);
        byte[] data = "test data".getBytes();
        tarArchiveOutputStream.write(data);
        tarArchiveOutputStream.closeArchiveEntry();
        byte[] archiveData = tarArchiveOutputStream.toByteArray();

        TarArchiveEntry archiveEntryFromArchive = new TarArchiveEntry("test.txt");
        int expectedSize = archiveEntry.getSize();
        int actualSize = archiveEntryFromArchive.getSize();

        Assert.assertEquals(expectedSize, actualSize);
        Assert.assertTrue(new String(archiveData, archiveEntryFromArchive.getName().getBytes())
                .contains(new String(data)));
    }

    @Test
    public void testWriteWithUnclosedEntry() throws IOException {
        TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(new ByteArrayOutputStream());
        TarArchiveEntry archiveEntry = new TarArchiveEntry("test.txt");
        tarArchiveOutputStream.putArchiveEntry(archiveEntry);
        byte[] data = "test data".getBytes();
        tarArchiveOutputStream.write(data);

        // Write should throw an exception here
        tarArchiveOutputStream.write(data);

        tarArchiveOutputStream.closeArchiveEntry();
        byte[] archiveData = tarArchiveOutputStream.toByteArray();

        TarArchiveEntry archiveEntryFromArchive = new TarArchiveEntry("test.txt");
        int expectedSize = archiveEntry.getSize();
        int actualSize = archiveEntryFromArchive.getSize();

        Assert.assertEquals(expectedSize, actualSize);
        Assert.assertTrue(new String(archiveData, archiveEntryFromArchive.getName().getBytes())
                .contains(new String(data)));
    }

    @Test
    public void testWriteLongLink() throws IOException {
        TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(new ByteArrayOutputStream());
        TarArchiveEntry archiveEntry = new TarArchiveEntry(".", ".");
        archiveEntry.setName(new String(new char[TarConstants.NAMELEN + 1]));
        tarArchiveOutputStream.putArchiveEntry(archiveEntry);
        byte[] nameBytes = archiveEntry.getName().getBytes();
        tarArchiveOutputStream.write(nameBytes);
        tarArchiveOutputStream.write((byte) 0); // NUL terminator

        LongLinkArchiveEntry longLinkArchiveEntry = new LongLinkArchiveEntry(new File("."), ".");
        archiveEntry = new TarArchiveEntry(longLinkArchiveEntry);
        archiveEntry.setName(new String(new char[TarConstants.NAMELEN + 1]));
        tarArchiveOutputStream.putArchiveEntry(archiveEntry);
        nameBytes = archiveEntry.getName().getBytes();
        tarArchiveOutputStream.write(nameBytes);
        tarArchiveOutputStream.write((byte) 0); // NUL terminator

        tarArchiveOutputStream.finish();
        byte[] archiveData = tarArchiveOutputStream.toByteArray();

        TarArchiveEntry archiveEntryFromArchive = new TarArchiveEntry(".");
        int expectedSize = archiveEntry.getSize();
        int actualSize = archiveEntryFromArchive.getSize();

        Assert.assertEquals(expectedSize, actualSize);
        Assert.assertArrayEquals(nameBytes, archiveEntryFromArchive.getName().getBytes());

        TarArchiveEntry longLinkArchiveEntryFromArchive = new TarArchiveEntry(archiveEntryFromArchive.getName());
        expectedSize = longLinkArchiveEntry.getSize();
        actualSize = longLinkArchiveEntryFromArchive.getSize();

        Assert.assertEquals(expectedSize, actualSize);
        Assert.assertEquals(expectedSize, archiveEntryFromArchive.getSize());
    }
}
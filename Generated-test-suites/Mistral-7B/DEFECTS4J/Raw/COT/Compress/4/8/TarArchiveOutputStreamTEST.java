import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarConstants;
import org.apache.commons.compress.archivers.tar.TarBuffer;
import org.apache.commons.compress.archivers.tar.TarArchiveOutputStream;
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

    @Test(expected = RuntimeException.class)
    public void testSetLongFileNameWithLongNameAndErrorMode() throws IOException {
        TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(new ByteArrayOutputStream());
        String longFileName = "longFileNameWithLongLength".repeat(TarConstants.NAMELEN + 1);
        ArchiveEntry archiveEntry = new TarArchiveEntry(new File("nonExistentFile"));
        archiveEntry.setName(longFileName);
        tarArchiveOutputStream.putArchiveEntry(archiveEntry);
    }

    @Test
    public void testPutArchiveEntry() throws IOException {
        TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(new ByteArrayOutputStream());
        String fileName = "testFileName.txt";
        byte[] fileContent = "testContent".getBytes(StandardCharsets.UTF_8);
        ArchiveEntry archiveEntry = new TarArchiveEntry(new File("nonExistentFile"), fileName);

        tarArchiveOutputStream.putArchiveEntry(archiveEntry);
        assert (!tarArchiveOutputStream.haveUnclosedEntry);

        byte[] expectedRecordHeader = new byte[TarConstants.BLOCKSIZE];
        TarConstants.writeMagicNumber(expectedRecordHeader, TarConstants.MAGIC_TAR);
        TarConstants.writeByte(expectedRecordHeader, TarConstants.STYPE_REGULAR);
        TarConstants.writeByte(expectedRecordHeader, (byte) (fileName.length() & 0xFF));
        TarConstants.writeByte(expectedRecordHeader, (byte) ((fileName.length() >> 8) & 0xFF));
        TarConstants.writeByte(expectedRecordHeader, (byte) ((fileName.length() >> 16) & 0xFF));
        TarConstants.writeByte(expectedRecordHeader, (byte) ((fileName.length() >> 24) & 0xFF));
        TarConstants.writeBytes(expectedRecordHeader, fileName.getBytes(StandardCharsets.UTF_8));
        TarConstants.writeLong(expectedRecordHeader, TarConstants.FILEMODE_UNIX_RW_RW_RW);
        TarConstants.writeLong(expectedRecordHeader, TarConstants.FILEMODE_UNIX_USER_OWNER);
        TarConstants.writeLong(expectedRecordHeader, TarConstants.FILEMODE_UNIX_GROUP_GROUP);
        TarConstants.writeLong(expectedRecordHeader, TarConstants.NULL);
        TarConstants.writeLong(expectedRecordHeader, TarConstants.NULL);
        TarConstants.writeLong(expectedRecordHeader, TarConstants.NULL);
        TarConstants.writeLong(expectedRecordHeader, TarConstants.NULL);
        TarConstants.writeLong(expectedRecordHeader, TarConstants.NULL);
        TarConstants.writeLong(expectedRecordHeader, TarConstants.NULL);

        byte[] recordToBeWritten = new byte[TarConstants.BLOCKSIZE];
        System.arraycopy(expectedRecordHeader, 0, recordToBeWritten, 0, TarConstants.BLOCKSIZE);
        Assert.assertEquals(recordToBeWritten, tarArchiveOutputStream.recordBuf);

        tarArchiveOutputStream.write(fileContent, 0, fileContent.length);
        Assert.assertEquals(fileContent.length, tarArchiveOutputStream.currBytes);
        tarArchiveOutputStream.closeArchiveEntry();
        Assert.assertFalse(tarArchiveOutputStream.haveUnclosedEntry);

        byte[] actualRecordHeader = new byte[tarArchiveOutputStream.buffer.size()];
        tarArchiveOutputStream.buffer.getBytes(0, actualRecordHeader, 0, tarArchiveOutputStream.buffer.size());
        Assert.assertArrayEquals(expectedRecordHeader, actualRecordHeader);

        byte[] actualContent = new byte[fileContent.length];
        tarArchiveOutputStream.buffer.getBytes(TarConstants.BLOCKSIZE, actualContent, 0, fileContent.length);
        Assert.assertArrayEquals(fileContent, actualContent);
    }

    @Test
    public void testWrite() throws IOException {
        TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(new ByteArrayOutputStream());
        String fileName = "testFileName.txt";
        byte[] fileContent = "testContent".getBytes(StandardCharsets.UTF_8);
        ArchiveEntry archiveEntry = new TarArchiveEntry(new File("nonExistentFile"), fileName);
        tarArchiveOutputStream.putArchiveEntry(archiveEntry);
        tarArchiveOutputStream.write(fileContent, 0, fileContent.length);
        tarArchiveOutputStream.closeArchiveEntry();
    }

    @Test
    public void testFinish() throws IOException {
        TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(new ByteArrayOutputStream());
        String fileName = "testFileName.txt";
        byte[] fileContent = "testContent".getBytes(StandardCharsets.UTF_8);
        ArchiveEntry archiveEntry = new TarArchiveEntry(new File("nonExistentFile"), fileName);
        tarArchiveOutputStream.putArchiveEntry(archiveEntry);
        tarArchiveOutputStream.write(fileContent, 0, fileContent.length);
        tarArchiveOutputStream.finish();
        Assert.assertFalse(tarArchiveOutputStream.haveUnclosedEntry);
    }

    @Test
    public void testClose() throws IOException {
        TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(new ByteArrayOutputStream());
        String fileName = "testFileName.txt";
        byte[] fileContent = "testContent".getBytes(StandardCharsets.UTF_8);
        ArchiveEntry archiveEntry = new TarArchiveEntry(new File("nonExistentFile"), fileName);
        tarArchiveOutputStream.putArchiveEntry(archiveEntry);
        tarArchiveOutputStream.write(fileContent, 0, fileContent.length);
        tarArchiveOutputStream.close();
        Assert.assertTrue(tarArchiveOutputStream.closed);
    }
}
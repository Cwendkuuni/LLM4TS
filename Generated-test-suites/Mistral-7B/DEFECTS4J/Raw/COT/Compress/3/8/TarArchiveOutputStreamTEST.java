import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarConstants;
import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class TarArchiveOutputStreamTest {

    @Test
    public void testConstructor() {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(outputStream);
        Assert.assertNotNull(tarArchiveOutputStream);
    }

    @Test
    public void testSetLongFileMode() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(outputStream);

        tarArchiveOutputStream.setLongFileMode(TarArchiveOutputStream.LONGFILE_TRUNCATE);
        Assert.assertEquals(TarArchiveOutputStream.LONGFILE_TRUNCATE, tarArchiveOutputStream.getLongFileMode());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidLongFileMode() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(outputStream);
        tarArchiveOutputStream.setLongFileMode(4);
    }

    @Test
    public void testFinish() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(outputStream);
        tarArchiveOutputStream.finish();

        ByteArrayInputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
        ArchiveEntry archiveEntry = tarArchiveOutputStream.createArchiveEntry(new File("test.txt"), "test.txt");
        tarArchiveOutputStream.putArchiveEntry(archiveEntry);
        tarArchiveOutputStream.write(new byte[512], 0, 512);
        tarArchiveOutputStream.closeArchiveEntry();
        tarArchiveOutputStream.finish();

        Assert.assertEquals(2 * 512, inputStream.available());
    }

    @Test
    public void testClose() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(outputStream);
        tarArchiveOutputStream.finish();
        tarArchiveOutputStream.close();

        ByteArrayInputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
        ArchiveEntry archiveEntry = tarArchiveOutputStream.createArchiveEntry(new File("test.txt"), "test.txt");
        tarArchiveOutputStream.putArchiveEntry(archiveEntry);
        tarArchiveOutputStream.write(new byte[512], 0, 512);
        tarArchiveOutputStream.closeArchiveEntry();
        tarArchiveOutputStream.finish();
        tarArchiveOutputStream.close();

        Assert.assertEquals(2 * 512, inputStream.available());
    }

    @Test
    public void testPutArchiveEntry() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(outputStream);
        ArchiveEntry archiveEntry = tarArchiveOutputStream.createArchiveEntry(new File("test.txt"), "test.txt");
        tarArchiveOutputStream.putArchiveEntry(archiveEntry);

        byte[] buffer = new byte[512];
        tarArchiveOutputStream.write(buffer, 0, buffer.length);
        tarArchiveOutputStream.closeArchiveEntry();

        ByteArrayInputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
        byte[] expectedHeader = new byte[512];
        System.arraycopy(TarConstants.STD_MAGIC, 0, expectedHeader, 0, TarConstants.STD_MAGIC.length);
        byte[] entryName = "test.txt".getBytes();
        System.arraycopy(entryName, 0, expectedHeader, TarConstants.NAME_INDEX, entryName.length);
        System.arraycopy(new byte[TarConstants.NAMELEN + 1], 0, expectedHeader, TarConstants.NAME_INDEX + entryName.length, 1); // NUL terminator
        byte[] fileSize = new byte[8];
        long size = 512;
        System.arraycopy(Long.toString(size).getBytes(), 0, fileSize, 0, fileSize.length);
        System.arraycopy(fileSize, 0, expectedHeader, TarConstants.FILE_SIZE_INDEX, fileSize.length);
        byte[] modifiedTime = new byte[12];
        long currentTime = System.currentTimeMillis();
        System.arraycopy(Long.toString(currentTime).getBytes(), 0, modifiedTime, 0, modifiedTime.length);
        System.arraycopy(modifiedTime, 0, expectedHeader, TarConstants.MOD_TIME_INDEX, modifiedTime.length);
        System.arraycopy(new byte[TarConstants.REC_SIZE_INDEX - TarConstants.MOD_TIME_INDEX], 0, expectedHeader, TarConstants.MOD_TIME_INDEX + modifiedTime.length, TarConstants.REC_SIZE_INDEX - TarConstants.MOD_TIME_INDEX); // padding

        Assert.assertArrayEquals(expectedHeader, inputStream.readNBytes(expectedHeader.length));

        tarArchiveOutputStream.finish();
        Assert.assertEquals(inputStream.available(), 2 * 512);
    }

    @Test
    public void testCloseArchiveEntry() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(outputStream);
        ArchiveEntry archiveEntry = tarArchiveOutputStream.createArchiveEntry(new File("test.txt"), "test.txt");
        tarArchiveOutputStream.putArchiveEntry(archiveEntry);

        byte[] buffer = new byte[512];
        tarArchiveOutputStream.write(buffer, 0, buffer.length);
        tarArchiveOutputStream.closeArchiveEntry();

        ByteArrayInputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
        byte[] expectedHeader = new byte[512];
        System.arraycopy(TarConstants.STD_MAGIC, 0, expectedHeader, 0, TarConstants.STD_MAGIC.length);
        byte[] entryName = "test.txt".getBytes();
        System.arraycopy(entryName, 0, expectedHeader, TarConstants.NAME_INDEX, entryName.length);
        System.arraycopy(new byte[TarConstants.NAMELEN + 1], 0, expectedHeader, TarConstants.NAME_INDEX + entryName.length, 1); // NUL terminator
        byte[] fileSize = new byte[8];
        long size = 512;
        System.arraycopy(Long.toString(size).getBytes(), 0, fileSize, 0, fileSize.length);
        System.arraycopy(fileSize, 0, expectedHeader, TarConstants.FILE_SIZE_INDEX, fileSize.length);
        byte[] modifiedTime = new byte[12];
        long currentTime = System.currentTimeMillis();
        System.arraycopy(Long.toString(currentTime).getBytes(), 0, modifiedTime, 0, modifiedTime.length);
        System.arraycopy(modifiedTime, 0, expectedHeader, TarConstants.MOD_TIME_INDEX, modifiedTime.length);
        System.arraycopy(new byte[TarConstants.REC_SIZE_INDEX - TarConstants.MOD_TIME_INDEX], 0, expectedHeader, TarConstants.MOD_TIME_INDEX + modifiedTime.length, TarConstants.REC_SIZE_INDEX - TarConstants.MOD_TIME_INDEX); // padding

        Assert.assertArrayEquals(expectedHeader, inputStream.readNBytes(expectedHeader.length));

        tarArchiveOutputStream.finish();
        Assert.assertEquals(inputStream.available(), 2 * 512);
    }

    @Test
    public void testWrite() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(outputStream);
        ArchiveEntry archiveEntry = tarArchiveOutputStream.createArchiveEntry(new File("test.txt"), "test.txt");
        tarArchiveOutputStream.putArchiveEntry(archiveEntry);

        byte[] buffer = new byte[512];
        tarArchiveOutputStream.write(buffer, 0, buffer.length);
        tarArchiveOutputStream.closeArchiveEntry();

        ByteArrayInputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
        byte[] expectedHeader = new byte[512];
        System.arraycopy(TarConstants.STD_MAGIC, 0, expectedHeader, 0, TarConstants.STD_MAGIC.length);
        byte[] entryName = "test.txt".getBytes();
        System.arraycopy(entryName, 0, expectedHeader, TarConstants.NAME_INDEX, entryName.length);
        System.arraycopy(new byte[TarConstants.NAMELEN + 1], 0, expectedHeader, TarConstants.NAME_INDEX + entryName.length, 1); // NUL terminator
        byte[] fileSize = new byte[8];
        long size = 512;
        System.arraycopy(Long.toString(size).getBytes(), 0, fileSize, 0, fileSize.length);
        System.arraycopy(fileSize, 0, expectedHeader, TarConstants.FILE_SIZE_INDEX, fileSize.length);
        byte[] modifiedTime = new byte[12];
        long currentTime = System.currentTimeMillis();
        System.arraycopy(Long.toString(currentTime).getBytes(), 0, modifiedTime, 0, modifiedTime.length);
        System.arraycopy(modifiedTime, 0, expectedHeader, TarConstants.MOD_TIME_INDEX, modifiedTime.length);
        System.arraycopy(new byte[TarConstants.REC_SIZE_INDEX - TarConstants.MOD_TIME_INDEX], 0, expectedHeader, TarConstants.MOD_TIME_INDEX + modifiedTime.length, TarConstants.REC_SIZE_INDEX - TarConstants.MOD_TIME_INDEX); // padding

        Assert.assertArrayEquals(expectedHeader, inputStream.readNBytes(expectedHeader.length));

        byte[] data = new byte[256];
        tarArchiveOutputStream.write(data, 0, data.length);
        tarArchiveOutputStream.write(buffer, 0, buffer.length);

        tarArchiveOutputStream.finish();
        inputStream.reset();

        byte[] expectedData = new byte[256];
        System.arraycopy(data, 0, expectedData, 0, data.length);
        byte[] expectedRecord = new byte[512];
        System.arraycopy(TarConstants.STD_MAGIC, 0, expectedRecord, 0, TarConstants.STD_MAGIC.length);
        System.arraycopy(new byte[TarConstants.NAMELEN + 1], 0, expectedRecord, TarConstants.NAME_INDEX, "test.txt".getBytes().length + 1); // NUL terminator
        System.arraycopy(new byte[8], 0, expectedRecord, TarConstants.FILE_SIZE_INDEX, Long.BYTES);
        System.arraycopy(new byte[12], 0, expectedRecord, TarConstants.MOD_TIME_INDEX, Long.BYTES);
        System.arraycopy(new byte[TarConstants.REC_SIZE_INDEX - TarConstants.MOD_TIME_INDEX], 0, expectedRecord, TarConstants.MOD_TIME_INDEX + Long.BYTES, TarConstants.REC_SIZE_INDEX - TarConstants.MOD_TIME_INDEX); // padding
        System.arraycopy(expectedData, 0, expectedRecord, TarConstants.HEADER_SIZE, data.length);
        System.arraycopy(new byte[TarConstants.REC_SIZE_INDEX - TarConstants.HEADER_SIZE], 0, expectedRecord, TarConstants.HEADER_SIZE + data.length, TarConstants.REC_SIZE_INDEX - TarConstants.HEADER_SIZE); // padding

        Assert.assertArrayEquals(expectedRecord, inputStream.readNBytes(expectedRecord.length));

        Assert.assertEquals(inputStream.available(), 2 * 512);
    }

    @Test
    public void testWriteLongFileName() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(outputStream);
        tarArchiveOutputStream.setLongFileMode(TarArchiveOutputStream.LONGFILE_GNU);

        String fileName = "test folder/test1.txt";
        ArchiveEntry archiveEntry = tarArchiveOutputStream.createArchiveEntry(new File(fileName), fileName);
        tarArchiveOutputStream.putArchiveEntry(archiveEntry);

        byte[] buffer = new byte[512];
        tarArchiveOutputStream.write(buffer, 0, buffer.length);
        tarArchiveOutputStream.closeArchiveEntry();

        ByteArrayInputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
        byte[] expectedHeader = new byte[512];
        System.arraycopy(TarConstants.STD_MAGIC, 0, expectedHeader, 0, TarConstants.STD_MAGIC.length);
        byte[] entryNameLength = new byte[2];
        System.arraycopy(new Long(fileName.getBytes().length + 2).toByteArray(), 0, entryNameLength, 0, entryNameLength.length);
        System.arraycopy(new byte[4 + 2], 0, expectedHeader, TarConstants.NAME_INDEX, 6);
        System.arraycopy(new byte[1], 0, expectedHeader, TarConstants.NAME_INDEX + 6, 1); // NUL terminator
        byte[] longLinkNameLength = new byte[2];
        System.arraycopy(new Long(fileName.getBytes().length + 2).toByteArray(), 0, longLinkNameLength, 0, longLinkNameLength.length);
        System.arraycopy(new byte[4 + 2], 0, expectedHeader, TarConstants.LNAME_INDEX, 6);
        System.arraycopy(new byte[1], 0, expectedHeader, TarConstants.LNAME_INDEX + 6, 1); // NUL terminator
        byte[] fileSize = new byte[8];
        long size = 512;
        System.arraycopy(Long.toString(size).getBytes(), 0, fileSize, 0, fileSize.length);
        System.arraycopy(fileSize, 0, expectedHeader, TarConstants.FILE_SIZE_INDEX, fileSize.length);
        byte[] modifiedTime = new byte[12];
        long currentTime = System.currentTimeMillis();
        System.arraycopy(Long.toString(currentTime).getBytes(), 0, modifiedTime, 0, modifiedTime.length);
        System.arraycopy(modifiedTime, 0, expectedHeader, TarConstants.MOD_TIME_INDEX, modifiedTime.length);
        System.arraycopy(new byte[TarConstants.REC_SIZE_INDEX - TarConstants.MOD_TIME_INDEX], 0, expectedHeader, TarConstants.MOD_TIME_INDEX + modifiedTime.length, TarConstants.REC_SIZE_INDEX - TarConstants.MOD_TIME_INDEX); // padding

        System.arraycopy(fileName.getBytes(), 0, expectedHeader, TarConstants.NAME_INDEX + 7, fileName.length());
        System.arraycopy(new byte[1], 0, expectedHeader, TarConstants.NAME_INDEX + 7 + fileName.length(), 1); // NUL terminator
        System.arraycopy(fileName.getBytes(), 0, expectedHeader, TarConstants.LNAME_INDEX + 7, fileName.length());
        System.arraycopy(new byte[1], 0, expectedHeader, TarConstants.LNAME_INDEX + 7 + fileName.length(), 1); // NUL terminator

        Assert.assertArrayEquals(expectedHeader, inputStream.readNBytes(expectedHeader.length));

        tarArchiveOutputStream.finish();
        Assert.assertEquals(inputStream.available(), 2 * 512);
    }
}
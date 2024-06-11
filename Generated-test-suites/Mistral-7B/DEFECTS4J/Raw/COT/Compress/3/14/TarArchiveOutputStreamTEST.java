import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarConstants;
import org.apache.commons.compress.archivers.tar.TarArchiveOutputStream;
import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class TarArchiveOutputStreamTest {

    @Test
    public void testConstructor() {
        TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(new ByteArrayOutputStream());
        Assert.assertNotNull(tarArchiveOutputStream);
    }

    @Test
    public void testSetLongFileMode() {
        TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(new ByteArrayOutputStream());
        tarArchiveOutputStream.setLongFileMode(TarArchiveOutputStream.LONGFILE_TRUNCATE);
        Assert.assertEquals(TarArchiveOutputStream.LONGFILE_TRUNCATE, tarArchiveOutputStream.getLongFileMode());
    }

    @Test(expected = RuntimeException.class)
    public void testPutArchiveEntryWithLongFileName() throws IOException {
        String longFileName = "longFileName" + new String(new char[TarConstants.NAMELEN + 1]).replace('\0', 'a');
        TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(new ByteArrayOutputStream());
        ArchiveEntry archiveEntry = new TarArchiveEntry("testFile.txt");
        tarArchiveOutputStream.putArchiveEntry(archiveEntry);
        tarArchiveOutputStream.putArchiveEntry(new TarArchiveEntry(longFileName.getBytes(StandardCharsets.UTF_8)));
    }

    @Test
    public void testFinish() throws IOException {
        TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(new ByteArrayOutputStream());
        ByteArrayInputStream inputStream = new ByteArrayInputStream("test data".getBytes(StandardCharsets.UTF_8));
        ArchiveEntry archiveEntry = new TarArchiveEntry("testFile.txt");
        tarArchiveOutputStream.putArchiveEntry(archiveEntry);
        tarArchiveOutputStream.setInputStream(inputStream);
        tarArchiveOutputStream.finish();

        ByteArrayOutputStream outputStream = (ByteArrayOutputStream) tarArchiveOutputStream.getOutputStream();
        byte[] expectedTarFile = getExpectedTarFile();
        byte[] actualTarFile = outputStream.toByteArray();

        Assert.assertArrayEquals(expectedTarFile, actualTarFile);
    }

    @Test
    public void testClose() throws IOException {
        TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(new ByteArrayOutputStream());
        ByteArrayInputStream inputStream = new ByteArrayInputStream("test data".getBytes(StandardCharsets.UTF_8));
        ArchiveEntry archiveEntry = new TarArchiveEntry("testFile.txt");
        tarArchiveOutputStream.putArchiveEntry(archiveEntry);
        tarArchiveOutputStream.setInputStream(inputStream);
        tarArchiveOutputStream.finish();
        tarArchiveOutputStream.close();

        ByteArrayOutputStream outputStream = (ByteArrayOutputStream) tarArchiveOutputStream.getOutputStream();
        byte[] expectedTarFile = getExpectedTarFile();
        byte[] actualTarFile = outputStream.toByteArray();

        Assert.assertArrayEquals(expectedTarFile, actualTarFile);
    }

    @Test(expected = IOException.class)
    public void testWriteAfterClose() throws IOException {
        TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(new ByteArrayOutputStream());
        ByteArrayInputStream inputStream = new ByteArrayInputStream("test data".getBytes(StandardCharsets.UTF_8));
        ArchiveEntry archiveEntry = new TarArchiveEntry("testFile.txt");
        tarArchiveOutputStream.putArchiveEntry(archiveEntry);
        tarArchiveOutputStream.setInputStream(inputStream);
        tarArchiveOutputStream.finish();
        tarArchiveOutputStream.close();
        tarArchiveOutputStream.write(new byte[1]);
    }

    private byte[] getExpectedTarFile() {
        byte[] header = new byte[512];
        byte[] record = new byte[512];
        byte[] data = "test data".getBytes(StandardCharsets.UTF_8);

        // Write header
        System.arraycopy(TarConstants.MAGIC_NUMBER.getBytes(), 0, header, 0, TarConstants.MAGIC_NUMBER.length);
        System.arraycopy(Integer.toByteArray(512), 0, header, 12, 4);
        System.arraycopy(new byte[]{0, 1, 0, 0, 0, 0}, 16, header, 16, 6);
        System.arraycopy("testFile.txt\0".getBytes(StandardCharsets.UTF_8), 0, record, 128, "testFile.txt".length() + 1);
        System.arraycopy(Integer.toByteArray(1024), 0, record, 510, 4);
        System.arraycopy(Integer.toByteArray(0), 0, record, 512, 4);
        System.arraycopy(data, 0, record, 516, data.length);

        // Write record
        byte[] tarFile = new byte[512 * 3];
        System.arraycopy(header, 0, tarFile, 0, header.length);
        System.arraycopy(record, 0, tarFile, 512, record.length);
        System.arraycopy(record, 0, tarFile, 1024, record.length);

        return tarFile;
    }
}
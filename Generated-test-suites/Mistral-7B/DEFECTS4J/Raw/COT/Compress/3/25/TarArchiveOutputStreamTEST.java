import org.apache.commons.compress.archivers. tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarConstants;
import org.apache.commons.compress.archivers.tar.TarBuffer;
import org.apache.commons.compress.archivers.tar.TarArchiveOutputStream;
import org.apache.commons.compress.utils.IOUtils;
import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

public class TarArchiveOutputStreamTest {

    @Test
    public void testConstructor() throws IOException {
        OutputStream outputStream = new ByteArrayOutputStream();
        TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(outputStream);
        Assert.assertNotNull(tarArchiveOutputStream);
    }

    @Test
    public void testSetLongFileMode() throws IOException {
        OutputStream outputStream = new ByteArrayOutputStream();
        TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(outputStream);

        tarArchiveOutputStream.setLongFileMode(TarArchiveOutputStream.LONGFILE_TRUNCATE);
        Assert.assertEquals(TarArchiveOutputStream.LONGFILE_TRUNCATE, tarArchiveOutputStream.getLongFileMode());

        tarArchiveOutputStream.setLongFileMode(TarArchiveOutputStream.LONGFILE_GNU);
        Assert.assertEquals(TarArchiveOutputStream.LONGFILE_GNU, tarArchiveOutputStream.getLongFileMode());
    }

    @Test
    public void testFinish() throws IOException {
        OutputStream outputStream = new ByteArrayOutputStream();
        TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(outputStream);
        tarArchiveOutputStream.finish();

        byte[] expectedEOFRecord = new byte[TarConstants.RECORD_SIZE * 2];
        Arrays.fill(expectedEOFRecord, (byte) 0);

        byte[] output = outputStream.toByteArray();
        Assert.assertArrayEquals(expectedEOFRecord, Arrays.copyOfRange(output, 0, expectedEOFRecord.length));
    }

    @Test
    public void testClose() throws IOException {
        OutputStream outputStream = new ByteArrayOutputStream();
        TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(outputStream);
        tarArchiveOutputStream.close();

        byte[] output = outputStream.toByteArray();
        Assert.assertEquals(0, output.length);
    }

    @Test(expected = IOException.class)
    public void testPutArchiveEntryWithLongFileName() throws IOException {
        OutputStream outputStream = new ByteArrayOutputStream();
        TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(outputStream);
        String longFileName = "a/b/c/d/e/f/g/h/i/j/k/l/m/n/o/p/q/r/s/t/u/v/w/x/y/z/aa/bb/cc/dd/ee";
        ArchiveEntry archiveEntry = new TarArchiveEntry(new File("dummyFile"), longFileName);
        tarArchiveOutputStream.putArchiveEntry(archiveEntry);
    }

    @Test
    public void testPutArchiveEntry() throws IOException {
        OutputStream outputStream = new ByteArrayOutputStream();
        TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(outputStream);
        String fileName = "testFile.txt";
        File file = new File("dummyFile");
        ArchiveEntry archiveEntry = new TarArchiveEntry(file, fileName);

        tarArchiveOutputStream.putArchiveEntry(archiveEntry);
        tarArchiveOutputStream.write("Hello World!".getBytes(StandardCharsets.UTF_8));
        tarArchiveOutputStream.closeArchiveEntry();

        byte[] expectedHeader = new TarArchiveEntry(file, fileName).getHeaderBytes();
        byte[] expectedContent = "Hello World!".getBytes(StandardCharsets.UTF_8);

        byte[] output = outputStream.toByteArray();
        Assert.assertArrayEquals(expectedHeader, Arrays.copyOfRange(output, 0, expectedHeader.length));
        Assert.assertArrayEquals(expectedContent, Arrays.copyOfRange(output, expectedHeader.length, expectedHeader.length + expectedContent.length));
    }

    @Test(expected = IOException.class)
    public void testWriteBeyondSize() throws IOException {
        OutputStream outputStream = new ByteArrayOutputStream();
        TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(outputStream);
        String fileName = "testFile.txt";
        File file = new File("dummyFile");
        ArchiveEntry archiveEntry = new TarArchiveEntry(file, fileName);
        tarArchiveOutputStream.putArchiveEntry(archiveEntry);
        byte[] content = "Hello World!".getBytes(StandardCharsets.UTF_8);

        tarArchiveOutputStream.write(content, 0, content.length + 1);
    }

    @Test
    public void testWrite() throws IOException {
        OutputStream outputStream = new ByteArrayOutputStream();
        TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(outputStream);
        String fileName = "testFile.txt";
        File file = new File("dummyFile");
        ArchiveEntry archiveEntry = new TarArchiveEntry(file, fileName);
        tarArchiveOutputStream.putArchiveEntry(archiveEntry);

        byte[] data = "Hello World!".getBytes(StandardCharsets.UTF_8);
        tarArchiveOutputStream.write(data, 0, data.length);
        tarArchiveOutputStream.closeArchiveEntry();

        byte[] output = outputStream.toByteArray();
        Assert.assertArrayEquals(data, Arrays.copyOfRange(output, 0, data.length));
    }

    @Test
    public void testWriteWithAssembly() throws IOException {
        OutputStream outputStream = new ByteArrayOutputStream();
        TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(outputStream);
        String fileName = "testFile.txt";
        File file = new File("dummyFile");
        ArchiveEntry archiveEntry = new TarArchiveEntry(file, fileName);
        tarArchiveOutputStream.putArchiveEntry(archiveEntry);

        byte[] data = "Hello World!".getBytes(StandardCharsets.UTF_8);
        tarArchiveOutputStream.write(data, 0, 16); // only 16 bytes are written for testing assembly
        tarArchiveOutputStream.closeArchiveEntry();

        byte[] output = outputStream.toByteArray();
        byte[] expected = new byte[TarConstants.RECORD_SIZE];

        // first header record
        System.arraycopy(archiveEntry.getHeaderBytes(), 0, expected, 0, archiveEntry.getHeaderBytes().length);
        System.arraycopy(data, 0, expected, archiveEntry.getHeaderBytes().length, 16);
        Assert.assertArrayEquals(expected, Arrays.copyOfRange(output, 0, expected.length));

        // second header record
        expected = new byte[TarConstants.RECORD_SIZE];
        System.arraycopy(new byte[TarConstants.MAGIC_NUMBER.length], 0, expected, 0, TarConstants.MAGIC_NUMBER.length);
        System.arraycopy(new byte[TarConstants.VERSION_NUMBER.length], 0, expected, TarConstants.MAGIC_NUMBER.length, TarConstants.VERSION_NUMBER.length);
        System.arraycopy(new byte[TarConstants.NAME_LENGTH], 0, expected, TarConstants.MAGIC_NUMBER.length + TarConstants.VERSION_NUMBER.length, TarConstants.NAME_LENGTH);
        System.arraycopy(new byte[TarConstants.FILE_SIZE.length], 0, expected, TarConstants.MAGIC_NUMBER.length + TarConstants.VERSION_NUMBER.length + TarConstants.NAME_LENGTH, TarConstants.FILE_SIZE.length);
        System.arraycopy(new byte[TarConstants.MODE.length], 0, expected, TarConstants.MAGIC_NUMBER.length + TarConstants.VERSION_NUMBER.length + TarConstants.NAME_LENGTH + TarConstants.FILE_SIZE.length, TarConstants.MODE.length);
        System.arraycopy(new byte[TarConstants.UID.length], 0, expected, TarConstants.MAGIC_NUMBER.length + TarConstants.VERSION_NUMBER.length + TarConstants.NAME_LENGTH + TarConstants.FILE_SIZE.length + TarConstants.MODE.length, TarConstants.UID.length);
        System.arraycopy(new byte[TarConstants.GID.length], 0, expected, TarConstants.MAGIC_NUMBER.length + TarConstants.VERSION_NUMBER.length + TarConstants.NAME_LENGTH + TarConstants.FILE_SIZE.length + TarConstants.MODE.length + TarConstants.UID.length, TarConstants.GID.length);
        System.arraycopy(new byte[TarConstants.MOD_TIME.length], 0, expected, TarConstants.MAGIC_NUMBER.length + TarConstants.VERSION_NUMBER.length + TarConstants.NAME_LENGTH + TarConstants.FILE_SIZE.length + TarConstants.MODE.length + TarConstants.UID.length + TarConstants.GID.length, TarConstants.MOD_TIME.length);
        System.arraycopy(new byte[TarConstants.CHECKSUM.length], 0, expected, TarConstants.MAGIC_NUMBER.length + TarConstants.VERSION_NUMBER.length + TarConstants.NAME_LENGTH + TarConstants.FILE_SIZE.length + TarConstants.MODE.length + TarConstants.UID.length + TarConstants.GID.length + TarConstants.MOD_TIME.length, TarConstants.CHECKSUM.length);
        System.arraycopy(new byte[TarConstants.NAME_LENGTH], 0, expected, TarConstants.MAGIC_NUMBER.length + TarConstants.VERSION_NUMBER.length + TarConstants.NAME_LENGTH + TarConstants.FILE_SIZE.length + TarConstants.MODE.length + TarConstants.UID.length + TarConstants.GID.length + TarConstants.MOD_TIME.length + TarConstants.CHECKSUM.length, TarConstants.NAME_LENGTH);
        System.arraycopy(new byte[1], 0, expected, TarConstants.MAGIC_NUMBER.length + TarConstants.VERSION_NUMBER.length + TarConstants.NAME_LENGTH + TarConstants.FILE_SIZE.length + TarConstants.MODE.length + TarConstants.UID.length + TarConstants.GID.length + TarConstants.MOD_TIME.length + TarConstants.CHECKSUM.length + TarConstants.NAME_LENGTH, 1);

        // second record with assembly
        System.arraycopy(data, 16, expected, TarConstants.NAME_LENGTH + TarConstants.MAGIC_NUMBER.length + TarConstants.VERSION_NUMBER.length, data.length - 16);

        Assert.assertArrayEquals(expected, Arrays.copyOfRange(output, TarConstants.RECORD_SIZE, output.length));
    }
}
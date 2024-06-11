import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveOutputStream;
import org.apache.commons.compress.archivers.tar.TarConstants;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.Arrays;

public class TarArchiveOutputStreamTest {

    private ByteArrayOutputStream outputStream;
    private TarArchiveOutputStream tarArchiveOutputStream;

    @Before
    public void setUp() throws IOException {
        outputStream = new ByteArrayOutputStream();
        tarArchiveOutputStream = new TarArchiveOutputStream(outputStream);
    }

    @Test
    public void testPutEmptyArchive() throws IOException {
        tarArchiveOutputStream.finish();
        byte[] expectedData = new byte[TarConstants.TAR_RECORD_LEN * 2];
        Arrays.fill(expectedData, (byte) 0);
        Assert.assertArrayEquals(expectedData, outputStream.toByteArray());
    }

    @Test
    public void testPutSingleFileEntry() throws IOException {
        String fileName = "testFile.txt";
        File file = new File("testFile.txt");
        TarArchiveEntry entry = tarArchiveOutputStream.createArchiveEntry(file, fileName);
        tarArchiveOutputStream.putArchiveEntry(entry);
        tarArchiveOutputStream.write("Hello World!".getBytes(StandardCharsets.UTF_8));
        tarArchiveOutputStream.closeArchiveEntry();
        tarArchiveOutputStream.finish();

        byte[] expectedData = getExpectedTarRecordData(TarConstants.NORMAL_FILE_MAGIC, fileName.getBytes(StandardCharsets.UTF_8), 10, 14, "Hello World!".getBytes(StandardCharsets.UTF_8));
        Assert.assertArrayEquals(expectedData, outputStream.toByteArray());
    }

    @Test
    public void testPutLongFileName() throws IOException {
        String fileName = "testFile_with_long_name.txt";
        File file = new File("testFile_with_long_name.txt");
        TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(outputStream, TarConstants.TAR_BLKSIZE, TarConstants.TAR_RECORDSIZE);
        tarArchiveOutputStream.setLongFileMode(TarArchiveOutputStream.LONGFILE_GNU);

        TarArchiveEntry entry = tarArchiveOutputStream.createArchiveEntry(file, fileName);
        tarArchiveOutputStream.putArchiveEntry(entry);
        tarArchiveOutputStream.write(fileName.getBytes(StandardCharsets.UTF_8));
        tarArchiveOutputStream.write(0);
        tarArchiveOutputStream.closeArchiveEntry();
        tarArchiveOutputStream.finish();

        byte[] expectedData = getExpectedTarRecordDataWithLongName(TarConstants.GNU_LONGLINK_MAGIC, fileName.getBytes(StandardCharsets.UTF_8), 10, 14, "testFile_with_long_name.txt".getBytes(StandardCharsets.UTF_8), "testFile_with_long_name.txt".getBytes(StandardCharsets.UTF_8).length);
        Assert.assertArrayEquals(expectedData, outputStream.toByteArray());
    }

    @Test(expected = RuntimeException.class)
    public void testPutLongFileNameWithoutGnuExtension() throws IOException {
        String fileName = "testFile_with_long_name.txt";
        File file = new File("testFile_with_long_name.txt");
        TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(outputStream, TarConstants.TAR_BLKSIZE, TarConstants.TAR_RECORDSIZE);
        tarArchiveOutputStream.setLongFileMode(TarArchiveOutputStream.LONGFILE_TRUNCATE);

        TarArchiveEntry entry = tarArchiveOutputStream.createArchiveEntry(file, fileName);
        tarArchiveOutputStream.putArchiveEntry(entry);
        tarArchiveOutputStream.write(fileName.getBytes(StandardCharsets.UTF_8));
        tarArchiveOutputStream.write(0);
        tarArchiveOutputStream.closeArchiveEntry();
        tarArchiveOutputStream.finish();

        // This test should throw a RuntimeException since the file name is too long
    }

    private byte[] getExpectedTarRecordData(int magic, byte[] fileName, int fileSize, int blockSize, byte[] data) {
        byte[] headerBytes = new byte[TarConstants.TAR_HEADER_LEN];
        byte[] recordBytes = new byte[TarConstants.TAR_RECORD_LEN];
        int pos = 0;

        // Write header
        System.arraycopy(Ints.toByteArray(magic), 0, headerBytes, 0, 4);
        headerBytes[4] = (byte) fileName.length;
        headerBytes[5] = (byte) (fileName.length >> 8);
        headerBytes[6] = (byte) (fileName.length >> 16);
        headerBytes[7] = (byte) (fileName.length >> 24);
        System.arraycopy(Ints.toByteArray(fileSize), 0, headerBytes, 8, 4);
        headerBytes[12] = (byte) (blockSize >> 8);
        headerBytes[13] = (byte) (blockSize >> 16);
        headerBytes[14] = (byte) (blockSize >> 24);
        System.arraycopy(fileName, 0, headerBytes, 25, fileName.length);
        System.arraycopy(Ints.toByteArray(fileSize), 0, headerBytes, 51, 4);

        // Write record
        System.arraycopy(Ints.toByteArray(magic), 0, recordBytes, 0, 4);
        System.arraycopy(headerBytes, 25, recordBytes, 51, headerBytes.length - 25);
        System.arraycopy(data, 0, recordBytes, 51 + headerBytes.length - 25, data.length);

        byte[] expectedData = new byte[headerBytes.length + recordBytes.length];
        System.arraycopy(headerBytes, 0, expectedData, 0, headerBytes.length);
        System.arraycopy(recordBytes, 0, expectedData, headerBytes.length, recordBytes.length);

        return expectedData;
    }

    private byte[] getExpectedTarRecordDataWithLongName(int magic, byte[] fileName, int fileSize, int blockSize, byte[] fileNameInArchive, int fileNameInArchiveLength) {
        byte[] headerBytes = new byte[TarConstants.GNU_LONGLINK_HEADER_LEN];
        byte[] recordBytes = new byte[TarConstants.TAR_RECORD_LEN];
        int pos = 0;

        // Write header
        System.arraycopy(Ints.toByteArray(magic), 0, headerBytes, 0, 4);
        headerBytes[4] = (byte) ( fileNameInArchiveLength >> 8 );
        headerBytes[5] = (byte) ( fileNameInArchiveLength >> 16 );
        headerBytes[6] = (byte) ( fileNameInArchiveLength >> 24 );
        headerBytes[7] = (byte) ( fileNameInArchiveLength >> 32 );
        System.arraycopy(Ints.toByteArray(fileSize), 0, headerBytes, 8, 4);
        headerBytes[12] = (byte) (blockSize >> 8);
        headerBytes[13] = (byte) (blockSize >> 16);
        headerBytes[14] = (byte) (blockSize >> 24);
        System.arraycopy(fileNameInArchive, 0, headerBytes, 20, fileNameInArchive.length);

        // Write record
        System.arraycopy(Ints.toByteArray(magic), 0, recordBytes, 0, 4);
        System.arraycopy(headerBytes, 0, recordBytes, 51, headerBytes.length);
        System.arraycopy(fileName, 0, recordBytes, 20 + headerBytes.length, fileName.length);
        System.arraycopy(data, 0, recordBytes, 20 + headerBytes.length + fileName.length, data.length);

        byte[] expectedData = new byte[headerBytes.length + recordBytes.length];
        System.arraycopy(headerBytes, 0, expectedData, 0, headerBytes.length);
        System.arraycopy(recordBytes, 0, expectedData, headerBytes.length, recordBytes.length);

        return expectedData;
    }
}
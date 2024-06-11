import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveOutputStream;
import org.apache.commons.compress.utils.IOUtils;
import org.junit.Assert;
import org.junit.Test;
import org.junit.Before;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;

public class TarArchiveOutputStreamTest {

    private static final int RECORD_SIZE = 512;
    private static final int BLOCK_SIZE = TarBuffer.DEFAULT_BLKSIZE;
    private TarArchiveOutputStream tarOutputStream;
    private OutputStream outputStream;
    private ByteArrayOutputStream byteArrayOutputStream;

    @Before
    public void setUp() throws IOException {
        outputStream = new CountingOutputStream(new ByteArrayOutputStream());
        byteArrayOutputStream = new ByteArrayOutputStream();
        tarOutputStream = new TarArchiveOutputStream(outputStream, BLOCK_SIZE, RECORD_SIZE);
        tarOutputStream.setLongFileMode(TarArchiveOutputStream.LONGFILE_TRUNCATE);
    }

    @Test
    public void testPutArchiveEntry_simple() throws IOException {
        ArchiveEntry archiveEntry = new TarArchiveEntry("test.txt");
        byte[] testData = "Hello World!".getBytes();

        tarOutputStream.putArchiveEntry(archiveEntry);
        tarOutputStream.write(testData, 0, testData.length);
        tarOutputStream.closeArchiveEntry();

        byte[] result = byteArrayOutputStream.toByteArray();
        byte[] header = new TarArchiveEntry("test.txt").getHeaderBytes();
        byte[] data = testData;

        int headerLength = header.length;
        int recordLength = RECORD_SIZE;
        int expectedLength = headerLength + data.length;

        byte[] expected = new byte[expectedLength];
        System.arraycopy(header, 0, expected, 0, headerLength);
        System.arraycopy(data, 0, expected, headerLength, data.length);

        Assert.assertArrayEquals(expected, result);
    }

    @Test
    public void testPutArchiveEntry_longFileName() throws IOException {
        String longFileName = "a/long/filename.txt";
        ArchiveEntry archiveEntry = new TarArchiveEntry(longFileName);

        tarOutputStream.putArchiveEntry(archiveEntry);

        byte[] result = byteArrayOutputStream.toByteArray();

        // Check if the long file name is truncated
        Assert.assertFalse(result[0] == (byte) 0);

        // Check if the GNU extension is used
        if (tarOutputStream.getLongFileMode() == TarArchiveOutputStream.LONGFILE_GNU) {
            // The first byte of the record should be LF_GNUTYPE_LONGNAME
            Assert.assertEquals((byte) TarConstants.GNU_LONGLINK, result[0]);

            // The second byte should be LF_GNUTYPE_NAME
            Assert.assertEquals((byte) TarConstants.LF_GNUTYPE_NAME, result[1]);

            // The long file name should be stored in the archive record
            String expectedLongFileName = "a/long/filename.txt";
            int nameLen = expectedLongFileName.length();
            byte[] longFileNameBytes = ArchiveUtils.toAsciiBytes(expectedLongFileName);
            int longFileNameBytesLength = longFileNameBytes.length;

            Assert.assertEquals(nameLen, result[2 + 1]);
            Assert.assertArrayEquals(longFileNameBytes, Arrays.copyOfRange(result, 3, 3 + longFileNameBytesLength));

            // The remaining bytes should be the same as in the simple test case
            byte[] data = "Hello World!".getBytes();
            byte[] expected = new byte[RECORD_SIZE];
            System.arraycopy(new TarArchiveEntry(longFileName).getHeaderBytes(), 0, expected, 0, headerLength);
            System.arraycopy(data, 0, expected, headerLength, data.length);

            Assert.assertArrayEquals(expected, Arrays.copyOfRange(result, 3 + longFileNameBytesLength, result.length));
        } else {
            throw new RuntimeException("Long file names are truncated or not handled correctly.");
        }
    }

    @Test(expected = IOException.class)
    public void testPutArchiveEntry_longFileName_LONGFILE_ERROR() throws IOException {
        String longFileName = "a/long/filename.txt";
        ArchiveEntry archiveEntry = new TarArchiveEntry(longFileName);

        TarArchiveOutputStream tarOutputStream = new TarArchiveOutputStream(new ByteArrayOutputStream(), BLOCK_SIZE, RECORD_SIZE);
        tarOutputStream.setLongFileMode(TarArchiveOutputStream.LONGFILE_ERROR);

        tarOutputStream.putArchiveEntry(archiveEntry);

        byte[] result = byteArrayOutputStream.toByteArray();

        // Check if the long file name is truncated
        Assert.assertTrue(result[0] == (byte) 0);

        // Check if an exception is thrown
        tarOutputStream.write("Hello World!".getBytes(), 0, "Hello World!".getBytes().length);
    }

    @Test
    public void testPutArchiveEntry_directory() throws IOException {
        String dirName = "test_directory";
        File dir = new File(dirName);
        dir.mkdir();

        File testFile1 = new File(dir, "test1.txt");
        File testFile2 = new File(dir, "test2.txt");

        ArchiveEntry archiveEntry = new TarArchiveEntry(dirName, TarArchiveEntry.DIRECTORY);

        tarOutputStream.putArchiveEntry(archiveEntry);

        tarOutputStream.putArchiveEntry(new TarArchiveEntry(testFile1));
        tarOutputStream.write("Hello World!".getBytes(), 0, "Hello World!".getBytes().length);
        tarOutputStream.closeArchiveEntry();

        tarOutputStream.putArchiveEntry(new TarArchiveEntry(testFile2));
        tarOutputStream.write("Hello World!".getBytes(), 0, "Hello World!".getBytes().length);
        tarOutputStream.closeArchiveEntry();

        tarOutputStream.closeArchiveEntry();
        tarOutputStream.finish();

        byte[] result = byteArrayOutputStream.toByteArray();

        Assert.assertTrue(result[0] == (byte) TarConstants.MAGIC_TAR);

        int expectedDirEntryLength = TarArchiveEntry.DIRECTORY.getHeaderLength(RECORD_SIZE);
        int expectedFileEntryLength = 512;
        int expectedNumberOfEntries = 3;

        int expectedLength = expectedDirEntryLength + (expectedNumberOfEntries * expectedFileEntryLength);

        Assert.assertEquals(expectedLength, result.length);
    }

    @Test
    public void testClose() throws IOException {
        ArchiveEntry archiveEntry = new TarArchiveEntry("test.txt");
        byte[] testData = "Hello World!".getBytes();

        tarOutputStream.putArchiveEntry(archiveEntry);
        tarOutputStream.write(testData, 0, testData.length);
        tarOutputStream.closeArchiveEntry();
        tarOutputStream.close();

        byte[] result = byteArrayOutputStream.toByteArray();

        Assert.assertEquals(result.length, byteArrayOutputStream.size());
    }

    @Test(expected = IOException.class)
    public void testPutArchiveEntry_afterFinish() throws IOException {
        ArchiveEntry archiveEntry = new TarArchiveEntry("test.txt");

        tarOutputStream.putArchiveEntry(archiveEntry);

        tarOutputStream.finish();

        tarOutputStream.putArchiveEntry(archiveEntry);
    }

    @Test(expected = IOException.class)
    public void testWrite_afterFinish() throws IOException {
        ArchiveEntry archiveEntry = new TarArchiveEntry("test.txt");

        tarOutputStream.putArchiveEntry(archiveEntry);
        tarOutputStream.write(new byte[1024], 0, 1024);
        tarOutputStream.finish();

        tarOutputStream.write(new byte[1024], 0, 1024);
    }

    @Test
    public void testWrite_afterClose() throws IOException {
        ArchiveEntry archiveEntry = new TarArchiveEntry("test.txt");

        tarOutputStream.putArchiveEntry(archiveEntry);
        tarOutputStream.write(new byte[1024], 0, 1024);
        tarOutputStream.closeArchiveEntry();
        tarOutputStream.close();

        byte[] readData = IOUtils.toByteArray(new ByteArrayInputStream(byteArrayOutputStream.toByteArray()));

        Assert.assertEquals(readData.length, byteArrayOutputStream.size());
    }
}
import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveOutputStream;
import org.apache.commons.compress.utils.IOUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

/**
 * Test class for TarArchiveOutputStream.
 */
public class TarArchiveOutputStreamTest {

    private static final int BLOCK_SIZE = 1024;
    private static final int RECORD_SIZE = 512;

    private ByteArrayOutputStream outputStream;
    private TarArchiveOutputStream tarArchiveOutputStream;

    @Before
    public void setUp() throws IOException {
        outputStream = new ByteArrayOutputStream(BLOCK_SIZE * 2);
        tarArchiveOutputStream = new TarArchiveOutputStream(outputStream, BLOCK_SIZE, RECORD_SIZE);
    }

    @After
    public void tearDown() throws IOException {
        tarArchiveOutputStream.finish();
        tarArchiveOutputStream.close();
        outputStream.close();
    }

    @Test
    public void testConstructor() {
        TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(new ByteArrayOutputStream(1024), 1024, 512);
        assertEquals(1024, tarArchiveOutputStream.getBufferSize());
        assertEquals(512, tarArchiveOutputStream.getRecordSize());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorNegativeBlockSize() {
        new TarArchiveOutputStream(new ByteArrayOutputStream(1024), -1024, 512);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorNegativeRecordSize() {
        new TarArchiveOutputStream(new ByteArrayOutputStream(1024), 1024, -512);
    }

    @Test
    public void testSetLongFileMode() {
        TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(new ByteArrayOutputStream(1024), 1024, 512);
        tarArchiveOutputStream.setLongFileMode(TarArchiveOutputStream.LONGFILE_TRUNCATE);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetLongFileModeNegativeValue() {
        TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(new ByteArrayOutputStream(1024), 1024, 512);
        tarArchiveOutputStream.setLongFileMode(-1);
    }

    @Test
    public void testWriteEmptyArchive() throws IOException {
        TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(outputStream, BLOCK_SIZE, RECORD_SIZE);
        tarArchiveOutputStream.finish();
        tarArchiveOutputStream.close();

        byte[] expected = new byte[2 * RECORD_SIZE];
        IOUtils.fillBytes(expected, (byte) 0);

        byte[] result = outputStream.toByteArray();
        assertArrayEquals(expected, result);
    }

    @Test
    public void testWriteFileEntry() throws IOException {
        String fileName = "testFile.txt";
        String expectedName = "testFile.txt";
        String expectedContent = "Test content.";

        TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(outputStream, BLOCK_SIZE, RECORD_SIZE);
        TarArchiveEntry archiveEntry = new TarArchiveEntry(fileName, expectedContent.getBytes(StandardCharsets.UTF_8));
        tarArchiveOutputStream.putArchiveEntry(archiveEntry);
        tarArchiveOutputStream.write(expectedContent.getBytes(StandardCharsets.UTF_8), 0, expectedContent.length());
        tarArchiveOutputStream.closeArchiveEntry();
        tarArchiveOutputStream.finish();
        tarArchiveOutputStream.close();

        byte[] expectedHeader = new byte[512];
        TarConstants.writeHeader(expectedName.getBytes(StandardCharsets.UTF_8), expectedContent.length(), expectedName.length(), TarConstants.FILE_TYPE_NORMAL, expectedName.length() > TarConstants.NAMELEN ? TarConstants.GNU_LONGLINK : 0, 0, 0, (byte) 0, tarArchiveOutputStream.getRecordSize(), recordBuf, 0);

        byte[] result = outputStream.toByteArray();
        int headerIndex = 0;

        for (int i = 0; i < 2; ++i) {
            System.arraycopy(expectedHeader, 0, result, headerIndex, expectedHeader.length);
            headerIndex += expectedHeader.length;

            byte[] expectedData = expectedContent.getBytes(StandardCharsets.UTF_8);
            int dataIndex = headerIndex;

            for (int j = 0; j < expectedData.length; ++j) {
                result[dataIndex++] = expectedData[j];
            }
        }

        assertArrayEquals(result, outputStream.toByteArray());
    }

    @Test
    public void testWriteDirectoryEntry() throws IOException {
        String dirName = "testDir/";

        TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(outputStream, BLOCK_SIZE, RECORD_SIZE);
        TarArchiveEntry archiveEntry = new TarArchiveEntry(dirName);
        tarArchiveOutputStream.putArchiveEntry(archiveEntry);
        tarArchiveOutputStream.finish();
        tarArchiveOutputStream.close();

        byte[] expectedHeader = new byte[512];
        TarConstants.writeHeader(dirName.getBytes(StandardCharsets.UTF_8), 0, dirName.length(), TarConstants.FILE_TYPE_DIR, 0, 0, 0, (byte) 0, tarArchiveOutputStream.getRecordSize(), recordBuf, 0);

        byte[] result = outputStream.toByteArray();
        assertArrayEquals(expectedHeader, new byte[expectedHeader.length][0]); // expected header is the same for every directory entry
    }
}
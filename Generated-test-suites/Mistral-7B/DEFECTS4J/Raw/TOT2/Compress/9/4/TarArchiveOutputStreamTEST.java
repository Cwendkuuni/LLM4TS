import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveOutputStream;
import org.apache.commons.compress.archivers.tar.TarConstants;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

/**
 * Test class for TarArchiveOutputStream.
 */
public class TarArchiveOutputStreamTest {

    private File tempFile;
    private TarArchiveOutputStream outputStream;
    private final byte[] testData = "Hello World!".getBytes(StandardCharsets.UTF_8);

    @Before
    public void setUp() throws IOException {
        tempFile = File.createTempFile("junit", null);
        outputStream = new TarArchiveOutputStream(new FileOutputStream(tempFile));
    }

    @After
    public void tearDown() throws IOException {
        if (outputStream != null) {
            outputStream.close();
        }
        if (tempFile != null && tempFile.exists()) {
            tempFile.delete();
        }
    }

    /**
     * Test for the constructor.
     * @throws IOException for any I/O error
     */
    @Test
    public void testConstructor() throws IOException {
        TarArchiveOutputStream tarOutputStream = new TarArchiveOutputStream(new ByteArrayOutputStream());
        assertNull(tarOutputStream.getRecordSize());
        tarOutputStream.close();

        tarOutputStream = new TarArchiveOutputStream(new FileOutputStream("test.tar"),
                TarConstants.DEFAULT_BLKSIZE, TarConstants.DEFAULT_RCDSIZE);
        assertEquals(TarConstants.DEFAULT_RCDSIZE, tarOutputStream.getRecordSize());
        tarOutputStream.close();
    }

    /**
     * Test for putArchiveEntry method with a directory entry.
     * @throws IOException for any I/O error
     */
    @Test
    public void testPutArchiveEntryDirectory() throws IOException {
        outputStream.putNextEntry(new TarArchiveEntry(new File(".")));
        outputStream.closeArchiveEntry();
        outputStream.finish();
        outputStream.close();

        InputStream inputStream = new FileInputStream(tempFile);
        TarArchiveEntry entry = new TarArchiveEntry(new File("."));
        TarArchiveEntry readEntry = new TarArchiveEntry(inputStream);
        assertEquals(entry, readEntry);
        inputStream.close();
    }

    /**
     * Test for putArchiveEntry method with a file entry.
     * @throws IOException for any I/O error
     */
    @Test
    public void testPutArchiveEntryFile() throws IOException {
        outputStream.putNextEntry(new TarArchiveEntry("test.txt"));
        outputStream.write(testData, 0, testData.length);
        outputStream.closeArchiveEntry();
        outputStream.finish();
        outputStream.close();

        InputStream inputStream = new FileInputStream(tempFile);
        TarArchiveEntry entry = new TarArchiveEntry("test.txt");
        assertEquals(entry.getSize(), testData.length);
        byte[] readData = new byte[testData.length];
        inputStream.read(readData, 0, testData.length);
        assertArrayEquals(testData, readData);
        inputStream.close();

        File file = new File(tempFile.getAbsolutePath() + "/test.txt");
        assertTrue(file.exists());
        assertTrue(file.length() == testData.length);
        file.delete();
    }

    /**
     * Test for putArchiveEntry method with a long file name.
     * @throws IOException for any I/O error
     */
    @Test
    public void testPutArchiveEntryLongFileName() throws IOException {
        outputStream.setLongFileMode(TarArchiveOutputStream.LONGFILE_GNU);
        String longFileName = "longFileNameThatExceedsNameLenLimit_";
        for (int i = 0; i < TarConstants.NAMELEN + 1; i++) {
            longFileName += "x";
        }
        outputStream.putNextEntry(new TarArchiveEntry(longFileName));
        outputStream.write(new byte[1], 0, 1);
        outputStream.closeArchiveEntry();
        outputStream.finish();
        outputStream.close();

        InputStream inputStream = new FileInputStream(tempFile);
        TarArchiveEntry entry = new TarArchiveEntry(longFileName);
        TarArchiveEntry readEntry = new TarArchiveEntry(inputStream);
        assertEquals(entry, readEntry);
        inputStream.close();
    }

    /**
     * Test for putArchiveEntry method with a long file name when longFileMode is set to TRUNCATE.
     * @throws IOException for any I/O error
     */
    @Test
    public void testPutArchiveEntryLongFileNameTruncate() throws IOException {
        outputStream.setLongFileMode(TarArchiveOutputStream.LONGFILE_TRUNCATE);
        String longFileName = "longFileNameThatExceedsNameLenLimit_";
        for (int i = 0; i < TarConstants.NAMELEN + 1; i++) {
            longFileName += "x";
        }
        outputStream.putNextEntry(new TarArchiveEntry(longFileName));
        outputStream.write(new byte[1], 0, 1);
        outputStream.closeArchiveEntry();
        outputStream.finish();
        outputStream.close();

        InputStream inputStream = new FileInputStream(tempFile);
        TarArchiveEntry entry = new TarArchiveEntry(longFileName.substring(0, TarConstants.NAMELEN));
        TarArchiveEntry readEntry = new TarArchiveEntry(inputStream);
        assertEquals(entry, readEntry);
        inputStream.close();
    }

    /**
     * Test for putArchiveEntry method with a null archive entry.
     * @throws IOException for any I/O error
     */
    @Test
    public void testPutArchiveEntryNullArchiveEntry() {
        assertThrows(NullPointerException.class, () -> {
            outputStream.putArchiveEntry(null);
        });
    }

    /**
     * Test for closeArchiveEntry method when no archive entry has been opened.
     * @throws IOException for any I/O error
     */
    @Test
    public void testCloseArchiveEntryNoEntryOpen() throws IOException {
        outputStream.closeArchiveEntry();
    }

    /**
     * Test for close method when an archive entry is still open.
     * @throws IOException for any I/O error
     */
    @Test
    public void testCloseArchiveEntryEntryOpen() throws IOException {
        outputStream.putNextEntry(new TarArchiveEntry("test.txt"));
        assertFalse(outputStream.finished());
        outputStream.close();
        assertTrue(outputStream.finished());
    }

    /**
     * Test for close method when no archive entry is open.
     * @throws IOException for any I/O error
     */
    @Test
    public void testCloseNoEntryOpen() throws IOException {
        outputStream.close();
    }

    /**
     * Test for finish method when an archive entry is still open.
     * @throws IOException for any I/O error
     */
    @Test
    public void testFinishEntryOpen() throws IOException {
        outputStream.putNextEntry(new TarArchiveEntry("test.txt"));
        assertFalse(outputStream.finished());
        outputStream.finish();
        assertTrue(outputStream.finished());
    }

    /**
     * Test for finish method when no archive entry is open.
     * @throws IOException for any I/O error
     */
    @Test
    public void testFinishNoEntryOpen() throws IOException {
        outputStream.finish();
    }

    /**
     * Test for write method when no archive entry is open.
     * @throws IOException for any I/O error
     */
    @Test
    public void testWriteNoEntryOpen() throws IOException {
        byte[] testData = "Hello World!".getBytes(StandardCharsets.UTF_8);
        outputStream.write(testData, 0, testData.length);
        assertThrows(IOException.class, () -> {
            outputStream.write(testData, 0, testData.length);
        });
    }

    /**
     * Test for write method when an archive entry is open.
     * @throws IOException for any I/O error
     */
    @Test
    public void testWriteEntryOpen() throws IOException {
        outputStream.putNextEntry(new TarArchiveEntry("test.txt"));
        byte[] testData = "Hello World!".getBytes(StandardCharsets.UTF_8);
        outputStream.write(testData, 0, testData.length);
        outputStream.closeArchiveEntry();
        outputStream.finish();
        outputStream.close();

        InputStream inputStream = new FileInputStream(tempFile);
        byte[] readData = new byte[testData.length];
        inputStream.read(readData, 0, testData.length);
        assertArrayEquals(testData, readData);
        inputStream.close();

        File file = new File(tempFile.getAbsolutePath() + "/test.txt");
        assertTrue(file.exists());
        assertTrue(file.length() == testData.length);
        file.delete();
    }

    /**
     * Test for write method with an empty buffer.
     * @throws IOException for any I/O error
     */
    @Test
    public void testWriteEmptyBuffer() throws IOException {
        outputStream.putNextEntry(new TarArchiveEntry("test.txt"));
        final byte[] writeData = new byte[0];
        outputStream.write(writeData, 0, writeData.length);
        outputStream.closeArchiveEntry();
        outputStream.finish();
        outputStream.close();

        InputStream inputStream = new FileInputStream(tempFile);
        byte[] readData = new byte[0];
        inputStream.read(readData, 0, readData.length);
        assertArrayEquals(writeData, readData);
        inputStream.close();

        File file = new File(tempFile.getAbsolutePath() + "/test.txt");
        assertTrue(file.exists());
        assertTrue(file.length() == 0);
        file.delete();
    }

    /**
     * Test for write method with a buffer that is smaller than record size.
     * @throws IOException for any I/O error
     */
    @Test
    public void testWriteSmallBuffer() throws IOException {
        outputStream.putNextEntry(new TarArchiveEntry("test.txt"));
        final byte[] writeData = new byte[TarConstants.DEFAULT_RCDSIZE / 2];
        Arrays.fill(writeData, (byte) 0x01);
        outputStream.write(writeData, 0, writeData.length);
        outputStream.closeArchiveEntry();
        outputStream.finish();
        outputStream.close();

        InputStream inputStream = new FileInputStream(tempFile);
        byte[] readData = new byte[TarConstants.DEFAULT_RCDSIZE];
        inputStream.read(readData, 0, readData.length);
        byte[] expectedData = new byte[writeData.length];
        System.arraycopy(writeData, 0, expectedData, 0, writeData.length);
        assertArrayEquals(expectedData, Arrays.copyOfRange(readData, 0, writeData.length));
        inputStream.close();

        File file = new File(tempFile.getAbsolutePath() + "/test.txt");
        assertTrue(file.exists());
        assertTrue(file.length() == TarConstants.DEFAULT_RCDSIZE);
        file.delete();
    }

    /**
     * Test for write method with a buffer that is larger than record size.
     * @throws IOException for any I/O error
     */
    @Test
    public void testWriteLargeBuffer() throws IOException {
        outputStream.putNextEntry(new TarArchiveEntry("test.txt"));
        final byte[] writeData = new byte[TarConstants.DEFAULT_RCDSIZE * 2];
        Arrays.fill(writeData, (byte) 0x01);
        outputStream.write(writeData, 0, writeData.length);
        outputStream.closeArchiveEntry();
        outputStream.finish();
        outputStream.close();

        InputStream inputStream = new FileInputStream(tempFile);
        byte[] readData = new byte[TarConstants.DEFAULT_RCDSIZE * 2];
        inputStream.read(readData, 0, readData.length);
        assertArrayEquals(writeData, readData);
        inputStream.close();

        File file = new File(tempFile.getAbsolutePath() + "/test.txt");
        assertTrue(file.exists());
        assertTrue(file.length() == TarConstants.DEFAULT_RCDSIZE * 2);
        file.delete();
    }
}
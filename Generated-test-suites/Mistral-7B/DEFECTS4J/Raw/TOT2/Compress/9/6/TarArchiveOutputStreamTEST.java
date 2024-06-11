import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveOutputStream;
import org.apache.commons.compress.utils.IOUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

import static org.junit.Assert.*;

/**
 * Test class for TarArchiveOutputStream
 */
public class TarArchiveOutputStreamTest {

    private File tempFile;
    private TarArchiveOutputStream tarOutputStream;
    private ByteArrayOutputStream outputStream;

    @Before
    public void setUp() throws IOException {
        tempFile = File.createTempFile("test", null);
        outputStream = new ByteArrayOutputStream();
        tarOutputStream = new TarArchiveOutputStream(outputStream);
    }

    @After
    public void tearDown() throws IOException {
        IOUtils.closeQuietly(tarOutputStream);
        IOUtils.closeQuietly(outputStream);
        tempFile.delete();
    }

    /**
     * Test putArchiveEntry method with valid input
     * @throws IOException
     */
    @Test
    public void testPutArchiveEntry() throws IOException {
        TarArchiveEntry testEntry = new TarArchiveEntry("test.txt");
        tarOutputStream.putArchiveEntry(testEntry);
        byte[] data = "Hello, World!".getBytes(StandardCharsets.UTF_8);
        tarOutputStream.write(data, 0, data.length);
        tarOutputStream.closeArchiveEntry();

        ByteArrayInputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
        TarArchiveEntry archiveEntry = new TarArchiveEntry(inputStream);
        byte[] expectedContent = "Hello, World!".getBytes(StandardCharsets.UTF_8);
        byte[] actualContent = new byte[archiveEntry.getSize()];
        inputStream.read(actualContent);

        assertArrayEquals(expectedContent, actualContent);
        assertEquals("test.txt", archiveEntry.getName());
    }

    /**
     * Test putArchiveEntry method with long file name
     * @throws IOException
     */
    @Test
    public void testPutArchiveEntryWithLongFileName() throws IOException {
        String longFileName = "long_file_name_test.txt";
        TarArchiveEntry testEntry = new TarArchiveEntry(longFileName);
        tarOutputStream.putArchiveEntry(testEntry);

        assertTrue("Long file name should not be truncated", testEntry.getName().contains(longFileName));
        tarOutputStream.finish();

        ByteArrayInputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
        TarArchiveEntry archiveEntry = new TarArchiveEntry(inputStream);
        assertEquals(longFileName, archiveEntry.getName());
    }

    /**
     * Test setLongFileMode method with LONGFILE_TRUNCATE
     * @throws IOException
     */
    @Test
    public void testSetLongFileModeWithLongFileNameTruncate() throws IOException {
        String longFileName = "long_file_name_test.txt";
        TarArchiveOutputStream tarOutputStream = new TarArchiveOutputStream(outputStream);
        tarOutputStream.setLongFileMode(TarArchiveOutputStream.LONGFILE_TRUNCATE);

        TarArchiveEntry testEntry = new TarArchiveEntry(longFileName);
        tarOutputStream.putArchiveEntry(testEntry);

        assertTrue("Long file name should be truncated", testEntry.getName().length() <= TarConstants.NAMELEN);
        tarOutputStream.finish();

        ByteArrayInputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
        TarArchiveEntry archiveEntry = new TarArchiveEntry(inputStream);
        assertTrue("Archive entry should have truncated name", archiveEntry.getName().length() <= TarConstants.NAMELEN);
    }

    /**
     * Test setLongFileMode method with LONGFILE_GNU
     * @throws IOException
     */
    @Test
    public void testSetLongFileModeWithLongFileNameGnu() throws IOException {
        String longFileName = "long_file_name_test.txt";
        TarArchiveOutputStream tarOutputStream = new TarArchiveOutputStream(outputStream);
        tarOutputStream.setLongFileMode(TarArchiveOutputStream.LONGFILE_GNU);

        TarArchiveEntry testEntry = new TarArchiveEntry(longFileName);
        tarOutputStream.putArchiveEntry(testEntry);

        // Create a long link entry
        TarArchiveEntry longLinkEntry = new TarArchiveEntry(TarConstants.GNU_LONGLINK, TarConstants.LF_GNUTYPE_LONGNAME);
        longLinkEntry.setSize(longFileName.getBytes(StandardCharsets.UTF_8).length + 1); // +1 for NUL
        tarOutputStream.putArchiveEntry(longLinkEntry);
        write(longFileName.getBytes(StandardCharsets.UTF_8));
        write(0); // NUL terminator
        tarOutputStream.closeArchiveEntry();
        tarOutputStream.finish();

        ByteArrayInputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
        TarArchiveEntry archiveEntry = new TarArchiveEntry(inputStream);
        assertEquals(longFileName, archiveEntry.getName());
    }

    /**
     * Test finish method
     * @throws IOException
     */
    @Test
    public void testFinish() throws IOException {
        TarArchiveEntry testEntry = new TarArchiveEntry("test.txt");
        tarOutputStream.putArchiveEntry(testEntry);
        tarOutputStream.finish();

        TarArchiveEntry archiveEntry = new TarArchiveEntry(new ByteArrayInputStream(outputStream.toByteArray()));
        assertEquals("test.txt", archiveEntry.getName());
    }

    /**
     * Test close method
     * @throws IOException
     */
    @Test
    public void testClose() throws IOException {
        TarArchiveEntry testEntry = new TarArchiveEntry("test.txt");
        tarOutputStream.putArchiveEntry(testEntry);
        tarOutputStream.write(new byte[10]);
        tarOutputStream.close();

        TarArchiveEntry archiveEntry = new TarArchiveEntry(new ByteArrayInputStream(outputStream.toByteArray()));
        byte[] expectedContent = new byte[10];
        byte[] actualContent = new byte[archiveEntry.getSize()];
        archiveEntry.getDataStream().read(actualContent);

        assertArrayEquals(expectedContent, actualContent);
    }

    /**
     * Test write method with valid input
     * @throws IOException
     */
    @Test
    public void testWriteWithValidInput() throws IOException {
        TarArchiveEntry testEntry = new TarArchiveEntry("test.txt");
        tarOutputStream.putArchiveEntry(testEntry);
        tarOutputStream.write(new byte[10]);
        tarOutputStream.closeArchiveEntry();

        TarArchiveEntry archiveEntry = new TarArchiveEntry(new ByteArrayInputStream(outputStream.toByteArray()));
        byte[] expectedContent = new byte[10];
        byte[] actualContent = new byte[archiveEntry.getSize()];
        archiveEntry.getDataStream().read(actualContent);

        assertArrayEquals(expectedContent, actualContent);
    }

    /**
     * Test write method with invalid input - size greater than entry size
     * @throws IOException
     */
    @Test(expected = IOException.class)
    public void testWriteWithInvalidInputSizeGreaterThanEntrySize() throws IOException {
        TarArchiveEntry testEntry = new TarArchiveEntry("test.txt");
        tarOutputStream.putArchiveEntry(testEntry);
        tarOutputStream.write(new byte[testEntry.getSize() + 1]);
        tarOutputStream.closeArchiveEntry();
    }

    /**
     * Test write method with invalid input - null byte array
     * @throws IOException
     */
    @Test(expected = IOException.class)
    public void testWriteWithInvalidInputNullByteArray() throws IOException {
        TarArchiveEntry testEntry = new TarArchiveEntry("test.txt");
        tarOutputStream.putArchiveEntry(testEntry);
        tarOutputStream.write(null);
        tarOutputStream.closeArchiveEntry();
    }

    /**
     * Helper method to write bytes to the output stream
     * @param bytes
     * @throws IOException
     */
    private void write(byte[] bytes) throws IOException {
        tarOutputStream.write(bytes, 0, bytes.length);
    }
}
import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveOutputStream;
import org.apache.commons.compress.utils.IOUtils;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

public class TarArchiveOutputStreamTest {

    private File tempFile;
    private TarArchiveOutputStream tarOutputStream;
    private ByteArrayOutputStream byteArrayOutputStream;

    @Before
    public void setUp() throws Exception {
        tempFile = File.createTempFile("junit-test-", null);
        byteArrayOutputStream = new ByteArrayOutputStream();
        tarOutputStream = new TarArchiveOutputStream(byteArrayOutputStream);
    }

    @After
    public void tearDown() throws Exception {
        tarOutputStream.close();
        IOUtils.closeQuietly(tempFile.toPath().toFile().getAbsoluteFile().toURI().toURL().openStream());
        tempFile.delete();
    }

    /**
     * Test putArchiveEntry method with valid TarArchiveEntry.
     */
    @Test
    public void testPutArchiveEntryValidEntry() throws IOException {
        ArchiveEntry archiveEntry = new TarArchiveEntry("test.txt");
        tarOutputStream.putArchiveEntry(archiveEntry);
        tarOutputStream.write("Hello World!".getBytes());
        tarOutputStream.closeArchiveEntry();
        tarOutputStream.finish();

        TarArchiveEntry entry = new TarArchiveEntry(tempFile);
        FileInputStream inputStream = new FileInputStream(tempFile);
        byte[] expectedBytes = "Hello World!\0".getBytes();
        byte[] actualBytes = new byte[expectedBytes.length];
        int bytesRead = inputStream.read(actualBytes);
        Assert.assertEquals(expectedBytes.length, bytesRead);
        Assert.assertArrayEquals(expectedBytes, actualBytes);
    }

    /**
     * Test putArchiveEntry method with long file name.
     */
    @Test(expected = RuntimeException.class)
    public void testPutArchiveEntryLongFileName() throws IOException {
        String longFileName = "a" + new String(new char[TarConstants.NAMELEN + 1]) + "b";
        TarArchiveEntry archiveEntry = new TarArchiveEntry(longFileName);
        tarOutputStream.putArchiveEntry(archiveEntry);
    }

    /**
     * Test putArchiveEntry method with invalid TarArchiveEntry.
     */
    @Test(expected = ClassCastException.class)
    public void testPutArchiveEntryInvalidEntry() throws IOException {
        Object invalidEntry = new Object();
        tarOutputStream.putArchiveEntry(invalidEntry);
    }

    /**
     * Test closeArchiveEntry method with valid write.
     */
    @Test
    public void testCloseArchiveEntryValidWrite() throws IOException {
        ArchiveEntry archiveEntry = new TarArchiveEntry("test.txt");
        tarOutputStream.putArchiveEntry(archiveEntry);
        tarOutputStream.write("Hello World!".getBytes());
        tarOutputStream.closeArchiveEntry();
        tarOutputStream.finish();

        TarArchiveEntry entry = new TarArchiveEntry(tempFile);
        FileInputStream inputStream = new FileInputStream(tempFile);
        byte[] expectedBytes = "Hello World!\0".getBytes();
        byte[] actualBytes = new byte[expectedBytes.length];
        int bytesRead = inputStream.read(actualBytes);
        Assert.assertEquals(expectedBytes.length, bytesRead);
        Assert.assertArrayEquals(expectedBytes, actualBytes);
    }

    /**
     * Test closeArchiveEntry method without writing to it.
     */
    @Test
    public void testCloseArchiveEntryEmpty() throws IOException {
        ArchiveEntry archiveEntry = new TarArchiveEntry("test.txt");
        tarOutputStream.putArchiveEntry(archiveEntry);
        tarOutputStream.closeArchiveEntry();
        tarOutputStream.finish();

        TarArchiveEntry entry = new TarArchiveEntry(tempFile);
        FileInputStream inputStream = new FileInputStream(tempFile);
        byte[] expectedBytes = new byte[0];
        byte[] actualBytes = new byte[expectedBytes.length];
        int bytesRead = inputStream.read(actualBytes);
        Assert.assertEquals(expectedBytes.length, bytesRead);
        Assert.assertArrayEquals(expectedBytes, actualBytes);
    }

    /**
     * Test write method with valid data.
     */
    @Test
    public void testWriteValidData() throws IOException {
        ArchiveEntry archiveEntry = new TarArchiveEntry("test.txt");
        tarOutputStream.putArchiveEntry(archiveEntry);
        byte[] data = "Hello World!".getBytes();
        tarOutputStream.write(data);
        tarOutputStream.closeArchiveEntry();
        tarOutputStream.finish();

        TarArchiveEntry entry = new TarArchiveEntry(tempFile);
        FileInputStream inputStream = new FileInputStream(tempFile);
        byte[] expectedBytes = "Hello World!\0".getBytes();
        byte[] actualBytes = new byte[expectedBytes.length];
        int bytesRead = inputStream.read(actualBytes);
        Assert.assertEquals(expectedBytes.length, bytesRead);
        Assert.assertArrayEquals(expectedBytes, actualBytes);
    }

    /**
     * Test write method with invalid data length.
     */
    @Test(expected = IOException.class)
    public void testWriteInvalidDataLength() throws IOException {
        ArchiveEntry archiveEntry = new TarArchiveEntry("test.txt");
        tarOutputStream.putArchiveEntry(archiveEntry);
        byte[] data = "Hello World!".getBytes();
        tarOutputStream.write(data, data.length + 1);
    }

    /**
     * Test write method with no data.
     */
    @Test
    public void testWriteNoData() throws IOException {
        ArchiveEntry archiveEntry = new TarArchiveEntry("test.txt");
        tarOutputStream.putArchiveEntry(archiveEntry);
        tarOutputStream.write(new byte[0]);
        tarOutputStream.closeArchiveEntry();
        tarOutputStream.finish();

        TarArchiveEntry entry = new TarArchiveEntry(tempFile);
        FileInputStream inputStream = new FileInputStream(tempFile);
        byte[] expectedBytes = new byte[0];
        byte[] actualBytes = new byte[expectedBytes.length];
        int bytesRead = inputStream.read(actualBytes);
        Assert.assertEquals(expectedBytes.length, bytesRead);
        Assert.assertArrayEquals(expectedBytes, actualBytes);
    }

    /**
     * Test finish method with valid data.
     */
    @Test
    public void testFinishValidData() throws IOException {
        ArchiveEntry archiveEntry = new TarArchiveEntry("test.txt");
        tarOutputStream.putArchiveEntry(archiveEntry);
        tarOutputStream.write("Hello World!".getBytes());
        tarOutputStream.closeArchiveEntry();
        tarOutputStream.finish();

        TarArchiveEntry entry = new TarArchiveEntry(tempFile);
        FileInputStream inputStream = new FileInputStream(tempFile);
        byte[] expectedBytes = "Hello World!\0".getBytes();
        byte[] actualBytes = new byte[expectedBytes.length];
        int bytesRead = inputStream.read(actualBytes);
        Assert.assertEquals(expectedBytes.length, bytesRead);
        Assert.assertArrayEquals(expectedBytes, actualBytes);
    }

    /**
     * Test finish method without writing to it.
     */
    @Test(expected = IOException.class)
    public void testFinishEmpty() throws IOException {
        ArchiveEntry archiveEntry = new TarArchiveEntry("test.txt");
        tarOutputStream.putArchiveEntry(archiveEntry);
        tarOutputStream.finish();
    }

    /**
     * Test setLongFileMode method with valid mode.
     */
    @Test
    public void testSetLongFileModeValidMode() throws IOException {
        tarOutputStream = new TarArchiveOutputStream(byteArrayOutputStream,
                TarArchiveOutputStream.LONGFILE_GNU);
        String longFileName = "a" + new String(new char[TarConstants.NAMELEN + 1]) + "b";
        TarArchiveEntry archiveEntry = new TarArchiveEntry(longFileName);
        tarOutputStream.putArchiveEntry(archiveEntry);
        tarOutputStream.write("Hello World!".getBytes());
        tarOutputStream.closeArchiveEntry();
        tarOutputStream.finish();

        ByteArrayInputStream inputStream = new ByteArrayInputStream(byteArrayOutputStream.toByteArray());
        byte[] expectedBytes = new byte[0]; // Long file name is written in the EOF record as a byte array
        byte[] actualBytes = new byte[expectedBytes.length];
        int bytesRead = inputStream.read(actualBytes);
        Assert.assertEquals(expectedBytes.length, bytesRead);
        Assert.assertArrayEquals(expectedBytes, actualBytes);
    }

    /**
     * Test setLongFileMode method with invalid mode.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testSetLongFileModeInvalidMode() throws IOException {
        tarOutputStream = new TarArchiveOutputStream(byteArrayOutputStream, 4);
    }
}
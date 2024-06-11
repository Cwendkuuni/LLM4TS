import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.ArchiveException;
import org.apache.commons.compress.archivers.ArchiveInputStream;
import org.apache.commons.compress.archivers.ArchiveOutputStream;
import org.apache.commons.compress.archivers.ar.ArArchiveEntry;
import org.apache.commons.compress.archivers.ar.ArArchiveOutputStream;
import org.apache.commons.compress.archivers.jar.JarArchiveEntry;
import org.apache.commons.compress.archivers.jar.JarArchiveOutputStream;
import org.apache.commons.compress.utils.IOUtils;
import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Test class for ArchiveStreamFactory.
 */
public class ArchiveStreamFactoryTest {

    /**
     * Test method for {@link ArchiveStreamFactory#createArchiveOutputStream(String, OutputStream)}.
     * @throws Exception if an error occurs
     */
    @Test
    public void testCreateArchiveOutputStream() throws Exception {
        String archiveName = ArchiveStreamFactory.ZIP;
        OutputStream outputStream = new ByteArrayOutputStream();
        ArchiveOutputStream archiveOutputStream = new ArchiveStreamFactory().createArchiveOutputStream(archiveName, outputStream);

        // Test creating an entry
        ArchiveEntry entry = new JarArchiveEntry("testEntry.txt");
        archiveOutputStream.putArchiveEntry(entry);

        // Write some data
        byte[] data = "This is a test".getBytes();
        archiveOutputStream.writeToArchiveEntry(entry.getSize(), data);
        archiveOutputStream.closeArchiveEntry();
        archiveOutputStream.finish();

        // Read the data back
        InputStream inputStream = new ArchiveStreamFactory().createArchiveInputStream(archiveName, new ByteArrayInputStream(outputStream.toByteArray()));
        byte[] readData = new byte[data.length];
        IOUtils.readFully(inputStream, readData);
        Assert.assertArrayEquals(data, readData);
        inputStream.close();

        // Clean up
        archiveOutputStream.close();
        outputStream.close();
    }

    /**
     * Test method for {@link ArchiveStreamFactory#createArchiveInputStream(String, InputStream)}.
     * @throws Exception if an error occurs
     */
    @Test
    public void testCreateArchiveInputStream() throws Exception {
        String archiveName = ArchiveStreamFactory.ZIP;
        String testData = "This is a test";

        // Write some data to a file
        String inputFileName = "testInput.zip";
        OutputStream outputStream = new FileOutputStream(inputFileName);
        ArchiveOutputStream archiveOutputStream = new ArchiveStreamFactory().createArchiveOutputStream(archiveName, outputStream);
        ArchiveEntry entry = new JarArchiveEntry("testEntry.txt");
        archiveOutputStream.putArchiveEntry(entry);
        archiveOutputStream.writeToArchiveEntry(testData.getBytes().length, testData.getBytes());
        archiveOutputStream.closeArchiveEntry();
        archiveOutputStream.finish();
        outputStream.close();

        // Read the data back
        InputStream inputStream = new FileInputStream(inputFileName);
        ArchiveInputStream archiveInputStream = new ArchiveStreamFactory().createArchiveInputStream(archiveName, inputStream);
        ArchiveEntry readEntry = archiveInputStream.getNextEntry();
        Assert.assertEquals("testEntry.txt", readEntry.getName());
        byte[] readData = new byte[testData.length().intValue()];
        IOUtils.readFully(archiveInputStream, readData);
        Assert.assertEquals(testData, new String(readData));
        archiveInputStream.close();
        inputStream.close();

        // Clean up
        File inputFile = new File(inputFileName);
        inputFile.delete();
    }

    /**
     * Test method for {@link ArchiveStreamFactory#createArchiveInputStream(InputStream)}.
     * @throws Exception if an error occurs
     */
    @Test
    public void testCreateArchiveInputStreamFromStream() throws Exception {
        String archiveName = ArchiveStreamFactory.ZIP;
        String testData = "This is a test";

        // Write some data to a byte array
        byte[] inputData = testData.getBytes();

        // Create a test input stream
        InputStream inputStream = new ByteArrayInputStream(inputData);
        ArchiveInputStream archiveInputStream = new ArchiveStreamFactory().createArchiveInputStream(archiveName, inputStream);

        // Read the data back
        ArchiveEntry entry = archiveInputStream.getNextEntry();
        Assert.assertEquals("testEntry.txt", entry.getName());
        byte[] readData = new byte[testData.length().intValue()];
        IOUtils.readFully(archiveInputStream, readData);
        Assert.assertEquals(testData, new String(readData));
        archiveInputStream.close();
        inputStream.close();
    }

    /**
     * Test method for {@link ArchiveStreamFactory#createArchiveOutputStream(String, OutputStream)} with unsupported encoding.
     * @throws Exception if an error occurs
     */
    @Test(expected = IllegalArgumentException.class)
    public void testCreateArchiveOutputStreamWithUnsupportedEncoding() throws Exception {
        String archiveName = ArchiveStreamFactory.ZIP;
        String encoding = "UTF-16BE";
        OutputStream outputStream = new ByteArrayOutputStream();
        new ArchiveStreamFactory(encoding).createArchiveOutputStream(archiveName, outputStream);
    }

    /**
     * Test method for {@link ArchiveStreamFactory#createArchiveInputStream(String, InputStream)} with unsupported encoding.
     * @throws Exception if an error occurs
     */
    @Test(expected = IllegalArgumentException.class)
    public void testCreateArchiveInputStreamWithUnsupportedEncoding() throws Exception {
        String archiveName = ArchiveStreamFactory.ZIP;
        String encoding = "UTF-16BE";
        InputStream inputStream = new ByteArrayInputStream(new byte[0]);
        new ArchiveStreamFactory(encoding).createArchiveInputStream(archiveName, inputStream);
    }

    /**
     * Test method for {@link ArchiveStreamFactory#createArchiveInputStream(String, InputStream)} with unsupported archive format.
     * @throws Exception if an error occurs
     */
    @Test(expected = ArchiveException.class)
    public void testCreateArchiveInputStreamWithUnsupportedFormat() throws Exception {
        String archiveName = "invalidFormat";
        InputStream inputStream = new ByteArrayInputStream(new byte[0]);
        new ArchiveStreamFactory().createArchiveInputStream(archiveName, inputStream);
    }

    /**
     * Test method for {@link ArchiveStreamFactory#createArchiveInputStream(InputStream)} with unsupported archive format.
     * @throws Exception if an error occurs
     */
    @Test(expected = ArchiveException.class)
    public void testCreateArchiveInputStreamFromStreamWithUnsupportedFormat() throws Exception {
        String archiveName = "invalidFormat";
        InputStream inputStream = new ByteArrayInputStream(new byte[0]);
        new ArchiveStreamFactory().createArchiveInputStream(archiveName, inputStream);
    }

    /**
     * Test method for {@link ArchiveStreamFactory#createArchiveOutputStream(String, OutputStream)} with invalid archive name.
     * @throws Exception if an error occurs
     */
    @Test(expected = ArchiveException.class)
    public void testCreateArchiveOutputStreamWithInvalidName() throws Exception {
        String archiveName = "invalidName";
        OutputStream outputStream = new ByteArrayOutputStream();
        new ArchiveStreamFactory().createArchiveOutputStream(archiveName, outputStream);
    }

    /**
     * Test method for {@link ArchiveStreamFactory#createArchiveOutputStream(String, OutputStream)} with null output stream.
     * @throws Exception if an error occurs
     */
    @Test(expected = IllegalArgumentException.class)
    public void testCreateArchiveOutputStreamWithNullOutputStream() throws Exception {
        String archiveName = ArchiveStreamFactory.ZIP;
        OutputStream outputStream = null;
        new ArchiveStreamFactory().createArchiveOutputStream(archiveName, outputStream);
    }

    /**
     * Test method for {@link ArchiveStreamFactory#createArchiveInputStream(String, InputStream)} with null input stream.
     * @throws Exception if an error occurs
     */
    @Test(expected = IllegalArgumentException.class)
    public void testCreateArchiveInputStreamWithNullInputStream() throws Exception {
        String archiveName = ArchiveStreamFactory.ZIP;
        InputStream inputStream = null;
        new ArchiveStreamFactory().createArchiveInputStream(archiveName, inputStream);
    }
}
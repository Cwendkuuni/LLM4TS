import org.apache.commons.compress.archivers.*;
import org.apache.commons.compress.archivers.archiveentry.ZipArchiveEntry;
import org.apache.commons.compress.archivers.stream.ZipArchiveInputStream;
import org.apache.commons.compress.archivers.stream.ZipArchiveOutputStream;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Test cases for ArchiveStreamFactory class.
 */
public class ArchiveStreamFactoryTest {

    private InputStream inputStream;
    private OutputStream outputStream;
    private ArchiveInputStream archiveInputStream;
    private ArchiveOutputStream archiveOutputStream;

    @Before
    public void setUp() throws Exception {
        inputStream = new ByteArrayInputStream(new byte[1024]);
        outputStream = new ByteArrayOutputStream();
    }

    @After
    public void tearDown() throws Exception {
        if (archiveInputStream != null) {
            archiveInputStream.close();
        }
        if (archiveOutputStream != null) {
            archiveOutputStream.finish();
            archiveOutputStream.close();
        }
        inputStream.close();
        outputStream.close();
    }

    /**
     * Test createArchiveInputStream method with correct archiver name and valid input stream.
     */
    @Test
    public void testCreateArchiveInputStreamWithValidInputStreamAndArchiverName() throws Exception {
        final String archiverName = "zip";
        archiveInputStream = new ArchiveStreamFactory().createArchiveInputStream(archiverName, inputStream);
        Assert.assertNotNull(archiveInputStream);
    }

    /**
     * Test createArchiveInputStream method with unknown archiver name and valid input stream.
     */
    @Test(expected = ArchiveException.class)
    public void testCreateArchiveInputStreamWithUnknownArchiverNameAndValidInputStream() throws Exception {
        final String archiverName = "unknown";
        new ArchiveStreamFactory().createArchiveInputStream(archiverName, inputStream);
    }

    /**
     * Test createArchiveInputStream method with null archiver name and valid input stream.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testCreateArchiveInputStreamWithNullArchiverNameAndValidInputStream() {
        new ArchiveStreamFactory().createArchiveInputStream(null, inputStream);
    }

    /**
     * Test createArchiveInputStream method with null input stream.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testCreateArchiveInputStreamWithNullInputStream() {
        new ArchiveStreamFactory().createArchiveInputStream("zip", null);
    }

    /**
     * Test createArchiveOutputStream method with correct archiver name and valid output stream.
     */
    @Test
    public void testCreateArchiveOutputStreamWithValidOutputStreamAndArchiverName() throws Exception {
        final String archiverName = "zip";
        archiveOutputStream = new ArchiveStreamFactory().createArchiveOutputStream(archiverName, outputStream);
        Assert.assertNotNull(archiveOutputStream);
    }

    /**
     * Test createArchiveOutputStream method with unknown archiver name and valid output stream.
     */
    @Test(expected = ArchiveException.class)
    public void testCreateArchiveOutputStreamWithUnknownArchiverNameAndValidOutputStream() throws Exception {
        final String archiverName = "unknown";
        new ArchiveStreamFactory().createArchiveOutputStream(archiverName, outputStream);
    }

    /**
     * Test createArchiveOutputStream method with null archiver name and valid output stream.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testCreateArchiveOutputStreamWithNullArchiverNameAndValidOutputStream() {
        new ArchiveStreamFactory().createArchiveOutputStream(null, outputStream);
    }

    /**
     * Test createArchiveOutputStream method with null output stream.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testCreateArchiveOutputStreamWithNullOutputStream() {
        new ArchiveStreamFactory().createArchiveOutputStream("zip", null);
    }

    /**
     * Test createArchiveOutputStream method with correct archiver name and input stream, and write entry.
     */
    @Test
    public void testCreateArchiveOutputStreamWithValidOutputStreamAndArchiverNameAndWriteEntry() throws Exception {
        final String archiverName = "zip";
        archiveOutputStream = new ArchiveStreamFactory().createArchiveOutputStream(archiverName, outputStream);
        final String entryName = "test";
        final long entrySize = 1024;
        final byte[] entryData = new byte[entrySize];
        archiveOutputStream.putArchiveEntry(new ZipArchiveEntry(entryName));
        outputStream.write(entryData, 0, entrySize);
        archiveOutputStream.closeArchiveEntry();
        archiveOutputStream.finish();

        final InputStream testInputStream = new ByteArrayInputStream(outputStream.toByteArray());
        final ArchiveInputStream testArchiveInputStream
                = new ArchiveStreamFactory().createArchiveInputStream(archiverName, testInputStream);
        final ZipArchiveEntry testEntry = (ZipArchiveEntry) testArchiveInputStream.getNextEntry();
        Assert.assertNotNull(testEntry);
        Assert.assertEquals(entryName, testEntry.getName());
        Assert.assertEquals(entrySize, testEntry.getSize());
        testArchiveInputStream.close();
    }
}
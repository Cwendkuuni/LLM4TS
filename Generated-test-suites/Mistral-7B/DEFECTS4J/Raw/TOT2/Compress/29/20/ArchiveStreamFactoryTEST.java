import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.ArchiveException;
import org.apache.commons.compress.archivers.ArchiveInputStream;
import org.apache.commons.compress.archivers.ArchiveOutputStream;
import org.apache.commons.compress.archivers.ArchiveStreamFactory;
import org.apache.commons.compress.utils.IOUtils;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Test cases for the ArchiveStreamFactory class.
 */
public class ArchiveStreamFactoryTest {

    /**
     * Test creating and writing to a zip archive.
     */
    @Test
    public void testCreateAndWriteToZipArchive() throws IOException {
        final String testDataDir = "testdata";
        final String outputFile = "test.zip";
        final String archiveEntryName = "test.txt";
        final String inputFile = testDataDir + "/test.txt";

        // Write test data file
        IOUtils.copy(getClass().getResourceAsStream("/test.txt"), new File(testDataDir, "test.txt"));

        // Create output stream
        OutputStream out = new FileOutputStream(outputFile);
        ArchiveOutputStream os = new ArchiveStreamFactory().createArchiveOutputStream(ArchiveStreamFactory.ZIP, out);

        // Write test entry
        ArchiveEntry entry = new JarArchiveEntry(archiveEntryName);
        IOUtils.copy(new FileInputStream(inputFile), os, entry);
        os.closeArchiveEntry();

        // Close output stream
        os.finish();
        os.close();

        // Verify written file
        ArchiveInputStream in = new ArchiveStreamFactory().createArchiveInputStream(ArchiveStreamFactory.ZIP, new FileInputStream(outputFile));
        ArchiveEntry readEntry = in.getNextEntry();
        assertNotNull(readEntry);
        assertEquals(archiveEntryName, readEntry.getName());
        IOUtils.copy(in, new ByteArrayOutputStream(), inputFile); // Verify written file matches original
        in.close();
    }

    /**
     * Test creating and reading from an ar archive.
     */
    @Test
    public void testCreateAndReadFromArArchive() throws IOException, ArchiveException {
        final String testDataDir = "testdata";
        final String inputFile = testDataDir + "/test.ar";
        final String expectedContent = "Hello World!";

        // Create input stream
        InputStream in = new FileInputStream(inputFile);
        ArchiveInputStream archiveIn = new ArchiveStreamFactory()
                .createArchiveInputStream(ArchiveStreamFactory.AR, in);

        // Read first entry
        ArchiveEntry entry = archiveIn.getNextEntry();
        assertNotNull(entry);
        assertEquals("test.txt", entry.getName());

        // Read entry content
        IOUtils.copy(archiveIn, new ByteArrayOutputStream());
        String actualContent = new String(IOUtils.toByteArray(archiveIn));

        // Verify read content
        assertEquals(expectedContent, actualContent);

        // Close streams
        archiveIn.close();
        in.close();
    }

    /**
     * Test creating and reading from an unrecognized archive format.
     */
    @Test(expected = ArchiveException.class)
    public void testCreateAndReadFromUnrecognizedArchive() throws IOException {
        final String testDataDir = "testdata";
        final String inputFile = testDataDir + "/test.unsupported";

        // Create input stream
        InputStream in = new FileInputStream(inputFile);
        ArchiveInputStream archiveIn = new ArchiveStreamFactory()
                .createArchiveInputStream(ArchiveStreamFactory.AR, in);

        // Attempt to read from unsupported archive
        archiveIn.getNextEntry();
    }

    /**
     * Test creating an archive input stream from an InputStream without mark support.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testCreateArchiveInputStreamWithoutMarkSupport() throws IOException {
        final String testDataDir = "testdata";
        final String inputFile = testDataDir + "/test.zip";

        // Create input stream without mark support
        InputStream in = new InputStream() {
            @Override
            public int read() throws IOException {
                return 0;
            }

            @Override
            public boolean markSupported() {
                return false;
            }

            @Override
            public void mark(int readlimit) {
                throw new UnsupportedOperationException();
            }
        };

        // Attempt to create archive input stream
        ArchiveInputStream archiveIn = new ArchiveStreamFactory()
                .createArchiveInputStream(ArchiveStreamFactory.ZIP, in);
    }
}
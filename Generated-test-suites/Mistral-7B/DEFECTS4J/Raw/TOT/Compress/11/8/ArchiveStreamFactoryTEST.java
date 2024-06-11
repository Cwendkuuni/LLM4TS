import org.apache.commons.compress.archivers.*;
import org.apache.commons.compress.archivers.archiveentry.*;
import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.utils.IOUtils;
import org.junit.Test;
import static org.junit.Assert.*;

public class ArchiveStreamFactoryTest {

    /**
     * Test creating an ArchiveInputStream for a known archive format (ZIP).
     */
    @Test
    public void testCreateArchiveInputStreamForKnownFormat() throws Exception {
        final String fileName = "test_file.zip";
        final InputStream inputStream = new FileInputStream(fileName);
        final ArchiveInputStream archiveInputStream = new ArchiveStreamFactory().createArchiveInputStream(ArchiveStreamFactory.ZIP, inputStream);
        assertNotNull(archiveInputStream);
        assertTrue(archiveInputStream instanceof ZipArchiveInputStream);
        archiveInputStream.close();
        inputStream.close();
    }

    /**
     * Test creating an ArchiveInputStream for an unknown archive format.
     */
    @Test(expected = ArchiveException.class)
    public void testCreateArchiveInputStreamForUnknownFormat() throws Exception {
        final String fileName = "test_file.unknown";
        final InputStream inputStream = new FileInputStream(fileName);
        new ArchiveStreamFactory().createArchiveInputStream("unknown", inputStream);
    }

    /**
     * Test creating an ArchiveOutputStream for a known archive format (ZIP).
     */
    @Test
    public void testCreateArchiveOutputStreamForKnownFormat() throws Exception {
        final String outputFileName = "test_file.zip";
        final OutputStream outputStream = new FileOutputStream(outputFileName);
        final ArchiveOutputStream archiveOutputStream = new ArchiveStreamFactory().createArchiveOutputStream(ArchiveStreamFactory.ZIP, outputStream);
        assertNotNull(archiveOutputStream);
        assertTrue(archiveOutputStream instanceof ZipArchiveOutputStream);
        archiveOutputStream.close();
        outputStream.close();
    }

    /**
     * Test creating an ArchiveOutputStream for an unknown archive format.
     */
    @Test(expected = ArchiveException.class)
    public void testCreateArchiveOutputStreamForUnknownFormat() throws Exception {
        final String outputFileName = "test_file.unknown";
        final OutputStream outputStream = new FileOutputStream(outputFileName);
        new ArchiveStreamFactory().createArchiveOutputStream("unknown", outputStream);
    }

    /**
     * Test creating an ArchiveInputStream from a given InputStream, autodetecting the archive type.
     */
    @Test
    public void testCreateArchiveInputStreamFromInputStream() throws Exception {
        final String testDataFileName = "test_data.tar.gz"; // A valid TAR.GZ file.
        final InputStream inputStream = new FileInputStream(testDataFileName);
        final ArchiveInputStream archiveInputStream = new ArchiveStreamFactory().createArchiveInputStream(inputStream);
        assertNotNull(archiveInputStream);
        assertTrue(archiveInputStream instanceof TarArchiveInputStream);
        final TarArchiveEntry tarEntry = (TarArchiveEntry) archiveInputStream.getNextEntry();
        assertEquals("test_file1.txt", tarEntry.getName());
        archiveInputStream.close();
        inputStream.close();
    }

    /**
     * Test creating an ArchiveOutputStream from an OutputStream, with an existing file.
     */
    @Test
    public void testCreateArchiveOutputStreamFromOutputStreamWithExistingFile() throws Exception {
        final String inputFileName = "test_file.txt";
        final String outputFileName = "test_file.tar";
        final InputStream inputStream = new FileInputStream(inputFileName);
        final OutputStream outputStream = new FileOutputStream(outputFileName);
        final ArchiveOutputStream archiveOutputStream = new ArchiveStreamFactory().createArchiveOutputStream(ArchiveStreamFactory.TAR, outputStream);
        archiveOutputStream.putArchiveEntry(new TarArchiveEntry(inputFileName));
        archiveOutputStream.writeTo(inputStream, 1024);
        archiveOutputStream.closeArchiveEntry();
        archiveOutputStream.close();
        inputStream.close();
        outputStream.close();

        final InputStream testInputStream = new FileInputStream(outputFileName);
        final ArchiveInputStream archiveTestInputStream = new ArchiveStreamFactory().createArchiveInputStream(ArchiveStreamFactory.TAR, testInputStream);
        final TarArchiveEntry tarEntry = (TarArchiveEntry) archiveTestInputStream.getNextEntry();
        assertEquals("test_file.txt", tarEntry.getName());
        archiveTestInputStream.close();
        testInputStream.close();
        new File(outputFileName).delete();
    }

    /**
     * Test creating an ArchiveOutputStream from an OutputStream, with a non-existing file.
     */
    @Test
    public void testCreateArchiveOutputStreamFromOutputStreamWithNonExistingFile() throws Exception {
        final String inputFileName = "test_file.txt";
        final String outputFileName = "test_file.tar";
        final OutputStream outputStream = new FileOutputStream(outputFileName);
        final ArchiveOutputStream archiveOutputStream = new ArchiveStreamFactory().createArchiveOutputStream(ArchiveStreamFactory.TAR, outputStream);
        archiveOutputStream.putArchiveEntry(new TarArchiveEntry("/nonexistent/test_file.txt"));
        archiveOutputStream.writeTo(new ByteArrayInputStream(new byte[0]), 1024);
        archiveOutputStream.closeArchiveEntry();
        archiveOutputStream.close();
        outputStream.close();

        final InputStream testInputStream = new FileInputStream(outputFileName);
        final ArchiveInputStream archiveTestInputStream = new ArchiveStreamFactory().createArchiveInputStream(ArchiveStreamFactory.TAR, testInputStream);
        assertNull(archiveTestInputStream.getNextEntry());
        archiveTestInputStream.close();
        testInputStream.close();
        new File(outputFileName).delete();
    }
}
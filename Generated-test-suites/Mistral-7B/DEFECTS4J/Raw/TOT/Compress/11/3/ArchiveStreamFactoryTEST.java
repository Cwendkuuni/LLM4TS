import org.apache.commons.compress.archivers.*;
import org.apache.commons.compress.archivers.ar.ArArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipFile;
import org.junit.Test;
import org.junit.Before;
import org.junit.After;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Test class for ArchiveStreamFactory.
 */
public class ArchiveStreamFactoryTest {

    private ArchiveStreamFactory factory;

    @Before
    public void setUp() {
        factory = new ArchiveStreamFactory();
    }

    @After
    public void tearDown() throws Exception {
        if (factory != null) {
            factory = null;
        }
    }

    /**
     * Test creating an archive input stream from an archive name and an input stream.
     */
    @Test
    public void testCreateArchiveInputStreamFromArchiveNameAndInputStream() throws Exception {
        // Test AR archive
        String archiverName = ArchiveStreamFactory.AR;
        InputStream in = new FileInputStream("test_data/test_ar_archive.tar.gz");
        ArchiveInputStream expectedResult = new ArArchiveInputStream(in);
        ArchiveInputStream result = factory.createArchiveInputStream(archiverName, in);
        assertEquals(expectedResult.getClass(), result.getClass());

        // Test ZIP archive
        archiverName = ArchiveStreamFactory.ZIP;
        in = new FileInputStream("test_data/test_zip_archive.zip");
        expectedResult = new ZipArchiveInputStream(in);
        result = factory.createArchiveInputStream(archiverName, in);
        assertEquals(expectedResult.getClass(), result.getClass());

        // Test TAR archive
        archiverName = ArchiveStreamFactory.TAR;
        in = new FileInputStream("test_data/test_tar_archive.tar.gz");
        expectedResult = new TarArchiveInputStream(in);
        result = factory.createArchiveInputStream(archiverName, in);
        assertEquals(expectedResult.getClass(), result.getClass());

        // Test JAR archive
        archiverName = ArchiveStreamFactory.JAR;
        in = new FileInputStream("test_data/test_jar_archive.jar");
        expectedResult = new JarArchiveInputStream(in);
        result = factory.createArchiveInputStream(archiverName, in);
        assertEquals(expectedResult.getClass(), result.getClass());

        // Test unknown archive format
        archiverName = "unknown";
        in = new FileInputStream("test_data/test_tar_archive.tar.gz");
        try {
            result = factory.createArchiveInputStream(archiverName, in);
            fail("Expected ArchiveException");
        } catch (ArchiveException e) {
            // expected
        }
    }

    /**
     * Test creating an archive output stream from an archiver name and an output stream.
     */
    @Test
    public void testCreateArchiveOutputStreamFromArchiverNameAndOutputStream() throws Exception {
        // Test AR archive
        String archiverName = ArchiveStreamFactory.AR;
        OutputStream out = new FileOutputStream("test_data/test_output_ar_archive.tar.gz");
        ArchiveOutputStream expectedResult = new ArArchiveOutputStream(out);
        ArchiveOutputStream result = factory.createArchiveOutputStream(archiverName, out);
        assertEquals(expectedResult.getClass(), result.getClass());

        // Test ZIP archive
        archiverName = ArchiveStreamFactory.ZIP;
        out = new FileOutputStream("test_data/test_output_zip_archive.zip");
        expectedResult = new ZipArchiveOutputStream(out);
        result = factory.createArchiveOutputStream(archiverName, out);
        assertEquals(expectedResult.getClass(), result.getClass());

        // Test TAR archive
        archiverName = ArchiveStreamFactory.TAR;
        out = new FileOutputStream("test_data/test_output_tar_archive.tar.gz");
        expectedResult = new TarArchiveOutputStream(out);
        result = factory.createArchiveOutputStream(archiverName, out);
        assertEquals(expectedResult.getClass(), result.getClass());

        // Test JAR archive
        archiverName = ArchiveStreamFactory.JAR;
        out = new FileOutputStream("test_data/test_output_jar_archive.jar");
        expectedResult = new JarArchiveOutputStream(out);
        result = factory.createArchiveOutputStream(archiverName, out);
        assertEquals(expectedResult.getClass(), result.getClass());

        // Test unknown archive format
        archiverName = "unknown";
        out = new FileOutputStream("test_data/test_output_unknown_archive.tar.gz");
        try {
            result = factory.createArchiveOutputStream(archiverName, out);
            fail("Expected ArchiveException");
        } catch (ArchiveException e) {
            // expected
        }
    }

    /**
     * Test creating an archive input stream from an input stream and autodetecting the archive format.
     */
    @Test
    public void testCreateArchiveInputStreamFromInputStream() throws Exception {
        // Test ZIP archive
        InputStream in = new FileInputStream("test_data/test_zip_archive.zip");
        ArchiveInputStream expectedResult = new ZipArchiveInputStream(in);
        ArchiveInputStream result = factory.createArchiveInputStream(in);
        assertEquals(expectedResult.getClass(), result.getClass());

        // Test JAR archive
        in = new FileInputStream("test_data/test_jar_archive.jar");
        expectedResult = new JarArchiveInputStream(in);
        result = factory.createArchiveInputStream(in);
        assertEquals(expectedResult.getClass(), result.getClass());

        // Test AR archive
        in = new FileInputStream("test_data/test_ar_archive.tar.gz");
        expectedResult = new ArArchiveInputStream(in);
        result = factory.createArchiveInputStream(in);
        assertEquals(expectedResult.getClass(), result.getClass());

        // Test TAR archive
        in = new FileInputStream("test_data/test_tar_archive.tar.gz");
        expectedResult = new TarArchiveInputStream(in);
        result = factory.createArchiveInputStream(in);
        assertEquals(expectedResult.getClass(), result.getClass());

        // Test unknown archive format
        in = new FileInputStream("test_data/test_unknown_archive.bin");
        try {
            result = factory.createArchiveInputStream(in);
            fail("Expected ArchiveException");
        } catch (ArchiveException e) {
            // expected
        }

        // Test empty input stream
        in = new ByteArrayInputStream(new byte[0]);
        try {
            result = factory.createArchiveInputStream(in);
            fail("Expected ArchiveException");
        } catch (ArchiveException e) {
            // expected
        }

        // Test input stream without mark support
        in = new FileInputStream("test_data/test_input_stream_without_mark_support.bin");
        try {
            factory.createArchiveInputStream(in);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    /**
     * Test extracting entries from an archive input stream.
     */
    @Test
    public void testExtractEntries() throws Exception {
        InputStream in = new FileInputStream("test_data/test_zip_archive.zip");
        ArchiveInputStream archiveInputStream = factory.createArchiveInputStream(ArchiveStreamFactory.ZIP, in);

        List<ZipArchiveEntry> entries = new ArrayList<>();
        ZipArchiveEntry entry;
        boolean exitLoop = false;

        while ((entry = (ZipArchiveEntry) archiveInputStream.getNextEntry()) != null) {
            entries.add(entry);
            if (entry.getName().equals("test_entry_to_be_extracted.txt")) {
                exitLoop = true;
            }
        }

        assertTrue(exitLoop);
        assertEquals(2, entries.size());

        archiveInputStream.close();
        in.close();
    }

    /**
     * Test creating an archive output stream and adding entries.
     */
    @Test
    public void testCreateArchiveOutputStreamAndAddEntries() throws Exception {
        String outputPath = "test_data/test_output_archive.tar";
        OutputStream out = new FileOutputStream(outputPath);
        ArchiveOutputStream archiveOutputStream = factory.createArchiveOutputStream(ArchiveStreamFactory.TAR, out);

        // Add an entry from a file
        File fileToAdd = new File("test_data/test_file_to_add.txt");
        ArchiveEntry archiveEntry = new TarArchiveEntry("test_entry_from_file.txt", fileToAdd.length(), lastModified(fileToAdd));
        archiveOutputStream.putArchiveEntry(archiveEntry);
        IOUtils.copy(new FileInputStream(fileToAdd), archiveOutputStream);
        archiveOutputStream.closeArchiveEntry();
        fileToAdd.delete();

        // Add an entry from an input stream
        InputStream inputStreamToAdd = new FileInputStream("test_data/test_input_stream_to_add.txt");
        archiveOutputStream.putArchiveEntry(new TarArchiveEntry("test_entry_from_input_stream.txt"));
        IOUtils.copy(inputStreamToAdd, archiveOutputStream);
        archiveOutputStream.closeArchiveEntry();
        inputStreamToAdd.close();

        archiveOutputStream.close();
        out.close();

        File outputFile = new File(outputPath);
        ArchiveInputStream archiveInputStream = factory.createArchiveInputStream(ArchiveStreamFactory.TAR, new FileInputStream(outputFile));
        ArchiveEntry entry;
        boolean exitLoop = false;

        while ((entry = archiveInputStream.getNextEntry()) != null) {
            if (entry.getName().equals("test_entry_from_file.txt")) {
                exitLoop = true;
                assertEquals("test_file_to_add.txt", entry.getName());
                assertEquals(fileToAdd.length(), entry.getSize());
                assertEquals(lastModified(fileToAdd), entry.getTime());
            }
            if (entry.getName().equals("test_entry_from_input_stream.txt")) {
                exitLoop = true;
                assertNull(entry.getName); // InputStream does not have a name
                assertEquals(new ByteArrayInputStream(new byte[0]).available(), entry.getSize());
            }
        }

        assertTrue(exitLoop);
        archiveInputStream.close();
        outputFile.delete();
    }

    /**
     * Test creating an archive input stream and extracting an entry with a corrupt signature.
     */
    @Test
    public void testCreateArchiveInputStreamWithCorruptSignature() throws Exception {
        byte[] corruptSignature = new byte[12]; // A corrupt signature
        InputStream inputStreamWithCorruptSignature = new ByteArrayInputStream(corruptSignature);

        try {
            ArchiveInputStream archiveInputStream = factory.createArchiveInputStream(inputStreamWithCorruptSignature);
            fail("Expected ArchiveException");
        } catch (ArchiveException e) {
            // expected
        }

        inputStreamWithCorruptSignature.close();
    }

    /**
     * Test creating an archive output stream and writing data to an entry with a corrupt signature.
     */
    @Test
    public void testCreateArchiveOutputStreamWithCorruptSignature() throws Exception {
        byte[] corruptSignature = new byte[12]; // A corrupt signature
        String outputPath = "test_data/test_output_archive_with_corrupt_signature.tar";
        OutputStream out = new FileOutputStream(outputPath);
        ArchiveOutputStream archiveOutputStream = factory.createArchiveOutputStream(ArchiveStreamFactory.TAR, out);

        try {
            ArchiveEntry archiveEntry = new TarArchiveEntry("test_entry_with_corrupt_signature.txt");
            archiveOutputStream.putArchiveEntry(archiveEntry);
            OutputStream outputStream = archiveOutputStream.getOutputStream(archiveEntry);
            outputStream.write(corruptSignature);
            outputStream.close();
            archiveOutputStream.closeArchiveEntry();
            archiveOutputStream.close();
            out.close();

            File outputFile = new File(outputPath);
            ArchiveInputStream archiveInputStream = factory.createArchiveInputStream(ArchiveStreamFactory.TAR, new FileInputStream(outputFile));

            try {
                ArchiveEntry entry = archiveInputStream.getNextEntry();
                fail("Expected ArchiveException");
            } catch (ArchiveException e) {
                // expected
            }

            archiveInputStream.close();
            outputFile.delete();
        } catch (IOException e) {
            throw new ArchiveException("Error writing to input stream", e);
        }
    }
}
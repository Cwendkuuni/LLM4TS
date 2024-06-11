import org.junit.*;
import static org.junit.Assert.*;

import java.io.*;

public class ArchiveStreamFactoryTest {

    private ArchiveStreamFactory factory;

    @Before
    public void setUp() {
        factory = new ArchiveStreamFactory();
    }

    // Test createArchiveInputStream with existing archiver name - "zip"
    @Test
    public void testCreateArchiveInputStream_existingArchiverName_zip() {
        try {
            InputStream inputStream = new FileInputStream("test.zip");
            ArchiveInputStream archiveInputStream = factory.createArchiveInputStream("zip", inputStream);
            assertNotNull("ArchiveInputStream should not be null for existing archiver name - zip", archiveInputStream);
            // ... test other assertions here ...
        } catch (Exception e) {
            fail("Exception occurred during testCreateArchiveInputStream_existingArchiverName_zip");
        }
    }

    // Test createArchiveInputStream with unknown archiver name
    @Test(expected = ArchiveException.class)
    public void testCreateArchiveInputStream_unknownArchiverName() throws ArchiveException {
        InputStream inputStream = new FileInputStream("test.tar");
        ArchiveInputStream archiveInputStream = factory.createArchiveInputStream("tar", inputStream);
    }

    // Test createArchiveOutputStream with existing archiver name - "tar"
    @Test
    public void testCreateArchiveOutputStream_existingArchiverName_tar() {
        try {
            OutputStream outputStream = new FileOutputStream("test.tar");
            ArchiveOutputStream archiveOutputStream = factory.createArchiveOutputStream("tar", outputStream);
            assertNotNull("ArchiveOutputStream should not be null for existing archiver name - tar", archiveOutputStream);
            // ... test other assertions here ...
        } catch (Exception e) {
            fail("Exception occurred during testCreateArchiveOutputStream_existingArchiverName_tar");
        }
    }

    // Test createArchiveOutputStream with null archiver name
    @Test(expected = IllegalArgumentException.class)
    public void testCreateArchiveOutputStream_nullArchiverName() throws ArchiveException {
        OutputStream outputStream = new FileOutputStream("test.tar");
        ArchiveOutputStream archiveOutputStream = factory.createArchiveOutputStream(null, outputStream);
    }

    // Test createArchiveInputStream with null input stream
    @Test(expected = IllegalArgumentException.class)
    public void testCreateArchiveInputStream_nullInputStream() throws ArchiveException {
        ArchiveInputStream archiveInputStream = factory.createArchiveInputStream("zip", null);
    }

    // Test createArchiveOutputStream with null input stream
    @Test(expected = IllegalArgumentException.class)
    public void testCreateArchiveOutputStream_nullOutputStream() throws ArchiveException {
        ArchiveOutputStream archiveOutputStream = factory.createArchiveOutputStream("zip", null);
    }

    // Test createArchiveInputStream with autodetecting archive type
    @Test
    public void testCreateArchiveInputStream_autodetectArchiveType() {
        try {
            InputStream inputStream = new FileInputStream("test.tar.gz");
            ArchiveInputStream archiveInputStream = factory.createArchiveInputStream(inputStream);
            assertNotNull("ArchiveInputStream should not be null for autodetecting archive type", archiveInputStream);
            // ... test other assertions here ...
        } catch (Exception e) {
            fail("Exception occurred during testCreateArchiveInputStream_autodetectArchiveType");
        }
    }

    // Test createArchiveInputStream with unsupported input stream
    @Test(expected = IllegalArgumentException.class)
    public void testCreateArchiveInputStream_unsupportedInputStream() throws ArchiveException {
        InputStream inputStream = new FileInputStream("test.tar");
        ArchiveInputStream archiveInputStream = factory.createArchiveInputStream(inputStream);
    }

    // ... add more test cases for other methods ...

}
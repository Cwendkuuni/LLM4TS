import org.junit.Test;
import static org.junit.Assert.*;

public class ArchiveStreamFactoryTest {

    @Test
    public void testCreateArchiveInputStream() {
        // Test with null archiverName
        try {
            new ArchiveStreamFactory().createArchiveInputStream(null, new InputStream());
            fail("Should throw IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("Archivername must not be null.", e.getMessage());
        } catch (Exception e) {
            fail("Wrong exception thrown");
        }

        // Test with null InputStream
        try {
            new ArchiveStreamFactory().createArchiveInputStream("ar", null);
            fail("Should throw IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("InputStream must not be null.", e.getMessage());
        } catch (Exception e) {
            fail("Wrong exception thrown");
        }

        // Test with unknown archiverName
        try {
            new ArchiveStreamFactory().createArchiveInputStream("unknown", new InputStream());
            fail("Should throw ArchiveException");
        } catch (ArchiveException e) {
            assertEquals("Archiver: unknown not found.", e.getMessage());
        } catch (Exception e) {
            fail("Wrong exception thrown");
        }

        // Test with known archiverName "ar"
        try {
            ArchiveInputStream is = new ArchiveStreamFactory().createArchiveInputStream("ar", new InputStream());
            assertTrue(is instanceof ArArchiveInputStream);
        } catch (Exception e) {
            fail("Exception thrown");
        }
    }

    @Test
    public void testCreateArchiveOutputStream() {
        // Test with null archiverName
        try {
            new ArchiveStreamFactory().createArchiveOutputStream(null, new OutputStream());
            fail("Should throw IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("Archivername must not be null.", e.getMessage());
        } catch (Exception e) {
            fail("Wrong exception thrown");
        }

        // Test with null OutputStream
        try {
            new ArchiveStreamFactory().createArchiveOutputStream("ar", null);
            fail("Should throw IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("OutputStream must not be null.", e.getMessage());
        } catch (Exception e) {
            fail("Wrong exception thrown");
        }

        // Test with unknown archiverName
        try {
            new ArchiveStreamFactory().createArchiveOutputStream("unknown", new OutputStream());
            fail("Should throw ArchiveException");
        } catch (ArchiveException e) {
            assertEquals("Archiver: unknown not found.", e.getMessage());
        } catch (Exception e) {
            fail("Wrong exception thrown");
        }

        // Test with known archiverName "ar"
        try {
            ArchiveOutputStream os = new ArchiveStreamFactory().createArchiveOutputStream("ar", new OutputStream());
            assertTrue(os instanceof ArArchiveOutputStream);
        } catch (Exception e) {
            fail("Exception thrown");
        }
    }

    @Test
    public void testCreateArchiveInputStream_autoDetect() {
        // Test with null InputStream
        try {
            new ArchiveStreamFactory().createArchiveInputStream(null);
            fail("Should throw IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("Stream must not be null.", e.getMessage());
        } catch (Exception e) {
            fail("Wrong exception thrown");
        }

        // Test with unsupported mark
        try {
            new ArchiveStreamFactory().createArchiveInputStream(new InputStreamWithoutMark());
            fail("Should throw IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("Mark is not supported.", e.getMessage());
        } catch (Exception e) {
            fail("Wrong exception thrown");
        }

        // Test with unknown archiver
        try {
            new ArchiveStreamFactory().createArchiveInputStream(new InputStreamWithUnknownArchive());
            fail("Should throw ArchiveException");
        } catch (ArchiveException e) {
            assertEquals("No Archiver found for the stream signature", e.getMessage());
        } catch (Exception e) {
            fail("Wrong exception thrown");
        }

        // Test with known archiver "zip"
        try {
            ArchiveInputStream is = new ArchiveStreamFactory().createArchiveInputStream(new InputStreamWithZipArchive());
            assertTrue(is instanceof ZipArchiveInputStream);
        } catch (Exception e) {
            fail("Exception thrown");
        }

        // Test with known archiver "jar"
        try {
            ArchiveInputStream is = new ArchiveStreamFactory().createArchiveInputStream(new InputStreamWithJarArchive());
            assertTrue(is instanceof JarArchiveInputStream);
        } catch (Exception e) {
            fail("Exception thrown");
        }

        // Test with known archiver "ar"
        try {
            ArchiveInputStream is = new ArchiveStreamFactory().createArchiveInputStream(new InputStreamWithArArchive());
            assertTrue(is instanceof ArArchiveInputStream);
        } catch (Exception e) {
            fail("Exception thrown");
        }

        // Test with known archiver "cpio"
        try {
            ArchiveInputStream is = new ArchiveStreamFactory().createArchiveInputStream(new InputStreamWithCpioArchive());
            assertTrue(is instanceof CpioArchiveInputStream);
        } catch (Exception e) {
            fail("Exception thrown");
        }

        // Test with known archiver "dump"
        try {
            ArchiveInputStream is = new ArchiveStreamFactory().createArchiveInputStream(new InputStreamWithDumpArchive());
            assertTrue(is instanceof DumpArchiveInputStream);
        } catch (Exception e) {
            fail("Exception thrown");
        }

        // Test with known archiver "tar"
        try {
            ArchiveInputStream is = new ArchiveStreamFactory().createArchiveInputStream(new InputStreamWithTarArchive());
            assertTrue(is instanceof TarArchiveInputStream);
        } catch (Exception e) {
            fail("Exception thrown");
        }
    }

    public class InputStreamWithoutMark extends InputStream {
        // This InputStream does not support mark
    }

    public class InputStreamWithUnknownArchive extends InputStream {
        // This InputStream does not contain any known archive format
    }

    public class InputStreamWithZipArchive extends InputStream {
        // This InputStream contains a zip archive
    }

    public class InputStreamWithJarArchive extends InputStream {
        // This InputStream contains a jar archive
    }

    public class InputStreamWithArArchive extends InputStream {
        // This InputStream contains an ar archive
    }

    public class InputStreamWithCpioArchive extends InputStream {
        // This InputStream contains a cpio archive
    }

    public class InputStreamWithDumpArchive extends InputStream {
        // This InputStream contains a dump archive
    }

    public class InputStreamWithTarArchive extends InputStream {
        // This InputStream contains a tar archive
    }

}
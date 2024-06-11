import static org.junit.Assert.*;
import org.junit.Test;
import org.apache.commons.compress.archivers.*;

public class ArchiveStreamFactoryTest {

    @Test
    public void testCreateArchiveInputStream() {
        // Test for AR archive format
        try {
            ArchiveInputStream arInputStream = ArchiveStreamFactory.createArchiveInputStream("ar", new ByteArrayInputStream(new byte[]{}));
            assertNotNull(arInputStream);
            assertTrue(arInputStream instanceof ArArchiveInputStream);
        } catch (ArchiveException e) {
            fail(e.getMessage());
        }

        // Test for ZIP archive format
        try {
            ArchiveInputStream zipInputStream = ArchiveStreamFactory.createArchiveInputStream("zip", new ByteArrayInputStream(new byte[]{}));
            assertNotNull(zipInputStream);
            assertTrue(zipInputStream instanceof ZipArchiveInputStream);
        } catch (ArchiveException e) {
            fail(e.getMessage());
        }

        // Test for TAR archive format
        try {
            ArchiveInputStream tarInputStream = ArchiveStreamFactory.createArchiveInputStream("tar", new ByteArrayInputStream(new byte[]{}));
            assertNotNull(tarInputStream);
            assertTrue(tarInputStream instanceof TarArchiveInputStream);
        } catch (ArchiveException e) {
            fail(e.getMessage());
        }

        // Test for JAR archive format
        try {
            ArchiveInputStream jarInputStream = ArchiveStreamFactory.createArchiveInputStream("jar", new ByteArrayInputStream(new byte[]{}));
            assertNotNull(jarInputStream);
            assertTrue(jarInputStream instanceof JarArchiveInputStream);
        } catch (ArchiveException e) {
            fail(e.getMessage());
        }

        // Test for CPIO archive format
        try {
            ArchiveInputStream cpioInputStream = ArchiveStreamFactory.createArchiveInputStream("cpio", new ByteArrayInputStream(new byte[]{}));
            assertNotNull(cpioInputStream);
            assertTrue(cpioInputStream instanceof CpioArchiveInputStream);
        } catch (ArchiveException e) {
            fail(e.getMessage());
        }

        // Test for DUMP archive format
        try {
            ArchiveInputStream dumpInputStream = ArchiveStreamFactory.createArchiveInputStream("dump", new ByteArrayInputStream(new byte[]{}));
            assertNotNull(dumpInputStream);
            assertTrue(dumpInputStream instanceof DumpArchiveInputStream);
        } catch (ArchiveException e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void testCreateArchiveOutputStream() {
        // Test for AR archive format
        try {
            ArchiveOutputStream arOutputStream = ArchiveStreamFactory.createArchiveOutputStream("ar", new ByteArrayOutputStream());
            assertNotNull(arOutputStream);
            assertTrue(arOutputStream instanceof ArArchiveOutputStream);
        } catch (ArchiveException e) {
            fail(e.getMessage());
        }

        // Test for ZIP archive format
        try {
            ArchiveOutputStream zipOutputStream = ArchiveStreamFactory.createArchiveOutputStream("zip", new ByteArrayOutputStream());
            assertNotNull(zipOutputStream);
            assertTrue(zipOutputStream instanceof ZipArchiveOutputStream);
        } catch (ArchiveException e) {
            fail(e.getMessage());
        }

        // Test for TAR archive format
        try {
            ArchiveOutputStream tarOutputStream = ArchiveStreamFactory.createArchiveOutputStream("tar", new ByteArrayOutputStream());
            assertNotNull(tarOutputStream);
            assertTrue(tarOutputStream instanceof TarArchiveOutputStream);
        } catch (ArchiveException e) {
            fail(e.getMessage());
        }

        // Test for JAR archive format
        try {
            ArchiveOutputStream jarOutputStream = ArchiveStreamFactory.createArchiveOutputStream("jar", new ByteArrayOutputStream());
            assertNotNull(jarOutputStream);
            assertTrue(jarOutputStream instanceof JarArchiveOutputStream);
        } catch (ArchiveException e) {
            fail(e.getMessage());
        }

        // Test for CPIO archive format
        try {
            ArchiveOutputStream cpioOutputStream = ArchiveStreamFactory.createArchiveOutputStream("cpio", new ByteArrayOutputStream());
            assertNotNull(cpioOutputStream);
            assertTrue(cpioOutputStream instanceof CpioArchiveOutputStream);
        } catch (ArchiveException e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void testCreateArchiveInputStream_autodetect() {
        // Test for ZIP archive format
        try {
            ArchiveInputStream zipInputStream = ArchiveStreamFactory.createArchiveInputStream(new ByteArrayInputStream(new byte[]{80, 75, 3, 4}));
            assertNotNull(zipInputStream);
            assertTrue(zipInputStream instanceof ZipArchiveInputStream);
        } catch (ArchiveException e) {
            fail(e.getMessage());
        }

        // Test for JAR archive format
        try {
            ArchiveInputStream jarInputStream = ArchiveStreamFactory.createArchiveInputStream(new ByteArrayInputStream(new byte[]{80, 75, 3, 4}));
            assertNotNull(jarInputStream);
            assertTrue(jarInputStream instanceof JarArchiveInputStream);
        } catch (ArchiveException e) {
            fail(e.getMessage());
        }

        // Test for AR archive format
        try {
            ArchiveInputStream arInputStream = ArchiveStreamFactory.createArchiveInputStream(new ByteArrayInputStream(new byte[]{33, 60}));
            assertNotNull(arInputStream);
            assertTrue(arInputStream instanceof ArArchiveInputStream);
        } catch (ArchiveException e) {
            fail(e.getMessage());
        }

        // Test for CPIO archive format
        try {
            ArchiveInputStream cpioInputStream = ArchiveStreamFactory.createArchiveInputStream(new ByteArrayInputStream(new byte[]{'0', '7', '0', '7', '0', '7', '0', '7'}));
            assertNotNull(cpioInputStream);
            assertTrue(cpioInputStream instanceof CpioArchiveInputStream);
        } catch (ArchiveException e) {
            fail(e.getMessage());
        }

        // Test for DUMP archive format
        try {
            ArchiveInputStream dumpInputStream = ArchiveStreamFactory.createArchiveInputStream(new ByteArrayInputStream(new byte[]{126, 'd', 'u', 'm', 'p', '0', '0', '1'}));
            assertNotNull(dumpInputStream);
            assertTrue(dumpInputStream instanceof DumpArchiveInputStream);
        } catch (ArchiveException e) {
            fail(e.getMessage());
        }

        // Test for TAR archive format
        try {
            ArchiveInputStream tarInputStream = ArchiveStreamFactory.createArchiveInputStream(new ByteArrayInputStream(new byte[]{0, 120, 1})); 
            assertNotNull(tarInputStream);
            assertTrue(tarInputStream instanceof TarArchiveInputStream);
        } catch (ArchiveException e) {
            fail(e.getMessage());
        }

        // Test for unsupported archive format
        try {
            ArchiveStreamFactory.createArchiveInputStream(new ByteArrayInputStream(new byte[]{}));
            fail("Expected ArchiveException to be thrown");
        } catch (ArchiveException e) {
            // Expected, do nothing
        }
    }
}
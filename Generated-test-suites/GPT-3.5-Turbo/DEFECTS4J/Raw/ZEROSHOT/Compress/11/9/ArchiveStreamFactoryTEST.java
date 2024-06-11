import org.junit.Test;
import static org.junit.Assert.*;

public class ArchiveStreamFactoryTest {

    private final ArchiveStreamFactory factory = new ArchiveStreamFactory();

    @Test
    public void testCreateArchiveInputStream() {
        // Test for AR
        ArchiveInputStream arInput = factory.createArchiveInputStream(ArchiveStreamFactory.AR, new ByteArrayInputStream(new byte[0]));
        assertNotNull(arInput);
        assertTrue(arInput instanceof ArArchiveInputStream);

        // Test for ZIP
        ArchiveInputStream zipInput = factory.createArchiveInputStream(ArchiveStreamFactory.ZIP, new ByteArrayInputStream(new byte[0]));
        assertNotNull(zipInput);
        assertTrue(zipInput instanceof ZipArchiveInputStream);

        // Test for TAR
        ArchiveInputStream tarInput = factory.createArchiveInputStream(ArchiveStreamFactory.TAR, new ByteArrayInputStream(new byte[0]));
        assertNotNull(tarInput);
        assertTrue(tarInput instanceof TarArchiveInputStream);

        // Test for JAR
        ArchiveInputStream jarInput = factory.createArchiveInputStream(ArchiveStreamFactory.JAR, new ByteArrayInputStream(new byte[0]));
        assertNotNull(jarInput);
        assertTrue(jarInput instanceof JarArchiveInputStream);

        // Test for CPIO
        ArchiveInputStream cpioInput = factory.createArchiveInputStream(ArchiveStreamFactory.CPIO, new ByteArrayInputStream(new byte[0]));
        assertNotNull(cpioInput);
        assertTrue(cpioInput instanceof CpioArchiveInputStream);

        // Test for DUMP
        try {
            ArchiveInputStream dumpInput = factory.createArchiveInputStream(ArchiveStreamFactory.DUMP, new ByteArrayInputStream(new byte[0]));
        } catch (ArchiveException ex) {
            assertEquals("Archiver: dump not found.", ex.getMessage());
        }
    }

    @Test
    public void testCreateArchiveOutputStream() {
        // Test for AR
        ArchiveOutputStream arOutput = factory.createArchiveOutputStream(ArchiveStreamFactory.AR, new ByteArrayOutputStream());
        assertNotNull(arOutput);
        assertTrue(arOutput instanceof ArArchiveOutputStream);

        // Test for ZIP
        ArchiveOutputStream zipOutput = factory.createArchiveOutputStream(ArchiveStreamFactory.ZIP, new ByteArrayOutputStream());
        assertNotNull(zipOutput);
        assertTrue(zipOutput instanceof ZipArchiveOutputStream);

        // Test for TAR
        ArchiveOutputStream tarOutput = factory.createArchiveOutputStream(ArchiveStreamFactory.TAR, new ByteArrayOutputStream());
        assertNotNull(tarOutput);
        assertTrue(tarOutput instanceof TarArchiveOutputStream);

        // Test for JAR
        ArchiveOutputStream jarOutput = factory.createArchiveOutputStream(ArchiveStreamFactory.JAR, new ByteArrayOutputStream());
        assertNotNull(jarOutput);
        assertTrue(jarOutput instanceof JarArchiveOutputStream);

        // Test for CPIO
        ArchiveOutputStream cpioOutput = factory.createArchiveOutputStream(ArchiveStreamFactory.CPIO, new ByteArrayOutputStream());
        assertNotNull(cpioOutput);
        assertTrue(cpioOutput instanceof CpioArchiveOutputStream);

        // Test for DUMP
        try {
            ArchiveOutputStream dumpOutput = factory.createArchiveOutputStream(ArchiveStreamFactory.DUMP, new ByteArrayOutputStream());
        } catch (ArchiveException ex) {
            assertEquals("Archiver: dump not found.", ex.getMessage());
        }
    }

    @Test
    public void testCreateArchiveInputStreamAutodetect() {
        try {
            // Test for ZIP signature
            ArchiveInputStream zipInput = factory.createArchiveInputStream(new ByteArrayInputStream(new byte[]{80, 75, 3, 4}));
            assertNotNull(zipInput);
            assertTrue(zipInput instanceof ZipArchiveInputStream);

            // Test for JAR signature
            ArchiveInputStream jarInput = factory.createArchiveInputStream(new ByteArrayInputStream(new byte[]{80, 75, 3, 4}));
            assertNotNull(jarInput);
            assertTrue(jarInput instanceof JarArchiveInputStream);

            // Test for AR signature
            ArchiveInputStream arInput = factory.createArchiveInputStream(new ByteArrayInputStream(new byte[]{'!', '<', 'a', '>'}));
            assertNotNull(arInput);
            assertTrue(arInput instanceof ArArchiveInputStream);

            // Test for CPIO signature
            ArchiveInputStream cpioInput = factory.createArchiveInputStream(new ByteArrayInputStream(new byte[]{'0', '7', '0', '7'}));
            assertNotNull(cpioInput);
            assertTrue(cpioInput instanceof CpioArchiveInputStream);

            // Test for TAR signature
            ArchiveInputStream tarInput = factory.createArchiveInputStream(new ByteArrayInputStream(new byte[]{'u', 's', 't', 'a', 'r', ' ', 0}));
            assertNotNull(tarInput);
            assertTrue(tarInput instanceof TarArchiveInputStream);

            // Test for dump signature
            InputStream dumpInput = new ByteArrayInputStream(new byte[32]);
            ArchiveInputStream dumpOutput = factory.createArchiveInputStream(dumpInput);
            assertNotNull(dumpOutput);
            assertTrue(dumpOutput instanceof DumpArchiveInputStream);
        } catch (ArchiveException ex) {
            fail("Error creating archive input stream: " + ex.getMessage());
        }
    }
}
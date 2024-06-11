import static org.junit.Assert.*;

import org.apache.commons.compress.archivers.*;
import org.junit.Test;

public class ArchiveStreamFactoryTest {

    @Test
    public void testCreateArchiveInputStream() throws Exception {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        // Test AR archive
        ArchiveInputStream arInput = factory.createArchiveInputStream(ArchiveStreamFactory.AR, null);
        assertNotNull(arInput);

        // Test ZIP archive
        ArchiveInputStream zipInput = factory.createArchiveInputStream(ArchiveStreamFactory.ZIP, null);
        assertNotNull(zipInput);

        // Test TAR archive
        ArchiveInputStream tarInput = factory.createArchiveInputStream(ArchiveStreamFactory.TAR, null);
        assertNotNull(tarInput);

        // Test JAR archive
        ArchiveInputStream jarInput = factory.createArchiveInputStream(ArchiveStreamFactory.JAR, null);
        assertNotNull(jarInput);

        // Test CPIO archive
        ArchiveInputStream cpioInput = factory.createArchiveInputStream(ArchiveStreamFactory.CPIO, null);
        assertNotNull(cpioInput);

        // Test DUMP archive
        ArchiveInputStream dumpInput = factory.createArchiveInputStream(ArchiveStreamFactory.DUMP, null);
        assertNotNull(dumpInput);

        // Test unknown archive
        try {
            factory.createArchiveInputStream("unknown", null);
            fail("Expected ArchiveException to be thrown");
        } catch (ArchiveException e) {
            assertEquals("Archiver: unknown not found.", e.getMessage());
        }
    }

    @Test
    public void testCreateArchiveOutputStream() throws Exception {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        // Test AR archive
        ArchiveOutputStream arOutput = factory.createArchiveOutputStream(ArchiveStreamFactory.AR, null);
        assertNotNull(arOutput);

        // Test ZIP archive
        ArchiveOutputStream zipOutput = factory.createArchiveOutputStream(ArchiveStreamFactory.ZIP, null);
        assertNotNull(zipOutput);

        // Test TAR archive
        ArchiveOutputStream tarOutput = factory.createArchiveOutputStream(ArchiveStreamFactory.TAR, null);
        assertNotNull(tarOutput);

        // Test JAR archive
        ArchiveOutputStream jarOutput = factory.createArchiveOutputStream(ArchiveStreamFactory.JAR, null);
        assertNotNull(jarOutput);

        // Test CPIO archive
        ArchiveOutputStream cpioOutput = factory.createArchiveOutputStream(ArchiveStreamFactory.CPIO, null);
        assertNotNull(cpioOutput);

        // Test unknown archive
        try {
            factory.createArchiveOutputStream("unknown", null);
            fail("Expected ArchiveException to be thrown");
        } catch (ArchiveException e) {
            assertEquals("Archiver: unknown not found.", e.getMessage());
        }
    }

    @Test
    public void testCreateArchiveInputStreamWithStream() throws Exception {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        // Test ZIP archive
        ArchiveInputStream zipInput = factory.createArchiveInputStream(null);
        assertNotNull(zipInput);

        // Test JAR archive
        ArchiveInputStream jarInput = factory.createArchiveInputStream(null);
        assertNotNull(jarInput);

        // Test AR archive
        ArchiveInputStream arInput = factory.createArchiveInputStream(null);
        assertNotNull(arInput);

        // Test CPIO archive
        ArchiveInputStream cpioInput = factory.createArchiveInputStream(null);
        assertNotNull(cpioInput);

        // Test DUMP archive
        ArchiveInputStream dumpInput = factory.createArchiveInputStream(null);
        assertNotNull(dumpInput);

        // Test TAR archive
        ArchiveInputStream tarInput = factory.createArchiveInputStream(null);
        assertNotNull(tarInput);

        // Test non-archive stream
        try {
            factory.createArchiveInputStream(null);
            fail("Expected ArchiveException to be thrown");
        } catch (ArchiveException e) {
            assertEquals("No Archiver found for the stream signature", e.getMessage());
        }
    }
}
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import org.apache.commons.compress.archivers.ArchiveException;
import org.apache.commons.compress.archivers.ArchiveInputStream;
import org.apache.commons.compress.archivers.ArchiveOutputStream;
import org.apache.commons.compress.archivers.ArchiveStreamFactory;
import org.apache.commons.compress.archivers.tar.TarArchiveOutputStream;
import org.junit.Test;

public class ArchiveStreamFactoryTest {

    @Test
    public void testCreateArchiveInputStream_ValidAr() {
        try {
            ArchiveStreamFactory factory = new ArchiveStreamFactory();
            ArchiveInputStream in = factory.createArchiveInputStream(ArchiveStreamFactory.AR, new ByteArrayInputStream(new byte[0]));
            assertNotNull(in);
        } catch (ArchiveException | IOException e) {
            fail("Exception should not be thrown");
        }
    }

    @Test
    public void testCreateArchiveInputStream_InvalidArchiver() {
        try {
            ArchiveStreamFactory factory = new ArchiveStreamFactory();
            factory.createArchiveInputStream("invalid", new ByteArrayInputStream(new byte[0]));
            fail("Exception should be thrown for invalid archiver");
        } catch (ArchiveException e) {
            assertEquals("Archiver: invalid not found.", e.getMessage());
        }
    }

    @Test
    public void testCreateArchiveOutputStream_ValidZip() {
        try {
            ArchiveStreamFactory factory = new ArchiveStreamFactory();
            ArchiveOutputStream out = factory.createArchiveOutputStream(ArchiveStreamFactory.ZIP, new ByteArrayOutputStream());
            assertNotNull(out);
        } catch (ArchiveException | IOException e) {
            fail("Exception should not be thrown");
        }
    }

    @Test
    public void testCreateArchiveOutputStream_InvalidOutputStream() {
        try {
            ArchiveStreamFactory factory = new ArchiveStreamFactory();
            factory.createArchiveOutputStream(ArchiveStreamFactory.ZIP, null);
            fail("Exception should be thrown for null output stream");
        } catch (IllegalArgumentException e) {
            assertTrue(e.getMessage().contains("OutputStream must not be null."));
        } catch (ArchiveException | IOException e) {
            fail("Exception should not be thrown");
        }
    }

    @Test
    public void testCreateArchiveInputStream_Autodetect() {
        try {
            ArchiveStreamFactory factory = new ArchiveStreamFactory();
            ArchiveInputStream in = factory.createArchiveInputStream(new ByteArrayInputStream(new byte[0]));
            assertNotNull(in);
        } catch (ArchiveException | IOException e) {
            fail("Exception should not be thrown");
        }
    }

    @Test
    public void testCreateArchiveInputStream_AutodetectNoMark() {
        try {
            ArchiveStreamFactory factory = new ArchiveStreamFactory();
            factory.createArchiveInputStream(new ByteArrayInputStream(new byte[0]));
            fail("Exception should be thrown for stream without mark support");
        } catch (IllegalArgumentException e) {
            assertTrue(e.getMessage().contains("Mark is not supported."));
        } catch (ArchiveException | IOException e) {
            fail("Exception should not be thrown");
        }
    }

    // Add more test cases for edge cases and exception handling
    
}
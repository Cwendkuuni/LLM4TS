import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import org.apache.commons.compress.archivers.ArchiveException;
import org.apache.commons.compress.archivers.ArchiveInputStream;
import org.apache.commons.compress.archivers.ArchiveOutputStream;
import org.apache.commons.compress.archivers.ArchiveStreamFactory;
import org.junit.Test;

public class ArchiveStreamFactoryTest {

    private ArchiveStreamFactory factory;

    @Test
    public void testCreateArchiveInputStream() {
        factory = new ArchiveStreamFactory();
        try {
            InputStream inputStream = new ByteArrayInputStream(new byte[0]);
            ArchiveInputStream archiveInputStream = factory.createArchiveInputStream(ArchiveStreamFactory.ZIP, inputStream);
            
            assertNotNull(archiveInputStream);
        } catch (ArchiveException e) {
            fail("Exception not expected");
        }
    }

    @Test
    public void testCreateArchiveOutputStream() {
        factory = new ArchiveStreamFactory();
        try {
            ArchiveOutputStream archiveOutputStream = factory.createArchiveOutputStream(ArchiveStreamFactory.ZIP, System.out);
            
            assertNotNull(archiveOutputStream);
        } catch (ArchiveException e) {
            fail("Exception not expected");
        }
    }

    @Test
    public void testCreateArchiveInputStreamWithNullArchiverName() {
        factory = new ArchiveStreamFactory();
        try {
            InputStream inputStream = new ByteArrayInputStream(new byte[0]);
            factory.createArchiveInputStream(null, inputStream);
            fail("Exception expected");
        } catch (IllegalArgumentException e) {
            assertEquals("Archivername must not be null.", e.getMessage());
        } catch (ArchiveException e) {
            fail("Unexpected exception type");
        }
    }

    @Test
    public void testCreateArchiveOutputStreamWithNullArchiverName() {
        factory = new ArchiveStreamFactory();
        try {
            factory.createArchiveOutputStream(null, System.out);
            fail("Exception expected");
        } catch (IllegalArgumentException e) {
            assertEquals("Archivername must not be null.", e.getMessage());
        } catch (ArchiveException e) {
            fail("Unexpected exception type");
        }
    }

    // Add more test cases for edge cases and error scenarios as needed

}
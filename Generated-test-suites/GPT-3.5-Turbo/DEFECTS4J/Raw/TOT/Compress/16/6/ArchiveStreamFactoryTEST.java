import static org.junit.Assert.*;
import org.junit.Test;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import org.apache.commons.compress.archivers.*;

public class ArchiveStreamFactoryTest {

    @Test
    public void testCreateArchiveInputStream() {
        try {
            String archiverName = "zip";
            InputStream inputStream = new ByteArrayInputStream(new byte[0]);
            ArchiveStreamFactory factory = new ArchiveStreamFactory();
            ArchiveInputStream archiveInputStream = factory.createArchiveInputStream(archiverName, inputStream);
            
            assertNotNull(archiveInputStream);
            assertEquals(ZipArchiveInputStream.class, archiveInputStream.getClass());
            
            // Test typical use case
            assertTrue(archiveInputStream instanceof ZipArchiveInputStream);
            
            // Test edge case
            archiverName = "ar";
            archiveInputStream = factory.createArchiveInputStream(archiverName, inputStream);
            assertTrue(archiveInputStream instanceof ArArchiveInputStream);
            
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }
    
    @Test
    public void testCreateArchiveOutputStream() {
        try {
            String archiverName = "zip";
            OutputStream outputStream = new ByteArrayOutputStream();
            ArchiveStreamFactory factory = new ArchiveStreamFactory();
            ArchiveOutputStream archiveOutputStream = factory.createArchiveOutputStream(archiverName, outputStream);
            
            assertNotNull(archiveOutputStream);
            assertEquals(ZipArchiveOutputStream.class, archiveOutputStream.getClass());
            
            // Test typical use case
            assertTrue(archiveOutputStream instanceof ZipArchiveOutputStream);
            
            // Test edge case
            archiverName = "ar";
            archiveOutputStream = factory.createArchiveOutputStream(archiverName, outputStream);
            assertTrue(archiveOutputStream instanceof ArArchiveOutputStream);
            
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }
    
    @Test
    public void testCreateArchiveInputStream_autodetect() {
        try {
            InputStream inputStream = new ByteArrayInputStream(new byte[0]);
            ArchiveStreamFactory factory = new ArchiveStreamFactory();
            ArchiveInputStream archiveInputStream = factory.createArchiveInputStream(inputStream);
            
            assertNotNull(archiveInputStream);
            
            // Test typical use case
            assertTrue(archiveInputStream instanceof ZipArchiveInputStream);
            
            // Test edge case
            inputStream = new ByteArrayInputStream(new byte[512]);
            archiveInputStream = factory.createArchiveInputStream(inputStream);
            assertTrue(archiveInputStream instanceof TarArchiveInputStream);
            
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }
}
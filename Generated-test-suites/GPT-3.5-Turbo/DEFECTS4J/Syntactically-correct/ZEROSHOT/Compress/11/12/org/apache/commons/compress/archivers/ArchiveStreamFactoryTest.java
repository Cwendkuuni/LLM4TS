import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.apache.commons.compress.archivers.ArchiveException;
import org.apache.commons.compress.archivers.ArchiveInputStream;
import org.apache.commons.compress.archivers.ArchiveOutputStream;
import org.apache.commons.compress.archivers.ar.ArArchiveInputStream;
import org.apache.commons.compress.archivers.ar.ArArchiveOutputStream;
import org.apache.commons.compress.archivers.cpio.CpioArchiveInputStream;
import org.apache.commons.compress.archivers.cpio.CpioArchiveOutputStream;
import org.apache.commons.compress.archivers.dump.DumpArchiveInputStream;
import org.apache.commons.compress.archivers.jar.JarArchiveInputStream;
import org.apache.commons.compress.archivers.jar.JarArchiveOutputStream;
import org.apache.commons.compress.archivers.tar.TarArchiveInputStream;
import org.apache.commons.compress.archivers.tar.TarArchiveOutputStream;
import org.apache.commons.compress.archivers.zip.ZipArchiveInputStream;
import org.apache.commons.compress.archivers.zip.ZipArchiveOutputStream;
import org.junit.Test;

public class ArchiveStreamFactoryTest {
    
    private ArchiveStreamFactory factory;
    private InputStream input;
    private OutputStream output;
    
    public ArchiveStreamFactoryTest() {
        factory = new ArchiveStreamFactory();
        input = new ByteArrayInputStream(new byte[0]);
        output = new ByteArrayOutputStream();
    }
    
    @Test
    public void testCreateArchiveInputStreamWithNullArchiverName() throws ArchiveException {
        try {
            factory.createArchiveInputStream(null, input);
        } catch (IllegalArgumentException e) {
            assertEquals("Archivername must not be null.", e.getMessage());
        }
    }
    
    @Test
    public void testCreateArchiveInputStreamWithNullInputStream() throws ArchiveException {
        try {
            factory.createArchiveInputStream("zip", null);
        } catch (IllegalArgumentException e) {
            assertEquals("InputStream must not be null.", e.getMessage());
        }
    }
    
    @Test
    public void testCreateArchiveInputStreamWithCorreArchiverNameAndInputStream() throws ArchiveException {
        try {
            ArchiveInputStream archiveInputStream = factory.createArchiveInputStream("zip", input);
            assertNotNull(archiveInputStream);
            assertTrue(archiveInputStream instanceof ZipArchiveInputStream);
        } catch (IllegalArgumentException e) {
            assertEquals("InputStream must not be null.", e.getMessage());
        }
    }
    
    @Test
    public void testCreateArchiveOutputStreamWithNullArchiverName() throws ArchiveException {
        try {
            factory.createArchiveOutputStream(null, output);
        } catch (IllegalArgumentException e) {
            assertEquals("Archivername must not be null.", e.getMessage());
        }
    }
    
    @Test
    public void testCreateArchiveOutputStreamWithNullOutputStream() throws ArchiveException {
        try {
            factory.createArchiveOutputStream("zip", null);
        } catch (IllegalArgumentException e) {
            assertEquals("OutputStream must not be null.", e.getMessage());
        }
    }
    
    @Test
    public void testCreateArchiveOutputStreamWithCorreArchiverNameAndOutputStream() throws ArchiveException {
        try {
            ArchiveOutputStream archiveOutputStream = factory.createArchiveOutputStream("zip", output);
            assertNotNull(archiveOutputStream);
            assertTrue(archiveOutputStream instanceof ZipArchiveOutputStream);
        } catch (IllegalArgumentException e) {
            assertEquals("OutputStream must not be null.", e.getMessage());
        }
    }
    
    @Test
    public void testCreateArchiveInputStreamWithNullInputStream() throws ArchiveException {
        try {
            factory.createArchiveInputStream(null);
        } catch (IllegalArgumentException e) {
            assertEquals("Stream must not be null.", e.getMessage());
        }
    }
    
    @Test
    public void testCreateArchiveInputStreamWithInputStreamThatDoesNotSupportMark() throws ArchiveException {
        try {
            InputStream invalidInputStream = new InputStream() {
                public int read() throws IOException {
                    return 0;
                }
            };
            
            factory.createArchiveInputStream(invalidInputStream);
        } catch (IllegalArgumentException e) {
            assertEquals("Mark is not supported.", e.getMessage());
        }
    }
    
    @Test
    public void testCreateArchiveInputStreamWithValidInputStream() throws ArchiveException {
        try {
            InputStream validInputStream = new ByteArrayInputStream(new byte[512]);
            
            ArchiveInputStream archiveInputStream = factory.createArchiveInputStream(validInputStream);
            assertNotNull(archiveInputStream);
            assertTrue(archiveInputStream instanceof TarArchiveInputStream);
        } catch (IllegalArgumentException e) {
            assertEquals("Mark is not supported.", e.getMessage());
        }
    }
}
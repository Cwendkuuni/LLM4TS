import static org.junit.Assert.*;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import org.junit.Test;
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

public class ArchiveStreamFactoryTest {

    @Test
    public void testCreateArchiveInputStream() throws ArchiveException {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        
        InputStream mockInputStream = new ByteArrayInputStream(new byte[0]);
        
        ArchiveInputStream result = factory.createArchiveInputStream(ArchiveStreamFactory.AR, mockInputStream);
        assertNotNull(result);
        assertTrue(result instanceof ArArchiveInputStream);
        
        result = factory.createArchiveInputStream(ArchiveStreamFactory.ZIP, mockInputStream);
        assertNotNull(result);
        assertTrue(result instanceof ZipArchiveInputStream);
        
        result = factory.createArchiveInputStream(ArchiveStreamFactory.TAR, mockInputStream);
        assertNotNull(result);
        assertTrue(result instanceof TarArchiveInputStream);
        
        result = factory.createArchiveInputStream(ArchiveStreamFactory.JAR, mockInputStream);
        assertNotNull(result);
        assertTrue(result instanceof JarArchiveInputStream);
        
        result = factory.createArchiveInputStream(ArchiveStreamFactory.CPIO, mockInputStream);
        assertNotNull(result);
        assertTrue(result instanceof CpioArchiveInputStream);
        
        try {
            result = factory.createArchiveInputStream("unknown", mockInputStream);
            fail("Expected ArchiveException");
        } catch (ArchiveException e) {
            // expected
        }
        
        try {
            result = factory.createArchiveInputStream(null, mockInputStream);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }
        
        try {
            result = factory.createArchiveInputStream(ArchiveStreamFactory.ZIP, null);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test
    public void testCreateArchiveOutputStream() throws ArchiveException {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        
        OutputStream mockOutputStream = new ByteArrayOutputStream();
        
        ArchiveOutputStream result = factory.createArchiveOutputStream(ArchiveStreamFactory.AR, mockOutputStream);
        assertNotNull(result);
        assertTrue(result instanceof ArArchiveOutputStream);
        
        result = factory.createArchiveOutputStream(ArchiveStreamFactory.ZIP, mockOutputStream);
        assertNotNull(result);
        assertTrue(result instanceof ZipArchiveOutputStream);
        
        result = factory.createArchiveOutputStream(ArchiveStreamFactory.TAR, mockOutputStream);
        assertNotNull(result);
        assertTrue(result instanceof TarArchiveOutputStream);
        
        result = factory.createArchiveOutputStream(ArchiveStreamFactory.JAR, mockOutputStream);
        assertNotNull(result);
        assertTrue(result instanceof JarArchiveOutputStream);
        
        result = factory.createArchiveOutputStream(ArchiveStreamFactory.CPIO, mockOutputStream);
        assertNotNull(result);
        assertTrue(result instanceof CpioArchiveOutputStream);
        
        try {
            result = factory.createArchiveOutputStream("unknown", mockOutputStream);
            fail("Expected ArchiveException");
        } catch (ArchiveException e) {
            // expected
        }
        
        try {
            result = factory.createArchiveOutputStream(null, mockOutputStream);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }
        
        try {
            result = factory.createArchiveOutputStream(ArchiveStreamFactory.ZIP, null);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test
    public void testCreateArchiveInputStream_autoDetect() throws ArchiveException {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        
        InputStream mockInputStream = new ByteArrayInputStream(new byte[0]);
        
        ArchiveInputStream result = factory.createArchiveInputStream(mockInputStream);
        assertNotNull(result);
        assertTrue(result instanceof ZipArchiveInputStream);
        
        byte[] mockSignature = new byte[12];
        mockSignature[0] = 0x60; // ASCII value for 'A'
        
        mockInputStream = new ByteArrayInputStream(mockSignature);
        result = factory.createArchiveInputStream(mockInputStream);
        assertNotNull(result);
        assertTrue(result instanceof ArArchiveInputStream);
        
        try {
            byte[] emptySignature = new byte[0];
            mockInputStream = new ByteArrayInputStream(emptySignature);
            result = factory.createArchiveInputStream(mockInputStream);
            fail("Expected ArchiveException");
        } catch (ArchiveException e) {
            // expected
        }
        
        try {
            mockInputStream = null;
            result = factory.createArchiveInputStream(mockInputStream);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }
        
        byte[] dumpSignature = new byte[32];
        for (int i = 0; i < 32; i++) {
            dumpSignature[i] = 0x50; // ASCII value for 'P'
        }
        mockInputStream = new ByteArrayInputStream(dumpSignature);
        result = factory.createArchiveInputStream(mockInputStream);
        assertNotNull(result);
        assertTrue(result instanceof DumpArchiveInputStream);
        
        byte[] tarSignature = new byte[512];
        for (int i = 0; i < 512; i++) {
            tarSignature[i] = 0x74; // ASCII value for 't'
        }
        mockInputStream = new ByteArrayInputStream(tarSignature);
        result = factory.createArchiveInputStream(mockInputStream);
        assertNotNull(result);
        assertTrue(result instanceof TarArchiveInputStream);
        
        try {
            byte[] smallTarSignature = new byte[512];
            mockInputStream = new ByteArrayInputStream(smallTarSignature);
            result = factory.createArchiveInputStream(mockInputStream);
            fail("Expected ArchiveException");
        } catch (ArchiveException e) {
            // expected
        }
    }
}
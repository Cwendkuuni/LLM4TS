import static org.junit.Assert.*;
import org.junit.Test;
import org.apache.commons.compress.archivers.*;

public class ArchiveStreamFactoryTest {
    
    @Test
    public void testCreateArchiveInputStream() throws ArchiveException {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        
        // Test AR archive format
        ArchiveInputStream arInputStream = factory.createArchiveInputStream("ar", System.in);
        assertTrue(arInputStream instanceof ArArchiveInputStream);
        
        // Test ZIP archive format
        ArchiveInputStream zipInputStream = factory.createArchiveInputStream("zip", System.in);
        assertTrue(zipInputStream instanceof ZipArchiveInputStream);
        
        // Test TAR archive format
        ArchiveInputStream tarInputStream = factory.createArchiveInputStream("tar", System.in);
        assertTrue(tarInputStream instanceof TarArchiveInputStream);
        
        // Test JAR archive format
        ArchiveInputStream jarInputStream = factory.createArchiveInputStream("jar", System.in);
        assertTrue(jarInputStream instanceof JarArchiveInputStream);
        
        // Test CPIO archive format
        ArchiveInputStream cpioInputStream = factory.createArchiveInputStream("cpio", System.in);
        assertTrue(cpioInputStream instanceof CpioArchiveInputStream);
        
        // Test THROW archive format
        try {
            ArchiveInputStream invalidInputStream = factory.createArchiveInputStream("throw", System.in);
            fail("Expected ArchiveException");
        } catch (ArchiveException e) {
            // expected exception
        }
        
        // Test null archiverName
        try {
            ArchiveInputStream nullNameInputStream = factory.createArchiveInputStream(null, System.in);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected exception
        }
        
        // Test null input stream
        try {
            ArchiveInputStream nullInputStream = factory.createArchiveInputStream("zip", null);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected exception
        }
    }
    
    @Test
    public void testCreateArchiveOutputStream() throws ArchiveException {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        
        // Test AR archive format
        ArchiveOutputStream arOutputStream = factory.createArchiveOutputStream("ar", System.out);
        assertTrue(arOutputStream instanceof ArArchiveOutputStream);
        
        // Test ZIP archive format
        ArchiveOutputStream zipOutputStream = factory.createArchiveOutputStream("zip", System.out);
        assertTrue(zipOutputStream instanceof ZipArchiveOutputStream);
        
        // Test TAR archive format
        ArchiveOutputStream tarOutputStream = factory.createArchiveOutputStream("tar", System.out);
        assertTrue(tarOutputStream instanceof TarArchiveOutputStream);
        
        // Test JAR archive format
        ArchiveOutputStream jarOutputStream = factory.createArchiveOutputStream("jar", System.out);
        assertTrue(jarOutputStream instanceof JarArchiveOutputStream);
        
        // Test CPIO archive format
        ArchiveOutputStream cpioOutputStream = factory.createArchiveOutputStream("cpio", System.out);
        assertTrue(cpioOutputStream instanceof CpioArchiveOutputStream);
        
        // Test THROW archive format
        try {
            ArchiveOutputStream invalidOutputStream = factory.createArchiveOutputStream("throw", System.out);
            fail("Expected ArchiveException");
        } catch (ArchiveException e) {
            // expected exception
        }
        
        // Test null archiverName
        try {
            ArchiveOutputStream nullNameOutputStream = factory.createArchiveOutputStream(null, System.out);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected exception
        }
        
        // Test null output stream
        try {
            ArchiveOutputStream nullOutputStream = factory.createArchiveOutputStream("zip", null);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected exception
        }
    }
    
    @Test
    public void testCreateArchiveInputStreamAutoDetect() throws ArchiveException {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        
        // Test ZIP archive format
        ArchiveInputStream zipInputStream = factory.createArchiveInputStream(System.in);
        assertTrue(zipInputStream instanceof ZipArchiveInputStream);
        
        // Test JAR archive format
        ArchiveInputStream jarInputStream = factory.createArchiveInputStream(System.in);
        assertTrue(jarInputStream instanceof JarArchiveInputStream);
        
        // Test AR archive format
        ArchiveInputStream arInputStream = factory.createArchiveInputStream(System.in);
        assertTrue(arInputStream instanceof ArArchiveInputStream);
        
        // Test CPIO archive format
        ArchiveInputStream cpioInputStream = factory.createArchiveInputStream(System.in);
        assertTrue(cpioInputStream instanceof CpioArchiveInputStream);
        
        // Test DUMP archive format
        ArchiveInputStream dumpInputStream = factory.createArchiveInputStream(System.in);
        assertTrue(dumpInputStream instanceof DumpArchiveInputStream);
        
        // Test TAR archive format
        ArchiveInputStream tarInputStream = factory.createArchiveInputStream(System.in);
        assertTrue(tarInputStream instanceof TarArchiveInputStream);
        
        // Test null input stream
        try {
            ArchiveInputStream nullInputStream = factory.createArchiveInputStream(null);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected exception
        }
    }
    
    @Test
    public void testInvalidSignature() throws ArchiveException {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        byte[] invalidSignature = new byte[] { 1, 2, 3, 4 };
        ByteArrayInputStream in = new ByteArrayInputStream(invalidSignature);
        
        try {
            ArchiveInputStream invalidInputStream = factory.createArchiveInputStream(in);
            fail("Expected ArchiveException");
        } catch (ArchiveException e) {
            // expected exception
        }
    }
    
}
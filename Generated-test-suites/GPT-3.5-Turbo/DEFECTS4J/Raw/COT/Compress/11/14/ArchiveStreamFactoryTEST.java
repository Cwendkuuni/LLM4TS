
import static org.junit.Assert.*;
import org.junit.Test;
import org.apache.commons.compress.archivers.ArchiveException;
import org.apache.commons.compress.archivers.ArchiveInputStream;
import org.apache.commons.compress.archivers.ArchiveOutputStream;
import org.apache.commons.compress.archivers.tar.TarArchiveInputStream;
import org.apache.commons.compress.archivers.tar.TarArchiveOutputStream;
import org.apache.commons.compress.archivers.zip.ZipArchiveInputStream;
import org.apache.commons.compress.archivers.zip.ZipArchiveOutputStream;

public class ArchiveStreamFactoryTest {

    @Test
    public void testCreateArchiveInputStream() throws ArchiveException {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        
        // Test AR archive format
        ArchiveInputStream arInputStream = factory.createArchiveInputStream(ArchiveStreamFactory.AR, null);
        assertNull(arInputStream);
        
        // Test ZIP archive format
        ArchiveInputStream zipInputStream = factory.createArchiveInputStream(ArchiveStreamFactory.ZIP, null);
        assertNull(zipInputStream);
        
        // Test TAR archive format
        ArchiveInputStream tarInputStream = factory.createArchiveInputStream(ArchiveStreamFactory.TAR, null);
        assertNull(tarInputStream);
        
        // Test JAR archive format
        ArchiveInputStream jarInputStream = factory.createArchiveInputStream(ArchiveStreamFactory.JAR, null);
        assertNull(jarInputStream);
        
        // Test CPIO archive format
        ArchiveInputStream cpioInputStream = factory.createArchiveInputStream(ArchiveStreamFactory.CPIO, null);
        assertNull(cpioInputStream);
    }
    
    @Test
    public void testCreateArchiveOutputStream() throws ArchiveException {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        
        // Test AR archive format
        ArchiveOutputStream arOutputStream = factory.createArchiveOutputStream(ArchiveStreamFactory.AR, null);
        assertNull(arOutputStream);
        
        // Test ZIP archive format
        ArchiveOutputStream zipOutputStream = factory.createArchiveOutputStream(ArchiveStreamFactory.ZIP, null);
        assertNull(zipOutputStream);
        
        // Test TAR archive format
        ArchiveOutputStream tarOutputStream = factory.createArchiveOutputStream(ArchiveStreamFactory.TAR, null);
        assertNull(tarOutputStream);
        
        // Test JAR archive format
        ArchiveOutputStream jarOutputStream = factory.createArchiveOutputStream(ArchiveStreamFactory.JAR, null);
        assertNull(jarOutputStream);
        
        // Test CPIO archive format
        ArchiveOutputStream cpioOutputStream = factory.createArchiveOutputStream(ArchiveStreamFactory.CPIO, null);
        assertNull(cpioOutputStream);
    }
    
    @Test
    public void testCreateArchiveInputStreamAutodetect() throws ArchiveException {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        
        // Test ZIP archive format
        ArchiveInputStream zipInputStream = factory.createArchiveInputStream(null);
        assertNull(zipInputStream);
        
        // Test TAR archive format
        ArchiveInputStream tarInputStream = factory.createArchiveInputStream(null);
        assertNull(tarInputStream);
        
        // Test JAR archive format
        ArchiveInputStream jarInputStream = factory.createArchiveInputStream(null);
        assertNull(jarInputStream);
        
        // Test CPIO archive format
        ArchiveInputStream cpioInputStream = factory.createArchiveInputStream(null);
        assertNull(cpioInputStream);
    }
}

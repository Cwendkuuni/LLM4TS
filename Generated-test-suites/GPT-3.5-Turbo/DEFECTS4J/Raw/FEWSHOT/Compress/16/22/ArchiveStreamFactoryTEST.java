import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;

public class ArchiveStreamFactoryTest {
    
    @Test
    public void testCreateArchiveInputStream() {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        
        // Test AR
        ArchiveInputStream arin = factory.createArchiveInputStream(ArchiveStreamFactory.AR, null);
        assertNotNull(arin);
        
        // Test ZIP
        ArchiveInputStream zipin = factory.createArchiveInputStream(ArchiveStreamFactory.ZIP, null);
        assertNotNull(zipin);
        
        // Test TAR
        ArchiveInputStream tarin = factory.createArchiveInputStream(ArchiveStreamFactory.TAR, null);
        assertNotNull(tarin);
        
        // Test JAR
        ArchiveInputStream jarin = factory.createArchiveInputStream(ArchiveStreamFactory.JAR, null);
        assertNotNull(jarin);
        
        // Test CPIO
        ArchiveInputStream cpioin = factory.createArchiveInputStream(ArchiveStreamFactory.CPIO, null);
        assertNotNull(cpioin);
        
        // Test DUMP
        ArchiveInputStream dumpin = factory.createArchiveInputStream(ArchiveStreamFactory.DUMP, null);
        assertNotNull(dumpin);
        
    }
    
    @Test
    public void testCreateArchiveOutputStream() {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        
        // Test AR
        ArchiveOutputStream aros = factory.createArchiveOutputStream(ArchiveStreamFactory.AR, null);
        assertNotNull(aros);
        
        // Test ZIP
        ArchiveOutputStream zipos = factory.createArchiveOutputStream(ArchiveStreamFactory.ZIP, null);
        assertNotNull(zipos);
        
        // Test TAR
        ArchiveOutputStream taros = factory.createArchiveOutputStream(ArchiveStreamFactory.TAR, null);
        assertNotNull(taros);
        
        // Test JAR
        ArchiveOutputStream jaros = factory.createArchiveOutputStream(ArchiveStreamFactory.JAR, null);
        assertNotNull(jaros);
        
        // Test CPIO
        ArchiveOutputStream cpioos = factory.createArchiveOutputStream(ArchiveStreamFactory.CPIO, null);
        assertNotNull(cpioos);
    }
    
    @Test
    public void testCreateArchiveInputStreamAutoDetect() {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        
        // Test ZIP
        ArchiveInputStream zipin = factory.createArchiveInputStream(null);
        assertNotNull(zipin);
        
        // Test JAR
        ArchiveInputStream jarin = factory.createArchiveInputStream(null);
        assertNotNull(jarin);
        
        // Test AR
        ArchiveInputStream arin = factory.createArchiveInputStream(null);
        assertNotNull(arin);
        
        // Test CPIO
        ArchiveInputStream cpioin = factory.createArchiveInputStream(null);
        assertNotNull(cpioin);
        
        // Test DUMP
        ArchiveInputStream dumpin = factory.createArchiveInputStream(null);
        assertNotNull(dumpin);
        
        // Test TAR
        ArchiveInputStream tarin = factory.createArchiveInputStream(null);
        assertNotNull(tarin);
    }
}
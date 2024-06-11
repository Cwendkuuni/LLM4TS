import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

import org.apache.commons.compress.archivers.*;
import org.junit.Test;

public class ArchiveStreamFactoryTest {

    @Test
    public void testCreateArchiveInputStream() throws ArchiveException {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        
        // Test AR
        ArchiveInputStream arInput = factory.createArchiveInputStream(ArchiveStreamFactory.AR, new ByteArrayInputStream(new byte[0]));
        assertNotNull(arInput);
        assertTrue(arInput instanceof ArArchiveInputStream);
        
        // Test ZIP
        ArchiveInputStream zipInput = factory.createArchiveInputStream(ArchiveStreamFactory.ZIP, new ByteArrayInputStream(new byte[0]));
        assertNotNull(zipInput);
        assertTrue(zipInput instanceof ZipArchiveInputStream);
        
        // Test TAR
        ArchiveInputStream tarInput = factory.createArchiveInputStream(ArchiveStreamFactory.TAR, new ByteArrayInputStream(new byte[0]));
        assertNotNull(tarInput);
        assertTrue(tarInput instanceof TarArchiveInputStream);
        
        // Test JAR
        ArchiveInputStream jarInput = factory.createArchiveInputStream(ArchiveStreamFactory.JAR, new ByteArrayInputStream(new byte[0]));
        assertNotNull(jarInput);
        assertTrue(jarInput instanceof JarArchiveInputStream);
        
        // Test CPIO
        ArchiveInputStream cpioInput = factory.createArchiveInputStream(ArchiveStreamFactory.CPIO, new ByteArrayInputStream(new byte[0]));
        assertNotNull(cpioInput);
        assertTrue(cpioInput instanceof CpioArchiveInputStream);
        
        // Test DUMP
        ArchiveInputStream dumpInput = factory.createArchiveInputStream(ArchiveStreamFactory.DUMP, new ByteArrayInputStream(new byte[0]));
        assertNotNull(dumpInput);
        assertTrue(dumpInput instanceof DumpArchiveInputStream);
        
    }

    @Test
    public void testCreateArchiveOutputStream() throws ArchiveException {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        
        // Test AR
        ArchiveOutputStream arOutput = factory.createArchiveOutputStream(ArchiveStreamFactory.AR, new ByteArrayOutputStream());
        assertNotNull(arOutput);
        assertTrue(arOutput instanceof ArArchiveOutputStream);
        
        // Test ZIP
        ArchiveOutputStream zipOutput = factory.createArchiveOutputStream(ArchiveStreamFactory.ZIP, new ByteArrayOutputStream());
        assertNotNull(zipOutput);
        assertTrue(zipOutput instanceof ZipArchiveOutputStream);
        
        // Test TAR
        ArchiveOutputStream tarOutput = factory.createArchiveOutputStream(ArchiveStreamFactory.TAR, new ByteArrayOutputStream());
        assertNotNull(tarOutput);
        assertTrue(tarOutput instanceof TarArchiveOutputStream);
        
        // Test JAR
        ArchiveOutputStream jarOutput = factory.createArchiveOutputStream(ArchiveStreamFactory.JAR, new ByteArrayOutputStream());
        assertNotNull(jarOutput);
        assertTrue(jarOutput instanceof JarArchiveOutputStream);
        
        // Test CPIO
        ArchiveOutputStream cpioOutput = factory.createArchiveOutputStream(ArchiveStreamFactory.CPIO, new ByteArrayOutputStream());
        assertNotNull(cpioOutput);
        assertTrue(cpioOutput instanceof CpioArchiveOutputStream);
        
    }

    @Test
    public void testCreateArchiveInputStream_autodetect() throws ArchiveException {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        
        byte[] zipData = new byte[4];
        zipData[0] = 0x50; // P
        zipData[1] = 0x4B; // K
        zipData[2] = 0x03; // version needed to extract
        zipData[3] = 0x04; // version needed to extract
        
        ArchiveInputStream zipInput = factory.createArchiveInputStream(new ByteArrayInputStream(zipData));
        assertNotNull(zipInput);
        assertTrue(zipInput instanceof ZipArchiveInputStream);
        
        byte[] tarData = new byte[512];
        tarData[257] = 'u';
        tarData[258] = 's';
        tarData[259] = 't';
        tarData[260] = 'a';
        tarData[261] = 'r';
        
        ArchiveInputStream tarInput = factory.createArchiveInputStream(new ByteArrayInputStream(tarData));
        assertNotNull(tarInput);
        assertTrue(tarInput instanceof TarArchiveInputStream);
        
    }

}
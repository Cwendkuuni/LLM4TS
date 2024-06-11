import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.apache.commons.compress.archivers.jar.JarArchiveInputStream;
import org.apache.commons.compress.archivers.jar.JarArchiveOutputStream;
import org.apache.commons.compress.archivers.tar.TarArchiveInputStream;
import org.apache.commons.compress.archivers.tar.TarArchiveOutputStream;
import org.apache.commons.compress.archivers.zip.ZipArchiveInputStream;
import org.apache.commons.compress.archivers.zip.ZipArchiveOutputStream;
import org.junit.Test;

public class ArchiveStreamFactoryTest {
    @Test
    public void testCreateArchiveInputStream() throws ArchiveException {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        
        // Test for AR format
        ArchiveInputStream arInput = factory.createArchiveInputStream(ArchiveStreamFactory.AR, null);
        assertNull(arInput);
        
        // Test for ZIP format
        ArchiveInputStream zipInput = factory.createArchiveInputStream(ArchiveStreamFactory.ZIP, null);
        assertNotNull(zipInput);
        assertEquals(ZipArchiveInputStream.class, zipInput.getClass());
        
        // Test for TAR format
        ArchiveInputStream tarInput = factory.createArchiveInputStream(ArchiveStreamFactory.TAR, null);
        assertNotNull(tarInput);
        assertEquals(TarArchiveInputStream.class, tarInput.getClass());
        
        // Test for JAR format
        ArchiveInputStream jarInput = factory.createArchiveInputStream(ArchiveStreamFactory.JAR, null);
        assertNotNull(jarInput);
        assertEquals(JarArchiveInputStream.class, jarInput.getClass());
        
        // Test for CPIO format
        ArchiveInputStream cpioInput = factory.createArchiveInputStream(ArchiveStreamFactory.CPIO, null);
        assertNotNull(cpioInput);
        assertEquals(CpioArchiveInputStream.class, cpioInput.getClass());
        
        // Test for DUMP format
        ArchiveInputStream dumpInput = factory.createArchiveInputStream(ArchiveStreamFactory.DUMP, null);
        assertNotNull(dumpInput);
        assertEquals(DumpArchiveInputStream.class, dumpInput.getClass());
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testCreateArchiveInputStreamWithNullArchiverName() throws ArchiveException {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        InputStream inputStream = new ByteArrayInputStream(new byte[0]);
        factory.createArchiveInputStream(null, inputStream);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testCreateArchiveInputStreamWithNullInputStream() throws ArchiveException {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        factory.createArchiveInputStream(ArchiveStreamFactory.ZIP, null);
    }
    
    @Test
    public void testCreateArchiveOutputStream() throws ArchiveException {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        
        // Test for AR format
        ArchiveOutputStream arOutput = factory.createArchiveOutputStream(ArchiveStreamFactory.AR, null);
        assertNull(arOutput);
        
        // Test for ZIP format
        ArchiveOutputStream zipOutput = factory.createArchiveOutputStream(ArchiveStreamFactory.ZIP, null);
        assertNotNull(zipOutput);
        assertEquals(ZipArchiveOutputStream.class, zipOutput.getClass());
        
        // Test for TAR format
        ArchiveOutputStream tarOutput = factory.createArchiveOutputStream(ArchiveStreamFactory.TAR, null);
        assertNotNull(tarOutput);
        assertEquals(TarArchiveOutputStream.class, tarOutput.getClass());
        
        // Test for JAR format
        ArchiveOutputStream jarOutput = factory.createArchiveOutputStream(ArchiveStreamFactory.JAR, null);
        assertNotNull(jarOutput);
        assertEquals(JarArchiveOutputStream.class, jarOutput.getClass());
        
        // Test for CPIO format
        ArchiveOutputStream cpioOutput = factory.createArchiveOutputStream(ArchiveStreamFactory.CPIO, null);
        assertNotNull(cpioOutput);
        assertEquals(CpioArchiveOutputStream.class, cpioOutput.getClass());
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testCreateArchiveOutputStreamWithNullArchiverName() throws ArchiveException {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        OutputStream outputStream = new ByteArrayOutputStream();
        factory.createArchiveOutputStream(null, outputStream);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testCreateArchiveOutputStreamWithNullOutputStream() throws ArchiveException {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        factory.createArchiveOutputStream(ArchiveStreamFactory.ZIP, null);
    }
    
    @Test
    public void testCreateArchiveInputStreamWithAutodetection() throws ArchiveException {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        
        // Test for ZIP format
        InputStream zipInputStream = new ByteArrayInputStream(new byte[] { 0x50, 0x4B });
        ArchiveInputStream zipInput = factory.createArchiveInputStream(zipInputStream);
        assertNotNull(zipInput);
        assertEquals(ZipArchiveInputStream.class, zipInput.getClass());
        
        // Test for JAR format
        InputStream jarInputStream = new ByteArrayInputStream(new byte[] { 0x50, 0x4B, 0x03, 0x04 });
        ArchiveInputStream jarInput = factory.createArchiveInputStream(jarInputStream);
        assertNotNull(jarInput);
        assertEquals(JarArchiveInputStream.class, jarInput.getClass());
        
        // Test for AR format
        InputStream arInputStream = new ByteArrayInputStream(new byte[] { 0x21, 0x3C, 0x61, 0x72 });
        ArchiveInputStream arInput = factory.createArchiveInputStream(arInputStream);
        assertNotNull(arInput);
        assertEquals(ArArchiveInputStream.class, arInput.getClass());
        
        // Test for CPIO format
        InputStream cpioInputStream = new ByteArrayInputStream(new byte[] { 0x30, 0x37, 0x30, 0x37 });
        ArchiveInputStream cpioInput = factory.createArchiveInputStream(cpioInputStream);
        assertNotNull(cpioInput);
        assertEquals(CpioArchiveInputStream.class, cpioInput.getClass());
        
        // Test for DUMP format
        InputStream dumpInputStream = new ByteArrayInputStream(new byte[] { 0x2A, 0x2A, 0x2A, 0x4D });
        ArchiveInputStream dumpInput = factory.createArchiveInputStream(dumpInputStream);
        assertNotNull(dumpInput);
        assertEquals(DumpArchiveInputStream.class, dumpInput.getClass());
        
        // Test for TAR format
        InputStream tarInputStream = new ByteArrayInputStream(new byte[] { 0x75, 0x73, 0x74, 0x61, 0x72, 0x00 });
        ArchiveInputStream tarInput = factory.createArchiveInputStream(tarInputStream);
        assertNotNull(tarInput);
        assertEquals(TarArchiveInputStream.class, tarInput.getClass());
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testCreateArchiveInputStreamWithNullInputStreamForAutodetection() throws ArchiveException {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        factory.createArchiveInputStream(null);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testCreateArchiveInputStreamWithNonMarkSupportedInputStreamForAutodetection() throws ArchiveException {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        InputStream inputStream = new ByteArrayInputStream(new byte[0]);
        factory.createArchiveInputStream(inputStream);
    }
    
    @Test(expected = ArchiveException.class)
    public void testCreateArchiveInputStreamWithUnknownSignatureForAutodetection() throws ArchiveException {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        InputStream inputStream = new ByteArrayInputStream(new byte[] { 0x11, 0x22, 0x33, 0x44 });
        factory.createArchiveInputStream(inputStream);
    }
}
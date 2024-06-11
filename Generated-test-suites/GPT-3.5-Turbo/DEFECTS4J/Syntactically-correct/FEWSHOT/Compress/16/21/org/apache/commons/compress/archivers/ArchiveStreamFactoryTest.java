package org.apache.commons.compress.archivers;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Before;
import org.junit.Test;

public class ArchiveStreamFactoryTest {
    
    private ArchiveStreamFactory archiveStreamFactory;
    
    @Before
    public void setup() {
        archiveStreamFactory = new ArchiveStreamFactory();
    }
    
    @Test
    public void testCreateArchiveInputStream() throws ArchiveException {
        InputStream in = new ByteArrayInputStream(new byte[0]);
        assertEquals(ArArchiveInputStream.class, archiveStreamFactory.createArchiveInputStream(ArchiveStreamFactory.AR, in).getClass());
        assertEquals(ZipArchiveInputStream.class, archiveStreamFactory.createArchiveInputStream(ArchiveStreamFactory.ZIP, in).getClass());
        assertEquals(TarArchiveInputStream.class, archiveStreamFactory.createArchiveInputStream(ArchiveStreamFactory.TAR, in).getClass());
        assertEquals(JarArchiveInputStream.class, archiveStreamFactory.createArchiveInputStream(ArchiveStreamFactory.JAR, in).getClass());
        assertEquals(CpioArchiveInputStream.class, archiveStreamFactory.createArchiveInputStream(ArchiveStreamFactory.CPIO, in).getClass());
        assertEquals(DumpArchiveInputStream.class, archiveStreamFactory.createArchiveInputStream(ArchiveStreamFactory.DUMP, in).getClass());
        
        try {
            archiveStreamFactory.createArchiveInputStream("unknown", in);
            fail("Expected ArchiveException");
        } catch (ArchiveException e) {
            assertEquals("Archiver: unknown not found.", e.getMessage());
        }
        
        try {
            archiveStreamFactory.createArchiveInputStream(null, in);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("Archivername must not be null.", e.getMessage());
        }
        
        try {
            archiveStreamFactory.createArchiveInputStream(ArchiveStreamFactory.ZIP, null);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("InputStream must not be null.", e.getMessage());
        }
    }
    
    @Test
    public void testCreateArchiveOutputStream() throws ArchiveException {
        OutputStream out = new ByteArrayOutputStream();
        assertEquals(ArArchiveOutputStream.class, archiveStreamFactory.createArchiveOutputStream(ArchiveStreamFactory.AR, out).getClass());
        assertEquals(ZipArchiveOutputStream.class, archiveStreamFactory.createArchiveOutputStream(ArchiveStreamFactory.ZIP, out).getClass());
        assertEquals(TarArchiveOutputStream.class, archiveStreamFactory.createArchiveOutputStream(ArchiveStreamFactory.TAR, out).getClass());
        assertEquals(JarArchiveOutputStream.class, archiveStreamFactory.createArchiveOutputStream(ArchiveStreamFactory.JAR, out).getClass());
        assertEquals(CpioArchiveOutputStream.class, archiveStreamFactory.createArchiveOutputStream(ArchiveStreamFactory.CPIO, out).getClass());
        
        try {
            archiveStreamFactory.createArchiveOutputStream("unknown", out);
            fail("Expected ArchiveException");
        } catch (ArchiveException e) {
            assertEquals("Archiver: unknown not found.", e.getMessage());
        }
        
        try {
            archiveStreamFactory.createArchiveOutputStream(null, out);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("Archivername must not be null.", e.getMessage());
        }
        
        try {
            archiveStreamFactory.createArchiveOutputStream(ArchiveStreamFactory.ZIP, null);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("OutputStream must not be null.", e.getMessage());
        }
    }
    
    @Test
    public void testCreateArchiveInputStream_autoDetect() throws ArchiveException {
        InputStream in = new ByteArrayInputStream(new byte[0]);
        assertEquals(ZipArchiveInputStream.class, archiveStreamFactory.createArchiveInputStream(in).getClass());
        
        byte[] zipSignature = new byte[] { 80, 75, 3, 4 };
        in = new ByteArrayInputStream(zipSignature);
        assertEquals(ZipArchiveInputStream.class, archiveStreamFactory.createArchiveInputStream(in).getClass());
        
        byte[] jarSignature = new byte[] { 80, 75, 3, 4, 20, 0 };
        in = new ByteArrayInputStream(jarSignature);
        assertEquals(JarArchiveInputStream.class, archiveStreamFactory.createArchiveInputStream(in).getClass());
        
        byte[] arSignature = new byte[] { 33, 60, 97, 114, 33, 62 };
        in = new ByteArrayInputStream(arSignature);
        assertEquals(ArArchiveInputStream.class, archiveStreamFactory.createArchiveInputStream(in).getClass());
        
        byte[] cpioSignature = new byte[] { 43, 48, 50, 53, 71, 66, 48, 50, 48, 48, 48, 0, 48, 48, 48, 48, 48, 48, 48, 48, 48, 48, 48, 48, 48, 0, 48, 48, 48, 48, 48, 48, 48, 48, 48, 48, 48, 48, 48, 48, 48, 48, 48, 48, 48, 48, 48, 48, 0, 48, 48, 48, 48, 48, 48, 48, 48, 48, 48, 48, 48, 48, 48, 48, 48, 48, 48, 48, 48, 48, 48, 48, 48, 48, 0, 48, 48, 48, 48 };
        in = new ByteArrayInputStream(cpioSignature);
        assertEquals(CpioArchiveInputStream.class, archiveStreamFactory.createArchiveInputStream(in).getClass());
        
        byte[] dumpSignature = new byte[] { 119, 97, 115, 116, 101 };
        in = new ByteArrayInputStream(dumpSignature);
        assertEquals(DumpArchiveInputStream.class, archiveStreamFactory.createArchiveInputStream(in).getClass());
        
        try {
            byte[] unknownSignature = new byte[] { 0, 1, 2, 3 };
            in = new ByteArrayInputStream(unknownSignature);
            archiveStreamFactory.createArchiveInputStream(in);
            fail("Expected ArchiveException");
        } catch (ArchiveException e) {
            assertEquals("No Archiver found for the stream signature", e.getMessage());
        }
        
        try {
            archiveStreamFactory.createArchiveInputStream(null);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("Stream must not be null.", e.getMessage());
        }
        
        try {
            InputStream unsupportedMarkStream = new FileInputStream("path/to/file");
            archiveStreamFactory.createArchiveInputStream(unsupportedMarkStream);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("Mark is not supported.", e.getMessage());
        }
    }
}
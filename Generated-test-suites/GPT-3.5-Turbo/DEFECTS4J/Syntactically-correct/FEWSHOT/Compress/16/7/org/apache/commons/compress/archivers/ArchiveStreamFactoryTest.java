package org.apache.commons.compress.archivers;

import static org.junit.Assert.*;
import org.junit.Test;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class ArchiveStreamFactoryTest {
    
    @Test
    public void testCreateArchiveInputStream() {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        InputStream in = new ByteArrayInputStream(new byte[0]);
        
        try {
            ArchiveInputStream is = factory.createArchiveInputStream(ArchiveStreamFactory.AR, in);
            assertTrue(is instanceof ArArchiveInputStream);
        } catch (ArchiveException | IllegalArgumentException e) {
            fail(e.getMessage());
        }
        
        try {
            ArchiveInputStream is = factory.createArchiveInputStream(ArchiveStreamFactory.ZIP, in);
            assertTrue(is instanceof ZipArchiveInputStream);
        } catch (ArchiveException | IllegalArgumentException e) {
            fail(e.getMessage());
        }
        
        try {
            ArchiveInputStream is = factory.createArchiveInputStream(ArchiveStreamFactory.TAR, in);
            assertTrue(is instanceof TarArchiveInputStream);
        } catch (ArchiveException | IllegalArgumentException e) {
            fail(e.getMessage());
        }
        
        try {
            ArchiveInputStream is = factory.createArchiveInputStream(ArchiveStreamFactory.JAR, in);
            assertTrue(is instanceof JarArchiveInputStream);
        } catch (ArchiveException | IllegalArgumentException e) {
            fail(e.getMessage());
        }
        
        try {
            ArchiveInputStream is = factory.createArchiveInputStream(ArchiveStreamFactory.CPIO, in);
            assertTrue(is instanceof CpioArchiveInputStream);
        } catch (ArchiveException | IllegalArgumentException e) {
            fail(e.getMessage());
        }
        
        try {
            ArchiveInputStream is = factory.createArchiveInputStream(ArchiveStreamFactory.DUMP, in);
            assertTrue(is instanceof DumpArchiveInputStream);
        } catch (ArchiveException | IllegalArgumentException e) {
            fail(e.getMessage());
        }
        
        try {
            factory.createArchiveInputStream(null, in);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("Archivername must not be null.", e.getMessage());
        }
        
        try {
            factory.createArchiveInputStream(ArchiveStreamFactory.AR, null);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("InputStream must not be null.", e.getMessage());
        }
    }
    
    @Test
    public void testCreateArchiveOutputStream() {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        OutputStream out = new ByteArrayOutputStream();
        
        try {
            ArchiveOutputStream os = factory.createArchiveOutputStream(ArchiveStreamFactory.AR, out);
            assertTrue(os instanceof ArArchiveOutputStream);
        } catch (ArchiveException | IllegalArgumentException e) {
            fail(e.getMessage());
        }
        
        try {
            ArchiveOutputStream os = factory.createArchiveOutputStream(ArchiveStreamFactory.ZIP, out);
            assertTrue(os instanceof ZipArchiveOutputStream);
        } catch (ArchiveException | IllegalArgumentException e) {
            fail(e.getMessage());
        }
        
        try {
            ArchiveOutputStream os = factory.createArchiveOutputStream(ArchiveStreamFactory.TAR, out);
            assertTrue(os instanceof TarArchiveOutputStream);
        } catch (ArchiveException | IllegalArgumentException e) {
            fail(e.getMessage());
        }
        
        try {
            ArchiveOutputStream os = factory.createArchiveOutputStream(ArchiveStreamFactory.JAR, out);
            assertTrue(os instanceof JarArchiveOutputStream);
        } catch (ArchiveException | IllegalArgumentException e) {
            fail(e.getMessage());
        }
        
        try {
            ArchiveOutputStream os = factory.createArchiveOutputStream(ArchiveStreamFactory.CPIO, out);
            assertTrue(os instanceof CpioArchiveOutputStream);
        } catch (ArchiveException | IllegalArgumentException e) {
            fail(e.getMessage());
        }
        
        try {
            factory.createArchiveOutputStream(null, out);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("Archivername must not be null.", e.getMessage());
        }
        
        try {
            factory.createArchiveOutputStream(ArchiveStreamFactory.AR, null);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("OutputStream must not be null.", e.getMessage());
        }
    }
    
    @Test
    public void testCreateArchiveInputStreamAutoDetect() {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        InputStream in = new ByteArrayInputStream(new byte[0]);
        
        try {
            ArchiveInputStream is = factory.createArchiveInputStream(in);
            assertTrue(is instanceof ZipArchiveInputStream);
        } catch (ArchiveException | IllegalArgumentException e) {
            fail(e.getMessage());
        }
        
        try {
            byte[] signature = { 0x62, 0x75, 0x68, 0x0A };
            in = new ByteArrayInputStream(signature);
            ArchiveInputStream is = factory.createArchiveInputStream(in);
            assertTrue(is instanceof ArArchiveInputStream);
        } catch (ArchiveException | IllegalArgumentException e) {
            fail(e.getMessage());
        }
        
        try {
            byte[] signature = { 0x50, 0x4B, 0x03, 0x04 };
            in = new ByteArrayInputStream(signature);
            ArchiveInputStream is = factory.createArchiveInputStream(in);
            assertTrue(is instanceof ZipArchiveInputStream);
        } catch (ArchiveException | IllegalArgumentException e) {
            fail(e.getMessage());
        }
        
        try {
            byte[] signature = { 0x1F, 0x8B, 0x08 };
            in = new ByteArrayInputStream(signature);
            ArchiveInputStream is = factory.createArchiveInputStream(in);
            assertTrue(is instanceof TarArchiveInputStream);
        } catch (ArchiveException | IllegalArgumentException e) {
            fail(e.getMessage());
        }
        
        try {
            byte[] signature = { 0x4D, 0x5A };
            in = new ByteArrayInputStream(signature);
            ArchiveInputStream is = factory.createArchiveInputStream(in);
            assertTrue(is instanceof JarArchiveInputStream);
        } catch (ArchiveException | IllegalArgumentException e) {
            fail(e.getMessage());
        }
        
        try {
            byte[] signature = { 0x30, 0x37, 0x30, 0x37 };
            in = new ByteArrayInputStream(signature);
            ArchiveInputStream is = factory.createArchiveInputStream(in);
            assertTrue(is instanceof CpioArchiveInputStream);
        } catch (ArchiveException | IllegalArgumentException e) {
            fail(e.getMessage());
        }
        
        try {
            byte[] signature = { 0x30, 0x37, 0x30, 0x32 };
            in = new ByteArrayInputStream(signature);
            ArchiveInputStream is = factory.createArchiveInputStream(in);
            assertTrue(is instanceof DumpArchiveInputStream);
        } catch (ArchiveException | IllegalArgumentException e) {
            fail(e.getMessage());
        }
        
        try {
            factory.createArchiveInputStream(null);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("Stream must not be null.", e.getMessage());
        }
        
        try {
            InputStream unsupportedStream = new ByteArrayInputStream(new byte[0]) {
                @Override
                public boolean markSupported() {
                    return false;
                }
            };
            factory.createArchiveInputStream(unsupportedStream);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("Mark is not supported.", e.getMessage());
        }
        
        try {
            byte[] invalidSignature = { 0x00, 0x01 };
            in = new ByteArrayInputStream(invalidSignature);
            factory.createArchiveInputStream(in);
            fail("Expected ArchiveException");
        } catch (ArchiveException e) {
            assertEquals("No Archiver found for the stream signature", e.getMessage());
        }
    }
}
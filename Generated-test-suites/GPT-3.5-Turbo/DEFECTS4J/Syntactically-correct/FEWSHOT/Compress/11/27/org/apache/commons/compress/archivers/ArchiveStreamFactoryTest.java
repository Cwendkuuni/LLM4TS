package org.apache.commons.compress.archivers;

import org.junit.Test;
import static org.junit.Assert.*;

public class ArchiveStreamFactoryTest {
    
    @Test
    public void testCreateArchiveInputStream() {
        try {
            ArchiveInputStream arStream = ArchiveStreamFactory.createArchiveInputStream(ArchiveStreamFactory.AR, null);
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            // exception expected
        }
        
        try {
            ArchiveInputStream zipStream = ArchiveStreamFactory.createArchiveInputStream(ArchiveStreamFactory.ZIP, null);
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            // exception expected
        }
        
        try {
            ArchiveInputStream tarStream = ArchiveStreamFactory.createArchiveInputStream(ArchiveStreamFactory.TAR, null);
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            // exception expected
        }
        
        try {
            ArchiveInputStream jarStream = ArchiveStreamFactory.createArchiveInputStream(ArchiveStreamFactory.JAR, null);
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            // exception expected
        }
        
        try {
            ArchiveInputStream cpioStream = ArchiveStreamFactory.createArchiveInputStream(ArchiveStreamFactory.CPIO, null);
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            // exception expected
        }
        
        try {
            ArchiveInputStream dumpStream = ArchiveStreamFactory.createArchiveInputStream(ArchiveStreamFactory.DUMP, null);
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            // exception expected
        }
        
        try {
            ArchiveInputStream unknownStream = ArchiveStreamFactory.createArchiveInputStream("unknown", null);
            fail("Expected ArchiveException to be thrown");
        } catch (ArchiveException e) {
            // exception expected
        }
        
        try {
            InputStream inputStream = new ByteArrayInputStream("test".getBytes());
            ArchiveInputStream autoStream = ArchiveStreamFactory.createArchiveInputStream(inputStream);
            assertNotNull(autoStream);
        } catch (ArchiveException e) {
            fail("Unexpected ArchiveException thrown");
        }
    }
    
    @Test
    public void testCreateArchiveOutputStream() {
        try {
            ArchiveOutputStream arStream = ArchiveStreamFactory.createArchiveOutputStream(ArchiveStreamFactory.AR, null);
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            // exception expected
        }
        
        try {
            ArchiveOutputStream zipStream = ArchiveStreamFactory.createArchiveOutputStream(ArchiveStreamFactory.ZIP, null);
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            // exception expected
        }
        
        try {
            ArchiveOutputStream tarStream = ArchiveStreamFactory.createArchiveOutputStream(ArchiveStreamFactory.TAR, null);
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            // exception expected
        }
        
        try {
            ArchiveOutputStream jarStream = ArchiveStreamFactory.createArchiveOutputStream(ArchiveStreamFactory.JAR, null);
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            // exception expected
        }
        
        try {
            ArchiveOutputStream cpioStream = ArchiveStreamFactory.createArchiveOutputStream(ArchiveStreamFactory.CPIO, null);
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            // exception expected
        }
        
        try {
            ArchiveOutputStream unknownStream = ArchiveStreamFactory.createArchiveOutputStream("unknown", null);
            fail("Expected ArchiveException to be thrown");
        } catch (ArchiveException e) {
            // exception expected
        }
        
        try {
            OutputStream outputStream = new ByteArrayOutputStream();
            ArchiveOutputStream autoStream = ArchiveStreamFactory.createArchiveOutputStream(outputStream);
            assertNotNull(autoStream);
        } catch (ArchiveException e) {
            fail("Unexpected ArchiveException thrown");
        }
    }
}
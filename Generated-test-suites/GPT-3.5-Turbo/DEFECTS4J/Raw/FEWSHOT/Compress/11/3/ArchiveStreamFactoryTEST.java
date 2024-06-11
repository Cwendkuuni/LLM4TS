package org.apache.commons.compress.archivers;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.junit.Test;

public class ArchiveStreamFactoryTest {
    
    @Test
    public void testCreateArchiveInputStream() throws ArchiveException {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        InputStream input = new ByteArrayInputStream("test".getBytes());
        
        assertNotNull(factory.createArchiveInputStream(ArchiveStreamFactory.AR, input));
        assertNotNull(factory.createArchiveInputStream(ArchiveStreamFactory.ZIP, input));
        assertNotNull(factory.createArchiveInputStream(ArchiveStreamFactory.TAR, input));
        assertNotNull(factory.createArchiveInputStream(ArchiveStreamFactory.JAR, input));
        assertNotNull(factory.createArchiveInputStream(ArchiveStreamFactory.CPIO, input));
        
        try {
            factory.createArchiveInputStream("invalid", input);
        } catch(ArchiveException e) {
            assertEquals("Archiver: invalid not found.", e.getMessage());
        }
        
        try {
            factory.createArchiveInputStream(null, input);
        } catch(IllegalArgumentException e) {
            assertEquals("Archivername must not be null.", e.getMessage());
        }
        
        try {
            factory.createArchiveInputStream(ArchiveStreamFactory.ZIP, null);
        } catch(IllegalArgumentException e) {
            assertEquals("InputStream must not be null.", e.getMessage());
        }
    }
    
    @Test
    public void testCreateArchiveOutputStream() throws ArchiveException {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        OutputStream output = new ByteArrayOutputStream();
        
        assertNotNull(factory.createArchiveOutputStream(ArchiveStreamFactory.AR, output));
        assertNotNull(factory.createArchiveOutputStream(ArchiveStreamFactory.ZIP, output));
        assertNotNull(factory.createArchiveOutputStream(ArchiveStreamFactory.TAR, output));
        assertNotNull(factory.createArchiveOutputStream(ArchiveStreamFactory.JAR, output));
        assertNotNull(factory.createArchiveOutputStream(ArchiveStreamFactory.CPIO, output));
        
        try {
            factory.createArchiveOutputStream("invalid", output);
        } catch(ArchiveException e) {
            assertEquals("Archiver: invalid not found.", e.getMessage());
        }
        
        try {
            factory.createArchiveOutputStream(null, output);
        } catch(IllegalArgumentException e) {
            assertEquals("Archivername must not be null.", e.getMessage());
        }
        
        try {
            factory.createArchiveOutputStream(ArchiveStreamFactory.ZIP, null);
        } catch(IllegalArgumentException e) {
            assertEquals("OutputStream must not be null.", e.getMessage());
        }
    }
    
    @Test
    public void testCreateArchiveInputStreamWithAutodetection() throws ArchiveException, IOException {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        byte[] data = "test".getBytes();
        InputStream input = new ByteArrayInputStream(data);
        
        assertNotNull(factory.createArchiveInputStream(input));
        
        input = new ByteArrayInputStream(data);
        input.mark(12);
        try {
            assertNull(factory.createArchiveInputStream(input));
        } catch (IllegalArgumentException e) {
            assertEquals("Mark is not supported.", e.getMessage());
        }
        
        data = "PK\003\004".getBytes();  // ZIP signature
        input = new ByteArrayInputStream(data);
        assertNotNull(factory.createArchiveInputStream(input));
        
        data = new byte[]{0x1F, (byte)0x8B};  // TAR signature
        input = new ByteArrayInputStream(data);
        assertNotNull(factory.createArchiveInputStream(input));
        
        data = new byte[]{0x7F, 'E', 'L', 'F'};  // not a supported archive format
        input = new ByteArrayInputStream(data);
        try {
            assertNull(factory.createArchiveInputStream(input));
        } catch (ArchiveException e) {
            assertEquals("No Archiver found for the stream signature", e.getMessage());
        }
        
        byte[] tarheader = new byte[512];
        data = new byte[]{0x75, 0x73, 0x74, 0x61, 0x72};
        System.arraycopy(data, 0, tarheader, 0, data.length);  // "ustar"
        input = new ByteArrayInputStream(tarheader);
        assertNotNull(factory.createArchiveInputStream(input));
    }
}
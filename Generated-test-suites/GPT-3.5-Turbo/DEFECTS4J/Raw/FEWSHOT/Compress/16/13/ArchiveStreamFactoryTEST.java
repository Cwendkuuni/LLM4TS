package org.apache.commons.compress.archivers;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.apache.commons.compress.archivers.tar.TarArchiveInputStream;
import org.apache.commons.compress.archivers.tar.TarArchiveOutputStream;
import org.junit.Test;

public class ArchiveStreamFactoryTest {
    
    @Test
    public void testCreateArchiveInputStream() throws IOException {
        byte[] data = "test".getBytes();
        ByteArrayInputStream in = new ByteArrayInputStream(data);
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        
        try {
            factory.createArchiveInputStream(null, in);
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            // Expected exception
        }
        
        try {
            factory.createArchiveInputStream("tar", null);
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            // Expected exception
        }
        
        ArchiveInputStream archiveIn = factory.createArchiveInputStream("tar", in);
        assertNotNull(archiveIn);
        assertTrue(archiveIn instanceof TarArchiveInputStream);
        
        in.close();
    }
    
    @Test
    public void testCreateArchiveOutputStream() throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        
        try {
            factory.createArchiveOutputStream(null, out);
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            // Expected exception
        }
        
        try {
            factory.createArchiveOutputStream("tar", null);
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            // Expected exception
        }
        
        ArchiveOutputStream archiveOut = factory.createArchiveOutputStream("tar", out);
        assertNotNull(archiveOut);
        assertTrue(archiveOut instanceof TarArchiveOutputStream);
        
        out.close();
    }
    
    @Test
    public void testCreateArchiveInputStreamAutoDetect() throws IOException {
        byte[] tarData = createTarData();
        byte[] zipData = createZipData();
        
        ByteArrayInputStream tarStream = new ByteArrayInputStream(tarData);
        ByteArrayInputStream zipStream = new ByteArrayInputStream(zipData);
        
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        
        try {
            factory.createArchiveInputStream(null);
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            // Expected exception
        }
        
        try {
            factory.createArchiveInputStream(null);
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            // Expected exception
        }
        
        ArchiveInputStream tarArchiveIn = factory.createArchiveInputStream(tarStream);
        assertNotNull(tarArchiveIn);
        assertTrue(tarArchiveIn instanceof TarArchiveInputStream);
        
        ArchiveInputStream zipArchiveIn = factory.createArchiveInputStream(zipStream);
        assertNotNull(zipArchiveIn);
        assertTrue(zipArchiveIn instanceof ZipArchiveInputStream);
        
        tarStream.close();
        zipStream.close();
    }
    
    private byte[] createTarData() throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        
        try (ArchiveOutputStream archiveOut = factory.createArchiveOutputStream("tar", out)) {
            TarArchiveEntry entry = new TarArchiveEntry("test.txt");
            entry.setSize(4);
            
            archiveOut.putArchiveEntry(entry);
            archiveOut.write("test".getBytes());
            archiveOut.closeArchiveEntry();
        }
        
        return out.toByteArray();
    }
    
    private byte[] createZipData() throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        
        try (ArchiveOutputStream archiveOut = factory.createArchiveOutputStream("zip", out)) {
            ZipArchiveEntry entry = new ZipArchiveEntry("test.txt");
            
            archiveOut.putArchiveEntry(entry);
            archiveOut.write("test".getBytes());
            archiveOut.closeArchiveEntry();
        }
        
        return out.toByteArray();
    }
}
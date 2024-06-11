package org.apache.commons.compress.archivers;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.apache.commons.compress.archivers.ArchiveException;
import org.apache.commons.compress.archivers.ArchiveInputStream;
import org.apache.commons.compress.archivers.ArchiveOutputStream;
import org.apache.commons.compress.archivers.ArchiveStreamFactory;
import org.junit.Test;

public class ArchiveStreamFactoryTest {
    
    @Test
    public void testCreateArchiveInputStream() throws IOException, ArchiveException {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        byte[] data = new byte[]{1, 2, 3, 4, 5};
        InputStream input = new ByteArrayInputStream(data);
        
        ArchiveInputStream archiveInputStream = factory.createArchiveInputStream(ArchiveStreamFactory.ZIP, input);
        
        assertNotNull(archiveInputStream);
        assertTrue(archiveInputStream instanceof org.apache.commons.compress.archivers.zip.ZipArchiveInputStream);
    }
    
    @Test
    public void testCreateArchiveOutputStream() throws IOException, ArchiveException {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        
        ArchiveOutputStream archiveOutputStream = factory.createArchiveOutputStream(ArchiveStreamFactory.ZIP, output);
        
        assertNotNull(archiveOutputStream);
        assertTrue(archiveOutputStream instanceof org.apache.commons.compress.archivers.zip.ZipArchiveOutputStream);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testCreateArchiveInputStreamNullArchiverName() throws IOException, ArchiveException {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        InputStream input = new ByteArrayInputStream(new byte[0]);
        
        factory.createArchiveInputStream(null, input);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testCreateArchiveInputStreamNullInputStream() throws IOException, ArchiveException {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        
        factory.createArchiveInputStream(ArchiveStreamFactory.ZIP, null);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testCreateArchiveOutputStreamNullArchiverName() throws IOException, ArchiveException {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        OutputStream output = new ByteArrayOutputStream();
        
        factory.createArchiveOutputStream(null, output);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testCreateArchiveOutputStreamNullOutputStream() throws IOException, ArchiveException {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        
        factory.createArchiveOutputStream(ArchiveStreamFactory.ZIP, null);
    }
}
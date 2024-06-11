package org.apache.commons.compress.archivers;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.apache.commons.compress.archivers.ar.ArArchiveInputStream;
import org.apache.commons.compress.archivers.ar.ArArchiveOutputStream;
import org.apache.commons.compress.archivers.cpio.CpioArchiveInputStream;
import org.apache.commons.compress.archivers.cpio.CpioArchiveOutputStream;
import org.apache.commons.compress.archivers.dump.DumpArchiveInputStream;
import org.apache.commons.compress.archivers.jar.JarArchiveInputStream;
import org.apache.commons.compress.archivers.jar.JarArchiveOutputStream;
import org.apache.commons.compress.archivers.tar.TarArchiveInputStream;
import org.apache.commons.compress.archivers.tar.TarArchiveOutputStream;
import org.apache.commons.compress.archivers.zip.ZipArchiveInputStream;
import org.apache.commons.compress.archivers.zip.ZipArchiveOutputStream;
import org.junit.Test;

public class ArchiveStreamFactoryTest {
    
    @Test
    public void testCreateArchiveInputStream_withValidArchiverNameAndInputStream() throws ArchiveException {
        ArchiveStreamFactory archiveStreamFactory = new ArchiveStreamFactory();
        InputStream inputStream = new ByteArrayInputStream(new byte[0]);
        
        ArchiveInputStream archiveInputStream = archiveStreamFactory.createArchiveInputStream(ArchiveStreamFactory.ZIP, inputStream);
        
        assertNotNull(archiveInputStream);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testCreateArchiveInputStream_withNullArchiverName() throws ArchiveException {
        ArchiveStreamFactory archiveStreamFactory = new ArchiveStreamFactory();
        InputStream inputStream = new ByteArrayInputStream(new byte[0]);
        
        archiveStreamFactory.createArchiveInputStream(null, inputStream);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testCreateArchiveInputStream_withNullInputStream() throws ArchiveException {
        ArchiveStreamFactory archiveStreamFactory = new ArchiveStreamFactory();
        
        archiveStreamFactory.createArchiveInputStream(ArchiveStreamFactory.ZIP, null);
    }
    
    @Test(expected = ArchiveException.class)
    public void testCreateArchiveInputStream_withUnknownArchiverName() throws ArchiveException {
        ArchiveStreamFactory archiveStreamFactory = new ArchiveStreamFactory();
        InputStream inputStream = new ByteArrayInputStream(new byte[0]);
        
        archiveStreamFactory.createArchiveInputStream("unknown", inputStream);
    }
    
    @Test
    public void testCreateArchiveOutputStream_withValidArchiverNameAndOutputStream() throws ArchiveException {
        ArchiveStreamFactory archiveStreamFactory = new ArchiveStreamFactory();
        OutputStream outputStream = new ByteArrayOutputStream();
        
        ArchiveOutputStream archiveOutputStream = archiveStreamFactory.createArchiveOutputStream(ArchiveStreamFactory.ZIP, outputStream);
        
        assertNotNull(archiveOutputStream);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testCreateArchiveOutputStream_withNullArchiverName() throws ArchiveException {
        ArchiveStreamFactory archiveStreamFactory = new ArchiveStreamFactory();
        OutputStream outputStream = new ByteArrayOutputStream();
        
        archiveStreamFactory.createArchiveOutputStream(null, outputStream);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testCreateArchiveOutputStream_withNullOutputStream() throws ArchiveException {
        ArchiveStreamFactory archiveStreamFactory = new ArchiveStreamFactory();
        
        archiveStreamFactory.createArchiveOutputStream(ArchiveStreamFactory.ZIP, null);
    }
    
    @Test(expected = ArchiveException.class)
    public void testCreateArchiveOutputStream_withUnknownArchiverName() throws ArchiveException {
        ArchiveStreamFactory archiveStreamFactory = new ArchiveStreamFactory();
        OutputStream outputStream = new ByteArrayOutputStream();
        
        archiveStreamFactory.createArchiveOutputStream("unknown", outputStream);
    }
    
    @Test
    public void testCreateArchiveInputStream_withMarkedInputStream() throws ArchiveException {
        ArchiveStreamFactory archiveStreamFactory = new ArchiveStreamFactory();
        InputStream inputStream = new ByteArrayInputStream(new byte[0]);
        inputStream.mark(10);
        
        ArchiveInputStream archiveInputStream = archiveStreamFactory.createArchiveInputStream(inputStream);
        
        assertNotNull(archiveInputStream);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testCreateArchiveInputStream_withNullInputStream() throws ArchiveException {
        ArchiveStreamFactory archiveStreamFactory = new ArchiveStreamFactory();
        
        archiveStreamFactory.createArchiveInputStream(null);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testCreateArchiveInputStream_withUnsupportedMark() throws ArchiveException {
        ArchiveStreamFactory archiveStreamFactory = new ArchiveStreamFactory();
        InputStream inputStream = new ByteArrayInputStream(new byte[0]);
        
        archiveStreamFactory.createArchiveInputStream(inputStream);
    }
    
    @Test(expected = ArchiveException.class)
    public void testCreateArchiveInputStream_withInvalidSignature() throws ArchiveException {
        ArchiveStreamFactory archiveStreamFactory = new ArchiveStreamFactory();
        InputStream inputStream = new ByteArrayInputStream(new byte[0]);
        
        archiveStreamFactory.createArchiveInputStream(inputStream);
    }
    
    // Additional test cases can be added to further test different scenarios
    
}
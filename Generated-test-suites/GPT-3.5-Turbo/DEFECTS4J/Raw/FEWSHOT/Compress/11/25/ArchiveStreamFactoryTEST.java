package org.apache.commons.compress.archivers;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

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

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

public class ArchiveStreamFactoryTest {
    @Test
    public void testCreateArchiveInputStream() throws Exception {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        InputStream inputStream = new ByteArrayInputStream(new byte[0]);
        
        assertNotNull(factory.createArchiveInputStream("ar", inputStream));
        assertNotNull(factory.createArchiveInputStream("zip", inputStream));
        assertNotNull(factory.createArchiveInputStream("tar", inputStream));
        assertNotNull(factory.createArchiveInputStream("jar", inputStream));
        assertNotNull(factory.createArchiveInputStream("cpio", inputStream));
        assertNotNull(factory.createArchiveInputStream("dump", inputStream));
        
        try {
            factory.createArchiveInputStream("unknown", inputStream);
        } catch (ArchiveException e) {
            assertEquals("Archiver: unknown not found.", e.getMessage());
        }
        
        try {
            factory.createArchiveInputStream(null, inputStream);
        } catch (IllegalArgumentException e) {
            assertEquals("Archivername must not be null.", e.getMessage());
        }
        
        try {
            factory.createArchiveInputStream("ar", null);
        } catch (IllegalArgumentException e) {
            assertEquals("InputStream must not be null.", e.getMessage());
        }
    }
    
    @Test
    public void testCreateArchiveOutputStream() throws Exception {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        OutputStream outputStream = new ByteArrayOutputStream();
        
        assertNotNull(factory.createArchiveOutputStream("ar", outputStream));
        assertNotNull(factory.createArchiveOutputStream("zip", outputStream));
        assertNotNull(factory.createArchiveOutputStream("tar", outputStream));
        assertNotNull(factory.createArchiveOutputStream("jar", outputStream));
        assertNotNull(factory.createArchiveOutputStream("cpio", outputStream));
        
        try {
            factory.createArchiveOutputStream("unknown", outputStream);
        } catch (ArchiveException e) {
            assertEquals("Archiver: unknown not found.", e.getMessage());
        }
        
        try {
            factory.createArchiveOutputStream(null, outputStream);
        } catch (IllegalArgumentException e) {
            assertEquals("Archivername must not be null.", e.getMessage());
        }
        
        try {
            factory.createArchiveOutputStream("ar", null);
        } catch (IllegalArgumentException e) {
            assertEquals("OutputStream must not be null.", e.getMessage());
        }
    }
    
    @Test
    public void testCreateArchiveInputStreamWithInputStream() throws Exception {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        InputStream inputStream = new ByteArrayInputStream(new byte[0]);
        
        assertNotNull(factory.createArchiveInputStream(inputStream));
        
        try {
            factory.createArchiveInputStream(null);
        } catch (IllegalArgumentException e) {
            assertEquals("Stream must not be null.", e.getMessage());
        }
        
        InputStream unsupportedInputStream = new InputStream() {
            @Override
            public int read() throws IOException {
                return 0;
            }
        };
        
        try {
            factory.createArchiveInputStream(unsupportedInputStream);
        } catch (IllegalArgumentException e) {
            assertEquals("Mark is not supported.", e.getMessage());
        }
    }
}
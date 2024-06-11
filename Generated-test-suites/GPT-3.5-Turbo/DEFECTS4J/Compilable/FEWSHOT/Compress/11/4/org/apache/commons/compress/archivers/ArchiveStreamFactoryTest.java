package org.apache.commons.compress.archivers;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

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
    
    private ArchiveStreamFactory factory = new ArchiveStreamFactory();

    @Test
    public void testCreateArchiveInputStreamWithAr() throws ArchiveException {
        InputStream input = new ByteArrayInputStream(new byte[0]);
        ArchiveInputStream output = factory.createArchiveInputStream(ArchiveStreamFactory.AR, input);
        assertEquals(ArArchiveInputStream.class, output.getClass());
    }
    
    @Test
    public void testCreateArchiveInputStreamWithZip() throws ArchiveException {
        InputStream input = new ByteArrayInputStream(new byte[0]);
        ArchiveInputStream output = factory.createArchiveInputStream(ArchiveStreamFactory.ZIP, input);
        assertEquals(ZipArchiveInputStream.class, output.getClass());
    }
    
    @Test
    public void testCreateArchiveInputStreamWithTar() throws ArchiveException {
        InputStream input = new ByteArrayInputStream(new byte[0]);
        ArchiveInputStream output = factory.createArchiveInputStream(ArchiveStreamFactory.TAR, input);
        assertEquals(TarArchiveInputStream.class, output.getClass());
    }
    
    @Test
    public void testCreateArchiveInputStreamWithJar() throws ArchiveException {
        InputStream input = new ByteArrayInputStream(new byte[0]);
        ArchiveInputStream output = factory.createArchiveInputStream(ArchiveStreamFactory.JAR, input);
        assertEquals(JarArchiveInputStream.class, output.getClass());
    }
    
    @Test
    public void testCreateArchiveInputStreamWithCpio() throws ArchiveException {
        InputStream input = new ByteArrayInputStream(new byte[0]);
        ArchiveInputStream output = factory.createArchiveInputStream(ArchiveStreamFactory.CPIO, input);
        assertEquals(CpioArchiveInputStream.class, output.getClass());
    }    
    
    @Test
    public void testCreateArchiveInputStreamWithDump() throws ArchiveException {
        InputStream input = new ByteArrayInputStream(new byte[0]);
        ArchiveInputStream output = factory.createArchiveInputStream(ArchiveStreamFactory.DUMP, input);
        assertEquals(DumpArchiveInputStream.class, output.getClass());
    }
    
    @Test
    public void testCreateArchiveInputStreamThrowsExceptionWithInvalidArchiverName() {
        InputStream input = new ByteArrayInputStream(new byte[0]);
        assertThrows(ArchiveException.class, () -> factory.createArchiveInputStream("invalid", input));
    }
    
    @Test
    public void testCreateArchiveInputStreamThrowsExceptionWithNullArchiverName() {
        InputStream input = new ByteArrayInputStream(new byte[0]);
        assertThrows(IllegalArgumentException.class, () -> factory.createArchiveInputStream(null, input));
    }
    
    @Test
    public void testCreateArchiveInputStreamThrowsExceptionWithNullInputStream() {
        assertThrows(IllegalArgumentException.class, () -> factory.createArchiveInputStream(ArchiveStreamFactory.AR, null));
    }
    
    @Test
    public void testCreateArchiveOutputStreamWithAr() throws ArchiveException {
        OutputStream output = new ByteArrayOutputStream();
        ArchiveOutputStream archiveOutputStream = factory.createArchiveOutputStream(ArchiveStreamFactory.AR, output);
        assertEquals(ArArchiveOutputStream.class, archiveOutputStream.getClass());
    }
    
    @Test
    public void testCreateArchiveOutputStreamWithZip() throws ArchiveException {
        OutputStream output = new ByteArrayOutputStream();
        ArchiveOutputStream archiveOutputStream = factory.createArchiveOutputStream(ArchiveStreamFactory.ZIP, output);
        assertEquals(ZipArchiveOutputStream.class, archiveOutputStream.getClass());
    }
    
    @Test
    public void testCreateArchiveOutputStreamWithTar() throws ArchiveException {
        OutputStream output = new ByteArrayOutputStream();
        ArchiveOutputStream archiveOutputStream = factory.createArchiveOutputStream(ArchiveStreamFactory.TAR, output);
        assertEquals(TarArchiveOutputStream.class, archiveOutputStream.getClass());
    }
    
    @Test
    public void testCreateArchiveOutputStreamWithJar() throws ArchiveException {
        OutputStream output = new ByteArrayOutputStream();
        ArchiveOutputStream archiveOutputStream = factory.createArchiveOutputStream(ArchiveStreamFactory.JAR, output);
        assertEquals(JarArchiveOutputStream.class, archiveOutputStream.getClass());
    }
    
    @Test
    public void testCreateArchiveOutputStreamWithCpio() throws ArchiveException {
        OutputStream output = new ByteArrayOutputStream();
        ArchiveOutputStream archiveOutputStream = factory.createArchiveOutputStream(ArchiveStreamFactory.CPIO, output);
        assertEquals(CpioArchiveOutputStream.class, archiveOutputStream.getClass());
    }
    
    @Test
    public void testCreateArchiveOutputStreamThrowsExceptionWithInvalidArchiverName() {
        OutputStream output = new ByteArrayOutputStream();
        assertThrows(ArchiveException.class, () -> factory.createArchiveOutputStream("invalid", output));
    }
    
    @Test
    public void testCreateArchiveOutputStreamThrowsExceptionWithNullArchiverName() {
        OutputStream output = new ByteArrayOutputStream();
        assertThrows(IllegalArgumentException.class, () -> factory.createArchiveOutputStream(null, output));
    }
    
    @Test
    public void testCreateArchiveOutputStreamThrowsExceptionWithNullOutputStream() {
        assertThrows(IllegalArgumentException.class, () -> factory.createArchiveOutputStream(ArchiveStreamFactory.AR, null));
    }
    
    @Test
    public void testCreateArchiveInputStreamWithNullInputStream() {
        assertThrows(IllegalArgumentException.class, () -> factory.createArchiveInputStream(null));
    }
    
    @Test
    public void testCreateArchiveInputStreamThrowsExceptionWhenInputStreamDoesNotSupportMark() {
        InputStream input = new InputStream() {
            public int read() throws IOException {
                return 0;
            }
        };
        assertThrows(IllegalArgumentException.class, () -> factory.createArchiveInputStream(input));
    }
    
    @Test
    public void testCreateArchiveInputStreamThrowsExceptionWhenNoArchiverFound() {
        // Signature does not match any archive format
        InputStream input = new ByteArrayInputStream(new byte[0]);
        assertThrows(ArchiveException.class, () -> factory.createArchiveInputStream(input));
    }
}
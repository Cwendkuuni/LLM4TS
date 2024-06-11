package org.apache.commons.compress.archivers;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;

import java.io.ByteArrayInputStream;
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
    public void testCreateArchiveInputStream_withArchiverNameAndInputStream() throws ArchiveException {
        ArchiveInputStream archiveInputStream = new ArchiveStreamFactory().createArchiveInputStream(ArchiveStreamFactory.AR, mock(InputStream.class));
        assertNotNull(archiveInputStream);
        
        archiveInputStream = new ArchiveStreamFactory().createArchiveInputStream(ArchiveStreamFactory.CPIO, mock(InputStream.class));
        assertNotNull(archiveInputStream);
        
        archiveInputStream = new ArchiveStreamFactory().createArchiveInputStream(ArchiveStreamFactory.DUMP, mock(InputStream.class));
        assertNotNull(archiveInputStream);
        
        archiveInputStream = new ArchiveStreamFactory().createArchiveInputStream(ArchiveStreamFactory.JAR, mock(InputStream.class));
        assertNotNull(archiveInputStream);
        
        archiveInputStream = new ArchiveStreamFactory().createArchiveInputStream(ArchiveStreamFactory.TAR, mock(InputStream.class));
        assertNotNull(archiveInputStream);
        
        archiveInputStream = new ArchiveStreamFactory().createArchiveInputStream(ArchiveStreamFactory.ZIP, mock(InputStream.class));
        assertNotNull(archiveInputStream);
        
        try {
            new ArchiveStreamFactory().createArchiveInputStream("invalid", mock(InputStream.class));
        } catch (ArchiveException e) {
            assertEquals("Archiver: invalid not found.", e.getMessage());
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateArchiveInputStream_withNullArchiverName() throws ArchiveException {
        new ArchiveStreamFactory().createArchiveInputStream(null, mock(InputStream.class));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateArchiveInputStream_withNullInputStream() throws ArchiveException {
        new ArchiveStreamFactory().createArchiveInputStream(ArchiveStreamFactory.AR, null);
    }

    @Test
    public void testCreateArchiveOutputStream_withArchiverNameAndOutputStream() throws ArchiveException {
        ArchiveOutputStream archiveOutputStream = new ArchiveStreamFactory().createArchiveOutputStream(ArchiveStreamFactory.AR, mock(OutputStream.class));
        assertNotNull(archiveOutputStream);
        
        archiveOutputStream = new ArchiveStreamFactory().createArchiveOutputStream(ArchiveStreamFactory.CPIO, mock(OutputStream.class));
        assertNotNull(archiveOutputStream);
        
        archiveOutputStream = new ArchiveStreamFactory().createArchiveOutputStream(ArchiveStreamFactory.JAR, mock(OutputStream.class));
        assertNotNull(archiveOutputStream);
        
        archiveOutputStream = new ArchiveStreamFactory().createArchiveOutputStream(ArchiveStreamFactory.TAR, mock(OutputStream.class));
        assertNotNull(archiveOutputStream);
        
        archiveOutputStream = new ArchiveStreamFactory().createArchiveOutputStream(ArchiveStreamFactory.ZIP, mock(OutputStream.class));
        assertNotNull(archiveOutputStream);
        
        try {
            new ArchiveStreamFactory().createArchiveOutputStream("invalid", mock(OutputStream.class));
        } catch (ArchiveException e) {
            assertEquals("Archiver: invalid not found.", e.getMessage());
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateArchiveOutputStream_withNullArchiverName() throws ArchiveException {
        new ArchiveStreamFactory().createArchiveOutputStream(null, mock(OutputStream.class));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateArchiveOutputStream_withNullOutputStream() throws ArchiveException {
        new ArchiveStreamFactory().createArchiveOutputStream(ArchiveStreamFactory.AR, null);
    }

    @Test
    public void testCreateArchiveInputStream_withInputStream() throws ArchiveException {
        InputStream inputStream = new ByteArrayInputStream(new byte[0]);
        ArchiveInputStream archiveInputStream = new ArchiveStreamFactory().createArchiveInputStream(inputStream);
        assertNotNull(archiveInputStream);
        
        try {
            inputStream = new ByteArrayInputStream(new byte[0]);
            new ArchiveStreamFactory().createArchiveInputStream(inputStream);
        } catch (ArchiveException e) {
            assertEquals("No Archiver found for the stream signature", e.getMessage());
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateArchiveInputStream_withNullInputStream() throws ArchiveException {
        new ArchiveStreamFactory().createArchiveInputStream(null);
    }
}
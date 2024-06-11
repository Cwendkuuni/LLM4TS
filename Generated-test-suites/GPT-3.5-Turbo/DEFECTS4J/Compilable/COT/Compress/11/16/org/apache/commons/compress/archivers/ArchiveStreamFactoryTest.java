package org.apache.commons.compress.archivers;
import static org.junit.Assert.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.apache.commons.compress.archivers.ArchiveException;
import org.apache.commons.compress.archivers.ArchiveInputStream;
import org.apache.commons.compress.archivers.ArchiveOutputStream;
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
    public void testCreateArchiveInputStream_AR() throws IOException, ArchiveException {
        InputStream input = new ByteArrayInputStream(new byte[] { 1, 2, 3 });
        ArchiveInputStream in = new ArchiveStreamFactory().createArchiveInputStream(ArchiveStreamFactory.AR, input);
        assertTrue(in instanceof ArArchiveInputStream);
    }

    @Test
    public void testCreateArchiveInputStream_ZIP() throws IOException, ArchiveException {
        InputStream input = new ByteArrayInputStream(new byte[] { 1, 2, 3 });
        ArchiveInputStream in = new ArchiveStreamFactory().createArchiveInputStream(ArchiveStreamFactory.ZIP, input);
        assertTrue(in instanceof ZipArchiveInputStream);
    }

    @Test
    public void testCreateArchiveInputStream_TAR() throws IOException, ArchiveException {
        InputStream input = new ByteArrayInputStream(new byte[] { 1, 2, 3 });
        ArchiveInputStream in = new ArchiveStreamFactory().createArchiveInputStream(ArchiveStreamFactory.TAR, input);
        assertTrue(in instanceof TarArchiveInputStream);
    }

    @Test
    public void testCreateArchiveInputStream_JAR() throws IOException, ArchiveException {
        InputStream input = new ByteArrayInputStream(new byte[] { 1, 2, 3 });
        ArchiveInputStream in = new ArchiveStreamFactory().createArchiveInputStream(ArchiveStreamFactory.JAR, input);
        assertTrue(in instanceof JarArchiveInputStream);
    }

    @Test
    public void testCreateArchiveInputStream_CPIO() throws IOException, ArchiveException {
        InputStream input = new ByteArrayInputStream(new byte[] { 1, 2, 3 });
        ArchiveInputStream in = new ArchiveStreamFactory().createArchiveInputStream(ArchiveStreamFactory.CPIO, input);
        assertTrue(in instanceof CpioArchiveInputStream);
    }

    @Test
    public void testCreateArchiveInputStream_DUMP() throws IOException, ArchiveException {
        InputStream input = new ByteArrayInputStream(new byte[] { 1, 2, 3 });
        ArchiveInputStream in = new ArchiveStreamFactory().createArchiveInputStream(ArchiveStreamFactory.DUMP, input);
        assertTrue(in instanceof DumpArchiveInputStream);
    }

    @Test(expected = ArchiveException.class)
    public void testCreateArchiveInputStream_UnknownArchiver() throws IOException, ArchiveException {
        InputStream input = new ByteArrayInputStream(new byte[] { 1, 2, 3 });
        new ArchiveStreamFactory().createArchiveInputStream("unknown", input);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateArchiveInputStream_NullArchiverName() throws IOException, ArchiveException {
        InputStream input = new ByteArrayInputStream(new byte[] { 1, 2, 3 });
        new ArchiveStreamFactory().createArchiveInputStream(null, input);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateArchiveInputStream_NullInputStream() throws IOException, ArchiveException {
        new ArchiveStreamFactory().createArchiveInputStream(ArchiveStreamFactory.AR, null);
    }

    @Test
    public void testCreateArchiveOutputStream_AR() throws IOException, ArchiveException {
        OutputStream output = new ByteArrayOutputStream();
        ArchiveOutputStream out = new ArchiveStreamFactory().createArchiveOutputStream(ArchiveStreamFactory.AR, output);
        assertTrue(out instanceof ArArchiveOutputStream);
    }

    @Test
    public void testCreateArchiveOutputStream_ZIP() throws IOException, ArchiveException {
        OutputStream output = new ByteArrayOutputStream();
        ArchiveOutputStream out = new ArchiveStreamFactory().createArchiveOutputStream(ArchiveStreamFactory.ZIP, output);
        assertTrue(out instanceof ZipArchiveOutputStream);
    }

    @Test
    public void testCreateArchiveOutputStream_TAR() throws IOException, ArchiveException {
        OutputStream output = new ByteArrayOutputStream();
        ArchiveOutputStream out = new ArchiveStreamFactory().createArchiveOutputStream(ArchiveStreamFactory.TAR, output);
        assertTrue(out instanceof TarArchiveOutputStream);
    }

    @Test
    public void testCreateArchiveOutputStream_JAR() throws IOException, ArchiveException {
        OutputStream output = new ByteArrayOutputStream();
        ArchiveOutputStream out = new ArchiveStreamFactory().createArchiveOutputStream(ArchiveStreamFactory.JAR, output);
        assertTrue(out instanceof JarArchiveOutputStream);
    }

    @Test
    public void testCreateArchiveOutputStream_CPIO() throws IOException, ArchiveException {
        OutputStream output = new ByteArrayOutputStream();
        ArchiveOutputStream out = new ArchiveStreamFactory().createArchiveOutputStream(ArchiveStreamFactory.CPIO, output);
        assertTrue(out instanceof CpioArchiveOutputStream);
    }

    @Test(expected = ArchiveException.class)
    public void testCreateArchiveOutputStream_UnknownArchiver() throws IOException, ArchiveException {
        OutputStream output = new ByteArrayOutputStream();
        new ArchiveStreamFactory().createArchiveOutputStream("unknown", output);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateArchiveOutputStream_NullArchiverName() throws IOException, ArchiveException {
        OutputStream output = new ByteArrayOutputStream();
        new ArchiveStreamFactory().createArchiveOutputStream(null, output);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateArchiveOutputStream_NullOutputStream() throws IOException, ArchiveException {
        new ArchiveStreamFactory().createArchiveOutputStream(ArchiveStreamFactory.AR, null);
    }

    @Test
    public void testCreateArchiveInputStream_Autodetect_ZIP() throws IOException, ArchiveException {
        InputStream input = new ByteArrayInputStream(new byte[] { 'P', 'K', 3, 4 });
        ArchiveInputStream in = new ArchiveStreamFactory().createArchiveInputStream(input);
        assertTrue(in instanceof ZipArchiveInputStream);
    }

    @Test
    public void testCreateArchiveInputStream_Autodetect_JAR() throws IOException, ArchiveException {
        InputStream input = new ByteArrayInputStream(new byte[] { 'P', 'K', 3, 5 });
        ArchiveInputStream in = new ArchiveStreamFactory().createArchiveInputStream(input);
        assertTrue(in instanceof JarArchiveInputStream);
    }

    @Test
    public void testCreateArchiveInputStream_Autodetect_AR() throws IOException, ArchiveException {
        InputStream input = new ByteArrayInputStream(new byte[] { '!', '<' });
        ArchiveInputStream in = new ArchiveStreamFactory().createArchiveInputStream(input);
        assertTrue(in instanceof ArArchiveInputStream);
    }

    @Test
    public void testCreateArchiveInputStream_Autodetect_CPIO() throws IOException, ArchiveException {
        InputStream input = new ByteArrayInputStream(new byte[] { '0', '7', '0', '7' });
        ArchiveInputStream in = new ArchiveStreamFactory().createArchiveInputStream(input);
        assertTrue(in instanceof CpioArchiveInputStream);
    }

    @Test
    public void testCreateArchiveInputStream_Autodetect_DUMP() throws IOException, ArchiveException {
        InputStream input = new ByteArrayInputStream(new byte[] { '.', 1, 2, 3 });
        ArchiveInputStream in = new ArchiveStreamFactory().createArchiveInputStream(input);
        assertTrue(in instanceof DumpArchiveInputStream);
    }

    @Test
    public void testCreateArchiveInputStream_Autodetect_TAR() throws IOException, ArchiveException {
        InputStream input = new ByteArrayInputStream(new byte[] { 1, 2, 3, 4, 5 });
        ArchiveInputStream in = new ArchiveStreamFactory().createArchiveInputStream(input);
        assertTrue(in instanceof TarArchiveInputStream);
    }

    @Test
    public void testCreateArchiveInputStream_Autodetect_NotSupported() throws IOException, ArchiveException {
        InputStream input = new ByteArrayInputStream(new byte[] { 1, 2, 3 });
        try {
            new ArchiveStreamFactory().createArchiveInputStream(input);
            fail("Expected ArchiveException");
        } catch (ArchiveException e) {
            // expected
        }
    }
}
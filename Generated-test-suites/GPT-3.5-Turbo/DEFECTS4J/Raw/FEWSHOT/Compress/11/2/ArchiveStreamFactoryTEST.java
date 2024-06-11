
package org.apache.commons.compress.archivers;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

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
    public void testCreateArchiveInputStream_ar() throws ArchiveException {
        ArchiveInputStream in = new ArchiveStreamFactory().createArchiveInputStream(ArchiveStreamFactory.AR, new ByteArrayInputStream(new byte[0]));
        assertNotNull(in);
        assertTrue(in instanceof ArArchiveInputStream);
    }

    @Test
    public void testCreateArchiveInputStream_cpio() throws ArchiveException {
        ArchiveInputStream in = new ArchiveStreamFactory().createArchiveInputStream(ArchiveStreamFactory.CPIO, new ByteArrayInputStream(new byte[0]));
        assertNotNull(in);
        assertTrue(in instanceof CpioArchiveInputStream);
    }

    @Test
    public void testCreateArchiveInputStream_dump() throws ArchiveException {
        ArchiveInputStream in = new ArchiveStreamFactory().createArchiveInputStream(ArchiveStreamFactory.DUMP, new ByteArrayInputStream(new byte[0]));
        assertNotNull(in);
        assertTrue(in instanceof DumpArchiveInputStream);
    }

    @Test
    public void testCreateArchiveInputStream_jar() throws ArchiveException {
        ArchiveInputStream in = new ArchiveStreamFactory().createArchiveInputStream(ArchiveStreamFactory.JAR, new ByteArrayInputStream(new byte[0]));
        assertNotNull(in);
        assertTrue(in instanceof JarArchiveInputStream);
    }

    @Test
    public void testCreateArchiveInputStream_tar() throws ArchiveException {
        ArchiveInputStream in = new ArchiveStreamFactory().createArchiveInputStream(ArchiveStreamFactory.TAR, new ByteArrayInputStream(new byte[0]));
        assertNotNull(in);
        assertTrue(in instanceof TarArchiveInputStream);
    }

    @Test
    public void testCreateArchiveInputStream_zip() throws ArchiveException {
        ArchiveInputStream in = new ArchiveStreamFactory().createArchiveInputStream(ArchiveStreamFactory.ZIP, new ByteArrayInputStream(new byte[0]));
        assertNotNull(in);
        assertTrue(in instanceof ZipArchiveInputStream);
    }

    @Test
    public void testCreateArchiveInputStream_invalid() {
        assertThrows(ArchiveException.class, () -> {
            new ArchiveStreamFactory().createArchiveInputStream("invalid", new ByteArrayInputStream(new byte[0]));
        });
    }

    @Test
    public void testCreateArchiveOutputStream_ar() throws ArchiveException {
        ArchiveOutputStream out = new ArchiveStreamFactory().createArchiveOutputStream(ArchiveStreamFactory.AR, new ByteArrayOutputStream());
        assertNotNull(out);
        assertTrue(out instanceof ArArchiveOutputStream);
    }

    @Test
    public void testCreateArchiveOutputStream_cpio() throws ArchiveException {
        ArchiveOutputStream out = new ArchiveStreamFactory().createArchiveOutputStream(ArchiveStreamFactory.CPIO, new ByteArrayOutputStream());
        assertNotNull(out);
        assertTrue(out instanceof CpioArchiveOutputStream);
    }

    @Test
    public void testCreateArchiveOutputStream_jar() throws ArchiveException {
        ArchiveOutputStream out = new ArchiveStreamFactory().createArchiveOutputStream(ArchiveStreamFactory.JAR, new ByteArrayOutputStream());
        assertNotNull(out);
        assertTrue(out instanceof JarArchiveOutputStream);
    }

    @Test
    public void testCreateArchiveOutputStream_tar() throws ArchiveException {
        ArchiveOutputStream out = new ArchiveStreamFactory().createArchiveOutputStream(ArchiveStreamFactory.TAR, new ByteArrayOutputStream());
        assertNotNull(out);
        assertTrue(out instanceof TarArchiveOutputStream);
    }

    @Test
    public void testCreateArchiveOutputStream_zip() throws ArchiveException {
        ArchiveOutputStream out = new ArchiveStreamFactory().createArchiveOutputStream(ArchiveStreamFactory.ZIP, new ByteArrayOutputStream());
        assertNotNull(out);
        assertTrue(out instanceof ZipArchiveOutputStream);
    }

    @Test
    public void testCreateArchiveOutputStream_invalid() {
        assertThrows(ArchiveException.class, () -> {
            new ArchiveStreamFactory().createArchiveOutputStream("invalid", new ByteArrayOutputStream());
        });
    }

    @Test
    public void testCreateArchiveInputStream_autodetect_zip() throws ArchiveException, IOException {
        byte[] data = "PK".getBytes();
        InputStream in = new ByteArrayInputStream(data);
        ArchiveInputStream ais = new ArchiveStreamFactory().createArchiveInputStream(in);
        assertNotNull(ais);
        assertTrue(ais instanceof ZipArchiveInputStream);
    }

    @Test
    public void testCreateArchiveInputStream_autodetect_jar() throws ArchiveException, IOException {
        byte[] data = "PK".getBytes();
        InputStream in = new ByteArrayInputStream(data);
        ArchiveInputStream ais = new ArchiveStreamFactory().createArchiveInputStream(in);
        assertNotNull(ais);
        assertTrue(ais instanceof JarArchiveInputStream);
    }

    @Test
    public void testCreateArchiveInputStream_autodetect_ar() throws ArchiveException, IOException {
        byte[] data = "!<arch>\n".getBytes();
        InputStream in = new ByteArrayInputStream(data);
        ArchiveInputStream ais = new ArchiveStreamFactory().createArchiveInputStream(in);
        assertNotNull(ais);
        assertTrue(ais instanceof ArArchiveInputStream);
    }

    @Test
    public void testCreateArchiveInputStream_autodetect_cpio() throws ArchiveException, IOException {
        byte[] data = "070701".getBytes();
        InputStream in = new ByteArrayInputStream(data);
        ArchiveInputStream ais = new ArchiveStreamFactory().createArchiveInputStream(in);
        assertNotNull(ais);
        assertTrue(ais instanceof CpioArchiveInputStream);
    }

    @Test
    public void testCreateArchiveInputStream_autodetect_dump() throws ArchiveException, IOException {
        byte[] data = "070701".getBytes();
        InputStream in = new ByteArrayInputStream(data);
        ArchiveInputStream ais = new ArchiveStreamFactory().createArchiveInputStream(in);
        assertNotNull(ais);
        assertTrue(ais instanceof DumpArchiveInputStream);
    }

    @Test
    public void testCreateArchiveInputStream_autodetect_tar() throws ArchiveException, IOException {
        byte[] data = new byte[] { 'u', 's', 't', 'a', 'r', 0x0, '0', '0', '0', '0', '0', 0x0, 0x0, 0x0, 0x0, 0x0 };
        InputStream in = new ByteArrayInputStream(data);
        ArchiveInputStream ais = new ArchiveStreamFactory().createArchiveInputStream(in);
        assertNotNull(ais);
        assertTrue(ais instanceof TarArchiveInputStream);
    }

    @Test
    public void testCreateArchiveInputStream_autodetect_invalid() throws IOException {
        byte[] data = new byte[0];
        InputStream in = new ByteArrayInputStream(data);
        assertThrows(ArchiveException.class, () -> {
            new ArchiveStreamFactory().createArchiveInputStream(in);
        });
    }

}

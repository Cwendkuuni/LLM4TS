package org.apache.commons.compress.archivers;

import static org.junit.Assert.assertInstanceOf;
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

public class ArchiveStreamFactoryTest {

    @Test
    public void testCreateArchiveInputStreamAR() throws ArchiveException, IOException {
        final InputStream in = new ByteArrayInputStream(new byte[0]);
        final ArchiveInputStream archiverInputStream = ArchiveStreamFactory.getInstance()
                .createArchiveInputStream(ArchiveStreamFactory.AR, in);
        assertInstanceOf(ArArchiveInputStream.class, archiverInputStream);
    }

    @Test
    public void testCreateArchiveInputStreamJAR() throws ArchiveException, IOException {
        final InputStream in = new ByteArrayInputStream(new byte[0]);
        final ArchiveInputStream archiverInputStream = ArchiveStreamFactory.getInstance()
                .createArchiveInputStream(ArchiveStreamFactory.JAR, in);
        assertInstanceOf(JarArchiveInputStream.class, archiverInputStream);
    }

    @Test
    public void testCreateArchiveInputStreamTAR() throws ArchiveException, IOException {
        final InputStream in = new ByteArrayInputStream(new byte[0]);
        final ArchiveInputStream archiverInputStream = ArchiveStreamFactory.getInstance()
                .createArchiveInputStream(ArchiveStreamFactory.TAR, in);
        assertInstanceOf(TarArchiveInputStream.class, archiverInputStream);
    }

    @Test
    public void testCreateArchiveInputStreamZIP() throws ArchiveException, IOException {
        final InputStream in = new ByteArrayInputStream(new byte[0]);
        final ArchiveInputStream archiverInputStream = ArchiveStreamFactory.getInstance()
                .createArchiveInputStream(ArchiveStreamFactory.ZIP, in);
        assertInstanceOf(ZipArchiveInputStream.class, archiverInputStream);
    }

    @Test
    public void testCreateArchiveInputStreamCPIO() throws ArchiveException, IOException {
        final InputStream in = new ByteArrayInputStream(new byte[0]);
        final ArchiveInputStream archiverInputStream = ArchiveStreamFactory.getInstance()
                .createArchiveInputStream(ArchiveStreamFactory.CPIO, in);
        assertInstanceOf(CpioArchiveInputStream.class, archiverInputStream);
    }

    @Test
    public void testCreateArchiveInputStreamDump() throws ArchiveException, IOException {
        final InputStream in = new ByteArrayInputStream(new byte[0]);
        final ArchiveInputStream archiverInputStream = ArchiveStreamFactory.getInstance()
                .createArchiveInputStream(ArchiveStreamFactory.DUMP, in);
        assertInstanceOf(DumpArchiveInputStream.class, archiverInputStream);
    }

    @Test(expected = ArchiveException.class)
    public void testCreateArchiveInputStreamInvalidFormat() throws ArchiveException, IOException {
        final InputStream in = new ByteArrayInputStream(new byte[0]);
        ArchiveStreamFactory.getInstance().createArchiveInputStream("invalid", in);
    }

    @Test
    public void testCreateArchiveOutputStreamAR() throws ArchiveException, IOException {
        final OutputStream out = new ByteArrayOutputStream();
        final ArchiveOutputStream archiverOutputStream = ArchiveStreamFactory.getInstance()
                .createArchiveOutputStream(ArchiveStreamFactory.AR, out);
        assertInstanceOf(ArArchiveOutputStream.class, archiverOutputStream);
    }

    @Test
    public void testCreateArchiveOutputStreamJAR() throws ArchiveException, IOException {
        final OutputStream out = new ByteArrayOutputStream();
        final ArchiveOutputStream archiverOutputStream = ArchiveStreamFactory.getInstance()
                .createArchiveOutputStream(ArchiveStreamFactory.JAR, out);
        assertInstanceOf(JarArchiveOutputStream.class, archiverOutputStream);
    }

    @Test
    public void testCreateArchiveOutputStreamTAR() throws ArchiveException, IOException {
        final OutputStream out = new ByteArrayOutputStream();
        final ArchiveOutputStream archiverOutputStream = ArchiveStreamFactory.getInstance()
                .createArchiveOutputStream(ArchiveStreamFactory.TAR, out);
        assertInstanceOf(TarArchiveOutputStream.class, archiverOutputStream);
    }

    @Test
    public void testCreateArchiveOutputStreamZIP() throws ArchiveException, IOException {
        final OutputStream out = new ByteArrayOutputStream();
        final ArchiveOutputStream archiverOutputStream = ArchiveStreamFactory.getInstance()
                .createArchiveOutputStream(ArchiveStreamFactory.ZIP, out);
        assertInstanceOf(ZipArchiveOutputStream.class, archiverOutputStream);
    }

    @Test
    public void testCreateArchiveOutputStreamCPIO() throws ArchiveException, IOException {
        final OutputStream out = new ByteArrayOutputStream();
        final ArchiveOutputStream archiverOutputStream = ArchiveStreamFactory.getInstance()
                .createArchiveOutputStream(ArchiveStreamFactory.CPIO, out);
        assertInstanceOf(CpioArchiveOutputStream.class, archiverOutputStream);
    }

    @Test
    public void testCreateArchiveOutputStreamDump() throws ArchiveException, IOException {
        final OutputStream out = new ByteArrayOutputStream();
        final ArchiveOutputStream archiverOutputStream = ArchiveStreamFactory.getInstance()
                .createArchiveOutputStream(ArchiveStreamFactory.DUMP, out);
        assertInstanceOf(DumpArchiveOutputStream.class, archiverOutputStream);
    }

    @Test(expected = ArchiveException.class)
    public void testCreateArchiveOutputStreamNullOutputStream() throws ArchiveException {
        final ArchiveOutputStream archiverOutputStream = ArchiveStreamFactory.getInstance()
                .createArchiveOutputStream(ArchiveStreamFactory.AR, null);
        assertNull(archiverOutputStream);
    }

    @Test(expected = ArchiveException.class)
    public void testCreateArchiveOutputStreamInvalidFormat() throws ArchiveException, IOException {
        final OutputStream out = new ByteArrayOutputStream();
        ArchiveStreamFactory.getInstance().createArchiveOutputStream("invalid", out);
    }
}
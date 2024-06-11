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
import org.apache.commons.compress.archivers.common.ArchiveInputStream;
import org.apache.commons.compress.archivers.common.ArchiveOutputStream;
import org.junit.Test;

public class ArchiveStreamFactoryTest {

    @Test
    public void testCreateArchiveInputStream_AR() {
        final String archiverName = ArchiveStreamFactory.AR;
        final InputStream inputStream = new ByteArrayInputStream(new byte[0]);
        ArchiveInputStream archiveInputStream = ArchiveStreamFactory.getInstance()
                .createArchiveInputStream(archiverName, inputStream);
        assertInstanceOf(ArArchiveInputStream.class, archiveInputStream);
    }

    @Test
    public void testCreateArchiveInputStream_CPIO() {
        final String archiverName = ArchiveStreamFactory.CPIO;
        final InputStream inputStream = new ByteArrayInputStream(new byte[0]);
        ArchiveInputStream archiveInputStream = ArchiveStreamFactory.getInstance()
                .createArchiveInputStream(archiverName, inputStream);
        assertInstanceOf(CpioArchiveInputStream.class, archiveInputStream);
    }

    @Test
    public void testCreateArchiveInputStream_DUMP() {
        final String archiverName = ArchiveStreamFactory.DUMP;
        final InputStream inputStream = new ByteArrayInputStream(new byte[0]);
        ArchiveInputStream archiveInputStream = ArchiveStreamFactory.getInstance()
                .createArchiveInputStream(archiverName, inputStream);
        assertInstanceOf(DumpArchiveInputStream.class, archiveInputStream);
    }

    @Test
    public void testCreateArchiveInputStream_JAR() {
        final String archiverName = ArchiveStreamFactory.JAR;
        final InputStream inputStream = new ByteArrayInputStream(new byte[0]);
        ArchiveInputStream archiveInputStream = ArchiveStreamFactory.getInstance()
                .createArchiveInputStream(archiverName, inputStream);
        assertInstanceOf(JarArchiveInputStream.class, archiveInputStream);
    }

    @Test
    public void testCreateArchiveInputStream_TAR() {
        final String archiverName = ArchiveStreamFactory.TAR;
        final InputStream inputStream = new ByteArrayInputStream(new byte[0]);
        ArchiveInputStream archiveInputStream = ArchiveStreamFactory.getInstance()
                .createArchiveInputStream(archiverName, inputStream);
        assertInstanceOf(TarArchiveInputStream.class, archiveInputStream);
    }

    @Test
    public void testCreateArchiveInputStream_ZIP() {
        final String archiverName = ArchiveStreamFactory.ZIP;
        final InputStream inputStream = new ByteArrayInputStream(new byte[0]);
        ArchiveInputStream archiveInputStream = ArchiveStreamFactory.getInstance()
                .createArchiveInputStream(archiverName, inputStream);
        assertInstanceOf(ZipArchiveInputStream.class, archiveInputStream);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateArchiveInputStream_NullArchiverName() {
        final String archiverName = null;
        final InputStream inputStream = new ByteArrayInputStream(new byte[0]);
        ArchiveStreamFactory.getInstance()
                .createArchiveInputStream(archiverName, inputStream);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateArchiveInputStream_NullInputStream() {
        final String archiverName = ArchiveStreamFactory.AR;
        final InputStream inputStream = null;
        ArchiveStreamFactory.getInstance()
                .createArchiveInputStream(archiverName, inputStream);
    }

    @Test
    public void testCreateArchiveInputStream_UnknownArchiverName() {
        final String archiverName = "unknown";
        final InputStream inputStream = new ByteArrayInputStream(new byte[0]);
        ArchiveStreamFactory archiveStreamFactory = ArchiveStreamFactory.getInstance();
        assertNull(archiveStreamFactory.createArchiveInputStream(archiverName, inputStream));
    }

    @Test
    public void testCreateArchiveOutputStream_AR() {
        final String archiverName = ArchiveStreamFactory.AR;
        final OutputStream outputStream = new ByteArrayOutputStream();
        ArchiveOutputStream archiveOutputStream = ArchiveStreamFactory.getInstance()
                .createArchiveOutputStream(archiverName, outputStream);
        assertInstanceOf(ArArchiveOutputStream.class, archiveOutputStream);
    }

    @Test
    public void testCreateArchiveOutputStream_CPIO() {
        final String archiverName = ArchiveStreamFactory.CPIO;
        final OutputStream outputStream = new ByteArrayOutputStream();
        ArchiveOutputStream archiveOutputStream = ArchiveStreamFactory.getInstance()
                .createArchiveOutputStream(archiverName, outputStream);
        assertInstanceOf(CpioArchiveOutputStream.class, archiveOutputStream);
    }

    @Test
    public void testCreateArchiveOutputStream_DUMP() {
        final String archiverName = ArchiveStreamFactory.DUMP;
        final OutputStream outputStream = new ByteArrayOutputStream();
        ArchiveOutputStream archiveOutputStream = ArchiveStreamFactory.getInstance()
                .createArchiveOutputStream(archiverName, outputStream);
        assertInstanceOf(DumpArchiveOutputStream.class, archiveOutputStream);
    }

    @Test
    public void testCreateArchiveOutputStream_JAR() {
        final String archiverName = ArchiveStreamFactory.JAR;
        final OutputStream outputStream = new ByteArrayOutputStream();
        ArchiveOutputStream archiveOutputStream = ArchiveStreamFactory.getInstance()
                .createArchiveOutputStream(archiverName, outputStream);
        assertInstanceOf(JarArchiveOutputStream.class, archiveOutputStream);
    }

    @Test
    public void testCreateArchiveOutputStream_TAR() {
        final String archiverName = ArchiveStreamFactory.TAR;
        final OutputStream outputStream = new ByteArrayOutputStream();
        ArchiveOutputStream archiveOutputStream = ArchiveStreamFactory.getInstance()
                .createArchiveOutputStream(archiverName, outputStream);
        assertInstanceOf(TarArchiveOutputStream.class, archiveOutputStream);
    }

    @Test
    public void testCreateArchiveOutputStream_ZIP() {
        final String archiverName = ArchiveStreamFactory.ZIP;
        final OutputStream outputStream = new ByteArrayOutputStream();
        ArchiveOutputStream archiveOutputStream = ArchiveStreamFactory.getInstance()
                .createArchiveOutputStream(archiverName, outputStream);
        assertInstanceOf(ZipArchiveOutputStream.class, archiveOutputStream);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateArchiveOutputStream_NullArchiverName() {
        final String archiverName = null;
        final OutputStream outputStream = new ByteArrayOutputStream();
        ArchiveStreamFactory.getInstance()
                .createArchiveOutputStream(archiverName, outputStream);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateArchiveOutputStream_NullOutputStream() {
        final String archiverName = ArchiveStreamFactory.AR;
        final OutputStream outputStream = null;
        ArchiveStreamFactory.getInstance()
                .createArchiveOutputStream(archiverName, outputStream);
    }

    @Test
    public void testCreateArchiveOutputStream_UnknownArchiverName() {
        final String archiverName = "unknown";
        final OutputStream outputStream = new ByteArrayOutputStream();
        ArchiveStreamFactory archiveStreamFactory = ArchiveStreamFactory.getInstance();
        assertNull(archiveStreamFactory.createArchiveOutputStream(archiverName, outputStream));
    }
}
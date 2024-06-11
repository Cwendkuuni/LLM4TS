package org.apache.commons.compress.archivers;

import static org.junit.Assert.assertInstanceOf;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.apache.commons.compress.archivers.ar.ArArchiveInputStream;
import org.apache.commons.compress.archivers.ar.ArArchiveOutputStream;
import org.apache.commons.compress.archivers.arj.ArjArchiveInputStream;
import org.apache.commons.compress.archivers.cpio.CpioArchiveInputStream;
import org.apache.commons.compress.archivers.cpio.CpioArchiveOutputStream;
import org.apache.commons.compress.archivers.dump.DumpArchiveInputStream;
import org.apache.commons.compress.archivers.jar.JarArchiveInputStream;
import org.apache.commons.compress.archivers.jar.JarArchiveOutputStream;
import org.apache.commons.compress.archivers.tar.TarArchiveInputStream;
import org.apache.commons.compress.archivers.tar.TarArchiveOutputStream;
import org.apache.commons.compress.archivers.zip.ZipArchiveInputStream;
import org.apache.commons.compress.archivers.zip.ZipArchiveOutputStream;
import org.apache.commons.compress.archivers.sevenz.SevenZFile;
import org.apache.commons.compress.utils.IOUtils;

import org.junit.Test;

public class ArchiveStreamFactoryTest {

    @Test
    public void testCreateArchiveInputStream_knownFormat() throws Exception {
        // AR
        InputStream in = new ByteArrayInputStream(new byte[0]);
        ArchiveInputStream out = new ArchiveStreamFactory().createArchiveInputStream(ArchiveStreamFactory.AR, in);
        assertInstanceOf(ArArchiveInputStream.class, out);
        IOUtils.closeQuietly(out);

        // JAR
        in = new ByteArrayInputStream(new byte[0]);
        out = new ArchiveStreamFactory().createArchiveInputStream(ArchiveStreamFactory.JAR, in);
        assertInstanceOf(JarArchiveInputStream.class, out);
        IOUtils.closeQuietly(out);

        // ZIP
        in = new ByteArrayInputStream(new byte[0]);
        out = new ArchiveStreamFactory().createArchiveInputStream(ArchiveStreamFactory.ZIP, in);
        assertInstanceOf(ZipArchiveInputStream.class, out);
        IOUtils.closeQuietly(out);

        // TAR
        in = new ByteArrayInputStream(new byte[0]);
        out = new ArchiveStreamFactory().createArchiveInputStream(ArchiveStreamFactory.TAR, in);
        assertInstanceOf(TarArchiveInputStream.class, out);
        IOUtils.closeQuietly(out);

        // CPIO
        in = new ByteArrayInputStream(new byte[0]);
        out = new ArchiveStreamFactory().createArchiveInputStream(ArchiveStreamFactory.CPIO, in);
        assertInstanceOf(CpioArchiveInputStream.class, out);
        IOUtils.closeQuietly(out);

        // ARJ
        in = new ByteArrayInputStream(new byte[0]);
        out = new ArchiveStreamFactory().createArchiveInputStream(ArchiveStreamFactory.ARJ, in);
        assertInstanceOf(ArjArchiveInputStream.class, out);
        IOUtils.closeQuietly(out);

        // DUMP
        in = new ByteArrayInputStream(new byte[0]);
        out = new ArchiveStreamFactory().createArchiveInputStream(ArchiveStreamFactory.DUMP, in);
        assertInstanceOf(DumpArchiveInputStream.class, out);
        IOUtils.closeQuietly(out);

        // SEVEN_Z
        in = new ByteArrayInputStream(new byte[0]);
        assertThrows(StreamingNotSupportedException.class, () -> new ArchiveStreamFactory().createArchiveInputStream(ArchiveStreamFactory.SEVEN_Z, in));
    }

    @Test
    public void testCreateArchiveInputStream_unknownFormat() throws Exception {
        InputStream in = new ByteArrayInputStream(new byte[0]);
        assertThrows(ArchiveException.class, () -> new ArchiveStreamFactory().createArchiveInputStream(null, in));
    }

    @Test
    public void testCreateArchiveInputStream_nullInput() {
        assertThrows(IllegalArgumentException.class, () -> new ArchiveStreamFactory().createArchiveInputStream(ArchiveStreamFactory.AR, null));
    }

    @Test
    public void testCreateArchiveOutputStream_knownFormat() throws Exception {
        // AR
        OutputStream out = new ByteArrayOutputStream();
        ArchiveOutputStream archOutputStream = new ArchiveStreamFactory().createArchiveOutputStream(ArchiveStreamFactory.AR, out);
        assertInstanceOf(ArArchiveOutputStream.class, archOutputStream);
        archOutputStream.finish();
        IOUtils.closeQuietly(archOutputStream);

        // JAR
        out = new ByteArrayOutputStream();
        archOutputStream = new ArchiveStreamFactory().createArchiveOutputStream(ArchiveStreamFactory.JAR, out);
        assertInstanceOf(JarArchiveOutputStream.class, archOutputStream);
        archOutputStream.finish();
        IOUtils.closeQuietly(archOutputStream);

        // ZIP
        out = new ByteArrayOutputStream();
        archOutputStream = new ArchiveStreamFactory().createArchiveOutputStream(ArchiveStreamFactory.ZIP, out);
        assertInstanceOf(ZipArchiveOutputStream.class, archOutputStream);
        archOutputStream.finish();
        IOUtils.closeQuietly(archOutputStream);

        // TAR
        out = new ByteArrayOutputStream();
        archOutputStream = new ArchiveStreamFactory().createArchiveOutputStream(ArchiveStreamFactory.TAR, out);
        assertInstanceOf(TarArchiveOutputStream.class, archOutputStream);
        archOutputStream.finish();
        IOUtils.closeQuietly(archOutputStream);

        // CPIO
        out = new ByteArrayOutputStream();
        archOutputStream = new ArchiveStreamFactory().createArchiveOutputStream(ArchiveStreamFactory.CPIO, out);
        assertInstanceOf(CpioArchiveOutputStream.class, archOutputStream);
        archOutputStream.finish();
        IOUtils.closeQuietly(archOutputStream);

        // ARJ
        out = new ByteArrayOutputStream();
        archOutputStream = new ArchiveStreamFactory().createArchiveOutputStream(ArchiveStreamFactory.ARJ, out);
        assertInstanceOf(ArjArchiveOutputStream.class, archOutputStream);
        archOutputStream.finish();
        IOUtils.closeQuietly(archOutputStream);

        // DUMP
        out = new ByteArrayOutputStream();
        archOutputStream = new ArchiveStreamFactory().createArchiveOutputStream(ArchiveStreamFactory.DUMP, out);
        assertInstanceOf(CpioArchiveOutputStream.class, archOutputStream); // DUMP uses CPIO internally
        archOutputStream.finish();
        IOUtils.closeQuietly(archOutputStream);

        // SEVEN_Z
        out = new ByteArrayOutputStream();
        assertThrows(StreamingNotSupportedException.class, () -> new ArchiveStreamFactory().createArchiveOutputStream(ArchiveStreamFactory.SEVEN_Z, out));
    }

    @Test
    public void testCreateArchiveOutputStream_nullOutput() {
        assertThrows(IllegalArgumentException.class, () -> new ArchiveStreamFactory().createArchiveOutputStream(ArchiveStreamFactory.AR, null));
    }

    @Test
    public void testCreateArchiveOutputStream_nullArchiverName() {
        OutputStream out = new ByteArrayOutputStream();
        assertNull(new ArchiveStreamFactory().createArchiveOutputStream(null, out));
    }

}
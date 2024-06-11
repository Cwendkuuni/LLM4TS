package org.apache.commons.compress.archivers;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
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
    public void testCreateArchiveInputStream() throws IOException, ArchiveException {
        // Test AR
        InputStream arInputStream = new ByteArrayInputStream(new byte[0]);
        ArchiveInputStream arIn = new ArchiveStreamFactory().createArchiveInputStream(ArchiveStreamFactory.AR,
                arInputStream);
        assertNotNull(arIn);
        assertTrue(arIn instanceof ArArchiveInputStream);

        // Test ZIP
        InputStream zipInputStream = new ByteArrayInputStream(new byte[0]);
        ArchiveInputStream zipIn = new ArchiveStreamFactory().createArchiveInputStream(ArchiveStreamFactory.ZIP,
                zipInputStream);
        assertNotNull(zipIn);
        assertTrue(zipIn instanceof ZipArchiveInputStream);

        // Test TAR
        InputStream tarInputStream = new ByteArrayInputStream(new byte[0]);
        ArchiveInputStream tarIn = new ArchiveStreamFactory().createArchiveInputStream(ArchiveStreamFactory.TAR,
                tarInputStream);
        assertNotNull(tarIn);
        assertTrue(tarIn instanceof TarArchiveInputStream);

        // Test JAR
        InputStream jarInputStream = new ByteArrayInputStream(new byte[0]);
        ArchiveInputStream jarIn = new ArchiveStreamFactory().createArchiveInputStream(ArchiveStreamFactory.JAR,
                jarInputStream);
        assertNotNull(jarIn);
        assertTrue(jarIn instanceof JarArchiveInputStream);

        // Test CPIO
        InputStream cpioInputStream = new ByteArrayInputStream(new byte[0]);
        ArchiveInputStream cpioIn = new ArchiveStreamFactory().createArchiveInputStream(ArchiveStreamFactory.CPIO,
                cpioInputStream);
        assertNotNull(cpioIn);
        assertTrue(cpioIn instanceof CpioArchiveInputStream);

        // Test DUMP
        InputStream dumpInputStream = new ByteArrayInputStream(new byte[0]);
        ArchiveInputStream dumpIn = new ArchiveStreamFactory().createArchiveInputStream(ArchiveStreamFactory.DUMP,
                dumpInputStream);
        assertNotNull(dumpIn);
        assertTrue(dumpIn instanceof DumpArchiveInputStream);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateArchiveInputStreamWithNullArchiverName() throws IOException, ArchiveException {
        InputStream inputStream = new ByteArrayInputStream(new byte[0]);
        new ArchiveStreamFactory().createArchiveInputStream(null, inputStream);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateArchiveInputStreamWithNullInputStream() throws IOException, ArchiveException {
        new ArchiveStreamFactory().createArchiveInputStream(ArchiveStreamFactory.AR, null);
    }

    @Test
    public void testCreateArchiveOutputStream() throws IOException, ArchiveException {
        // Test AR
        OutputStream arOutputStream = new ByteArrayOutputStream();
        ArchiveOutputStream arOut = new ArchiveStreamFactory().createArchiveOutputStream(ArchiveStreamFactory.AR,
                arOutputStream);
        assertNotNull(arOut);
        assertTrue(arOut instanceof ArArchiveOutputStream);

        // Test ZIP
        OutputStream zipOutputStream = new ByteArrayOutputStream();
        ArchiveOutputStream zipOut = new ArchiveStreamFactory().createArchiveOutputStream(ArchiveStreamFactory.ZIP,
                zipOutputStream);
        assertNotNull(zipOut);
        assertTrue(zipOut instanceof ZipArchiveOutputStream);

        // Test TAR
        OutputStream tarOutputStream = new ByteArrayOutputStream();
        ArchiveOutputStream tarOut = new ArchiveStreamFactory().createArchiveOutputStream(ArchiveStreamFactory.TAR,
                tarOutputStream);
        assertNotNull(tarOut);
        assertTrue(tarOut instanceof TarArchiveOutputStream);

        // Test JAR
        OutputStream jarOutputStream = new ByteArrayOutputStream();
        ArchiveOutputStream jarOut = new ArchiveStreamFactory().createArchiveOutputStream(ArchiveStreamFactory.JAR,
                jarOutputStream);
        assertNotNull(jarOut);
        assertTrue(jarOut instanceof JarArchiveOutputStream);

        // Test CPIO
        OutputStream cpioOutputStream = new ByteArrayOutputStream();
        ArchiveOutputStream cpioOut = new ArchiveStreamFactory().createArchiveOutputStream(ArchiveStreamFactory.CPIO,
                cpioOutputStream);
        assertNotNull(cpioOut);
        assertTrue(cpioOut instanceof CpioArchiveOutputStream);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateArchiveOutputStreamWithNullArchiverName() throws IOException, ArchiveException {
        OutputStream outputStream = new ByteArrayOutputStream();
        new ArchiveStreamFactory().createArchiveOutputStream(null, outputStream);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateArchiveOutputStreamWithNullOutputStream() throws IOException, ArchiveException {
        new ArchiveStreamFactory().createArchiveOutputStream(ArchiveStreamFactory.AR, null);
    }

    @Test
    public void testCreateArchiveInputStreamWithInputStream() throws IOException, ArchiveException {
        InputStream inputStream = new ByteArrayInputStream(new byte[0]);
        ArchiveInputStream in = new ArchiveStreamFactory().createArchiveInputStream(inputStream);
        assertNotNull(in);
        assertTrue(in instanceof ZipArchiveInputStream);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateArchiveInputStreamWithNullInputStream() throws IOException, ArchiveException {
        new ArchiveStreamFactory().createArchiveInputStream((InputStream) null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateArchiveInputStreamWithUnsupportedMark() throws IOException, ArchiveException {
        InputStream inputStream = new InputStream() {
            public int read() throws IOException {
                return 0;
            }
        };
        new ArchiveStreamFactory().createArchiveInputStream(inputStream);
    }
}
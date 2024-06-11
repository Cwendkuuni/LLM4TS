package org.apache.commons.compress.archivers;

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
import static org.junit.Assert.*;
import org.junit.Test;

import java.io.*;
import org.apache.commons.compress.archivers.*;

public class ArchiveStreamFactoryTest {

    @Test
    public void testCreateArchiveInputStreamAr() throws ArchiveException {
        InputStream in = new ByteArrayInputStream(new byte[0]);
        ArchiveInputStream ais = new ArchiveStreamFactory().createArchiveInputStream(ArchiveStreamFactory.AR, in);
        assertTrue(ais instanceof ArArchiveInputStream);
    }

    @Test
    public void testCreateArchiveInputStreamZip() throws ArchiveException {
        InputStream in = new ByteArrayInputStream(new byte[0]);
        ArchiveInputStream ais = new ArchiveStreamFactory().createArchiveInputStream(ArchiveStreamFactory.ZIP, in);
        assertTrue(ais instanceof ZipArchiveInputStream);
    }

    @Test
    public void testCreateArchiveInputStreamTar() throws ArchiveException {
        InputStream in = new ByteArrayInputStream(new byte[0]);
        ArchiveInputStream ais = new ArchiveStreamFactory().createArchiveInputStream(ArchiveStreamFactory.TAR, in);
        assertTrue(ais instanceof TarArchiveInputStream);
    }

    @Test
    public void testCreateArchiveInputStreamJar() throws ArchiveException {
        InputStream in = new ByteArrayInputStream(new byte[0]);
        ArchiveInputStream ais = new ArchiveStreamFactory().createArchiveInputStream(ArchiveStreamFactory.JAR, in);
        assertTrue(ais instanceof JarArchiveInputStream);
    }

    @Test
    public void testCreateArchiveInputStreamCpio() throws ArchiveException {
        InputStream in = new ByteArrayInputStream(new byte[0]);
        ArchiveInputStream ais = new ArchiveStreamFactory().createArchiveInputStream(ArchiveStreamFactory.CPIO, in);
        assertTrue(ais instanceof CpioArchiveInputStream);
    }

    @Test
    public void testCreateArchiveInputStreamDump() throws ArchiveException {
        InputStream in = new ByteArrayInputStream(new byte[0]);
        ArchiveInputStream ais = new ArchiveStreamFactory().createArchiveInputStream(ArchiveStreamFactory.DUMP, in);
        assertTrue(ais instanceof DumpArchiveInputStream);
    }

    @Test(expected = ArchiveException.class)
    public void testCreateArchiveInputStreamInvalid() throws ArchiveException {
        InputStream in = new ByteArrayInputStream(new byte[0]);
        ArchiveInputStream ais = new ArchiveStreamFactory().createArchiveInputStream("invalid", in);
    }

    @Test
    public void testCreateArchiveOutputStreamAr() throws ArchiveException {
        OutputStream out = new ByteArrayOutputStream();
        ArchiveOutputStream aos = new ArchiveStreamFactory().createArchiveOutputStream(ArchiveStreamFactory.AR, out);
        assertTrue(aos instanceof ArArchiveOutputStream);
    }

    @Test
    public void testCreateArchiveOutputStreamZip() throws ArchiveException {
        OutputStream out = new ByteArrayOutputStream();
        ArchiveOutputStream aos = new ArchiveStreamFactory().createArchiveOutputStream(ArchiveStreamFactory.ZIP, out);
        assertTrue(aos instanceof ZipArchiveOutputStream);
    }

    @Test
    public void testCreateArchiveOutputStreamTar() throws ArchiveException {
        OutputStream out = new ByteArrayOutputStream();
        ArchiveOutputStream aos = new ArchiveStreamFactory().createArchiveOutputStream(ArchiveStreamFactory.TAR, out);
        assertTrue(aos instanceof TarArchiveOutputStream);
    }

    @Test
    public void testCreateArchiveOutputStreamJar() throws ArchiveException {
        OutputStream out = new ByteArrayOutputStream();
        ArchiveOutputStream aos = new ArchiveStreamFactory().createArchiveOutputStream(ArchiveStreamFactory.JAR, out);
        assertTrue(aos instanceof JarArchiveOutputStream);
    }

    @Test
    public void testCreateArchiveOutputStreamCpio() throws ArchiveException {
        OutputStream out = new ByteArrayOutputStream();
        ArchiveOutputStream aos = new ArchiveStreamFactory().createArchiveOutputStream(ArchiveStreamFactory.CPIO, out);
        assertTrue(aos instanceof CpioArchiveOutputStream);
    }

    @Test(expected = ArchiveException.class)
    public void testCreateArchiveOutputStreamInvalid() throws ArchiveException {
        OutputStream out = new ByteArrayOutputStream();
        ArchiveOutputStream aos = new ArchiveStreamFactory().createArchiveOutputStream("invalid", out);
    }

    @Test
    public void testCreateArchiveInputStreamAutoDetectAr() throws ArchiveException {
        InputStream in = new ByteArrayInputStream(new byte[0]);
        ArchiveInputStream ais = new ArchiveStreamFactory().createArchiveInputStream(in);
        assertTrue(ais instanceof ArArchiveInputStream);
    }

    @Test
    public void testCreateArchiveInputStreamAutoDetectZip() throws ArchiveException {
        InputStream in = new ByteArrayInputStream(new byte[0]);
        ArchiveInputStream ais = new ArchiveStreamFactory().createArchiveInputStream(in);
        assertTrue(ais instanceof ZipArchiveInputStream);
    }

    @Test
    public void testCreateArchiveInputStreamAutoDetectTar() throws ArchiveException {
        InputStream in = new ByteArrayInputStream(new byte[0]);
        ArchiveInputStream ais = new ArchiveStreamFactory().createArchiveInputStream(in);
        assertTrue(ais instanceof TarArchiveInputStream);
    }

    @Test
    public void testCreateArchiveInputStreamAutoDetectJar() throws ArchiveException {
        InputStream in = new ByteArrayInputStream(new byte[0]);
        ArchiveInputStream ais = new ArchiveStreamFactory().createArchiveInputStream(in);
        assertTrue(ais instanceof JarArchiveInputStream);
    }

    @Test
    public void testCreateArchiveInputStreamAutoDetectCpio() throws ArchiveException {
        InputStream in = new ByteArrayInputStream(new byte[0]);
        ArchiveInputStream ais = new ArchiveStreamFactory().createArchiveInputStream(in);
        assertTrue(ais instanceof CpioArchiveInputStream);
    }

    @Test
    public void testCreateArchiveInputStreamAutoDetectDump() throws ArchiveException {
        InputStream in = new ByteArrayInputStream(new byte[0]);
        ArchiveInputStream ais = new ArchiveStreamFactory().createArchiveInputStream(in);
        assertTrue(ais instanceof DumpArchiveInputStream);
    }

    @Test(expected = ArchiveException.class)
    public void testCreateArchiveInputStreamAutoDetectInvalid() throws ArchiveException {
        InputStream in = new ByteArrayInputStream(new byte[0]);
        ArchiveInputStream ais = new ArchiveStreamFactory().createArchiveInputStream(in);
    }

}